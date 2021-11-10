package shinhands.com.model.daml.template.service;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class ServiceRequestPayload {
    private String uuid = UUID.randomUUID().toString();
    private String userid;
    private String serviceName;
    private String tokenSymbol;
    private String symbolImage;
    private Number totalSupply = 0;
    // private List<Map<String,Boolean>> attribute;
    private Object attribute;
    private String issuer;
    private String operator = "operator";
    private List<String> signatories;
    private String category;
    private String ownerAddress;
    private String issuerAddress;
    private String owner;
    private List<String> observers;
    private String serviceType;
}
