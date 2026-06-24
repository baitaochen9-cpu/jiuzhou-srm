package nccloud.web.ct.price.action.dto;

/**
 * 
 * @description pageInfo中查询卡片数据用的dto
 * @author zhaoypm
 * @time 2019-3-22 下午1:26:49
 * @since ncc1.0
 */
public class QueryInfo {
	private String[] pks;
	private String pageId;
	private String pk_org;
	private String[] pk_oid;
	private String areacode;

	public String[] getPk_oid() {
		return pk_oid;
	}

	public void setPk_oid(String[] pk_oid) {
		this.pk_oid = pk_oid;
	}

	public String getAreacode() {
		return areacode;
	}

	public void setAreacode(String areacode) {
		this.areacode = areacode;
	}

	public String getPk_org() {
		return pk_org;
	}

	public void setPk_org(String pk_org) {
		this.pk_org = pk_org;
	}

	public String[] getPks() {
		return pks;
	}

	public void setPks(String[] pks) {
		this.pks = pks;
	}

	public String getPageId() {
		return pageId;
	}

	public void setPageId(String pageId) {
		this.pageId = pageId;
	}

}
