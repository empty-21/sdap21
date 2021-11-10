package shinhands.com.routes;

import javax.enterprise.context.ApplicationScoped;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class RestConfiguration extends RouteBuilder {

    @ConfigProperty(name = "rest.port", defaultValue="8088")
    int port;

    // dap/api/v2/manage/tokentemplete?page=undefined&rows=undefined
    @Override
    public void configure() throws Exception {
        // 시스템에서 발생하는 uuid 설정
        // this.getContext().setUuidGenerator(new SimpleUuidGenerator());
        // https://camel.apache.org/manual/latest/uuidgenerator.html
        // 53A01904467CF4C-000000000000000D
        this.getContext().getRegistry().bind("uuidGenerator",new org.apache.camel.support.ShortUuidGenerator());
        restConfiguration().component("netty-http")
            .contextPath("/dap/api/v2")
            .port(port)
            .bindingMode(RestBindingMode.json)
            .jsonDataFormat("json-jackson").enableCORS(true)
            .dataFormatProperty("prettyPrint", "true");
    }

    // @Override
    // protected JndiRegistry createRegistry() throws Exception {
    //     JndiRegistry registry = super.createRegistry();
    //     registry.bind("uuidGenerator", new UUIDGenerator());
    //     return registry;
    // }
}
