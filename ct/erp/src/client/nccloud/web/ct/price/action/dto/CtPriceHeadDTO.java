package nccloud.web.ct.price.action.dto;

public class CtPriceHeadDTO {
	private String pk_ct_price;
	private String ts;
	private String pk_group;
	private String pk_oid;
	private String pageid;
	private String pk_supplier;
	private String pk_org;

	
	
	
	public String getPk_supplier() {
		return pk_supplier;
	}

	public void setPk_supplier(String pk_supplier) {
		this.pk_supplier = pk_supplier;
	}

	public String getPk_org() {
		return pk_org;
	}

	public void setPk_org(String pk_org) {
		this.pk_org = pk_org;
	}

	public String getPageid() {
		return pageid;
	}

	public void setPageid(String pageid) {
		this.pageid = pageid;
	}

	public String getPk_ct_price() {
		return pk_ct_price;
	}

	public void setPk_ct_price(String pk_ct_price) {
		this.pk_ct_price = pk_ct_price;
	}

	public String getTs() {
		return ts;
	}

	public void setTs(String ts) {
		this.ts = ts;
	}

	public String getPk_group() {
		return pk_group;
	}

	public void setPk_group(String pk_group) {
		this.pk_group = pk_group;
	}

	public String getPk_oid() {
		return pk_oid;
	}

	public void setPk_oid(String pk_oid) {
		this.pk_oid = pk_oid;
	}

}
