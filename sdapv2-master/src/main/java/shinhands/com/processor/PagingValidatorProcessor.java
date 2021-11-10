package shinhands.com.processor;

import javax.enterprise.context.RequestScoped;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

@RequestScoped
public class PagingValidatorProcessor implements Processor {

    public void process(Exchange ex) throws Exception {
        Integer page = ex.getIn().getHeader("page", Integer.class);
        Integer rows = ex.getIn().getHeader("rows", Integer.class);
        // String role = ex.getIn().getHeader("role", String.class);
        if (page != null && page > 0) {
            page -= page;
        } else {
            page = 0;
        }

        if (rows != null && rows > 100) {
            rows = 30;
        } else {
            rows = 30;
        }
        ex.getIn().setHeader("rows", rows);
        ex.getIn().setHeader("page", page);
        // if (role != null) {
        //     ex.getIn().setHeader("role", role);
        // }
    }
}