package nc.impl.ewm.workorder.prealert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import nc.bs.pub.pa.PreAlertContext;
import nc.bs.pub.pa.PreAlertObject;
import nc.bs.pub.pa.PreAlertReturnType;
import nc.impl.am.common.InSqlManager;
import nc.impl.am.db.DBAccessUtil;
import nc.impl.am.db.QueryUtil;
import nc.impl.am.prealert.AbstractAlertPlugin;
import nc.impl.am.prealert.BasePreAlertDataSource;
import nc.vo.am.common.AbstractAggBill;
import nc.vo.am.common.BizContext;
import nc.vo.am.common.util.ArrayUtils;
import nc.vo.am.common.util.StringUtils;
import nc.vo.am.constant.CommonKeyConst;
import nc.vo.am.constant.WOStatusType;
import nc.vo.ewm.workorder.AggWorkOrderVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDate;

import org.apache.commons.collections.CollectionUtils;
/**
 * 工单延期完成预警
 * @author 曾令智
 * @date 2012-10-17
 *
 */
public class WoFinishAlertPlugin extends AbstractAlertPlugin{

	public static String TARG_START_TIME = "targ";         //统计方式：目标开始时间
	public static String PLAN_START_TIME = "plan";         //统计方式：计划开始时间
	public static String TARGORPLAN_START_TIME = "torp";   //统计方式：目标或计划开始时间

	@SuppressWarnings("unchecked")
	@Override
	public PreAlertObject executeTask(PreAlertContext context)throws BusinessException {
		PreAlertObject retObj = null;
		// 直接过滤表体的数据
		List<String> pks = new DBAccessUtil().querySingleColumn(getCondition(context));
		if (CollectionUtils.isNotEmpty(pks)) {
			// 根据表体数据获取表头数据
			AggWorkOrderVO[] billVOs = (AggWorkOrderVO[]) QueryUtil.queryBillVOByPks(getBillVOClass(),
					pks.toArray(new String[0]), false);
			// 定制数据源
			BasePreAlertDataSource ds = getPredAlertDataSource();
			// 表头和表体数据合成为billVO
			ds.setBillVOs(billVOs);
			retObj = new PreAlertObject();
			retObj.setReturnType(PreAlertReturnType.RETURNDATASOURCE);
			retObj.setReturnObj(ds);
		}
		return retObj;
	}

	@Override
	protected String getCondition(PreAlertContext context) {

		HashMap<String, Object> keyMap = context.getKeyMap();
		// 当前日期
		UFDate currDate = BizContext.getInstance().getServerDate();
		// 维修组织
//		Object pk_org = context.getPk_org();//获取组织面板中所选组织
		String [] pk_orgs = context.getPk_orgs(); //获取组织（预警升级后多组织模式）
		//责任部门
		Object pk_dept = keyMap.get(CommonKeyConst.pk_dept);
		// 预警天数
		Object alertDays = keyMap.get(CommonKeyConst.ALERTDAYS);
		//统计方式
		Object statmanner = keyMap.get(CommonKeyConst.STATMANNER);
		//提前天数
		Object advanceDays = keyMap.get("advancedays");
		

		StringBuffer sqlStr = new StringBuffer();
		sqlStr.append(" select pk_wo from ewm_wo where ");
		// 若资产组织为空，继续判断部门，部门不为空则对部门预警，否则若组织和部门都为空则对整个集团的预警
		if (ArrayUtils.isEmpty(pk_orgs)) {
			 //如果负责部门不为空，对部门预警
		    if (!StringUtils.isBlank((String)pk_dept)){
			     sqlStr.append("  pk_wo_dept in ( ").append(getPksAsSQLCondition((String)pk_dept)).append(") ");
		     }
		    else{//组织和部门都为空，则对集团预警
		    	sqlStr.append(" pk_group = '").append(context.getGroupId()).append("' ");
		    }

		} else {//组织不为空
			     sqlStr.append(" pk_org in ").append(InSqlManager.getInSQLValue(pk_orgs));
			    //如果负责部门不为空，对部门预警，否则对所选组织预警
			    if (!StringUtils.isBlank((String)pk_dept)){
				   sqlStr.append(" and pk_wo_dept in ( ").append(getPksAsSQLCondition((String)pk_dept)).append(") ");
			     }
		}

		//根据统计方式构造条件
		if(statmanner.equals("targ")){//统计方式：目标完成时间
		     sqlStr.append(" and ( targ_end_time <= '").append(currDate).append("' ");
		     sqlStr.append(" and targ_end_time > '").append(currDate.getDateBefore(Integer.parseInt((String)alertDays))).append("' )");
		     sqlStr.append("or(to_char(TO_DATE(targ_end_time,'YYYY-MM-DD HH24:MI:SS') -"+Integer.parseInt((String)advanceDays)+",'YYYY-MM-DD HH24:MI:SS')  <=  '"+currDate+"' ");
		     sqlStr.append(" )");
		}
		else if(statmanner.equals("plan")){//统计方式：计划完成时间
			 sqlStr.append(" and ( plan_end_time <= '").append(currDate).append("' ");
			 sqlStr.append(" and plan_end_time > '").append(currDate.getDateBefore(Integer.parseInt((String)alertDays))).append("' )");
			 sqlStr.append("or( ");
		     sqlStr.append("  to_char(TO_DATE(plan_end_time,'YYYY-MM-DD HH24:MI:SS') -"+Integer.parseInt((String)advanceDays)+",'YYYY-MM-DD HH24:MI:SS')  <=  '"+currDate+"' )");
		}
		else if(statmanner.equals("torp")){//统计方式：目标完成时间或计划完成时间
			 sqlStr.append("and ( ");
			 sqlStr.append(" ( ").append(" targ_end_time <= '").append(currDate).append("' ");
			 sqlStr.append(" and targ_end_time > '").append(currDate.getDateBefore(Integer.parseInt((String)alertDays))).append("' )");
			 sqlStr.append(" or ");
			 sqlStr.append(" ( ").append(" plan_end_time <= '").append(currDate).append("' ");
			 sqlStr.append(" and plan_end_time > '").append(currDate.getDateBefore(Integer.parseInt((String)alertDays))).append("' )");
			 sqlStr.append("or(to_char(TO_DATE(targ_end_time,'YYYY-MM-DD HH24:MI:SS') -"+Integer.parseInt((String)advanceDays)+",'YYYY-MM-DD HH24:MI:SS')  <=  '"+currDate+"' ");
		     sqlStr.append(" or to_char(TO_DATE(plan_end_time,'YYYY-MM-DD HH24:MI:SS') -"+Integer.parseInt((String)advanceDays)+",'YYYY-MM-DD HH24:MI:SS')  <=  '"+currDate+"' )");
		     sqlStr.append(" )");
		}
		     //作废类型工单、待批省类型工单不预警
		     sqlStr.append(" and pk_wo_status in (");
		     sqlStr.append(" select pk_wo_status from ewm_wo_status where ");
		     sqlStr.append(" status_type not in (")
		           .append("'").append(WOStatusType.STATUSTYPE_1_CANCELED).append("'").append(",")
		           .append("'").append(WOStatusType.STATUSTYPE0_UN_CHECK).append("'")
		           .append(") ");
		     sqlStr.append(" )");
		    //实际完成结束的工单不预警
		     sqlStr.append(" and actu_end_time is null ");
		   return sqlStr.toString();
	}

	/** 构建查询条件（组织或者部门） */
	private String getPksAsSQLCondition(String pksStr) {

		StringBuilder cond = new StringBuilder();
		// 获取选择的组织
		List<String> IDs = new ArrayList<String>();
		String[] pks = pksStr.split(",");
		for(String pk : pks) {
			IDs.add(pk.trim());
		}
		// 构建条件
		for(String ID : IDs) {
			cond.append("'").append(ID).append("', ");
		}
		return cond.substring(0, cond.length() - 2);
	}

	@Override
	protected Class<? extends AbstractAggBill> getBillVOClass() {
		return AggWorkOrderVO.class;
	}

	@Override
	protected Class<? extends SuperVO> getHeadVOClass() {
		return null;
	}

	@Override
	protected Class<? extends SuperVO>[] getBodyVOClass() {
		return null;
	}

	@Override
	protected String getTitle() {
		return nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("workorder_0","04560003-0514")/*@res "工单延期完成预警"*/;
	}

}