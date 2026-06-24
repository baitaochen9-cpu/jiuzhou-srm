package nccloud.pubimpl.ct.payplan.operator;

import nc.impl.pubapp.pattern.data.view.ViewQuery;
import nc.ui.pub.print.IDataSource;
import nc.vo.ct.purdaily.entity.AggPayPlanVO;
import nc.vo.ct.purdaily.entity.CtPuVO;
import nc.vo.ct.purdaily.entity.PayPlanVO;
import nc.vo.ct.purdaily.entity.PayPlanViewVO;
import nccloud.dto.ct.pub.utils.OperateExceptionUtils;
import nccloud.pubimpl.ct.payplan.utils.PayplanBeforePrintDataProcess;
import nccloud.pubitf.scmpub.pub.print.BaseMetaPrintService;

/**
 * @description 采购合同付款计划打印操作
 * @author xiahui
 * @date 创建时间：2019-3-8 下午3:05:27
 * @version ncc1.0
 **/
public class PayplanPrintOperator extends BaseMetaPrintService {

	@Override
	public Object[] getDatas(String[] ids) {
		ViewQuery<PayPlanViewVO> query = new ViewQuery<PayPlanViewVO>(PayPlanViewVO.class);
		PayPlanViewVO[] bills = query.query(ids);
		// 检查并发
		OperateExceptionUtils.checkVo(bills, null);
		return this.getAggVOFromViewVO(bills);
	}

	@Override
	public IBeforePrintDataProcess getProcessor() {
		return new PayplanBeforePrintDataProcess();
	}

	@Override
	protected IDataSource[] getDataSource(Object[] datas) {
		return new IDataSource[] { new MetaDataSource(datas) };
	}

	public AggPayPlanVO[] getAggVOFromViewVO(PayPlanViewVO[] viewVOs) {
		AggPayPlanVO[] aggVOs = new AggPayPlanVO[viewVOs.length];
		for (int i = 0; i < viewVOs.length; ++i) {
			CtPuVO headerVO = (CtPuVO) viewVOs[i].getVO(CtPuVO.class);
			PayPlanVO payplanVO = (PayPlanVO) viewVOs[i].getVO(PayPlanVO.class);
			aggVOs[i] = new AggPayPlanVO();
			aggVOs[i].setParent(headerVO);
			aggVOs[i].setChildrenVO(new PayPlanVO[] { payplanVO });
		}
		return aggVOs;
	}

}
