package nc.bs.srm.sys;

import java.util.Map;

import nc.bs.logging.Logger;
import nc.bs.srm.bd.HS_Material;
import nc.bs.srm.bd.SRM_Suppiler;
import nc.bs.srm.ic.po.PoOrder_rows;
import nc.bs.srm.ic.po.PoOrder_update;
import nc.bs.srm.ic.po.SRM_PoOrderOfOther;
import nc.bs.srm.ic.po.SRM_PoOrder_del;
import nc.bs.srm.ic.po.file.SRM_Poorder_fileadd;
import nc.vo.scmpub.api.rest.utils.RestUtils;
import nccloud.api.jzsrm.AbstracProcessor4Ext;
import nccloud.base.exception.BusinessException;

import org.json.JSONString;

import com.alibaba.fastjson.JSONObject;

/**
 * 
 * @author liyf 主要用来识别外系统业务标志，分发到不同的业务组件中进行
 *
 */
public class SYS_DISPATCHER {

	public JSONString process(String billltypecode, JSONObject rqJsonObj, Map <String, Object> otherpms)
			throws BusinessException {
		// 处理返回结果
		JSONObject rs = null;
		String funcType = rqJsonObj.getString("functype");
		try {
			AbstracProcessor4Ext ap = null;
			//采购订单新增  SRM_PoOrder_del
			if ("SRM_ORDER_ADD".equalsIgnoreCase(funcType)) {
				ap = new SRM_PoOrderOfOther();
				rs = (JSONObject) ap.process(rqJsonObj);
			}
			//采购订删除  SRM_PoOrder_del
			if ("SRM_ORDER_DEL".equalsIgnoreCase(funcType)) {
				ap = new SRM_PoOrder_del();
				rs = (JSONObject) ap.process(rqJsonObj);
			}
			//采购订单修改
			if ("SRM_ORDER_MOD".equalsIgnoreCase(funcType)) {
				ap = new PoOrder_update();
				rs = (JSONObject) ap.process(rqJsonObj);
			}
			//采购订单修改行状态
			if ("SRM_ORDER_STATUS".equalsIgnoreCase(funcType)) {
				ap = new PoOrder_rows();
				rs = (JSONObject) ap.process(rqJsonObj);
			}
			//供应商新增与修改
			if ("SRM_SUPPILER_ADD_UP".equalsIgnoreCase(funcType)) {
				ap = new SRM_Suppiler();
				rs = (JSONObject) ap.process(rqJsonObj);
			}
			//采购订单-新增附件接口
			if ("SRM_POORDER_FILE_ADD".equalsIgnoreCase(funcType)) {
				ap = new SRM_Poorder_fileadd();
				rs = (JSONObject) ap.process(rqJsonObj);
			}
			//HS信息后回写ERP物料档案
			if ("material_update".equalsIgnoreCase(funcType)) {
				ap = new HS_Material();
				rs = (JSONObject) ap.process(rqJsonObj);
			}
		} catch (Exception e) { 
			Logger.error(e.getMessage(), e);
			throw new BusinessException(e.getMessage());
		}
		return RestUtils.toJSONString(rs);
	}

}
