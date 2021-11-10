package shinhands.com.model.hub.misc.manage;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SaveAudit {
    private String userId;
    private String url;
    private Date runAt;
}
