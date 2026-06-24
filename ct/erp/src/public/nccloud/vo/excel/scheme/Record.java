/*
 * Created on 2004-10-25 TODO To change the template for this generated file go
 * to Window - Preferences - Java - Code Style - Code Templates
 */
package nccloud.vo.excel.scheme;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import nc.bs.logging.Logger;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.pfxx.pub.SwapException;
import nc.vo.pfxx.pub.property.PropertyBeanInfo;
import nccloud.vo.excel.pub.ExcelConstants;
import nccloud.vo.excel.scheme.BillDefination;
import nccloud.vo.excel.scheme.Field;
import nccloud.vo.excel.util.VOXMLUtils;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

/**
 * 记录定义类。
 * 
 */
@XStreamAlias("record")
public class Record implements Cloneable, Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 8496278846824733009L;

	@XStreamAsAttribute()
	@XStreamAlias("tablename")
	private String name = null; // 表名称
	
	@XStreamAsAttribute()
	@XStreamAlias("areaname")
	private String areaname = ""; // 单据模板areaname名称
	
	@XStreamAsAttribute()
	@XStreamAlias("aggname")
	private String aggName = ""; // 聚合vo的全类名
	
	public String getAggName() {
		return aggName;
	}

	public void setAggName(String aggName) {
		this.aggName = aggName;
	}

	public String getMarfreeAreacode() {
		return marfreeAreacode;
	}

	public void setMarfreeAreacode(String marfreeAreacode) {
		this.marfreeAreacode = marfreeAreacode;
	}

	@XStreamAsAttribute()
	@XStreamAlias("marfreeareacode")
	private String marfreeAreacode = null; // 物料辅助属性的areacode


	/**
	 * 显示名称
	 */
	@XStreamAsAttribute()
	@XStreamAlias("displayName")
	@PropertyBeanInfo(catalog = "0pfxx0187"/* 表信息 */, displayName = "Record-000000"/* 显示名称 */)
	private String displayName = null;
	
	/**
	 * 校验控制
	 */
	@XStreamAsAttribute()
	@XStreamAlias("controlval")
	@PropertyBeanInfo(catalog = "0pfxx0187"/* 表信息 */, displayName = "Record-000000"/* 显示名称 */)
	private boolean controlval;

	public boolean isControlval() {
		return controlval;
	}

	public void setControlval(boolean controlval) {
		this.controlval = controlval;
	}

	/**
	 * 显示名称ID
	 */
	@XStreamAsAttribute()
	@XStreamAlias("displayNameResid")
	private String displayNameResid = null;
	
	 
	/**
	 * @return the displayNameResid
	 */
	public String getDisplayNameResid()
	{
	
		return displayNameResid;
	}

	/**
	 * @param displayNameResid the displayNameResid to set
	 */
	public void setDisplayNameResid(String displayNameResid)
	{
		this.displayNameResid = displayNameResid;
	}
	
	/**
	 * 前置多语
	 */
	@XStreamAsAttribute()
	@XStreamAlias("prefix")
	private String prefix = null;

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	/**
	 * 元数据实体ID
	 */
	@XStreamAsAttribute()
	@XStreamAlias("metadataid")
	private String meteDataID = null;

	@XStreamAsAttribute()
	@XStreamAlias("tabletype")
	private String type = null;

	@XStreamAsAttribute()
	private String typeName = null;

	@XStreamAsAttribute()
	@XStreamAlias("entrytag")
	// @PropertyBeanInfo(catalog = "表信息", displayName = "表体记录标签")
	private String entrytag = null;

	@XStreamAsAttribute()
	@XStreamAlias("tabletag")
	@PropertyBeanInfo(catalog = "0pfxx0187"/* 表信息 */, displayName = "0pfxx0188"/* 外系统定义的表标签 */)
	private String exTag = null;

	@XStreamAsAttribute()
	@XStreamAlias("stabletag")
	@PropertyBeanInfo(catalog = "0pfxx0187"/* 表信息 */, displayName = "stabletag"/* 转换后标准的表标签 */)
	private String matchTag = null;

	@XStreamAsAttribute()
	@XStreamAlias("recordtype")
	// record的类型:1.Array 2.VO 3.VOS 4.Collection(包含简单类型或复杂类型)
	private String recordtype = null;

	@XStreamAsAttribute()
	@XStreamAlias("classname")
	// 本Record对应的classname
	private String classname = null;

	@XStreamAsAttribute()
	@XStreamAlias("innerrecordtype")
	// 当Record是Collection时需要的参数
	private String innerRecordtype = null; // Collection内部元素数据类型

	@XStreamAsAttribute()
	@XStreamAlias("entityposition")
	@PropertyBeanInfo(catalog = "0pfxx0187"/* 表信息 */, displayName = "entityposition"/* 实体元素在外系统中的位置 */)
	private String entityPosition = null; // 实体元素位置属性，只对于VO数组和集合类型的Record有用

	@XStreamAsAttribute()
	@XStreamAlias("entitytag")
	// ljian++ at 2006-06-22
	private String entityExtag = null;
	
	@XStreamAsAttribute()
	@XStreamAlias("translationrule")
	// wangjwo 
	private String translationrule = "1" ;

	@XStreamAsAttribute()
	@XStreamAlias("sentitytag")
	private String entityMatchedTag = null;

	// FIXME
	// zhaoxub at 2009-12-23
	@XStreamAsAttribute()
	@PropertyBeanInfo(catalog = "0pfxx0187"/* 表信息 */, displayName = "0pfxx0191"/* 外部系统POJO名称 */)
	private String exclassname = null; // 本Record对应的外部类类名

	@XStreamImplicit
	private List<Field> fields = null;

	@XStreamOmitField
	private BillDefination billDefination = null;
	
	
	public String getAreaname() {
		return areaname;
	}

	public void setAreaname(String areaname) {
		this.areaname = areaname;
	}

	public String getExclassname() {
		return exclassname;
	}

	public void setExclassname(String exclassname) {
		this.exclassname = exclassname;
	}

	// FIXME

	public String getMeteDataID() {
		return meteDataID;
	}

	public void setMeteDataID(String meteDataID) {
		this.meteDataID = meteDataID;
	}

	public String getInnerRecordtype() {
		return innerRecordtype;
	}

	public void setInnerRecordtype(String innerRecordtype) {
		this.innerRecordtype = innerRecordtype;
	}

	public String getRecordtype() {
		return recordtype;
	}

	public void setRecordtype(String recordtype) {
		this.recordtype = recordtype;
	}

	public String getClassname() {
		return classname;
	}

	public void setClassname(String classname) {
		this.classname = classname;
	}

	public String getEntrytag() {
		return entrytag;
	}

	public void setEntrytag(String entrytag) {
		this.entrytag = entrytag;
	}

	public String getExTag() {
		return exTag;
	}

	public void setExTag(String id) {
		this.exTag = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		if (IRecordType.HEAD.equals(type)) {
			setTypeName(NCLangRes4VoTransl.getNCLangRes().getStrByID("pfxx", "head")/* 表头 */);
		} else if (IRecordType.BODY.equals(type)) {
			setTypeName(NCLangRes4VoTransl.getNCLangRes().getStrByID("pfxx", "body")/* 表体 */);
		} else if (IRecordType.EMBED.equals(type)) {
			setTypeName(NCLangRes4VoTransl.getNCLangRes().getStrByID("pfxx", "0pfxx0194")/* 内嵌结构 */);
		}
		this.type = type;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public List<Field> getFields() {
		if (fields == null) {
			fields = new ArrayList<Field>();
		}
		return fields;
	}

	public void setFields(List<Field> fields) {
		this.fields = fields;
	}

	public String getMatchTag() {
		return matchTag;
	}

	public void setMatchTag(String matchTag) {
		this.matchTag = matchTag;
	}

	public BillDefination getBillDefination() {
		return billDefination;
	}

	public void setBillDefination(BillDefination billDefination) {
		this.billDefination = billDefination;
	}

	/**
	 * 是否存在指定的字段
	 * 
	 * @param fieldname
	 * @return
	 */
	public boolean hasField(String fieldname) {
		return getFieldByName(fieldname, 0) == null ? false : true;
	}

	/**
	 * 根据字段名(NC里的名称)返回相应的字段
	 * 
	 * @param fieldname
	 * @return
	 */
	private Field getFieldByName(String fieldname, int type) {
		if (fields != null) {
			for (Field field : fields) {
				try {
					String curfieldname = null;
					if (type == 0) {
						curfieldname = VOXMLUtils.getFieldName(field);
					} else // 按外部系统名
					{
						curfieldname = field.getName();
					}
					if (curfieldname.equals(fieldname))
						return field;
				} catch (SwapException e) {
					Logger.error(e.getMessage());
					continue;
				}
			}
		}
		return null;
	}

	/**
	 * 按字段在NC系统的名称查找
	 * 
	 * @param fieldname
	 * @return
	 */
	public Field getFieldByNcName(String fieldname) {
		return getFieldByName(fieldname, 0);
	}

	/**
	 * 按字段在外部系统的名称查找
	 * 
	 * @param fieldname
	 * @return
	 */
	public Field getFieldByExName(String fieldname) {
		return getFieldByName(fieldname, 1);
	}

	/**
	 * 按复杂字段引用的元数据实体ID查找
	 * 
	 * @param fieldname
	 * @return
	 */
	public Field getFieldByRefItem(String metaDataID) {
		if (fields != null) {
			for (Field field : fields) {
				if (metaDataID.equals(field.getRefItem())) {
					return field;
				}
			}
		}
		return null;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEntityPosition() {
		return entityPosition;
	}

	public void setEntityPosition(String entityPosition) {
		this.entityPosition = entityPosition;
		nodePath = new NodePosition(entityPosition);
	}

	/**
	 * 得到字段内联实体元素在外部XML文件中位置
	 * 
	 * @return
	 */
	public List[] getEntityPositionLevel() {
		ArrayList alPositions = new ArrayList();

		String position = getEntityPosition();
		if (position != null) {
			List positions = StringUtil.toList(position, ExcelConstants.PositionBetweenSplitChar);
			for (int i = 0; i < positions.size(); i++) {
				alPositions.add(StringUtil.toList((String) positions.get(i), ExcelConstants.ElementLevelSplitChar));
			}
		}

		return (List[]) alPositions.toArray(new List[0]);
	}

	public String getEntityExtag() {
		return entityExtag;
	}

	public void setEntityExtag(String entityExtag) {
		this.entityExtag = entityExtag;
	}

	public String getEntityMatchedTag() {
		return entityMatchedTag;
	}

	public void setEntityMatchedTag(String entityMatchedTag) {
		this.entityMatchedTag = entityMatchedTag;
	}

	public String getDisplayName() {
		if (!StringUtil.isEmptyWithTrim(getDisplayNameResid()))
		{
			return NCLangRes4VoTransl.getNCLangRes().getStrByID("pfxx", getDisplayNameResid());
		}
		return displayName;
	}

	public void setDisplayName(String displayName) {
		setDisplayNameResid(null);
		this.displayName = displayName;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		Record cloned = (Record) super.clone();
		if (fields != null) {
			List<Field> tmpFields = new ArrayList<Field>();
			for (Field field : fields) {
				tmpFields.add((Field) field.clone());

			}
			cloned.setFields(tmpFields);
		}
		return cloned;
	}

	// ########一下为解决导出问题添加的代码,同时对position进行重构###############

	/** 将路径提取出来 */
	@XStreamOmitField
	private NodePosition nodePath = null;
	/** Record对应的数组/集合是否是由外部枚举值映射而来 */
	@XStreamOmitField
	private boolean isEnumToVos = false;
	/** 导出时未处理的来源路径,仅枚举时有效 */
	private List pathEnums = null;

	public NodePosition getNodePath() {
		return nodePath;
	}

	public boolean isEnumToVos() {
		return isEnumToVos;
	}

	public void setBeEnumToVos(boolean isEnumToVos) {
		this.isEnumToVos = isEnumToVos;
	}

	public List getPathEnums() {
		if (pathEnums == null) {
			pathEnums = new ArrayList();
			// 添加所有来源路径
			if (getNodePath() != null) {
				pathEnums = new ArrayList();
				List paths = getNodePath().getNodePaths();
				for (Iterator iter = paths.iterator(); iter.hasNext();) {
					NodePath npunit = (NodePath) iter.next();
					pathEnums.add(npunit.getEndLevelWithoutCon());
				}
			}
		}
		return pathEnums;
	}
	 
	private Object readResolve()
	{
		if(fields!=null)
		{
			for (Field field : fields)
			{
				if(field!=null) {
					field.setRecord(this);
				}
			}
		}
		return this;
	}

	public String getTranslationrule() {
		return translationrule==null?"1":translationrule;
	}

	public void setTranslationrule(String translationrule) {
		this.translationrule = translationrule;
	}
}
