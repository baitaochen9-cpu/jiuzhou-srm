package nc.bs.cm.equivrate.bp;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bd.accperiod.InvalidAccperiodExcetion;
import nc.bs.framework.common.NCLocator;
import nc.impl.pubapp.pattern.data.bill.BillQuery;
import nc.itf.cm.equivrate.IEquivrateMaintainService;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ArrayListProcessor;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.pubitf.accperiod.AccountCalendar;
import nc.vo.bd.period2.AccperiodmonthVO;
import nc.vo.cm.equivrate.entity.EquivrateAggVO;
import nc.vo.cm.equivrate.entity.EquivrateItemVO;
import nc.vo.cm.equivrate.entity.EquivrateVO;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.obm.util.DateUtils;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.AppContext;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.trade.voutils.SafeCompute;
/***
 * 自动约当
 * @author yunfeng.li
 *
 */
public class EquivrateSaveBP {

	public EquivrateAggVO[] create(String pk_org, String period)
			throws BusinessException {
		//查询当月在产的生产订单,生成标准工时
		UFDate date = new UFDate(period + "-01");
		AccperiodmonthVO month = getCurrentAccPeriod(date, pk_org);
		List<EquivrateVO> list = getListEquivrateVO(pk_org, month);
		if(list == null || list.size() ==0){
			throw new BusinessException("当月没有在产的订单,不需要自动约当");
		}
		UFDateTime dcurdate = new UFDateTime(month.getEnddate().toDate());

		Map<String, List<EquivrateVO>> map = new HashMap<>();
		for (EquivrateVO vo : list) {
			vo.setDcurdate(dcurdate);
			String key = vo.getCcostobjectid() + vo.getPk_costcenter();
			List<EquivrateVO> elist = null;
			if (map.containsKey(key)) {
				elist = map.get(key);
			} else {
				elist = new ArrayList<>();
			}
			elist.add(vo);
			map.put(key, elist);
		}
		//上个会计期间
		UFDate predate = new UFDate(DateUtils.getPreviousMonth(date	.getMillis()));
		AccperiodmonthVO premonth = getCurrentAccPeriod(predate, pk_org);

		List<EquivrateAggVO> addlist = new ArrayList<>();
		List<EquivrateAggVO> updatelist = new ArrayList<>();
		StringBuffer errorMsg = new StringBuffer();
		for (Map.Entry<String, List<EquivrateVO>> entry : map.entrySet()) {
			String key = entry.getKey();
			List<EquivrateVO> elist = entry.getValue();
			// 成本对象 成本中心对应的list集合
			// 根据 会计期间判断本月是否存在 约当系数 如果存在更新 如果不存在 则查上一个月是否存在
			if (list == null || list.size() == 0)
				continue;
			// 计算月当系数
			UFDouble nequivrate = UFDouble.ZERO_DBL;
			nequivrate = calquivrate(elist);

			String ccostobjectid = elist.get(0).getCcostobjectid();
			String pk_costcenter = elist.get(0).getPk_costcenter();
			//查询当期的约当系数
			EquivrateAggVO equivo[] = queryEquivrateAggVO(ccostobjectid,
					pk_costcenter, pk_org, period);

			if (equivo != null  && equivo.length > 0) {
				// 更新约当系数
				for (EquivrateAggVO vo : equivo) {
					produceEquivrateAggVO(vo, pk_org, period, nequivrate, false);
					vo.getParentVO().setVnote("自动约当更新");
					updatelist.add(vo);
				}
			} else {			
				//查询约当系数模板
				EquivrateAggVO[] equivo1 = queryEquivrateAggVO(ccostobjectid,
						pk_costcenter, pk_org, null);
				if (equivo1 == null || equivo1.length == 0){
					String msg = "成本中心["+elist.get(0).getCcname()+"],成本对象["+elist.get(0).getVcostobjcode()+"]";
					errorMsg.append(msg+"\n");
					continue;
				}
				// 插入约当系数
				for (EquivrateAggVO vo : equivo1) {
					produceEquivrateAggVO(vo, pk_org, period, nequivrate, true);
					vo.getParentVO().setVnote("自动约当");
					addlist.add(vo);
				}
			}
		}
		if(errorMsg.length() >0){
			String errTitle="以下成本对象 约当系数模板不存在,请维护:\n";
			throw new BusinessException(errTitle+errorMsg.toString());
		}

		
		List<EquivrateAggVO> rs = new ArrayList<>();

		IEquivrateMaintainService service = NCLocator.getInstance().lookup(
				IEquivrateMaintainService.class);
		if (addlist != null && addlist.size() > 0) {
			service.insert(addlist.toArray(new EquivrateAggVO[addlist.size()]));
			rs.addAll(addlist);
		}

		if (updatelist != null && updatelist.size() > 0) {
			service.update(updatelist.toArray(new EquivrateAggVO[updatelist
					.size()]));
			rs.addAll(updatelist);

		}

		return rs.toArray(new EquivrateAggVO[0]);
	}
	/**
	 * 查询投产状态的计划订单
	 * @param pk_org
	 * @param month
	 * @return
	 * @throws BusinessException
	 */
	private List<EquivrateVO> getListEquivrateVO(String pk_org,
			AccperiodmonthVO month) throws BusinessException {
		String begindate = month.getBegindate().toString();
		String endate = month.getEnddate().toString();
		StringBuffer strb = new StringBuffer();
		strb.append(" select distinct cmoid  , f.ccostobjectid,  f.vcostobjcode ,  f.vcostobjname,  ");
		strb.append(" o.cbomversionid,  s.pk_costcenter,  r.cccode,  r.ccname,  o.tactstarttime  as tactstarttime,  mm_pmo.dmakedate,");
//		strb.append(" m.nmmnum as hvdef2, ");//计划产出主数量 
		strb.append(" om.hvdef1");// om.hvdef1 --BOM标准工时
		strb.append(" from mm_mo o");
		strb.append(" INNER JOIN mm_pmo mm_pmo ON o.cpmohid= mm_pmo.cpmohid ");
		strb.append(" JOIN bd_bom om ON o.cbomversionid = om.cbomid");
		strb.append(" JOIN resa_ccdepts s ON o.cdeptid = s.pk_dept");
		strb.append(" JOIN resa_costcenter r ON s.pk_costcenter = r.pk_costcenter");
//		strb.append(" JOIN cm_costobject t ON o.cmaterialid      = t.cmaterialid");
//		0=归集，1=最终， 
		strb.append(" inner join cm_costobject f on o.cmaterialvid  =f.cmaterialid  and f.enablestate =2 and f.itype=1 and nvl(o.cprojectid,'~')= nvl(f.cprojectid,'~') "); //产成品的成本对象
	
		strb.append(" WHERE  NVL(o.dr, 0)      = 0 and nvl(mm_pmo.dr,0)=0");
		strb.append(" AND mm_pmo.pk_group        = '"+AppContext.getInstance().getPkGroup()+"'");
		strb.append(" AND mm_pmo.pk_org          = '"+pk_org+"' ");
		strb.append(" AND (o.tactstarttime  >= '"+begindate+"' AND o.tactstarttime   <= '"+endate+"' )");
		//  fitemstatus  行状态  fitemstatus int  流程生产订单行状态   0=自由，4=审批，1=投放，2=完工，3=关闭，    
		strb.append("  and o.fitemstatus in (1) ");
		//2025-04-30 liyf 需要剔除在成本中心
		strb.append(" and r.cccode not in (select code from bd_defdoc where dr=0 and  pk_defdoclist =(select  pk_defdoclist from bd_defdoclist where code='CM001_YF'))");//排除技术服务部—溶媒回收

		IUAPQueryBS query = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		List<EquivrateVO> list = (List<EquivrateVO>) query.executeQuery(strb.toString(),new BeanListProcessor(EquivrateVO.class));
		return list;
	}
	
	/**
	 * 计算约当系数
	 * @param elist
	 * @return
	 * @throws ParseException 
	 */
	private UFDouble calquivrate(List<EquivrateVO> elist) {
		int size = elist.size();
		UFDouble sum = UFDouble.ZERO_DBL;
		UFDouble avg_quivrate = UFDouble.ZERO_DBL;
		UFDouble  baifen =new UFDouble(100);
//		UFDouble sum_nmmnum  = UFDouble.ZERO_DBL;
		for (EquivrateVO vo : elist) {
			//按小时换算天
			long endTime =vo.getDcurdate().getMillis();
			long beginTime =vo.getTactstarttime().getMillis();
			long diff_minute1 = (endTime-beginTime)/1000/60;			
//			UFDouble diff_minute = new UFDouble(diff_minute1);
			UFDouble diff_houre = new UFDouble(diff_minute1).div(60.00);
			UFDouble diff_day =diff_houre.div(24).setScale(2, UFDouble.ROUND_HALF_UP);
			UFDouble nequivrate = UFDouble.ZERO_DBL;	
//			UFDouble nmmnum  = new UFDouble(vo.getHvdef2());
			if (StringUtil.isEmpty(vo.getHvdef1())
					|| "~".equals(vo.getHvdef1())) {
//				nequivrate =  SafeCompute.div(new UFDouble(diff), new UFDouble(
//						100));
			} else {
				UFDouble biaozhun_day = new UFDouble(vo.getHvdef1());
//				UFDouble biaozhun_houre = new UFDouble(vo.getHvdef1()).multiply(24);
				nequivrate = SafeCompute.div(diff_day, biaozhun_day);
			}
			if(nequivrate.doubleValue()>1){
				nequivrate =UFDouble.ONE_DBL;
			}
			sum = SafeCompute.add(sum, nequivrate);
		}
		avg_quivrate = SafeCompute.div(sum, new UFDouble(size));
		int xs = SafeCompute.multiply(avg_quivrate, baifen).intValue();
		return new UFDouble(xs);
	}

	private void produceEquivrateAggVO(EquivrateAggVO vo, String pk_org,
			String period, UFDouble nequivrate, boolean isadd) {

		if (isadd) {
			vo.getParentVO().setPrimaryKey(null);
			vo.getParentVO().setCperiod(period);
			vo.getParentVO().setCreationtime(new UFDateTime());
			vo.getParentVO().setCreator(AppContext.getInstance().getPkUser());
			vo.getParentVO().setModifiedtime(null);
			vo.getParentVO().setModifier(null);
			vo.getParentVO().setStatus(VOStatus.NEW);
		} else {
			vo.getParentVO().setModifiedtime(new UFDateTime());
			vo.getParentVO().setModifier(AppContext.getInstance().getPkUser());
			vo.getParentVO().setStatus(VOStatus.UPDATED);
		}

		for (EquivrateItemVO child : vo.getItemVO()) {
			if (isadd) {
				child.setPrimaryKey(null);
				child.setStatus(VOStatus.NEW);
			} else {
				child.setStatus(VOStatus.UPDATED);
			}
			child.setNequivrate(nequivrate);
		}
	}
	

	private AccperiodmonthVO getCurrentAccPeriod(String pk_org) {
		return getCurrentAccPeriod(AppContext.getInstance().getBusiDate(),
				pk_org);
	}

	private AccperiodmonthVO getCurrentAccPeriod(UFDate date, String pk_org) {
		AccountCalendar calendar = AccountCalendar.getInstanceByPk_org(pk_org);
		try {
			calendar.setDate(date);
		} catch (InvalidAccperiodExcetion e) {
			ExceptionUtils.wrappException(e);
		}
		return calendar.getMonthVO();
	}

	private EquivrateAggVO[] queryEquivrateAggVO(String ccostobjectid,
			String pk_costcenter, String pk_org, String period)
			throws BusinessException {
		StringBuffer strb = new StringBuffer();
		strb.append(" select  cequivrateid from cm_equivrate where nvl(dr,0) = 0 ");
		strb.append(" and ccostobjectid   ='" + ccostobjectid + "'");
		strb.append(" and ccostcenterid  ='" + pk_costcenter + "'");
		strb.append(" and pk_org    ='" + pk_org + "'");
		if(period == null){
			StringBuffer maxSql = new StringBuffer();
			maxSql.append(" select max(cperiod) cperiod from cm_equivrate where nvl(dr,0) = 0"); 
			maxSql.append(" and ccostobjectid   ='" + ccostobjectid + "'");
			maxSql.append(" and ccostcenterid  ='" + pk_costcenter + "'");
			maxSql.append(" and pk_org    ='" + pk_org + "'");		
			strb.append(" and cperiod   =(" + maxSql + ")");
		}else{
			strb.append(" and cperiod   ='" + period + "'");

		}
		
		// String cequivrateid = getStrFsql(strb.toString());
		//
		// if (StringUtil.isEmpty(cequivrateid)) {
		// return null;
		// }

		IUAPQueryBS query = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		List list = (List) query.executeQuery(strb.toString(),
				new ArrayListProcessor());

		if (list == null || list.size() == 0)
			return null;
		List<String> slist = new ArrayList<>();
		for(Object o :list){
			Object[] objs = (Object[])o; 
			String s = objs[0].toString();
			slist.add(s);
		}
		BillQuery<EquivrateAggVO> query1 = new BillQuery<EquivrateAggVO>(
				EquivrateAggVO.class);
		// String[] cpkd = new String[] { cequivrateid };
		EquivrateAggVO[] bills = query1.query(slist.toArray(new String[slist
				.size()]));
		if (bills == null || bills.length == 0)
			return null;
		return bills;

	}

	private String getStrFsql(String sql) throws BusinessException {
		IUAPQueryBS query = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		List list = (List) query.executeQuery(sql, new ArrayListProcessor());

		if (list != null && list.size() > 0) {
			String s = ((Object[]) list.get(0))[0].toString();
			return s;
		}
		return null;
	}

}
