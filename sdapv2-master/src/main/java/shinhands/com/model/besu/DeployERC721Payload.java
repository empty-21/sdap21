package shinhands.com.model.besu;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DeployERC721Payload {
    private String tokenName;
    private String tokenSymbol;
}
