package nc.bs.pu.m21;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.iufo.util.SqlUtil;
import nc.cmp.bill.util.SQLUtil;
import nc.jdbc.framework.processor.ResultSetProcessor;
import nc.pubitf.pu.m21.pub.PrayBillQueryMaterialSever;
import nc.util.fi.pub.SqlUtils;
import nc.vo.pub.BusinessException;

public class PrayBillQueryMaterialSeverImp implements PrayBillQueryMaterialSever{

	
// public Map<String,String>	mateiralquery(String cont){
//	 BaseDAO dao = new BaseDAO();
//	 String sql ="";
//	 dao.executeQuery(sql, processor)
//	return null;
//	 
// }

@Override
public Map<String, String> materialQuery(String[] codes) throws Exception {
	// TODO Auto-generated method stub
	BaseDAO dao = new BaseDAO();
	 String sql ="select code,pk_material from bd_material where  "+ SqlUtils.getInStr("code", codes);
	
	 Map<String, String> refDataMapInfo = (HashMap<String, String>)dao.executeQuery(sql, new ResultSetProcessor(){
			@Override
			public Object handleResultSet(ResultSet paramResultSet)
					throws SQLException {
//				 TODO Auto-generated method stub
				Map<String, String> map = new HashMap<String,String>();
				
				while (paramResultSet.next()){
					
					map.put(paramResultSet.getString("code"), paramResultSet.getString("pk_material"));
				}
				return map;
			}
	 });
	 
	return refDataMapInfo;
}

@Override
public Map<String, String> unitQuery(Map codesMap) throws Exception {
	// TODO Auto-generated method stub
	BaseDAO dao = new BaseDAO();
	
	String whereSql="where code in (";
	
//	Iterator<String> iterator=codesMap.keySet().iterator();
	
	
//	while (codesMap.keySet().iterator().hasNext()){
//      String key=iterator.next();
//      whereSql+="("+codesMap.get(key)+", ";
//	}
		
		
	Iterator it = codesMap.entrySet().iterator();
    while (it.hasNext()) {
        Map.Entry entry = (Map.Entry) it.next();
        String key = (String) entry.getKey();
        String value = (String) entry.getValue();
        System.out.println("key:" + key + "---" + "value:" + value);
        whereSql+="'"+codesMap.get(key)+"',";
    }
	  
	  if(!"where code in (".equals(whereSql)){
		  whereSql=whereSql.substring(0, whereSql.length()-1);
		  whereSql+=")";
	  }
	  else{
		  whereSql="";
	  }
	  
	String sql ="select code,pk_measdoc from bd_measdoc "+whereSql;
	
	 Map<String, String> refDataMapInfo = (HashMap<String, String>)dao.executeQuery(sql, new ResultSetProcessor(){
			@Override
			public Object handleResultSet(ResultSet paramResultSet)
					throws SQLException {
//				 TODO Auto-generated method stub
				Map<String, String> map = new HashMap<String,String>();
				
				while (paramResultSet.next()){
					map.put(paramResultSet.getString("code"), paramResultSet.getString("pk_measdoc"));
				}
				return map;
			}
	 });
	 
	return refDataMapInfo;
}

//@Override
//public Map<String, String> unitQuery(String[] codes) throws Exception {
//	// TODO Auto-generated method stub
//		BaseDAO dao = new BaseDAO();
//		
//		String sql ="select code,pk_measdoc from bd_measdoc "+SqlUtils.getInStr("code", codes);
//		
//		 Map<String, String> refDataMapInfo = (HashMap<String, String>)dao.executeQuery(sql, new ResultSetProcessor(){
//				@Override
//				public Object handleResultSet(ResultSet paramResultSet)
//						throws SQLException {
////					 TODO Auto-generated method stub
//					Map<String, String> map = new HashMap<String,String>();
//					
//					while (paramResultSet.next()){
//						map.put(paramResultSet.getString("code"), paramResultSet.getString("pk_measdoc"));
//					}
//					return map;
//				}
//		 });
//		 
//		return refDataMapInfo;
//}

@Override
public String orgQuery(String pkOrg) throws Exception {
	// TODO Auto-generated method stub
		BaseDAO dao = new BaseDAO();
		
		String sql ="select pk_org,pk_vid from org_orgs_v where pk_org ='"+pkOrg+"';";
		
		 String orgCode = (String)dao.executeQuery(sql, new ResultSetProcessor(){
				@Override
				public Object handleResultSet(ResultSet paramResultSet)
						throws SQLException {
//					 TODO Auto-generated method stub
					String orgCode="";
					
					while (paramResultSet.next()){
						orgCode=paramResultSet.getString("pk_vid");
					}
					return orgCode;
				}
		 });
		 
		return orgCode;
}

}
