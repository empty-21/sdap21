package shinhands.com.model.repository;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Contract {
    private String contractId;
    private String parentId;
    private String archivedId;
    private String signatories;
    private String observers;
    private String templateId;
    private String execiserChoice;
    @Builder.Default
    private int status = 0;
    private String contractItem;
    private String uuid;
    private String tokenSymbol;
}
