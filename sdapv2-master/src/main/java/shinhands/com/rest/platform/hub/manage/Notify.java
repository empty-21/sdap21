package shinhands.com.rest.platform.hub.manage;

import java.util.Map;

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
import shinhands.com.processor.PagingResultProcessor;
import shinhands.com.processor.PagingValidatorProcessor;

@ApplicationScoped
public class Notify extends RouteBuilder {

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

        rest("/manage/notify")
            .consumes(MediaType.APPLICATION_JSON)
            .produces(MediaType.APPLICATION_JSON)
            .get("/").description("DAP-SIF142 수/발신 알림목록")
                .to("direct:manage-notify-list")
            .post("/update").description("읽음상태 변경")
                .to("direct:manage-notify-update")
            .post("/add").description("알람 추가")
                .to("direct:manage-notify-add")
            .get("/get_my_notify_cnt").description("getMyNotifyCnt")
                .to("direct:manage-notify-get_my_notify_cnt");


        from("direct:manage-notify-list").routeId("manage:notify-list")
            .process(new PagingValidatorProcessor())
            .to("sql:classpath:sql/vue/findWalletByAddress.sql?outputHeader=wallet&outputType=SelectOne")
            .choice()
                .when(header("wallet").isNull())
                    .throwException(new ForbiddenError("지갑 주소가 존재하지 않습니다."))
                .otherwise()
                    .to("sql:classpath:sql/vue/findMemberByAddress.sql?outputHeader=user&outputType=SelectOne")
                    .choice()
                        .when(header("user").isNull())
                            .throwException(new ForbiddenError("사용자 정보가 존재하지 않습니다."))
                        .when(header("user").isNotEqualTo("${header.id}"))
                            .throwException(new ForbiddenError("사용자와 지갑정보가 일치하지 않습니다."))
                        .otherwise()
                            .to("sql:classpath:sql/vue/manage/notify/notifyList.sql?outputHeader=result")
                            .setBody(simple("${headers.result}"))
                    .end()
            .end()
            .to("direct:response-200");

        /**
         * TODO
         * [2021-08-18 14:36:09] 읽기 상태만 변경하는데 왜?
         */
        from("direct:manage-notify-update").routeId("manage:notify-update")
            .marshal().json(JsonLibrary.Jackson)
            .unmarshal().json(JsonLibrary.Jackson, Map.class)
            .to("sql:classpath:sql/vue/manage/notify/notifyUpdate.sql?outputHeader=result")
            .to("direct:response-success-200");

        /**
         * TODO
         * [2021-08-18 14:19:20] 누가 언제 작성했는지 로그를 남길필요는 없나?
         */
        from("direct:manage-notify-add").routeId("manage:notify-add")
            .marshal().json(JsonLibrary.Jackson)
            .to("json-validator:json-schema/manage-notify-add-schema.json")
            .unmarshal().json(JsonLibrary.Jackson, shinhands.com.model.hub.misc.manage.Notify.class)
            .to("sql:classpath:sql/vue/manage/notify/notifyAdd.sql?outputHeader=result")
            .to("direct:response-success-200");

        /**
         * TODO
         * [2021-08-18 14:41:00] 코드 분석후 개발예정
         */
        from("direct:manage-notify-get_my_notify_cnt").routeId("manage:notify-get_my_notify_cnt")
            .marshal().json(JsonLibrary.Jackson)
            .to("mock:ok");
    }
}
