package nccloud.pubimpl.ct.purdaily.event;

import nc.vo.ct.entity.CtAbstractBVO;
import nc.vo.ct.entity.CtAbstractTermVO;
import nc.vo.ct.pub.CTVatNameConst;
import nc.vo.ct.purdaily.entity.AggCtPuVO;
import nc.vo.ct.purdaily.entity.CtPaymentVO;
import nc.vo.ct.purdaily.entity.CtPuBVO;
import nc.vo.scmpub.res.billtype.CTBillType;
import nccloud.dto.scmpub.pub.event.rule.AbstractBodyAfterHandler;
import nccloud.dto.scmpub.pub.event.rule.IBodyAfterRule;
import nccloud.pubimpl.ct.purdaily.event.after.body.CTCheckDateAfterRule;
import nccloud.pubimpl.ct.purdaily.event.after.body.CTEffectAddMonthAfterRule;
import nccloud.pubimpl.ct.purdaily.event.after.body.CTEffectMonthAfterRule;
import nccloud.pubimpl.ct.purdaily.event.after.body.CTOutAccountDateAfterRule;
import nccloud.pubimpl.ct.purdaily.event.after.body.CTPaymentDayAfterRule;
import nccloud.pubimpl.ct.purdaily.event.after.body.CTPuArrvstockAfterRule;
import nccloud.pubimpl.ct.purdaily.event.after.body.CTPuCountryAfterRule;
import nccloud.pubimpl.ct.purdaily.event.after.body.CTPuFinanceorgAfterRule;
import nccloud.pubimpl.ct.purdaily.event.after.body.CTPuMaterialAfterRule;
import nccloud.pubimpl.ct.purdaily.event.after.body.ChangeRateAfterRule;
import nccloud.pubimpl.ct.purdaily.event.after.body.CqtUnitIdAfterRule;
import nccloud.pubimpl.ct.purdaily.event.after.body.CtTaxcodeAfterRule;
import nccloud.pubimpl.ct.purdaily.event.after.body.DelivDateAfterRule;
import nccloud.pubimpl.ct.purdaily.event.after.body.PuCastUnitIdAfterRule;
import nccloud.pubimpl.ct.purdaily.event.after.body.PuMarBasClassAfterRule;
import nccloud.pubimpl.ct.purdaily.event.after.body.RelationCalculateAfterRule;
import nccloud.pubimpl.ct.purdaily.event.after.body.TermAfterRule;

/**
 * @description 表体编辑后事件
 * @author xiahui
 * @date 创建时间：2019-1-17 下午4:12:44
 * @version ncc1.0
 * @ref nc.ui.ct.purdaily.editor.after.PuBodyAfterEventHandler
 **/
public class BodyAfterEventHandler extends AbstractBodyAfterHandler {

	@Override
	protected IBodyAfterRule<AggCtPuVO> getAfterRule(String key) {
		IBodyAfterRule<AggCtPuVO> rule = null;

		// 计划收发货日期
		if (CtAbstractBVO.DELIVDATE.equals(key)) {
			rule = new DelivDateAfterRule();
		}
		// 物料
		else if (CtAbstractBVO.PK_MATERIAL.equals(key)) {
			rule = new CTPuMaterialAfterRule();
		}
		// 物料分类
		else if (CtAbstractBVO.PK_MARBASCLASS.equals(key)) {
			rule = new PuMarBasClassAfterRule();
		}
		// 报价换算率
		else if (CtAbstractBVO.VQTUNITRATE.equals(key)) {
			rule = new ChangeRateAfterRule(CtAbstractBVO.VQTUNITRATE);
		}
		// 换算率
		else if (CtAbstractBVO.VCHANGERATE.equals(key)) {
			rule = new ChangeRateAfterRule(CtAbstractBVO.VCHANGERATE);
		}
		// 单位
		else if (CtAbstractBVO.CASTUNITID.equals(key)) {
			rule = new PuCastUnitIdAfterRule();
		}
		// 报价单位
		else if (CtAbstractBVO.CQTUNITID.equals(key)) {
			rule = new CqtUnitIdAfterRule();
		}
		// 国家
		else if (CTVatNameConst.CSENDCOUNTRYID.equals(key)) {
			rule = new CTPuCountryAfterRule(CTVatNameConst.CSENDCOUNTRYID);
		} else if (CTVatNameConst.CRECECOUNTRYID.equals(key)) {
			rule = new CTPuCountryAfterRule(CTVatNameConst.CRECECOUNTRYID);
		} else if (CTVatNameConst.CTAXCOUNTRYID.equals(key)) {
			rule = new CTPuCountryAfterRule(CTVatNameConst.CTAXCOUNTRYID);
		}
		// 税码
		else if (CTVatNameConst.CTAXCODEID.equals(key)) {
			rule = new CtTaxcodeAfterRule(CTBillType.PurDaily.getCode());
		}
		// 财务组织
		else if (CtAbstractBVO.PK_FINANCEORG_V.equals(key)) {
			rule = new CTPuFinanceorgAfterRule();
		}
		// 收货库存组织
		else if (CtPuBVO.PK_ARRVSTOCK_V.equals(key)) {
			rule = new CTPuArrvstockAfterRule();
		}
		// 出账日
		else if (CtPaymentVO.OUTACCOUNTDATE.equals(key)) {
			rule = new CTOutAccountDateAfterRule();
		}
		// 账期天数
		else if (CtPaymentVO.PAYMENTDAY.equals(key)) {
			rule = new CTPaymentDayAfterRule();
		}
		// 固定结账日
		else if (CtPaymentVO.CHECKDATA.equals(key)) {
			rule = new CTCheckDateAfterRule();
		}
		// 生效月
		else if (CtPaymentVO.EFFECTMONTH.equals(key)) {
			rule = new CTEffectMonthAfterRule();
		}
		// 附加月
		else if (CtPaymentVO.EFFECTADDMONTH.equals(key)) {
			rule = new CTEffectAddMonthAfterRule();
		}
		// 合同条款
		else if (CtAbstractTermVO.VTERMCODE.equals(key)) {
			rule = new TermAfterRule();
		}
		// 单据金额联动
		else if (CtAbstractBVO.NASTNUM.equals(key) || CtAbstractBVO.VCHANGERATE.equals(key)
				|| CtAbstractBVO.NNUM.equals(key) || CtAbstractBVO.NQTORIGPRICE.equals(key)
				|| CtAbstractBVO.NQTORIGTAXPRICE.equals(key) || CtAbstractBVO.NORIGPRICE.equals(key)
				|| CtAbstractBVO.NORIGTAXPRICE.equals(key) || CtAbstractBVO.NGPRICE.equals(key)
				|| CtAbstractBVO.NGTAXPRICE.equals(key) || CtAbstractBVO.NQTUNITNUM.equals(key)
				|| CtAbstractBVO.NTAXRATE.equals(key) || CtAbstractBVO.FTAXTYPEFLAG.equals(key)
				|| CtAbstractBVO.NORIGTAXMNY.equals(key) || CtAbstractBVO.NTAXMNY.equals(key) || CtAbstractBVO.NMNY.equals(key)
				|| CtAbstractBVO.NTAX.equals(key) || CtAbstractBVO.NTAXMNY.equals(key)
				|| CTVatNameConst.NNOSUBTAXRATE.equals(key) || CTVatNameConst.NNOSUBTAX.equals(key)
				|| CTVatNameConst.NCALTAXMNY.equals(key) || CtAbstractBVO.NQTPRICE.equals(key)
				|| CtAbstractBVO.NQTTAXPRICE.equals(key)) {
			rule = new RelationCalculateAfterRule();
		}
		return rule;
	}

}
