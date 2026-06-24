package nc.ui.bd.ref.model;

import java.util.Hashtable;

import nc.bs.bd.baseservice.IBaseServiceConst;
import nc.bs.framework.common.NCLocator;
import nc.bs.sec.esapi.NCESAPI;
import nc.individuation.property.itf.IPropertyService;
import nc.ui.bd.ref.AbstractRefGridTreeModel;
import nc.vo.bd.pub.IPubEnumConst;
import nc.vo.bd.supplier.suppliergradesys.SupStatus;
import nc.vo.bd.supplier.suppliergradesys.SupplierGradeSysVO;
import nc.vo.bd.supplier.suppliergradesys.SupplierGradeVO;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.AppContext;
import nc.vo.util.SqlWhereUtil;

/**
 * 
 * <code>SupplierGradeSysRefModel<code>
 * <strong>供应商等级体系设置等级参照</strong>
 * <p>说明：
 * <li></li>
 * </p>
 * 
 * @since NC6.31
 * @version 2013-9-16 下午2:49:19
 * @author hanbinc
 */

public class SupplierGradeRefModel extends AbstractRefGridTreeModel {

  public SupplierGradeRefModel() {
    this.reset();
  }

  @Override
  public void reset() {
    this.setRootName(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("10140sgrade",
        "010140sgrade-0004"));// "等级体系");
    this.setClassFieldCode(new String[] {SupplierGradeSysVO.CODE, SupplierGradeSysVO.NAME,
        SupplierGradeSysVO.PK_SUPPLIERGRADE});
    this.setClassJoinField(SupplierGradeSysVO.PK_SUPPLIERGRADE);
    this.setClassTableName(SupplierGradeSysVO.getDefaultTableName());
    this.setClassDataPower(true);

    this.setFieldCode(new String[] {SupplierGradeVO.ISDEFAULT, SupplierGradeVO.SUPPLIERGRADE,
        SupplierGradeVO.SUPSTATUS, SupplierGradeVO.GRADEEXP});
    this.setFieldName(new String[] {
        nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("10140sgrade", "010140sgrade-0008"),
        nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("10140sgrade", "010140sgrade-0005"),
        nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("10140sgrade", "010140sgrade-0006"),
        nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("10140sgrade", "010140sgrade-0007")
    // "等级", "状态", "说明", "是否默认"
    });

    this.setRefNameField(SupplierGradeVO.SUPPLIERGRADE);
    this.setRefCodeField(SupplierGradeVO.SUPPLIERGRADE);

    // 默认显示列数
    this.setDefaultFieldCount(4);
    this.setHiddenFieldCode(new String[] {SupplierGradeVO.PK_GRADE_INFO});
    this.setTableName(SupplierGradeVO.getDefaultTableName());

    // 返回主键
    this.setPkFieldCode(SupplierGradeVO.PK_GRADE_INFO);
    this.setDocJoinField(SupplierGradeVO.PK_SUPPLIERGRADE);

    // 特殊处理，对于编码不规则的不分级次的档案。
    this.setCodingRule("0");

    // 枚举值转换为名称
    Hashtable<String, String> content = new Hashtable<String, String>();
    content.put(String.valueOf(SupStatus.QUALIFIED.value()), SupStatus.QUALIFIED.getName());
    content.put(String.valueOf(SupStatus.NOT_QUALIFIED.value()), SupStatus.NOT_QUALIFIED.getName());
    Hashtable<String, Hashtable<String, String>> convert =
        new Hashtable<String, Hashtable<String, String>>();
    convert.put(SupplierGradeVO.SUPSTATUS, content);
    setDispConvertor(convert);

    this.setRefTitle(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("10140sgrade",
        "010140sgrade-0005"));// "等级");
    this.setHistoryVersionRef(false);
    this.resetFieldName();
  }

  @Override
  public String getClassWherePart() {
    SqlWhereUtil sw = new SqlWhereUtil(super.getClassWherePart());
    if (this.isDisabledDataShow()) {
      sw.and(SupplierGradeSysVO.ENABLESTATE + " in (" + IPubEnumConst.ENABLESTATE_ENABLE + ","
          + IPubEnumConst.ENABLESTATE_DISABLE + ") ");
    } else {
      sw.and(SupplierGradeSysVO.ENABLESTATE + " = " + IPubEnumConst.ENABLESTATE_ENABLE);
    }
    String pkUser = AppContext.getInstance().getPkUser();
    String pkGroup = AppContext.getInstance().getPkGroup();//org_df_biz
//    sw.and(IBaseServiceConst.PK_ORG_FIELD + "='" + NCESAPI.clientSqlEncode(getPk_group()) + "'");
  try {
	sw.and(IBaseServiceConst.PK_ORG_FIELD + "='" +NCLocator.getInstance().lookup(IPropertyService.class).queryDefaultDBizOrg(pkUser, pkGroup, "org_df_biz")  + "'");
} catch (BusinessException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}
    return sw.getSQLWhere();
  }
}
