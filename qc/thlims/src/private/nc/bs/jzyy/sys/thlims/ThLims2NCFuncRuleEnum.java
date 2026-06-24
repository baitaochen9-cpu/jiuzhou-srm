package nc.bs.jzyy.sys.thlims;


import nc.bs.jzyy.sys.AbstracAdapter4Ext;
import nc.bs.jzyy.sys.thlims2nc.TH_LIMS_Q_MATERIAL;
import nc.bs.jzyy.sys.thlims2nc.TH_LIMS_Q_ONHAND;
import nc.bs.jzyy.sys.thlims2nc.TH_LIMS_ICSTATE_CHANGE;

public enum ThLims2NCFuncRuleEnum {

	/**
	 * 物料档案查询接口
	 * */
	TH_LIMS_Q_MATERIAL("TH_LIMS_Q_MATERIAL",new TH_LIMS_Q_MATERIAL()),
	
	
	/**
	 * 现存量查询接口
	 * */
	TH_LIMS_Q_ONHAND("TH_LIMS_Q_ONHAND",new TH_LIMS_Q_ONHAND()),
	
	
	/**
	 * 库存状态调整
	 * */
	TH_LIMS_ICSTATE_CHANGE("TH_LIMS_ICSTATE_CHANGE",new TH_LIMS_ICSTATE_CHANGE()),
	
	;
	public String name;
	public AbstracAdapter4Ext adapter1;
	
	ThLims2NCFuncRuleEnum(String name,AbstracAdapter4Ext adapter1){
		this.name=name;
		this.adapter1 =adapter1;
	}
	
	
	/**
	 * 根据单据类型匹配到对应的业务处理类
	 * @param name
	 * @return
	 */
	public static ThLims2NCFuncRuleEnum  match(String name){
		ThLims2NCFuncRuleEnum[] values = ThLims2NCFuncRuleEnum.values();
		for(ThLims2NCFuncRuleEnum value:values){
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
