package nc.vo.ewm.workorder.vochange.event;

import java.util.HashMap;
import java.util.Map;

import nc.bs.logging.Logger;
import nc.itf.aim.pub.IWarContractService;
import nc.vo.am.common.AbstractAggBill;
import nc.vo.am.common.BizContext;
import nc.vo.am.proxy.AMProxy;
import nc.vo.eampub.gennext.event.IGenNextEvent;
import nc.vo.ewm.workorder.WorkOrderHeadVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;

public class DefaultMakeUpWOValueEvent implements IGenNextEvent {

	/** 用于返回的有效的聚合VO映射(已设置默认值) */
	private Map<String, AbstractAggBill> availableVOs = null;
	
	@Override
	public void processFunc(Map<String, AbstractAggBill> billVOMap) throws BusinessException {
		availableVOs = billVOMap;
		setFlag();
	}
	
	/**
	 * 设置是否在保信息
	 */
	public void setFlag(){
		
		//key:设备主键  value:当前日期
		Map<String,UFDate> pk_equipDateMap = new HashMap<String,UFDate>();
		UFDate nowDate = BizContext.getInstance().getBizDate();
		for(AbstractAggBill billVO : availableVOs.values()) {
			CircularlyAccessibleValueObject headVO = billVO.getParentVO();
			Object pk_equip = headVO.getAttributeValue(WorkOrderHeadVO.PK_EQUIP);
			if(pk_equip == null){
				continue ;
			}
			pk_equipDateMap.put(pk_equip.toString(),nowDate);
		}
		if(pk_equipDateMap.size() == 0){
			//没有设备不设置是否在保的信息
			return ;
		}
		Map<String, UFBoolean> pk_equipIsWarrantMap;
		try {
			pk_equipIsWarrantMap = AMProxy.lookup(IWarContractService.class).isEquipsWarrant(pk_equipDateMap);
		} catch (BusinessException e) {
			Logger.error("查询设备是否在保时出错");//在保字段默认为空
			return ;
		}
		for(AbstractAggBill billVO : availableVOs.values()) {
			CircularlyAccessibleValueObject headVO = billVO.getParentVO();
			Object pk_equip = headVO.getAttributeValue(WorkOrderHeadVO.PK_EQUIP);
			if(pk_equip == null){
				continue ;
			}
			headVO.setAttributeValue(WorkOrderHeadVO.WARRANT_FLAG,pk_equipIsWarrantMap.get(pk_equip.toString()));
		}
	}

	@Override
	public Map<String, AbstractAggBill> getAvailableAggVOs() {
		return availableVOs;
	}

	@Override
	public Map<String, AbstractAggBill> getUnAvailableAggVOs() {
		return null;
	}

}
