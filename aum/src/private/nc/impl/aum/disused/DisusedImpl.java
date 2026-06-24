package nc.impl.aum.disused;

import java.util.ArrayList;
import java.util.List;

import nc.bs.am.framework.action.IActionTemplate;
import nc.bs.am.framework.action.IRule;
import nc.bs.am.framework.action.approve.ApproveActionTemplate;
import nc.bs.am.framework.action.approve.UnApproveActionTemplate;
import nc.bs.am.framework.common.rule.ValidateServiceRule;
import nc.bs.aum.common.rule.DeleteMessageToFaAfterRule;
import nc.bs.aum.common.rule.UpdateBillAfterRule;
import nc.bs.aum.common.validator.FaBillExistValidator;
import nc.bs.aum.common.validator.TransiRuleValidator;
import nc.bs.pub.compiler.AbstractCompiler2;
import nc.bs.uif2.validation.Validator;
import nc.impl.aim.validator.OrgUnitPreDateValidator;
import nc.impl.aim.validator.SrcBillClosedValidator;
import nc.impl.am.bill.BillBaseDAO;
import nc.impl.am.bill.rule.AppendBusiTypeBeforeRule;
import nc.impl.am.bill.rule.BillCodeDeleteRule;
import nc.impl.am.bill.rule.BillCodeInsertAfterRule;
import nc.impl.am.bill.rule.BillCodeInsertBeforeRule;
import nc.impl.am.bill.rule.BillCodeUpdateRule;
import nc.impl.am.bill.rule.CloseToSrcBillRule;
import nc.impl.am.bill.rule.CommitBeforeRule;
import nc.impl.am.bill.rule.DataIntegralityCheckByMDRule;
import nc.impl.am.db.BillPersistUtil;
import nc.impl.aum.disused.rule.DisusedCloseUsedProcessAfterRule;
import nc.impl.aum.disused.rule.DisusedConfirmFlagProcessAfterRule;
import nc.impl.aum.disused.rule.DisusedSendMessageToFaAfterRule;
import nc.impl.aum.disused.rule.DisusedSupplyEquipmentInfoAfterRule;
import nc.impl.aum.disused.rule.DisusedSupplyStatusByTransiRuleAfterRule;
import nc.impl.aum.disused.rule.DisusedSupplyWareHouseIdAfterRule;
import nc.impl.aum.disused.rule.DisusedWriteBackToEquipAfterRule;
import nc.impl.aum.disused.validator.DisusedBusinessValidator;
import nc.impl.aum.disused.validator.DisusedDataNotNullValidator;
import nc.impl.aum.disused.validator.DisusedICInboundBillExistValidator;
import nc.impl.aum.disused.validator.DisusedICOutboundBillExistValidator;
import nc.impl.aum.disused.validator.DisusedNextStepValidator;
import nc.impl.aum.disused.validator.DisusedOpenValidator;
import nc.impl.aum.disused.validator.DisusedTransiRuleValidator;
import nc.impl.aum.disused.validator.DisusedUnApproveAfterRule;
import nc.impl.aum.reduce.rule.GetOrgCurrencyBeforeInsertRule;
import nc.itf.aum.prv.IDisused;
import nc.pub.aim.rule.LockEquipBeforeRule;
import nc.pub.aim.validator.CommonEquipForBillValidator;
import nc.pub.am.common.validator.SrcBillUnApproveAndDeleteValidator;
import nc.vo.am.common.BaseLockData;
import nc.vo.am.common.TransportBillVO;
import nc.vo.am.common.util.BillTransportTool;
import nc.vo.am.constant.CommonKeyConst;
import nc.vo.am.manager.LockManager;
import nc.vo.aum.disused.DisusedBodyVO;
import nc.vo.aum.disused.DisusedHeadVO;
import nc.vo.aum.disused.DisusedVO;
import nc.vo.aum.unused.UnusedVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.compiler.PfParameterVO;
import nc.vo.uif2.LoginContext;

/**
 * <p>
 * <b>资产报废单服务实现类</b>
 * </p>
 * 
 * @version nc6.0
 * @since nc6.0
 * @author <a href="mailto:taorz1@ufida.com.cn">taorz1</a>
 * @time 2010-12-24 下午02:30:51
 */
public class DisusedImpl extends BillBaseDAO<DisusedVO> implements IDisused {

	@Override
	public TransportBillVO[] insertDisusedVos(LoginContext context,
			DisusedVO[] aggVos) throws BusinessException {

		// 调用保存新增；
		aggVos = insertBillVO(aggVos);

		// 回写表头和表体字段
		String[] otherHeadItems = new String[] { CommonKeyConst.pk_currency };
		String[] otherBodyItems = new String[] {};
		// 变化后的数据，减少下行流量
		TransportBillVO[] transportBilllVos = BillTransportTool
				.createTransportBills(aggVos, otherHeadItems, otherBodyItems,
						BillTransportTool.MODE_INSERT);

		return transportBilllVos;
	}

	@Override
	public TransportBillVO[] updateDisusedVos(LoginContext context,
			DisusedVO... aggVos) throws BusinessException {
		// 加锁
		BaseLockData<DisusedVO> lockdata = new BaseLockData<DisusedVO>(aggVos);
		LockManager.lock(lockdata, new String[] { CommonKeyConst.ts }, null);
		// 修改保存
		aggVos = updateBillVO(aggVos, null);

		// 变化后的数据，减少下行流量
		TransportBillVO[] transportBilllVos = BillTransportTool
				.createTransportBills(aggVos, BillTransportTool.MODE_UPDATE);

		return transportBilllVos;
	}

	@Override
	public void deleteDisusedVos(LoginContext context, DisusedVO[] aggVos)
			throws BusinessException {
		// 加锁
		BaseLockData<DisusedVO> lockdata = new BaseLockData<DisusedVO>(aggVos);
		LockManager.lock(lockdata, new String[] { CommonKeyConst.ts }, null);
		// 删除
		deleteBillVO(aggVos);
	}

	/**
	 * 提交按钮脚本调用（N_4A19_SAVE）
	 * 
	 * @param context
	 * @param aggVos
	 * @return
	 * @throws BusinessException
	 * @author taorz1
	 * @time 2010-12-24 下午02:10:24
	 */
	public DisusedVO[] commitDisusedVos(LoginContext context, DisusedVO[] aggVos)
			throws BusinessException {

		// 提交操作
		return commitBillVO(aggVos);
	}

	/**
	 * 收回按钮脚本调用（N_4A19_UNSAVE）
	 * 
	 * @param context
	 * @param aggVos
	 * @return
	 * @throws BusinessException
	 * @author taorz1
	 * @time 2010-12-24 下午02:12:50
	 */
	public TransportBillVO[] unCommitDisusedVos(LoginContext context,
			DisusedVO[] aggVos) throws BusinessException {
		// 调用收回操作
		DisusedVO[] billVos = unSaveBillVOs(aggVos);
		// 变化后的数据，减少下行流量
		TransportBillVO[] transportBilllVos = BillTransportTool
				.createTransportBills(billVos, BillTransportTool.MODE_COMMIT);

		return transportBilllVos;
	}

	/**
	 * 脚本调用，收回
	 * 
	 * @param pk
	 * @return
	 * @throws BusinessException
	 */
	public Object unCommit(DisusedVO... vos) throws BusinessException {
		return vos;
	}
	
	/**


	/**
	 * 新增单据后处理
	 */
	@Override
	protected void initInsertActionRules(IActionTemplate<DisusedVO> template) {
		super.initInsertActionRules(template);
		// 补充busi_type
		template.addBeforeRule(new AppendBusiTypeBeforeRule<DisusedVO>());
		// 对设备进行加锁处理
		template.addBeforeRule(new LockEquipBeforeRule<DisusedVO>());
		// 数据完整性校验(校验数据是否引用了被删除的实体)
		template.addBeforeRule(new DataIntegralityCheckByMDRule<DisusedVO>());
		// 前编码单据号处理
		template.addBeforeRule(new BillCodeInsertBeforeRule<DisusedVO>());
		// 获取组织币种
		template.addBeforeRule(new GetOrgCurrencyBeforeInsertRule<DisusedVO>());
		// 新增后单据号处理
		template.addAfterRule(new BillCodeInsertAfterRule<DisusedVO>());

		// 交易规则校验
		template.addBeforeRule(new ValidateServiceRule<DisusedVO>(
				new Validator[] {
						// 参照设备是否为审核通过
						new CommonEquipForBillValidator(),
						// 验证报废日期晚于模块的启用日期
						new OrgUnitPreDateValidator(DisusedHeadVO.BILL_DATE,
								null),
						// 交易规则校验
						new TransiRuleValidator<DisusedVO>(),
						// 来源单据并发删除或弃审验证
						new SrcBillUnApproveAndDeleteValidator(),
						// 上游闲置单据是否关闭校验规则
						new SrcBillClosedValidator<DisusedVO>(UnusedVO.class) }));
	}

	/**
	 * 注册修改前后的扩展动作
	 */
	@Override
	protected void initUpdateActionRules(IActionTemplate<DisusedVO> template) {
		super.initUpdateActionRules(template);
		// 补充busi_type
		template.addBeforeRule(new AppendBusiTypeBeforeRule<DisusedVO>());
		// 对设备进行加锁处理
		template.addBeforeRule(new LockEquipBeforeRule<DisusedVO>());
		// 数据完整性校验(校验数据是否引用了被删除的实体)
		template.addBeforeRule(new DataIntegralityCheckByMDRule<DisusedVO>());
		// 单据号处理
		template.addBeforeRule(new BillCodeUpdateRule<DisusedVO>());
		// 交易规则校验
		template.addBeforeRule(new ValidateServiceRule<DisusedVO>(
				new Validator[] {
						// 参照设备是否为审核通过
						new CommonEquipForBillValidator(),
						// 验证报废日期晚于模块的启用日期
						new OrgUnitPreDateValidator(DisusedHeadVO.BILL_DATE,
								null),
						// 交易规则校验
						new TransiRuleValidator<DisusedVO>(),
						// 来源单据并发删除或弃审验证
						new SrcBillUnApproveAndDeleteValidator(),
						// 上游闲置单据是否关闭校验规则
						new SrcBillClosedValidator<DisusedVO>(UnusedVO.class)

				}));
	}

	/**
	 * 删除单据后处理
	 */
	@Override
	protected void initDeleteActionRules(IActionTemplate<DisusedVO> template) {
		super.initDeleteActionRules(template);
		// 删除后单据号处理
		template.addAfterRule(new BillCodeDeleteRule<DisusedVO>());
	}

	@Override
	protected void initApproveRule(ApproveActionTemplate<DisusedVO> template) {
		// 第二步:配置业务规则
		// 1、批准审核前规则
		List<IRule<DisusedVO>> approvedBeforeRules = new ArrayList<IRule<DisusedVO>>();
		// 2、审核通过后规则
		List<IRule<DisusedVO>> approvePassAfterRules = new ArrayList<IRule<DisusedVO>>();
		// 1.0 校验
		List<Validator> validators = new ArrayList<Validator>();
		// 参照设备是否为审核通过
		validators.add(new CommonEquipForBillValidator());
		// 上游闲置单据是否关闭校验规则
		validators.add(new SrcBillClosedValidator<DisusedVO>(UnusedVO.class));
		validators.add(new DisusedBusinessValidator<DisusedVO>()); // 执行业务校验
		validators.add(new DisusedTransiRuleValidator());
		// 执行交易规则校验
		// 对设备进行加锁处理
		approvedBeforeRules.add(new LockEquipBeforeRule<DisusedVO>());
		// 1.1 审核校验执行；
		// 修订处理规则
		// approvedBeforeRules.add(new DisusedReviseBeforeRule());
		// 校验规则
		approvedBeforeRules.add(new ValidateServiceRule<DisusedVO>(validators));
		// 确定报废不钩选的设备在单据审批通过后 自动为关闭状态，且不能打开
		approvePassAfterRules.add(new DisusedCloseUsedProcessAfterRule());
		// 整理确认报废的单据
		approvePassAfterRules.add(new DisusedConfirmFlagProcessAfterRule());
		// 补充设备卡片最新信息到审核前信息（设备状态）
		approvePassAfterRules.add(new DisusedSupplyEquipmentInfoAfterRule());
		// 根据交易规则设置到审核后信息（设备状态）
		approvePassAfterRules
				.add(new DisusedSupplyStatusByTransiRuleAfterRule());
		// 更新单据表体（默认更新）
		approvePassAfterRules.add(new UpdateBillAfterRule<DisusedVO>(
				DisusedBodyVO.class));
		// 变动后项目更新设备卡片相应字段；
		approvePassAfterRules.add(new DisusedWriteBackToEquipAfterRule(true));
		// 向固定资产发消息；
		approvePassAfterRules.add(new DisusedSendMessageToFaAfterRule());
		// 补充仓库；
		approvePassAfterRules.add(new DisusedSupplyWareHouseIdAfterRule());
		// // 断开父子关系（报废不断开父子关系）
		// approvePassAfterRules.add(new DisusedCutOffEquipmentRelationRule());
		// 自动关闭上游闲置单据
		approvePassAfterRules.add(new CloseToSrcBillRule<DisusedVO>(
				UnusedVO.class, true));
		// 自动关闭上游闲置单据
		approvePassAfterRules.add(new DisusedCloseUsedProcessAfterRule());

		// 第三步：将规则添加到模板上
		// 批准审批前规则
		template.addApprovedBeforeRule(approvedBeforeRules);
		// 审批通过后规则
		template.addApprovePassAfterRule(approvePassAfterRules);
	}

	@Override
	protected void initCommitActionRules(IActionTemplate<DisusedVO> template) {
		// 给设备加锁
		template.addBeforeRule(new LockEquipBeforeRule<DisusedVO>());
		
		/*******************************************************************/
		// 检索设备是否有启用的预防性维护
		template.addBeforeRule(new CommitBeforeRule<DisusedVO>());
		/*******************************************************************/
		
		List<Validator> validators = new ArrayList<Validator>();
		// 参照设备是否为审核通过
		validators.add(new CommonEquipForBillValidator());
		validators.add(new DisusedDataNotNullValidator<DisusedVO>());
		template.addBeforeRule(new ValidateServiceRule<DisusedVO>(validators));
	}

	@Override
	protected void initUnApproveRule(UnApproveActionTemplate<DisusedVO> template) {
		// 取消审核校验类
		List<Validator> validators = new ArrayList<Validator>();
		// 1、否有库存后续单据（出库单）
		validators.add(new DisusedICOutboundBillExistValidator());
		// 2、否有库存后续单据（入库单）
		validators.add(new DisusedICInboundBillExistValidator());
		// 3、是否有固定资产后续单据（减少单）
		validators.add(new FaBillExistValidator<DisusedVO>());
		// 4、是否有后续业务
		validators.add(new DisusedNextStepValidator());
		validators.add(new DisusedUnApproveAfterRule<DisusedVO>());
		// 弃审前业务规则
		List<IRule<DisusedVO>> unApprovedBeforeRules = new ArrayList<IRule<DisusedVO>>();
		// 对设备进行加锁处理
		unApprovedBeforeRules.add(new LockEquipBeforeRule<DisusedVO>());
		// 1、校验规则；
		unApprovedBeforeRules
				.add(new ValidateServiceRule<DisusedVO>(validators));
		// 最后取消审核人取消审核业务规则
		List<IRule<DisusedVO>> lastUnApprovedAfterRules = new ArrayList<IRule<DisusedVO>>();
		// 1、整理确认报废的单据
		lastUnApprovedAfterRules.add(new DisusedConfirmFlagProcessAfterRule());
		// 2、删除固定资产消息
		lastUnApprovedAfterRules
				.add(new DeleteMessageToFaAfterRule<DisusedVO>());
		// 3、回写设备卡片；
		lastUnApprovedAfterRules
				.add(new DisusedWriteBackToEquipAfterRule(false));
		// 5、弃审打开上游单据
		lastUnApprovedAfterRules.add(new CloseToSrcBillRule<DisusedVO>(
				UnusedVO.class, false));
		// 添加取消审核前业务规则
		template.addUnApprovedBeforeRules(unApprovedBeforeRules);
		// 添加取消审核后业务规则
		template.addLastUnApprovedAfterRules(lastUnApprovedAfterRules);
	}

	@Override
	public Object doClose(LoginContext context, DisusedVO... billVOs)
			throws BusinessException {
		BillPersistUtil persist = new BillPersistUtil();
		Object pkBillVO = persist.update(billVOs);
		return pkBillVO;
	}

	/**
	 * 注册打开前后的扩展动作
	 */
	@Override
	protected void initOpenActionRules(IActionTemplate<DisusedVO> template) {
		super.initOpenActionRules(template);
		template.addBeforeRule(new ValidateServiceRule<DisusedVO>(
				new Validator[] { new DisusedOpenValidator() }));
	}

	@Override
	public Object doOpen(LoginContext context, DisusedVO... billVOs)
			throws BusinessException {
		DisusedVO[] pkBillVOs = super.openBillBodyVO(billVOs, null);
		// TODO 后续要做调整
		// SuperVO[] bodyvos = billVOs[0].getChildrenVO();
		// for (SuperVO superVO : bodyvos) {
		// UFBoolean confiFlag = (UFBoolean) superVO
		// .getAttributeValue(DisusedBodyVO.CONFIRM_FLAG);
		// UFBoolean closeFlag = (UFBoolean) superVO
		// .getAttributeValue(DisusedBodyVO.CLOSE_FLAG);
		// if (confiFlag.booleanValue()) {
		// throw new BusinessException("已确认报废的表体不允许打开");
		// }
		// if (closeFlag.booleanValue()) {
		// throw new BusinessException("当前行表体已经被处理，不允许打开");
		// }
		// }
		// BillPersistUtil persist = new BillPersistUtil();
		// Object pkBillVO = persist.update(billVOs);
		TransportBillVO[] transportBilllVos = BillTransportTool
				.createTransportBills(pkBillVOs, BillTransportTool.MODE_UPDATE);

		return transportBilllVos;
	}

}
