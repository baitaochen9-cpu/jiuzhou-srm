package nc.api.rest.ct.utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import nc.ui.ct.util.CtRelationItemForCal;
import nc.ui.ct.util.UnitAndChangeRateUtil;
import nc.vo.ct.entity.CtAbstractBVO;
import nc.vo.ct.entity.CtAbstractVO;
import nc.vo.ct.pub.CTVatNameConst;
import nc.vo.ct.purdaily.entity.CtPuBVO;
import nc.vo.ct.saledaily.entity.CtSaleBVO;
import nc.vo.ct.util.RelationCalculateUtil;
import nc.vo.pubapp.calculator.Calculator;
import nc.vo.pubapp.calculator.Condition;
import nc.vo.pubapp.calculator.data.IDataSetForCal;
import nc.vo.pubapp.calculator.data.IRelationForItems;
import nc.vo.pubapp.scale.ScaleUtils;

/**
 * 合同计算单价 金额
 * @Description: 
 *   
 * @author: 刘伟
 * @date:   2019-4-26 上午9:16:02   
 * @version NCC1909
 */
public class CTRelationCalculateUtil {
	
	public void calculate(CtAbstractVO parentVO, CtAbstractBVO[] bvos){
		doCalculate(parentVO, bvos);
	}
	
	public void doCalculate(CtAbstractVO parentVO, CtAbstractBVO[] bvos) {
//		CtSaleVO parentVO = aggvo.getParentVO();
	    IRelationForItems item = new CtRelationItemForCal();
	    item.setNnetpriceKey(CtAbstractBVO.NGPRICE);
	    item.setNtaxnetpriceKey(CtAbstractBVO.NGTAXPRICE);

	    String pk_group = parentVO.getPk_group();
	    ScaleUtils scale = new ScaleUtils(pk_group);
	    boolean isTaxPrior = RelationCalculateUtil.isTaxPrior(pk_group);
	    boolean isGlobalEnable = RelationCalculateUtil.isGlobalEnable();
	    boolean isGroupEnable = RelationCalculateUtil.isGroupEnable(pk_group);
	    boolean isOrigCurToGroupMoney =
	        RelationCalculateUtil.isOrigCurToGroupMoney(pk_group);
	    boolean isOrigCurToGlobalMoney =
	        RelationCalculateUtil.isOrigCurToGlobalMoney();
	    boolean isInternational = false;
	    boolean isFbuysellflagHead = false;

	    Set<String> allKeys = new HashSet<String>();
		allKeys.addAll(Arrays.asList(parentVO.getAttributeNames()));
		allKeys.addAll(Arrays.asList(bvos[0].getAttributeNames()));
		CtBillCardPanelDataSetUtil dateSet = new CtBillCardPanelDataSetUtil();
	    for (CtAbstractBVO bvo : bvos) {
	      String[] calkeys = dateSet.calculateBefor(bvo);
	      Map<String, Object> value = this.saveOriValue(bvo, calkeys);
	      for(String calkey : calkeys){
	    		for(String key : value.keySet()){
	    			if(bvo.getAttributeValue(key) == null){
	    				bvo.setAttributeValue(key, value.get(key));
	    			}
	    		}
	    		// 创建数据集实例 初始化数据关系计算用的数据集
	    		IDataSetForCal data =
	    				new CtBillCardPanelDataSetUtil(parentVO, bvo, item, allKeys);
	    		Calculator tool = new Calculator(data, scale);
	    		// 创建参数实例，在计算的时候用来获得参数条件：是否含税优先等
	    		Condition cond = new Condition();// 创建参数实例
	    		// 设置是否进行本币换算
	    		cond.setIsCalLocalCurr(true);
	    		// 设置调单价方式调折扣
	    		cond.setIsChgPriceOrDiscount(false);
	    		String pk_material = bvo.getPk_material();
	    		String cunitid = bvo.getCunitid();
	    		String castunitid = bvo.getCastunitid();
	    		String cqtunitid = bvo.getCqtunitid();
	    		// 设置是否固定单位换算率
	    		cond.setIsFixNchangerate(UnitAndChangeRateUtil.isFixUnitRate(pk_material,
	    				cunitid, castunitid));
	    		// 是否固定报价单位换算率
	    		cond.setIsFixNqtunitrate(UnitAndChangeRateUtil.isFixUnitRate(pk_material,
	    				cunitid, cqtunitid));
	    		
	    		// 设置含税优先还是无税优先
	    		cond.setIsTaxOrNet(isTaxPrior);
	    		// 设置全局本币是否启用
	    		cond.setGlobalLocalCurrencyEnable(isGlobalEnable);
	    		// 设置集团本币是否启用
	    		cond.setGroupLocalCurrencyEnable(isGroupEnable);
	    		// 用原币折算全局本位币金额（否则用组织本位币）
	    		cond.setOrigCurToGlobalMoney(isOrigCurToGlobalMoney);
	    		// 用原币折算集团本位币金额（否则用组织本位币）
	    		cond.setOrigCurToGroupMoney(isOrigCurToGroupMoney);
	    		
	    		if (!isFbuysellflagHead) {
	    			if(bvo instanceof CtSaleBVO){
	    				CtSaleBVO saleBvo = (CtSaleBVO) bvo;
	    				isInternational =
	    						RelationCalculateUtil.isInternational((Integer) saleBvo.getFbuysellflag());
	    			}else if(bvo instanceof CtPuBVO){
	    				CtPuBVO puBvo = (CtPuBVO) bvo;
	    				isInternational =
	    						RelationCalculateUtil.isInternational((Integer) puBvo.getFbuysellflag());
	    			}
	    		}
	    		// 是否跨国
	    		cond.setInternational(isInternational);
	    		
	    		// 不可抵扣税率联动计算时设置为FALSE 否则会进行联动计算将主本币无税单价设置为空
	    		if (CTVatNameConst.NNOSUBTAXRATE.equals(calkey)
	    				|| CTVatNameConst.NCALTAXMNY.equals(calkey)
	    				|| CtAbstractBVO.NTAX.equals(calkey)) {
	    			cond.setCalOrigCurr(false);
	    		}
	    		// 两个参数 cond 为计算时的参数条件
	    		tool.calculate(cond, calkey);
	    	}
	    	
	    }
	  }
	
	private Map<String, Object> saveOriValue(CtAbstractBVO bvo, String[] calkeys) {
		Map<String, Object> result = new HashMap<String, Object>();
		for(String calkey : calkeys){
			result.put(calkey, bvo.getAttributeValue(calkey));
		}
		return result;
	}

}
