package shinhands.com.model.hub.misc.manage;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Notify {
    private String fromid;
    private String toid;
    private String notimsg;
}
