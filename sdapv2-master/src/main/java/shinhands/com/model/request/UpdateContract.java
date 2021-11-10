package shinhands.com.model.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateContract {
    private String contractId;
    private String templateId;
    private String archivedId;
    @Builder.Default
    private Number status = 0;
    private String contractItem;
    private String execiserChoice;
}