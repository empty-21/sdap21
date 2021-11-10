package shinhands.com.processor.assset;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.camel.Exchange;
import org.apache.camel.FluentProducerTemplate;
import org.apache.camel.Processor;

import lombok.extern.slf4j.Slf4j;
import shinhands.com.beans.ErcUtil;
import shinhands.com.beans.Ledger;
import shinhands.com.beans.RepositoryHelper;
import shinhands.com.beans.ServiceUtil;
import shinhands.com.exception.ForbiddenError;
import shinhands.com.model.besu.DistributeData;
import shinhands.com.model.besu.EncodDataDistributePayload;
import shinhands.com.model.daml.DAMLExeciseResponse;
import shinhands.com.model.daml.Exercise;
import shinhands.com.model.daml.template.DefaultDAMLResult;
import shinhands.com.model.repository.Contract;
import shinhands.com.model.repository.Transfer;
import shinhands.com.model.request.DistributeAccount;
import shinhands.com.model.request.DistributionPayload;

@Slf4j
public class DistributionProcessor implements Processor {

    public void process(Exchange ex) throws Exception {
        ObjectMapper om = new ObjectMapper();
        FluentProducerTemplate templatet = ex.getContext().createFluentProducerTemplate();
        String userid = (String) ex.getIn().getHeader("userid");
        DistributionPayload body = ex.getMessage().getBody(DistributionPayload.class);
        List<DistributeAccount> accounts =  body.getAccounts();
        
        // Besu tx용
        List<DistributeAccount> ddAccounts = new ArrayList<DistributeAccount>();

        // service uuid 조회
        DefaultDAMLResult service = ServiceUtil.service(ex,body.getUuid(),userid);
        Map<String,Object> servicePayload = service.getPayload();
        
        if (service == null) {
            throw new ForbiddenError(body.getUuid() + "서비스가 존재하지 않습니다");
        }

        // erc 체크
        DefaultDAMLResult Erc = ErcUtil.erc(ex, "ERC20", body.getUuid());
        if (Erc == null) throw new ForbiddenError("ERC 토큰이 존재하지 않습니다.");
        String contractAddress = Erc.getPayload().get("blockchainAddr").toString();
        
        // distributable 속성 체크
        List<List> attributes = (List<List>) service.getPayload().get("attribute");
        // 익명함수 이슈 
        AtomicInteger distributable = new AtomicInteger(0);
        AtomicInteger dividable = new AtomicInteger(0);
        attributes.forEach(atts -> {
            if (distributable.get() == 0) {
                if (atts.get(0).equals("burnable") && atts.get(1).equals(true)) {
                    distributable.getAndIncrement();
                }
            }
            if (dividable.get() == 0) {
                if (atts.get(0).equals("dividable") && atts.get(1).equals(true)) {
                    dividable.getAndIncrement();
                }
            }
        });

        if (distributable.get() == 0) {
            throw new ForbiddenError("distributable 속성이 없는 토큰입니다.");
        } 
        // 사용자및 지갑 주소 조회
        List<Map<String,String>> accountRows = new ArrayList<Map<String,String>>();
        accounts.forEach(account -> {
            Map<String,String> wallet = templatet.withHeader("dsl",Map.of("account_addr",account.getAddress()))
                                            .to("direct:sql-get-wallet2")
                                            .request(Map.class);
            if (wallet == null) {
                // throw new ForbiddenError(account.getAddress()+" address 정보가 존재하지 않습니다");
            } else {
                // da.setAddress(account.getAddress());
                // da.setAmount(account.getAmount());
                ddAccounts.add(new DistributeAccount(account.getAmount(),account.getAddress()));
                    // DistributeAccount.builder()
                    //         .address(account.getAddress())
                    //         .amount(account.getAmount()).build());

                accountRows.add(Map.of(
                        "address",account.getAddress(),
                        "amount",account.getAmount(),
                        "id",wallet.get("userid")));
            }                
        });

        accountRows.forEach(row -> {
            // [2021-10-15 09:15:51]
            // 매회 erc20에 대한 exercise를 처리할때마다 contractId가 갱신되므로 재조회 필요
            DefaultDAMLResult innerErc = ErcUtil.erc(ex, "ERC20", body.getUuid());
            DAMLExeciseResponse execise = new Ledger().execise(ex,
                            Exercise.builder()
                                .templateId("ERC20:ERC20")
                                .contractId(innerErc.getContractId())
                                .choice("TransferERC20")
                                .argument(Map.of(
                                    "fromID",userid,
                                    "toID",row.get("id").toString(),
                                    "fromAddress",body.getAddress(),
                                    "toAddress",row.get("address").toString(),
                                    "amount",row.get("amount")
                                )).build()
                                ,userid);

            Map<String,Object> eventItem = 
                    (Map<String,Object>) execise.getResult().getEvents().get(1);
            
            // if (transferERC20Response.getResult() != 200) {
            //     // thro
            // }
            // update and select (contractUpdate3.sql)
            
            try {
                String created = om.writeValueAsString(eventItem.get("created"));
                String templateId = innerErc.getTemplateId().split(":")[2];
                Contract contract = Contract.builder()
                                        .contractId(innerErc.getContractId())
                                        .templateId(templateId)
                                        .status(0)
                                        .contractItem(om.writeValueAsString(eventItem.get("created")))
                                        .execiserChoice(om.writeValueAsString(Map.of("choiceName", "TransferERC20","performer", userid)))
                                        .build();
                // TODO
                // [2021-10-13 17:54:59]
                // 쿼리구문에서 오류가 발생하면 해당 서비스를 다시는 사용하지 못함. 관리 추적기능 필요
                // [2021-10-15 10:52:15]
                // insert update 구분으로 변경..
                Map<String,Object> ug = templatet.withHeader("dsl",contract)
                                                        .to("direct:sql-update-get-contract")
                                                        .request(Map.class);
                contract = Contract.builder()
                            .contractId(execise.getResult().getExerciseResult())
                            .archivedId(ug.get("contractid").toString())
                            .parentId(ug.get("parentid").toString())
                            .uuid(body.getUuid())
                            .signatories(ug.get("signatories").toString())
                            .observers(ug.get("observers").toString())
                            .templateId(templateId)
                            .status(1)
                            .contractItem(created)
                            .execiserChoice(om.writeValueAsString(Map.of("choiceName", "TransferERC20","performer", userid)))
                            .build();
                Object saved = RepositoryHelper.addContract(ex,contract);                                     
                
                log.info(saved.toString());       

                Transfer transfer = Transfer.builder()
                                            .serviceName(servicePayload.get("serviceName").toString())
                                            .tokenSymbol(servicePayload.get("tokenSymbol").toString())
                                            .signatories(om.writeValueAsString(service.getSignatories()))
                                            .observers(om.writeValueAsString(service.getObservers()))
                                            .fromId(userid)
                                            .fromAddress(body.getAddress())
                                            .toId(row.get("id").toString())
                                            .toAddress(row.get("address").toString())
                                            .amount(row.get("amount"))
                                            .operator("dap_operator")                    
                                            .uuid(body.getUuid())
                                            .build();
                saved = RepositoryHelper.addTransferHistory(ex,transfer);                                     
                log.info("# log",saved.toString()); 
            } catch (JsonProcessingException e) {
                // stop!
                e.printStackTrace();
            }
        });
        DistributeData dd = DistributeData.builder()
                                    .accounts(ddAccounts)
                                    .dividable(dividable.get() == 1)
                                    .build();
        EncodDataDistributePayload edd = EncodDataDistributePayload.builder()
                                            .contractAddress(contractAddress)
                                            .distributeData(dd)
                                            .build();
        ex.getIn().setHeader("dsl", edd);
    }
}