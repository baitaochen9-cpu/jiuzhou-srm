package nccloud.pubimpl.ct.saledaily.event.head;

import java.util.HashMap;
import java.util.Map;

import nc.bs.framework.common.NCLocator;
import nc.itf.org.IOrgConst;
import nc.itf.scmpub.reference.uap.org.DeptPubService;
import nc.itf.scmpub.reference.uap.org.OrgUnitPubService;
import nc.itf.scmpub.reference.uap.rbac.UserManageQuery;
import nc.pubitf.uapbd.IPsndocPubService;
import nc.vo.ct.enumeration.CtFlowEnum;
import nc.vo.ct.saledaily.entity.AggCtSaleVO;
import nc.vo.ct.saledaily.entity.CtSaleBVO;
import nc.vo.ct.saledaily.entity.CtSaleChangeVO;
import nc.vo.ct.saledaily.entity.CtSaleExecVO;
import nc.vo.ct.saledaily.entity.CtSaleExpVO;
import nc.vo.ct.saledaily.entity.CtSaleMemoraVO;
import nc.vo.ct.saledaily.entity.CtSalePayTermVO;
import nc.vo.ct.saledaily.entity.CtSaleTermVO;
import nc.vo.ct.saledaily.entity.CtSaleVO;
import nc.vo.ct.uitl.ValueUtil;
import nc.vo.ct.util.CTVatUtil;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.org.OrgVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.AppContext;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.pubapp.pattern.pub.PubAppTool;
import nc.vo.scmf.pub.keyvalue.VOKeyValue;
import nc.vo.scmpub.res.billtype.CTBillType;
import nccloud.commons.lang.StringUtils;
import nccloud.dto.ct.saledaily.utils.ExchangeRateUtil;
import nccloud.dto.scmpub.pub.context.BillCardHeadEditEvent;
import nccloud.dto.scmpub.pub.event.rule.IHeadAfterRule;

/**
 * @description 销售合同组织编辑后
 * @author wangshrc
 * @date 2019年3月3日 下午9:17:34
 * @version ncc1.0
 */
public class SaleDailyOrgAfterRule implements IHeadAfterRule<AggCtSaleVO> {

	@SuppressWarnings("rawtypes")
	@Override
	public AggCtSaleVO afterEdit(AggCtSaleVO billvo,
			BillCardHeadEditEvent event, Map userobject) {
		this.clearAllValues(billvo);
		String pk_org_v = (String) event.getNewValue();
		billvo.getParentVO().setPk_org_v(pk_org_v);
		String pk_org = null;
		if (!ValueUtil.isEmpty(pk_org_v)) {
			pk_org = OrgUnitPubService.getOrgIDByVID(pk_org_v);
		}
		this.setBaseView(billvo, pk_org_v);
		this.setLinkView(billvo, pk_org);
		return billvo;
	}

	private void clearAllValues(AggCtSaleVO billvo) {
		billvo.setParentVO(new CtSaleVO());
		billvo.setCtSaleBVO(new CtSaleBVO[0]);
		billvo.setCtSaleChangeVO(new CtSaleChangeVO[0]);
		billvo.setCtSaleExecVO(new CtSaleExecVO[0]);
		billvo.setCtSaleTermVO(new CtSaleTermVO[0]);
		billvo.setCtSaleExpVO(new CtSaleExpVO[0]);
		billvo.setCtSaleMemoraVO(new CtSaleMemoraVO[0]);
		billvo.setCtSalePayTermVO(new CtSalePayTermVO[0]);
	}

	@SuppressWarnings("rawtypes")
	protected void setBaseView(AggCtSaleVO billvo, String pk_org_v) {
//		IKeyValue keyValue = (IKeyValue) new VOKeyValue<AggCtSaleVO>(billvo);
		String pk_group = AppContext.getInstance().getPkGroup();
		String pk_org = null;
		boolean blen = false;
		if (!ValueUtil.isEmpty(pk_org_v)) {
			pk_org = OrgUnitPubService.getOrgIDByVID(pk_org_v);
		}
//		if (!PubAppTool.isNull(pk_org_v)) {
//			keyValue.setHeadValue(CtSaleVO.PK_ORG, pk_org);
//		}else{
//			keyValue.setHeadValue(CtSaleVO.PK_ORG, null);
//			pk_org = null;
//		}
		OrgVO vo = OrgUnitPubService.getOrg(pk_org);
		CtSaleVO hvo = billvo.getParentVO();
		CtSaleBVO[] bvos = billvo.getCtSaleBVO();
		if (!PubAppTool.isNull(pk_org_v)) {
			hvo.setPk_org(pk_org);
		}
		// 设置单据类型
		hvo.setCbilltypecode(CTBillType.SaleDaily.getCode());
		// 1）如果当前主组织为财务组织，则自动带入当前主组织；
		// 2）如果当前主组织不是财务组织，则自动带入当前主组织所属公司。
		blen = OrgUnitPubService.isTypeOf(pk_org, IOrgConst.FINANCEORGTYPE);
		if (blen) {
			String taxcountry = CTVatUtil.getTaxCountry(pk_org);
			for (int i = 0; i < bvos.length; i++) {
				bvos[i].setPk_financeorg(pk_org);
				bvos[i].setPk_financeorg_v(pk_org_v);
				bvos[i].setCtaxcountryid(taxcountry);
			}
			// model.setPk_financeorg(pk_org);
			// model.setPk_financeorg_v(pk_org_v);
		} else {
			String pk_corp = vo.getPk_corp();
			String pk_corp_v = OrgUnitPubService.getOrgVid(pk_corp);
			String taxcountry = CTVatUtil.getTaxCountry(pk_corp);
			for (int i = 0; i < bvos.length; i++) {
				bvos[i].setPk_financeorg(pk_corp);
				bvos[i].setPk_financeorg_v(pk_corp_v);
				bvos[i].setCtaxcountryid(taxcountry);
			}
			// model.setPk_financeorg(pk_corp);
			// model.setPk_financeorg_v(pk_corp_v);
		}
		UFDate busiDate = AppContext.getInstance().getBusiDate();
		// 设置版本信息
		hvo.setVersion(UFDouble.ONE_DBL);
		// 设置打印次数为0
		hvo.setIprintcount(Integer.valueOf(0));
		// 设置制单时间
		hvo.setDbilldate(busiDate);
		hvo.setPk_org(pk_org);
		// 这里应该使用客户端时间，单据模板会转换为标准时间
		// 如果不通过单据模板，直接向VO中设置值，则需要使用标准时间
		hvo.setSubscribedate(busiDate);
		hvo.setFstatusflag(CtFlowEnum.Free.toIntValue());
		if (!ValueUtil.isEmpty(pk_group)) {
			hvo.setPk_group(pk_group);
		}
		// 设置人员和部门
		if (hvo.getPersonnelid() == null) {
			String psnid = UserManageQuery.queryPsndocByUserid(AppContext
					.getInstance().getPkUser());
			hvo.setPersonnelid(psnid);
		}

		String psn = hvo.getPersonnelid();
		if (StringUtil.isEmptyWithTrim(psn)) {
			return;
		}
		if (StringUtils.isBlank(pk_org)) {
			return;
		}
		Map results = null;
		try {
			results = NCLocator.getInstance().lookup(IPsndocPubService.class)
					.queryMainDeptByPandocIDs(psn);
		} catch (BusinessException e) {
			ExceptionUtils.wrappException(e);
		}
		Map<String, String> dept = new HashMap<String, String>();
		if (results != null && results.get("pk_dept") != null) {
			dept.put(psn, results.get("pk_dept").toString());
		}
		if (null != dept && null != dept.get(psn)) {
			hvo.setDepid(dept.get(psn));
			String dept_v = this.getLastdept_v(dept.get(psn));
			hvo.setDepid_v(dept_v);
		}

	}

	private String getLastdept_v(String dept) {
		Map<String, String> retMap = DeptPubService
				.getLastVIDSByDeptIDS(new String[] { dept });
		return retMap.get(dept);
	}

	protected void setLinkView(AggCtSaleVO billvo, String pk_org) {
		CtSaleVO hvo = billvo.getParentVO();
		if (!ValueUtil.isEmpty(pk_org)) {
			String pk_currtype = null;
			// 组织本位币
			pk_currtype = OrgUnitPubService.queryOrgCurrByPk(pk_org);

			// 设置原币
			hvo.setCorigcurrencyid(pk_currtype);
			// 设置本币
			hvo.setCcurrencyid(pk_currtype);
			// 设置折本汇率
			hvo.setNexchangerate(UFDouble.ONE_DBL);
			ExchangeRateUtil
					.setDefaultGroupGlobalRate(new VOKeyValue<AggCtSaleVO>(
							billvo));
		}
	}
}
