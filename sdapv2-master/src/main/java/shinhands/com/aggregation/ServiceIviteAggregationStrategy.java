package shinhands.com.aggregation;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.camel.AggregationStrategy;
import org.apache.camel.Exchange;

import shinhands.com.beans.Ledger;
import shinhands.com.model.daml.DAMLExeciseResponse;
import shinhands.com.model.daml.DAMLQueryResponse;
import shinhands.com.model.daml.Exercise;
import shinhands.com.model.daml.template.Query;
import shinhands.com.model.request.ServiceProposalPayload;
import shinhands.com.util.TokenUtils;

public class ServiceIviteAggregationStrategy implements AggregationStrategy {

    public Exchange aggregate(Exchange oex, Exchange nex) {
        // ServiceProposal 계약이 있는지 체크, 해당 Proposal는 Finalize이후 archive처리
        Map<String,Object> dsl = oex.getIn().getHeader("dsl", Map.class);
        String userid = dsl.get("userid").toString();
        ServiceProposalPayload proposal = (ServiceProposalPayload) dsl.get("proposal");
        Ledger ledger = new Ledger();

        DAMLQueryResponse findProposal =
            ledger.getActiveContractsByQuery(nex, Query.builder()
                    .templateIds(List.of("Service:ServiceProposal"))
                    .query(Map.of("uuid",proposal.getUuid())).build()
            );

        if (findProposal.getStatus() != 200 || findProposal.getResult().size() == 0) {
            // exception error
        } else {
            // Finalize 가 성공하면 다음 process에서 사용
            dsl.put("proposal_in_daml", findProposal);
        }

        DAMLExeciseResponse result = nex.getIn().getBody(DAMLExeciseResponse.class);
         // DB 저장용, 마지막에 하는게 좋은가?
         Map<String,Object> created = (Map) ((Map) result.getResult().getEvents().get(0)).get("created");
         try {
                dsl.put("contract",
                         Map.of(
                             "contractid",created.get("contractId".toString()),
                             "templateid",created.get("templateId").toString().split(":")[2],
                             "uuid",((Map)created.get("payload")).get("uuid").toString(),
                             "status",1,
                             "contractitem",new ObjectMapper().writeValueAsString(created),
                             "signatories",new ObjectMapper().writeValueAsString(created.get("signatories")),
                             "observers",new ObjectMapper().writeValueAsString(created.get("observers"))
                            ));
         } catch (JsonProcessingException e) {
         }

        // call daml
        //  async finalizeService(data: any)
        // 서비스 생성
        Exercise exercise = Exercise.builder()
                                .templateId("Service:ServiceProposal")
                                // .contractId(result.getResult().getExerciseResult())
                                // proposal을 통해 계약이 실행되야함
                                .contractId(findProposal.getResult().get(0).getContractId())
                                .choice("Finalize")
                                .argument(Map.of("signer", userid))
                                .build();
        oex.getIn().setHeader("Authorization", "Bearer " + TokenUtils.generateDamlTokenString(userid));
        // 다음 process에서 사용됨
        // oex.getIn().setHeader("invite_response", result);
        oex.getIn().setBody(exercise);
        // set headers
        oex.getIn().setHeader("dsl",dsl);
        return oex;
    }
}
