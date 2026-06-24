package nc.bs.jzyy.sys.oa.pu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.arap.bill.CalcMoneyUtil;
import nc.bs.arap.util.ArapFlowUtil;
import nc.bs.dao.BaseDAO;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;

import nc.bs.logging.Logger;

import nc.impl.pubapp.pattern.data.bill.BillQuery;
import nc.impl.pubapp.pattern.data.vo.VOQuery;
import nc.itf.scmpub.reference.uap.pf.PfServiceScmUtil;
import nc.itf.uap.pf.IPFBusiAction;
import nc.itf.uap.pf.IplatFormEntry;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapProcessor;
import nc.bs.jzyy.sys.AbstracAdapter4Ext;

import nc.vo.arap.gathering.AggGatheringBillVO;
import nc.vo.arap.pay.AggPayBillVO;
import nc.vo.arap.pay.PayBillItemVO;
import nc.vo.ic.m45.entity.PurchaseInVO;
import nc.vo.pu.m21.entity.OrderHeaderVO;
import nc.vo.pu.m21.entity.OrderVO;
import nc.vo.pu.m21.entity.PayPlanVO;
import nc.vo.pu.m23.entity.ArriveVO;
import nc.vo.pu.m25.entity.InvoiceHeaderVO;
import nc.vo.pu.m25.entity.InvoiceItemVO;
import nc.vo.pu.m25.entity.InvoiceVO;

import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVOUtil;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.workflownote.WorkflownoteVO;
import nc.vo.pubapp.pattern.pub.PubAppTool;
import nc.vo.pubapp.pflow.PfUserObject;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class OA_PuOrderToPaybillSave extends AbstracAdapter4Ext {

	@Override
	public JSONObject sys(Object billvo) throws BusinessException {
		// SaleOrderResource
		// nc.ui.so.m30.billui.editor.bodyevent.BodyBeforeEditHandler
		JSONObject jsonObject = (JSONObject) billvo;
		// 检索数据
		AggPayBillVO order = null;
		try {
//			JSONObject bill = jsonObject.getJSONObject("data");
			order = getBillAggVO(jsonObject);
			processBill(order);
		} catch (Exception e) {
			e.printStackTrace();
			Logger.error(e.getMessage(), e);
			return getRsultJsonFailed("生成付款单出错:" + e.getMessage());
		}
		JSONObject data = new JSONObject();
		data.put("code", order.getParent().getAttributeValue("vbillcode"));
		data.put("pk", order.getParentVO().getPrimaryKey());
		// 将结果返回
		return getRsultDataSuccess(data, "生成付款单成功");
	}

	private AggPayBillVO getBillAggVO(JSONObject bill) throws BusinessException {
		OrderVO  inv= new OrderVO();
		
		
		
		
		
		
//		String  pk=headmap.get("pk").toString();
		
//		JSONObject data = jsonObject.getJSONObject("data");

		String  pk_payplan = bill.getString("pk_payplan");
		if (getString_TrimZeroLenAsNull(pk_payplan) == null) {
			throw new BusinessException("单据号不能为空");
		}
		
		String sql=
				"select a.pk_order\n" +
						"  from po_order a\n" + 
						" where a.pk_order in\n" + 
						"       (select a.pk_order\n" + 
						"          from po_order_payplan a\n" + 
						"         where a.pk_order_payplan = '"+pk_payplan+"')";

		
		BaseDAO dao = new BaseDAO();

		String pk="";
		Map<String, String> map = (Map<String, String>) dao
				.executeQuery(sql, null, new MapProcessor());
		if (map != null) {
			
			pk=String.valueOf(map
					.get("pk_order"));
		}else{
			throw new BusinessException("nc中不存在的采购订单,"+pk_payplan);
		}
		

//		VOQuery<OrderHeaderVO> query = new VOQuery<OrderHeaderVO>(
//				OrderHeaderVO.class);
//		OrderHeaderVO[] hvos = query.query(" pk_order = (select  a.pk_order from  po_order_payplan a where a.pk_order_payplan='" + pk_payplan + "' )", null);
//		if (hvos == null || hvos.length == 0) {
//			throw new BusinessException("nc中不存在的采购订单,"+pk_payplan);
//
//		}
		
		
		BillQuery<OrderVO>billquery=new BillQuery(OrderVO.class);
		OrderVO[]  order=billquery.query(new String[]{pk});
		if(order==null||order.length==0){
			throw  new  BusinessException("未查询到采购订单");
		}
		
		PayPlanVO    pay=new  PayPlanVO();
		for(int i=0;i<order[0].getTableVO("pk_order_payplan").length;i++){
			String pk_order_payplan=order[0].getTableVO("pk_order_payplan")[i].getAttributeValue("pk_order_payplan").toString();
			if(pk_order_payplan.equals(pk_payplan)){
				pay=(PayPlanVO) order[0].getTableVO("pk_order_payplan")[i];
			}
		}
//		
		order[0].setTableVO("pk_order_payplan", new PayPlanVO[]{pay});
		AggPayBillVO[] changevo=(AggPayBillVO[])PfServiceScmUtil.exeVOChangeByBizFlow("21", "F3", order);
		 
		
		
			
	
		
		return changevo[0];
	}
	public PurchaseInVO [] queryaggvo(String pk) {
		BillQuery<PurchaseInVO> billquery = new BillQuery<PurchaseInVO>(PurchaseInVO.class);
		PurchaseInVO[] aggvo = billquery.query(new String[] { pk });
		return aggvo;
	}

	
	private Object processBill(AggPayBillVO order) throws BusinessException {

		// 2. 校验数据的合法性:1.数据结构完整 2.根据组织+单据号校验是否重复.
		checkData(order);
//		// 3.补全数据,并且调整单据状态
//		fillData(order);

	

		AggPayBillVO bill2 = (AggPayBillVO) insert(order);

		return bill2.getParentVO().getPrimaryKey();
	}

	

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
		
	

//		AggPayBillVO[] returnvo = (AggPayBillVO[]) service.processBatch(
//				actioncode, "F3", new AggPayBillVO[] { billvo }, null, null,
//				null);
		AggPayBillVO [] returnvo=(AggPayBillVO[]) service.processBatch(actioncode,"F3", new AggPayBillVO[] {billvo}, new PfUserObject[] { new PfUserObject() }, new WorkflownoteVO(), null);
		return returnvo[0];
	}
	
	public OrderVO [] queryaggvo2(String pk) {
		BillQuery<OrderVO> billquery = new BillQuery<OrderVO>(OrderVO.class);
		OrderVO[] aggvo = billquery.query(new String[] { pk });
		return aggvo;
	}

	private void checkData(AggPayBillVO order) throws BusinessException {
		if (order == null || order.getParentVO() == null)
			throw new BusinessException("未获取的转换后的数据");
		if (order.getChildrenVO() == null || order.getChildrenVO().length == 0) {
			throw new BusinessException("表体不允许为空");
		}

	}

}
