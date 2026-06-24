package nc.ui.qc.supplierqualitystatus.model;

import java.util.List;

import nc.bs.bd.baseservice.IBaseServiceConst;
import nc.bs.framework.common.NCLocator;
import nc.bs.mmgp.common.CommonUtils;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.ui.mmgp.uif2.model.MMGPTreeMangeModelDataManager;
import nc.ui.mmgp.uif2.service.MMGPQueryService;
import nc.ui.mmgp.uif2.utils.MMGPMetaUtils;
import nc.ui.pubapp.uif2app.model.BillManageModel;
import nc.ui.pubapp.uif2app.model.IQueryService;
import nc.ui.pubapp.uif2app.query2.model.IModelDataManager;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.ui.uif2.AppEvent;
import nc.ui.uif2.AppEventListener;
import nc.ui.uif2.IShowMsgConstant;
import nc.ui.uif2.ShowStatusBarMsgUtil;
import nc.ui.uif2.model.HierachicalDataAppModel;
import nc.ui.uif2.model.IAppModelDataManagerEx;
import nc.vo.bd.access.tree.ITreeCreateStrategy;
import nc.vo.bd.meta.BDObjectTreeCreateStrategy;
import nc.vo.bd.pub.IPubEnumConst;
import nc.vo.bd.supplier.stock.SupStockVO;
import nc.vo.mmgp.util.MMStringUtil;
import nc.vo.pub.SuperVO;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.uif2.LoginContext;
import nc.vo.util.BDVisibleUtil;
import nc.vo.util.SqlWhereUtil;

import org.apache.commons.lang.StringUtils;

/**
 * 
 */
public class SupplierTreeMangeModelDataManager extends
		MMGPTreeMangeModelDataManager implements IAppModelDataManagerEx,
		AppEventListener, IModelDataManager {
	public static final String ENABLE_DEFAULT = "(isnull("
			+ IBaseServiceConst.ENABLESTATE_FIELD + ","
			+ IPubEnumConst.ENABLESTATE_ENABLE + ") = "
			+ IPubEnumConst.ENABLESTATE_ENABLE + ")";
	private IQueryService queryService = null;

	private BillManageModel model;

	private HierachicalDataAppModel treeModel = null;
	// zhaoli 2013-4-19 支持右侧管理界面的‘显示停用’按钮
	private boolean isShowSeal = false;

	private String parentFieldName;

	private IUAPQueryBS service;
	private String sqlWhere;

	public SupplierTreeMangeModelDataManager() {
	}

	public void initModel() {
		initDefaultService();
		initManageModelBySelectNode(null);
	}

	/**
	 * 
	 * @param condition
	 */
	public void initManageModelBySelectNode(final String pk_node) {

		String condition = null;
		if (pk_node != null) {
			condition = getParentFieldName() + " = '" + pk_node + "'";
		}
		initModelBySqlWhere(condition);
	}

	@Override
	public void initModelBySqlWhere(String sqlWhere) {
		this.sqlWhere = sqlWhere;
		LoginContext context = getModel().getContext();
		Object[] data = queryByCondition(sqlWhere);
		// 如果根据查询条件没有查询到数据时，需要进行状态栏的信息提示
		if (data == null || data.length == 0) {
			ShowStatusBarMsgUtil.showStatusBarMsg(IShowMsgConstant
					.getQueryNullInfo(), getModel().getContext());
		} else
			ShowStatusBarMsgUtil.showStatusBarMsg(
					IShowMsgConstant.getQuerySuccessInfo(data.length),
					getModel().getContext());
		getModel().initModel(data);
	}

	@Override
	public void refresh() {
		// LoginContext context = getModel().getContext();
		// List<OrgVO> orgvos = context.getOrgvos();
		// String[] orgs = null;
		// if (orgvos != null && orgvos.size() > 0) {
		// orgs = new String[orgvos.size()];
		// for (int i = 0; i < orgvos.size(); i++) {
		// orgs[i] = orgvos.get(i).getPk_org();
		// }
		//
		// }
		// String[] data = queryPsndocByOrg(sqlWhere, orgs);
		// if (data == null || data.length == 0) {
		// ShowStatusBarMsgUtil.showStatusBarMsg(IShowMsgConstant
		// .getQueryNullInfo(), getModel().getContext());
		// } else
		// ShowStatusBarMsgUtil.showStatusBarMsg(
		// IShowMsgConstant.getQuerySuccessInfo(data.length),
		// getModel().getContext());

	}

	/**
	 * 根据条件查询数据
	 * 
	 * @param sqlWhere
	 * @return
	 */
	private Object[] queryByCondition(String sqlWhere) {
		Object[] data = null;
		// // 记录查询语句
		// this.sqlWhere = sqlWhere;

		// 条件为null时，返回null
		String parentname = getParentFieldName();
		String newPk_tradeterm = null;
		if (StringUtils.isBlank(sqlWhere)) {
			SuperVO obj = (SuperVO) getTreeModel().getSelectedData();
			String pk_node = null;
			if (obj != null) {
				pk_node = obj.getPrimaryKey();
				if (pk_node != null) {
					sqlWhere = parentname + " = '"
							+ pk_node + "'";
				}
			}else{
				return null;
			}
		}else{
			if (sqlWhere.indexOf(parentname) == -1) {
				newPk_tradeterm = sqlWhere;
				SuperVO obj = (SuperVO) getTreeModel().getSelectedData();
				String pk_node = null;
				if (obj != null) {
					pk_node = obj.getPrimaryKey();
					if (pk_node != null) {
						sqlWhere = sqlWhere + "and " + parentname + " = '"
								+ pk_node + "'";
					}
				}
			}
			if (sqlWhere.indexOf("pk_supplier") > -1) {
				sqlWhere = sqlWhere.replace("pk_supplier", "r.pk_supplier");
			}
			
			if (sqlWhere.indexOf("pk_vendor") > -1) {
				sqlWhere = sqlWhere.replace("pk_vendor", "y.pk_vendor");
			}
			
			if (sqlWhere.indexOf("pk_material") > -1) {
				sqlWhere = sqlWhere.replace("pk_material", "y.pk_material");
			}
			
		}
		SqlWhereUtil swu = new SqlWhereUtil();
		// 根据是否过滤封存，增加封存条件 赵利 2013-4-19
		if (!isShowSeal)
			swu.s(ENABLE_DEFAULT);
		// end zhaoli
		if (!StringUtils.isBlank(sqlWhere))
			swu.and(sqlWhere);

		// // 默认排序字段编码、名称（可配置排序规则暂未提供）
		// String defaultOrderPart = " order by code, name";
		LoginContext context = getModel().getContext();
		// 赵利 2013-4-23 只需要对当前组织过滤，不需要所有的可见组织。
		String[] pk_orgs = { context.getPk_org() };// context.getFuncInfo().getFuncPermissionPkorgs();
		// end

		

		try {
			// if (pk_orgs != null && pk_orgs.length > 0) {
			// StringBuilder pkorg = new StringBuilder();
			// for (String pk_org : pk_orgs) {
			// pkorg.append("'").append(pk_org).append("', ");
			// }
			// pkorg.delete(pkorg.length() - 2, pkorg.length());
			//
			// // componentid like '93c59c03-f7d9-45db-8326-fe0878e001b6';供应商
			// // classid 92eed375-f4bb-4852-b537-e31c5269bab8 供应商采购信息
			// IBean bean = MMMetaUtils
			// .getBeanByMetaID("92eed375-f4bb-4852-b537-e31c5269bab8");//
			// 供应商采购信息
			// ConditionBuilder conditionBuilder = new ConditionBuilder(
			// " pk_org in (" + pkorg + ")",
			// VisibleUtil.CONDITIONSTRUTYPE_SQL);
			// conditionBuilder.setMdclassid(bean.getID());
			// String orgSql = conditionBuilder.getCondition(bean.getTable()
			// .getName());
			// swu.and(orgSql);
			// }

			// swu.and(" h.pk_org = '" +context.getPk_org()+ "'");
			StringBuffer strb = new StringBuffer();
			strb.append(" select distinct g.pk_org,r.pk_supplier,h.pk_supstock, h.supgrade from bd_supplier r ");
			strb.append(" left join bd_supstock h on r.pk_supplier = h.pk_supplier");
			strb.append(" left join bd_suporg g on r.pk_supplier = g.pk_supplier");
			strb.append(" left join qc_supplierquality y on r.pk_supplier = y.pk_supplier and nvl(y.dr,0) = 0");
			String visibleCondition = BDVisibleUtil.getRefVisibleCondition(
					context.getPk_group(), pk_orgs,
					"720dcc7c-ff19-48f4-b9c5-b90906682f45");
			visibleCondition = visibleCondition.replace("pk_supplier",
					"r.pk_supplier");

			strb.append(" where nvl(r.dr,0) = 0 ");
			strb.append(" and " + swu.getSQLWhere());
			strb.append(" and " + visibleCondition);
			strb.append(" and g.pk_org = '" + context.getPk_org() + "' ");
			strb.append(" and h.pk_org = '" + context.getPk_org() + "' ");
			List<SupStockVO> list = (List<SupStockVO>) getService()
					.executeQuery(strb.toString(),
							new BeanListProcessor(SupStockVO.class));
			
			for(SupStockVO vo :list){
				vo.setPk_tradeterm(newPk_tradeterm);;
			}
			data = list.toArray(new SupStockVO[list.size()]);
		} catch (Exception e) {
			ExceptionUtils.wrappException(e);
		}
		return data;
	}

	public BillManageModel getModel() {
		return model;
	}

	public void setModel(BillManageModel model) {
		this.model = model;
		model.addAppEventListener(this);
	}

	public void setTreeModel(HierachicalDataAppModel treeModel) {
		this.treeModel = treeModel;

	}

	public IQueryService getQueryService() {
		return queryService;
	}

	public void setQueryService(IQueryService queryService) {
		this.queryService = queryService;
	}

	protected void initDefaultService() {
		// 懒加载现在多表体有问题，先改为非懒加载
		// 默认使用通用service
		if (getQueryService() == null) {
			MMGPQueryService service = new MMGPQueryService();
			service.setClassName(getClassFullName());
			// if (getModel() instanceof BillManageModel) {
			// BillManageModel billMangeModel = (BillManageModel) getModel();
			// service.setSupportLazilyLoad(billMangeModel
			// .isSupportLazilyLoad());
			// }
			setQueryService(service);

		}
		IUAPQueryBS service1 = (IUAPQueryBS) NCLocator.getInstance().lookup(
				IUAPQueryBS.class.getName());
		setService(service1);
	}

	protected String getClassFullName() {
		return MMGPMetaUtils.getClassFullName(getModel().getContext());
	}

	public HierachicalDataAppModel getTreeModel() {
		return treeModel;
	}

	// private void setPsndocToPaginationModel(String[] pks) {
	// try {
	// paginationModel.setObjectPks(pks);
	// } catch (BusinessException e) {
	// Logger.error(e.getMessage());
	// throw new BusinessExceptionAdapter(e);
	// }
	//
	// }
	//
	// @Override
	// public void onDataReady() {
	// getDelegator().onDataReady();
	//
	// }
	//
	// @Override
	// public void onStructChanged() {
	// }
	//
	// public BillManagePaginationDelegator getDelegator() {
	// if (delegator == null) {
	// delegator = new BillManagePaginationDelegator(getModel(),
	// getPaginationModel());
	// }
	// return delegator;
	// }
	//
	// public PaginationModel getPaginationModel() {
	// return paginationModel;
	// }

	public String getParentFieldName() {
		if (MMStringUtil.isEmpty(parentFieldName)) {
			ITreeCreateStrategy strategy = getTreeModel()
					.getTreeCreateStrategy();
			if (strategy instanceof BDObjectTreeCreateStrategy) {
				String className = ((BDObjectTreeCreateStrategy) strategy)
						.getClassName();
				parentFieldName = CommonUtils.getPKFieldName(className);
			}
		}
		return parentFieldName;
	}

	public void setParentFieldName(String parentFieldName) {
		this.parentFieldName = parentFieldName;
	}

	@Override
	public void handleEvent(AppEvent event) {

	}

	@Override
	public void setShowSealDataFlag(boolean showSealDataFlag) {
		this.isShowSeal = showSealDataFlag;
	}

	@Override
	public void initModelByQueryScheme(IQueryScheme paramIQueryScheme) {
		String condition = paramIQueryScheme.getWhereSQLOnly();
		initModelBySqlWhere(condition);
	}

	public IUAPQueryBS getService() {
		return service;
	}

	public void setService(IUAPQueryBS service) {
		this.service = service;
	}

}
