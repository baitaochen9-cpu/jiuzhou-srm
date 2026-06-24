package nccloud.web.ct.saledaily.action;

import nc.vo.ct.saledaily.entity.AggCtSaleVO;
import nccloud.pubitf.scmpub.pub.batchopr.dto.ISCMBatchOprContext;
import nccloud.pubitf.scmpub.pub.batchopr.dto.SCMBatchOprContext;
import nccloud.pubitf.scmpub.pub.batchopr.dto.SCMBatchResultDTO;
import nccloud.web.ct.saledaily.utils.BatchOprResultConvertor;
import nccloud.web.ct.saledaily.utils.SaleDailyPrecisionUtil;
import nccloud.web.scmpub.pub.utils.SCMBatchOperatorResult;
import nccloud.web.scmpub.pub.utils.batchopr.SCMBatchOperator;

/**
 * @description 销售合同列表变更删除
 * @author wangshrc
 * @date 2019年2月13日 上午9:34:12
 * @version ncc1.0
 */
public class SaleDailyListModiDeleteAction extends SaleDailyListCommonAction {
	@Override
	public String getPFActionName() {
		return null;
	}

	protected Object action(AggCtSaleVO[] vos) {

		ISCMBatchOprContext context = new SCMBatchOprContext();
		context.setInterfaceName("nc.itf.ct.saledaily.ISaledailyApprove");
		context.setMethodName("modiDelete");
		context.setParamTypes(new Class[] { AggCtSaleVO[].class });
		context.setParams(new Object[] { vos });
		context.setBillPos(0);
		SCMBatchResultDTO resulet = SCMBatchOperator.doRemoteCall(context);
		SCMBatchOperatorResult ret = new BatchOprResultConvertor().convert(resulet, "400600200_list");
		SaleDailyPrecisionUtil.dealPrecision(ret.getSucessVOs());
		return ret;
	}
}
