package shinhands.com.model.hub.misc.assetGiftCard;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class mintAsset {
    private String userId;
    private String issuerAddress;
    private String issuer;
    private String uuid;
    private String address;
    private String amount;
    private String tokenName;
    private Number tokenImageNum;
    private String files;// : Express.Multer.File[];
}
