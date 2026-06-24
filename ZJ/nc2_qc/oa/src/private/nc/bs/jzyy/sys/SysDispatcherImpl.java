package nc.bs.jzyy.sys;

import java.util.Map;

import nc.bs.jzyy.sys.oa.NC2OADisPatcher;
import nc.bs.jzyy.sys.oa.out.ct.CtPuSavePlugin;
import nc.bs.jzyy.sys.oa.out.pu.PoOrderSavePlugin;
import nc.bs.jzyy.sys.oa.out.pu.PoPayPlanPlugin;
import nc.itf.jzyy.sys.ISysDispatcher;
import nc.vo.pub.BusinessException;

import com.alibaba.fastjson.JSONObject;

public class SysDispatcherImpl implements ISysDispatcher {

	/*
	 * 外系统 同步至 ERP-NC
	 * */
	@Override
	public Object dispatch_RequiresNew(Object o, String billltypecode,
			Map<String, Object> param) throws BusinessException {
		Object rs = null;
        if ("SYS_DISPATCHER".equalsIgnoreCase(billltypecode)) {
			return new SYS_DISPATCHER().process(billltypecode, (JSONObject) o,
					param);
		}
		return rs;
	}
	
	@Override
	public Object dispatch(Object obj, String billltypecode,
			Map<String, Object> param) throws BusinessException {
		Object rs = null;
		//NC同步OA
		if(billltypecode.startsWith("OA_")){
			return new NC2OADisPatcher().dispatch(obj, billltypecode, param);
		}	

		//采购付款计划 2022年7月19日 xuchong 
		if ("OA_PAYPLAN".equalsIgnoreCase(billltypecode)) {
			PoPayPlanPlugin plugin = new PoPayPlanPlugin();
			return plugin.process(billltypecode, obj, param);
		}
		
		//采购订单单
		if ("OA_POORDER".equalsIgnoreCase(billltypecode)) {
			PoOrderSavePlugin plugin = new PoOrderSavePlugin();
			return plugin.process(billltypecode, obj, param);
		}	
		
		//采购合同单
		if ("OA_CT".equalsIgnoreCase(billltypecode)) {
			CtPuSavePlugin plugin = new CtPuSavePlugin();
			return plugin.process(billltypecode, obj, param);
		}		
		return rs;
	}
	private ProcessServiceImpl serviceImpl;
	private ProcessServiceImpl getServiceImpl(){
		if(null==serviceImpl){
			serviceImpl=new ProcessServiceImpl();
		}
		return serviceImpl;
	}
	
}
