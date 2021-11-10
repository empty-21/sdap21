package shinhands.com.model.hub.request.reqifs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginPayload {
    private String userid;
    private String secret;
    private String userip;
}
