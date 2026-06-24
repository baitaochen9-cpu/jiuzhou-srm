//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package nc.impl.pp.m28.action;

import nc.bs.pp.m28.maintain.rule.insert.PAuditAfterMarAssRule;
import nc.bs.pp.m28.plugin.PriceAuditPluginPoint;
import nc.bs.pub.compiler.AbstractCompiler2;
import nc.bs.scmpub.pf.PfParameterUtil;
import nc.impl.pp.m28.action.approve.PushOaRule;
import nc.impl.pp.m28.action.rule.UpdateInfoRule;
import nc.impl.pp.m28.action.rule.approve.CheckOrderFlagRule;
import nc.impl.pp.m28.action.rule.commit.ChangeBillStatusRule;
import nc.impl.pp.m28.action.rule.commit.ChkCanCommitRule;
import nc.impl.pubapp.pattern.data.bill.BillQuery;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.pp.m28.entity.PriceAuditVO;
import nc.vo.pu.pub.util.AggVOUtil;

public class PriceAuditCommitAction {
    public PriceAuditCommitAction() {
    }

    public PriceAuditVO[] commit(PriceAuditVO[] vos, AbstractCompiler2 script) {
        PfParameterUtil<PriceAuditVO> tool = new PfParameterUtil(script == null ? null : script.getPfParameterVO(), vos);
        PriceAuditVO[] fullVos = (PriceAuditVO[])tool.getClientFullInfoBill();
        PriceAuditVO[] orgVos = (PriceAuditVO[])tool.getOrginBills();
        CompareAroundProcesser<PriceAuditVO> processer = new CompareAroundProcesser(PriceAuditPluginPoint.COMMIT_BP);
        this.addRule(processer, script);
        processer.before(fullVos, orgVos);
        processer.after(fullVos, orgVos);
        String[] ids = AggVOUtil.getPrimaryKeys(fullVos);
        PriceAuditVO[] approvedVos = (PriceAuditVO[])(new BillQuery(PriceAuditVO.class)).query(ids);
        PAuditAfterMarAssRule marassRule = new PAuditAfterMarAssRule();
        marassRule.process(approvedVos);
        PushOaRule rule = new PushOaRule();
        rule.process(approvedVos);
        return approvedVos;
    }

    private void addRule(CompareAroundProcesser<PriceAuditVO> processer, AbstractCompiler2 script) {
        if (processer != null && script != null) {
            processer.addBeforeFinalRule(new ChkCanCommitRule());
            processer.addBeforeFinalRule(new ChangeBillStatusRule());
            processer.addAfterFinalRule(new CheckOrderFlagRule());
            processer.addAfterFinalRule(new UpdateInfoRule());
        }
    }
}
