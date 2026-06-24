package nc.ewm.print.add;

import nc.bd.ewm.print.PrintAdd;
import nc.vo.ewm.workorder.AggWorkOrderVO;

public class PrintAddImpl implements PrintAdd {

	@Override
	public String PushPrintAdd (AggWorkOrderVO AggWorkOrderVO)throws Exception {
		return new PrintAddService().sysList(AggWorkOrderVO);
	}
}
