package shinhands.com.model.message;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BaseResult {
    private String id = java.util.UUID.randomUUID().toString();
    private int code = 0; // 0
    private String description = "";
    private Object message = "";
}
