/*
 * Created on 2005-9-26
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package nccloud.bs.excel.scheme;


import nc.vo.pub.BusinessException;
import nccloud.vo.excel.scheme.BillDefination;
import nccloud.vo.excel.scheme.Record;

/**
 * @author ljian
 * 
 *         TODO To change the template for this generated type comment go to Window - Preferences - Java - Code Style -
 *         Code Templates
 */
public class BdVOSchemeCreator extends VOSchemeCreator {

	/**
	 * @param billname
	 * @param exsystemcode
	 * @param headVOClassName
	 */
	public BdVOSchemeCreator(String moduleName, String baseClassName) {
		super(moduleName, baseClassName);
	}

	@Override
	public void generateBodyRecords(BillDefination billDefination) {
		// 不实现，单表没有表体
	}

	@Override
	public void generateHeadRecord(BillDefination billDefination) throws BusinessException {
		Record record = new Record();
		record.setName("billhead");
		record.setDisplayName("billhead");
		record.setExTag("billhead");
		record.setType("head");
		record.setMatchTag("header");
		record.setBillDefination(billDefination);
		billDefination.getRecords().add(record);
		appendRecordFields(record, baseClassName);
	}
}
