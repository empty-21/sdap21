package shinhands.com.model.hub.misc.assetRight;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Burn {
    private String address;
    private Number tokenId;
    private String signData;
    private String tokenSymbol;
    private String uuid;
}
