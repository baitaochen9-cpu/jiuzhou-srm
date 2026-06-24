package nc.bs.jzqc.labelprint.ace.bp.rule;

import java.util.ArrayList;
import java.util.List;

import nc.bs.dao.BaseDAO;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.jdbc.framework.SQLParameter;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.jzqc.labelprint.AggLabelPrintHVO;
import nc.vo.pfxx.util.ArrayUtils;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

public class UpdateICAdjustStateRule implements IRule<AggLabelPrintHVO> {
	@Override
	public void process(AggLabelPrintHVO[] vos) {
		if (ArrayUtils.isEmpty(vos))
			return;
		if (vos == null || vos.length == 0)
			return;

		try {

			List<String> list = new ArrayList<>();
			for (AggLabelPrintHVO vo : vos) {
				// 库存调整单回写
				if (!isMul(vo.getParentVO().getTranstype())) {
					continue;
				}

				if (StringUtil.isEmpty(vo.getParentVO().getSrcbillrowid())) {
					continue;
				}
				String[] ids = vo.getParentVO().getSrcbillrowid().split(",");

				for (String id : ids) {
					if (!list.contains(id)) {
						list.add(id);
					}
				}
			}
			String sql = "update ic_stateadjust set vdef1 = 'N'  where cstateadjustid = ? and nvl(dr,0) = 0 ";
			SQLParameter parameter = null;

			for (String id : list) {
				parameter = new SQLParameter();
				parameter.addParam(id);
				new BaseDAO().executeUpdate(sql, parameter);
			}
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}

	}

	private boolean isMul(String transtype) {
		if ("JZ01-Cxx-25".equals(transtype)) {// 标签档案-合格标签
			return true;
		} else if ("JZ01-Cxx-30".equals(transtype)) {// 标签档案-过期标签
			return true;
		} else if ("JZ01-Cxx-45".equals(transtype)) {// 标签档案-不合格标签
			return true;
		}
		return false;
	}

}