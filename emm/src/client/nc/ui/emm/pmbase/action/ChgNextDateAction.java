/** <a href="http://www.cpupk.com/decompiler">Eclipse Class Decompiler</a> plugin, Copyright (c) 2017 Chen Chao. */
package nc.ui.emm.pmbase.action;
import java.awt.event.ActionEvent;

import nc.ui.am.action.info.ActionInfoInitalizer;
import nc.ui.am.action.info.IAMActionCode;
import nc.ui.emm.pmbase.action.ChgNextInfoAction;
import nc.ui.emm.pmbase.dlg.ChgNextWoDateDlg;
import nc.ui.emm.pmbase.model.PMModelService;
import nc.ui.pub.beans.UIDialog;
import nc.vo.am.common.BizContext;
import nc.vo.am.common.util.EqualsBuilder;
import nc.vo.emm.premaintain.PMBillVO;
import nc.vo.emm.premaintain.PMHeadVO;
import nc.vo.emm.premaintain.utils.PMCommonUtils;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFLiteralDate;

/**
 * <p>
 * 时间型预防性维护更改下一工单目标日期按钮。
 * </p>
 *
 * @author cuikai
 * @version 6.0
 */
public class ChgNextDateAction extends ChgNextInfoAction {

	private static final long serialVersionUID = 1L;

	public ChgNextDateAction() {
		super();
		ActionInfoInitalizer.initializeAction(this, IAMActionCode.ChgNextDate);
	}

	@Override
	public void doAction(ActionEvent e) throws Exception {
		// 从缓存中取得值
		PMBillVO billVO = (PMBillVO) getModel().getSelectedData();
		// 校验
		validate(billVO);
		PMHeadVO headVO = billVO.getParentVO();
		// 将界面上的下一工单目标日期设置到对话框中
		UFDate oldWoDate = headVO.getNext_start_date();
		// 将界面上的调整后目标日期设置到对话框中
		UFDate adjustDate = headVO.getAdjust_start_date();

		// 校验
		if (oldWoDate == null && adjustDate == null) {
			showErrorMessage(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("pmbase_0", "04550002-0056")/*
																												 * @res
																												 * "下一工单目标日期或调整后目标日期不能为空,不能调整"
																												 */);
			return;
		}

		// 弹出对话框
		ChgNextWoDateDlg chgNextWoDateDlg = new ChgNextWoDateDlg(getToftPanelAdaptor(), headVO);
		if (UIDialog.ID_OK == chgNextWoDateDlg.showModal()) {
			// 从调整下一工单状态日期对话框中取出新的日期并更改界面上的值
			Object newChgDate = chgNextWoDateDlg.getNewDateComp().getValueObj();
			// 是否永久调整久
			Object persistFlag = chgNextWoDateDlg.getAdjustCBox().isSelected();
			// 调整人和调整日期
			PMBillVO billVONew = (PMBillVO) billVO.clone();
			PMHeadVO headVONew = billVONew.getParentVO();
			headVONew.setStatus(VOStatus.UPDATED);
			if (EqualsBuilder.isNull(newChgDate)) {
				// 如果调整后目标日期不为空，则新调整目标日期可为空（即清除掉调整日期），
				// 确定后，清空预防性维护界面的调整后目标日期和永久调整字段内容，同时记录调整人和调整日期
				headVONew.setAdjust_start_date(null);
				headVONew.setPersist_flag(UFBoolean.FALSE);
			} else {
				// 日期转换
				UFDate newDate = new UFDate(((UFLiteralDate)newChgDate).toString());
				headVONew.setAdjust_start_date(newDate.asLocalBegin());
				headVONew.setPersist_flag(UFBoolean.valueOf(persistFlag.toString()));
				// 提前天数
				int aheadDays = headVONew.getAhead_days();
				headVONew.setNext_create_date(newDate.asLocalBegin().getDateBefore(aheadDays));
			}
			// 设置调整人
			headVONew.setPk_adjuster(getModel().getContext().getPk_loginUser());
			// 设置调整日期
			headVONew.setAdjust_date(BizContext.getInstance().getBizDateTime());
			// 20200715 yezhian TORayBow 调整下一个工单的目标日期,下一个工单生成日期，下一个工单结束日期
			this.sendDate(headVONew);
			// 调用服务更新下一工单目标日期到数据库中
			((PMModelService)getModel().getService()).chgNextDate(billVONew);
			// 直接更新到界面上
			getModel().directlyUpdate(billVONew);
			// 提示信息
			showHintMessage(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("pmbase_0","04550002-0087")/*@res "调整下一工单目标日期成功"*/);
		}
	}

	private void sendDate(PMHeadVO headVONew) {
		// TODO Auto-generated method stub
		UFDate adjust_date = headVONew.getAdjust_start_date();
		Integer def1 =  Integer.parseInt(headVONew.getDef1()== null ? "0" : headVONew.getDef1()); //允差
		headVONew.setNext_start_date(adjust_date);
		//下一工单创建日期 = 下一工单开始日期 - 提前天数
		headVONew.setNext_create_date(PMCommonUtils.getDateByFre(
				(headVONew.getAhead_days() * -1)
				,0
				, adjust_date));
		if (isSpecialEquipmentPM(headVONew)) {
			// 特种设备预防性维护：DEF2 = adjust_date（不加允差）
			headVONew.setDef2(adjust_date == null ? null : adjust_date.toString());
		} else {
			//下一个工单计划结束日期 = 下一个工单开始日期 + 允差
			headVONew.setDef2(PMCommonUtils.getDateByFre(
					def1
					,0
					, adjust_date).toString());
		}
	}

	private boolean isSpecialEquipmentPM(PMHeadVO headVO) {
		if (headVO == null || headVO.getTransi_type() == null) {
			return false;
		}
		return "4B72-Cxx-02".equals(headVO.getTransi_type().trim());
	}
	
	

}