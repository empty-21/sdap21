package shinhands.com.aggregation;

import java.util.List;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.camel.AggregationStrategy;
import org.apache.camel.Exchange;

import shinhands.com.beans.Ledger;
import shinhands.com.exception.ForbiddenError;
import shinhands.com.model.daml.DAMLExeciseResponse;
import shinhands.com.model.daml.DAMLQueryResponse;
import shinhands.com.model.daml.Exercise;
import shinhands.com.model.daml.template.DefaultDAMLResult;
import shinhands.com.model.daml.template.Query;
import shinhands.com.model.request.ServiceProposalPayload;

@ApplicationScoped
public class BesuContractAggregationStrategy implements AggregationStrategy {

    // @Inject
    // Ledger ledger;

    /**
     * - damlbiz, services.beus.index.ts
     * - damlbiz, event.besu.index.ts
     */
    public Exchange aggregate(Exchange oex, Exchange nex) {
        Map<String,Object> dsl = oex.getIn().getHeader("dsl", Map.class);
        ServiceProposalPayload proposal = (ServiceProposalPayload) dsl.get("proposal");

        Ledger ledger = new Ledger();
        // daml에서 token생성후 besu에서 contract생성 결과
        final String besuContractAddress = nex.getIn().getBody(String.class);
        // besuContractAddress 값 체크
        /*
        if (result.status === undefined || result.status === '0x0') {
      console.dir(result);
      console.log('Create Besu Contract is  fail');
      const failData = {
        serviceName: data.serviceName,
        userid: data.userid
      };
      te.message = makeResult(false, { userid: data.userid, failData });
      sendQueueMessage(te);
      const archiveData = {
        uuid: data.uuid,
        issuer: data.issuer
      };
      ManageSV.ArchiveService(archiveData);
      ManageSV.ArchiveFTToken(archiveData);
      return;
    }
        */
        /**
         * TODO
         * [2021-08-31 17:52:09]
         *  - Model 사용
         * [2021-09-06 11:04:35]
         *  - proposal을 계속 사용할지 아니면 getActiveContractsByQuery를 통해 결과값을 사용할지..
         *  - 데이터는 항상 같아야함, 상태 변경이 안되긴함
         */
        // getService
        DAMLQueryResponse service =
            ledger.getActiveContractsByQuery(nex, Query.builder()
                .templateIds(List.of("Service:Service"))
                .query(Map.of("uuid",proposal.getUuid())).build()
            );

        if (service.getStatus() != 200 || service.getResult().size() == 0) {
            // throw new ForbiddenError("서비스가 존재하지 않습니다.");
        }


        DefaultDAMLResult contract = service.getResult().get(0);
        // getWallet, 필수항목
        // getWallet(data: any)
        DAMLQueryResponse wallet =
            ledger.getActiveContractsByQuery(nex, Query.builder()
                .templateIds(List.of("Wallet:Wallet"))
                .query(Map.of(
                    "owner",proposal.getOwner(),
                    "address",proposal.getOwnerAddress())
                    ).build(),
                proposal.getOwner()
        );

        if (wallet.getStatus() != 200 || wallet.getResult().size() == 0) {
            try {
                throw(new ForbiddenError("사용자 지갑이 존재하지 않습니다."));
            } catch (Exception ignored) {}
        }

        Map<String,Object> walletArgument = Map.of(
            "blockchainAddr",besuContractAddress,
            "serviceName",contract.getPayload().get("serviceName").toString(),
            "tokenSymbol",contract.getPayload().get("tokenSymbol").toString(),
            "serviceType",contract.getPayload().get("serviceType").toString()
            );

        DAMLExeciseResponse joinWallet =
            ledger.execise(nex, Exercise.builder()
                .templateId("Wallet:Wallet")
                .contractId(wallet.getResult().get(0).getContractId())
                .choice("JoinService")
                .argument(walletArgument).build(),
                proposal.getOwner());
        // todo
        // [2021-09-06 10:47:03] rollback, 과거 계약 정보 전부 archive필요
        if (joinWallet.getStatus() != 200) {
            try {
                throw(new ForbiddenError("사용자 지갑 등록에 실패하였습니다.(1)"));
            } catch (Exception ignored) { }
        };


        // 서비스 생성자와 Onwer가 다른경우, issuer와 issuerAddress로 처리
        if (!proposal.getIssuer().equals(proposal.getOwner())) {
            DAMLQueryResponse wallet2 =
                ledger.getActiveContractsByQuery(nex, Query.builder()
                    .templateIds(List.of("Wallet:Wallet"))
                    .query(Map.of(
                        "owner",proposal.getIssuer(),
                        "address",proposal.getIssuerAddress())
                        ).build(),
                    proposal.getIssuer()
                );

            if (wallet2.getStatus() != 200 || wallet2.getResult().size() == 0) {
                try {
                    throw(new ForbiddenError("(issuer) 사용자 지갑이 존재하지 않습니다."));
                } catch (Exception ignored) {}
            }
            DAMLExeciseResponse joinWallet2 =
                ledger.execise(nex, Exercise.builder()
                    .templateId("Wallet:Wallet")
                    .contractId(wallet2.getResult().get(0).getContractId())
                    .choice("JoinService")
                    .argument(walletArgument).build(),
                    proposal.getIssuer());
            oex.getIn().setHeader("debug_joinWallet2", joinWallet2);
            // rollback!!
            if (joinWallet2.getStatus() != 200) {
                try {
                    throw(new ForbiddenError("(issuer) 사용자 지갑 등록에 실패하였습니다."));
                } catch (Exception ignored) {
                    //
                }
            }
        }

        // todo
        // [2021-09-06 11:16:07] 서비스 독립 고민 필요
        // await AfterBesuSV.saveContractIdERC20
        // getService
        DAMLQueryResponse findERCService =
            ledger.getActiveContractsByQuery(nex, Query.builder()
                .templateIds(List.of("ERC20:ERC20"))
                .query(Map.of("uuid",proposal.getUuid())).build(),
                proposal.getIssuer()
            );
        if (findERCService.getStatus() != 200 || findERCService.getResult().size() == 0) {
            try {
                throw(new ForbiddenError("계약서가 존재하지 않습니다."));
            } catch (Exception ignored) { }
        }

         DAMLExeciseResponse saveContractIdERC20 =
                ledger.execise(nex, Exercise.builder()
                    .templateId("ERC20:ERC20")
                    .contractId(findERCService.getResult().get(0).getContractId())
                    .choice("SetupBlockChainAddr")
                    .argument(Map.of(
                        "requestBlockchainAddr", besuContractAddress
                        )).build(),
                    proposal.getIssuer());

        if (saveContractIdERC20.getStatus() != 200) {
            try {
                throw(new ForbiddenError("SetupBlockChainAddr choice 실행에 실패했습니다."));
            } catch (Exception ignored) {
                // rollback!!??
            }
        }
        oex.getIn().setBody(saveContractIdERC20);
        // oex.getIn().setHeader("result", saveContractIdERC20);
        // save contract



        // await saveContractService.saveContractChoice_Transfer
        try {
            Map<String,Object> eventItem = (Map<String,Object>) saveContractIdERC20.getResult().getEvents().get(1);
            String choice = new ObjectMapper().writeValueAsString(
                                Map.of("choiceName","SetupBlockChainAddr",
                                        "performer",proposal.getIssuer()));
            dsl.put("update_contract",
                Map.of(
                    "execiserchoice", choice,
                    "contractid",findERCService.getResult().get(0).getContractId(),
                    "status",0,
                    "contractitem",new ObjectMapper().writeValueAsString(eventItem.get("created"))
                    )
            );
        } catch (JsonProcessingException ignored) {
            // stop!
        }
        //  await AfterBesuSV.saveContractIdService
        Map<String, Object> UpdateContract = ( Map<String, Object>) oex.getIn().getHeader("update_contract");

        // const contrctInsertServiceResult = await AfterBesuSV.saveContractIdService(
        oex.getIn().setHeader("debug_service", service);
        oex.getIn().setHeader("debug_wallet", wallet);
        oex.getIn().setHeader("debug_joinWallet", joinWallet);
        oex.getIn().setHeader("debug_update_contract", UpdateContract);

        dsl.put("contractid", findERCService.getResult().get(0).getContractId());

/**
 * [2021-08-31 16:44:41]
 *   damlbiz, service.wallets.joinService
   * wallet에 service로 만들어진 token에 추가함
   * 10.16 : operator 로 서비스를 조인할 수있음
   * @param data : {operator, owner, tokenSymbol, serviceName, address}
   */
        dsl.put("besu_created_contract_address", besuContractAddress);
        oex.getIn().setHeader("dsl",dsl);
        return oex;
    }
}
