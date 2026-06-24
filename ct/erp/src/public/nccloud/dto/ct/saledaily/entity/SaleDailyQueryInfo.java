package nccloud.dto.ct.saledaily.entity;

import nc.vo.pub.lang.UFDateTime;

/**
 * @description 销售合同信息
 * @author wangshrc
 * @date 2019年1月16日 上午9:56:14
 * @version ncc1.0
 */
public class SaleDailyQueryInfo {
	private String pk;
	private String[] pks;
	private String reason;
	private String pageid;
	//币种
	private String corigcurrencyid;
	//下游单据类型
	private String destbillType;
	//交易类型
	private String ctrantypeid;

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String[] getPks() {
		return pks;
	}

	public void setPks(String[] pks) {
		this.pks = pks;
	}

	private UFDateTime ts;
	private String[] pk_tss;

	public String[] getPk_tss() {
		return pk_tss;
	}

	public void setPk_tss(String[] pk_tss) {
		this.pk_tss = pk_tss;
	}

	public UFDateTime getTs() {
		return ts;
	}

	public void setTs(UFDateTime ts) {
		this.ts = ts;
	}

	public String getPk() {
		return pk;
	}

	public void setPk(String pk) {
		this.pk = pk;
	}

	public String getPageid() {
		return pageid;
	}

	public void setPageid(String pageid) {
		this.pageid = pageid;
	}

	public String getCorigcurrencyid() {
		return corigcurrencyid;
	}

	public void setCorigcurrencyid(String corigcurrencyid) {
		this.corigcurrencyid = corigcurrencyid;
	}

	public String getDestbillType() {
		return destbillType;
	}

	public void setDestbillType(String destbillType) {
		this.destbillType = destbillType;
	}

	public String getCtrantypeid() {
		return ctrantypeid;
	}

	public void setCtrantypeid(String ctrantypeid) {
		this.ctrantypeid = ctrantypeid;
	}
	
}
