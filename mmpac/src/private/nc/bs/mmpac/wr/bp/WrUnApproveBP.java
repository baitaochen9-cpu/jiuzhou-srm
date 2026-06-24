package nc.bs.mmpac.wr.bp;

import nc.bs.mmpac.wr.plugin.WrPluginPoint;
import nc.bs.mmpac.wr.rule.audit.WrAuditSetValueRule;
import nc.bs.mmpac.wr.rule.audit.WrUnApproveSetMarkRule;
import nc.bs.mmpac.wr.rule.audit.WrUnAuditHasForwardBillRule;
import nc.bs.mmpac.wr.rule.audit.WrUnAuditValidateForFlowRule;
import nc.bs.mmpac.wr.rule.batch.WrDeleteProdAndInBatchRelRule;
import nc.bs.mmpac.wr.rule.lims.WrUnPushLimsRule;
import nc.impl.pubapp.pattern.rule.ICompareRule;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.util.mmf.framework.gc.GCUpdateBPTemplate;
import nc.vo.mmpac.wr.entity.AggWrVO;

public class WrUnApproveBP {
    // 是否工序完工报告弃审调用
    // 1 工序完工报告触发的弃审 2 收货单触发的弃审 0 手工删除
    private int unApproveSrc;

    public AggWrVO[] unAudit(AggWrVO[] fullBills, AggWrVO[] originBills, int issfc) {
        this.setUnApproveSrc(issfc);
        // 调用修改模板
        GCUpdateBPTemplate<AggWrVO> bp = new GCUpdateBPTemplate<AggWrVO>(WrPluginPoint.UNAPPROVE);
        // 执行前规则
        this.addBeforeRule(bp.getAroundProcesser());
        
        this.addAfterRule(bp.getAroundProcesser());
        // 执行更新操作
        return bp.update(fullBills, originBills);
    }

    /**
     * 
     * 
     * @return
     */
    /** @param processor */
    private void addAfterRule(@SuppressWarnings("unused") CompareAroundProcesser<AggWrVO> processor) {
    	/*取消审批不需要同步LIMS 
    	 * @hew 20220606
    	 * */
//    	 processor.addAfterRule(new WrSetBackNumRule());
//    	 processor.addAfterRule(new WrUnPushLimsRule());


    }
    protected void addBeforeRule(CompareAroundProcesser<AggWrVO> processor) {
        // 弃审合法校验规则
        ICompareRule<AggWrVO> wrUnAuditValidateForFlowRule = new WrUnAuditValidateForFlowRule(this.unApproveSrc);
        processor.addBeforeRule(wrUnAuditValidateForFlowRule);
        // 是否有下游单据
        IRule<AggWrVO> wrUnAuditHasForwardBillRule = new WrUnAuditHasForwardBillRule();
        processor.addBeforeRule(wrUnAuditHasForwardBillRule);
        // 删除批次对照表的
        IRule<AggWrVO> wrDelProdAndInBatRelRule = new WrDeleteProdAndInBatchRelRule();
        processor.addBeforeRule(wrDelProdAndInBatRelRule);
        // 取消审批时候需要将齐套检查通过标识置为否 倒冲标识”置为“空”
        processor.addBeforeRule(new WrUnApproveSetMarkRule());
        // 生产报告审核设置默认值，更新审计信息规则
        IRule<AggWrVO> wrAuditSetValueRule = new WrAuditSetValueRule(false, this.unApproveSrc);
        processor.addBeforeRule(wrAuditSetValueRule);

    }

    public int getUnApproveSrc() {
        return this.unApproveSrc;
    }

    public void setUnApproveSrc(int unApproveSrc) {
        this.unApproveSrc = unApproveSrc;
    }
}
