package nc.ui.bd.ref.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import nc.bs.logging.Logger;
import nc.itf.bd.pub.IBDMetaDataIDConst;
import nc.mmbd.adapter.uap.UAPAdapter;
import nc.ui.bd.ref.AbstractRefGridTreeModel;
import nc.util.mmf.busi.service.MaterialPubService;
import nc.util.mmf.framework.base.MMValueCheck;
import nc.vo.bd.bom.bom0202.entity.BomVO;
import nc.vo.bd.bom.bom0202.enumeration.BomTypeEnum;
import nc.vo.bd.bom.bom0202.enumeration.FBomBillstatusEnum;
import nc.vo.bd.bom.bom0202.message.MMBDLangConstBom0202;
import nc.vo.bd.material.MaterialVO;
import nc.vo.bd.material.MaterialVersionVO;
import nc.vo.bd.material.marbasclass.MarBasClassVO;
import nc.vo.logging.Debug;
import nc.vo.ml.MultiLangUtil;
import nc.vo.org.OrgVO;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.pubapp.pattern.pub.SqlBuilder;
import nc.vo.util.SqlWhereUtil;
import nc.vo.util.VisibleUtil;

/**
 * <b> 生产BOM参照 </b>
 * <p>
 * 生产BOM参照
 * </p>
 * 创建日期:2010-3-16
 * 
 * @author:zhoujuna
 */
public class BomRefModel2 extends AbstractRefGridTreeModel {

    /**
     * 过滤出指定存在某个子项物料的BOM
     */
    public static final int EXIST_MATERIAL = 1;

    /**
     * 过滤出不存在指定某个子项物料的BOM
     */
    public static final int NOT_EXIST_MATERIAL = 2;

    /**
     * 不对子项物料做过滤，直接显示所有的BOM
     */
    public static final int NO_FILTER_MATERIAL = 3;

    /**
     * 过滤出自由态的BOM
     */
    public static final int FREEDOM_BOM = 4;

    /**
     * 参照名称
     */
    private final String title = nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("1014362_0", "01014362-0057")/*
                                                                                                                     * @res
                                                                                                                     * "生产物料清单"
                                                                                                                     */;

    private boolean iscustomized;

    // /**
    // * 物料版本id
    // */
    // private String materialvid;
    //
    // /**
    // * 过滤条件类型
    // */
    // private int filterType;

    /**
     * 默认构造函数
     */
    public BomRefModel2() {
        super();
        this.reset();
    }

    @Override
    public void reset() {
        this.setRootName(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("1014362_0", "01014362-0054")/*
                                                                                                             * @res
                                                                                                             * "物料基本分类"
                                                                                                             */);
        this.setClassFieldCode(new String[] {
            MarBasClassVO.CODE, MarBasClassVO.NAME, MarBasClassVO.PK_MARBASCLASS, MarBasClassVO.PK_ORG,
            MarBasClassVO.PK_PARENT
        });
        this.setClassJoinField(MarBasClassVO.PK_MARBASCLASS);
        this.setClassTableName("bd_marbasclass");
        this.setClassDefaultFieldCount(2);
        this.setFatherField(MarBasClassVO.PK_PARENT);

        this.setClassWherePart("1 = 1");

        this.setFieldCode(new String[] {
            "bd_bom.pk_org", "bd_material_v.code", "bd_material.name", "bd_material.version",
            BomVO.TABLE_NAME + "." + BomVO.FBOMTYPE, "bd_bom." + BomVO.HVERSION, "bd_material.materialspec",
            "bd_material.materialtype", "bd_material.graphid", "bd_bom." + BomVO.CBOMID,
            "bd_bom." + BomVO.HCMATERIALVID, "bd_bom." + BomVO.HCMATERIALID
        });
        this.setFieldName(new String[] {
            nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("1014362_0", "01014362-0530")/* @res BOM主组织 */,
            nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("common", "UC000-0002911")/* @res "物料编码" */,
            nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("common", "UC000-0002908")/* @res "物料名称" */,
            nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("1014362_0", "01014362-0049")/* @res "物料版本" */,
            nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("1014362_0", "01014362-0247"),
            /* @res "BOM类型" */
            nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("common", "UC000-0000001")/* @res "BOM版本" */,
            nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("common", "UC000-0003448")/* @res "规格" */,
            nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("common", "UC000-0001240")/* @res "型号" */,
            nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("common", "UC000-0001223")/* @res "图号" */,
            nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("1014362_0", "01014362-0055")/* @res "BOM编号" */,
            nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("1014362_0", "01014362-0056")
        /* @res "物料版本主键" */
        });

        this.setHiddenFieldCode(new String[] {
            "bd_material.pk_material", "bd_material.pk_source", "bd_material.pk_marbasclass"
        });

        this.setTableName("bd_material_v inner join bd_bom on  bd_material_v.pk_source = bd_bom." + BomVO.HCMATERIALID  
                + " left join bd_material on " + BomVO.TABLE_NAME + "." + BomVO.HCMATERIALVID + "="
                + MaterialVO.getDefaultTableName() + "." + MaterialVO.PK_MATERIAL);

        this.setPkFieldCode("bd_bom." + BomVO.CBOMID);
        this.setDefaultFieldCount(8);
        this.setDocJoinField("bd_material_v.pk_marbasclass");
        this.setRefCodeField(this.getRefCodeField());
        this.setRefNameField(this.getRefNameField());
        this.setWherePart(" bd_bom.hfversiontype = 1 ");  
        this.resetFieldName();
        Hashtable content = new Hashtable();
        content.put("1", MMBDLangConstBom0202.getREFPRODUCTBOM()

        );// "生产BOM");
        content.put("2", MMBDLangConstBom0202.getREFPACKAGEBOM()

        );// "包装BOM");

        Hashtable convert = new Hashtable();
        convert.put(BomVO.TABLE_NAME + "." + BomVO.FBOMTYPE, content);
        // add by wangdxa 2012-11-30上午11：08
        this.setResourceID("10140BOMM");
        this.setDispConvertor(convert);
    }

    @Override
    public String getClassWherePart() {
        SqlWhereUtil sw = new SqlWhereUtil(super.getClassWherePart());
        try {
            sw.and(VisibleUtil.getRefVisibleCondition(super.getPk_group(), super.getPk_org(),
                    IBDMetaDataIDConst.MARBASCLASS));
        }
        catch (BusinessException e) {
            Logger.error(e.getMessage());
        }
        return sw.getSQLWhere();
    }

    /**
     * 参照名称字段
     * 
     * @return 参照名称
     */
    @Override
    public String getRefNameField() {
        return MaterialVO.getDefaultTableName() + "." + MaterialVO.NAME;
    }

    /**
     * 参照编码字段
     * 
     * @return 编码字段
     */
    @Override
    public String getRefCodeField() {
        return MaterialVersionVO.getDefaultTableName() + "." + MaterialVersionVO.CODE;
    }

    /**
     * 设置过滤条件类型
     * 
     * @param filterType 类型
     * @param materialvid 子项物料版本id
     */
    public void setFilter(int filterType, String materialvid, boolean isFilterFreeStatus) {
        // this.filterType = filterType;
        // this.materialvid = materialvid;

        SqlBuilder buf = new SqlBuilder();
        if (isFilterFreeStatus) {
            buf.append(" bd_bom.fbillstatus=" + FBomBillstatusEnum.FREEDOM.toIntValue());
            buf.append(" and ");
        }
        if (filterType == BomRefModel.EXIST_MATERIAL) {
            buf.append(" bd_bom.cbomid ");
            buf.append(" in ");
            buf.append(" (select cbomid from bd_bom_b where bd_bom_b.cmaterialvid ='" + materialvid + "' and "
                    + this.getVisibleCondition("bd_bom_b") + " and bd_bom_b.dr = 0 )");
        }
        else if (filterType == BomRefModel.NOT_EXIST_MATERIAL) {
            buf.append(" not exists ");
            buf.append(" (select cbomid from bd_bom_b b where b.cbomid = bd_bom.cbomid and bd_bom_b.cmaterialvid ='"
                    + materialvid + "' and " + this.getVisibleCondition("bd_bom_b") + " and bd_bom_b.dr = 0 )");

        }
        else if (filterType == BomRefModel.NO_FILTER_MATERIAL) {
            // 默认过滤类型为NO_FILTER_MATERIAL
            buf.append(this.getVisibleCondition("bd_bom") + " and bd_bom.dr=0");

        }
        SqlBuilder filterFeatureSql = new SqlBuilder();
        if (this.getIscustomized()) {
            filterFeatureSql.append(BomVO.HBCUSTOMIZED + " = 'N'");
            // filterFeatureSql.append(BomVO.HBISFEATURE, UFBoolean.FALSE);
            filterFeatureSql.append(" and ");
            filterFeatureSql.append(BomVO.FBOMTYPE + "!= " + BomTypeEnum.CONFIGBOM.toIntValue());
        }
        if (MMValueCheck.isNotEmpty(filterFeatureSql.toString())) {
            filterFeatureSql.append(" and ");
            filterFeatureSql.append(buf.toString());
            buf = filterFeatureSql;
        }
        this.setWherePart( " bd_bom.hfversiontype = 1 and "+buf.toString());
    }

    /**
     * 根据表名构造可见性过滤条件
     * 
     * @param tableName 表名
     * @return 过滤条件
     */
    private String getVisibleCondition(String tableName) {
        try {
            // 可见性返回
            String whereSql =
                    VisibleUtil.getRefVisibleCondition(this.getPk_group(), this.getPk_org(),
                            IBDMetaDataIDConst.MATERIAL);
            // 加入表前缀
            return this.replaceAllOrg(whereSql, tableName);
        }
        catch (BusinessException e) {
            ExceptionUtils.wrappException(e);
            return null;
        }
    }

    @Override
    public String getOrderPart() {
        return "bd_material_v.code" + "," + "bd_material.version" + "," + BomVO.TABLE_NAME + "." + BomVO.FBOMTYPE + ","
                + BomVO.TABLE_NAME + "." + BomVO.HVERSION;
    }

    /**
     * 加上表名
     * 
     * @param sql
     * @param tableName
     * @return
     */
    private String replaceAllOrg(String scope, String tableName) {
        String cond = scope;
        cond = cond.replaceAll("\\(", " ( ");
        cond = cond.replaceAll(",", " , ");
        cond = cond.replaceAll("pk_org", "pk_org ");
        cond = cond.replaceAll(" pk_org ", " " + tableName + ".pk_org ");

        cond = cond.replaceAll("pk_group", "pk_group ");
        cond = cond.replaceAll(" pk_group ", " " + tableName + ".pk_group ");
        return cond;
    }

    @Override
    public Vector getData() {

        String sql = this.getRefSql();

        Vector v = null;
        if (this.isCacheEnabled()) {
            /** 从缓存读数据 */
            v = this.modelHandler.getFromCache(this.getRefDataCacheKey(), this.getRefCacheSqlKey());
        }

        if (v == null) {

            // 从数据库读--也可以在此定制数据

            try {

                if (this.isFromTempTable()) {
                    v = this.modelHandler.queryRefDataFromTemplateTable(sql);
                }
                else {
                    // 同时读取参照栏目数据
                    v = this.getQueryResultVO();
                }

            }
            catch (Exception e) {
                Debug.debug(e.getMessage(), e);
            }

        }
        this.setEnumDate(v);

        if (v != null && this.isCacheEnabled()) {
            /** 加入到缓存中 */
            this.modelHandler.putToCache(this.getRefDataCacheKey(), this.getRefCacheSqlKey(), v);
        }

        this.m_vecData = v;
        return this.m_vecData;
    }

    // 将枚举值显示出来
    private void setEnumDate(Vector v) {
        if (MMValueCheck.isEmpty(v)) {
            return;
        }
        List<String> cmaterialoidlist = new ArrayList<String>();
        for (int i = 0; i < v.size(); i++) {
            Vector item = (Vector) v.get(i);
            String cmaterilvid = (String) item.get(10);
            String cmateriloid = (String) item.get(11);
            if (cmaterilvid == null) {
                cmaterialoidlist.add(cmateriloid);
            }

        }
        Map<String, String> materialOid2Vid =
                MaterialPubService.queryMaterialLatestVidByOid(cmaterialoidlist.toArray(new String[cmaterialoidlist
                        .size()]));
        Map<String, String> bom2materialvid = new HashMap<String, String>();
        List<String> orgs = new ArrayList<String>();
        Map<String, OrgVO> orgMap = new HashMap<String, OrgVO>();
        for (int i = 0; i < v.size(); i++) {
            Vector item = (Vector) v.get(i);
            String pkorg = (String) item.get(0);
            orgs.add(pkorg);
            String bomid = (String) item.get(9);
            String cmaterilvid = (String) item.get(10);
            String cmateriloid = (String) item.get(11);
            if (cmaterilvid == null) {
                bom2materialvid.put(bomid, materialOid2Vid.get(cmateriloid));
            }
            else {
                bom2materialvid.put(bomid, cmaterilvid);
            }

        }
        // 应该将物料所有的名称都查询出来,不然支持不了多语环境 modify by lijbe
        Map<String, MaterialVO> materialVOMap =
                MaterialPubService.queryMaterialBaseInfoByPks(bom2materialvid.values().toArray(new String[0]),
                        new String[] {
                            MaterialVO.CODE, MaterialVO.NAME, MaterialVO.NAME2, MaterialVO.NAME3, MaterialVO.NAME4,
                            MaterialVO.NAME5, MaterialVO.NAME6
                        });
        try {
            OrgVO[] orgvos = UAPAdapter.getOrgBaseInfos(orgs.toArray(new String[0]));
            for (OrgVO orgVO : orgvos) {
                orgMap.put(orgVO.getPk_org(), orgVO);
            }
        }
        catch (BusinessException e1) {
            ExceptionUtils.wrappException(e1);
        }
        for (int i = 0; i < v.size(); i++) {
            Vector item = (Vector) v.get(i);
            String pkorg = (String) item.get(0);
            item.set(0, MultiLangUtil.getSuperVONameOfCurrentLang(orgMap.get(pkorg), OrgVO.NAME, null));
            String bomid = (String) item.get(9);
            String cmaterilvid = (String) item.get(10);
            if (materialVOMap.containsKey(bom2materialvid.get(bomid))) {
                item.set(2, MultiLangUtil.getSuperVONameOfCurrentLang(materialVOMap.get(bom2materialvid.get(bomid)),
                        MaterialVO.NAME, null));
            }
            if (cmaterilvid == null) {
                item.set(10, bom2materialvid.get(bomid));
            }
        }

    }

    /**
     * 参照标题
     * 
     * @return 参照标题
     */
    @Override
    public String getRefTitle() {
        return this.title;
    }

    public boolean getIscustomized() {
        return this.iscustomized;
    }

    public void setIscustomized(boolean iscustomized) {
        this.iscustomized = iscustomized;
    }

}
