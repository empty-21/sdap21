package shinhands.com.beans;

import java.util.List;
import java.util.Map;

import org.apache.camel.Exchange;

import shinhands.com.model.daml.DAMLQueryResponse;
import shinhands.com.model.daml.template.DefaultDAMLResult;
import shinhands.com.model.daml.template.Query;

public class ServiceUtil {
    private static String operator = "dap_operator";

    private ServiceUtil() {
    }

    public static DefaultDAMLResult service(Exchange ex, String uuid, String queryOwner) {
        DAMLQueryResponse response = new Ledger().getActiveContractsByQuery(ex, Query.builder()
                                                .templateIds(List.of("Service:Service"))
                                                .query(Map.of("uuid",uuid)).build(),
                                            queryOwner);

        if (response.getStatus() == 200 && !response.getResult().isEmpty()) {
            return response.getResult().get(0);
        } else {
            return null;
        }
    }

    public static DefaultDAMLResult service(Exchange ex, String uuid) {
        return ServiceUtil.service(ex, uuid, ServiceUtil.operator);
    }

    public static Boolean exist(Exchange ex, String uuid, String queryOwner) {
        return ServiceUtil.service(ex, uuid, queryOwner) != null;
    }

    public static Boolean exist(Exchange ex, String uuid) {
        return ServiceUtil.service(ex, uuid) != null;
    }
}
