package shinhands.com.routes;

import javax.enterprise.context.ApplicationScoped;

import org.apache.camel.builder.endpoint.EndpointRouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class UserRoute extends EndpointRouteBuilder {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserRoute.class);

    @Override
    public void configure() throws Exception {
        // from("direct:createUser").routeId("user:create").
        // marshal().json(JsonLibrary.Jackson).unmarshal()
        //         .json(JsonLibrary.Jackson, Member.class)
        //         .process(exchange -> {
        //             Member body = exchange.getIn().getBody(Member.class);
        //             exchange.getIn().setHeader("body", body);
        //         })
        // .to("mybatis:setPerson?statementType=Insert&inputHeader=body")
        // .to("log:insertLog?showBody=true");
    }
}
