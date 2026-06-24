package nc.bs.pub.relatedApp;

import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDateTime;
/*
 * 
 * 关联小应用
 */
public class RelatedAppVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String pk_billtypeid;
	private String pk_billtypecode;
	private String appcode;
	private String pagecode;
	/*
	 * 1 默认
	 * 2 
	 * 3 审批
	 * 4 联查
	 * 
	 */
	private int sence;
	
	//主键 
	private String pk_relatedapp;
	private int priority;
	private String vdef1; //列表页面编码          
	private String vdef2;
	private String vdef3;
	private Integer dr;

	private UFDateTime ts;
	
	
	
	
	public String getPk_billtypecode() {
		return pk_billtypecode;
	}
	public void setPk_billtypecode(String pk_billtypecode) {
		this.pk_billtypecode = pk_billtypecode;
	}
	public Integer getDr() {
		return dr;
	}
	public void setDr(Integer dr) {
		this.dr = dr;
	}
	public UFDateTime getTs() {
		return ts;
	}
	public void setTs(UFDateTime ts) {
		this.ts = ts;
	}
	public String getPk_billtypeid() {
		return pk_billtypeid;
	}
	public void setPk_billtypeid(String pk_billtypeid) {
		this.pk_billtypeid = pk_billtypeid;
	}
	public String getAppcode() {
		return appcode;
	}
	public void setAppcode(String appcode) {
		this.appcode = appcode;
	}
	public String getPagecode() {
		return pagecode;
	}
	public void setPagecode(String pagecode) {
		this.pagecode = pagecode;
	}
	public int getSence() {
		return sence;
	}
	public void setSence(int sence) {
		this.sence = sence;
	}

	public int getPriority() {
		return priority;
	}
	public void setPriority(int priority) {
		this.priority = priority;
	}
	public String getVdef1() {
		return vdef1;
	}
	public void setVdef1(String vdef1) {
		this.vdef1 = vdef1;
	}
	public String getVdef2() {
		return vdef2;
	}
	public void setVdef2(String vdef2) {
		this.vdef2 = vdef2;
	}
	public String getVdef3() {
		return vdef3;
	}
	public void setVdef3(String vdef3) {
		this.vdef3 = vdef3;
	}
	
	
	
	
	public String getPk_relatedapp() {
		return pk_relatedapp;
	}
	public void setPk_relatedapp(String pk_relatedapp) {
		this.pk_relatedapp = pk_relatedapp;
	}
	public void setPrimaryKey(String pk_relatedapp){
		this.pk_relatedapp = pk_relatedapp;
	}
	
	public java.lang.String getPKFieldName() {

		return "pk_relatedapp";
	}
	public java.lang.String getTableName() {

		return "bd_relatedapp";
	}
	
	
}
