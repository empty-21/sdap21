package shinhands.com.rest.platform.daml;

import java.util.Map;

import javax.enterprise.context.ApplicationScoped;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jsonvalidator.JsonValidationException;
import org.apache.camel.model.dataformat.JsonLibrary;

import shinhands.com.enumeration.Route;
import shinhands.com.exception.DefaultException;
import shinhands.com.exception.ForbiddenError;
import shinhands.com.exception.UnauthorizedError;
import shinhands.com.model.daml.DAMLResponse;

@ApplicationScoped
public class BesuRoute extends RouteBuilder {

    @Override
    public void configure() {
        onException(Exception.class).handled(true).maximumRedeliveries(0).to(Route.Response.COMMON_RESPONSE_500.label);
        onException(DefaultException.class).handled(true).maximumRedeliveries(0).to(Route.Response.BAD_RESPONSE_200.label);
        onException(DefaultException.class).handled(true).maximumRedeliveries(0).to(Route.Response.BAD_RESPONSE_200.label);
        onException(ForbiddenError.class).handled(true).maximumRedeliveries(0).to(Route.Response.BAD_RESPONSE_200.label);
        onException(JsonValidationException.class).handled(true).maximumRedeliveries(0).to(Route.Response.BAD_JSONVALIDATOR_200.label);
        onException(UnauthorizedError.class).handled(true).maximumRedeliveries(0).to(Route.Response.BAD_RESPONSE_200.label);

        /**
         * TODO
         * [2021-08-31 16:04:43]
         *  - 실패 응답시 모든 contract를 archvie처리 
         */
        from("direct:besu-create-contract-response").routeId("besu-create-contract-response").description("BESU Created")
            .unmarshal().json(JsonLibrary.Jackson, Map.class);
        
        from("direct:besu-create-contract").routeId("besu-create-contract")
            .setBody(simple("${bean:uuidGenerator.generateUuid}"));
            // .to("direct:besu-create-response");
    }
}
