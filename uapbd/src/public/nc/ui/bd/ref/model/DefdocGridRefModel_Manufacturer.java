package nc.ui.bd.ref.model;

import nc.bs.framework.common.NCLocator;
import nc.individuation.property.itf.IPropertyService;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.AppContext;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

/**
 * 
 * @author zhian.ye  2022/07/26
 *t生产厂商参照
 *
 *继承了原产品自定义档案 ，重新定义组织获取方式。
 */
public class DefdocGridRefModel_Manufacturer extends DefdocGridRefModel {
    
    @Override
    protected String getEnvWherePart() {
//        return util.getEnvWherePart(this.getPara1(), this.getPk_group(),
//                this.getPk_org());
    	IPropertyService sv= NCLocator.getInstance().lookup(IPropertyService.class);
    	String pkUser = AppContext.getInstance().getPkUser();
    	String queryDefaultDBizOrg = null;
    	try {
			 queryDefaultDBizOrg = sv.queryDefaultDBizOrg(pkUser, this.getPk_group(), "org_df_biz");
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
    	
    	if(queryDefaultDBizOrg.isEmpty()|| queryDefaultDBizOrg.equals("~")){
    		
    		ExceptionUtils.wrappBusinessException("默认登录组织未设置，无法获取过滤条件。");
    	}
    	
        return util.getEnvWherePart(this.getPara1(), this.getPk_group(),
        		queryDefaultDBizOrg);
    }
    
    

    
}
