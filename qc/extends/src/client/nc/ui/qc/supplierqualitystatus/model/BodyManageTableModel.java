package nc.ui.qc.supplierqualitystatus.model;

import nc.ui.mmgp.uif2.model.MMGPBillManageModel;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.uif2.AppEvent;
import nc.ui.uif2.AppEventListener;
import nc.ui.uif2.model.AppEventConst;
import nc.ui.uif2.model.BatchBillTableModel;
import nc.uif.pub.exception.UifException;
import nc.vo.bd.meta.BatchOperateVO;
import nc.vo.bd.supplier.stock.SupStockVO;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pu.supqualistatus.SupplierqualityHVO;

/**
 * 按钮管理的模型类，需要实现对功能节点模型的监听
 * 
 * @author lkp
 * 
 */
public class BodyManageTableModel extends BatchBillTableModel implements
		AppEventListener {

	private MMGPBillManageModel funcModel = null;

	public MMGPBillManageModel getFuncModel() {
		return funcModel;
	}

	public void setFuncModel(MMGPBillManageModel funcModel) {
		this.funcModel = funcModel;
		this.funcModel.addAppEventListener(this);
	}

	public void handleEvent(AppEvent event) {

		if (AppEventConst.SELECTION_CHANGED == event.getType()) {
			initModelSelectData();
		}
	}

	public void directSave(BatchOperateVO vo) throws Exception {
		super.directSave(vo);
		initModelSelectData();
	}

	private void initModelSelectData() {
		Object obj = getFuncModel().getSelectedData();
		if (obj == null) {
			initModel(null);
		} else {
			SupStockVO vo = (SupStockVO) obj;
			SupplierqualityHVO[] vos = null;
			try {
				String strWhere = " pk_supplier = '" + vo.getPk_supplier()
						+ "' and pk_org ='" + vo.getPk_org() + "'";
				if (!StringUtil.isEmpty(vo.getPk_tradeterm())) {
					strWhere = strWhere + " and " + vo.getPk_tradeterm();
				}
				vos = (SupplierqualityHVO[]) HYPubBO_Client.queryByCondition(
						SupplierqualityHVO.class, strWhere);
			} catch (UifException e) {
				e.printStackTrace();
			}
			initModel(vos);
		}
	}

}
