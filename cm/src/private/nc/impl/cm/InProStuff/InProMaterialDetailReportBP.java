package nc.impl.cm.InProStuff;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bd.accperiod.InvalidAccperiodExcetion;
import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.pub.smart.data.DataSet;
import nc.pub.smart.metadata.DataTypeConstant;
import nc.pub.smart.metadata.Field;
import nc.pub.smart.metadata.MetaData;
import nc.pubitf.accperiod.AccountCalendar;
import nc.vo.bd.period2.AccperiodmonthVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import org.apache.commons.lang3.StringUtils;

import com.ufida.dataset.IContext;
/**
 * 在产材料清单统计-按照明细统计
 * @author yunfeng.li
 *
 */
public class InProMaterialDetailReportBP {
	IUAPQueryBS query = NCLocator.getInstance().lookup(IUAPQueryBS.class);

	IContext context = null;
	List<String> fieldnames = new ArrayList<>();
	public DataSet dealData(IContext context)
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
				{"cgeneralbid", DataTypeConstant.STRING,"材料出库单明细pk"},
				{"cmoid", DataTypeConstant.STRING,"生产订单单明细pk"},
				
				{"pk_costcenter", DataTypeConstant.STRING,"成本中心pk"},
				{"costcentercode", DataTypeConstant.STRING,"成本中心编码"},
				{"costcentername", DataTypeConstant.STRING,"成本中心名称"},
				
				{"ccostobjectid", DataTypeConstant.STRING,"产成品PK"},
				{"vcostobjcode", DataTypeConstant.STRING,"成本对编码"},
				{"vcostobjname", DataTypeConstant.STRING,"成本对象名称"},
				{"cmaterialvid", DataTypeConstant.STRING,"材料PK"},
				{"clcode", DataTypeConstant.STRING,"物料编码"},
				{"clname", DataTypeConstant.STRING,"物料名称"},
				{"nnum", DataTypeConstant.DOUBLE,"主数量"},
				{"nprice", DataTypeConstant.DOUBLE,"单价"},
				{"nmny", DataTypeConstant.DOUBLE,"金额"}
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
	 * 在产材料明细查询,
	 * 公用方法:在产材料汇总和在产材料判断都依赖次方法
	 * @param pk_org
	 * @param period
	 * @return
	 * @throws BusinessException 
	 */
	public List<Map<String, Object>> qryDataDetail(String pk_org, String period,String strWhere ) throws BusinessException{
		UFDate date = new UFDate(period+"-01");
		AccperiodmonthVO month = getCurrentAccPeriod(date, pk_org);
		String begindate = month.getBegindate().toString();
		String endate = month.getEnddate().toString();
		StringBuffer sql = new StringBuffer();
		sql.append(" select a.pk_group,a.pk_org,a.pk_org_v,");	
		 
	    sql.append(" a.cmoid  as cmoid,");//生产订单明细Pk
		sql.append(" a.cpmohid  as cpmohid,");//生产订单表头Pk
		sql.append(" a.cunitid   as cunitid ,");//产成品单位

		
		sql.append(" e.pk_costcenter as pk_costcenter,"); //成本中心
		sql.append(" costcenter.cccode as costcentercode,"); //成本中心
		sql.append(" costcenter.ccname  as costcentername,"); //成本中心
		
		sql.append(" f.ccostobjectid as  ccostobjectid,"); //产成品的成本对象
		sql.append(" f.vcostobjcode as  vcostobjcode,"); 
		sql.append(" f.VCOSTOBJNAME as  vcostobjname,"); 

		sql.append("  c.cgeneralhid  as cgeneralhid,");//材料出库单主键
		sql.append("  c.cgeneralbid  as cgeneralbid,");//材料出库单明细主键

		sql.append(" c.cmaterialvid   as cmaterialvid ,");//材料
		sql.append(" c.cmaterialoid   as cmaterialoid ,");

		sql.append(" h.code as clcode,");//材料编码
		sql.append(" h.name as clname,");//材料名称
		sql.append(" c.nnum  as  nnum, ");// 材料-数量
		sql.append(" c.cprojectid   as  cprojectid, ");// 材料-项目
		sql.append(" c.cunitid as cmeasdocid,");//材料主单位

		sql.append(" case  when substr(i.code,0,2)='07' then j.nabprice ");// 中间体取最新结存价
		sql.append(" when substr(i.code,0,2)='05' and h.name like '%晶%' then j.nabprice ");//原物料但是物料名称带晶的取最新结存价
		sql.append(" when  substr(i.code,0,2)='11'  then 0 ");
		sql.append(" else c.ncostprice end  as nprice");

		sql.append(" from mm_mo  a"); //流程生产订单明细
		sql.append(" inner join mm_pmo on a.cpmohid =mm_pmo.cpmohid ");
		sql.append(" inner join resa_ccdepts  e on a.cdeptid = e.pk_dept and  e.pk_org = a.pk_org "); //成本中心和部门对照表
		sql.append(" inner join resa_costcenter costcenter on e.pk_costcenter  =costcenter.pk_costcenter "); //成本中心
//		0=归集，1=最终， 
		sql.append(" inner join cm_costobject f on a.cmaterialvid  =f.cmaterialid  and f.enablestate =2 and f.itype=1 and nvl(a.cprojectid,'~')= nvl(f.cprojectid,'~') "); //产成品的成本对象
		/**---------1.在产材料------------*/
		sql.append(" left join  mm_pickm   b on  b.csourcebillrowid = a.cmoid");//备料
		sql.append(" left join   ic_material_b c  on  b.cpickmid = c.csourcebillhid"); //材料出库单
		sql.append(" left join  ic_material_h d on  c.cgeneralhid = d.cgeneralhid"); //材料出库单
		sql.append(" left join  bd_material h on c.cmaterialoid =h.pk_material");//		材料出库明细数据
		sql.append("  left  join  bd_marbasclass  i on  i.pk_marbasclass =h.pk_marbasclass");
		
		sql.append(" LEFT JOIN bd_material hccp ON  c.ccostobject  =hccp.pk_material ");// 产成品

		/**---------1.最新结存价------------*/
		sql.append(" left join( ");
		sql.append(" select  a.nabprice,a.cinventoryid ,cc.cprojectid ");
		sql.append(" from ia_monthnab a left join org_costregion orgcost on a.pk_org =orgcost.pk_costregion");
		//2024-09-01 liyf 增加项目维护
		sql.append(" left join ia_calcrange cc on a.ccalcrangeid =cc.ccalcrangeid");
		
		sql.append(" where a.dr=0");
		sql.append(" and  caccountperiod =(");
		sql.append(" select max(caccountperiod)");
		sql.append(" from    ia_monthnab b left join org_costregion orgcost on b.pk_org =orgcost.pk_costregion   ");

		sql.append(" where b.dr=0");
		sql.append(" and orgcost.pk_org='"+pk_org+"'");
		sql.append(" ) ");
		sql.append(" and orgcost.pk_org='"+pk_org+"'");
		sql.append(" ) j  on c.cmaterialoid=j.cinventoryid   and nvl(c.cprojectid,'~')= nvl(j.cprojectid,'~')");	 
	
				
		sql.append(" where "); 
		sql.append("  a.dr = 0   and b.dr = 0   and c.dr = 0   and d.dr = 0   and e.dr = 0   and f.dr = 0 "); 
		sql.append(" and a.pk_org ='"+pk_org+"'"	);
		sql.append(" and substr(i.code,0,2)<>'11'");//过虑掉虚拟物料
		sql.append(" and d.fbillflag =3"); //1=删除，2=自由，3=签字，4=审核，5=审核中，6=审核不通过，7=已调差状态，  
		sql.append(" and a.fitemstatus in(1)");// fitemstatus  行状态  fitemstatus int  流程生产订单行状态   0=自由，4=审批，1=投放，2=完工，3=关闭，    
		sql.append(" AND (d.dbilldate  >= '"+begindate+"' AND d.dbilldate   <= '"+endate+"' )");
		
		//2025-04-30 liyf 需要剔除在成本中心
		sql.append(" and costcenter.cccode not in (select code from bd_defdoc where dr=0 and  pk_defdoclist =(select  pk_defdoclist from bd_defdoclist where code='CM002_YF'))");//

		if(StringUtils.isNotEmpty(strWhere)){
			sql.append(strWhere);
		}
		sql.append(" order by vcostobjcode,clcode ");

		//test
//		sql.append(" and vcostobjcode='803534(COM)'");
//		sql.append(" and h.code='803523'");
		//
	
		
		List<Map<String,Object>> rs =  (List<Map<String, Object>>) query.executeQuery(sql.toString(),new MapListProcessor());
		if(rs == null || rs.size() ==0){
			return null;
		}
		  
		//取最新的产成品入库单单价:因为中间体例如805927 没有结存
		fillUpPrice(pk_org,rs);		
		//
		fillUpPrice2(pk_org,rs);	
		
		return rs;
		
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
			
		
        String pvcostobjcode =(String) context.getAttribute("vcostobjcode");//
		StringBuffer strWhere = new StringBuffer();
		if(StringUtils.isNotEmpty(pvcostobjcode)){
			strWhere.append(" and f.vcostobjcode ='"+pvcostobjcode+"'"	);
		}
		

		return qryDataDetail(pk_org, period, strWhere.toString());
	}
	/**
	 * 803180如果维护了计划价则取计划价
	 * @param pk_org
	 * @param rs
	 * @throws BusinessException
	 */
	private void fillUpPrice2(String pk_org, List<Map<String, Object>> rs) throws BusinessException {
		// TODO Auto-generated method stub
		Map<String,UFDouble> mid_price = new HashMap<String,UFDouble>();
		ColumnProcessor columnProcessor = new ColumnProcessor();
		for(Map<String,Object> rowdata:rs){
			String clcode = (String) rowdata.get("clcode");
			if(!"803180".equalsIgnoreCase(clcode)){
				continue;
			}
			String cmaterialvid = (String)rowdata.get("cmaterialvid");
			  UFDouble nprice =null;
			  if(mid_price.containsKey(cmaterialvid)){
				  nprice = mid_price.get(cmaterialvid);
			  }else{
				  StringBuffer sql2 = new StringBuffer();
				   sql2.append(" select  h.planedprice  from  bd_materialcostmod h");
				   sql2.append(" left join org_costregion orgcost on h.pk_costregion =orgcost.pk_costregion");
				   sql2.append(" where h.dr=0  ");
				   sql2.append(" and costmode=5");//计价方式 =计划价
				   sql2.append(" and orgcost.pk_org='"+pk_org+"'");
				   sql2.append(" and h.pk_material ='"+ rowdata.get("cmaterialvid")+"'");
				   Object planedprice = query.executeQuery(sql2.toString(), columnProcessor);
				   if(planedprice!=null){
					   nprice = new UFDouble(planedprice.toString());
				   }
			  }
			  //如果询到计划价,则重新计算金额等
			  if(nprice != null ){
				  mid_price.put(cmaterialvid, nprice);
				   rowdata.put("nprice", nprice);
				   //计算金额
				   String value = rowdata.get("nnum")==null? "0":rowdata.get("nnum").toString();
					UFDouble nnum = new UFDouble(value);
					rowdata.put("nnum", nnum);//
					UFDouble nmny= nnum.multiply(nprice);
					rowdata.put("nmny", nmny);// 
			  }
			  
			  
			 
		}

	}

	/**
	 * 取最新的产成品入库单单价:因为中间体例如805927 没有结存
	 * @param pk_org
	 * @param rs
	 * @throws BusinessException 
	 */
	private void fillUpPrice(String pk_org,List<Map<String, Object>> rs) throws BusinessException {
		// TODO Auto-generated method stub
		Map<String,UFDouble> mid_price = new HashMap<String,UFDouble>();
		ColumnProcessor columnProcessor = new ColumnProcessor();
		for(Map<String,Object> rowdata:rs){
			String cmaterialvid = (String) rowdata.get("cmaterialvid");
			String cprojectid = (String) rowdata.get("cprojectid");
			if(StringUtils.isEmpty(cprojectid)){
				cprojectid ="~";
			}
			  Object nprice1 = rowdata.get("nprice");
			  UFDouble nprice =null;
			   if(nprice1 == null){
				   nprice = mid_price.get(cmaterialvid);
			   }else{
				   nprice= new UFDouble(nprice1.toString());
			   }
	
			   if(nprice == null){
				   StringBuffer sql = new StringBuffer();
				   sql.append(" select  a.nabprice  ");
					sql.append(" from ia_monthnab a left join org_costregion orgcost on a.pk_org =orgcost.pk_costregion");
					//2024-09-01 liyf 增加项目维护
					sql.append(" left join ia_calcrange cc on a.ccalcrangeid =cc.ccalcrangeid");
					sql.append(" where a.dr=0");
					sql.append(" and  caccountperiod =(");
					sql.append(" select max(caccountperiod)");
					sql.append(" from    ia_monthnab b left join org_costregion orgcost on b.pk_org =orgcost.pk_costregion   ");
					sql.append(" where b.dr=0");
					sql.append(" and orgcost.pk_org='"+pk_org+"'");
					sql.append(" ) ");
					sql.append(" and orgcost.pk_org='"+pk_org+"'");
					sql.append(" and a.cinventoryid='"+cmaterialvid+"'");
					sql.append("  and nvl(cc.cprojectid,'~')='"+cprojectid+"'");
					Object nprice2 = query.executeQuery(sql.toString(), columnProcessor);
					if(nprice2!=null){
						 nprice = new UFDouble(nprice2.toString());
					}
			   }
			   if(nprice == null){
				   StringBuffer sql2 = new StringBuffer();
				   sql2.append(" select  b.nprice from  ia_i3bill h");
				   sql2.append(" left join ia_i3bill_b b on h.cbillid=b.cbillid  ");
				   sql2.append(" left join org_costregion orgcost on h.pk_org =orgcost.pk_costregion  ");
				   sql2.append(" where h.dr=0 and b.dr=0 ");
				   sql2.append(" and orgcost.pk_org='"+pk_org+"'");
				   sql2.append(" and nvl(b.nprice,0)<>0 ");
				   sql2.append(" and b.cinventoryid='"+ rowdata.get("cmaterialvid")+"'");
				   sql2.append(" order by h.dbilldate desc ");
				   Object nprice2 = query.executeQuery(sql2.toString(), columnProcessor);
				   if(nprice2!=null){
					   nprice = new UFDouble(nprice2.toString());
				   }else{
					   nprice = UFDouble.ZERO_DBL;
				   }
				  
				   mid_price.put(cmaterialvid, nprice);
			   }
			   rowdata.put("nprice", nprice);
			   
			   //2023-05-16 803180 取当前计划价
			   
			   
			   //计算金额
			   String value = rowdata.get("nnum")==null? "0":rowdata.get("nnum").toString();
				UFDouble nnum = new UFDouble(value);
				rowdata.put("nnum", nnum);//
				
				UFDouble nmny= nnum.multiply(nprice);
				rowdata.put("nmny", nmny);//
		}

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
