package nccloud.pubimpl.ct.saledaily.operator;

import nc.impl.pubapp.pattern.data.bill.BillQuery;
import nc.vo.ct.saledaily.entity.AggCtSaleVO;
import nccloud.pubitf.scmpub.pub.print.BaseMetaPrintService;

/**
 * @description 销售合同打印处理
 * @author wangshrc
 * @date 2019年2月22日 下午3:58:41
 * @version ncc1.0
 */
public class SaleDailyPrintOperator extends BaseMetaPrintService {

	@Override
	public Object[] getDatas(String[] ids) {
		SaleDailyProcessor processor = new SaleDailyProcessor();
		this.setProcessor(processor);
		BillQuery<AggCtSaleVO> query = new BillQuery<AggCtSaleVO>(
				AggCtSaleVO.class);
		AggCtSaleVO[] bills = query.query(ids);
		return bills;
	}

}
