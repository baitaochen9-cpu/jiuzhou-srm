/*    */ package nc.ui.uif2;
/*    */ 
/*    */ import java.awt.event.ActionEvent;
/*    */ import java.util.concurrent.ExecutionException;

/*    */ import javax.swing.SwingWorker;

/*    */ import nc.bs.logging.Logger;
import nc.md.data.access.NCObject;
import nc.ui.uif2.gmplog.GmpLogProcessor;
/*    */ import nc.vo.pub.BusinessException;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class NCAsynAction
/*    */   extends NCAction
/*    */   implements IAsynAction
/*    */ {
/*    */   private static final long serialVersionUID = 1853357843335038909L;
/*    */   
/*    */   public void actionPerformed(final ActionEvent actionEvent)
/*    */   {
/* 21 */     Logger.debug("Entering " + getClass().toString() + ".actionPerformed");
			boolean ischeck = beforeDoAction(actionEvent);
			if (!ischeck)
				return;
/* 22 */     beforeDoAction();
/* 23 */     SwingWorker<Object, Object> sw = null;
/*    */     try
/*    */     {
/* 26 */       if (((interceptor != null) && (!interceptor.beforeDoAction(this, actionEvent))) || (!beforeStartDoAction(actionEvent))) {
/*    */         return;
/*    */       }

				//获取当前界面数据，用来后续比较
				final Object cilentBill ;
				//当前数据库保存的数据
				final NCObject oldncobj;
				if(getPk() !=null){
					cilentBill =  getSuperVO(actionEvent);
					GmpLogProcessor process = new GmpLogProcessor();
					 oldncobj = process.qyrNCObj(cilentBill);
				}else{
					cilentBill  = null;
					oldncobj  = null;

				}
/*    */       
/*    */ 
/* 31 */       sw = new SwingWorker()
/*    */       {
/*    */         protected Object doInBackground() throws Exception {
/* 34 */           doAction(actionEvent);
				//如果增加了电子签名记录
				if(getPk() !=null){
					try {
						updatePk(actionEvent);
					//20230710 liyf 记录审计日志
						setGmpLog(actionEvent,cilentBill,oldncobj);
					} catch (Exception e1) {
						Logger.debug(e1.getMessage());
					}
				}
/* 35 */           return null;
/*    */         }
/*    */         
/*    */         protected void done()
/*    */         {
/*    */           try {
/* 41 */             get();
/* 42 */             if (interceptor != null) {
/* 43 */               interceptor.afterDoActionSuccessed(NCAsynAction.this, actionEvent);
/*    */             }
/* 45 */             doAfterSuccess(actionEvent);
/*    */           } catch (ExecutionException ex) {
/* 47 */             boolean ret = doAfterFailure(actionEvent, ex);
/* 48 */             if ((ret) && ((interceptor == null) || (interceptor.afterDoActionFailed(NCAsynAction.this, actionEvent, ex))) && 
/* 49 */               (exceptionHandler != null)) {
/* 50 */               if ((ex.getCause() instanceof BusinessException)) {
/* 51 */                 processExceptionHandler((BusinessException)ex.getCause());
/*    */               }
/* 53 */               else if ((ex.getCause() instanceof RuntimeException)) {
/* 54 */                 if ((ex.getCause().getCause() != null) && ((ex.getCause().getCause() instanceof BusinessException))) {
/* 55 */                   processExceptionHandler((BusinessException)ex.getCause().getCause());
/*    */                 }
/*    */                 else {
/* 58 */                   processExceptionHandler((RuntimeException)ex.getCause());
/*    */                 }
/*    */               }
/*    */               else {
/* 62 */                 processExceptionHandler(ex);
/*    */               }
/*    */             }
/*    */           }
/*    */           catch (InterruptedException ex) {
/* 67 */             throw new RuntimeException(ex);
/*    */           } finally {
/* 69 */             Logger.debug("Leaving " + getClass().toString() + ".actionPerformed");
/*    */           }
/*    */         }
/* 72 */       };
/* 73 */       sw.execute();
/*    */     } catch (Exception ex) {
/* 75 */       boolean ret = doAfterFailure(actionEvent, ex);
/* 76 */       if ((ret) && ((interceptor == null) || (interceptor.afterDoActionFailed(this, actionEvent, ex))) && 
/* 77 */         (exceptionHandler != null)) {
/* 78 */         processExceptionHandler(ex);
/*    */       }
/*    */     }
/*    */     finally
/*    */     {
/* 83 */       if (sw == null) {
/* 84 */         Logger.debug("Leaving " + getClass().toString() + ".actionPerformed");
/*    */       }
/*    */     }
/*    */   }
/*    */ }
