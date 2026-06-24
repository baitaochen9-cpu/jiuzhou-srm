package nc.ui.jzqc.labelstatus.ace.model;

import java.util.Arrays;

import nc.bs.framework.common.NCLocator;
import nc.itf.jzqc.ILabelstatusMaintain;
import nc.ui.pubapp.uif2app.model.BatchModelDataManager;
import nc.ui.uif2.model.BatchBillTableModel;
import nc.vo.jzqc.labelstatus.LabelStatusVO;
import nc.vo.pub.BusinessException;

public class LabelStatusModelDataManager extends BatchModelDataManager {
	private boolean isShowSealData;
	private ILabelstatusMaintain queryServicer;

	public LabelStatusModelDataManager() {
		isShowSealData = false;
	}

	public void initModel() {
		try {
			LabelStatusVO[] datas = getQueryServicer().queryAllVOByContext(
					isShowSealData(), getModel().getContext());

			getModel().initModel(datas);
		} catch (BusinessException e) {
			nc.bs.logging.Logger.error(e.getMessage());
		}
	}

	public boolean isShowSealData() {
		return isShowSealData;
	}

	private ILabelstatusMaintain getQueryServicer() {
		if (queryServicer == null) {
			queryServicer = ((ILabelstatusMaintain) NCLocator.getInstance()
					.lookup(ILabelstatusMaintain.class));
		}
		return queryServicer;
	}

	public void initModelBySqlWhere(String sqlWhere) {
	}

	public void refresh() {
		initModel();
	}

	public void setShowSealDataFlag(boolean showSealDataFlag) {
		isShowSealData = showSealDataFlag;
	}
}