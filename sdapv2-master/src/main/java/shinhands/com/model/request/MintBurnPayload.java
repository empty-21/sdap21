package shinhands.com.model.request;

import lombok.Data;


@Data
public class MintBurnPayload {
    private String tokenSymbol;
    private String address;
    private String amount;
    private String uuid;
}
