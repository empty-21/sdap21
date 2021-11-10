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
import shinhands.com.model.hub.misc.group.GroupPayload;
import shinhands.com.processor.JwtVerfyProcessor;
import shinhands.com.processor.PagingValidatorProcessor;
import shinhands.com.processor.PagingResultProcessor;

@ApplicationScoped
/**
 * 관리자 권한의 사용자 전용
 */
public class Authgroup extends RouteBuilder {

    @Inject
    JwtVerfyProcessor jwtVerfyProcessor;

    @Override
    public void configure() {
        onException(Exception.class).handled(true).maximumRedeliveries(0).to("direct:common-500");
        onException(DefaultException.class).handled(true).maximumRedeliveries(0).to("direct:bad-response-200");
        onException(ConflictError.class).handled(true).maximumRedeliveries(0).to("direct:bad-response-200");
        onException(JsonValidationException.class).handled(true).maximumRedeliveries(0).to("direct:bad-jsonvalidator-200");
        onException(UnauthorizedError.class).handled(true).maximumRedeliveries(0).to("direct:bad-response-200");

        rest("/manage/authgroup")
            .consumes(MediaType.APPLICATION_JSON)
            .produces(MediaType.APPLICATION_JSON)
            .get("/").description("DAP-SIF057 (권한 그룹 목록조회)")
                .to("direct:manage-authgroup-list")
            .post("/").description("DAP-SIF058 (권한 그룹 추가)")
                .to("direct:manage-authgroup-add")
            .get("/detail").description("DAP-SIF060 (권한 그룹 상세조회)")
                .to("direct:manage-authgroup-detail")
            .post("/delete").description("DAP-SIF059 (권한 그룹 삭제)")
                .to("direct:manage-authgroup-delete")
            .post("/update").description("DAP-SIF061 (권한 그룹 수정)")
                .to("direct:manage-authgroup-update")
            .post("/delete/all").description("DAP-SIF062 (권한 그룹 전체 삭제)")
                .to("direct:manage-authgroup-delete-all");

        from("direct:manage-authgroup-list").routeId("manage:authgroup-list")
            // .process(jwtVerfyProcessor)
            .process(new PagingValidatorProcessor())
            .to("sql:classpath:sql/vue/manage/group/authGroupList.sql?outputHeader=result")
            .to("sql:classpath:sql/vue/manage/group/authGroupListRowsCount.sql?outputHeader=total_count&outputType=SelectOne")
            .process(new PagingResultProcessor())
            .to("direct:response-200");

        from("direct:manage-authgroup-add").routeId("manage:authgroup-add")
            .marshal().json(JsonLibrary.Jackson)
            .to("json-validator:json-schema/group-add-schema.json")
            .unmarshal().json(JsonLibrary.Jackson, GroupPayload.class)
            .to("sql:classpath:sql/vue/manage/group/authGroupAdd.sql?outputHeader=result")
            .to("direct:response-success-200");

        // [2021-08-10 16:26:10]
        // todo
        // query parameter validation
        from("direct:manage-authgroup-detail").routeId("manage:authgroup-detail")
            // .process(jwtVerfyProcessor)
            .to("sql:classpath:sql/vue/manage/group/authGroupDetail.sql?outputHeader=result&outputType=SelectOne")
            .setBody(simple("${headers.result}"))
            .to("direct:response-200");

        from("direct:manage-authgroup-delete").routeId("manage:authgroup-delete")
            .marshal().json(JsonLibrary.Jackson)
            .unmarshal().json(JsonLibrary.Jackson, Map.class)
            .to("sql:classpath:sql/vue/manage/group/authGroupDelete.sql?outputHeader=result")
            .to("direct:response-success-200");

        from("direct:manage-authgroup-update").routeId("manage:authgroup-update")
            // .to("direct:service-audit")
            .marshal().json(JsonLibrary.Jackson)
            .to("json-validator:json-schema/group-update-schema.json")
            .unmarshal().json(JsonLibrary.Jackson, GroupPayload.class)
            .to("sql:classpath:sql/vue/manage/group/authGroupUpdate.sql?outputHeader=result")
            .to("direct:response-success-200");

        from("direct:manage-authgroup-delete-all").routeId("manage:authgroup-delete-all")
            .to("sql:classpath:sql/vue/manage/group/authGroupDeleteAll.sql?outputHeader=result")
            .to("direct:response-success-200");
    }
}