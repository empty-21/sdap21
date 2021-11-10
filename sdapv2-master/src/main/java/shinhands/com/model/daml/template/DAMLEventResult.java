package shinhands.com.model.daml.template;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;


@Data
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DAMLEventResult {
    private List<Object> events;
    // event.created.contractId 와 동일함
    private String exerciseResult;
}
