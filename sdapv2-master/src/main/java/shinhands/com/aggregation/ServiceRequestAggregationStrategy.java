package shinhands.com.aggregation;

import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.camel.AggregationStrategy;
import org.apache.camel.Exchange;

import shinhands.com.enumeration.Service;
import shinhands.com.model.besu.DeployERC20Payload;
import shinhands.com.model.daml.DAMLResponse;
import shinhands.com.model.daml.template.erc20.ERC20Payload;
import shinhands.com.model.request.ServiceProposalPayload;

public class ServiceRequestAggregationStrategy implements AggregationStrategy {

    /**
     * TODO
     * [2021-08-31 14:29:29]
     *  - DAML내 서비스와 ERC Token 생성이 완료됨
     *  - Besu를 통해 Token을 생성하고 BlockChain Address가 생성되면 해당 DAML 서비스에 BlockChain Address를 등록
     *  - Besu응답이 undefined 또는 0x0인경우 오류, 이제까지 작성된 계약서를 Archive한다. (damlbiz, service.digitalasset.manage.ts)
     */
    public Exchange aggregate(Exchange oex, Exchange nex) {

        // error casting
        // duplicate
        Map<String,Object> dsl = oex.getIn().getHeader("dsl", Map.class);
        String parentid = dsl.get("parentid").toString();
        // - 서비스 종류 구분자
        Boolean isErc20 = dsl.get("service_type").toString().toUpperCase().equals(Service.Type.FT.toString());

        DAMLResponse result = nex.getIn().getBody(DAMLResponse.class);

        try {
            dsl.put("contract",
                Map.of(
                    "contractid",result.getResult().getContractId(),
                    "templateid",result.getResult().getTemplateId().split(":")[2],
                    "uuid",result.getResult().getPayload().get("uuid").toString(),
                    "status",1,
                    "contractitem",new ObjectMapper().writeValueAsString(result.getResult().getPayload()),
                    "signatories",new ObjectMapper().writeValueAsString(result.getResult().getSignatories()),
                    "observers",new ObjectMapper().writeValueAsString(result.getResult().getObservers()),
                    "parentid",parentid
                    ));
        } catch (JsonProcessingException e) {

        }


        // [2021-08-31 14:54:39]


        if (isErc20) {
            // oex.getIn().getHeader(name, type)ERC20Payload
            ERC20Payload erc = (ERC20Payload) dsl.get("request_token");
             // Besu로 보낼 데이터
            DeployERC20Payload deploy = DeployERC20Payload.builder()
                                            .totalSupply(erc.getTotalSupply())
                                            // [2021-08-31 14:57:57]
                                            // - tokenName은 GiftCard에서만 사용됨
                                            .tokenName("ecr20")
                                            .decimals(erc.getDecimals())
                                            .burnable(erc.getBurnable())
                                            .mintable(erc.getMintable())
                                            .distributable(erc.getDistributable())
                                            .owner(erc.getOwner())
                                            .tokenSymbol(erc.getTokenSymbol())
                                            .build();
            oex.getIn().setBody(deploy);
        } else {
         // not yet
        }
        oex.getIn().setHeader("dsl",dsl);
        // oex.getIn().setHeader("created_erc_response", result);
        return oex;
    }
}
