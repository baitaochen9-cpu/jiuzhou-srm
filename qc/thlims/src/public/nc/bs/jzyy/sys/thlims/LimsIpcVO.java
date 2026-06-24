package nc.bs.jzyy.sys.thlims;

import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDateTime;

public class LimsIpcVO extends SuperVO {
	
	/**
	 * 
	 */
	//private static final long serialVersionUID = 1L;

	public static String table_name="TH_LIMS_IPC";
	
	private String pk_lims_ipc;//PK
	private String ID;//同步ID
	private String cpmohid;//流程生产订单  
	private String cmoid;//流程生产订单表体ID
	private String qc_point;//报检点
	private int qc_times;//报检次数
	private String qc_psn;//报检人
	private String qc_time;//报检时间
	private String cmaterialid;//物料
	private String qc_contorl_num;//控制码 批次号
	
	private String qc_final;//质检结果
	private String qc_passs_psn;//放行人
	private String qc_passs_time;//放行时间
	
	private UFDateTime ts;
	
	private String def1;
	private String def2;
	private String def3;
	private String def4;
	private String def5;
	
	
	/*@Override
	public IVOMeta getMetaData() {
		IVOMeta meta = VOMetaFactory.getInstance().getVOMeta("qc.th_lims_ipc");
		return meta;
	}*/
	@Override
	public void setPrimaryKey(String key) {
		super.setPrimaryKey(key);
	}
	
	@Override
	public String getParentPKFieldName() {
		return null;
	}
	
	@Override
	public String getPKFieldName() {
		return "pk_lims_ipc";
	}
	
	
	public String getPk_lims_ipc() {
		return pk_lims_ipc;
	}

	public void setPk_lims_ipc(String pk_lims_ipc) {
		this.pk_lims_ipc = pk_lims_ipc;
	}

	@Override
	public String getTableName() {
		return table_name;
	}
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public String getCpmohid() {
		return cpmohid;
	}
	public void setCpmohid(String cpmohid) {
		this.cpmohid = cpmohid;
	}
	public String getCmoid() {
		return cmoid;
	}
	public void setCmoid(String cmoid) {
		this.cmoid = cmoid;
	}
	public String getQc_point() {
		return qc_point;
	}
	public void setQc_point(String qc_point) {
		this.qc_point = qc_point;
	}
	public int getQc_times() {
		return qc_times;
	}
	public void setQc_times(int qc_times) {
		this.qc_times = qc_times;
	}
	public String getQc_psn() {
		return qc_psn;
	}
	public void setQc_psn(String qc_psn) {
		this.qc_psn = qc_psn;
	}
	public String getQc_time() {
		return qc_time;
	}
	public void setQc_time(String qc_time) {
		this.qc_time = qc_time;
	}
	public String getCmaterialid() {
		return cmaterialid;
	}
	public void setCmaterialid(String cmaterialid) {
		this.cmaterialid = cmaterialid;
	}
	public String getQc_contorl_num() {
		return qc_contorl_num;
	}
	public void setQc_contorl_num(String qc_contorl_num) {
		this.qc_contorl_num = qc_contorl_num;
	}
	public String getQc_final() {
		return qc_final;
	}
	public void setQc_final(String qc_final) {
		this.qc_final = qc_final;
	}
	public String getQc_passs_psn() {
		return qc_passs_psn;
	}
	public void setQc_passs_psn(String qc_passs_psn) {
		this.qc_passs_psn = qc_passs_psn;
	}
	public String getQc_passs_time() {
		return qc_passs_time;
	}
	public void setQc_passs_time(String qc_passs_time) {
		this.qc_passs_time = qc_passs_time;
	}
	public UFDateTime getTs() {
		return ts;
	}
	public void setTs(UFDateTime ts) {
		this.ts = ts;
	}
	public String getDef1() {
		return def1;
	}
	public void setDef1(String def1) {
		this.def1 = def1;
	}
	public String getDef2() {
		return def2;
	}
	public void setDef2(String def2) {
		this.def2 = def2;
	}
	public String getDef3() {
		return def3;
	}
	public void setDef3(String def3) {
		this.def3 = def3;
	}
	public String getDef4() {
		return def4;
	}
	public void setDef4(String def4) {
		this.def4 = def4;
	}
	public String getDef5() {
		return def5;
	}
	public void setDef5(String def5) {
		this.def5 = def5;
	}
	/*public static long getSerialversionuid() {
		return serialVersionUID;
	}*/
}
