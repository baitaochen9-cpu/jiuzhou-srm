package nc.bs.mmpac.wr.bp;

import nc.bs.mmpac.wr.plugin.WrPluginPoint;
import nc.bs.mmpac.wr.rule.audit.WrAuditSetValueRule;
import nc.bs.mmpac.wr.rule.audit.WrAuditValidateForFlowRule;
import nc.bs.mmpac.wr.rule.createpick.WrQueryBackFlushFlagRule;
import nc.bs.mmpac.wr.rule.lims.WrPushLimsRule;
import nc.impl.pubapp.pattern.rule.ICompareRule;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.util.mmf.framework.gc.GCUpdateBPTemplate;
import nc.vo.mmpac.wr.entity.AggWrVO;

/**
 * 生产报告审批BP
 * 
 * @since 6.0
 * @version 2013-5-27 下午08:40:56
 * @author liweiz
 */
public class WrApproveBP {
    public AggWrVO[] audit(AggWrVO[] fullBills, AggWrVO[] originBills) {
        // 调用修改模板
        GCUpdateBPTemplate<AggWrVO> bp = new GCUpdateBPTemplate<AggWrVO>(WrPluginPoint.AUDITEUPDATE);
        // 执行前规则
        this.addBeforeRule(bp.getAroundProcesser());
        // 执行后规则
        this.addAfterRule(bp.getAroundProcesser());
        // 执行更新操作
        return bp.update(fullBills, originBills);
    }

    private void addBeforeRule(CompareAroundProcesser<AggWrVO> processor) {
        // 审核合法校验规则
        ICompareRule<AggWrVO> wrAuditValidateForFlowRule = new WrAuditValidateForFlowRule();
        processor.addBeforeRule(wrAuditValidateForFlowRule);
        // 生产报告审核设置默认值，更新审计信息规则,这里第二个参数象征性的传入，不起任何作用
        IRule<AggWrVO> wrAuditSetValueRule = new WrAuditSetValueRule(true, 0);
        processor.addBeforeRule(wrAuditSetValueRule);
        // 查询备料计划或小备料表倒冲标识给报告赋值 无订单的场景做不到
        processor.addBeforeRule(new WrQueryBackFlushFlagRule());

    }

    /**
     * 
     * @return
     */
    /** @param processor */
    private void addAfterRule( CompareAroundProcesser<AggWrVO> processor) {
//    	 processor.addAfterRule(new WrSetBackNumRule());
//    	 //增加审批自动推送LIMS
    	 processor.addAfterRule(new WrPushLimsRule());

    }

    /**
     * 审核完成后，新开事务进行自动倒冲
     * 该方法将在V631版本废弃掉
     * 
     * @return
     */
    @Deprecated
    protected IRule<AggWrVO> getWrAuditAutoBackflushRule() {
        return null;
    }

}
