package nccloud.pubimpl.ct.saledaily.event.head;

import java.util.Map;

import nc.itf.scmpub.reference.uap.bd.customer.CustomerPubService;
import nc.itf.scmpub.reference.uap.bd.vat.BuySellFlagEnum;
import nc.itf.scmpub.reference.uap.org.DeptPubService;
import nc.vo.bd.cust.saleinfo.CustsaleVO;
import nc.vo.ct.saledaily.entity.AggCtSaleVO;
import nc.vo.ct.saledaily.entity.CtSaleBVO;
import nc.vo.ct.saledaily.entity.CtSaleVO;
import nc.vo.ct.uitl.ArrayUtil;
import nc.vo.ct.uitl.ValueUtil;
import nc.vo.ct.util.CTVatUtil;
import nc.vo.scmf.pub.keyvalue.IKeyValue;
import nc.vo.scmf.pub.keyvalue.VOKeyValue;
import nccloud.dto.ct.saledaily.utils.CtSaleDefaultAddressUtil;
import nccloud.dto.ct.saledaily.utils.CustMaterialInfoUtil;
import nccloud.dto.scmpub.pub.context.BillCardHeadEditEvent;
import nccloud.dto.scmpub.pub.event.rule.IHeadAfterRule;

/**
 * @description 销售合同表头客户编辑后
 * @author wangshrc
 * @date 2019年2月13日 下午1:58:46
 * @version ncc1.0
 */
public class SaleDailyCustomerAfterRule implements IHeadAfterRule<AggCtSaleVO> {

	@SuppressWarnings("rawtypes")
	@Override
	public AggCtSaleVO afterEdit(AggCtSaleVO billvo,
			BillCardHeadEditEvent event, Map userobject) {
		CtSaleVO hvo = billvo.getParentVO();
		CustsaleVO cvendor = this.getCustomerInfo(billvo);
		if (cvendor == null) {
			// 清空客户信息
			this.clearCustomerInfo(billvo);
			billvo.setCtSalePayTermVO(null);
		} else {
			// 部门
			if (!ValueUtil.isEmpty(cvendor.getRespdept())) {
				String deptid = cvendor.getRespdept();
				hvo.setDepid(deptid);
				Map<String, String> vidMap = DeptPubService
						.getLastVIDSByDeptIDS(new String[] { deptid });
				hvo.setDepid_v(vidMap.get(deptid));
			}
			// 人员
			if (!ValueUtil.isEmpty(cvendor.getRespperson())) {
				hvo.setPersonnelid(cvendor.getRespperson());
			}
			// 交货地点
			String defaultAddress = this.getAddress(billvo);
			if (!ValueUtil.isEmpty(defaultAddress)) {
				hvo.setDeliaddr(defaultAddress);
			}
			// 币种
			if (!ValueUtil.isEmpty(cvendor.getCurrencydefault())) {
				if (!cvendor.getCurrencydefault().equals(
						hvo.getCorigcurrencyid())) {
					hvo.setCorigcurrencyid(cvendor.getCurrencydefault());
					new SaleDailyCorigcurrencyAfterRule().afterEdit(billvo,
							event, userobject);
				}
			}
			// 收款协议
			if (!ValueUtil.isEmpty(cvendor.getPaytermdefault())) {
				if (!cvendor.getPaytermdefault().equals(hvo.getPk_payterm())) {
					hvo.setPk_payterm(cvendor.getPaytermdefault());
					new SaleDailyPayTermAfterRule().afterEdit(billvo, event,
							userobject);
				}
			}
		}

		IKeyValue keyValue = new VOKeyValue<AggCtSaleVO>(billvo);
		// 客户改变，收货国家也相应变动
		if (!ValueUtil.isEmpty(event.getNewValue())) {

			CtSaleDefaultAddressUtil.setBodyDefaultAddress(keyValue);
			// 国家改变才进行后续处理
			String crececountryid = CTVatUtil.getSaleRececountry(event.getNewValue().toString());
			this.changeCountry(billvo, crececountryid);
		}
		// 设置客户物料信息
		int[] rows = new int[billvo.getChildrenVO().length];
		for (int i = 0; i < rows.length; i++) {
			rows[i] = i;
		}
		new CustMaterialInfoUtil().setCustMaterialInfo(keyValue, rows);
		return billvo;
	}

	private void clearCustomerInfo(AggCtSaleVO billvo) {
		CtSaleVO hvo = billvo.getParentVO();
		hvo.setPersonnelid(null);
		hvo.setDepid(null);
		hvo.setDepid_v(null);
		hvo.setDeliaddr(null);
		hvo.setPk_payterm(null);
	}

	private CustsaleVO getCustomerInfo(AggCtSaleVO billvo) {
		String cvendorid = billvo.getParentVO().getPk_customer();
		if (cvendorid == null) {
			return null;
		}
		String pk_org = billvo.getParentVO().getPk_org();
		String[] pkArray = new String[] { cvendorid };
		String[] feilds = new String[] { CustsaleVO.RESPDEPT,// 专管部门
				CustsaleVO.RESPPERSON,// 专管业务员
				CustsaleVO.CURRENCYDEFAULT,// 默认交易币种
				CustsaleVO.PAYTERMDEFAULT,// 默认首付款协议
		};
		CustsaleVO[] custSaleVO = CustomerPubService.getCustSaleVO(pkArray,
				pk_org, feilds);

		if (ArrayUtil.isEmpty(custSaleVO)) {
			return null;
		}
		return custSaleVO != null ? custSaleVO[0] : null;
	}

	private String getAddress(AggCtSaleVO billvo) {
		CtSaleVO hvo = billvo.getParentVO();
		// 客户
		String cvendorid = hvo.getPk_customer();
		// 组织
		String pk_org = hvo.getPk_org();
		// 地址
		String ret = CustomerPubService.getDefaultAddress(cvendorid, pk_org);

		return ret;
	}

	private void changeCountry(AggCtSaleVO billvo, String crececountryid) {
		CtSaleBVO[] bvos = billvo.getCtSaleBVO();
		for (int i = 0; i < bvos.length; i++) {
			bvos[i].setCrececountryid(crececountryid);
			String taxcountry = CTVatUtil.getTaxCountry(bvos[i].getPk_financeorg());
			bvos[i].setCtaxcountryid(taxcountry);
			BuySellFlagEnum buysell = CTVatUtil.getSaleBuySellFlag(crececountryid,
					taxcountry);
			if (null != buysell) {
				bvos[i].setFbuysellflag(buysell.value());
			}
		}
	}
}
