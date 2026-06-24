/*     */ package nc.ui.ewm.workorder.tabaction;
/*     */ 
/*     */ import nc.ui.am.action.support.AMAddLineAction;
/*     */ import nc.ui.am.editor.AMBillForm;
/*     */ import nc.ui.am.model.BillManageModel;
/*     */ import nc.ui.am.util.RownoClientUtils;
/*     */ import nc.ui.ewm.param.query.WorkOrderParam;
/*     */ import nc.ui.ewm.workorder.utils.WorkOrderBodyCostUtils;
/*     */ import nc.ui.ewm.workorder.view.WorkOrderEditableUtil;
/*     */ import nc.ui.ewm.workorder.view.WorkOrderViewUtil;
/*     */ import nc.ui.pub.bill.BillCardPanel;
/*     */ import nc.ui.pub.bill.BillModel;
import nc.ui.trade.business.HYPubBO_Client;
import nc.uif.pub.exception.UifException;
/*     */ import nc.vo.am.common.BizContext;
/*     */ import nc.vo.logging.Debug;
/*     */ import nc.vo.ml.AbstractNCLangRes;
/*     */ import nc.vo.ml.NCLangRes4VoTransl;
/*     */ import nc.vo.uif2.LoginContext;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LineAddAction
/*     */   extends AMAddLineAction
/*     */ {
/*     */   private String tabcode;
/*     */   
/*     */   protected void setDefaultValue()
/*     */   {
/*  34 */     super.setDefaultValue();
/*  35 */     if (getTabcode() == null) {
/*  36 */       return;
/*     */     }
/*  38 */     if (("wo_taskobj".equals(getTabcode())) || ("wo_task".equals(getTabcode())))
/*     */     {
/*     */ 
/*  41 */       RownoClientUtils.whenAddLineVarStep(getBillForm().getBillCardPanel(), "sequence_num", 1);
/*     */     }
/*     */     
/*  44 */     if ("wo_plan_inv".equals(getTabcode())) {
/*  45 */       Object objRequireDate = WorkOrderViewUtil.getPlanInvDefaultRequireDate(getBillForm());
/*     */       
/*  47 */       setDefaultValueWhenAddLine(getBillForm().getBillCardPanel(), "required_date", objRequireDate, 1, getTabcode());
/*     */ 
/*     */ 
/*     */ 
/*     */     }
/*  52 */     else if ("wo_actual_inv".equals(getTabcode())) {
/*  53 */       setDefaultValueWhenAddLine(getBillForm().getBillCardPanel(), "inout_date", BizContext.getInstance().getBizDate(), 1, getTabcode());
/*     */     }
/*  55 */     else if ("wo_part".equals(getTabcode())) {
/*  56 */       setDefaultValueWhenAddLine(getBillForm().getBillCardPanel(), "replacedate", BizContext.getInstance().getBizDate(), 1, getTabcode());
/*     */     }
/*  58 */     else if (getTabcode().equals("wo_log"))
/*     */     {
/*  60 */       setDefaultValueWhenAddLine(getBillForm().getBillCardPanel(), "pk_recorder", getModel().getContext().getPk_loginUser(), 1, getTabcode());
/*     */       
/*  62 */       getBillForm().getBillCardPanel().getBillModel(getTabcode()).loadLoadRelationItemValue();
/*  63 */       setDefaultValueWhenAddLine(getBillForm().getBillCardPanel(), "rec_time", BizContext.getInstance().getBizDate(), 1, getTabcode());
/*     */     }else if("wo_actual_psn".equals(getTabcode())){
				Object pk_org = getEditedCardPanel().getHeadItem("pk_org").getValueObject();
				Object o = null;
				try {
					o = HYPubBO_Client.findColValue("pub_sysinit", "value",
							" nvl(dr,0)= 0 and pk_org = '" + pk_org + "' and initcode = 'YF603'");
				} catch (UifException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
				setDefaultValueWhenAddLine(getBillForm().getBillCardPanel(), "rate",o, 1, getTabcode());
			}
/*     */     
/*     */ 
/*  67 */     if ((!"wo_taskobj".equals(getTabcode())) && (!"wo_task".equals(getTabcode())) && (!getTabcode().equals("wo_log")))
/*     */     {
/*     */ 
/*  70 */       WorkOrderBodyCostUtils.headTotalCostBody(getBillForm().getBillCardPanel());
/*     */     }
/*     */   }
/*     */   
/*     */   protected boolean isActionEnable()
/*     */   {
/*  76 */     if (super.isActionEnable()) {
/*  77 */       return (WorkOrderEditableUtil.isBodyTabEnable(getBillForm(), null)) && (WorkOrderParam.getGroupParamBEditTask(getBillForm(), null));
/*     */     }
/*     */     
/*  80 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void setDefaultValueWhenAddLine(BillCardPanel billCardPanel, String key, Object value, int count, String tab)
/*     */   {
/*  91 */     int rowCount = billCardPanel.getRowCount();
/*  92 */     int[] givenRows = new int[count];
/*  93 */     int preRow = rowCount - count - 1;
/*  94 */     for (int i = 0; i < count; i++) {
/*  95 */       givenRows[i] = (preRow + i + 1);
/*     */     }
/*     */     
/*  98 */     if ((billCardPanel == null) || (key == null) || (billCardPanel.getBodyItem(key) == null) || (givenRows == null)) {
/*  99 */       Debug.debug(NCLangRes4VoTransl.getNCLangRes().getStrByID("workorder_0", "04560003-0162"));
/*     */       
/*     */ 
/*     */ 
/* 103 */       return;
/*     */     }
/* 105 */     int length = givenRows.length;
/* 106 */     for (int i = 0; i < length; i++)
/*     */     {
/* 108 */       billCardPanel.setBodyValueAt(value, givenRows[i], key, tab);
/*     */     }
/*     */   }
/*     */   
/*     */   public String getTabcode()
/*     */   {
/* 114 */     return tabcode;
/*     */   }
/*     */   
/*     */   public void setTabcode(String tabcode) {
/* 118 */     this.tabcode = tabcode;
/*     */   }
/*     */ }