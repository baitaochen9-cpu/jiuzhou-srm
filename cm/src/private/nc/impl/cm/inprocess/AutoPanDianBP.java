package nc.impl.cm.inprocess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.itf.cm.inprocess.IInprocessMaintainService;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.md.persist.framework.IMDPersistenceQueryService;
import nc.vo.cm.inprocess.entity.InprocessAggVO;
import nc.vo.cm.inprocess.entity.InprocessHeadVO;
import nc.vo.cm.inprocess.entity.InprocessItemVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFDate;

public class AutoPanDianBP {
	public InprocessAggVO[] backFlushProduct(InprocessAggVO bill) throws Exception{
		InprocessHeadVO headVO = (InprocessHeadVO)bill.getParent();
		String pk_org=headVO.getPk_org();
		String ccostcenterid = headVO.getCcostcenterid();
		String  period=headVO.getCperiod();
		UFDate begindate =  new UFDate(period+"-01").asBegin();
		UFDate date = begindate.getDateAfter(begindate.getDaysMonth()-1);
		UFDate endate =date.asEnd();
		IUAPQueryBS  bs=(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		StringBuffer sql = new StringBuffer();
		sql.append(" select a.pk_group,a.pk_org,");
		sql.append(" e.pk_costcenter as vbdef1,"); //成本中心
		sql.append(" f.ccostobjectid as  ccostobjectid,"); //产成品的成本对象
		sql.append("  f.vcostobjcode,  f.vcostobjname,");
		sql.append(" a.cunitid as cmeasdocid,");//产成本主单位
		sql.append(" sum(nmmnum *round(eq.nequivrate/100,2)) as  ninpronum ");// 计划产出主数量*约当系数
		sql.append(" from mm_mo  a"); //流程生产订单明细
		sql.append(" inner join mm_pmo on a.cpmohid =mm_pmo.cpmohid ");
		sql.append(" inner join resa_ccdepts  e on a.cdeptid = e.pk_dept and  e.pk_org = a.pk_org "); //成本中心和部门对照表
		sql.append(" JOIN resa_costcenter r ON e.pk_costcenter = r.pk_costcenter");

//		sql.append(" inner join cm_costobject f on a.cmaterialvid  =f.cmaterialid "); //产成品的成本对象
//		0=归集，1=最终， 
		sql.append(" inner join cm_costobject f on a.cmaterialvid  =f.cmaterialid  and f.enablestate =2 and f.itype=1 and nvl(a.cprojectid,'~')= nvl(f.cprojectid,'~') "); //产成品的成本对象
	
		/**---------1.约当系数------------*/
		sql.append(" left join ");
		sql.append("( ");
		sql.append(" select cmh.pk_group,cmh.pk_org,ccostcenterid, cmh.ccostobjectid,cmh.cperiod,  ");
		sql.append("  avg(nequivrate) as nequivrate ");
		sql.append(" ");
		sql.append(" from cm_equivrate cmh ");
		sql.append(" inner join cm_equivrate_b  cmb on cmh.cequivrateid = cmb.cequivrateid ");
		sql.append(" where nvl(cmh.dr,0) = 0 ");
		sql.append(" and nvl(cmb.dr,0) = 0 ");
		sql.append(" and cmh.cperiod='"+period+"'");
		sql.append(" and cmh.pk_org    ='" + pk_org + "'");
		if(StringUtils.isNotBlank(ccostcenterid)){
			sql.append(" and cmh.ccostcenterid='"+ccostcenterid+"'");
		}
		sql.append(" ");
		sql.append(" group by  cmh.pk_group,cmh.pk_org,ccostcenterid, cmh.ccostobjectid,cmh.cperiod");
		sql.append(" ) eq  ");
		sql.append(" on  a.pk_org=eq.pk_org and e.pk_costcenter=eq.ccostcenterid and f.ccostobjectid=eq.ccostobjectid ");
		/**---------1.约当系数------------*/
		sql.append(" where "); 
		sql.append(" a.dr=0 and  e.dr=0 and f.dr=0 "); 
		sql.append(" and a.pk_org ='"+pk_org+"'"	);
		if(StringUtils.isNotBlank(ccostcenterid)){
			sql.append(" and e.pk_costcenter='"+ccostcenterid+"'");
		}
//		sql.append(" AND f.vcostobjcode IN ('803535')");
		//test
//		sql.append(" and d.fbillflag =3"); //1=删除，2=自由，3=签字，4=审核，5=审核中，6=审核不通过，7=已调差状态，  
		sql.append(" and a.fitemstatus in(1)");// fitemstatus  行状态  fitemstatus int  流程生产订单行状态   0=自由，4=审批，1=投放，2=完工，3=关闭，    
		sql.append(" AND (a.tactstarttime  >= '"+begindate+"' AND a.tactstarttime   <= '"+endate+"' )");
		//2025-04-30 liyf 需要剔除在成本中心
		sql.append(" and r.cccode not in (select code from bd_defdoc where dr=0 and  pk_defdoclist =(select  pk_defdoclist from bd_defdoclist where code='CM001_YF'))");//排除技术服务部—溶媒回收

		sql.append(" group by  a.pk_group,a.pk_org,e.pk_costcenter,f.ccostobjectid, f.vcostobjcode,  f.vcostobjname,a.cunitid"); 
		List<InprocessItemVO>  list=(List<InprocessItemVO>) bs.executeQuery(sql.toString(), new BeanListProcessor(InprocessItemVO.class));
	
//				根据成本中分组
	   HashMap<String, List<InprocessItemVO> > map =getVOHashByGroupFields(list.toArray( new InprocessItemVO[list.size()]),new String[]{"vbdef1"});
	   List<InprocessAggVO> agglist=new ArrayList<InprocessAggVO>();
	   
//	 缓存当前周期生成的物料 如果已经生成提示报错
	   Map<String,String>map2=getReadyCreat(pk_org,period);//  new HashMap<String,String>();
//	   记录以生成成本对象名称
//	   Set<String>set=new HashSet<String>();
	   String err="";
	   if(map!=null&&map.size()>0){
		   for(String key:map.keySet()){
			   InprocessAggVO aggvo=new InprocessAggVO();
			   InprocessHeadVO hvo =new InprocessHeadVO();  
			   hvo.setPk_org(pk_org);
			   hvo.setCperiod(period);
			   hvo.setDbusinessdate(date);		
			   hvo.setCcostcenterid(map.get(key).get(0).getVbdef1());
			   hvo.setPk_group(InvocationInfoProxy.getInstance().getGroupId()) ;
			   hvo.setVnote("自动盘点");
			   aggvo.setParentVO(hvo);
			   List<InprocessItemVO> itemlist=map.get(key);
			   List<InprocessItemVO> itemlist2=new ArrayList<InprocessItemVO>();
			   for(int i=0;i<itemlist.size();i++){
				   if(map2.containsKey( itemlist.get(i).getCcostobjectid())){
					   err=err+","+map2.get( itemlist.get(i).getCcostobjectid());
					   continue;
				   }
				  
				   itemlist.get(i).setVbdef1(null);
				   itemlist.get(i).setCperiod(period);
				   itemlist2.add(itemlist.get(i));
			   }
//				   表体都已生成
			   if(itemlist2.size()==0){
				   continue;
			   }
			   aggvo.setChildrenVO(itemlist2.toArray(new InprocessItemVO[itemlist2.size()]));
			   agglist.add(aggvo);
		   }
		  
	   }
	   if(err.length()>0){
		   throw new BusinessException("以下成本对象"+period+"期间,已生成在产品盘点："+err.replaceFirst(",",""));
	   }
	 if(agglist.size()>0){
		 IInprocessMaintainService ser=	NCLocator.getInstance().lookup(IInprocessMaintainService.class);
     	ser.insertInprocessForBackFlush(agglist.toArray(new InprocessAggVO[agglist.size()]));
     	
  	  String sqlwhere ="pk_org='"+pk_org+"' and dbusinessdate='"+date+"' and dr=0";
		IMDPersistenceQueryService qryService = NCLocator.getInstance().lookup(IMDPersistenceQueryService.class);
		Collection<InprocessAggVO> colVOs = qryService.queryBillOfVOByCond(InprocessAggVO.class,
				sqlwhere , true, false); 
		InprocessAggVO[] aggArap_invoice= colVOs.toArray(new InprocessAggVO[colVOs.size()]) ;
		
     	return aggArap_invoice;
	 }
				
	 return null;
	
	}
	
//  获取该公司该账期数据
private Map<String, String> getReadyCreat(String pk_org, String period) throws BusinessException {
		// TODO Auto-generated method stub
	String sql=
			"select b.ccostobjectid,c.vcostobjname\n" +
					"from cm_inprocess a,cm_inprocess_b b,cm_costobject c\n" + 
					"where a.cinprocessid=b.cinprocessid\n" + 
					"and b.ccostobjectid=c.ccostobjectid\n" + 
					"and a.dr=0\n" + 
					"and b.dr=0\n" + 
					"and a.cperiod ='"+period+"'\n" + 
					"and a.pk_org='"+pk_org+"'";
	IUAPQueryBS  iuap=(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
	List<Map> list=(List<Map>)iuap.executeQuery(sql, new MapListProcessor());
	Map<String, String> map=new HashMap<String, String>();
	if(list!=null&&list.size()>0){
		for(int i=0;i<list.size();i++){
			map.put(list.get(i).get("ccostobjectid").toString(), list.get(i).get("vcostobjname").toString());
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
