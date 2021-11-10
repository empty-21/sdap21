package shinhands.com.model.message;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import shinhands.com.model.daml.DAMLResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "id", "int", "data", "description", "messages" })
// @RegisterForReflection
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DAMLResult extends BaseResult {
    private DAMLResponse data;
}
