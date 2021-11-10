package shinhands.com.rest.platform;

import java.util.HashMap;
import java.util.Map;
import java.util.Arrays;

import javax.ws.rs.core.MediaType;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.model.rest.RestBindingMode;

import shinhands.com.aggregation.ExistServiceAggregationStrategy;
import shinhands.com.exception.DefaultException;
import shinhands.com.model.daml.Exercise;
import shinhands.com.model.daml.GetContract;
// import shinhands.com.model.daml.template.User;
import shinhands.com.model.daml.template.user.LockUser;
import shinhands.com.model.daml.template.user.Payload;
import shinhands.com.model.daml.template.user.Query;

import com.fasterxml.jackson.databind.ObjectMapper;

public class UserRest extends RouteBuilder {

    private static String logName = UserRest.class.getName();

    @Override
    public void configure() throws Exception {
        onException(Exception.class).handled(true).maximumRedeliveries(0).to("direct:common-500");
        onException(DefaultException.class).handled(true).maximumRedeliveries(0).to("direct:bad-response-200");

        restConfiguration().component("netty-http")
            .bindingMode(RestBindingMode.json).dataFormatProperty("prettyPrint", "true");

        rest("/v1/private/daml/module/user")
            .consumes(MediaType.APPLICATION_JSON)
            .produces(MediaType.APPLICATION_JSON)
            .post("/createUser").description("사용자 생성")
                .to("direct:createUser")
            .post("/getUser")
                .description("사용자 조회")
                .to("direct:getUser")
            .post("/lockUser").description("사용자 lock")
                .to("direct:lockUser");

        from("direct:createUser")
            .routeId("user:createUser")
            .description("사용자 생성")
            .marshal().json(JsonLibrary.Jackson)
        .unmarshal().json(JsonLibrary.Jackson, Payload.class)
        .process(exchange -> {
            Payload payload = exchange.getIn().getBody(Payload.class);
                    Query query = new Query(payload.getUserid());
                    exchange.getIn().setBody(query);
                    exchange.setProperty("$payload", payload);
                    exchange.setProperty("$headers", exchange.getIn().getHeaders());
                })
            .enrich("direct:getUser", new ExistServiceAggregationStrategy("direct:getUser"))
                .choice()
                    .when(header("isExistUser").isEqualTo(Boolean.FALSE)).process(exchange -> {
                        // Map payload0 = exchange.getMessage().getBody(Map.class);
                        // Payload payload = new ObjectMapper().convertValue(payload0,Payload.class);
                        // User user = new User();
                        // user.setTemplateId("User:User");
                        // user.setPayload(payload);
                        // exchange.getIn().setBody(user);
                    })
                    .log(LoggingLevel.INFO, logName, "[${routeId}] ${body}]").to("direct:restful-daml-create-contract")
                    .to("direct:response-200")
                .otherwise().description("사용자가 이미 존재하는 경우")
                    .log(LoggingLevel.INFO, logName, "otherwise => ${body}")
                    .throwException(new DefaultException("User is already exist"))
                .end();
        from("direct:lockUser").routeId("user:lockUser")
            .marshal().json(JsonLibrary.Jackson)
            .unmarshal().json(JsonLibrary.Jackson, LockUser.class)
            .process(exchange -> {
                LockUser payload = exchange.getIn().getBody(LockUser.class);
                Query query = new Query(payload.getLockerid());
                exchange.getIn().setBody(query);
                exchange.setProperty("$payload", payload);
                exchange.setProperty("$headers", exchange.getIn().getHeaders());
            }).
            enrich("direct:getUser", new ExistServiceAggregationStrategy("direct:getUser"))
                .choice()
                    .when(header("isExistUser").isEqualTo(Boolean.FALSE))
                    .throwException(new DefaultException("User is not found."))
                .otherwise()
                    .process(exchange -> {
                        Map<String, String> payload0 = exchange.getMessage().getBody(Map.class);
                        LockUser payload = new ObjectMapper().convertValue(payload0, LockUser.class);
                        String userContractId = exchange.getMessage().getHeader("userContractId", String.class);

                        Exercise exercise = Exercise.builder()
                                                .templateId("User:User")
                                                .contractId(userContractId)
                                                .choice("LockUser")
                                                .argument(Map.of("lockerid", payload.getLockerid()))
                                                .build();
                        exchange.getMessage().setBody(exercise);
                    })
                    .to("direct:restful-daml-exercise-contract")
                    .to("direct:response-200")
            .end();

        from("direct:unlockUser").routeId("user:unlockUser").to("mock:up");
        from("direct:getUser").routeId("user:getUser").description("사용자 조회하기")
            .log(LoggingLevel.INFO, logName, "Start of [${routeId}] ${body}]")
            .marshal().json(JsonLibrary.Jackson)
            .unmarshal().json(JsonLibrary.Jackson, Query.class)
            .process(exchange -> {
                Query body = exchange.getIn().getBody(Query.class);
                GetContract query = new GetContract(Arrays.asList("User:User"), body);
                    exchange.getIn().setBody(query);
                })
            .log(LoggingLevel.INFO, logName, "End of [${routeId}] ${body}]")
            .to("direct:restful-daml-query-contract")
            .to("direct:response-200");
    }
}

