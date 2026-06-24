/*     */ package nc.vo.ewm.vochange;
/*     */ 
/*     */ import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nc.bs.dao.DAOException;
import nc.bs.framework.common.NCLocator;
import nc.bs.trade.business.HYPubBO;
import nc.impl.am.db.DBAccessUtil;
import nc.impl.pubapp.pattern.data.vo.VOQuery;
import nc.itf.corg.ICostRegionQryService;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.md.persist.framework.IMDPersistenceQueryService;
import nc.pubitf.uapbd.IMaterialPubService;
import nc.util.mmf.framework.db.SqlInUtil;
import nc.vo.am.common.util.ArrayConstructor;
import nc.vo.am.common.util.BillTypeUtils;
import nc.vo.am.proxy.AMProxy;
import nc.vo.bd.material.MaterialVO;
import nc.vo.corg.CostRegionStockStoreVO;
import nc.vo.corg.CostRegionVO;
import nc.vo.ewm.workorder.AggWorkOrderVO;
import nc.vo.ewm.workorder.WOActualInvVO;
import nc.vo.ewm.workorder.WorkOrderHeadVO;
import nc.vo.pf.change.ChangeVOAdjustContext;
import nc.vo.pf.change.IChangeVOAdjust;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.trade.voutils.SafeCompute;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ChgAfter4DTo4B36
/*     */   implements IChangeVOAdjust
/*     */ {
/*     */   public AggregatedValueObject adjustBeforeChange(AggregatedValueObject srcVO, ChangeVOAdjustContext adjustContext)
/*     */     throws BusinessException
/*     */   {
/*  33 */     return null;
/*     */   }
/*     */   
/*     */   public AggregatedValueObject adjustAfterChange(AggregatedValueObject srcVO, AggregatedValueObject destVO, ChangeVOAdjustContext adjustContext)
/*     */     throws BusinessException
/*     */   {
/*  39 */     return null;
/*     */   }
/*     */   
/*     */   public AggregatedValueObject[] batchAdjustBeforeChange(AggregatedValueObject[] srcVOs, ChangeVOAdjustContext adjustContext)
/*     */     throws BusinessException
/*     */   {
/*  45 */     return null;
/*     */   }
/*     */   
/*     */   public AggregatedValueObject[] batchAdjustAfterChange(AggregatedValueObject[] srcVOs, AggregatedValueObject[] destVOs, ChangeVOAdjustContext adjustContext)
/*     */     throws BusinessException
/*     */   {
/*  51 */     if ((destVOs == null) || (destVOs.length == 0) || (srcVOs == null) || (srcVOs.length == 0)) {
/*  52 */       return null;
/*     */     }
/*     */     
/*  55 */     Map<String, String> idMap = get4DBId2WOHIdMap(srcVOs);
/*  56 */     Set<String> linkedSrcBIdSet = getLinkedSrcBIdSet(srcVOs);
/*     */     
/*  58 */     for (AggregatedValueObject destVO : destVOs) {
/*  59 */       AggWorkOrderVO workOrderBillVO = (AggWorkOrderVO)destVO;
				WorkOrderHeadVO head = workOrderBillVO.getParentVO();
/*  60 */       WOActualInvVO[] invVOs = (WOActualInvVO[])ArrayConstructor.getArray(workOrderBillVO.getChildren(WOActualInvVO.class));

				writeBackActualInvCost(invVOs);
/*  61 */       for (WOActualInvVO invVO : invVOs) {
/*  62 */         String srcBId = invVO.getSrc_pk_bill_b();
/*     */         
/*  64 */         String woHid = (String)idMap.get(srcBId);
/*  65 */         if (woHid != null) {
/*  66 */           invVO.setPk_wo(woHid);
/*  67 */           workOrderBillVO.getParentVO().setPrimaryKey(woHid);
/*     */         }
/*     */         
/*  70 */         if (linkedSrcBIdSet.contains(srcBId)) {
/*  71 */           invVO.setLink_flag(UFBoolean.TRUE);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*  77 */     return destVOs;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private Map<String, String> get4DBId2WOHIdMap(AggregatedValueObject[] srcVOs)
/*     */   {
/*  87 */     Map<String, String> idMap = new HashMap();
/*  88 */     for (AggregatedValueObject billVO : srcVOs) {
/*  89 */       CircularlyAccessibleValueObject[] bodyVOs = billVO.getChildrenVO();
/*  90 */       for (CircularlyAccessibleValueObject body : bodyVOs) {
/*  91 */         String srcBId = ((SuperVO)body).getPrimaryKey();
/*  92 */         String woHId = null;
/*  93 */         String srcType = (String)body.getAttributeValue("csourcetype");
/*     */         
/*  95 */         boolean isFromWorkOrder = BillTypeUtils.isEqual("4B36", srcType);
/*  96 */         if (isFromWorkOrder) {
/*  97 */           woHId = (String)body.getAttributeValue("csourcebillhid");
/*     */         } else {
/*  99 */           woHId = (String)body.getAttributeValue("cworkorderhid");
/*     */         }
/* 101 */         if (woHId != null) {
/* 102 */           idMap.put(srcBId, woHId);
/*     */         }
/*     */       }
/*     */     }
/* 106 */     return idMap;
/*     */   }
/*     */   
/*     */   private Set<String> getLinkedSrcBIdSet(AggregatedValueObject[] srcVOs) {
/* 110 */     Set<String> linkedSrcBIdSet = new HashSet();
/* 111 */     for (AggregatedValueObject billVO : srcVOs) {
/* 112 */       CircularlyAccessibleValueObject[] bodyVOs = billVO.getChildrenVO();
/* 113 */       for (CircularlyAccessibleValueObject body : bodyVOs) {
/* 114 */         String srcBId = ((SuperVO)body).getPrimaryKey();
/* 115 */         String woHId = null;
/* 116 */         String srcType = (String)body.getAttributeValue("csourcetype");
/*     */         
/* 118 */         boolean isFromWorkOrder = BillTypeUtils.isEqual("4B36", srcType);
/* 119 */         if (!isFromWorkOrder) {
/* 120 */           woHId = (String)body.getAttributeValue("cworkorderhid");
/*     */         }
/* 122 */         if ((!isFromWorkOrder) && (woHId != null)) {
/* 123 */           linkedSrcBIdSet.add(srcBId);
/*     */         }
/*     */       }
/*     */     }
/* 127 */     return linkedSrcBIdSet;
/*     */   }
// add by 2021-05-26 更新實際成本和服務成本
private void writeBackActualInvCost(WOActualInvVO[] retActualVOs) throws BusinessException {
	// 回写物料的服务费用和材料费用 UpRewriteWorkOrder
	// ??
	// 先区分存货是服务还是材料，根据存货分类的编码09默认服务，其他为材料
	List<String> list = new ArrayList<>();
	for (WOActualInvVO vo : retActualVOs) {
		list.add(vo.getPk_material());
	}
	IMaterialPubService materialService = AMProxy
			.lookup(IMaterialPubService.class);
	String[] fields = new String[] { MaterialVO.PK_MATERIAL,MaterialVO.PK_MARBASCLASS,
			MaterialVO.PK_SOURCE, MaterialVO.CODE };
	Map<String, MaterialVO> voMap = materialService
			.queryMaterialBaseInfoByPks(
					list.toArray(new String[list.size()]), fields);
	// 服务类存货取材料出库单的单价
	// 材料类型的存货取最新成本价
	HYPubBO bo = new HYPubBO();
	for (WOActualInvVO vo : retActualVOs) {
		MaterialVO mvo = voMap.get(vo.getPk_material());
		
		if (mvo == null)
			throw new BusinessException("物料信息出错，请检查！");
		String str = (String) bo.findColValue(
				"bd_marbasclass",
				"code ",
				" nvl(dr,0) = 0 and pk_marbasclass = '"
						+ mvo.getPk_marbasclass() + "'");
		UFDouble price = UFDouble.ZERO_DBL;
		if (str.startsWith("09")) {// 服務類
			price = getMaterialPrice(vo.getPk_material(),vo.getCother_bill_bid());
			if(vo.getPrice() != null){
				price = vo.getPrice();
			}
		} else {
		    price = getCostPrice(vo.getPk_material(),
		    		vo.getPk_stockorg(),vo.getPk_group(),vo.getPk_stordoc());//多传一个仓库参数
//			price = UFDouble.ZERO_DBL;
			if(UFDouble.ZERO_DBL.equals(price) || price == null){
			 price = (UFDouble) bo.findColValue(
						"bd_materialfi",
						"planprice  ",
						" nvl(dr,0) = 0 and pk_material  = '"
								+ vo.getPk_material() + "'");
			 
			}
			if(UFDouble.ZERO_DBL.equals(price) || price == null){
				price = vo.getPrice();
			}
		}
		UFDouble mny = SafeCompute.multiply(price, vo.getNnum());
		vo.setPrice(price);
		vo.setMoney(mny);
		vo.setPrice_org(price);
		vo.setMoney_org(mny);
	}
}

private UFDouble getCostPrice(String pk_material,String pk_org,String pk_group) throws BusinessException{
//	String sql = " select b.nabprice from  ia_monthnab b where b.cinventoryid ='"+pk_material+"' and b.pk_org ='"+pk_org+"' and nvl(b.dr,0) = 0 order by ts desc;";
//	List<UFDouble> list =(List<UFDouble>) new DBAccessUtil().executeQuery(sql, new ColumnListProcessor());
//	UFDouble price  = UFDouble.ZERO_DBL;
//	if(list != null && list.size()>0){
//		price = list.get(0);
//	}
	   String[] cmaterialoids = new String[]{pk_material};
	   Map<String,String> qryParam = new HashMap<String,String>();
	   qryParam.put("pk_org", pk_org);
	   qryParam.put("pk_group", pk_group);
	List<Map> list =  dealMonthnab(qryParam,cmaterialoids);
	if(list.isEmpty() || list.size() ==0){
		return UFDouble.ZERO_DBL;
	}else{
		UFDouble  price = UFDouble.ZERO_DBL;
		Object  o  = list.get(0).get("nprice");
		if(o instanceof BigDecimal){
			price = new UFDouble(((BigDecimal)o));
		}else{
			price = (UFDouble)o;
		}
		return price;
	}
}

private UFDouble getCostPrice(String pk_material,String pk_org,String pk_group,String pk_stordoc) throws BusinessException{
//	String sql = " select b.nabprice from  ia_monthnab b where b.cinventoryid ='"+pk_material+"' and b.pk_org ='"+pk_org+"' and nvl(b.dr,0) = 0 order by ts desc;";
//	List<UFDouble> list =(List<UFDouble>) new DBAccessUtil().executeQuery(sql, new ColumnListProcessor());
//	UFDouble price  = UFDouble.ZERO_DBL;
//	if(list != null && list.size()>0){
//		price = list.get(0);
//	}
	   String[] cmaterialoids = new String[]{pk_material};
	   Map<String,String> qryParam = new HashMap<String,String>();
	   qryParam.put("pk_org", pk_org);
	   qryParam.put("pk_group", pk_group);
	   qryParam.put("pk_stordoc", pk_stordoc);
	List<Map> list =  dealMonthnab(qryParam,cmaterialoids);
	if(list.isEmpty() || list.size() ==0){
		return UFDouble.ZERO_DBL;
	}else{
		UFDouble  price = UFDouble.ZERO_DBL;
		Object  o  = list.get(0).get("nprice");
		if(o instanceof BigDecimal){
			price = new UFDouble(((BigDecimal)o));
		}else{
			price = (UFDouble)o;
		}
		return price;
	}
}


/**
  * 根据指定物料(可以为空，为空则查询所有的物料)
  * 查询最新月结存信息
  * 查询最新结存月的信息
  * @param qryParam
  * @param cmaterialoids
  * @return
  * @throws BusinessException
  */
 public List<Map> dealMonthnab(Map qryParam,
   String[] cmaterialoids) throws BusinessException {

  StringBuffer sql = new StringBuffer();
  sql.append("select caccountperiod as dbilldate, cinventoryid as  cmaterialoid,");
  sql.append(" nabnum, nabprice as nprice , nabmny");
  sql.append(" from  ia_monthnab h ");
  sql.append(" where nvl(h.dr, 0) = 0");
  // 最大月份的会计期间
  sql.append(" and caccountperiod =( ");
  sql.append(" select max(caccountperiod)  from ia_monthnab");
  sql.append(" where nvl(dr, 0) = 0 ");
  sql.append(" and pk_group = '" + qryParam.get("pk_group") + "'");
  // 库存组织---成本域
  sql.append(" and pk_org = " +"'" + getCostOrg(qryParam.get("pk_org").toString(), qryParam.get("pk_org").toString(),qryParam.get("pk_stordoc").toString()) + "'");//修改
  
  //sql.append(" and pk_stordoc = '" + qryParam.get("pk_stordoc").toString() + "'");
  
  // 本月之前的期间--当前业务场景应该不需要，如果需要，请注意会计月的转换
  sql.append(" )");
  // 过滤物料
  if(cmaterialoids!=null && cmaterialoids.length >0){
   sql.append(" and cinventoryid " + getMaterialsIn(cmaterialoids));
  }
  sql.append(" and h.pk_group = '" +  qryParam.get("pk_group") + "'");
  // 成本域
  sql.append(" and pk_org = " +"'" + getCostOrg(qryParam.get("pk_org").toString(), qryParam.get("pk_org").toString() , qryParam.get("pk_stordoc").toString()) + "'");//修改
  
  sql.append(" and abs(nabprice)>0");

  List<Map> list = (List<Map>) new DBAccessUtil().executeQuery(
    sql.toString(), new MapListProcessor());
  return list;
 }


/**
* 根据库存组织查询成本域
* 
* @param pk_stockorg
* @return
 * @throws BusinessException 
*/
String getCostOrg(String pk_org,String pk_stockorg,String pk_stordoc) throws BusinessException {
	ICostRegionQryService quer = NCLocator.getInstance().lookup(ICostRegionQryService.class);
	CostRegionVO[] query = quer.queryCostRegionVOsByClause( " nvl(dr,0)=0 and pk_org='"+pk_stockorg+"' and layertype = 1 " +
			" and pk_costregion in (select pk_costregion from org_cr_stockorg where pk_stockorg = '" + pk_stordoc +"' and nvl(dr,0)=0)");
	CostRegionVO[] query2 = quer.queryCostRegionVOsByClause(" nvl(dr,0)=0 and pk_org='"+pk_stockorg+"' and layertype = 2  " +
			" and pk_costregion in (select pk_costregion from org_cr_stockstore where pk_storage = '" + pk_stordoc + "' and nvl(dr,0)=0)");
	List<String> keys  = new ArrayList<String>();
	//成本域对应层级为 库存组织+仓库
	 if (query2 !=null){
		 if(  query2.length > 1){
			 throw new BusinessException("检查出仓库参与多个成本域设置，无法进行计划发料！ ");
		 }
			 return  query2[0].getPk_costregion();		 
	 }
	 else if (null != query ){//成本域对应层级为 库存组织,且有多个库存组织
		 if(query.length > 1){	
			 throw new BusinessException("检查出成本域对应多个库存组织，无法进行计划发料！ ");
		 }
		 return  query[0].getPk_costregion();	
		 
	 }
	String sql = " ( select  pk_costregion  from org_costregion where nvl(dr,0)=0  "
	+ " and pk_org='" + pk_stockorg + "')";
	return sql;
}

String getMaterialsIn(String[] cmaterialoids) throws BusinessException {

SqlInUtil util = new SqlInUtil(cmaterialoids);
return util.getInSql();
}

private UFDouble getMaterialPrice(String pk_material,String csourcebid) throws DAOException{
	String sql = " select pb.nprice from ic_material_b b join ic_purchasein_b pb on b.csourcebillbid = pb.cgeneralbid   where pb.cgeneralbid = '"+csourcebid+"' and nvl(b.dr, 0 ) = 0 " ;
	BigDecimal price = (BigDecimal) new DBAccessUtil().executeQuery(sql, new ColumnProcessor());
	UFDouble uprice = UFDouble.ZERO_DBL;	
	if(price != null){
		 uprice = new UFDouble(price.doubleValue());
	}
	return uprice;
}
/*     */ }

