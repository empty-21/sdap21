package shinhands.com.model.hub.misc.policy.userifs;

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
public class UserPayload {
    private String userid;
    private String secret;
    private String username;
    private String useremail;
    private String usercompany;
    private String userdepart;
    private String usertel;
    private String userstat;
    // 로그인시 패스워드 유효성 체크
    private Boolean validsecret;
    private String authgrp = "COMMON_USER";
}
