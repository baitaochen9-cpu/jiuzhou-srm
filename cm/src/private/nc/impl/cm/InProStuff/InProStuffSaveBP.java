package nc.impl.cm.InProStuff;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.itf.cm.inprostuff.IInproStuffMaintainService;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.md.persist.framework.IMDPersistenceQueryService;
import nc.vo.cm.inprostuff.entity.InproStuffAggVO;
import nc.vo.cm.inprostuff.entity.InproStuffHeadVO;
import nc.vo.cm.inprostuff.entity.InproStuffItemVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
/**
 * 材料自动盘点单
 * @author yunfeng.li
 *
 */
public class InProStuffSaveBP {
	

	public InproStuffAggVO[] create(String pk_org, String period) throws Exception{
		
	
		List<Map<String, Object>> detail = new InProMaterialDetailReportBP().qryDataDetail(pk_org, period, null);
		if(detail == null || detail.size() ==0){
//			return null;
			   throw new BusinessException("没有在产数据.");
		}
		//将报表数据转换成vo
		List<InproStuffItemVO>  list = new ArrayList<InproStuffItemVO>();
		for(int i=0;i<detail.size();i++){
			Map<String, Object> rs = detail.get(i);
			InproStuffItemVO vo = new InproStuffItemVO();
			list.add(vo);
			vo.setAttributeValue("pk_group", rs.get("pk_group"));
			vo.setAttributeValue("pk_org", rs.get("pk_org"));
			vo.setAttributeValue("pk_org_v", rs.get("pk_org_v"));
			vo.setAttributeValue("vbdef1", rs.get("pk_costcenter")); //成本中心
			vo.setAttributeValue("vbdef2", rs.get("ccostobjectid"));//产成品的成本对象
			vo.setAttributeValue("vbdef3", rs.get("cunitid"));//产成品单位
			vo.setAttributeValue("cmaterialid", rs.get("cmaterialoid"));//材料
			vo.setAttributeValue("cmaterialvid", rs.get("cmaterialvid"));
			vo.setAttributeValue("cmeasdocid", rs.get("cmeasdocid"));//材料主单位
			vo.setAttributeValue("nnum", rs.get("nnum"));// 材料-数量
			vo.setAttributeValue("cprojectid", rs.get("cprojectid"));// 材料-项目
			vo.setAttributeValue("nprice", rs.get("nprice"));// 单价

		}
		
		UFDate begindate =  new UFDate(period+"-01").asBegin();
		UFDate date = begindate.getDateAfter(begindate.getDaysMonth()-1);
		UFDate endate =date.asEnd();
		
	
//		根据成本中心—+成本对象分组
	   HashMap<String, List<InproStuffItemVO> > map =getVOHashByGroupFields(list.toArray( new InproStuffItemVO[list.size()]),new String[]{"vbdef1","vbdef2"});
	   List<InproStuffAggVO> agglist=new ArrayList<InproStuffAggVO>();
	   
//	 缓存当前周期生成的物料 如果已经生成提示报错
	   Map<String,String> map2 = getReadyCreat(pk_org,period);//  new HashMap<String,String>();
//	   记录以生成成本对象名称
//	   Set<String>set=new HashSet<String>();
	   String err="";
	   Map<String,UFDouble> mid_price = new HashMap<String,UFDouble>();
	   if(map!=null&&map.size()>0){
		   for(String key:map.keySet()){
			   InproStuffAggVO aggvo=new InproStuffAggVO();
			   InproStuffHeadVO hvo =new InproStuffHeadVO();  
			   hvo.setPk_org(pk_org);
			   hvo.setCperiod(period);
			   hvo.setDbusinessdate(date);		
			   hvo.setCcostcenterid(map.get(key).get(0).getVbdef1());
			   hvo.setCcostobjectid(map.get(key).get(0).getVbdef2());
			   hvo.setCmeasdocid(map.get(key).get(0).getVbdef3());
			   //按照成本中心+成本对象校验是否已经存在
			   String checkKkey =map.get(key).get(0).getVbdef1()+map.get(key).get(0).getVbdef2();
			   if(map2.containsKey(checkKkey)){
				   err=err+","+map2.get(checkKkey);
				   continue;
			   }
//			   hvo.setCcosttypeid(newCcosttypeid);
			   hvo.setPk_group(InvocationInfoProxy.getInstance().getGroupId()) ;
			   hvo.setVnote("自动材料计算");
			   aggvo.setParentVO(hvo);
			   List<InproStuffItemVO> itemlist=map.get(key);
			   List<InproStuffItemVO> itemlist2=new ArrayList<InproStuffItemVO>();
			   for(int i=0;i<itemlist.size();i++){
				  
				   itemlist.get(i).setVbdef1(null);
				   itemlist.get(i).setVbdef2(null);
				   itemlist.get(i).setVbdef3(null);
				   UFDouble nprice = itemlist.get(i).getNprice();
				   if(nprice == null){
					   nprice = UFDouble.ZERO_DBL;
				   }
		
				   itemlist.get(i).setNprice(nprice);
				   itemlist.get(i).setNmoney(nprice.multiply( itemlist.get(i).getNnum()));
				   itemlist.get(i).setCperiod(period);
				   itemlist2.add(itemlist.get(i));
			   }
//				   表体都已生成
			   if(itemlist2.size()==0){
				   continue;
			   }
			   aggvo.setChildrenVO(itemlist2.toArray(new InproStuffItemVO[itemlist2.size()]));
			   agglist.add(aggvo);
		   }
		  
	   }
	   if(err.length()>0){
		   throw new BusinessException("以下成本对象"+period+"期间,已生成在在产材料盘点单："+err.replaceFirst(",",""));
	   }
	 if(agglist.size()>0){
		 IInproStuffMaintainService ser=	NCLocator.getInstance().lookup(IInproStuffMaintainService.class);
     	ser.insertInProStuffForBackFlush(agglist.toArray(new InproStuffAggVO[agglist.size()]));
     	
  	  String sqlwhere ="pk_org='"+pk_org+"' and dbusinessdate='"+date+"' and dr=0";
		IMDPersistenceQueryService qryService = NCLocator.getInstance().lookup(IMDPersistenceQueryService.class);
		Collection<InproStuffAggVO> colVOs = qryService.queryBillOfVOByCond(InproStuffAggVO.class,
				sqlwhere , true, false); 
		InproStuffAggVO[] aggArap_invoice= colVOs.toArray(new InproStuffAggVO[colVOs.size()]) ;
		
     	return aggArap_invoice;
	 }
				
	 return null;
	
	}
	
//  获取该公司该账期数据
private Map<String, String> getReadyCreat(String pk_org, String period) throws BusinessException {
		// TODO Auto-generated method stub
	String sql=
			"select  a.ccostcenterid, a.ccostobjectid,c.vcostobjcode " +
					" from cm_inprostuff a,cm_costobject c" + 
					" where  " + 
					"  a.ccostobjectid=c.ccostobjectid " + 
					" and a.dr=0" + 
					" and a.cperiod ='"+period+"'" + 
					" and a.pk_org='"+pk_org+"'";
	IUAPQueryBS  iuap=(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
	List<Map> list=(List<Map>)iuap.executeQuery(sql, new MapListProcessor());
	Map<String, String> map=new HashMap<String, String>();
	if(list!=null&&list.size()>0){
		for(int i=0;i<list.size();i++){
			map.put(list.get(i).get("ccostcenterid").toString()+list.get(i).get("ccostobjectid").toString(), list.get(i).get("vcostobjcode").toString());
		}
		
	}
		return map;
	}




	public  HashMap getVOHashByGroupFields(CircularlyAccessibleValueObject[] vos, String[] groupFields)
			throws Exception {

		HashMap<String,List<CircularlyAccessibleValueObject>> hash = new HashMap<String,List<CircularlyAccessibleValueObject>>();

		for (int i = 0; vos != null && i < vos.length; i++) {
			String key = getGroupKeyByVO(vos[i], groupFields);
			if (!hash.containsKey(key)) {
				List<CircularlyAccessibleValueObject> list=new ArrayList<CircularlyAccessibleValueObject>() ;
				list.add(vos[i]);
				hash.put(key,list );
			} else {
				 hash.get(key).add(vos[i]);
			}
		}
		return hash;
	}
	
	
	public static String getGroupKeyByVO(CircularlyAccessibleValueObject vo, String[] groupFields) {
		String key = "";
		for (int i = 0; groupFields != null && i < groupFields.length; i++) {
			key += (String) (vo.getAttributeValue(groupFields[i])==null?"":vo.getAttributeValue(groupFields[i]));
		}
		return key;
	}



}
