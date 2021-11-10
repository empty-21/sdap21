package shinhands.com.beans;

import java.util.Map;

import javax.enterprise.context.ApplicationScoped;

import org.apache.camel.Exchange;
import org.apache.camel.FluentProducerTemplate;
import org.apache.camel.http.common.HttpMethods;

import shinhands.com.model.daml.DAMLExeciseResponse;
import shinhands.com.model.daml.DAMLQueryResponse;
import shinhands.com.model.daml.DAMLResponse;
import shinhands.com.model.daml.Exercise;
import shinhands.com.model.daml.Request;
import shinhands.com.model.daml.template.Query;
import shinhands.com.util.TokenUtils;

@ApplicationScoped
public class Ledger {
    // todo
    // [2021-09-06 09:47:51] bind operator id
    private final String OPERATOR = "dap_operator";

    public DAMLResponse create(Exchange exchange, Request request) {
        return create(exchange.getContext().createFluentProducerTemplate(), exchange.getIn().getHeaders(), request);
    }

    public DAMLResponse create(Exchange exchange, Request request, String userid) {
        return create(exchange.getContext().createFluentProducerTemplate(), exchange.getIn().getHeaders(),request, userid);
    }

    public DAMLResponse create(FluentProducerTemplate template, Map<String,Object> heders, Request request) {
        return create(template, heders, request, OPERATOR);
    }

    public DAMLResponse create(FluentProducerTemplate template, Map<String,Object> heders, Request request, String userid) {
        return template.withHeaders(heders)
            .withHeader("Authorization", "Bearer "+TokenUtils.generateDamlTokenString(userid))
            .withHeader(Exchange.HTTP_METHOD, HttpMethods.POST)
            .withBody(request)
        .to("direct:restful-daml-create-contract")
        .request(DAMLResponse.class);
    }

    /**
     * execise
     * @param exchange
     * @param exercise
     * @return
     */
    public DAMLExeciseResponse execise(Exchange exchange, Exercise exercise) {
        return execise(exchange.getContext().createFluentProducerTemplate(), exchange.getIn().getHeaders(),exercise);
    }

    public DAMLExeciseResponse execise(Exchange exchange, Exercise exercise, String userid) {
        return execise(exchange.getContext().createFluentProducerTemplate(), exchange.getIn().getHeaders(),exercise, userid);
    }

    public DAMLExeciseResponse execise(FluentProducerTemplate template, Map<String,Object> heders, Exercise exercise) {
        return execise(template, heders, exercise, OPERATOR);
    }

    public DAMLExeciseResponse execise(FluentProducerTemplate template, Map<String,Object> heders, Exercise exercise, String userid) {
        return template.withHeaders(heders)
            .withHeader("Authorization", "Bearer "+TokenUtils.generateDamlTokenString(userid))
            .withHeader(Exchange.HTTP_METHOD, HttpMethods.POST)
            .withBody(exercise)
        .to("direct:restful-daml-exercise-contract")
        .request(DAMLExeciseResponse.class);
    }

    /**
     * getActiveContractsByQuery
     * @param exchange
     * @param query
     * @return
     */
    public DAMLQueryResponse getActiveContractsByQuery(Exchange exchange, Query query) {
        return getActiveContractsByQuery(exchange.getContext().createFluentProducerTemplate(), exchange.getIn().getHeaders(),query);
    }

    public DAMLQueryResponse getActiveContractsByQuery(Exchange exchange, Query query, String userid) {
        return getActiveContractsByQuery(exchange.getContext().createFluentProducerTemplate(), exchange.getIn().getHeaders(),query, userid);
    }

    public DAMLQueryResponse getActiveContractsByQuery(FluentProducerTemplate template, Map<String,Object> heders, Query query) {
        return getActiveContractsByQuery(template, heders, query, OPERATOR);
    }

    public DAMLQueryResponse getActiveContractsByQuery(FluentProducerTemplate template, Map<String,Object> heders, Query query, String userid) {
        return template.withHeaders(heders)
            .withHeader("Authorization", "Bearer "+TokenUtils.generateDamlTokenString(userid))
            .withHeader(Exchange.HTTP_METHOD, HttpMethods.POST)
            .withBody(query)
        .to("direct:restful-daml-query-contract")
        .request(DAMLQueryResponse.class);
    }
}