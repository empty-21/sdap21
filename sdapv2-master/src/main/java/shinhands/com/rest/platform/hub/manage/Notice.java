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
import shinhands.com.model.hub.misc.policy.userifs.UserPayload;
import shinhands.com.model.hub.misc.policy.userifs.UserUpdateManage;
import shinhands.com.processor.JwtVerfyProcessor;
import shinhands.com.processor.PagingResultProcessor;
import shinhands.com.processor.PagingValidatorProcessor;

@ApplicationScoped
public class Notice extends RouteBuilder {

    @Inject
    JwtVerfyProcessor jwtVerfyProcessor;

    @Override
    public void configure() {
        onException(Exception.class).handled(true).maximumRedeliveries(0).to("direct:common-500");
        onException(DefaultException.class).handled(true).maximumRedeliveries(0).to("direct:bad-response-200");
        onException(ConflictError.class).handled(true).maximumRedeliveries(0).to("direct:bad-response-200");
        onException(JsonValidationException.class).handled(true).maximumRedeliveries(0).to("direct:bad-jsonvalidator-200");
        onException(UnauthorizedError.class).handled(true).maximumRedeliveries(0).to("direct:bad-response-200");

        rest("/manage/notice")
            .consumes(MediaType.APPLICATION_JSON)
            .produces(MediaType.APPLICATION_JSON)
            .get("/").description("DAP-SIF139 notice 목록")
                .to("direct:manage-notice-list")
            .post("/").description("DAP-SIF138 Notice 등록")
                .to("direct:manage-notice-add")
            .get("/detail").description("DAP-SIF140 Notice 상세조회")
                .to("direct:manage-notice-detail")
            .post("/update").description("DAP-SIF136 notice 공지사항 수정")
                .to("direct:manage-notice-update")
            .post("/delete").description("DAP-SIF137 공지사항 삭제")
                .to("direct:manage-notice-delete");


        from("direct:manage-notice-list").routeId("manage:notice-list")
            .process(new PagingValidatorProcessor())
            .to("sql:classpath:sql/vue/manage/notice/noticeList.sql?outputHeader=result")
            .to("sql:classpath:sql/vue/manage/notice/noticeListRowsCount.sql?outputHeader=total_count&outputType=SelectOne")
            .process(new PagingResultProcessor())
            .to("direct:response-200");

        from("direct:manage-notice-add").routeId("manage:notice-add")
            .process(jwtVerfyProcessor)
            // authenticate,
            // auditlog,
            .marshal().json(JsonLibrary.Jackson)
            .to("json-validator:json-schema/manage-notice-add_update-schema.json")
            .unmarshal().json(JsonLibrary.Jackson, shinhands.com.model.hub.misc.manage.Notice.class)
            .to("sql:classpath:sql/vue/manage/notice/noticeAdd.sql?outputHeader=result")
            .to("direct:response-success-200");

        from("direct:manage-notice-detail").routeId("manage:notice-detail")
            .to("sql:classpath:sql/vue/manage/notice/noticeDetail.sql?outputHeader=result&outputType=SelectOne")
            .to("sql:classpath:sql/vue/manage/notice/noticeUpdateCount.sql?outputHeader=update_result")
            .setBody(simple("${headers.result}"))
            .to("direct:response-200");

        from("direct:manage-notice-delete").routeId("manage:notice-delete")
            .marshal().json(JsonLibrary.Jackson)
            .unmarshal().json(JsonLibrary.Jackson, Map.class)
            .to("sql:classpath:sql/vue/manage/notice/noticeDelete.sql?outputHeader=result")
            .to("direct:response-success-200");

        /**
         * TODO
         * [2021-08-17 15:05:39] 공지사항 수정 히스토리?
         */
        from("direct:manage-notice-update").routeId("manage:notice-update")
            .marshal().json(JsonLibrary.Jackson)
            .to("json-validator:json-schema/manage-notice-add_update-schema.json")
            .unmarshal().json(JsonLibrary.Jackson, shinhands.com.model.hub.misc.manage.Notice.class)
            .to("sql:classpath:sql/vue/manage/notice/noticeUpdate.sql?outputHeader=result")
            .to("direct:response-success-200");
    }
}
