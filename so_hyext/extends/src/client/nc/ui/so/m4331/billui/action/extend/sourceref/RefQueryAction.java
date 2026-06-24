package nc.ui.so.m4331.billui.action.extend.sourceref;

import java.awt.event.ActionEvent;
import java.lang.reflect.Array;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.pf.IPFConfig;
import nc.ui.pubapp.billref.src.DefaultBillReferQuery;
import nc.ui.pubapp.billref.src.RefBillModel;
import nc.ui.pubapp.billref.src.RefContext;
import nc.ui.pubapp.billref.src.action.QueryAction;
import nc.ui.pubapp.uif2app.model.IQueryService;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

public class RefQueryAction extends QueryAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1998802445449910811L;
	private boolean isInitQuery;
	private RefContext refContext;

	public void doAction(ActionEvent e) throws Exception {
		DefaultBillReferQuery referQuery = (DefaultBillReferQuery) getRefContext()
				.getRefDialog().getQueyDlg();
		executeQuery(referQuery.getBillSrcVar().getWhereStr());
	}

	public RefBillModel getRefBillModel() {
		return refContext.getRefBill().getRefBillModel();
	}

	public RefContext getRefContext() {
		return refContext;
	}

	public boolean isInitQuery() {
		return isInitQuery;
	}

	public void setInitQuery(boolean initQuery) {
		isInitQuery = initQuery;
	}

	public void setRefBillModel(RefBillModel refBillModel) {
	}

	public void setRefContext(RefContext refContext) {
		this.refContext = refContext;
	}

	protected void executeQuery(String sqlWhere) {
		try {
			DefaultBillReferQuery referQuery = (DefaultBillReferQuery) getRefContext()
					.getRefDialog().getQueyDlg();

			if (getRefContext().getRefInfo().getQueryService() != null) {
				IQueryService queryService = getRefContext().getRefInfo()
						.getQueryService();

				AggregatedValueObject[] billvos = (AggregatedValueObject[]) queryService
						.queryByWhereSql(sqlWhere);

				getRefBillModel().setBillVOs(billvos);
			} else {
				getRefBillModel().setSqlWhere(
						referQuery.getQueryDlg().getWhereSQL());

				getRefBillModel().setQueryScheme(referQuery.getQueryScheme());
				IPFConfig pfConfig = (IPFConfig) NCLocator.getInstance()
						.lookup(IPFConfig.class.getName());

				CircularlyAccessibleValueObject[] tmpHeadVo = pfConfig
						.queryHeadAllData(getRefContext().getRefInfo()
								.getBillSrcVar().getBillType(),
								referQuery.getWhereSQL());

				SuperVO[] superHeadVOs = null;
				if ((tmpHeadVo != null) && (tmpHeadVo.length > 0)) {
					superHeadVOs = (SuperVO[]) Array.newInstance(
							tmpHeadVo[0].getClass(), tmpHeadVo.length);

					System.arraycopy(tmpHeadVo, 0, superHeadVOs, 0,
							tmpHeadVo.length);
				}

				getRefBillModel().setHeadVOs(superHeadVOs);
			}
		} catch (Exception ex) {
			ExceptionUtils.wrappException(ex);
		} finally {
			setInitQuery(false);
		}
	}

	protected boolean isActionEnable() {
		return true;
	}
}