package shinhands.com.model.besu;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransferGiftCardBesupayload {
    private String sender;
    private String receiver;
    private String amount;
    private Number tokenID;
    private Boolean dividable;
    private String NFTContract;
    private String FTContract;
    private Object SaveDBResult;
}
