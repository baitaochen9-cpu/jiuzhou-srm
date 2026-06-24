package nc.ui.mmpac.pmo.pac0002.action;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;

import nc.bs.framework.common.NCLocator;
import nc.bs.jzyy.sys.thlims.LimsIpcVO;
import nc.bs.trade.business.HYPubBO;
import nc.itf.jzyy.sys.thlims.ISysDispatcherThLims;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillEditListener;
import nc.ui.pub.bill.BillEditListener2;
import nc.ui.pubapp.uif2app.FuncletDialog;
import nc.ui.pubapp.util.CardPanelValueUtils;
import nc.ui.uif2.NCAction;
import nc.ui.uif2.editor.BillForm;
import nc.uif.pub.exception.UifException;
import nc.vo.mmpac.pmo.pac0002.entity.PMOAggVO;
import nc.vo.mmpac.pmo.pac0002.entity.PMOItemVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.pattern.model.entity.bill.AbstractBill;
import nc.vo.pubapp.util.VOSortUtils;

import org.apache.commons.lang3.StringUtils;



public class IPC_Dialog extends FuncletDialog implements ActionListener, BillEditListener ,BillEditListener2{

	private static final long serialVersionUID = 1L;

	// 命令按钮Panel
	private UIPanel ivjCmdPanel = null;

	private BillCardPanel billCardPanel = null;

	private JPanel btnPanel = new JPanel();

	private UIButton ivjbtnSave = null;

	private UIButton ivjbtnClose = null;

	// 列表和命令按钮panel的容器panel
	private nc.ui.pub.beans.UIPanel ivjPanel = null;

	private NCAction cardRefreshAction;

	// 上游BillForm editor
	private BillForm editor = null;

	private ActionEvent e = null;

	private String pk_templetid = null;
	//主键
	private String primaryKey = null;

	private PMOAggVO pmoAggVO;
	

	public IPC_Dialog(Container parent, String title) {
		super(parent);
	}

	public IPC_Dialog(Container parent, String title, AbstractBill djvo,String pk_templetid) {
		super(parent);
		this.editor = (BillForm) parent;
		initialize(title, pk_templetid);
		
		try {
			this.initDate(djvo);
		} catch (BusinessException e) {
			MessageDialog.showErrorDlg(this, "错误", "初始化数据错误!!");
			e.printStackTrace();
			return;
		}
		
		this.getDate(djvo);
	}
	
	
	private HYPubBO pubBO;
	private HYPubBO getPubBO(){
		if(null==pubBO){
			pubBO=new HYPubBO();
		}
		return pubBO;
	}
	/**
	 * 获取报检记录的信息
	 * @param aggVO
	 */
	private void getDate(AbstractBill vo){
		
		List<PMOItemVO> newItemVOs=new ArrayList<PMOItemVO>();
		
		try {
			LimsIpcVO[] ipcVOs=(LimsIpcVO[])this.getPubBO().queryByCondition(LimsIpcVO.class, "CPMOHID='"+this.getPmoAggVO().getPrimaryKey()+"' ORDER BY QC_TIME ASC");
			if(null!=ipcVOs && ipcVOs.length>0){
				int row_no=10;
				UFDouble U10=new UFDouble("10");
				for (LimsIpcVO limsIpcVO : ipcVOs) {
					PMOItemVO itemVO=(PMOItemVO)this.getPmoAggVO().getChildrenVO()[0].clone();
					
					itemVO.setVrowno(row_no+"");
					
					itemVO.setVfirstcode(limsIpcVO.getID());//源头单据号
					itemVO.setCmaterialid(limsIpcVO.getCmaterialid());//报检物料
					itemVO.setVdef1(limsIpcVO.getQc_point());//报检点
					itemVO.setVdef2(limsIpcVO.getQc_times()+"");//报检次数
					//itemVO.setVdef2(new UFDouble(row_no).div(U10).intValue()+"");//报检次数
					itemVO.setVdef3(limsIpcVO.getQc_psn());//报检人
					itemVO.setVdef4(limsIpcVO.getQc_time());//报检时间
					itemVO.setVdef5(limsIpcVO.getQc_contorl_num());//控制编码
					itemVO.setVdef6(limsIpcVO.getQc_final());//质检结果
					itemVO.setVdef7(limsIpcVO.getQc_passs_psn());//放行人
					itemVO.setVdef8(limsIpcVO.getQc_passs_time());//放行时间
					
					row_no=row_no+10;
					
					newItemVOs.add(itemVO);
				}
			}
			PMOItemVO[] bodys = newItemVOs.toArray(new PMOItemVO[newItemVOs.size()]);
			VOSortUtils.ascSort(bodys, new String[]{"vdef4"});
			//重新赋值表体数据
			billCardPanel.getBillModel().setBodyDataVO(bodys);
			billCardPanel.getBillModel().execLoadFormula();
			billCardPanel.getBillModel().loadLoadRelationItemValue();
		} catch (UifException e) {
			MessageDialog.showErrorDlg(this, "错误", "初始化报检记录数据错误!!");
			e.printStackTrace();
			return ;
		}
		
		
    }

	private void initDate(AbstractBill vo) throws BusinessException {
		PMOAggVO aggVO=(PMOAggVO)vo.clone();
		this.setPmoAggVO(aggVO);
		//billCardPanel.setBillValueVO(pmoAggVO);
		//单据号
		billCardPanel.setHeadItem("vbillcode",pmoAggVO.getParentVO().getVbillcode());
		//物料信息
		billCardPanel.setHeadItem("cmaterialvid",pmoAggVO.getChildrenVO()[0].getCmaterialvid());
		//批次信息
		billCardPanel.setHeadItem("version",pmoAggVO.getChildrenVO()[0].getVbatchcode());
		billCardPanel.setEnabled(true);
		//赋值表题VO
		if(pmoAggVO.getChildrenVO().length>0){
			billCardPanel.getBillModel().setBodyDataVO(pmoAggVO.getChildrenVO());
		}
		billCardPanel.getBillTable().setEnabled(false);
		billCardPanel.getBillModel().execLoadFormula();
		billCardPanel.getBillModel().loadLoadRelationItemValue();
	}
    
	
	public void setUIData(SuperVO[] bodyvos,String billtype){
		billCardPanel.setEnabled(true);
		billCardPanel.getBillModel().setBodyDataVO(bodyvos);
		billCardPanel.getBillModel().setEnabledAllItems(false);//
		billCardPanel.getBillModel().execLoadFormula();
		billCardPanel.getBillModel().loadLoadRelationItemValue();
		//billCardPanel.getBillModel().setRowState(i, SELECTED);
		
	}
	
	/**
	 * 此处插入方法说明。 创建日期：(2001-8-22 19:00:00)
	 * 
	 * @return nc.ui.pub.bill.BillListPanel
	 */
	public BillCardPanel getBillCardPanel() {
		if (billCardPanel == null) {
			billCardPanel = new BillCardPanel();
			billCardPanel.loadTemplet(pk_templetid);// 模版主键
			billCardPanel.setBodyMultiSelect(true);
		}
		return billCardPanel;
	}

	/**
	 * 此处插入方法说明。 功能描述:构造返回容器Panel.包含CtBillListPanel和ivjCmdPanel 输入参数: 返回值: 异常处理:
	 * 作者:程起伍 日期:
	 * 
	 * @return nc.ui.pub.beans.UIPanel
	 */
	private nc.ui.pub.beans.UIPanel getPanel() {
		if (ivjPanel == null) {
			try {
				ivjPanel = new nc.ui.pub.beans.UIPanel();
				ivjPanel.setName("ParentPanel");
				ivjPanel.setLayout(new java.awt.BorderLayout());
				ivjPanel.add(getBillCardPanel(), java.awt.BorderLayout.CENTER);
				//底部按钮Panel
				ivjPanel.add(getCmdPanel(), java.awt.BorderLayout.SOUTH);
			} catch (java.lang.Throwable ivjExc) {
			}
		}
		return ivjPanel;
	}

	/**
	 * 此处插入方法说明。 功能描述:构造返回命令Panel，目前只添加关闭按钮。 输入参数: 返回值: 异常处理: 作者:程起伍 日期:
	 * 
	 * @return nc.ui.pub.beans.UIPanel
	 */
	private UIPanel getCmdPanel() {
		if (ivjCmdPanel == null) {
			try {
				ivjCmdPanel = new nc.ui.pub.beans.UIPanel();
				ivjCmdPanel.setName("CmdPanel");
				
				btnPanel.add(getbtnSave());
				btnPanel.add(getbtnClose());
				
				ivjCmdPanel.add(btnPanel);

			} catch (java.lang.Throwable ivjExc) {
				
			}
		}
		return ivjCmdPanel;
	}

	/**
	 * 初始化类。
	 */
	/* 警告：此方法将重新生成。 */
	private void initialize(String title, String pk_templetid) {
		try {
			this.pk_templetid = pk_templetid;
			setName(title);
			setTitle(title);
			setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
			setSize(870, 400);
			setLocation(200, 200);
			setContentPane(getPanel());

			billCardPanel.addEditListener(this);//编辑后
			billCardPanel.addBodyEditListener2(this);//编辑前
			// 表体不可排序
			billCardPanel.getBillTable().setSortEnabled(true);
			// 表体菜单显示
			billCardPanel.getBodyPanel().setBBodyMenuShow(true);
			// billCardPanel.removeListener();
			// 设置不自动增行
			billCardPanel.getBodyPanel().setAutoAddLine(false);
		} catch (java.lang.Throwable ivjExc) {

		}
	}

	/*
	 * 取消按钮
	 * */
	public nc.ui.pub.beans.UIButton getbtnClose() {
		if (ivjbtnClose == null) {
			try {
				ivjbtnClose = new nc.ui.pub.beans.UIButton();
				ivjbtnClose.setName("btnCancel");
				ivjbtnClose.setText("取消");
				ivjbtnClose.setActionCommand("esc_action");
				ivjbtnClose.setPreferredSize(new Dimension(60, 30));
				ivjbtnClose.setSize(60, 30);
				ivjbtnClose.addActionListener(this);

			} catch (java.lang.Throwable ivjExc) {

			}
		}
		return ivjbtnClose;
	}

	/*
	 * 报检按钮
	 * */
	public nc.ui.pub.beans.UIButton getbtnSave() {
		if (ivjbtnSave == null) {
			try {
				ivjbtnSave = new nc.ui.pub.beans.UIButton();
				ivjbtnSave.setName("btnSave");
				ivjbtnSave.setText("报检");
				ivjbtnSave.setActionCommand("qc_action");
				ivjbtnSave.setPreferredSize(new Dimension(60, 30));
				ivjbtnSave.setSize(60, 30);
				ivjbtnSave.addActionListener(this);

			} catch (java.lang.Throwable ivjExc) {

			}
		}
		return ivjbtnSave;
	}
	
	/*
	 * 按钮操作
	 * */
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("esc_action")) {
			setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
			this.close();
			/*if (getE()!=null){
				getCardRefreshAction().doAction(getE());//刷新界面
			}*/
		}
		if (e.getActionCommand().equals("qc_action")) {
			try {
				Pusb2Lims();
				//this.close();
			} catch (BusinessException bException) {
				bException.printStackTrace();
				MessageDialog.showErrorDlg(this, "报检Lims异常", bException.getMessage());
			}

		}
	}
	/**
	 * 报检按钮的实现
	 * @throws BusinessException
	 */
	@SuppressWarnings("restriction")
	public void Pusb2Lims() throws BusinessException {
		System.out.println("报检---------------------------------");
	    CardPanelValueUtils cardtool=new CardPanelValueUtils(this.getBillCardPanel());
	    /*//UFDateTime newTs = AppContext.getInstance().getServerTime();
	    //String cgeneralbid=cardtool.getBodyStringValue(bodycurrow, "cgeneralbid");*/
	    //报检点
	    String bjd = cardtool.getHeadTailStringValue("bjd");
	    if(StringUtils.isEmpty(bjd)){
	    	MessageDialog.showErrorDlg(this, "提示", "请输入报检点信息!");
	    	return ;
	    }
	    //报检点信息赋值给 版本 暂存this.getPmoAggVO().getParentVO().setVersion(bjd);
	  	ISysDispatcherThLims outerService=(ISysDispatcherThLims) NCLocator.getInstance().lookup(ISysDispatcherThLims.class.getName());
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("bjd", bjd);
		String fun_type="TH_LIMS_IPC";
		PMOAggVO returnvo = (PMOAggVO) outerService.dispatch(this.getPmoAggVO(),fun_type, param);
		MessageDialog.showOkCancelDlg(this, "提示", "LIMS-IPC报检成功");
		
		//刷新数据
		this.getDate(getPmoAggVO());
	}
	

	@Override
	public void afterEdit(BillEditEvent e) {
	}

	@Override
	public void bodyRowChange(BillEditEvent e) {
	}

	public BillForm getEditor() {
		return editor;
	}

	public void setEditor(BillForm editor) {
		this.editor = editor;
	}

	public ActionEvent getE() {
		return e;
	}

	public void setE(ActionEvent e) {
		this.e = e;
	}

	public void setBillCardPanel(BillCardPanel billCardPanel) {
		this.billCardPanel = billCardPanel;
	}

	public NCAction getCardRefreshAction() {
		return cardRefreshAction;
	}

	public void setCardRefreshAction(NCAction cardRefreshAction) {
		this.cardRefreshAction = cardRefreshAction;
	}

	@Override
	public boolean beforeEdit(BillEditEvent e) {
		return true;
	}

	public String getPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(String primaryKey) {
		this.primaryKey = primaryKey;
	}
	
	public PMOAggVO getPmoAggVO() {
		return pmoAggVO;
	}

	public void setPmoAggVO(PMOAggVO pmoAggVO) {
		this.pmoAggVO = pmoAggVO;
	}
}
