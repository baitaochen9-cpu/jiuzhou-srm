package nc.bs.pub.action;

import java.util.ArrayList;
import java.util.Hashtable;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.pub.compiler.AbstractCompiler2;
import nc.jdbc.framework.processor.ArrayListProcessor;
import nc.medpub.util.SysInitParamUtil;
import nc.pub.jz.util.RaybowIcMassageUtil;
import nc.vo.ic.m46.entity.FinProdInVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.compiler.PfParameterVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

/**
 * 备注：库存产成品入库单的推式保存 单据动作执行中的动态执行类的动态执行类。 创建日期：(2010-4-19)
 * 
 * @author 平台脚本生成
 */
public class N_46_PUSHSAVE extends AbstractCompiler2 {
	private java.util.Hashtable m_methodReturnHas = new java.util.Hashtable();

	private Hashtable m_keyHas = null;

	/**
	 * N_46_PUSHSAVE 构造子注解。
	 */
	public N_46_PUSHSAVE() {
		super();
	}

	/*
	 * 备注：平台编写规则类 接口执行类
	 */
	public Object runComClass(PfParameterVO vo) throws BusinessException {
		try {
			super.m_tmpVo = vo;
			nc.vo.ic.m46.entity.FinProdInVO[] inVOs = (nc.vo.ic.m46.entity.FinProdInVO[]) getVos();

			FinProdInVO[] insert = nc.bs.framework.common.NCLocator
					.getInstance()
					.lookup(nc.itf.ic.m46.IProductInMaitain.class)
					.insert(inVOs);

		

			return insert;
		} catch (Exception ex) {
			ExceptionUtils.marsh(ex);
		}
		return null;
	}



	/*
	 * 备注：平台编写原始脚本
	 */
	@Override
	public String getCodeRemark() {
		return nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
				"4008002_0", "04008002-0076")/* @res "不支持修改脚本" */;
	}

	/*
	 * 备注：设置脚本变量的HAS
	 */
	private void setParameter(String key, Object val) {
		if (m_keyHas == null) {
			m_keyHas = new Hashtable();
		}
		if (val != null) {
			m_keyHas.put(key, val);
		}
	}
}