package shinhands.com.model.hub.misc.policy.jwtifs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Payload {
    private String userid;
    // private String authGrpNameId;
    private String token;
}
