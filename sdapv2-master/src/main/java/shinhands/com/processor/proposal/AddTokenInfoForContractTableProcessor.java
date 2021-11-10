package shinhands.com.processor.proposal;

import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import shinhands.com.model.daml.DAMLExeciseResponse;
import shinhands.com.model.request.ServiceProposalPayload;

public class AddTokenInfoForContractTableProcessor implements Processor {

    public void process(Exchange ex) throws Exception {
        Map<String,Object> dsl = ex.getIn().getHeader("dsl", Map.class);
        ServiceProposalPayload proposal = (ServiceProposalPayload) dsl.get("proposal");

        Map<String, Object> contract = (Map) ex.getIn().getHeader("fetch_contract");
        DAMLExeciseResponse result = ex.getIn().getBody(DAMLExeciseResponse.class);
        Map<String, Object> eventItem = (Map<String, Object>) result.getResult().getEvents().get(1);
        contract.put("archivedid", contract.get("contractid"));
        contract.put("contractid", result.getResult().getExerciseResult());
        contract.put("status", 1);
        contract.put("contractitem", new ObjectMapper().writeValueAsString(eventItem.get("created")));
        contract.put("execiserchoice", new ObjectMapper()
                .writeValueAsString(Map.of("choiceName", "AddTokenInfo", "performer", proposal.getIssuer())));
        dsl.put("contract", contract);
        ex.getIn().setHeader("dsl",dsl);
        ex.getIn().setBody(contract);
    }
}