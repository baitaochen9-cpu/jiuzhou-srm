/** <a href="http://www.cpupk.com/decompiler">Eclipse Class Decompiler</a> plugin, Copyright (c) 2017 Chen Chao. **/
/*    */package nc.ui.ic.storestate.model;
/*    */
/*    */import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import sun.awt.AppContext;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.logging.Logger;
/*    */
import nc.ui.pubapp.uif2app.model.BatchBillTableModel;
/*    */
import nc.jdbc.framework.processor.MapProcessor;
import nc.ui.uif2.model.IAppModelDataManager;
/*    */
import nc.ui.uif2.model.IBatchAppModelService;
import nc.itf.uap.IUAPQueryBS;
import nc.bs.framework.common.NCLocator;
/*    */
import nc.vo.ic.storestate.StoreStateVO;
/*    */
/*    */public class StoreStateModelDataManager
/*    */implements IAppModelDataManager
/*    */{
	/*    */private BatchBillTableModel model;
	/*    */
	/*    */public void initModel()
	/*    */{
		/*    */try
		/*    */{
			/* 26 */StoreStateVO[] datas = (StoreStateVO[]) (StoreStateVO[]) getModel()
					.getService().queryByDataVisibilitySetting(
							getModel().getContext());
			
					String user=InvocationInfoProxy.getInstance().getUserId();
							
							
//					nc.desktop.ui.WorkbenchEnvironment.getInstance().getLoginUser().get
							
						Object  obj	=getModel().getContext().getInitData();
			/*    */
					IUAPQueryBS iuap = (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
	    			
					String  sql=
							"select  f.value\n" +
									"from sm_individual_property  f\n" + 
									"where  f.pk_user='"+user+"'\n" + 
									"and f.propertyname='org_df_biz'";

					

	    					List<StoreStateVO>list=new ArrayList<StoreStateVO>();
	    			Map map = (Map)iuap.executeQuery(sql, new MapProcessor());
	    				String pk_org= map.get("value")==null?"":map.get("value").toString() ;
	    				for(int i=0;i<datas.length;i++){
	    					String  pk=datas[i].getPk_org();
	    					if(pk!=null&&pk_org!=null&&!pk_org.equals("")&&pk.equals(pk_org)){
	    						list.add(datas[i]);
	    					}
	    				}
	    				if(list.size()>0){
	    					getModel().initModel(list.toArray(new StoreStateVO[list.size()] ));
	    				}else{
	    					getModel().initModel(null);
	    				}
	    				
					
			/* 29 */
			/*    */} catch (Exception e) {
			/* 31 */Logger.error(e.getMessage(), e);
			/*    */}
		/*    */}
	/*    */
	/*    */public BatchBillTableModel getModel() {
		/* 36 */return this.model;
		/*    */}
	/*    */
	/*    */public void setModel(BatchBillTableModel model) {
		/* 40 */this.model = model;
		/*    */}
	/*    */
}