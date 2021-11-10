package shinhands.com.model.daml;

import java.util.List;

import lombok.Data;
import shinhands.com.model.daml.template.DefaultDAMLResult;

@Data
public class DAMLQueryResponse {
    private int status;
    private List<DefaultDAMLResult> result;
}