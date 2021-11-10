package shinhands.com.model.hub.misc.policy.userifs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserSetFidoPayload {
    private String fidoKey;
}
