package nc.ui.so.m4331.billui.action.extend;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.framework.common.NCLocator;
import nc.itf.scmpub.reference.uap.pf.PfServiceScmUtil;
import nc.itf.uap.busibean.SysinitAccessor;
import nc.itf.uap.pf.IPFBusiAction;
import nc.itf.uap.pf.busiflow.PfButtonClickContext;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.pf.PfUtilClient;
import nc.ui.pubapp.uif2app.actions.AbstractReferenceAction;
import nc.ui.pubapp.uif2app.model.BillManageModel;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.uif2.ShowStatusBarMsgUtil;
import nc.ui.uif2.UIState;
import nc.ui.uif2.editor.IBillCardPanelEditor;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pu.pub.util.ListUtil;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.pf.BillStatusEnum;
import nc.vo.pub.workflownote.WorkflownoteVO;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.pubapp.util.VORowNoUtils;
import nc.vo.so.m4331.entity.DeliveryBVO;
import nc.vo.so.m4331.entity.DeliveryHVO;
import nc.vo.so.m4331.entity.DeliveryVO;
import nc.vo.so.salepacklist.AggSalePackListHVO;
import nc.vo.so.salepacklist.SalePackListBVO;
import nc.vo.so.salepacklist.SalePackListHVO;
import nc.vo.trade.checkrule.VOChecker;

public class CreateSalePackListAction extends AbstractReferenceAction {
	private static final long serialVersionUID = -7167526730230052116L;
	private IBillCardPanelEditor editor;
	private BillManageModel model;

	public void doAction(ActionEvent e) throws Exception {

		// DeliveryEditAction
		DeliveryVO selectedData = (DeliveryVO) getModel().getSelectedData();
		if (VOChecker.isEmpty(selectedData)) {

			ShowStatusBarMsgUtil.showErrorMsg(
					NCLangRes.getInstance().getStrByID("uif2",
							"ExceptionHandlerWithDLG-000000")/* 错误 */, "请选中数据",
					getModel().getContext());
		} else {
			PfUtilClient
					.childButtonClickedNew(createPfButtonClickContext(selectedData
							.getPrimaryKey()));
			if (PfUtilClient.isCloseOK()) {
				AggregatedValueObject[] retvos = (AggregatedValueObject[]) PfUtilClient
						.getRetOldVos();
				if (retvos == null || retvos.length == 0)
					return;
				retvos = changeAggSalePackListHVO(retvos,
						selectedData.getPrimaryKey(), selectedData);
				if (retvos == null || retvos.length == 0)
					return;
				AggSalePackListHVO[] newVOs = (AggSalePackListHVO[]) processBatch(
						"SAVEBASE", "4345", retvos, null, null);
				newVOs = (AggSalePackListHVO[]) processBatch("SAVE", "4345",
						newVOs, null, null);
				ShowStatusBarMsgUtil.showStatusBarMsg("操作成功", getModel()
						.getContext());
			}
		}
	}

	private Object processBatch(String actionName, String billType,
			AggregatedValueObject[] billvos, Object[] userObjAry,
			WorkflownoteVO worknoteVO) {
		try {
			return ((IPFBusiAction) NCLocator.getInstance().lookup(
					IPFBusiAction.class)).processBatch(actionName, billType,
					billvos, userObjAry, worknoteVO, null);

		} catch (BusinessException e) {

			ExceptionUtils.wrappException(e);
		}
		return null;
	}

	private AggSalePackListHVO[] changeAggSalePackListHVO(
			AggregatedValueObject[] retvos, String id, DeliveryVO selectedData)
			throws BusinessException {
		Map<String, DeliveryBVO> map = getDeliveryBVOs(id);
		List<AggSalePackListHVO> alist = new ArrayList<>();

		for (AggregatedValueObject aggvo1 : retvos) {
			AggSalePackListHVO aggvo = (AggSalePackListHVO) aggvo1;
			DeliveryBVO bvo = map.get(aggvo.getPrimaryKey());
			if (bvo == null)
				continue;
			SalePackListHVO hvo = getHeadVO(aggvo, bvo, selectedData);
			SalePackListBVO[] bodys = getBodys(aggvo, bvo, selectedData);
			Arrays.sort(bodys,LetterNumberSortUtil.letterNumberOrder("palleno"));
			VORowNoUtils.setVOsRowNoByRule(bodys, "rowno");
			aggvo.setParentVO(hvo);
			aggvo.setChildrenVO(bodys);
			alist.add(aggvo);
		}

		Map<String, AggSalePackListHVO> saleMap = new HashMap<>();

		for (AggSalePackListHVO hvo : alist) {

			String pk_material = hvo.getParentVO().getPk_material();

			if (!saleMap.containsKey(pk_material)) {
				saleMap.put(pk_material, hvo);
			} else {
				AggSalePackListHVO hvo1 = saleMap.get(pk_material);
				SalePackListBVO[] bvos = (SalePackListBVO[]) hvo1
						.getChildrenVO();
				List<SalePackListBVO> list = new ArrayList<>();
				for (SalePackListBVO bvo : bvos) {
					list.add(bvo);
				}
				SalePackListBVO[] bvos1 = (SalePackListBVO[]) hvo
						.getChildrenVO();
				for (SalePackListBVO bvo : bvos1) {
					list.add(bvo);
				}
				SalePackListBVO[] bodys = list.toArray(new SalePackListBVO[list
						.size()]);
				VORowNoUtils.setVOsRowNoByRule(bodys, "rowno");
				hvo1.setChildrenVO(bodys);
				saleMap.put(pk_material, hvo1);
			}
		}
		Collection<AggSalePackListHVO> coll = saleMap.values();
		return coll.toArray(new AggSalePackListHVO[coll.size()]);
	}

	private SalePackListHVO getHeadVO(AggSalePackListHVO aggvo,
			DeliveryBVO bvo, DeliveryVO selectedData) throws BusinessException {
		SalePackListHVO hvo = new SalePackListHVO();
		// 设置来源信息
		hvo.setSrcbillid(bvo.getCdeliveryid());// 来源id
		hvo.setSrcbilltype("4331");// 来源类型

		// 设置转换信息
		hvo.setPk_srcmaterial(bvo.getCmaterialid());// 物料id
		hvo.setPk_material(bvo.getCmaterialid());// 物料id
		hvo.setNgrosswt(bvo.getNastnum());// 数量
		hvo.setPk_group(bvo.getPk_group());
		hvo.setPk_org(bvo.getPk_org());
		hvo.setCcustomerid(bvo.getCordercustid());
		DeliveryHVO ehvo = selectedData.getParentVO();
		hvo.setVsalebillcode(bvo.getVsrccode());
		hvo.setVcustorderno(ehvo.getVbillcode());
		hvo.setDef2(aggvo.getParentVO().getDef2());
		hvo.setDef3(aggvo.getParentVO().getDef3());
		hvo.setDef4(aggvo.getParentVO().getDef5());
		setDefaultValue(hvo);
		return hvo;
	}

	private SalePackListBVO[] getBodys(AggSalePackListHVO aggvo,
			DeliveryBVO bvo, DeliveryVO selectedData) throws BusinessException {

		SalePackListHVO hvo = aggvo.getParentVO();
		SalePackListBVO[] bodys = (SalePackListBVO[]) aggvo.getChildrenVO();

		List<SalePackListBVO> list = new ArrayList<>();

		for (SalePackListBVO vbody : bodys) {
			SalePackListBVO body = getBody(hvo, vbody, bvo, selectedData);
			list.add(body);
		}
		return list.toArray(new SalePackListBVO[list.size()]);
	}

	private SalePackListBVO getBody(SalePackListHVO hvo, SalePackListBVO vbody,
			DeliveryBVO bvo, DeliveryVO selectedData) throws BusinessException {
		SalePackListBVO body = new SalePackListBVO();
		body.setBatchcode(bvo.getVbatchcode());
		body.setPk_batchcode(bvo.getPk_batchcode());
		body.setNgrosswt(vbody.getNgrosswt());
		body.setNpiece(vbody.getNpiece());
		body.setNtarenum(vbody.getNtarenum());
		body.setNweight(vbody.getNweight());
		body.setPalleno(vbody.getPalleno());
		body.setSpec_t(vbody.getSpec_t());
		body.setSpec(vbody.getSpec());
		body.setUnit(bvo.getCunitid());
		body.setPk_sourcebillrowid(bvo.getCdeliverybid());

		body.setCfirstbid(bvo.getCfirstbid());
		body.setCfirstid(bvo.getCfirstid());
		body.setCsrcbid(bvo.getCdeliverybid());
		body.setCsrcid(bvo.getCdeliveryid());
		body.setVfirstcode(bvo.getVfirstcode());
		body.setVfirstrowno(bvo.getVfirstrowno());
		body.setVfirsttrantype(bvo.getVfirsttrantype());
		body.setVfirsttype(bvo.getVfirsttype());
		body.setVsrcrowno(bvo.getCrowno());
		DeliveryHVO ehvo = selectedData.getParentVO();
		body.setVsrccode(ehvo.getVbillcode());
		body.setVsrctrantype(ehvo.getVtrantypecode());
		body.setVsrctype("4331");

		return body;
	}

	private Map<String, DeliveryBVO> getDeliveryBVOs(String id)
			throws BusinessException {
		DeliveryBVO[] bvos = (DeliveryBVO[]) HYPubBO_Client.queryByCondition(
				DeliveryBVO.class, " so_delivery_b.cdeliveryid ='" + id + "'");

		if (bvos == null || bvos.length == 0)
			throw new BusinessException("该发货单不需要生成销售包装清单");
		Map<String, DeliveryBVO> mapvos = new HashMap<>();
		for (DeliveryBVO vo : bvos) {
			String key = vo.getCdeliverybid();
			if (!StringUtil.isEmpty(key)) {

				mapvos.put(key, vo);
			}
		}
		return mapvos;
	}

	private void setDefaultValue(SalePackListHVO headvo) {
		headvo.setAttributeValue("pkorg", headvo.getPk_org());
		// 设置单据状态、单据业务日期默认值
		headvo.setAttributeValue("approvestatus", BillStatusEnum.FREE.value());
		headvo.setAttributeValue("dbilldate",
				new UFDate(System.currentTimeMillis()));
		headvo.setAttributeValue("billtype", "4345");
		headvo.setAttributeValue("transtype", "4345");
		headvo.setAttributeValue("transtypepk", PfServiceScmUtil
				.getTrantypeidByCode(new String[] { "4345" }).get("4345"));
		headvo.setAttributeValue("creationtime",
				new UFDateTime(System.currentTimeMillis()));
		headvo.setAttributeValue("creator", getModel().getContext()
				.getPk_loginUser());
		headvo.setAttributeValue("maketime",
				new UFDateTime(System.currentTimeMillis()));
		headvo.setAttributeValue("billmaker", getModel().getContext()
				.getPk_loginUser());
	}

	private PfButtonClickContext createPfButtonClickContext(String pk_delivery) {
		PfButtonClickContext context = new PfButtonClickContext();
		context.setParent(getModel().getContext().getEntranceUI());
		context.setSrcBillType(getSourceBillType());
		context.setPk_group(getModel().getContext().getPk_group());
		context.setUserId(getModel().getContext().getPk_loginUser());
		context.setCurrBilltype("4345");
		context.setUserObj(null);
		context.setSrcBillId(null);
		context.setBusiTypes(getBusitypes());
		context.setSrcBillId(pk_delivery);

		if ((!ListUtil.isEmpty(getTranstypes()))
				&& (!getTranstypes().contains(""))) {
			context.setTransTypes(getTranstypes());
		}

		context.setClassifyMode(2);
		return context;
	}

	protected boolean isActionEnable() {
		boolean yf635 = false;
		DeliveryVO selectedData = (DeliveryVO) getModel().getSelectedData();
		if (selectedData != null) {
			try {
				yf635 = SysinitAccessor
						.getInstance()
						.getParaBoolean(selectedData.getParentVO().getPk_org(),
								"YF635").booleanValue();
			} catch (BusinessException e) {
				e.printStackTrace();
			}
		}
		return UIState.NOT_EDIT == getModel().getUiState() && yf635;
	}

	public IBillCardPanelEditor getEditor() {
		return editor;
	}

	public BillManageModel getModel() {
		return model;
	}

	public void setEditor(IBillCardPanelEditor editor) {
		this.editor = editor;
	}

	public void setModel(BillManageModel model) {
		this.model = model;
		model.addAppEventListener(this);
	}
}
