package nc.impl.cm.InProStuff;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bd.accperiod.InvalidAccperiodExcetion;
import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
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
 * 在产材料清单统计-按照产成品维度统计
 * @author yunfeng.li
 *
 */
public class InProMaterialReportBP {
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
				{"pk_costcenter", DataTypeConstant.STRING,"成本中心pk"},
				{"costcentercode", DataTypeConstant.STRING,"成本中心编码"},
				{"costcentername", DataTypeConstant.STRING,"成本中心名称"},
				{"ccostobjectid", DataTypeConstant.STRING,"产成品PK"},
				{"vcostobjcode", DataTypeConstant.STRING,"产品物料编码"},
				{"vcostobjname", DataTypeConstant.STRING,"成本对象"},
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
		
		List<Map<String, Object>> rs = new InProMaterialDetailReportBP().qryDataDetail(pk_org, period, strWhere.toString());
		
		//进行汇总统计
		Map<String,Map<String,Object>> grouprs = new HashMap<String, Map<String,Object>>();
		for(Map<String,Object> rowdata:rs){
			String value = rowdata.get("nnum")==null? "0":rowdata.get("nnum").toString();
			UFDouble nnum = new UFDouble(value);
			rowdata.put("nnum", nnum);//
			
			
		     value = rowdata.get("nprice")==null? "0":rowdata.get("nprice").toString();
			UFDouble nprice = new UFDouble(value);
			rowdata.put("nprice", nprice);//
			
			UFDouble nmny= nnum.multiply(nprice);
			rowdata.put("nmny", nmny);//

			//按照成本中心+产成品+物料汇总统
			String costcentercode = (String) rowdata.get("costcentercode");
			String vcostobjcode = (String) rowdata.get("vcostobjcode");
			String clcode = (String) rowdata.get("clcode");
			String groupKey = costcentercode+vcostobjcode+clcode;
			if(!grouprs.containsKey(groupKey)){
				grouprs.put(groupKey, rowdata);
			}else{
				Map<String, Object> map = grouprs.get(groupKey);
				UFDouble nnum1= (UFDouble) map.get("nnum");
				nnum1 = nnum1.add(nnum);
				map.put("nnum", nnum1);//

				UFDouble nmny1= (UFDouble) map.get("nmny");
				nmny1 = nmny1.add(nmny);
				map.put("nmny", nmny1);//
			}
			
		}
		List<Map<String,Object>> rs2 = new ArrayList<>();
		rs2.addAll(grouprs.values());
		return rs2;
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
