package nc.ui.bd.ref.model;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Vector;

import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.bs.sec.esapi.NCESAPI;
import nc.individuation.property.itf.IPropertyService;
import nc.itf.bd.material.assign.IMaterialAssignService;
import nc.itf.bd.pub.IBDMetaDataIDConst;
import nc.itf.bd.pub.IBDResourceIDConst;
import nc.itf.uap.bd.ref.IAssignDataShowRefModel;
import nc.ui.bd.ref.AbstractRefGridTreeBigDataModel;
import nc.ui.bd.ref.IRefDocEdit;
import nc.ui.bd.ref.IRefMaintenanceHandler;
import nc.ui.bd.ref.RefSearchFieldSetting;
import nc.ui.pub.beans.ValueChangedEvent;
import nc.vo.bd.material.MaterialVO;
import nc.vo.bd.material.marbasclass.MarBasClassSysParaUtil;
import nc.vo.bd.material.marbasclass.MarBasClassVO;
import nc.vo.bd.material.measdoc.MeasdocVO;
import nc.vo.bd.pub.IPubEnumConst;
import nc.vo.org.OrgVO;
import nc.vo.pfxx.util.ArrayUtils;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.AppContext;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.util.BDVisibleUtil;
import nc.vo.util.SqlWhereUtil;
import nc.vo.util.VisibleUtil;

public class MaterialMultiVersionRefModel_s extends MaterialMultiVersionRefModel {

  public MaterialMultiVersionRefModel_s() {
	  
	  super();
    
  }
  
  private String org = null ;
  public String getOrg() {
	return org;
}
public void setOrg(String org) {
	this.org = org;
}
@Override
	public String getPk_org() {
		
	  if(null == org ){
		  IPropertyService sv= NCLocator.getInstance().lookup(IPropertyService.class);
	    	String pkUser = AppContext.getInstance().getPkUser();
	    	String queryDefaultDBizOrg = null;
	    	try {
				 queryDefaultDBizOrg = sv.queryDefaultDBizOrg(pkUser, this.getPk_group(), "org_df_biz");
				 this.setOrg(queryDefaultDBizOrg);
			} catch (BusinessException e) {
				// TODO Auto-generated catch block
				ExceptionUtils.wrappBusinessException(e.getMessage());
			}
	    	
	    	if(queryDefaultDBizOrg.isEmpty()|| queryDefaultDBizOrg.equals("~")){
	    		
	    		ExceptionUtils.wrappBusinessException("默认登录组织未设置，无法获取过滤条件。");
	    	}
	  }
	  	
		
		return null != this.getOrg() ? this.getOrg() : super.getPk_org();
	}

 
}
