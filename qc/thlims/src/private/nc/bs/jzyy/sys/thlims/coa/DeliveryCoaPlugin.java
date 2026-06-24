package nc.bs.jzyy.sys.thlims.coa;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.bs.framework.common.NCLocator;
import nc.bs.jzyy.sys.thlims.THLimsLogVO;
import nc.bs.jzyy.sys.thlims.out.AbstractSender4ThLims;
import nc.impl.pubapp.pattern.data.bill.BillQuery;
import nc.itf.jzyy.sys.thlims.ISysDispatcherThLims;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.so.m4331.entity.DeliveryBVO;
import nc.vo.so.m4331.entity.DeliveryVO;

import com.alibaba.fastjson.JSONObject;

public class DeliveryCoaPlugin {

	BaseDAO dao;
	public BaseDAO getDao() {
		if (dao == null) {
			dao = new BaseDAO();
		}
		return dao;
	}

	// 判断是否同步
	public Object sys(String billltypecode, Object obj,Map<String, Object> otherpms) throws BusinessException {
		
		DeliveryVO bill =(DeliveryVO) ((DeliveryVO)obj).clone();
		DeliveryBVO[] bvos= bill.getChildrenVO();
		//物料+批次号合并
		Map<String,DeliveryBVO> groupMap=new HashMap<String, DeliveryBVO>();
		//行PK
		Map<String,ArrayList<String>> pkMapList=new HashMap<String, ArrayList<String>>();
		//1. 物料+批次号汇总
		for (DeliveryBVO bvo : bvos) {
			//行pk 
			String key=bvo.getCmaterialid()+bvo.getVbatchcode();
			if(groupMap.containsKey(key)){
				DeliveryBVO itemVO = groupMap.get(key);
				itemVO.setNnum(itemVO.getNnum().add(bvo.getNnum()));
				itemVO.setNastnum(itemVO.getNastnum().add(bvo.getNastnum()));
			}else{
				groupMap.put(key, bvo);
			}
			
			if(pkMapList.containsKey(key)){
				pkMapList.get(key).add(bvo.getPrimaryKey());
			}else{
				ArrayList<String> pkList=new ArrayList<String>();
				pkList.add(bvo.getPrimaryKey());
				
				pkMapList.put(key, pkList);
			}
		}
		
		otherpms.put("pkList", pkMapList);
		otherpms.put("vo", (DeliveryVO)  obj);
		
		//逐行请求
		for (DeliveryBVO deliveryBVO : groupMap.values()) {
			bill.setChildrenVO(new DeliveryBVO[]{deliveryBVO});
			// 执行同步
			process(billltypecode, bill, otherpms);
		}
		BillQuery<DeliveryVO> qry = new BillQuery<DeliveryVO>(DeliveryVO.class);
		return qry.query(new String[]{bill.getPrimaryKey()})[0];
	}

	private Object process(String billltypecode, Object obj,
			Map<String, Object> otherpms) throws BusinessException {
		AbstractSender4ThLims sender = new DeliveryCoaSender();
		if(otherpms == null ){
			otherpms = new HashMap<String, Object>();
		}
		JSONObject resp=null;
		try {
			resp = (JSONObject) sender.process(billltypecode, obj,otherpms);
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessException(e.getMessage());
		}
		THLimsLogVO limsLogVO = sender.getLimsLogVO();
		if(null!=limsLogVO){
			this.addNewLog(limsLogVO);
		}
		return resp;
	}

	private ISysDispatcherThLims pross;
	private ISysDispatcherThLims getPross() {
		if(pross == null){
			pross = NCLocator.getInstance().lookup(ISysDispatcherThLims.class);
		}
		return pross;
	}
	
	private void addNewLog(SuperVO obj) {
		 try {
			this.getPross().dispatch_RequiresNew(obj, "LIMS_SYS_LOGGER", null);
		} catch (BusinessException e) {
			e.printStackTrace();
		}
	}
}
