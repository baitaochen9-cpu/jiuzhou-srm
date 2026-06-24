package nc.bd.ewm.print;

import nc.vo.ewm.workorder.AggWorkOrderVO;

public interface PrintAdd {
//	推送新打印申请单
	public String PushPrintAdd (AggWorkOrderVO AggWorkOrderVO)throws Exception ;
}
