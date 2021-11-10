package shinhands.com.aggregation;

import java.util.List;
import java.util.Map;

// import da.platform.model.daml.template.wallet.Payload;
// import da.platform.model.daml.template.wallet.Payload;
// import da.platform.model.daml.DAMLResponse;
// import da.platform.model.daml.template.wallet.Payload;
import org.apache.camel.AggregationStrategy;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import shinhands.com.model.daml.DAMLResponse;
import shinhands.com.model.daml.template.wallet.Payload;

public class WalletAggregationStrategy implements AggregationStrategy {

    private static final Logger logger = LoggerFactory.getLogger(WalletAggregationStrategy.class);

    /**
     * TODO Wallet상태 정보 관리
     */
    public Exchange aggregate(Exchange oldExchange, Exchange resource) {
        DAMLResponse DAMLResponse = resource.getMessage().getBody(DAMLResponse.class);
        List wallets = (List) DAMLResponse.getResult();
        Payload payload = oldExchange.getMessage().getHeader("original_payload", Payload.class);
        Map<String, Object> headers = oldExchange.getMessage().getHeader("original_headers", Map.class);
        oldExchange.getMessage().setHeaders(headers);
        oldExchange.getMessage().setBody(payload);
        oldExchange.getMessage().setHeader("wallets", wallets.size());
        return oldExchange;
    }
}
