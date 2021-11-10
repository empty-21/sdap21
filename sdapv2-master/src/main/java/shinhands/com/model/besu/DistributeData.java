package shinhands.com.model.besu;

import java.util.List;

import lombok.Builder;
import lombok.Data;
import shinhands.com.model.request.DistributeAccount;

@Data
@Builder
public class DistributeData {
    private List<DistributeAccount> accounts; // { amount: string; address: string }[];
    private Boolean dividable;
}