package nccloud.web.ct.saledaily.action;

/**
 * @description 销售合同列表变更
 * @author wangshrc
 * @date 2019年2月13日 上午9:50:14
 * @version ncc1.0
 */
public class SaleDailyListModifyAction extends SaleDailyListCommonAction {

	@Override
	protected String getPFActionName() {
		return "MODIFY";
	}

	@Override
	public String getActioncode() {
		return "modify";
	}
}
