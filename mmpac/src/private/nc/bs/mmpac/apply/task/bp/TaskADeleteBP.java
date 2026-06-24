package nc.bs.mmpac.apply.task.bp;

import nc.bs.mmpac.apply.task.plugin.TaskAPluginPoint;
import nc.bs.mmpac.apply.task.rule.CheckUpdateWGBGRule;
import nc.bs.mmpac.apply.task.rule.CheckUpdateWGBGRule2;
import nc.bs.mmpac.wr.rule.backflush.WrSetBackNumRule;
import nc.bs.pubapp.pub.rule.ReturnBillCodeRule;
import nc.impl.pubapp.pattern.data.bill.template.DeleteBPTemplate;
import nc.impl.pubapp.pattern.data.bill.tool.BillTransferTool;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.AroundProcesser;
import nc.util.mmf.framework.base.MMValueCheck;
import nc.vo.mmpac.apply.task.consts.TaskABillConsts;
import nc.vo.mmpac.apply.task.entity.AggTaskAVO;
import nc.vo.mmpac.apply.task.entity.TaskAHVO;

/**
 * 作业申报删除 BP
 */
public class TaskADeleteBP {

    public void delete(AggTaskAVO[] vos) {

        if (MMValueCheck.isEmpty(vos)) {
            return;
        }

        // 加锁 比较ts
        BillTransferTool<AggTaskAVO> transferTool = new BillTransferTool<AggTaskAVO>(vos);

        // 补全VO
        AggTaskAVO[] fullBills = transferTool.getClientFullInfoBill();

        // 创建删除数据模板BP
        DeleteBPTemplate<AggTaskAVO> bp = new DeleteBPTemplate<AggTaskAVO>(TaskAPluginPoint.DELETE);

        // 注入删除前规则
        this.addBeforeRule(bp.getAroundProcesser());

        // 注入删除后规则
        this.addAfterRule(bp.getAroundProcesser());

        // 执行删除操作
        bp.delete(fullBills);
    }

    /**
     * 删除前业务规则
     * 
     * @param processer
     */
    private void addBeforeRule(AroundProcesser<AggTaskAVO> processer) {
    	 
    }

    /**
     * 删除后业务规则
     * 
     * @param processer
     */
    @SuppressWarnings("unchecked")
    private void addAfterRule(AroundProcesser<AggTaskAVO> processer) {
    	 
        // 退回单据号规则
        IRule<AggTaskAVO> rule =
                new ReturnBillCodeRule(TaskABillConsts.TASKA_BILLTYPE, TaskAHVO.VBILLCODE, TaskAHVO.PK_GROUP,
                        TaskAHVO.PK_ORG);
//        processer.addAfterRule(new WrSetBackNumRule());
       
   	 
        
        processer.addAfterRule(rule);
        rule = new CheckUpdateWGBGRule2();
        processer.addAfterRule(rule);
    }
}
