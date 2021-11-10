package shinhands.com.model.hub.misc.asset;

import java.util.UUID;

import lombok.Data;

@Data
// @Builder
public class ServiceJoin {
    private String userid;
    private String serviceName;
    private String tokenSymbol;
    // 사용자 지갑 주소
    private String address;
    private String uuid;
    // @Builder.Default
    private String operator = "dap_operator";
    private String owner;
    // @Builder.Default
    private String serviceType = "";
    // @Builder.Default
    private String blockchainAddr = UUID.randomUUID().toString();
    private String walletAddressContractId;
}
