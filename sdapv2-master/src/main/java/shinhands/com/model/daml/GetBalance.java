package shinhands.com.model.daml;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetBalance {
    private String serviceName;
    private String tokenSymbol;
    private String address;
    private Object balance;
    private String uuid;
}
