package shinhands.com.model.hub.misc.manage;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Policy {
    private Integer pwdrulemin;
    private Integer pwdrulemax;
    private Integer pwdruleperiod;
    private Integer pwdrulefailnotlogin;
    private Integer pwdrulefailcntlimit;
    private Integer pwdruleanywhereable;
    private Integer pwdruleincnum;
    private Integer pwdruleincspecial;
    private Integer pwdruleincupper;
    private Integer pwdruleinclower;
    private Integer sstimeoutable;
    private Integer sstimeoutmin;
    private Integer dapconsoid;
    private Integer dapprivacygrpid;
    private Integer ctrlimitwon;
}
