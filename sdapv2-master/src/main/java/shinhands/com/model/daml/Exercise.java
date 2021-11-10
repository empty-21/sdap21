package shinhands.com.model.daml;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Exercise {
    private String templateId;
    private String contractId;
    private String choice;
    private Object argument;
}