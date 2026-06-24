package nc.ui.ic.m4n.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import nc.ui.ic.special.model.ICSpeBizEditorModel;
import nc.ui.ic.special.view.ICUISpecialBillEntity;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pubapp.uif2app.actions.BodyAddLineAction;
import nc.ui.pubapp.uif2app.event.card.CardBodyAfterEditEvent;
import nc.ui.trade.business.HYPubBO_Client;
import nc.uif.pub.exception.UifException;
import nc.vo.ic.m4n.entity.TransformBodyVO;
import nc.vo.ic.m4n.entity.TransformRowFlag;
import nc.vo.ic.pub.define.ICPubMetaNameConst;
import nc.vo.ic.special.define.ICSpecialBodyEntity;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.bill.BillTabVO;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.pattern.data.ValueUtils;
import nc.vo.trade.checkrule.VOChecker;
import nc.vo.trade.voutils.SafeCompute;

/**
 * 按钮增行操作
 * 
 * @author lkp
 * 
 */
public class TransBodyAddLineAction extends BodyAddLineAction {

	private static final long serialVersionUID = 9074669956715385595L;

	private ICSpeBizEditorModel editorModel;
	private Collection<String> clearItems;

	@Override
	public void doAction() {
		super.doAction();
		initRowFlag();
	}

	private void initRowFlag() {
		String vtrantypecode = (String) getEditorModel().getICBizView()
				.getBillCardPanel().getHeadItem("vdef20")
				.getValueObject();
		String code = null;
		try {
			code = (String)HYPubBO_Client.findColValue("bd_defdoc", "code", " nvl(dr,0) = 0 and pk_defdoc = '"+vtrantypecode+"'");
		} catch (UifException e) {
			e.printStackTrace();
		}
		if (!"01".equals(code)) {// 序列号拆分
			return;
		}
		int count = getEditorModel().getICBizView().getBillCardPanel()
				.getRowCount();

		if (count > 1) {
			CircularlyAccessibleValueObject bodyRowVO = getEditorModel()
					.getICBizView().getBillCardPanel().getBillModel()
					.getBodyValueRowVO(0, TransformBodyVO.class.getName());
			bodyRowVO.setAttributeValue(TransformBodyVO.FBILLROWFLAG,
					TransformRowFlag.AFTERCONVERT.value());

			// 计算第一行的总数量 然后计算当前行数量
			// 当前行数量 = 总数量减去其他行之前行的数量 按照顺序计算
			ICUISpecialBillEntity billEntity = getEditorModel()
					.getICUISpecialBillEntity();
			UFDouble totalnum = ValueUtils.getUFDouble(bodyRowVO
					.getAttributeValue(TransformBodyVO.NNUM));
			UFDouble othertotalnum = UFDouble.ZERO_DBL;
			for (int i = 1; i < billEntity.getBodys().length; i++) {
				ICSpecialBodyEntity body = billEntity.getBodys()[i];
				othertotalnum = SafeCompute.add(othertotalnum, body.getNnum());
			}
			bodyRowVO.setAttributeValue(
					ICPubMetaNameConst.CROWNO,
					getEditorModel().getICBizView().getBillCardPanel()
							.getBillModel()
							.getValueAt(count - 1, ICPubMetaNameConst.CROWNO));
			bodyRowVO.setAttributeValue(TransformBodyVO.NNUM,
					SafeCompute.sub(totalnum, othertotalnum));
			getEditorModel().getICBizView().getBillCardPanel().getBillModel()
					.setBodyRowVO(bodyRowVO, count - 1);
			clearItem(count - 1);
			getEditorModel().getICBizView().getBillCardPanel().getBillModel()
					.loadLoadRelationItemValue();

			BillEditEvent billEditEnent = new BillEditEvent(getEditorModel()
					.getICBizView().getBillCardPanel(),
					bodyRowVO.getAttributeValue(TransformBodyVO.NNUM), "nnum",
					count - 1);
			CardBodyAfterEditEvent cardEditEvent = new CardBodyAfterEditEvent(
					getEditorModel().getICBizView().getBillCardPanel(),
					billEditEnent, null);
			getModel().fireEvent(cardEditEvent);
		}
	}

	private void clearItem(int row) {
		if (!VOChecker.isEmpty(getClearItems())) {
			List<String> actClearKeys = new ArrayList();
			for (String key : getClearItems()) {
				if (key.indexOf(":") > 0) {
					String[] splits = key.split(":");
					String tablecode = splits[0];
					String itemkey = splits[1];
					if (getCardPanel().getCurrentBodyTableCode().equals(
							tablecode)) {
						actClearKeys.add(itemkey);
					} else {
						BillTabVO tabVO = getCardPanel().getBillData()
								.getTabVO(1, tablecode);

						if ((null != tabVO.getBasetab())
								&& (tabVO.getBasetab().equals(tablecode))) {
							actClearKeys.add(itemkey);
						}
					}
				} else {
					actClearKeys.add(key);
				}
			}

			for (String key : actClearKeys) {
				getCardPanel().getBillModel().setValueAt(null, row, key);
			}
		}
	}

	public ICSpeBizEditorModel getEditorModel() {
		return editorModel;
	}

	public void setEditorModel(ICSpeBizEditorModel editorModel) {
		this.editorModel = editorModel;
	}

	public Collection<String> getClearItems() {
		return clearItems;
	}

	public void setClearItems(Collection<String> clearItems) {
		this.clearItems = clearItems;
	}

}
