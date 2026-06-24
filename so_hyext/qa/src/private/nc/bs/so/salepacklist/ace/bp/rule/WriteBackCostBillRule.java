package nc.bs.so.salepacklist.ace.bp.rule;

import nc.bs.dao.BaseDAO;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.jdbc.framework.SQLParameter;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.so.salepacklist.AggSalePackListHVO;
import nc.vo.so.salepacklist.SalePackListBVO;

public class WriteBackCostBillRule implements IRule<AggSalePackListHVO> {

	private boolean flag;

	public void process(AggSalePackListHVO[] vos) {
		try {
			reWriteToPlan(vos, isFlag());
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
	}

	private void reWriteToPlan(AggSalePackListHVO[] vos, boolean flag)
			throws BusinessException {

		if (vos == null || vos.length == 0)
			return;
		if (vos == null || vos.length == 0)
			return;

		String sql = "update so_delivery_b set vbdef19 = ? where cdeliverybid = ? and nvl(dr,0) = 0 ";
		SQLParameter parameter = null;
		for (AggSalePackListHVO vo : vos) {
			SalePackListBVO[] bodys = (SalePackListBVO[]) vo.getChildrenVO();
			for (SalePackListBVO body : bodys) {
				parameter = new SQLParameter();
				if (flag) {
					if (body.getStatus() == VOStatus.DELETED) {
						parameter.addParam("N");
					} else {
						parameter.addParam("Y");
					}
				} else {
					parameter.addParam("N");
				}
				parameter.addParam(body.getCsrcbid());

				new BaseDAO().executeUpdate(sql, parameter);
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
