package shinhands.com.model.request;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ServiceProposalPayload {
    private String uuid = UUID.randomUUID().toString();
    private String userid;
    private String serviceName;
    private String tokenSymbol;
    private String symbolImage;
    private BigDecimal totalSupply;
    // private List<Map<String,Boolean>> attribute;
    private Object attribute;
    private String issuer;
    private String operator = "dap_operator";
    private List<String> signatories;
    private String category;
    private String ownerAddress;
    private String issuerAddress;
    private String owner;
    private List<String> observers;
    private String serviceType;

    // DAML
    private Object service;
    private List<String> alreadySignedParties;
    private List<String> rejectedParties;
    private String rejectedReason;
}