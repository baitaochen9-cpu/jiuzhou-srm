package nc.impl.ia.monthprice;

import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.ia.audit.pub.IAContext;
import nc.bs.ia.monthlycalc.pub.MonthCalcTool;
import nc.bs.ia.monthprice.calculate.bp.AvePriceCalculateBP;
import nc.bs.ia.monthprice.uncalculate.bp.AvePriceUncalculateBP;
import nc.itf.ia.monthprice.IMonthPrice;
import nc.jdbc.framework.processor.MapProcessor;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.ia.monthprice.MonthPriceResultVO;
import nc.vo.ia.pub.util.CalcRangeUtil;
import nc.vo.ia.pub.util.QuerySchemeUtil;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.pubapp.pattern.model.entity.view.AbstractDataView;

public class MonthPriceImpl implements IMonthPrice {
	public MonthPriceResultVO[] auditAfterHandInput(MonthPriceResultVO[] paras)
			throws BusinessException {
		/* 32 */MonthPriceResultVO[] ret = null;
		/* 33 */IAContext context = MonthCalcTool.getContext(paras, false);
		/* 34 */AvePriceCalculateBP action = new AvePriceCalculateBP(context);
		try {
			/* 36 */ret = action.calculate(paras);
			/* 37 */initCalcrangeFields(ret);
		} catch (Exception e) {
			/* 40 */ExceptionUtils.marsh(e);
		}
		/* 42 */return ret;
	}

	public MonthPriceResultVO[] cancelMonthPrice(IQueryScheme queryScheme)
			throws BusinessException {
		/* 48 */MonthPriceResultVO[] ret = null;
		/* 49 */IAContext context = MonthCalcTool
				.getContext(queryScheme, false);
		/* 50 */Integer fBillUnauditflag = QuerySchemeUtil.getInteger(
				queryScheme, "billunauditflag");

		/* 52 */context.setFBillUnauditflag(fBillUnauditflag);
		try {
			/* 54 */AvePriceUncalculateBP action = new AvePriceUncalculateBP(
					context);
			/* 55 */ret = action.unCalclate(queryScheme);
			/* 56 */initCalcrangeFields(ret);

//			2021-07-28 liyf  回写循环物料的计划价0：计算会写计划价 1：取消计算清空计划价
			wrieteBackPlanPrice(ret, 0);

		} catch (Exception e) {
			/* 59 */ExceptionUtils.marsh(e);
		}
		/* 61 */return ret;
	}

	public MonthPriceResultVO[] computeMonthPrice(IQueryScheme queryScheme)
			throws BusinessException {
		MonthPriceResultVO[] ret = null;
		IAContext context = MonthCalcTool.getContext(queryScheme, false);
		try {
			AvePriceCalculateBP action = new AvePriceCalculateBP(context);
			ret = action.calculate(queryScheme);
			initCalcrangeFields(ret);
//			2021-07-28 liyf  回写循环物料的计划价0：计算会写计划价 1：取消计算清空计划价
			wrieteBackPlanPrice(ret, 0);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return ret;
	}

	/**
	 * 2021-07-28
	 * liyf 回写循环物料的计划价0：计算会写计划价 1：取消计算清空计划价
	 * 
	 * @param ret
	 * @param i
	 * @throws DAOException 
	 */
	private void wrieteBackPlanPrice(MonthPriceResultVO[] ret, int opetype) throws DAOException {
		// TODO Auto-generated method stub
		if(ret == null || ret.length ==0){
			return ;
		}
		
		MapProcessor processor = new MapProcessor();
		BaseDAO dao = new BaseDAO();
		
		for(MonthPriceResultVO bill:ret){
			String pk_org = bill.getPk_org();
			String  pk_material  = bill.getCinventoryid();
			String sql=" select  pk_materialcost , def1  from  bd_materialcost where pk_org='"+pk_org+"' and  pk_material='"+ pk_material +"'";
			Map<String,String> costinfor = (Map<String, String>) dao.executeQuery(sql, processor);
			if("Y".equalsIgnoreCase(costinfor.get("def1"))){	
				UFDouble nmonthlyprice = bill.getNmonthlyprice();
				if(1 ==opetype){
					nmonthlyprice = UFDouble.ZERO_DBL;
				}
				if(nmonthlyprice == null){
					nmonthlyprice = UFDouble.ZERO_DBL;
				}
				String pk_materialcost = costinfor.get("pk_materialcost");
				sql=" update bd_materialcostmod set planedprice='"+nmonthlyprice+"'  where pk_costregion ='"+pk_org+"' and  pk_material='"+ pk_material +"' and   pk_materialcost='"+pk_materialcost+"'";
				dao.executeUpdate(sql);
			}
			
		}

	}

	public MonthPriceResultVO[] saveMonthPrice(MonthPriceResultVO[] paras)
			throws BusinessException {
		/* 83 */MonthPriceResultVO[] ret = null;
		/* 84 */IAContext context = MonthCalcTool.getContext(paras, false);
		/* 85 */AvePriceCalculateBP action = new AvePriceCalculateBP(context);
		try {
			/* 87 */ret = action.calculateForSavePrice(paras);
			/* 88 */initCalcrangeFields(ret);
		} catch (Exception e) {
			/* 91 */ExceptionUtils.marsh(e);
		}
		/* 93 */return ret;
	}

	private void initCalcrangeFields(AbstractDataView[] result) {
		/* 97 */if (result.length == 0) {
			/* 98 */return;
		}
		/* 100 */CalcRangeUtil util = new CalcRangeUtil();
		/* 101 */util.loadCalcRangeFields(result);
	}

}