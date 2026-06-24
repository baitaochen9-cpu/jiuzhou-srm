package nc.ui.qc.supplierqualitystatus.action;

import nc.ui.pubapp.uif2app.query2.IQueryConditionDLGInitializer;
import nc.ui.pubapp.uif2app.query2.QueryConditionDLGDelegator;
import nc.ui.pubapp.uif2app.query2.util.QueryDlgUtils;
import nc.ui.scmpub.query.refregion.QMarterialoidFilter;
import nc.vo.pub.lang.UFBoolean;

/**
 * @since 6.0
 * @version 2011-3-6 下午03:39:11
 * @author wangxhi
 */
@SuppressWarnings("restriction")
public class SupplierStatusQueryDlgWrapper implements IQueryConditionDLGInitializer {

  

  @Override
  public void initQueryConditionDLG(QueryConditionDLGDelegator condDLGDelegator) {
//    this.setDefaultValue(condDLGDelegator);
    this.initRefFilter(condDLGDelegator);
  }

  /**
   * 设置默认值
   * 
   * @param condDLGDelegator
   */
private void setDefaultValue(QueryConditionDLGDelegator condDLGDelegator) {
	    String corp = QueryDlgUtils.getDefaultOrgUnit();
    condDLGDelegator.setDefaultValue("pk_org", corp);
    

  }

  /**
   * 初始化查询条件参照过滤
   * 
   * @param delegator
   */
  private void initRefFilter(QueryConditionDLGDelegator delegator) {
	  
	  String corp = QueryDlgUtils.getDefaultOrgUnit();
      String orgCode = "pk_org";
//    // 过滤物料
//    ICQueryRefFilterUtil.filterMaterial(delegator,
//       "pk_material", pk_org);
    
	QMarterialoidFilter materialFilter = new QMarterialoidFilter(
			delegator, orgCode,  "pk_material");
	materialFilter.setbDiscount(UFBoolean.FALSE);
	materialFilter.setbFee(UFBoolean.FALSE);
	materialFilter.filter();
	materialFilter.addEditorListener();
  
 

  }



}
