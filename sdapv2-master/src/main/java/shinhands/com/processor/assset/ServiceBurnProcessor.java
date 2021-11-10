package shinhands.com.processor.assset;

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

@Slf4j
public class ServiceBurnProcessor implements Processor {

    public void process(Exchange ex) throws Exception {
    //     Ledger ledger = new Ledger();
    //     ObjectMapper om = new ObjectMapper();
    //     String userid = (String) ex.getIn().getHeader("userid");
    //     MintBurnPayload body = ex.getMessage().getBody(MintBurnPayload.class);
    //     DefaultDAMLResult service = ServiceUtil.service(ex,body.getUuid(),userid);
    //     if (service == null) {
    //         throw new ForbiddenError(body.getUuid() + "서비스가 존재하지 않습니다");
    //     }
    //     if (service.getPayload().get("serviceType").equals("FT")) {
    //         throw new ForbiddenError("burn 기능은 FT토큰에서만 사용가능합니다.");
    //     };

    //     DefaultDAMLResult token = ErcUtil.erc(ex, "ERC20", body.getUuid());
    //     if (token == null) {
    //         throw new ForbiddenError("ERC 토큰이 존재하지 않습니다.");                        
    //     } else {
    //         // [2021-10-27 13:00:21]
    //         // operator의 고유권한인지 issuer도 가능한지 확인 필요함 (현재 issuer도 가능함)
    //         // erc token burn후 service정보도 같이 갱신(burn)
    //         DAMLExeciseResponse burnToServiceExecise = ledger.execise(ex,
    //                                                         Exercise.builder()
    //                                                             .templateId("Service:Service")
    //                                                             .contractId(service.getContractId())
    //                                                             .choice("BurnERC20")
    //                                                             .argument(Map.of(
    //                                                                 "userid",userid,
    //                                                                 "address",body.getAddress(),
    //                                                                 "amount",body.getAmount()
    //                                                             )).build()
    //                                                             ,userid);
    //         if (burnToServiceExecise.getStatus() != 200) {
    //             // throw
    //         }
    //         Map<String,Object> eventItem = (Map<String,Object>) burnToServiceExecise.getResult().getEvents().get(1);

    //         try {
    //             String created = om.writeValueAsString(eventItem.get("created"));
    //             String templateId = token.getTemplateId().split(":")[2];
    //             Contract contract = Contract.builder()
    //                                     .contractId(token.getContractId())
    //                                     .templateId(templateId)
    //                                     .status(0)
    //                                     .contractItem(created)
    //                                     .execiserChoice(om.writeValueAsString(Map.of("choiceName", "Burn","performer", userid)))
    //                                     .build();
                                        
    //             Map<String,Object> ug = RepositoryHelper.updateContractAndGet(ex,contract);

    //             contract = Contract.builder()
    //                         .contractId(burnExecise.getResult().getExerciseResult())
    //                         .archivedId(ug.get("contractid").toString())
    //                         .parentId(ug.get("parentid").toString())
    //                         .uuid(body.getUuid())
    //                         .signatories(ug.get("signatories").toString())
    //                         .observers(ug.get("observers").toString())
    //                         .templateId(templateId)
    //                         .status(1)
    //                         .contractItem(created)
    //                         .execiserChoice(om.writeValueAsString(Map.of("choiceName", "Burn","performer", userid)))
    //                         .build();
            
    //             Object newContractId = RepositoryHelper.addContract(ex,contract);
                
    //             ex.getIn().setBody(eventItem.get("created"));
    //         } catch (JsonProcessingException e) {
    //             // stop!
    //             e.printStackTrace();
    //         }
    //     }
    }
}