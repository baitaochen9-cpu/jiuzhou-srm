/**
 *
 */
package nc.ui.iufo.freereport;

import java.awt.Dimension;

import nc.bs.framework.common.NCLocator;
import nc.desktop.ui.WorkbenchEnvironment;
import nc.funcnode.ui.FuncletInitData;
import nc.funcnode.ui.FuncletWindowLauncher;
import nc.itf.iufo.constants.ModuleConstants;
import nc.itf.uap.bbd.func.IFuncRegisterQueryService;
import nc.itf.uap.qrytemplate.IQueryTemplateQry;
import nc.sfbase.client.ClientToolKit;
import nc.ui.iufo.ClientEnv;
import nc.ui.pub.beans.MessageDialog;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.querytemplate.QryTempletVOWithInfo;
import nc.vo.querytemplate.TemplateInfo;
import nc.vo.sm.funcreg.FuncRegisterVO;

import com.ufida.dataset.IContext;
import com.ufida.iufo.pub.tools.AppDebug;
import com.ufida.report.anadesigner.AbsAnaReportDesigner;
import com.ufida.report.anareport.FreeReportContextKey;
import com.ufida.report.free.view.FreeReportDesignerTool;
import com.ufida.zior.exception.MessageException;
import com.ufida.zior.view.Mainboard;
import com.ufida.zior.view.Viewer;

/**
 * 负责FreeFunclet相关控制
 * 
 * @author wangyga
 * @created at 2011-3-31,下午06:16:00
 * 
 */
public class FreeFuncletConsole {

	private FreeReportFunclet funclet = null;

	FreeFuncletConsole(FreeReportFunclet funclet) {
		this.funclet = funclet;
	}

	public Mainboard openFreeReport(boolean toFunclet) {
		String uri = funclet.getConfigFile();
		IContext context = null;
		UFBoolean ufBoolean = UFBoolean.valueOf(true);// add by yuyangi
		try {
			context = FreeFuncletContextFactory.createContext(funclet);
			//是否支持新的查询模型
			Object ob = context.getAttribute(FreeRepPublishUtil.SUPPORT_FRQUERYMODEL);
			if(ob!=null && ob instanceof Boolean && ((Boolean)ob).booleanValue()){
				ufBoolean = UFBoolean.FALSE;
			}else{
				// add by yuyang 从上下文信息中获取“显示查询面板”参数 20110223
				Boolean obj = (Boolean) context.getAttribute(FreeRepPublishUtil.SHOW_QUERY_PANEL);
				if (obj != null) {
					ufBoolean = UFBoolean.valueOf(obj.toString());
				}	
			}			
		} catch (Throwable e1) {
			AppDebug.debug(e1);
			throw new MessageException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("1413006_0",
					"01413006-0696")/* @res "context参数初始化失败：" */
					+ e1.toString());
		}

		String funcName = funclet.getFuncletContext().getCurrFuncRegisterVO().getFun_name();
		if (toFunclet)
			return toFunclet(uri, context, ufBoolean);
		else
			return toFrame(uri, context, funcName);
	}

	private AbsAnaReportDesigner getDeisgner(Mainboard mainboard) {
		if (mainboard == null) {
			return null;
		}
		if (mainboard.getAllViews() == null) {
			return null;
		}
		AbsAnaReportDesigner designer = null;
		for (Viewer viewer : mainboard.getAllViews()) {
			if (viewer instanceof AbsAnaReportDesigner) {
				designer = (AbsAnaReportDesigner) viewer;
			}
		}
		return designer;
	}

	private Mainboard toFunclet(String url, IContext context, UFBoolean showQueryPanel) {
		Object ob = context.getAttribute(FreeRepPublishUtil.SUPPORT_FRQUERYMODEL);
		boolean flag = ob!=null && ob instanceof Boolean && ((Boolean)ob).booleanValue();
		boolean hasTemplate = hasTemplate(flag, showQueryPanel, context);
		if(!flag){
			if(!showQueryPanel.booleanValue() && hasTemplate &&url.endsWith("bi/zior/zior-publish.xml")){
				url = ModuleConstants.MODULE_PATH + "bi/zior/zior-publish-min.xml";
			}	
		}
		
		Mainboard m_mainboard = new Mainboard(url, context);
		m_mainboard.getMainBoardConsole().toNCFunclet(funclet);
		Object repPks = context.getAttribute(FreeReportContextKey.REPORT_PUBLISHNODEALLREPPK);
		if (repPks != null) {
			String[] pks = (String[]) repPks;
			if (!StringUtil.isEmpty(pks[0])) {
				String id = FreeReportDesignerTool.getFreeDesignerViewerId(pks[0]);
				if (m_mainboard.getDockingManager().getDockable(id) != null) {
					m_mainboard.getDockingManager().getDockable(id).active();
				}
			}
		}
		boolean hideByForce = false;
		if(context.getAttribute(FreeRepPublishUtil.LEFT_QUERY_PANEL_HIDE_BYFORCE)!=null){
			hideByForce = (Boolean)context.getAttribute(FreeRepPublishUtil.LEFT_QUERY_PANEL_HIDE_BYFORCE);
		}
		
		// add by yuyangi 如果没有选择显示查询面板，则不在透视图中显示查询面板20110223
		if (flag || !showQueryPanel.booleanValue()&& !hasTemplate || hideByForce) {
			m_mainboard.getDockingManager().close(
					m_mainboard.getDockingManager().getDockable(
							"com.ufida.report.free.publish.query.FreeReportQueryViewer") , false);
			m_mainboard.getDockingManager().close(
					m_mainboard.getDockingManager().getDockable(
							"com.ufida.report.free.publish.query.FreeReportQueryMiniViewer") , false);
		} // end 20110223
		context.removeAttribute(FreeReportContextKey.KEY_LW_QUERY_ITEMS_PRELOADING);
		return m_mainboard;
	}

	private boolean hasTemplate(boolean hasFrQueryModel, UFBoolean showQueryPanel, IContext context) {
		if(hasFrQueryModel){
			return false;
		}
		
		if(showQueryPanel.booleanValue()){
			return true;
		}
		
		TemplateInfo tempinfo = new TemplateInfo();
		tempinfo.setPk_Org(ClientEnv.getInstance().getGroupID());
		tempinfo.setFunNode((String) context.getAttribute(FreeReportContextKey.REPORT_FUNCODE));
		tempinfo.setUserid(ClientEnv.getInstance().getLoginUserID());		
		
		try {
			IQueryTemplateQry qry = NCLocator.getInstance().lookup(IQueryTemplateQry.class);
			QryTempletVOWithInfo temp = qry.findAndGetTemplateVO(tempinfo);
			return temp != null;
		} catch (Exception ex) {// 找不到查询模板时不再抛一堆异常
			AppDebug.debug(ex.getLocalizedMessage());
		}
		
		return false;
	}

	private Mainboard toFrame(String url, IContext context, String title) {
		Mainboard m_mainboard = new Mainboard(url, context);
		m_mainboard.getMainBoardConsole().showByFrame(title);
		return m_mainboard;
	}

	/**
	 * 按节点code打开节点
	 * 
	 * @param funcode
	 * @param initData
	 * @return
	 */
	public static boolean openReportNode(String funcode, FuncletInitData initData) {
		try {
//			IFuncRegisterQueryService qry = (IFuncRegisterQueryService) NCLocator.getInstance().lookup(
//					IFuncRegisterQueryService.class);
			FuncRegisterVO frVO = WorkbenchEnvironment.getInstance().getFuncRegisterVO(funcode);
            if(frVO != null){
                FuncletWindowLauncher.openFuncNodeInTabbedPane(null, frVO, initData, null, false);
            } else {
                MessageDialog.showErrorDlg(ClientToolKit.getApplet(), null, nc.ui.ml.NCLangRes.getInstance().getStrByID("1413006_0", "01413006-1891")/* @res "没有打开此节点的权限 . 节点号=" */
                        + funcode);
            }


//			FuncRegisterVO function = qry.queryFunctionByCode(funcode);
			
//			String name = NCLangRes.getInstance().getString(IProductCode.PRODUCTCODE_FUNCODE, "", function.getFuncode());
			
		} catch (Exception e) {
			AppDebug.debug(e);
			throw new MessageException(e);
		}
		return true;
	}
	
	/**
	 * 按节点code以全屏或特定尺寸的frame打开节点
	 * 
	 * @param funcode
	 * @param initData
	 * @return
	 */
	public static boolean openReportNodeByFrame(String funcode, FuncletInitData initData, boolean isAllScreen) {
		try {
			IFuncRegisterQueryService qry = (IFuncRegisterQueryService) NCLocator.getInstance().lookup(
					IFuncRegisterQueryService.class);
			FuncRegisterVO function = qry.queryFunctionByCode(funcode);
			if(isAllScreen){
				FuncletWindowLauncher.openFuncNodeFrame(null, function, initData, null, false);
			}else{
				FuncletWindowLauncher.openFuncNodeFrame(null, function, initData, null, false, new Dimension(900, 500));
			}
		} catch (Exception e) {
			AppDebug.debug(e);
			throw new MessageException(e);
		}
		return true;
	}
	
	/**
	 * 按节点id打开节点
	 * 
	 * @param funcid
	 * @param initData
	 * @return
	 */
	public static boolean openReportNodeById(String funcid, FuncletInitData initData) {
		try {
			IFuncRegisterQueryService qry = (IFuncRegisterQueryService) NCLocator.getInstance().lookup(
					IFuncRegisterQueryService.class);
			FuncRegisterVO function = qry.queryFunctionByCFunid(funcid);
			FuncletWindowLauncher.openFuncNodeFrame(null, function, initData, null, false);
		} catch (Exception e) {
			AppDebug.debug(e);
			throw new MessageException(e);
		}
		return true;

	}

}