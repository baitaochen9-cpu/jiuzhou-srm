package nc.vo.ic.m4460.util;

import java.util.HashMap;
import java.util.Map;

public class ScmSendBuziMsgPara {
	private String approverField = "approver";

	private Map<String, Object> attachment = new HashMap();

	private String billType;

	private String contentType;

	private String ctrantypeidField = "ctrantypeid";

	private String detail;

	private String msgrescode;

	private String msgSourceType;

	private String pk_detail;

	private String pk_groupField = "pk_group";

	private String pk_materialField = "pk_material";

	private String pk_orgField = "pk_org";

	private String vtrantypecodeField = "vtrantypecode";

	public String getApproverField() {
		return approverField;
	}

	public Object getAttachment(String name) {
		return attachment.get(name);
	}

	public String getBillType() {
		return billType;
	}

	public String getContentType() {
		return contentType;
	}

	public String getCtrantypeidField() {
		return ctrantypeidField;
	}

	public String getDetail() {
		return detail;
	}

	public String getMsgrescode() {
		return msgrescode;
	}

	public String getMsgSourceType() {
		return msgSourceType;
	}

	public String getPk_detail() {
		return pk_detail;
	}

	public String getPk_groupField() {
		return pk_groupField;
	}

	public String getPk_materialField() {
		return pk_materialField;
	}

	public String getPk_orgField() {
		return pk_orgField;
	}

	public String getVtrantypecodeField() {
		return vtrantypecodeField;
	}

	public void putAttachment(String name, Object attach) {
		attachment.put(name, attach);
	}

	public void setApproverField(String approverField) {
		this.approverField = approverField;
	}

	public void setBillType(String billType) {
		this.billType = billType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public void setCtrantypeidField(String ctrantypeidField) {
		this.ctrantypeidField = ctrantypeidField;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public void setMsgrescode(String msgrescode) {
		this.msgrescode = msgrescode;
	}

	public void setMsgSourceType(String msgSourceType) {
		this.msgSourceType = msgSourceType;
	}

	public void setPk_detail(String pk_detail) {
		this.pk_detail = pk_detail;
	}

	public void setPk_groupField(String pk_groupField) {
		this.pk_groupField = pk_groupField;
	}

	public void setPk_materialField(String pk_materialField) {
		this.pk_materialField = pk_materialField;
	}

	public void setPk_orgField(String pk_orgField) {
		this.pk_orgField = pk_orgField;
	}

	public void setVtrantypecodeField(String vtrantypecodeField) {
		this.vtrantypecodeField = vtrantypecodeField;
	}
}
