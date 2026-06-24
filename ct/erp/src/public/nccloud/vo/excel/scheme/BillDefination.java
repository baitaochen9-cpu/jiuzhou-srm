/*
 * Created on 2004-10-25
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package nccloud.vo.excel.scheme;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import nc.vo.pfxx.pub.property.PropertyBeanInfo;
import nccloud.vo.excel.scheme.Record;

/**
 * 单据校验文件定义类。
 * 
 * @author weijj
 * @modify zhaoxub 2011-02-23<br>
 *         1)删除"级次调整类"<code>private String adjustclass</code><br>
 *         2)删除"级次调整参数"<code>private String adjustparam</code><br>
 *         3)删除"单据校验类"<code>private String checkclass</code><br>
 * @modify zhaoxub 2011-03-07<br>
 *         1)增加"全局变量"<code>varsDisplay</code><br>
 *         2)增加"全局变量"<code>vars</code><br>
 *         3)删除<code>public Record getRecordById(String id)</code><br>
 */
@XStreamAlias("ufinterface")
public class BillDefination implements Cloneable, Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 7626806319146176121L;

	/**
	 * 根标签
	 */
	@XStreamAsAttribute()
	@XStreamAlias("roottag")
	@PropertyBeanInfo(catalog = "0pfxx0161"/* 全局信息 */, displayName = "roottag"/* 根标签 */)
	private String rootTag = null;

	/**
	 * 单据备用编号字段<br>
	 * 如果根元素中没有定义ID号，那么在子元素中取docIDName对应的值为文档ID号
	 */
	@XStreamAsAttribute()
	@XStreamAlias("docidname")
	@PropertyBeanInfo(catalog = "0pfxx0161"/* 全局信息 */, displayName = "docidname"/* 单据备用编号字段 */)
	private String docIDName = null;
	
	/**
	 * 单据备用编号字段<br>
	 * 如果根元素中没有定义ID号，那么在子元素中取docIDName对应的值为文档ID号
	 */
	@XStreamAsAttribute()
	@XStreamAlias("multilangtype")
	@PropertyBeanInfo(catalog = "0pfxx0161"/* 全局信息 */, displayName = "multilangtype"/* 多语资源类别 */)
	private String multiLangType = null;
	/**
	 * 注册加密解密类名
	 * 
	 * @date 2007-08-28
	 * @author Larrylau
	 */
	@XStreamAsAttribute()
	@XStreamAlias("cryptClassname")
	@PropertyBeanInfo(catalog = "0pfxx0161"/* 全局信息 */, displayName = "cryptClassname"/* 注册加密解密类名 */)
	private String cryptClassName = null;

	@PropertyBeanInfo(catalog = "0pfxx0161"/* 全局信息 */, displayName = "0pfxx0165"/* 全局变量 */, editorClassname = "nc.ui.pfxx.propertyeditor.VarsRefPanePropertyEditor")
	private String varsDisplay = null;

	private Map<String, String> vars = null;

	@XStreamImplicit
	private List<Record> records = null;

	public String getRootTag() {
		return rootTag;
	}

	public void setRootTag(String rootTag) {
		this.rootTag = rootTag;
	}

	public String getDocIDName() {
		return docIDName;
	}
	
	public void setDocIDName(String docIDName) {
		this.docIDName = docIDName;
	}
	
	public String getMultiLangType() {
		return multiLangType;
	}
	
	public void setMultiLangType(String MultiLangType) {
		this.multiLangType = MultiLangType;
	}
	
	public String getCryptClassName() {
		return cryptClassName;
	}

	public void setCryptClassName(String cryptClassName) {
		this.cryptClassName = cryptClassName;
	}

	public String getVarsDisplay() {
		return varsDisplay;
	}

	public void setVarsDisplay(String varsDisplay) {
		// if (varsDisplay != null) {
		// String[] vars = varsDisplay.split(";");
		// // 如果设定了变量，则先清空原来的变量
		// getVars().clear();
		// for (String var : vars) {
		// String[] varKeyValue = var.split(",");
		// if (varKeyValue != null) {
		// getVars().put(varKeyValue[0], varKeyValue[1]);
		// }
		// }
		// }
		this.varsDisplay = varsDisplay;
	}

	public Map<String, String> getVars() {
		if (vars == null) {
			vars = new HashMap<String, String>();
		}
		return vars;
	}

	public void setVars(Map<String, String> vars) {
		this.vars = vars;
	}

	public String getVarByName(String name) {
		return getVars().get(name);
	}

	public List<Record> getRecords() {
		if (records == null) {
			records = new ArrayList<Record>();
		}
		return records;
	}

	public void setRecords(List<Record> records) {
		this.records = records;
	}

	/**
	 * 通过元数据实体ID取得对应的Record
	 * 
	 * @param metaDataID
	 * @return Record
	 */
	public Record getRecordByMetaDataID(String metaDataID) {
		if (records == null || metaDataID == null)
			return null;

		for (Record record : records) {
			if (metaDataID.equals(record.getMeteDataID()) || metaDataID.equals(record.getName())) {
				return record;
			}
		}
		return null;
	}

	@Override
	public Object clone() {
		try {
			BillDefination cloned = (BillDefination) super.clone();
			if (records != null) {
				List<Record> tmpRecords = new ArrayList<Record>();
				for (Record record : records) {
					tmpRecords.add((Record) record.clone());
				}
				cloned.setRecords(tmpRecords);
			}
			return cloned;
		} catch (CloneNotSupportedException e) {
			return null;
		}
	}

}