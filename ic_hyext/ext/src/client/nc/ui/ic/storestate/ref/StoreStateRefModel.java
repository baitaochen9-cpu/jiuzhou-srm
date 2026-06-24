package nc.ui.ic.storestate.ref;
/** <a href="http://www.cpupk.com/decompiler">Eclipse Class Decompiler</a> plugin, Copyright (c) 2017 Chen Chao. **/
/*     */
/*     */import java.util.Hashtable;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.individuation.property.itf.IPropertyService;
/*     */
import nc.md.model.IEnumValue;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
/*     */
import nc.ui.bd.ref.AbstractRefModel;
/*     */
import nc.vo.ic.storestate.EInvUsability;
/*     */
import nc.vo.ic.storestate.StoreStateVO;
/*     */
import nc.vo.ml.AbstractNCLangRes;
/*     */
import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.pub.BusinessException;
/*     */
import nc.vo.pub.IAttributeMeta;
/*     */
import nc.vo.pub.IColumnMeta;
/*     */
import nc.vo.pub.ITableMeta;
/*     */
import nc.vo.pub.IVOMeta;
/*     */
import nc.vo.pubapp.pattern.pub.PubAppTool;
/*     */
/*     */public class StoreStateRefModel extends AbstractRefModel
/*     */{
	/*  16 */private String[] m_sFieldCodes = {StoreStateVO.VCODE,
			StoreStateVO.VNAME, StoreStateVO.IUSABILITY, StoreStateVO.VMEMO,
			StoreStateVO.PK_STORESTATE,StoreStateVO.PK_ORG};
	/*     */
	/*  21 */private String[] m_sFieldNames = {
			NCLangRes4VoTransl.getNCLangRes().getStrByID("4008025_0",
					"04008025-0000"),
			NCLangRes4VoTransl.getNCLangRes().getStrByID("4008025_0",
					"04008025-0001"),
			NCLangRes4VoTransl.getNCLangRes().getStrByID("4008025_0",
					"04008025-0002"),
			NCLangRes4VoTransl.getNCLangRes().getStrByID("common",
					"UC000-0003660"),
			NCLangRes4VoTransl.getNCLangRes().getStrByID("4008025_0",
					"04008025-0003"),"公司"};
	/*     */
	/*  25 */private String tableName = null;
	/*     */
	/*     */public StoreStateRefModel(String refNodeName)
	/*     */{
		/*  33 */setRefNodeName(refNodeName);
		/*     */}
	/*     */
	/*     */public int getDefaultFieldCount()
	/*     */{
		/*  41 */return 2;
		/*     */}
	/*     */
	/*     */public String[] getFieldCode()
	/*     */{
		/*  49 */return this.m_sFieldCodes;
		/*     */}
	/*     */
	/*     */public String[] getFieldName()
	/*     */{
		/*  57 */return this.m_sFieldNames;
		/*     */}
	/*     */
	/*     */public String getPkFieldCode()
	/*     */{
		/*  65 */return StoreStateVO.PK_STORESTATE;
		/*     */}
	/*     */
	/*     */public String getRefTitle()
	/*     */{
		/*  73 */return NCLangRes4VoTransl.getNCLangRes().getStrByID(
				"4008025_0", "04008025-0004");
		/*     */}
	/*     */
	/*     */public String getTableName()
	/*     */{
		/*  81 */if (this.tableName == null) {
			/*  82 */this.tableName = new StoreStateVO().getMetaData()
					.getPrimaryAttribute().getColumn().getTable().getName();
			/*     */}
		/*     */
		/*  86 */return this.tableName;
		/*     */}
	/*     */
	/*     */public String getWherePart()
	/*     */{
		/*  94 */String where = super.getWherePart();
		IPropertyService  service = NCLocator.getInstance().lookup(IPropertyService.class);
		String userId = InvocationInfoProxy.getInstance().getUserId();
		String pk_org = "0001V110000000000FH0";
		try {
			pk_org = service.queryDefaultDBizOrg(userId,super.getPk_group(),"org_df_biz");
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException("org_df_biz查询时发生异常,请检查 默认登陆组织设置!");
		}
		/*  95 */if (PubAppTool.isNull(where)) {
					
			/*  96 */return " pk_group='" + super.getPk_group()
					+ "' and pk_org ='"+pk_org+"' and dr = 0  and sealflag = 'N' ";
			/*     */}
		/*     */
		/*  99 */return where + " and pk_group='" + super.getPk_group()
				+ "' and pk_org ='"+pk_org+"'  and dr = 0 and sealflag = 'N' ";
		/*     */}
	/*     */
	/*     */public void setRefNodeName(String refNodeName)
	/*     */{
		/* 104 */this.m_strRefNodeName = refNodeName;
		/*     */
		/* 106 */setFieldCode(this.m_sFieldCodes);
		/* 107 */setFieldName(this.m_sFieldNames);
		/* 108 */setPkFieldCode(StoreStateVO.PK_STORESTATE);
		/* 109 */setRefCodeField(StoreStateVO.VCODE);
		/* 110 */setRefNameField(StoreStateVO.VNAME);
		/* 111 */setTableName(getTableName());
		/*     */
		/* 113 */setDisabledDataShow(true);
		/*     */
		/* 115 */setAddEnableStateWherePart(true);
		/* 116 */resetFieldName();
		/*     */}
	/*     */
	/*     */protected String getDisableDataWherePart(boolean isDisableDataShow)
	/*     */{
		/* 127 */if (isDisableDataShow) {
			/* 128 */return " sealflag in ('Y', 'N') ";
			/*     */}
		/*     */
		/* 131 */return " sealflag = 'N' ";
		/*     */}
	/*     */
	/*     */public Hashtable getDispConvertor()
	/*     */{
		/* 136 */Hashtable conv = new Hashtable();
		/* 137 */Hashtable contents = new Hashtable();
		/* 138 */for (EInvUsability en : (EInvUsability[]) EInvUsability
				.values(EInvUsability.class)) {
			/* 139 */contents.put(en.getEnumValue().getValue(), en
					.getEnumValue().getName());
			/*     */}
		/* 141 */conv.put(StoreStateVO.IUSABILITY, contents);
		/* 142 */return conv;
		/*     */}
	/*     */
}