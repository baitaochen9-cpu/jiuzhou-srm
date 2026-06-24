/** <a href="http://www.cpupk.com/decompiler">Eclipse Class Decompiler</a> plugin, Copyright (c) 2017 Chen Chao. **/
/*    */package nc.bs.pu.m21.maintain.rule.save;
/*    */import java.util.ArrayList;
/*    */
import java.util.List;
import nc.bs.framework.common.NCLocator;
/*    */
import nc.impl.pubapp.bill.billcode.BillCodeUtils;
/*    */
import nc.impl.pubapp.pattern.rule.ICompareRule;
import nc.itf.jzyy.sys.ISysDispatcher;
import nc.itf.uap.pf.IplatFormEntry;
/*    */
import nc.vo.pu.m21.entity.OrderHeaderVO;
/*    */
import nc.vo.pu.m21.entity.OrderVO;
/*    */
import nc.vo.pu.pub.util.PUBillCodeUtils;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
/*    */
import org.apache.commons.lang.ArrayUtils;
/*    */
import org.apache.commons.lang.ObjectUtils;

import nc.impl.pubapp.pattern.rule.IRule;

/*    */
/*    */public class SendOAAfterRule
/*    */implements IRule<OrderVO>
/*    */{
	/*    */public void process(OrderVO[] vos)
	/*    */{
		/*    */try
		/*    */{
			/* 27 */
			List list = new ArrayList();
			/* 39 */for (int i = 0; i < vos.length; ++i) {
				ISysDispatcher iIplatFormEntry = (ISysDispatcher)NCLocator.getInstance().lookup(ISysDispatcher.class.getName());
				 /*      */ 
				 /* 1182 */  try {
					 iIplatFormEntry.dispatch(vos[i], "OA_POORDER", null);
				} catch (BusinessException e) {
					// TODO Auto-generated catch block
					ExceptionUtils.wrappException(e);
				}
				
				/*    */}
			/*    */}
		/*    */catch (Exception e)
		/*    */{
			/* 32 */ExceptionUtils.wrappException(e);
			/*    */}
		/*    */}
	/*    */
}