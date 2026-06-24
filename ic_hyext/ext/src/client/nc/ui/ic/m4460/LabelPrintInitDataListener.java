package nc.ui.ic.m4460;

import nc.funcnode.ui.FuncletInitData;
import nc.ui.ic.m4460.model.StateAdjustModel;
import nc.ui.pubapp.uif2app.model.DefaultFuncNodeInitDataListener;
import nc.vo.ic.m4460.entity.StateAdjustVO;
import nc.vo.ic.pub.util.ValueCheckUtil;

public class LabelPrintInitDataListener extends DefaultFuncNodeInitDataListener {

	private StateAdjustModel adjustModel;

	public void initData(FuncletInitData data) {
		if (data == null) {
			super.initData(data);
		} else {
			if (data != null && data.getInitData() != null
					&& (data.getInitData() instanceof StateAdjustVO[])) {

				StateAdjustVO[] vos = (StateAdjustVO[]) data.getInitData();
				if (ValueCheckUtil.isNullORZeroLength(vos)) {
					getAdjustModel().initModel(null);
				} else {
					getAdjustModel().initModel(vos);
				}
			} else {
				super.initData(data);
			}
		}
	}

	public StateAdjustModel getAdjustModel() {
		return adjustModel;
	}

	public void setAdjustModel(StateAdjustModel adjustModel) {
		this.adjustModel = adjustModel;
	}

}