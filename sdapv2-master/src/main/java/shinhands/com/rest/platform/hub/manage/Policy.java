package shinhands.com.rest.platform.hub.manage;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.core.MediaType;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jsonvalidator.JsonValidationException;
import org.apache.camel.model.dataformat.JsonLibrary;

import shinhands.com.enumeration.Route;
import shinhands.com.exception.DefaultException;
import shinhands.com.exception.ForbiddenError;
import shinhands.com.exception.UnauthorizedError;
import shinhands.com.processor.JwtVerfyProcessor;

@ApplicationScoped
public class Policy extends RouteBuilder {

    @Inject
    JwtVerfyProcessor jwtVerfyProcessor;

    @Override
    public void configure() {
        onException(Exception.class).handled(true).maximumRedeliveries(0).to(Route.Response.COMMON_RESPONSE_500.label);
        onException(DefaultException.class).handled(true).maximumRedeliveries(0).to(Route.Response.BAD_RESPONSE_200.label);
        onException(DefaultException.class).handled(true).maximumRedeliveries(0).to(Route.Response.BAD_RESPONSE_200.label);
        onException(ForbiddenError.class).handled(true).maximumRedeliveries(0).to(Route.Response.BAD_RESPONSE_200.label);
        onException(JsonValidationException.class).handled(true).maximumRedeliveries(0).to(Route.Response.BAD_JSONVALIDATOR_200.label);
        onException(UnauthorizedError.class).handled(true).maximumRedeliveries(0).to(Route.Response.BAD_RESPONSE_200.label);

        rest("/manage/policy")
            .consumes(MediaType.APPLICATION_JSON)
            .produces(MediaType.APPLICATION_JSON)
            .get("/").description("정책정보 조회")
                .to("direct:manage-policy-detail")
            .post("/").description("정책정보 수정")
                .to("direct:manage-policy-update");

        from("direct:manage-policy-update").routeId("manage:policy-update")
            .marshal().json(JsonLibrary.Jackson)
            .to("json-validator:json-schema/manage-policy-update-schema.json")
            .unmarshal().json(JsonLibrary.Jackson, shinhands.com.model.hub.misc.manage.Policy.class)
            .to("sql:classpath:sql/vue/manage/policy/policyUpdate.sql?outputHeader=result")
            .to("direct:response-success-200");

        from("direct:manage-policy-detail").routeId("manage:policy-detail")
            .to("sql:classpath:sql/vue/manage/policy/policyGet.sql?outputHeader=result&outputType=SelectOne")
            .setBody(simple("${headers.result}"))
            .to("direct:response-200");
    }
}