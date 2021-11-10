package shinhands.com.aggregation;

import java.util.List;
import java.util.Map;

import org.apache.camel.AggregationStrategy;
import org.apache.camel.Exchange;
import org.apache.camel.FluentProducerTemplate;
import org.apache.camel.Produce;

import lombok.extern.slf4j.Slf4j;
import shinhands.com.model.daml.DAMLQueryResponse;
import shinhands.com.model.daml.template.Query;
import shinhands.com.model.request.ServiceProposalPayload;
import shinhands.com.util.TokenUtils;

@Slf4j
public class JoinServiceAggregationStrategy implements AggregationStrategy {

    // @Produce("direct:restful-daml-query-contract")
    // FluentProducerTemplate fluentProducerTemplate;
    /**
     * 지갑 서비스 등록
     */
    public Exchange aggregate(Exchange oex, Exchange nex) {
        ServiceProposalPayload proposal = (ServiceProposalPayload) oex.getIn().getHeader("proposal");
        // daml에서 token생성후 besu에서 contract생성 결과
        final String besuContractAddress = nex.getIn().getBody(String.class);
        /**
         * TODO
         * [2021-08-31 17:52:09]
         *  - Model 사용
         */
        Map<String,Object> body = Map.of(
                                    "operator",proposal.getOperator(),
                                    "owner", proposal.getOwner(),
                                    "tokenSymbol", proposal.getTokenSymbol(),
                                    "serviceName", proposal.getServiceName(),
                                    "address", proposal.getOwnerAddress(),
                                    "serviceType", proposal.getServiceType(),
                                    "blockchainAddr", besuContractAddress,
                                    "uuid",proposal.getUuid()
                                    );
        // 서비스 조회
        DAMLQueryResponse res = nex.getContext().createFluentProducerTemplate()
                                    .withHeader("Content-Type", "application/json")
                                    .withHeader("Authorization", "Bearer "+TokenUtils.generateDamlTokenString(proposal.getOwner()))
                                    .withBody(Query.builder()
                                                .templateIds(List.of("Service:Service"))
                                                .query(Map.of(
                                                    "operator",proposal.getOperator(),
                                                    "uuid",proposal.getUuid()
                                                ))
                                                .build())
                                    .to("direct:restful-daml-query-contract")
                                    .request(DAMLQueryResponse.class);
        log.info("DAMLQueryResponse Result size =>", res.getResult().size());
        oex.getIn().setHeader("besu_created_contract_address", besuContractAddress);
        oex.getIn().setBody(body);
        return oex;
    }
}
