package shinhands.com.model.hub.misc.asset;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ServiceProposal {
    private String serviceName;
    private String tokenSymbol;
    private String totalSupply;
    private String attribute;
    private String signatories;
    private String ownerAddress;
    private String issuerAddress;
    private String owner;
    private String category;
    private String issuer;
    private String observers;
    private String serviceType;
}
