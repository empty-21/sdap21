package shinhands.com.processor.assset;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import lombok.extern.slf4j.Slf4j;
import shinhands.com.beans.ErcUtil;
import shinhands.com.beans.Ledger;
import shinhands.com.beans.RepositoryHelper;
import shinhands.com.beans.ServiceUtil;
import shinhands.com.exception.ForbiddenError;
import shinhands.com.model.daml.DAMLExeciseResponse;
import shinhands.com.model.daml.Exercise;
import shinhands.com.model.daml.template.DefaultDAMLResult;
import shinhands.com.model.repository.Contract;
import shinhands.com.model.request.MintBurnPayload;
import shinhands.com.model.besu.EncodDataPayload;

@Slf4j
public class ERCBurnProcessor implements Processor {
    
    private Boolean burn(Exchange ex,Exercise exercise, DefaultDAMLResult token, String owner) {
        ObjectMapper om = new ObjectMapper();
        String uuid = token.getPayload().get("uuid").toString();
        DAMLExeciseResponse burnExecise = new Ledger().execise(ex,exercise);
        if (burnExecise.getStatus() != 200) {
        // throw
        }
        Map<String,Object> event = (Map<String,Object>) burnExecise.getResult().getEvents().get(1);

        // [2021-10-28 10:32:02] TODO
        // 공통 Helper Class 구현필요 
        try {
            String created = om.writeValueAsString(event.get("created"));
            String templateId = token.getTemplateId().split(":")[2];
            Contract contract = Contract.builder()
                                    .contractId(token.getContractId())
                                    .templateId(templateId)
                                    .status(0)
                                    .contractItem(created)
                                    .execiserChoice(om.writeValueAsString(
                                            Map.of("choiceName",exercise.getChoice(),"performer", owner)
                                        ))
                                    .build();

            Map<String,Object> ug = RepositoryHelper.updateContractAndGet(ex,contract);

            contract = Contract.builder()
                            .contractId(burnExecise.getResult().getExerciseResult())
                            .archivedId(ug.get("contractid").toString())
                            .parentId(ug.get("parentid").toString())
                            .uuid(uuid)
                            .signatories(ug.get("signatories").toString())
                            .observers(ug.get("observers").toString())
                            .templateId(templateId)
                            .status(1)
                            .contractItem(created)
                            .execiserChoice(om.writeValueAsString(
                                Map.of("choiceName",exercise.getChoice(),"performer", owner)
                                ))
                            .build();

            // Object newContractId = RepositoryHelper.addContract(ex,contract);
            Boolean success = RepositoryHelper.addContract(ex,contract) != null;
            if (!success) {
                // TODO
                // [2021-10-28 10:57:01]
                // 계약서는 완료됐고 DB가 실패하면 어떻게해야할까?
            }
            return success;
        } catch (JsonProcessingException e) {
            // stop!
            e.printStackTrace();
            return null;
        }
    }

    public void process(Exchange ex) throws Exception {
        
        String userid = (String) ex.getIn().getHeader("userid");
        MintBurnPayload body = ex.getIn().getBody(MintBurnPayload.class);
        DefaultDAMLResult service = ServiceUtil.service(ex,body.getUuid(),userid);
        if (service == null) {
            throw new ForbiddenError(body.getUuid() + "서비스가 존재하지 않습니다");
        }
        if (!service.getPayload().get("serviceType").equals("FT")) {
            throw new ForbiddenError("burn 기능은 FT토큰에서만 사용가능합니다.");
        };
        
        DefaultDAMLResult token = ErcUtil.erc(ex, "ERC20", body.getUuid());
        if (token == null) {
            throw new ForbiddenError("ERC 토큰이 존재하지 않습니다.");                        
        } else {
            // [2021-10-27 13:00:21]
            // operator의 고유권한인지 issuer도 가능한지 확인 필요함 (현재 issuer도 가능함)
            Exercise exercise = Exercise.builder()
                                    .templateId("ERC20:ERC20")
                                    .contractId(token.getContractId())
                                    .choice("Burn")
                                    .argument(Map.of(
                                        "userID",userid,
                                        "address",body.getAddress(),
                                        "amount",body.getAmount()
                                    )).build();
            Boolean success = burn(ex,exercise,token,userid);
           
            // [2021-10-28 11:06:57]
            // erc와 serice는 개별 계약이므로 각각의 계약서를 독립적으로 갱신함
            // TODO
            // 계약서 2개에 대한 동시성 및 신뢰성을 어떻게 보장할 수 있나? (항상 둘다 동시에 성공또는 실패가 되어야함)
            exercise = Exercise.builder()
                                    .templateId("Service:Service")
                                    .contractId(service.getContractId())
                                    .choice("BurnERC20")
                                    .argument(Map.of(
                                        "userID",userid,
                                        "address",body.getAddress(),
                                        "amount",body.getAmount()
                                    )).build();
            success = burn(ex,exercise,service,userid);

            List<List> attributes = (List<List>) service.getPayload().get("attribute");
            Boolean dividable = ErcUtil.findAttributes(attributes,"dividable");
            // //  if (distributable.get() == 0) {
            // throw new ForbiddenError("distributable 속성이 없는 토큰입니다.");
        // } 
            // TODO
            // [2021-10-28 11:06:34]
            // FTTokenController.ts:243
            // service에서 하위 FT 항목을 탐색하는 과정 필수여부 확인필요함
            EncodDataPayload edpl = EncodDataPayload.builder()
                                        .contractAddress(token.getPayload().get("blockchainAddr").toString())
                                        .amount(body.getAmount())
                                        .dividable(dividable)
                                        .build();

            ex.getIn().setHeader("dsl_to_besu", edpl);
        }
    }
}