package shinhands.com.processor.proposal;

import java.util.HashMap;
import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import shinhands.com.model.daml.Request;
import shinhands.com.model.request.ServiceProposalPayload;
import shinhands.com.util.TokenUtils;

public class ServiceRequestProcessor implements Processor {

    public void process(Exchange ex) throws Exception {
        String userid = (String) ex.getIn().getHeader("userid");
        // [2021-08-26 08:01:55
        // inviteService 에서는 signatories 항목만 필요함
        // daml에서 요구되는 항목이 초과되더라도 문제 없음, 장기적으로 daml전용 model객체 구현 필요
        // String t = TokenUtils.generateDamlTokenString("operator");

        // TODO [2021-08-30 17:21:57]
        // 1. choice를 실행하기 위해서는 issuer와 userid는 필수적으로 같아햐함.동일하지 않은경우 오류 처리필요
        // 2. daml에서도 issuer와 userid가 동일하지 않은경우 권한 오류가 아닌 contractid not found가 됨
        // 3. issuer가 userid가 아닌경우가 있는지 분석필욯
        ServiceProposalPayload proposal = ex.getMessage().getBody(ServiceProposalPayload.class);
        // TODO [2021-08-30 17:24:33]
        // 지금은 이렇게..
        proposal.setIssuer(userid);
        // if (payload.getIssuer().isEmpty()) {
        //     payload.setIssuer(userid);
        // }
        // [2021-08-30 18:03:28]
        // service에서는 signatories 사용하지 않음
        // inviteService에서 signatories사용함..
        // ex.getIn().setHeader("inapp", Map.of("signatories",payload.getSignatories()));
        // payload.setSignatories(List.of());

        if (proposal.getOwner().isEmpty()) {
            proposal.setOwner(userid);
        }
        //
        Request request = Request.builder()
                        .templateId("Service:ServiceRequest")
                        .payload(proposal)
                        .build();
        ex.getIn().setBody(request);
        ex.getIn().setHeader("Authorization", "Bearer " + TokenUtils.generateDamlTokenString(proposal.getIssuer()));
        // ex.getIn().setHeader("proposal",payload);
        // ex.getIn().setHeader();

        Map<String,Object> dsl = new HashMap<>();
        dsl.put("signatories",proposal.getSignatories());
        dsl.put("proposal",proposal);
        dsl.put("service_type",proposal.getServiceType());
        dsl.put("userid",userid);
        ex.getIn().setHeader("dsl",dsl);
    }

}