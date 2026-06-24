package nc.ui.so.tmprint.pub.action;

import java.awt.event.ActionEvent;

import javax.swing.JOptionPane;

import nc.sfbase.client.ClientToolKit;
import nc.ui.pub.FuncNodeStarter;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.linkoperate.ILinkType;
import nc.ui.pubapp.uif2app.model.BillManageModel;
import nc.ui.pubapp.uif2app.view.ShowUpableBillForm;
import nc.ui.uap.sf.SFClientUtil;
import nc.ui.uif2.NCAction;
import nc.vo.sm.funcreg.FuncRegisterVO;
import nc.vo.tmprint.tmprint.LinkAddData;

/**
 * 产成品入库单、采购入库单、期初余额-物料条码打印按钮
 * @author tts20
 *
 */
public class TmprintAction extends NCAction{
	private ShowUpableBillForm editorModel;
	private BillManageModel model;
	public TmprintAction() {
		super();
		setBtnName("物料标签打印");
		setCode("TmprintAction");
	}
	@Override
	public void doAction(ActionEvent e) throws Exception {
		String billtype = getModel().getBillType();
        // 获得所选择的行号
        int[] selectRows = this.getEditorModel().getBillCardPanel().getBillTable().getSelectedRows();
        String bids = "";
        for (int i = 0; i < selectRows.length; i ++){
        	Object o = this.getEditorModel().
        			getBillCardPanel().getBillModel().getValueAt(selectRows[i], "4331".equals(billtype)?"cdeliverybid":"csaleorderbid");
        	String bid = o == null ? "" : o.toString();
        	bids = bids + "," + bid;
        }
        if(bids == null || "".equals(bids)){
        	JOptionPane.showMessageDialog(null, "请选中表体数据后再打印");
			return;
        }
        if(!"4331".equals(billtype)){
        	billtype="30";
        }
        LinkAddData querydata = new LinkAddData();
        querydata.setCsourcebillid(bids.substring(1));
        querydata.setCsourcebilltype(billtype);
        querydata.setPk_org(this.getEditorModel().getBillCardPanel().getHeadItem("pk_org").getValueObject().toString());
//        SFClientUtil.openLinkedADDDialog("40H120", getModel().getContext().getEntranceUI(), querydata);
//        SFClientUtil.openNodeLinkedADD("40H120", querydata);
        String funCode="40H120";
        FuncRegisterVO frVO = SFClientUtil.findFRVOFromWorkbenchEnvironment(funCode);
        if (frVO != null) {
            FuncNodeStarter.openLinkedTabbedPane(frVO, ILinkType.LINK_TYPE_ADD, querydata, null, true, null);
        } else {
            MessageDialog.showErrorDlg(ClientToolKit.getApplet(), nc.ui.ml.NCLangRes.getInstance().getStrByID("sysframev5", "UPPsysframev5-000062")/* @res "错误" */, nc.ui.ml.NCLangRes.getInstance().getStrByID("sysframev5", "UPPsysframev5-000095")/* @res "没有打开此节点的权限 . 节点号=" */
                    + funCode);
        }
	}
	public BillManageModel getModel() {
		return model;
	}
	public void setModel(BillManageModel model) {
		this.model = model;
	}
	public ShowUpableBillForm getEditorModel() {
		return editorModel;
	}
	public void setEditorModel(ShowUpableBillForm editorModel) {
		this.editorModel = editorModel;
	}
}
