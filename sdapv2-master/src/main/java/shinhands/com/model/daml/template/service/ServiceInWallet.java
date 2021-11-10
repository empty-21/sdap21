package shinhands.com.model.daml.template.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// 지갑에 포함된 서비스 항목
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ServiceInWallet {
    private String walletAddress;
    private String type;
    private String symbol;
    private String name;
}
