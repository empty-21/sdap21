package shinhands.com.rest.platform.hub.manage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
import shinhands.com.model.hub.misc.templete.Create;
import shinhands.com.processor.JwtVerfyProcessor;
import shinhands.com.processor.PagingResultProcessor;
import shinhands.com.processor.PagingValidatorProcessor;

@ApplicationScoped
public class TokenTemplate extends RouteBuilder {

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

        // dap/api/v2/manage/tokentemplete?page=undefined&rows=undefined
        rest("/manage/tokentemplete")
            .consumes(MediaType.APPLICATION_JSON)
            .produces(MediaType.APPLICATION_JSON)
            .get("/").description("DAP-SIF0 (토큰 탬플렛 조회) - 신규")
                .to("direct:manage-template-list")
            .post("/create").description("DAP-SIF0 (토큰 탬플렛 생성) - 신규")
                .to("direct:manage-template-create")
            .get("/base").description("DAP-SIF0 (토큰 베이스 조회) - 신규")
                .to("direct:manage-template-base-list")
            .get("/behavior").description("DAP-SIF0 (토큰 성질 조회) - 신규")
                .to("direct:manage-template-behavior-list");


        from("direct:manage-template-list").routeId("manage:template-list")
            .process(new PagingValidatorProcessor())
            .to("sql:classpath:sql/vue/manage/tokentemplete/templateList.sql?outputHeader=result")
            .to("sql:classpath:sql/vue/manage/tokentemplete/templateListRowsCount.sql?outputHeader=total_count&outputType=SelectOne")
            .process(new PagingResultProcessor())
            .to("direct:response-200");
        /**
         * TODO
         * - 사용자id
         * - 템플릿이름
         * - 베이스 id
         * - beharviors 목록 체크
         * - pk, fk 확인
         */
        from("direct:manage-template-create").routeId("manage:template-create")
            .process(jwtVerfyProcessor)
            .marshal().json(JsonLibrary.Jackson)
            .to("json-validator:json-schema/manage-tokentemplate-add-schema.json")
            .unmarshal().json(JsonLibrary.Jackson, Create.class)
            .process(ex->{
                String creator = (String) ex.getIn().getHeader("userid");
                Create body = ex.getIn().getBody(Create.class);
                List<Map<String, String>> list = new ArrayList<Map<String,String>>();
                for (String behavior : body.getBehaviors()) {
                    Map<String, String> map = Map.of("name", body.getName(),
                            "base", body.getBase(),
                            "behavior", behavior,
                            "creator", creator);
                    list.add(map);
                }
                ex.getIn().setBody(list);
            })
            .split(body()).parallelProcessing()
            .log("${body}")
                .to("sql:classpath:sql/vue/manage/tokentemplete/templateAdd.sql?outputHeader=result")
            .end()
            .to("direct:response-success-200");

        from("direct:manage-template-base-list").routeId("manage:template-base-list")
            .to("sql:classpath:sql/vue/manage/tokentemplete/tokeBaseAll.sql?outputHeader=result")
            .setBody(simple("${headers.result}"))
            .to("direct:response-200");

        from("direct:manage-template-behavior-list").routeId("manage:template-behavior-list")
            .to("sql:classpath:sql/vue/manage/tokentemplete/tokenBehaviorAll.sql?outputHeader=result")
            .setBody(simple("${headers.result}"))
            .to("direct:response-200");
    }
}
