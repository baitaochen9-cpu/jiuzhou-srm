package nc.ui.riasm.electronicsignature.view;

import javax.swing.tree.DefaultMutableTreeNode;

import nc.desktop.ui.WorkbenchEnvironment;
import nc.ui.pub.tree.ILocationByText;
import nc.vo.bd.meta.BDObjectAdpaterFactory;
import nc.vo.bd.meta.IBDObject;
import nc.vo.bd.meta.IBDObjectAdapterFactory;
import nc.vo.ml.Language;
import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.sm.funcreg.BusiActiveVO;
import nc.vo.sm.funcreg.ButtonRegVO;
import nc.vo.sm.funcreg.FuncRegisterVO;
import nc.vo.sm.funcreg.MenuItem;
import nc.vo.sm.funcreg.ModuleVO;
import nc.vo.sm.funcreg.PageVO;

/**
 * 功能节点树节点过滤器  name字段需使用resid来定位至对应语种文本再进行匹配
 * @author guoting
 *
 */
public class FuncRegLocationByText implements ILocationByText {

	final private String productCode = "funcode";
	
	private IBDObjectAdapterFactory bdObjectAdapterFactory = null;
	
	public void setBdObjectAdapterFactory(IBDObjectAdapterFactory bdObjectAdapterFactory) {
		this.bdObjectAdapterFactory = bdObjectAdapterFactory;
	}

	public IBDObjectAdapterFactory getBdObjectAdapterFactory() {
		if (this.bdObjectAdapterFactory == null) {
			this.bdObjectAdapterFactory = new BDObjectAdpaterFactory();
		}
		return bdObjectAdapterFactory;
	}
	
	@Override
	public boolean match(String inputText, DefaultMutableTreeNode node) {
		if (node == null || node.getUserObject() == null)
			return false;
		
		Object userObj = node.getUserObject();
		
		String code = "";
		String name = "";
		IBDObject bdobject = getBdObjectAdapterFactory().createBDObject(userObj);
		if (bdobject != null) {
			code = null2Empty(bdobject.getCode());
			name = null2Empty(bdobject.getName());
			
			//简体中文语种直接判断name字段，其它语种需要根据resid查询对应语种多语资源再做判断
			if (Language.SIMPLE_CHINESE_CODE.equals(WorkbenchEnvironment.getInstance().getCurrLanguage().getCode())) 
				return (code.indexOf(inputText) != -1 || name.indexOf(inputText) != -1);
		}

		if (userObj instanceof FuncRegisterVO) {
			FuncRegisterVO funcvo = (FuncRegisterVO) userObj;
			// 功能多语名称
			name = NCLangRes4VoTransl.getNCLangRes().getString(productCode, funcvo.getFun_name(), funcvo.getFuncode());
		} else if (userObj instanceof PageVO) {
			// 页签多语名称
			PageVO pageVO = (PageVO) userObj;
			name = NCLangRes4VoTransl.getNCLangRes().getString(productCode, pageVO.getPagename(), pageVO.getResid());
		} else if (userObj instanceof ButtonRegVO) {
			// 按钮多语名称
			ButtonRegVO btnVO = (ButtonRegVO) userObj;
			name = NCLangRes4VoTransl.getNCLangRes().getString(productCode, btnVO.getBtnname(),	btnVO.getResid());
		} else if (userObj instanceof BusiActiveVO) {
			// 业务活动多语名称
			BusiActiveVO baVO = (BusiActiveVO) userObj;
			name = NCLangRes4VoTransl.getNCLangRes().getString(productCode, baVO.getName(),	baVO.getResid());
		} else if (userObj instanceof MenuItem) {
			// 菜单多语名称
			MenuItem item = (MenuItem) userObj;
			name = NCLangRes4VoTransl.getNCLangRes().getString(productCode, item.getMenuitemname(),	item.getResid());
		} else if (userObj instanceof ModuleVO) {
			// 模块多语名称
			ModuleVO moduleVO = (ModuleVO) userObj;
			name = NCLangRes4VoTransl.getNCLangRes().getString(productCode, moduleVO.getSystypename(), moduleVO.getResid());
		} 
		
		return (null2Empty(code).indexOf(inputText) != -1 || null2Empty(name).indexOf(inputText) != -1);
	}

	private String null2Empty(Object o) {
		return o == null ? "" : o + "";
	}
	
}