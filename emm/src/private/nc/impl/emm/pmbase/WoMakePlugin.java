package nc.impl.emm.pmbase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.pub.pa.PreAlertObject;
import nc.bs.pub.pa.PreAlertReturnType;
import nc.bs.pub.taskcenter.BgWorkingContext;
import nc.bs.pub.taskcenter.IBackgroundWorkPlugin;
import nc.impl.am.common.InSqlManager;
import nc.impl.am.prealert.BasePreAlertDataSource;
import nc.md.persist.framework.IMDPersistenceQueryService;
import nc.pub.jz.util.RaybowIcMassageUtil;
import nc.vo.am.common.BizContext;
import nc.vo.am.common.util.ArrayUtils;
import nc.vo.bd.pub.IPubEnumConst;
import nc.vo.emm.premaintain.PMBillVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pubapp.AppContext;

/**
 * <p>
 * 预防性维护生成工单的后台任务实现类。
 * </p>
 * 
 * @author cuikai
 * @version 6.0
 */
public class WoMakePlugin implements IBackgroundWorkPlugin {

	// 制单人取数方式
	public static String CreatorMode = "MBTYPE";
	// 制单人
	public static String CREATOR = "MBER";
//	// 维修组织
//	public static String MaintainBU = "MBU";
	// 固定用户
	public static String CreatorMode_Value_Fix = "use";
	// 工单执行人作为制单人
	public static String CreatorMode_Value_Exec = "exe";
	// 工单主管人作为制单人
	public static String CreatorMode_Value_Director = "dir";
	
	public static String ErroMessageToUser = "EUSER";
	// 返回信息
	public StringBuffer retmsg = new StringBuffer();
	public StringBuffer erromsg = new StringBuffer();
	RaybowIcMassageUtil rimu = new RaybowIcMassageUtil();

	@Override
	public PreAlertObject executeTask(BgWorkingContext bgwc)
			throws BusinessException {
		
		PreAlertObject retObj = new PreAlertObject();
		retObj.setReturnType(PreAlertReturnType.RETURNMESSAGE);
		retObj.setMsgTitle("PM自动生成工单执行记录");
		retmsg.append(AppContext.getInstance().getServerTime()+"  nc.impl.emm.pmbase.WoMakePlugin.executeTask()  as begin>>>>>>>>> \n ");
		
		// 维修业务单元
		String[] pk_orgs = bgwc.getPk_orgs();
		
		
		IMDPersistenceQueryService q= NCLocator.getInstance().lookup(IMDPersistenceQueryService.class);
		List<PMBillVO> queryBillOfVOByCond = null;
		retmsg.append(AppContext.getInstance().getServerTime()+"  begin query PMheadVOS   >>>>>>>>> \n ");
		try {
			
			queryBillOfVOByCond = (List<PMBillVO>)q.queryBillOfVOByCond(PMBillVO.class, getWoMakePMSql(pk_orgs), false);
		} catch (nc.md.model.MetaDataException e) {
			erromsg.append("IMDP Query the PM is ERRO! msg:"+e.getMessage()+"     >>>>>>>>> \n ");
		}
		
 		if (null == queryBillOfVOByCond || queryBillOfVOByCond.size() == 0) {
			retmsg.append(AppContext.getInstance().getServerTime()+"   No WorOrder is created!!");
			
			retObj.setReturnObj(retmsg.toString());
			/********************************************bbt 2023.09.11*********   ***/
			//对于瑞博苏州厂区，当执行情况发送内容为空时，则清空消息接收人的人员清单，只保留一个本部用户1006100
			if(pk_orgs.length == 1){
				if("0001V11000000000374G".equals(pk_orgs[0]) && null != bgwc.getKeyMap().get("EUSER")){
					try {
						
						rimu.sedMassage("0001V1100000000038N7", (String)bgwc.getKeyMap().get("EUSER"), "瑞博苏州预防性维护后台无组装工单推送消息", 
								"此次瑞博苏州预防性维护后台任务无组装工单", "billid", "billtype");
						
						return null;
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			/********************************************************************************/
			return retObj;
		}
		
 		retmsg.append(AppContext.getInstance().getServerTime()+"  queryUtil.queryBillVOByHeadCond  rst pmbillvos.length = "+queryBillOfVOByCond.size()+"    >>>>>>>>> \n ");
		
		PMCreateWorOrderImpl createBillImpl = new PMCreateWorOrderImpl();
		PMBillVO[] pmbillvos = null;
		
		try {
			// 初始化后台任务参数作为查询条件
			HashMap<String, Object> km = bgwc.getKeyMap();
			Set<Entry<String, Object>> name = km.entrySet();
			HashMap<String, Object> hmParam = new HashMap<String, Object>();
			for (Entry<String, Object> entry : name) {
				hmParam.put(entry.getKey(), entry.getValue());
			}
			pmbillvos = createBillImpl.makeWorkOrderForPlugin( queryBillOfVOByCond.toArray(new PMBillVO[0]), hmParam); //批量执行工单生成
		} catch (Exception e) {
			erromsg.append( ">>>>>批量生成工单时发生异常！\n "+e.getMessage());		
		}

	
		


		/*重新定义信息返回，如果有异常数据的话，把异常数据和执行过程返回回去，如果的正常执行的，就把结果集以附件形式返回回去*/
		if((null != erromsg && erromsg.length() > 0 )){
			//存在异常数据，
			try {
				if( null != bgwc.getKeyMap().get("EUSER"))
				rimu.sedMassage("0001V1100000000038N7", (String)bgwc.getKeyMap().get("EUSER"), 
						"预防性维护异常", erromsg.toString(), "~", "~");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				throw new BusinessException(e.getMessage());
			}
			retObj.setReturnObj(erromsg.toString());
			
		}
		else{
			//无异常
			retObj.setReturnType(PreAlertReturnType.RETURNDATASOURCE);
			BasePreAlertDataSource ds = new BasePreAlertDataSource();
			// 表头和表体数据合成为billVO
			ds.setBillVOs(pmbillvos);
			// 模板展示生成工单数据
			retObj.setReturnObj(ds);
			
			Thread thread = new Thread(new PmPluginCheckWorkBill(pmbillvos,queryBillOfVOByCond,(String)bgwc.getKeyMap().get("EUSER")));
			
			
			thread.start();
		}
		

		return retObj;

	}

	/**
	 * 查询符合生成工单条件的预防性维护
	 * 
	 * @param pk_mainOrg
	 * @return
	 */
		private  String getWoMakePMSql(String[] pk_orgs) {
		// 当前集团
		String pk_group = InvocationInfoProxy.getInstance().getGroupId();
		// 当前日期  
		
		UFDateTime currDate = BizContext.getInstance().getServerDateTime();
		
		// 时间性预防性维护预警条件
		String pmTimeSql = " emm_pm.next_create_date <= '" + currDate + "' ";
		// 绩效性预防性维护预警条件
		String pmResuSql = " ( select last_meas_result from eom_measure_point_b where eom_measure_point_b.pk_measure_point_b = emm_pm_result.pk_measure_point_b ) >= next_num - ahead_num ";

		StringBuilder querySql = new StringBuilder();
		querySql.append("  emm_pm.pk_pm in ");
		querySql.append(" ( SELECT DISTINCT     ");
		querySql.append("		emm_pm.pk_pm	");
		querySql.append("	FROM emm_pm 		");
		/******************增加预防性维护关联资产卡片状态判断.bbt.251219***************************************/
		querySql.append(" LEFT JOIN pam_equip on pam_equip.pk_equip = emm_pm.pk_equip ");
		
		//20260508 yza 增加判断资产状态过滤
		querySql.append( "left join pam_status on pam_equip.pk_used_status  = pam_status.pk_status ");
		
		querySql.append(" WHERE ");
		querySql.append(" emm_pm.pk_group = '").append(pk_group).append("' ");
		
		//20260508 yza 增加判断资产状态过滤
		querySql.append( "and pam_status.status_type not in (3,4) " );
		
		querySql.append(" AND pam_equip.card_status = 3 ");
		/**********************************************************************************************/
//		querySql.append(" 	WHERE ");
//		querySql.append(" 		emm_pm.pk_group = '").append(pk_group).append("' ");
		if (ArrayUtils.isNotEmpty(pk_orgs)) {
			querySql.append(" AND emm_pm.pk_org in ").append(
					InSqlManager.getInSQLValue(pk_orgs));
		}
		querySql.append("	AND emm_pm.dr = 0 ");
		querySql.append(" 	AND emm_pm.enablestate = ").append(
				IPubEnumConst.ENABLESTATE_ENABLE);
		querySql.append("	AND emm_pm.bill_type = '4B72' ");
		querySql.append("	AND ").append(pmTimeSql);
		querySql.append("	UNION               ");
		querySql.append("   SELECT DISTINCT   	");
		querySql.append("   	emm_pm.pk_pm	");
		querySql.append(" 	FROM emm_pm LEFT JOIN emm_pm_result ");
		querySql.append(" 	ON emm_pm.pk_pm = emm_pm_result.pk_pm ");
		querySql.append(" 	WHERE ");
		querySql.append(" 		emm_pm.pk_group = '").append(pk_group).append("' ");
		if (ArrayUtils.isNotEmpty(pk_orgs)) {
			querySql.append(" AND emm_pm.pk_org in ").append(
					InSqlManager.getInSQLValue(pk_orgs));
		}
		querySql.append("	AND emm_pm.dr = 0 ");
		querySql.append(" 	AND emm_pm.enablestate = ").append(
				IPubEnumConst.ENABLESTATE_ENABLE);
		querySql.append(" 	AND ( ");
		// 查询绩效型预防性维护预警
		querySql.append("  		( emm_pm.bill_type = '4B74' ");
		querySql.append("    	AND ").append(pmResuSql).append(" ) ");
		querySql.append("  	OR ");
		// 查询条件型预防性维护预警
		querySql.append("  		( emm_pm.bill_type = '4B76' ");
		querySql.append("    	AND (").append(pmTimeSql).append(" OR ")
				.append(pmResuSql).append(") ) ");
		querySql.append("       )");
		querySql.append(" ) ");
//		querySql.append(" and rownum = 1");

		return querySql.toString();

	}

}