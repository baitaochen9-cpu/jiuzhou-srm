package nccloud.web.ct.purdaily.action;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import nc.itf.ct.purdaily.IPurdailyMaintain;
import nc.itf.scmpub.reference.uap.org.DeptPubService;
import nc.itf.scmpub.reference.uap.org.OrgUnitPubService;
import nc.vo.ct.enumeration.CtFlowEnum;
import nc.vo.ct.enumeration.EnumModify;
import nc.vo.ct.purdaily.entity.AggCtPuVO;
import nc.vo.ct.purdaily.entity.CtPaymentVO;
import nc.vo.ct.purdaily.entity.CtPuBVO;
import nc.vo.ct.purdaily.entity.CtPuChangeVO;
import nc.vo.ct.purdaily.entity.CtPuTermVO;
import nc.vo.ct.purdaily.entity.CtPuVO;
import nc.vo.ct.uitl.ValueUtil;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scmpub.util.StringUtil;
import nccloud.dto.ct.pub.utils.OperateExceptionUtils;
import nccloud.dto.ct.purdaily.constance.CommonConst;
import nccloud.framework.core.exception.ExceptionUtils;
import nccloud.framework.core.json.IJson;
import nccloud.framework.service.ServiceLocator;
import nccloud.framework.web.action.itf.ICommonAction;
import nccloud.framework.web.container.IRequest;
import nccloud.framework.web.container.SessionContext;
import nccloud.framework.web.json.JsonFactory;
import nccloud.framework.web.ui.pattern.extbillcard.ExtBillCard;
import nccloud.framework.web.ui.pattern.extbillcard.ExtBillCardOperator;
import nccloud.web.ct.purdaily.info.QueryInfo;
import nccloud.web.ct.purdaily.utils.PrecisionUtil;

/**
 * @description 复制
 * @author xiahui
 * @date 创建时间：2019-2-20 上午10:07:54
 * @version ncc1.0
 * @ref
 **/
public class CopyAction implements ICommonAction {

	@Override
	public Object doAction(IRequest request) {
		String str = request.read();
		IJson json = JsonFactory.create();
		QueryInfo info = json.fromJson(str, QueryInfo.class);

		ExtBillCard retCard = null;
		try {
			IPurdailyMaintain service = ServiceLocator.find(IPurdailyMaintain.class);
			AggCtPuVO[] bills = service.queryCtPuVoByIds(new String[] { info.getPk() });
			// 检验单据删除
			OperateExceptionUtils.checkVo(bills, null);
			// 构建CopyVO
			this.processVOAfterCopy(bills[0]);

			ExtBillCardOperator operator = new ExtBillCardOperator(CommonConst.PAGECODE_CARD);
			retCard = operator.toCard(bills[0]);
			// 精度处理
			PrecisionUtil.setExtCardPrecision(retCard);
		} catch (BusinessException e) {
			ExceptionUtils.wrapException(e);
		}
		return retCard;
	}

	/**
	 * 构建返回前台的CopyVO
	 * 
	 * @param aggCtPuVO
	 */
	private void processVOAfterCopy(AggCtPuVO billVO) {
		// 设置表头
		if (!ValueUtil.isEmpty(billVO.getParentVO())) {
			this.setHeadValue(billVO.getParentVO());
		}
		// 设置表体合同基本页签
		if (!ValueUtil.isEmpty(billVO.getCtPuBVO())) {
			this.setBodyPuValue(billVO.getCtPuBVO(), billVO.getParentVO());
		}
		// 设置表体变更历史页签
		if (!ValueUtil.isEmpty(billVO.getCtPuChangeVO())) {
			this.setBodyChangeValue(billVO);
		}
		// 设置表体合同条款页签
		if (!ValueUtil.isEmpty(billVO.getCtPuTermVO())) {
			this.setBodyTermValue(billVO.getCtPuTermVO(), billVO.getParentVO());
		}
		// 设置表体合同费用页签
		if (!ValueUtil.isEmpty(billVO.getCtPuExpVO())) {
			billVO.setCtPuExpVO(null);
		}
		// 设置表体合同大事记页签
		if (!ValueUtil.isEmpty(billVO.getCtPuMemoraVO())) {
			billVO.setCtPuMemoraVO(null);
		}
		// 设置表体执行情况页签
		if (!ValueUtil.isEmpty(billVO.getCtPuExecVO())) {
			// 复制操作 执行表清空 操作对象是billVO
			billVO.setCtPuExecVO(null);
		}
		// 设置付款协议页签
		if (!ValueUtil.isEmpty(billVO.getCtPaymentVO())) {
			this.setBodyPaymentValue(billVO.getCtPaymentVO(), billVO.getParentVO());
		}
	}

	/**
	 * 设置复制的表头信息 合同的预付款、累计付款总额、累计付款金额信息不复制。
	 * 
	 * @param parentVO
	 */
	private void setHeadValue(CtPuVO parentVO) {
		parentVO.setPk_origct(null);
		parentVO.setPk_ct_pu(null);
		parentVO.setVbillcode(null);
		parentVO.setCtname(null);
		parentVO.setApprover(null);
		parentVO.setTaudittime(null);
		parentVO.setBillmaker(null);
		parentVO.setDmakedate(null);
		parentVO.setCreator(null);
		parentVO.setCreationtime(null);
		parentVO.setModifiedtime(null);
		parentVO.setModifier(null);
		parentVO.setActualvalidate(null);
		parentVO.setActualinvalidate(null);
		parentVO.setFstatusflag((Integer) CtFlowEnum.Free.value());
		parentVO.setVersion(UFDouble.ONE_DBL);

		// 最新版本
		parentVO.setBlatest(UFBoolean.TRUE);
		// 港华变更修订状态和最新显示
		parentVO.setModifyStatus(EnumModify.simple.toInteger());
		parentVO.setBshowLatest(UFBoolean.TRUE);

		// 累计原币付款总额
		parentVO.setNorigpshamount(null);
		// 累计本币付款总额
		parentVO.setNtotalgpamount(null);
		// 已生成订单量作为合同执行 false为普通合同 true为从请购单过来的采购合同
		parentVO.setBordernumexec(UFBoolean.FALSE);
		// 设置版本信息
		this.setHeadVID(parentVO);
		// 清空电子商务相关字段
		parentVO.setBpublish(null);
		parentVO.setIrespstatus(null);
		parentVO.setPk_pubpsn(null);
		parentVO.setPk_resppsn(null);
		parentVO.setTpubtime(null);
		parentVO.setTresptime(null);
		parentVO.setVreason(null);
		// 来源协同合同
		parentVO.setBsrcecmct(UFBoolean.FALSE);

		UFDate currentDate = UFDate.getDate(SessionContext.getInstance().getClientInfo().getBizDateTime());
		
		// 636表头签订日期为系统日期、计划生效、计划终止清空
		parentVO.setSubscribedate(currentDate);
		parentVO.setValdate(null);
		parentVO.setInvallidate(null);

		// nccloud 设置制单日期为当前业务日期
		parentVO.setDbilldate(currentDate);

		// 清空 ts
		parentVO.setTs(null);
	}

	private void setBodyPuValue(CtPuBVO[] ctPuBVO, CtPuVO parentVO) {
		// modify by liangchen1 批量查询财务组织vid
		Set<String> pk_financeorgs = new HashSet<String>();
		for (CtPuBVO vo : ctPuBVO) {
			pk_financeorgs.add(vo.getPk_financeorg());
		}
		Map<String, String> orgOidVidMap = OrgUnitPubService
				.getNewVIDSByOrgIDS(pk_financeorgs.toArray(new String[pk_financeorgs.size()]));

		for (CtPuBVO ctBVO : ctPuBVO) {
			ctBVO.setPk_ct_pu(null);
			ctBVO.setPk_ct_pu_b(null);
			// 订单执行累计数量
			ctBVO.setNordnum(null);
			// 订单执行累计原币价税合计
			ctBVO.setNordsum(null);
			// 累计原币付款金额
			ctBVO.setNoritotalgpmny(UFDouble.ZERO_DBL);
			// 累计本币付款金额
			ctBVO.setNtotalgpmny(null);
			// 来源子子表
			ctBVO.setCsrcbbid(null);
			// 来源单据子表行ID
			ctBVO.setCsrcbid(null);
			// 来源单据主表ID
			ctBVO.setCsrcid(null);
			// //来源交易类型
			ctBVO.setVrstrantypecode(null);
			// 来源单据号
			ctBVO.setVsrccode(null);
			// 来源单据行号
			ctBVO.setVsrcrowno(null);
			// 来源单据类型
			ctBVO.setVsrctype(null);
			// 电子商务
			ctBVO.setCecbillbid(null);
			ctBVO.setCecbillid(null);
			ctBVO.setCectypecode(null);
			ctBVO.setVecbillcode(null);
			ctBVO.setVctbillcode(null);

			ctBVO.setPk_ctrelating(null);
			ctBVO.setPk_ctrelating_b(null);
			ctBVO.setPk_ct_price(null);
			ctBVO.setNschedulernum(null);
			// 设置表体版本信息
			ctBVO.setPk_org_v(parentVO.getPk_org_v());
			ctBVO.setPk_financeorg_v(orgOidVidMap.get(ctBVO.getPk_financeorg()));
			// this.setBodyVID(ctBVO);
			// 港华变更
			ctBVO.setPk_origctb(null);
			// 协同合同
			ctBVO.setVecmctbillcode(null);
			ctBVO.setPk_ecmct(null);
			ctBVO.setPk_ecmct_b(null);
			// 请购单号
			ctBVO.setVpraybillcode(null);
			// 请购单主键
			ctBVO.setPk_praybill(null);
			// 请购单行号主键
			ctBVO.setPk_praybill_b(null);
			// 请购单行号
			ctBVO.setCpraybillrowno(null);
		}
	}

	private void setBodyChangeValue(AggCtPuVO bill) {
		// 先清空执行表的数据
		bill.setCtPuChangeVO(null);
		// 再set原始版本
		CtPuChangeVO originChangeVO = new CtPuChangeVO();
		originChangeVO.setPk_group(bill.getParentVO().getPk_group());
		originChangeVO.setPk_org(bill.getParentVO().getPk_group());
		originChangeVO.setPk_org_v(bill.getParentVO().getPk_org_v());
		originChangeVO.setVmemo(
				nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("4020003_0", "04020003-0005")/*
																									 * @ res "原始版本"
																									 */);
		originChangeVO.setVchangecode(UFDouble.ONE_DBL);
		bill.setCtPuChangeVO(new CtPuChangeVO[] { originChangeVO });
	}

	private void setBodyTermValue(CtPuTermVO[] ctPuTermVO, CtPuVO parentVO) {
		for (CtPuTermVO termVO : ctPuTermVO) {
			termVO.setPk_ct_pu(null);
			termVO.setPk_ct_pu_term(null);
			termVO.setPk_org_v(parentVO.getPk_org_v());
		}
	}

	private void setBodyPaymentValue(CtPaymentVO[] paymentVOs, CtPuVO parentVO) {
		for (CtPaymentVO paymentVO : paymentVOs) {
			paymentVO.setPk_ct_pu(null);
			paymentVO.setPk_ct_pu_payment(null);
			paymentVO.setPk_org_v(parentVO.getPk_org_v());
		}
	}

	private void setHeadVID(CtPuVO parentVO) {
		String pk_org = parentVO.getPk_org();
		if (!StringUtil.isEmptyTrimSpace(pk_org)) {
			String pk_org_v = OrgUnitPubService.getOrgVid(pk_org);
			parentVO.setPk_org_v(pk_org_v);
		}

		String pk_dept = parentVO.getDepid();
		if (!StringUtil.isEmptyTrimSpace(pk_dept)) {
			String pk_dept_v = this.getDeptVID(pk_dept);
			parentVO.setDepid_v(pk_dept_v);
		}
	}

	private String getDeptVID(String deptid) {
		Map<String, String> deptmap = DeptPubService.getLastVIDSByDeptIDS(new String[] { deptid });
		return deptmap.get(deptid);
	}

}
