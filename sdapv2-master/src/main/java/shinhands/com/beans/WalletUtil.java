package shinhands.com.beans;

import java.util.List;
import java.util.Map;

import org.apache.camel.Exchange;

import lombok.extern.slf4j.Slf4j;
import shinhands.com.exception.ConflictError;
import shinhands.com.model.daml.DAMLQueryResponse;
import shinhands.com.model.daml.DAMLResponse;
import shinhands.com.model.daml.Request;
import shinhands.com.model.daml.template.DefaultDAMLResult;
import shinhands.com.model.daml.template.Query;
import shinhands.com.model.response.DamlResult;

@Slf4j
public class WalletUtil {
    private WalletUtil() {}
    public static DefaultDAMLResult wallet(Exchange ex, String owner,String address, String queryOwner) {
        DAMLQueryResponse wallet = new Ledger().getActiveContractsByQuery(ex, Query.builder()
                                                            .templateIds(List.of("Wallet:Wallet"))
                                                            .query(Map.of("owner",owner,"address",address)
                                                        ).build(),
                                                        queryOwner);
        if (wallet.getStatus() == 200 && !wallet.getResult().isEmpty()) {
            return wallet.getResult().get(0);
        } else {
            return null;
        }
    }

    public static DefaultDAMLResult wallet(Exchange ex, String owner,String address) {
        return WalletUtil.wallet(ex, owner, address, owner);
    }

    public static Boolean exist(Exchange ex, String owner,String address) {
        return WalletUtil.wallet(ex, owner, address, owner) != null;
    }

    public static DamlResult create(Exchange ex, String owner, String address) {
        DamlResult result = new DamlResult();
        // TODO
        // 응답값 관리
        /*

        */
        DefaultDAMLResult user = UserUtil.get(ex,owner);
        // if (user != null) {
        //     result.setException(new ConflictError(" 이미 등록된 사용자입니다."));
        // } else {
        if (user == null) {
            DAMLResponse create = UserUtil.create(ex,owner);
            if (create.getStatus() != 200) {
                log.error("DAML 사용자 생성 오류");
                result.setException(new ConflictError("DAML 사용자 생성 오류"));
                return result;
            }
        }

        DAMLResponse res = new Ledger().create(ex,Request.builder()
                                    .templateId("Wallet:Wallet")
                                    .payload(Map.of(
                                        "name",owner,
                                        "paused",false,
                                        "pausable",true,
                                        "owner",owner,
                                        "address",address,
                                        "services",List.of(),
                                        "operator","dap_operator")).build()
                                    ,owner);
        result.setRespose(res);
        // }
        return result;
    }
}
