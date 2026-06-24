/*     */ package nc.impl.ewm.workorder;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;

/*     */ import nc.bs.am.framework.action.IActionTemplate;
/*     */ import nc.bs.am.framework.action.update.UpdateActionTemplate;
/*     */ import nc.bs.am.framework.common.rule.ValidateServiceRule;
/*     */ import nc.bs.logging.Logger;
import nc.bs.trade.business.HYPubBO;
/*     */ import nc.bs.uif2.validation.Validator;
/*     */ import nc.impl.am.bill.BillBaseDAO;
/*     */ import nc.impl.am.bill.rule.AppendBusiTypeBeforeRule;
/*     */ import nc.impl.am.bill.rule.AppendTransiOrBusiTypeRule;
/*     */ import nc.impl.am.bill.rule.BillCodeDeleteRule;
/*     */ import nc.impl.am.bill.rule.BillCodeInsertAfterRule;
/*     */ import nc.impl.am.bill.rule.BillCodeInsertBeforeRule;
/*     */ import nc.impl.am.bill.rule.BillCodePushSaveBeforeRule;
/*     */ import nc.impl.am.bill.rule.SetBizScopeInfoBeforeRule;
/*     */ import nc.impl.am.bill.rule.SetOrgVidBeforeRule;
/*     */ import nc.impl.am.bill.rule.VoStatusSetBeforeRule;
/*     */ import nc.impl.am.common.InSqlManager;
/*     */ import nc.impl.am.db.DBAccessUtil;
/*     */ import nc.impl.am.db.QueryUtil;
/*     */ import nc.impl.eampub.util.srcbill.SrcBillConsistenceValidator;
/*     */ import nc.impl.ewm.workorder.rule.ComputerCostBeforePersist;
/*     */ import nc.impl.ewm.workorder.rule.DecompressInRoadInfoRule;
/*     */ import nc.impl.ewm.workorder.rule.FailureReasonSaveAfterRule;
/*     */ import nc.impl.ewm.workorder.rule.ProcessChildWorkOrdersRule;
/*     */ import nc.impl.ewm.workorder.rule.ReWriteLinkedStockOutAfterInsert;
/*     */ import nc.impl.ewm.workorder.rule.ReWriteLinkedStockOutAfterUpdate;
/*     */ import nc.impl.ewm.workorder.rule.ReWriteLinkedStockOutWhenDelete;
/*     */ import nc.impl.ewm.workorder.rule.ReWritePlanCostAfterDel;
/*     */ import nc.impl.ewm.workorder.rule.ReWritePlanCostAfterInsert;
/*     */ import nc.impl.ewm.workorder.rule.ReWritePlanCostAfterUpdate;
/*     */ import nc.impl.ewm.workorder.rule.ReWriteRequirPlanReqCutDownNumRule;
/*     */ import nc.impl.ewm.workorder.rule.SetDefaultValueBeforeInsert;
/*     */ import nc.impl.ewm.workorder.rule.StartWorkFlowAfterPushSaveRule;
/*     */ import nc.impl.ewm.workorder.rule.UpdateATPAfterRule;
/*     */ import nc.impl.ewm.workorder.rule.UpdateATPBeforeRule;
/*     */ import nc.impl.ewm.workorder.rule.UpdateChildFlagOfParentRule;
/*     */ import nc.impl.ewm.workorder.rule.UpdateReserveRule;
/*     */ import nc.impl.ewm.workorder.rule.UpdateSetInnerCodeAfterPersist;
/*     */ import nc.impl.ewm.workorder.rule.WOAddAuditInfoBeforeRule;
/*     */ import nc.impl.ewm.workorder.rule.WriteBackPermitWhenUpdate;
/*     */ import nc.impl.ewm.workorder.rule.WriteBackSourceWhenDelete;
/*     */ import nc.impl.ewm.workorder.rule.WriteBackSourceWhenInsert;
/*     */ import nc.impl.ewm.workorder.validator.InsertBillValidator;
/*     */ import nc.impl.ewm.workorder.validator.UpdateBillValidator;
/*     */ import nc.impl.ewm.workorder.validator.WorkOrderSrcInfoAdapterFactory;
/*     */ import nc.impl.ewm.workorder.workflow.AlterStatusImpl;
/*     */ import nc.impl.ewm.wouseouter.WorkOrderUsePUImpl;
/*     */ import nc.itf.am.prv.IDataService;
/*     */ import nc.itf.am.pub.IFailurereasonService;
/*     */ import nc.itf.am.pub.IWoStatusPubService;
/*     */ import nc.itf.ewm.prv.IWorkOrderService;
/*     */ import nc.pubitf.uapbd.IMaterialPubService;
/*     */ import nc.vo.aim.equip.EquipHeadVO;
/*     */ import nc.vo.am.common.BaseLockData;
/*     */ import nc.vo.am.common.TransportBillVO;
/*     */ import nc.vo.am.common.util.ArrayUtils;
/*     */ import nc.vo.am.common.util.BaseVOUtils;
/*     */ import nc.vo.am.common.util.BillTransportTool;
/*     */ import nc.vo.am.common.util.ExceptionUtils;
/*     */ import nc.vo.am.common.util.OrgUtils;
/*     */ import nc.vo.am.common.util.SqlBuilder;
/*     */ import nc.vo.am.manager.LockManager;
/*     */ import nc.vo.am.proxy.AMProxy;
/*     */ import nc.vo.am.pub.uap.ModuleInfoQuery;
/*     */ import nc.vo.bd.material.stock.MaterialStockVO;
/*     */ import nc.vo.ewm.workorder.AggWorkOrderVO;
/*     */ import nc.vo.ewm.workorder.WOHisVO;
/*     */ import nc.vo.ewm.workorder.WOPermitVO;
/*     */ import nc.vo.ewm.workorder.WOPlanInVVO;
/*     */ import nc.vo.ewm.workorder.WOTaskObjVO;
/*     */ import nc.vo.ewm.workorder.WorkOrderHeadVO;
/*     */ import nc.vo.ewm.workorder.utils.TransiTypeUtils;
/*     */ import nc.vo.jcom.lang.StringUtil;
/*     */ import nc.vo.ml.AbstractNCLangRes;
/*     */ import nc.vo.ml.NCLangRes4VoTransl;
/*     */ import nc.vo.org.DeptVO;
/*     */ import nc.vo.org.MaintainOrgVO;
/*     */ import nc.vo.pub.AggregatedValueObject;
/*     */ import nc.vo.pub.BusinessException;
/*     */ import nc.vo.pub.ISuperVO;
/*     */ import nc.vo.pub.SuperVO;
/*     */ import nc.vo.pub.lang.UFBoolean;
/*     */ import nc.vo.uif2.LoginContext;
/*     */ import nc.vo.util.BDVersionValidationUtil;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class WorkOrderBaseImpl
/*     */   extends BillBaseDAO<AggWorkOrderVO>
/*     */   implements IWorkOrderService
/*     */ {
/*     */   private void lockAndCheckTs(AggWorkOrderVO... billVOs)
/*     */     throws BusinessException
/*     */   {
/* 106 */     BaseLockData<AggWorkOrderVO> lockdata = new BaseLockData(billVOs);
/* 107 */     LockManager.lock(lockdata, new String[] { "ts" }, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Object updateWorkOrder(AggWorkOrderVO billVO)
/*     */     throws BusinessException
/*     */   {
/* 115 */     lockAndCheckTs(new AggWorkOrderVO[] { billVO });
/*     */     
/* 117 */     AggWorkOrderVO[] billVOs = (AggWorkOrderVO[])updateBillVO(new AggWorkOrderVO[] { billVO }, null);
/* 118 */     return billVOs[0];
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private SqlBuilder checkBeforeDelBill(AggWorkOrderVO[] billVOs)
/*     */     throws BusinessException
/*     */   {
/* 128 */     SqlBuilder sbErrMsg = new SqlBuilder();
/*     */     
/* 130 */     AggregatedValueObject[] pubills = WorkOrderUsePUImpl.qryPUPrayBillBySrcHeadid(billVOs);
/* 131 */     if (!ArrayUtils.isEmpty(pubills)) {
/* 132 */       sbErrMsg.append(NCLangRes4VoTransl.getNCLangRes().getStrByID("workorder_0", "04560003-0306"));
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 139 */     SqlBuilder sExistLatter = checkExistLatterWO(billVOs);
/* 140 */     sbErrMsg.append(sExistLatter);
/*     */     
/* 142 */     SqlBuilder sExistchild = checkExistChildWO(billVOs);
/* 143 */     sbErrMsg.append(sExistchild);
/*     */     
/*     */ 
/* 146 */     SqlBuilder sExisetAllocationDo = checkIsAllocationDoing(billVOs);
/* 147 */     sbErrMsg.append(sExisetAllocationDo);
/*     */     
/*     */ 
/* 150 */     String sExistPermit = checkExistPermit(billVOs);
/*     */     
/* 152 */     if (sExistPermit != null) {
/* 153 */       sbErrMsg.append(sExistPermit);
/*     */     }
/* 155 */     sbErrMsg.append(checkExistInvPlan(billVOs));
/* 156 */     return sbErrMsg;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private SqlBuilder checkIsAllocationDoing(AggWorkOrderVO[] billVOs)
/*     */   {
/* 165 */     SqlBuilder sbMsg = new SqlBuilder();
/* 166 */     for (int i = 0; i < billVOs.length; i++) {
/* 167 */       WOTaskObjVO[] woTaskObjVOs = (WOTaskObjVO[])billVOs[i].getChildren(WOTaskObjVO.class);
/* 168 */       if (!ArrayUtils.isEmpty(woTaskObjVOs))
/*     */       {
/*     */ 
/* 171 */         for (WOTaskObjVO woTaskObjVO : woTaskObjVOs) {
/* 172 */           Integer allocationstatus = woTaskObjVO.getAllocationstatus();
/* 173 */           if ((allocationstatus != null) && ((2 == allocationstatus.intValue()) || (3 == allocationstatus.intValue())))
/*     */           {
/* 175 */             sbMsg.append(NCLangRes4VoTransl.getNCLangRes().getStrByID("workorder_0", "04560003-0571"));
/* 176 */             break;
/*     */           }
/*     */         } }
/*     */     }
/* 180 */     return sbMsg;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private String checkExistPermit(AggWorkOrderVO[] billVOs)
/*     */   {
/* 190 */     for (AggWorkOrderVO billVO : billVOs) {
/* 191 */       ISuperVO[] permitVOs = billVO.getChildren(WOPermitVO.class);
/* 192 */       if ((permitVOs != null) && (permitVOs.length > 0)) {
/* 193 */         return NCLangRes4VoTransl.getNCLangRes().getStrByID("workorder_0", "04560003-0523");
/*     */       }
/*     */     }
/* 196 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private SqlBuilder checkExistInvPlan(AggWorkOrderVO[] billVOs)
/*     */     throws BusinessException
/*     */   {
/* 207 */     SqlBuilder sbMsg = new SqlBuilder();
/* 208 */     for (int i = 0; i < billVOs.length; i++) {
/* 209 */       WOPlanInVVO[] planInvVOs = (WOPlanInVVO[])billVOs[i].getChildren(WOPlanInVVO.class);
/* 210 */       if (!ArrayUtils.isEmpty(planInvVOs))
/*     */       {
/*     */ 
/* 213 */         for (WOPlanInVVO planInvVO : planInvVOs) {
/* 214 */           String srcBillid = planInvVO.getSrc_pk_bill();
/* 215 */           if (srcBillid != null) {
/* 216 */             sbMsg.append(NCLangRes4VoTransl.getNCLangRes().getStrByID("workorder_0", "04560003-0307"));
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 224 */     return sbMsg;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private SqlBuilder checkExistChildWO(AggWorkOrderVO[] billVOs)
/*     */     throws BusinessException
/*     */   {
/* 236 */     SqlBuilder sbMsg = new SqlBuilder();
/* 237 */     ArrayList<String> alheadid = new ArrayList();
/* 238 */     for (int i = 0; i < billVOs.length; i++) {
/* 239 */       alheadid.add(billVOs[i].getPrimaryKey());
/*     */     }
/* 241 */     SqlBuilder sbSql = new SqlBuilder();
/* 242 */     sbSql.append("pk_parent_wo");
/* 243 */     sbSql.append(" in ");
/* 244 */     sbSql.append(InSqlManager.getInSQLValue(alheadid));
/*     */     
/* 246 */     WorkOrderHeadVO[] childrenVOs = (WorkOrderHeadVO[])QueryUtil.queryVOByCond(WorkOrderHeadVO.class, sbSql.toString(), null);
/* 247 */     if (ArrayUtils.isNotEmpty(childrenVOs)) {
/* 248 */       sbMsg.append(NCLangRes4VoTransl.getNCLangRes().getStrByID("workorder_0", "04560003-0308"));
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 254 */     return sbMsg;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private SqlBuilder checkExistLatterWO(AggWorkOrderVO[] billVOs)
/*     */     throws BusinessException
/*     */   {
/* 266 */     SqlBuilder sbMsg = new SqlBuilder();
/* 267 */     for (int i = 0; i < billVOs.length; i++) {
/* 268 */       WorkOrderHeadVO head = billVOs[i].getParentVO();
/* 269 */       if (head.getHaslatter_flag().booleanValue()) {
/* 270 */         sbMsg.append(NCLangRes4VoTransl.getNCLangRes().getStrByID("workorder_0", "04560003-0288"));
/*     */         
/*     */ 
/*     */ 
/*     */ 
/* 275 */         break;
/*     */       }
/*     */     }
/*     */     
/* 279 */     return sbMsg;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void initInsertActionBeforeSave(IActionTemplate<AggWorkOrderVO> insertAction)
/*     */   {
/* 288 */     SrcBillConsistenceValidator srcBillConsistenceValidator = new SrcBillConsistenceValidator();
/* 289 */     srcBillConsistenceValidator.setSrcInfoAdapterFactory(new WorkOrderSrcInfoAdapterFactory());
/* 290 */     insertAction.addBeforeRule(new ValidateServiceRule(new Validator[] { srcBillConsistenceValidator }));
/*     */     
/* 292 */     insertAction.addBeforeRule(new SetDefaultValueBeforeInsert());
/*     */     
/* 294 */     insertAction.addBeforeRule(new ComputerCostBeforePersist());
/*     */     
/* 296 */     insertAction.addBeforeRule(new ValidateServiceRule(new Validator[] { new InsertBillValidator() }));
/*     */     
/* 298 */     insertAction.addAfterRule(new UpdateSetInnerCodeAfterPersist());
/*     */     
/* 300 */     insertAction.addAfterRule(new WriteBackSourceWhenInsert());
/*     */     
/* 302 */     insertAction.addAfterRule(new UpdateChildFlagOfParentRule());
/*     */     
/* 304 */     insertAction.addAfterRule(new DecompressInRoadInfoRule());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void initInsertActionRules(IActionTemplate<AggWorkOrderVO> insertAction)
/*     */   {
/* 312 */     insertAction.addBeforeRule(new ProcessChildWorkOrdersRule());
/* 313 */     super.initInsertActionRules(insertAction);
/*     */     
/* 315 */     insertAction.addBeforeRule(new BillCodeInsertBeforeRule());
/*     */     
/* 317 */     insertAction.addBeforeRule(new AppendBusiTypeBeforeRule());
/*     */     
/* 319 */     insertAction.addAfterRule(new BillCodeInsertAfterRule());
/*     */     
/* 321 */     insertAction.addAfterRule(new ReWritePlanCostAfterInsert());
/*     */     
/* 323 */     insertAction.addAfterRule(new ReWriteLinkedStockOutAfterInsert());
/*     */     
/* 325 */     insertAction.addAfterRule(new FailureReasonSaveAfterRule());
/* 326 */     initInsertActionBeforeSave(insertAction);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void initPushSaveActionRules(IActionTemplate<AggWorkOrderVO> template)
/*     */   {
/* 337 */     template.addBeforeRule(new VoStatusSetBeforeRule(2));
/*     */     
/* 339 */     template.addBeforeRule(new WOAddAuditInfoBeforeRule());
/*     */     
/* 341 */     template.addBeforeRule(new SetOrgVidBeforeRule());
/*     */     
/* 343 */     template.addBeforeRule(new SetBizScopeInfoBeforeRule());
/*     */     
/* 345 */     template.addBeforeRule(new BillCodePushSaveBeforeRule());
/*     */     
/* 347 */     template.addBeforeRule(new AppendTransiOrBusiTypeRule());
/*     */     
/* 349 */     initInsertActionBeforeSave(template);
/*     */     
/* 351 */     template.addAfterRule(new ReWritePlanCostAfterInsert());
/* 352 */     template.addAfterRule(new StartWorkFlowAfterPushSaveRule());
/*     */     
/* 354 */     template.addAfterRule(new FailureReasonSaveAfterRule());
/*     */   }
/*     */   
/*     */   protected void initDeleteActionRules(IActionTemplate<AggWorkOrderVO> template)
/*     */   {
/* 359 */     super.initDeleteActionRules(template);
/*     */     
/* 361 */     template.addAfterRule(new WriteBackSourceWhenDelete());
/* 362 */     template.addAfterRule(new BillCodeDeleteRule());
/* 363 */     template.addAfterRule(new UpdateChildFlagOfParentRule());
/* 364 */     template.addAfterRule(new ReWritePlanCostAfterDel());
/* 365 */     template.addAfterRule(new ReWriteLinkedStockOutWhenDelete());
/*     */     
/* 367 */     template.addAfterRule(new FailureReasonSaveAfterRule());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void initUpdateActionRules(IActionTemplate<AggWorkOrderVO> template)
/*     */   {
/* 377 */     template.addBeforeRule(new ProcessChildWorkOrdersRule());
/* 378 */     super.initUpdateActionRules(template);
/*     */     
/* 380 */     template.addBeforeRule(new AppendBusiTypeBeforeRule());
/* 381 */     template.addBeforeRule(new ValidateServiceRule(new Validator[] { new UpdateBillValidator() }));
/*     */     
/* 383 */     template.addBeforeRule(new ComputerCostBeforePersist());
/* 384 */     template.addBeforeRule(new UpdateReserveRule());
/* 385 */     template.addBeforeRule(new UpdateATPBeforeRule());
/* 386 */     template.addBeforeRule(new WriteBackPermitWhenUpdate());
/*     */     
/* 388 */     template.addAfterRule(new UpdateSetInnerCodeAfterPersist());
/*     */     
/* 390 */     template.addAfterRule(new UpdateChildFlagOfParentRule());
/* 391 */     template.addAfterRule(new DecompressInRoadInfoRule());
/* 392 */     template.addAfterRule(new UpdateATPAfterRule());
/* 393 */     template.addAfterRule(new ReWriteLinkedStockOutAfterUpdate());
/* 394 */     template.addAfterRule(new ReWritePlanCostAfterUpdate());
/* 395 */     template.addAfterRule(new ReWriteRequirPlanReqCutDownNumRule());
/*     */     
/* 397 */     template.addAfterRule(new FailureReasonSaveAfterRule());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Object updateWorkOrderStatus(AggWorkOrderVO workOrderVO, WOHisVO statusVO, boolean isRoot)
/*     */     throws BusinessException
/*     */   {
/* 406 */     lockAndCheckTs(new AggWorkOrderVO[] { workOrderVO });
/*     */     
/* 408 */     AggWorkOrderVO[] resVos = (AggWorkOrderVO[])QueryUtil.queryBillVOByPks(AggWorkOrderVO.class, new String[] { workOrderVO.getPrimaryKey() }, true);
/*     */     
/* 410 */     workOrderVO = resVos[0];
/*     */     
/* 412 */     workOrderVO = new AlterStatusImpl().alterWorkOrderStatus(workOrderVO, statusVO, isRoot);
/*     */     
/* 414 */     Map<String, String[]> bodyFieldsMap = new HashMap();
/* 415 */     bodyFieldsMap.put("wo_plan_inv", new String[] { "nreqrsnum" });
/* 416 */     TransportBillVO transportBillVO = BillTransportTool.createTransportBill(workOrderVO, new String[] { "wo_statustype", "pk_wo_status", "status_time", "actu_start_time", "actu_end_time", "plan_start_time", "plan_end_time" }, bodyFieldsMap, true);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 421 */     return transportBillVO;
/*     */   }
/*     */   
/*     */   public void deleteWorkOrders(AggWorkOrderVO[] billVOs) throws BusinessException
/*     */   {
/* 426 */     lockAndCheckTs(billVOs);
/* 427 */     AggWorkOrderVO[] billVOsDel = new MakeUpDataImpl().makeUpWOBeforeDelete(billVOs);
/*     */     
/* 429 */     SqlBuilder sErrMsg = checkBeforeDelBill(billVOsDel);
/* 430 */     if (sErrMsg.length() > 0) {
/* 431 */       throw new BusinessException(sErrMsg.toString());
/*     */     }
/*     */     
/* 434 */     deleteBillVO(billVOsDel);
/*     */   }
/*     */   
/*     */   public void checkBodyVOExisted(AggWorkOrderVO[] billVOs) throws BusinessException
/*     */   {
/* 439 */     lockAndCheckTs(billVOs);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Object insertWorkOrder(AggregatedValueObject vo)
/*     */     throws BusinessException
/*     */   {
/* 447 */     AggWorkOrderVO billVO = ((AggWorkOrderVO[])super.insertBillVO(new AggWorkOrderVO[] { (AggWorkOrderVO)vo }))[0];
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 478 */     return billVO;
/*     */   }
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
/*     */   public List<String> queryTransType(String billtype)
/*     */   {
/* 564 */     StringBuffer sbSql = new StringBuffer("select pk_billtypecode from bd_billtype where istransaction = 'Y'  and parentbilltype = '");
/*     */     
/* 566 */     sbSql.append(billtype);
/* 567 */     sbSql.append("'");
/* 568 */     DBAccessUtil dbaccess = new DBAccessUtil();
/* 569 */     List<String> res = null;
/*     */     try {
/* 571 */       res = dbaccess.querySingleColumn(sbSql.toString());
/*     */     } catch (BusinessException e) {
/* 573 */       Logger.error(e.getMessage(), e);
/* 574 */       ExceptionUtils.asBusinessRuntimeException(e);
/*     */     }
/* 576 */     return res;
/*     */   }
/*     */   
/*     */   public Object woCapital(AggWorkOrderVO billVO, Object operatorid)
/*     */     throws BusinessException
/*     */   {
/* 582 */     if (!ModuleInfoQuery.isFAEnabled()) {
/* 583 */       throw new BusinessException(NCLangRes4VoTransl.getNCLangRes().getStrByID("workorder_0", "04560003-0309"));
/*     */     }
/*     */     
/*     */ 
/* 587 */     lockAndCheckTs(new AggWorkOrderVO[] { billVO });
/* 588 */     Object oret = new MakeFaBillImpl().sendMsgToFA(billVO, operatorid);
/* 589 */     return oret;
/*     */   }
/*     */   
/*     */   public Object woCapitalCancel(AggWorkOrderVO billVO) throws BusinessException
/*     */   {
/* 594 */     lockAndCheckTs(new AggWorkOrderVO[] { billVO });
/* 595 */     Object oret = new MakeFaBillImpl().cancelSendToFA(billVO);
/* 596 */     return oret;
/*     */   }
/*     */   
/*     */ 
/*     */   public WOPlanInVVO[] filterRemainable(WOPlanInVVO[] planVOs)
/*     */     throws BusinessException
/*     */   {
/* 603 */     if ((planVOs != null) && (planVOs.length > 0))
/*     */     {
/* 605 */       IMaterialPubService materialService = (IMaterialPubService)AMProxy.lookup(IMaterialPubService.class);
/*     */       
/* 607 */       Map<List<Object>, List<WOPlanInVVO>> voMap = BaseVOUtils.groupingVOToMap(planVOs, new String[] { "pk_stockorg" });
/*     */       
/*     */ 
/* 610 */       List<WOPlanInVVO> remainableList = new ArrayList();
/* 611 */       for (Map.Entry<List<Object>, List<WOPlanInVVO>> entry : voMap.entrySet()) {
/* 612 */         String stockOrgId = (String)((List)entry.getKey()).get(0);
/* 613 */         String[] materialIds = new String[((List)entry.getValue()).size()];
/* 614 */         if (stockOrgId != null) {
/* 615 */           for (int i = 0; i < ((List)entry.getValue()).size(); i++) {
/* 616 */             materialIds[i] = ((WOPlanInVVO)((List)entry.getValue()).get(i)).getPk_material_v();
/*     */           }
/* 618 */            Map<String, MaterialStockVO> stockInfoMap = materialService.queryMaterialStockInfoByPks(materialIds, stockOrgId, new String[] { "remain" });
/*     */           
/* 620 */           for (WOPlanInVVO planVO : entry.getValue()) {
/* 621 */             String materialId = planVO.getPk_material_v();
/* 622 */             MaterialStockVO stockInfo = (MaterialStockVO)stockInfoMap.get(materialId);
/* 623 */             if ((stockInfo != null) && (stockInfo.getRemain() != null) && (stockInfo.getRemain().booleanValue()))
/* 624 */               remainableList.add(planVO);
/*     */           }
/*     */         }
/*     */       }
/*     */       Map<String, MaterialStockVO> stockInfoMap;
/* 629 */       return (WOPlanInVVO[])remainableList.toArray(new WOPlanInVVO[remainableList.size()]);
/*     */     }
/* 631 */     return null;
/*     */   }
/*     */   
/*     */   public Object[] getDefaultWoStatus(String transType, LoginContext context)
/*     */     throws BusinessException
/*     */   {
/* 637 */     boolean isAfterWard = TransiTypeUtils.isAfterward(transType);
/* 638 */     int defaultStatusType = 0;
/*     */     
/* 640 */     if (isAfterWard) {
/* 641 */       defaultStatusType = 2;
/*     */     } else {
/* 643 */       defaultStatusType = 0;
/*     */     }
/*     */     
/* 646 */     String defaultWoStatusId = ((IWoStatusPubService)AMProxy.lookup(IWoStatusPubService.class)).getWoStatusPKByWOType(defaultStatusType, context);
/*     */     
/* 648 */     return new Object[] { Integer.valueOf(defaultStatusType), defaultWoStatusId };
/*     */   }
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
/*     */   public AggWorkOrderVO[] queryAllLevelChilrenWOByInnerCode(String innercode)
/*     */     throws BusinessException
/*     */   {
/* 664 */     if (StringUtil.isEmpty(innercode)) {
/* 665 */       return null;
/*     */     }
/* 667 */     String condition = " innercode like '" + innercode + "_%' ";
/* 668 */     String order = " order by bill_code ";
/* 669 */     AggWorkOrderVO[] childWOs = (AggWorkOrderVO[])QueryUtil.queryBillVOByHeadCond(AggWorkOrderVO.class, condition, order, true);
/* 670 */     return childWOs;
/*     */   }
/*     */   
/*     */   protected UpdateActionTemplate<AggWorkOrderVO> getUpdateActionTemplate(AggWorkOrderVO[] billVOs)
/*     */   {
/* 675 */     return new UpdateActionWorkOrder(billVOs);
/*     */   }
/*     */   
/*     */   protected IActionTemplate<AggWorkOrderVO> getInsertActionTemplate(AggWorkOrderVO... billVOs)
/*     */   {
/* 680 */     return new InsertActionWorkOrder(billVOs);
/*     */   }
/*     */   
/*     */   public void WOLockValidate(AggWorkOrderVO billVO)
/*     */     throws BusinessException
/*     */   {
/* 686 */     LockManager.lockAggVO(new AggregatedValueObject[] { billVO });
/* 687 */     BDVersionValidationUtil.validateSuperVO(new SuperVO[] { billVO.getParentVO() });
/* 688 */     WOPlanInVVO[] bodyVOs = (WOPlanInVVO[])billVO.getChildren(WOPlanInVVO.class);
/* 689 */     if ((bodyVOs != null) && (bodyVOs.length > 0)) {
/* 690 */       BDVersionValidationUtil.validateSuperVO(bodyVOs);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Object[] queryObjectByPks(String[] pks)
/*     */     throws BusinessException
/*     */   {
/* 703 */     AggWorkOrderVO[] aggWorkApplyVOs = (AggWorkOrderVO[])((IDataService)AMProxy.lookup(IDataService.class)).queryObjectByPks(AggWorkOrderVO.class, pks, "bodyvos");
/*     */     
/* 705 */     return dealFailurereasonName(aggWorkApplyVOs);
/*     */   }
/*     */   
/* 708 */   private AggWorkOrderVO[] dealFailurereasonName(AggWorkOrderVO... workOrderVO) throws BusinessException { List<String> pk_woList = new ArrayList();
/* 709 */     for (AggWorkOrderVO vo : workOrderVO) {
/* 710 */       pk_woList.add(vo.getParentVO().getPrimaryKey());
/*     */     }
/* 712 */     if (pk_woList.size() > 0)
/*     */     {
/* 714 */       Map<String, List<String>> reasonInfoMap = ((IFailurereasonService)AMProxy.lookup(IFailurereasonService.class)).queryMultiReasonMap((String[])pk_woList.toArray(new String[0]));
/* 715 */       if ((reasonInfoMap != null) && (reasonInfoMap.size() > 0)) {
/* 716 */         for (AggWorkOrderVO vo : workOrderVO) {
/* 717 */           WorkOrderHeadVO headVO = vo.getParentVO();
/* 718 */           if ((reasonInfoMap.get(headVO.getPk_wo()) != null) && (((List)reasonInfoMap.get(headVO.getPk_wo())).size() == 2)) {
/* 719 */             headVO.setFailure_reason_name((String)((List)reasonInfoMap.get(headVO.getPk_wo())).get(1));
/* 720 */             headVO.setPk_failure_reason((String)((List)reasonInfoMap.get(headVO.getPk_wo())).get(0));
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 725 */     return workOrderVO;
/*     */   }
/*     */   
/*     */   public AggWorkOrderVO makeUpCopyVOInfo(AggWorkOrderVO aggWorkOrderVO) throws BusinessException
/*     */   {
/* 730 */     WorkOrderHeadVO headVO = aggWorkOrderVO.getParentVO();
/* 731 */     String pk_org = headVO.getPk_org();
/* 732 */     String pk_equip = headVO.getPk_equip();
/* 733 */     String pk_wo_dept = headVO.getPk_wo_dept();
/* 734 */     EquipHeadVO[] equipHeadVO = pk_equip == null ? null : (EquipHeadVO[])QueryUtil.querySuperVOByPks(EquipHeadVO.class, new String[] { pk_equip });
/* 735 */     DeptVO[] deptVO = pk_wo_dept == null ? null : (DeptVO[])DBAccessUtil.querySuperVosByIds(DeptVO.class, new String[] { pk_wo_dept });
/*     */     
/* 737 */     if ((deptVO != null) && (deptVO.length > 0)) {
/* 738 */       headVO.setPk_wo_dept_v(deptVO[0].getPk_vid());
/*     */     }
/*     */     
/* 741 */     if ((equipHeadVO != null) && (equipHeadVO.length > 0)) {
/* 742 */       String pk_location = equipHeadVO[0].getPk_location();
/* 743 */       if ((pk_location != null) && (!pk_location.equals(headVO.getPk_location()))) {
/* 744 */         headVO.setPk_location(pk_location);
/*     */       }
/* 746 */       headVO.setPk_usedunit(equipHeadVO[0].getPk_usedunit());
/* 747 */       headVO.setPk_usedept(equipHeadVO[0].getPk_usedept());
/* 748 */       headVO.setPk_user(equipHeadVO[0].getPk_user());
/* 749 */       headVO.setPk_ownerorg(equipHeadVO[0].getPk_ownerorg());
/* 750 */       headVO.setPk_mandept(equipHeadVO[0].getPk_mandept());
/* 751 */       headVO.setPk_usedorg(equipHeadVO[0].getPk_usedorg());
/* 752 */       headVO.setPk_manager(equipHeadVO[0].getPk_manager());
/* 753 */       headVO.setPk_fiorg(equipHeadVO[0].getPk_fiorg());
/*     */     }
/*     */     
/*     */ 
/* 757 */     MaintainOrgVO[] maintainOrgVO = pk_org == null ? null : (MaintainOrgVO[])DBAccessUtil.querySuperVosByIds(MaintainOrgVO.class, new String[] { pk_org });
/* 758 */     List<String> idList = new ArrayList();
/* 759 */     if (pk_org != null) {
/* 760 */       idList.add(pk_org);
/*     */     }
/* 762 */     String financeOrgID = null;
/* 763 */     String liaCenterID = null;
/* 764 */     String financeOrgID_V = null;
/* 765 */     String liaCenterID_V = null;
/* 766 */     String pk_org_v = null;
/* 767 */     if ((maintainOrgVO != null) && (maintainOrgVO.length > 0)) {
/* 768 */       financeOrgID = maintainOrgVO[0].getPk_financeorg();
/* 769 */       liaCenterID = maintainOrgVO[0].getPk_liacenter();
/*     */       
/* 771 */       if (financeOrgID != null) {
/* 772 */         idList.add(financeOrgID);
/*     */       }
/* 774 */       if (liaCenterID != null) {
/* 775 */         idList.add(liaCenterID);
/*     */       }
/*     */     }
/* 778 */     Map<String, String> vidMap = idList.size() == 0 ? null : OrgUtils.getVersionIdsByOids((String[])idList.toArray(new String[0]));
/* 779 */     if (vidMap != null) {
/* 780 */       financeOrgID_V = (String)vidMap.get(financeOrgID);
/* 781 */       liaCenterID_V = (String)vidMap.get(liaCenterID);
/*     */     }
/*     */     
/* 784 */     pk_org_v = (String)vidMap.get(pk_org);
/* 785 */     headVO.setPk_org_v(pk_org_v);
/*     */     
/* 787 */     headVO.setPk_fiorg_ap(financeOrgID);
/* 788 */     headVO.setPk_fiorg_ap_v(financeOrgID_V);
/*     */     
/* 790 */     headVO.setPk_fiorg_armt(financeOrgID);
/* 791 */     headVO.setPk_fiorg_armt_v(financeOrgID_V);
/*     */     
/* 793 */     headVO.setPk_fiorg_apmt(financeOrgID);
/* 794 */     headVO.setPk_fiorg_apmt_v(financeOrgID_V);
/*     */     
/* 796 */     headVO.setPk_fiorg_ic(financeOrgID);
/* 797 */     headVO.setPk_fiorg_ic_v(financeOrgID_V);
/*     */     
/* 799 */     headVO.setPk_profitcen_ap(liaCenterID);
/* 800 */     headVO.setPk_profitcen_ap_v(liaCenterID_V);
/* 801 */     headVO.setPk_profitcen_armt(liaCenterID);
/* 802 */     headVO.setPk_profitcen_armt_v(liaCenterID_V);
/* 803 */     headVO.setPk_profitcen_apmt(liaCenterID);
/* 804 */     headVO.setPk_profitcen_apmt_v(liaCenterID_V);
/*     */     
/* 806 */     return aggWorkOrderVO;
/*     */   }

		public AggWorkOrderVO updateWorkOrderHeadVO(AggWorkOrderVO[] paramAggWorkOrderVOs)
	    throws BusinessException{
			if(paramAggWorkOrderVOs == null || paramAggWorkOrderVOs.length==0)
				return null;
			
			List<WorkOrderHeadVO> list = new ArrayList<>();
			for(AggWorkOrderVO aggvo: paramAggWorkOrderVOs){
				list.add(aggvo.getParentVO());
			}
			HYPubBO bo = new HYPubBO();
			bo.updateAry(list.toArray(new WorkOrderHeadVO[list.size()]));
			return null;
		}
/*     */ }

