package shinhands.com.model.hub.misc.asset;

import lombok.Data;

@Data
public class Transfer {
    private String fromAddress;
    private String toid;
    private String toAddress;
    private String amount;
    private String uuid;
}
