package shinhands.com.routes;

import javax.ws.rs.core.Response;

import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;

/**
 * 외부 서비스 호출시 500 에러 응답을 받았을때 처리 end() 설명 https://stackoverflow.com/questions/33800993/camel-using-end
 */
public class HttpErrorRoute extends RouteBuilder {

    private static String logName = HttpErrorRoute.class.getName();

    @Override
    public void configure() throws Exception {
        /**
         * 500-Internal-Server-Error 응답을 JSON 형식으로 반환
         */
        from("direct:common-500").routeId("common-500-http-code-route")
                .log(LoggingLevel.ERROR, logName, ">>> ${routeId} - IN: headers:[${headers}] - body:[${body}]")
                // .marshal().json(JsonLibrary.Jackson)
                .unmarshal().json(JsonLibrary.Jackson, shinhands.com.model.daml.Error.class).id("log-common-500-request")
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()))
                .id("set-common-500-http-code")
                .setHeader(Exchange.HTTP_RESPONSE_TEXT,
                        constant(Response.Status.INTERNAL_SERVER_ERROR.getReasonPhrase()))
                .id("set-common-500-http-reason").setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
                .id("set-500-json-content-type").setBody()
                .method("errorResponseHelper",
                        "generateErrorResponse(${headers.CamelHttpResponseCode}, ${headers.CamelHttpResponseText}, ${exception}, ${body})")
                .id("set-500-errorresponse-object").end().marshal().json(JsonLibrary.Jackson, true)
                .id("marshal-500-errorresponse-to-json").convertBodyTo(String.class)
                .id("convert-500-errorresponse-to-string")
                .log(LoggingLevel.ERROR, logName, ">>> ${routeId} - OUT: headers:[${headers}] - body:[${body}]")
                .id("log-common-500-response");

        // [2021-08-03 15:14:12]
        // todo 200오류 처리
        from("direct:bad-jsonvalidator-200").routeId("bad-jsonvalidator-200")
            .log(LoggingLevel.ERROR, logName,
                    "[JsonValidationException1]>>> ${routeId} - Caught exception after JSON Schema Validation: ${exception.stacktrace}")
            .id("log-validateErc20JSON-exception")
            .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(Response.Status.BAD_REQUEST.getStatusCode()))
            .setProperty(Exchange.HTTP_RESPONSE_TEXT, constant(Response.Status.BAD_REQUEST.getReasonPhrase()))
            .setBody().method("validationResultHelper", "generateKOValidationResult(${exception.message})")
            .id("set-KO-validationResult").marshal().json(JsonLibrary.Jackson, true)
            .id("marshal-KO-validationResult-to-json")
            .log(LoggingLevel.ERROR, logName,
                    "[JsonValidationException1]>>> ${routeId} - validateERC20 response: headers:[${headers}] - body:[${body}]")
            .id("log-validateErc20JSON-KO-response");

        from("direct:response-200").routeId("response-200-http-code-route")
                // .marshal().json(JsonLibrary.Jackson).convertBodyTo(String.class)
                .log(LoggingLevel.INFO, logName, ">>> ${routeId} - IN: headers:[${headers}] - body:[${body}]")
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(Response.Status.OK.getStatusCode()))
                .setHeader(Exchange.HTTP_RESPONSE_TEXT, constant(Response.Status.OK.getReasonPhrase()))
                .setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
                .setBody()
                .method("responseHelper", "generateResponse(${body})").end();

        from("direct:response-success-200").routeId("response-empty-200-http-code-route")
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(Response.Status.OK.getStatusCode()))
                .setHeader(Exchange.HTTP_RESPONSE_TEXT, constant(Response.Status.OK.getReasonPhrase()))
                .setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
                .setBody()
                .method("responseHelper", "generateSuccessResponse()").end();

        from("direct:bad-response-200").routeId("bad-response-200-http-code-route")
                .log(LoggingLevel.ERROR, logName, ">>> ${routeId} - exception : ${exception}]")
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(Response.Status.OK.getStatusCode()))
                .setHeader(Exchange.HTTP_RESPONSE_TEXT, constant(Response.Status.OK.getReasonPhrase()))
                .setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
                .setBody()
                .method("responseHelper", "generateBadResponse(${exception.message})")
                .end();

        from("direct:custom-http-error").routeId("custom-http-error-route")
                .log(LoggingLevel.INFO, logName, ">>> ${routeId} - IN: headers:[${headers}] - body:[${body}]")
                .id("log-custom-http-error-request")
                .setHeader(Exchange.HTTP_RESPONSE_CODE, simple("${exchangeProperty.httpStatusCode}"))
                .id("set-custom-http-code")
                .setHeader(Exchange.HTTP_RESPONSE_TEXT, simple("${exchangeProperty.httpStatusMsg}"))
                .id("set-custom-http-msg").setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
                .id("set-custom-json-content-type").setBody()
                .method("errorResponseHelper",
                        "generateErrorResponse(${exchangeProperty.errorId}, ${exchangeProperty.errorDescription}, ${exchangeProperty.errorMessage})")
                .id("set-custom-errorresponse-object").end().marshal().json(JsonLibrary.Jackson, true)
                .id("marshal-custom-errorresponse-to-json").convertBodyTo(String.class)
                .id("convert-custom-errorresponse-to-string")
                .log(LoggingLevel.INFO, logName, ">>> ${routeId} - OUT: headers:[${headers}] - body:[${body}]")
                .id("log-custom-http-error-response");
    }

}
