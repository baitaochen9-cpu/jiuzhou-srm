package nc.ui.iufo.freereport;

import java.awt.BorderLayout;

import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import nc.bs.framework.common.NCLocator;
import nc.desktop.ui.WorkbenchEnvironment;
import nc.funcnode.ui.AbstractFunclet;
import nc.funcnode.ui.FuncletInitData;
import nc.itf.iufo.constants.ModuleConstants;
import nc.itf.iufo.freereport.IFreeReportQryService;
import nc.pub.iufo.exception.UFOSrvException;
import nc.ui.iufo.freereport.model.FreeReportMngNode;
import nc.ui.uif2.IFunNodeClosingListener;
import nc.ui.uif2.LoadingPanel;
import nc.ui.uif2.NodeTypeUtil;
import nc.vo.bd.pub.NODE_TYPE;
import nc.vo.iufo.freereport.FreeReportBox;
import nc.vo.iufo.freereport.FreeReportFmtBox;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.querytemplate.queryscheme.SimpleQuerySchemeVO;
import nc.vo.sm.funcreg.FuncRegisterVO;
import nc.vo.uif2.AppStatusRegistery;
import nc.vo.uif2.LoginContext;

import org.apache.commons.lang.ArrayUtils;

import uap.pub.bqriart.base.adaptor.OrgSettingAccessorProxy;
import uap.pub.bqrt.base.adaptor.IOrgConstProxy;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufida.report.anadesigner.AbsAnaReportDesigner;
import com.ufida.report.anareport.FreeReportFucletContextKey;
import com.ufida.report.anareport.base.FreeReportDrillParam;
import com.ufida.report.anareport.base.IReportNodeInitData;
import com.ufida.report.utils.FreeRepQueryUtil;
import com.ufida.zior.comp.KTabbedPane;
import com.ufida.zior.view.Mainboard;
import com.ufida.zior.view.Viewer;

/**
 * 自由报表发布节点
 * 
 * @author wanyonga
 * 
 */
public class FreeReportFunclet extends AbstractFunclet {
	private static final long serialVersionUID = 1421746759512280111L;

	public static final String ALL_PUB_REP_LIST = "allPubRepList";
	// //装载应用状态信息 干什么用的还要确认
	protected AppStatusRegistery statusRegistery = null;

	private FreeFuncletConsole funcletConsole = new FreeFuncletConsole(this);

	private AbsAnaReportDesigner designer = null;

	private boolean hasOpened = false;

	private IFunNodeClosingListener m_FunNodeClosingListener = null;

	@Override
	public void init() {
		// @edit by wangyga at 2010-8-11,上午08:51:28
		setLayout(new BorderLayout());
		add(new LoadingPanel(), BorderLayout.CENTER);

		SwingWorker<Void, Void> sw = new SwingWorker<Void, Void>() {
			@Override
			protected Void doInBackground() throws Exception {// 必须异步执行
				long starttime = System.currentTimeMillis();
				Mainboard mainboard = getFuncletConsole().openFreeReport(true);
				designer = getDeisgner(mainboard);
				designer.requestFocus();
				setTabPaneProps();// 设置多页签的属性
				hasOpened = true;
				AppDebug.debug(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("1413006_0", "01413006-1577")/*
																												 * @
																												 * res
																												 * "打开节点，共计耗时："
																												 */
						+ (System.currentTimeMillis() - starttime));
				return null;
			}
			// 设置多页签的属性
			private void setTabPaneProps() {
				// 设置页签位置
				KTabbedPane ktablePane = (KTabbedPane) SwingUtilities.getAncestorOfClass(KTabbedPane.class, designer);
				if (ktablePane != null) {
					//页签位置
					String position = getParameter(FreeReportFucletContextKey.MUILT_TABE_SHOW_POSITION);
					if (position != null) {
						Integer positionValue = Integer.valueOf(position.trim());
						if (positionValue > 0 && positionValue < 5) {
							ktablePane.setTabPlacement(positionValue);
						}
					}
					//页签是否可关闭
					String canClose = getParameter(FreeReportFucletContextKey.MUILT_TABE_CAN_CLOSE);
					if ("N".equals(canClose.trim().toUpperCase())) {
						ktablePane.setAddCloseBtn(false);
					}else{
						ktablePane.setAddCloseBtn(true);
					}
					//页签是否显示右键菜单
					String showPopMenu = getParameter(FreeReportFucletContextKey.MUILT_TABE_SHOW_POPMENU);
					if ("N".equals(showPopMenu.trim().toUpperCase())) {
						ktablePane.setShowMenu(false);
					}else{
						ktablePane.setShowMenu(true);
					}
				}
			}
		};
		sw.execute();
	}

	/**
	 * TODO 初始化数据功能节点 如果节点已经打开，则只会执行Funclet中的initData。所以，当穿透到打开节点时 在此做穿透的查询
	 */
	@Override
	public void initData(FuncletInitData data) {
		super.initData(data);
		if (data == null) {
			return;
		}
		// 如果节点已经打开，则判断打开节的初始化参数
		// @edit by ll at 2012-5-24,下午02:10:06 add process to
		// SimpleQuerySchemeVO
		Object initData = data.getInitData();
		if (initData == null)
			return;

		if (initData instanceof SimpleQuerySchemeVO) {
			SimpleQuerySchemeVO vo = (SimpleQuerySchemeVO) initData;
			if (designer != null) {
				designer.getMainboard().getContext().removeAttribute(FreeReportFucletContextKey.SIMPLE_QUERY_SCHEME);// 从上下文中去除快捷查询方案，确保只有首张报表执行
				FreeRepQueryUtil.doQueryBySimpleQueryScheme(designer, vo);
			}
		} else if (initData instanceof IReportNodeInitData) { // 如果是穿透，执行穿透
			initData = ((IReportNodeInitData) initData).getReportDrillParam();
			if (initData instanceof FreeReportDrillParam && hasOpened) {
				FreeReportDrillParam drillInfo = (FreeReportDrillParam) initData;
				if (drillInfo != null) {
					if (designer != null) {
						FreeRepQueryUtil.doQueryByDrill(designer, drillInfo);
					}
				}
			}
		}
		// @edit by yuyangi at 2012-5-25,下午02:51:27 初始化参数为穿透参数
		else if (initData instanceof FreeReportDrillParam && hasOpened) {
			FreeReportDrillParam drillInfo = (FreeReportDrillParam) initData;
			if (drillInfo != null) {
				if (designer != null) {
					FreeRepQueryUtil.doQueryByDrill(designer, drillInfo);
				}
			}
		}
	}

	/*
	 * 点击节点关闭按钮时，调用该方法；返回值表示该节点是否可关闭
	 */
	@Override
	public boolean funcletCloseing() {
		if (this.m_FunNodeClosingListener != null) {
			return this.m_FunNodeClosingListener.canBeClosed();
		}

		return true;
	}

	/**
	 * 设置关闭监听器
	 * 
	 * @param closingListener
	 */
	public void setFunNodeClosingListener(IFunNodeClosingListener closingListener) {
		this.m_FunNodeClosingListener = closingListener;
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

	/**
	 * <p>
	 * 增加用来选择不同的zior配置文件
	 * </p>
	 * add by yuyangi
	 * 
	 * @return
	 */
	protected String getConfigFile() {
		String repType = getParameter(FreeReportFucletContextKey.PUBLISH_REP_TYPE_KEY);
		if (repType == null) {// 如果没有节点类型参数，则按旧发布节点处理
			return ModuleConstants.MODULE_PATH + "bi/zior/zior-publish.xml";
		} else if (FreeReportFucletContextKey.PUBLISH_REP_QUERY_NODE.equals(repType)) {// 查询型节点
			return ModuleConstants.MODULE_PATH + "bi/zior/zior-publish.xml";
		} else if (FreeReportFucletContextKey.PUBLISH_REP_REPORT_NODE.equals(repType)) {// 报表型节点
			return ModuleConstants.MODULE_PATH + "bi/zior/zior-myreport.xml";
		}
		return ModuleConstants.MODULE_PATH + "bi/zior/zior-publish.xml";
	}

	/**
	 * <p>
	 * 按功能ID查询报表PK
	 * </p>
	 * 
	 * @param funcId
	 * @return
	 */
	protected FreeReportBox getRepPks() {
		String funcId = getFuncletContext().getCurrFuncRegisterVO().getCfunid();
		IFreeReportQryService srv = NCLocator.getInstance().lookup(IFreeReportQryService.class);
		FreeReportBox box = null;
		try {
			String pubRepType = this.getParameter(FreeReportFucletContextKey.PUBLISH_REP_TYPE_KEY);
			box = srv.findFreeReportByFuncId(funcId, pubRepType);
			// 存储远程加载的报表信息
			// context.setAttribute(FreePrivateContextKey.REPORT_FREEREPORTBOX,
			// box) ;
		} catch (UFOSrvException e) {
			AppDebug.debug(e);
		}
		return box;
		// return RepPublishUtil.getPublishRepIds(funcId);
	}

	protected FreeReportFmtBox getFreeReportFmtBox(String repPk) {
		return null;
	}

	/**
	 * 是否是预置节点 add by yuyangi
	 * 
	 * @return
	 */
	protected boolean getIsSystInit() {
		String sysInit = getParameter(FreeReportMngNode.PARAM_ISSYSINIT);
		boolean m_bInitNode = false;
		if (sysInit != null && sysInit.equals("Y"))
			m_bInitNode = true;
		return m_bInitNode;
	}

	/**
	 * 获取目录条件 add by yuyangi
	 * 
	 * @param pk_org
	 * @return
	 */
	protected String getDirFilter() {
		String paraFilter = getParameter(FreeReportMngNode.PARAM_FREEDIR_FILTER);
		if (paraFilter != null) {
			paraFilter = paraFilter.replaceAll("\"", "\'");
		}
		// String whereCond = getDirCondSql(paraFilter);
		// if(whereCond != null && whereCond.length() > 0) {
		// return whereCond;
		// }
		return paraFilter;
	}

	/**
	 * 构造一个LoginContext对象，主要赋值Pk_org,NodeType和Pk_group，为在VisibleUtil.
	 * getVisibleCondition方法中使用 add by yuyangi
	 * 
	 * @return LoginContext
	 */
	protected LoginContext getPrivateContext() {
		LoginContext context = new LoginContext();
		FuncRegisterVO funcRegisterVO = getFuncletContext().getFuncRegisterVO();
		context.setNodeType(NodeTypeUtil.funcregisterVO2NODE_TYPE(funcRegisterVO));
		context.setNodeCode(getFuncCode());
		context.setPk_loginUser(WorkbenchEnvironment.getInstance().getLoginUser().getPrimaryKey());
		if (WorkbenchEnvironment.getInstance().getGroupVO() != null) {
			context.setPk_group(WorkbenchEnvironment.getInstance().getGroupVO().getPk_group());
		}

		context.setFuncInfo(getFuncletContext().getFuncSubInfo());
		if (getFuncletContext().getFuncSubInfo() != null) {
			String[] funcPermissionPkOrgs = getFuncletContext().getFuncSubInfo().getFuncPermissionPkorgs();
			// OrgVO[] funcPermissionOrgVOs =
			// getFuncletContext().getFuncSubInfo()
			// .getFuncPermissionOrgVOs();
			if (funcPermissionPkOrgs == null)
				// context.setOrgvos(null);
				context.setPkorgs(null);
			else
				// context.setOrgvos(Arrays.asList(funcPermissionOrgVOs));
				context.setPkorgs(funcPermissionPkOrgs);
		}

		context.setEntranceUI(this);
		context.setPk_org(getPk_org(funcRegisterVO));
		// 装载应用状态信息，没有用
		// loadAppStatus();
		// context.setStatusRegistery(this.statusRegistery);
		return context;
	}

	// 装载应用状态信息
	protected void loadAppStatus() {
		statusRegistery = new AppStatusRegistery();
		String pkUser = WorkbenchEnvironment.getInstance().getLoginUser().getCuserid();
		statusRegistery.load(getFuncCode(), pkUser);
	}

	/**
	 * 根据功能节点类型，返回组织主键 add by yuyangi
	 * 
	 * @param funcRegisterVO
	 * @return
	 */
	protected String getPk_org(FuncRegisterVO funcRegisterVO) {
		NODE_TYPE nodeType = NodeTypeUtil.funcregisterVO2NODE_TYPE(funcRegisterVO);
		switch (nodeType) {
		case GLOBE_NODE:
			return IOrgConstProxy.GLOBEORG;
		case GROUP_NODE:
			return WorkbenchEnvironment.getInstance().getGroupVO().getPk_group();
		case ORG_NODE:
			return getDefaultOrg();
		default:
			return IOrgConstProxy.GLOBEORG;
		}
	}

	/**
	 * 返回当前用户在当前集团的默认业务单元主键 add by yuyangi
	 * 
	 * @return
	 */
	protected String getDefaultOrg() {
		try {
			String defaultOrg = OrgSettingAccessorProxy.getDefaultOrgUnit();
			String[] pkorgs = getFuncletContext().getFuncSubInfo().getFuncPermissionPkorgs();
			if (ArrayUtils.isEmpty(pkorgs) || StringUtil.isEmptyWithTrim(defaultOrg))
				return null;
			else {
				boolean match = false;
				for (String pkorg : pkorgs) {
					if (defaultOrg.equals(pkorg)) {
						match = true;
						break;
					}
				}
				if (match)
					return defaultOrg;
				else
					return null;
			}
		} catch (Exception e) {
			AppDebug.debug(e);
		}
		return null;
	}

	protected FuncletInitData getFuncletInitData() {
		return funcletInitData;
	}

	protected FreeFuncletConsole getFuncletConsole() {
		return funcletConsole;
	}

}
