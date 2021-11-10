package shinhands.com.model.hub.misc.assetRight;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FetchAsset {
    private String userId;
    private Number page;
    private Number rows;
    private String address;
}
