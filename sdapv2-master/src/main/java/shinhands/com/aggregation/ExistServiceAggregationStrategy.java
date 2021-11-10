package shinhands.com.aggregation;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.camel.AggregationStrategy;
import org.apache.camel.Exchange;

import lombok.extern.slf4j.Slf4j;
import shinhands.com.model.daml.DAMLResponse;
import shinhands.com.model.daml.template.wallet.Query;
import shinhands.com.model.message.DAMLResult;
import shinhands.com.model.message.ResultDAMLResponse;

@Slf4j
public class ExistServiceAggregationStrategy implements AggregationStrategy {

    private String fromRouteId;

    // [2021-05-08] 더 좋은방법은 없는듯
    public ExistServiceAggregationStrategy(String fromRouteId) {
        super();
        this.fromRouteId = fromRouteId;
    }

    public Exchange aggregate(Exchange oldExchange, Exchange resource) {
        // [2021-05-08] 응답값의 최상위 객체로 캐스팅함
        ResultDAMLResponse result0 = resource.getIn().getBody(ResultDAMLResponse.class);
        DAMLResult result = result0.getResult();
        DAMLResponse DAMLResponse = result.getData();

        // [2021-05-08] 더 좋은 방법?
        Object payload0 = oldExchange.getProperty("$payload", Object.class);
        Map<String, String> payload = new ObjectMapper().convertValue(payload0, Map.class);
        oldExchange.getMessage().setBody(payload);

        if (fromRouteId.equals("direct:getService")) {
            Boolean isExistService = false;
            if (result.getCode() == 0 && DAMLResponse.getStatus() == 200) {

                Map<String, Object> headers = oldExchange.getProperty("$headers", Map.class);
                // [2021-05-08] exist header 정보가 overwrite 되지 않도록 주의
                oldExchange.getMessage().setHeaders(headers);

                isExistService = !((List<Object>) DAMLResponse.getResult()).isEmpty();
                oldExchange.getMessage().setHeader("isExistService", isExistService);
            }
            // [2021-05-08] getWallet 을 위한 body 설정을 여기에서 해야하나? (process 따로 분리?)
            // owner
            String owner = payload.get("owner");
            // 지갑주소
            String address = payload.get("address");
            Query query = new Query(owner, address);
            oldExchange.getMessage().setBody(query);

        } else if (fromRouteId.equals("direct:getWallet")) {
            // [2021-05-08] route 에서도 동일한 로직이 있어서 혼란이 있음
            // oldExchange.getMessage().setBody(payload);
            List<Map<String, Object>> wallets = (List<Map<String, Object>>) DAMLResponse.getResult();
            Boolean isExistWallet = !wallets.isEmpty();
            oldExchange.getMessage().setHeader("isExistWallet", isExistWallet);
            if (isExistWallet) {
                String contractId = (String) wallets.get(0).get("contractId");
                oldExchange.getMessage().setHeader("walletContractId", contractId);
            }
        } else if (fromRouteId.equals("direct:getUser")) {
            // oldExchange.getMessage().setBody(payload);
            List<Map<String, Object>> users = (List<Map<String, Object>>) DAMLResponse.getResult();
            Boolean isExistUser = !users.isEmpty();
            oldExchange.getMessage().setHeader("isExistUser", isExistUser);

            if (isExistUser) {
                String contractId = (String) users.get(0).get("contractId");
                oldExchange.getMessage().setHeader("userContractId", contractId);
            }
        } else {
            // throw
        }

        return oldExchange;
    }
}
