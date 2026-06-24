package nccloud.web.ct.saledaily.action;

import nc.vo.pub.pf.workflow.IPFActionName;
import nc.vo.pubapp.pub.power.PowerActionEnum;

/**
 * @description 销售合同列表删除
 * @author wangshrc
 * @date 2019年1月21日 上午9:22:55
 * @version ncc1.0
 */
public class SaleDailyListDeleteAction extends SaleDailyListCommonAction {

	@Override
	public String getPFActionName() {
		return IPFActionName.DEL_DELETE;
	}

	@Override
	public String getActioncode() {
		return PowerActionEnum.DELETE.getActioncode();
	}

}
