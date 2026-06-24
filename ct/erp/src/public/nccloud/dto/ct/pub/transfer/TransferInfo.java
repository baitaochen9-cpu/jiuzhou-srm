package nccloud.dto.ct.pub.transfer;

/**
 * @description 转单数据结构包含表头id，ts，表体id，ts
 * @author zhangjyp
 * @date 2018-6-2 下午2:05:54
 * @version ncc1.0
 */
public class TransferInfo {
	private String cbilltype;
	private String[] hidts;
	private String[] bidts;

	public String getCbilltype() {
		return cbilltype;
	}

	public void setCbilltype(String cbilltype) {
		this.cbilltype = cbilltype;
	}

	public String[] getHidts() {
		return hidts;
	}

	public void setHidts(String[] hidts) {
		this.hidts = hidts;
	}

	public String[] getBidts() {
		return bidts;
	}

	public void setBidts(String[] bidts) {
		this.bidts = bidts;
	}

}
