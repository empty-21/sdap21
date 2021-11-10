package shinhands.com.model.hub.misc.contract;

import lombok.Data;

@Data
public class TransferFlow {
    private String userid;
    private Number page;
    private Number rows;
    private String contractId;
    private String tokenSymbol;
    private String uuid;
}