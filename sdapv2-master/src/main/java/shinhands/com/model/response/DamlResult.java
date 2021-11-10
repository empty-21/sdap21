package shinhands.com.model.response;

import lombok.Data;
import shinhands.com.model.daml.DAMLResponse;

@Data
public class DamlResult {
    private Exception exception;
    private DAMLResponse respose;
}
