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
import shinhands.com.model.hub.misc.policy.userifs.UserUpdateManage;
import shinhands.com.processor.JwtVerfyProcessor;
import shinhands.com.processor.PagingResultProcessor;
import shinhands.com.processor.PagingValidatorProcessor;

@ApplicationScoped
public class Member extends RouteBuilder {

    @Inject
    JwtVerfyProcessor jwtVerfyProcessor;

    @Override
    public void configure() {
        onException(Exception.class).handled(true).maximumRedeliveries(0).to("direct:common-500");
        onException(DefaultException.class).handled(true).maximumRedeliveries(0).to("direct:bad-response-200");
        onException(ConflictError.class).handled(true).maximumRedeliveries(0).to("direct:bad-response-200");
        onException(JsonValidationException.class).handled(true).maximumRedeliveries(0).to("direct:bad-jsonvalidator-200");
        onException(UnauthorizedError.class).handled(true).maximumRedeliveries(0).to("direct:bad-response-200");

        rest("/manage/member")
            .consumes(MediaType.APPLICATION_JSON)
            .produces(MediaType.APPLICATION_JSON)
            .get("/").description("DAP-SIF125 (사용자 목록 조회)")
                .to("direct:manage-member-list")
            .post("/").description("DAP-SIF124 (사용자 추가)")
                .to("direct:member-join")
                // .to("direct:manage-member-add")
            .post("/delete").description("사용자 삭제")
                .to("direct:manage-member-delete")
            .post("/update").description("사용자 수정")
                .to("direct:manage-member-update");

        /**
         * TODO
         * [2021-08-17 09:35:51] role 기반 조회
         */
        from("direct:manage-member-list").routeId("manage:member-list")
            .process(new PagingValidatorProcessor())
            .to("sql:classpath:sql/vue/manage/member/memberList.sql?outputHeader=result")
            .to("sql:classpath:sql/vue/manage/member/memberListRowsCount.sql?outputHeader=total_count&outputType=SelectOne")
            .process(new PagingResultProcessor())
            .to("direct:response-200");

        from("direct:manage-member-delete").routeId("manage:member-delete")
            .marshal().json(JsonLibrary.Jackson)
            .unmarshal().json(JsonLibrary.Jackson, Map.class)
            .to("sql:classpath:sql/vue/manage/member/memberDelete.sql?outputHeader=result")
            .to("direct:response-success-200");

        /**
         * TODO
         * [2021-08-17 13:37:47] 기존 코드는 email값도 update가능함, email변경가능 여부 확인필요
         */
        from("direct:manage-member-update").routeId("manage:member-update")
            .marshal().json(JsonLibrary.Jackson)
            .to("json-validator:json-schema/manage-member-edit-schema.json")
            .unmarshal().json(JsonLibrary.Jackson, UserUpdateManage.class)
            .to("sql:classpath:sql/vue/manage/member/memberUpdate.sql?outputHeader=result")
            .to("direct:response-success-200");
    }
}

// from("direct:member-editown").routeId("member:editown")
//             .process(jwtVerfyProcessor)
//             .marshal().json(JsonLibrary.Jackson)
//             .to("json-validator:json-schema/member-edit-schema.json")
//             .unmarshal().json(JsonLibrary.Jackson, UserUpdatePayload.class)
//             .to("sql:classpath:sql/vue/userUpdate.sql?outputHeader=result&outputType=SelectOne")
//             .setBody(simple("${headers.result}"))
//             .to("direct:response-200");