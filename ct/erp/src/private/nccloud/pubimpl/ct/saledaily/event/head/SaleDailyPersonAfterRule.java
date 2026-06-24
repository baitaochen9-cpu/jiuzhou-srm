package nccloud.pubimpl.ct.saledaily.event.head;

import java.util.List;
import java.util.Map;

import nc.itf.scmpub.reference.uap.bd.psn.PsndocPubService;
import nc.itf.scmpub.reference.uap.org.DeptPubService;
import nc.vo.ct.saledaily.entity.AggCtSaleVO;
import nccloud.dto.scmpub.pub.context.BillCardHeadEditEvent;
import nccloud.dto.scmpub.pub.event.rule.IHeadAfterRule;

/**
 * @description 销售合同人员编辑后
 * @author wangshrc
 * @date 2019年2月13日 下午3:40:28
 * @version ncc1.0
 */
public class SaleDailyPersonAfterRule implements IHeadAfterRule<AggCtSaleVO> {

	@SuppressWarnings("rawtypes")
	@Override
	public AggCtSaleVO afterEdit(AggCtSaleVO billvo, BillCardHeadEditEvent event, Map userobject) {
		String pk_person = billvo.getParentVO().getPersonnelid();
		if (pk_person == null) {
			return billvo;
		}
		Map<String, List<String>> retMap = null;
		retMap = PsndocPubService.queryDeptIDByPsndocIDs(new String[] { pk_person });
		if (retMap != null && !retMap.isEmpty()) {
			int len = retMap.get(pk_person).size();
			// 设置部门，如果人员有唯一的部门时。
			if (len == 1) {
				String pk_dept = retMap.get(pk_person).get(0);
				billvo.getParentVO().setDepid(pk_dept);
				String pk_dept_v = this.getLastdept_v(pk_dept);
				billvo.getParentVO().setDepid_v(pk_dept_v);
			}

		}
		return billvo;
	}

	private String getLastdept_v(String dept) {
		Map<String, String> retMap = DeptPubService.getLastVIDSByDeptIDS(new String[] { dept });
		return retMap.get(dept);
	}

}
