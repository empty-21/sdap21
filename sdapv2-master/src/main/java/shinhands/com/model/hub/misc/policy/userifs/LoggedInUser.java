package shinhands.com.model.hub.misc.policy.userifs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import shinhands.com.model.hub.misc.policy.jwtifs.Payload;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoggedInUser extends Payload {
    private Number sessionId;
}
