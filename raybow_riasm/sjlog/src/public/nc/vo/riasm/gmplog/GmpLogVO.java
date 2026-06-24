package nc.vo.riasm.gmplog;

import nc.vo.pub.SuperVO;

public class GmpLogVO extends SuperVO {
	
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

public String pk_group;
  
  public String pk_org;
  
  public String funcode;
  
  public String funname;

  
  public String columncode;

  public String columnname;
  
  public String contendold;
  
  public String contendchg;
  
  public String contendoldcode;
  
  public String contendchgcode;
  
  
  public String contendoldpk;
  
  public String contendchgpk;

  
  public String chgusername;
  
  public String chgusercode;
  
  public String chguserpk;

  
  public String chgtime;
  
  public String chgdesc;

  
  public String ts;

  public int  dr;
  
  public String clientip;
  
  /**
  *自定义项1--动作
  */
  public String def1;
  /**
  *自定义项2-表体对象名称
  */
  public String def2;
  /**
  *自定义项3--表体动作  增行, 修改,删除
  */
  public String def3;
  /**
  *自定义项4
  */
  public String def4;
  /**
  *自定义项5
  */
  public String def5;
  /**
  *自定义项6
  */
  public String def6;
  /**
  *自定义项7
  */
  public String def7;
  /**
  *自定义项8
  */
  public String def8;
  /**
  *自定义项9
  */
  public String def9;
  /**
  *自定义项10
  */
  public String def10;
  /**
  *自定义项11
  */
  public String def11;
  /**
  *自定义项12
  */
  public String def12;
  /**
  *自定义项13
  */
  public String def13;
  /**
  *自定义项14
  */
  public String def14;
  /**
  *自定义项15
  */
  public String def15;
  /**
  *自定义项16
  */
  public String def16;
  /**
  *自定义项17
  */
  public String def17;
  /**
  *自定义项18
  */
  public String def18;
  /**
  *自定义项19
  */
  public String def19;
  /**
  *自定义项20
  */
  public String def20;
  
  public String pk_bill;
  
  
  
  public String busipk;

  public String busicode;
  
  public String businame;

  @Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return "qc_gmp_chglog";
	}
  
  @Override
	public String getPKFieldName() {
		// TODO Auto-generated method stub
		return "pk_bill";
	}
  
  

public String getBusipk() {
	return busipk;
}

public void setBusipk(String busipk) {
	this.busipk = busipk;
}

public String getBusicode() {
	return busicode;
}

public void setBusicode(String busicode) {
	this.busicode = busicode;
}

public String getBusiname() {
	return businame;
}

public void setBusiname(String businame) {
	this.businame = businame;
}

public String getPk_bill() {
	return pk_bill;
}

public void setPk_bill(String pk_bill) {
	this.pk_bill = pk_bill;
}

public String getPk_group() {
	return pk_group;
}

public void setPk_group(String pk_group) {
	this.pk_group = pk_group;
}

public String getPk_org() {
	return pk_org;
}

public void setPk_org(String pk_org) {
	this.pk_org = pk_org;
}

public String getFuncode() {
	return funcode;
}

public void setFuncode(String funcode) {
	this.funcode = funcode;
}

public String getFunname() {
	return funname;
}

public void setFunname(String funname) {
	this.funname = funname;
}

public String getColumncode() {
	return columncode;
}

public void setColumncode(String columncode) {
	this.columncode = columncode;
}

public String getColumnname() {
	return columnname;
}

public void setColumnname(String columnname) {
	this.columnname = columnname;
}

public String getContendold() {
	return contendold;
}

public void setContendold(String contendold) {
	this.contendold = contendold;
}

public String getContendchg() {
	return contendchg;
}

public void setContendchg(String contendchg) {
	this.contendchg = contendchg;
}

public String getContendoldcode() {
	return contendoldcode;
}

public void setContendoldcode(String contendoldcode) {
	this.contendoldcode = contendoldcode;
}

public String getContendchgcode() {
	return contendchgcode;
}

public void setContendchgcode(String contendchgcode) {
	this.contendchgcode = contendchgcode;
}

public String getContendoldpk() {
	return contendoldpk;
}

public void setContendoldpk(String contendoldpk) {
	this.contendoldpk = contendoldpk;
}

public String getContendchgpk() {
	return contendchgpk;
}

public void setContendchgpk(String contendchgpk) {
	this.contendchgpk = contendchgpk;
}

public String getChgusername() {
	return chgusername;
}

public void setChgusername(String chgusername) {
	this.chgusername = chgusername;
}

public String getChgusercode() {
	return chgusercode;
}

public void setChgusercode(String chgusercode) {
	this.chgusercode = chgusercode;
}

public String getChguserpk() {
	return chguserpk;
}

public void setChguserpk(String chguserpk) {
	this.chguserpk = chguserpk;
}

public String getChgtime() {
	return chgtime;
}

public void setChgtime(String chgtime) {
	this.chgtime = chgtime;
}

public String getChgdesc() {
	return chgdesc;
}

public void setChgdesc(String chgdesc) {
	this.chgdesc = chgdesc;
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

public String getClientip() {
	return clientip;
}

public void setClientip(String clientip) {
	this.clientip = clientip;
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

public String getDef6() {
	return def6;
}

public void setDef6(String def6) {
	this.def6 = def6;
}

public String getDef7() {
	return def7;
}

public void setDef7(String def7) {
	this.def7 = def7;
}

public String getDef8() {
	return def8;
}

public void setDef8(String def8) {
	this.def8 = def8;
}

public String getDef9() {
	return def9;
}

public void setDef9(String def9) {
	this.def9 = def9;
}

public String getDef10() {
	return def10;
}

public void setDef10(String def10) {
	this.def10 = def10;
}

public String getDef11() {
	return def11;
}

public void setDef11(String def11) {
	this.def11 = def11;
}

public String getDef12() {
	return def12;
}

public void setDef12(String def12) {
	this.def12 = def12;
}

public String getDef13() {
	return def13;
}

public void setDef13(String def13) {
	this.def13 = def13;
}

public String getDef14() {
	return def14;
}

public void setDef14(String def14) {
	this.def14 = def14;
}

public String getDef15() {
	return def15;
}

public void setDef15(String def15) {
	this.def15 = def15;
}

public String getDef16() {
	return def16;
}

public void setDef16(String def16) {
	this.def16 = def16;
}

public String getDef17() {
	return def17;
}

public void setDef17(String def17) {
	this.def17 = def17;
}

public String getDef18() {
	return def18;
}

public void setDef18(String def18) {
	this.def18 = def18;
}

public String getDef19() {
	return def19;
}

public void setDef19(String def19) {
	this.def19 = def19;
}

public String getDef20() {
	return def20;
}

public void setDef20(String def20) {
	this.def20 = def20;
}
  
  

}
