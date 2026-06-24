package nc.bs.jz.imp.statement;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.jdbc.framework.processor.ProcessorUtils;
import nc.jdbc.framework.processor.ResultSetProcessor;
import nc.pub.jz.itf.statement.SluggishMaterils;
import nc.pub.smart.data.DataSet;
import nc.pub.smart.metadata.DataTypeConstant;
import nc.pub.smart.metadata.Field;
import nc.pub.smart.metadata.MetaData;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;

import com.ufida.dataset.IContext;

/**
 * 
 * @author yezhian
 *呆滞物料查询报表，依托自定义档案进行计算
 */
public class SluggishMaterilsImp implements SluggishMaterils{

	private BaseDAO dao = null;
	private BaseDAO getDao(){
			if(null == dao ){
				dao = new BaseDAO();
			}
		return dao;
		
	}
	
	IContext context = null;
	List<String> fieldnames = new ArrayList<>();
	public DataSet rayBowSluggishMaterils(IContext context)
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
				{"cproductorid", DataTypeConstant.STRING,"产厂商"},
				{"cmaterialvid", DataTypeConstant.STRING,"物料ID"},
				
				{"material_code", DataTypeConstant.STRING,"物料CODE"},
				{"material_name", DataTypeConstant.STRING,"物料name"},
				{"costcentername", DataTypeConstant.STRING,"成本中心名称"},
				
				{"pk_batchcode", DataTypeConstant.STRING,"批次ID"},
				{"cwarehouseid", DataTypeConstant.STRING,"仓库"},
				{"vbatchcode", DataTypeConstant.STRING,"批次CODE"},
				{"pk_measdoc", DataTypeConstant.STRING,"单位"},
				{"dvalidate", DataTypeConstant.STRING,"失效日期"},
				{"cstateid", DataTypeConstant.STRING,"库存状态"},
				{"dproducedate", DataTypeConstant.DOUBLE,"生产日期"},
				{"nonhandnum", DataTypeConstant.DOUBLE,"当前结存数量"},
				{"plan_num", DataTypeConstant.DOUBLE,"计划用量"},
				{"residue_num", DataTypeConstant.DOUBLE,"扣减余数"}
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
	
	/***
	 * 数据排列
	 * @param fieldnames 
	 * @return
	 */
	private Object[][] getDatas() {
		List<Object[]> alldatas = new ArrayList<Object[]>();
		String pk_org=(String) context.getAttribute("pk_org");
		//语义模型设计的时候,查询参数不会传值
		if(StringUtils.isEmpty(pk_org)){
			return null;
		}
	
		String period =(String) context.getAttribute("period");//组织可能有默认值
		if(StringUtils.isEmpty(period)){
			return null;
		}
		Map<String,List<Map<String, Object>>> onHand = getOnHand();
		 Map<String,Map<String,Object>> bomMaterial = getBomMaterial();
		if(null == bomMaterial || bomMaterial.size() == 0){ //如果这里都没有数据 ，那么就是没有维护我们设计的档案 或bom和档案 的计算有问题
			// 不管是哪一种结论，都 返回空值就好了。
			return null;
		}		
		
		/*
		 * 还是要以存量的数据为基准数，再检查需要消耗的材料，然后按物料号去逐一的减，
		 * 改变代码思路，不应该以消耗物料驱动去创建返回数据，而是存量数据应该全部返回才对，存量数据的每个对象都需要保留
		 */
		
		for(String key :onHand.keySet()){/*同物料批次集合*/
			Map<String,Object> materialList = bomMaterial.get(key);//指向的bom对应的物料消耗，这个可能会空
			UFDouble i = materialList == null ? UFDouble.ZERO_DBL : new UFDouble(materialList.get("num").toString());
			for(Map<String,Object> hand : onHand.get(key)){ //批次物料
				List<Object> listnew = new ArrayList<Object>(); //创建单行缓存
				for(String fieldkey :fieldnames){
					if(fieldkey.equals("plan_num") ){
						if(materialList == null){
							listnew.add(UFDouble.ZERO_DBL.doubleValue());
						}else{
							listnew.add(new UFDouble(materialList.get("num").toString()).doubleValue() );
							
						}
					}else if(fieldkey.equals("residue_num")  ){
						UFDouble nonhandnum = new UFDouble( hand.get("nonhandnum").toString());
						if(null == materialList){
							listnew.add(nonhandnum);
						}else {
							
							if( nonhandnum.sub(i).doubleValue() >= 0 ){
								listnew.add(nonhandnum.sub(i).doubleValue());
								i = UFDouble.ZERO_DBL;
							}else{
								listnew.add(UFDouble.ZERO_DBL);
								i=i.sub(nonhandnum);
							} 
							
						}
						
					}else if(fieldkey.equals("nonhandnum")){
						listnew.add(new UFDouble(hand.get("nonhandnum").toString()).doubleValue() );
					}else
//					if(fieldkey.equals("dproducedate") && null != hand.get("dproducedate") ){
//						listnew.add(hand.get("dproducedate").toString());
//					}else
					{
						listnew.add(hand.get(fieldkey));
					}
				}
				alldatas.add(listnew.toArray(new Object[0]));
			}
		}
		
		return alldatas.toArray(new Object[0][0]);
	}
 /**
  * 取计划消耗量
  */
  @SuppressWarnings("unchecked")
private  Map<String,Map<String,Object>> getBomMaterial() {
	  StringBuffer sql = new StringBuffer();  /*BOM计算出的消耗物料*/
	  sql.append(" select cmaterialvid,cassmeasureid,sum(num) num ");
	  sql.append(" from( ");
	  sql.append(" select bd_bom.fbillstatus ");  /*-1=自由，0=审批不通过，1=审批通过，2=审批中，  */
	  sql.append(" ,bd_bom.cbomid ");
	  sql.append(" ,bd_bom.hfversiontype ");  /*1=有效版本，2=无效版本， */
	  sql.append("  ,bd_bom.hcmaterialid  "); /*产品*/
	  sql.append("  ,bd_bom.vbillcode ");
	  sql.append("  ,bd_bom.hversion ");
	  sql.append(" ,bd_bom_b.cmaterialvid "); /*原料*/ 
      sql.append(" ,bd_bom_b.cproductorid  ");/*生产商*/
      sql.append("  ,bd_bom_b.cassmeasureid "); /*主单位*/
      sql.append("  ,bd_bom_b.nitemnum  ");/*主数量 */
      sql.append(" ,nvl(bd_defdoc.def1,0) def1  ");/*计划批数*/
      sql.append("  ,bd_bom_b.nitemnum*nvl(bd_defdoc.def1,0) num  ");/*计划料*/
      sql.append(" from bd_bom_b ");
      sql.append(" left join bd_bom on bd_bom.cbomid = bd_bom_b.cbomid ");
      sql.append(" left join bd_defdoc on bd_defdoc.def2 = bd_bom.cbomid and bd_defdoc.pk_defdoclist in (select pk_defdoclist from bd_defdoclist where code ='MM001') "); 
      sql.append(" where nvl(bd_bom_b.dr,0)=0 ");
      sql.append("  and nvl(bd_bom.dr,0) =0 ");
      sql.append(" and bd_bom.hfversiontype =1 ");
      sql.append("  and   nvl(bd_defdoc.def1,0) <> 0 ");
      sql.append("  )  group by cmaterialvid,cproductorid,cassmeasureid ");
      Map<String,Map<String,Object>> executeQuery = null;
      try {
    	  executeQuery = (Map<String,Map<String, Object>>) getDao().executeQuery(sql.toString(),new  ResultSetProcessor(){

			@Override
			public Object handleResultSet(ResultSet paramResultSet)
					throws SQLException {
				Map<String,Map<String,Object>> map = new HashMap<String,Map<String,Object>>();
				while(paramResultSet.next()){

					String cmaterialvid = paramResultSet.getString("cmaterialvid");
					String cassmeasureid =  paramResultSet.getString("cassmeasureid");
					String string = cmaterialvid+"~"+cassmeasureid;

					map.put(string, ProcessorUtils.toMap(paramResultSet));
				
				}
				return map;
			}
    		  
    	  });
	} catch (DAOException e) {
		// TODO 自动生成的 catch 块
		e.printStackTrace();
	}
	return  executeQuery;
		
	}
	
	/**
	 * 查询当前现在
	 * 此处已过滤
	 * @return  map<物料，[Map<field,object>]>  处理成三维数组，为后续解除循环取数的麻烦
	 */
	@SuppressWarnings({ "serial", "unchecked" })
	private Map<String,List<Map<String, Object>>> getOnHand() {  
		StringBuffer sql = new StringBuffer();  /*物料当前存量，全部*/
		sql.append(" select  ic_onhanddim.pk_org,ic_onhanddim.pk_group, ic_flow. cproductorid , ic_onhanddim.cmaterialvid");
		sql.append("  ,bd_material.code material_code  ");
		sql.append("   ,bd_material.name material_name ");
		sql.append("  ,scm_batchcode.pk_batchcode  "); 
		sql.append("  ,scm_batchcode.vbatchcode ");
		sql.append("  ,scm_batchcode.dproducedate ");
		sql.append("  ,sum(ic_onhandnum.nonhandnum)  nonhandnum ");
		sql.append(" ,ic_onhanddim.cwarehouseid ");
		sql.append(" ,ic_onhanddim.cstateid ");
		sql.append(" ,bd_material.pk_measdoc  ");
		sql.append("  ,scm_batchcode.dvalidate  ");
		sql.append(" from ic_onhandnum  ");
		sql.append(" left join ic_onhanddim on ic_onhanddim.PK_ONHANDDIM = ic_onhandnum.PK_ONHANDDIM and nvl(ic_onhanddim.dr,0)=0  ");
		sql.append(" LEFT OUTER JOIN scm_batchcode   ON ( ic_onhanddim.pk_batchcode = scm_batchcode.pk_batchcode ) ");
		sql.append("  INNER JOIN bd_stordoc bd_stordoc   ON ( ic_onhanddim.cwarehouseid = bd_stordoc.pk_stordoc ) ");
		sql.append("  inner join bd_material on bd_material.pk_material = ic_onhanddim.CMATERIALVID  ");
		sql.append("  inner join (select code ,name ,PK_MARBASCLASS from bd_marbasclass start with code in( '05','07','08') ");
		sql.append("      connect by prior bd_marbasclass.pk_marbasclass = bd_marbasclass.pk_parent ) bd_marbasclass  on  bd_material.pk_marbasclass= bd_marbasclass.pk_marbasclass ");
		sql.append(" left join ic_flow on ic_flow.vhashcode =  ic_onhanddim.vsubhashcode   and nvl(ic_flow.dr,0)=0     ");
		sql.append(" where nvl(ic_onhandnum.dr,0) =0  ");
		sql.append("  AND bd_stordoc.gubflag   = 'N' ");
		sql.append("  and ic_onhandnum.nonhandnum <> 0 ");
		sql.append(" group by ic_onhanddim.CMATERIALVID,bd_material.code,bd_material.name,scm_batchcode.pk_batchcode ,scm_batchcode.vbatchcode,bd_material.pk_measdoc  ,scm_batchcode.dvalidate , ic_flow.cproductorid ,ic_onhanddim.cwarehouseid  ,ic_onhanddim.cstateid  ,scm_batchcode.dproducedate,ic_onhanddim.pk_org,ic_onhanddim.pk_group  "); 
		sql.append("  ORDER by bd_material.code,scm_batchcode.vbatchcode ");
		
		Map<String,List<Map<String,Object>>> executeQuery = null;
		try {
//			executeQuery = (List<Map<String, Object>>) getDao().executeQuery(sql.toString(), new  MapListProcessor());
			executeQuery = (Map<String, List<Map<String, Object>>>) getDao().executeQuery(sql.toString(), 
					new  ResultSetProcessor(){

				/**
						 * 
						 */
				private static final long serialVersionUID = 2961492467919327694L;

				@Override
				public Object handleResultSet(ResultSet paramResultSet)
						throws SQLException {
					Map<String,List<Map<String,Object>>> map = new HashMap<String,List<Map<String,Object>>>();
					while(paramResultSet.next()){
						String cmaterialvid = paramResultSet.getString("cmaterialvid");
						String pk_measdoc =  paramResultSet.getString("pk_measdoc");
						String string = cmaterialvid+"~"+pk_measdoc;
						
						List<Map<String,Object>> materialList = map.get(string);
						if (null == materialList){
							materialList= new ArrayList<Map<String,Object>>();
						}
						materialList.add(ProcessorUtils.toMap(paramResultSet));
						map.put(string, materialList);
					}
					
					return map; 
				}
				
			});
		} catch (DAOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		return executeQuery;
	}
	

}
