/** <a href="http://www.cpupk.com/decompiler">Eclipse Class Decompiler</a> plugin, Copyright (c) 2017 Chen Chao. **/
/*    */package nc.bs.pub.action;

/*    */
/*    */import java.util.Hashtable;
import nc.bs.dao.BaseDAO;
/*    */
import nc.bs.framework.common.NCLocator;
/*    */
import nc.bs.pub.compiler.AbstractCompiler2;
import nc.cmp.tools.StringUtil;
import nc.impl.pubapp.pattern.rule.IRule;
/*    */
import nc.itf.ic.m45.self.IPurchaseInMaintain;
import nc.jdbc.framework.processor.ColumnProcessor;
/*    */
import nc.vo.ic.m45.entity.PurchaseInVO;
/*    */
import nc.vo.ml.NCLangRes4VoTransl;
/*    */
import nc.vo.pub.BusinessException;
/*    */
import nc.vo.pub.compiler.PfParameterVO;
/*    */
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

/*    */public class N_45_CANCELSIGN extends AbstractCompiler2
/*    */{
	/* 16 */private Hashtable m_methodReturnHas = new Hashtable();
	/*    */
	/* 18 */private Hashtable m_keyHas = null;

	/*    */
	/*    */
	/*    */
	/*    */
	/*    */
	/*    */
	/*    */
	/*    */
	/*    */public Object runComClass(PfParameterVO vo)
/*    */     throws BusinessException
/*    */   {
/*    */     try
/*    */     {
/* 32 */       this.m_tmpVo = vo;
/* 33 */       PurchaseInVO[] inVOs = (PurchaseInVO[])(PurchaseInVO[])getVos();
/*    */ 
			   inVOs = ((IPurchaseInMaintain) NCLocator.getInstance().lookup(
			   IPurchaseInMaintain.class)).cancelSign(inVOs);
			   //采购入库取消签字签字推srm
			   for (PurchaseInVO vo1 : inVOs) {
					
					   IRule<PurchaseInVO> rule = new PushSrmCancelRule();
					   rule.process(inVOs);
				   
			   }
/*    */     }

		
/*    */     catch (Exception ex)
/*    */     {
/* 40 */       ExceptionUtils.marsh(ex);
/*    */     }
/* 42 */     return null;
/*    */   }

	/*    */
	/*    */
	/*    */
	/*    */
	/*    */public String getCodeRemark()
	/*    */{
		/* 50 */return NCLangRes4VoTransl.getNCLangRes().getStrByID(
				"4008002_0", "04008002-0076");
		/*    */}

	/*    */
	/*    */
	/*    */
	/*    */private void setParameter(String key, Object val)
	/*    */{
		/* 57 */if (this.m_keyHas == null) {
			/* 58 */this.m_keyHas = new Hashtable();
			/*    */}
		/* 60 */if (val != null)
			/* 61 */this.m_keyHas.put(key, val);
		/*    */}
	/*    */
}
