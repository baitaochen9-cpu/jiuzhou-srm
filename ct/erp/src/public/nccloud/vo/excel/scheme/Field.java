/*
 * Created on 2004-10-25
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package nccloud.vo.excel.scheme;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import nc.vo.jcom.lang.StringUtil;
import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.pfxx.pub.SwapException;
import nc.vo.pfxx.pub.property.PropertyBeanInfo;
import nccloud.vo.excel.pub.ExcelConstants;
import nccloud.vo.excel.scheme.Record;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

/**
 * 字段定义类。
 * 
 * @author weijj
 */
@XStreamAlias("field")
public class Field implements Cloneable, Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 2101048464752822957L;

	@XStreamAlias("matchtag")
	@PropertyBeanInfo(catalog = "Field-000000"/* 基本信息 */, displayName = "0pfxx0166"/* 该字段在NC的名称 */)
	private String matchTag = null;

	@XStreamAlias("name")
	@PropertyBeanInfo(catalog = "Field-000000"/* 基本信息 */, displayName = "name"/* 该字段在外系统中的名称 */)
	private String name = null;

	@XStreamAsAttribute()
	@XStreamAlias("desc")
	@PropertyBeanInfo(catalog = "Field-000000"/* 基本信息 */, displayName = "desc"/* 字段描述 */)
	private String desc = null;
	
	/**
	 *必须 可见
	 */
	@XStreamAsAttribute()
	@XStreamAlias("visual")
	@XStreamConverter(BooleanConverter.class)
	@PropertyBeanInfo(catalog = "Field-000000"/* 基本信息 */, displayName = "desc"/* visual*/)
	private boolean visual ;
	
	public boolean isVisual() {
		return visual;
	}

	public void setVisual(boolean visual) {
		this.visual = visual;
	}
	
	/**
	 *必须 可见
	 */
	@XStreamAsAttribute()
	@XStreamAlias("set")
	@XStreamConverter(BooleanConverter.class)
	@PropertyBeanInfo(catalog = "Field-000000"/* 基本信息 */, displayName = "desc"/* iset*/)
	private boolean set ;


	public boolean isSet() {
		return set;
	}

	public void setSet(boolean set) {
		this.set = set;
	}

	/**
	 *必须输入
	 */
	@XStreamAsAttribute()
	@XStreamAlias("isRequired")
	@XStreamConverter(BooleanConverter.class)
	@PropertyBeanInfo(catalog = "Field-000000"/* 基本信息 */, displayName = "desc"/* isRequired*/)
	private boolean isRequired ;
	
	

	public boolean isRequired() {
		return isRequired;
	}

	public void setRequired(boolean isRequired) {
		this.isRequired = isRequired;
	}


	@XStreamAsAttribute()
	@XStreamAlias("descresid")
	private String descResid = null;
	
	/**
	 * @return the descResid
	 */
	public String getDescResid()
	{
		return descResid;
	}

	/**
	 * @param descResid the descResid to set
	 */
	public void setDescResid(String descResid)
	{
		this.descResid = descResid;
	}

	@XStreamAlias("type")
	@PropertyBeanInfo(catalog = "Field-000000"/* 基本信息 */, displayName = "type"/* 数据类型 */, editorClassname = "nc.ui.pfxx.propertyeditor.DataTypeComboBoxPropertyEditor")
	private String type = null;

	@XStreamAlias("nullallowed")
	@XStreamConverter(BooleanConverter.class)
	@PropertyBeanInfo(catalog = "Field-000000"/* 基本信息 */, displayName = "0pfxx0169"/* 允许为空 */)
	private boolean nullAllowed = true;
	
	@XStreamAlias("translatorrequired")
	@XStreamConverter(BooleanConverter.class)
	@PropertyBeanInfo(catalog = "Field-000000"/* 基本信息 */, displayName = "0pfxx0169"/* 允许为空 */)
	private boolean translatorrequired = false;

	@XStreamAlias("maxLength")
	@PropertyBeanInfo(catalog = "Field-000000"/* 基本信息 */, displayName = "0pfxx0170"/* 最大长度 */)
	private int maxLength = 0;
	
	@XStreamAlias("precision")
	@PropertyBeanInfo(catalog = "Field-000000"/* 基本信息 */, displayName = "precision"/* 精度 */)
	private int precision = 0;

	public int getPrecision() {
		return precision;
	}

	public void setPrecision(int precision) {
		this.precision = precision;
	}
	
	@XStreamAlias("translationrule")
	@PropertyBeanInfo(catalog = "Field-000000"/* 基本信息 */, displayName = "translationrule"/* 精度 */)
	private String translationrule = "-1";
	

	@XStreamAlias("defaultvalue")
	@PropertyBeanInfo(catalog = "Field-000000"/* 基本信息 */, displayName = "defaultvalue"/* 默认值 */)
	private String defaultValue = null;

	@XStreamAlias("needexport")
	@XStreamConverter(BooleanConverter.class)
	@PropertyBeanInfo(catalog = "Field-000000"/* 基本信息 */, displayName = "needexport"/* 是否需要导出 */)
	private boolean needexport = true;
	
	@XStreamAlias("exportnbymd")
	@XStreamConverter(BooleanConverter.class)
	@PropertyBeanInfo(catalog = "Field-000000"/* 基本信息 */, displayName = "exportnbymd"/* 是否需要导出 */)
	private boolean exportnbymd = false;

	@XStreamAlias("position")
	@PropertyBeanInfo(catalog = "Field-000000"/* 基本信息 */, displayName = "position"/* 该字段在外系统中的位置 */)
	private String position = null; // 字段在XML文件中的位置(外围元素)

	@XStreamAlias("basdoc")
	@PropertyBeanInfo(catalog = "0pfxx0174"/* 翻译信息 */, displayName = "0pfxx0175"/* 参照的元数据实体 */, editorClassname = "nc.ui.pfxx.propertyeditor.MetaDataRefPanePropertyEditor")
	private String baseDocMetaDataID = null;// Add by zhaoxub 2011-01-28

	@XStreamAlias("translator")
	@PropertyBeanInfo(catalog = "0pfxx0174"/* 翻译信息 */, displayName = "0pfxx0057"/* 自定义翻译器 */, editorClassname = "nc.ui.pfxx.propertyeditor.TranslatorRefPanePropertyEditor")
	private String translator = null;

	@PropertyBeanInfo(catalog = "0pfxx0174"/* 翻译信息 */, displayName = "0pfxx0176"/* 自定义翻译器变量参数列表（使用';'分隔） */)
	private String translatorParams = null;

	@XStreamAlias("checkTranslatedPK")
	@XStreamConverter(BooleanConverter.class)
	@PropertyBeanInfo(catalog = "0pfxx0174"/* 翻译信息 */, displayName = "0pfxx0224"/* 校验翻译后的PK是否存在 */)
	private boolean checkTranslatedPK = false;// Add by zhaoxub 2011-11-10 判断是否需要校验翻译后的pk在NC系统中的存在性

	@PropertyBeanInfo(catalog = "0pfxx0174"/* 翻译信息 */, displayName = "0pfxx0177"/* 基础数据对照依赖的组织变量名称 */)
	private String bdconstraOrgParam = null;

	@XStreamAlias("enumid")
	@PropertyBeanInfo(catalog = "0pfxx0174"/* 翻译信息 */, displayName = "UPPpfxxV6-000068"/* 枚举类型 */, editorClassname = "nc.ui.pfxx.propertyeditor.EnumRefPanePropertyEditor")
	private String enumMetaDataID = null;// Add by zhaoxub 2011-05-25

	@XStreamAlias("rule")
	@PropertyBeanInfo(catalog = "0pfxx0174"/* 翻译信息 */, displayName = "0pfxx0179"/* 简单对照规则 */, editorClassname = "nc.ui.pfxx.propertyeditor.SimpleContrastRuleRefPanePropertyEditor")
	private String rule = null; // 用于简单数据对照
	
	@XStreamAlias("translatorProperty")
	@PropertyBeanInfo(catalog = "0pfxx0174"/* 翻译信息 */, displayName = "0pfxx0179"/* 简单对照规则 */, editorClassname = "nc.ui.pfxx.propertyeditor.SimpleContrastRuleRefPanePropertyEditor")
	private String translatorProperty = null; // 用于简单数据对照

	public String getTranslatorProperty() {
		return translatorProperty;
	}

	public void setTranslatorProperty(String translatorProperty) {
		this.translatorProperty = translatorProperty;
	}

	@XStreamAlias("variablename")
	@PropertyBeanInfo(catalog = "0pfxx0174"/* 翻译信息 */, displayName = "0pfxx0180"/* 变量名称定义 */)
	private String variableName = null;

	// Add by cch 2004-11-29 Begin
	@XStreamAlias("formulain")
	@PropertyBeanInfo(catalog = "0pfxx0181"/* 数据修正 */, displayName = "formulain"/* 导入公式 */, editorClassname = "nc.ui.pfxx.propertyeditor.FormulaRefPanePropertyEditor")
	private String formulaIn = null;

	@XStreamAlias("formulaout")
	@PropertyBeanInfo(catalog = "0pfxx0181"/* 数据修正 */, displayName = "0pfxx0183"/* 导出公式 */, editorClassname = "nc.ui.pfxx.propertyeditor.FormulaRefPanePropertyEditor")
	private String formulaOut = null;

	@XStreamAlias("refitem")
	private String refItem = null; // 参照Item

	/**
	 * 所属的Record
	 */
	@XStreamOmitField
	private Record record = null;
	/**
	 * 多语资源类型，解析单据定义文件时传入，不在xml中显示。
	 */
	@XStreamOmitField
	private String multiLangType;
	/**
	 * 返回多语资源类型
	 * multiLangType=null固定返回"pfxx"
	 * @return multiLangType
	 */
	
    private boolean visiable = true;
    
	public String getMultiLangType(){
		if(StringUtil.isEmptyWithTrim(multiLangType))
			return "pfxx";
		return multiLangType;
	}
	/**
	 * 设置，在解析单据定义文件时根据单据定义文件读取然后设置。
	 * @param multilangtype
	 */
	public void setMultiLangType(String multilangtype){
		this.multiLangType = multilangtype;
	}
	
	public String getBaseDocMetaDataID() {
		return baseDocMetaDataID;
	}

	public void setBaseDocMetaDataID(String baseDocMetaDataID) {
		this.baseDocMetaDataID = baseDocMetaDataID;
	}

	public String getEnumMetaDataID() {
		return enumMetaDataID;
	}

	public void setEnumMetaDataID(String enumMetaDataID) {
		this.enumMetaDataID = enumMetaDataID;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public boolean isNeedexport() {
		return needexport;
	}

	public void setNeedexport(boolean needexport) {
		this.needexport = needexport;
	}

	public String getFormulaIn() {
		return formulaIn;
	}

	public void setFormulaIn(String formulaIn) {
		this.formulaIn = formulaIn;
	}

	public String getFormulaOut() {
		return formulaOut;
	}

	public void setFormulaOut(String formulaOut) {
		this.formulaOut = formulaOut;
	}

	public String getRefItem() {
		return refItem;
	}

	public void setRefItem(String refItem) {
		this.refItem = refItem;
	}

	public String getDefaultValue() {
		if (defaultValue != null)
			return defaultValue.trim();
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	// Add by cch 2004-11-29 End
	public String getDesc() {
		//
		if (!StringUtil.isEmptyWithTrim(getDescResid())&&getRecord()!=null&&!StringUtil.isEmptyWithTrim(getRecord().getDisplayNameResid()))
		{
			return NCLangRes4VoTransl.getNCLangRes().getStrByID(getRecord().getDisplayNameResid(), getDescResid());
		}
		return desc;
	}

	public void setDesc(String desc) {
		setDescResid(null);
		this.desc = desc;
	}

	public String getMatchTag() {
		return matchTag;
	}

	public void setMatchTag(String matchTag) {
		this.matchTag = matchTag;
	}

	public int getMaxLength() {
		return maxLength;
	}

	public void setMaxLength(int maxLength) {
		this.maxLength = maxLength;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isNullAllowed() {
		return nullAllowed;
	}

	public void setNullAllowed(boolean nullAllowed) {
		this.nullAllowed = nullAllowed;
	}

	public String getRule() {
		return rule;
	}

	public void setRule(String rule) {
		this.rule = rule;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTranslator() {
		return translator;
	}

	public void setTranslator(String translator) {
		this.translator = translator;
	}

	public String getVariableName() {
		return variableName;
	}

	public void setVariableName(String variableName) {
		this.variableName = variableName;
	}

	public String getTranslatorParams() {
		return translatorParams;
	}

	public void setTranslatorParams(String translatorParams) {
		this.translatorParams = translatorParams;
	}

	public boolean isCheckTranslatedPK() { 
		return checkTranslatedPK;
	}

	public void setCheckTranslatedPK(boolean checkTranslatedPK) {
		this.checkTranslatedPK = checkTranslatedPK;
	}

	public String getBdconstraOrgParam() {
		return bdconstraOrgParam;
	}

	public void setBdconstraOrgParam(String bdconstraOrgParam) {
		this.bdconstraOrgParam = bdconstraOrgParam;
	}

	public Record getRecord() {
		return record;
	}

	public void setRecord(Record record) {
		this.record = record;
	}

	/**
	 * 取得负责字段的结构定义
	 * 
	 * @param field
	 * @return
	 */
	public String getRefFieldItem() throws SwapException {
		String item = getRefItem();
		if (item == null || item.trim().length() == 0) {
			String type = getType().trim();
			item = type.substring(type.indexOf("(") + 1, type.lastIndexOf(")"));
		}
		if (item == null || item.length() == 0) {
			throw new SwapException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("pfxx", "UPPpfxx-000277")/*
																													 * @res
																													 * "复杂字段的refItem不能为空!字段名称:"
																													 */
					+ getName());
		}
		return item;
	}

	/**
	 * 得到字段在外部XML文件中的绝对位置
	 * 
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List[] getPositionLevel() {
		List<List<String>> alPositions = new ArrayList<List<String>>();
		String position = getPosition();
		if (position != null) {
			List<String> positions = StringUtil.toList(position, ExcelConstants.PositionBetweenSplitChar);
			for (int i = 0; i < positions.size(); i++) {
				alPositions.add(StringUtil.toList(positions.get(i), ExcelConstants.ElementLevelSplitChar));
			}
		}

		return alPositions.toArray(new List[0]);
	}

	/**
	 * 返回字段注释，例如<!--存货分类编码,不能为空-->
	 * 
	 * @return
	 */
	public String getCommentDesc() {
		StringBuffer buf = new StringBuffer();
		String displayName = getDesc();
		if (StringUtil.isEmpty(displayName)) {
			displayName = getName();
		}
		buf.append(displayName);
		buf.append(",");
		if (!isNullAllowed()) {
			buf.append(NCLangRes4VoTransl.getNCLangRes().getStrByID("pfxx", "0pfxx0184")/* 不能为空, */);
		}
		buf.append(NCLangRes4VoTransl.getNCLangRes().getStrByID("pfxx", "Field-000001", null,
				new String[] { String.valueOf(getMaxLength()) })/* 最大长度为{0} */);
		buf.append(NCLangRes4VoTransl.getNCLangRes().getStrByID("pfxx", "Field-000002", null,
				new String[] { getType() })/* ,类型为:{0} */);

		return buf.toString();
	}

	/**
	 * 是否是变量
	 * 
	 * @return
	 */
	public boolean isVariable() {
		return !StringUtil.isEmpty(getVariableName());
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	public String getTranslationrule() {
		return translationrule==null?"-1":translationrule;
	}

	public void setTranslationrule(String translationrule) {
		this.translationrule = translationrule;
	}

	public boolean isVisiable() {
		return visiable;
	}

	public void setVisiable(boolean visiable) {
		this.visiable = visiable;
	}

	public boolean isExportnbymd() {
		return exportnbymd;
	}

	public void setExportnbymd(boolean exportnbymd) {
		this.exportnbymd = exportnbymd;
	}

	public boolean isTranslatorrequired() {
		return translatorrequired;
	}

	public void setTranslatorrequired(boolean translatorrequired) {
		this.translatorrequired = translatorrequired;
	}

}
