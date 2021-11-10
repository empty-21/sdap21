package shinhands.com.model.hub.response.resIfs;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class APIResponse {
    private Number code;
    private String message;
    private Map<Object, Object> body;
}
