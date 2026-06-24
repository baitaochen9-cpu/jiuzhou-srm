package nc.bs.jzyy.sys.oa.ic.m45;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import nc.api.pubitf.IJsonParamMapping;
import nc.api.rest.ic.utils.BillFieldsCodeToPkUtil;
import nc.api.rest.ic.utils.QuerySchemeUtils;
import nc.api.rest.utils.CallReturnBuildUtil;
import nc.api.rest.utils.NCCRestUtils;
import nc.bs.framework.common.NCLocator;
import nc.bs.jzyy.sys.AbstracAdapter4Ext;
import nc.bs.logging.Logger;
import nc.bs.scmpub.query.SCMBillQuery;
import nc.md.MDBaseQueryFacade;
import nc.md.model.IBean;
import nc.md.model.MetaDataException;
import nc.pubitf.ic.m45.m25.IPurchaseInQueryFor25;
import nc.ui.querytemplate.operator.EqOperator;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.ui.querytemplate.value.DefaultFieldValue;
import nc.ui.querytemplate.value.DefaultFieldValueElement;
import nc.ui.querytemplate.value.IFieldValue;
import nc.ui.querytemplate.value.IFieldValueElement;
import nc.vo.ic.m45.entity.PurchaseInBodyVO;
import nc.vo.ic.m45.entity.PurchaseInHeadVO;
import nc.vo.ic.m45.entity.PurchaseInVO;
import nc.vo.ic.pub.define.ICPubMetaNameConst;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.pubapp.pattern.model.entity.bill.AbstractBill;
import nc.vo.pubapp.pattern.pub.SqlBuilder;
import nc.vo.scmpub.util.QuerySchemeBuilder;
import nccloud.api.jzyy.JZYYResultMessageUtil;

import org.apache.commons.lang.StringUtils;
import org.json.JSONString;

import com.alibaba.fastjson.JSONObject;

/**
 * 
 * @Description: 库存采购入库单资源类
 * 
 * @author: 刘伟
 * @date: 2019-5-5 上午10:01:13
 * @version NCC1909
 */
public class PurchaseInQuery extends AbstracAdapter4Ext {

	private IJsonParamMapping jsonParamMapping = new M45FieldMapping();

	private static String[] DATEFIELDS = { ICPubMetaNameConst.DBILLDATE,
			ICPubMetaNameConst.DMAKEDATE, ICPubMetaNameConst.TAUDITTIME,
			"cgeneralbid.dbizdate", "cgeneralbid.dinbounddate",
			"cgeneralbid.dproducedate", "cgeneralbid.dvalidate",
			"cgeneralbid.ts" };

	@Override
	public JSONObject sys(Object billvo) throws BusinessException {
		JSONObject rqJosn = (JSONObject) billvo;
		// 检索数据
		List<Map<String, Object>> respData = null;
		try {
			JSONObject bill = rqJosn.getJSONObject("bill");
			Map<String, Object> paramMap = getCondMap(bill);
			respData = queryVOByCommon(paramMap);
		} catch (Exception e) {
			return JZYYResultMessageUtil.getFailedRsultJson("获取数据0条:"
					+ e.getMessage());
		}
		if (respData == null || respData.size() == 0) {
			return JZYYResultMessageUtil.getFailedRsultJson("查询成功,匹配条件的0条");
		}
		// 将结果返回
		return JZYYResultMessageUtil.getSuccessQryRsultJson(respData);
	}

	private Map<String, Object> getCondMap(JSONObject bill) {
		Map<String, Object> paramMap = new HashMap<>();

		paramMap.put("cgeneralbid.cvendorid", bill.get("cvendorid"));// 供应商
		paramMap.put("ct_code", bill.get("ct_code"));// 合同号
		paramMap.put("cgeneralbid.vfirstbillcode", bill.get("order_code"));// 订单号
		paramMap.put(ICPubMetaNameConst.VBILLCODE, bill.get("vbillcode"));// 入库单号
		paramMap.put("cgeneralbid.dbizdate", bill.get("dbizdate"));// 入库日期
		paramMap.put(ICPubMetaNameConst.DBILLDATE, bill.get("billdate"));// 订单日期
		paramMap.put("cgeneralbid.cmaterialoid", bill.get("material"));// 物料
		paramMap.put(ICPubMetaNameConst.PK_ORG, bill.get("org_code"));// 组织
		paramMap.put(ICPubMetaNameConst.PK_GROUP, bill.get("group_code"));// 集团
		return paramMap;
	}

	public List<Map<String, Object>> queryVOByCommon(
			Map<String, Object> paramMap) throws BusinessException {
		if (paramMap == null
				|| !paramMap.containsKey(ICPubMetaNameConst.PK_ORG)
				|| !paramMap.containsKey(ICPubMetaNameConst.DBILLDATE)) {
			throw new BusinessException("单据日期或者组织部能为空");
		}

		if (getString_TrimZeroLenAsNull(paramMap.get(ICPubMetaNameConst.PK_ORG)) == null) {
			throw new BusinessException("组织不能为空");
		}

		if (getString_TrimZeroLenAsNull(paramMap
				.get(ICPubMetaNameConst.DBILLDATE)) == null) {
			throw new BusinessException("单据日期不能为空");
		}

		IQueryScheme queryScheme = createQueryScheme(paramMap);
		IPurchaseInQueryFor25 query = NCLocator.getInstance().lookup(
				IPurchaseInQueryFor25.class);
		PurchaseInVO[] purInVOs = query.queryBills(queryScheme);

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		if (purInVOs == null || purInVOs.length == 0)
			return list;
		for (PurchaseInVO billvo : purInVOs) {
			Map<String, Object> map = new HashMap<String, Object>();
			PurchaseInHeadVO headVO = billvo.getHead();
			PurchaseInBodyVO[] itemvos = billvo.getBodys();
			for (PurchaseInBodyVO item : itemvos) {
				map.put("vbillcode", headVO.getVbillcode());// 入库单号
				map.put("cgeneralhid", headVO.getCgeneralhid());// 入库单表头ID
				map.put("cgeneralbid", item.getCgeneralbid());// 入库单表体ID
				map.put("cvendorid", item.getCvendorid());// 供应商
				map.put("corigcurrencyid", item.getCcurrencyid());// 币种
				map.put("ninvoicemny", item.getNinvoicemny() == null ? null
						: item.getNinvoicemny().toString());// 可开票金额
				map.put("ninvoicenum", item.getNinvoicenum() == null ? null
						: item.getNinvoicenum().toString());// 可开票数量
				map.put("vbatchcode", item.getVbatchcode());// 批次号
				map.put("dept_code", headVO.getCdptid());// 采购部门
				map.put("usercode", headVO.getCbizid());// 采购员
				map.put("project", item.getCprojectid());// 项目
				map.put("manufacturer", item.getCproductorid());// 生产厂商
				map.put("cwarehouseid", headVO.getCwarehouseid());// 仓库
				list.add(map);
			}
		}
		return list;
	}

	private IQueryScheme createQueryScheme(Map<String, Object> paramMap) {

		// 翻译查询条件
		paramMap = BillFieldsCodeToPkUtil.doTranslateFields(jsonParamMapping,
				paramMap);
		// 处理查询方案
		QuerySchemeBuilder builder = QuerySchemeBuilder
				.buildByFullClassName(PurchaseInVO.class.getName());

		Object keyValue = null;
		Iterator<String> iterator = paramMap.keySet().iterator();
		while (iterator.hasNext()) {
			String queryKey = iterator.next();
			keyValue = paramMap.get(queryKey);
			if (getString_TrimZeroLenAsNull(keyValue) == null) {
				continue;
			}
			if (Arrays.asList(DATEFIELDS).contains(queryKey)) {
				builder = QuerySchemeUtils.dealDateFieldBuilder(builder,
						queryKey, (String) keyValue);
			} else if ("ct_code".equals(queryKey)) {
				IBean bean = getBean(PurchaseInVO.class);
				CtcodeFilterMeta meta = new CtcodeFilterMeta(bean.getID(),
						"cgeneralbid.cfirstbillbid");
				SqlBuilder where = new SqlBuilder();
				where.append("  select b.pk_order_b from po_order_b b where b.ccontractid in ( select u.pk_ct_pu from ct_pu u where u.vbillcode ='"
						+ keyValue + "')");
				builder.append(
						meta,
						EqOperator.getInstance(),
						buildFilterValue("cgeneralbid.cfirstbillbid",
								where.toString()));
			} else if (keyValue instanceof List
					&& ((List<String>) keyValue).size() > 0) {
				builder.append(queryKey, EqOperator.getInstance(),
						((List<String>) keyValue).toArray(new String[0]));
			} else if (keyValue instanceof String[]
					&& ((String[]) keyValue).length > 0) {
				builder.append(queryKey, EqOperator.getInstance(),
						(String[]) keyValue);
			} else if (keyValue instanceof String
					&& StringUtils.isNotEmpty((String) keyValue)) {
				builder.append(queryKey, EqOperator.getInstance(),
						new String[] { (String) keyValue });
			}
		}
		IQueryScheme queryScheme = builder.create();
		return queryScheme;
	}

	private IFieldValueElement buildFieldValueElement(String attribute,
			Object value) {

		IFieldValueElement fieldValueElement = null;

		fieldValueElement = new DefaultFieldValueElement(value.toString(),
				value.toString(), value);
		return fieldValueElement;
	}

	private IFieldValue buildFilterValue(String attribute, Object... values) {
		DefaultFieldValue fieldValue = new DefaultFieldValue();
		for (Object value : values) {
			if (value != null) {

				fieldValue.add(buildFieldValueElement(attribute, value));
			}
		}
		return fieldValue;
	}

	private IBean getBean(Class<? extends AbstractBill> cls) {
		try {
			return MDBaseQueryFacade.getInstance().getBeanByFullClassName(
					cls.getName());
		} catch (MetaDataException e) {
			Logger.error("查询元数据错误", e);
		}
		return null;
	}

	public JSONString queryVOByScheme(Map<String, Object> paramMap) {
		if (paramMap == null) {
			return NCCRestUtils.toJSONString(CallReturnBuildUtil
					.buildNullParamResult());
		}
		try {

			// 翻译查询条件
			paramMap = BillFieldsCodeToPkUtil.doTranslateFields(
					jsonParamMapping, paramMap);
			// 处理查询方案
			QuerySchemeBuilder builder = QuerySchemeBuilder
					.buildByFullClassName(PurchaseInVO.class.getName());
			Object keyValue = null;
			Iterator<String> iterator = paramMap.keySet().iterator();
			while (iterator.hasNext()) {
				String queryKey = iterator.next();
				keyValue = paramMap.get(queryKey);
				if (keyValue == null) {
					continue;
				}
				if (Arrays.asList(DATEFIELDS).contains(queryKey)) {
					builder = QuerySchemeUtils.dealDateFieldBuilder(builder,
							queryKey, (String) keyValue);
				} else if (keyValue instanceof List
						&& ((List<String>) keyValue).size() > 0) {
					builder.append(queryKey, EqOperator.getInstance(),
							((List<String>) keyValue).toArray(new String[0]));
				} else if (keyValue instanceof String[]
						&& ((String[]) keyValue).length > 0) {
					builder.append(queryKey, EqOperator.getInstance(),
							(String[]) keyValue);
				} else if (keyValue instanceof String
						&& StringUtils.isNotEmpty((String) keyValue)) {
					builder.append(queryKey, EqOperator.getInstance(),
							new String[] { (String) keyValue });
				} else {
					return NCCRestUtils.toJSONString(CallReturnBuildUtil
							.buildParamFormatResult(queryKey));
				}
			}
			PurchaseInVO[] purInVOs = queryVOByScheme(builder.create());
			return NCCRestUtils
					.toJSONString(CallReturnBuildUtil
							.buildSuccessResult(
									purInVOs,
									nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
											.getStrByID("4008027_0",
													"04008027-0417")/*
																	 * @res
																	 * "采购入库单查询成功"
																	 */));
		} catch (Exception e) {
			return NCCRestUtils.toJSONString(CallReturnBuildUtil
					.buildFailResult(null,
							nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
									.getStrByID("4008027_0", "04008027-0418")/*
																			 * @res
																			 * "采购入库单查询异常:"
																			 */
									+ e.getMessage()));
		}

	}

	public PurchaseInVO[] queryVOByScheme(IQueryScheme queryScheme)
			throws BusinessException {
		try {
			SCMBillQuery<PurchaseInVO> queryTool = new SCMBillQuery<PurchaseInVO>(
					PurchaseInVO.class);
			return queryTool.queryVOByScheme(queryScheme);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
			return null;
		}
	}
}
