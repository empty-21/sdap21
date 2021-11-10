package shinhands.com.model.besu;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EncodDataDistributePayload {
    private DistributeData distributeData;
    private String contractAddress;
}
