package shinhands.com.rest;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.core.MediaType;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.model.rest.RestBindingMode;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.jwt.JsonWebToken;

import io.quarkus.runtime.annotations.RegisterForReflection;
import shinhands.com.model.hub.User;
import shinhands.com.processor.JwtGenerateProcessorSample;
import shinhands.com.processor.JwtVerfyProcessor;

@RegisterForReflection
@ApplicationScoped
public class UserRest extends RouteBuilder {

    @ConfigProperty(name = "greeting.message")
    String message;

    // @ConfigProperty(name="mp.jwt.verify.publickey.location" default="")
    // String location;

    @Inject
    JsonWebToken jwt;

    @Inject
    JwtGenerateProcessorSample jwtGenerateProcessor;

    @Inject
    JwtVerfyProcessor jwtVerfyProcessor;

    @Override
    public void configure() throws Exception {
        // log.info("hello => {}, location => {}",message, location);
        restConfiguration().component("netty-http").bindingMode(RestBindingMode.json).dataFormatProperty("prettyPrint",
                "true");

        rest("/user").consumes(MediaType.APPLICATION_JSON).produces(MediaType.APPLICATION_JSON)
            .post("/create").description("사용자 생성").to("direct:createUser");

        rest("/secured")
            .consumes(MediaType.APPLICATION_JSON)
            .produces(MediaType.APPLICATION_JSON)
            .get("/").to("direct:echo")
            .get("/verify").to("direct:jwt-verify")
            .get("/generation").to("direct:jwt-generate");
            // .get("/sql").to("sql:classpath:sql/daml/parties.sql");

        from("direct:jwt-generate")
            // .to("micrometer:counter:jwt-generate.counter")
            // .to("micrometer:timer:jwt-generate.timer?action=start")
            .description("JWT생성하기")
            .routeId("token:jwt-generate")
            .marshal()
                .json(JsonLibrary.Jackson)
                .unmarshal().json(JsonLibrary.Jackson, User.class)
            .process(jwtGenerateProcessor)
            .to("mock:ok");
            // .to("micrometer:timer:jwt-generate.timer?action=end");

        from("direct:jwt-verify")
            // .to("micrometer:counter:jwt-verify.counter")
            // .to("micrometer:timer:jwt-verify.timer?action=start")
            .routeId("token:jwt-verify")
            .process(jwtVerfyProcessor)
            .to("mock:ok");
            // .to("micrometer:timer:jwt-verify.timer?action=end");

        // from("direct:echo")
        //     .routeId("token:echo")
        //     .log(LoggingLevel.INFO, "Message Trace Id: ${header.breadcrumbId}")
        //     .process(new Processor() {
        //         public void process(exchange -> {
        //             // jwt
        //             // jwt.get
        //             // 인증정보, JWT 체크
        //             // User user = new User();
        //             // user.setEmail("test@email.com");
        //             // user.setName("myname!");
        //             // user.setRole("VIP");
        //             Map<String,Object> user = new HashMap<String,Object>();
        //             user.put("name", "myName");
        //             user.put("email","test@email.com");
        //             user.put("role","VIP");

        //             long currentTimeInSecs = TokenUtils.currentTimeInSecs();
        //             long exp = currentTimeInSecs + 300;
        //             long iat = currentTimeInSecs;
        //             long authTime = currentTimeInSecs;
        //             boolean expWasInput = false;

        //             JwtClaimsBuilder claims = Jwt.claims(user)
        //                                         .issuedAt(iat)
        //                                         .claim(Claims.auth_time.name(), authTime);
        //             // String auth = exchange.getIn().getHeader("Authorization");
        //             // check jwt
        //             // true or false or expire or ..
        //             String payload = exchange.getIn().getBody(String.class);
        //             exchange.getIn().setBody("Changed body");
        //     }
        // }).to("mock:xx");
    }
}
