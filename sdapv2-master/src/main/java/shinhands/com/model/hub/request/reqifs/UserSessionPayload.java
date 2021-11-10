package shinhands.com.model.hub.request.reqifs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserSessionPayload {
    private String token;
    private String userid;
}
