/** <a href="http://www.cpupk.com/decompiler">Eclipse Class Decompiler</a> plugin, Copyright (c) 2017 Chen Chao. **/
/*     */package nc.ui.ic.m4460.action;
/*     */
/*     */import java.awt.event.ActionEvent;
import java.util.List;



/*     */
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Log;
/*     */
import nc.itf.ic.m4460.IStateAdjustMaintain;
/*     */
import nc.ui.ic.m4460.model.StateAdjustModel;
/*     */
import nc.ui.ml.NCLangRes;
/*     */
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIDialog;
/*     */
import nc.ui.pub.bill.BillListPanel;
/*     */
import nc.ui.pub.bill.BillModel;
/*     */
import nc.ui.pubapp.uif2app.view.BillListView;
/*     */
import nc.ui.querytemplate.querytree.IQueryScheme;
/*     */
import nc.ui.scmpub.action.SCMActionInitializer;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.uif2.AccreditLoginDialog;
/*     */
import nc.ui.uif2.NCAction;
/*     */
import nc.ui.uif2.UIState;
import nc.ui.uif2.model.AbstractUIAppModel;
import nc.uif.pub.exception.UifException;
/*     */
import nc.vo.ic.m4460.entity.StateAdjustVO;
import nc.vo.ic.onhand.entity.OnhandDimVO;
/*     */
import nc.vo.ic.pub.util.NCBaseTypeUtils;
/*     */
import nc.vo.ic.pub.util.StringUtil;
/*     */
import nc.vo.ic.pub.util.ValueCheckUtil;
import nc.vo.ic.storestate.StoreStateVO;
/*     */
import nc.vo.ml.AbstractNCLangRes;
/*     */
import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
/*     */
import nc.vo.pub.lang.UFDouble;
import nc.vo.riasm.electronicsignature.ElectronicSignatureVO;
import nc.vo.riasm.electronicsignature.IconstEnum;
import nc.vo.scmf.ic.mbatchcode.BatchcodeVO;
import nc.vo.sm.UserVO;
import nc.vo.uif2.LoginContext;
/*     */
/*     */public class StateAdjustSaveAction extends NCAction
/*     */{
	/*     */private static final long serialVersionUID = 510372490113110846L;
	/*     */private StateAdjustOnhandQueryAction queryAction;
	/*     */private BillListView list;
	/*     */private StateAdjustModel model;
	/*     */
	/*     */public StateAdjustSaveAction()
	/*     */{
		/*  41 */SCMActionInitializer.initializeAction(this, "Confirm");
		/*     */}
	/*     */
	/*     */public void doAction(ActionEvent e)
	/*     */throws Exception
	/*     */{
		/*  47 */StateAdjustVO[] vos = (StateAdjustVO[]) (StateAdjustVO[]) getList()
				.getBillListPanel().getHeadBillModel()
				.getBodyValueVOs(StateAdjustVO.class.getName());
		/*     */
		
		/*  51 */if (ValueCheckUtil.isNullORZeroLength(vos)) {
			/*  52 */MessageDialog.showErrorDlg(
					null,
					NCLangRes4VoTransl.getNCLangRes().getStrByID("4008009_0",
							"04008009-0000"),
					NCLangRes4VoTransl.getNCLangRes().getStrByID("4008009_0",
							"04008009-0001"));
			/*     */
			/*  59 */return;
			/*     */}
		/*  61 */String err = checNumBeforeSave(vos);
		/*  62 */if (!(StringUtil.isSEmptyOrNull(err))) {
			/*  63 */MessageDialog.showErrorDlg(
					null,
					NCLangRes4VoTransl.getNCLangRes().getStrByID("4008009_0",
							"04008009-0003"),
					NCLangRes4VoTransl.getNCLangRes().getStrByID("4008009_0",
							"04008009-0004")
							+ err);
			/*     */
			/*  69 */return;
			/*     */}
		/*     */
		/*  72 */err = checkCstatidBeforeSave(vos);
		/*  73 */if (!(StringUtil.isSEmptyOrNull(err))) {
			/*  74 */MessageDialog.showErrorDlg(
					null,
					NCLangRes4VoTransl.getNCLangRes().getStrByID("4008009_0",
							"04008009-0003"),
					NCLangRes4VoTransl.getNCLangRes().getStrByID("4008009_0",
							"04008009-0043")
							+ err);
			/*     */
			/*  80 */return;
			/*     */}
		/*     */
		boolean ismust = false;
		for(StateAdjustVO vo:vos){
			OnhandDimVO dimvo = (OnhandDimVO) HYPubBO_Client.queryByPrimaryKey(
					OnhandDimVO.class, vo.getPk_onhanddim());
			if (dimvo == null) {
				throw new BusinessException("现存量维度不存在");
			}
			if(!nc.vo.jcom.lang.StringUtil.isEmpty(dimvo.getCstateid())){
				StoreStateVO statevo = getStateVO(dimvo.getCstateid());
				if (statevo != null) {
					if("不合格".equals(statevo.getVname())){
						if(!nc.vo.jcom.lang.StringUtil.isEmpty(vo.getCadjuststateid())){
							StoreStateVO state1 =  getStateVO(vo.getCadjuststateid());
							if("合格".equals(state1.getVname())){
								ismust =true;
							}
						}
					}
				}
			}
		}
		boolean ischeck = checkElectronicSignature(model.getContext(), "确认", model, ismust);
		if (!ischeck)
			return;
		
		StateAdjustVO[] newvos =((IStateAdjustMaintain) NCLocator.getInstance().lookup(
				IStateAdjustMaintain.class)).stateAdjust(vos);
		for(StateAdjustVO vo: newvos){
			updatebillId("保存",vo.getPk_onhanddim_adj());
		}
			
		/*     */
		/*  87 */getModel().setUiState(UIState.NOT_EDIT);
		/*  88 */getList().getBillListPanel().setMultiSelect(true);
		/*  89 */getList().getBillListPanel().setEnabled(false);
		/*  90 */IQueryScheme queryScheme = getModel().getQueryScheme();
		/*  91 */if (null != queryScheme)
			/*  92 */getQueryAction().executeQuery(queryScheme);
		/*     */}
	// 库存状态
		private StoreStateVO getStateVO(String value) throws UifException {
			StoreStateVO bvo = (StoreStateVO) HYPubBO_Client.queryByPrimaryKey(
					StoreStateVO.class, value);
			return bvo;
		}
	public boolean checkElectronicSignature(LoginContext context, String cmd,
			AbstractUIAppModel model,boolean ismust) {
		if (context == null)
			return true;
		String nodecode = context.getNodeCode();
		Log.getInstance(this.getClass()).info("功能节点号：" + nodecode);
		
		List<ElectronicSignatureVO> list = getListElect();
		if (list == null || list.size() == 0)
			return true;
		ElectronicSignatureVO bill = null;
		for (ElectronicSignatureVO vo : list) {
			if (nodecode.equals(vo.getPk_parent())) {
				bill = vo;
				break;
			}
		}
		boolean ischeck = IconstEnum.isCheckElectronicSignature(bill, cmd);
		if (!ischeck)
			return true;
		// 校验录入的签名
		AccreditLoginDialog dialog = null;
		try {
			Object o = model.getSelectedData();
			String billid = null;
			SuperVO vo1 = null;
			if (o instanceof AggregatedValueObject) {
				AggregatedValueObject aggvo = (AggregatedValueObject) o;
				billid = aggvo.getParentVO().getPrimaryKey();
			} else if (o instanceof SuperVO) {
				vo1 = (SuperVO) o;
				billid = vo1.getPrimaryKey();
			}
			dialog = new AccreditLoginDialog(context.getEntranceUI(), context,
					cmd, billid,ismust);

			UserVO vo = (UserVO) HYPubBO_Client.queryByPrimaryKey(UserVO.class,
					context.getPk_loginUser());
			dialog.getTfUser().setText(vo.getUser_code());
			dialog.getAction().setText(cmd);
			if (dialog.showModal() == UIDialog.ID_OK) {
				setPk(dialog.getPk());
				return true;
			} else {
				return false;
			}
		} catch (UifException e) {
			e.printStackTrace();
		} catch (BusinessException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		return false;
	}
	/*     */private String checNumBeforeSave(StateAdjustVO[] vos)
	/*     */{
		/* 103 */StringBuffer errMsg = new StringBuffer();
		/* 104 */int pos = 1;
		/* 105 */label104 : for (StateAdjustVO vo : vos) {
			/* 106 */if (!(NCBaseTypeUtils.isLEZero(vo.getNadjustnum())))
				if (!(NCBaseTypeUtils.isGt(
						vo.getNadjustnum(),
						NCBaseTypeUtils.sub(vo.getNnum(),
								new UFDouble[]{vo.getNlocknum()})))) {
					/*     */break label104;
					/*     */}
			/* 109 */errMsg.append(NCLangRes.getInstance().getStrByID(
					"4008009_0", "04008009-0042", null,
					new String[]{String.valueOf(pos)}));
			/*     */
			/* 114 */++pos;
			/*     */}
		/* 116 */return errMsg.toString();
		/*     */}
	/*     */
	/*     */private String checkCstatidBeforeSave(StateAdjustVO[] vos)
	/*     */{
		/* 126 */StringBuffer errMsg = new StringBuffer();
		/* 127 */int pos = 1;
		/* 128 */for (StateAdjustVO vo : vos) {
			/* 129 */if (StringUtil.isSEmptyOrNull(vo.getCadjuststateid())) {
				/* 130 */errMsg.append(NCLangRes.getInstance().getStrByID(
						"4008009_0", "04008009-0042", null,
						new String[]{String.valueOf(pos)}));
				/*     */}
			/*     */
			/* 135 */++pos;
			/*     */}
		/* 137 */return errMsg.toString();
		/*     */}
	/*     */
	/*     */public StateAdjustOnhandQueryAction getQueryAction() {
		/* 141 */return this.queryAction;
		/*     */}
	/*     */
	/*     */public void setQueryAction(
			StateAdjustOnhandQueryAction queryAction) {
		/* 145 */this.queryAction = queryAction;
		/*     */}
	/*     */
	/*     */public BillListView getList() {
		/* 149 */return this.list;
		/*     */}
	/*     */
	/*     */public void setList(BillListView list) {
		/* 153 */this.list = list;
		/*     */}
	/*     */
	/*     */public void setModel(StateAdjustModel model) {
		/* 157 */this.model = model;
		/* 158 */model.addAppEventListener(this);
		/*     */}
	/*     */
	/*     */public StateAdjustModel getModel() {
		/* 162 */return this.model;
		/*     */}
	/*     */
}