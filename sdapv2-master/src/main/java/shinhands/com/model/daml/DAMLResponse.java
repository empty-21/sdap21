package shinhands.com.model.daml;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import lombok.Data;
import shinhands.com.model.daml.template.DefaultDAMLResult;

@Data
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class DAMLResponse {
    private int status;
    private DefaultDAMLResult result;
}