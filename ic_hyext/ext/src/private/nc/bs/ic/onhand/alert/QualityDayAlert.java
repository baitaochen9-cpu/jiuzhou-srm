package nc.bs.ic.onhand.alert;

import java.util.ArrayList;
import java.util.List;

import nc.bs.ic.alert.ICAlertUtil;
import nc.bs.ic.pub.alert.AlertConst;
import nc.bs.ic.pub.db.ICDBVisitor;
import nc.bs.ic.pub.db.SqlIn;
import nc.bs.pub.pa.PreAlertContext;
import nc.bs.pub.pa.PreAlertObject;
import nc.bs.pub.pa.PreAlertReturnType;
import nc.itf.bd.pub.IBDMetaDataIDConst;
import nc.pubitf.bd.accessor.GeneralAccessorFactory;
import nc.pubitf.bd.accessor.IGeneralAccessor;
import nc.vo.bd.accessor.IBDData;
import nc.vo.bd.material.marbasclass.MarBasClassVO;
import nc.vo.ic.onhand.entity.OnhandDimVO;
import nc.vo.ic.pub.sql.AttributeResultSet;
import nc.vo.ic.pub.util.StringUtil;
import nc.vo.ic.pub.util.ValueCheckUtil;

/**
 * 保质期预警功能类
 * 
 * @since 6.0
 * @version 2011-2-23 下午04:03:47
 * @author chennn
 */

public abstract class QualityDayAlert {

  public PreAlertObject alert(PreAlertContext context) {
    QualityDayView qualityview = this.getQualityView(context);
    String onhandWhere = this.getOnhandWhere(context);
    String qualityWhere = this.getQualityWhere(context);
    qualityview.setOnhandWhere(onhandWhere);
    qualityview.setQualityWhere(qualityWhere);
    qualityview.initView();
    AttributeResultSet attributeSet =
        new ICDBVisitor().query(qualityview, null);
    return this.getReturnObject(attributeSet);
  }

  /**
   * 根据物料基本分类PK查询该分类所有下级物料基本分类PK
   * 
   * @param pk_docs
   */
  private String[] fetchMaterilClass(String pk_org, String[] pk_docs) {
    IGeneralAccessor ass =
        GeneralAccessorFactory.getAccessor(IBDMetaDataIDConst.MARBASCLASS);
    List<IBDData> bdDataList = new ArrayList<IBDData>();
    for (String pk_doc : pk_docs) {
      bdDataList.addAll(ass.getChildDocs(pk_org, pk_doc, true));
    }
    if (ValueCheckUtil.isNullORZeroLength(bdDataList)) {
      return pk_docs;
    }
    List<String> pkList = new ArrayList<String>();
    for (IBDData bdData : bdDataList) {
      pkList.add(bdData.getPk());
    }
    return pkList.toArray(new String[pkList.size()]);
  }

  private String getInvClassWhere(PreAlertContext context) {
    // 物料分类
    final String materialClassTable = new MarBasClassVO().getTableName();
    String invclass = (String) context.getKeyMap().get(AlertConst.INVCLASS);
    // 组织单元
    // String pk_org = context.getPk_org();
    // by:zhaofeid, at:2012-12-03
    // 预警适配组织参数调整
    String pk_org = ICAlertUtil.getSinglePkOrgFromContext(context);
    if (StringUtil.isSEmptyOrNull(invclass)) {
      return " 1 = 1 ";
    }
    String[] invcls = this.fetchMaterilClass(pk_org, invclass.split(","));
    return materialClassTable + "." + MarBasClassVO.PK_MARBASCLASS + " in "
        + this.lrTrim(SqlIn.formInValue(invcls));
  }

  private String getOrgWhere(PreAlertContext context) {
    // String pk_org = (String) context.getKeyMap().get(AlertConst.PK_CALBODY)
    // by:zhaofeid, at:2012-12-03
    // 预警适配组织参数调整
    String pk_org = ICAlertUtil.getSinglePkOrgFromContext(context);
    return OnhandDimVO.ALIASNAME + "." + OnhandDimVO.PK_ORG + " = '" + pk_org
        + "'";
  }

  private String lrTrim(String str) {
    int start = 0;
    int end = str.length() - 1;
    char[] val = str.toCharArray();
    while (start < end && val[start] == ' ') {
      start++;
    }
    while (start < end && val[end] == ' ') {
      end--;
    }
    return str.substring(start, end + 1);
  }

  protected String getOnhandWhere(PreAlertContext context) {
    return this.getOrgWhere(context);
  }

  protected abstract QualityDayView getQualityView(PreAlertContext context);

  protected String getQualityWhere(PreAlertContext context) {
    return this.getInvClassWhere(context);
  }

  protected PreAlertObject getReturnObject(AttributeResultSet attributeSet) {
    PreAlertObject retObj = new PreAlertObject();
    QualityDataSource ds = new QualityDataSource(attributeSet);
    if (null == attributeSet || attributeSet.getRowCount() <= 0) {
      retObj.setReturnType(PreAlertReturnType.RETURNNOTHING);
    }
    else {
      retObj.setReturnType(PreAlertReturnType.RETURNDATASOURCE);
    }
    retObj.setReturnObj(ds);
    return retObj;
  }

}
