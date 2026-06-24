package nc.bs.jzyy.sys.thlims;

import java.util.Map;

import nc.bs.jzyy.sys.AbstracAdapter4Ext;
import nc.bs.jzyy.sys.lims.logger.LIMSLogerProcessor;
import nc.bs.jzyy.sys.thlims.coa.DeliveryCoaPlugin;
import nc.bs.jzyy.sys.thlims.iccheck.IccheckUpPlugin;
import nc.bs.jzyy.sys.thlims.mmpacipcheck.IpcheckUpPlugin;
import nc.bs.jzyy.sys.thlims.mmwrcheck.MmwrcheckUpPlugin;
import nc.bs.jzyy.sys.thlims.pucheck.PucheckCancelPlugin;
import nc.bs.jzyy.sys.thlims.pucheck.PucheckUpPlugin;
import nc.itf.jzyy.sys.thlims.ISysDispatcherThLims;
import nc.vo.pub.BusinessException;

import com.alibaba.fastjson.JSONObject;

public class SysDispatcherImplThLims implements ISysDispatcherThLims {

	/*
	 * 
	 * */
	@Override
	public Object dispatch_RequiresNew(Object o, String billltypecode,
			Map<String, Object> param) throws BusinessException {
		Object rs = null;

		// LIMS日志
		if ("LIMS_SYS_LOGGER".equalsIgnoreCase(billltypecode)) {
			new LIMSLogerProcessor().deal(o, param);
		}

		//LIMS同步到NC的处理
		if ("THLIM_NC".equalsIgnoreCase(billltypecode)) {
			JSONObject rqJsonObj = (JSONObject) o;
			 String funcType = rqJsonObj.getString("functype");
			AbstracAdapter4Ext adpter = null;
			ThLims2NCFuncRuleEnum ruleEnum = ThLims2NCFuncRuleEnum.match(funcType);
			if(ruleEnum != null ){
				adpter =ruleEnum.getAdapter1();
			}
		
			if(adpter == null){
				throw new BusinessException("未支持的业务功能 functype="+funcType);
			}
			//
			rs= (JSONObject) adpter.process(rqJsonObj);
		}
		return rs;
	}
	
	/*
	 * ERP-NC 同步至 外系统
	 * */
	@Override
	public Object dispatch(Object obj, String billltypecode,
			Map<String, Object> param) throws BusinessException {

		Object rs = null;
		
		/*
		 * 到货质检调用
		 * */
		if ("TH_LIMS_PU_CHECK".equalsIgnoreCase(billltypecode)) {
			PucheckUpPlugin pucheckUpPlugin=new PucheckUpPlugin();
			return pucheckUpPlugin.sys(billltypecode, obj, param);
		}
		
		// 采购到货报检撤销
		if ("TH_LIMS_PU_CHECK_CANCEL".equalsIgnoreCase(billltypecode)) {
			PucheckCancelPlugin plugin = new PucheckCancelPlugin();
			return plugin.sys(billltypecode, obj, param);
		}
		
		/*
		 * 到货质检调用
		 * */
		if ("TH_LIMS_PU_CHECK".equalsIgnoreCase(billltypecode)) {
			PucheckUpPlugin pucheckUpPlugin=new PucheckUpPlugin();
			return pucheckUpPlugin.sys(billltypecode, obj, param);
		}
		
		
		// 库存检验冻结单报检
		if ("TH_LIMS_IC_CHECK".equalsIgnoreCase(billltypecode)) {
			IccheckUpPlugin plugin = new IccheckUpPlugin();
			return plugin.sys(billltypecode, obj, param);
		}

		
		//流程生产订单报检
		if ("TH_LIMS_MM_CHECK".equalsIgnoreCase(billltypecode)) {
			//MmpaccheckUpPlugin plugin = new MmpaccheckUpPlugin();
			//return plugin.sys(billltypecode, obj, param);
		}

		
		// 生产报告报检
		if ("TH_LIMS_WR_CHECK".equalsIgnoreCase(billltypecode)) {
			MmwrcheckUpPlugin plugin = new MmwrcheckUpPlugin();
			return plugin.sys(billltypecode, obj, param);
		}

		/*生产报告撤销
		if ("TH_LIMS_WR_CHECK_CANCEL".equalsIgnoreCase(billltypecode)) {
			//MmwrcheckCancelPlugin plugin = new MmwrcheckCancelPlugin();
			//return plugin.sys(billltypecode, obj, param);
			//ExceptionUtils.wrapBusinessException("该数据已经生成LIMS报检记录，请联系质量人员进行调整！");
		}*/
		
		// 外部COA
		if ("TH_LIMS_COA".equalsIgnoreCase(billltypecode)) {
			DeliveryCoaPlugin plugin = new DeliveryCoaPlugin();
			return plugin.sys(billltypecode, obj, param);
		}
		
		// 外部COA
		if ("TH_LIMS_IPC".equalsIgnoreCase(billltypecode)) {
			IpcheckUpPlugin plugin = new IpcheckUpPlugin();
			return plugin.sys(billltypecode, obj, param);
		}
		
		return rs;
		
	
	
		
	}
	
	
}
