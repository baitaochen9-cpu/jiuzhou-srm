package nccloud.web.ct.purdaily.info;

/**
 * 到货单转单 缓存数据结构 储存用户选取的表头表体主键和ts 时间戳
 * 
 * @author ligangt
 * @date 2018-6-2
 * @version v1.0
 */
public class TransferData {
	private PkTsParamsVO head;

	private PkTsParamsVO[] bodys;

	public PkTsParamsVO[] getBodys() {
		return bodys;
	}

	public void setBodys(PkTsParamsVO[] bodys) {
		this.bodys = bodys;
	}

	public PkTsParamsVO getHead() {
		return head;
	}

	public void setHead(PkTsParamsVO head) {
		this.head = head;
	}
}
