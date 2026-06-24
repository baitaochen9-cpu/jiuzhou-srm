package nccloud.web.ct.purdaily.info;

import nccloud.framework.web.ui.pattern.billcard.BillCard;

/**
 * 转单查询前台dto
 * 
 * @author ligangt
 * @date 2018-6-2
 * @version v1.0
 */
public class TransferInfo {

	private BillCard bill;

	private String pagecode;

	private String oid;

	private String key;

	private String queryAreaCode;

	private TransferData[] data;

	public BillCard getBill() {
		return bill;
	}

	public void setBill(BillCard bill) {
		this.bill = bill;
	}

	public String getPagecode() {
		return pagecode;
	}

	public void setPagecode(String pagecode) {
		this.pagecode = pagecode;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getOid() {
		return oid;
	}

	public void setOid(String oid) {
		this.oid = oid;
	}

	public TransferData[] getData() {
		return data;
	}

	public void setData(TransferData[] data) {
		this.data = data;
	}

	public String getQueryAreaCode() {
		return queryAreaCode;
	}

	public void setQueryAreaCode(String queryAreaCode) {
		this.queryAreaCode = queryAreaCode;
	}
}
