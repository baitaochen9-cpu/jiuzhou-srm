package nccloud.web.ct.saledaily.action;

import nc.vo.ct.enumeration.CtFlowEnum;
import nc.vo.ct.saledaily.entity.AggCtSaleVO;
import nccloud.web.ct.saledaily.utils.SaleDailyUtil;

/**
 * @description 销售合同列表取消生效
 * @author wangshrc
 * @date 2019年1月25日 下午3:49:20
 * @version ncc1.0
 */
public class SaleDailyListUnValidateAction extends SaleDailyListCommonAction {

	@Override
	public String getPFActionName() {
		return "UNVALIDATE";
	}

	@Override
	public String getActioncode() {
		return "unvalidate";
	}

	@Override
	protected void beforeGetVos(AggCtSaleVO[] vos) {
		SaleDailyUtil.addNewExecVO(vos, (Integer) CtFlowEnum.VALIDATE.value(),
				this.reason, nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("4004132_0","04004132-0011")/*@res "取消生效"*/);
	}
}