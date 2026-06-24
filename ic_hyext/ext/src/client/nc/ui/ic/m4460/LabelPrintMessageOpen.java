package nc.ui.ic.m4460;

import java.awt.Container;
import java.util.List;

import nc.bs.framework.common.NCLocator;
import nc.desktop.ui.WorkbenchEnvironment;
import nc.funcnode.ui.FuncletInitData;
import nc.funcnode.ui.FuncletLinkEvent;
import nc.funcnode.ui.FuncletLinkListener;
import nc.funcnode.ui.FuncletWindowLauncher;
import nc.itf.ic.onhand.OnhandResService;
import nc.message.msgcenter.event.AbstractMessageTypeInfo;
import nc.message.msgcenter.event.IStateChangeProcessor;
import nc.message.vo.MessageVO;
import nc.message.vo.NCMessage;
import nc.pubitf.ic.onhand.IOnhandQry;
import nc.ui.am.util.FuncnodeUtils;
import nc.vo.am.common.util.StringUtils;
import nc.vo.ic.m4460.entity.StateAdjustVO;
import nc.vo.ic.m4460.util.StateAdjustOnhandUtil;
import nc.vo.ic.onhand.entity.OnhandDimVO;
import nc.vo.ic.onhand.entity.OnhandVO;
import nc.vo.ic.pub.util.ValueCheckUtil;
import nc.vo.org.GroupVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.sm.funcreg.FuncRegisterVO;

public class LabelPrintMessageOpen extends AbstractMessageTypeInfo {
	public void processMsgOpen(NCMessage ncmsg, Container parent,
			IStateChangeProcessor processor) {
		String pk_detail = ncmsg.getMessage().getPk_detail();
		ncmsg.getMessage().setIsread(UFBoolean.TRUE);
		ncmsg.getMessage().setIshandled(UFBoolean.TRUE);
		processor.processStateChange(ncmsg);
		// BilltypeVO billtypeVO = PfUIDataCache
		// .getBillType(new BillTypeCacheKey().buildBilltype("4611")
		// .buildPkGroup(getLoginGroup()));

		// PfLinkData linkData = new PfLinkData();
		// linkData.setBillID(pk_detail);
		// linkData.setBillType("4611");
		// linkData.setUserObject(null);
		// linkData.setPkOrg(ncmsg.getMessage().getPk_org());
		StateAdjustVO[] vos = null;
		try {
			List<StateAdjustVO> list = getStateAdjustVO(ncmsg);
			vos = list.toArray(new StateAdjustVO[list.size()]);
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		FuncletInitData initData = new FuncletInitData();
		initData.setInitData(vos);
		FuncRegisterVO funvo = WorkbenchEnvironment.getInstance()
				.getFuncRegisterVO("40081009");

		if (!FuncnodeUtils.checkFuncnode(funvo, "40081009")) {
			return;
		}

		if (StringUtils.equals(ncmsg.getMessage().getPk_group(),
				getLoginGroup())) {
			FuncletLinkListener linkListener = new FuncletLinkListener() {
				public void dealLinkEvent(FuncletLinkEvent event) {
					if (event.getID() == 0) {
					}
				}
			};
			FuncletWindowLauncher.openFuncNodeInTabbedPane(parent, funvo,
					initData, linkListener, false, null);
		}
	}

	private String getLoginGroup() {
		GroupVO gVO = WorkbenchEnvironment.getInstance().getGroupVO();
		String pkGroup = gVO == null ? null : gVO.getPk_group();
		return pkGroup;
	}

	private List<StateAdjustVO> getStateAdjustVO(NCMessage ncmsg)
			throws BusinessException {
		MessageVO msg = ncmsg.getMessage();

		// IOnhandQry onbandquery = (IOnhandQry) NCLocator.getInstance().lookup(
		// IOnhandQry.class);
		// OnhandQryCond cond = new OnhandQryCond();
		// cond.setISSum(true);
		// cond.addSelectFields((String[]) CollectionUtils
		// .combineArrs(new String[][] {
		// OnhandDimVO.getDimContentFields(), { "pk_onhanddim" } }));
		//
		// cond.setWhere(where.toString());
		// onbandquery.queryOnhand(cond);
		IOnhandQry onhandquery = NCLocator.getInstance().lookup(
				IOnhandQry.class);
		OnhandDimVO[] onhandDims = onhandquery
				.queryOnhandDim(new String[] { msg.getPk_detail() });
		if (ValueCheckUtil.isNullORZeroLength(onhandDims)) {
			return null;
		}
		OnhandResService onhandquery1 = NCLocator.getInstance().lookup(
				OnhandResService.class);
		OnhandVO[] onhandvo = onhandquery1.queryOnhandVOByDims(onhandDims);

		List<StateAdjustVO> billList = new StateAdjustOnhandUtil()
				.onhand2StateAdjust(onhandvo, false, null);

		return billList;
	}
}