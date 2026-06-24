/*
 * @(#)FailureBaseImpl.java V60
 *
 * Copyright 2010 UFIDA Software Co. Ltd. All rights reserved.
 * UFIDA PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package nc.impl.eom.failure;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nc.bs.am.framework.action.IActionTemplate;
import nc.bs.am.framework.action.approve.ApproveActionTemplate;
import nc.bs.am.framework.action.approve.UnApproveActionTemplate;
import nc.bs.dao.DAOException;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.pub.compiler.AbstractCompiler2;
import nc.bs.pub.filesystem.IFileSystemService;
import nc.bs.uif2.validation.Validator;
import nc.impl.am.bill.BillBaseDAO;
import nc.impl.am.bill.rule.AddAuditInfoBeforeRule;
import nc.impl.am.bill.rule.AppendBusiTypeBeforeRule;
import nc.impl.am.bill.rule.AppendTransiOrBusiTypeRule;
import nc.impl.am.bill.rule.BillCodeDeleteRule;
import nc.impl.am.bill.rule.BillCodeInsertAfterRule;
import nc.impl.am.bill.rule.BillCodeInsertBeforeRule;
import nc.impl.am.bill.rule.BillCodeUpdateRule;
import nc.impl.am.bill.rule.DataIntegralityCheckByMDRule;
import nc.impl.am.bill.rule.SetBizScopeInfoBeforeRule;
import nc.impl.am.bill.rule.SetOrgVidBeforeRule;
import nc.impl.am.bill.rule.VOFieldLengthCheckRule;
import nc.impl.am.bill.rule.ValidateServiceRule;
import nc.impl.am.bill.rule.VoStatusSetBeforeRule;
import nc.impl.am.common.InSqlManager;
import nc.impl.am.db.DBAccessUtil;
import nc.impl.am.db.QueryUtil;
import nc.impl.eampub.util.srcbill.GeneralFunc4SrcBillRule;
import nc.impl.eampub.util.srcbill.GeneralFunc4SrcBillUtils.FuncType;
import nc.impl.eampub.validator.EquipValidator;
import nc.impl.eom.failure.genfunc.FailureDocWriteBackDailyPointCheck;
import nc.impl.eom.failure.genfunc.FailureDocWriteBackInspection;
import nc.impl.eom.failure.genfunc.FailureDocWriteBackMeasureDoc;
import nc.impl.eom.failure.genfunc.FailureDocWriteBackSpecialPointCheck;
import nc.impl.eom.failure.genfunc.FailureGenFunc4Operation;
import nc.impl.eom.failure.genfunc.FailureGenFunc4WorkOrder;
import nc.impl.eom.failure.rule.AddFailureSendEmilRule;
import nc.impl.eom.failure.rule.AddIdFailureInfoRule;
import nc.impl.eom.failure.rule.FailureBillCodePushSaveBeforeRule;
import nc.impl.eom.failure.rule.FailureBusiTypeJumpRule;
import nc.impl.eom.failure.rule.FailureReasonRelationRule;
import nc.impl.eom.failure.rule.GenFailureByTempSaveFailureAfterPushSaveRule;
import nc.impl.eom.failure.rule.MakeUpFailureInfoRule;
import nc.impl.eom.failure.rule.UpdateBillGenTypeWhenMergedBodyVODeleteRule;
import nc.impl.eom.failure.rule.UpdateEquipStatusRule;
import nc.impl.eom.failure.validator.FailureDBGenNextValidator;
import nc.impl.eom.failure.validator.FailureDataNotNullValidator;
import nc.impl.eom.failure.validator.FailureFieldScaleValidator;
import nc.impl.eom.failure.validator.FailureTypeAndSymptomNotNullValidator;
import nc.impl.eom.validator.RelatedMaintainOrgValidator;
import nc.itf.am.prv.IDataService;
import nc.itf.am.pub.IFailurereasonService;
import nc.itf.eom.prv.IFailureBase;
import nc.jdbc.framework.processor.BaseProcessor;
import nc.jdbc.framework.processor.ResultSetProcessor;
import nc.vo.am.common.util.ArrayUtils;
import nc.vo.am.common.util.BaseVOUtils;
import nc.vo.am.common.util.CollectionUtils;
import nc.vo.am.common.util.MultiLanguageUtil;
import nc.vo.am.constant.CommonKeyConst;
import nc.vo.am.constant.FailureStatusConst;
import nc.vo.am.failurereason.FailurereasonVO;
import nc.vo.am.failurereasonrelation.FailureReasonRelation;
import nc.vo.am.manager.LockManager;
import nc.vo.am.proxy.AMProxy;
import nc.vo.eampub.util.EamBillTransportUtils;
import nc.vo.eampub.vochange.AddApprovePushFlagRule;
import nc.vo.eom.failure.BillGenTypeConst;
import nc.vo.eom.failure.FailureBodyVO;
import nc.vo.eom.failure.FailureHeadVO;
import nc.vo.eom.failure.FailureVO;
import nc.vo.eom.failure.FailureValidityConst;
import nc.vo.eom.failure.validator.DateValidator;
import nc.vo.eom.failure.validator.FailureGenNextValidator;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.VOStatus;
import nc.vo.pub.compiler.PfParameterVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDateTime;

import org.apache.commons.lang.StringUtils;

/**
 * 故障记录基础后台实现类
 *
 * @author 段云涛
 * @version 6.0
 */
public class FailureBaseImpl extends BillBaseDAO<FailureVO> implements IFailureBase {

	/** 校验服务 */
	private ValidateServiceRule<FailureVO> validationRule = null;
	// 故障记录 推/拉 生成标识
	private UFBoolean pushFlag;
	// 单据删除类型：手动删除 或 上游弃审后删除
	private String billDeletedType = BILL_DELETED_BY_HAND;
	private static final String BILL_DELETED_BY_HAND = "BillDeletedByHand";
	private static final String BILL_DELETED_WHEN_SRC_BILL_UNAPPROVED = "BillDeletedWhenSrcBillUnapproved";

	// * 故障记录新增保存操作
	public Object insert(FailureVO vo) throws BusinessException {
		if (vo == null)
			return vo;
		// 故障结束时间不为空则回写关闭状态
		updateCloseFlag(vo);
		
		// 生成校验规则
		genValidationRule(vo);

		// 保存到数据库
		FailureVO[] billVOs = insertBillVO(vo);

		if (ArrayUtils.isEmpty(billVOs)) {
			throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("failure_0","04540002-0016")/*@res "数据插入数据库失败！"*/);
		}
		// 用于减少流量
		return EamBillTransportUtils.createTransBillVOForInsert(billVOs[0], null, null);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void initInsertActionRules(IActionTemplate<FailureVO> insertAction) {		
		super.initInsertActionRules(insertAction);
		// 保存前处理后编码
		insertAction.addBeforeRule(new BillCodeInsertBeforeRule<FailureVO>());
		// 设置业务流程
		insertAction.addBeforeRule(new AppendBusiTypeBeforeRule<FailureVO>());
		// 校验数据完整性
		insertAction.addBeforeRule(new DataIntegralityCheckByMDRule<FailureVO>());
		// 校验规则
		insertAction.addBeforeRule(getValidationServiceRule());
		//在保存之前添加id
		insertAction.addBeforeRule(new AddIdFailureInfoRule());
		// 字段长度校验		
		insertAction.addBeforeRule(new VOFieldLengthCheckRule<FailureVO>(lengthCheckFields()));		

		// 保存后提交前编码
		insertAction.addAfterRule(new BillCodeInsertAfterRule<FailureVO>());
		
		// 来源信息校验
		GeneralFunc4SrcBillRule<FailureVO> srcValidationRule = getHandleSrcBillsRule();
		srcValidationRule.setFuncType(FuncType.Validate);
		insertAction.addBeforeRule(srcValidationRule);
		// 回写
		GeneralFunc4SrcBillRule<FailureVO> genFuncRule = (GeneralFunc4SrcBillRule<FailureVO>) srcValidationRule.clone();
		genFuncRule.setFuncType(FuncType.Insert);
		insertAction.addAfterRule(genFuncRule);
		//故障记录保存成功后，添加到多故障原因连接实体表中
		insertAction.addAfterRule(new FailureReasonRelationRule());
		//20240808 yza 九洲瑞博苏州需求 故障记录生成时，向用户发送邮件
		insertAction.addAfterRule(new AddFailureSendEmilRule());
		
	}
	
	// 需要长度校验的字段
	private Set<String> lengthCheckFields() {
		Set<String> checkFields = new HashSet<String>();
		checkFields.add(FailureBodyVO.TROUBLE_LOCATION);// 故障部位
		checkFields.add(FailureBodyVO.FAILURE_SCENE);   // 发生地点
		checkFields.add(FailureBodyVO.DESCRIPTION);		// 发生经过
		checkFields.add(FailureBodyVO.FAILURE_EXPOUND); // 故障详细说明
		checkFields.add(FailureBodyVO.DIRECT_CLAUSE);	// 直接原因
		checkFields.add(FailureBodyVO.INDIRECT_CLAUSE);	// 间接原因
		checkFields.add(FailureBodyVO.MINOR_CLAUSE);	// 次要原因
		checkFields.add(FailureBodyVO.MANAGE_CLAUSE);	// 管理原因
		checkFields.add(FailureBodyVO.DEAL_RESULT);		// 处理结果
		checkFields.add(FailureBodyVO.INFLUENCE);		// 工序影响
		checkFields.add(FailureBodyVO.EFFECT);			// 对前后工序影响
		checkFields.add(FailureBodyVO.COORDINATE);		// 位置点坐标
		checkFields.add(FailureBodyVO.COORDINATE_DESC); // 坐标说明
		checkFields.add(FailureBodyVO.MEMO);			// 备注		
		
		return checkFields;
	}
	

	// ###############################

	// * 故障记录删除操作
	public void delete(FailureVO vo) throws BusinessException {
		if (vo == null)
			return;
		// 加锁并检验
		LockManager.lockAggVOs(new FailureVO[] { vo }, new String[] { "ts" }, null);
		// 由于前台传来的VO仅包含少量VO信息，如主键、TS等，故需要重新查找数据
//		String[] pks = new String[] { vo.getPrimaryKey() };
		// CHECKED_DUANYT 现在应该不用重新查VO了
//		FailureVO[] realVOs = QueryUtil.queryBillVOByPks(FailureVO.class, pks, true);
		// 删除单据
		deleteBillVO(vo);
	}

	/**
	 * 测量记录删除操作。
	 *
	 * @param context
	 * @param vo
	 * @throws BusinessException
	 */
	public void delete(FailureVO[] vos) throws BusinessException {
		LockManager.lockAggVOs(vos, new String[] { CommonKeyConst.ts }, null);
		deleteBillVO(vos);
	}
	
	@Override
	protected void initDeleteActionRules(IActionTemplate<FailureVO> deleteTemplate) {
		super.initDeleteActionRules(deleteTemplate);		
		// 删除后回退单据编码
		deleteTemplate.addAfterRule(new BillCodeDeleteRule<FailureVO>());
		// 单据删除后，回写上游单据的 生成故障标识
		GeneralFunc4SrcBillRule<FailureVO> genFuncRule = getHandleSrcBillsRule();
		genFuncRule.setFuncType(FuncType.Delete);
		deleteTemplate.addAfterRule(genFuncRule);
		//故障记录删除成功后，删除对应的多故障连接表中的数据
		deleteTemplate.addAfterRule(new FailureReasonRelationRule());
		
		// 当手动删除 推式/拉式生成的故障记录时,将其对应的来源故障记录表体行的 生成类型 设置为NOT_PUSH_OR_PULL
		// 这样该表体行在故障来源选择界面可以再选择合并
		if(BILL_DELETED_BY_HAND.equals(billDeletedType)) {
			UpdateBillGenTypeWhenMergedBodyVODeleteRule updateBillGenTypeRule = 
					new UpdateBillGenTypeWhenMergedBodyVODeleteRule();		
			deleteTemplate.addAfterRule(updateBillGenTypeRule);
		}
		
	}
	// ###############################

	// * 故障记录修改保存操作
	public Object update(FailureVO vo) throws BusinessException {
		if (vo == null)
			return vo;
		// 加锁并检验
		LockManager.lockAggVOs(new FailureVO[] { vo }, new String[] { "ts" }, null);
		// 故障结束时间不为空则回写关闭状态
		updateCloseFlag(vo);
		// 生成校验规则
		genValidationRule(vo);
		// 更新操作需要数组参数，vo转为数组调用父类方法
		FailureVO[] billVOs = updateBillVO(new FailureVO[] { vo }, null);
		//方法用于修改故障原因字段
		updateMultiReason(vo);
		// 返回值处理
		if (ArrayUtils.isEmpty(billVOs))
			throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("failure_0","04540002-0017")/*@res "数据更新到数据库失败！"*/);
		// 用于减少流量
		return EamBillTransportUtils.createTransBillVOForUpdate(billVOs[0], null, null);
	}
	
	
	/**
	 * 方法用于修改故障原因字段
	 * @param vo
	 * @throws DAOException
	 */
	private void updateMultiReason(FailureVO vo) throws DAOException {
		FailureBodyVO[] failureBodyVOs=(FailureBodyVO[]) vo.getChildrenVO();
		if(ArrayUtils.isEmpty(failureBodyVOs)){
			return ;
		}
		List<String> list=new ArrayList<String>();
		//获取故障记录子表主键放入list
		for (FailureBodyVO failureBodyVO : failureBodyVOs) {
				String pk_failure_b = failureBodyVO.getPk_failure_b();
				if(StringUtils.isNotEmpty(failureBodyVO.getPk_failure_reason())){
					list.add(pk_failure_b);
				}
		}
		//删除连接表中的有关pk_failure_b的所有记录
		if(list.size()>0){
			String sql="  pk_source in " + InSqlManager.getInSQLValue(list.toArray(new String[0]));
			new DBAccessUtil().getBaseDAO().deleteByClause(FailureReasonRelation.class, sql);
		}
		//将新输入插入数据库
		List<FailureReasonRelation> listFailure=new ArrayList<FailureReasonRelation>();
		for (FailureBodyVO failureBodyVO : failureBodyVOs) {
			if (VOStatus.DELETED != failureBodyVO.getStatus()) {
				String pk_failure_reason=failureBodyVO.getPk_failure_reason();
				String pk_failure_b=failureBodyVO.getPk_failure_b();
				if(StringUtils.isNotEmpty(pk_failure_reason)){
					if(pk_failure_reason.contains(CommonKeyConst.FAILURE_NAME_DIVIDE)){
						String[] pk_reasons=pk_failure_reason.split(CommonKeyConst.FAILURE_NAME_DIVIDE);
						for(String pk_reason : pk_reasons){
							FailureReasonRelation failureReasonRelation=new FailureReasonRelation();
							failureReasonRelation.setPk_source(pk_failure_b);
							failureReasonRelation.setPk_failure_reason(pk_reason);
							listFailure.add(failureReasonRelation);
						}
						//只有一个故障原因
					}else{
						FailureReasonRelation failureReasonRelation=new FailureReasonRelation();
						failureReasonRelation.setPk_source(pk_failure_b);
						failureReasonRelation.setPk_failure_reason(pk_failure_reason);
						listFailure.add(failureReasonRelation);
					}
				}
			}
		}
		if(listFailure.size()>0){
			new DBAccessUtil().getBaseDAO().insertVOList(listFailure);
		}
	}

	/**
	 * 修改故障记录时间时，修改关闭状态
	 * @param vo
	 */
	private void updateCloseFlag(FailureVO vo) {
		FailureBodyVO[] bodyVOs = (FailureBodyVO[]) vo.getChildrenVO();
		if (bodyVOs != null) {
			for (FailureBodyVO bodyVO : bodyVOs) {
				// 故障记录结束时间不为空，则将回写关闭字段
				if (bodyVO.getRestore_time() != null) {
					bodyVO.setClose_flag(UFBoolean.TRUE);
				}else{
					bodyVO.setClose_flag(UFBoolean.FALSE);
				}
			}
		}
	}

	@Override
	protected void initUpdateActionRules(IActionTemplate<FailureVO> updateTemplate) {
		super.initUpdateActionRules(updateTemplate);
		// 增加执行校验规则
		updateTemplate.addBeforeRule(getValidationServiceRule());
		updateTemplate.addBeforeRule(new BillCodeUpdateRule<FailureVO>());
		// 设置业务流程
		updateTemplate.addBeforeRule(new AppendBusiTypeBeforeRule<FailureVO>());

		// 回写(在更新前调用)上游单据的生成故障标识
		GeneralFunc4SrcBillRule<FailureVO> genFuncRule = getHandleSrcBillsRule();
		genFuncRule.setFuncType(FuncType.Update);
		updateTemplate.addBeforeRule(genFuncRule);
		
		// 当删除 合并拉式生成的故障记录表体行时,将其对应的来源故障记录表体行的 生成类型 设置为NOT_PUSH_OR_PULL
		// 这样该表体行在故障来源选择界面可以再选择合并
		UpdateBillGenTypeWhenMergedBodyVODeleteRule updateBillGenTypeRule = 
				new UpdateBillGenTypeWhenMergedBodyVODeleteRule();		
		updateTemplate.addBeforeRule(updateBillGenTypeRule);
		
		// 字符长度校验
		updateTemplate.addBeforeRule(new VOFieldLengthCheckRule<FailureVO>(lengthCheckFields()));	
		
		//故障记录修改成功后，修改对应的多故障连接表中的数据
		//updateAction.addAfterRule(new FailureReasonRelationRule());
	// ###############################
	}

	// 推式保存
	public FailureVO[] pushSave(FailureVO[] billVOs) throws BusinessException {
		if (ArrayUtils.isEmpty(billVOs)) {
			return billVOs;			
		}
		
		UFBoolean pushFlag = ((FailureHeadVO)billVOs[0].getParentVO()).getPushFlag();
		setPushFlag(pushFlag);
		return pushSaveBillVO(billVOs);
	}

	@Override
	protected void initPushSaveActionRules(IActionTemplate<FailureVO> insertTemplate) {
//		super.initPushSaveActionRules(insertTemplate);
		// 设置表头vo状态为修改状态
		insertTemplate.addBeforeRule(new VoStatusSetBeforeRule<FailureVO>(VOStatus.NEW));
		// 设置新增时审计信息
		insertTemplate.addBeforeRule(new AddAuditInfoBeforeRule<FailureVO>());
		// 生成单据号(暂存态故障记录的单据号：设置为TEMPSAVEGZJL)
		//template.addBeforeRule(new BillCodePushSaveBeforeRule<T>());
		insertTemplate.addBeforeRule(new FailureBillCodePushSaveBeforeRule());
		// 补充主组织VID，必须在SetBizScopeInfoBeforeRule之前
		insertTemplate.addBeforeRule(new SetOrgVidBeforeRule<FailureVO>());
		// 设置组织、集团等业务范围信息
		insertTemplate.addBeforeRule(new SetBizScopeInfoBeforeRule<FailureVO>());
		// 业务流程单据补充交易类型
		insertTemplate.addBeforeRule(new AppendTransiOrBusiTypeRule<FailureVO>());
				
		// 推式保存前，补充数据
		insertTemplate.addBeforeRule(new MakeUpFailureInfoRule());
		// 推式保存前，设置跳转后的业务流程
		insertTemplate.addBeforeRule(new FailureBusiTypeJumpRule());
		
		// 回写来源信息
		GeneralFunc4SrcBillRule<FailureVO> writeBackSrcBillRule = getHandleSrcBillsRule();
		writeBackSrcBillRule.setFuncType(FuncType.Insert);
		// 校验来源单据
		// genFuncRule.getGenFunc4SrcBill().setNeedCheckSrc(true);
		insertTemplate.addAfterRule(writeBackSrcBillRule);

		// 如果为推单，则同时生成暂存态和提交态的单据
		boolean pushToFailure = (UFBoolean.TRUE).equals(getPushFlag());
		if (pushToFailure) {
			insertTemplate.addAfterRule(new GenFailureByTempSaveFailureAfterPushSaveRule());
		}
		//故障记录保存成功后，添加到多故障原因连接实体表中
		insertTemplate.addAfterRule(new FailureReasonRelationRule());
		//yza 20240911 推式生成故障记录，发邮件通知用户
		insertTemplate.addAfterRule(new AddFailureSendEmilRule());
		
	}
	// ###############################

	/** 故障记录送审操作 */
	public FailureVO[] commit(FailureVO[] vos) throws BusinessException {
		// 生成校验规则
		genValidationRule(null);
		// 调用父类送审操作
		FailureVO[] failureVOs = commitBillVO(vos);
		// 操作成功后返回数组failureVOs第0个元素与vo相同
		if (ArrayUtils.isEmpty(failureVOs)) {
			throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("failure_0","04540002-0018")/*@res "提交失败！"*/);
		}

		return failureVOs;
	}

	@Override
	protected void initCommitActionRules(IActionTemplate<FailureVO> template) {
		super.initCommitActionRules(template);
		// 审核前的校验处理
		template.addBeforeRule(validationRule);
	}

	// ###############################

	/**  故障记录收回操作 */
	public Object unSave(FailureVO... vos) throws BusinessException {
		return unSaveBillVOs(vos);
	}
	// ###############################

	/**  故障记录审核操作 */
	@Override
	public Object approveBillVOs(AbstractCompiler2 asc,
			PfParameterVO parameterVo) throws BusinessException {
		// 生成校验规则
		genValidationRule(null);
		return super.approveBillVOs(asc, parameterVo);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void initApproveRule(ApproveActionTemplate<FailureVO> template) {
		super.initApproveRule(template);
		// 审核前的校验处理
		// 审批前校验必输项，如果是故障有效性字段为有效，则故障类别、故障现象字段为必输。
		FailureTypeAndSymptomNotNullValidator failureTypeAndSymptomValidator = 
				new FailureTypeAndSymptomNotNullValidator();
		validationRule.addValidator(failureTypeAndSymptomValidator);
		template.addApprovedBeforeRule(validationRule);
		
		// 审核通过后回写设备状态
		template.addApprovePassAfterRule(new UpdateEquipStatusRule(true));
		// 审核通过后，添加推式生单的标志
		template.addApprovePassAfterRule(new AddApprovePushFlagRule<FailureVO>());
	}
	// ###############################

	/** 故障记录弃审操作 */
	@Override
	public Object unApproveBillVOs(AbstractCompiler2 asc,
			PfParameterVO parameterVo) throws BusinessException {
		//弃审表体表头字段校验
		LockManager.lockAggVOs(parameterVo.m_preValueVos, new String[]{CommonKeyConst.ts}, new String[]{CommonKeyConst.ts});
		return super.unApproveBillVOs(asc, parameterVo);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void initUnApproveRule(UnApproveActionTemplate<FailureVO> template) {
		// 弃审前校验
		template.addUnApprovedBeforeRules(getValidationServiceRule(unAuditValidate()));
		//template.addUnApprovedBeforeRules(new UnApproveFailureRule());
		super.initUnApproveRule(template);
		// 弃审后回写设备状态
		template.addLastUnApprovedAfterRules(new UpdateEquipStatusRule(false));
	}

	/** 故障记录弃审操作前的校验方法 */
	public Validator[] unAuditValidate() {
		// 是否生成下游单据的校验
		FailureGenNextValidator validator = new FailureDBGenNextValidator();
		// 待校验字段
		List<String> checkKeyFields = new ArrayList<String>();
		checkKeyFields.add(FailureBodyVO.WO_FLAG);
		validator.setSingleTabKeyFields(checkKeyFields);
		// 错误提示信息
		validator.setErrMsg(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("failure_0","04540002-0019")/*@res "已生成下游单据，不能进行弃审操作"*/);
		return new Validator[] { validator };
	}
	// ###############################

	/** 生成校验规则 */
	private void genValidationRule(FailureVO billVO) {
		List<Validator> validators = new ArrayList<Validator>();
		// // 设备和位置不能都为空校验
		// validators.add(new NotEmptyValidator());
		// 非空校验
//		validators.add(new FailureNullValidator());
		validators.add(new FailureDataNotNullValidator());
		validators.add(new FailureFieldScaleValidator());
		
		// 开始日期不能在结束日期之后校验
		validators.add(new DateValidator());
		
		// 委托维修组织的验证
		validators.add(new RelatedMaintainOrgValidator());
		
		if (billVO != null) {
			// 设备校验
			FailureHeadVO headVO = (FailureHeadVO) billVO.getParentVO();
			// 校验设备未处于处置状态和拥有其使用权
			EquipValidator equipValidator = EquipValidator.getValidator();
			equipValidator.setOwnerOrg(false);
			equipValidator.setPk_orgs(headVO.getPk_org());
			validators.add(equipValidator);
		}
		// 构建校验服务
		validationRule = new ValidateServiceRule<FailureVO>(validators);
	}

	/** 获取校验服务 */
	protected ValidateServiceRule<FailureVO> getValidationServiceRule() {
		if (validationRule == null)
			validationRule = new ValidateServiceRule<FailureVO>();
		return validationRule;
	}

	protected ValidateServiceRule<FailureVO> getValidationServiceRule(Validator[] validators) {
		if (validationRule == null)
			validationRule = new ValidateServiceRule<FailureVO>();
		validationRule.setValidators(validators);
		return validationRule;
	}

	/** 获取对来源单据的操作规则 */
	protected GeneralFunc4SrcBillRule<FailureVO> getHandleSrcBillsRule() {
		GeneralFunc4SrcBillRule<FailureVO> writeBackSrcBillsRule = new GeneralFunc4SrcBillRule<FailureVO>();
		// 运行记录
		writeBackSrcBillsRule.addBusiFunc4SrcBill(new FailureGenFunc4Operation());
		// 工单
		writeBackSrcBillsRule.addBusiFunc4SrcBill(new FailureGenFunc4WorkOrder());
		// 测量记录
		writeBackSrcBillsRule.addBusiFunc4SrcBill(new FailureDocWriteBackMeasureDoc());
		// 日常点检
		writeBackSrcBillsRule.addBusiFunc4SrcBill(new FailureDocWriteBackDailyPointCheck());
		// 专业点检
		writeBackSrcBillsRule.addBusiFunc4SrcBill(new FailureDocWriteBackSpecialPointCheck());
		// 巡检记录
		writeBackSrcBillsRule.addBusiFunc4SrcBill(new FailureDocWriteBackInspection());
		// 维修计划
		writeBackSrcBillsRule.addBusiFunc4SrcBill(new FailureGenFunc4WorkOrder());
		
		return writeBackSrcBillsRule;
	}

	/**
	 * <p>
	 * 通过设备主键，查询对应设备或位置下的故障记录主键信息
	 * </p>
	 *
	 * @param pks
	 *            设备主键或位置主键
	 * @param field
	 *            pk_equip 或者 pk_location
	 * @return 主键与对应故障记录主键数组的Map
	 * @throws BusinessException
	 */
	@SuppressWarnings("rawtypes")
	public Map<String, String[]> queryFailureIDsByIDs(String[] pks, String field) throws BusinessException {
		// 设备主键与对应故障记录主键数组的Map
		Map<String, String[]> failureIDs = new HashMap<String, String[]>();
		if (ArrayUtils.isEmpty(pks))
			return failureIDs;
		// 查询设备主键、故障记录主键
		StringBuilder querySql = new StringBuilder();
		querySql.append(" SELECT ");
		querySql.append(field);
		querySql.append(" , eom_failure_b.pk_failure ");
		querySql.append(" FROM eom_failure_b ");
		querySql.append(" WHERE ");
		querySql.append(field);
		if (pks.length == 1)
			querySql.append(" = '").append(pks[0]).append("'");
		else
			querySql.append(" in ").append(InSqlManager.getInSQLValue(pks));
		querySql.append(" AND eom_failure_b.dr = 0");

		// 查询
		List queryResult = new DBAccessUtil().executeQuerySQL(querySql.toString());

		// 若查询结果不为空，则保存查询信息
		if (CollectionUtils.isNotEmpty(queryResult)) {
			// 主键缓存（主键——故障记录主键）
			Map<String, Set<String>> idCache = new HashMap<String, Set<String>>();
			for (Object result : queryResult) {
				if (result != null && result.getClass().isArray()) {
					String fieldPk = ((Object[]) result)[0].toString();
					String pk_failure = ((Object[]) result)[1].toString();
					if (idCache.containsKey(fieldPk)) {
						Set<String> pk_failures = idCache.get(fieldPk);
						pk_failures.add(pk_failure);
					} else {
						Set<String> pk_failures = new HashSet<String>();
						pk_failures.add(pk_failure);
						idCache.put(fieldPk, pk_failures);
					}
				}
			}
			// 格式转换
			for (Map.Entry<String, Set<String>> currEquip : idCache.entrySet()) {
				failureIDs.put(currEquip.getKey(), currEquip.getValue()
						.toArray(new String[currEquip.getValue().size()]));
			}
		}

		return failureIDs;
	}
	//@Override
	/**
	 * 查询多故障原因
	 */
//	@SuppressWarnings("rawtypes")
//	public List queryMultiReason(String[] pk_failure_bs) throws BusinessException {
//		InSqlBuilder builder = InSqlManager.newBuilder();
//		String sql="select a.pk_source,b.pk_failure_reason,b.reason_name from " +
//				" pam_reasonrelation a, pam_failure_reason b "+
//				" where a.pk_failure_reason=b.pk_failure_reason " +
//				" and a.pk_source in " + builder.getInSQLValue(pk_failure_bs);
//		List list=new DBAccessUtil().executeQuerySQL(sql);
//		return list;
//	}

	/**
	 * 查询暂存状态的故障记录
	 * @return
	 */
	public FailureVO[] queryTempSaveFailureVOsByWhereSql(String whereSql) throws BusinessException{		
		boolean isQueryBody = true;
		String order = null;
		FailureVO[] bills = QueryUtil.queryBillVOByHeadCond(
				FailureVO.class, whereSql.toString(), order, isQueryBody);
		return bills;
	}
	
	// 根据条件语句查询故障记录表体	
	public FailureBodyVO[] queryBodyVOsByWhereSql(String whereSql)
			throws BusinessException {
		if (whereSql == null) {
			return null;
		}

		String[] allFields = null;
		FailureBodyVO[] results = QueryUtil.querySuperVOBySQL(allFields,
				whereSql, FailureBodyVO.class);
		return results;
	}
	
	
	/**
	 * 通过故障记录表体主键，查询相关字段信息
	 */
	@Override
	public FailureBodyVO queryFailureBodyVOSpecialInfoByPkBody (
			String pkFailureBody, String[] queryFields) throws BusinessException{
		if(pkFailureBody.isEmpty()) {
			return null;
		}
		
		return QueryUtil.querySuperVOByPks(FailureBodyVO.class, queryFields, new String[]{ pkFailureBody })[0];
	}

	/**
	 * 通过故障记录表体主键，获取其对应的照片路径
	 */
	@Override
	public Map<String, byte[]> queryFailureImagesFor(String pkBodyVO) throws BusinessException{
		if(pkBodyVO.isEmpty()) {
			return null;
		}
		
		StringBuilder querySql = new StringBuilder();
		/*
		querySql.append(" SELECT pk, filepath ")
		   		.append(" FROM 	 sm_pub_filesystem ")		   		
		   		.append(" WHERE  pk_doc IN (")
		   		.append("    SELECT pk_fileexattr ")
		   		.append("    FROM   bd_fileexattr ")
		   		.append("    WHERE  pk_exattr = '" + pkBodyVO + "'")
		   		.append("	        AND dr = 0 ")
		   		.append("    )" );
		*/
		querySql.append(" SELECT pk, filepath ")
   				.append(" FROM 	 sm_pub_filesystem ")		   		
   				.append(" WHERE  isfolder = 'n' ")
   				.append("   AND  filepath like '%" + pkBodyVO + "%' ")
   				.append("	AND  dr = 0 ");

		@SuppressWarnings("unchecked")
		Map<String, String> fullPaths = (Map<String, String>) new DBAccessUtil().executeQuery(
				querySql.toString(),
				new ResultSetProcessor() {
					public Object handleResultSet(final ResultSet rs) throws SQLException {
						final Map<String, String> fullPaths = new HashMap<String, String>();
						while (rs.next()) {
							final String pkImage = rs.getString("pk");
							final String aPath = rs.getString("filepath");
							if (!aPath.isEmpty()) {
								fullPaths.put(pkImage, aPath);
							}
						}						
						return fullPaths;					}
				});		
		
		
		if (fullPaths == null || fullPaths.isEmpty()) {
			return null;
		}
		
		IFileSystemService fileSystemService = 
				NCLocator.getInstance().lookup(IFileSystemService.class);
		// String dataSourceName = DataSourceCenter.getInstance().getSourceName();
		List<FileOutputStream> files = new ArrayList<FileOutputStream>();
		Map<String, byte[]> images = new HashMap<String, byte[]>();
		for (String pkImage : fullPaths.keySet()) {
			// File aFile = new File(aPath);
			// FileOutputStream fos = null;
			String imagePath = fullPaths.get(pkImage);
			ByteArrayOutputStream imageByteStream = new ByteArrayOutputStream();			
			fileSystemService.downLoadFile(imagePath, imageByteStream);
			byte[] imageBytes = imageByteStream.toByteArray();
			if (imageBytes == null) {
				continue;
			}
			
			// 获取图片文件名
			int lastSeparateIndex = imagePath.lastIndexOf("/");
			String imageName = imagePath.substring(lastSeparateIndex, imagePath.length());
			String imageKey = pkImage + "_" + imageName;
			images.put(imageKey, imageBytes);
			try {
				if (imageByteStream != null) {
					imageByteStream.close();
				}
			} catch (Exception e) {
				throw new BusinessException(e);
			}
			
			
			/*
			try {
				fos = new FileOutputStream(aFile);
				fileSystemService.downLoadFile(aPath, fos);				
			} catch (Exception e) {
				throw new BusinessException(e);
			} finally {				
				try {
					if (fos != null) {
						fos.close();
					}
				} catch (Exception e) {
					throw new BusinessException(e);
				}
									
			}
			*/
			

		}
		
		return images;
	}

	@Override
	public SuperVO[] queryFailureBodyVOsWithCoordinateByOrg(String pkOrg)
			throws BusinessException {
		if(pkOrg == null) {
			return null;
		}	
				
		StringBuilder whereSql = new StringBuilder();
		whereSql.append(orgSql(pkOrg))
	       		.append(" AND " + auditBillStatusSql());
		
		String nullOrder = null;
		boolean queryBody = true;
		FailureVO[] bills = QueryUtil.queryBillVOByHeadCond(
				FailureVO.class, whereSql.toString(), nullOrder, queryBody);
		if(ArrayUtils.isEmpty(bills)) {
			return null;
		}
		
		
		List<SuperVO> bodyVOsWithCoordinate = new ArrayList<SuperVO>();
		for(FailureVO aBill : bills) {
			SuperVO[] bodyVOs = aBill.getAllChildrenVO();
			for(SuperVO aBodyVO : bodyVOs) {
				FailureBodyVO aFailureBodyVO = (FailureBodyVO)aBodyVO;
				String coordinate = aFailureBodyVO.getCoordinate();
				boolean hasCoordinate = (coordinate != null && !coordinate.trim().equals("~"));				
				
				// 过滤出有效的故障记录
				Integer failureValidity = aFailureBodyVO.getValidity();
				boolean isValidFailure = (FailureValidityConst.valid == failureValidity);
				
				// 过滤出未处理完成的故障记录(生成工单未完成状态或未列入维修计划)，根据故障结束时间判断
				UFDateTime restoreTime = aFailureBodyVO.getRestore_time();
				boolean hasNotCompleted = (restoreTime == null);						
				
				if(hasCoordinate && isValidFailure && hasNotCompleted) {
					bodyVOsWithCoordinate.add(aBodyVO);
				}
			}
		}
		
		return bodyVOsWithCoordinate.toArray(new SuperVO[0]);
	}
	
	private String orgSql(String pkOrg) {
		return "pk_org = '" + pkOrg + "'";
	}
	
	private String auditBillStatusSql() {
		return "bill_status = " + FailureStatusConst.check_pass;	
	}
	
	/**
	 * 上游单据弃审时，删除其生成的故障记录表体行
	 */	
	//       <上游单据主键, <下游故障记录单据号，     行号列表>>	
	public Map<String, Map<String, List<String>>> deleteFailureDocGeneratedFrom(
			String[] srcBillPks) throws BusinessException {
		if (ArrayUtils.isEmpty(srcBillPks)){
			return null;
		}
		
		//<上游单据主键, <下游故障记录单据号， 行号列表>>
		Map<String, Map<String, List<String>>> results = null;
		DeleteFailureDocGeneratedFromSrcBill service = new DeleteFailureDocGeneratedFromSrcBill();
		results = service.getCannotDeleteFailureBillInfos(srcBillPks);
		
		// 如果存在不可以删除的下游故障记录单据，则表明上游单据不可以弃审
		boolean canUnapprove = (results == null || results.isEmpty());		
		// 如果上游单据可以弃审，则删除下游故障记录单据
		if (canUnapprove) {
			List<FailureVO> canDeleteBills = service.getDeletableFailureBills();
			if (canDeleteBills != null && canDeleteBills.size() > 0) {
				LockManager.lockAggVOs(canDeleteBills.toArray(new FailureVO[0]), 
						new String[] { "ts" }, null);
				
				billDeletedType = BILL_DELETED_WHEN_SRC_BILL_UNAPPROVED;
				deleteBillVO(canDeleteBills.toArray(new FailureVO[0]));			
				resetBillDeletedType();
			}			
		}
		
		return results;
	}

	private void resetBillDeletedType() {
		billDeletedType = BILL_DELETED_BY_HAND;
		
	}

	public UFBoolean getPushFlag() {
		return pushFlag;
	}

	public void setPushFlag(UFBoolean pushFlag) {
		this.pushFlag = pushFlag;
	}

	/**
	 * 通过故障记录表体主键，查询故障记录单据相关字段信息
	 */
	@Override
	public FailureHeadVO queryFailureVOSpecialInfoByPkBody(String pkFailureBody)
			throws BusinessException {
		StringBuilder querySql = new StringBuilder();
		querySql.append(" SELECT A.pk_failure, A.bill_type, A.transi_type ")
		   		.append(" FROM 	 eom_failure_b B ")
		   		.append(" LEFT JOIN eom_failure A ON B.pk_failure = A.pk_failure ")
		   		.append(" WHERE B.pk_failure_b = '" + pkFailureBody + "' ")
		   		.append("	   AND A.dr = 0");
		
		@SuppressWarnings("unchecked")
		FailureHeadVO aHeadVO = (FailureHeadVO) new DBAccessUtil().executeQuery(
				querySql.toString(),
				new ResultSetProcessor() {
					public Object handleResultSet(final ResultSet rs) throws SQLException {
						final FailureHeadVO headVO = new FailureHeadVO();
						while (rs.next()) {
							final String pkBill = rs.getString(FailureHeadVO.PK_FAILURE);
							final String billType = rs.getString(FailureHeadVO.BILL_TYPE);
							final String transiType = rs.getString(FailureHeadVO.TRANSI_TYPE);
							headVO.setPk_failure(pkBill);
							headVO.setBill_type(billType);
							headVO.setTransi_type(transiType);							
						}
						
						return headVO;					}
				});		
		
		return aHeadVO;
	}
	
	/**
	 * 根据故障原因编码查询故障原因主键，excel导入使用
	 * @throws BusinessException 
	 */
	@Override
	public Object queryMultiReasonByCode() throws BusinessException {
		final String reasonName = MultiLanguageUtil.getMultiNameField(FailurereasonVO.REASON_NAME);
		String sql = " select reason_code, pk_failure_reason,"+reasonName+" from pam_failure_reason where dr=0 "+
				" and pk_group ='"+InvocationInfoProxy.getInstance().getGroupId()+"'";;
		Object obj = new DBAccessUtil().getBaseDAO().executeQuery(sql, new BaseProcessor(){
			private static final long serialVersionUID = 1L;

			@Override
			public Object processResultSet(ResultSet rs) throws SQLException {
				Map<String, Object> rsValues = new HashMap<String, Object>();
				while(rs.next()){
					String code = rs.getString("reason_code");
					String pk = rs.getString("pk_failure_reason");
					String reason_name = rs.getString(reasonName);
					FailurereasonVO failurereasonVO = new FailurereasonVO();
					failurereasonVO.setPk_failure_reason(pk);
					failurereasonVO.setReason_code(code);
					failurereasonVO.setReason_name(reason_name);
					rsValues.put(code, failurereasonVO);
				}
				return rsValues;
			}
		});
		return obj;
	}
	
	/**
	 * 根据故障原因名称查询故障原因编码，excel导入使用
	 * @throws BusinessException 
	 */
	@Override
	public Object queryMultiReasonByName() throws BusinessException {
		final String reasonName = MultiLanguageUtil.getMultiNameField(FailurereasonVO.REASON_NAME);
		String sql = " select reason_code, pk_failure_reason,"+reasonName+" from pam_failure_reason where dr=0 " +
				" and pk_group ='"+InvocationInfoProxy.getInstance().getGroupId()+"'";
		Object obj = new DBAccessUtil().getBaseDAO().executeQuery(sql, new BaseProcessor(){
			private static final long serialVersionUID = 1L;

			@Override
			public Object processResultSet(ResultSet rs) throws SQLException {
				Map<String, Object> rsValues = new HashMap<String, Object>();
				while(rs.next()){
					String code = rs.getString("reason_code");
					String pk = rs.getString("pk_failure_reason");
					String reason_name = rs.getString(reasonName);
					FailurereasonVO failurereasonVO = new FailurereasonVO();
					failurereasonVO.setPk_failure_reason(pk);
					failurereasonVO.setReason_code(code);
					failurereasonVO.setReason_name(reason_name);
					rsValues.put(reason_name, failurereasonVO);
				}
				return rsValues;
			}
		});
		return obj;
	}
	
	
	
	/**
	 * 根据故障类别名称查询故障类别主键，excel导入使用
	 * @throws BusinessException 
	 */
	@Override
	public Object queryMultiTypeByName() throws BusinessException {
		//final String typeName = MultiLanguageUtil.getMultiNameField(FailuretypeVO.TYPE_NAME);
		String sql = " select type_code, pk_failure_type from pam_failure_type where dr=0 "+
				" and pk_group ='"+InvocationInfoProxy.getInstance().getGroupId()+"'";
		Object obj = new DBAccessUtil().getBaseDAO().executeQuery(sql,new BaseProcessor(){
			private static final long serialVersionUID = 1L;
			@Override
			public Object processResultSet(ResultSet rs) throws SQLException {
				Map<String, Object> rsValues = new HashMap<String, Object>();
				while(rs.next()){
					String name = rs.getString("type_code");
					String value = rs.getString("pk_failure_type");
					rsValues.put(name, value);
				}
				return rsValues;
			}
		});
		return obj;
	}
	
	/**
	 * 合并两个查询条件，流量优化
	 * @throws BusinessException 
	 */
	@Override
	public Map<String,Map> queryReasonAndType() throws BusinessException {
		Map<String,Map> reasonAndType =
				new HashMap<String,Map>();
		
		@SuppressWarnings("unchecked")
		Map<String,String> failureTypeVOMap = (Map<String, String>)this
				.queryMultiTypeByName();
		@SuppressWarnings("unchecked")
		Map<String,FailurereasonVO> failureReasonVOMap = 
				(Map<String, FailurereasonVO>)this.queryMultiReasonByCode();
		reasonAndType.put("reason", failureReasonVOMap);
		reasonAndType.put("type", failureTypeVOMap);
		return reasonAndType;
		
	}
	
	

	// 保存合并生成故障记录
	@Override
	public FailureVO[] pushSaveMergedBills(FailureVO[] mergedBills, FailureBodyVO[] toMergeBodyVOs)
			throws BusinessException {
		// 加锁并校验TS
		lockBodyVOsAndCheckTs(toMergeBodyVOs);					
		
		// 保存 合并生成故障记录
		FailureVO[] savedBills = pushSave(mergedBills);

		// 更新表体生成类型：合并保存成功后，设置合并表体行的生成类型为拉式
		updateBillGenTypeFor(toMergeBodyVOs);
		
		return savedBills;
	}
	

	// 表体加锁并校验TS
	private void lockBodyVOsAndCheckTs(FailureBodyVO[] bodyVOs) throws BusinessException {
		LockManager.lockBaseVOs(bodyVOs, new String[] { FailureBodyVO.TS });
	}
	
	/**
	 * 设置合并标识
	 * @param toMergeBodyVOs
	 * @throws Exception
	 */
	private void updateBillGenTypeFor(FailureBodyVO[] toMergeBodyVOs) throws BusinessException {
		for(FailureBodyVO aBodyVO : toMergeBodyVOs) {
			// 设置合并标识
			aBodyVO.setBill_gen_type(BillGenTypeConst.pull);
		}
		// 更新
		updateMergedBodyVOs(toMergeBodyVOs, new String[]{ FailureBodyVO.BILL_GEN_TYPE });
	}
	

	// 更新合并后的故障记录表体的状态
	private void updateMergedBodyVOs(SuperVO[] mergedBodyVOs, String[] fieldNames) throws BusinessException {
		if (ArrayUtils.isEmpty(mergedBodyVOs)){
			return;
		}
		
		// 加锁
		LockManager.lockSuperVO(mergedBodyVOs);	
		boolean updateTimeStamp = true;
		new DBAccessUtil().updateVOArray(mergedBodyVOs, fieldNames, updateTimeStamp);		
		
	}
	/**
	 * 将DataServiceImpl，和failureserviceimpl两个查询合并，
	 * 这样做主要是为了减少连接次数
	 * @param pks
	 * @return
	 * @throws BusinessException
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Object[] queryObjectByPks(String[] pks) throws BusinessException {
		// 查询当前页表头
		FailureVO[] failureVOs= (FailureVO[]) AMProxy.lookup(IDataService.class)
				.queryObjectByPks(FailureVO.class, pks, CommonKeyConst.default_child_name);
		List<String> listpk=new ArrayList<String>();
		List list=new ArrayList();
		if(ArrayUtils.isNotEmpty(failureVOs)){
			for(FailureVO failureVO : failureVOs){
				FailureBodyVO[] failureBodyVOs = (FailureBodyVO[]) failureVO.getChildrenVO();
				if(ArrayUtils.isNotEmpty(failureBodyVOs)){
					Map<String,List<FailureBodyVO>> pk_failure_b_map = BaseVOUtils.groupingVOsToMap(failureBodyVOs,new String[]{FailureBodyVO.PK_FAILURE_B});
					String [] pk_failure_b_s = pk_failure_b_map.keySet().toArray(new String[0]);
					for(String pk_failure_b : pk_failure_b_s){
						listpk.add(pk_failure_b);
					}
				}
			}
			if(listpk.size()>0){
				String[] pk_failure_bs= listpk.toArray(new String[0]);
				//调用后台服务查询多故障原因
				list=AMProxy.lookup(IFailurereasonService.class).queryMultiReason(pk_failure_bs);
			}
			for(FailureVO failureVO : failureVOs){
				FailureBodyVO[] failureBodyVOs=(FailureBodyVO[]) failureVO.getChildrenVO();
				if(ArrayUtils.isNotEmpty(failureBodyVOs)){
					for (FailureBodyVO failureBodyVO : failureBodyVOs) {
						String pk_failure_bvo=failureBodyVO.getPk_failure_b();
						String pk_failure_reason="";
						String failure_reason_name="";
						for (int i = 0; i < list.size(); i++) {
							Object[] pk_failure_b= (Object[]) list.get(i);
							if(pk_failure_bvo.equals((String)pk_failure_b[0])){
								pk_failure_reason+=(String)pk_failure_b[1]+CommonKeyConst.FAILURE_NAME_DIVIDE;
								failure_reason_name+=(String)pk_failure_b[2]+CommonKeyConst.FAILURE_NAME_DIVIDE;
							}
						}
						if(pk_failure_reason.endsWith(CommonKeyConst.FAILURE_NAME_DIVIDE)){
							pk_failure_reason=pk_failure_reason.substring(0, pk_failure_reason.length()-1);
							failure_reason_name=failure_reason_name.substring(0, failure_reason_name.length()-1);
							failureBodyVO.setPk_failure_reason(pk_failure_reason);
							failureBodyVO.setFailure_reason_name(failure_reason_name);
						}
					}
				}
			}
		}
		return failureVOs;
	}

	// 根据条件语句查询故障记录表体
	@Override
	public FailureBodyVO[] retrieveBodyVOs(String parentPK, String metadatapath) throws BusinessException {
		FailureBodyVO[] failureBodyVOs = (FailureBodyVO[]) AMProxy.lookup(IDataService.class).retrieveBodyVOs(
				FailureVO.class.getName(), parentPK, metadatapath);
		List<String> bodyPks = new ArrayList<String>();		
		for(FailureBodyVO aBodyVO : failureBodyVOs){
			String pkBody = aBodyVO.getPk_failure_b();
			bodyPks.add(pkBody);
		}
		
		if (bodyPks.isEmpty()) {
			return null;
		}	
		String[] bodyPkArray = bodyPks.toArray(new String[0]);
		Map<String, List<String>> failureReasonByPkBody = AMProxy.lookup(IFailurereasonService.class)
				.queryMultiReasonMap(bodyPkArray);

		for (FailureBodyVO aBodyVO : failureBodyVOs) {
			String pkBody = aBodyVO.getPk_failure_b();
			List<String> failureReasonInfo = failureReasonByPkBody.get(pkBody);
			if (failureReasonInfo == null || failureReasonInfo.isEmpty()) {
				continue;
			}

			// 赋值给故障记录表体
			String pkFailureReason = (String) failureReasonInfo.get(0);
			String failureReasonName = (String) failureReasonInfo.get(1);
			aBodyVO.setPk_failure_reason(pkFailureReason);
			aBodyVO.setFailure_reason_name(failureReasonName);

		}

		return failureBodyVOs;
	}
	
}
