package shinhands.com.rest.platform.hub.asset;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.core.MediaType;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.camel.FluentProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jsonvalidator.JsonValidationException;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import lombok.extern.slf4j.Slf4j;
import shinhands.com.aggregation.BesuContractAggregationStrategy;
import shinhands.com.aggregation.ServiceFinalizeAggregationStrategy;
import shinhands.com.aggregation.ServiceIviteAggregationStrategy;
import shinhands.com.aggregation.ServiceProposalAggregationStrategy;
import shinhands.com.aggregation.ServiceRequestAggregationStrategy;
import shinhands.com.beans.ErcUtil;
import shinhands.com.beans.Ledger;
import shinhands.com.beans.RepositoryHelper;
import shinhands.com.beans.ServiceUtil;
import shinhands.com.beans.WalletUtil;
import shinhands.com.enumeration.Route;
import shinhands.com.exception.DefaultException;
import shinhands.com.exception.ForbiddenError;
import shinhands.com.exception.UnauthorizedError;
import shinhands.com.model.besu.EncodDataDistributePayload;
import shinhands.com.model.besu.EncodDataPayload;
import shinhands.com.model.besu.TransferERC20Payload;
import shinhands.com.model.daml.DAMLExeciseResponse;
import shinhands.com.model.daml.DAMLQueryResponse;
import shinhands.com.model.daml.Exercise;
import shinhands.com.model.daml.exercise.Transfer;
import shinhands.com.model.daml.template.DefaultDAMLResult;
import shinhands.com.model.daml.template.Query;
import shinhands.com.model.hub.misc.asset.ServiceJoin;
import shinhands.com.model.repository.Contract;
import shinhands.com.model.request.DistributionPayload;
import shinhands.com.model.request.MintBurnPayload;
import shinhands.com.model.request.ServiceProposalPayload;
import shinhands.com.model.request.TranferPayload;
import shinhands.com.model.response.DamlResult;
import shinhands.com.processor.JwtVerfyProcessor;
import shinhands.com.processor.assset.DistributionProcessor;
import shinhands.com.processor.assset.ERCBurnProcessor;
import shinhands.com.processor.assset.JoinServiceProcessor;
import shinhands.com.processor.proposal.AddTokenInfoForContractTableProcessor;
import shinhands.com.processor.proposal.ServiceAddTokenInfoProcessor;
import shinhands.com.processor.proposal.ServiceRequestProcessor;
import shinhands.com.processor.proposal.SetupBlockChainAddrForContractTableProcessor;

@Slf4j
@ApplicationScoped
public class Asset extends RouteBuilder {

    @Inject
    JwtVerfyProcessor jwtVerfyProcessor;

    @Inject
    Ledger ledger;

    @ConfigProperty(name = "daml.auth.operator")
    String jwt_operator;

    @Override
    public void configure() {
        onException(Exception.class).handled(true).maximumRedeliveries(0).to(Route.Response.COMMON_RESPONSE_500.label);
        onException(DefaultException.class).handled(true).maximumRedeliveries(0).to(Route.Response.BAD_RESPONSE_200.label);
        onException(ForbiddenError.class).handled(true).maximumRedeliveries(0).to(Route.Response.BAD_RESPONSE_200.label);
        onException(JsonValidationException.class).handled(true).maximumRedeliveries(0).to(Route.Response.BAD_JSONVALIDATOR_200.label);
        onException(UnauthorizedError.class).handled(true).maximumRedeliveries(0).to(Route.Response.BAD_RESPONSE_200.label);

        rest("/member/asset")
            .consumes(MediaType.APPLICATION_JSON)
            .produces(MediaType.APPLICATION_JSON)
            .get("/").description("DAP-SIF01? ( DAML 발행된 토큰 목록 조회 ERC20만 )")
                .to("direct:asset-list")
            .get("/detail").description("DAP-SIF01? ( DAML 발행된 토큰 상세 조회 )")
                .to("direct:asset-detail")
            .get("/balances").description("DAP-SIF01? ( DAML 토큰별 잔액조회 )")
                .to("direct:asset-balances")
            .post("/wallet/create").description("DAP-SIF01? ( DAML 지갑 생성 )")
                .to("direct:asset-wallet-create")
            .post("/service/proposal").description("DAP-SIF01? ( DAML 서비스 생성 요청 )")
                .to("direct:asset-service-proposal")
            .post("/service/join").description("DAP-SIF01? ( DAML 서비스 연결 )")
                .to("direct:asset-service-join")
            .post("/transfer").description("DAP-SIF01? ( DAML 토큰 전송 )")
                .to("direct:asset-transfer")
            .post("/mint").description("DAP-SIF01? ( DAML 토큰 추가발행 )")
                .to("direct:asset-mint")
            .post("/burn").description("DAP-SIF01? ( DAML 토큰 소각 )")
                .to("direct:asset-burn")
            .post("/distribution").description("distribution")
                .to("direct:asset-distribution")
            .post("/deposit").description("deposit")
                .to("direct:asset-deposit")
            .post("/swap").description("swap")
                .to("direct:asset-swap");


          from("direct:asset-detail").routeId("asset:detail")
            .process(jwtVerfyProcessor)
            // .marshal().json(JsonLibrary.Jackson)
            //     .to("json-validator:json-schema/asset/asset-service-detail-schema.json")
            // .unmarshal().json(JsonLibrary.Jackson, Map.class)
            .process(ex -> {
                String uuid = ex.getIn().getHeader("uuid").toString();
                DAMLQueryResponse services = ledger.getActiveContractsByQuery(ex,
                                                Query.builder()
                                                    .templateIds(List.of("Service:Service"))
                                                    .query(Map.of("uuid",uuid))
                                                .build());

                if (services.getStatus() != 200 || services.getResult().size() == 0) {
                    throw new ForbiddenError("서비스가 존재하지 않습니다.");
                }
                DefaultDAMLResult service = services.getResult().get(0);
                ex.getIn().setBody(service);
            })
            .to("direct:response-200");

        /**
         * TODO
         * [2021-08-31 10:13:56]
         *  - daml json rpc에서 페이징 지원하지 않음, 필요시 메모리에서 페이징 필요
         *  - sort옵션 없음
         *  - 이미지 객체는 base64로 묶기?
         */
        from("direct:asset-list").routeId("asset:list")
            .process(jwtVerfyProcessor)
            .process(ex-> {
                DAMLQueryResponse services = ledger.getActiveContractsByQuery(ex, Query.builder()
                                                    .templateIds(List.of("Service:Service")).build());
                if (services.getStatus() != 200 || services.getResult().size() == 0) {
                    throw new ForbiddenError("서비스가 존재하지 않습니다.");
                }
                List<Object> list = new ArrayList<>();
                for (DefaultDAMLResult result : services.getResult()) {
                    list.add(result.getPayload());
                };
                ex.getIn().setBody(list);
            })
            .to("direct:response-200");


        from("direct:asset-wallet-create").routeId("asset:wallet-create")
            .process(jwtVerfyProcessor)
            .marshal().json(JsonLibrary.Jackson)
            //     .to("json-validator:json-schema/asset/asset-add-wallet-schema.json")
            .unmarshal().json(JsonLibrary.Jackson, Map.class)
            .process(ex -> {
                String address = ex.getIn().getBody(Map.class).get("address").toString();
                String userid = (String) ex.getIn().getHeader("userid");
                ex.getIn().setHeader("dsl", new HashMap<String, String>() {{
                    put("account_addr", address);
                    put("userid",userid);
                   }});
            })
            .to("direct:sql-get-user")
            .choice()
                .when(header("sql_get_user").isNull())
                    .throwException(new ForbiddenError("사용자 정보가 존재하지 않습니다."))
                .otherwise()
                    .to("direct:sql-get-wallet")
                    .choice()
                        .when(header("sql_get_wallet").isNotNull())
                            .throwException(new ForbiddenError("이미 등록된 지갑입니다."))
                .end()
            .end()
            .to("direct:sql-get-wallet-count")
            .process(ex -> {
                Map<String,Object> dsl = ex.getIn().getHeader("dsl", Map.class);
                Map<String,String> user = (Map<String,String>)ex.getIn().getHeader("sql_get_user");
                Boolean isFirstWallet = ((Long) ex.getIn().getHeader("sql_get_wallet_count")) == 1;
                //
                // TODO
                // [2021-10-01 14:15:11] 사용자 지갑이 1개이상인경우 account_addr_rep = false 로 설정한다. 대표지갑 설정때문인듯?
                // account_addr_rep
                DamlResult result = WalletUtil.create(ex, dsl.get("userid").toString(), dsl.get("account_addr").toString());
                if (result.getException() != null) {
                    throw result.getException();
                } else {
                    dsl.put("account_addr_rep", isFirstWallet);
                }
                // ex.getIn().removeHeader("dsl");
                // ex.getIn().removeHeaders("sql_*");
                ex.getIn().setHeader("dsl", dsl);
            })
            .to("sql:classpath:sql/vue/asset/walletSave.sql")
            .to("direct:response-200");

        from("direct:save-contract").routeId("asset:save-contract")
            .log("# [${routeId}] => [headers] ${headers} , [body] ${body}")
            .to("sql:classpath:sql/vue/asset/contractSave2.sql?outputHeader=result")
            .log("2# [${routeId}] => [headers] ${headers} , [body] ${body}")
            .setBody(simple("${headers.result}"));
        from("direct:update-contract").routeId("asset:update-contract")
            .log("# [${routeId}] => [headers] ${headers} , [body] ${body}")
            .to("sql:classpath:sql/vue/asset/contractUpdate2.sql?outputHeader=result")
            .log("2# [${routeId}] => [headers] ${headers} , [body] ${body}")
            .setBody(simple("${headers.result}"));
        from("direct:get-contract").routeId("asset:get-contract")
            .log("# [${routeId}] => [headers] ${headers} , [body] ${body}")
            .to("sql:classpath:sql/vue/asset/contractByContractId2.sql?outputHeader=result&outputType=SelectOne")
            .log("2# [${routeId}] => [headers] ${headers} , [body] ${body}")
            .setBody(simple("${headers.result}"));

        /**
         * const serviceResult = await DAPService.serviceRequest(requestArg)
         * const proposalResult = await DAPService.inviteService(proposalArg)
         */
        from("direct:asset-service-proposal").routeId("asset:service-proposal")
            .process(jwtVerfyProcessor)
            .marshal().json(JsonLibrary.Jackson)
                .to("json-validator:json-schema/asset/service-propoal-schema.json")
            .unmarshal().json(JsonLibrary.Jackson, ServiceProposalPayload.class)
            .process(new ServiceRequestProcessor())
            .enrich("direct:restful-daml-create-contract", new ServiceProposalAggregationStrategy())
                .to("sql:classpath:sql/vue/asset/contractSave.sql?outputHeader=save_ServiceProposalAggregationStrategy")
            .enrich("direct:restful-daml-exercise-contract", new ServiceIviteAggregationStrategy())
                .to("sql:classpath:sql/vue/asset/contractSave.sql?outputHeader=save_ServiceIviteAggregationStrategy")
            .enrich("direct:restful-daml-exercise-contract", new ServiceFinalizeAggregationStrategy())
                .to("sql:classpath:sql/vue/asset/contractSave.sql?outputHeader=save_ServiceFinalizeAggregationStrategy")
                .to("sql:classpath:sql/vue/asset/contractUpdate.sql?outputHeader=update_ServiceFinalizeAggregationStrategy")
            .enrich("direct:restful-daml-create-contract", new ServiceRequestAggregationStrategy())
                .to("sql:classpath:sql/vue/asset/contractSave.sql?outputHeader=save_ServiceRequestAggregationStrategy")
            .enrich("direct:besu-create-contract", new BesuContractAggregationStrategy())
                .to("sql:classpath:sql/vue/asset/contractUpdate.sql?outputHeader=update_BesuContractAggregationStrategy")
                .to("sql:classpath:sql/vue/asset/contractByContractId.sql?outputHeader=fetch_contract&outputType=SelectOne")
            .process(new SetupBlockChainAddrForContractTableProcessor())
                .to("sql:classpath:sql/vue/asset/contractSave.sql?outputHeader=contractSave")
            .process(new ServiceAddTokenInfoProcessor())
                .to("sql:classpath:sql/vue/asset/contractUpdate.sql?outputHeader=update_result")
                .to("sql:classpath:sql/vue/asset/contractByContractId.sql?outputHeader=fetch_contract&outputType=SelectOne")
            .process(new AddTokenInfoForContractTableProcessor())
                .to("sql:classpath:sql/vue/asset/contractSave.sql?outputHeader=save")
            .process(ex -> {
                ex.getIn().removeHeader("dsl");
                ex.getIn().removeHeaders("sql_*");
            })
            // .setBody(simple("${headers.contract}"))
            .to("direct:response-200");
        from("direct:asset-service-join").routeId("manage:service-join")
            .process(jwtVerfyProcessor)
            .marshal().json(JsonLibrary.Jackson)
            .to("json-validator:json-schema/asset/asset-service-join-schema.json")
            .unmarshal().json(JsonLibrary.Jackson, ServiceJoin.class)
            .process(ex -> {
                // emitAndWait('daml.request.InsertUser'
                // 요청을 보낸 사용자 정보를 서비스 항목에 등록 (insert_user)
                String userid = (String) ex.getIn().getHeader("userid");
                ServiceJoin body = (ServiceJoin) ex.getIn().getBody(ServiceJoin.class);
                body.setUserid(userid);
                body.setOwner(userid);
                // ex.getIn().setHeader("service_join", body);
                // ex.getIn().setHeader("Authorization", "Bearer " + TokenUtils.generateDamlTokenString(userid));
                // 서비스 유무 체크, operator권한을 통해 전체 서비스 목록중에서 체크
                DAMLQueryResponse service = ledger.getActiveContractsByQuery(ex, Query.builder()
                                                .templateIds(List.of("Service:Service"))
                                                .query(Map.of("uuid",body.getUuid())).build());
                if (service.getStatus() != 200 || service.getResult().size() == 0) {
                    throw new ForbiddenError("서비스가 존재하지 않습니다.");
                }

                DefaultDAMLResult wallet = WalletUtil.wallet(ex,body.getOwner(),body.getAddress(),body.getUserid());
                if (wallet == null) {
                    throw new ForbiddenError("사용자 지갑이 존재하지 않습니다.");
                }

                List<Object> infos = (List) service.getResult().get(0).getPayload().get("tokenInfos");
                Map<String,Object> payload = service.getResult().get(0).getPayload();
                List<Object> infos1 = (List)infos.get(0);
                String blockChainAddress = infos1.get(0).toString();

                body.setBlockchainAddr(blockChainAddress);
                body.setServiceType(payload.get("serviceType").toString());
                body.setTokenSymbol(payload.get("tokenSymbol").toString());
                body.setServiceName(payload.get("serviceName").toString());
                body.setWalletAddressContractId(wallet.getContractId());
                ex.getIn().setBody(body);
            })
            .choice()
                // .when(simple("${body.status} != 200 || ${body.result.size} == 0"))
                //     .throwException(new ForbiddenError("계약서가 존재하지 않습니다."))
                .when(simple(String.format("${body.serviceType} == '%1s'","FT")))
                    .process(new JoinServiceProcessor())
                .when(simple(String.format("${body.serviceType} == '%1s'","NFT")))
                .when(simple(String.format("${body.serviceType} == '%1s'","GiftCard")))
                .otherwise()
                    .setBody(simple("${body.serviceType}"))
            .end()
            .process(ex -> {
                ServiceJoin body = ex.getIn().getBody(ServiceJoin.class);
                DAMLExeciseResponse joinServiceExecise = new Ledger().execise(ex,
                                        Exercise.builder()
                                            .templateId("Wallet:Wallet")
                                            .contractId(body.getWalletAddressContractId())
                                            .choice("JoinService")
                                            .argument(Map.of(
                                                "blockchainAddr", body.getBlockchainAddr(),
                                                "serviceName", body.getServiceName(),
                                                "tokenSymbol", body.getTokenSymbol(),
                                                "serviceType",body.getServiceType()))
                                            .build()
                                            ,body.getUserid());
                // todo
                // [2021-09-14 10:48:46] 유효성체크
                ex.getIn().setBody(joinServiceExecise);
            })
            .to("direct:response-200");
        from("direct:asset-balances").routeId("asset:balances")
            .process(jwtVerfyProcessor)
            .process(ex -> {
                String address = ex.getIn().getHeader("address").toString();
                String userid = (String) ex.getIn().getHeader("userid");

                DefaultDAMLResult wallet = WalletUtil.wallet(ex,userid,address);
                if (wallet == null) {
                    throw new ForbiddenError("서비스 또는 지갑이 존재하지 않습니다.");
                }
                // 결과 목록
                List<Map<String,String>> result = new ArrayList<Map<String,String>>();
                List<List> tokenSymbols = (List<List>) wallet.getPayload().get("services");
                // List<Map<String,Object>> balances = new ArrayList<Map<String,Object>>();
                tokenSymbols.forEach(symbol -> {
                    String blockChainAddress = symbol.get(0).toString();
                    DAMLQueryResponse token = ledger.getActiveContractsByQuery(ex, Query.builder()
                                                        .templateIds(List.of("ERC20:ERC20"))
                                                        .query(Map.of("blockchainAddr",blockChainAddress))
                                                        .build(),
                                                        userid);
                    if (token.getStatus() != 200 || token.getResult().size() == 0) {
                        //
                    } else {
                        // DefaultDAMLResult service = 
                        Map<String,Object> payload = token.getResult().get(0).getPayload();
                        List<List> inBalances =  (List<List>) payload.get("balances");//(List<List>) service.getPayload().get("balances");
                        inBalances.forEach(e -> {
                            // 0 address, 1 balance
                            if (e.get(0).toString().equals(address)) {
                                result.add(Map.of(
                                        "whoiam",userid,
                                        "serviceName",payload.get("serviceName").toString(),
                                        "tokenSymbol",payload.get("tokenSymbol").toString(),
                                        "address",address,
                                        "balance",e.get(1).toString(),
                                        "uuid",payload.get("uuid").toString()));
                            }
                        });
                    }
                });
                ex.getIn().setBody(result);
            })
            .to("direct:response-200");
        from("direct:asset-transfer").routeId("asset:transfer")
            .process(jwtVerfyProcessor)
            .marshal().json(JsonLibrary.Jackson)
                .to("json-validator:json-schema/asset/asset-trasfer-schema.json")
            .unmarshal().json(JsonLibrary.Jackson, TranferPayload.class)
            .process(ex -> {
                // [2021-10-26 17:04:12]
                // DAML과 DB에 각각 저장되어 있는 정보에 대해 어디를 기준으로 체크를 해야할까?
                String fromId = (String) ex.getIn().getHeader("userid");
                TranferPayload body = ex.getMessage().getBody(TranferPayload.class);
                // service uuid 조회
                DefaultDAMLResult service = ServiceUtil.service(ex,body.getUuid());
                
                if (service == null) {
                    throw new ForbiddenError(body.getUuid() + "서비스가 존재하지 않습니다");
                };
                
                Map<String,Object> wallet = RepositoryHelper.wallet(ex,body.getToAddress());
                // userid,account_addr
                if (wallet == null) {
                    throw new ForbiddenError(body.getFromAddress() + "지갑 정보가 존재하지 않습니다");
                }

                String toId = wallet.get("userid").toString();

                Map<String,Object> user = RepositoryHelper.user(ex, toId);
                if (user == null) {
                    throw new ForbiddenError("사용자 정보가 존재하지 않습니다");
                }

                DefaultDAMLResult token = ErcUtil.erc(ex, "ERC20", body.getUuid());
                if (token == null) {
                    throw new ForbiddenError("ERC 토큰이 존재하지 않습니다.");                        
                } else {
                    Map<String,Object> payload = service.getPayload();
                    if (payload.get("serviceType").equals("FT")) {
                        Transfer argument = Transfer.builder()
                                            .amount(body.getAmount())
                                            .fromAddress(body.getFromAddress())
                                            .fromID(fromId)
                                            .toAddress(body.getToAddress())
                                            .toID(toId).build();
                        DAMLExeciseResponse transfereExecise =
                            ledger.execise(ex, 
                                Exercise.builder()
                                    .templateId("ERC20:ERC20")
                                    .contractId(token.getContractId())
                                    .choice("TransferERC20")
                                    .argument(argument).build(),
                                    fromId);
                                    
                        if (transfereExecise.getStatus() != 200) {
                            // throw 자산이 없으면 오류
                        }

                        ObjectMapper om = new ObjectMapper();
                        Map<String,Object> event = (Map<String,Object>) transfereExecise.getResult().getEvents().get(1);
                        Map<String,Object> created = (Map<String,Object>) event.get("created");

                        try {
                            String templateId = token.getTemplateId().split(":")[2];
                            Contract contract = Contract.builder()
                                                    .contractId(token.getContractId())
                                                    .templateId(templateId)
                                                    .status(0)
                                                    .tokenSymbol(token.getPayload().get("tokenSymbol").toString())
                                                    .contractItem(om.writeValueAsString(created))
                                                    .execiserChoice(om.writeValueAsString(Map.of("choiceName", "TransferERC20","performer", fromId)))
                                                    .build();

                            Map<String,Object> ug = RepositoryHelper.updateContractAndGet(ex,contract);
                            contract = Contract.builder()
                                        .contractId(transfereExecise.getResult().getExerciseResult())
                                        .archivedId(ug.get("contractid").toString())
                                        .parentId(ug.get("parentid").toString())
                                        .uuid(body.getUuid())
                                        .signatories(ug.get("signatories").toString())
                                        .observers(ug.get("observers").toString())
                                        .templateId(templateId)
                                        .status(1)
                                        .contractItem(om.writeValueAsString(created))
                                        .execiserChoice(om.writeValueAsString(Map.of("choiceName", "TransferERC20","performer", fromId)))
                                        .build();

                            Object saved = RepositoryHelper.addContract(ex,contract);   
                            
                            // [2021-10-27 09:25:35]
                            // 중복된 클래스이름은 나중에 리펙토링
                            shinhands.com.model.repository.Transfer transfer = shinhands.com.model.repository.Transfer.builder()
                                            .serviceName(token.getPayload().get("serviceName").toString())
                                            .tokenSymbol(token.getPayload().get("tokenSymbol").toString())
                                            .signatories(om.writeValueAsString(service.getSignatories()))
                                            .observers(om.writeValueAsString(service.getObservers()))
                                            .fromId(fromId)
                                            .fromAddress(body.getFromAddress())
                                            .toId(toId)
                                            .toAddress(body.getToAddress())
                                            .amount(body.getAmount())
                                            .operator("dap_operator")                    
                                            .uuid(body.getUuid())
                                            .build();
                                            
                            saved = RepositoryHelper.addTransferHistory(ex,transfer);
                            // BESU 전송, 0X -> 0x
                            // TODO
                            // BESU
                            // Event.on('transfer_FT', async (data: any) 
                            TransferERC20Payload.builder()
                                .sender(body.getFromAddress())
                                .receiver(body.getToAddress())
                                .amount(body.getAmount())
                                .dividable((Boolean) token.getPayload().get("dividable"))
                                .contractAddress(token.getPayload().get("blockchainAddr").toString())
                                .saveDBResult(null)
                                .build();
                            log.info("# log",saved.toString()); 
                            ex.getIn().setBody(created);
                        } catch (JsonProcessingException e) {
                            // stop!
                            e.printStackTrace();
                        }
                    } else {
                        throw new ForbiddenError("해당 서비스는 FT 토큰만 전송 기능을 제공합니다.");
                    }
                }
            })
            .to("direct:response-200");
        from("direct:asset-mint").routeId("asset:mint")
            .process(jwtVerfyProcessor)
                .marshal().json(JsonLibrary.Jackson)
                    .to("json-validator:json-schema/asset/asset-mint-burn-schema.json")
                .unmarshal().json(JsonLibrary.Jackson, MintBurnPayload.class)
                .process(ex -> {
                    FluentProducerTemplate templatet = ex.getContext().createFluentProducerTemplate();
                    ObjectMapper om = new ObjectMapper();
                    String userid = (String) ex.getIn().getHeader("userid");
                    MintBurnPayload body = ex.getMessage().getBody(MintBurnPayload.class);
                    DefaultDAMLResult service = ServiceUtil.service(ex,body.getUuid(),userid);
                    // Map<String,Object> servicePayload = service.getPayload();
                    if (service == null) {
                        throw new ForbiddenError(body.getUuid() + "서비스가 존재하지 않습니다");
                    }
                    if (service.getPayload().get("serviceType").equals("FT")) {
                        DefaultDAMLResult token = ErcUtil.erc(ex, "ERC20", body.getUuid());
                        if (token == null) {
                            throw new ForbiddenError("ERC 토큰이 존재하지 않습니다.");                        
                        } else {
                            DAMLExeciseResponse mintExecise = new Ledger().execise(ex,
                                                                        Exercise.builder()
                                                                            .templateId("ERC20:ERC20")
                                                                            .contractId(token.getContractId())
                                                                            .choice("Mint")
                                                                            .argument(Map.of(
                                                                                "userID",userid,
                                                                                "address",body.getAddress(),
                                                                                "amount",body.getAmount()
                                                                            )).build()
                                                                            ,userid);
                            if (mintExecise.getStatus() != 200) {
                                // throw
                            }
                            Map<String,Object> eventItem = (Map<String,Object>) mintExecise.getResult().getEvents().get(1);

                            try {
                                String created = om.writeValueAsString(eventItem.get("created"));
                                String templateId = token.getTemplateId().split(":")[2];
                                Contract contract = Contract.builder()
                                                        .contractId(token.getContractId())
                                                        .templateId(templateId)
                                                        .status(0)
                                                        .contractItem(created)
                                                        .execiserChoice(om.writeValueAsString(Map.of("choiceName", "Mint","performer", userid)))
                                                        .build();

                                Map<String,Object> ug = RepositoryHelper.updateContractAndGet(ex,contract);

                                contract = Contract.builder()
                                            .contractId(mintExecise.getResult().getExerciseResult())
                                            .archivedId(ug.get("contractid").toString())
                                            .parentId(ug.get("parentid").toString())
                                            .uuid(body.getUuid())
                                            .signatories(ug.get("signatories").toString())
                                            .observers(ug.get("observers").toString())
                                            .templateId(templateId)
                                            .status(1)
                                            .contractItem(created)
                                            .execiserChoice(om.writeValueAsString(Map.of("choiceName", "Mint","performer", userid)))
                                            .build();
                            
                                Object newContractId = RepositoryHelper.addContract(ex,contract);
                                ex.getIn().setBody(eventItem.get("created"));
                            } catch (JsonProcessingException e) {
                                // stop!
                                e.printStackTrace();
                            }
                        }
                    } else {
                        throw new ForbiddenError("mint 기능은 FT토큰에서만 사용가능합니다.");
                    }
                })
                .to("direct:response-200");
        from("direct:asset-burn").routeId("asset:burn")
            .process(jwtVerfyProcessor)
                .marshal().json(JsonLibrary.Jackson)
                    .to("json-validator:json-schema/asset/asset-mint-burn-schema.json")
                .unmarshal().json(JsonLibrary.Jackson, MintBurnPayload.class)
                .process(new ERCBurnProcessor())
                .process(ex -> {
                    // [2021-10-28 10:38:17] TODO
                    // besu event
                    EncodDataPayload edpl = ex.getIn().getHeader("dsl_to_besu",EncodDataPayload.class);
                    ex.getIn().setBody(edpl);
                })
                .to("direct:response-200");
        from("direct:asset-distribution").routeId("asset:distribution")
        /*
        '/distribution',
  authenticate,
  auditlog,
        */
            .process(jwtVerfyProcessor)
            .marshal().json(JsonLibrary.Jackson)
            // .to("json-validator:json-schema/asset/asset-service-join-schema.json")
            .unmarshal().json(JsonLibrary.Jackson, DistributionPayload.class)
            .process(new DistributionProcessor())
            .process(ex -> {
                /**
                 * TODO
                 * [2021-10-13 15:38:43] besu처리
                 */
                EncodDataDistributePayload edd = ex.getIn().getHeader("dsl",EncodDataDistributePayload.class);
                // BesuEvnet.emit('Distribute_FT', DistributePayload);
            })
            .to("direct:response-success-200");
        // 미구현, transfer으로만 구성됨 (deposiot : fiat money -> token)
        from("direct:asset-deposit").routeId("asset:deposit").to("mock:ok");
        // 미구현, transfer으로만 구성됨 (swap : sell -> service owner, buy -> user)
        from("direct:asset-swap").routeId("asset:swap").to("mock:ok");
    }
}