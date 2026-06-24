package nc.vo.ic.pub.calc;

import java.util.Map;

import nc.bs.framework.common.NCLocator;
import nc.bs.ic.pub.env.ICBSContext;
import nc.itf.scmpub.ia.ICostPriceQuery;
import nc.itf.scmpub.reference.uap.org.CostRegionPubService;
import nc.vo.ic.general.define.ICBillBodyVO;
import nc.vo.ic.general.define.ICBillHeadVO;
import nc.vo.ic.param.ICSysParam;
import nc.vo.ic.pub.define.ICPubMetaNameConst;
import nc.vo.ic.pub.util.NCBaseTypeUtils;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.calculator.Calculator;
import nc.vo.pubapp.calculator.Condition;
import nc.vo.pubapp.calculator.data.IRelationForItems;
import nc.vo.pubapp.calculator.data.VODataSetForCal;
import nc.vo.pubapp.scale.ScaleUtils;

/**
 * 库存单据数量变化金额字段计算类
 * 
 * @author xhs 2019-12-13 create
 */
public class ICBodyCalculator {
	private static IRelationForItems rfi;
	private static ScaleUtils scale;
	private static CalcContext cont;
	private static Boolean isTaxOrNet;
	private Integer fbuysellflag = null;
	private ICBillHeadVO headVO = null;

	public ICBodyCalculator(ICBillHeadVO hVO) {
		this.fbuysellflag = (Integer) hVO.getAttributeValue("fbuysellflag");
		this.headVO = hVO;
		if(rfi == null) {
			rfi = new ICRelationItemForCal();
			rfi.setNqtorigtaxnetprcKey(DataSetForCalCreator.NQTORIGTAXNETPRICE);
		}
		if(isTaxOrNet == null) {
			try {
				isTaxOrNet = new ICSysParam(hVO.getPk_group()).getSO23(hVO.getPk_group());
			} catch (BusinessException e) {
				isTaxOrNet = true;
			}
		}
		if(cont == null)
		    cont = new CalcContext(new ICSysParam(hVO.getPk_group()), new ICBSContext().getInvInfo());
		if(scale == null)
			scale = new ScaleUtils(hVO.getPk_group());
	}

	/**
	 * 采购入库表体数量/金额计算
	 */
	public void calc(ICBillBodyVO[] bvos, String changeNumItem) {
		if(bvos == null || bvos.length == 0)
			return;
		rfi.setnumKey(ICPubMetaNameConst.NNUM);
		rfi.setNassistnumKey(ICPubMetaNameConst.NASSISTNUM);
		for(ICBillBodyVO bvo : bvos)
			new Calculator(new ICBillBodyVO[]{bvo}, rfi).calculate(
					getConditionForCalc(bvo), changeNumItem, scale);
	}

	/**
	 * 处理数量精度
	 * 根据是否固定换算率计算实收/发主/辅数量/换算率
	 */
	public void adjustNumAssNumScale(ICBillBodyVO[] bvos, String changeNumItem) {
		if(bvos == null || bvos.length == 0)
			return;
		rfi.setnumKey(ICPubMetaNameConst.NNUM);
		rfi.setNassistnumKey(ICPubMetaNameConst.NASSISTNUM);
		for(ICBillBodyVO bVO : bvos)
			new Calculator(new VODataSetForCal(bVO, rfi), scale).calculateOnlyNumAssNumQtNum(getConditionForCalc(bVO), changeNumItem);
	}

	/**
	 * 处理数量精度
	 * 根据是否固定换算率计算应收/发主/辅数量/换算率
	 */
	public void adjustShouldNumAssNumScale(ICBillBodyVO[] bvos, String changeNumItem) {
		if(bvos == null || bvos.length == 0)
			return;
		rfi.setnumKey(ICPubMetaNameConst.NSHOULDNUM);
		rfi.setNassistnumKey(ICPubMetaNameConst.NSHOULDASSISTNUM);
		for(ICBillBodyVO bVO : bvos)
			new Calculator(new VODataSetForCal(bVO, rfi), scale).calculateOnlyNumAssNumQtNum(getConditionForCalc(bVO), changeNumItem);
	}

	/**
	 * 其它出入库单据表体数量/金额计算
	 */
	public void calcGeneral(ICBillBodyVO[] bvos) {
	    // key:库存组织+仓库，value:成本域
	    Map<String, String> map = CostRegionPubService.queryCostRegionIDByStockOrgsAndStordocs(
	    		new String[]{headVO.getPk_org()}, new String[]{headVO.getCwarehouseid()});
	    String costDomain = map.get(headVO.getPk_org()+headVO.getCwarehouseid());
	    // 如果成本域为空，则不处理
	    if(costDomain == null || costDomain.length() == 0)
	    	return;

	    // 构建查询条件，调用IA接口查询最新单价（结存单价）
	    String[] costdomains = new String[bvos.length];
	    String[] invIDs = new String[bvos.length];
	    String[] batchIDs = new String[bvos.length];
	    for (int i = 0; i < bvos.length; i++) {
	      costdomains[i] = costDomain;
	      invIDs[i] = bvos[i].getCmaterialoid();
	      batchIDs[i] = bvos[i].getPk_batchcode();
	    }
	    Map<String, UFDouble> mapPrice = null;
		try {
			mapPrice = NCLocator.getInstance().lookup(ICostPriceQuery.class).getCostPriceMap(costdomains, invIDs, batchIDs);
	        if (mapPrice == null || mapPrice.size() == 0)
		          return;
		} catch (BusinessException e) {
			e.printStackTrace();
			return;
		}
		for(ICBillBodyVO bVO : bvos) {
			UFDouble p = mapPrice.get(costDomain +"|"+ bVO.getCmaterialoid() +"|"+ bVO.getPk_batchcode());
			if(p != null) {
				bVO.setNcostprice(p);
				//处理金额精度
				bVO.setNcostmny(scale.adjustOrgMnyScale(p.multiply(bVO.getNnum()), headVO.getPk_org()));
			}
		}
	}

	private Condition getConditionForCalc(ICBillBodyVO bvo) {
	    Condition cond = new Condition();
	    // 设置业务单位是否固定换算率
	    cond.setIsFixNchangerate(cont.isFixFlag(bvo.getCmaterialvid(), bvo.getCastunitid()));
	    // 设置报价单位是否固定换算率
	    cond.setIsFixNqtunitrate(cont.isFixFlag(bvo.getCmaterialvid(), (String) bvo.getAttributeValue("cqtunitid")));
	    // 设置是否进行本币换算
	    cond.setIsCalLocalCurr(true);
	    // 设置调单价方式调折扣(modified by chenlla 调整单价,折扣不变)
	    cond.setIsChgPriceOrDiscount(true);
	    // 是否处理集团本位币
	    cond.setGroupLocalCurrencyEnable(cont.isUseGroupCur());
	    // 是否处理全局本位币
	    cond.setGlobalLocalCurrencyEnable(cont.isUseGlobalCur());
	    // 是否基于原币计算
	    cond.setOrigCurToGroupMoney(cont.isLocalCurToGroupMoney());
	    cond.setOrigCurToGlobalMoney(cont.isLocalCurToGlobalMoney());
	    if (NCBaseTypeUtils.isNull(bvo.getAttributeValue("cqtunitid"))) {
	      // 报价单位为空则主单位优先
	      cond.setUnitPriorType(Condition.MAIN_PRIOR);
	    } else {
	      // 报价单位不为空则报价单位优先(采购入和销售出库)
	      cond.setUnitPriorType(Condition.QT_PRIOR);
	    }
	    // 设置含税优先还是无税优先
	    cond.setIsTaxOrNet(isTaxOrNet);
	    // 设置是否跨国贸易
	    cond.setInternational(cont.isInternational(fbuysellflag));

	    return cond;
	}

}
