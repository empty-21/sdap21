package shinhands.com.rest.platform.daml;
import java.util.Arrays;

import javax.enterprise.context.ApplicationScoped;

import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;

import shinhands.com.aggregation.MongoOperationAggregationStrategy;
import shinhands.com.exception.DefaultException;
import shinhands.com.model.daml.GetContract;
import shinhands.com.model.daml.template.service.GetToken;
import shinhands.com.model.daml.template.service.ServiceList;

@ApplicationScoped
public class FinanceRoute extends RouteBuilder {
    private static String logName = FinanceRoute.class.getName();

    @Override
    public void configure() {
        onException(Exception.class).handled(true).maximumRedeliveries(0).to("direct:common-500");
        onException(DefaultException.class).handled(true).maximumRedeliveries(0).to("direct:bad-response-200");

        from("direct:createTokenERC20").routeId("erc20:createTokenERC20").marshal().json(JsonLibrary.Jackson)
                // .unmarshal().json(JsonLibrary.Jackson, shinhands.com.model.daml.template.erc20.Payload.class)
                // .process(exchange -> {
                //     shinhands.com.model.daml.template.erc20.Payload payload =
                //         exchange.getIn().getBody(shinhands.com.model.daml.template.erc20.Payload.class);
                //     ERC20 erc = new ERC20();
                //     erc.setTemplateId("ERC20:ERC20");
                //     erc.setPayload(payload);
                //     exchange.getIn().setBody(erc);
                // }).log(LoggingLevel.INFO, logName, "[${routeId}] ${body}]")
                // .enrich("direct:restful-daml-create-contract", new MongoOperationAggregationStrategy())
                .to("direct:saveContract").to("direct:response-200");

        from("direct:createTokenERC721").routeId("erc721:createTokenERC721").marshal().json(JsonLibrary.Jackson)
                // .unmarshal().json(JsonLibrary.Jackson, shinhands.com.model.daml.template.erc721.Payload.class)
                // .process(exchange -> {
                //     shinhands.com.model.daml.template.erc721.Payload payload = exchange.getIn()
                //             .getBody(shinhands.com.model.daml.template.erc721.Payload.class);
                //     ERC721 erc = new ERC721();
                //     erc.setTemplateId("ERC721:ERC721");
                //     erc.setPayload(payload);
                //     exchange.getIn().setBody(erc);
                // }).log(LoggingLevel.INFO, logName, "[${routeId}] ${body}]")
                // .enrich("direct:restful-daml-create-contract", new MongoOperationAggregationStrategy())
                .to("direct:saveContract").to("direct:response-200");

        /*
         * https://github.com/bsaunder/camel/blob/master/eap_6/cdi-jaxws-splitter/src/main/java/net/bryansaunders/camel/
         * eap_6/cdi_jaxws_splitter/CamelRoutes.java
         * https://stackoverflow.com/questions/15948792/apache-camel-accessing-camelloopindex
         * https://github.com/bsaunder/camel/blob/18640d579ce014579f11abe2571b43fd4d55d66e/eap_6/cdi-split-build-
         * infinispan/src/main/java/net/bryansaunders/camel/eap_6/cdi_split_agg/CamelRoutes.java
         * https://github.com/ShaneLLucyk/ZoHo_CRM/blob/65b83b2a49d1b92ef8263b2498aa89d5b6269ebc/src/main/java/com/
         * shanelucyk/camel/classes/routes/DemoRoutes.java
         * https://github.com/brianblaze14/spring-boot-camel-training/blob/master/camel-sql-training/src/main/java/com/
         * mclebtec/demo/route/CamelDemoRepoRoute.java
         *
         * https://camel.apache.org/components/3.7.x/languages/bean-language.html
         * https://camel.apache.org/components/latest/languages/simple-language.html
         * https://camel.apache.org/components/3.7.x/eips/loop-eip.html
         */
        from("direct:myServiceList").routeId("wallet:myServiceList").marshal().json(JsonLibrary.Jackson).unmarshal()
                .json(JsonLibrary.Jackson, ServiceList.class).loop(simple("${body.tokenSymbols.size}"))
                .process(exchange -> {
                    Integer count = exchange.getProperty(Exchange.LOOP_INDEX, Integer.class);
                    ServiceList sl = exchange.getIn().getBody(ServiceList.class);
                    String symbol = sl.getTokenSymbols().get(count);
                    GetToken body = new GetToken(symbol);
                    GetContract query = new GetContract(Arrays.asList("ERC20:ERC20"), body);
                    exchange.getIn().setBody(query);
                }).enrich("direct:restful-daml-query-contract", new MongoOperationAggregationStrategy())
                // if - else 721 query - append
                .end().to("direct:response-200");
        from("direct:getFTBalanceList").routeId("wallet:getFTBalanceList").to("mock:up");
        from("direct:getNFTBalanceList").routeId("wallet:getNFTBalanceList").to("mock:up");
        from("direct:getContract").routeId("wallet:getContract").to("mock:up");
        from("direct:distribution").routeId("wallet:distribution").to("mock:up");
    }
}
