package nc.vo.bd.goodscode;

import nc.vo.pub.*;
import nc.vo.pubapp.pattern.model.meta.entity.vo.VOMetaFactory;

/**
 * <b> 此处简要描述此类功能 </b>
 * <p>
 *   此处添加类的描述信息
 * </p>
 *  创建日期:2025-5-6
 * @author 
 * @version NCPrj ??
 */
public class GoodscodeVO extends nc.vo.pub.SuperVO{
	
    private java.lang.String pk_goodscode;
    private java.lang.String pk_group;
    private java.lang.String pk_org;
    private java.lang.String code;
    private java.lang.String name = "";
    private java.lang.String name2;
    private java.lang.String name3;
    private java.lang.String name4;
    private java.lang.String name5;
    private java.lang.String name6;
    private java.lang.String pk_customunit;
    private java.lang.String pk_customunitwo;
    private java.lang.String creator;
    private nc.vo.pub.lang.UFDateTime creationtime;
    private java.lang.String modifier;
    private nc.vo.pub.lang.UFDateTime modifiedtime;
    private java.lang.String remark;
    private java.lang.Integer enablestate = Integer.valueOf(2);
    private java.lang.Integer dataoriginflag = Integer.valueOf(0);
    private java.lang.String def1;
    private java.lang.String def2;
    private java.lang.String def3;
    private java.lang.Integer dr = 0;
    private nc.vo.pub.lang.UFDateTime ts;    
	
    private nc.vo.bd.goodscode.GoodstaxesItemVO[] pk_goodstaxes;
	
    public static final String PK_GOODSCODE = "pk_goodscode";
    public static final String PK_GROUP = "pk_group";
    public static final String PK_ORG = "pk_org";
    public static final String CODE = "code";
    public static final String NAME = "name";
    public static final String NAME2 = "name2";
    public static final String NAME3 = "name3";
    public static final String NAME4 = "name4";
    public static final String NAME5 = "name5";
    public static final String NAME6 = "name6";
    public static final String PK_CUSTOMUNIT = "pk_customunit";
    public static final String PK_CUSTOMUNITWO = "pk_customunitwo";
    public static final String CREATOR = "creator";
    public static final String CREATIONTIME = "creationtime";
    public static final String MODIFIER = "modifier";
    public static final String MODIFIEDTIME = "modifiedtime";
    public static final String REMARK = "remark";
    public static final String ENABLESTATE = "enablestate";
    public static final String DATAORIGINFLAG = "dataoriginflag";
    public static final String DEF1 = "def1";
    public static final String DEF2 = "def2";
    public static final String DEF3 = "def3";

	/**
	 * 属性 pk_goodscode的Getter方法.属性名：海关商品编码主键
	 *  创建日期:2025-5-6
	 * @return java.lang.String
	 */
	public java.lang.String getPk_goodscode () {
		return pk_goodscode;
	}   
	/**
	 * 属性pk_goodscode的Setter方法.属性名：海关商品编码主键
	 * 创建日期:2025-5-6
	 * @param newPk_goodscode java.lang.String
	 */
	public void setPk_goodscode (java.lang.String newPk_goodscode ) {
	 	this.pk_goodscode = newPk_goodscode;
	} 	 
	
	/**
	 * 属性 pk_group的Getter方法.属性名：集团
	 *  创建日期:2025-5-6
	 * @return java.lang.String
	 */
	public java.lang.String getPk_group () {
		return pk_group;
	}   
	/**
	 * 属性pk_group的Setter方法.属性名：集团
	 * 创建日期:2025-5-6
	 * @param newPk_group java.lang.String
	 */
	public void setPk_group (java.lang.String newPk_group ) {
	 	this.pk_group = newPk_group;
	} 	 
	
	/**
	 * 属性 pk_org的Getter方法.属性名：组织
	 *  创建日期:2025-5-6
	 * @return java.lang.String
	 */
	public java.lang.String getPk_org () {
		return pk_org;
	}   
	/**
	 * 属性pk_org的Setter方法.属性名：组织
	 * 创建日期:2025-5-6
	 * @param newPk_org java.lang.String
	 */
	public void setPk_org (java.lang.String newPk_org ) {
	 	this.pk_org = newPk_org;
	} 	 
	
	/**
	 * 属性 code的Getter方法.属性名：海关商品编码
	 *  创建日期:2025-5-6
	 * @return java.lang.String
	 */
	public java.lang.String getCode () {
		return code;
	}   
	/**
	 * 属性code的Setter方法.属性名：海关商品编码
	 * 创建日期:2025-5-6
	 * @param newCode java.lang.String
	 */
	public void setCode (java.lang.String newCode ) {
	 	this.code = newCode;
	} 	 
	
	/**
	 * 属性 name的Getter方法.属性名：null
	 *  创建日期:2025-5-6
	 * @return java.lang.String
	 */
	public java.lang.String getName () {
		return name;
	}   
	/**
	 * 属性name的Setter方法.属性名：null
	 * 创建日期:2025-5-6
	 * @param newName java.lang.String
	 */
	public void setName (java.lang.String newName ) {
	 	this.name = newName;
	} 	 
	
	/**
	 * 属性 name2的Getter方法.属性名：null
	 *  创建日期:2025-5-6
	 * @return java.lang.String
	 */
	public java.lang.String getName2 () {
		return name2;
	}   
	/**
	 * 属性name2的Setter方法.属性名：null
	 * 创建日期:2025-5-6
	 * @param newName2 java.lang.String
	 */
	public void setName2 (java.lang.String newName2 ) {
	 	this.name2 = newName2;
	} 	 
	
	/**
	 * 属性 name3的Getter方法.属性名：null
	 *  创建日期:2025-5-6
	 * @return java.lang.String
	 */
	public java.lang.String getName3 () {
		return name3;
	}   
	/**
	 * 属性name3的Setter方法.属性名：null
	 * 创建日期:2025-5-6
	 * @param newName3 java.lang.String
	 */
	public void setName3 (java.lang.String newName3 ) {
	 	this.name3 = newName3;
	} 	 
	
	/**
	 * 属性 name4的Getter方法.属性名：null
	 *  创建日期:2025-5-6
	 * @return java.lang.String
	 */
	public java.lang.String getName4 () {
		return name4;
	}   
	/**
	 * 属性name4的Setter方法.属性名：null
	 * 创建日期:2025-5-6
	 * @param newName4 java.lang.String
	 */
	public void setName4 (java.lang.String newName4 ) {
	 	this.name4 = newName4;
	} 	 
	
	/**
	 * 属性 name5的Getter方法.属性名：null
	 *  创建日期:2025-5-6
	 * @return java.lang.String
	 */
	public java.lang.String getName5 () {
		return name5;
	}   
	/**
	 * 属性name5的Setter方法.属性名：null
	 * 创建日期:2025-5-6
	 * @param newName5 java.lang.String
	 */
	public void setName5 (java.lang.String newName5 ) {
	 	this.name5 = newName5;
	} 	 
	
	/**
	 * 属性 name6的Getter方法.属性名：null
	 *  创建日期:2025-5-6
	 * @return java.lang.String
	 */
	public java.lang.String getName6 () {
		return name6;
	}   
	/**
	 * 属性name6的Setter方法.属性名：null
	 * 创建日期:2025-5-6
	 * @param newName6 java.lang.String
	 */
	public void setName6 (java.lang.String newName6 ) {
	 	this.name6 = newName6;
	} 	 
	
	/**
	 * 属性 pk_customunit的Getter方法.属性名：海关单位1
	 *  创建日期:2025-5-6
	 * @return java.lang.String
	 */
	public java.lang.String getPk_customunit () {
		return pk_customunit;
	}   
	/**
	 * 属性pk_customunit的Setter方法.属性名：海关单位1
	 * 创建日期:2025-5-6
	 * @param newPk_customunit java.lang.String
	 */
	public void setPk_customunit (java.lang.String newPk_customunit ) {
	 	this.pk_customunit = newPk_customunit;
	} 	 
	
	/**
	 * 属性 pk_customunitwo的Getter方法.属性名：海关单位2
	 *  创建日期:2025-5-6
	 * @return java.lang.String
	 */
	public java.lang.String getPk_customunitwo () {
		return pk_customunitwo;
	}   
	/**
	 * 属性pk_customunitwo的Setter方法.属性名：海关单位2
	 * 创建日期:2025-5-6
	 * @param newPk_customunitwo java.lang.String
	 */
	public void setPk_customunitwo (java.lang.String newPk_customunitwo ) {
	 	this.pk_customunitwo = newPk_customunitwo;
	} 	 
	
	/**
	 * 属性 creator的Getter方法.属性名：创建人
	 *  创建日期:2025-5-6
	 * @return java.lang.String
	 */
	public java.lang.String getCreator () {
		return creator;
	}   
	/**
	 * 属性creator的Setter方法.属性名：创建人
	 * 创建日期:2025-5-6
	 * @param newCreator java.lang.String
	 */
	public void setCreator (java.lang.String newCreator ) {
	 	this.creator = newCreator;
	} 	 
	
	/**
	 * 属性 creationtime的Getter方法.属性名：创建时间
	 *  创建日期:2025-5-6
	 * @return nc.vo.pub.lang.UFDateTime
	 */
	public nc.vo.pub.lang.UFDateTime getCreationtime () {
		return creationtime;
	}   
	/**
	 * 属性creationtime的Setter方法.属性名：创建时间
	 * 创建日期:2025-5-6
	 * @param newCreationtime nc.vo.pub.lang.UFDateTime
	 */
	public void setCreationtime (nc.vo.pub.lang.UFDateTime newCreationtime ) {
	 	this.creationtime = newCreationtime;
	} 	 
	
	/**
	 * 属性 modifier的Getter方法.属性名：最后修改人
	 *  创建日期:2025-5-6
	 * @return java.lang.String
	 */
	public java.lang.String getModifier () {
		return modifier;
	}   
	/**
	 * 属性modifier的Setter方法.属性名：最后修改人
	 * 创建日期:2025-5-6
	 * @param newModifier java.lang.String
	 */
	public void setModifier (java.lang.String newModifier ) {
	 	this.modifier = newModifier;
	} 	 
	
	/**
	 * 属性 modifiedtime的Getter方法.属性名：最后修改时间
	 *  创建日期:2025-5-6
	 * @return nc.vo.pub.lang.UFDateTime
	 */
	public nc.vo.pub.lang.UFDateTime getModifiedtime () {
		return modifiedtime;
	}   
	/**
	 * 属性modifiedtime的Setter方法.属性名：最后修改时间
	 * 创建日期:2025-5-6
	 * @param newModifiedtime nc.vo.pub.lang.UFDateTime
	 */
	public void setModifiedtime (nc.vo.pub.lang.UFDateTime newModifiedtime ) {
	 	this.modifiedtime = newModifiedtime;
	} 	 
	
	/**
	 * 属性 remark的Getter方法.属性名：描述
	 *  创建日期:2025-5-6
	 * @return java.lang.String
	 */
	public java.lang.String getRemark () {
		return remark;
	}   
	/**
	 * 属性remark的Setter方法.属性名：描述
	 * 创建日期:2025-5-6
	 * @param newRemark java.lang.String
	 */
	public void setRemark (java.lang.String newRemark ) {
	 	this.remark = newRemark;
	} 	 
	
	/**
	 * 属性 enablestate的Getter方法.属性名：启用状态
	 *  创建日期:2025-5-6
	 * @return java.lang.Integer
	 */
	public java.lang.Integer getEnablestate () {
		return enablestate;
	}   
	/**
	 * 属性enablestate的Setter方法.属性名：启用状态
	 * 创建日期:2025-5-6
	 * @param newEnablestate java.lang.Integer
	 */
	public void setEnablestate (java.lang.Integer newEnablestate ) {
	 	this.enablestate = newEnablestate;
	} 	 
	
	/**
	 * 属性 dataoriginflag的Getter方法.属性名：分布式
	 *  创建日期:2025-5-6
	 * @return java.lang.Integer
	 */
	public java.lang.Integer getDataoriginflag () {
		return dataoriginflag;
	}   
	/**
	 * 属性dataoriginflag的Setter方法.属性名：分布式
	 * 创建日期:2025-5-6
	 * @param newDataoriginflag java.lang.Integer
	 */
	public void setDataoriginflag (java.lang.Integer newDataoriginflag ) {
	 	this.dataoriginflag = newDataoriginflag;
	} 	 
	
	/**
	 * 属性 pk_goodstaxes的Getter方法.属性名：海关商品税率
	 *  创建日期:2025-5-6
	 * @return nc.vo.bd.goodscode.GoodstaxesItemVO[]
	 */
	public nc.vo.bd.goodscode.GoodstaxesItemVO[] getPk_goodstaxes () {
		return pk_goodstaxes;
	}   
	/**
	 * 属性pk_goodstaxes的Setter方法.属性名：海关商品税率
	 * 创建日期:2025-5-6
	 * @param newPk_goodstaxes nc.vo.bd.goodscode.GoodstaxesItemVO[]
	 */
	public void setPk_goodstaxes (nc.vo.bd.goodscode.GoodstaxesItemVO[] newPk_goodstaxes ) {
	 	this.pk_goodstaxes = newPk_goodstaxes;
	} 	 
	
	/**
	 * 属性 def1的Getter方法.属性名：自定义项1
	 *  创建日期:2025-5-6
	 * @return java.lang.String
	 */
	public java.lang.String getDef1 () {
		return def1;
	}   
	/**
	 * 属性def1的Setter方法.属性名：自定义项1
	 * 创建日期:2025-5-6
	 * @param newDef1 java.lang.String
	 */
	public void setDef1 (java.lang.String newDef1 ) {
	 	this.def1 = newDef1;
	} 	 
	
	/**
	 * 属性 def2的Getter方法.属性名：自定义项2
	 *  创建日期:2025-5-6
	 * @return java.lang.String
	 */
	public java.lang.String getDef2 () {
		return def2;
	}   
	/**
	 * 属性def2的Setter方法.属性名：自定义项2
	 * 创建日期:2025-5-6
	 * @param newDef2 java.lang.String
	 */
	public void setDef2 (java.lang.String newDef2 ) {
	 	this.def2 = newDef2;
	} 	 
	
	/**
	 * 属性 def3的Getter方法.属性名：自定义项3
	 *  创建日期:2025-5-6
	 * @return java.lang.String
	 */
	public java.lang.String getDef3 () {
		return def3;
	}   
	/**
	 * 属性def3的Setter方法.属性名：自定义项3
	 * 创建日期:2025-5-6
	 * @param newDef3 java.lang.String
	 */
	public void setDef3 (java.lang.String newDef3 ) {
	 	this.def3 = newDef3;
	} 	 
	
	/**
	 * 属性 dr的Getter方法.属性名：dr
	 *  创建日期:2025-5-6
	 * @return java.lang.Integer
	 */
	public java.lang.Integer getDr () {
		return dr;
	}   
	/**
	 * 属性dr的Setter方法.属性名：dr
	 * 创建日期:2025-5-6
	 * @param newDr java.lang.Integer
	 */
	public void setDr (java.lang.Integer newDr ) {
	 	this.dr = newDr;
	} 	 
	
	/**
	 * 属性 ts的Getter方法.属性名：ts
	 *  创建日期:2025-5-6
	 * @return nc.vo.pub.lang.UFDateTime
	 */
	public nc.vo.pub.lang.UFDateTime getTs () {
		return ts;
	}   
	/**
	 * 属性ts的Setter方法.属性名：ts
	 * 创建日期:2025-5-6
	 * @param newTs nc.vo.pub.lang.UFDateTime
	 */
	public void setTs (nc.vo.pub.lang.UFDateTime newTs ) {
	 	this.ts = newTs;
	} 	 
	
	
	/**
	  * <p>取得父VO主键字段.
	  * <p>
	  * 创建日期:2025-5-6
	  * @return java.lang.String
	  */
	public java.lang.String getParentPKFieldName() {
	    return null;
	}   
    
	/**
	  * <p>取得表主键.
	  * <p>
	  * 创建日期:2025-5-6
	  * @return java.lang.String
	  */
	public java.lang.String getPKFieldName() {
			
		return "pk_goodscode";
	}
    
	/**
	 * <p>返回表名称
	 * <p>
	 * 创建日期:2025-5-6
	 * @return java.lang.String
	 */
	public java.lang.String getTableName() {
		return "bd_goodscode";
	}    
	
	/**
	 * <p>返回表名称.
	 * <p>
	 * 创建日期:2025-5-6
	 * @return java.lang.String
	 */
	public static java.lang.String getDefaultTableName() {
		return "bd_goodscode";
	}    
    
    /**
	  * 按照默认方式创建构造子.
	  *
	  * 创建日期:2025-5-6
	  */
     public GoodscodeVO() {
		super();	
	}    
	
	
	@nc.vo.annotation.MDEntityInfo(beanFullclassName = "nc.vo.bd.goodscode.GoodscodeVO" )
	public IVOMeta getMetaData() {
		return VOMetaFactory.getInstance().getVOMeta("uap.goodscode");
		
   	}
     
}