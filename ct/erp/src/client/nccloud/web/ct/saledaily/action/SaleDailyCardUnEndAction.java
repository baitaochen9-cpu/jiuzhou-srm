package nccloud.web.ct.saledaily.action;

import nc.vo.ct.enumeration.CtFlowEnum;
import nc.vo.ct.saledaily.entity.AggCtSaleVO;
import nccloud.web.ct.saledaily.utils.SaleDailyUtil;

/**
 * @description 销售合同取消终止
 * @author wangshrc
 * @date 2019年1月25日 下午1:57:33
 * @version ncc1.0
 */
public class SaleDailyCardUnEndAction extends SaleDailyCardCommonAction {

	@Override
	public String getPFActionName() {
		return "UNTERMINATE";
	}

	@Override
	protected void beforeGetVos(AggCtSaleVO[] vos) {
		SaleDailyUtil.addNewExecVO(vos, (Integer) CtFlowEnum.TERMINATE.value(),
				this.reason, nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("4004132_0","04004132-0009")/*@res "取消终止"*/);
	}
}