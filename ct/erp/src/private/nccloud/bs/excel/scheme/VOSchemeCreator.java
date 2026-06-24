/*
 * Created on 2005-9-23
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package nccloud.bs.excel.scheme;

import nc.bs.framework.core.util.ObjectCreator;
import nc.vo.pub.BusinessException;
import nccloud.vo.excel.scheme.BillDefination;
import nccloud.vo.excel.scheme.Record;

/**
 * @author ljian
 */
public abstract class VOSchemeCreator implements ISchemeCreator {

	/**
	 * 单据类型的名称
	 */
	protected String baseClassName = null;

	protected Object baseClassVO = null;

	private BillDefination billDefination;

	protected String moduleName;

	public VOSchemeCreator(String moduleName, String baseClassName) {
		this.moduleName = moduleName;
		this.baseClassName = baseClassName;
	}

	@Override
	public BillDefination generate() throws BusinessException {

		billDefination = new BillDefination();
		billDefination.setRootTag("bill");

		baseClassVO = ObjectCreator.newInstance(moduleName, baseClassName);

		// 创建并挂接单据头Record
		generateHeadRecord(billDefination);

		// 创建并挂接单据体Record
		generateBodyRecords(billDefination);

		return billDefination;
	}

	public abstract void generateHeadRecord(BillDefination billDefination) throws BusinessException;

	public abstract void generateBodyRecords(BillDefination billDefination) throws BusinessException;

	/**
	 * 为新创建的记录元素添加字段属性记录
	 * 
	 * @param record
	 * @param voclassname
	 * @throws BusinessException
	 */
	protected void appendRecordFields(Record record, String voclassname) throws BusinessException {
		FieldsAppendor fieldsAppendor = new FieldsAppendor(billDefination, record, voclassname);
		fieldsAppendor.appendRecordFields();
	}

}