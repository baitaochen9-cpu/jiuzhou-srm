package nccloud.dto.ct.saledaily.utils;

import nc.vo.pub.AggregatedValueObject;
import nc.vo.pubapp.scale.BillVOScaleProcessor;
import nc.vo.pubapp.scale.TotalValueScale;
import nc.vo.pubapp.scale.TotalValueVOScaleProcessor;

public class CtVOScaleRule {

  private BillVOScaleProcessor scaleProcessor = null;

  private TotalValueScale totalScale = null;

  public CtVOScaleRule(String pk_group, AggregatedValueObject[] bills) {
    this.scaleProcessor = new BillVOScaleProcessor(pk_group, bills);
    this.totalScale = new TotalValueVOScaleProcessor(bills);
  }

  public void setSacle() {
    PrintScaleUtil util = new PrintScaleUtil();
    util.setScale(this.scaleProcessor, this.totalScale);
  }
}
