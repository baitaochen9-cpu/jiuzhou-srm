package nc.vo.cm.equivrate.entity;

import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDateTime;

public class EquivrateVO extends SuperVO{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//cm_equivrate
	private String cpickmid;
	private String cmaterialid;
	private String cmaterialvid;
	private String ccostobjectid;//成本对象
	private String vcostobjcode ;
	private String vcostobjname;
    private String pk_costcenter;//成本中心
    private String cccode;
    private String ccname;
	private String cbomversionid;//bomid
	private UFDateTime  dmakedate;//材料出单时间
	private UFDateTime dcurdate;//当前时间
	private UFDateTime tactstarttime;//实际开工时间
	private String  hvdef1;//标准工时
	
	private String  hvdef2;//
	private String  hvdef3;//
	private String  hvdef4;//
	private String  hvdef5;//

	public String getHvdef2() {
		return hvdef2;
	}
	public void setHvdef2(String hvdef2) {
		this.hvdef2 = hvdef2;
	}
	public String getHvdef3() {
		return hvdef3;
	}
	public void setHvdef3(String hvdef3) {
		this.hvdef3 = hvdef3;
	}
	public String getHvdef4() {
		return hvdef4;
	}
	public void setHvdef4(String hvdef4) {
		this.hvdef4 = hvdef4;
	}
	public String getHvdef5() {
		return hvdef5;
	}
	public void setHvdef5(String hvdef5) {
		this.hvdef5 = hvdef5;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getCcostobjectid() {
		return ccostobjectid;
	}
	public void setCcostobjectid(String ccostobjectid) {
		this.ccostobjectid = ccostobjectid;
	}
	public String getPk_costcenter() {
		return pk_costcenter;
	}
	public void setPk_costcenter(String pk_costcenter) {
		this.pk_costcenter = pk_costcenter;
	}
	public String getCbomversionid() {
		return cbomversionid;
	}
	public void setCbomversionid(String cbomversionid) {
		this.cbomversionid = cbomversionid;
	}
	public UFDateTime getDmakedate() {
		return dmakedate;
	}
	public void setDmakedate(UFDateTime dmakedate) {
		this.dmakedate = dmakedate;
	}
	public UFDateTime getDcurdate() {
		return dcurdate;
	}
	public void setDcurdate(UFDateTime dcurdate) {
		this.dcurdate = dcurdate;
	}
	public UFDateTime getTactstarttime() {
		return tactstarttime;
	}
	public void setTactstarttime(UFDateTime tactstarttime) {
		this.tactstarttime = tactstarttime;
	}
	public String getHvdef1() {
		return hvdef1;
	}
	public void setHvdef1(String hvdef1) {
		this.hvdef1 = hvdef1;
	}
	public String getCmaterialid() {
		return cmaterialid;
	}
	public void setCmaterialid(String cmaterialid) {
		this.cmaterialid = cmaterialid;
	}
	public String getCmaterialvid() {
		return cmaterialvid;
	}
	public void setCmaterialvid(String cmaterialvid) {
		this.cmaterialvid = cmaterialvid;
	}
	public String getCpickmid() {
		return cpickmid;
	}
	public void setCpickmid(String cpickmid) {
		this.cpickmid = cpickmid;
	}
	public String getVcostobjcode() {
		return vcostobjcode;
	}
	public void setVcostobjcode(String vcostobjcode) {
		this.vcostobjcode = vcostobjcode;
	}
	public String getVcostobjname() {
		return vcostobjname;
	}
	public void setVcostobjname(String vcostobjname) {
		this.vcostobjname = vcostobjname;
	}
	public String getCccode() {
		return cccode;
	}
	public void setCccode(String cccode) {
		this.cccode = cccode;
	}
	public String getCcname() {
		return ccname;
	}
	public void setCcname(String ccname) {
		this.ccname = ccname;
	}
	
}
