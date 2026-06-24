package nccloud.web.ct.pub.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import nc.vo.pubapp.pattern.model.entity.bill.AbstractBill;
import nccloud.dto.scmpub.script.entity.SCMScriptResultDTO;

/**
 * @description 基础脚本执行Action
 * @author guozhq
 * @date 2018-8-10 上午10:31:33
 * @version ncc1.0
 */
public abstract class DeleteScriptAction extends BaseScriptAction {
	/**
	 *
	 * 由脚本结果VO,转换成最终返回前台VO
	 *
	 * @param dto
	 * @param bills
	 * @return
	 *
	 */
	public BaseBatchResult convertToResult(SCMScriptResultDTO dto, AbstractBill[] bills) {
		BaseBatchResult result = new BaseBatchResult();
		if (bills.length == 1) {
			result.setMessage(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("4009012_0","04009012-0024")/*@res "操作成功!"*/);
			result.setSuccessIds(new String[]{bills[0].getParent().getPrimaryKey()});
		} else {
			Map<Integer, String> errMsgs = dto.getErrorMessageMap();
			List<String> failIds = new ArrayList<>();
			if (errMsgs != null && errMsgs.size() > 0) {
				List<String> msgs = new ArrayList<String>();
				for (Entry<Integer, String> entry : errMsgs.entrySet()) {			
					msgs.add(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("4009012_0", "04009012-0022", null,
							new String[] { String.valueOf(entry.getKey() + 1) })/*
																																	 * @res
																																	 * "第{0}条记录失败："
																																	 */
							+ entry.getValue());
					failIds.add(bills[entry.getKey()].getPrimaryKey());
				}
				result.setErrMsg(msgs.toArray(new String[0]));
			}

			// 删除处理成功的id
			List<String> successIds = new ArrayList<>();
			for (AbstractBill bill : bills) {
				if (!failIds.contains(bill.getPrimaryKey())) {
					successIds.add(bill.getPrimaryKey());
				}
			}
			result.setSuccessIds(successIds.toArray(new String[] {}));
		}
		return result;
	}

	@Override
	public Map<String, Object> processSuccessResult(AbstractBill[] bills, AbstractBill[] orginalBills) {
		return null;
	}
}