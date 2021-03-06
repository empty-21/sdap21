package shinhands.com.rest.platform.hub;

import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.core.MediaType;

import org.apache.camel.FluentProducerTemplate;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jsonvalidator.JsonValidationException;
import org.apache.camel.model.dataformat.JsonLibrary;

import lombok.extern.slf4j.Slf4j;
import shinhands.com.enumeration.Stat;
import shinhands.com.exception.ConflictError;
import shinhands.com.exception.ForbiddenError;
import shinhands.com.exception.UnauthorizedError;
import shinhands.com.model.hub.misc.policy.userifs.UserPayload;
import shinhands.com.model.hub.request.reqifs.LoginPayload;
import shinhands.com.processor.JwtGenerateProcessor;
import shinhands.com.processor.JwtVerfyProcessor;

@Slf4j
@ApplicationScoped
public class Auth extends RouteBuilder {
    @Inject
    JwtGenerateProcessor jwtGenerateProcessor;

    @Inject
    JwtVerfyProcessor jwtVerfyProcessor;

    @Override
    public void configure() {
        // onException(Exception.class).handled(true).maximumRedeliveries(0).to("direct:common-500");
        // onException(DefaultException.class).handled(true).maximumRedeliveries(0).to("direct:bad-response-200");
        onException(ConflictError.class).handled(true).maximumRedeliveries(0).to("direct:bad-response-200");
        onException(ForbiddenError.class).handled(true).maximumRedeliveries(0).to("direct:bad-response-200");
        onException(UnauthorizedError.class).handled(true).maximumRedeliveries(0).to("direct:bad-response-200");
        onException(JsonValidationException.class).handled(true).maximumRedeliveries(0).to("direct:bad-jsonvalidator-200");

        rest("/auth")
            .consumes(MediaType.APPLICATION_JSON)
            .produces(MediaType.APPLICATION_JSON)
            .post("/login")
                .to("direct:auth-login")
            .post("/logout")
                .to("direct:auth-logout");

        from("direct:auth-login").routeId("auth:login")
            .marshal().json(JsonLibrary.Jackson)
            .to("json-validator:json-schema/member-login-schema.json")
            .unmarshal().json(JsonLibrary.Jackson, LoginPayload.class)
            .to("sql:classpath:sql/vue/login.sql?outputHeader=result&outputType=SelectOne")
            .log(LoggingLevel.INFO, "#UUID ?????????", "marker","${bean:uuidGenerator.generateUuid}")
            // .process(ex ->{
            //     FluentProducerTemplate producerTemplate = ex.getContext().createFluentProducerTemplate();
            //     Object res = producerTemplate
            //         .withHeader("Content-Type", "application/json")
            //         .withBody(Map.of("userid","dap_operator","secret","111111"))
            //         .to("direct:auth-test")
            //         .request();
            //     log.error("# producerTemplate ?????????",res.toString());
            //     ex.getIn();
            // })
            .choice()
                .when(header("CamelSqlRowCount").isEqualTo(0))
                    .throwException(new UnauthorizedError("????????? ????????? ???????????? ????????????."))
                .when(simple(String.format("${headers.result[validsecret]} == '%1s'",Boolean.FALSE)))
                    .to("sql:classpath:sql/vue/updateLoginFailStat.sql?outputHeader=result")
                    .throwException(new UnauthorizedError("??????????????? ??????????????????."))
                .when(simple(String.format("${headers.result[userstat]} in '%1s,%2s'",
                        Stat.User.DELETED, Stat.User.DEACTIVATE)))
                    .throwException(new ForbiddenError("????????? ?????? ????????????"))
                .otherwise()
                    .setBody(simple("${headers.result}"))
                    .marshal().json(JsonLibrary.Jackson)
                    .unmarshal().json(JsonLibrary.Jackson, UserPayload.class)
                    .process(jwtGenerateProcessor) // headers.token
                    .to("sql:classpath:sql/vue/updateLoginStat.sql?outputHeader=result")
                    .to("sql:classpath:sql/vue/createdLoginHistory.sql?outputHeader=result")
                    .setBody(simple("${headers.accessToken}"))
                    .to("direct:response-200")
            .end();

        /*
            ????????????
                - token ?????? ??????
                - ????????? ??????
                - session ??????????????? ??????
            ????????? ??????
                -jwt??? db?????? ?????? ??????? ????????? ????????????..
            ????????? ?????? ??????????????? session??? ????????? id??? ???????????? ?????? ?????? ?????? active - false ??????
            [2021-08-05 12:49:31] jwtVerfyProcessor ????????? ?????? 2????????? ?????? ?????????..
        */
        from("direct:auth-logout").routeId("auth:logout")
            .process(jwtVerfyProcessor)
            .choice()
                .when(simple(String.format("${headers.verified} == '%1s'",Boolean.TRUE)))
                    .to("sql:classpath:sql/vue/removeUserSession.sql?outputHeader=result")
                    .to("log:query")
                    .to("direct:response-success-200")
                .otherwise()
                    .log(LoggingLevel.ERROR, "Auth",
                            "[direct:auth-logout]>>> [${headers}] [${body}]")
                    .to("direct:response-success-200")
            .end();
    }
}