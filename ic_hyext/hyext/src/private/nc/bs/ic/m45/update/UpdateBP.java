package nc.bs.ic.m45.update;

import nc.bs.ic.general.insert.rule.before.CheckCliabilityValue;
import nc.bs.ic.general.rule.after.AtpAfterUpdate;
import nc.bs.ic.general.rule.before.CtplcustomeridCheck;
import nc.bs.ic.general.update.IUpdateBP;
import nc.bs.ic.general.update.UpdateBPTemplate;
import nc.bs.ic.m45.base.BPPlugInPoint;
import nc.bs.ic.m45.base.rule.FillCostOrgRule;
import nc.bs.ic.m45.base.rule.PurchaseBillCheckRule;
import nc.bs.ic.m45.base.rule.PurchaseInAssetWarehouseCheck;
import nc.bs.ic.m45.base.rule.PurchaseInVOScaleCheckRule;
import nc.bs.ic.m45.base.rule.PurchaseinRetMarginProcess;
import nc.bs.ic.m45.base.rule.PurchaseinRetMarginProcessFor5805;
import nc.bs.ic.m45.base.rule.ReturnSnIsExistInEquipcardCheck;
import nc.bs.ic.m45.base.rule.VmiCheckRule;
import nc.bs.ic.m45.insert.rule.InsertCheckMaterialUnit;
import nc.bs.ic.m45.update.rule.PurchInQualiStatuskRule;
import nc.bs.ic.m45.update.rule.UpdReWritePIM;
import nc.bs.ic.m45.update.rule.UpdateRewriteITRule;
import nc.bs.ic.m45.update.rule.UpdateRewritePORule;
import nc.bs.ic.pub.base.ICCompareAroundProcesser;
import nc.bs.ic.pub.base.IUpdateRuleProvider;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.ic.general.define.MetaNameConst;
import nc.vo.ic.m45.entity.PurchaseInBodyVO;
import nc.vo.ic.m45.entity.PurchaseInVO;
import nc.vo.ic.pub.util.StringUtil;
import nc.vo.pub.VOStatus;

/**
 * 采购入库单后台更新BP
 * 
 * @author songhy
 */
public class UpdateBP implements IUpdateBP<PurchaseInVO>,
    IUpdateRuleProvider<PurchaseInVO> {

  @Override
  public void addAfterRule(PurchaseInVO[] vos, PurchaseInVO[] originVOs,
      CompareAroundProcesser<PurchaseInVO> processor) {
    /**
     * 放在现存量更新规则（OnhandAfterUpdate）之后，因为现存量更新时会调用预留接口，预留接口会回写采购订单，
     * 如果入库数量=订单数量，订单会自动入库关闭，入库关闭会解除预留，导致后续操作错误
     */
    ((ICCompareAroundProcesser<PurchaseInVO>) processor).addAfterRuleAt(
        new UpdateRewritePORule(), AtpAfterUpdate.class);
    processor.addAfterRule(new UpdReWritePIM());
    // processor.addAfterRule(new UpdateRewritePORule());
    processor.addAfterRule(new UpdateRewriteITRule());
  }

  @Override
  public void addBeforeRule(PurchaseInVO[] vos, PurchaseInVO[] originVOs,
      CompareAroundProcesser<PurchaseInVO> processor) {
    processor.addBeforeRule(new FillCostOrgRule());
    processor.addBeforeRule(new PurchaseBillCheckRule());
    processor.addBeforeRule(new PurchaseInAssetWarehouseCheck());
    processor.addBeforeRule(new PurchInQualiStatuskRule());
    processor.addBeforeRule(new CtplcustomeridCheck<PurchaseInVO>());
    processor.addBeforeRule(new VmiCheckRule());
    processor.addBeforeRule(new ReturnSnIsExistInEquipcardCheck());
    processor.addBeforeRule(new PurchaseinRetMarginProcess());
    processor.addBeforeRule(new PurchaseinRetMarginProcessFor5805());
    processor.addBeforeRule(new PurchaseInVOScaleCheckRule());
 // 利润中心校验规则
    processor.addBeforeRule(new CheckCliabilityValue<PurchaseInVO>(MetaNameConst.CLIABILITYOID,MetaNameConst.CIOLIABILITYOID));
    processor.addBeforeRule(new InsertCheckMaterialUnit());
  }

  /**
   * 父类方法重写
   * 
   */
  @Override
  public PurchaseInVO[] update(PurchaseInVO[] vos, PurchaseInVO[] originVOs) {
    this.fillCvendorid(vos);
    UpdateBPTemplate<PurchaseInVO> updateBP =
        new UpdateBPTemplate<PurchaseInVO>(BPPlugInPoint.UpdateBP, this);
    PurchaseInVO[] retVOs = updateBP.update(vos, originVOs);
    return retVOs;
  }
  
  private void fillCvendorid(PurchaseInVO[] vos) {
    String cvendor = null;
    for (PurchaseInVO vo : vos) {
      // 取表头供应商
      cvendor = vo.getHead().getCvendorid();
      
      // 为表体供应商赋值
      for (PurchaseInBodyVO body : vo.getBodys()) {
        if(StringUtil.isStringEqual(cvendor, body.getCvendorid()))
          continue;
        body.setCvendorid(cvendor);
        if(VOStatus.UNCHANGED == body.getStatus()){
          // 此处必须判断状态，不能全部设为UPDATED，否则删行后，再新增行，保存会报错
          body.setStatus(VOStatus.UPDATED);
        }
        
      }
    }
  }


}
