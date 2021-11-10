package shinhands.com.model.hub.misc.assetRight;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Transfer {
    private String fromAddress;
    private String toAddress;
    private Number tokenId;
    private String signData;
    // tokenSymbol: string;
    private String uuid;
}
