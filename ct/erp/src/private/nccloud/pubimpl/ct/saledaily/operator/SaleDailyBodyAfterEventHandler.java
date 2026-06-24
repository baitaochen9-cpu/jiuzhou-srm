package nccloud.pubimpl.ct.saledaily.operator;

import nc.vo.ct.entity.CtAbstractBVO;
import nc.vo.ct.entity.CtAbstractExpVO;
import nc.vo.ct.entity.CtAbstractPayTermVO;
import nc.vo.ct.entity.CtAbstractTermVO;
import nc.vo.ct.pub.CTVatNameConst;
import nc.vo.ct.saledaily.entity.AggCtSaleVO;
import nc.vo.ct.saledaily.entity.CtSaleBVO;
import nccloud.dto.scmpub.pub.event.rule.AbstractBodyAfterHandler;
import nccloud.dto.scmpub.pub.event.rule.IBodyAfterRule;
import nccloud.pubimpl.ct.saledaily.event.body.SaleDailyAccountDayAfterRule;
import nccloud.pubimpl.ct.saledaily.event.body.SaleDailyAccrateAfterRule;
import nccloud.pubimpl.ct.saledaily.event.body.SaleDailyCastUnitAfterRule;
import nccloud.pubimpl.ct.saledaily.event.body.SaleDailyChangeRateAfterRule;
import nccloud.pubimpl.ct.saledaily.event.body.SaleDailyCheckDataAfterRule;
import nccloud.pubimpl.ct.saledaily.event.body.SaleDailyCountryAfterRule;
import nccloud.pubimpl.ct.saledaily.event.body.SaleDailyCqtUnitAfterRule;
import nccloud.pubimpl.ct.saledaily.event.body.SaleDailyCustMarAfterRule;
import nccloud.pubimpl.ct.saledaily.event.body.SaleDailyCustMaterialAfterRule;
import nccloud.pubimpl.ct.saledaily.event.body.SaleDailyDelivDateAfterRule;
import nccloud.pubimpl.ct.saledaily.event.body.SaleDailyDepositAfterRule;
import nccloud.pubimpl.ct.saledaily.event.body.SaleDailyEffectAddDateAfterRule;
import nccloud.pubimpl.ct.saledaily.event.body.SaleDailyEffectAddMonthAfterRule;
import nccloud.pubimpl.ct.saledaily.event.body.SaleDailyEffectMonthAfterRule;
import nccloud.pubimpl.ct.saledaily.event.body.SaleDailyFinanceorgAfterRule;
import nccloud.pubimpl.ct.saledaily.event.body.SaleDailyMarBaseClassAfterRule;
import nccloud.pubimpl.ct.saledaily.event.body.SaleDailyMaterialAfterRule;
import nccloud.pubimpl.ct.saledaily.event.body.SaleDailyPayMentDayAfterRule;
import nccloud.pubimpl.ct.saledaily.event.body.SaleDailyPlanEffectDateAfterRule;
import nccloud.pubimpl.ct.saledaily.event.body.SaleDailyPlanEndDateAfterRule;
import nccloud.pubimpl.ct.saledaily.event.body.SaleDailyRealEffectDateAfterRule;
import nccloud.pubimpl.ct.saledaily.event.body.SaleDailyRealEndDateAfterRule;
import nccloud.pubimpl.ct.saledaily.event.body.SaleDailyRelationCalculateAfterRule;
import nccloud.pubimpl.ct.saledaily.event.body.SaleDailyTaxCodeAfterRule;
import nccloud.pubimpl.ct.saledaily.event.body.SaleDailyTermCodeAfterRule;

/**
 * @description 销售合同表体编辑后
 * @author wangshrc
 * @date 2019年2月14日 下午1:24:40
 * @version ncc1.0
 */
public class SaleDailyBodyAfterEventHandler extends AbstractBodyAfterHandler {

	@Override
	protected IBodyAfterRule<AggCtSaleVO> getAfterRule(String changeKey) {
		IBodyAfterRule<AggCtSaleVO> rule = null;
		if (CtAbstractBVO.DELIVDATE.equals(changeKey)) {
			// 计划收发货日期编辑后
			rule = new SaleDailyDelivDateAfterRule();
		} else if (CtAbstractBVO.PK_MATERIAL.equals(changeKey)) {
			// 销售合同物料编辑后
			rule = new SaleDailyMaterialAfterRule();
		} else if (CtAbstractBVO.PK_MARBASCLASS.equals(changeKey)) {
			// 销售合同物料基本分类编辑后
			rule = new SaleDailyMarBaseClassAfterRule();
		} else if (CtAbstractBVO.VQTUNITRATE.equals(changeKey)
				|| CtAbstractBVO.VCHANGERATE.equals(changeKey)) {
			// 销售合同换算率
			rule = new SaleDailyChangeRateAfterRule();
		} else if (CtAbstractBVO.CASTUNITID.equals(changeKey)) {
			// 单位编辑后
			rule = new SaleDailyCastUnitAfterRule();
		} else if (CtAbstractBVO.CQTUNITID.equals(changeKey)) {
			// 报价单位编辑后
			rule = new SaleDailyCqtUnitAfterRule();
		} else if (CTVatNameConst.CSENDCOUNTRYID.equals(changeKey)
				|| CTVatNameConst.CTAXCOUNTRYID.equals(changeKey)
				|| CTVatNameConst.CRECECOUNTRYID.equals(changeKey)) {
			// 销售合同国家编辑后
			rule = new SaleDailyCountryAfterRule();
		} else if (CTVatNameConst.CTAXCODEID.equals(changeKey)) {
			// 税码编辑后
			rule = new SaleDailyTaxCodeAfterRule();
		} else if (CtAbstractBVO.PK_FINANCEORG_V.equals(changeKey)) {
			// 销售合同财务组织编辑后
			rule = new SaleDailyFinanceorgAfterRule();
		} else if (CtSaleBVO.CCUSTMATERIALID.equals(changeKey)) {
			// 销售合同客户物料编辑后
			rule = new SaleDailyCustMaterialAfterRule();
		} else if (CtSaleBVO.CPRODUCTORID.equals(changeKey)) {
			// 客户物料码
			rule = new SaleDailyCustMarAfterRule();
		} else if (CtAbstractPayTermVO.CHECKDATA.equals(changeKey)) {
			// 结账日编辑后
			rule = new SaleDailyCheckDataAfterRule();
		} else if (CtAbstractPayTermVO.EFFECTMONTH.equals(changeKey)) {
			// 生效日期编辑后
			rule = new SaleDailyEffectMonthAfterRule();
		} else if (CtAbstractPayTermVO.EFFECTADDMONTH.equals(changeKey)) {
			// 附加月
			rule = new SaleDailyEffectAddMonthAfterRule();
		} else if (CtAbstractPayTermVO.PAYMENTDAY.equals(changeKey)) {
			// 账期天数编辑后
			rule = new SaleDailyPayMentDayAfterRule();
		} else if (CtAbstractPayTermVO.ISDEPOSIT.equals(changeKey)) {
			// 销售合同质保金编辑后
			rule = new SaleDailyDepositAfterRule();
		} else if (CtAbstractPayTermVO.ACCRATE.equals(changeKey)) {
			// 收款比例
			rule = new SaleDailyAccrateAfterRule();
		} else if (CtAbstractPayTermVO.DPLANEFFECTDATE.equals(changeKey)) {
			// 计划生效日期
			rule = new SaleDailyPlanEffectDateAfterRule();
		} else if (CtAbstractPayTermVO.DPLANENDDATE.equals(changeKey)) {
			// 计划到期日
			rule = new SaleDailyPlanEndDateAfterRule();
		} else if (CtAbstractPayTermVO.DREALEFFECTDATE.equals(changeKey)) {
			// 实际生效日
			rule = new SaleDailyRealEffectDateAfterRule();
		} else if (CtAbstractPayTermVO.DREALENDDATE.equals(changeKey)) {
			// 实际到期日
			rule = new SaleDailyRealEndDateAfterRule();
		} else if (CtAbstractPayTermVO.EFFECTDATEADDDATE.equals(changeKey)) {
			// 延期天数
			rule = new SaleDailyEffectAddDateAfterRule();
		} else if (CtAbstractPayTermVO.ACCOUNTDAY.equals(changeKey)) {
			// 出账日
			rule = new SaleDailyAccountDayAfterRule();
		} else if (CtAbstractTermVO.VTERMCODE.equals(changeKey)) {
			// 合同条款
			rule = new SaleDailyTermCodeAfterRule();
		} else if (CtAbstractBVO.VFREE1.equals(changeKey)
				|| CtAbstractBVO.VFREE2.equals(changeKey)
				|| CtAbstractBVO.VFREE3.equals(changeKey)
				|| CtAbstractBVO.VFREE4.equals(changeKey)
				|| CtAbstractBVO.VFREE5.equals(changeKey)
				|| CtAbstractBVO.VFREE6.equals(changeKey)
				|| CtAbstractBVO.VFREE7.equals(changeKey)
				|| CtAbstractBVO.VFREE8.equals(changeKey)
				|| CtAbstractBVO.VFREE9.equals(changeKey)
				|| CtAbstractBVO.VFREE10.equals(changeKey)) {
			// 客户物料码
			rule = new SaleDailyCustMarAfterRule();
		}// 单据金额联动
		else if (CtAbstractBVO.NASTNUM.equals(changeKey)
				|| CtAbstractBVO.VCHANGERATE.equals(changeKey)
				|| CtAbstractBVO.NNUM.equals(changeKey)
				|| CtAbstractBVO.NQTORIGPRICE.equals(changeKey)
				|| CtAbstractBVO.NQTORIGTAXPRICE.equals(changeKey)
				|| CtAbstractBVO.NORIGPRICE.equals(changeKey)
				|| CtAbstractBVO.NORIGTAXPRICE.equals(changeKey)
				|| CtAbstractBVO.NGPRICE.equals(changeKey)
				|| CtAbstractBVO.NGTAXPRICE.equals(changeKey)
				|| CtAbstractBVO.NQTUNITNUM.equals(changeKey)
				|| CtAbstractBVO.NTAXRATE.equals(changeKey)
				|| CtAbstractBVO.FTAXTYPEFLAG.equals(changeKey)
				|| CtAbstractBVO.NORIGTAXMNY.equals(changeKey)
				|| CtAbstractBVO.NTAXMNY.equals(changeKey)
				|| CtAbstractBVO.NMNY.equals(changeKey)
				|| CtAbstractBVO.NTAX.equals(changeKey)
				|| CtAbstractBVO.NTAXMNY.equals(changeKey)
				|| CTVatNameConst.NNOSUBTAXRATE.equals(changeKey)
				|| CTVatNameConst.NNOSUBTAX.equals(changeKey)
				|| CTVatNameConst.NCALTAXMNY.equals(changeKey)
				|| CtAbstractBVO.NQTPRICE.equals(changeKey)
				|| CtAbstractBVO.NQTTAXPRICE.equals(changeKey)) {
			rule = new SaleDailyRelationCalculateAfterRule();
		}else if(CtAbstractExpVO.VEXPCODE.equals(changeKey)) {
			rule = null;
		}
		return rule;
	}

}
