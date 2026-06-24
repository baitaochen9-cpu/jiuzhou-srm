package nc.ui.jzqc.labelprint.action;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import nc.bs.framework.common.NCLocator;
import nc.itf.jzqc.ILabelprintMaintain;
import nc.ui.bd.print.printlog.Printlistenner;
import nc.ui.pub.print.IMetaDataDataSource;
import nc.ui.pub.print.PrintException;
import nc.ui.pubapp.uif2app.actions.BaseMetaDataBasedPrintAction.MetaDataSource;
import nc.ui.pubapp.uif2app.actions.RefreshSingleAction;
import nc.ui.uif2.model.AbstractAppModel;
import nc.ui.uif2.model.AbstractUIAppModel;
import nc.vo.bd.printcheck.PrintResultVO;
import nc.vo.jzqc.labelprint.AggLabelPrintHVO;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

public class LabelPrintRecordListener extends Printlistenner {
	private AbstractUIAppModel model;

	public LabelPrintRecordListener(AbstractUIAppModel model) {
		this.model = model;
	}

	@Override
	public void beforePrint() throws PrintException {
		super.beforePrint();
	}

	public void afterPrint(int rowCount) throws PrintException {
		ILabelprintMaintain operator = NCLocator.getInstance().lookup(
				ILabelprintMaintain.class);
		List<Object> list = new ArrayList<>();

		IMetaDataDataSource[] mdatas = getDatasource();

		if (mdatas == null || mdatas.length == 0) {
			return;
		}

		for (IMetaDataDataSource data : mdatas) {
			MetaDataSource datasource = (MetaDataSource) data;
			data.getMDObjects();
			Object[] pdatas = datasource.getPrintData();
			if (pdatas != null && pdatas.length > 0) {
				list.add(pdatas[0]);
			}
		}
		Object[] datas = list.toArray(new Object[list.size()]);
		Map<String, PrintResultVO> resultMap;
		try {
			resultMap = operator.getPrintResultVO(datas);
			for (Object data : datas) {
				AggLabelPrintHVO ordervo = (AggLabelPrintHVO) data;
				PrintResultVO resultvo = (PrintResultVO) resultMap.get(ordervo
						.getParentVO().getPk_labelprint());
				if (resultvo != null) {
					ordervo.getParentVO().setIprintcount(
							resultvo.getPrintcount());
				}
			}
			operator.updatePrintResultVO(datas);

			RefreshSingleAction srefresh = new RefreshSingleAction();
			srefresh.setModel((AbstractAppModel) model);
			ActionEvent event = new ActionEvent(srefresh, 1001, "Ë¢ÐÂ");
			srefresh.doAction(event);
		} catch (Exception e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
	}

	public String getFuncode() {
		return "H3010200";
	}
}
