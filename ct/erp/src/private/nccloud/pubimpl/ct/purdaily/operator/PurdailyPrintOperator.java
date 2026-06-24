package nccloud.pubimpl.ct.purdaily.operator;

import nc.impl.pubapp.pattern.data.bill.BillQuery;
import nc.vo.ct.purdaily.entity.AggCtPuVO;
import nccloud.dto.ct.pub.utils.OperateExceptionUtils;
import nccloud.pubimpl.ct.purdaily.utils.PurdailyBeforePrintDataProcess;
import nccloud.pubitf.scmpub.pub.print.BaseMetaPrintService;

/**
 * @description 采购合同维护打印操作
 * @author xiahui
 * @date 创建时间：2019-2-13 下午5:59:35
 * @version ncc1.0
 **/
public class PurdailyPrintOperator extends BaseMetaPrintService {

	@Override
	public Object[] getDatas(String[] ids) {
		BillQuery<AggCtPuVO> query = new BillQuery<AggCtPuVO>(AggCtPuVO.class);
		AggCtPuVO[] bills = query.query(ids);
		// 检查并发
		OperateExceptionUtils.checkVo(bills, null);
		return bills;
	}
	
	@Override
	public IBeforePrintDataProcess getProcessor() {
		return new PurdailyBeforePrintDataProcess();
	}

}
