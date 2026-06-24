package nc.bs.jzyy.sys.thlims;

import nc.vo.pub.SuperVO;

/**
 * 请验日志
 */

public class THLimsLogVO extends SuperVO{
	
	private static final long serialVersionUID = 1L;

	public static String table_name="qc_jc_thlims";
	
	private String pk_org;//组织编码
	private String pk_org_v  ;//单据主键
	
	/*采购报检
	生产完工报检
	库存报检（复验）
	中控检验
	清洁批报检*/
	private String ctrantype ;//报检类型  
	private String cuserid ;//报检人ID
	private String usercode  ;//报检人拜尼马
	private String username  ;//报检人
	private String qctime  ;//报检时间
	private String pk_log  ;//PK
	private String vbillid ;//单据主键
	private String vbill_bid;//BPK
	private String resp;//结果
	private String qcpoint ;//报检点
	private String qccount;//报检次数
	private String passtime;//放行时间
	private String ts;
	private String sys_id;//同步ID

	private int dr;

	private String vbillcode ;//单据编码

	public String getPk_org() {
		return pk_org;
	}

	public void setPk_org(String pk_org) {
		this.pk_org = pk_org;
	}

	public String getPk_org_v() {
		return pk_org_v;
	}

	public void setPk_org_v(String pk_org_v) {
		this.pk_org_v = pk_org_v;
	}

	public String getCtrantype() {
		return ctrantype;
	}

	public void setCtrantype(String ctrantype) {
		this.ctrantype = ctrantype;
	}

	public String getVbill_bid() {
		return vbill_bid;
	}

	public void setVbill_bid(String vbill_bid) {
		this.vbill_bid = vbill_bid;
	}
	
	public String getCuserid() {
		return cuserid;
	}

	public void setCuserid(String cuserid) {
		this.cuserid = cuserid;
	}

	public String getUsercode() {
		return usercode;
	}

	public void setUsercode(String usercode) {
		this.usercode = usercode;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getQctime() {
		return qctime;
	}

	public void setQctime(String qctime) {
		this.qctime = qctime;
	}

	public String getVbillcode() {
		return vbillcode;
	}

	public void setVbillcode(String vbillcode) {
		this.vbillcode = vbillcode;
	}

	public String getVbillid() {
		return vbillid;
	}

	public void setVbillid(String vbillid) {
		this.vbillid = vbillid;
	}

	public String getResp() {
		return resp;
	}

	public void setResp(String resp) {
		this.resp = resp;
	}

	public String getQcpoint() {
		return qcpoint;
	}

	public void setQcpoint(String qcpoint) {
		this.qcpoint = qcpoint;
	}

	public String getQccount() {
		return qccount;
	}

	public void setQccount(String qccount) {
		this.qccount = qccount;
	}

	public String getPasstime() {
		return passtime;
	}

	public void setPasstime(String passtime) {
		this.passtime = passtime;
	}

	public String getTs() {
		return ts;
	}

	public void setTs(String ts) {
		this.ts = ts;
	}

	public int getDr() {
		return dr;
	}

	public void setDr(int dr) {
		this.dr = dr;
	}

	

	@Override
	public String getPKFieldName() {
		// TODO Auto-generated method stub
		return "pk_log";
	}

	@Override
	public String getParentPKFieldName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return table_name;
	}


	public String getPk_log() {
		return pk_log;
	}

	public void setPk_log(String pk_log) {
		this.pk_log = pk_log;
	}
	
	public String getSys_id() {
		return sys_id;
	}

	public void setSys_id(String sys_id) {
		this.sys_id = sys_id;
	}
}
