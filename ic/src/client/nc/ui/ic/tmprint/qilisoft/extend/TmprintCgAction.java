package nc.ui.ic.tmprint.qilisoft.extend;

import java.awt.event.ActionEvent;

import javax.swing.JOptionPane;

import nc.sfbase.client.ClientToolKit;
import nc.ui.pu.pub.view.PUBillForm;
import nc.ui.pu.uif2.PUBillManageModel;
import nc.ui.pub.FuncNodeStarter;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.linkoperate.ILinkType;
import nc.ui.uap.sf.SFClientUtil;
import nc.ui.uif2.NCAction;
import nc.vo.sm.funcreg.FuncRegisterVO;
import nc.vo.tmprint.tmprint.LinkAddData;

/**
 * 采购订单、到货单-物料条码打印按钮
 * @author tts20
 *
 */
public class TmprintCgAction extends NCAction{
	private PUBillForm editor;
	private PUBillManageModel model;
	public TmprintCgAction() {
		super();
		setBtnName("物料标签打印");
		setCode("TmprintAction");
	}
	@Override
	public void doAction(ActionEvent e) throws Exception {
		String billtype = getModel().getBillType();
        // 获得所选择的行号
        int[] selectRows = this.getEditor().getBillCardPanel().getBillTable().getSelectedRows();
        String bids = "";
        String field = "";
        if("21".equals(billtype)){
        	field = "pk_order_b";
        }else if("23".equals(billtype)){
        	field = "pk_arriveorder_b";
        }
        for (int i = 0; i < selectRows.length; i ++){
        	Object o = this.getEditor().getBillCardPanel().getBillModel().getValueAt(selectRows[i],field );
        	String bid = o == null ? "" : o.toString();
        	bids = bids + "," + bid;
        }
        if(bids == null || "".equals(bids)){
        	JOptionPane.showMessageDialog(null, "请选中表体数据后再打印");
			return;
        }
        LinkAddData querydata = new LinkAddData();
        querydata.setCsourcebillid(bids.substring(1));
        querydata.setCsourcebilltype(billtype);
        querydata.setPk_org(this.getEditor().getBillCardPanel().getHeadItem("pk_org").getValueObject().toString());
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
	public PUBillForm getEditor() {
		return editor;
	}
	public void setEditor(PUBillForm editor) {
		this.editor = editor;
	}
	public PUBillManageModel getModel() {
		return model;
	}
	public void setModel(PUBillManageModel model) {
		this.model = model;
	}
}
