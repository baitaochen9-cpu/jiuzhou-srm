/** <a href="http://www.cpupk.com/decompiler">Eclipse Class Decompiler</a> plugin, Copyright (c) 2017 Chen Chao. */
package nc.pub.scmf.ic.batchcoderule;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nc.impl.pub.util.db.StringUtil;
import nc.vo.fipub.annotation.TestCase;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pubapp.AppContext;

public class NewJZBatchCodeVO extends SuperVO{
	
	public NewJZBatchCodeVO(){};
	
	public NewJZBatchCodeVO(Map<String, String>  parseRule,String pk_material ,String pk_org,String materialcode) throws BusinessException {
		initialize(parseRule, materialcode , pk_org,pk_material);
	}
	
	/**
	 * 数据初始化，设定默认值；
	 * @param pk_org2 
	 * @param material_code 
	 * @param paraString
	 * @throws BusinessException 
	 */
	private void initialize(Map<String, String>  parseRule, String material_code, String pk_org2,String pk_material) throws BusinessException {
		//通过参数，检查需要初始化的的有哪些值；		 { 1 : "materialcode,6,N,N" , 2 : "atz,1,N,N" , 3 : "num,3,N,N" , 4 ："years,2,Y,N"}
		this.setMaterial(pk_material);
		this.setPk_org(pk_org2);
		this.setTs(AppContext.getInstance().getServerTime());
		
		for(String key : parseRule.keySet()){
			String string = parseRule.get(key);
			if(string == null || string.length() < 1 ) continue;
			String[] split = string.split(",");
			if(split.length < 3 ) continue;  // 长度小于4 的话，说明格式出再问题；先跳出去，后续做一次规则的对比，看看总个数是否有差异；
			
//			String property = split[0];
			Integer valuelength = new Integer( split[0]);//字段长度
			String compensator =split[1];//进行补位字符 
			UFBoolean reset = new UFBoolean(split[2]);//是否流水依据
			
			if(reset.equals(UFBoolean.TRUE)){
				this.setCoverage(key);
			}
			
						
			if(key.equals(MATERIALCODE)){// 物料
				this.setMaterialcode(this.setMaterialcode(material_code, valuelength,compensator));
				continue;
			}else 
				if(key.equals(ATZ)){//自定义字符 
				this.setAtz(this.setAtz(valuelength,compensator));
				continue;
			}else
				if(key.equals(NUM)){ //流水
					this.setNum(this.setNum(valuelength,compensator));
					continue;
			}else
				if (key.equals(YEARS)) {//年,最多4位
						this.setYears(this.setYears(valuelength,compensator));
						continue;
				} else 
					if (key.equals(MONTHS)) {//月
						this.setMonths(this.setMonths(valuelength,compensator));
						continue;
					}else if (key.equals(DAYS)) {//日
						this.setDays(this.setDays(valuelength,compensator));
						continue;
					}else
					if(key.equals(Default_str)){
						this.setDefault_str(this.setDefault_str(valuelength,compensator));
						continue;
					}

		}
		
	}
	

	

	public String setDays(Integer valuelength, String compensator) {
		String y = AppContext.getInstance().getBusiDate().getDay()+"";
		Integer length = (y.length() - valuelength) < 0 ? 0 : (y.length() - valuelength); 
		String substring = y.substring(length, y.length());
		if(!compensator.isEmpty()){//需要进行补位，物料编码前尝试补0
		substring = NewJZBatchCodeCreatUtil.isCompensator(substring, valuelength, compensator);
		this.setAttributeValue(DAYS, substring);
		}
		return substring;
	}

	/**
	 * 规则获取月份数据
	 * @param valuelength
	 * @param compensator
	 * @return
	 */
	public String setMonths(Integer valuelength, String compensator) {
		String y = AppContext.getInstance().getBusiDate().getMonth()+"";
		Integer length = (y.length() - valuelength) < 0 ? 0 : (y.length() - valuelength); 
		String substring = y.substring(length, y.length());
		if(!compensator.isEmpty()){//需要进行补位，物料编码前尝试补0
		substring = NewJZBatchCodeCreatUtil.isCompensator(substring, valuelength, compensator);
		this.setAttributeValue(MONTHS, substring);
		}
		return substring;
	}

	public String setMaterialcode(String materialcode  , Integer valuelength ,String compensator ) {
		int j = materialcode.length() - valuelength <= 0 ? 0 : materialcode.length() - valuelength  ;
		int i = materialcode.length() - valuelength <= 0 ? materialcode.length() : valuelength ;
		String substring = materialcode.substring(j , i);//目标值
		if(!compensator.isEmpty()){//需要进行补位，物料编码前尝试补0
			substring = NewJZBatchCodeCreatUtil.isCompensator(substring, valuelength,compensator);
		}
//		this.setAttributeValue(MATERIALCODE, substring);
		
		
		return substring;
	}
	
	private String setDefault_str(Integer valuelength, String compensator)  throws BusinessException{
		String val = this.getDefault_str();
		if(val == null || val.isEmpty()){
			throw new BusinessException("请检查批次号元素 【"+Default_str+"】是否正确配置，当前获取值为空，导致无法正常生成批次号！");
		}
		String compensator2 = "";
		if(!compensator.isEmpty()){//需要进行补位，物料编码前尝试补0
			compensator2 = NewJZBatchCodeCreatUtil.isCompensator(val, valuelength, compensator);
		}
		return compensator2;
	}
	
	public String setAtz(Integer valuelength ,String compensator ) {
		String val = this.getAtz();
		if(val == null || val.isEmpty()){
			val = strList[0];
		}
		String compensator2 = "";
		if(!compensator.isEmpty()){//需要进行补位，物料编码前尝试补0
			compensator2 = NewJZBatchCodeCreatUtil.isCompensator(val, valuelength, compensator);
		}
		return compensator2;
	}
	
	public String setNum(Integer valuelength,String compensator ) {
		String val = this.getNum();
		if(val == null || val.isEmpty() || this.getIsmax() == UFBoolean.TRUE){
			val = "1";
		}
		String compensator2 ="";
		if(!compensator.isEmpty()){//需要进行补位，物料编码前尝试补0
		 compensator2 = NewJZBatchCodeCreatUtil.isCompensator(val, valuelength, compensator);
		this.setAttributeValue(NUM, compensator2);
		}
		return compensator2;
	}
	
	public String setYears(Integer valuelength , String compensator ) {
		String y = AppContext.getInstance().getBusiDate().getYear()+"";
		String substring = y.substring(y.length() - valuelength, y.length());
		if(!compensator.isEmpty()){//需要进行补位，物料编码前尝试补0
		substring = NewJZBatchCodeCreatUtil.isCompensator(substring, valuelength, compensator);
		this.setAttributeValue(YEARS, substring);
		}
		return substring;
	}
	

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return "jz_vbatchnumber";
	}
	@Override
	public String getPKFieldName(){
		return "id";
		
	}
	
	public static String ID="id";
	public static String PK_ORG="pk_org";
	public static String MATERIAL ="material"; /*物料ID*/
	public static String MATERIALCODE = "materialcode"; /*物料 编码*/
	public static String CODERULE = "coderule";
	public static String ATZ ="atz";/*字符排列*/
	public static String NUM="num";/*流水*/
	public static String DR ="dr";
	public static String TS ="ts";
	public static String YEARS="years";/*年*/
	public static String MONTHS="months";/*月*/
	public static String DAYS="days";/*日*/
	public static String COVERAGE ="coverage";/*归零流水依据*/
	public static final String[] strList = {"A","B","C","D","E","F","G","H","J","K","L","M","N","P","Q","R","S","T","U","V","W","X","Y","Z"}; /*递增范围，*/
	public static String Default_str = "default_str";
	
	private String id;
	private String pk_org;
	private String material;
	private String materialcode;
	private String coderule;
	private String atz;
	private String num;
	private Integer dr;
	private String years;
	private String months;
	private String days;
	private String coverage;
	private UFDateTime ts;
	private UFBoolean ismax = UFBoolean.FALSE;//已到最大值
	private String default_str; //此字段只做批次号组装，不参与流水持久化。
	
	public UFBoolean getIsmax() {
		return ismax;
	}

	public void setIsmax(UFBoolean ismax) {
		this.ismax = ismax;
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPk_org() {
		return pk_org;
	}
	public void setPk_org(String pk_org) {
		this.pk_org = pk_org;
	}
	public String getMaterial() {
		return material;
	}
	public void setMaterial(String material) {
		this.material = material;
	}
	public String getMaterialcode() {
		return materialcode;
	}
	public void setMaterialcode(String materialcode) {
		this.materialcode = materialcode;
	}
	public String getCoderule() {
		return coderule;
	}
	public void setCoderule(String coderule) {
		this.coderule = coderule;
	}
	public String getAtz() {
		return atz;
	}
	public void setAtz(String atz) {
		this.atz = atz;
	}
	public String getNum() {
		return num;
	}
	public void setNum(String num) {
		this.num = num;
	}
	public Integer getDr() {
		return dr;
	}
	public void setDr(Integer dr) {
		this.dr = dr;
	}
	public String getYears() {
		return years;
	}
	public void setYears(String years) {
		this.years = years;
	}
	public String getCoverage() {
		return coverage;
	}
	public void setCoverage(String coverage) {
		this.coverage = coverage;
	}

	public UFDateTime getTs() {
		return ts;
	}

	public void setTs(UFDateTime ufDateTime) {
		this.ts = ufDateTime;
	}

	public String getDefault_str() {
		return NewJZBatchCodeCreatUtil.getMaterialstockByID(material, pk_org);
	}

	public void setDefault_str(String default_str) {
		this.default_str = default_str;
	}

	public String getMonths() {
		return months;
	}

	public void setMonths(String months) {
		this.months = months;
	}

	public String getDays() {
		return days;
	}

	public void setDays(String days) {
		this.days = days;
	}


	
	


}
