package nc.bs.pu.m23.maintain;

import nc.bs.pu.m23.maintain.rule.ArriveQualiStatuskRule;
import nc.bs.pu.m23.maintain.rule.ChkBackArWaitNumRule;
import nc.bs.pu.m23.maintain.rule.ChkBackFor23Rule;
import nc.bs.pu.m23.maintain.rule.ChkBillCodeUniqueRule;
import nc.bs.pu.m23.maintain.rule.ChkCheckNumOnBackRule;
import nc.bs.pu.m23.maintain.rule.ChkMrlInStockOrg;
import nc.bs.pu.m23.maintain.rule.DealFreeChkItemRule;
import nc.bs.pu.m23.maintain.rule.DealLocalMnyRule;
import nc.bs.pu.m23.maintain.rule.ValiVOBeforSaveRule;
import nc.bs.pu.m23.maintain.rule.WriteBackFor23Rule;
import nc.bs.pu.m23.maintain.rule.update.UpdateAfterEventRule;
import nc.bs.pu.m23.maintain.rule.update.UpdateBatchCodeAfterRule;
import nc.bs.pu.m23.maintain.rule.update.UpdateBatchCodeBeforeRule;
import nc.bs.pu.m23.maintain.rule.update.UpdateBeforeEventRule;
import nc.bs.pu.m23.maintain.rule.update.UpdateBillCodeRule;
import nc.bs.pu.m23.maintain.rule.update.UpdateFillDataRule;
import nc.bs.pu.m23.maintain.rule.update.Write21WhenUpdateRule;
import nc.bs.pu.m23.maintain.rule.update.Write61WhenUpdateRule;
import nc.bs.pu.m23.plugin.ArriveBPPlugInPoint;
import nc.impl.pubapp.bd.userdef.UserDefSaveRule;
import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.pu.m23.entity.ArriveHeaderVO;
import nc.vo.pu.m23.entity.ArriveItemVO;
import nc.vo.pu.m23.entity.ArriveVO;
import nc.vo.pu.m23.env.ArrivalUIToBSEnv;
import nc.vo.pu.m23.rule.ArriveATPUpdateRule;
import nc.vo.pu.pub.enumeration.EnumOperate;
import nc.vo.pu.pub.rule.NewestOrgVersionFillRule;
import nc.vo.pu.pub.rule.TrantypeNotNullCheckRule;
import nc.vo.pu.pub.rule.pf.NoPassUpdateRule;

/**
 * 方法功能描述：完成修改保存到货单
 * <p>
 * <b>参数说明</b>
 * 
 * @param aggVO数组 <p>
 * @since 6.0
 * @author hanbin
 * @time 2010-1-13 上午10:18:08
 */
public class ArriveUpdateBP {
  private ArrivalUIToBSEnv env;

  public ArriveUpdateBP(ArrivalUIToBSEnv env) {
    this.env = env;
  }

  /**
   * 方法功能描述：完成修改保存到货单
   * <p>
   * <b>参数说明</b>
   * 
   * @param aggVO数组 <p>
   * @since 6.0
   * @author hanbin
   * @time 2010-1-13 上午10:18:08
   */
  public ArriveVO[] updateArrive(ArriveVO[] aggVO, ArriveVO[] originBills) {
    CompareAroundProcesser<ArriveVO> processer =
        new CompareAroundProcesser<ArriveVO>(ArriveBPPlugInPoint.ArriveUpdateBP);

    // 添加BP规则
    this.addBeforeRule(processer);
    this.addAfterRule(processer);
    processer.before(aggVO, originBills);
    BillUpdate<ArriveVO> bo = new BillUpdate<ArriveVO>();
    ArriveVO[] vos = bo.update(aggVO, originBills);
    processer.after(aggVO, originBills);
    return vos;
  }

  private void addAfterRule(CompareAroundProcesser<ArriveVO> processer) {
    // 检查单据号的唯一性
    processer.addAfterRule(new ChkBillCodeUniqueRule());
    // 修改保存后批次号处理
    processer.addAfterRule(new UpdateBatchCodeAfterRule());
    // 回写来源到货单的累计退货数量。
    processer.addAfterRule(new WriteBackFor23Rule(EnumOperate.MODIFY));
    // 回写采购订单的累计到货数量、途耗数量
    processer.addAfterRule(new Write21WhenUpdateRule(this.env));
    // 回写委外订单的累计到货数量、途耗数量
    processer.addAfterRule(new Write61WhenUpdateRule(this.env));
    // 退货单到货待检量检查
    processer.addAfterRule(new ChkBackArWaitNumRule());
    // 可用量变更后操作
    processer.addAfterRule(new ArriveATPUpdateRule(false));
    // 触发更新后事件
    processer.addAfterRule(new UpdateAfterEventRule());
  }

  private void addBeforeRule(CompareAroundProcesser<ArriveVO> processer) {
    // 在基于到货单退货时，检查报检、检验报告
    processer.addBeforeRule(new ChkCheckNumOnBackRule(this.env));
    // 检查交易类型是否为空
    processer.addBeforeFinalRule(new TrantypeNotNullCheckRule<ArriveVO>());
    // 计算主组织最新版
    processer.addBeforeFinalRule(new NewestOrgVersionFillRule<ArriveVO>(
        ArriveHeaderVO.PK_ARRIVEORDER));
    // 填补数据
    processer.addBeforeRule(new UpdateFillDataRule());
    // 审批不通过时修改后的状态及审批人审批日期等处理
    processer.addBeforeFinalRule(new NoPassUpdateRule<ArriveVO>());
    // 保存前检查VO
    processer.addBeforeRule(new ValiVOBeforSaveRule());
    
    processer.addBeforeRule(new ArriveQualiStatuskRule());
    
    // 处理本币金额
    processer.addBeforeRule(new DealLocalMnyRule());
    // 检查物料与库存组织是否匹配
    processer.addBeforeRule(new ChkMrlInStockOrg());
    // 处理免检物料行
    processer.addBeforeRule(new DealFreeChkItemRule());
    // 修改保存前批次号处理
    processer.addBeforeRule(new UpdateBatchCodeBeforeRule());
    // 单据号处理
    processer.addBeforeRule(new UpdateBillCodeRule());
    // 基于到货单退货的相关检查。
    processer.addBeforeRule(new ChkBackFor23Rule());
    // 自定义项校验
    processer.addBeforeRule(new UserDefSaveRule<ArriveVO>(new Class[] {
      ArriveHeaderVO.class, ArriveItemVO.class
    }));
    // 可用量变更前操作
    processer.addBeforeRule(new ArriveATPUpdateRule(true));
    // 触发更新前事件
    processer.addAfterRule(new UpdateBeforeEventRule());
  }
}
