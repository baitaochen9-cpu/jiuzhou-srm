package nc.bs.jzyy.sys.oa.arap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.api.rest.arap.utils.ArapBillConvert;
import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.jzyy.sys.AbstracAdapter4Ext;
import nc.itf.arap.fieldmap.IBillFieldGet;
import nc.jdbc.framework.processor.ColumnListProcessor;
import nc.md.persist.framework.MDPersistenceService;
import nc.vo.arap.basebill.BaseBillVO;
import nc.vo.arap.basebill.BaseItemVO;
import nc.vo.arap.gathering.AggGatheringBillVO;
import nc.vo.arap.gathering.GatheringBillItemVO;
import nc.vo.arap.gathering.GatheringBillVO;
import nc.vo.ic.pub.define.ICPubMetaNameConst;
import nc.vo.pub.BusinessException;
import nccloud.api.jzyy.JZYYResultMessageUtil;

import com.alibaba.fastjson.JSONObject;

public class OA_GaterBillQuery extends AbstracAdapter4Ext {

	@Override
	public JSONObject sys(Object billvo) throws BusinessException, DAOException {

		JSONObject rqJosn = (JSONObject) billvo;
		// 检索数据
		List<Map<String, Object>> respData = null;
		try { 
			JSONObject bill = rqJosn.getJSONObject("bill");
			Map<String, String> paramMap = getCondMap(bill);
			respData = queryBill(paramMap);
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

	private Map<String, String> getCondMap(JSONObject bill) {
		Map<String, String> paramMap = new HashMap<>();
		paramMap.put(IBillFieldGet.BILLNO, bill.getString("vbillcode"));// 单据号
		paramMap.put(IBillFieldGet.BILLDATE, bill.getString("dbilldate"));// 收款日期
		paramMap.put(IBillFieldGet.RECACCOUNT, bill.getString("recaccount"));// 收款银行账号
		paramMap.put(IBillFieldGet.CASHACCOUNT, bill.getString("cashaccount"));// 现金账户
		paramMap.put(IBillFieldGet.PAYACCOUNT, bill.getString("payaccount"));// 付款银行账号
		paramMap.put(IBillFieldGet.PK_CURRTYPE, bill.getString("currtype"));// 币种
		paramMap.put(IBillFieldGet.LOCAL_MONEY_CR,
				bill.getString("local_money_cr"));// 金额
		paramMap.put(IBillFieldGet.PK_BALATYPE, bill.getString("pk_balatype"));// 结算方式
		paramMap.put(BaseBillVO.PK_ORG, bill.getString("org_code"));// 组织
		paramMap.put(ICPubMetaNameConst.PK_GROUP, bill.getString("group_code"));// 集团
		return paramMap;
	}

	private List<Map<String, Object>> queryBill(Map<String, String> conditionMap)
			throws BusinessException {
		if (conditionMap == null || conditionMap.size() == 0)
			return null;

		ArapBillConvert billconvert = new ArapBillConvert();
		String select = "select distinct ar_gatherbill.pk_gatherbill ";
		String where = billconvert.getConditionSql(conditionMap,
				"ar_gatherbill", "ar_gatheritem");
		String from = "ar_gatherbill ar_gatherbill inner join ar_gatheritem ar_gatheritem ON ar_gatheritem.pk_gatherbill = ar_gatherbill.pk_gatherbill";

		String sql = select + " from " + from + " where " + where
				+ " and abs(COALESCE(ar_gatheritem.money_bal, 0.0)) > 0.0";
		List<String> pks = (List<String>) new BaseDAO().executeQuery(sql,
				new ColumnListProcessor());
		if (pks != null && pks.size() > 0) {
			Collection<AggGatheringBillVO> billvos = MDPersistenceService
					.lookupPersistenceQueryService().queryBillOfVOByPKs(
							AggGatheringBillVO.class,
							pks.toArray(new String[0]), false);
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			for (AggGatheringBillVO billvo : billvos) {
				GatheringBillVO headVO = billvo.getHeadVO();
				GatheringBillItemVO[] itemvos = billvo.getBodyVOs();
				for (GatheringBillItemVO item : itemvos) {
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("vbillcode", headVO.getBillno());// 应付单号
					map.put("pk_gatherbill", headVO.getPk_gatherbill());// 应付单表头ID
					map.put("pk_gatheritem", item.getPk_gatheritem());// 应付单表体ID
					map.put("cvendorid", item.getSupplier());// 供应商
					map.put("corigcurrencyid", item.getPk_currtype());// 币种
					map.put("ninvoicemny", item.getMoney_bal() == null ? null
							: item.getMoney_bal().toString());// 原币余额
					map.put("material", item.getMaterial());// 物料
					map.put("vbatchcode", item.getVendorvatcode());// 批次号
					map.put("dept_code", item.getPk_deptid());// 采购部门
					map.put("usercode", item.getPk_psndoc());// 采购员
					map.put("project", item.getProject());// 项目
					// map.put("paymentdays", item.getPayaccount());// 账期天数
					map.put("pointsdays", headVO.getBusidate() == null ? null
							: headVO.getBusidate().toString());// 启算日期
					list.add(map);
				}
			}
			return list;
		}
		return null;
	}

	private List<Map<String, Object>> convertVOToMap(
			Collection<AggGatheringBillVO> billvos, ArapBillConvert billconvert) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (AggGatheringBillVO billvo : billvos) {
			Map<String, Object> map = new HashMap<String, Object>();
			GatheringBillVO headVO = billvo.getHeadVO();
			Map<String, String> headMap = billconvert.convertVOToMap(headVO);
			map.putAll(headMap);
			BaseItemVO[] itemvos = billvo.getItems();
			List<Map<String, String>> items = new ArrayList<Map<String, String>>();
			for (BaseItemVO item : itemvos) {
				Map<String, String> itemMap = billconvert.convertVOToMap(item);
				items.add(itemMap);
			}
			map.put("items", items);
			list.add(map);
		}
		return list;

	}
}
