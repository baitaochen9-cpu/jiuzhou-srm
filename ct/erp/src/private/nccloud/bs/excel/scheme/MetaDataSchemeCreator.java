package nccloud.bs.excel.scheme;

import nc.md.model.IBean;
import nc.vo.pfxx.util.PfxxUtils;
import nc.vo.pub.BusinessException;
import nccloud.vo.excel.scheme.BillDefination;
import nccloud.vo.excel.scheme.Record;

public class MetaDataSchemeCreator implements ISchemeCreator {

	private BillDefination billDefination;
	private String billMetaDataId;

	public MetaDataSchemeCreator(String billMetaDataId) {
		this.billMetaDataId = billMetaDataId;
	}

	@Override
	public BillDefination generate() throws BusinessException {
		IBean metaDataBean = PfxxUtils.getMetaDataBeanByID(billMetaDataId);
		billDefination = new BillDefination();
		billDefination.setRootTag("bill");

		// 创建并挂接单据头Record
		generateRecord(metaDataBean);

		return billDefination;
	}

	/**
	 * 创建并挂接单据Record
	 * 
	 * @param metaDataBean
	 * @throws BusinessException
	 */
	private void generateRecord(IBean metaDataBean) throws BusinessException {
		Record record = new Record();
		record.setName("billhead");
		record.setExTag("billhead");
		record.setType("head");
		record.setMatchTag("header");
		record.setBillDefination(billDefination);
		billDefination.getRecords().add(record);

		// 为新创建的记录元素添加字段属性记录
		MetaDataFieldsAppendor fieldsAppendor = new MetaDataFieldsAppendor(billDefination, record, metaDataBean);
		fieldsAppendor.appendRecordFields();
	}

}
