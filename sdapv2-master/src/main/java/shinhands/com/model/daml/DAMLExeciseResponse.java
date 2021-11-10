package shinhands.com.model.daml;

import lombok.Data;
import shinhands.com.model.daml.template.DAMLEventResult;
@Data
public class DAMLExeciseResponse {
    private int status;
    private DAMLEventResult result;
}