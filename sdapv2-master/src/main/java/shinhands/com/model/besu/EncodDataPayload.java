package shinhands.com.model.besu;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EncodDataPayload {
    private String contractAddress;
    private String method;// 'mint' | 'burn';
    private String amount;
    private Boolean dividable; // 소수점단위 전송 가능 여부
    private Object saveData;
}
