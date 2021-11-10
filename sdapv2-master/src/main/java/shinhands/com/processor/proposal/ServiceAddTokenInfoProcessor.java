package shinhands.com.processor.proposal;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import shinhands.com.beans.Ledger;
import shinhands.com.exception.ForbiddenError;
import shinhands.com.model.daml.DAMLExeciseResponse;
import shinhands.com.model.daml.DAMLQueryResponse;
import shinhands.com.model.daml.Exercise;
import shinhands.com.model.daml.template.DefaultDAMLResult;
import shinhands.com.model.daml.template.Query;
import shinhands.com.model.request.ServiceProposalPayload;

public class ServiceAddTokenInfoProcessor implements Processor {

    public void process(Exchange ex) throws Exception {
        Ledger ledger = new Ledger();
        // saveContractIdService
        Map<String,Object> dsl = ex.getIn().getHeader("dsl", Map.class);
        ServiceProposalPayload proposal = (ServiceProposalPayload) dsl.get("proposal");

        DAMLQueryResponse service =
            ledger.getActiveContractsByQuery(ex, Query.builder()
                .templateIds(List.of("Service:Service"))
                .query(Map.of("uuid",proposal.getUuid())).build()
            );

        if (service.getStatus() != 200 || service.getResult().size() == 0) {
            throw new ForbiddenError("서비스가 존재하지 않습니다.");
        }
        String besuBlockChainAddress = dsl.get("besu_created_contract_address").toString();
        DefaultDAMLResult contract = service.getResult().get(0);

        DAMLExeciseResponse result =
            ledger.execise(ex, Exercise.builder()
                .templateId("Service:Service")
                .contractId(contract.getContractId())
                .choice("AddTokenInfo")
                .argument(Map.of(
                    "requestBlockchainAddr",besuBlockChainAddress,
                    "requestTokenType",proposal.getServiceType(),
                    "requestTokenSymbol",proposal.getTokenSymbol())
                ).build(),
                proposal.getIssuer());

        Map<String,Object> eventItem = (Map<String,Object>) result.getResult().getEvents().get(1);
        String choice = new ObjectMapper().writeValueAsString(
                                Map.of("choiceName","AddTokenInfo",
                                        "performer",proposal.getIssuer()));
        dsl.put("update_contract",
            Map.of(
                "execiserchoice", choice,
                "contractid",contract.getContractId(),
                "status",0,
                "contractitem",new ObjectMapper().writeValueAsString(eventItem.get("created"))
                )
        );
        dsl.put("contractid",contract.getContractId());
        ex.getIn().setHeader("dsl",dsl);
        ex.getIn().setBody(result);
    }
}