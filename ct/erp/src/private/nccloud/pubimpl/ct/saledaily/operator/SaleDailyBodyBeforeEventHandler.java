package nccloud.pubimpl.ct.saledaily.operator;

import nc.vo.ct.entity.CtAbstractBVO;
import nc.vo.ct.entity.CtAbstractPayTermVO;
import nc.vo.ct.saledaily.entity.CtSaleBVO;
import nccloud.dto.scmpub.pub.event.rule.AbstractBeforeHandler;
import nccloud.dto.scmpub.pub.event.rule.IBeforeRule;
import nccloud.pubimpl.ct.saledaily.event.body.SaleDailyCustMaterialBeforeRule;
import nccloud.pubimpl.ct.saledaily.event.body.SaleDailyEffectDateBeforeRule;
import nccloud.pubimpl.ct.saledaily.event.body.SaleDailyMarbasclassBeforeRulle;
import nccloud.pubimpl.ct.saledaily.event.body.SaleDailyMaterialBeforeRulle;
import nccloud.pubimpl.ct.saledaily.event.body.SaleDailyUnitBeforeRule;
import nccloud.pubimpl.ct.saledaily.event.body.SaleDailyUnitRateBeforeRule;
import nccloud.pubimpl.ct.saledaily.event.body.SaleDailydrealdateBeforeRule;

/**
 * @description 销售合同表体编辑前
 * @author wangshrc
 * @date 2019年2月19日 上午9:36:26
 * @version ncc1.0
 */
public class SaleDailyBodyBeforeEventHandler extends AbstractBeforeHandler {

	@Override
	protected IBeforeRule getBeforeRule(String key) {
		IBeforeRule rule = null;
		if (CtAbstractBVO.VQTUNITRATE.equals(key)
				|| CtAbstractBVO.VCHANGERATE.equals(key)) {
			// 换算率编辑前
			rule = new SaleDailyUnitRateBeforeRule();
		} else if (CtSaleBVO.CCUSTMATERIALID.equals(key)) {
			// 客户物料码编辑前
			rule = new SaleDailyCustMaterialBeforeRule();
		} else if (CtAbstractPayTermVO.DREALEFFECTDATE.equals(key)
				|| CtAbstractPayTermVO.DREALENDDATE.equals(key)) {
			// 生效日期编辑前
			rule = new SaleDailyEffectDateBeforeRule();
		} else if (CtSaleBVO.CQTUNITID.equals(key)) {
			// 报价单位编辑前
			rule = new SaleDailyUnitBeforeRule();
		}else if(CtSaleBVO.PK_MATERIAL.equals(key) ) {
			//物料
			rule = new SaleDailyMaterialBeforeRulle();
		}else if( CtSaleBVO.PK_MARBASCLASS.equals(key)) {
			//物料分类编辑前
			rule = new SaleDailyMarbasclassBeforeRulle();
		}else if(CtAbstractPayTermVO.DREALEFFECTDATE.equals(key) || CtAbstractPayTermVO.DREALENDDATE.equals(key)) {
			//收款协议   实际起效日和实际到期日编辑前事件
			rule = new SaleDailydrealdateBeforeRule();
		}
		return rule;
	}

}
