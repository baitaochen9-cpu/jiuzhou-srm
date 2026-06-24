package nc.bs.cm.equivrate.bp;

import nc.pub.smart.data.DataSet;
import nc.vo.pub.BusinessException;

import com.ufida.dataset.IContext;
/**
 * 本代码只为了做报表使用
 * @author yunfeng.li
 *
 */
public class ReportCode {
	
	private void code1() throws BusinessException{
		//1. 获取查询参数,并放到context,以便后台使用
        Object pk_org = getParameter("pk_org");
        Object period = getParameter("period");
        IContext context = getContext();
        context.setAttribute("pk_org", pk_org);
        context.setAttribute("period", period);
        //2.后台查询代码,返回结果集,
        //注意对应的HOME中接口和实现类以及.upm配置文件要已经部署--开环境也需要
        //注意不要写成如下形式 ,否则编译会报错:因为对应的EJB文件还没加载进来，用正确的方式编译以后，再这样写就OK
//        nc.itf.cm.inprocess.IInprocessBackFlushService2 s =nc.bs.framework.common.NCLocator.getInstance() .lookup(nc.itf.cm.inprocess.IInprocessBackFlushService2.class);
//        
//        nc.pub.smart.data.DataSet ds =s.proWrOrders(context);
        nc.pub.smart.data.DataSet ds = nc.bs.framework.common.NCLocator.getInstance()
                                                                       .lookup(nc.itf.cm.inprocess.IInprocessBackFlushService2.class)
                                                                       .proWrOrders(context);
        setDataSet(ds);
    
	}
	

	private IContext getContext() {
		// TODO Auto-generated method stub
		return null;
	}


	private void setDataSet(DataSet ds) {
		// TODO Auto-generated method stub
		
	}

	private Object getParameter(String string) {
		// TODO Auto-generated method stub
		return null;
	}

}
