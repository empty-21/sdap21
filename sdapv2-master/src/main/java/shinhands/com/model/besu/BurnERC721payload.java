package shinhands.com.model.besu;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BurnERC721payload {
    private String contractAddress;
    private Number tokenId;
}
