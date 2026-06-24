/** <a href="http://www.cpupk.com/decompiler">Eclipse Class Decompiler</a> plugin, Copyright (c) 2017 Chen Chao. **/
/*    */ package nc.ui.pu.m23.action.approve;
/*    */ import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.ui.pubapp.uif2app.AppUiState;
/*    */ import nc.ui.pubapp.uif2app.actions.pflow.CommitScriptAction;
/*    */ import nc.ui.pubapp.uif2app.model.BillManageModel;
import nc.vo.pu.m23.entity.ArriveHeaderVO;
import nc.vo.pu.m23.entity.ArriveItemVO;
/*    */ import nc.vo.pu.m23.entity.ArriveVO;
/*    */ import nc.vo.pu.pub.util.ApproveFlowUtil;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import org.apache.commons.lang.ArrayUtils;
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
/*    */ 
/*    */ public class SendUIAction extends CommitScriptAction
/*    */ {
/*    */   private static final long serialVersionUID = 9076203557899139959L;
/*    */ 
/*    */   private boolean isOneVOEnable(ArriveVO vo)
/*    */   {
/* 30 */     return ApproveFlowUtil.isCanSendApprove(vo);
/*    */   }
/*    */ @Override
			public void doBeforAction()
/*    */   {
/* 24 */     if (this.model.getAppUiState() == AppUiState.NOT_EDIT) {
/* 25 */       setComposite(false);
/*    。。
 * */     }
/*    */     else{
				ArriveVO aggVO = (ArriveVO) this.editor.getValue();
				//云峰网络  2020-12-20 校验失效日期不能早于到货日期
				new CheckDataValide().check(aggVO);
				super.doBeforAction();
/* 28 */       
/*    */   }
/*    */ }



/*    */   protected boolean isActionEnable()
/*    */   {
/* 35 */     if ((AppUiState.ADD == getModel().getAppUiState()) || (AppUiState.TRANSFERBILL_ADD == getModel().getAppUiState()) || (AppUiState.EDIT == getModel().getAppUiState()))
/*    */ 
/*    */     {
/* 38 */       return true;
/*    */     }
/*    */ 
/* 41 */     if (getModel().getSelectedData() == null) {
/* 42 */       return false;
/*    */     }
/*    */ 
/* 45 */     Object[] objs = getModel().getSelectedOperaDatas();
/* 46 */     if ((this.model.getSelectedData() != null) && (ArrayUtils.isEmpty(objs))) {
/* 47 */       return isOneVOEnable((ArriveVO)this.model.getSelectedData());
/*    */     }
/*    */ 
/* 50 */     if (objs.length > 1) {
/* 51 */       return true;
/*    */     }
/* 53 */     return isOneVOEnable((ArriveVO)objs[0]);
/*    */   }
/*    */ }
