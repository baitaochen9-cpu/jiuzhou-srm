package nccloud.pubimpl.ct.saledaily.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.framework.common.NCLocator;
import nc.impl.pubapp.pattern.data.view.ViewQuery;
import nc.itf.org.IOrgConst;
import nc.itf.scmpub.reference.uap.bd.vat.BuySellFlagEnum;
import nc.itf.scmpub.reference.uap.bd.vat.VATBDService;
import nc.itf.scmpub.reference.uap.bd.vat.VATInfoQueryVO;
import nc.itf.scmpub.reference.uap.bd.vat.VATInfoVO;
import nc.itf.scmpub.reference.uap.org.OrgUnitPubService;
import nc.itf.uap.pf.IPfExchangeService;
import nc.itf.uap.pf.busiflow.PfButtonClickContext;
import nc.vo.ct.saledaily.entity.AggCtSaleVO;
import nc.vo.ct.saledaily.entity.CtSaleBVO;
import nc.vo.ct.saledaily.entity.CtSaleVO;
import nc.vo.ct.uitl.StringUtil;
import nc.vo.ct.uitl.ValueUtil;
import nc.vo.ct.util.CTVatUtil;
import nc.vo.org.OrgVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pub.compiler.PfParameterVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pubapp.pattern.pub.MapList;
import nc.vo.scmf.pub.keyvalue.IKeyValue;
import nc.vo.scmf.pub.keyvalue.VOKeyValue;
import nc.vo.scmpub.res.billtype.CTBillType;
import nc.vo.scmpub.util.ViewToBillConvertor;
import nc.vo.so.pub.comparator.BillNOandRowNoComparator;
import nc.vo.so.salequotation.entity.AggSalequotationHVO;
import nc.vo.so.salequotation.entity.SalequoViewVO;
import nc.vo.so.salequotation.entity.SalequotationBVO;
import nc.vo.so.salequotation.entity.SalequotationHVO;
import nccloud.dto.ct.pub.transfer.TransferInfo;
import nccloud.dto.ct.saledaily.utils.CtSaleDefaultAddressUtil;
import nccloud.dto.ct.saledaily.utils.VatEditUtil;
import nccloud.pubitf.ct.saledaily.service.ISaleDailyQueryForNCCloudService;
import nccloud.util.so.TransBillUtil;

/**
 * @description 销售合同
 * @author wangshrc
 * @date 2019年3月1日 下午4:27:31
 * @version ncc1.0
 */
public class SaleDailyQueryForNCCloudServiceImpl implements
		ISaleDailyQueryForNCCloudService {

	@Override
	public AggCtSaleVO setAddLineInfo(AggCtSaleVO vo) throws Exception {
		this.setDefaultValue(vo);
		this.setVatDefaultValue(vo);
		return vo;
	}

	public void setDefaultValue(AggCtSaleVO vo) {
		CtSaleVO hvo = vo.getParentVO();
		
		for(int i = 0;i<vo.getCtSaleBVO().length;i++) {
			CtSaleBVO bvo = vo.getCtSaleBVO()[i];
			// 组织
			String pk_org = hvo.getPk_org();
			String pk_org_v = hvo.getPk_org_v();
			// 集团
			String pk_group = hvo.getPk_group();

			if (!ValueUtil.isEmpty(pk_org)) {
				bvo.setPk_org(pk_org);
			}
			if (!ValueUtil.isEmpty(pk_org_v)) {
				bvo.setPk_org_v(pk_org_v);
			}
			if (!ValueUtil.isEmpty(pk_group)) {
				bvo.setPk_group(pk_group);
			}
			OrgVO orgvo = OrgUnitPubService.getOrg(pk_org);
			boolean blen = OrgUnitPubService.isTypeOf(pk_org,
					IOrgConst.FINANCEORGTYPE);
			if (blen) {
				bvo.setPk_financeorg(pk_org);
				bvo.setPk_financeorg_v(pk_org_v);
			} else {
				bvo.setPk_financeorg(orgvo.getPk_corp());
				bvo.setPk_financeorg_v(OrgUnitPubService.getOrgVid(orgvo
						.getPk_corp()));
			}
			// 设置换算率
			bvo.setVchangerate("1.0000/1.0000");
			bvo.setVqtunitrate("1.0000/1.0000");
		}
		
	}

	public void setVatDefaultValue(AggCtSaleVO vo) {
		CtSaleVO hvo = vo.getParentVO();
		CtSaleBVO bvo = vo.getCtSaleBVO()[0];
		String pk_org = hvo.getPk_org();
		Integer ninvctlstyle = (Integer) new VatEditUtil().getNinvctlstyle(vo);
		String sendcountry = CTVatUtil.getSaleSendcountry(pk_org);
		bvo.setCsendcountryid(sendcountry);
		String rececountry = CTVatUtil.getSaleRececountry(hvo.getPk_customer());
		bvo.setCrececountryid(rececountry);
		String taxcountry = CTVatUtil.getTaxCountry(bvo.getPk_financeorg());
		bvo.setCtaxcountryid(taxcountry);

		// util.getEditor().getBillModel()
		// .loadLoadRelationItemValue(lineNum, CTVatNameConst.CSENDCOUNTRYID);
		// util.getEditor().getBillModel()
		// .loadLoadRelationItemValue(lineNum, CTVatNameConst.CRECECOUNTRYID);
		// util.getEditor().getBillModel()
		// .loadLoadRelationItemValue(lineNum, CTVatNameConst.CTAXCOUNTRYID);

		BuySellFlagEnum buysell = CTVatUtil.getSaleBuySellFlag(rececountry,
				taxcountry);
		if (null != buysell) {
			bvo.setFbuysellflag(buysell.value());
		}
		Boolean triatrade = CTVatUtil.getSaleTriatradeFlag(sendcountry,
				taxcountry, buysell);
		bvo.setBtriatradeflag(UFBoolean.valueOf(triatrade));
		String pk_customer = hvo.getPk_customer();
		if (null != ninvctlstyle && !StringUtil.isEmptyTrimSpace(pk_customer)
				&& !StringUtil.isEmptyTrimSpace(taxcountry)
				&& !StringUtil.isEmptyTrimSpace(sendcountry)
				&& !StringUtil.isEmptyTrimSpace(rececountry)) {
			UFDate date = hvo.getSubscribedate();
			VATInfoQueryVO query = new VATInfoQueryVO(taxcountry, buysell,
					UFBoolean.valueOf(triatrade.booleanValue()), sendcountry,
					rececountry, pk_customer, null, date);
			VATInfoVO[] info = VATBDService
					.queryCustVATInfo(new VATInfoQueryVO[] { query });

			if (null != info && info.length > 0 && null != info[0]) {
				bvo.setCtaxcodeid(info[0].getCtaxcodeid());
				// util.getEditor().getBillModel()
				// .loadLoadRelationItemValue(lineNum,
				// CTVatNameConst.CTAXCODEID);
				bvo.setFtaxtypeflag(info[0].getFtaxtypeflag());
				bvo.setNtaxrate(info[0].getNtaxrate());
			}
		}
		if (null != hvo.getPk_customer()) {
			// 设置默认收货地址、收货地区、收货地点
			IKeyValue keyValue = new VOKeyValue<AggCtSaleVO>(vo);
			CtSaleDefaultAddressUtil.setLineDefaultAddress(keyValue, 0);
		}
	}

	@Override
	public AggCtSaleVO[] transToSaleDaily(TransferInfo[] transferInfo)
			throws Exception {
		List<AggCtSaleVO> bills = new ArrayList<AggCtSaleVO>();
		Map<String, UFDateTime> hid_ts = new HashMap<String, UFDateTime>();
		Map<String, UFDateTime> bid_ts = new HashMap<String, UFDateTime>();
		MapList<String, String> bids = new MapList<String, String>();
		// 拆分pk和ts
		this.splitIdts(transferInfo, hid_ts, bid_ts, bids);
		List<String> curbids= bids.get("4310");
		if (null == curbids || curbids.size() == 0) {
			return null;
		}
		ViewQuery<SalequoViewVO> query = new ViewQuery<SalequoViewVO>(
				SalequoViewVO.class);
		SalequoViewVO[] views = query.query(curbids
				.toArray(new String[0]));
		if (null != views && views.length > 0) {
			ViewToBillConvertor<AggSalequotationHVO, SalequoViewVO, SalequotationHVO> convertor = new ViewToBillConvertor<AggSalequotationHVO, SalequoViewVO, SalequotationHVO>(
					AggSalequotationHVO.class);

			AggSalequotationHVO[] orders = (AggSalequotationHVO[]) convertor
					.getBillsByViews(views);
			// 填充ts
			TransBillUtil.fillTs(orders, SalequotationHVO.PK_SALEQUOTATION,
					SalequotationBVO.PK_SALEQUOTATION_B, hid_ts, bid_ts);

			AggCtSaleVO[] destbills = (AggCtSaleVO[]) execVOChange(
					"4310",
					CTBillType.SaleDaily.getCode(), orders, null,
					PfButtonClickContext.ClassifyByBusiflow);
			bills.addAll(Arrays.asList(destbills));
		}
		for (AggCtSaleVO bill : bills) {
			// 根据来源单据号和行号排序，保证与上游单据的行顺序
			BillNOandRowNoComparator c = new BillNOandRowNoComparator();
			Arrays.sort(bill.getCtSaleBVO(), c);
			// 設置vo狀態
			bill.getParentVO().setStatus(VOStatus.NEW);
			CtSaleBVO[] bodys = bill.getCtSaleBVO();
			for (CtSaleBVO bvo : bodys) {
				bvo.setStatus(VOStatus.NEW);
			}
		}
		return bills.toArray(new AggCtSaleVO[0]);
	}
	private void splitIdts(TransferInfo[] transinfo,
			Map<String, UFDateTime> hid_ts, Map<String, UFDateTime> bid_ts,
			MapList<String, String> bids) {
		for (TransferInfo info : transinfo) {
			for (String hidts : info.getHidts()) {
				String[] hpkts = hidts.split(",");
				hid_ts.put(hpkts[0], new UFDateTime(hpkts[1]));

			}
			for (String idts : info.getBidts()) {
				String[] pkts = idts.split(",");
				bid_ts.put(pkts[0], new UFDateTime(pkts[1]));
				bids.put(info.getCbilltype(), pkts[0]);
			}
		}

	}
	private AggregatedValueObject[] execVOChange(String srcBillType,
			String destBillType, AggregatedValueObject[] bills,
			PfParameterVO srcParaVo, int classifyCode) throws BusinessException {
		IPfExchangeService exchangeService = NCLocator.getInstance().lookup(
				IPfExchangeService.class);
		AggregatedValueObject[] destVos = null;
		destVos = exchangeService.runChangeDataAryNeedClassify(srcBillType,
				destBillType, bills, srcParaVo, classifyCode);
		return destVos;
	}
}
