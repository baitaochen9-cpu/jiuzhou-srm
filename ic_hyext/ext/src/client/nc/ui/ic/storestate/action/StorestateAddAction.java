/** <a href="http://www.cpupk.com/decompiler">Eclipse Class Decompiler</a> plugin, Copyright (c) 2017 Chen Chao. **/
/*    */
package nc.ui.ic.storestate.action;
/*    */import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
/*    */
import nc.bs.bd.cache.CacheProxy;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.MapProcessor;
/*    */
import nc.ui.pubapp.uif2app.actions.batch.BatchAddLineAction;
/*    */
import nc.ui.uif2.model.BatchBillTableModel;
/*    */
import nc.vo.ic.storestate.StoreStateVO;
import nc.vo.pub.BusinessException;
/*    */
import nc.vo.uif2.LoginContext;
/*    */
/*    */public class StorestateAddAction extends BatchAddLineAction
/*    */{
	/*    */private static final long serialVersionUID = 1L;
	
	
//			private BatchBillTable editor = null;
	/*    */
	/*    */protected void setDefaultData(Object obj)
	/*    */{
		
		
		
		
		
		/* 29 */((StoreStateVO) obj).setPk_group(super.getModel().getContext()
				.getPk_group());
		
		
			
			
			
		
		
		
		/*    */}
	/*    */
	/*    */public void doAction(ActionEvent e)
	/*    */throws Exception
	/*    */{
		/* 35 */super.doAction(e);
	
		
		/* 36 */CacheProxy.fireDataInserted(new StoreStateVO().getTableName());
		
		StoreStateVO  vo=new  StoreStateVO();

		
		String user=InvocationInfoProxy.getInstance().getUserId();
			
			IUAPQueryBS iuap = (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
			
			String  sql=
					"select  f.value\n" +
							"from sm_individual_property  f\n" + 
							"where  f.pk_user='"+user+"'\n" + 
							"and f.propertyname='org_df_biz'";

			

					List<StoreStateVO>list=new ArrayList<StoreStateVO>();
			Map map;
			
				map = (Map)iuap.executeQuery(sql, new MapProcessor());
				String pk_org= map.get("value")==null?"":map.get("value").toString() ;
				
				
//				vo.setVname("66666");
				 vo.setPk_org(pk_org);
				 
				 
//				 getModel().updateLine(index, obj)
		getModel().updateLine(getModel().getSelectedIndex(), new  Object[]{vo});
	
		/*    */}
	/*    */
}