package shinhands.com.aggregation;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.camel.AggregationStrategy;
import org.apache.camel.Exchange;

import shinhands.com.model.daml.DAMLResponse;
import shinhands.com.model.daml.Exercise;
import shinhands.com.util.TokenUtils;

public class ServiceProposalAggregationStrategy implements AggregationStrategy {

    public Exchange aggregate(Exchange oex, Exchange nex) {
        Map<String,Object> dsl = oex.getIn().getHeader("dsl", Map.class);
        String userid = dsl.get("userid").toString();
        List<String> signatories = (List<String>) dsl.get("signatories");

        // String userid = (String) oex.getIn().getHeader("userid");
        DAMLResponse result = nex.getIn().getBody(DAMLResponse.class);
        // Service:ServiceRequest 저장
        // digitalAsset.ts:63
         try {
            dsl.put("contract",
                    Map.of(
                        "contractid",result.getResult().getContractId(),
                        "signatories",new ObjectMapper().writeValueAsString(result.getResult().getSignatories()),
                        "observers",new ObjectMapper().writeValueAsString(result.getResult().getObservers()),
                        "templateid",result.getResult().getTemplateId().split(":")[2],
                        "status",1,
                        "contractitem",new ObjectMapper().writeValueAsString(result.getResult()),
                        "uuid",result.getResult().getPayload().get("uuid").toString()
                        // "execiserchoice",new ObjectMapper().writeValueAsString(List.of())
                        )
                );
        } catch (JsonProcessingException e) {

        }

        Exercise exercise = Exercise.builder()
                                .templateId("Service:ServiceRequest")
                                .choice("Invite")
                                .contractId(result.getResult().getContractId())
                                .argument(Map.of("signatories", signatories))
                                .build();
        oex.getIn().setBody(exercise);
        oex.getIn().setHeader("Authorization", "Bearer " + TokenUtils.generateDamlTokenString(userid));
        oex.getIn().setHeader("dsl",dsl);
        return oex;
    }
}
