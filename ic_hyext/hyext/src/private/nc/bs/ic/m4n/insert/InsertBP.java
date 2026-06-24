package nc.bs.ic.m4n.insert;

import nc.bs.ic.m4n.base.BPPluginPoint;
import nc.bs.ic.m4n.insert.rule.BillDataCheck;
import nc.bs.ic.m4n.insert.rule.CheckBodyNumRule;
import nc.bs.ic.m4n.insert.rule.RecordBeforebid;
import nc.bs.ic.m4n.insert.rule.SpecialBillDataIDValueSet;
import nc.bs.ic.pub.base.IInsertRuleProvider;
import nc.bs.ic.special.insert.IInsertBP;
import nc.bs.ic.special.insert.InsertBPTemplate;
import nc.impl.pubapp.pattern.rule.processer.AroundProcesser;
import nc.vo.ic.m4n.entity.TransformVO;

/**
 * <p>
 * <b>本类主要完成以下功能：</b>
 * <ul>
 * <li>
 * </ul>
 * <p>
 * <p>
 * 
 * @version 6.0
 * @since 6.0
 * @author chennn
 * @time 2010-5-28 上午11:32:46
 */
public class InsertBP implements IInsertBP<TransformVO>,
    IInsertRuleProvider<TransformVO> {

  @Override
  public void addAfterRule(TransformVO[] vos,
      AroundProcesser<TransformVO> processor) {
 
   processor.addAfterRule(new RecordBeforebid());
  }

  @Override
  public void addBeforeRule(TransformVO[] vos,
      AroundProcesser<TransformVO> processor) {

    processor.addBeforeRule(new BillDataCheck());
    // 转换前ID的重新设置（63通版bug）
    processor.addBeforeRule(new SpecialBillDataIDValueSet());
    
    processor.addBeforeRule(new CheckBodyNumRule());

  }

  @Override
  public TransformVO[] insert(TransformVO[] bills) {
    return new InsertBPTemplate<TransformVO>(BPPluginPoint.InsertBP, this)
        .insert(bills);
  }

}
