package shinhands.com.model.request;

import java.util.List;

import lombok.Data;

@Data
public class DistributionPayload {
    private List<DistributeAccount> accounts;
    private String address;
    private String uuid;
}
