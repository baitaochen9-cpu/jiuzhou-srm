package nccloud.web.ct.saledaily.action;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import nc.itf.ct.saledaily.ISaledailyMaintainApp;
import nc.pubitf.ct.business.IBusinessTypeService;
import nc.pubitf.org.cache.IDeptPubService_C;
import nc.pubitf.org.cache.IOrgUnitPubService_C;
import nc.vo.ct.business.entity.BusinessSetVO;
import nc.vo.ct.enumeration.CtFlowEnum;
import nc.vo.ct.saledaily.entity.AggCtSaleVO;
import nc.vo.ct.saledaily.entity.CtSaleBVO;
import nc.vo.ct.saledaily.entity.CtSaleChangeVO;
import nc.vo.ct.saledaily.entity.CtSalePayTermVO;
import nc.vo.ct.saledaily.entity.CtSaleTermVO;
import nc.vo.ct.saledaily.entity.CtSaleVO;
import nc.vo.ct.uitl.ArrayUtil;
import nc.vo.ct.uitl.ValueUtil;
import nc.vo.org.DeptVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nccloud.dto.ct.saledaily.entity.SaleDailyQueryInfo;
import nccloud.dto.ct.saledaily.utils.ExchangeRateUtil;
import nccloud.framework.core.exception.ExceptionUtils;
import nccloud.framework.core.json.IJson;
import nccloud.framework.service.ServiceLocator;
import nccloud.framework.web.action.itf.ICommonAction;
import nccloud.framework.web.container.IRequest;
import nccloud.framework.web.container.SessionContext;
import nccloud.framework.web.json.JsonFactory;
import nccloud.framework.web.ui.pattern.extbillcard.ExtBillCard;
import nccloud.framework.web.ui.pattern.extbillcard.ExtBillCardOperator;
import nccloud.web.ct.saledaily.utils.SaleDailyPrecisionUtil;

import nccloud.commons.lang.StringUtils;

/**
 * @description 销售合同复制
 * @author wangshrc
 * @date 2019年2月27日 上午10:18:44
 * @version ncc1.0
 */
public class SaleDailyCardCopyAction implements ICommonAction {

	@Override
	public Object doAction(IRequest request) {
		String str = request.read();
		IJson json = JsonFactory.create();
		SaleDailyQueryInfo info = json.fromJson(str, SaleDailyQueryInfo.class);
		ExtBillCardOperator operator = new ExtBillCardOperator("400600200_card");
		AggCtSaleVO[] aggvos = null;
		ISaledailyMaintainApp service = ServiceLocator
				.find(ISaledailyMaintainApp.class);
		try {
			aggvos = service.queryMZ3App(new String[] { info.getPk() });
			if (aggvos.length == 0) {				
				return null;
			}
			AggCtSaleVO aggvo = aggvos[0];
			this.processVOAfterCopy(aggvo);
			String ctrantypeid = aggvo.getParentVO().getCtrantypeid();
			IBusinessTypeService iBusiness = ServiceLocator
					.find(IBusinessTypeService.class);
			aggvo.getParentVO().setNinvctlstyle(
					iBusiness.queryMaterial(ctrantypeid));
			ExchangeRateUtil.changeSellExchangeRate(aggvo);
			operator.setTransFlag(true);
			ExtBillCard card = operator.toCard(aggvo);
			SaleDailyPrecisionUtil.dealPrecision(card);
			Map<Object, Object> map = new HashMap<Object, Object>();
			map.put("extCard", card);
			boolean showPayTerm = false;
			BusinessSetVO businessvo = iBusiness.queryBusinessVO(ctrantypeid);
			if (businessvo == null
					|| businessvo.getBshowpayterm() == null
					|| UFBoolean.TRUE.booleanValue() != businessvo
							.getBshowpayterm().booleanValue()) {				
				showPayTerm = false;
			}else {				
				showPayTerm = true;
			}
			map.put("showPayTerm", showPayTerm);
			return map;
		} catch (BusinessException e) {
			ExceptionUtils.wrapException(e);
		}
		return null;
	}

	public void processVOAfterCopy(AggCtSaleVO billVO) {
		// 设置表头
		if (!ValueUtil.isEmpty(billVO.getParentVO())) {
			this.setHeadValue(billVO.getParentVO());
		}
		// 设置表体合同基本页签
		if (!ValueUtil.isEmpty(billVO.getCtSaleBVO())) {
			this.setBodySaleValue(billVO.getCtSaleBVO(), billVO.getParentVO());
		}
		// 设置表体变更历史页签
		if (!ValueUtil.isEmpty(billVO.getCtSaleChangeVO())) {
			this.setBodyChangeValue(billVO);
		}
		// 设置表体合同条款页签
		if (!ValueUtil.isEmpty(billVO.getCtSaleTermVO())) {
			this.setBodyTermValue(billVO.getCtSaleTermVO(),
					billVO.getParentVO());
		}
		// 设置表体合同费用页签
		if (!ValueUtil.isEmpty(billVO.getCtSaleExpVO())) {
			billVO.setCtSaleExpVO(null);
		}
		// 设置表体合同大事记页签
		if (!ValueUtil.isEmpty(billVO.getCtSaleMemoraVO())) {
			billVO.setCtSaleMemoraVO(null);
		}
		// 设置表体执行情况页签
		if (!ValueUtil.isEmpty(billVO.getCtSaleExecVO())) {
			// 复制操作 执行表清空 操作对象是billVO
			billVO.setCtSaleExecVO(null);
		}
		// 设置表体收款协议.
		if (!ValueUtil.isEmpty(billVO.getCtSalePayTermVO())) {
			this.setBodyPayTermValue(billVO);
		}

	}

	private String getDeptVID(String deptid) {
		Map<String, String> deptmap = this
				.getLastVIDSByDeptIDS(new String[] { deptid });
		return deptmap.get(deptid);
	}

	public Map<String, String> getLastVIDSByDeptIDS(String[] pk_depts) {
		DeptVO[] vos = this.queryDeptVOsByPKS(pk_depts);
		Map<String, String> retMap = new HashMap<String, String>();
		if (null == vos) {
			return retMap;
		}
		for (DeptVO deptVO : vos) {
			if (null != deptVO) {
				retMap.put(deptVO.getPk_dept(), deptVO.getPk_vid());
			}
		}
		return retMap;
	}

	public DeptVO[] queryDeptVOsByPKS(String[] pks) {
		IDeptPubService_C service = ServiceLocator
				.find(IDeptPubService_C.class);
		try {
			return service.queryDeptVOsByPKS(pks);
		} catch (BusinessException e) {
			ExceptionUtils.wrapException(e);
		}
		return new DeptVO[0];
	}

	private void setBodyChangeValue(AggCtSaleVO bill) {
		// 先清空执行表的数据
		bill.setCtSaleChangeVO(null);
		// 再set原始版本
		CtSaleChangeVO originChangeVO = new CtSaleChangeVO();
		originChangeVO.setPk_group(bill.getParentVO().getPk_group());
		originChangeVO.setPk_org(bill.getParentVO().getPk_group());
		originChangeVO.setPk_org_v(bill.getParentVO().getPk_org_v());
		originChangeVO.setVmemo(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
				.getStrByID("4020003_0", "04020003-0005")/* @res "原始版本" */);
		originChangeVO.setVchangecode(UFDouble.ONE_DBL);
		originChangeVO.setStatus(VOStatus.NEW);
		bill.setCtSaleChangeVO(new CtSaleChangeVO[] { originChangeVO });
	}

	private void setBodyPayTermValue(AggCtSaleVO billVO) {
		CtSalePayTermVO[] ctSalePayTermVOs = billVO.getCtSalePayTermVO();
		if (ArrayUtil.isEmpty(ctSalePayTermVOs)) {
			return;
		}
		for (CtSalePayTermVO vo : ctSalePayTermVOs) {
			vo.setPk_ct_sale_payterm(null);
			vo.setPk_ct_sale(null);
			vo.setNrealrecymny(null);
			vo.setDrealeffectdate(null);
			vo.setDrealenddate(null);
			vo.setNctrecvmny(null);
			vo.setDplaneffectdate(null);
			vo.setDplanenddate(null);
			vo.setNglobalpanrecymny(null);
			vo.setNgroupplanrecvmny(null);
			vo.setNlocalplanmny(null);
			vo.setNplanrecmny(null);
			vo.setPk_org_v(billVO.getParentVO().getPk_org_v());
			vo.setStatus(VOStatus.NEW);
		}

	}

	public Map<String, String> getNewVIDSByOrgIDS(String[] pk_orgs) {
		Map<String, String> omap;
		try {
			omap = ServiceLocator.find(IOrgUnitPubService_C.class)
					.getNewVIDSByOrgIDS(pk_orgs);
			return omap;
		} catch (BusinessException e) {
			// 日志异常
			ExceptionUtils.wrapException(e);

		}
		return null;

	}

	/**
	 * 方法功能描述：
	 * <p>
	 * <b>参数说明</b>
	 * 
	 * @param ctSaleBVO
	 *            <p>
	 * @since 6.0
	 * @author liuchx
	 * @time 2010-6-10 上午11:20:56
	 */
	private void setBodySaleValue(CtSaleBVO[] ctSaleBVO, CtSaleVO parentVO) {
		// modify by liangchen1 批量查询财务组织vid
		Set<String> pk_financeorgs = new HashSet<String>();
		for (CtSaleBVO vo : ctSaleBVO) {
			pk_financeorgs.add(vo.getPk_financeorg());
		}
		Map<String, String> orgOidVidMap = this
				.getNewVIDSByOrgIDS(pk_financeorgs
						.toArray(new String[pk_financeorgs.size()]));
		for (CtSaleBVO ctBVO : ctSaleBVO) {
			ctBVO.setPk_ct_sale(null);
			ctBVO.setPk_ct_sale_b(null);
			// 订单执行累计数量
			ctBVO.setNordnum(UFDouble.ZERO_DBL);
			// 订单执行累计原币价税合计
			ctBVO.setNordsum(UFDouble.ZERO_DBL);
			// 累计本币收款金额
			ctBVO.setNtotalgpmny(UFDouble.ZERO_DBL);
			// 累计原币收款金额
			ctBVO.setNoritotalgpmny(UFDouble.ZERO_DBL);
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
			ctBVO.setPk_origctb(null);

			// 协同合同
			ctBVO.setVecmctbillcode(null);
			ctBVO.setPk_ecmct(null);
			ctBVO.setPk_ecmct_b(null);
			ctBVO.setPk_org_v(parentVO.getPk_org_v());
			ctBVO.setPk_financeorg_v(orgOidVidMap.get(ctBVO.getPk_financeorg()));
			ctBVO.setStatus(VOStatus.NEW);
		}

	}

	private void setBodyTermValue(CtSaleTermVO[] ctSaleTermVO, CtSaleVO parentVO) {
		for (CtSaleTermVO termVO : ctSaleTermVO) {
			termVO.setPk_ct_sale(null);
			termVO.setPk_ct_sale_term(null);
			termVO.setPk_org_v(parentVO.getPk_org_v());
			termVO.setStatus(VOStatus.NEW);
		}
	}

	/**
	 * 方法功能描述：设置复制的表头信息 合同的预付款、累计付款总额、累计付款金额信息不复制。
	 * <p>
	 * <b>参数说明</b>
	 * 
	 * @param parentVO
	 * @param context
	 *            <p>
	 * @since 6.0
	 * @author liuchx
	 * @time 2010-6-10 上午09:58:44
	 */
	private void setHeadValue(CtSaleVO parentVO) {
		parentVO.setPk_ct_sale(null);
		parentVO.setPk_origct(null);
		parentVO.setVbillcode(null);
		parentVO.setCtname(null);
		parentVO.setCtname2(null);
		parentVO.setCtname3(null);
		parentVO.setCtname4(null);
		parentVO.setCtname5(null);
		parentVO.setCtname6(null);
		parentVO.setApprover(null);
		// 制单人，制单时间清空
		parentVO.setBillmaker(null);
		parentVO.setDmakedate(null);
		parentVO.setCreator(null);
		parentVO.setTaudittime(null);
		parentVO.setModifiedtime(null);
		parentVO.setModifier(null);
		parentVO.setCreationtime(null);
		parentVO.setActualvalidate(null);
		parentVO.setActualinvalidate(null);
		parentVO.setFstatusflag((Integer) CtFlowEnum.Free.value());
		parentVO.setVersion(UFDouble.ONE_DBL);

		// 最新版本
		parentVO.setBlatest(UFBoolean.TRUE);
		// 原币预付款
		parentVO.setNoriprepaymny(UFDouble.ZERO_DBL);
		// 本币预付款
		parentVO.setNprepaymny(UFDouble.ZERO_DBL);
		// 累计原币收款总额
		parentVO.setNorigpshamount(null);
		// 累计本币收款总额
		parentVO.setNtotalgpamount(null);
		// 已生成订单量作为合同执行 false为普通合同 true为从请购单过来的采购合同
		parentVO.setBordernumexec(UFBoolean.FALSE);
		// 来源协同合同
		parentVO.setBsrcecmct(UFBoolean.FALSE);

		// 636表头签订日期为系统日期、计划生效、计划终止清空
		parentVO.setSubscribedate(new UFDate(SessionContext.getInstance()
				.getClientInfo().getBizDateTime()));
		parentVO.setValdate(null);
		parentVO.setInvallidate(null);
		//设置变更状态为普通（普通=-1,修订=0）
		parentVO.setModifystatus(-1);
		// 设置版本信息
		this.setHeadVID(parentVO);

		// 清空 ts
		parentVO.setTs(null);
	}

	private void setHeadVID(CtSaleVO parentVO) {
		String pk_org = parentVO.getPk_org();
		if (StringUtils.isNotBlank(pk_org)) {
			String pk_org_v = this.getNewVIDSByOrgIDS(new String[] { pk_org })
					.get(pk_org);
			parentVO.setPk_org_v(pk_org_v);
		}

		String pk_dept = parentVO.getDepid();
		if (StringUtils.isNotBlank(pk_dept)) {
			String pk_dept_v = this.getDeptVID(pk_dept);
			parentVO.setDepid_v(pk_dept_v);
		}
	}
}
