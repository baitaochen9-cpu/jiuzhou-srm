package nc.bs.cm.equivrate.bp;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import nc.bd.accperiod.InvalidAccperiodExcetion;
import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.pub.smart.data.DataSet;
import nc.pub.smart.metadata.DataTypeConstant;
import nc.pub.smart.metadata.Field;
import nc.pub.smart.metadata.MetaData;
import nc.pubitf.accperiod.AccountCalendar;
import nc.vo.bd.period2.AccperiodmonthVO;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.AppContext;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.trade.voutils.SafeCompute;

import org.apache.commons.lang3.StringUtils;

import com.ufida.dataset.IContext;
/**
 * 在产订单查询报表
 * @author yunfeng.li
 *
 */
public class ProWrOrderReportBP {

	IContext context = null;
	List<String> fieldnames = new ArrayList<>();
	public DataSet proWrOrders(IContext context)
			throws BusinessException {
		this. context = context;
		Field[] filds  = getField();
	
		Object[][] datas = getDatas();
		
		DataSet ds = new DataSet();
		ds.setDatas(datas);
		
		MetaData md = new MetaData(filds);
		ds.setMetaData(md);
		return ds;
	}
	
	/**
	 * 提供报表所需元数据
	 * 
	 * @return
	 */
	private Field[] getField() {
		Object[][] rowkeys= new Object[][]{
				{"pk_group", DataTypeConstant.STRING,"pk_group"},
				{"pk_org", DataTypeConstant.STRING,"pk_org"},
				{"vsrccode", DataTypeConstant.STRING,"计划单号"},
				{"vbillcode", DataTypeConstant.STRING,"生产订单号"},
				{"fbillstatus", DataTypeConstant.STRING,"订单状态  "},
				{"cmaterialvid", DataTypeConstant.STRING,"产成品PK"},
				{"ccpcode", DataTypeConstant.STRING,"产品物料编码"},
				{"ccpname", DataTypeConstant.STRING,"产品物料名称"},
				{"bzgs", DataTypeConstant.STRING,"标准完工天数"},
				{"vbatchcode", DataTypeConstant.STRING,"产品批次号  "},
				{"fitemstatus", DataTypeConstant.STRING,"订单行状态 "},
				{"tactstarttime", DataTypeConstant.STRING,"实际开工时间"}, 
				{"tactendtime", DataTypeConstant.STRING,"实际完工时间 "},
				{"tmoclosedtime", DataTypeConstant.STRING,"关闭时间  "},
				{"nmmnum", DataTypeConstant.DOUBLE,"计划产出主数量"},
				{"nwrnum", DataTypeConstant.DOUBLE,"完工主数量  "},
				{"cyts", DataTypeConstant.DOUBLE,"差异天数"},
				{"ydxs", DataTypeConstant.DOUBLE,"约当系数"}
		};
		List<Field> fieldlist = new ArrayList<Field>();
		for(int i=0;i<rowkeys.length;i++){
			Field field = new Field();
			field.setFldname((String)rowkeys[i][0]);//列编码
			field.setDataType((int)rowkeys[i][1]);//列数据类型
			field.setCaption((String)rowkeys[i][2]);//列名称
			//
			fieldnames.add((String)rowkeys[i][0]);
			
			fieldlist.add(field);
		}
		return fieldlist.toArray(new Field[0]);

	}
	
	/**
	 *查询数据集
	 * @throws BusinessException 
	 */
	@SuppressWarnings({ "serial", "unchecked" })
	private List<Map<String, Object>> qryDataList() throws BusinessException {  
		String pk_org=(String) context.getAttribute("pk_org");
		//语义模型设计的时候,查询参数不会传值
		if(StringUtils.isEmpty(pk_org)){
			return null;
		}
	
		String period =(String) context.getAttribute("period");//组织可能有默认值
		if(StringUtils.isEmpty(period)){
			return null;
		}
		UFDate date = new UFDate(period+"-01");
		AccperiodmonthVO month = getCurrentAccPeriod(date, pk_org);
		String begindate = month.getBegindate().toString();
		String endate = month.getEnddate().toString();
		StringBuffer sql = new StringBuffer();
		sql.append(" select  ");
		sql.append(" orderh.pk_group,");
		sql.append(" orderh.pk_org,");
		sql.append(" orderb.vsrccode ,");//计划单号
		sql.append(" orderh.vbillcode  ,");//生产订单号
		sql.append("CASE orderh.fbillstatus    WHEN 0    THEN '自由'    WHEN 1    THEN '审批'    WHEN 2    THEN '提交'    WHEN 3    THEN '审批中'    WHEN 4    THEN '审批不通过'    ELSE '未知状态'  END AS fbillstatus,");
		sql.append(" orderb.cmaterialvid  ,");//产成品PK
		sql.append(" ccp.code as ccpcode,");//产品物料编码
		sql.append(" ccp.name as ccpname,");//产品物料名称
		sql.append(" bom.hvdef1 as bzgs,");// 标准完工天数
		sql.append(" orderb.vbatchcode,");// 产品批次号  
//		  fitemstatus   fitemstatus int  流程生产订单行状态   0=自由，4=审批，1=投放，2=完工，3=关闭，    
	    sql.append("CASE orderb.fitemstatus    WHEN 0    THEN '自由'    WHEN 1    THEN '投放'    WHEN 2    THEN '完工'    WHEN 3    THEN '关闭'    WHEN 4    THEN '审批'    ELSE '未知状态'  END AS fitemstatus, ");
		sql.append(" orderb.tactstarttime,");// 实际开工时间
		sql.append(" orderb.tactendtime,");//  实际完工时间 
		sql.append(" orderb.tmoclosedtime,");//  关闭时间  
		sql.append(" orderb.nmmnum,");//  计划产出主数量  
		sql.append(" orderb.nwrnum");//   完工主数量  
		sql.append(" from mm_mo orderb");//流程生产订单明细
		sql.append(" inner join mm_pmo orderh ON orderb.cpmohid= orderh.cpmohid ");
		sql.append(" left join bd_bom bom ON orderb.cbomversionid = bom.cbomid");//bom
		sql.append(" left join bd_material ccp on orderb.cmaterialvid=  ccp.pk_material ");
		sql.append(" JOIN resa_ccdepts s ON orderb.cdeptid = s.pk_dept");//生产部门对应成本中心过滤
		sql.append(" JOIN resa_costcenter r ON s.pk_costcenter = r.pk_costcenter");
//		sql.append(" JOIN cm_costobject t ON orderb.cmaterialid      = t.cmaterialid");//成本对象
		sql.append(" WHERE  NVL(orderb.dr, 0)      = 0 and nvl(orderh.dr,0)=0");
		sql.append(" AND orderh.pk_group        = '"+AppContext.getInstance().getPkGroup()+"'");
		sql.append(" AND orderh.pk_org          = '"+pk_org+"' ");
		sql.append(" AND ((orderb.tactstarttime  >= '"+begindate+"' AND orderb.tactstarttime   <= '"+endate+"' )  or" +
				" (orderb.tactendtime  >= '"+begindate+"' AND orderb.tactendtime   <= '"+endate+"' ))");
		//  fitemstatus  行状态  fitemstatus int  流程生产订单行状态   0=自由，4=审批，1=投放，2=完工，3=关闭，    
//		sql.append("  and o.fitemstatus in (1) ");
		sql.append(" and r.cccode not in ('PBSR')");//排除技术服务部—溶媒回收
		sql.append(" order by  vsrccode,vbillcode ");

		IUAPQueryBS query = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		
		List<Map<String,Object>> rs =  (List<Map<String, Object>>) query.executeQuery(sql.toString(),new MapListProcessor());
		if(rs == null || rs.size() ==0){
			return rs;
		}
		//月末日期
		UFDateTime dcurdate = new UFDateTime(month.getEnddate().toDate());
		long endTime =dcurdate.getMillis();
		//计算约当系数
		for(Map<String,Object> rowdata:rs){
			String tactstarttime = (String) rowdata.get("tactstarttime");//实际开始时间
			long beginTime = new UFDateTime(tactstarttime).getMillis();
			long diff_minute1 = (endTime-beginTime)/1000/60;			
			UFDouble diff_houre = new UFDouble(diff_minute1).div(60.00);
			UFDouble diff_day =diff_houre.div(24).setScale(2, UFDouble.ROUND_HALF_UP);//差异天数
			//按小时换算天
			UFDouble nequivrate = UFDouble.ZERO_DBL;	
			String bzgs = (String) rowdata.get("bzgs");
			if (StringUtil.isEmpty(bzgs)
					|| "~".equals(bzgs)) {
				rowdata.put("ydxs", null);
			} else {
				
//				UFDouble biaozhun_houre = new UFDouble(vo.getHvdef1()).multiply(24);
				UFDouble biaozhun_day = new UFDouble(bzgs);
				nequivrate = SafeCompute.div(diff_day, biaozhun_day);
			}
			if(nequivrate.doubleValue()>1){
				nequivrate =UFDouble.ONE_DBL;
			}
			rowdata.put("cyts", diff_day);//差异天数
			rowdata.put("ydxs", nequivrate);//约当系数

		}
		
		
	
		return rs;
	}
	
	/***
	 * 数据排列
	 * @param fieldnames 
	 * @return
	 * @throws BusinessException 
	 */
	private Object[][] getDatas() throws BusinessException {
		List<Object[]> alldatas = new ArrayList<Object[]>();
		List<Map<String, Object>>  dataList = qryDataList();
		if(null == dataList || dataList.size() == 0){ 
			return null;
		}		
		for(Map<String,Object> hand : dataList){ 
			List<Object> listnew = new ArrayList<Object>(); //创建单行缓存
			for(String fieldkey :fieldnames){
								listnew.add(hand.get(fieldkey));

			}
			alldatas.add(listnew.toArray(new Object[0]));
		}
		
		return alldatas.toArray(new Object[0][0]);
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
	


}
