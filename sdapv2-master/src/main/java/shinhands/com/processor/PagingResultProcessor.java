package shinhands.com.processor;

import java.util.HashMap;
import java.util.Map;

import javax.enterprise.context.RequestScoped;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

@RequestScoped
public class PagingResultProcessor implements Processor {

    public void process(Exchange ex) throws Exception {
        Map<String, Object> result = ex.getIn().getHeaders();
        Map<String, Object> body = new HashMap<>();
        body.put("total_count", result.get("total_count"));
        body.put("rows", result.get("result"));
        body.put("offset", result.get("page"));
        // body.put("limit", result.get("rows"));
        ex.getIn().setBody(body);
    }
}