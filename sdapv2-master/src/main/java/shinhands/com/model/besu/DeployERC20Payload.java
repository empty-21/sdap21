package shinhands.com.model.besu;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DeployERC20Payload {
    private BigDecimal totalSupply;
    private String tokenName;
    private String tokenSymbol;
    private Integer decimals;
    private Boolean burnable;
    private Boolean mintable;
    private Boolean distributable;
    // address
    private String owner;
}