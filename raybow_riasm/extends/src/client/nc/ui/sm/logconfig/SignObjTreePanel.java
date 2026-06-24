package nc.ui.sm.logconfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.SpringLayout;
import javax.swing.tree.TreeCellRenderer;

import nc.bs.pub.logconfig.vo.LogModeVO;
import nc.bs.sm.busilog.util.LogConfigServiceFacade;
import nc.bs.sm.busilog.util.LogConfigServiceWrapper;
import nc.bs.sm.logconfig.vo.AttributeVO;
import nc.bs.sm.logconfig.vo.BusilogRuleVO;
import nc.signlogconfig.util.BusilogConfigUtil;
import nc.ui.md.signog.treetotree.SignLogMDTreeToTreePanel;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIScrollPane;
import nc.ui.sm.logconfig.common.AbstractTreePanel;
import nc.ui.trade.component.IItemChooserModel;
import nc.vo.bd.access.tree.ITreeCreateStrategy;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;

/**
 * @version NC6.0
 * @author gaijf
 * @date 2009-7-21 上机日志配置->日志规则树面板
 */
public class SignObjTreePanel extends AbstractTreePanel {
	public SignObjTreePanel(){
		
	}
	private String ownmodule = null ;
	public SignObjTreePanel(String mod){
		ownmodule = mod ;
	}
	public void setOwnmodule(String mod){
		ownmodule = mod ;
	}
	/** */
	private static final long serialVersionUID = -3399797581921461761L;

	private SpringLayout spring = null;

	private UIScrollPane scrollPane = null;

	/**
	 * 操作和属性的数据集合缓存
	 */
	private Map<String,Map<String,List<SuperVO>>> dataMap = new HashMap<String, Map<String,List<SuperVO>>>();
	/**
	 * 操作和属性的预置数据集合
	 */
	private Map<String,Map<String,List<SuperVO>>> prevalueMap = new HashMap<String, Map<String,List<SuperVO>>>();
	/************* 用于存储保存数据的集合 *****************/
	private List<BusilogRuleVO> oper4Save = new ArrayList<BusilogRuleVO>();
	private List<AttributeVO> attr4Save = new ArrayList<AttributeVO>();
	/************* 用于存储保存数据的集合 *****************/

	/**
	 * @deprecated
	 * @since NC6.1
	 **/
	@Override
	public ITreeCreateStrategy getTreeCreateStrategy(List<Object> objs) {
		// return new BusiobjTreeCreateStrategy();
		return null;
	}

	/**
	 * @deprecated
	 * @since NC6.1
	 **/
	@Override
	public TreeCellRenderer getTreeRenderer() {
		// return new MDTreeCellRender();
		return null;
	}

	/**
	 * @deprecated
	 * @since NC6.1
	 **/
	@Override
	public IItemChooserModel getItemChooserModel(Object[] leftData,
			Object[] rightData) {
		// return new MDItemChooserModel(leftData, rightData);
		return null;
	}

	/**
	 * 根据当前日志模式，设置树面板的是否可编辑
	 * 
	 * @deprecated
	 * @sinceNC6.1
	 */
	public void resetEditable(int recordMode) {
		if (recordMode == LogModeVO.BUSILOG_RULES/*
												 * || recordMode ==
												 * LogModeVO.BUSILOG_ALL
												 */) {
			setEditable(true);
		} else {
			setEditable(false);
		}
	}

	@Override
	public void init() {
		setLayout(getSpring());
		setBorder(BorderFactory.createEmptyBorder());
		initData();
		add(getScrollPane(getMDTreeToTreePanel()));
	}

	/*
	 * 设置Tree2Tree面板按钮组的编辑性
	 */
	@Override
	public void setEditable(boolean isEditable) {
		getMDTreeToTreePanel().setBtnEnabled(isEditable); 
	}
	
	private SignLogMDTreeToTreePanel mdTreeToTreePanel = null;

	public SignLogMDTreeToTreePanel getMDTreeToTreePanel() {
		if (mdTreeToTreePanel == null) {
			mdTreeToTreePanel = new SignLogMDTreeToTreePanel(BusilogConfigUtil
					.constructNodeInfoList(getDataMap()), BusilogConfigUtil
					.constructNodeInfoList(getPrevalueMap()),ownmodule);
		}
		return mdTreeToTreePanel;
	}
	
//	private void setMdTreeToTreePanel(SignLogMDTreeToTreePanel mdTreeToTreePanel) {
//		this.mdTreeToTreePanel = mdTreeToTreePanel;
//	}
	
	private SpringLayout getSpring() {
		if (spring == null) {
			spring = new SpringLayout();
		}
		return spring;
	}

	private UIScrollPane getScrollPane(SignLogMDTreeToTreePanel view) {
		if (scrollPane == null) {
			scrollPane = new UIScrollPane();
//			scrollPane.setViewportView(getMDTreeToTreePanel());

			scrollPane.setBorder(BorderFactory.createEmptyBorder());
			getSpring().putConstraint(SpringLayout.NORTH, scrollPane, 30,
					SpringLayout.NORTH, SignObjTreePanel.this);
			getSpring().putConstraint(SpringLayout.WEST, scrollPane, 50,
					SpringLayout.WEST, SignObjTreePanel.this);
			getSpring().putConstraint(SpringLayout.EAST, scrollPane, -60,
					SpringLayout.EAST, SignObjTreePanel.this);
			getSpring().putConstraint(SpringLayout.SOUTH, scrollPane, -20,
					SpringLayout.SOUTH, SignObjTreePanel.this);
		}
		scrollPane.setViewportView(view);
		return scrollPane;
	}

	private void initData() {
		loadMap();
	}

	public void backUp() {
		// 空实现 因为数据存在dataMap中，dataMap中的数据和tree2tree右面板的已选数据相互独立
	}

	private void loadMap() {
		try {
			//加载数据
			setDataMap(LogConfigServiceWrapper
					.queryOperAndAttrDataByPk_Group(BusilogConfigUtil
							.getPk_Group()));
			setPrevalueMap(LogConfigServiceWrapper.queryPrevalueOfOperAndAttr());
		} catch (BusinessException e) {
			nc.bs.logging.Logger.error("后台查询出错！");
			nc.bs.logging.Logger.error(e);
		}
	}

	/**
	 * 回滚树数据
	 */
	public void rollBack() {
		//TODO::
		//点击取消时，数据初始化，只是为了实现树数据刷新
		//注：这样操作目前只是由于没有提供可供刷新树的方法，即无法给树换数据,目前只是单纯的换掉了树所在的整个面板
		initData();
		
		mdTreeToTreePanel = new SignLogMDTreeToTreePanel(BusilogConfigUtil
				.constructNodeInfoList(getDataMap()), BusilogConfigUtil
				.constructNodeInfoList(getPrevalueMap()));
		
		getScrollPane(mdTreeToTreePanel);
	}

//	/**
//	 * @param recordMode
//	 *            保存业务配置节点所有相关联的数据(日志模式+操作+属性)
//	 */
//	public void save(LogModeVO recordMode) {
//		getOper4Save().clear();
//		getAttr4Save().clear();
//		BusilogConfigUtil.addNeedSaveData2Map(getMDTreeToTreePanel(), getDataMap(),
//				getOper4Save(), getAttr4Save());
//		try {
//
//			LogConfigServiceFacade.getInstance().compositeSave(
//					recordMode,
//					getOper4Save().toArray(
//							new BusilogRuleVO[getOper4Save().size()]),
//					getAttr4Save().toArray(
//							new AttributeVO[getAttr4Save().size()]));
//		} catch (BusinessException e) {
//			nc.bs.logging.Logger.error("保存失败,详细请看日志");
//			nc.bs.logging.Logger.error(e);
//		}
//		loadMap();
//	}
	
	/**
	 * 保存业务配置节点所有相关联的数据(操作+属性)
	 */
	public void save() {
		getOper4Save().clear();
		getAttr4Save().clear();
		BusilogConfigUtil.addNeedSaveData2Map(getMDTreeToTreePanel(),
				BusilogConfigUtil.mergeMap(getDataMap(), getPrevalueMap()),
				getOper4Save(), getAttr4Save());
		try {

			LogConfigServiceFacade.getInstance().compositeSave(
					getOper4Save().toArray(
							new BusilogRuleVO[getOper4Save().size()]),
					getAttr4Save().toArray(
							new AttributeVO[getAttr4Save().size()]));
			MessageDialog.showHintDlg(
					this,
					NCLangRes.getInstance().getStrByID("syslog",
							"HintMessageGeneratorForBusiLog-000001")/*
																	 * @res
																	 * "日志配置"
																	 */,
					NCLangRes.getInstance().getStrByID("syslog",
							"HintMessageGeneratorForBusiLog-000002")/*
																	 * @res
																	 * "保存成功"
																	 */);
		} catch (BusinessException e) {
			MessageDialog.showHintDlg(
					this,
					NCLangRes.getInstance().getStrByID("syslog",
							"HintMessageGeneratorForBusiLog-000001")/*
																	 * @res
																	 * "日志配置"
																	 */,
					NCLangRes.getInstance().getStrByID("syslog",
							"HintMessageGeneratorForBusiLog-000003")/*
																	 * @res
																	 * "保存失败"
																	 */);
			nc.bs.logging.Logger.error("保存失败,详细请看日志");/*-=notranslate=-*/
			nc.bs.logging.Logger.error(e);
		}
		loadMap();
	}
	

	private List<BusilogRuleVO> getOper4Save() {
		return oper4Save;
	}

	private List<AttributeVO> getAttr4Save() {
		return attr4Save;
	}

	private Map<String, Map<String, List<SuperVO>>> getDataMap() {
		return dataMap;
	}

	private void setDataMap(Map<String, Map<String, List<SuperVO>>> dataMap) {
		this.dataMap = dataMap;
	}

	private Map<String, Map<String, List<SuperVO>>> getPrevalueMap() {
		return prevalueMap;
	}

	private void setPrevalueMap(Map<String, Map<String, List<SuperVO>>> prevalueMap) {
		this.prevalueMap = prevalueMap;
	}
	
}
