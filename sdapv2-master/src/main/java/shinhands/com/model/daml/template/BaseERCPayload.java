package shinhands.com.model.daml.template;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder(toBuilder = true)
public class BaseERCPayload {
    private String parentId;
    private String serviceName;
    private String tokenSymbol;
    private String symbolImage;
    private String category;
    private String uuid;
    private Boolean pausable;
    private Boolean mintable;
    private Boolean burnable;
    private Boolean distributable;
    private Boolean dividable;
    private Integer decimals;
    private Boolean delegable;

    private List<String> userIDs;
    private List<String> lockedUserIDs;

    private String owner;
    private String ownerAddress;
    private String issuer;
    private String issuerAddress;
    private String operator;
    private List<String> observers;
    private String blockchainAddr;
}
