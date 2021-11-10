package shinhands.com.model.request;

import lombok.Data;

@Data
public class TranferPayload {
    private String fromAddress;
    // private String fromid;
    private String toAddress;
    // toAddress를 통해 사용자 id를 조회하므로 해당 필드는 필수값이 아님
    private String toid;
    private String amount;
    private String uuid;
}