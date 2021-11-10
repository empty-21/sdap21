package shinhands.com.routes;

import javax.enterprise.context.ApplicationScoped;

import org.apache.camel.builder.endpoint.EndpointRouteBuilder;

@ApplicationScoped
public class AuditRoute extends EndpointRouteBuilder {

    @Override
    public void configure() throws Exception {
        from("direct:service-audit").routeId("service:audit")
            .to("mock:ok");
        from("direct:service-audit-save").routeId("service:audit-save")
            .to("mock:ok");
        from("direct:service-audit-get").routeId("service:audit-get")
            .to("mock:ok");
    }
}
