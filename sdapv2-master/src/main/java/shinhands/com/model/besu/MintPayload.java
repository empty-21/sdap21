package shinhands.com.model.besu;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MintPayload {
    private String method;// 'Mint' | 'Burn';
    private String contractAddress;
    private String address;
    private String metaDataUrl;
}
