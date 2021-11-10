package shinhands.com.beans;

import java.util.List;
import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.FluentProducerTemplate;

import lombok.extern.slf4j.Slf4j;
import shinhands.com.exception.ConflictError;
import shinhands.com.model.daml.DAMLQueryResponse;
import shinhands.com.model.daml.DAMLResponse;
import shinhands.com.model.daml.Request;
import shinhands.com.model.daml.template.DefaultDAMLResult;
import shinhands.com.model.daml.template.Query;
import shinhands.com.model.repository.Contract;
import shinhands.com.model.repository.Transfer;
import shinhands.com.model.response.DamlResult;

@Slf4j
public class RepositoryHelper {
    private RepositoryHelper() {}
    //[2021-10-26 15:00:27]
    // type cating 안됨.. map형태만 가능한것같음
    // 응답값을 타입으로 변경필요
    public static Map<String,Object> wallet(Exchange ex, String walletAddress) {
        return ex.getContext()
                    .createFluentProducerTemplate()
                    .withHeader("dsl",Map.of("account_addr",walletAddress))
                    .to("direct:sql-get-wallet2")
                    .request(Map.class);
    }

    
    public static Map<String,Object> user(Exchange ex, String userId) {
        return ex.getContext()
                    .createFluentProducerTemplate()
                    .withHeader("dsl",Map.of("userid",userId))
                    .to("direct:sql-get-user2")
                    .request(Map.class);
    }

    /**
     * 거래 기록 저장
     * @param ex
     * @param transfer
     * @return
     */
    public static Object addTransferHistory(Exchange ex, Transfer transfer) {
        return ex.getContext()
                    .createFluentProducerTemplate()
                    .withHeader("dsl", transfer)
                    .to("direct:sql-save-transfer")
                    .request(Object.class);
    }

    public static Map<String,Object> updateContractAndGet(Exchange ex, Contract contract) {
        return ex.getContext()
                    .createFluentProducerTemplate()
                    .withHeader("dsl",contract)
                    .to("direct:sql-update-get-contract")
                    .request(Map.class);
    }
    public static Object addContract(Exchange ex, Contract contract) {
        return ex.getContext()
                    .createFluentProducerTemplate()
                    .withHeader("dsl", contract)
                    .to("direct:sql-save-contract")
                    .request(Object.class);  
    }
}
