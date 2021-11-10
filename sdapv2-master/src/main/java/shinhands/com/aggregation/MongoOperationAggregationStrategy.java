package shinhands.com.aggregation;

import java.util.List;
import java.util.Map;

import org.apache.camel.AggregationStrategy;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MongoOperationAggregationStrategy implements AggregationStrategy {

    // [2021-05-10] oldExchange 가 500일때는 여기까지 안오겠지?
    public Exchange aggregate(Exchange oldExchange, Exchange resource) {
        // IErcPayload erc = (IErcPayload) oldExchange.getIn().getBody(Object.class);
        // BaseERCPayload payload = erc.getBaseERCPayload();
        // DAMLResponse response = resource.getMessage().getBody(DAMLResponse.class);
        // Map result = (Map) response.getResult();
        // String contractId = result.get("contractId").toString();
        // String parentId = payload.getParentId();
        // List signatories = (List) result.get("signatories");
        // List observers = (List) result.get("observers");
        // String templateId = result.get("templateId").toString().split(":")[2];
        // Number status = 1;
        // // Object contractItem = result;
        // String uuid = payload.getUuid();

        // SaveContract contract = new SaveContract();
        // contract.setContractId(contractId);
        // contract.setParentId(parentId);
        // contract.setSignatories(signatories);
        // contract.setObservers(observers);
        // contract.setTemplateId(templateId);
        // contract.setStatus(status);
        // contract.setContractItem(result);
        // contract.setUuid(uuid);
        // oldExchange.getMessage().setBody(contract);
        return oldExchange;
    }
}
