package shinhands.com.model.hub.misc.asset;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Deposit {
    private String uuid;
    private String amount;
    private String address;
}
