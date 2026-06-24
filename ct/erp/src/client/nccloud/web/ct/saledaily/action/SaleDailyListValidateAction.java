package nccloud.web.ct.saledaily.action;

import nc.vo.ct.enumeration.CtFlowEnum;
import nc.vo.ct.saledaily.entity.AggCtSaleVO;
import nccloud.web.ct.saledaily.utils.SaleDailyUtil;

/**
 * @description 销售合同列表生效
 * @author wangshrc
 * @date 2019年1月25日 下午3:49:11
 * @version ncc1.0
 */
public class SaleDailyListValidateAction extends SaleDailyListCommonAction {
	@Override
	public String getPFActionName() {
		return "VALIDATE";
	}

	@Override
	public String getActioncode() {
		return "validate";
	}

	@Override
	protected void beforeGetVos(AggCtSaleVO[] vos) {
		SaleDailyUtil.addNewExecVO(vos, (Integer) CtFlowEnum.APPROVE.value(),
				this.reason, nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("4004132_0","04004132-0012")/*@res "生效"*/);
	}
}