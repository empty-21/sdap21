package shinhands.com.rest.platform.hub.asset;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.core.MediaType;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.camel.FluentProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jsonvalidator.JsonValidationException;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import lombok.extern.slf4j.Slf4j;
import shinhands.com.aggregation.BesuContractAggregationStrategy;
import shinhands.com.aggregation.ServiceFinalizeAggregationStrategy;
import shinhands.com.aggregation.ServiceIviteAggregationStrategy;
import shinhands.com.aggregation.ServiceProposalAggregationStrategy;
import shinhands.com.aggregation.ServiceRequestAggregationStrategy;
import shinhands.com.beans.ErcUtil;
import shinhands.com.beans.Ledger;
import shinhands.com.beans.RepositoryHelper;
import shinhands.com.beans.ServiceUtil;
import shinhands.com.beans.WalletUtil;
import shinhands.com.enumeration.Route;
import shinhands.com.exception.DefaultException;
import shinhands.com.exception.ForbiddenError;
import shinhands.com.exception.UnauthorizedError;
import shinhands.com.model.besu.EncodDataDistributePayload;
import shinhands.com.model.besu.EncodDataPayload;
import shinhands.com.model.besu.TransferERC20Payload;
import shinhands.com.model.daml.DAMLExeciseResponse;
import shinhands.com.model.daml.DAMLQueryResponse;
import shinhands.com.model.daml.Exercise;
import shinhands.com.model.daml.exercise.Transfer;
import shinhands.com.model.daml.template.DefaultDAMLResult;
import shinhands.com.model.daml.template.Query;
import shinhands.com.model.hub.misc.asset.ServiceJoin;
import shinhands.com.model.repository.Contract;
import shinhands.com.model.request.DistributionPayload;
import shinhands.com.model.request.MintBurnPayload;
import shinhands.com.model.request.ServiceProposalPayload;
import shinhands.com.model.request.TranferPayload;
import shinhands.com.model.response.DamlResult;
import shinhands.com.processor.JwtVerfyProcessor;
import shinhands.com.processor.assset.DistributionProcessor;
import shinhands.com.processor.assset.ERCBurnProcessor;
import shinhands.com.processor.assset.JoinServiceProcessor;
import shinhands.com.processor.proposal.AddTokenInfoForContractTableProcessor;
import shinhands.com.processor.proposal.ServiceAddTokenInfoProcessor;
import shinhands.com.processor.proposal.ServiceRequestProcessor;
import shinhands.com.processor.proposal.SetupBlockChainAddrForContractTableProcessor;

@Slf4j
@ApplicationScoped
public class IPRight extends RouteBuilder {

    @Inject
    JwtVerfyProcessor jwtVerfyProcessor;

    @Inject
    Ledger ledger;

    @ConfigProperty(name = "daml.auth.operator")
    String jwt_operator;

    @Override
    public void configure() {
        onException(Exception.class).handled(true).maximumRedeliveries(0).to(Route.Response.COMMON_RESPONSE_500.label);
        onException(DefaultException.class).handled(true).maximumRedeliveries(0).to(Route.Response.BAD_RESPONSE_200.label);
        onException(ForbiddenError.class).handled(true).maximumRedeliveries(0).to(Route.Response.BAD_RESPONSE_200.label);
        onException(JsonValidationException.class).handled(true).maximumRedeliveries(0).to(Route.Response.BAD_JSONVALIDATOR_200.label);
        onException(UnauthorizedError.class).handled(true).maximumRedeliveries(0).to(Route.Response.BAD_RESPONSE_200.label);

        rest("/member/ipright")
            .consumes(MediaType.APPLICATION_JSON)
            .produces(MediaType.APPLICATION_JSON)
            .get("/").description("DAP-DIF090 ( DAML 발행된 개인저작물 토큰 목록 조회 )")
                .to("direct:ipright-list")
            .get("/metadata").description("DAP-DIF090 ( DAML 발행된 개인저작물 토큰 목록 조회 )")
                .to("direct:ipright-metadata")
            .get("/mint").description("DAP-DIF087 ( ERC721 토큰 발행 )")
                .to("direct:ipright-mint")
            .post("/transfer").description("DAP-DIF088 ( DAML 발행된 개인저작물 전송 )")
                .to("direct:ipright-transfer")
            .post("/burn").description("DAP-DIF089")
                .to("direct:ipright-burn");
        

        from("direct:ipright-list").routeId("ipright:list")
            .process(jwtVerfyProcessor)
            .marshal().json(JsonLibrary.Jackson)
                .to("json-validator:json-schema/nft/nft-tokens-schema.json")
            .unmarshal().json(JsonLibrary.Jackson, ServiceProposalPayload.class)
            .process(ex-> {
                DAMLQueryResponse services = ledger.getActiveContractsByQuery(ex, Query.builder()
                                                    .templateIds(List.of("Service:Service")).build());
                if (services.getStatus() != 200 || services.getResult().size() == 0) {
                    throw new ForbiddenError("서비스가 존재하지 않습니다.");
                }
                List<Object> list = new ArrayList<>();
                for (DefaultDAMLResult result : services.getResult()) {
                    list.add(result.getPayload());
                };
                ex.getIn().setBody(list);
            })
            .to("direct:response-200");
        
        from("direct:ipright-metadata").routeId("ipright:metadata")
            .to("direct:response-200");
        
        from("direct:ipright-mint").routeId("ipright:mint")
            .to("direct:response-200");
        
        from("direct:ipright-transfer").routeId("ipright:transfer")
            .to("direct:response-200");

        from("direct:ipright-burn").routeId("ipright:burn")
            .to("direct:response-200");
        
    }
}