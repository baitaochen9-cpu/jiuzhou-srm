package nc.vo.bd.rack;

import nc.vo.pub.*;
import nc.vo.pubapp.pattern.model.meta.entity.vo.VOMetaFactory;

/**
 * <b> 此处简要描述此类功能 </b>
 * <p>
 *   此处添加类的描述信息
 * </p>
 *  创建日期:2021-3-1
 * @author 
 * @version NCPrj ??
 */
public class RackVO extends nc.vo.pub.SuperVO{
	
    private java.lang.String pk_rack;
    private java.lang.String pk_group;
    private java.lang.String pk_org;
    private java.lang.String pk_stordoc;
    private java.lang.String code;
    private java.lang.String name;
    private java.lang.String name2;
    private java.lang.String name3;
    private java.lang.String name4;
    private java.lang.String name5;
    private java.lang.String name6;
    private java.lang.String pk_parent;
    private java.lang.String pk_psndoc;
    private nc.vo.pub.lang.UFDouble volume;
    private nc.vo.pub.lang.UFBoolean isrmplace;
    private java.lang.Integer outpriority;
    private java.lang.Integer inpriority;
    private nc.vo.pub.lang.UFBoolean ischecked;
    private java.lang.String memo;
    private java.lang.Integer enablestate;
    private java.lang.String creator;
    private nc.vo.pub.lang.UFDateTime creationtime;
    private java.lang.String modifier;
    private nc.vo.pub.lang.UFDateTime modifiedtime;
    private java.lang.String innercode;
    private java.lang.Integer dataoriginflag;
    private nc.vo.pub.lang.UFBoolean isrestore;
    private java.lang.Integer dr = 0;
    private nc.vo.pub.lang.UFDateTime ts;    
	
	
    public static final String PK_RACK = "pk_rack";
    public static final String PK_GROUP = "pk_group";
    public static final String PK_ORG = "pk_org";
    public static final String PK_STORDOC = "pk_stordoc";
    public static final String CODE = "code";
    public static final String NAME = "name";
    public static final String NAME2 = "name2";
    public static final String NAME3 = "name3";
    public static final String NAME4 = "name4";
    public static final String NAME5 = "name5";
    public static final String NAME6 = "name6";
    public static final String PK_PARENT = "pk_parent";
    public static final String PK_PSNDOC = "pk_psndoc";
    public static final String VOLUME = "volume";
    public static final String ISRMPLACE = "isrmplace";
    public static final String OUTPRIORITY = "outpriority";
    public static final String INPRIORITY = "inpriority";
    public static final String ISCHECKED = "ischecked";
    public static final String MEMO = "memo";
    public static final String ENABLESTATE = "enablestate";
    public static final String CREATOR = "creator";
    public static final String CREATIONTIME = "creationtime";
    public static final String MODIFIER = "modifier";
    public static final String MODIFIEDTIME = "modifiedtime";
    public static final String INNERCODE = "innercode";
    public static final String DATAORIGINFLAG = "dataoriginflag";
    public static final String ISRESTORE = "isrestore";

	/**
	 * 属性 pk_rack的Getter方法.属性名：货位主键
	 *  创建日期:2021-3-1
	 * @return java.lang.String
	 */
	public java.lang.String getPk_rack () {
		return pk_rack;
	}   
	/**
	 * 属性pk_rack的Setter方法.属性名：货位主键
	 * 创建日期:2021-3-1
	 * @param newPk_rack java.lang.String
	 */
	public void setPk_rack (java.lang.String newPk_rack ) {
	 	this.pk_rack = newPk_rack;
	} 	 
	
	/**
	 * 属性 pk_group的Getter方法.属性名：所属集团
	 *  创建日期:2021-3-1
	 * @return java.lang.String
	 */
	public java.lang.String getPk_group () {
		return pk_group;
	}   
	/**
	 * 属性pk_group的Setter方法.属性名：所属集团
	 * 创建日期:2021-3-1
	 * @param newPk_group java.lang.String
	 */
	public void setPk_group (java.lang.String newPk_group ) {
	 	this.pk_group = newPk_group;
	} 	 
	
	/**
	 * 属性 pk_org的Getter方法.属性名：所属组织
	 *  创建日期:2021-3-1
	 * @return java.lang.String
	 */
	public java.lang.String getPk_org () {
		return pk_org;
	}   
	/**
	 * 属性pk_org的Setter方法.属性名：所属组织
	 * 创建日期:2021-3-1
	 * @param newPk_org java.lang.String
	 */
	public void setPk_org (java.lang.String newPk_org ) {
	 	this.pk_org = newPk_org;
	} 	 
	
	/**
	 * 属性 pk_stordoc的Getter方法.属性名：所属仓库
	 *  创建日期:2021-3-1
	 * @return java.lang.String
	 */
	public java.lang.String getPk_stordoc () {
		return pk_stordoc;
	}   
	/**
	 * 属性pk_stordoc的Setter方法.属性名：所属仓库
	 * 创建日期:2021-3-1
	 * @param newPk_stordoc java.lang.String
	 */
	public void setPk_stordoc (java.lang.String newPk_stordoc ) {
	 	this.pk_stordoc = newPk_stordoc;
	} 	 
	
	/**
	 * 属性 code的Getter方法.属性名：货位号
	 *  创建日期:2021-3-1
	 * @return java.lang.String
	 */
	public java.lang.String getCode () {
		return code;
	}   
	/**
	 * 属性code的Setter方法.属性名：货位号
	 * 创建日期:2021-3-1
	 * @param newCode java.lang.String
	 */
	public void setCode (java.lang.String newCode ) {
	 	this.code = newCode;
	} 	 
	
	/**
	 * 属性 name的Getter方法.属性名：null
	 *  创建日期:2021-3-1
	 * @return java.lang.String
	 */
	public java.lang.String getName () {
		return name;
	}   
	/**
	 * 属性name的Setter方法.属性名：null
	 * 创建日期:2021-3-1
	 * @param newName java.lang.String
	 */
	public void setName (java.lang.String newName ) {
	 	this.name = newName;
	} 	 
	
	/**
	 * 属性 name2的Getter方法.属性名：null
	 *  创建日期:2021-3-1
	 * @return java.lang.String
	 */
	public java.lang.String getName2 () {
		return name2;
	}   
	/**
	 * 属性name2的Setter方法.属性名：null
	 * 创建日期:2021-3-1
	 * @param newName2 java.lang.String
	 */
	public void setName2 (java.lang.String newName2 ) {
	 	this.name2 = newName2;
	} 	 
	
	/**
	 * 属性 name3的Getter方法.属性名：null
	 *  创建日期:2021-3-1
	 * @return java.lang.String
	 */
	public java.lang.String getName3 () {
		return name3;
	}   
	/**
	 * 属性name3的Setter方法.属性名：null
	 * 创建日期:2021-3-1
	 * @param newName3 java.lang.String
	 */
	public void setName3 (java.lang.String newName3 ) {
	 	this.name3 = newName3;
	} 	 
	
	/**
	 * 属性 name4的Getter方法.属性名：null
	 *  创建日期:2021-3-1
	 * @return java.lang.String
	 */
	public java.lang.String getName4 () {
		return name4;
	}   
	/**
	 * 属性name4的Setter方法.属性名：null
	 * 创建日期:2021-3-1
	 * @param newName4 java.lang.String
	 */
	public void setName4 (java.lang.String newName4 ) {
	 	this.name4 = newName4;
	} 	 
	
	/**
	 * 属性 name5的Getter方法.属性名：null
	 *  创建日期:2021-3-1
	 * @return java.lang.String
	 */
	public java.lang.String getName5 () {
		return name5;
	}   
	/**
	 * 属性name5的Setter方法.属性名：null
	 * 创建日期:2021-3-1
	 * @param newName5 java.lang.String
	 */
	public void setName5 (java.lang.String newName5 ) {
	 	this.name5 = newName5;
	} 	 
	
	/**
	 * 属性 name6的Getter方法.属性名：null
	 *  创建日期:2021-3-1
	 * @return java.lang.String
	 */
	public java.lang.String getName6 () {
		return name6;
	}   
	/**
	 * 属性name6的Setter方法.属性名：null
	 * 创建日期:2021-3-1
	 * @param newName6 java.lang.String
	 */
	public void setName6 (java.lang.String newName6 ) {
	 	this.name6 = newName6;
	} 	 
	
	/**
	 * 属性 pk_parent的Getter方法.属性名：上级货位
	 *  创建日期:2021-3-1
	 * @return java.lang.String
	 */
	public java.lang.String getPk_parent () {
		return pk_parent;
	}   
	/**
	 * 属性pk_parent的Setter方法.属性名：上级货位
	 * 创建日期:2021-3-1
	 * @param newPk_parent java.lang.String
	 */
	public void setPk_parent (java.lang.String newPk_parent ) {
	 	this.pk_parent = newPk_parent;
	} 	 
	
	/**
	 * 属性 pk_psndoc的Getter方法.属性名：保管员
	 *  创建日期:2021-3-1
	 * @return java.lang.String
	 */
	public java.lang.String getPk_psndoc () {
		return pk_psndoc;
	}   
	/**
	 * 属性pk_psndoc的Setter方法.属性名：保管员
	 * 创建日期:2021-3-1
	 * @param newPk_psndoc java.lang.String
	 */
	public void setPk_psndoc (java.lang.String newPk_psndoc ) {
	 	this.pk_psndoc = newPk_psndoc;
	} 	 
	
	/**
	 * 属性 volume的Getter方法.属性名：货位容积
	 *  创建日期:2021-3-1
	 * @return nc.vo.pub.lang.UFDouble
	 */
	public nc.vo.pub.lang.UFDouble getVolume () {
		return volume;
	}   
	/**
	 * 属性volume的Setter方法.属性名：货位容积
	 * 创建日期:2021-3-1
	 * @param newVolume nc.vo.pub.lang.UFDouble
	 */
	public void setVolume (nc.vo.pub.lang.UFDouble newVolume ) {
	 	this.volume = newVolume;
	} 	 
	
	/**
	 * 属性 isrmplace的Getter方法.属性名：在途货位
	 *  创建日期:2021-3-1
	 * @return nc.vo.pub.lang.UFBoolean
	 */
	public nc.vo.pub.lang.UFBoolean getIsrmplace () {
		return isrmplace;
	}   
	/**
	 * 属性isrmplace的Setter方法.属性名：在途货位
	 * 创建日期:2021-3-1
	 * @param newIsrmplace nc.vo.pub.lang.UFBoolean
	 */
	public void setIsrmplace (nc.vo.pub.lang.UFBoolean newIsrmplace ) {
	 	this.isrmplace = newIsrmplace;
	} 	 
	
	/**
	 * 属性 outpriority的Getter方法.属性名：出库优先级
	 *  创建日期:2021-3-1
	 * @return java.lang.Integer
	 */
	public java.lang.Integer getOutpriority () {
		return outpriority;
	}   
	/**
	 * 属性outpriority的Setter方法.属性名：出库优先级
	 * 创建日期:2021-3-1
	 * @param newOutpriority java.lang.Integer
	 */
	public void setOutpriority (java.lang.Integer newOutpriority ) {
	 	this.outpriority = newOutpriority;
	} 	 
	
	/**
	 * 属性 inpriority的Getter方法.属性名：入库优先级
	 *  创建日期:2021-3-1
	 * @return java.lang.Integer
	 */
	public java.lang.Integer getInpriority () {
		return inpriority;
	}   
	/**
	 * 属性inpriority的Setter方法.属性名：入库优先级
	 * 创建日期:2021-3-1
	 * @param newInpriority java.lang.Integer
	 */
	public void setInpriority (java.lang.Integer newInpriority ) {
	 	this.inpriority = newInpriority;
	} 	 
	
	/**
	 * 属性 ischecked的Getter方法.属性名：检验区货位
	 *  创建日期:2021-3-1
	 * @return nc.vo.pub.lang.UFBoolean
	 */
	public nc.vo.pub.lang.UFBoolean getIschecked () {
		return ischecked;
	}   
	/**
	 * 属性ischecked的Setter方法.属性名：检验区货位
	 * 创建日期:2021-3-1
	 * @param newIschecked nc.vo.pub.lang.UFBoolean
	 */
	public void setIschecked (nc.vo.pub.lang.UFBoolean newIschecked ) {
	 	this.ischecked = newIschecked;
	} 	 
	
	/**
	 * 属性 memo的Getter方法.属性名：备注
	 *  创建日期:2021-3-1
	 * @return java.lang.String
	 */
	public java.lang.String getMemo () {
		return memo;
	}   
	/**
	 * 属性memo的Setter方法.属性名：备注
	 * 创建日期:2021-3-1
	 * @param newMemo java.lang.String
	 */
	public void setMemo (java.lang.String newMemo ) {
	 	this.memo = newMemo;
	} 	 
	
	/**
	 * 属性 enablestate的Getter方法.属性名：启用状态
	 *  创建日期:2021-3-1
	 * @return java.lang.Integer
	 */
	public java.lang.Integer getEnablestate () {
		return enablestate;
	}   
	/**
	 * 属性enablestate的Setter方法.属性名：启用状态
	 * 创建日期:2021-3-1
	 * @param newEnablestate java.lang.Integer
	 */
	public void setEnablestate (java.lang.Integer newEnablestate ) {
	 	this.enablestate = newEnablestate;
	} 	 
	
	/**
	 * 属性 creator的Getter方法.属性名：创建人
	 *  创建日期:2021-3-1
	 * @return java.lang.String
	 */
	public java.lang.String getCreator () {
		return creator;
	}   
	/**
	 * 属性creator的Setter方法.属性名：创建人
	 * 创建日期:2021-3-1
	 * @param newCreator java.lang.String
	 */
	public void setCreator (java.lang.String newCreator ) {
	 	this.creator = newCreator;
	} 	 
	
	/**
	 * 属性 creationtime的Getter方法.属性名：创建时间
	 *  创建日期:2021-3-1
	 * @return nc.vo.pub.lang.UFDateTime
	 */
	public nc.vo.pub.lang.UFDateTime getCreationtime () {
		return creationtime;
	}   
	/**
	 * 属性creationtime的Setter方法.属性名：创建时间
	 * 创建日期:2021-3-1
	 * @param newCreationtime nc.vo.pub.lang.UFDateTime
	 */
	public void setCreationtime (nc.vo.pub.lang.UFDateTime newCreationtime ) {
	 	this.creationtime = newCreationtime;
	} 	 
	
	/**
	 * 属性 modifier的Getter方法.属性名：最后修改人
	 *  创建日期:2021-3-1
	 * @return java.lang.String
	 */
	public java.lang.String getModifier () {
		return modifier;
	}   
	/**
	 * 属性modifier的Setter方法.属性名：最后修改人
	 * 创建日期:2021-3-1
	 * @param newModifier java.lang.String
	 */
	public void setModifier (java.lang.String newModifier ) {
	 	this.modifier = newModifier;
	} 	 
	
	/**
	 * 属性 modifiedtime的Getter方法.属性名：最后修改时间
	 *  创建日期:2021-3-1
	 * @return nc.vo.pub.lang.UFDateTime
	 */
	public nc.vo.pub.lang.UFDateTime getModifiedtime () {
		return modifiedtime;
	}   
	/**
	 * 属性modifiedtime的Setter方法.属性名：最后修改时间
	 * 创建日期:2021-3-1
	 * @param newModifiedtime nc.vo.pub.lang.UFDateTime
	 */
	public void setModifiedtime (nc.vo.pub.lang.UFDateTime newModifiedtime ) {
	 	this.modifiedtime = newModifiedtime;
	} 	 
	
	/**
	 * 属性 innercode的Getter方法.属性名：内部编码
	 *  创建日期:2021-3-1
	 * @return java.lang.String
	 */
	public java.lang.String getInnercode () {
		return innercode;
	}   
	/**
	 * 属性innercode的Setter方法.属性名：内部编码
	 * 创建日期:2021-3-1
	 * @param newInnercode java.lang.String
	 */
	public void setInnercode (java.lang.String newInnercode ) {
	 	this.innercode = newInnercode;
	} 	 
	
	/**
	 * 属性 dataoriginflag的Getter方法.属性名：分布式
	 *  创建日期:2021-3-1
	 * @return java.lang.Integer
	 */
	public java.lang.Integer getDataoriginflag () {
		return dataoriginflag;
	}   
	/**
	 * 属性dataoriginflag的Setter方法.属性名：分布式
	 * 创建日期:2021-3-1
	 * @param newDataoriginflag java.lang.Integer
	 */
	public void setDataoriginflag (java.lang.Integer newDataoriginflag ) {
	 	this.dataoriginflag = newDataoriginflag;
	} 	 
	
	/**
	 * 属性 isrestore的Getter方法.属性名：是否重复入库
	 *  创建日期:2021-3-1
	 * @return nc.vo.pub.lang.UFBoolean
	 */
	public nc.vo.pub.lang.UFBoolean getIsrestore () {
		return isrestore;
	}   
	/**
	 * 属性isrestore的Setter方法.属性名：是否重复入库
	 * 创建日期:2021-3-1
	 * @param newIsrestore nc.vo.pub.lang.UFBoolean
	 */
	public void setIsrestore (nc.vo.pub.lang.UFBoolean newIsrestore ) {
	 	this.isrestore = newIsrestore;
	} 	 
	
	/**
	 * 属性 dr的Getter方法.属性名：dr
	 *  创建日期:2021-3-1
	 * @return java.lang.Integer
	 */
	public java.lang.Integer getDr () {
		return dr;
	}   
	/**
	 * 属性dr的Setter方法.属性名：dr
	 * 创建日期:2021-3-1
	 * @param newDr java.lang.Integer
	 */
	public void setDr (java.lang.Integer newDr ) {
	 	this.dr = newDr;
	} 	 
	
	/**
	 * 属性 ts的Getter方法.属性名：ts
	 *  创建日期:2021-3-1
	 * @return nc.vo.pub.lang.UFDateTime
	 */
	public nc.vo.pub.lang.UFDateTime getTs () {
		return ts;
	}   
	/**
	 * 属性ts的Setter方法.属性名：ts
	 * 创建日期:2021-3-1
	 * @param newTs nc.vo.pub.lang.UFDateTime
	 */
	public void setTs (nc.vo.pub.lang.UFDateTime newTs ) {
	 	this.ts = newTs;
	} 	 
	
	
	/**
	  * <p>取得父VO主键字段.
	  * <p>
	  * 创建日期:2021-3-1
	  * @return java.lang.String
	  */
	public java.lang.String getParentPKFieldName() {
	    return null;
	}   
    
	/**
	  * <p>取得表主键.
	  * <p>
	  * 创建日期:2021-3-1
	  * @return java.lang.String
	  */
	public java.lang.String getPKFieldName() {
			
		return "pk_rack";
	}
    
	/**
	 * <p>返回表名称
	 * <p>
	 * 创建日期:2021-3-1
	 * @return java.lang.String
	 */
	public java.lang.String getTableName() {
		return "bd_rack";
	}    
	
	/**
	 * <p>返回表名称.
	 * <p>
	 * 创建日期:2021-3-1
	 * @return java.lang.String
	 */
	public static java.lang.String getDefaultTableName() {
		return "bd_rack";
	}    
    
    /**
	  * 按照默认方式创建构造子.
	  *
	  * 创建日期:2021-3-1
	  */
     public RackVO() {
		super();	
	}    
	
	
	@nc.vo.annotation.MDEntityInfo(beanFullclassName = "nc.vo.bd.rack.RackVO" )
	public IVOMeta getMetaData() {
		return VOMetaFactory.getInstance().getVOMeta("uap.rack");
		
   	}
     
}