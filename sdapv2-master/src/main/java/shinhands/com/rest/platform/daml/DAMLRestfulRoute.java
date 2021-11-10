package shinhands.com.rest.platform.daml;

import javax.enterprise.context.ApplicationScoped;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jsonvalidator.JsonValidationException;
import org.apache.camel.model.dataformat.JsonLibrary;

import shinhands.com.enumeration.Route;
import shinhands.com.exception.DefaultException;
import shinhands.com.exception.ForbiddenError;
import shinhands.com.exception.UnauthorizedError;
import shinhands.com.model.daml.DAMLExeciseResponse;
import shinhands.com.model.daml.DAMLQueryResponse;
import shinhands.com.model.daml.DAMLResponse;

@ApplicationScoped
public class DAMLRestfulRoute extends RouteBuilder {

    @Override
    public void configure() {
        onException(Exception.class).handled(true).maximumRedeliveries(0).to(Route.Response.COMMON_RESPONSE_500.label);
        onException(DefaultException.class).handled(true).maximumRedeliveries(0).to(Route.Response.BAD_RESPONSE_200.label);
        onException(DefaultException.class).handled(true).maximumRedeliveries(0).to(Route.Response.BAD_RESPONSE_200.label);
        onException(ForbiddenError.class).handled(true).maximumRedeliveries(0).to(Route.Response.BAD_RESPONSE_200.label);
        onException(JsonValidationException.class).handled(true).maximumRedeliveries(0).to(Route.Response.BAD_JSONVALIDATOR_200.label);
        onException(UnauthorizedError.class).handled(true).maximumRedeliveries(0).to(Route.Response.BAD_RESPONSE_200.label);

        from("direct:restful-daml-response").routeId("restful-daml-response").description("DAML Created")
            .convertBodyTo(String.class)
            // .log("# daml-response => ${body}")
            .unmarshal().json(JsonLibrary.Jackson,DAMLResponse.class);
        from("direct:restful-daml-query-response").routeId("restful-daml-query-response").description("DAML Query")
            .convertBodyTo(String.class)
            // .log("# daml-query-result => ${body}")
            .unmarshal().json(JsonLibrary.Jackson,DAMLQueryResponse.class);
        from("direct:restful-daml-exercise-response").routeId("restful-daml-exercise-response").description("DAML Exercise")
            .convertBodyTo(String.class)
            // .log("# daml-exercise-result => ${body}")
            .unmarshal().json(JsonLibrary.Jackson,DAMLExeciseResponse.class);

        from("direct:restful-daml-query-contract").routeId("restful-daml-query-contract")
                .marshal().json(JsonLibrary.Jackson)
                .convertBodyTo(String.class)
                .log("# [${routeId}] daml-query => ${body}")
                .toD("netty-http:http://{{daml.host}}/v1/query")
                .to("direct:restful-daml-query-response");
                // .log("### [${routeId}] daml-query => ${body}");

        from("direct:restful-daml-create-contract").routeId("restful-daml-create-contract")
                .marshal().json(JsonLibrary.Jackson)
                .convertBodyTo(String.class)
                .log("# [${routeId}] daml-contract => ${body}")
                .process(ex->{
                    ex.getIn();
                })
                .toD("netty-http:http://{{daml.host}}/v1/create")
                .to("direct:restful-daml-response");

        from("direct:restful-daml-exercise-contract").routeId("restful-daml-exercise-contract")
                .marshal().json(JsonLibrary.Jackson)
                .convertBodyTo(String.class)
                .log("# [${routeId}] daml-exercise => ${body}")
                .process(ex->{
                    ex.getIn();
                })
                .toD("netty-http:http://{{daml.host}}/v1/exercise")
                .to("direct:restful-daml-exercise-response");
    }
}
