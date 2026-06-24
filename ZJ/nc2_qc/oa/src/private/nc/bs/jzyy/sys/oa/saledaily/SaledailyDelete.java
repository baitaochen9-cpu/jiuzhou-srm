package nc.bs.jzyy.sys.oa.saledaily;

import nc.itf.scmpub.reference.uap.pf.PfServiceScmUtil;
import nc.vo.ct.enumeration.CtFlowEnum;
import nc.vo.ct.saledaily.entity.AggCtSaleVO;
import nc.vo.ct.saledaily.entity.CtSaleVO;
import nc.vo.pub.BusinessException;

import org.apache.commons.lang.ArrayUtils;

public class SaledailyDelete {

	public void delete(AggCtSaleVO[] vos) throws BusinessException {
		if (ArrayUtils.isEmpty(vos)) {
			return;
		}
		try {
			for (AggCtSaleVO aggvo : vos) {
				CtSaleVO parentVO = aggvo.getParentVO();
				if (!parentVO.getFstatusflag().equals(CtFlowEnum.Free.value())) {
					throw new BusinessException("当前合同编号："
							+ parentVO.getVbillcode() + "不是自由状态，不能删除");
				}
			}
			PfServiceScmUtil.processBatch("DELETE", "Z3", vos, null, null);
		} catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
	}

	private AggCtSaleVO[] unApprove(AggCtSaleVO[] vos) throws BusinessException {
		if (ArrayUtils.isEmpty(vos)) {
			return null;
		}
		try {
			for (AggCtSaleVO aggvo : vos) {
				CtSaleVO parentVO = aggvo.getParentVO();
				if (!parentVO.getFstatusflag().equals(
						CtFlowEnum.APPROVE.value())
						&& !parentVO.getFstatusflag().equals(
								CtFlowEnum.APPROVING.value())) {
					throw new BusinessException("当前合同编号："
							+ parentVO.getVbillcode() + "状态不符合弃审条件，不能弃审");
				}
			}
			return (AggCtSaleVO[]) PfServiceScmUtil.processBatch("UNAPPROVE",
					"Z3", vos, null, null);
		} catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
	}

}
