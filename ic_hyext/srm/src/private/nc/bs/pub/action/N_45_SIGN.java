package nc.bs.pub.action;

import java.util.Hashtable;

import nc.bs.dao.BaseDAO;
import nc.bs.framework.common.NCLocator;
import nc.bs.pub.compiler.AbstractCompiler2;
import nc.cmp.tools.StringUtil;
import nc.impl.pubapp.pattern.rule.IRule;

import nc.itf.ic.m45.self.IPurchaseInMaintain;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.vo.ic.m45.entity.PurchaseInVO;
import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.pub.BusinessException;
import nc.vo.pub.compiler.PfParameterVO;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

public class N_45_SIGN extends AbstractCompiler2 {
	private Hashtable m_methodReturnHas = new Hashtable();

	private Hashtable m_keyHas = null;

	public Object runComClass(PfParameterVO vo) throws BusinessException {
		try {
			this.m_tmpVo = vo;
			PurchaseInVO[] inVOs = (PurchaseInVO[]) (PurchaseInVO[]) getVos();

			inVOs = ((IPurchaseInMaintain) NCLocator.getInstance().lookup(
					IPurchaseInMaintain.class)).sign(inVOs);
			// ²É¹ºÈë¿âÇ©×ÖÍÆsrm
			for (PurchaseInVO vo1 : inVOs) {
				IRule<PurchaseInVO> rule = new PushSrmRule();
				rule.process(inVOs);
			}
			return inVOs;
		} catch (Exception ex) {
			ExceptionUtils.marsh(ex);
		}
		return null;
	}

	public String getCodeRemark() {
		return NCLangRes4VoTransl.getNCLangRes().getStrByID("4008002_0",
				"04008002-0076");
	}

	private void setParameter(String key, Object val) {
		if (this.m_keyHas == null) {
			this.m_keyHas = new Hashtable();
		}
		if (val != null)
			this.m_keyHas.put(key, val);
	}
}