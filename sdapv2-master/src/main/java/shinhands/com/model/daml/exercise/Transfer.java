package shinhands.com.model.daml.exercise;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Transfer {
    private String fromID;
    private String toID;
    private String fromAddress;
    private String toAddress;
    private String amount;
}
