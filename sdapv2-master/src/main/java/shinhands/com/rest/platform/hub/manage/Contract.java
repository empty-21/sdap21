package shinhands.com.rest.platform.hub.manage;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.core.MediaType;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jsonvalidator.JsonValidationException;

import shinhands.com.exception.ConflictError;
import shinhands.com.exception.DefaultException;
import shinhands.com.exception.UnauthorizedError;
import shinhands.com.processor.JwtVerfyProcessor;
import shinhands.com.processor.PagingResultProcessor;
import shinhands.com.processor.PagingValidatorProcessor;

@ApplicationScoped
public class Contract extends RouteBuilder {

    @Inject
    JwtVerfyProcessor jwtVerfyProcessor;

    @Override
    public void configure() {
        onException(Exception.class).handled(true).maximumRedeliveries(0).to("direct:common-500");
        onException(DefaultException.class).handled(true).maximumRedeliveries(0).to("direct:bad-response-200");
        onException(ConflictError.class).handled(true).maximumRedeliveries(0).to("direct:bad-response-200");
        onException(JsonValidationException.class).handled(true).maximumRedeliveries(0).to("direct:bad-jsonvalidator-200");
        onException(UnauthorizedError.class).handled(true).maximumRedeliveries(0).to("direct:bad-response-200");

        rest("/manage/contract")
            .consumes(MediaType.APPLICATION_JSON)
            .produces(MediaType.APPLICATION_JSON)
            .get("/").description("DAP-SIF39 (계약목록 조회)")
                .to("direct:manage-contract-list")
            .get("/detail").description("DAP-SIF40 (계약목록 상세조회)")
                .to("direct:manage-contract-detail");


        from("direct:manage-contract-list").routeId("manage:contract-list")
            .process(new PagingValidatorProcessor())
            .to("sql:classpath:sql/vue/manage/contract/contractList.sql?outputHeader=result")
            .to("sql:classpath:sql/vue/manage/contract/contractListRowsCount.sql?outputHeader=total_count&outputType=SelectOne")
            .process(new PagingResultProcessor())
            .to("direct:response-200");

        from("direct:manage-contract-detail").routeId("manage:contract-detail")
            // .process(jwtVerfyProcessor)
            .to("sql:classpath:sql/vue/manage/contract/contractDetail.sql?outputHeader=result&outputType=SelectOne")
            .setBody(simple("${headers.result}"))
            .to("direct:response-200");
    }
}