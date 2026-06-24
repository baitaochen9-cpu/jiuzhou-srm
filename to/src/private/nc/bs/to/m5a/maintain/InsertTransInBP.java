package nc.bs.to.m5a.maintain;

import nc.bs.to.m5a.maintain.rule.insert.CheckUnique5ABillCodeRule;
import nc.bs.to.m5a.maintain.rule.insert.CommonTranInBPRule;
import nc.bs.to.m5a.maintain.rule.insert.FillInsertDataRule;
import nc.bs.to.m5a.maintain.rule.insert.ReWriteFor5AInsertRule;
import nc.bs.to.m5a.maintain.rule.insert.SaveTransInBPRule;
import nc.bs.to.m5a.maintain.rule.pub.CheckDataRule;
import nc.bs.to.m5a.maintain.rule.pub.PubFillDateRule;
import nc.bs.to.m5a.plugin.BP5APlugInPoint;
import nc.bs.to.pub.rule.atp.TOATPAfterCheckRule;
import nc.bs.to.pub.rule.atp.TOATPBeforeCheckRule;
import nc.impl.pubapp.pattern.data.bill.BillInsert;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.AroundProcesser;
import nc.vo.pubapp.pattern.log.TimeLog;
import nc.vo.scmpub.rule.StockOrgEnableCheckRule;
import nc.vo.to.m5a.entity.TransInVO;

public class InsertTransInBP {

  public TransInVO[] insert(TransInVO[] bills) {
    AroundProcesser<TransInVO> processer =
        new AroundProcesser<TransInVO>(BP5APlugInPoint.InsertTransInBP);

    // 增加执行前业务规则
    this.addBeforeRule(processer);

    // 增加执行后业务规则
    this.addAfterRule(processer);

    TimeLog.logStart();
    processer.before(bills);
    TimeLog.info("保存前执行业务规则"); /*-=notranslate=-*/

    TimeLog.logStart();
    BillInsert<TransInVO> bo = new BillInsert<TransInVO>();
    TransInVO[] vos = bo.insert(bills);
    TimeLog.info("保存单据到数据库"); /*-=notranslate=-*/

    TimeLog.logStart();
    processer.after(vos);
    TimeLog.info("保存后执行业务规则"); /*-=notranslate=-*/

    return vos;
  }

  private void addAfterRule(AroundProcesser<TransInVO> processer) {

    // 检查单据号是否重复
    IRule<TransInVO> rule = new CheckUnique5ABillCodeRule();
    processer.addAfterRule(rule);

    // 检查调入申请是否超要货月计划
    rule = new CheckDataRule();
    processer.addAfterRule(rule);
    // 回写单据
    rule = new ReWriteFor5AInsertRule();
    processer.addAfterRule(rule);

    // 可用量变更后操作
    rule = new TOATPAfterCheckRule<TransInVO>();
    processer.addAfterRule(rule);

  }

  private void addBeforeRule(AroundProcesser<TransInVO> processer) {
    // 填充规则
    IRule<TransInVO> rule = new FillInsertDataRule();
    processer.addBeforeRule(rule);

    // 补充计划发货日期
    rule = new PubFillDateRule();
    processer.addBeforeRule(rule);

    // 保存自身检查规则
    rule = new SaveTransInBPRule();
    processer.addBeforeRule(rule);

    // 保存通用检查规则
    rule = new CommonTranInBPRule();
    processer.addBeforeRule(rule);

    rule = new StockOrgEnableCheckRule<TransInVO>();
    processer.addBeforeRule(rule);

    // 可用量变更前操作
    rule = new TOATPBeforeCheckRule<TransInVO>();
    processer.addBeforeRule(rule);

  }

}
