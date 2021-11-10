package shinhands.com.model.daml.template.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JoinService {
    private String uuid;
    private String operator;
    private String owner;
    private String address;
    private String blockchainAddr;
    private String tokenSymbol;
    private String serviceName;
    private String serviceType;
}
