package shinhands.com.beans;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.camel.Exchange;

import shinhands.com.model.daml.DAMLQueryResponse;
import shinhands.com.model.daml.template.DefaultDAMLResult;
import shinhands.com.model.daml.template.Query;

public class ErcUtil {
    private static String operator = "dap_operator";

    private ErcUtil() {
    }

    public static DefaultDAMLResult erc(Exchange ex, String uuid, String type, String queryOwner) {
        String TemplateId = type == "ERC20" ? "ERC20:ERC20" : "ERC721:ERC721";
        
        DAMLQueryResponse response = new Ledger().getActiveContractsByQuery(ex, Query.builder()
                                                .templateIds(List.of(TemplateId))
                                                .query(Map.of("uuid",uuid)).build(),
                                            queryOwner);

        if (response.getStatus() == 200 && !response.getResult().isEmpty()) {
            return response.getResult().get(0);
        } else {
            return null;
        }
    }

    public static DefaultDAMLResult erc(Exchange ex, String type, String uuid) {
        return ErcUtil.erc(ex, uuid, type, ErcUtil.operator);
    }

    public static Boolean exist(Exchange ex, String uuid, String type, String queryOwner) {
        return ErcUtil.erc(ex, uuid, type, queryOwner) != null;
    }

    public static Boolean exist(Exchange ex, String type, String uuid) {
        return ErcUtil.erc(ex, uuid, type) != null;
    }

    public static Boolean findAttributes(List<List> attributes, String attribute) {
        return findAttributes(attributes,attribute, true);
    }

    public static Boolean findAttributes(List<List> attributes, String attribute, Boolean status) {
        AtomicInteger ai = new AtomicInteger(0);
        attributes.forEach(atts -> {
            if (ai.get() == 0) {
                if (atts.get(0).equals(attribute) && atts.get(1).equals(status)) {
                    ai.getAndIncrement();
                }
            }
        });
        return ai.get() > 0;
    }
}
