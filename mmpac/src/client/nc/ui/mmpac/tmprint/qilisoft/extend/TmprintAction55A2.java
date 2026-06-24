package nc.ui.mmpac.tmprint.qilisoft.extend;

import java.awt.event.ActionEvent;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;

import nc.sfbase.client.ClientToolKit;
import nc.ui.pubapp.uif2app.AppUiState;
import nc.ui.pubapp.uif2app.model.BillManageModel;
import nc.ui.pubapp.uif2app.view.BillForm;
import nc.ui.pub.FuncNodeStarter;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.linkoperate.ILinkType;
import nc.ui.uap.sf.SFClientUtil;
import nc.ui.uif2.NCAction;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.mmpac.pmo.pac0002.entity.PMOAggVO;
import nc.vo.org.OrgVO;
import nc.vo.sm.funcreg.FuncRegisterVO;
import nc.vo.tmprint.tmprint.LinkAddData;

/**
 * 生产订单-物料条码打印按钮
 * @author tts20
 *
 */
public class TmprintAction55A2 extends NCAction{
	private BillForm editorModel;
	private BillManageModel model;
	private String typevalue;
	public String getTypevalue() {
		return typevalue;
	}
	public void setTypevalue(String typevalue) {
		this.typevalue = typevalue;
	}
	public TmprintAction55A2() {
		super();
		setBtnName("预标签打印-23");
		setCode("TmprintAction");
	}
	@Override
	public void doAction(ActionEvent e) throws Exception {
		String billtype = getTypevalue();
        // 获得所选择的行号
        int[] selectRows = this.getEditorModel().getBillCardPanel().getBillTable().getSelectedRows();
        String bids = "";
        for (int i = 0; i < selectRows.length; i ++){
        	Object o = this.getEditorModel().
        			getBillCardPanel().getBillModel().getValueAt(selectRows[i], "cmoid");
        	String bid = o == null ? "" : o.toString();
        	bids = bids + "," + bid;
        }
        if(bids == null || "".equals(bids)){
        	MessageDialog.showHintDlg(getModel().getContext().getEntranceUI(), "提示", "请选中表体数据后再打印");
			return;
        }
        LinkAddData querydata = new LinkAddData();
        querydata.setCsourcebillid(bids.substring(1));
        querydata.setCsourcebilltype(billtype);
        querydata.setPk_org(this.getEditorModel().getBillCardPanel().getHeadItem("pk_org").getValueObject().toString());

       // SFClientUtil.openLinkedADDDialog("40H120", getModel().getContext().getEntranceUI(), querydata);
//        SFClientUtil.openNodeLinkedADD("40H120", querydata);
        String funCode="40H192";
        FuncRegisterVO frVO = SFClientUtil.findFRVOFromWorkbenchEnvironment(funCode);
        if (frVO != null) {
            FuncNodeStarter.openLinkedTabbedPane(frVO, ILinkType.LINK_TYPE_ADD, querydata, null, true, null);
        } else {
            MessageDialog.showErrorDlg(ClientToolKit.getApplet(), nc.ui.ml.NCLangRes.getInstance().getStrByID("sysframev5", "UPPsysframev5-000062")/* @res "错误" */, nc.ui.ml.NCLangRes.getInstance().getStrByID("sysframev5", "UPPsysframev5-000095")/* @res "没有打开此节点的权限 . 节点号=" */
                    + funCode);
        }
	}
	public BillForm getEditorModel() {
		return editorModel;
	}
	public void setEditorModel(BillForm editorModel) {
		this.editorModel = editorModel;
	}
	public BillManageModel getModel() {
		return model;
	}
	public void setModel(BillManageModel model) {
		this.model = model;
		this.model.addAppEventListener(this);
	}
	
	@Override
	protected boolean isActionEnable() {
		if (this.getModel().getAppUiState() == AppUiState.EDIT
				|| this.getModel().getSelectedData() == null) {
			return true;
		}
		
		PMOAggVO vo = (PMOAggVO)this.getModel().getSelectedData();
		OrgVO org = getOrgVO(vo.getParentVO().getPk_org());
		if (org == null || StringUtil.isEmpty(org.getPk_org())) {
			return false;
		}

		if (!"23".equals(org.getCode())) {
			return false;
		}

		return super.isActionEnable();
	}

	private OrgVO getOrgVO(String pk_org) {
		List<OrgVO> list = getModel().getContext().getOrgvos();
		for (OrgVO org : list) {
			if (org.getPk_org().equals(pk_org))
				return org;
		}
		return null;
	}
	
}
