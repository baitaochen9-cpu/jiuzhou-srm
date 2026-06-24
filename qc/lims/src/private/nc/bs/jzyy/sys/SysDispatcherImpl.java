package nc.bs.jzyy.sys;

import java.util.Map;

import nc.bs.jzyy.sys.lims.iccheck.IccheckCancelPlugin;
import nc.bs.jzyy.sys.lims.iccheck.IccheckUpPlugin;
import nc.bs.jzyy.sys.lims.logger.LIMSLogerProcessor;
import nc.bs.jzyy.sys.lims.mmpaccheck.MmpaccheckCancelPlugin;
import nc.bs.jzyy.sys.lims.mmpaccheck.MmpaccheckUpPlugin;
import nc.bs.jzyy.sys.lims.mmwrcheck.MmwrISCHECKPlugin;
import nc.bs.jzyy.sys.lims.mmwrcheck.MmwrcheckCancelPlugin;
import nc.bs.jzyy.sys.lims.mmwrcheck.MmwrcheckUpPlugin;
import nc.bs.jzyy.sys.lims.pucheck.PucheckCancelPlugin;
import nc.bs.jzyy.sys.lims.pucheck.PucheckUpPlugin;
import nc.bs.jzyy.sys.oa.NC2OADisPatcher;
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
		// LIMS日志
		if ("LIMS_SYS_LOGGER".equalsIgnoreCase(billltypecode)) {
			new LIMSLogerProcessor().deal(o, param);
		}

		if ("SYS_DISPATCHER".equalsIgnoreCase(billltypecode)) {
			return new SYS_DISPATCHER().process(billltypecode, (JSONObject) o,
					param);
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
		
		// 采购到货报检
		if ("LIMS_PU_CHECK".equalsIgnoreCase(billltypecode)) {
			PucheckUpPlugin plugin = new PucheckUpPlugin();
			return plugin.sys(billltypecode, obj, param);
		}

		// 采购到货报检撤销
		if ("LIMS_PU_CHECK_CANCEL".equalsIgnoreCase(billltypecode)) {
			PucheckCancelPlugin plugin = new PucheckCancelPlugin();
			return plugin.sys(billltypecode, obj, param);
		}

		// 库存检验冻结单报检
		if ("LIMS_IC_CHECK".equalsIgnoreCase(billltypecode)) {
			IccheckUpPlugin plugin = new IccheckUpPlugin();
			return plugin.sys(billltypecode, obj, param);
		}

		// 库存检验冻结单报检撤销
		if ("LIMS_IC_CHECK_CANCEL".equalsIgnoreCase(billltypecode)) {
			IccheckCancelPlugin plugin = new IccheckCancelPlugin();
			return plugin.sys(billltypecode, obj, param);
		}

		// 流程生产订单报检
		if ("LIMS_MM_CHECK".equalsIgnoreCase(billltypecode)) {
	
			MmpaccheckUpPlugin plugin = new MmpaccheckUpPlugin();
			return plugin.sys(billltypecode, obj, param);
		}

		// 流程生产订单报检撤销
		if ("LIMS_MM_CHECK_CANCEL".equalsIgnoreCase(billltypecode)) {
		
			
			MmpaccheckCancelPlugin plugin = new MmpaccheckCancelPlugin();
			return plugin.sys(billltypecode, obj, param);
		}

		// 生产报告回写
		if ("LIMS_WR_CHECK".equalsIgnoreCase(billltypecode)) {
			MmwrcheckUpPlugin plugin = new MmwrcheckUpPlugin();
			return plugin.sys(billltypecode, obj, param);
		}

		// 生产报告回写撤销
		if ("LIMS_WR_CHECK_CANCEL".equalsIgnoreCase(billltypecode)) {
			;
			
			MmwrcheckCancelPlugin plugin = new MmwrcheckCancelPlugin();
			return plugin.sys(billltypecode, obj, param);
		}
		
		// 生产报告校验是否已经完成
		if ("LIMS_WR_ISCHECK".equalsIgnoreCase(billltypecode)) {
			MmwrISCHECKPlugin plugin = new MmwrISCHECKPlugin();
			return plugin.sys(billltypecode, obj, param);
		}
		
		/**
		 * NC同步OA 逻辑统一处理
		 */
		if(billltypecode.startsWith("OA_")){
			return new NC2OADisPatcher().dispatch(obj, billltypecode, param);
		}	
		
		return rs;
	}
	
	
	

}
