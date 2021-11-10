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
public class Kyc extends RouteBuilder {

    @Inject
    JwtVerfyProcessor jwtVerfyProcessor;

    @Override
    public void configure() {
        onException(Exception.class).handled(true).maximumRedeliveries(0).to("direct:common-500");
        onException(DefaultException.class).handled(true).maximumRedeliveries(0).to("direct:bad-response-200");
        onException(ConflictError.class).handled(true).maximumRedeliveries(0).to("direct:bad-response-200");
        onException(JsonValidationException.class).handled(true).maximumRedeliveries(0).to("direct:bad-jsonvalidator-200");
        onException(UnauthorizedError.class).handled(true).maximumRedeliveries(0).to("direct:bad-response-200");

        rest("/manage/kyc")
            .consumes(MediaType.APPLICATION_JSON)
            .produces(MediaType.APPLICATION_JSON)
            .get("/").description("DAP-SIF121 (KYC신청목록)")
                .to("direct:manage-kyc-list")
            .get("/detail").description("DAP-SIF120 (kYC신청 상세조회)")
                .to("direct:manage-kyc-detail")
            .post("/update").description("DAP-SIF122 (KYC신청 승인/거절/수정)")
                .to("direct:manage-kyc-update")
            .post("/delete").description("DAP-SIF123 (KYC 신청 삭제)")
                .to("direct:manage-kyc-delete");

        from("direct:manage-kyc-list").routeId("manage:kyc-list")
            .process(new PagingValidatorProcessor())
            .to("sql:classpath:sql/vue/manage/kyc/kycList.sql?outputHeader=result")
            .to("sql:classpath:sql/vue/manage/kyc/kycListRowsCount.sql?outputHeader=total_count&outputType=SelectOne")
            .process(new PagingResultProcessor())
            .to("direct:response-200");

        from("direct:manage-kyc-detail").routeId("manage:kyc-detail")
            // .process(jwtVerfyProcessor)
            .to("sql:classpath:sql/vue/manage/kyc/kycDetail.sql?outputHeader=result&outputType=SelectOne")
            .setBody(simple("${headers.result}"))
            .to("direct:response-200");


        from("direct:manage-kyc-update").routeId("manage:kyc-update")
            .marshal().json(JsonLibrary.Jackson)
            .to("json-validator:json-schema/kyc-update-schema.json")
            .unmarshal().json(JsonLibrary.Jackson, GroupPayload.class)
            .to("sql:classpath:sql/vue/manage/kyc/kycUpdate.sql?outputHeader=result")
            .to("direct:response-success-200");

        from("direct:manage-kyc-delete").routeId("manage:kyc-delete")
            .marshal().json(JsonLibrary.Jackson)
            .unmarshal().json(JsonLibrary.Jackson, Map.class)
            .to("sql:classpath:sql/vue/manage/kyc/kycDelete.sql?outputHeader=result")
            .to("direct:response-success-200");
    }
}