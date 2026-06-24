package nc.itf.jzyy.sys.lims;

import nc.vo.pub.SuperVO;

/**
 * 登录日志实体
 */
public class LimsLogVO extends SuperVO{
	
	private static final long serialVersionUID = 1L;

	public static String table_name="LIMS_SYS_LOGGER";
	
	
	private String pk_log;
	private String pk_bill;//单据主键
	
	private String vbillno;//单据编号
	private String name;//档案名称

	private String busitype;//业务类型
	private String taget;//目标系统
	private String issuccess;//是否成功

	private String sendDate;//发送日期
	private String sendTime;//发送时间
	private String sendJson;//发送报文

	private String resTime;//回执时间
	private String resJson;//反馈报文
	private String errorinfor;//执行结果
	
	private String vdef1;
	private String vdef2;
	private String vdef3;
	private String vdef4;

	private String vdef5;


	
	public String getVdef5() {
		return vdef5;
	}

	public void setVdef5(String vdef5) {
		this.vdef5 = vdef5;
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

	public String getPk_bill() {
		return pk_bill;
	}

	public void setPk_bill(String pk_bill) {
		this.pk_bill = pk_bill;
	}

	public String getVbillno() {
		return vbillno;
	}

	public void setVbillno(String vbillno) {
		this.vbillno = vbillno;
	}

	public String getSendTime() {
		return sendTime;
	}

	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}

	public String getErrorinfor() {
		return errorinfor;
	}

	public void setErrorinfor(String errorinfor) {
		this.errorinfor = errorinfor;
	}

	public String getSendJson() {
		return sendJson;
	}

	public void setSendJson(String sendJson) {
		this.sendJson = sendJson;
	}

	public String getResJson() {
		return resJson;
	}

	public void setResJson(String resJson) {
		this.resJson = resJson;
	}

	public String getPk_log() {
		return pk_log;
	}

	public void setPk_log(String pk_log) {
		this.pk_log = pk_log;
	}

	public String getBusitype() {
		return busitype;
	}

	public void setBusitype(String busitype) {
		this.busitype = busitype;
	}

	public String getResTime() {
		return resTime;
	}

	public void setResTime(String resTime) {
		this.resTime = resTime;
	}

	public String getSendDate() {
		return sendDate;
	}

	public void setSendDate(String sendDate) {
		this.sendDate = sendDate;
	}

	public String getTaget() {
		return taget;
	}

	public void setTaget(String taget) {
		this.taget = taget;
	}

	public String getIssuccess() {
		return issuccess;
	}

	public void setIssuccess(String issuccess) {
		this.issuccess = issuccess;
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

	public String getVdef4() {
		return vdef4;
	}

	public void setVdef4(String vdef4) {
		this.vdef4 = vdef4;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	

}
