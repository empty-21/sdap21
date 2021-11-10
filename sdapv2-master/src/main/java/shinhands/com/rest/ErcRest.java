package shinhands.com.rest;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.core.MediaType;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.model.rest.RestBindingMode;

import shinhands.com.exception.DefaultException;
// import shinhands.com.model.daml.template.ERC20;
// import shinhands.com.model.daml.template.erc20.Payload;

@ApplicationScoped
public class ErcRest extends RouteBuilder {
    private static String logName = ErcRest.class.getName();

    @Override
    public void configure() throws Exception {
        onException(Exception.class).handled(true).maximumRedeliveries(0).to("direct:common-500");
        onException(DefaultException.class).handled(true).maximumRedeliveries(0).to("direct:bad-response-200");

        restConfiguration()
            .component("netty-http")
            .bindingMode(RestBindingMode.json)
            .dataFormatProperty("prettyPrint","true");


        rest("/private/erc")
            .consumes(MediaType.APPLICATION_JSON)
            .produces(MediaType.APPLICATION_JSON)
            .post("/createTokenERC20")
            .to("direct:createTokenERC20");

        from("direct:createTokenERC20")
            .routeId("erc20:createTokenERC20")
            // .marshal().json(JsonLibrary.Jackson)
            // .unmarshal().json(JsonLibrary.Jackson, shinhands.com.model.daml.template.erc20.Payload.class)
            // .process(exchange -> {
            //     // Payload payload = exchange.getIn().getBody(shinhands.com.model.daml.template.erc20.Payload.class);
            //     // ERC20 erc = new ERC20();
            //     // erc.setTemplateId("ERC20:ERC20");
            //     // erc.setPayload(payload);
            //     // exchange.getIn().setBody(erc);
            // }).log(LoggingLevel.INFO, logName, "[${routeId}] ${body}]")
            // .enrich("direct:restful-daml-create-contract", new MongoOperationAggregationStrategy())
            .to("direct:saveContract").to("direct:response-200");
    }
}
