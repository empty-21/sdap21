package shinhands.com.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetNFTBalance {
    private String serviceName;
    private String tokenSymbol;
    private String address;
    private Object tokens;
    private String uuid;
}
