package shinhands.com.model.besu;

import lombok.Builder;
import lombok.Data;
@Data
@Builder
public class TransferERC20Payload {
    private String sender;
    private String receiver;
    private String amount;
    private Boolean dividable;
    private String contractAddress;
    // 명세 확인 필요
    private Object saveDBResult;
}
