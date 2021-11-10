package shinhands.com.model.daml.template;

import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class DefaultDAMLResult {
    private String templateId;
    private String agreementText;
    private String contractId;
    private List<String> observers;
    private List<String> signatories;
    private Map<String, Object> payload;
    private Map<String, Object> key;
}
