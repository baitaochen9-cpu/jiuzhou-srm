package nc.bs.jzyy.sys.oa.pu;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import nc.bs.arap.bill.CalcMoneyUtil;
import nc.bs.arap.util.ArapFlowUtil;
import nc.bs.dao.BaseDAO;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;

import nc.bs.logging.Logger;

import nc.itf.uap.pf.IPFBusiAction;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapProcessor;
import nc.bs.jzyy.sys.AbstracAdapter4Ext;

import nc.vo.arap.pay.AggPayBillVO;
import nc.vo.arap.pay.PayBillItemVO;
import nc.vo.arap.pay.PayBillVO;

import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVOUtil;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.workflownote.WorkflownoteVO;
import nc.vo.pubapp.pattern.pub.PubAppTool;
import nc.vo.pubapp.pflow.PfUserObject;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class OA_Ap_PaybillSave extends AbstracAdapter4Ext {

	@Override
	public JSONObject sys(Object billvo) throws BusinessException {
		// SaleOrderResource
		// nc.ui.so.m30.billui.editor.bodyevent.BodyBeforeEditHandler
		JSONObject jsonObject = (JSONObject) billvo;
		// 检索数据
		AggPayBillVO order = null;
		try {
			JSONObject bill = jsonObject.getJSONObject("data");
			order = getBillAggVO(bill);
			processBill(order);
		} catch (Exception e) {
			e.printStackTrace();
			Logger.error(e.getMessage(), e);
			return getRsultJsonFailed("生成付款单出错:" + e.getMessage());
		}
		JSONObject data = new JSONObject();
		data.put("code", order.getParent().getAttributeValue("billno"));
		data.put("pk", order.getHeadVO().getPk_paybill());
		// 将结果返回
		return getRsultDataSuccess(data, "生成付款单成功");
	}

	private AggPayBillVO getBillAggVO(JSONObject bill) throws BusinessException {

		AggPayBillVO order = new AggPayBillVO();
		PayBillVO hvo = getBillVO(bill);
		List<PayBillItemVO> list = getBillDetailVO(bill);
		order.setParent(hvo);
		order.setChildrenVO(list.toArray(new PayBillItemVO[list.size()]));
		return order;
	}

	private PayBillVO getBillVO(JSONObject bill) throws BusinessException {
		PayBillVO hvo = new PayBillVO();
		Map<String, String> headmap = (Map) bill.getJSONObject("hand");
		for (String itemkey : headmap.keySet()) {
			if (headmap.get(itemkey) != null && !"".equals(itemkey)) {
				hvo.setAttributeValue(itemkey, headmap.get(itemkey));
			}
		}
		hvo.setStatus(VOStatus.NEW);
		String pk_org = hvo.getPk_org();
		if (getString_TrimZeroLenAsNull(pk_org) == null) {
			throw new BusinessException("组织不能为空");
		}
		return hvo;

	}

	private List<PayBillItemVO> getBillDetailVO(JSONObject bill)
			throws BusinessException {

		List<PayBillItemVO> list = new ArrayList<PayBillItemVO>();

		JSONArray childArray = bill.getJSONArray("bodys"); // 如果json格式的字符串里含有数组格式的属性，将其转换成JSONArray，以方便后面转换成对应的实体
		for (int i = 0; i < childArray.size(); i++) {
			PayBillItemVO vo = new PayBillItemVO();
			Map<String, String> childVomap = (Map) childArray.get(i);
			for (String itemkey : childVomap.keySet()) {
				// prepay 付款性质 prepay int(50) 付款性质 0=应付款，1=预付款，
				if ("prepay".equals(itemkey)) {
					if (childVomap.get(itemkey) != null
							&& !"".equals(childVomap.get(itemkey))) {
						String prepay = childVomap.get(itemkey).toString();
						if ("应付款".equals(prepay)) {
							vo.setPrepay(0);
						} else if ("预付款".equals(prepay)) {
							vo.setPrepay(1);
						}
					}
					continue;

				}

				if ("taxcodeid".equals(itemkey)) {

					if (childVomap.get(itemkey) != null
							&& !"".equals(childVomap.get(itemkey))) {

						String taxrate = childVomap.get(itemkey);
						// String pk_org=childVomap.get("pk_org");
						String sql = "select  b.TAXRATE,a.pk_taxcode \n"
								+ "  from BD_TAXCODE a\n"
								+ "  join BD_TAXRATE b\n"
								+ "    on a.PK_TAXCODE = b.PK_TAXCODE\n"
								+ "    where a.dr=0\n" + "    and a.code='"
								+ taxrate + "'";

						BaseDAO dao = new BaseDAO();
						// UFDouble taxrate2 = (UFDouble)
						// getDao().executeQuery(sql, new ColumnProcessor());

						Map<String, String> map = (Map<String, String>) dao
								.executeQuery(sql, null, new MapProcessor());
						if (map != null) {
							vo.setTaxrate(new UFDouble(String.valueOf(map
									.get("taxrate"))));
							vo.setTaxcodeid(String.valueOf(map
									.get("pk_taxcode")));
						}

						continue;
					}

				}

				if ("subjcode".equals(itemkey)) {
					if (childVomap.get(itemkey) != null
							&& !"".equals(childVomap.get(itemkey))) {

						String subjcode = childVomap.get(itemkey);
						String pk_org = childVomap.get("pk_org");
						String sql = "select bd_accasoa.pk_accasoa \n"
								+ "                     \n"
								+ "               from bd_accasoa bd_accasoa\n"
								+ "               left join bd_account\n"
								+ "                 on bd_accasoa.pk_account = bd_account.pk_account\n"
								+ "               left join bd_accchart f\n"
								+ "                 on f.pk_accchart = bd_accasoa.pk_accchart\n"
								+ "               left join org_orgs g\n"
								+ "                 on g.pk_org = f.pk_org\n"
								+ "              where 11 = 11\n"
								+ "                and (bd_accasoa.enablestate = 2)\n"
								+ "                and bd_account.code = '"
								+ subjcode + "' and g.code='" + pk_org + "'";
						BaseDAO dao = new BaseDAO();
						String pk_accasoa = (String) getDao().executeQuery(sql,
								new ColumnProcessor());
						if (!PubAppTool.isNull(pk_accasoa)) {
							vo.setSubjcode(pk_accasoa);
						}

						continue;
					}
				}

				if (childVomap.get(itemkey) != null
						&& !"".equals(childVomap.get(itemkey))) {
					vo.setAttributeValue(itemkey, childVomap.get(itemkey));

				}

			}
			list.add(vo);
		}
		return list;

	}

	private Object processBill(AggPayBillVO order) throws BusinessException {

		// 2. 校验数据的合法性:1.数据结构完整 2.根据组织+单据号校验是否重复.
		checkData(order);
		// 3.补全数据,并且调整单据状态
		fillData(order);

		CalcMoneyUtil calmny = new CalcMoneyUtil();
		calmny.processMoney(order);

		AggPayBillVO bill2 = (AggPayBillVO) insert(order);

		return bill2.getParentVO().getPrimaryKey();
	}

	private void fillData(AggPayBillVO order) {
		// TODO Auto-generated method stub
		String[] formulas_F3 = {
				// 集团
				"pk_group->getColValue(org_group,  pk_group ,code ,pk_group)",
				// 创建人
				"creator->getColValue(sm_user,cuserid,user_code,creator)",
				// 最新的销售组织
				"pk_org->getColValue2(org_financeorg,pk_financeorg,code,pk_org,islastversion,\"Y\")",
				// 销售组织版本号
				"pk_org_v->getColValue2(org_financeorg,pk_vid,pk_financeorg,pk_org,islastversion,\"Y\")",
				// 币种
				"pk_currtype->getColValue(bd_currtype,pk_currtype,code,pk_currtype)",
				// 单据类型编码
				"pk_billtype->\"F3\"",
				// 业务流程
				"pk_busitype->\"0001V1100000000010NY\"",
				"syscode->\"1\"",
				// "pk_tradetype->\"F3\"",
				// 付款类型
				"pk_tradetypeid->getColValue(bd_billtype,pk_billtypeid,pk_billtypecode,pk_tradetype)",

				// 部门
				"pk_dept->getColValue2(org_dept,pk_dept,code,pk_dept,pk_org,pk_org)",
				"pk_dept_v->getColValue2(org_dept_v,pk_vid,pk_dept,pk_dept,islastversion,\"Y\")",
				// 部门版本

				// "pk_deptid_v->getColValue2(org_dept_v,pk_vid,pk_dept,pk_deptid,islastversion,\"Y\")",

				"src_syscode->\"1\"", "billstatus->\"-1\"",
				//"creator->getColValue(sm_user,cuserid,user_code,creator)",
				"billmaker->creator", "pk_fiorg->pk_org",
				"pk_fiorg_v->pk_org_v", "sett_org->pk_org",
				"sett_org_v->pk_org_v"

		};

		SuperVOUtil.execFormulaWithVOs(
				new PayBillVO[] { (PayBillVO) order.getParentVO() },
				formulas_F3, null);

		String[] formulas_F3_B = {
				// 币种
				"pk_currtype->getColValue(bd_currtype,pk_currtype,code,pk_currtype)",
				// 最新的组织
				"pk_org->getColValue2(org_financeorg,pk_financeorg,code,pk_org,islastversion,\"Y\")",
				// 组织版本号
				"pk_org_v->getColValue2(org_financeorg,pk_vid,pk_financeorg,pk_org,islastversion,\"Y\")",

				// 物料
				"material->getColValue(bd_material,pk_material,code,material)",
				// 税码
				// "taxcodeid->getColValue(bd_taxcode,pk_taxcode,code,taxcodeid)",
				// 制单人
				"creator->getColValue(sm_user,cuserid,user_code,creator)",
				// "money_bal->money_de",
				// "local_notax_de->money_de",
				// "local_money_de->money_de",

				// "local_price->local_taxprice",
				// "price->local_taxprice",
				"direction->1",
				// 结算方式
				"pk_balatype->getColValue(bd_balatype,pk_balatype,code,pk_balatype)",
				// 付款业务类型
				"pk_recpaytype->getColValue(fi_recpaytype,pk_recpaytype,code,pk_recpaytype)",
				"rate->\"1\"",
				"material->getColValue(bd_material,pk_material,code,material)",

				"pu_deptid->getColValue(org_dept,pk_dept,code,pu_deptid)",
				"pu_psndoc->getColValue(bd_psndoc,pk_psndoc,code,pu_psndoc)",
				// 收支项目~
				"pk_subjcode->getColValue2(bd_inoutbusiclass,pk_inoutbusiclass,code,pk_subjcode,dr,\"0\")",

				"supplier->getColValue(bd_supplier,pk_supplier,code,supplier)",
				"recaccount->getColValue(bd_bankaccsub,pk_bankaccsub,accnum,recaccount)",
				// 付款银行账户
				"payaccount->getColValue(bd_bankaccsub,pk_bankaccsub,accnum,payaccount)",
				"pk_fiorg->pk_org", "pk_fiorg_v->pk_org_v", "sett_org->pk_org",
				"sett_org_v->pk_org_v"

		};

		SuperVOUtil.execFormulaWithVOs(order.getChildrenVO(), formulas_F3_B,
				null);
		for (int i = 0; i < order.getChildrenVO().length; i++) {
			order.getChildrenVO()[i].setAttributeValue("pk_group", order.getHeadVO().getAttributeValue("pk_group").toString());
		}
	}

	// public String[] formulas_F3 = {
	// //最新的销售组织
	// "pk_org->getColValue2(org_financeorg,pk_financeorg,code,pk_org,islastversion,\"Y\")",
	// //销售组织版本号
	// "pk_org_v->getColValue2(org_financeorg,pk_vid,pk_financeorg,pk_org,islastversion,\"Y\")",
	// //单据类型编码
	// "pk_billtype->\"F3\"",
	// // 业务流程
	// "pk_busitype->\"0001A110000000000PJZ\"",
	// "syscode->\"1\"",
	// //"pk_tradetype->\"F3\"",
	// // 单据类型编码
	// "pk_tradetypeid->getColValue(bd_billtype,pk_billtypeid,pk_billtypecode,pk_tradetype)",
	//
	//
	//
	// "src_syscode->\"1\"",
	// "billstatus->\"-1\"",
	// "creator->getColValue(sm_user,cuserid,user_code,creator)",
	// "billmaker->creator",
	// "pk_currtype->getColValue(bd_currtype,pk_currtype,code,pk_currtype)"
	//
	// };

	/**
	 * 付款单接口表体执行公式
	 */

	protected AggPayBillVO insert(AggPayBillVO billvo) throws BusinessException {

		IPFBusiAction service = NCLocator.getInstance().lookup(
				IPFBusiAction.class);
		// 保存

		InvocationInfoProxy.getInstance().setGroupId(
				billvo.getHeadVO().getAttributeValue("pk_group").toString());
		InvocationInfoProxy.getInstance().setUserId(
				billvo.getHeadVO().getCreator());
		billvo.getHeadVO().setStatus(VOStatus.NEW);
		String actioncode = ArapFlowUtil.getCommitActionCode(billvo.getHeadVO()
				.getPk_org(), billvo.getHeadVO().getPk_tradetype());

//		AggPayBillVO[] returnVO1 = (AggPayBillVO[]) service.processBatch(
//				actioncode, "F3", new AggPayBillVO[] { billvo }, null, null,
//				null);
		AggPayBillVO [] returnvo=(AggPayBillVO[]) service.processBatch(actioncode,"F3", new AggPayBillVO[] {billvo}, new PfUserObject[] { new PfUserObject() }, new WorkflownoteVO(), null);
		return returnvo[0];
	}

	private void checkData(AggPayBillVO resvo) throws BusinessException {
		if (resvo == null || resvo.getParentVO() == null)
			throw new BusinessException("未获取的转换后的数据");
		if (resvo.getChildrenVO() == null || resvo.getChildrenVO().length == 0) {
			throw new BusinessException("表体不允许为空");
		}

	}

}
