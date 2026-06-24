/** <a href="http://www.cpupk.com/decompiler">Eclipse Class Decompiler</a> plugin, Copyright (c) 2017 Chen Chao. **/
/*    */ package nc.vo.ewm.workorder;
/*    */ 
/*    */ import nc.vo.am.common.AbstractAggBill;
/*    */ import nc.vo.annotation.AggVoInfo;
/*    */ import nc.vo.pub.SuperVO;
/*    */ import nc.vo.pub.lang.UFBoolean;
/*    */ import nc.vo.pubapp.pattern.model.meta.entity.bill.IBillMeta;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @AggVoInfo(parentVO="nc.vo.ewm.workorder.WorkOrderHeadVO")
/*    */ public class AggWorkOrderVO extends AbstractAggBill
/*    */ {
/*    */   public WorkOrderHeadVO getParentVO()
/*    */   {
/* 27 */     return ((WorkOrderHeadVO)super.getParentVO());
/*    */   }
/*    */ 
/*    */   public SuperVO[] getChildrenVO()
/*    */   {
/* 32 */     IBillMeta billMeta = getMetaData();
/*    */ 
/* 34 */     if (billMeta.getChildren().length == 0) {
/* 35 */       return null;
/*    */     }
/*    */ 	 
/* 38 */     return ((SuperVO[])(SuperVO[])getChildren(WOPlanInVVO.class));
/*    */   }

		  public SuperVO[] getChildrenVO(String s)
/*    */   {
/* 32 */     IBillMeta billMeta = getMetaData();
/*    */ 
/* 34 */     if (billMeta.getChildren().length == 0) {
/* 35 */       return null;
/*    */     }
				/********************20230802 bbt************************************/
				if(s.equals("WOActualInvVO"))
					return ((SuperVO[])(SuperVO[])getChildren(WOActualInvVO.class));
				else
					return ((SuperVO[])(SuperVO[])getChildren(WOPlanInVVO.class));
				/*****************************************************************/
				
/*    */ 	
/*    */   }
/*    */ 
/*    */   public UFBoolean getBICRETBill() {
/* 42 */     return getParentVO().getBICRETBill();
/*    */   }
/*    */ 
/*    */   public void setBICRETBill(UFBoolean isRetbill) {
/* 46 */     getParentVO().setBICRETBill(isRetbill);
/*    */   }
/*    */ }
