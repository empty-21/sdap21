package shinhands.com.model.hub.misc.policy;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Info {
    private Number pwdRuleMin;
    private Number pwdRuleMax;
    private Number pwdRulePeriod;
    private Number pwdRuleFailNotLogin;
    private Number pwdRuleFailCntLimit;
    private Number pwdRuleAnywhereAble;
    private Number pwdRuleIncNum;
    private Number pwdRuleIncSpecial;
    private Number pwdRuleIncUpper;
    private Number pwdRuleIncLower;
    private Number ssTimeOutAble;
    private Number ssTimeOutMin;
    private Number dapConsoId;
    private Number dapPrivacyGrpId;
    private Number ctrLimitWon;
}
