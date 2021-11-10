package shinhands.com.rest.platform.hub;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.core.MediaType;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jsonvalidator.JsonValidationException;
import org.apache.camel.model.dataformat.JsonLibrary;

import shinhands.com.exception.ConflictError;
import shinhands.com.exception.DefaultException;
import shinhands.com.exception.UnauthorizedError;
import shinhands.com.model.hub.misc.policy.userifs.UserPayload;
import shinhands.com.model.hub.misc.policy.userifs.UserUpdatePayload;
import shinhands.com.processor.JwtVerfyProcessor;

@ApplicationScoped
public class Member extends RouteBuilder {
    private static String logName = Member.class.getName();

    @Inject
    JwtVerfyProcessor jwtVerfyProcessor;

    @Override
    public void configure() {
        onException(Exception.class).handled(true).maximumRedeliveries(0).to("direct:common-500");
        onException(DefaultException.class).handled(true).maximumRedeliveries(0).to("direct:bad-response-200");
        onException(ConflictError.class).handled(true).maximumRedeliveries(0).to("direct:bad-response-200");
        onException(JsonValidationException.class).handled(true).maximumRedeliveries(0).to("direct:bad-jsonvalidator-200");
        onException(UnauthorizedError.class).handled(true).maximumRedeliveries(0).to("direct:bad-response-200");

        rest("/member")
            .consumes(MediaType.APPLICATION_JSON)
            .produces(MediaType.APPLICATION_JSON)
            .post("/join")
                .to("direct:member-join")
            .post("/checkid")
                .to("direct:member-checkid")
            .post("/findown").description("회원정보 조회")
                .to("direct:member-findown")
            .post("/editown").description("회원정보 수정")
                .to("direct:member-editown");


        from("direct:member-join")
            .routeId("member:join")
            .marshal().json(JsonLibrary.Jackson)
            .to("json-validator:json-schema/member-join-schema.json")
            .log(LoggingLevel.INFO, logName,
                "[marshal]>>> [${headers}] [${body}]")
            .unmarshal().json(JsonLibrary.Jackson, UserPayload.class)
            .to("sql:classpath:sql/vue/existUser.sql?outputHeader=userCount&outputType=SelectOne")
            .to("log:query")
            .to("log:row")
            .choice()
                .when(header("userCount").isGreaterThan(0))
                    .throwException(new ConflictError("아이디 또는 이메일 주소가 이미 존재합니다."))
                .otherwise()
                    // [2021-08-03 15:09:45] exception 처리
                    .to("sql:classpath:sql/vue/saveUser.sql?outputHeader=result")
                    .to("direct:response-success-200")
            .end();

        // 회원정보 조회
        from("direct:member-findown").routeId("member:findown")
            .process(jwtVerfyProcessor)
            // .choice()
                // .when(simple(String.format("${headers.verified} == '%1s'",Boolean.TRUE)))
                    .to("sql:classpath:sql/vue/userInfo.sql?outputHeader=result&outputType=SelectOne")
                    .setBody(simple("${headers.result}"))
                    .to("direct:response-200");
                // .otherwise()
                    // .throwException(new UnauthorizedError("사용자 세션이 정보가 존재하지 않습니다."))
            // .end();

        from("direct:member-editown").routeId("member:editown")
            .process(jwtVerfyProcessor)
            .marshal().json(JsonLibrary.Jackson)
            .to("json-validator:json-schema/member-edit-schema.json")
            .unmarshal().json(JsonLibrary.Jackson, UserUpdatePayload.class)
            .to("sql:classpath:sql/vue/userUpdate.sql?outputHeader=result&outputType=SelectOne")
            .setBody(simple("${headers.result}"))
            .to("direct:response-200");

        from("direct:member-checkid").routeId("member:checkid")
            .marshal().json(JsonLibrary.Jackson)
            .to("json-validator:json-schema/member-checkUserId-schema.json")
            .unmarshal().json(JsonLibrary.Jackson, UserPayload.class)
            .to("sql:classpath:sql/vue/existUserId.sql?outputHeader=userCount&outputType=SelectOne")
            .choice()
                .when(header("userCount").isGreaterThan(0))
                    .throwException(new ConflictError("아이디가 이미 존재합니다."))
                .otherwise()
                    .to("direct:response-success-200")
            .end();
    }
}
