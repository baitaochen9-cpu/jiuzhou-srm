package nc.bs.jzqc.labelprint.ace.bp.rule;

import nc.bs.dao.BaseDAO;
import nc.jdbc.framework.SQLParameter;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.vo.jzqc.labelprint.AggLabelPrintHVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.trade.voutils.SafeCompute;

public class WriteBackArriveNumRule {

	private boolean flag;

	public void process(AggLabelPrintHVO[] vos, AggLabelPrintHVO[] orgvos,
			boolean flag) {
//		try {
//			reWriteToPlan(vos, orgvos, flag);
//		} catch (BusinessException e) {
//			ExceptionUtils.wrappBusinessException(e.getMessage());
//		}
	}

	private void reWriteToPlan(AggLabelPrintHVO[] vos,
			AggLabelPrintHVO[] orgvos, boolean flag) throws BusinessException {

		if (vos == null || vos.length == 0)
			return;

		String sql = "update po_arriveorder_b set vbdef16 = to_number(replace(vbdef16,'~','0'))+? where pk_arriveorder_b = ? and nvl(dr,0) = 0 ";
		SQLParameter parameter = null;

		int len = vos.length;
		for (int i = 0; i < len; i++) {
			// 托盘标签回写
			if (!"JZ01-Cxx-10".equalsIgnoreCase(vos[i].getParentVO()
					.getTranstype())) {
				continue;
			}
			parameter = new SQLParameter();
			if (orgvos == null || orgvos.length == 0) {
				parameter.addParam(getReturnNum(vos[i], null, flag));
			} else {
				parameter.addParam(getReturnNum(vos[i], orgvos[i], flag));
			}
			parameter.addParam(vos[i].getParentVO().getSrcbillrowid());
			new BaseDAO().executeUpdate(sql, parameter);
			checkNum(vos[i]);
		}
	}

	private UFDouble getReturnNum(AggLabelPrintHVO vo, AggLabelPrintHVO orgvo,
			boolean flag) {
		UFDouble temp = vo.getParentVO().getNum_b();
		if (flag) {// 删除
			temp = SafeCompute.multiply(temp, new UFDouble(-1));
		} else {
			if (orgvo == null) {// 新增

			} else {// 修改
				UFDouble otemp = SafeCompute.multiply(orgvo.getParentVO()
						.getNum_b(), new UFDouble(-1));
				temp = SafeCompute.add(temp, otemp);
			}
		}
		return temp;

	}

	private void checkNum(AggLabelPrintHVO vo) throws BusinessException {
		String sql = "select count(0) from po_arriveorder_b b where (b.nnum - to_number( replace(b.vbdef16,'~','0'))) < 0 and b.pk_arriveorder_b = ?";
		SQLParameter parameter = new SQLParameter();
		parameter.addParam(vo.getParentVO().getSrcbillrowid());
		Object o = new BaseDAO().executeQuery(sql, parameter,
				new ColumnProcessor());
		
		if(o != null){
			int count = (int)o;
			if(count >0){
				throw new BusinessException("生成的标签数量超过到货单主数量！");
			}
		}
	}

	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

}
