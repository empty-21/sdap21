package shinhands.com.routes;

import javax.enterprise.context.ApplicationScoped;

import org.apache.camel.builder.endpoint.EndpointRouteBuilder;

// import org.infinispan.client.hotrod.configuration.Configuration;
// import org.infinispan.client.hotrod.configuration.ConfigurationBuilder;
// import org.infinispan.manager.EmbeddedCacheManager;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
public class InfinispanRoute extends EndpointRouteBuilder {
    // @BindToRegistry("cacheContainerConfiguration")
    // Configuration configuration;

    // public InfinispanRoute() {
    //     configuration = new ConfigurationBuilder()
    //             .security()
    //             .authentication()
    //             .saslMechanism("DIGEST-MD5")
    //             .username("myuser")
    //             .password("myuser")
    //             .addServer()
    //             .host("localhost")
    //             .port(11222)
    //             .version(org.infinispan.client.hotrod.ProtocolVersion.PROTOCOL_VERSION_25)
    //             // .addContextInitializer(new SepaplusSchemaImpl())
    //             .build();
    // }
    // @Inject
    // EmbeddedCacheManager cacheManager;

    @Override
    public void configure() throws Exception {
        // log.info("### getCacheNames => {}",cacheManager.getClusterName());
        // from("timer:foo?delay=1s")
        //     .setHeader(InfinispanConstants.OPERATION).constant(InfinispanOperation.PUT)
        //     .setHeader(InfinispanConstants.KEY).constant("123")
        // .to("infinispan-embedded:default&cacheContainer=#default");

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
