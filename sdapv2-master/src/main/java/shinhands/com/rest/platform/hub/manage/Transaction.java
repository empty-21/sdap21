package shinhands.com.rest.platform.hub.manage;

import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.core.MediaType;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jsonvalidator.JsonValidationException;
import org.apache.camel.model.dataformat.JsonLibrary;

import shinhands.com.exception.ConflictError;
import shinhands.com.exception.DefaultException;
import shinhands.com.exception.UnauthorizedError;
import shinhands.com.processor.JwtVerfyProcessor;

@ApplicationScoped
public class Transaction extends RouteBuilder {

    @Inject
    JwtVerfyProcessor jwtVerfyProcessor;

    @Override
    public void configure() {
        onException(Exception.class).handled(true).maximumRedeliveries(0).to("direct:common-500");
        onException(DefaultException.class).handled(true).maximumRedeliveries(0).to("direct:bad-response-200");
        onException(ConflictError.class).handled(true).maximumRedeliveries(0).to("direct:bad-response-200");
        onException(JsonValidationException.class).handled(true).maximumRedeliveries(0).to("direct:bad-jsonvalidator-200");
        onException(UnauthorizedError.class).handled(true).maximumRedeliveries(0).to("direct:bad-response-200");

        rest("/manage/receipt")
            .consumes(MediaType.APPLICATION_JSON)
            .produces(MediaType.APPLICATION_JSON)
            .get("/").description("transaction hash 가져오기")
                .to("direct:manage-transaction-list");

        from("direct:manage-transaction-list").routeId("manage:transaction-list")
            .marshal().json(JsonLibrary.Jackson)
            .to("json-validator:json-schema/transaction-schema.json")
            .unmarshal().json(JsonLibrary.Jackson, Map.class)
            // TODO
            // 나중에 구현
            // try {
            //     const mqManager = getEventManager();
            //     const payload = await mqManager.emitAndWait('besu.request.GetReceipt', {
            //       txhash
            //     });
            //     if (!payload) {
            //       throw new BadRequestError(errors.transaciton.unknownErr);
            //     }
            //     return payload;
            //   } catch (e) {
            //     throw e;
            //   }
            // .to("sql:classpath:sql/vue/manage/contract/contractDetail.sql?outputHeader=result&outputType=SelectOne")
            // .setBody(simple("${headers.result}"))
            .to("direct:response-success-200");
    }
}