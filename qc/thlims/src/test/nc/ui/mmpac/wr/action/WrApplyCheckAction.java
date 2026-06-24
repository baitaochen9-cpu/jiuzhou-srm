package nc.ui.mmpac.wr.action;

import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.framework.common.NCLocator;
import nc.bs.uif2.validation.ValidationException;
import nc.itf.jzyy.sys.IProcessService;
import nc.itf.jzyy.sys.thlims.ISysDispatcherThLims;
import nc.ui.mmf.framework.action.ActionInitializer;
import nc.ui.mmpac.wr.serviceproxy.WrBusinessServiceProxy;
import nc.ui.mmpac.wr.util.WrUIHelper;
import nc.ui.mmpac.wr.util.WrUIHelperPatch;
import nc.ui.mmpac.wr.util.WrUIOperateUtil;
import nc.ui.mmpac.wr.validator.WrApplyCheckValidator;
import nc.ui.mmpac.wr.validator.WrItemNullValidator;
import nc.ui.mmpac.wr.validator.WrModuleEnableValidator;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.uif2.ShowStatusBarMsgUtil;
import nc.ui.uif2.UIState;
import nc.util.mmf.framework.base.MMArrayUtil;
import nc.util.mmf.framework.base.MMNumberUtil;
import nc.util.mmf.framework.base.MMValueCheck;
import nc.vo.mmpac.wr.consts.WrBtnConst;
import nc.vo.mmpac.wr.consts.WrptLangConst;
import nc.vo.mmpac.wr.entity.AggWrVO;
import nc.vo.mmpac.wr.entity.WrItemVO;
import nc.vo.mmpac.wr.entity.WrSerialNoVO;
import nc.vo.mmpac.wr.enumeration.WrBillStatusEnum;
import nc.vo.mmpac.wr.enumeration.WrSerialNoStateEnum;
import nc.vo.pub.BusinessException;
import nc.vo.pub.ISuperVO;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

public class WrApplyCheckAction extends WrBaseAction {
	private static final long serialVersionUID = 9171339325966721135L;

	public WrApplyCheckAction() {
		ActionInitializer.initializeAction(this,
				WrBtnConst.getBTN_CODE_WR_APPLY_CHECK(),
				WrBtnConst.getBTN_NAME_WR_APPLY_CHECK(),
				WrBtnConst.getBTN_TOOLTIP_WR_APPLY_CHECK());
	}

	public void doAction(ActionEvent e) throws Exception {
		AggWrVO[] aggWrVOs = getSelectAggWrVOs();

		if (MMValueCheck.isEmpty(aggWrVOs)) {
			return;
		}

		/**
		 * 报检
		 * */
		String pk_org = aggWrVOs[0].getParentVO().getPk_org();
		if (this.getProcessService().isOutSystem(pk_org)) {
			this.PushLims(aggWrVOs);
			return;
		}

		super.doAction(e);
		WrModuleEnableValidator wrModuleEnableValidator = new WrModuleEnableValidator();
		wrModuleEnableValidator.checkQcModule(aggWrVOs);

		Map returnMessageMap = null;

		if (aggWrVOs.length == 1) {
			returnMessageMap = processSingleData(aggWrVOs[0]);
		} else {
			returnMessageMap = appleCheck(aggWrVOs);
		}
		WrUIHelper.refresh(aggWrVOs, getModel());

		if ((!(MMValueCheck.isNotEmpty(returnMessageMap)))
				|| (!(returnMessageMap.containsKey("cancle")))) {
			List message = WrUIOperateUtil
					.formatReturnMessage(returnMessageMap);

			if (MMValueCheck.isEmpty(message)) {
				ShowStatusBarMsgUtil.showStatusBarMsg(WrptLangConst
						.APPLYCHECK_SUCCESS_MSG(), getModel().getContext());
			} else {
				ExceptionUtils.wrappException(new ValidationException(message));
			}
		}
	}

	protected boolean isActionEnable() {
		if (UIState.NOT_EDIT != getModel().getUiState()) {
			return false;
		}

		Object[] selDatas = getModel().getSelectedOperaDatas();
		if ((null == selDatas) || (selDatas.length <= 0)) {
			return false;
		}
		if (selDatas.length > 1) {
			return true;
		}
		AggWrVO agg = (AggWrVO) selDatas[0];

		if (!(WrBillStatusEnum.COMMITE.equalsValue(agg.getParentVO()
				.getFbillstatus()))) {
			return false;
		}
		/* *
		 * edit by xuchong 2022年9月28日 注释 23组织 报告不支持报检 28组织支持 这里都放开
		 * 在doaction里校验做提醒 try { if(this.getProcessService().isOutSystem(
		 * agg.getParentVO().getPk_org())){//如果是外系统质检 return false; } } catch
		 * (Exception e) { e.printStackTrace(); }
		 */
		return ((!(getBillForm().isShowing())) || (!(getBillForm()
				.getBillCardPanel().getBillTable("pick").isShowing())));
	}

	public AggWrVO[] getSelectAggWrVOs() {
		return new WrUIHelperPatch(getModel(), getBillForm())
				.getProductSelDatas();
	}

	private Map<String, String> processSingleData(AggWrVO aggWrVO) {
		WrItemNullValidator wrItemNullValidator = new WrItemNullValidator();
		wrItemNullValidator.checkWrItemVO(aggWrVO);
		ISuperVO[] wrItems = aggWrVO.getChildren(WrItemVO.class);
		if (MMArrayUtil.isEmpty(wrItems)) {
			return null;
		}
		if (wrItems.length == 1) {
			WrApplyCheckValidator wrApplyCheckValidator = new WrApplyCheckValidator();
			wrApplyCheckValidator.check(aggWrVO, getModel());
		}

		if (isWrApplyChecked(aggWrVO)) {
			int dialog = MessageDialog.showYesNoDlg(getModel().getContext()
					.getEntranceUI(), WrptLangConst.getHIT_OK(), WrptLangConst
					.getApplyCheckHIT_HASAPPLY());

			if (dialog == 4) {
				return appleCheck(new AggWrVO[] { aggWrVO });
			}

			Map rstMap = new HashMap();
			rstMap.put("cancle", "");
			return rstMap;
		}

		return appleCheck(new AggWrVO[] { aggWrVO });
	}

	private boolean isWrApplyChecked(AggWrVO aggWrVO) {
		WrItemVO[] wrItemVOs = (WrItemVO[]) (WrItemVO[]) aggWrVO
				.getChildren(WrItemVO.class);

		for (WrItemVO wrItemVO : wrItemVOs) {
			WrSerialNoVO[] snvos = wrItemVO.getSerialnovos();
			if (MMArrayUtil.isNotEmpty(snvos)) {
				for (WrSerialNoVO snvo : snvos) {
					if (WrSerialNoStateEnum.FREE.equalsValue(snvo
							.getFsserialnostatus())) {
						return false;
					}
				}
			}
			if (MMNumberUtil.isGtZero(wrItemVO.getNbsldchecknum())) {
				return true;
			}
		}
		return false;
	}

	private Map<String, String> appleCheck(AggWrVO[] aggWrVOs) {
		WrBusinessServiceProxy wrBusinessServiceProxy = new WrBusinessServiceProxy();
		try {
			return wrBusinessServiceProxy.applyCheck(aggWrVOs);
		} catch (BusinessException e) {
			ExceptionUtils.wrappException(e);
		}
		return null;
	}

	private IProcessService iuap;

	private IProcessService getProcessService() {
		if (null == iuap) {
			iuap = (IProcessService) NCLocator.getInstance().lookup(
					IProcessService.class.getName());
		}
		return iuap;
	}

	/**
	 * 报检
	 * 
	 * @throws BusinessException
	 * */
	private void PushLims(AggWrVO[] aggWrVOs) throws BusinessException {
		if (aggWrVOs.length > 1) {
			ExceptionUtils.wrappBusinessException("请逐条报检!");
		}
	
		// 判断是否为28组织 非28组织提示不支持报检
		 //如果是药物科技则走药物科技的报检
   	  if(!"0001V110000000012E56".equalsIgnoreCase(aggWrVOs[0].getParentVO().getPk_org())){
			ShowStatusBarMsgUtil.showErrorMsg("提醒", "该组织完工报告不支持外系统报检!", this
					.getModel().getContext());
			return;
		}
		String fun_type = "TH_LIMS_WR_CHECK";
		ISysDispatcherThLims outerService = (ISysDispatcherThLims) NCLocator.getInstance()
				.lookup(ISysDispatcherThLims.class.getName());
		Map<String, Object> param = new HashMap<String, Object>();
		// param.put("opetype", "手动报检");
		AggWrVO aggWrVO = (AggWrVO) outerService.dispatch(aggWrVOs[0],
				fun_type, param);
//		new ClientBillCombinServer().combine(aggWrVOs,
//				new AggWrVO[] { aggWrVO });
//		this.getModel().directlyUpdate(aggWrVOs);
		WrUIHelper.refresh(aggWrVOs, getModel());

		ShowStatusBarMsgUtil.showStatusBarMsg("LIMS报检成功", getModel()
				.getContext());
		return;
	}
}