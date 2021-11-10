package shinhands.com.model.besu;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransferERC721Payload {
    private String contractAddress;
    private String sender;
    private String receiver;
    private Number tokenID;
    // 타입 확인필요
    private String SaveDBResult;
}
