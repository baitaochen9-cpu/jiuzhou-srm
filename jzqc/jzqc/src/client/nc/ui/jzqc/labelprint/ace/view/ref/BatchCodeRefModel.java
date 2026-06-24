package nc.ui.jzqc.labelprint.ace.view.ref;

import nc.ms.tb.pub.NtbBSLangRes;

//import nc.vo.fi.pub.*;
/**
 * 基础档案表参照模型。 创建日期：(04-02-20 19:57:15)
 * 
 * @author：李伟
 */
public class BatchCodeRefModel extends nc.ui.bd.ref.AbstractRefModel {

	/**
	 * UserDefRefModel 构造子注解。
	 */
	public BatchCodeRefModel() {
		super();
		// setHiddenFieldCode(new String[] { "pk_batchcode" });
	}

	/**
	 * getDefaultFieldCount 方法注解。
	 */
	public int getDefaultFieldCount() {
		return 4;
	}

	/**
	 * getFieldCode 方法注解。
	 */
	public java.lang.String[] getFieldCode() {
		return new String[] { "vbatchcode", "vvendbatchcode", "dproducedate",
				"dvalidate", "pk_batchcode" };
	}

	/**
	 * getFieldName 方法注解。
	 */
	public java.lang.String[] getFieldName() {
		return new String[] { "批次号", "供应商批次号", "生产日期", "复测日期", "主键" };
	}

	/**
	 * 参照数据库表或者视图名 创建日期：(01-4-4 0:57:23)
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getPkFieldCode() {
		return "pk_batchcode";
	}

	/**
	 * getRefTitle 方法注解。
	 */
	public String getRefTitle() {
		return NtbBSLangRes.getInstance().getStrByID("批次号参照");
	}

	/**
	 * getTableName 方法注解。
	 */
	public String getTableName() {
		return "scm_batchcode";
	}

	/**
	 * 此处插入方法说明。 创建日期：(01-4-18 20:05:25)
	 * 
	 * @return java.lang.String
	 */
	public String getWherePart() {
		return "1=1";
	}
}
