package shinhands.com.model.hub.misc.policy.userifs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDetail {
    private Number id;
    private String name;
    private String email;
    private Number roleId;
    private String createdAt;
    private String updatedAt;
}
