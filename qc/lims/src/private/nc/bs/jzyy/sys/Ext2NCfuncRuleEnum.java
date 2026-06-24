package nc.bs.jzyy.sys;


import nc.bs.jzyy.sys.mdm.onhandnum.LIMS_ONHAND;
import nc.bs.jzyy.sys.mdm.productor.MDM_PRODUCTOR;
import nc.bs.jzyy.sys.mdm.turncard.MDM_TURNCARD;
import nc.bs.jzyy.sys.mdm.wuliao.MDM_WULIAO;


public enum Ext2NCfuncRuleEnum {
	
	
	/**
	 * MDM物料主数据查询接口
	 */
	MDM_WULIAO("MDM_WULIAO",new MDM_WULIAO()),
	

	/**
	 * MDM现存量查询接口
	 */
	
	LIMS_ONHAND("LIMS_ONHAND",new LIMS_ONHAND()),
	
	/**
	 * MDM翻牌
	 */
	MDM_TURNCARD("MDM_TURNCARD",new MDM_TURNCARD()),
	
	/**
	 * MDM生产厂商
	 */
	MDM_PRODUCTOR("MDM_PRODUCTOR", new MDM_PRODUCTOR()),
	
	
	
	
	;
	
	public String name;
	public AbstracAdapter4Ext adapter1;
	
	Ext2NCfuncRuleEnum(String name,AbstracAdapter4Ext adapter1){
		this.name=name;
		this.adapter1 =adapter1;
	}
	
	
	/**
	 * 根据单据类型匹配到对应的业务处理类
	 * @param name
	 * @return
	 */
	public static Ext2NCfuncRuleEnum  match(String name){
		Ext2NCfuncRuleEnum[] values = Ext2NCfuncRuleEnum.values();
		for(Ext2NCfuncRuleEnum value:values){
			 if(value.getName().equals(name)){
	                return value;
	            }
		}
		return null;
	}

	public String getName() {
		return name;
	}





	public void setName(String name) {
		this.name = name;
	}





	public AbstracAdapter4Ext getAdapter1() {
		return adapter1;
	}





	public void setAdapter1(AbstracAdapter4Ext adapter1) {
		this.adapter1 = adapter1;
	}

	
	
}
