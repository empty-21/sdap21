package shinhands.com.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// @Data
// @Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DistributeAccount {
    private String amount;
    private String address;
    
    // public DistributeAccount(String amount, String address) {
    //     this.amount = amount;
    //     this.address = address;
    // }
    
}
