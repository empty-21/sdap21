package shinhands.com.rest.platform.hub.manage;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.core.MediaType;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jsonvalidator.JsonValidationException;

import shinhands.com.exception.ConflictError;
import shinhands.com.exception.DefaultException;
import shinhands.com.exception.UnauthorizedError;
import shinhands.com.processor.PagingResultProcessor;
import shinhands.com.processor.PagingValidatorProcessor;

@ApplicationScoped
public class Audit extends RouteBuilder {

    @Override
    public void configure() {
        onException(Exception.class).handled(true).maximumRedeliveries(0).to("direct:common-500");
        onException(DefaultException.class).handled(true).maximumRedeliveries(0).to("direct:bad-response-200");
        onException(ConflictError.class).handled(true).maximumRedeliveries(0).to("direct:bad-response-200");
        onException(JsonValidationException.class).handled(true).maximumRedeliveries(0).to("direct:bad-jsonvalidator-200");
        onException(UnauthorizedError.class).handled(true).maximumRedeliveries(0).to("direct:bad-response-200");

        rest("/manage/audit")
            .consumes(MediaType.APPLICATION_JSON)
            .produces(MediaType.APPLICATION_JSON)
            .get("/").description("DAP-SIF015 목록 조회")
                .to("direct:manage-audit-list")
            // TODO
            // [2021-08-24 13:22:16] 구현
            .post("/").description("Audit 등록")
                .to("direct:manage-audit-add");
            
        from("direct:manage-audit-list").routeId("manage:audit-list")
            .process(new PagingValidatorProcessor())
            .to("sql:classpath:sql/vue/manage/audit/auditList.sql?outputHeader=result")
            .to("sql:classpath:sql/vue/manage/audit/auditListRowsCount.sql?outputHeader=total_count&outputType=SelectOne")
            .process(new PagingResultProcessor())
            .to("direct:response-200");

        from("direct:manage-audit-add").routeId("manage:audit-add")
            // .marshal().json(JsonLibrary.Jackson)
            // .to("sql:classpath:sql/vue/manage/audit/auditAdd.sql?outputHeader=result")
            .to("direct:response-success-200");
    }
}
