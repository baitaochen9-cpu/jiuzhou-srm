package nccloud.dto.ct.saledaily.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import nc.bs.arap.util.BillTermUtils;
import nc.itf.scmpub.reference.uap.bd.payterm.PaytermService;
import nc.itf.scmpub.reference.uap.org.OrgUnitPubService;
import nc.vo.arap.termitem.ArapTermDateVO;
import nc.vo.bd.income.IncomeChVO;
import nc.vo.bd.income.IncomeVO;
import nc.vo.ct.business.entity.BusinessSetVO;
import nc.vo.ct.entity.CtAbstractPayTermVO;
import nc.vo.ct.saledaily.entity.AggCtSaleVO;
import nc.vo.ct.saledaily.entity.CtSalePayTermVO;
import nc.vo.ct.saledaily.entity.CtSaleVO;
import nc.vo.ct.uitl.ValueUtil;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.scmpub.util.CollectionUtils;

/**
 * @description 收款协议工具
 * @author wangshrc
 * @date 2019年2月14日 上午11:01:17
 * @version ncc1.0
 */
public class SalePayTermUtil {
	public static void setValue(BusinessSetVO businessvo, AggCtSaleVO billvo) {
		CtSaleVO hvo = billvo.getParentVO();
		if (businessvo != null
				&& businessvo.getBshowpayterm() != null
				&& UFBoolean.TRUE.booleanValue() == businessvo
						.getBshowpayterm().booleanValue()) {
			billvo.getCtSalePayTermVO();
			List<CtSalePayTermVO> tvos = new ArrayList<CtSalePayTermVO>();
			IncomeChVO[] incomeChVOs = SalePayTermUtil.getIncomeChVO(hvo
					.getPk_payterm());
			if (incomeChVOs != null) {
				String pk_org = hvo.getPk_org();
				String pk_group = hvo.getPk_group();
				String pk_org_v = null;
				if (!ValueUtil.isEmpty(pk_org)) {
					pk_org_v = OrgUnitPubService.getOrgVid(pk_org);
				}

				for (IncomeChVO incomeChVO : incomeChVOs) {
					CtSalePayTermVO tvo = new CtSalePayTermVO();
					String[] attrName = incomeChVO.getAttributeNames();
					for (int b = 0; b < attrName.length; b++) {
						if (SalePayTermUtil.check(attrName[b])) {
							Object obj = incomeChVO
									.getAttributeValue(attrName[b]);
							tvo.setAttributeValue(attrName[b], obj);
						}
					}
					tvo.setPk_org(pk_org);
					tvo.setPk_org_v(pk_org_v);
					tvo.setPk_group(pk_group);
					tvos.add(tvo);
				}
				
				billvo.setCtSalePayTermVO(CollectionUtils.listToArray(tvos));
				// util.setbodyItemsEnabled(CtSaleTableCode.CTSALEPAYTERM,
				// true);
				// util.getEditor().getBillModel(CtSaleTableCode.CTSALEPAYTERM)
				// .loadLoadRelationItemValue();
			}else {
				billvo.setCtSalePayTermVO(CollectionUtils.listToArray(tvos));
			}
		}
	}

	private static IncomeChVO[] getIncomeChVO(String pk_income) {
		if (pk_income != null) {
			Map<String, IncomeVO> map = PaytermService
					.queryIncomeByPk(new String[] { pk_income });
			IncomeChVO[] incomeChVOs = map.get(pk_income).getPaymentch();
			return incomeChVOs;
		}
		return null;
	}

	private static boolean check(String name) {
		String items[] = new String[] { CtAbstractPayTermVO.SHOWORDER,
				CtAbstractPayTermVO.ACCRATE, CtAbstractPayTermVO.PREPAYMENT,
				CtAbstractPayTermVO.PK_INCOMEPERIOD,
				CtAbstractPayTermVO.EFFECTDATEADDDATE,
				CtAbstractPayTermVO.PAYMENTDAY, CtAbstractPayTermVO.CHECKDATA,
				CtAbstractPayTermVO.EFFECTMONTH,
				CtAbstractPayTermVO.EFFECTADDMONTH,
				CtAbstractPayTermVO.PK_BALATYPE, CtAbstractPayTermVO.ISDEPOSIT,
				CtAbstractPayTermVO.PK_RATE, CtAbstractPayTermVO.ACCOUNTDAY };

		for (String item : items) {
			if (item.equals(name)) {
				return true;
			}
		}
		return false;
	}

	public static void setPlanEndDate(AggCtSaleVO billvo, int row) {
		UFDate effectdate = billvo.getCtSalePayTermVO()[row].getDplaneffectdate();
		if (effectdate != null) {
			ArapTermDateVO arapTermDateVO = new ArapTermDateVO();
			arapTermDateVO.setPaydate(effectdate);
			IncomeChVO incomechVO = SalePayTermUtil.convert(billvo, row);
			arapTermDateVO.setIncomevo(incomechVO);
			UFDate expiredate = BillTermUtils
					.getExpiredateByTermDateVO(arapTermDateVO);
			billvo.getCtSalePayTermVO()[row].setDplanenddate(expiredate);
		}
	}

	private static IncomeChVO convert(AggCtSaleVO billvo, int row) {
		IncomeChVO incomechVO = new IncomeChVO();
		String[] attrnames = incomechVO.getAttributeNames();
		for (String attr : attrnames) {
			if(billvo.getCtSalePayTermVO()[row].getAttributeValue(attr)!=null) {
				String val = billvo.getCtSalePayTermVO()[row].getAttributeValue(attr).toString();
				incomechVO.setAttributeValue(attr, val);
			}
			
		}
		return incomechVO;
	}

	public static void setRealEndDate(AggCtSaleVO billvo, int row) {
		UFDate realEffectDate =billvo.getCtSalePayTermVO()[row].getDrealeffectdate();
		if (realEffectDate != null) {
			ArapTermDateVO arapTermDateVO = new ArapTermDateVO();
			arapTermDateVO.setPaydate(realEffectDate);
			IncomeChVO incomechVO = SalePayTermUtil.convert(billvo, row);
			arapTermDateVO.setIncomevo(incomechVO);
			UFDate expiredate = BillTermUtils
					.getExpiredateByTermDateVO(arapTermDateVO);
			billvo.getCtSalePayTermVO()[row].setDrealenddate(expiredate);
		}
	}
}
