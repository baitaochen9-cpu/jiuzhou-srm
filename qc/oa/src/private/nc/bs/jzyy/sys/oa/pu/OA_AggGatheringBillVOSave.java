package nc.bs.jzyy.sys.oa.pu;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import nc.bs.arap.bill.CalcMoneyUtil;
import nc.bs.dao.BaseDAO;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.ic.pub.env.ICBSContext;
import nc.bs.logging.Logger;
import nc.impl.pubapp.pattern.data.bill.BillQuery;
import nc.impl.pubapp.pattern.data.vo.VOQuery;
import nc.itf.scmpub.reference.uap.bd.customer.CustomerPubService;
import nc.itf.scmpub.reference.uap.org.DeptPubService;
import nc.itf.scmpub.reference.uap.org.OrgUnitPubService;
import nc.itf.scmpub.reference.uap.pf.PfServiceScmUtil;
import nc.vo.cmp.bill.BillDetailVO;
import nc.vo.cmp.bill.BillVO;
import nc.itf.uap.pf.IPFBusiAction;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.bs.jzyy.sys.AbstracAdapter4Ext;
import nc.uif.pub.exception.UifException;
import nc.vo.arap.gathering.AggGatheringBillVO;
import nc.vo.arap.gathering.GatheringBillItemVO;
import nc.vo.arap.gathering.GatheringBillVO;
import nc.vo.arap.pay.PayBillItemVO;
import nc.vo.bd.cust.saleinfo.CustsaleVO;
import nc.vo.cmp.bill.BillAggVO;
import nc.vo.ic.material.define.InvBasVO;
import nc.vo.ic.org.OrgVO;
import nc.vo.jcom.lang.StringUtil;

import nc.vo.pu.m21.entity.OrderVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVOUtil;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.pubapp.pattern.model.entity.bill.IBill;
import nc.vo.pubapp.pattern.pub.PubAppTool;

import nc.vo.trade.checkrule.VOChecker;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class OA_AggGatheringBillVOSave extends AbstracAdapter4Ext {

	@Override
	public JSONObject sys(Object billvo) throws BusinessException {
		// SaleOrderResource
		// nc.ui.so.m30.billui.editor.bodyevent.BodyBeforeEditHandler
		JSONObject jsonObject = (JSONObject) billvo;
		// 检索数据
		AggGatheringBillVO order = null;
		try {
//			JSONObject bill = jsonObject.getJSONObject("data");
			order = getBillAggVO(jsonObject);
			processBill(order,jsonObject);
		} catch (Exception e) {
			e.printStackTrace();
			Logger.error(e.getMessage(), e);
			return getRsultJsonFailed("生成收款单出错:" + e.getMessage());
		}
		JSONObject data = new JSONObject();
		data.put("code", order.getParent().getAttributeValue("billno"));
		data.put("pk", order.getHeadVO().getPk_gatherbill());
		// 将结果返回
		return getRsultDataSuccess(data, "生成收款单成功");
	}

	private AggGatheringBillVO getBillAggVO(JSONObject bill)
			throws BusinessException {
		
		Map<String, String> headmap = (Map) bill;

		String  vbillcode = headmap.get("vbillcode");
		if (getString_TrimZeroLenAsNull(vbillcode) == null) {
			throw new BusinessException("单据号不能为空");
		}

		String pk_org =  headmap.get("pk_org");
		if (getString_TrimZeroLenAsNull(pk_org) == null) {
			throw new BusinessException("采购组织不能为空");
		}

		pk_org = getSalesorg(pk_org);

		if (getString_TrimZeroLenAsNull(pk_org) == null) {
			throw new BusinessException("采购组织在nc中不存在");
		}
		VOQuery<GatheringBillVO> query = new VOQuery<GatheringBillVO>(
				GatheringBillVO.class);
		GatheringBillVO[] hvos = query.query(" and pk_org='" + pk_org
				+ "' and billno ='" + vbillcode + "'", null);
		if (hvos == null || hvos.length == 0) {
			throw new BusinessException("nc中不存在的收款的,单据编号"+vbillcode);

		}
		
		BillQuery<AggGatheringBillVO>billquery=new BillQuery(AggGatheringBillVO.class);
		AggGatheringBillVO[]  order=billquery.query(new String[]{hvos[0].getPk_gatherbill()});
		if(order==null||order.length==0){
			throw  new  BusinessException("未查询到采购订单");
		}
		
		return order[0];
	}



	private List<GatheringBillItemVO> getBillDetailVO(JSONObject bill)
			throws BusinessException {

		List<GatheringBillItemVO> list = new ArrayList<GatheringBillItemVO>();

		JSONArray childArray = bill.getJSONArray("childvo"); // 如果json格式的字符串里含有数组格式的属性，将其转换成JSONArray，以方便后面转换成对应的实体
		String pk_group = InvocationInfoProxy.getInstance().getGroupId();

		for (int i = 0; i < childArray.size(); i++) {
			GatheringBillItemVO vo = new GatheringBillItemVO();
			Map<String, String> childVomap = (Map) childArray.get(i);
			vo.setPk_group("0001C1100000000007QJ");
			for (String itemkey : childVomap.keySet()) {
				// 0=，1=，
				if ("prepay".equals(itemkey)) {
					if (childVomap.get(itemkey) != null
							&& !"".equals(childVomap.get(itemkey))) {
						String prepay = childVomap.get(itemkey).toString();
						if ("应收款".equals(prepay)) {
							vo.setPrepay(0);
						} else if ("预收款".equals(prepay)) {
							vo.setPrepay(1);
						}
					}
					continue;

				}

				if ("taxcodeid".equals(itemkey)) {

					if (childVomap.get(itemkey) != null
							&& !"".equals(childVomap.get(itemkey))) {

						String taxcode = childVomap.get(itemkey);
						// String pk_org=childVomap.get("pk_org");
						String sql = "select  b.TAXRATE\n"
								+ "  from BD_TAXCODE a\n"
								+ "  join BD_TAXRATE b\n"
								+ "    on a.PK_TAXCODE = b.PK_TAXCODE\n"
								+ "    where a.dr=0\n" + "    and a.code='"
								+ taxcode + "'";

						BaseDAO dao = new BaseDAO();
						String taxrate2 = (String) getDao().executeQuery(sql,
								new ColumnProcessor());
						if (!PubAppTool.isNull(taxrate2)) {
							vo.setTaxrate(new UFDouble(taxrate2));
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

	private Object processBill(AggGatheringBillVO order,JSONObject jsonObject )
			throws BusinessException {

		// 2. 校验数据的合法性:1.数据结构完整 2.根据组织+单据号校验是否重复.
		checkData(order);
		// 3.补全数据,并且调整单据状态
		fillData(order, jsonObject );
		


		AggGatheringBillVO bill2 = (AggGatheringBillVO) insert(order);
		


		return bill2.getParentVO().getPrimaryKey();
	}

	private void fillData(AggGatheringBillVO order,JSONObject jsonObject ) throws BusinessException {
		
	
//TODO
		
		Map<String, String> headmap = (Map) jsonObject;

		
		
//		这个地方是否有多行的情况
		List<String>formulas=new ArrayList<String>();
		
		for (int i = 0; i < order.getBodyVOs().length; i++) {
			String  purchaseorder = headmap.get("purchaseorder");
			order.getBodyVOs()[i].setPurchaseorder(purchaseorder);
			
			String  invoiceno = headmap.get("invoiceno");
			order.getBodyVOs()[i].setInvoiceno(invoiceno);
			
			
			if("".equals(purchaseorder)&&"".equals(invoiceno)){
				throw  new  BusinessException("订单号或销售发票号必须有值");
			}
			
			
			String  customer = headmap.get("customer");
			if(customer!=null&&!"".equals(customer)){
				order.getBodyVOs()[i].setAttributeValue("customer", customer);
				if(i==0)
				formulas.add("customer->getColValue2(bd_customer,pk_customer,code ,customer,dr,\"0\")");
			}
			
			String  pk_psndoc = headmap.get("pk_psndoc");
			if(pk_psndoc!=null&&!"".equals(pk_psndoc)){
				order.getBodyVOs()[i].setAttributeValue("pk_psndoc", pk_psndoc);
				if(i==0)
				formulas.add("pk_psndoc->getColValue2(bd_psndoc,pk_psndoc,code,pk_psndoc,dr,\"0\")");
			}
			
			String  prepay = headmap.get("prepay");
			if(prepay!=null&&!"".equals(prepay)){
				order.getBodyVOs()[i].setAttributeValue("prepay", prepay);
//				formulas_F3_B.add("pk_psndoc->getColValue2(bd_psndoc,pk_psndoc,code,pk_psndoc,dr,\"0\")");
			}
			
				String  material = headmap.get("material");
			if(material!=null&&!"".equals(material)){
				order.getBodyVOs()[i].setAttributeValue("material", material);
				if(i==0)
				formulas.add("material->getColValue2(bd_material,pk_material,code,material,dr,\"0\")");
			}
			
			
			String  project = headmap.get("project");
			if(project!=null&&!"".equals(prepay)){
				order.getBodyVOs()[i].setAttributeValue("project", project);
				if(i==0)
				formulas.add("project->getColValue2(bd_project,pk_project,project_code,project,dr,\"0\")");
			}
			
		}
		if(formulas.size()>0){
			SuperVOUtil.execFormulaWithVOs(order.getBodyVOs(), formulas.toArray(new String[formulas.size()]),
					null);

		}
		
		
	}

	// public

	// 收款结算单表体

	// public
	protected AggGatheringBillVO insert(AggGatheringBillVO billvo)
			throws BusinessException {

		IPFBusiAction service = NCLocator.getInstance().lookup(
				IPFBusiAction.class);
		InvocationInfoProxy.getInstance().setGroupId(
				billvo.getHeadVO().getAttributeValue("pk_group").toString());
		InvocationInfoProxy.getInstance().setUserId(
				billvo.getHeadVO().getCreator());

		// 保存
		AggGatheringBillVO[] returnVO1 = (AggGatheringBillVO[]) service
				.processBatch("SAVE", "F2",
						new AggGatheringBillVO[] { billvo }, null, null, null);
		
		AggGatheringBillVO[]  agg=queryaggvo(returnVO1[0].getHeadVO().getPk_gatherbill());
//		审核
		service.processBatch("APPROVE", "F2",
						new AggGatheringBillVO[] { billvo }, null, null, null);
		
		
		
		return returnVO1[0];
	}

	
	public AggGatheringBillVO[] queryaggvo(String pk) {
		BillQuery<AggGatheringBillVO> billquery = new BillQuery<AggGatheringBillVO>(AggGatheringBillVO.class);
		AggGatheringBillVO[] aggvo = billquery.query(new String[] { pk });
		return aggvo;
	}
	private void checkData(AggGatheringBillVO resvo) throws BusinessException {
		if (resvo == null || resvo.getParentVO() == null)
			throw new BusinessException("未获取的转换后的数据");
		if (resvo.getChildrenVO() == null || resvo.getChildrenVO().length == 0) {
			throw new BusinessException("表体不允许为空");
		}

	}

}
