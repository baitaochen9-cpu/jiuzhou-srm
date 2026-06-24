package nccloud.web.ct.saledaily.action;

import nc.vo.ct.enumeration.CtFlowEnum;
import nc.vo.ct.saledaily.entity.AggCtSaleVO;
import nccloud.web.ct.saledaily.utils.SaleDailyUtil;

/**
 * @description 销售合同解冻
 * @author wangshrc
 * @date 2019年1月25日 下午1:54:38
 * @version ncc1.0
 */
public class SaleDailyCardUnFreezeAction extends SaleDailyCardCommonAction {

	@Override
	public String getPFActionName() {
		return "UNFREEZE";
	}

	@Override
	public String getActioncode() {
		return "unfrozen";
	}

	@Override
	protected void beforeGetVos(AggCtSaleVO[] vos) {
		SaleDailyUtil.addNewExecVO(vos, (Integer) CtFlowEnum.FROZEN.value(),
				this.reason, nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("4004132_0","04004132-0010")/*@res "解冻"*/);
	}
}