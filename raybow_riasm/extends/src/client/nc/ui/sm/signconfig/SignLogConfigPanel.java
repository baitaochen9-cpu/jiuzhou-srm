package nc.ui.sm.signconfig;

import java.awt.event.ActionEvent;

import javax.swing.BorderFactory;
import javax.swing.SpringLayout;

import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIScrollPane;
import nc.ui.pub.beans.UITextArea;
import nc.ui.sm.logconfig.BusilogConfigModel;
import nc.ui.sm.logconfig.SignObjTreePanel;
import nc.ui.sm.logconfig.HintMessageGeneratorForBusiLog;
import nc.ui.sm.logconfig.common.AbstractLogConfigPanel;
import nc.ui.sm.logconfig.common.ILogModePanel;

/**
 * @version NC6.0
 * @author gaijf
 * @date 2009-7-22
 * 业务日志配置主界面
 */
public class SignLogConfigPanel extends AbstractLogConfigPanel{

	private static final long serialVersionUID = -1021822370995341945L;
	private SignObjTreePanel busiTreePanel; 
//	private ILogModePanel logModePanel;
	private BusilogConfigModel configModel;
//	private LogModeVO modeVO_backup = null;
	public final static int BROWSER = 0;	
	public final static int EDIT = 1;
	private int status = BROWSER;
	private SpringLayout spring = null;
	private UIScrollPane scrollPane = null;
	private UITextArea textArea = null;
	//加上所属模块
	private String ownmodule ="";
	public String getOwnmodule(){
		return ownmodule ;
	}
	
	public SignLogConfigPanel(){		
		setLayout(getSpring());
		add((UIPanel)getTreePanel());
//		add((UIPanel)getLogModePanel());	
		add(getScrollPane());
	}
	
	private SpringLayout getSpring(){
		if(spring == null){
			spring = new SpringLayout();
		}
		return spring;
	}
	
	@Override
	public void init() {
		if (getParameter("ownmodule") != null) {
			ownmodule = getParameter("ownmodule");
		}
		setMenuActions(getMenuButtons());
		getTreePanel().setOwnmodule(ownmodule);
		getTreePanel().init();
//		//日志配置模式初始化
//		getLogModePanel().setModel(getModel());
//		getLogModePanel().setTreePanel(getTreePanel());
//		getLogModePanel().init();
		//设置按钮的编辑性和模式面板的编辑性
		updateBtnStatus(getStatus());
	}
	
	/*
	 * @see nc.ui.sm.logconfig.common.AbstractLogConfigPanel#getTreePanel()
	 * 业务日志配置规则树面板
	 */
	public SignObjTreePanel getTreePanel() {
		if (busiTreePanel == null) {
			busiTreePanel = new SignObjTreePanel(getOwnmodule());
			
			getSpring().putConstraint(SpringLayout.NORTH, busiTreePanel, 70, SpringLayout.NORTH, this);
			getSpring().putConstraint(SpringLayout.WEST, busiTreePanel, 5,
					SpringLayout.WEST, this);
			getSpring().putConstraint(SpringLayout.EAST, busiTreePanel, -60,
					SpringLayout.EAST, this);
			getSpring().putConstraint(SpringLayout.SOUTH, busiTreePanel, -1, 
					SpringLayout.SOUTH,this);
		}
		return busiTreePanel;
	}

	/*
	 * @see nc.ui.sm.logconfig.common.AbstractLogConfigPanel#getConfigModel()
	 * 返回业务日志配置模型
	 */
	@Override
	protected BusilogConfigModel getConfigModel() {
		if(configModel == null){
			configModel = new BusilogConfigModel();
		}
		return configModel;
	}

	/*
	 * @see nc.ui.sm.logconfig.common.AbstractLogConfigPanel#getLogModePanel()
	 * 返回业务日志配置模式单选面板
	 */
	@Override
	public ILogModePanel getLogModePanel() {
//		if (logModePanel == null) {
//			logModePanel = new BusiLogModePanel();
//			UIPanel modePanel = (UIPanel) logModePanel;
//			getSpring().putConstraint(SpringLayout.NORTH, modePanel, 10, SpringLayout.NORTH, this);
//			getSpring().putConstraint(SpringLayout.WEST, modePanel, 2,
//					SpringLayout.WEST, this);
//			getSpring().putConstraint(SpringLayout.EAST, modePanel, 220,
//					SpringLayout.WEST, this);
//			getSpring().putConstraint(SpringLayout.SOUTH, modePanel, 0, 
//					SpringLayout.NORTH,getTreePanel());
//		}
//		return logModePanel;
		return null;
	}
	
	private UIScrollPane getScrollPane(){
		if(scrollPane == null){
			scrollPane = new UIScrollPane(getTextArea());
			getSpring().putConstraint(SpringLayout.NORTH, scrollPane, 10, SpringLayout.NORTH, this);
			getSpring().putConstraint(SpringLayout.WEST, scrollPane, 60,
					SpringLayout.WEST,this);
			getSpring().putConstraint(SpringLayout.EAST, scrollPane, -60,
					SpringLayout.EAST, this);
			getSpring().putConstraint(SpringLayout.SOUTH, scrollPane, 0, 
					SpringLayout.NORTH,getTreePanel());
			scrollPane.setBorder(BorderFactory.createEmptyBorder());
		}
		return scrollPane;
	}
	
	private UITextArea getTextArea(){
		if(textArea == null){
			textArea = new UITextArea();
			textArea.setEditable(false);
			textArea.setLineWrap(true);
			textArea.setOpaque(false);
			textArea.setBorder(BorderFactory.createEmptyBorder()); 
			textArea.setText(HintMessageGeneratorForBusiLog.getHintMsg());
		}
		return textArea;
	}

	/**
	 * 保存当前配置
	 * 视图->模型；保存
	 */
	public void save(){	
//		LogModeVO recordMode = getNewModeVOByMode();
//		getTreePanel().save(recordMode); 
		getTreePanel().save();
		setStatus(BROWSER);
		updateBtnStatus(getStatus());
	}
	
	/**
	 * @param editable 设置按钮状态
	 * @deprecated
	 * @since NC6.1
	 */
	public void setEditable(boolean editable) {
//		//设置日志模式面板的编辑性
//		getLogModePanel().setEditable(editable);
		// 根据日志模式、按钮状态决定树是否可编辑
		getTreePanel().setEditable(editable);
	}
	
	@Override
	public void cancel() {
//		//回滚日志记录模式
//		((BusiLogModePanel) getLogModePanel()).setSelected(getModeVO_backup()				
//				.getRecordmodel());
		//回滚复选框树数据
		//注：目前是对树数据的重新查询，里面有需要该进的地方
		getTreePanel().rollBack();
		
		setStatus(BROWSER);
		updateBtnStatus(getStatus());
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		setStatus(EDIT);
		updateBtnStatus(getStatus());
		backUp();
	}
	
//	private void setModeVO_backup(LogModeVO modeVO_backup) {
//		this.modeVO_backup = modeVO_backup;
//	}
	
//	private LogModeVO getModeVO_backup() {
//		return modeVO_backup; 
//	}
	
	private void backUp(){
		//备份日志模式数据
//		LogModeVO modeVO = getConfigModel().getLogModeVO();
//		modeVO.setRecordmodel(getLogModePanel().getRecordMode());
//		setModeVO_backup(modeVO);
		//备份复选框树数据
		getTreePanel().backUp();	
	}
	
//	private LogModeVO getNewModeVOByMode(){
//		//当前最新的模式
//		int currMode = getLogModePanel().getRecordMode();
//		getConfigModel().handleFirstSave();
//		LogModeVO modeVO = getConfigModel().getLogModeVO();
//		if(currMode == modeVO.getRecordmodel()){
//			return null;
//		}
//		modeVO.setRecordmodel(currMode);
//		return modeVO;
//	}
	
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	
	private void updateBtnStatus(int status){
		if(status == BROWSER){
			getEditAction().setEnabled(true);
			getSaveAction().setEnabled(false);
			getCancelAction().setEnabled(false);
//			//设置日志模式面板的编辑性
//			getLogModePanel().setEditable(false);
			//设置树面板的编辑性
			getTreePanel().setEditable(false);
		}else{
			getEditAction().setEnabled(false);
			getSaveAction().setEnabled(true);
			getCancelAction().setEnabled(true);
//			//设置日志模式面板的编辑性
//			getLogModePanel().setEditable(true);
			//设置树面板的编辑性
			getTreePanel().setEditable(true);
		}
	}
	
}

