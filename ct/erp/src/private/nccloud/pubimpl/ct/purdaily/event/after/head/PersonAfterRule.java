package nccloud.pubimpl.ct.purdaily.event.after.head;

import java.util.List;
import java.util.Map;

import nc.bs.framework.common.NCLocator;
import nc.itf.scmpub.reference.uap.org.DeptPubService;
import nc.pubitf.uapbd.IPsndocPubService;
import nc.vo.bd.psn.PsndocVO;
import nc.vo.bd.psn.PsnjobVO;
import nc.vo.ct.entity.CtAbstractVO;
import nc.vo.ct.purdaily.entity.AggCtPuVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFLiteralDate;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nccloud.dto.ct.pub.utils.ExtBillUtil;
import nccloud.dto.scmpub.pub.context.BillCardHeadEditEvent;
import nccloud.dto.scmpub.pub.event.rule.IHeadAfterRule;

/**
 * @description 人员编辑后事件（带出部门）
 * @author xiahui
 * @date 2019年7月31日 上午9:57:05
 * @version ncc1.0
 */
public class PersonAfterRule implements IHeadAfterRule<AggCtPuVO> {

	@SuppressWarnings("rawtypes")
	@Override
	public AggCtPuVO afterEdit(AggCtPuVO billvo, BillCardHeadEditEvent event, Map userobject) {
		ExtBillUtil util = new ExtBillUtil(billvo);

		String pk_person = util.getHeadTailStringValue(CtAbstractVO.PERSONNELID);
		if (pk_person == null || pk_person.isEmpty()) {
			util.setHeadValue(CtAbstractVO.DEPID, null);
			util.setHeadValue(CtAbstractVO.DEPID_V, null);
			return billvo;
		}

		// NC_SCM_CT-NC6.5-Special-20190123-206048044
		IPsndocPubService service = NCLocator.getInstance().lookup(IPsndocPubService.class);
		try {
			List<PsndocVO> psndocVOs = service.queryPsndocAndMainJobByPks(new String[] { pk_person });
			for (PsndocVO psndocVO : psndocVOs) {
				PsnjobVO[] psnjobs = psndocVO.getPsnjobs();
				for (PsnjobVO psnjobVO : psnjobs) {
					UFLiteralDate Enddutydate = psnjobVO.getEnddutydate();
					if (Enddutydate == null) {
						String pk_dept = psnjobVO.getPk_dept();
						Map<String, String> deptMap = DeptPubService.getLastVIDSByDeptIDS(new String[] { pk_dept });
						util.setHeadValue(CtAbstractVO.DEPID, pk_dept);
						util.setHeadValue(CtAbstractVO.DEPID_V, deptMap.get(pk_dept));
						break;
					}
				}
			}
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException("带出部门异常。");
		}

		return billvo;
	}

}
