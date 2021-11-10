package shinhands.com.model.request;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ServiceFinalizePayload {
    private String uuid;
    private String userid;
    private String serviceName;
    private String tokenSymbol;
    private Number totalSupply;
    private Object attribute;
    private String issuer;
    private List<String> signatories;
    private String category;
    private String ownerAddress;
    private String issuerAddress;
    private String owner;
    private List<String> observers;
    private String serviceType;
}