package shinhands.com.model.repository;

import lombok.Builder;
import lombok.Data;
/**
 * if (payload.serviceType === 'FT') {
        transferData.amount = payload.amount;
      } else if (payload.serviceType === 'NFT') {
        transferData.amount = 'Non Fungible Token';
        transferData.tokenID = payload.tokenID;
      } else if (payload.serviceType === 'GiftCard') {
        transferData.amount = payload.amount;
        transferData.tokenID = payload.tokenID;
      }
 */
@Data
@Builder
public class Transfer {
    private String id;
    private String serviceName;
    private String tokenSymbol;
    private String fromId;
    private String toId;
    private String fromAddress;
    private String toAddress;
    private String amount;
    private String operator;
    private String signatories;
    private String observers;
    private String tokenId;
    private String commitmentHash;
    private String uuid;    
}
