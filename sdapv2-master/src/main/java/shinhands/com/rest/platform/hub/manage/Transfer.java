package shinhands.com.rest.platform.hub.manage;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.core.MediaType;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jsonvalidator.JsonValidationException;
import org.apache.camel.model.dataformat.JsonLibrary;

import shinhands.com.exception.ConflictError;
import shinhands.com.exception.DefaultException;
import shinhands.com.exception.UnauthorizedError;
import shinhands.com.processor.JwtVerfyProcessor;

@ApplicationScoped
public class Transfer extends RouteBuilder {

    @Inject
    JwtVerfyProcessor jwtVerfyProcessor;

    @Override
    public void configure() {
        onException(Exception.class).handled(true).maximumRedeliveries(0).to("direct:common-500");
        onException(DefaultException.class).handled(true).maximumRedeliveries(0).to("direct:bad-response-200");
        onException(ConflictError.class).handled(true).maximumRedeliveries(0).to("direct:bad-response-200");
        onException(JsonValidationException.class).handled(true).maximumRedeliveries(0).to("direct:bad-jsonvalidator-200");
        onException(UnauthorizedError.class).handled(true).maximumRedeliveries(0).to("direct:bad-response-200");

        rest("/manage/transfer")
            .consumes(MediaType.APPLICATION_JSON)
            .produces(MediaType.APPLICATION_JSON)
            .get("/").description("DAP-SIF0 (전송 이력 전체 조회)")
                .to("direct:manage-transfer-list")
            .get("/detail").description("DAP-SIF0  전송이력 상세조회 -> 전체 전송이력중 조건으로 가져오기")
                .to("direct:manage-transfer-detail");

        from("direct:manage-transfer-list").routeId("manage:transfer-list")
            .marshal().json(JsonLibrary.Jackson)
            .to("json-validator:json-schema/manage-transfers-schema.json")
            // .process(new PagingValidatorProcessor())
            // .process(new PagingResultProcessor())
            // TODO
            // 구현
//             export async function getTransferAll(
//   page: number,
//   rows: number,
//   address: string | undefined
// ): Promise<any> {
//   const mqManager = getEventManager();
//   try {
//     const mqManager = getEventManager();
//     const payload = await mqManager.emitAndWait('daml.request.TransferList', {
//       page,
//       rows,
//       address
//     });
//     if (!payload.result) {
//       throw new BadRequestError(payload.message);
//     }
//     return payload.message;
//   } catch (e) {
//     throw e;
//   }
// }

            .to("direct:response-success-200");

        from("direct:manage-transfer-detail").routeId("manage:transfer-detail")
        // TODO
        // 구현
//         export async function getTransferDetail(
//   userid: string,
//   page: number,
//   rows: number,
//   uuid: string
// ): Promise<any> {
//   const mqManager = getEventManager();
//   try {
//     // const mqManager = getEventManager();
//     const payload = await mqManager.emitAndWait(
//       'daml.request.TransferListDetail',
//       {
//         userid,
//         page,
//         rows,
//         uuid
//       }
//     );
//     if (!payload.result) {
//       throw new BadRequestError(payload.message);
//     }
//     return payload.message;
//   } catch (e) {
//     throw e;
//   }
// }

            .to("direct:response-success-200");
    }
}