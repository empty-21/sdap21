package shinhands.com.model.hub.misc.group;

import shinhands.com.entity.Permission;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GroupUpdatePayload {
    private String authgrpid;
    private String authgrpname;
    private String authgrpnameid;
    private Permission permission;
}
