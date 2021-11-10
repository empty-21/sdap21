package shinhands.com.model.hub.misc.asset;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Swap {
    private Sell Sell;
    private Buy buy;
    private String address;
}

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
class Sell {
    private String uuid;
    private String amount;
}

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
class Buy {
    private String uuid;
    private String amount;
}
