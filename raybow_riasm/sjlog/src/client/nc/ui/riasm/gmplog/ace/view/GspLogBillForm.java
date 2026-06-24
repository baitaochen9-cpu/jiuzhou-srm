package nc.ui.riasm.gmplog.ace.view;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.TransferHandler;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import nc.bs.logging.Logger;
import nc.md.MDBaseQueryFacade;
import nc.md.model.IAttribute;
import nc.md.model.IBean;
import nc.md.model.MetaDataException;
import nc.md.model.context.IMDNodeFilter;
import nc.md.model.context.MDNode;
import nc.pub.querytemplet.util.QueryTempletFieldNameProcssor;
import nc.ui.md.MDTreeBuilder;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIScrollPane;
import nc.ui.pub.beans.UISplitPane;
import nc.ui.pub.beans.UITree;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pubapp.bill.BillCardPanelWithoutScale;
import nc.ui.pubapp.uif2app.view.ShowUpableBillForm;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.riasm.gmplog.GmpLogConfigBvo;



public class GspLogBillForm extends ShowUpableBillForm {
	
	
	public static DataFlavor META_TREE_NODE_FLAVOR = new DataFlavor(DefaultMutableTreeNode.class, "application/x-java-jvm-local-objectref;metatreenode");
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private UISplitPane m_splitPane = null;
	private UIScrollPane treePane = null;
	private UITree metaDataTree = null;
	// 直接使用元数据主实体
	private IBean bean;

	   @Override
	protected void createBillCardPanel() {

      billCardPanel = new BillCardPanel();
     if ((billCardPanel instanceof BillCardPanelWithoutScale)) {
      ((BillCardPanelWithoutScale)billCardPanel).setBillForm(this);
     }
   
		//ewei+ 子表加树
    	billCardPanel.getBodyUIPanel().add(getSplitPane(),
				java.awt.BorderLayout.CENTER);
		getSplitPane().setLeftComponent(getTreePane());
		getSplitPane().setRightComponent(
				billCardPanel.getBodyTabbedPane());
		billCardPanel.getBodyTabbedPane().setTransferHandler(
				new BodyTransferHandler());
	
  
	}

	   
	   @Override
	public nc.ui.pub.bill.BillCardPanel getBillCardPanel() {
		// TODO 自动生成的方法存根
		BillCardPanel billCardPanel = super.getBillCardPanel();
		//界面切换的时候清空一下树的数据
		refreshTree();
		//根据表头加载数据
	    Object benid = billCardPanel.getHeadItem("billid").getValueObject();
		if(benid !=null){
			try {
				 bean = MDBaseQueryFacade.getInstance().getBeanByID(benid.toString());
			} catch (MetaDataException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
		}

		billCardPanel.getBodyUIPanel().add(getSplitPane(),
				java.awt.BorderLayout.CENTER);
		getSplitPane().setLeftComponent(getTreePane());
		getSplitPane().setRightComponent(
				billCardPanel.getBodyTabbedPane());
		billCardPanel.getBodyTabbedPane().setTransferHandler(
				new BodyTransferHandler());
	
	
    	
	
		return billCardPanel;
	
	}
	   
	   public void refreshTree(){
		   treePane = null;
		   metaDataTree = null;
		   bean = null ;
	   }
	   
	   
		private IBean getBean(){
			return bean;
		}
		public void setBean(IBean bean){
			this.bean = bean;
		}
	   
	   
	   /**
		 * ewei+   begin 
		 * 2008-2-29
		 * @return nc.ui.pub.beans.UISplitPane
		 */
		public UISplitPane getSplitPane() {
			if (m_splitPane == null){
				m_splitPane = new UISplitPane(UISplitPane.HORIZONTAL_SPLIT);
				m_splitPane.setResizeWeight(0.1);
				m_splitPane.setContinuousLayout(true);
				//m_splitPane.setDividerSize(5);
			}
			return m_splitPane;
		}
		
		/**
		 * ewei+
		 * 获得元数据树panel
		 * @return
		 */
		
		private UIScrollPane getTreePane(){
			if(getBean() == null){
				return null;
			}
			if (treePane == null){
				treePane = new UIScrollPane();
				treePane.setBorder(BorderFactory.createTitledBorder(NCLangRes.getInstance().getStrByID("_template", "UPP_Template-000543")/*元数据树*/));
				treePane.setViewportView(getMetaDataTree());
			}
			return treePane;
		}
		
		
		/**
		 * 获得元数据树，全component
		 * @return
		 */
		private UITree getMetaDataTree(){
			
			if (metaDataTree == null) {
				metaDataTree = MDTreeBuilder.constructMDTreeWithPowerType(getBean(), new AttrFilter(), "businessquery");
		//		metaDataTree = MDTreeBuilder.constructMDTree(getBean(), new AttrFilter());
				metaDataTree.setDragEnabled(true);	
				metaDataTree.setTransferHandler(new TreeTransferHandler());	
				metaDataTree.addMouseListener(new MetaTreeMouseAdapter());
			}
			return metaDataTree;
		}
	   
		
		static class AttrFilter implements  IMDNodeFilter{
			
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public boolean acceptNode(MDNode parentNode, IAttribute attr) {
				// 隐藏属性、计算属性不进行展示
				if(attr.isHide() || attr.isCalculation())
					return false;
				return true;
			}	
		}
		
		
		private class MetaTreeMouseAdapter extends MouseAdapter{
			public void mousePressed(MouseEvent e) {
				int selRow = metaDataTree.getRowForLocation(e.getX(), e.getY());
				TreePath selPath = metaDataTree.getPathForLocation(e.getX(), e.getY());		
				if (selRow != -1) {
					if (e.getClickCount() == 2) {
						doubleClick(selPath);
					}
				}
			}
			private void doubleClick(TreePath selPath) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) selPath.getLastPathComponent();		
				if (!node.isRoot() && node.getUserObject() instanceof MDNode){
//					addLineWithMD((MDNode) node.getUserObject());
				}
			}
			
		}
		
		
	   /**
		 * 对元数据树添加拖动事件
		 * @author ewei
		 */
		private class MetaDataTreeTransferable implements Transferable{
			
			DefaultMutableTreeNode[] node = null;
			
			public MetaDataTreeTransferable(DefaultMutableTreeNode[] node){
				super();
				this.node = node;
			}
			
			public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
				if (!GspLogBillForm.META_TREE_NODE_FLAVOR.equals(flavor))
					throw new UnsupportedFlavorException(flavor);
				return node;
			}
		
			public DataFlavor[] getTransferDataFlavors() {
				return new DataFlavor[]{GspLogBillForm.META_TREE_NODE_FLAVOR};
			}
		
			public boolean isDataFlavorSupported(DataFlavor flavor) {
				return GspLogBillForm.META_TREE_NODE_FLAVOR.equals(flavor);
			}	
		}
		
		private class TreeTransferHandler extends TransferHandler{
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;
			public int getSourceActions(JComponent c) {
				return COPY;
			}
			public Transferable createTransferable(JComponent c){
				UITree tree = (UITree)c;
				DefaultMutableTreeNode[] node = new DefaultMutableTreeNode[tree.getSelectionPaths().length];
					for (int i = 0; i < node.length; i++) {
						node[i] = (DefaultMutableTreeNode)tree.getSelectionPaths()[i].getLastPathComponent();
					}
				return new MetaDataTreeTransferable(node);
			}	
		}

		/**
		 * 给子表panel添加接收拖动数据事件
		 * @author ewei
		 */
		private class BodyTransferHandler extends TransferHandler{
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public boolean canImport(JComponent comp, DataFlavor[] flavors){
				for (int i = 0; i < flavors.length; i++) {
		            if (GspLogBillForm.META_TREE_NODE_FLAVOR.equals(flavors[i]));
		            {
		                return true;
		            }
		        }
				return false;
			}
			
			public boolean importData(JComponent c, Transferable t) {
				if (canImport(c, t.getTransferDataFlavors())) {
					if(hasMetaNodeFlavor(t))//从待选树中直接拖拽过去的
					{
						DefaultMutableTreeNode[] node = null;
						try {
							node = (DefaultMutableTreeNode[])t.getTransferData(GspLogBillForm.META_TREE_NODE_FLAVOR);
						} catch (UnsupportedFlavorException e) {
							Logger.error(e.getMessage(),e);
						} catch (IOException e) {
							Logger.error(e.getMessage(),e);
						}
						for (int i = 0; i < node.length; i++) {
							addLineWithMD((MDNode) node[i].getUserObject());
						}
													
					}
				}
				return false;
			}
			
			private boolean hasMetaNodeFlavor(Transferable t)
			{
				DataFlavor[] flavors = t.getTransferDataFlavors();
				for (DataFlavor flavor : flavors)
				{
					if(GspLogBillForm.META_TREE_NODE_FLAVOR.equals(flavor))
						return true;
				}
				return false;			
			}	
		}	

		/**
		 * 向子表中添加元数据行
		 * atrrcode的处理，主要是collection的，因为有关联关系和引用关系
		 * 需要加以区分
		 * @param node
		 */
		private void addLineWithMD(MDNode node){
			if(node==null)
				return;
			if(!node.isRoot()&&!node.isCollection())
				addoneMDnode(node);	
			else{
				List<MDNode> childrens = node.getChildNodes();
				for (MDNode node2 : childrens) {
					addoneMDnode(node2);
				}
			}
		}

		

		private void addoneMDnode(MDNode node) {
			String atrrcode = node.getAbsoluteAttributePath();
//			if(alreadyIn(atrrcode)){
//				MessageDialog.showErrorDlg(this, null, NCLangRes.getInstance().getStrByID("_template", "UPP_Template-000544")/*元数据对象*/+atrrcode+NCLangRes.getInstance().getStrByID("_template", "UPP_Template-000545")/*已经存在*/);
//				Logger.debug(NCLangRes.getInstance().getStrByID("_template", "UPP_Template-000544")/*元数据对象*/+atrrcode+NCLangRes.getInstance().getStrByID("_template", "UPP_Template-000545")/*已经存在*/);
//				return;
//			}
			 super.getBillCardPanel().getBillModel().addLine();
			//得到存有默认数据的vo
			GmpLogConfigBvo  cvo = new GmpLogConfigBvo();
			cvo.setAtrrcode(atrrcode);
			String attrname= QueryTempletFieldNameProcssor.getMdFieldName(getBean(),atrrcode);
			cvo.setAttrname(attrname);
			cvo.setGmpname(attrname);
			cvo.setIsenable(UFBoolean.TRUE);
		
			int last_row = getBillCardPanel().getBillModel().getRowCount() - 1;
			 super.getBillCardPanel().getBillModel().setBodyRowVO(cvo,last_row);
		}
		
		/**
		 * 检验元数据编码是否已存在与子表中
		 * @param code
		 * @return
		 */
		private boolean alreadyIn(String code){
			ArrayList<String> codeList = new ArrayList<String>();
			for(int i=0;i<getBillCardPanel().getBillModel().getRowCount();i++)
				codeList.add((String)getBillCardPanel().getBodyValueAt(i, "atrrcode"));
			if(codeList.contains(code))
				return true;
			return false;
		}
		
	 

}
