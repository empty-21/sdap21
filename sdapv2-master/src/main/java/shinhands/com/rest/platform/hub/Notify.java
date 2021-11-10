package shinhands.com.rest.platform.hub;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.core.MediaType;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jsonvalidator.JsonValidationException;

import shinhands.com.exception.ConflictError;
import shinhands.com.exception.DefaultException;
import shinhands.com.exception.UnauthorizedError;
import shinhands.com.processor.JwtVerfyProcessor;
import shinhands.com.processor.PagingValidatorProcessor;
import shinhands.com.processor.PagingResultProcessor;

@ApplicationScoped
public class Notify extends RouteBuilder {

    @Inject
    JwtVerfyProcessor jwtVerfyProcessor;

    @Override
    public void configure() {
        onException(Exception.class).handled(true).maximumRedeliveries(0).to("direct:common-500");
        onException(DefaultException.class).handled(true).maximumRedeliveries(0).to("direct:bad-response-200");
        onException(ConflictError.class).handled(true).maximumRedeliveries(0).to("direct:bad-response-200");
        onException(JsonValidationException.class).handled(true).maximumRedeliveries(0).to("direct:bad-jsonvalidator-200");
        onException(UnauthorizedError.class).handled(true).maximumRedeliveries(0).to("direct:bad-response-200");

        rest("/notify")
            .consumes(MediaType.APPLICATION_JSON)
            .produces(MediaType.APPLICATION_JSON)
            .get("/ownerecv").description("DAP-SIF022 본인의 수신알림 목록조회")
                .to("direct:notify-ownerecv")
            .get("/ownsend").description("DAP-SIF023 본인의 발신알림 목록조회")
                .to("direct:notify-ownsend")
            .get("/detail").description("DAP-SIF025 수신/발신 알림 상세조회")
                .to("direct:notify-detail");

        from("direct:notify-ownerecv").routeId("notify:ownerecv")
            .process(jwtVerfyProcessor)
            .process(new PagingValidatorProcessor())
            .to("sql:classpath:sql/vue/notifyOwneRecvList.sql?outputHeader=result")
            .to("sql:classpath:sql/vue/notifyOwneRecvListRowsCount.sql?outputHeader=total_count&outputType=SelectOne")
            .process(new PagingResultProcessor())
            // TODO
            // 규격화된 페이징 응답 모델 구현 필요
            // 데이터가 더이상 없는경우 마지막 페이지 정보 표기
            // .setBody(simple("${headers}"))
            .to("direct:response-200");

        from("direct:notify-ownsend").routeId("notify:ownsend")
            .process(jwtVerfyProcessor)
            .process(new PagingValidatorProcessor())
            .to("sql:classpath:sql/vue/notifyOwneSendList.sql?outputHeader=result")
            .to("sql:classpath:sql/vue/notifyOwneSendListRowsCount.sql?outputHeader=total_count&outputType=SelectOne")
            .process(new PagingResultProcessor())
            .to("direct:response-200");

        from("direct:notify-detail").routeId("notify:detail")
            .process(jwtVerfyProcessor)
            .to("sql:classpath:sql/vue/notifyDetail.sql?outputHeader=result&outputType=SelectOne")
            .to("sql:classpath:sql/vue/notifyMarkeUpdate.sql?outputHeader=update_result")
            .setBody(simple("${headers.result}"))
            .to("direct:response-200");
        }
}
