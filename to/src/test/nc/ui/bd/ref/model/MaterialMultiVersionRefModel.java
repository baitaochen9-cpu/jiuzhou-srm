package nc.ui.bd.ref.model;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Vector;

import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.bs.sec.esapi.NCESAPI;
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
import nc.vo.util.BDVisibleUtil;
import nc.vo.util.SqlWhereUtil;
import nc.vo.util.VisibleUtil;

public class MaterialMultiVersionRefModel extends AbstractRefGridTreeBigDataModel implements
    IAssignDataShowRefModel {
  // 是否显示已分配checkbox
  private boolean isShowAssginDataChk = false;

  // 数据是否按已分配过滤
  private boolean isAssginShowData = false;

  private IMaterialAssignService assignService = null;

  private Map<String, String> assginSqlMap = new HashMap<String, String>();

  private boolean isShowDisableData;

  private UserDefRefShow userDefRefShow;

  public MaterialMultiVersionRefModel() {
    this.userDefRefShow =
        UserDefRefShowUtil.getInstance().getUserDefRefShow(IBDMetaDataIDConst.MATERIAL);
    this.reset();
  }

  @Override
  public void filterValueChanged(ValueChangedEvent changedValue) {
    super.filterValueChanged(changedValue);
    String[] pk_orgs = (String[]) changedValue.getNewValue();
    if (ArrayUtils.isNotEmpty(pk_orgs)/* pk_orgs != null && pk_orgs.length > 0 */) {
      this.setPk_org(pk_orgs[0]);
    }
  }

  @Override
  public String getClassWherePart() {
    SqlWhereUtil sw = new SqlWhereUtil(super.getClassWherePart());
    try {
      sw.and(VisibleUtil.getRefVisibleCondition(this.getPk_group(), this.getPk_org(),
          IBDMetaDataIDConst.MARBASCLASS));
      if (this.isDisabledDataShow()) {
        sw.and(MarBasClassVO.ENABLESTATE + " in (" + IPubEnumConst.ENABLESTATE_ENABLE + ","
            + IPubEnumConst.ENABLESTATE_DISABLE + ") ");
      } else {
        sw.and(MarBasClassVO.ENABLESTATE + " = " + IPubEnumConst.ENABLESTATE_ENABLE);
      }
    } catch (BusinessException e) {
      Logger.error(e.getMessage());
    }
    return sw.getSQLWhere();
  }

  @Override
  public String getCodingRule() {
    // 物料基本分类编码规则
    String codeRule = null;
    try {
      codeRule = MarBasClassSysParaUtil.getCodeRuleSysPara(this.getPk_group(), this.getPk_org());
    } catch (BusinessException e) {
      Logger.error(e.getMessage(), e);
    }
    return codeRule;
  }

  @Override
  public void reset() {
    this.setRefNodeName("物料（多版本）");/* -=notranslate=- */// 设置setRefNodeName的目的在于减少userDefRefShow的远程连接数
    this.setRootName(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("10140mclg",
        "110140mclg0002")/* @res "物料基本分类" */);
    this.setClassFieldCode(new String[] {MarBasClassVO.CODE, MarBasClassVO.NAME,
        MarBasClassVO.PK_MARBASCLASS, MarBasClassVO.PK_ORG});
    this.setClassJoinField(MarBasClassVO.PK_MARBASCLASS);
    this.setClassTableName(MarBasClassVO.getDefaultTableName());
    this.setClassDefaultFieldCount(2);
    this.setClassDataPower(true);
    this.setClassWherePart("1 = 1");

    this.setFieldCode(new String[] {MaterialVO.PK_ORG, MaterialVO.CODE, MaterialVO.NAME,
        MaterialVO.VERSION, MaterialVO.MATERIALSPEC, MaterialVO.MATERIALTYPE,
        MaterialVO.MATERIALSHORTNAME, MaterialVO.MATERIALMNECODE, MaterialVO.GRAPHID,
        MaterialVO.PK_MEASDOC, MaterialVO.PK_SOURCE, MaterialVO.MEMO});

    String[] newFieldNames =
        new String[] {
            nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("common", "2UC000-000360") /*
                                                                                              * @res
                                                                                              * "所属组织"
                                                                                              */,
            nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("common", "UC000-0002911")/*
                                                                                             * @res
                                                                                             * "物料编码"
                                                                                             */,
            nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("common", "UC000-0002908") /*
                                                                                              * @res
                                                                                              * "物料名称"
                                                                                              */,
            nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("common", "UC000-0002905")/*
                                                                                             * @res
                                                                                             * "版本号"
                                                                                             */,
            nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("common", "UC000-0003448")/*
                                                                                             * @res
                                                                                             * "规格"
                                                                                             */,
            nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("common", "UC000-0001240") /*
                                                                                              * @res
                                                                                              * "型号"
                                                                                              */,
            nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("10140mag", "2materia-000017")/*
                                                                                                 * @res
                                                                                                 * "物料简称"
                                                                                                 */,
            nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("common", "UC000-0000703") /*
                                                                                              * @res
                                                                                              * "助记码"
                                                                                              */,
            nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("common", "UC000-0001223"),/*
                                                                                              * @res
                                                                                              * "图号"
                                                                                              */
            nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("common", "2UC000-000031"),/*
                                                                                              * @res
                                                                                              * "主单位"
                                                                                              */
            nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("10140mag", "2materia-000400"),/*
                                                                                                  * @res
                                                                                                  * "原始编码"
                                                                                                  */
            nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("common", "2UC000-000258")
        /* @res "备注" */
        };
    this.setFieldName(newFieldNames);

    String orgFomula =
        "getMLCValue(\"" + OrgVO.getDefaultTableName() + "\",\"" + OrgVO.NAME + "\",\""
            + OrgVO.PK_ORG + "\"," + MaterialVO.PK_ORG + ")";
    String mesFomula =
        "getMLCValue(\"" + MeasdocVO.getDefaultTableName() + "\",\"" + MeasdocVO.NAME + "\",\""
            + MeasdocVO.PK_MEASDOC + "\"," + MaterialVO.PK_MEASDOC + ")";
    // 显示原始版本的编码
    String sourceFomula =
        "getMLCValue(\"" + MaterialVO.getDefaultTableName() + "\",\"" + MaterialVO.CODE + "\",\""
            + MaterialVO.PK_MATERIAL + "\"," + MaterialVO.PK_SOURCE + ")";
    this.setFormulas(new String[][] { {MaterialVO.PK_ORG, orgFomula},
        {MaterialVO.PK_MEASDOC, mesFomula}, {MaterialVO.PK_SOURCE, sourceFomula}});
    // 默认显示列数
    this.setDefaultFieldCount(8);
    this.setHiddenFieldCode(new String[] {MaterialVO.PK_MATERIAL, /*
                                                                   * MaterialVO . PK_SOURCE ,
                                                                   */
    MaterialVO.PK_MARBASCLASS});
    this.setTableName(MaterialVO.getDefaultTableName());
    // 返回主键
    this.setPkFieldCode(MaterialVO.PK_MATERIAL);
    this.setDocJoinField(MaterialVO.PK_MARBASCLASS);
    this.setRefCodeField(MaterialVO.CODE);
    this.setRefNameField(MaterialVO.NAME);
    this.setMnecode(new String[] {MaterialVO.MATERIALMNECODE});

    // 使用启用条件
    this.setAddEnableStateWherePart(true);

    // 数据权限
    this.setResourceID(IBDResourceIDConst.MATERIAL_V);
    this.setDataPowerColumn(MaterialVO.PK_SOURCE);

    // 分类数据权限
    this.setClassResouceID(IBDResourceIDConst.MATERIALBASCLASS);

    this.setOrderPart(MaterialVO.CODE + "," + MaterialVO.VERSION);

    // 维护
    this.setRefMaintenanceHandler(new IRefMaintenanceHandler() {

      @Override
      public String[] getFucCodes() {
        return new String[] {"10140MAG", "10140MAO"};
      }

      @Override
      public IRefDocEdit getRefDocEdit() {
        return null;
      }
    });

    // 常用数据
    this.setCommonDataTableName(MaterialVO.getDefaultTableName());

    this.setFilterRefNodeName(new String[] {"业务单元"/* -=notranslate=- */
    });

    this.setVersionDataLabelName(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("10140mag",
        "010140mag0203")/* @res "显示历史版本" */);
    this.setHistoryVersionRef(true);
    // 增加自定义项列
    this.userDefRefShow.addUserDefItemFields(this);
    this.resetFieldName();
    this.assginSqlMap.clear();

    // ------------------------------------------指定全文检索相关的设置信息-------------------------
    // 所有的搜索列及列描述
    Map<String, String> allFields = new LinkedHashMap<String, String>();

    allFields.put(MaterialVO.PK_ORG, newFieldNames[0]/* @res "所属组织" */);
    allFields.put(MaterialVO.CODE, newFieldNames[1]/* @res "物料编码" */);
    allFields.put(MaterialVO.NAME, newFieldNames[2] /* @res "物料名称" */);
    allFields.put(MaterialVO.VERSION, newFieldNames[3]/* @res "版本号" */);
    allFields.put(MaterialVO.MATERIALSPEC, newFieldNames[4]/* @res "规格" */);
    allFields.put(MaterialVO.MATERIALTYPE, newFieldNames[5] /* @res "型号" */);
    allFields.put(MaterialVO.MATERIALSHORTNAME, newFieldNames[6]/*
                                                                 * @res "物料简称"
                                                                 */);
    allFields.put(MaterialVO.MATERIALMNECODE, newFieldNames[7] /*
                                                                * @res "助记码"
                                                                */);
    allFields.put(MaterialVO.GRAPHID, newFieldNames[8]/* @res "图号" */);

    // 黙认的搜索列，按顺序进行搜索
    String[] defaultSearchFields = new String[] {"code", "name"};

    // 需要指定当前参照的nodeName
    RefSearchFieldSetting setting =
        new RefSearchFieldSetting(this.getRefNodeName(), allFields, defaultSearchFields);/*
                                                                                          * -=
                                                                                          * notranslate
                                                                                          * =-
                                                                                          */
    setSearchFieldSetting(setting);

    // -------------------------------------------------------------------------------------------
  }

  @Override
  public java.util.Vector getData() {
    Vector<Vector> v = super.getData();
    this.userDefRefShow.proessorData(v);
    return v;
  }

  @Override
  protected String getEnvWherePart() {
    SqlWhereUtil sw = new SqlWhereUtil();
    String swTemp = new String();
    try {
      sw.and(BDVisibleUtil.getRefVisibleCondition(NCESAPI.clientSqlEncode(this.getPk_group()),
          NCESAPI.clientSqlEncode(this.getPk_org()), IBDMetaDataIDConst.MATERIAL,
          isShowDisableData()));
      swTemp = sw.getSQLWhere();
      if (!this.isHistoryVersionDataShow()) {
        sw.and(MaterialVO.LATEST + " = 'Y' ");
      }
    } catch (BusinessException e) {
      Logger.error(e.getMessage());
    }
    // 修改参照显示历史版本的BUG
    String sqlWhere =
        "(" + sw.getSQLWhere().substring(0, swTemp.length()) + ") "
            + sw.getSQLWhere().substring(swTemp.length(), sw.getSQLWhere().length());
    return sqlWhere;
  }

  @Override
  public boolean isAssginShowData() {
    return this.isAssginShowData;
  }

  @Override
  public void setAssginShowData(boolean isAssginShowData) {
    this.isAssginShowData = isAssginShowData;
  }

  private IMaterialAssignService getAssignService() {
    if (this.assignService == null) {
      this.assignService = NCLocator.getInstance().lookup(IMaterialAssignService.class);
    }
    return this.assignService;
  }

  @Override
  public boolean isShowAssginDataChk() {
    return this.isShowAssginDataChk;
  }

  @Override
  public void setShowAssginDataChk(boolean isShowAssginDataChk) {
    this.isShowAssginDataChk = isShowAssginDataChk;
  }

  public void setShowDisableData(boolean isShowDisableData) {
    this.isShowDisableData = isShowDisableData;
  }

  public boolean isShowDisableData() {
    return isShowDisableData;
  }

}
