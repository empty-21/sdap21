package shinhands.com.aggregation;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.enterprise.context.ApplicationScoped;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.camel.AggregationStrategy;
import org.apache.camel.Exchange;
import org.springframework.beans.BeanUtils;

import shinhands.com.beans.Ledger;
import shinhands.com.model.daml.DAMLExeciseResponse;
import shinhands.com.model.daml.DAMLQueryResponse;
import shinhands.com.model.daml.Request;
import shinhands.com.model.daml.template.BaseERCPayload;
import shinhands.com.model.daml.template.DefaultDAMLResult;
import shinhands.com.model.daml.template.Query;
import shinhands.com.model.daml.template.erc20.ERC20Payload;
import shinhands.com.model.daml.template.erc721.ERC721Payload;
import shinhands.com.model.request.ServiceProposalPayload;

@ApplicationScoped
public class ServiceFinalizeAggregationStrategy implements AggregationStrategy {

    public Exchange aggregate(Exchange oex, Exchange nex) {

        // invite 결과 저장
        // damlResult.data.result.events[0].created.contractId,
        Map<String,Object> dsl = oex.getIn().getHeader("dsl", Map.class);
        DAMLQueryResponse findProposal = (DAMLQueryResponse) dsl.get("proposal_in_daml");

        DefaultDAMLResult damlResult = (DefaultDAMLResult) findProposal.getResult().get(0);
        ServiceProposalPayload proposal = new ObjectMapper().convertValue (damlResult.getPayload(), ServiceProposalPayload.class);

        Boolean isErc20 = proposal.getServiceType().toLowerCase().equals("ft");
        String templateId = isErc20 ? "ERC20:ERC20" : "ERC721:ERC721";

        // ERC20 생성
        List<Object> attrList = (List<Object>) proposal.getAttribute();
        HashMap<String, Boolean> attr = new HashMap<>();

        for (int i = 0; i < attrList.size(); i++) {
            List attr1 = (List) attrList.get(i);
            attr.put((String)attr1.get(0), (Boolean)attr1.get(1));
        }

        Boolean pausable = attr.containsKey("pausable") ? attr.get("pausable") : Boolean.FALSE;
        Boolean burnable = attr.containsKey("burnable") ? attr.get("burnable") : Boolean.FALSE;
        Boolean mintable = attr.containsKey("mintable") ? attr.get("mintable") : Boolean.FALSE;
        Boolean distributable = attr.containsKey("distributable") ? attr.get("distributable") : Boolean.FALSE;
        Boolean dividable = attr.containsKey("dividable") ? attr.get("dividable") : Boolean.FALSE;
        Boolean delegable = attr.containsKey("delegable") ? attr.get("delegable") : Boolean.FALSE;

        List<String> users = new ArrayList<>();
        List<List<Object>> balances = new ArrayList<>();
        Map<String,Object> service = (Map<String,Object>) proposal.getService();

        if (proposal.getOwner().equals(proposal.getIssuer())) {
            // totalSupply를 proposal에 넣지않는이유?
            balances.add(List.of(proposal.getOwnerAddress(),service.get("totalSupply")));
            users.add(proposal.getOwner());
        } else {
            balances.add(List.of(proposal.getOwnerAddress(),service.get("totalSupply")));
            balances.add(List.of(proposal.getIssuerAddress(),0));
            users.add(proposal.getOwner());
            users.add(proposal.getIssuer());
        }

        // shinhands.com.model.daml.template.erc721.Payload payload =
        // shinhands.com.model.daml.template.erc721.Payload.builder().
        BaseERCPayload erc = BaseERCPayload.builder()
                                .pausable(pausable)
                                .burnable(burnable)
                                .mintable(mintable)
                                .distributable(distributable)
                                .dividable(dividable)
                                .category(proposal.getCategory())
                                .delegable(delegable)
                                .decimals(dividable ? 18:0)
                                .issuer(proposal.getIssuer())
                                .issuerAddress(proposal.getIssuerAddress())
                                .operator(proposal.getOperator())
                                .owner(proposal.getOwner())
                                .ownerAddress(proposal.getOwnerAddress())
                                .serviceName(proposal.getServiceName())
                                .symbolImage(proposal.getSymbolImage())
                                .tokenSymbol(proposal.getTokenSymbol())
                                // .totalSupply(proposal.getTotalSupply())
                                .uuid(proposal.getUuid())
                                .blockchainAddr(UUID.randomUUID().toString())

                                .lockedUserIDs(List.of())
                                .observers(List.of())
                                // .balances(balances)
                                .userIDs(users)
                                .build();

        Object token;
        if (isErc20) {
            token = ERC20Payload.builder()
                        .balances(balances)
                        .totalSupply(new BigDecimal(service.get("totalSupply").toString()))
                        .build();
        } else {
            token = ERC721Payload.builder()
                        .baseURI("baseURI")
                        .build();
        }
        BeanUtils.copyProperties(erc, token);
        Request request = Request.builder()
                                .templateId(templateId)
                                .payload(token)
                                .build();
        oex.getIn().setBody(request);

        /**
         * [2021-08-31 14:49:58]
         *  - ServiceRequestAggregationStrategy에서 재활용
         */
        // oex.getIn().setHeader("request_token", );
        dsl.put("request_token", token);

        // oex.getIn().setHeader("finalize_response", nex.getIn().getBody(DAMLExeciseResponse.class));

        // parentid용 service조회
        DAMLQueryResponse service1 = new Ledger().getActiveContractsByQuery(nex, Query.builder()
                                            .templateIds(List.of("Service:Service"))
                                            .query(Map.of("uuid",proposal.getUuid())).build());
        // erc가 정상적으로 처리되면 다음 process에서 parentid값을 참조한다.
        String parentid = service1.getResult().get(0).getContractId();
        // oex.getIn().setHeader("parentid",parentid);
        dsl.put("parentid", parentid);

        /**
         * [2021-08-30 10:32:18] jsonb type
         * [2021-09-07 10:19:18] digitalAssets.ts:264
         */
        try {
            // DB Insert
            DAMLExeciseResponse finalize = nex.getIn().getBody(DAMLExeciseResponse.class);
            // DB 저장용, 마지막에 하는게 좋은가?
            Map<String,Object> archived = (Map) ((Map) finalize.getResult().getEvents().get(0)).get("archived");
            Map<String,Object> created = (Map) ((Map) finalize.getResult().getEvents().get(1)).get("created");
            dsl.put("contract",
                    Map.of(
                            "contractid",created.get("contractId").toString(),
                            "parentid",archived.get("contractId").toString(),
                            "templateid",created.get("templateId").toString().split(":")[2],
                            "uuid",((Map)created.get("payload")).get("uuid").toString(),
                            "status",1,
                            "contractitem",new ObjectMapper().writeValueAsString(created),
                            "signatories",new ObjectMapper().writeValueAsString(created.get("signatories")),
                            "observers",new ObjectMapper().writeValueAsString(created.get("observers"))
                        ));

            // DB Update
            String proposalItem = new ObjectMapper().writeValueAsString(findProposal.getResult().get(0));
            String choice = new ObjectMapper().writeValueAsString(
                            Map.of("choiceName","Finalize",
                                    "performer",proposal.getIssuer()));
            // oex.getIn().setHeader("update_contract",
            //     Map.of(
            //         "contractid",findProposal.getResult().get(0).getContractId(),
            //         "execiserchoice",choice,
            //         "status",0,
            //         "contractitem",proposalItem
            //     )
            // );
            dsl.put("update_contract", Map.of(
                    "contractid",findProposal.getResult().get(0).getContractId(),
                    "execiserchoice",choice,
                    "status",0,
                    "contractitem",proposalItem
                ));
        } catch (JsonProcessingException e) {
            // error
        }
        oex.getIn().setHeader("dsl",dsl);
        return oex;
    }
}
