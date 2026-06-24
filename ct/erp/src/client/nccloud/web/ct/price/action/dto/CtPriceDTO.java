package nccloud.web.ct.price.action.dto;

/**
 * 
 * @description 价格信息表生效失效dto
 * @author zhaoypm
 * @time 2019-3-28 下午5:58:11
 * @since ncc1.0
 */
public class CtPriceDTO {
	private String[] pks;

	private CtPriceHeadDTO[] heads;

	private String pageId;

	public String getPageId() {
		return pageId;
	}

	public void setPageId(String pageId) {
		this.pageId = pageId;
	}

	public String[] getPks() {
		return pks;
	}

	public void setPks(String[] pks) {
		this.pks = pks;
	}

	public CtPriceHeadDTO[] getHeads() {
		return heads;
	}

	public void setHeads(CtPriceHeadDTO[] heads) {
		this.heads = heads;
	}
}
