package shinhands.com.model.daml.template.erc20;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import lombok.Data;
import lombok.experimental.SuperBuilder;
import shinhands.com.model.daml.template.BaseERCPayload;

@Data
@SuperBuilder(toBuilder = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class ERC20Payload extends BaseERCPayload {
    private BigDecimal totalSupply;
    private Object balances;
}
