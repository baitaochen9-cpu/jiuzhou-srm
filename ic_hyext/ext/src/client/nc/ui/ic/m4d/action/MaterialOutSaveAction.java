package nc.ui.ic.m4d.action;

import java.util.Map;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.MapProcessor;
import nc.ui.ic.general.action.GeneralSaveAction;
import nc.ui.pub.beans.MessageDialog;
import nc.vo.ic.general.define.ICBillBodyVO;
import nc.vo.ic.general.define.ICBillVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

/**
 * <p>
 * <b>本类主要完成以下功能：</b>
 * <ul>
 * <li>
 * </ul>
 * <p>
 * <p>
 * 
 * @version 6.0
 * @since 6.0
 * @author chennn
 * @time 2010-6-13 下午01:47:26
 */
@SuppressWarnings("serial")
public class MaterialOutSaveAction extends GeneralSaveAction {
	
  @Override
  protected Object[] processBefore(Object[] vos) {
    try {
    	ICBillVO[] aggvo=(ICBillVO[]) vos;
    	for(int i=0;i<aggvo.length;i++){
    		ICBillBodyVO[] bvo=aggvo[i].getBodys();
    		for(int j=0;j<bvo.length;j++){
    			String marerial=bvo[j].getCmaterialoid();
//    			批次主键
    			String pk_batchcode=bvo[j].getPk_batchcode();
    			IUAPQueryBS iuap = (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
    			String pk_org=aggvo[i].getHead().getPk_org();
//    			失效天数
    					String sql = 
    					"select nvl(a.value,0)   as value " + "\n" +
    							"from  pub_sysinit a " + "\n" + 
    							"where a.initcode ='IC145' and  a.pk_org='"+pk_org+"'";
    			Map map = (Map)iuap.executeQuery(sql, new MapProcessor());
    				int tianshu= Integer.valueOf(map.get("value").toString()) ;
    			
//    		失效时间
    			String sql2 = 
    					"select a.dvalidate " + "\n" +
    							"from  scm_batchcode a " + "\n" + 
    							"where a.pk_batchcode='"+pk_batchcode+"'";
    			Map map2 = (Map)iuap.executeQuery(sql2, new MapProcessor());
    				if (map2!=null&&map2.size()!= 0&&map2.get("dvalidate")!=null&&!"".equals(map2.get("dvalidate"))) {
    					UFDate date= new UFDate( map2.get("dvalidate").toString());	
    					UFDate dbilldate =aggvo[i].getHead().getDbilldate();
        				
        				if(dbilldate.getDateAfter(tianshu).after(date)){
        					MessageDialog.showHintDlg(null,"提示", "到效期大于失效天数");
        					
        				}
    				}
    				
    			
    		}
    		
    	}
    	

    }
    catch (BusinessException ex) {
      ExceptionUtils.wrappException(ex);
    }
    return super.processBefore(vos);
  }

}
