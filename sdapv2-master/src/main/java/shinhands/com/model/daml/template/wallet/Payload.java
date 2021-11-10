package shinhands.com.model.daml.template.wallet;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Payload {
    private String name;
    private String address;
    private String owner;
    private String operator;
    private Boolean paused = false;
    private Boolean pausable = false;
    // private List<Map<String, BigDecimal>> balances;
    private List<Object> services = new ArrayList<>();
}
