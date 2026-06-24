package nccloud.pubimpl.ct.purdaily.event.after.head;

import java.util.HashMap;
import java.util.Map;

import nc.bs.framework.common.NCLocator;
import nc.itf.org.IOrgConst;
import nc.itf.scmpub.reference.uap.org.DeptPubService;
import nc.itf.scmpub.reference.uap.org.OrgUnitPubService;
import nc.itf.scmpub.reference.uap.org.StockOrgPubService;
import nc.itf.scmpub.reference.uap.rbac.UserManageQuery;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.pubitf.ct.business.IBusinessTypeService;
import nc.pubitf.setting.defaultdata.UAPOrgSettingAccessor;
import nc.vo.ct.business.entity.BusinessSetVO;
import nc.vo.ct.entity.CtAbstractBVO;
import nc.vo.ct.entity.CtAbstractVO;
import nc.vo.ct.enumeration.CtFlowEnum;
import nc.vo.ct.pub.CTVatNameConst;
import nc.vo.ct.purdaily.entity.AggCtPuVO;
import nc.vo.ct.purdaily.entity.CtPuBVO;
import nc.vo.ct.purdaily.entity.CtPuExpVO;
import nc.vo.ct.purdaily.entity.CtPuMemoraVO;
import nc.vo.ct.purdaily.entity.CtPuTermVO;
import nc.vo.ct.purdaily.entity.CtPuVO;
import nc.vo.ct.uitl.StringUtil;
import nc.vo.ct.uitl.ValueUtil;
import nc.vo.ct.util.CTVatUtil;
import nc.vo.org.OrgVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.AppContext;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.pubapp.pattern.pub.SqlBuilder;
import nc.vo.scmpub.res.billtype.CTBillType;
import nccloud.dto.ct.pub.utils.AddLineUtil;
import nccloud.dto.ct.pub.utils.ExchangeRateUtil;
import nccloud.dto.ct.pub.utils.ExtBillUtil;
import nccloud.dto.scmpub.pub.context.BillCardHeadEditEvent;
import nccloud.dto.scmpub.pub.event.rule.IHeadAfterRule;

/**
 * @description 采购合同主组织编辑后
 * @author xiahui
 * @date 创建时间：2019-1-24 上午11:01:48
 * @version ncc1.0
 * @ref nc.ui.ct.editor.org.OrgChangedEventHandler
 **/
public class OrgChangeAfterRule implements IHeadAfterRule<AggCtPuVO> {

	@Override
	public AggCtPuVO afterEdit(AggCtPuVO billvo, BillCardHeadEditEvent event,
			@SuppressWarnings("rawtypes") Map userobject) {
		CtPuVO headerVO = new CtPuVO();
		headerVO.setPk_org(billvo.getParentVO().getPk_org());
		headerVO.setPk_org_v(billvo.getParentVO().getPk_org_v());
		billvo.setParentVO(headerVO);
		
		ExtBillUtil util = new ExtBillUtil(billvo);
		this.addBodysLine(billvo);
		this.setBaseView(util, event);
		this.setLinkView(util);

		return billvo;
	}

	/**
	 * 对应表体新增一行
	 * 
	 * @param billvo
	 */
	private void addBodysLine(AggCtPuVO billvo) {
		billvo.setCtPuBVO(new CtPuBVO[] { new CtPuBVO() });
		billvo.setCtPuExpVO(new CtPuExpVO[] { new CtPuExpVO() });
		billvo.setCtPuTermVO(new CtPuTermVO[] { new CtPuTermVO() });
		billvo.setCtPuMemoraVO(new CtPuMemoraVO[] { new CtPuMemoraVO() });
	}

	/**
	 * 设置联动信息
	 * 
	 * @param billvo
	 */
	private void setLinkView(ExtBillUtil util) {
		String pk_org = util.getHeadTailStringValue(CtPuVO.PK_ORG);
		if (!ValueUtil.isEmpty(pk_org)) {
			String pk_currtype = OrgUnitPubService.queryOrgCurrByPk(pk_org); // 组织本位币
			util.setHeadValue(CtPuVO.CORIGCURRENCYID, pk_currtype); // 设置原币
			util.setHeadValue(CtPuVO.CCURRENCYID, pk_currtype); // 设置本币
			util.setHeadValue(CtPuVO.NEXCHANGERATE, UFDouble.ONE_DBL);

			ExchangeRateUtil.setDefaultGroupGlobalRate(util);
		}

	}

	/**
	 * 设置基本信息
	 * 
	 * @param event
	 * 
	 * @param billvo
	 */
	private void setBaseView(ExtBillUtil util, BillCardHeadEditEvent event) {

		String pk_org_v = util.getHeadTailStringValue(CtPuVO.PK_ORG_V);
		if (pk_org_v == null) {
			pk_org_v = event.getNewValue().toString();
		}
		String pk_org = OrgUnitPubService.getOrgIDByVID(pk_org_v);

		// 设置表体默认值
		AddLineUtil.setVatDefaultValue(util, new int[] { 0 }, pk_org);

		OrgVO orgVO = OrgUnitPubService.getOrg(pk_org);

		util.setHeadValue(CtPuVO.PK_ORG, pk_org);
		util.setHeadValue(CtPuVO.PK_ORG_V, pk_org_v);
		util.setHeadValue(CtPuVO.PK_PURCORP, orgVO.getPk_corp()); // 设置公司
		util.setHeadValue(CtAbstractVO.CBILLTYPECODE, CTBillType.PurDaily.getCode()); // 设置单据类型

		util.setBodyValue(0, CtAbstractBVO.PK_ORG, pk_org, CtPuBVO.class);
		util.setBodyValue(0, CtAbstractBVO.PK_ORG, pk_org, CtPuExpVO.class);
		util.setBodyValue(0, CtAbstractBVO.PK_ORG, pk_org, CtPuTermVO.class);
		util.setBodyValue(0, CtAbstractBVO.PK_ORG, pk_org, CtPuMemoraVO.class);
		util.setBodyValue(0, CtAbstractBVO.PK_ORG_V, pk_org_v, CtPuBVO.class);
		util.setBodyValue(0, CtAbstractBVO.PK_ORG_V, pk_org_v, CtPuExpVO.class);
		util.setBodyValue(0, CtAbstractBVO.PK_ORG_V, pk_org_v, CtPuTermVO.class);
		util.setBodyValue(0, CtAbstractBVO.PK_ORG_V, pk_org_v, CtPuMemoraVO.class);

		String pk_group = orgVO.getPk_group();
		if (!ValueUtil.isEmpty(pk_group)) {
			util.setHeadValue(CtAbstractVO.PK_GROUP, pk_group);
			util.setBodyValue(0, CtAbstractBVO.PK_GROUP, pk_group, CtPuBVO.class);
			util.setBodyValue(0, CtAbstractBVO.PK_GROUP, pk_group, CtPuExpVO.class);
			util.setBodyValue(0, CtAbstractBVO.PK_GROUP, pk_group, CtPuTermVO.class);
			util.setBodyValue(0, CtAbstractBVO.PK_GROUP, pk_group, CtPuMemoraVO.class);
		}

		// 如果是交易类型发布节点，则组织改编后就需要根据交易类型设置“总括订单”标识
		this.setBbracketorder(util);
		// 设置收货库存组织和收货国家
		this.setArrvstockAndCountry(util);

		UFDate busiDate = AppContext.getInstance().getBusiDate();
		util.setHeadValue(CtPuVO.VERSION, UFDouble.ONE_DBL);
		util.setHeadValue(CtPuVO.IPRINTCOUNT, 0);
		util.setHeadValue(CtPuVO.DBILLDATE, busiDate);
		util.setHeadValue(CtPuVO.SUBSCRIBEDATE, busiDate);
		util.setHeadValue(CtPuVO.FSTATUSFLAG, CtFlowEnum.Free.toIntValue());

		util.setBodyValue(0, CtPuBVO.CROWNO, "10");
		// util.setBodyValue(0, CtPuBVO., UFDouble.ONE_DBL);
		util.setBodyValue(0, CtPuBVO.PK_FINANCEORG, pk_org);
		util.setBodyValue(0, CtPuBVO.PK_FINANCEORG_V, pk_org_v);
		String taxcountry = CTVatUtil.getTaxCountry(orgVO.getPk_corp());
		util.setBodyValue(0, CTVatNameConst.CTAXCOUNTRYID, taxcountry);
		// 设置人员和部门
		this.setPsnAndDept(util);

		util.setHeadValue(CtPuVO.MODIFYSTATUS, -1); // 变更状态设置为‘普通’
		util.setHeadValue(CtPuVO.BSHOWLATEST, UFBoolean.TRUE); // 是否显示最新版设置为'true'
	}

	private void setPsnAndDept(ExtBillUtil util) {
		String psn = util.getHeadTailStringValue(CtAbstractVO.PERSONNELID);
		if (psn == null) {
			psn = UserManageQuery.queryPsndocByUserid(AppContext.getInstance().getPkUser());
			util.setHeadValue(CtAbstractVO.PERSONNELID, psn);
		}
		if (StringUtil.isEmptyTrimSpace(psn)) {
			return;
		}
//		Map<String, String> results = null;
		Map<String, String> results = new HashMap<String, String>();
//		try {
		String pk_org = util.getHeadTailStringValue(CtAbstractVO.PK_ORG);
		results.put("pk_dept", this.getDept(pk_org, psn));
//			results = NCLocator.getInstance().lookup(IPsndocPubService.class).queryMainDeptByPandocIDs(psn);
//		} catch (BusinessException e) {
//			ExceptionUtils.wrappException(e);
//		}
		Map<String, String> dept = new HashMap<String, String>();
		if (results != null && results.get("pk_dept") != null) {
			dept.put(psn, results.get("pk_dept").toString());
		}
		if (null != dept && null != dept.get(psn)) {
			util.setHeadValue(CtAbstractVO.DEPID, dept.get(psn));
			Map<String, String> retMap = DeptPubService.getLastVIDSByDeptIDS(new String[] { dept.get(psn) });
			String dept_v = retMap.get(dept.get(psn));
			util.setHeadValue(CtAbstractVO.DEPID_V, dept_v);
		}
	}

	private void setArrvstockAndCountry(ExtBillUtil util) {
		String oldstock = util.getBodyStringValue(0, CtPuBVO.PK_ARRVSTOCK);
		if (!StringUtil.isEmptyTrimSpace(oldstock)) {
			return;
		}
		// 设置收货库存组织及收货国家
		String pk_arrvstock = null;
		try {
			pk_arrvstock = UAPOrgSettingAccessor.getDefaultOrgUnit();
		} catch (Exception e) {
			ExceptionUtils.wrappException(e);
		}
		if (!ValueUtil.isEmpty(pk_arrvstock) && OrgUnitPubService.isTypeOf(pk_arrvstock, IOrgConst.STOCKORGTYPE)) {
			String pk_arrvstock_v = OrgUnitPubService.getOrgVid(pk_arrvstock);
			util.setBodyValue(0, CtPuBVO.PK_ARRVSTOCK, pk_arrvstock, CtPuBVO.class);
			util.setBodyValue(0, CtPuBVO.PK_ARRVSTOCK_V, pk_arrvstock_v, CtPuBVO.class);
			Map<String, String> countryIDs = StockOrgPubService.queryCountryByStockOrg(new String[] { pk_arrvstock });
			util.setBodyValue(0, CTVatNameConst.CRECECOUNTRYID, countryIDs.get(pk_arrvstock), CtPuBVO.class);
		}
	}

	private void setBbracketorder(ExtBillUtil util) {
		String ctrantypeid = util.getHeadTailStringValue(CtAbstractVO.CTRANTYPEID);
		if (StringUtil.isEmptyTrimSpace(ctrantypeid)) {
			return;
		}
		IBusinessTypeService service = NCLocator.getInstance().lookup(IBusinessTypeService.class);
		try {
			BusinessSetVO businessVO = service.queryBusinessVO(ctrantypeid);
			if (businessVO.getBbracketOrder().booleanValue()) {
				util.setHeadValue(CtPuVO.BBRACKETORDER, UFBoolean.TRUE);
			}
		} catch (BusinessException e) {
			ExceptionUtils.wrappException(e);
		}
	}

	// 执行查询仓库sql
	private String getDept(String pk_org, String psn) {
		String dept = null;
		SqlBuilder sql = new SqlBuilder();
		sql.append("select pk_dept from bd_psnjob where ");
		sql.append(" pk_psndoc= '" + psn + "' and pk_org='" + pk_org + "'");
		try {
			dept = (String) NCLocator.getInstance().lookup(IUAPQueryBS.class).executeQuery(sql.toString(),
					new ColumnProcessor());
		} catch (BusinessException e) {
			// 日志异常
			ExceptionUtils.wrappException(e);

		}
		return dept;
	}
}
