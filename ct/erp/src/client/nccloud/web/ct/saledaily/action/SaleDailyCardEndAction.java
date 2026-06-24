package nccloud.web.ct.saledaily.action;

import nc.vo.ct.enumeration.CtFlowEnum;
import nc.vo.ct.saledaily.entity.AggCtSaleVO;
import nccloud.web.ct.saledaily.utils.SaleDailyUtil;

/**
 * @description 销售合同终止
 * @author wangshrc
 * @date 2019年1月25日 下午1:57:41
 * @version ncc1.0
 */
public class SaleDailyCardEndAction extends SaleDailyCardCommonAction {

	@Override
	public String getPFActionName() {
		return "TERMINATE";
	}

	@Override
	public String getActioncode() {
		return "terminate";
	}

	@Override
	protected void beforeGetVos(AggCtSaleVO[] vos) {
		SaleDailyUtil.addNewExecVO(vos, (Integer) CtFlowEnum.VALIDATE.value(),
				this.reason, nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("4004132_0","04004132-0007")/*@res "终止"*/);
	}
}