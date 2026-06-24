 package nc.impl.ewm.workorder.service;
 
 import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.pf.pub.PfDataCache;
import nc.bs.trade.business.HYPubBO;
import nc.impl.am.common.InSqlManager;
import nc.impl.am.common.PfUtilToolsAM;
import nc.impl.am.db.DBAccessUtil;
import nc.impl.am.db.QueryUtil;
import nc.impl.am.db.VOPersistUtil;
import nc.impl.emm.repairplan.RepairPlanCostCheck;
import nc.impl.ewm.workorder.WorkOrderCostImpl;
import nc.itf.ewm.prv.IWorkOrderMakeBill;
import nc.itf.ewm.pub.IWorkOrderForICService;
import nc.jdbc.framework.SQLParameter;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.pubitf.ic.m4d.ewm.IMaterialOutServiceForEWM;
import nc.pubitf.invp.plan.IReqResultForInvp;
import nc.pubitf.invp.plan.ReqResultForInvpVO;
import nc.pubitf.org.IStockOrgPubService;
import nc.pubitf.uapbd.IMaterialPubService;
import nc.util.mmf.framework.db.SqlInUtil;
import nc.vo.am.common.BillRowNoVO;
import nc.vo.am.common.util.ArrayConstructor;
import nc.vo.am.common.util.ArrayUtils;
import nc.vo.am.common.util.BaseVOUtils;
import nc.vo.am.common.util.BillTypeUtils;
import nc.vo.am.common.util.ExceptionUtils;
import nc.vo.am.common.util.SqlBuilder;
import nc.vo.am.common.util.UFDoubleUtils;
import nc.vo.am.manager.CurrencyManager;
import nc.vo.am.manager.LockManager;
import nc.vo.am.proxy.AMProxy;
import nc.vo.am.pub.uap.ModuleInfoQuery;
import nc.vo.bd.material.MaterialVO;
import nc.vo.ewm.workorder.AggWorkOrderVO;
import nc.vo.ewm.workorder.WOActualInvVO;
import nc.vo.ewm.workorder.WOPlanInVVO;
import nc.vo.ewm.workorder.WorkOrderHeadVO;
import nc.vo.ewm.workorder.param.WorkOrderParamUtils;
import nc.vo.ewm.workorder.utils.ActualInvFrom4DProcessUtil;
import nc.vo.ic.general.define.ICBillBodyVO;
import nc.vo.ic.general.define.ICBillVO;
import nc.vo.ic.m4d.entity.MaterialOutBodyVO;
import nc.vo.ic.m4d.entity.MaterialOutVO;
import nc.vo.ic.pub.util.VOEntityUtil;
import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.pf.change.ChangeVOAdjustContext;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.ISuperVO;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.pattern.model.entity.bill.AbstractBill;
import nc.vo.trade.voutils.SafeCompute;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class WorkOrderForICServiceImpl
   implements IWorkOrderForICService
 {
   public void updateWOWhenDelOutBills(AggregatedValueObject[] outBillVOs)
     throws BusinessException
   {
     try
     {
/*   83 */       updateWOWhenUpdateOutBill(outBillVOs, true);
     } catch (BusinessException e) {
/*   85 */       ExceptionUtils.asBusinessException(e);
     }
   }
   
 
 
 
 
 
   public void updateWOWhenSaveOutBill(AggregatedValueObject[] outBillVOs)
     throws BusinessException
   {
     try
     {
/*   99 */       updateWOWhenUpdateOutBill(outBillVOs, false);
     } catch (BusinessException e) {
/*  101 */       ExceptionUtils.asBusinessException(e);
     }
   }
   
 
 
 
 
 
 
 
 
 
 
   public IReqResultForInvp getWorkOrderReqForInvP(String pk_org, String temp, boolean needRed)
     throws BusinessException
   {
/*  118 */     SqlBuilder sbFrom = new SqlBuilder();
/*  119 */     sbFrom.append(" ewm_wo h inner join ewm_wo_plan_inv b on h.pk_wo=b.pk_wo ");
/*  120 */     sbFrom.append(" inner join ");
/*  121 */     sbFrom.append(temp);
/*  122 */     sbFrom.append(" temp on b.pk_material=temp.pk_material ");
/*  123 */     sbFrom.append(" and b.required_date >= temp.dstart and b.required_date<=temp.dend ");
     
/*  125 */     SqlBuilder sbWhere = new SqlBuilder();
/*  126 */     sbWhere.append("  h.dr=0 and b.dr=0 ");
/*  127 */     sbWhere.append(" and ");
/*  128 */     sbWhere.append(" h.wo_statustype ", new int[] { 1, 2 });
     
/*  130 */     sbWhere.append(" and ");
/*  131 */     sbWhere.append(" b.nnum>isnull(b.outnum,0)");
/*  132 */     sbWhere.append(" and ");
/*  133 */     sbWhere.append(" b.pk_stockorg", pk_org);
     
 
 
 
 
/*  139 */     ReqResultForInvpVO reqResult = createReqResult(sbFrom.toString(), sbWhere.toString());
/*  140 */     return reqResult;
   }
   
 
 
 
 
 
 
   private ReqResultForInvpVO createReqResult(String from, String where)
   {
/*  151 */     ReqResultForInvpVO reqResult = new ReqResultForInvpVO();
/*  152 */     reqResult.setFrom(from);
/*  153 */     reqResult.setWhere(where);
/*  154 */     reqResult.setReqTypecode("4B36");
/*  155 */     reqResult.setReqTypeid(PfDataCache.getBillType("4B36").getPk_billtypeid());
/*  156 */     String headAliasAndDot = "h.";
/*  157 */     String bodyAliasAndDot = "b.";
/*  158 */     reqResult.setReqTrantypecode("h.transi_type");
/*  159 */     reqResult.setReqTrantypeid("h.pk_transitype");
/*  160 */     reqResult.setReqid("h.pk_wo");
/*  161 */     reqResult.setReqbid("b.pk_wo_plan_inv");
/*  162 */     reqResult.setReqRowno("b.rowno");
/*  163 */     reqResult.setReqOrgid("b.pk_stockorg");
/*  164 */     reqResult.setReqOrgvid("b.pk_stockorg_v");
/*  165 */     reqResult.setMaterialid("b.pk_material");
/*  166 */     reqResult.setMaterialvid("b.pk_material_v");
/*  167 */     reqResult.setCunitid("b.pk_measdoc");
/*  168 */     reqResult.setReqCode("h.bill_code");
/*  169 */     reqResult.setReqDate("b.required_date");
/*  170 */     reqResult.setBillDate("h.creationtime");
     
/*  172 */     reqResult.setNnum("case when  isnull(b.outnum,0)>isnull(b.applynum,0) then isnull(b.nnum,0)-isnull(b.outnum,0) else isnull(b.nnum,0)-isnull(b.applynum,0) end");
     
 
 
 
 
/*  178 */     reqResult.setNallocnum("b.nreqrsnum");
/*  179 */     return reqResult;
   }
   
 
 
 
 
   private void checkUpdatable(WOActualInvVO[] actualVOsBeforeUpdate)
     throws BusinessException
   {
/*  189 */     if (ArrayUtils.isNotEmpty(actualVOsBeforeUpdate))
     {
/*  191 */       WOActualInvVO[] returnInvVO = queryCorrespondReturn(actualVOsBeforeUpdate);
/*  192 */       if (ArrayUtils.isNotEmpty(returnInvVO)) {
/*  193 */         throw new BusinessException(NCLangRes4VoTransl.getNCLangRes().getStrByID("workorder_0", "04560003-0247"));
       }
     }
   }
   
 
 
 
 
 
 
 
   private void updateWOWhenUpdateOutBill(AggregatedValueObject[] outBillVOs, boolean isDel)
     throws BusinessException
   {
/*  208 */     if (ArrayUtils.isEmpty(outBillVOs)) {
/*  209 */       return;
     }
     
/*  212 */     checkICMaterialVO(outBillVOs);
/*  213 */     String[] woIds = getWoIds(outBillVOs);
     
/*  215 */     LockManager.lockString(woIds);
     
/*  217 */     Map<String, WorkOrderHeadVO> woHeadVOMap = loadWoHeadVOById(woIds);
     
/*  219 */     checkWorkOrderStatus(woHeadVOMap);
     
/*  221 */     WOActualInvVO[] actualVOsBeforeUpdate = getActualVOsBeforeUpdate(outBillVOs);
     
/*  223 */     checkUpdatable(actualVOsBeforeUpdate);
     
 
/*  226 */     outBillVOs = nullToZero(outBillVOs);
     
/*  228 */     WOActualInvVO[] actualVONewByIC = getNewActualVOWhenICUpdate(outBillVOs, isDel);
     
/*  230 */     recoverRowNo(actualVOsBeforeUpdate, actualVONewByIC);
     
 
/*  233 */     ArrayList<WOActualInvVO[]> alActualInvBeforeUpdate = splitActualInvVO(actualVOsBeforeUpdate);
/*  234 */     WOActualInvVO[] actualVOsOutStockBeforeUpdate = (WOActualInvVO[])alActualInvBeforeUpdate.get(0);
/*  235 */     WOActualInvVO[] actualVOsRetStockBeforeUpdate = (WOActualInvVO[])alActualInvBeforeUpdate.get(1);
     
/*  237 */     ArrayList<WOActualInvVO[]> alActualVONewByIC = splitActualInvVO(actualVONewByIC);
/*  238 */     WOActualInvVO[] actualVOsOutStockNewByIC = (WOActualInvVO[])alActualVONewByIC.get(0);
/*  239 */     WOActualInvVO[] actualVOsRetStockNewByIC = (WOActualInvVO[])alActualVONewByIC.get(1);
     
 
/*  242 */     writeBackMaterial(actualVOsOutStockBeforeUpdate, actualVOsOutStockNewByIC, woHeadVOMap, false);
     
/*  244 */     writeBackMaterial(actualVOsRetStockBeforeUpdate, actualVOsRetStockNewByIC, woHeadVOMap, true);
     
 
/*  247 */     updateActualVO(actualVOsBeforeUpdate, actualVONewByIC, woHeadVOMap);
     
/*  249 */     DBAccessUtil.updateTs(WorkOrderHeadVO.getDefaultTableName(), "pk_wo", woIds);
   }
   
 
 
 
 
 
 
 
   private void recoverRowNo(WOActualInvVO[] actualVOsBeforeUpdate, WOActualInvVO[] actualVONewByIC)
   {
/*  261 */     Map<String, Object> srcBId2RowNoMap = new HashMap();
     
/*  263 */     Set<String> needSetRowNoWOIdSet = new HashSet();
     
/*  265 */     List<String> originInvIdList = new ArrayList();
     
/*  267 */     if ((actualVOsBeforeUpdate != null) && (actualVOsBeforeUpdate.length > 0)) {
/*  268 */       for (WOActualInvVO invVO : actualVOsBeforeUpdate) {
/*  269 */         srcBId2RowNoMap.put(invVO.getSrc_pk_bill_b(), invVO.getRowno());
/*  270 */         originInvIdList.add(invVO.getPk_wo_actual_inv());
       }
     }
/*  273 */     if ((actualVONewByIC != null) && (actualVONewByIC.length > 0))
/*  274 */       for (WOActualInvVO invVO : actualVONewByIC) {
/*  275 */         Object rowNo = srcBId2RowNoMap.get(invVO.getSrc_pk_bill_b());
/*  276 */         if (rowNo != null) {
/*  277 */           invVO.setRowno((String)rowNo);
         } else
/*  279 */           needSetRowNoWOIdSet.add(invVO.getPk_wo());
       }
     Map<String, List<WOActualInvVO>> changeVOMap;
     Map<String, List<WOActualInvVO>> unChangeVOMap;
/*  283 */     if (needSetRowNoWOIdSet.size() > 0)
     {
/*  285 */       changeVOMap = BaseVOUtils.groupingVOsToMap(actualVONewByIC, new String[] { "pk_wo" });
       
 
/*  288 */       StringBuilder querySql = new StringBuilder();
/*  289 */       querySql.append(" from ").append(WOActualInvVO.getDefaultTableName()).append(" where pk_wo in");
/*  290 */       querySql.append(InSqlManager.getInSQLValue((String[])needSetRowNoWOIdSet.toArray(new String[needSetRowNoWOIdSet.size()])));
       
/*  292 */       if (originInvIdList.size() > 0) {
/*  293 */         querySql.append(" and pk_wo_actual_inv not in");
/*  294 */         querySql.append(InSqlManager.getInSQLValue((String[])originInvIdList.toArray(new String[originInvIdList.size()])));
       }
/*  296 */       querySql.append(" and dr = 0");
/*  297 */       WOActualInvVO[] unChangeInvVOs = (WOActualInvVO[])QueryUtil.querySuperVOBySQL(null, querySql.toString(), WOActualInvVO.class);
       
 
/*  300 */       unChangeVOMap = new HashMap(0);
/*  301 */       if ((unChangeInvVOs != null) && (unChangeInvVOs.length > 0)) {
/*  302 */         unChangeVOMap = BaseVOUtils.groupingVOsToMap(unChangeInvVOs, new String[] { "pk_wo" });
       }
       
/*  305 */       for (String woId : needSetRowNoWOIdSet) {
/*  306 */         List<WOActualInvVO> unChangeVOList = (List)unChangeVOMap.get(woId);
/*  307 */         if (unChangeVOList == null)
/*  308 */           unChangeVOList = new ArrayList();
/*  309 */         unChangeVOList.addAll((Collection)changeVOMap.get(woId));
/*  310 */         BillRowNoVO.setVOsRowNoByRule((CircularlyAccessibleValueObject[])unChangeVOList.toArray(new WOActualInvVO[unChangeVOList.size()]), "4B36", "rowno");
       }
     }
   }
   
   private void checkWorkOrderStatus(Map<String, WorkOrderHeadVO> woHeadVOMap) throws BusinessException
   {
/*  317 */     for (Map.Entry<String, WorkOrderHeadVO> entry : woHeadVOMap.entrySet()) {
/*  318 */       WorkOrderHeadVO headVO = (WorkOrderHeadVO)entry.getValue();
/*  319 */       Integer statusType = headVO.getWo_statustype();
/*  320 */       if (statusType.intValue() == 5) {
/*  321 */         ExceptionUtils.asBusinessException(NCLangRes4VoTransl.getNCLangRes().getStrByID("workorder_0", "04560003-0374"));
       }
     }
   }
   
 
 
   private AggregatedValueObject[] nullToZero(AggregatedValueObject[] outBillVOs)
   {
/*  330 */     for (int i = 0; i < outBillVOs.length; i++) {
/*  331 */       outBillVOs[i] = ((AggregatedValueObject)((AbstractBill)outBillVOs[i]).clone());
/*  332 */       AggregatedValueObject outBillVO = outBillVOs[i];
/*  333 */       CircularlyAccessibleValueObject[] bodyVOs = outBillVO.getChildrenVO();
/*  334 */       if ((bodyVOs != null) && (bodyVOs.length > 0)) {
/*  335 */         for (CircularlyAccessibleValueObject bodyVO : bodyVOs) {
/*  336 */           UFDouble nnum = (UFDouble)bodyVO.getAttributeValue("nnum");
/*  337 */           if (nnum == null) {
/*  338 */             bodyVO.setAttributeValue("nnum", UFDouble.ZERO_DBL);
           }
         }
       }
     }
/*  343 */     return outBillVOs;
   }
   
 
 
 
 
 
   private String[] getWoIds(AggregatedValueObject[] outBillVOs)
   {
/*  353 */     HashSet<String> woIdSet = new HashSet();
/*  354 */     for (AggregatedValueObject outBillVO : outBillVOs) {
/*  355 */       CircularlyAccessibleValueObject[] outBodyVOs = outBillVO.getChildrenVO();
/*  356 */       if (outBodyVOs != null) {
/*  357 */         for (CircularlyAccessibleValueObject outBodyVO : outBodyVOs) {
/*  358 */           String woId = null;
/*  359 */           String srcType = (String)outBodyVO.getAttributeValue("csourcetype");
/*  360 */           if (BillTypeUtils.isEqual("4B36", srcType))
           {
/*  362 */             woId = (String)outBodyVO.getAttributeValue("csourcebillhid");
           }
           else {
/*  365 */             woId = (String)outBodyVO.getAttributeValue("cworkorderhid");
           }
/*  367 */           woIdSet.add(woId);
         }
       }
     }
/*  371 */     String[] woIds = (String[])woIdSet.toArray(new String[woIdSet.size()]);
/*  372 */     return woIds;
   }
   
   private Map<String, WorkOrderHeadVO> loadWoHeadVOById(String... woIds) {
/*  376 */     WorkOrderHeadVO[] woHeadVOs = (WorkOrderHeadVO[])QueryUtil.querySuperVOByPks(WorkOrderHeadVO.class, woIds);
/*  377 */     Map<String, WorkOrderHeadVO> woHeadVOMap = new HashMap();
/*  378 */     if ((woHeadVOs != null) && (woHeadVOs.length > 0)) {
/*  379 */       for (WorkOrderHeadVO headVO : woHeadVOs) {
/*  380 */         woHeadVOMap.put(headVO.getPk_wo(), headVO);
       }
     }
/*  383 */     return woHeadVOMap;
   }
   
 
 
 
 
 
   private ArrayList<WOActualInvVO[]> splitActualInvVO(WOActualInvVO[] actualInvVOs)
   {
/*  393 */     ArrayList<WOActualInvVO> alOutBillVOs = new ArrayList();
/*  394 */     ArrayList<WOActualInvVO> alRetOutBillVOs = new ArrayList();
/*  395 */     for (WOActualInvVO actualInvVO : actualInvVOs) {
/*  396 */       UFDouble nnum = actualInvVO.getNnum();
/*  397 */       if (UFDoubleUtils.isLessThan(nnum, UFDouble.ZERO_DBL)) {
/*  398 */         alRetOutBillVOs.add(actualInvVO);
       } else {
/*  400 */         alOutBillVOs.add(actualInvVO);
       }
     }
/*  403 */     ArrayList<WOActualInvVO[]> alRet = new ArrayList();
/*  404 */     alRet.add(alOutBillVOs.toArray(new WOActualInvVO[0]));
/*  405 */     alRet.add(alRetOutBillVOs.toArray(new WOActualInvVO[0]));
/*  406 */     return alRet;
   }
   
 
 
 
 
 
 
   private WOActualInvVO[] getNewActualVOWhenICUpdate(AggregatedValueObject[] outBillVOs, boolean isDel)
     throws BusinessException
   {
/*  418 */     if (!isDel) {
/*  419 */       outBillVOs = removeDeleteDatas(outBillVOs);
/*  420 */       if ((outBillVOs == null) || (outBillVOs.length == 0)) {
/*  421 */         return new WOActualInvVO[0];
       }
/*  423 */       setCurrencyToStockOutBill((MaterialOutVO[])ArrayConstructor.getArray(outBillVOs));
       
/*  425 */       AggWorkOrderVO[] actualBillVOFromIC = (AggWorkOrderVO[])PfUtilToolsAM.runChangeDataAry("4D", null, "4B36", null, outBillVOs, null);
       
/*  427 */       WOActualInvVO[] actualVONewByIC = null;
/*  428 */       if ((actualBillVOFromIC != null) && (actualBillVOFromIC.length > 0)) {
/*  429 */         ActualInvFrom4DProcessUtil.processWOBillAfterChgFrom4D(actualBillVOFromIC);
         
/*  431 */         actualVONewByIC = (WOActualInvVO[])BaseVOUtils.getBodyVOs(actualBillVOFromIC, WOActualInvVO.class);
         
/*  433 */         Set<String> planBIdSetWithUncertainCostAdded = getPlanBIdsWithUncertainCostAdded(outBillVOs, actualBillVOFromIC);
         
/*  435 */         if (planBIdSetWithUncertainCostAdded.size() > 0) {
/*  436 */           RepairPlanCostCheck.getCheckContext().addPlanBIdsWithUncertainCostAdded((String[])planBIdSetWithUncertainCostAdded.toArray(new String[planBIdSetWithUncertainCostAdded.size()]));
         }
       }
       
 
 
 
/*  443 */       for (WOActualInvVO actualVO : actualVONewByIC) {
/*  444 */         String vchangeRateFieldvalue = actualVO.getVchangerate();
         
/*  446 */         actualVO.setVchangerate(vchangeRateFieldvalue);
       }
/*  448 */       return actualVONewByIC;
     }
/*  450 */     return new WOActualInvVO[0];
   }
   
 
 
 
 
 
 
 
   private AggregatedValueObject[] removeDeleteDatas(AggregatedValueObject[] outBillVOs)
   {
/*  462 */     List<AggregatedValueObject> billVOList = new ArrayList();
/*  463 */     for (AggregatedValueObject billVO : outBillVOs) {
/*  464 */       MaterialOutVO outBillVO = (MaterialOutVO)billVO;
/*  465 */       ISuperVO[] outBodyVOs = outBillVO.getChildren(MaterialOutBodyVO.class);
/*  466 */       if ((outBodyVOs != null) && (outBodyVOs.length > 0)) {
/*  467 */         List<ISuperVO> notDeletedBodyList = new ArrayList();
/*  468 */         for (ISuperVO outBodyVO : outBodyVOs) {
/*  469 */           if (outBodyVO.getStatus() != 3) {
/*  470 */             notDeletedBodyList.add(outBodyVO);
           }
         }
/*  473 */         if (outBodyVOs.length != notDeletedBodyList.size()) {
/*  474 */           outBillVO.setChildren(MaterialOutBodyVO.class, (ISuperVO[])ArrayConstructor.getArray(notDeletedBodyList));
         }
       }
/*  477 */       outBodyVOs = outBillVO.getChildren(MaterialOutBodyVO.class);
/*  478 */       if ((outBodyVOs != null) && (outBodyVOs.length > 0)) {
/*  479 */         billVOList.add(billVO);
       }
     }
/*  482 */     return (AggregatedValueObject[])ArrayConstructor.getArray(billVOList);
   }
   
 
   private MaterialOutVO[] setCurrencyToStockOutBill(MaterialOutVO[] billvos)
     throws BusinessException
   {
/*  489 */     String[] orgs = VOEntityUtil.getVOsNotRepeatValue(VOEntityUtil.getHeadVOs(billvos), "pk_org");
/*  490 */     IStockOrgPubService stockOrgService = (IStockOrgPubService)AMProxy.lookup(IStockOrgPubService.class);
/*  491 */     Map<String, String> finaorgMap = stockOrgService.queryFinanceOrgIDsByStockOrgIDs(orgs);
/*  492 */     for (ICBillVO vo : billvos) {
/*  493 */       String pk_org = vo.getHead().getPk_org();
/*  494 */       String currencyId = CurrencyManager.getLocalCurrencyPK((String)finaorgMap.get(pk_org));
/*  495 */       ICBillBodyVO[] bodys = vo.getBodys();
/*  496 */       for (ICBillBodyVO icBillBodyVO : bodys) {
/*  497 */         ((MaterialOutBodyVO)icBillBodyVO).setCcurrencyid(currencyId);
       }
     }
/*  500 */     return billvos;
   }
   
 
 
 
 
 
 
 
   private Set<String> getPlanBIdsWithUncertainCostAdded(AggregatedValueObject[] outBillVOs, AggWorkOrderVO[] workOrderBillVOs)
   {
/*  512 */     Set<String> planBIdSetWithUncertainCostAdded = new HashSet();
/*  513 */     Set<String> outBillBIdSetWithUncertainCostAdded = new HashSet();
     
/*  515 */     for (AggregatedValueObject outBillVO : outBillVOs) {
/*  516 */       CircularlyAccessibleValueObject[] outBodyVOs = outBillVO.getChildrenVO();
/*  517 */       if ((outBodyVOs != null) && (outBodyVOs.length == 0)) {
/*  518 */         for (CircularlyAccessibleValueObject outBodyVO : outBodyVOs)
         {
 
/*  521 */           if (outBodyVO.getAttributeValue("ncostmny") == null) {
/*  522 */             outBillBIdSetWithUncertainCostAdded.add(((SuperVO)outBodyVO).getPrimaryKey());
           }
         }
       }
     }
     
 
/*  529 */     for (AggWorkOrderVO workOrderBillVO : workOrderBillVOs) {
/*  530 */       WorkOrderHeadVO headVO = workOrderBillVO.getParentVO();
/*  531 */       if (headVO.getPk_repair_plan_b() != null)
       {
 
/*  534 */         WOActualInvVO[] invVOs = (WOActualInvVO[])ArrayConstructor.getArray(workOrderBillVO.getChildren(WOActualInvVO.class));
/*  535 */         if ((invVOs != null) && (invVOs.length > 0)) {
/*  536 */           for (WOActualInvVO invVO : invVOs) {
/*  537 */             if (outBillBIdSetWithUncertainCostAdded.contains(invVO.getSrc_pk_bill_b()))
/*  538 */               planBIdSetWithUncertainCostAdded.add(headVO.getPk_repair_plan_b());
           }
         }
       }
     }
/*  543 */     return planBIdSetWithUncertainCostAdded;
   }
   
 
 
 
   private void checkICMaterialVO(AggregatedValueObject[] outBillVOs)
     throws BusinessException
   {
/*  552 */     StringBuffer sMsg = new StringBuffer();
/*  553 */     for (AggregatedValueObject billVO : outBillVOs) {
/*  554 */       CircularlyAccessibleValueObject[] bodyVOs = billVO.getChildrenVO();
/*  555 */       for (CircularlyAccessibleValueObject body : bodyVOs) {
/*  556 */         String srcType = (String)body.getAttributeValue("csourcetype");
         
/*  558 */         boolean isFromWorkOrder = BillTypeUtils.isEqual("4B36", srcType);
/*  559 */         if (!isFromWorkOrder)
         {
/*  561 */           String linkedWorkOrderId = (String)body.getAttributeValue("cworkorderhid");
/*  562 */           if (linkedWorkOrderId != null)
/*  563 */             isFromWorkOrder = true;
         }
/*  565 */         if (!isFromWorkOrder)
         {
 
/*  568 */           String billcode = (String)billVO.getParentVO().getAttributeValue("vbillcode");
           
/*  570 */           sMsg.append(NCLangRes4VoTransl.getNCLangRes().getStrByID("workorder_0", "04560003-0248") + billcode + NCLangRes4VoTransl.getNCLangRes().getStrByID("workorder_0", "04560003-0249"));
           
 
 
 
 
 
 
 
 
 
/*  581 */           break;
         }
       }
     }
/*  585 */     if (sMsg.length() > 0) {
/*  586 */       throw new BusinessException(sMsg.toString());
     }
   }
   
 
 
 
 
 
   private WOActualInvVO[] getActualVOsBeforeUpdate(AggregatedValueObject[] outBillVOs)
     throws BusinessException
   {
/*  598 */     ArrayList<String> alKeys = new ArrayList();
/*  599 */     for (int i = 0; i < outBillVOs.length; i++) {
/*  600 */       alKeys.add(outBillVOs[i].getParentVO().getPrimaryKey());
     }
     
/*  603 */     WOActualInvVO[] originalVOs = queryActualInv(alKeys);
/*  604 */     return originalVOs;
   }
   
 
 
 
 
 
 
 
   private void writeBackMaterial(WOActualInvVO[] actualVOsBeforeUpdate, WOActualInvVO[] newActualVOs, Map<String, WorkOrderHeadVO> woHeadVOMap, boolean isRetStock)
     throws BusinessException
   {
/*  617 */     WOActualInvVO[] difActualVOs = getWriteBackActualVO(actualVOsBeforeUpdate, newActualVOs);
/*  618 */     if ((difActualVOs != null) && (difActualVOs.length > 0)) {
/*  619 */       Map<List<String>, UFDouble> numChgMap = calculateNumChg(difActualVOs);
/*  620 */       for (Map.Entry<List<String>, UFDouble> entry : numChgMap.entrySet())
       {
/*  622 */         if (!UFDouble.ZERO_DBL.equals(entry.getValue()))
         {
 
 
/*  626 */           WorkOrderHeadVO woHeadVO = (WorkOrderHeadVO)woHeadVOMap.get(((List)entry.getKey()).get(1));
/*  627 */           int woStatusType = woHeadVO.getWo_statustype().intValue();
/*  628 */           if ((woStatusType == 4) || (woStatusType == 5))
           {
/*  630 */             ExceptionUtils.asBusinessException(NCLangRes4VoTransl.getNCLangRes().getStrByID("workorder_0", "04560003-0372"));
 
 
 
 
           }
/*  636 */           else if ((((UFDouble)entry.getValue()).compareTo(UFDouble.ZERO_DBL) > 0) && 
/*  637 */             (woStatusType == 3)) {
/*  638 */             ExceptionUtils.asBusinessException(NCLangRes4VoTransl.getNCLangRes().getStrByID("workorder_0", "04560003-0373"));
           }
         }
       }
     }
     
 
 
 
 
//writeBackActualInvCost(difActualVOs,woHeadVOMap);
/*  649 */     writeBackPlanInv(difActualVOs, isRetStock);
/*  650 */     if (isRetStock)
     {
/*  652 */       writeBackActualInv(difActualVOs);
				 
     }
   }
   
 
   private Map<List<String>, UFDouble> calculateNumChg(WOActualInvVO[] difActualVOs)
   {
/*  659 */     Map<List<String>, UFDouble> numChgMap = new HashMap();
/*  660 */     for (WOActualInvVO difActualVO : difActualVOs) {
/*  661 */       if (UFBoolean.TRUE.equals(difActualVO.getLink_flag()))
       {
/*  663 */         String actualInvId = difActualVO.getPk_wo_actual_inv();
/*  664 */         String woId = difActualVO.getPk_wo();
/*  665 */         if ((actualInvId != null) && (woId != null)) {
/*  666 */           List<String> key = Arrays.asList(new String[] { actualInvId, woId });
/*  667 */           numChgMap.put(key, difActualVO.getNnum());
         }
       }
       else {
/*  671 */         String planInvId = difActualVO.getFirst_billbid();
/*  672 */         String woId = difActualVO.getFirst_billid();
/*  673 */         if ((planInvId != null) && (woId != null)) {
/*  674 */           List<String> key = Arrays.asList(new String[] { planInvId, woId });
/*  675 */           if (numChgMap.containsKey(key)) {
/*  676 */             numChgMap.put(key, ((UFDouble)numChgMap.get(key)).add(difActualVO.getNnum()));
           } else {
/*  678 */             numChgMap.put(key, difActualVO.getNnum());
           }
         }
       }
     }
/*  683 */     return numChgMap;
   }
   
 
 
 
 
 
 
 
   private void updateActualVO(WOActualInvVO[] originalVOs, WOActualInvVO[] actualVONewByIC, Map<String, WorkOrderHeadVO> woHeadVOMap)
     throws BusinessException
   {
/*  696 */     ArrayList<String> alhid = new ArrayList();
     
/*  698 */     if (ArrayUtils.isNotEmpty(originalVOs))
     {
/*  700 */       VOPersistUtil.delete(originalVOs);
/*  701 */       for (int i = 0; i < originalVOs.length; i++) {
/*  702 */         if (!alhid.contains(originalVOs[i].getPk_wo())) {
/*  703 */           alhid.add(originalVOs[i].getPk_wo());
         }
       }
     }
     
/*  708 */     if (ArrayUtils.isNotEmpty(actualVONewByIC)) {
/*  709 */       List<WOActualInvVO> notZeroVOList = new ArrayList();
       
/*  711 */       List<String> zeroNum4DBIdList = new ArrayList();
/*  712 */       for (WOActualInvVO actualInvVO : actualVONewByIC) {
/*  713 */         if ((actualInvVO.getNnum() != null) && (!UFDouble.ZERO_DBL.equals(actualInvVO.getNnum()))) {
/*  714 */           notZeroVOList.add(actualInvVO);
         }
/*  716 */         else if (UFBoolean.TRUE.equals(actualInvVO.getLink_flag())) {
/*  717 */           zeroNum4DBIdList.add(actualInvVO.getSrc_pk_bill_b());
         }
       }
/*  720 */       actualVONewByIC = (WOActualInvVO[])notZeroVOList.toArray(new WOActualInvVO[notZeroVOList.size()]);
       
/*  722 */       if (zeroNum4DBIdList.size() > 0)
       {
/*  724 */         if (ModuleInfoQuery.isICEnabled()) {
/*  725 */           ((IMaterialOutServiceForEWM)AMProxy.lookup(IMaterialOutServiceForEWM.class)).updateMOWhenDelOrSaveLinkedWO((String[])zeroNum4DBIdList.toArray(new String[zeroNum4DBIdList.size()]));
         }
       }
     }
     
/*  730 */     if (ArrayUtils.isNotEmpty(actualVONewByIC))
     {
/*  732 */       VOPersistUtil.insert(actualVONewByIC);
/*  733 */       for (int i = 0; i < actualVONewByIC.length; i++) {
/*  734 */         if (!alhid.contains(actualVONewByIC[i].getPk_wo())) {
/*  735 */           alhid.add(actualVONewByIC[i].getPk_wo());
         }
       }
     }
     
/*  740 */     setUncertainCostInfo(originalVOs, actualVONewByIC, woHeadVOMap);
     
/*  742 */     setRepairPlanBId2WOCodeMap(woHeadVOMap);
     
/*  744 */     if (alhid.size() > 0) {
/*  745 */       String[] headKeys = (String[])alhid.toArray(new String[0]);
/*  746 */       new WorkOrderCostImpl().calWorkOrdreMaterialCost(headKeys);
     }
   }
   
 
 
 
 
   private void setRepairPlanBId2WOCodeMap(Map<String, WorkOrderHeadVO> woHeadVOMap)
   {
/*  756 */     RepairPlanCostCheck.getCheckContext().setOuterCheck(true);
/*  757 */     for (WorkOrderHeadVO woHeadVO : woHeadVOMap.values()) {
/*  758 */       RepairPlanCostCheck.getCheckContext().addToRepairPlan2WOCodeMap(woHeadVO.getPk_repair_plan_b(), woHeadVO.getBill_code());
     }
   }
   
 
 
 
 
 
 
 
 
   private void setUncertainCostInfo(WOActualInvVO[] originInvVOs, WOActualInvVO[] newInvVOs, Map<String, WorkOrderHeadVO> woHeadVOMap)
   {
/*  772 */     if (RepairPlanCostCheck.checkAbandoned())
/*  773 */       return;
/*  774 */     if ((newInvVOs != null) && (newInvVOs.length > 0))
     {
/*  776 */       Map<String, String> m4DBId2PlanBIdMap = new HashMap();
       
/*  778 */       Map<String, UFDouble> m4DBId2NumMap = new HashMap();
       
/*  780 */       for (WOActualInvVO newInvVO : newInvVOs) {
/*  781 */         String planBId = ((WorkOrderHeadVO)woHeadVOMap.get(newInvVO.getPk_wo())).getPk_repair_plan_b();
/*  782 */         if ((planBId != null) && 
/*  783 */           (newInvVO.getMoney() == null)) {
/*  784 */           m4DBId2PlanBIdMap.put(newInvVO.getSrc_pk_bill_b(), planBId);
/*  785 */           m4DBId2NumMap.put(newInvVO.getSrc_pk_bill_b(), newInvVO.getNnum());
         }
       }
       
 
/*  790 */       if ((originInvVOs != null) && (originInvVOs.length > 0)) {
/*  791 */         for (WOActualInvVO originInvVO : originInvVOs) {
/*  792 */           if (originInvVO.getMoney() == null) {
/*  793 */             UFDouble newNum = (UFDouble)m4DBId2NumMap.get(originInvVO.getSrc_pk_bill_b());
/*  794 */             if (newNum != null) {
/*  795 */               UFDouble originNum = originInvVO.getNnum();
/*  796 */               if (!UFDoubleUtils.isLessThan(originNum, newNum)) {
/*  797 */                 m4DBId2PlanBIdMap.remove(originInvVO.getSrc_pk_bill_b());
               }
             }
           }
         }
       }
       
/*  804 */       if (m4DBId2PlanBIdMap.size() > 0) {
/*  805 */         RepairPlanCostCheck.getCheckContext().addPlanBIdsWithUncertainCostAdded((String[])m4DBId2PlanBIdMap.values().toArray(new String[m4DBId2PlanBIdMap.size()]));
       }
     }
   }
   
 
 
 
 
 
   private void writeBackActualInv(WOActualInvVO[] retActualVOs)
     throws BusinessException
   {
/*  818 */     if (ArrayUtils.isNotEmpty(retActualVOs))
     {
 
/*  821 */       String sql = " update ewm_wo_actual_inv set retnum=COALESCE(retnum,0)-? where pk_wo_actual_inv=? and dr=0 ";
/*  822 */       SQLParameter[] sqlpara = new SQLParameter[retActualVOs.length];
/*  823 */       Set<String> albid = new HashSet();
/*  824 */       for (int i = 0; i < retActualVOs.length; i++) {
/*  825 */         sqlpara[i] = new SQLParameter();
/*  826 */         sqlpara[i].addParam(retActualVOs[i].getNnum());
/*  827 */         sqlpara[i].addParam(retActualVOs[i].getCother_bill_bid());
/*  828 */         albid.add(retActualVOs[i].getCother_bill_bid());
       }
       
 
/*  832 */       String checksrcOutSql = "select pk_wo_actual_inv from ewm_wo_actual_inv where dr=0 and pk_wo_actual_inv in " + InSqlManager.getInSQLValue((String[])albid.toArray(new String[albid.size()]));
       
/*  834 */       List<?> srcOutCheckResult = new DBAccessUtil().executeQuerySQL(checksrcOutSql.toString());
/*  835 */       if (srcOutCheckResult.size() < albid.size()) {
/*  836 */         ExceptionUtils.asBusinessException(NCLangRes4VoTransl.getNCLangRes().getStrByID("workorder_0", "04560003-0519"));
       }
       
 
       try
       {
/*  842 */         new DBAccessUtil().batchUpdate(sql, sqlpara);
       } catch (BusinessException e) {
/*  844 */         ExceptionUtils.asBusinessException(e);
       }
       
 
/*  848 */       SqlBuilder sqlCheck = new SqlBuilder(" select rowno from ewm_wo_actual_inv where retnum>nnum and dr=0 ");
/*  849 */       sqlCheck.append("and pk_wo_actual_inv in ");
/*  850 */       sqlCheck.append(InSqlManager.getInSQLValue((String[])albid.toArray(new String[albid.size()])));
/*  851 */       List<String[]> listValue = new DBAccessUtil().executeQuerySQL(sqlCheck.toString());
/*  852 */       if (listValue.size() > 0) {
/*  853 */         throw new BusinessException(NCLangRes4VoTransl.getNCLangRes().getStrByID("workorder_0", "04560003-0257"));
       }
     }
   }
// add by 2021-05-26 更新實際成本和服務成本
	private void writeBackActualInvCost(WOActualInvVO[] retActualVOs,
			Map<String, WorkOrderHeadVO> woHeadVOMap) throws BusinessException {
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
		
		HYPubBO bo = new HYPubBO();
		// 服务类存货取材料出库单的单价
		// 材料类型的存货取最新成本价
		Map<String, UFDouble> serviceMap = new HashMap<String, UFDouble>();
		Map<String, UFDouble> materialMap = new HashMap<String, UFDouble>();
		for (WOActualInvVO vo : retActualVOs) {
			MaterialVO vo1 = voMap.get(vo.getPk_material());
			if (vo1 == null)
				throw new BusinessException("物料信息出错，请检查！");
			String str = (String) bo.findColValue(
					"bd_marbasclass",
					"code ",
					" nvl(dr,0) = 0 and pk_marbasclass = '"
							+ vo1.getPk_marbasclass() + "'");
			if (str.startsWith("09")) {// 服務類
				UFDouble price = getMaterialPrice(vo.getPk_material(),
						vo.getSrc_pk_bill_b());
				calMny(serviceMap, price, vo);
			} else {
				UFDouble price = getCostPrice(vo.getPk_material(),
						vo.getPk_stockorg(),vo.getPk_group());
				calMny(materialMap, price, vo);
			}
		}
		updateNmny(retActualVOs);
		updateHeadNmny(serviceMap, "def4");//实际fuwu成本
		updateHeadNmny(materialMap, "ac_mtr_mny_org");//实际物料成本
	}
	
	private void updateNmny(WOActualInvVO[] retActualVOs) throws BusinessException{
		String sql = " update ewm_wo_actual_inv set money=? where pk_wo_actual_inv=? and dr=0 ";
		List<SQLParameter> sqlparaList = new ArrayList();
		for (int i = 0; i < retActualVOs.length; i++) {
			SQLParameter sqlpara = new SQLParameter();
			sqlpara.addParam(retActualVOs[i].getNnum());
			sqlpara.addParam(retActualVOs[i].getFirst_billbid());
			sqlparaList.add(sqlpara);
		}
		if (sqlparaList.size() > 0) {
			try {
				new DBAccessUtil().batchUpdate(sql,
						(SQLParameter[]) sqlparaList
								.toArray(new SQLParameter[sqlparaList.size()]));
			} catch (BusinessException e) {
				ExceptionUtils.asBusinessException(e);
			}
		}
	}
	
	private void updateHeadNmny(Map<String, UFDouble> serviceMap,String itemkey) throws BusinessException{
		String sql = " update ewm_wo set "+itemkey+"=? where pk_wo=? and dr=0 ";
		List<SQLParameter> sqlparaList = new ArrayList();
		for (Map.Entry<String, UFDouble> entry : serviceMap.entrySet()) {
			SQLParameter sqlpara = new SQLParameter();
			sqlpara.addParam(entry.getValue());
			sqlpara.addParam(entry.getKey());
			sqlparaList.add(sqlpara);
		}
		if (sqlparaList.size() > 0) {
			try {
				new DBAccessUtil().batchUpdate(sql,
						(SQLParameter[]) sqlparaList
								.toArray(new SQLParameter[sqlparaList.size()]));
			} catch (BusinessException e) {
				ExceptionUtils.asBusinessException(e);
			}
		}
	}
	
	private void calMny(Map<String,UFDouble> materialMap,UFDouble price,WOActualInvVO vo){
		UFDouble mny = UFDouble.ZERO_DBL;
		mny = SafeCompute.multiply(price, vo.getNnum());
		if(materialMap.containsKey(vo.getPk_wo())){
			mny = SafeCompute.add(vo.getMoney(), materialMap.get(vo.getPk_wo()));
		}else{
			mny = SafeCompute.add(vo.getMoney(), mny);
		}
		materialMap.put(vo.getPk_wo(), mny);
	}
	
	private UFDouble getCostPrice(String pk_material,String pk_org,String pk_group) throws BusinessException{
//		String sql = " select b.nabprice from  ia_monthnab b where b.cinventoryid ='"+pk_material+"' and b.pk_org ='"+pk_org+"' and nvl(b.dr,0) = 0 order by ts desc;";
//		List<UFDouble> list =(List<UFDouble>) new DBAccessUtil().executeQuery(sql, new ColumnListProcessor());
//		UFDouble price  = UFDouble.ZERO_DBL;
//		if(list != null && list.size()>0){
//			price = list.get(0);
//		}
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
	  sql.append(" and pk_org = " + getCostOrg(qryParam.get("pk_org").toString()));
	  // 本月之前的期间--当前业务场景应该不需要，如果需要，请注意会计月的转换
	  sql.append(" )");
	  // 过滤物料
	  if(cmaterialoids!=null && cmaterialoids.length >0){
	   sql.append(" and cinventoryid " + getMaterialsIn(cmaterialoids));
	  }
	  sql.append(" and h.pk_group = '" +  qryParam.get("pk_group") + "'");
	  // 成本域
	  sql.append(" and pk_org = " + getCostOrg(qryParam.get("pk_org").toString()));
	  
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
  */
 String getCostOrg(String pk_stockorg) {
  String sql = " ( select  pk_costregion  from org_costregion where nvl(dr,0)=0  "
    + " and pk_org='" + pk_stockorg + "')";
  return sql;
 }

 String getMaterialsIn(String[] cmaterialoids) throws BusinessException {

  SqlInUtil util = new SqlInUtil(cmaterialoids);
  return util.getInSql();
 }

 public UFDouble getUFDoubleNullASZero(UFDouble value){
  if(value== null ||"".equals(value)){
   return UFDouble.ZERO_DBL;
  }
  return value;
 }
	private UFDouble getMaterialPrice(String pk_material,String csourcebid) throws DAOException{
		String sql = " select  ncostprice  from ic_material_b b where b.cgeneralbid ='"+csourcebid+"' and nvl(b.dr,0) = 0";
		UFDouble price = (UFDouble) new DBAccessUtil().executeQuery(sql, new ColumnProcessor());
		return price;
	}
	
 
   private void writeBackPlanInv(WOActualInvVO[] retActualVOs, boolean isRetStock)
     throws BusinessException
   {
/*  865 */     if (ArrayUtils.isEmpty(retActualVOs)) {
/*  866 */       return;
     }
     
/*  869 */     String sql = " update ewm_wo_plan_inv set outnum=COALESCE(outnum,0)+? where pk_wo_plan_inv=? and dr=0 ";
     
 
 
 
 
 
 
 
 
 
 
 
 
 
 
/*  885 */     List<SQLParameter> sqlparaList = new ArrayList();
/*  886 */     ArrayList<String> albid = new ArrayList();
/*  887 */     for (int i = 0; i < retActualVOs.length; i++) {
/*  888 */       if (!UFBoolean.TRUE.equals(retActualVOs[i].getLink_flag())) {
/*  889 */         SQLParameter sqlpara = new SQLParameter();
/*  890 */         sqlpara.addParam(retActualVOs[i].getNnum());
         
/*  892 */         sqlpara.addParam(retActualVOs[i].getFirst_billbid());
/*  893 */         sqlparaList.add(sqlpara);
/*  894 */         albid.add(retActualVOs[i].getFirst_billbid());
       }
     }
/*  897 */     if (sqlparaList.size() > 0) {
       try {
/*  899 */         new DBAccessUtil().batchUpdate(sql, (SQLParameter[])sqlparaList.toArray(new SQLParameter[sqlparaList.size()]));
       } catch (BusinessException e) {
/*  901 */         ExceptionUtils.asBusinessException(e);
       }
     }
     
 
/*  906 */     checkSendOutMaterialNum(albid, isRetStock);
   }
   
 
 
 
 
   private void checkSendOutMaterialNum(ArrayList<String> alPlanInvBid, boolean isRetStock)
     throws BusinessException
   {
/*  916 */     SqlBuilder sqlCheck = new SqlBuilder();
     
/*  918 */     sqlCheck.append("  outnum>nnum  and dr=0 ");
/*  919 */     sqlCheck.append(" and pk_wo_plan_inv in ");
/*  920 */     sqlCheck.append(InSqlManager.getInSQLValue(alPlanInvBid));
     
/*  922 */     WOPlanInVVO[] planInvVOs = (WOPlanInVVO[])QueryUtil.queryVOByCond(WOPlanInVVO.class, sqlCheck.toString(), null);
     
/*  924 */     String sMsg = null;
/*  925 */     if (ArrayUtils.isNotEmpty(planInvVOs)) {
/*  926 */       if (isRetStock) {
/*  927 */         sMsg = NCLangRes4VoTransl.getNCLangRes().getStrByID("workorder_0", "04560003-0257");
 
 
       }
       else
       {
 
/*  934 */         String pk_org = planInvVOs[0].getPk_org();
/*  935 */         UFBoolean bPermit = WorkOrderParamUtils.getBPermitWhenOverPlanNum(pk_org);
/*  936 */         if (!bPermit.booleanValue()) {
/*  937 */           sMsg = NCLangRes4VoTransl.getNCLangRes().getStrByID("workorder_0", "04560003-0258");
         }
       }
     }
     
 
 
 
/*  945 */     if (sMsg != null) {
/*  946 */       throw new BusinessException(sMsg);
     }
/*  948 */     if (isRetStock) {
/*  949 */       sqlCheck.reset();
       
/*  951 */       sqlCheck.append("  outnum<0  and dr=0 ");
/*  952 */       sqlCheck.append(" and pk_wo_plan_inv in ");
/*  953 */       sqlCheck.append(InSqlManager.getInSQLValue(alPlanInvBid));
       
/*  955 */       WOPlanInVVO[] planInvVOsForRet = (WOPlanInVVO[])QueryUtil.queryVOByCond(WOPlanInVVO.class, sqlCheck.toString(), null);
       
/*  957 */       String sMsgForRet = null;
/*  958 */       if (ArrayUtils.isNotEmpty(planInvVOsForRet)) {
/*  959 */         sMsgForRet = NCLangRes4VoTransl.getNCLangRes().getStrByID("workorder_0", "04560003-0257");
       }
       
 
 
 
/*  965 */       if (sMsgForRet != null) {
/*  966 */         throw new BusinessException(sMsgForRet);
       }
     }
   }
   
 
 
 
 
 
 
 
 
   private WOActualInvVO[] getWriteBackActualVO(WOActualInvVO[] actualVOsBeforeUpdate, WOActualInvVO[] newActualVOs)
   {
/*  981 */     WOActualInvVO[] retActualVOs = null;
/*  982 */     if (ArrayUtils.isEmpty(actualVOsBeforeUpdate))
     {
/*  984 */       retActualVOs = dealNewRow(newActualVOs);
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
     }
/* 1004 */     else if (ArrayUtils.isEmpty(newActualVOs))
     {
/* 1006 */       retActualVOs = dealDelRow(actualVOsBeforeUpdate);
 
 
 
 
 
     }
     else
     {
 
 
 
 
 
/* 1020 */       HashMap<String, WOActualInvVO> hmNewActual = new HashMap();
       
/* 1022 */       for (WOActualInvVO actualvo : newActualVOs) {
/* 1023 */         hmNewActual.put(actualvo.getSrc_pk_bill_b(), actualvo);
       }
/* 1025 */       ArrayList<WOActualInvVO> alActualVO = new ArrayList();
/* 1026 */       for (WOActualInvVO oriactualvo : actualVOsBeforeUpdate) {
/* 1027 */         String outbillbid = oriactualvo.getSrc_pk_bill_b();
/* 1028 */         WOActualInvVO newActualVO = (WOActualInvVO)hmNewActual.get(outbillbid);
/* 1029 */         if (newActualVO == null)
         {
/* 1031 */           alActualVO.add(dealDelRow(oriactualvo));
         }
         else {
/* 1034 */           WOActualInvVO retActualVO = dealUpdateRow(oriactualvo, newActualVO);
/* 1035 */           if (retActualVO != null) {
/* 1036 */             alActualVO.add(retActualVO);
           }
/* 1038 */           hmNewActual.remove(outbillbid);
         }
       }
       
/* 1042 */       if (hmNewActual.size() > 0) {
/* 1043 */         WOActualInvVO[] addActualVOs = dealNewRow((WOActualInvVO[])hmNewActual.values().toArray(new WOActualInvVO[0]));
/* 1044 */         for (WOActualInvVO addActualVO : addActualVOs) {
/* 1045 */           alActualVO.add(addActualVO);
         }
       }
/* 1048 */       retActualVOs = (WOActualInvVO[])alActualVO.toArray(new WOActualInvVO[0]);
     }
     
 
/* 1052 */     return retActualVOs;
   }
   
 
 
 
 
   private WOActualInvVO dealDelRow(WOActualInvVO oriActualvo)
   {
/* 1061 */     return dealDelRow(new WOActualInvVO[] { oriActualvo })[0];
   }
   
 
 
 
 
   private WOActualInvVO[] dealDelRow(WOActualInvVO[] oriActualvo)
   {
/* 1070 */     for (int i = 0; i < oriActualvo.length; i++) {
/* 1071 */       oriActualvo[i].setNassistnum(UFDouble.ZERO_DBL.sub(oriActualvo[i].getNassistnum()));
/* 1072 */       oriActualvo[i].setNnum(UFDouble.ZERO_DBL.sub(oriActualvo[i].getNnum()));
     }
/* 1074 */     return oriActualvo;
   }
   
 
 
 
 
 
 
 
   private WOActualInvVO dealUpdateRow(WOActualInvVO oriActualvo, WOActualInvVO newActualvo)
   {
/* 1086 */     UFDouble nnumber = newActualvo.getNnum().sub(oriActualvo.getNnum());
/* 1087 */     if (nnumber.equals(UFDouble.ZERO_DBL)) {
/* 1088 */       oriActualvo = null;
     } else {
/* 1090 */       oriActualvo.setNnum(nnumber);
     }
/* 1092 */     return oriActualvo;
   }
   
 
 
 
 
   private WOActualInvVO[] dealNewRow(WOActualInvVO[] newActualVOs)
   {
/* 1101 */     return newActualVOs;
   }
   
 
 
 
 
   private WOActualInvVO[] queryActualInv(List<String> listOutBillHeadPks)
     throws BusinessException
   {
/* 1111 */     String sqlIn = InSqlManager.getInSQLValue(listOutBillHeadPks);
/* 1112 */     StringBuffer sb = new StringBuffer();
/* 1113 */     sb.append("src_pk_bill");
/* 1114 */     sb.append(" in ");
/* 1115 */     sb.append(sqlIn);
/* 1116 */     sb.append(" and dr=0 ");
/* 1117 */     WOActualInvVO[] originalVO = (WOActualInvVO[])QueryUtil.queryVOByCond(WOActualInvVO.class, sb.toString(), null);
/* 1118 */     return originalVO;
   }
   
 
 
 
 
 
   private WOActualInvVO[] queryCorrespondReturn(WOActualInvVO[] actualVOsBeforeUpdate)
   {
/* 1128 */     ArrayList<String> alkeys = new ArrayList();
/* 1129 */     for (WOActualInvVO invVO : actualVOsBeforeUpdate) {
/* 1130 */       alkeys.add(invVO.getPk_wo_actual_inv());
     }
/* 1132 */     String sqlIn = null;
/* 1133 */     sqlIn = InSqlManager.getInSQLValue(alkeys);
/* 1134 */     StringBuffer sb = new StringBuffer();
/* 1135 */     sb.append("cother_bill_bid");
/* 1136 */     sb.append(" in ");
/* 1137 */     sb.append(sqlIn);
/* 1138 */     sb.append(" and dr=0 ");
/* 1139 */     WOActualInvVO[] acutalReturnVO = (WOActualInvVO[])QueryUtil.queryVOByCond(WOActualInvVO.class, sb.toString(), null);
/* 1140 */     return acutalReturnVO;
   }
   
 
 
 
 
 
 
 
 
 
 
 
   public void updateNtalrsnum(Map<String, UFDouble> param)
     throws BusinessException
   {
/* 1157 */     WOPlanInVVO[] planInvVOs = (WOPlanInVVO[])QueryUtil.querySuperVOByPks(WOPlanInVVO.class, (String[])param.keySet().toArray(new String[0]));
     
 
/* 1160 */     for (int i = 0; i < planInvVOs.length; i++)
     {
 
 
 
 
/* 1166 */       UFDouble nreqrsnum = planInvVOs[i].getNreqrsnum();
/* 1167 */       if (nreqrsnum == null) {
/* 1168 */         nreqrsnum = UFDouble.ZERO_DBL;
       }
       
/* 1171 */       UFDouble incrementNum = (UFDouble)param.get(planInvVOs[i].getPk_wo_plan_inv());
/* 1172 */       if (incrementNum != null)
       {
/* 1174 */         nreqrsnum = nreqrsnum.add(incrementNum);
         
 
 
 
 
 
 
/* 1182 */         planInvVOs[i].setNreqrsnum(nreqrsnum);
       }
     }
     
/* 1186 */     new BaseDAO().updateVOArray(planInvVOs);
     
/* 1188 */     HashSet<String> woIdSet = new HashSet();
/* 1189 */     for (WOPlanInVVO planInvVO : planInvVOs) {
/* 1190 */       woIdSet.add(planInvVO.getPk_wo());
     }
/* 1192 */     DBAccessUtil.updateTs(WorkOrderHeadVO.getDefaultTableName(), "pk_wo", (String[])woIdSet.toArray(new String[0]));
   }
   
 
   public AggregatedValueObject[] adjustAfterChgTo4D(AggregatedValueObject[] srcVOs, AggregatedValueObject[] destVOs, ChangeVOAdjustContext adjustContext)
     throws BusinessException
   {
/* 1199 */     AggWorkOrderVO[] workOrderVOs = (AggWorkOrderVO[])ArrayConstructor.getArray(srcVOs);
     
/* 1201 */     if ((workOrderVOs[0].getBICRETBill() == null) || (!workOrderVOs[0].getBICRETBill().booleanValue())) {
/* 1202 */       return destVOs;
     }
/* 1204 */     AggregatedValueObject[] icOutBillVOs = ((IWorkOrderMakeBill)AMProxy.lookup(IWorkOrderMakeBill.class)).getMaterialRetStockOutBill(workOrderVOs);
     
/* 1206 */     return icOutBillVOs;
   }
   
   public void updateWOWhenSaveLinkedOutBill(AggregatedValueObject[] outBillVOs)
     throws BusinessException
   {
/* 1212 */     updateWOWhenSaveOutBill(outBillVOs);
   }
   
   public void updateWOWhenDelLinkedOutBills(AggregatedValueObject[] outBillVOs) throws BusinessException
   {
/* 1217 */     updateWOWhenDelOutBills(outBillVOs);
   }
   
 
 
   public void abandonWOCostCheck() {}
   
 
   @Deprecated
   public String getWorkOrderSqlForInvP(String pk_org, String temp, boolean needRed)
     throws BusinessException
   {
/* 1229 */     return null;
   }
 }