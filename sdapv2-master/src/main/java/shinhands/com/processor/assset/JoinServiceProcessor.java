package shinhands.com.processor.assset;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.camel.Exchange;
import org.apache.camel.FluentProducerTemplate;
import org.apache.camel.Processor;

import lombok.extern.slf4j.Slf4j;
import shinhands.com.beans.Ledger;
import shinhands.com.exception.ForbiddenError;
import shinhands.com.model.daml.DAMLExeciseResponse;
import shinhands.com.model.daml.DAMLQueryResponse;
import shinhands.com.model.daml.Exercise;
import shinhands.com.model.daml.template.Query;
import shinhands.com.model.hub.misc.asset.ServiceJoin;
import shinhands.com.model.request.UpdateContract;
import shinhands.com.rest.platform.hub.manage.Contract;

/**
 * [2021-09-13 11:06:13]
 *  - InsertUser, InsertAddress 합침
 */
@Slf4j
public class JoinServiceProcessor implements Processor {

    /**
     * TODO
     * [2021-09-13 11:03:05]
     * 생성자를 통해 FT,NFT,GiftCard 구분
     */
    public void process(Exchange ex) throws Exception {
        ServiceJoin body = ex.getIn().getBody(ServiceJoin.class);

        // Map<String, Object> dsl = ex.getIn().getHeader("dsl", Map.class);
        // String userid = dsl.get("userid").toString();
        // ServiceJoin payload = (ServiceJoin) dsl.get("service_join");
        Ledger ledger = new Ledger();
        // UUID에 해당하는 ERC 조회
        DAMLQueryResponse erc20 = ledger.getActiveContractsByQuery(ex,
                Query.builder()
                .templateIds(List.of("ERC20:ERC20"))
                .query(Map.of("uuid", body.getUuid()))
                .build());

        if (erc20.getStatus() != 200 || erc20.getResult().size() == 0) {
            throw new ForbiddenError("ERC 토큰이 존재하지 않습니다.");
        }
        // erc20 validate
        // 서비스를 요청한 사용자를 ERC 토큰에 항목에 추가한다.
        // Map.insert address balance balances
        // todo
        // [2021-09-14 13:42:19] 이미 추가된 사용자인경우 진행 불가, 서비스,ERC,Wallet 사용자 등록 일관성을 위해 계약서 변경 필요
        DAMLExeciseResponse insertUserResponse = ledger.execise(ex,
                Exercise.builder()
                    .templateId("ERC20:ERC20")
                    .contractId(erc20.getResult().get(0).getContractId())
                    .choice("InsertUser")
                    .argument(Map.of("userID", body.getUserid())).build());
        // 사용자의 지갑 주소를 등록한다.
        String newContractId = insertUserResponse.getResult().getExerciseResult();
        DAMLExeciseResponse inserUserWalletAddressResponse = ledger.execise(ex,
                Exercise.builder()
                    .templateId("ERC20:ERC20")
                    .contractId(newContractId)
                    .choice("InsertAddress")
                    .argument(Map.of("address", body.getAddress())).build());

        // DAML응답 DB 저장
        FluentProducerTemplate templatet = ex.getContext().createFluentProducerTemplate();
        direct(body.getUserid(),templatet,insertUserResponse,erc20);
        direct(body.getUserid(),templatet,inserUserWalletAddressResponse,erc20);
        ex.getIn().setBody(body);
    }

    private void direct(String user_id,FluentProducerTemplate templatet,
                                DAMLExeciseResponse response, DAMLQueryResponse token) throws JsonProcessingException {
        Map<String, Object> created = (Map) ((Map) response.getResult().getEvents().get(1)).get("created");

        UpdateContract uc = UpdateContract.builder()
                                .contractId(token.getResult().get(0).getContractId())
                                .templateId(created.get("templateId").toString().split(":")[2])
                                .contractItem(new ObjectMapper().writeValueAsString(created))
                                .build();
        // update
        Object u = templatet.withBody(uc)
                        .to("direct:update-contract")
                        .request(Object.class);
        // contract를 조회하고 상태를 덮어씌운후 신규 저장
        String contractId = token.getResult().get(0).getContractId();
        Object o =  templatet.withHeader("contractid",contractId)
                        .to("direct:get-contract")
                        .request();
        Map<String,String> body = new HashMap<String,String>();
        body.put("contractid", contractId);
        Map<String, Object> c = templatet.withBody(body)
                                    .withHeader("contractid",contractId )
                                    .to("direct:get-contract")
                                    .request(Map.class);
        c.put("archivedid", c.get("contractid"));
        c.put("contractid", created.get("contractId").toString());
        c.put("status", 1);
        c.put("contractitem", new ObjectMapper().writeValueAsString(created));
        c.put("execiserchoice",
                new ObjectMapper().writeValueAsString(Map.of("choiceName", "InsertUser", "performer", user_id)));

        Object r = templatet.withHeader("dsl", c)
                        .to("direct:save-contract")
                        .request(Object.class);
        // log.info(u.toString(), r.toString());
        log.info(this.getClass().getName(),"저장 완료!");
    }
}