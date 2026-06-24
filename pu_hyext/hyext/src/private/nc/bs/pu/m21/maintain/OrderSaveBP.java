/**
 * $文件说明$
 * 
 * @author zhaoyha
 * @version
 * @see
 * @since
 * @time 2009-12-29 上午09:43:53
 */
package nc.bs.pu.m21.maintain;

import nc.bs.pu.m21.maintain.rule.ATPBeforeUpdateRule;
import nc.bs.pu.m21.maintain.rule.ATPUpdateRule;
import nc.bs.pu.m21.maintain.rule.MaintainMPPCtrlChkRule;
import nc.bs.pu.m21.maintain.rule.NumAndPriceCheckRule;
import nc.bs.pu.m21.maintain.rule.OrderScaleCheckRule;
import nc.bs.pu.m21.maintain.rule.PrayToPoLimitRule;
import nc.bs.pu.m21.maintain.rule.WriteBackSourceRule;
import nc.bs.pu.m21.maintain.rule.save.AccrateCheckRule;
import nc.bs.pu.m21.maintain.rule.save.ArrliabCenterRule;
import nc.bs.pu.m21.maintain.rule.save.ArrvPlanChkRule;
import nc.bs.pu.m21.maintain.rule.save.BatchCodeBeforeRule;
import nc.bs.pu.m21.maintain.rule.save.BillCodeUniqueRule;
import nc.bs.pu.m21.maintain.rule.save.BorrowpurSetterRule;
import nc.bs.pu.m21.maintain.rule.save.CntPurMaterialChkRule;
import nc.bs.pu.m21.maintain.rule.save.ConfirmOrderBiztypeRule;
import nc.bs.pu.m21.maintain.rule.save.DirectOrgCheckRule;
import nc.bs.pu.m21.maintain.rule.save.DocVisibilityChkRule;
import nc.bs.pu.m21.maintain.rule.save.IsBlistBeforeRule;
import nc.bs.pu.m21.maintain.rule.save.IsdepositCheckRule;
import nc.bs.pu.m21.maintain.rule.save.ItemEmptyRule;
import nc.bs.pu.m21.maintain.rule.save.MaterialInStorckOrgRule;
import nc.bs.pu.m21.maintain.rule.save.MaxPriceChkRule;
import nc.bs.pu.m21.maintain.rule.save.MaxStocksChkRule;
import nc.bs.pu.m21.maintain.rule.save.NecessaryFillRule;
import nc.bs.pu.m21.maintain.rule.save.OrderCodeProceRule;
import nc.bs.pu.m21.maintain.rule.save.OrderDirectPurchaseUpdateRule;
import nc.bs.pu.m21.maintain.rule.save.OrderQualiStatuskRule;
import nc.bs.pu.m21.maintain.rule.save.PaymentCheckDataBeforeRule;
import nc.bs.pu.m21.maintain.rule.save.PlanArriveDateCheckRule;
import nc.bs.pu.m21.maintain.rule.save.PrePayLimitCheckRule;
import nc.bs.pu.m21.maintain.rule.save.PresentStocksChkRule;
import nc.bs.pu.m21.maintain.rule.save.PurtypeSetterRule;
import nc.bs.pu.m21.maintain.rule.save.SaveEventAfterRule;
import nc.bs.pu.m21.maintain.rule.save.SaveEventBeforeRule;
import nc.bs.pu.m21.maintain.rule.save.SaveVOValidateRule;
import nc.bs.pu.m21.maintain.rule.save.TranTypeChkRule;
import nc.bs.pu.m21.maintain.rule.save.WriteBackPriceForET;
import nc.bs.pu.m21.plugin.OrderPluginPoint;
import nc.bs.scmpub.rule.CrossRuleValidateRule;
import nc.impl.pu.m21.action.rule.RowNoRule;
import nc.impl.pu.m21.action.rule.revise.DeleteRule;
import nc.impl.pubapp.bd.userdef.UserDefSaveRule;
import nc.impl.pubapp.pattern.data.bill.BillInsert;
import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.pu.m21.context.OrderContext;
import nc.vo.pu.m21.entity.OrderHeaderVO;
import nc.vo.pu.m21.entity.OrderItemVO;
import nc.vo.pu.m21.entity.OrderVO;
import nc.vo.pu.pub.rule.NewestOrgVersionFillRule;
import nc.vo.pu.pub.rule.TrantypeNotNullCheckRule;
import nc.vo.pu.pub.rule.pf.NoPassUpdateRule;
import nc.vo.pub.VOStatus;
import nc.vo.scmpub.res.billtype.POBillType;
import nc.vo.scmpub.rule.PurchaseOrgEnableCheckRule;

/**
 * <p>
 * <b>本类主要完成以下功能：</b>
 * <ul>
 * <li>订单保存BP
 * <li>
 * <li>
 * </ul>
 * <p>
 * <b>变更历史（可选）：</b>
 * <p>
 * XXX版本增加XXX的支持。
 * <p>
 * <p>
 * 
 * @version 6.0
 * @since 6.0
 * @author zhaoyha
 * @time 2009-12-29 上午09:43:53
 */
public class OrderSaveBP {

	private OrderContext ctx;

	public OrderSaveBP(OrderContext ctx) {
		this.ctx = ctx;
	}

	/**
	 * 方法功能描述：订单保存BP（前台保存、推式保存）。
	 * <p>
	 * <b>examples:</b>
	 * <p>
	 * 使用示例
	 * <p>
	 * <b>参数说明</b>
	 * 
	 * @param orderVos
	 * @return <p>
	 * @since 6.0
	 * @author zhaoyha
	 * @time 2009-12-29 下午01:19:28
	 */
	public OrderVO[] save(OrderVO[] clientVos, OrderVO[] originVos) {

		OrderVO[] returnedVos;
		if (clientVos[0].getHVO().getStatus() != VOStatus.NEW) {
			CompareAroundProcesser<OrderVO> processer = new CompareAroundProcesser<OrderVO>(
					OrderPluginPoint.UPDATE);
			this.addBeforeRule(processer, clientVos);
			this.addAfterRule(processer);
			processer.before(clientVos, originVos);
			returnedVos = new BillUpdate<OrderVO>().update(clientVos, originVos);
			processer.after(returnedVos, originVos);
		} else {
			CompareAroundProcesser<OrderVO> processer = new CompareAroundProcesser<OrderVO>(
					OrderPluginPoint.INSERT);
			this.addBeforeRule(processer, clientVos);
			this.addAfterRule(processer);
			processer.before(clientVos, originVos);
			returnedVos = new BillInsert<OrderVO>().insert(clientVos);
			processer.after(returnedVos, null);
		}

		// 这里的VO不能保证所有数据与数据库同步，如调用者需要请重查，或提需求这里查也可
		return returnedVos;
	}

	private void addAfterRule(CompareAroundProcesser<OrderVO> processer) {
		processer.addAfterRule(new BillCodeUniqueRule());
		// 超现存量检查
		processer.addAfterRule(new PresentStocksChkRule(this.ctx));
		// 物料（供应商）可见性检查
		processer.addAfterRule(new DocVisibilityChkRule());
		// 订单交易类型相关检查
		processer.addAfterRule(new TranTypeChkRule());
		// 必须合同采购物料的检查
		processer.addAfterRule(new CntPurMaterialChkRule());
		// 最高限价检查
		processer.addAfterRule(new MaxPriceChkRule(this.ctx));
		// 回写上游单据
		processer.addAfterRule(new WriteBackSourceRule(this.ctx));
		// 回写出口合同采购本币无税单价
		processer.addAfterRule(new WriteBackPriceForET());

		// 保存后更新可用量
		processer.addAfterRule(new ATPUpdateRule());
		// 最高库存量检查，该规则必须在ATPUpdateRule之后
		processer.addAfterRule(new MaxStocksChkRule(this.ctx));
		// 更新内部交易信息，V60不实现
		// 批次
		// processer.addAfterRule(new BatchCodeAfterRule());
		// 触发保存后事件
		processer.addAfterRule(new SaveEventAfterRule());

		//
		// processer.addAfterRule(new WriteBusiLogCompareRule<OrderVO>(
		// PuBusiLogActionCode.save));
	}

	private void addBeforeRule(CompareAroundProcesser<OrderVO> processer,
			OrderVO[] orgUpdateVos) {
		processer.addBeforeRule(new CrossRuleValidateRule<OrderVO>(POBillType.Order
				.getCode()));
		// VO检查
		processer.addBeforeRule(new SaveVOValidateRule());
		// 付款比例
		processer.addBeforeRule(new AccrateCheckRule());
		// 质保金
		processer.addBeforeRule(new IsdepositCheckRule());
		// 行号
		processer.addBeforeRule(new RowNoRule());
		// 计算主组织最新版
		processer.addBeforeFinalRule(new NewestOrgVersionFillRule<OrderVO>(
				OrderHeaderVO.PK_ORDER));
		// 非空项检查
		processer.addBeforeRule(new ItemEmptyRule());
		// 精度检查规则
		processer.addBeforeRule(new OrderScaleCheckRule());
		// 检查交易类型是否为空
		processer.addBeforeFinalRule(new TrantypeNotNullCheckRule<OrderVO>());
		// 审批不通过时修改后的状态及审批人审批日期等处理
		processer.addBeforeFinalRule(new NoPassUpdateRule<OrderVO>());
		// 库存组织与物料的匹配检查
		processer.addBeforeRule(new MaterialInStorckOrgRule());
		
		processer.addBeforeRule(new OrderQualiStatuskRule());
				
		// 收货利润中心校验 by guoyk 2014年9月23日
		processer.addBeforeFinalRule(new ArrliabCenterRule());
		// 单价和数量的合法性校验
		processer.addBeforeRule(new NumAndPriceCheckRule());
		// 主组织停用检查
		processer.addBeforeRule(new PurchaseOrgEnableCheckRule<OrderVO>());
		// 到货计划相关检查
		processer.addBeforeRule(new ArrvPlanChkRule(orgUpdateVos));
		// 采购合同控制，根据合同控制方式控制单价，在合同那控制
		// processer.addBeforeRule(new CntControlRule());
		// 计划到货日期的检查
		processer.addBeforeRule(new PlanArriveDateCheckRule());
		processer.addBeforeRule(new DirectOrgCheckRule());
		// 删除检查，已关闭的行所，有后续操作的行，不能删除
		processer.addBeforeRule(new DeleteRule());
		// 自定义项检查
		// 必要项补充
		processer.addBeforeRule(new NecessaryFillRule());
		// 直运标志处理
		processer.addBeforeRule(new OrderDirectPurchaseUpdateRule());
		// 预付款限额的检查
		processer.addBeforeRule(new PrePayLimitCheckRule());
		// 采购订单单据号(订单号)补充规则
		processer.addBeforeRule(new OrderCodeProceRule());
		// 交易类型控制
		processer.addBeforeRule(new PrayToPoLimitRule());

		// 可用量保存前处理
		processer.addBeforeRule(new ATPBeforeUpdateRule());
		// 匹配业务流程(启动流程)
		processer.addBeforeRule(new ConfirmOrderBiztypeRule());
		// 采购计划控制检查
		processer.addBeforeRule(new MaintainMPPCtrlChkRule());
		// 自由辅助属性校验
		// MarAssistantSaveRule<OrderVO> marRule = new
		// MarAssistantSaveRule<OrderVO>();
		// marRule.setNotNullValidate(OrderItemVO.PK_MATERIAL);
		// processer.addBeforeRule(marRule);
		processer.addBeforeRule(new PurtypeSetterRule());
		processer.addBeforeRule(new UserDefSaveRule<OrderVO>(new Class[] {
				OrderHeaderVO.class, OrderItemVO.class }));
		// 设置借入标志
		processer.addBeforeFinalRule(new BorrowpurSetterRule());
		// 批次规则
		processer.addBeforeRule(new BatchCodeBeforeRule());
		// 触发保存前事件
		processer.addBeforeRule(new SaveEventBeforeRule());
		// yanxm5 采购订单保存时校验，如果供应商被列入黑名单则不能保存
		processer.addBeforeRule(new IsBlistBeforeRule());
		// 不允许出账日有值，固定结账日无值
		processer.addBeforeRule(new PaymentCheckDataBeforeRule());
	}

}
