package nc.ui.qc.supplierqualitystatus.view;

import java.awt.Component;
import java.awt.Font;

import javax.swing.Icon;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import nc.sfbase.client.ClientToolKit;
import nc.ui.pub.beans.tree.BDObjectFilterByText;
import nc.ui.pub.beans.tree.FilterTreeCellRendererUtil;
import nc.ui.pub.beans.tree.IFilterTreeCellRender;
import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.sm.funcreg.BusiActiveVO;
import nc.vo.sm.funcreg.ButtonRegVO;
import nc.vo.sm.funcreg.FuncRegisterVO;
import nc.vo.sm.funcreg.MenuItem;
import nc.vo.sm.funcreg.ModuleVO;
import nc.vo.sm.funcreg.PageVO;


/**
 * 功能节点树的渲染器
 * 
 * @author lkp
 *
 */
public class SupplierTreeCellRender extends DefaultTreeCellRenderer implements IFilterTreeCellRender{

	final private String productCode = "funcode";
	
	private static final long serialVersionUID = 9133467646810341426L;
	private  Icon inexecfuncIcon = ClientToolKit.loadImageIcon("/themeroot/blue/themeres/control/tree/folder.png"); 
	private  Icon pageIcon = ClientToolKit.loadImageIcon("/images/treeImages/tab.png");
	private  Icon funcIcon = ClientToolKit.loadImageIcon("/themeroot/blue/themeres/ui/icons/function.png"); 
	private  Icon btnIcon = ClientToolKit.loadImageIcon("/images/treeImages/btn.gif"); 
	private Icon busiactiveIcon = ClientToolKit.loadImageIcon("/themeroot/blue/themeres/ui/icons/activity.png");
	private static Icon moduleIcon = ClientToolKit.loadImageIcon("/themeroot/blue/themeres/control/tree/folder.png");
	public SupplierTreeCellRender(){
		super();  
	}
	public Component getTreeCellRendererComponent(JTree tree, Object value,
			boolean sel, boolean expanded, boolean leaf, int row,
			boolean hasFocus) {

		super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
		
		String   stringValue = tree.convertValueToText(value, sel, expanded, leaf, row, hasFocus);
		this.hasFocus = hasFocus;
		setText(stringValue);

		if(sel) 
		    setForeground(getTextSelectionColor());
		else
		    setForeground(getTextNonSelectionColor());
		if(value != null && value instanceof DefaultMutableTreeNode)
		{
			Object userObj = ((DefaultMutableTreeNode)value).getUserObject();
			String mlResult = null;
			
			if (userObj instanceof FuncRegisterVO) {
				FuncRegisterVO funcvo = (FuncRegisterVO) userObj;
				
				// 功能多语名称
				mlResult = NCLangRes4VoTransl.getNCLangRes().getString(productCode, funcvo.getFun_name(),
						funcvo.getFuncode());

				if (funcvo.getIsfunctype() != null && funcvo.getIsfunctype().booleanValue()) {
					// 实际功能节点
					setText(mlResult);
					innerSetIcon(tree, inexecfuncIcon);
				} else {
					// 功能分类
					setText(funcvo.getFuncode() + "  " + mlResult);
					innerSetIcon(tree, funcIcon);
				}

			} else if (userObj instanceof PageVO) {
				// 页签多语名称
				PageVO pageVO = (PageVO) userObj;
				if(pageVO.getResid()!=null){
					mlResult = NCLangRes4VoTransl.getNCLangRes().getString(productCode, pageVO.getPagename(),
							pageVO.getResid());
				}else {
					mlResult = pageVO.getPagename();
				}
				setText(mlResult);
				
				innerSetIcon(tree, pageIcon);
			} else if (userObj instanceof ButtonRegVO) {
				// 按钮多语名称
				ButtonRegVO btnVO = (ButtonRegVO) userObj;
				if(btnVO.getResid() != null){
					mlResult = NCLangRes4VoTransl.getNCLangRes().getString(productCode, btnVO.getBtnname(),	btnVO.getResid());
				}else{
					mlResult = btnVO.getBtnname();
				}
				setText(mlResult);
				
				innerSetIcon(tree, btnIcon);
			} else if (userObj instanceof String) {
//				innerSetIcon(tree, inexecfuncIcon);
			} else if (userObj instanceof BusiActiveVO) {
				// 业务活动多语名称
				BusiActiveVO baVO = (BusiActiveVO) userObj;
				mlResult = NCLangRes4VoTransl.getNCLangRes().getString(productCode, baVO.getName(),	baVO.getResid());
				if(baVO.getResid() != null){
					mlResult = NCLangRes4VoTransl.getNCLangRes().getString(productCode,baVO.getName(),	baVO.getResid());
				}else {
					mlResult = baVO.getName();
				}
				setText(mlResult);
				
				innerSetIcon(tree, busiactiveIcon);
			} else if (userObj instanceof MenuItem) {
				// 菜单多语名称
				MenuItem item = (MenuItem) userObj;
				if(item.getResid() != null)
					mlResult = NCLangRes4VoTransl.getNCLangRes().getString(productCode, item.getMenuitemname(),	item.getResid());
				else 
					mlResult = item.getMenuitemname();
				setText(item.getMenuitemcode() + "  " + item.getMenuitemname());

				if (item.getIsLeafMenu() != null
						&& item.getIsLeafMenu().booleanValue())
					innerSetIcon(tree, funcIcon);
				else
					innerSetIcon(tree, inexecfuncIcon);
			} else if (userObj instanceof ModuleVO) {
				// 模块多语名称
				ModuleVO moduleVO = (ModuleVO) userObj;
				if(moduleVO.getResid() != null)
					mlResult = NCLangRes4VoTransl.getNCLangRes().getString(productCode, moduleVO.getSystypename(), moduleVO.getResid());
				else
					mlResult = moduleVO.getSystypename();
				setText(moduleVO.getModuleid() + "  " + mlResult);
				
				innerSetIcon(tree, moduleIcon);
			}
		}
		
		if(value != null && value instanceof DefaultMutableTreeNode)
		{
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
			if (FilterTreeCellRendererUtil.getInstance().getFiltertext() != null&& FilterTreeCellRendererUtil.getInstance().getFiltertext().trim().length() > 0) {
				if (FilterTreeCellRendererUtil.getInstance().getFilter().accept(node,FilterTreeCellRendererUtil.getInstance().getFiltertext())) {
				     setFont(getFont().deriveFont(Font.BOLD));
				} else {
					setFont(getFont().deriveFont(Font.PLAIN));
				}
			}else{
				setFont(getFont().deriveFont(Font.PLAIN));
			}
		}
		
	    setComponentOrientation(tree.getComponentOrientation());
	    
		selected = sel;

		return this;
	}
	
	private void innerSetIcon(JTree tree, Icon icon)
	{
		if(!tree.isEnabled())
			setDisabledIcon(icon);
		else
			setIcon(icon);
	}
	@Override
	public void setFiltertext(String filtertext) {
		//初始化工具类
		FilterTreeCellRendererUtil  TreeCellRendererUtil = FilterTreeCellRendererUtil.getInstance();
		TreeCellRendererUtil.setFilter(new BDObjectFilterByText());//设置过滤功能类
		TreeCellRendererUtil.setFiltertext(filtertext);//设置过滤文本
	}
}
