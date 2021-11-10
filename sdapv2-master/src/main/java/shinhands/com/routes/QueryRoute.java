package shinhands.com.routes;

import javax.enterprise.context.ApplicationScoped;

import org.apache.camel.builder.endpoint.EndpointRouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class QueryRoute extends EndpointRouteBuilder {

    @Override
    public void configure() throws Exception {
        from("direct:sql-get-wallet").routeId("sql-get-wallet-route")
            .to("sql:classpath:sql/vue/common/walletByAddress.sql?outputHeader=sql_get_wallet&outputType=SelectOne")
            .log("# otput [${routeId}] => ${headers.sql_get_wallet}");
        from("direct:sql-get-user").routeId("sql-get-user-route")
            .to("sql:classpath:sql/vue/common/userByUserId.sql?outputHeader=sql_get_user&outputType=SelectOne")
            .log("# [${routeId}] => ${headers.sql_get_user}");
        from("direct:sql-get-wallet-count").routeId("sql-wallet-count-route")
            .to("sql:classpath:sql/vue/common/walletCountByUserId.sql?outputHeader=sql_get_wallet_count&outputType=SelectOne")
            .log("# [${routeId}] => ${headers.sql_get_wallet_count}");
        
        // template를 사용하여 호출시 setBody 필수
        // [2021-10-27 09:14:53]
        // execise이후 contract갱신용
        // ts:async saveContractChoice_Transfer(payload: any)
        from("direct:sql-update-get-contract").routeId("sql-update-get-contract")
            .log("# [${routeId}] => [headers] ${headers} , [body] ${body}")
            .to("sql:classpath:sql/vue/asset/contractUpdate3.sql?outputHeader=result&outputType=SelectOne")
            .log("# [${routeId}] => ${headers.result}")
            .setBody(simple("${headers.result}"));
        
        // contract 저장 [2021-10-13 18:05:50]
        from("direct:sql-save-contract").routeId("sql-save-contract")
            .log("# [${routeId}] => [headers] ${headers} , [body] ${body}")
            .to("sql:classpath:sql/vue/asset/contractSave3.sql?outputHeader=result&outputType=SelectOne")
            .log("# [${routeId}] => ${headers.result}")
            .setBody(simple("${headers.result}"));

        // 전송 기록 저장
        from("direct:sql-save-transfer").routeId("sql-update-save-transfer")
            .log("# [${routeId}] => [headers] ${headers} , [body] ${body}")
            .to("sql:classpath:sql/vue/asset/transferSave.sql?outputHeader=result&outputType=SelectOne")
            .log("# [${routeId}] => ${headers.result}")
            .setBody(simple("${headers.result}"));

        from("direct:sql-get-wallet2").routeId("sql-get-wallet2")
            .to("direct:sql-get-wallet")
            .setBody(simple("${headers.sql_get_wallet}"));
        
        from("direct:sql-get-user2").routeId("sql-get-user2")
            .to("direct:sql-get-user")
            .setBody(simple("${headers.sql_get_user}"));
    }
}
