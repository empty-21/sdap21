package shinhands.com.beans;

import java.util.List;
import java.util.Map;

import org.apache.camel.Exchange;

import shinhands.com.model.daml.DAMLQueryResponse;
import shinhands.com.model.daml.DAMLResponse;
import shinhands.com.model.daml.Request;
import shinhands.com.model.daml.template.DefaultDAMLResult;
import shinhands.com.model.daml.template.Query;

public class UserUtil {
    private UserUtil() {}
    public static DefaultDAMLResult get(Exchange ex, String userid) {
        DAMLQueryResponse user = new Ledger().getActiveContractsByQuery(ex, Query.builder()
                                                            .templateIds(List.of("User:User"))
                                                            .query(Map.of("userid",userid)
                                                        ).build(),
                                                        "dap_operator");
        if (user.getStatus() == 200 && !user.getResult().isEmpty()) {
            return user.getResult().get(0);
        } else {
            return null;
        }
    }

    public static DAMLResponse create(Exchange ex, String userid) {
        DAMLResponse result = new Ledger().create(ex,Request.builder()
                                        .templateId("User:User")
                                        .payload(Map.of("userid",userid,"operator","dap_operator"))
                                        .build()
                                        ,userid);
        if (result.getStatus() != 200) {
            return null;
        } else {
            return result;
        }
    }
}
