package nc.bs.mmpub.setanalysis.bp.match;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import nc.bs.mmpub.setanalysis.bp.SaMonolayerBP;
import nc.bs.mmpub.setanalysis.bp.log.SaLogRule;
import nc.bs.mmpub.setanalysis.bp.utils.SaBooleanUtils;
import nc.bs.mmpub.setanalysis.bp.utils.SaVOUtils;
import nc.impl.pubapp.pattern.data.vo.VOQuery;
import nc.util.mmf.framework.base.MMValueCheck;
import nc.vo.bd.material.plan.MaterialPlanVO;
import nc.vo.bd.material.stock.MaterialStockVO;
import nc.vo.mmpub.setanalysis.entity.SaDemandVO;
import nc.vo.mmpub.setanalysis.entity.SaMatchVO;
import nc.vo.mmpub.setanalysis.entity.SaSupplyVO;
import nc.vo.mmpub.setanalysis.entity.SaWorkDateCache;
import nc.vo.mmpub.setanalysis.enumeration.SaSupplyType;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;

/**
 * 生成匹配结果处理类
 * 
 * @since 6.3
 * @version 2012-8-9 上午10:46:38
 * @author zhaohyc
 */
public class SaMatchResultBP {
    /**
     * 构造
     * 
     * @param newsmc
     */
    public SaMatchResultBP(SaMatchContainer newsmc) {
        this.setSmc(newsmc);
    }

    /**
     * 初始化数据
     */
    private SaMatchContainer smc;

    public SaMatchContainer getSmc() {
        return this.smc;
    }

    public void setSmc(SaMatchContainer smc) {
        this.smc = smc;
    }

    /**
     * 开始匹配
     * 
     * @param demand
     * @param supplies
     */
    public void doMatchResult(SaDemandVO demand, List<SaSupplyVO> supplies, List<String> orgLis) {
        if (MMValueCheck.isEmpty(supplies)) {
            return;
        }
        List<SaSupplyVO> usedSupplyList = new LinkedList<SaSupplyVO>();
        for (SaSupplyVO supply : supplies) {
            // 匹配条件：需求未匹配量>0，供给未匹配量>0, 供给日期<需求日期
            if (this.isMatch(demand, supply, orgLis)) {
                this.createMatchDetail(demand, supply, usedSupplyList);

            }
            if (this.isDemandOver(demand.getNremainnum())) {
                break;// 需求匹配完后结束
            }
        }
        supplies.removeAll(usedSupplyList);
    }

    /**
     * 严格匹配源头单据(源头相同的先占用)
     * 
     * @param demand
     * @param supplies
     * @param smc
     */
    public void doMatchResultForFirstCode(SaDemandVO demand, List<SaSupplyVO> supplies, List<String> orgLis) {
        if (MMValueCheck.isEmpty(supplies)) {
            return;
        }
        List<SaSupplyVO> usedSupplyList = new ArrayList<SaSupplyVO>();
        for (SaSupplyVO supply : supplies) {
            // 匹配条件：需求未匹配量>0，供给未匹配量>0,源头相同或供给源头为空
            if (!MMValueCheck.isEmpty(supply.getVfirstcode()) && !MMValueCheck.isEmpty(demand.getVfirstcode())
                    && demand.getVfirstcode().equals(supply.getVfirstcode()) && this.isMatch(demand, supply, orgLis)) {
                this.createMatchDetail(demand, supply, usedSupplyList);
            }
            if (this.isDemandOver(demand.getNremainnum())) {
                break;// 需求匹配完后结束
            }
        }
        supplies.removeAll(usedSupplyList);
    }

    /**
     * 需求匹配结束
     * 
     * @param nremainnum
     * @return
     */
    private boolean isDemandOver(UFDouble nremainnum) {
        return nremainnum.doubleValue() <= UFDouble.ZERO_DBL.doubleValue();
    }

    /**
     * 公共匹配规则
     * 
     * @param demand 需求
     * @param supply 供给
     * @return
     */
    @SuppressWarnings("boxing")
    private boolean isMatch(SaDemandVO demand, SaSupplyVO supply, List<String> orgList) {
        boolean retBoolean = false;
        // 分共匹配条件，需求缺料＞０，供给未匹配量＞０，供给匹配日期＜＝需求日期(库存除外)
        if (MMValueCheck.isNotEmpty(supply.getNremainnum())
                && supply.getNremainnum().doubleValue() > UFDouble.ZERO_DBL.doubleValue()
                && demand.getNremainnum().doubleValue() > UFDouble.ZERO_DBL.doubleValue()
                // TODO 63只敏感结构辅助属性 zhaohyc
                // && (supply.getFsupplytype().equals(SaSupplyType.ONHAND_NUM.toIntValue())
                // && !SaMaterialUtil.isVfreeEnable(demand) || !supply.getFsupplytype().equals(
                // SaSupplyType.ONHAND_NUM.toIntValue())// 存量未启用辅助属性或非存量需求匹配日期<供给匹配日期
                // && supply.getDmatchdate().compareTo(demand.getDmatchdate()) <= 0)
                // 生产领料委托关系
                && (this.smc.getSc().getPk_org().equals(supply.getPk_supplyorg()) || !MMValueCheck.isEmpty(orgList)
                        && orgList.contains(supply.getPk_supplyorg()))) {
        	//2023-12-29 liyf 库存的匹配增加是否考虑质检计划周期  的判断条件
        	if(supply.getFsupplytype().equals(SaSupplyType.ONHAND_NUM.toIntValue())){
        		
        		retBoolean = isMatchedForOnhand(demand, supply);
        	}else{
        		if(supply.getDmatchdate()
                        .compareTo(demand.getDmatchdate()) <= 0){
                    retBoolean = true;

        		}
        	}
        	
        }
        return retBoolean;
    }
    
    
   /**
    * liyf
    * @param demand
    * @param supply
    * @return
    */
	private boolean isMatchedForOnhand(SaDemandVO demand, SaSupplyVO supply) {
		// TODO Auto-generated method stub
		  //liyf 202312-18 库存状态,首次入库日期和失效日期
//        vdef1//库存状态
//       vdef2//首次入库日期,后续用来比较先入先出
//        vdef3//失效日期5
        //test
//		if(!"1001V1100000000J8RXZ".equalsIgnoreCase(supply.getCmaterialid())){
//			return false;
//		}
		//test
		
        //liyf 202312-18 库存状态,首次入库日期和失效日期
		 //是否考虑质检计划周期  ， 如果考虑，待检物料供给日期加上物料后段提前期
		String vdef1 = (String)getSmc().getSc().getOption().getVdef1();
		boolean isCheck = false;
		if("Y".equalsIgnoreCase(vdef1)){
			isCheck =true;
		}
		if(!isCheck){
			return true;
		}
		UFDate demandEndDate = demand.getDemandtbeforeadpter();
//		 如果库存失效日期>需求日期则不进行匹配
		if(supply.getVdef3() !=null){
			UFDate dvalidate = new UFDate(supply.getVdef3()).asBegin();
			if (dvalidate.before(demandEndDate)) {
				return false;
			}
		}
		UFDate dvalidate = new UFDate(supply.getVdef3()).asEnd();
		//如果可开始匹配日期<晚于需求日期则不进行匹配:
//		可开始匹配日期，正常库存状态的等于首次入库dinbounddate,
		//待检状态的 制造建 =首次入库日期+后段提前期 工作日历(节假日+休息日)  采购件 = =首次入库日期+后段提前期  工作日历 （自然日）
		if(supply.getVdef2() !=null){
			UFDate dinbounddate = new UFDate(supply.getVdef2()).asEnd();

			//如果库存状态时合格的，则直接返回true
			if(supply.getVdef1()!=null && supply.getVdef1().contains("待检")){

				MaterialPlanVO [] planVOs =  new VOQuery<MaterialPlanVO  >(MaterialPlanVO .class).query(" and pk_material ='"+supply.getCmaterialvid()+"' and pk_org='"+supply.getPk_org()+"'", null);
				UFDouble endahead = planVOs[0].getEndahead();
				int days =  endahead != null ? endahead.intValue() : 0;
				//DR=分销补货，FR=工厂补货，MR=制造件，PR=采购件，OT=委外件，ET=其他，  
				MaterialStockVO[] stockVOs =  new VOQuery<MaterialStockVO >(MaterialStockVO.class).query(" and pk_material ='"+supply.getCmaterialvid()+"' and pk_org='"+supply.getPk_org()+"'", null);
				String martype = stockVOs[0].getMartype();
				if("MR".equalsIgnoreCase(martype)){
			        SaWorkDateCache sadc = this.smc.getSc().getWorkDateCache();
					dinbounddate = sadc.getAfterWorkDates(dinbounddate,days);
				}else{
					dinbounddate = dinbounddate.getDateAfter(days);
				}
			
			}
			//如果=首次入库日期+后段提前期  晚于需求日期，则不匹配
			if (dinbounddate.after(demandEndDate)) {
				return false;
			}
			//如果失效日期 早于需求日期，则不匹配
			if (dvalidate.before(demandEndDate)) {
				return false;
			}
		}
		
	
		return true;
	}

    /**
     * 创建匹配明细
     * 
     * @param demand
     * @param supply
     */
    private void createMatchDetail(SaDemandVO demand, SaSupplyVO supply, List<SaSupplyVO> usedSupplyList) {
        UFDouble num = null;
        UFDouble supNremainnum = supply.getNremainnum();
        UFDouble demandNremainnum = demand.getNremainnum();
        if (supNremainnum.compareTo(demandNremainnum) > 0) {
            num = demandNremainnum;
        }
        else {
            num = supNremainnum;
        }

        // 供给匹配数量累加、未匹配数量累减
        // 需求匹配数量累加、单层齐套数量累加
        SaMatchVO match = this.smc.getSma().createMatchVO(demand, supply, num);
        // 展算相关需求(计划订单、主生产计划、委外、流程生产订单、离散生产订单),多层,非替代料,非跨组织
        if (SaMatchResultBP.isExpand(supply, this.smc, demand)) {
            if (!supply.getFsupplytype().equals(SaSupplyType.OUT_SOURCE.toInteger())) {
                this.smc.getSde().expandPDemand(demand, supply, match, num);
            }
            // 委外订单有备料计划时才展算
            else if (SaVOUtils.isPickExpand(supply, this.getSmc().getSc())) {
                this.smc.getSde().expandPDemand(demand, supply, match, num);
            }

            // 单层勾选则第一层匹配后结束

        }
        else {
            if (this.smc.getSc().isSalog() && !SaMatchResultBP.isPk_org(this.smc.getSc().getPk_org(), supply)) {
                SaLogRule slr = new SaLogRule(this.smc.getSc());
                slr.testingPk_org(demand.getPk_analysis(), supply.getVsupplycode());
            }
            // 齐套+累加净匹配量
            SaMonolayerBP.setMatchNum(num, demand);
            if (!SaBooleanUtils.isNotReplace(demand)) {
                match.setNmaterialnum(match.getNsupplynum());// 替代供给只计算齐料
            }
            else {
                SaMonolayerBP.setNsetsnum(match, demand, null);
            }
        }
        // 匹配明细记录
        this.smc.getSmi().addMatchDetail(match);

        // 需求供给减少。
        SaMonolayerBP.setMonolayer(demand, supply, num);
        // 供给信息改变记录
        // 提升效率，全部被占用的(未匹配数量=0)才进行更新
        if (UFDouble.ZERO_DBL.doubleValue() == supply.getNremainnum().doubleValue()) {
            this.smc.getSsu().addSupplyDetail(supply);
            usedSupplyList.add(supply);
        }
    }

    /**
     * supply 供给vo；smc上下文；demand：需求vo
     * 供给展算需求条件： (1)多层齐套分析(2)非替代需求
     * (3)供给类型为计划订单、主生产计划、流程生产订单、离散生产订单时 分析工厂=供给生产工厂或者供给类型为委外订单
     **/
    private static boolean isExpand(SaSupplyVO supply, SaMatchContainer smc, SaDemandVO demand) {
        // 多层非替代需求
        boolean issingle = smc.getSc().getOption().getSingleset().booleanValue();
        boolean isnotreplace = SaBooleanUtils.isNotReplace(demand);
        boolean iscanexpand = !issingle && isnotreplace;
        // 供给类型为计划订单、主生产计划、dmo、pmo时 分析工厂=生产工厂；
        // 供给类型为委外订单时 分析工厂=库存组织
        boolean isexpandtype = SaMatchResultBP.istype(supply);
        boolean isexpandpkorg = SaMatchResultBP.isPk_org(smc.getSc().getPk_org(), supply);
        boolean istypeandpkorg = isexpandtype && isexpandpkorg;
        // 同时满足
        return iscanexpand && istypeandpkorg;

    }

    /**
     * 判断是否是可以展算
     * 
     * @param supplytype 供给单据类型
     * @return
     */
    private static boolean istype(SaSupplyVO supply) {
        boolean type = false;
        // 展算相关需求(计划订单、主生产计划、流程生产订单、离散生产订单、委外订单)
        if (!MMValueCheck.isEmpty(supply.getFsupplytype())
                && supply.getFsupplytype().equals(SaSupplyType.MPS.toInteger())
                || supply.getFsupplytype().equals(SaSupplyType.PLANNED_ORDER.toInteger())
                || supply.getFsupplytype().equals(SaSupplyType.DMANUFACTURE_ORDER.toInteger())
                || supply.getFsupplytype().equals(SaSupplyType.PMANUFACTURE_ORDER.toInteger())
                || supply.getFsupplytype().equals(SaSupplyType.OUT_SOURCE.toInteger())) {
            type = true;
        }
        return type;
    }

    /**
     * 跨组织的生产订单,委外,等供给一律不展相关需求
     * 规则:委外取库存组织=分析工厂,其它则取主组=生产工厂
     * 
     * @param demand
     * @param supply
     * @return
     */
    private static boolean isPk_org(String pk_org, SaSupplyVO supply) {
        if (MMValueCheck.isEmpty(supply.getPk_org()) || MMValueCheck.isEmpty(supply.getPk_supplyorg())) {
            return false;
        }
        boolean ret = false;
        if (supply.getFsupplytype().equals(SaSupplyType.OUT_SOURCE.toInteger())
                && pk_org.equals(supply.getPk_supplyorg())
                || !supply.getFsupplytype().equals(SaSupplyType.OUT_SOURCE.toInteger())
                && pk_org.equals(supply.getPk_productfacotry())) {
            ret = true;
        }
        return ret;
    }

}
