package shinhands.com.model.hub.misc.assetGiftCard;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Assignment {
    private String userId;
    private String uuid;
    private String toAddress;
    private String fromAddress;
    private Number tokenId;
}
