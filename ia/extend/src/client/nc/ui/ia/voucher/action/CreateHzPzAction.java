package nc.ui.ia.voucher.action;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.itf.fip.generate.IGenerateService;
import nc.itf.fip.opreatinglog.IOperatingLog;
import nc.ui.fip.pub.CodeGenerator;
import nc.ui.ia.voucher.model.BatchTableModel;
import nc.ui.ia.voucher.model.ModelService;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pubapp.uif2app.model.BatchBillTableModel;
import nc.ui.pubapp.uif2app.view.BatchBillTable;
import nc.ui.pubapp.util.CardPanelValueUtils;
import nc.ui.scmpub.util.NCActionProgressor;
import nc.ui.uif2.NCAction;
import nc.ui.uif2.ShowStatusBarMsgUtil;
import nc.vo.fip.operatinglogs.OperatingLogVO;
import nc.vo.fip.pub.SqlTools;
import nc.vo.ia.detailledger.para.ia.AuditPara;
import nc.vo.ia.detailledger.view.ia.AuditDisplayVO;
import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.uif2.LoginContext;

public class CreateHzPzAction extends NCAction {
	private static final long serialVersionUID = 1L;
	private BatchBillTable billTable;
	private BatchBillTableModel model;
	private ModelService service;

	public CreateHzPzAction() {
		this.setCode("CreateHzPz");
		this.setBtnName("汇总生成正式凭证");
	}

	public void doAction(ActionEvent e) {
		LoginContext context = getModel().getContext();

		String name = NCLangRes.getInstance().getStrByID("2014002_0",
				"02014002-0232");

		NCActionProgressor handler = new NCActionProgressor(this, "create",
				context);

		handler.process(name);
	}

	public BatchBillTableModel getModel() {
		return this.model;
	}

	public ModelService getService() {
		return this.service;
	}

	public void setBillTable(BatchBillTable billTable) {
		this.billTable = billTable;
	}

	public void setModel(BatchBillTableModel model) {
		this.model = model;
		model.addAppEventListener(this);
	}

	public void setService(ModelService service) {
		this.service = service;
	}

	protected boolean isActionEnable() {
		boolean isEnable = true;
		Object[] tempData = getModel().getSelectedOperaDatas();
		if (tempData == null) {
			isEnable = false;
		} else {
			String operate = ((BatchTableModel) getModel()).getOperate();
			if (operate.equals("cancel")) {
				isEnable = false;
			}
		}
		
		return isEnable;
	}

	private void create() {
		BillCardPanel card = this.billTable.getBillCardPanel();
		CardPanelValueUtils util = new CardPanelValueUtils(card);

		BatchBillTableModel model1 = getModel();
		Object[] tempData = model1.getSelectedOperaDatas();

		if ((tempData == null) || (tempData.length == 0)) {
			String msg = NCLangRes4VoTransl.getNCLangRes().getStrByID(
					"2014002_0", "02014002-0000");

			ExceptionUtils.wrappBusinessException(msg);
			return;
		}

		int auditsequence = ((AuditDisplayVO) tempData[0]).getIauditsequence()
				.intValue();

		boolean auditedflag = true;
		if (auditsequence == -1) {
			auditedflag = false;
		}

		String[] detailledgerid = new String[tempData.length];
		for (int i = 0; i < tempData.length; ++i) {
			detailledgerid[i] = ((AuditDisplayVO) tempData[i])
					.getCdetailledgerid();
		}
		String pk_org = util.getHeadTailStringValue("pk_org");
		String pk_book = util.getHeadTailStringValue("pk_book");
		String accountperiod = util.getHeadTailStringValue("caccountperiod");

		AuditPara para = new AuditPara();
		para.setAuditedflag(auditedflag);
		para.setPk_orgs(new String[] { pk_org });

		para.setPk_book(pk_book);

		para.setCaccountperiod(accountperiod);
		para.setCdetailledgerids(detailledgerid);
		this.service.createVoucher(para);

		Integer[] rows = model1.getSelectedOperaRows();
		util.delBodyLine(rows);

		deleteModelDate(rows);

		String msg = NCLangRes4VoTransl.getNCLangRes().getStrByID("2014002_0",
				"02014002-0048");

		ShowStatusBarMsgUtil.showStatusBarMsg(msg, getModel().getContext());
		
		//模拟单据生成   对临时凭证进行分组，并生成正式凭证
		try {
			IOperatingLog ip = (IOperatingLog) NCLocator.getInstance().lookup(
					IOperatingLog.class);
			String realwhere = SqlTools.getInStr("src_relationid",
					detailledgerid, true);
			OperatingLogVO[] rs = ip.queryByWhere(realwhere);
			if(rs != null && rs.length >0){
				for(OperatingLogVO vo:rs){
					//设置分组
					vo.setGroupid(CodeGenerator.getCode("" +1, 4));
				}
				 IGenerateService op = (IGenerateService)NCLocator.getInstance().lookup(IGenerateService.class);
				  op.generate(rs,null);
			}
			
			ShowStatusBarMsgUtil.showStatusBarMsg("生成汇总正式凭证完成", getModel().getContext());


		} catch (BusinessException e) {
			Logger.error("", e);
		}

	}

	private void deleteModelDate(Integer[] rows) {
		/* 178 */List list = new ArrayList();
		/* 179 */for (Integer row : rows) {
			/* 180 */Object ob = getModel().getRow(row.intValue());
			/* 181 */list.add(ob);
		}
		/* 183 */getModel().getRows().removeAll(list);

		/* 185 */getModel().setSelectedIndex(-1);
	}
}
