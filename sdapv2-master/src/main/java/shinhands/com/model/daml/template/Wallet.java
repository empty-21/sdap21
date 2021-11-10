package shinhands.com.model.daml.template;

import lombok.Data;
import shinhands.com.model.daml.template.wallet.Payload;

@Data
public class Wallet {
    private String templateId = "Wallet:Wallet";
    private Payload payload;
}
