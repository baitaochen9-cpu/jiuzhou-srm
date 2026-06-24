package nc.bs.jzqc.labelprint.ace.bp;

import nc.impl.pubapp.pattern.data.vo.VOQuery;
import nc.vo.am.common.util.ExceptionUtils;
import nc.vo.jzqc.labelprint.LabelPrintHVO;
import nc.vo.pub.BusinessException;

/**
 * 标准单据的BP
 */
public class AceLabelprintCheckBP {

	/**
	 * 校验
	 * 
	 * @param vos
	 * @param script
	 * @return
	 */
	public void checkAggLabelPrintHVO(String pk_org, String[] billid,
			String transtype) {
		VOQuery<LabelPrintHVO> query = new VOQuery<LabelPrintHVO>(
				LabelPrintHVO.class);
		String condition = " and pk_org = '" + pk_org + "' and  transtype ='"
				+ transtype + "'";

		if (isMul(transtype)) {
			String[] ids = billid[0].split(",");
			int len = ids.length;
			for (int i = 0; i < len; i++) {
				if (i == 0) {
					condition = condition + " and ( srcbillrowid like '%"
							+ ids[i] + "%'";
				} else {
					condition = condition + " or srcbillrowid like '%" + ids[i]
							+ "%'";
				}
				if (i == len - 1) {
					condition = condition + "  )";
				}
			}
		} else {

			String where = append("srcbillrowid", billid);
			condition = condition + " and " + where;
		}
		LabelPrintHVO[] hvos = query.query(condition, null);
		if (hvos != null && hvos.length > 0) {
			ExceptionUtils.asBusinessRuntimeException(new BusinessException(
					"已经生产了对应的标签档案不能打印,标签档案号：" + hvos[0].getBillno() + "！"));
		}
	}

	private String append(String name, String[] values) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(" ");
		buffer.append(name);
		buffer.append(" in (");
		int length = values.length;
		for (int i = 0; i < length; i++) {
			buffer.append("'"+values[i]+"'");
			buffer.append(",");
		}
		length = buffer.length();
		buffer.deleteCharAt(length - 1);
		buffer.append(") ");
		return buffer.toString();
	}

	private boolean isMul(String transtype) {
		if ("JZ01-Cxx-25".equals(transtype)) {// 标签档案-合格标签
			return true;
		} else if ("JZ01-Cxx-30".equals(transtype)) {// 标签档案-过期标签
			return true;
		} else if ("JZ01-Cxx-45".equals(transtype)) {// 标签档案-不合格标签
			return true;
		} else if ("JZ01-Cxx-15".equals(transtype)) {// 标签档案-预标签
			return true;
		} else if ("JZ01-Cxx-10".equals(transtype)) {// 标签档案-预标签
			return true;
		}
		return false;
	}

}
