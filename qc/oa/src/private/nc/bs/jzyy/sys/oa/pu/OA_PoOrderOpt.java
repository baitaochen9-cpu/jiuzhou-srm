package nc.bs.jzyy.sys.oa.pu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.arap.util.ArapFlowUtil;
import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.jzyy.sys.AbstracAdapter4Ext;
import nc.bs.logging.Logger;
import nc.impl.pubapp.pattern.data.bill.BillQuery;
import nc.impl.pubapp.pattern.data.vo.VOQuery;
import nc.itf.jzyy.sys.ISysDispatcher;
import nc.itf.pu.m21.IOrderApprove;
import nc.itf.pu.m21.IOrderMaintain;
import nc.itf.uap.pf.IPFBusiAction;
import nc.itf.uap.pf.IplatFormEntry;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.vo.pu.m21.entity.OrderHeaderVO;
import nc.vo.pu.m21.entity.OrderItemVO;
import nc.vo.pu.m21.entity.OrderPaymentVO;
import nc.vo.pu.m21.entity.OrderVO;
import nc.vo.pu.m21.rule.RelationCalculate;
import nc.vo.pu.pub.util.ApproveFlowUtil;

import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVOUtil;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.workflownote.WorkflownoteVO;
import nc.vo.pubapp.pflow.PfUserObject;
import nc.vo.so.m30.entity.SaleOrderHVO;
import nc.vo.uap.pf.PFBatchExceptionInfo;
import nc.vo.uap.pf.PfProcessBatchRetObject;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class OA_PoOrderOpt extends AbstracAdapter4Ext {

	@Override
	public JSONObject sys(Object billvo) throws BusinessException {
		// SaleOrderResource
		// nc.ui.so.m30.billui.editor.bodyevent.BodyBeforeEditHandler
		JSONObject jsonObject = (JSONObject) billvo;
		// 检索数据
		OrderVO order = null;
		try {
			JSONObject bill = jsonObject.getJSONObject("data");
			String action = ((Map<String, Object>) bill.getJSONObject("hand"))
					.get("action").toString(); // 0=新增， 1=删除
			if("0".equals(action)){
				order = getBillAggVO(bill, action.toString());
				processBill(order);
			}else  if("1".equals(action)){
				
					order = getBillAggVO(bill, action.toString());
					if(order!=null){
						del(order);
						}else{
							throw  new  BusinessException("未查询到采购订单");
					}
				
				return getRsultJsonSuccess("删除采购订单成功" );
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
			Logger.error(e.getMessage(), e);
			System.out.println("-------------------------"+e.getMessage()+e.getStackTrace()[0].toString());
			return getRsultJsonFailed("操作采购订单出错:" + e.getMessage());
		}
		JSONObject data = new JSONObject();
		data.put("code", order.getHVO().getVbillcode());
		data.put("pk", order.getHVO().getPk_order());
		// 将结果返回v
		return getRsultDataSuccess(data, "生成采购订单成功");
	}

	private OrderVO getBillAggVO(JSONObject bill, String action)
			throws BusinessException {

		if (action != null && "0".equals(action)) {
			OrderVO order = new OrderVO();
			OrderHeaderVO hvo = getBillVO(bill);
			List<OrderItemVO> list = getBillDetailVO(bill);
			order.setParent(hvo);
			order.setChildrenVO(list.toArray(new OrderItemVO[list.size()]));
			return order;
		} else {
			
			Map<String, String> headmap = (Map) bill.getJSONObject("hand");
//			String  pk=headmap.get("pk").toString();
			
//			JSONObject data = jsonObject.getJSONObject("data");

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
			VOQuery<OrderHeaderVO> query = new VOQuery<OrderHeaderVO>(
					OrderHeaderVO.class);
			OrderHeaderVO[] hvos = query.query(" and pk_org='" + pk_org
					+ "' and vbillcode='" + vbillcode + "'", null);
			if (hvos == null || hvos.length == 0) {
				throw new BusinessException("nc中不存在的采购订单,单据编号"+vbillcode);

			}
			
			BillQuery<OrderVO>billquery=new BillQuery(OrderVO.class);
			OrderVO[]  order=billquery.query(new String[]{hvos[0].getPk_order()});
			if(order==null||order.length==0){
				throw  new  BusinessException("未查询到采购订单");
			}
			
//			
//			VOQuery<OrderHeaderVO> query = new VOQuery<OrderHeaderVO>(
//					OrderHeaderVO.class);
//			OrderHeaderVO[] hvos = query.query(" and pk_org='" + pk_org
//					+ "' and vbillcode='" + vbillcode + "'", null);
//			if (hvos == null || hvos.length == 0) {
//				throw new BusinessException("nc中不存在的销售订单,单据编号"+vbillcode);
//
//			}
			
			return order[0];
		}

	}

	private OrderHeaderVO getBillVO(JSONObject bill) throws BusinessException {
		OrderHeaderVO hvo = new OrderHeaderVO();
		Map<String, String> headmap = (Map) bill.getJSONObject("hand");

		// if(ct_code)

		for (String itemkey : headmap.keySet()) {
			if (itemkey.equals("group_code")) {
				hvo.setPk_group(headmap.get(itemkey));
				continue;
			}

			if (itemkey.equals("org_code")) {
				hvo.setPk_org(headmap.get(itemkey));
				continue;
			}

			if (itemkey.equals("dept_code")) {
				hvo.setPk_dept(headmap.get(itemkey));
				continue;
			}

			if (headmap.get(itemkey) != null && !"".equals(itemkey)) {
				hvo.setAttributeValue(itemkey, headmap.get(itemkey));
			}

		}
		// hvo.setPk_group(InvocationInfoProxy.getInstance().getGroupId());
		hvo.setStatus(VOStatus.NEW);
		// hvo.setBillmaker(hvo.get);// 制单人

		if(hvo.getBreturn()==null){
//			不设置   有的环境会报空指针  。。。。。。。。。。。。。。。。
			hvo.setBreturn(UFBoolean.FALSE);
		}
		return hvo;

	}

	private List<OrderItemVO> getBillDetailVO(JSONObject bill)
			throws BusinessException {

		List<OrderItemVO> list = new ArrayList<OrderItemVO>();

		JSONArray childArray = bill.getJSONArray("bodys"); // 如果json格式的字符串里含有数组格式的属性，将其转换成JSONArray，以方便后面转换成对应的实体
		String pk_group = InvocationInfoProxy.getInstance().getGroupId();

		for (int i = 0; i < childArray.size(); i++) {
			OrderItemVO vo = new OrderItemVO();
			Map<String, String> childVomap = (Map) childArray.get(i);
			vo.setPk_group(InvocationInfoProxy.getInstance().getGroupId());

			for (String itemkey : childVomap.keySet()) {
				if (childVomap.get(itemkey) != null
						&& !"".equals(childVomap.get(itemkey))) {
					vo.setAttributeValue(itemkey, childVomap.get(itemkey));

				}
			}
			list.add(vo);
		}
		return list;

	}

	private Object processBill(OrderVO order) throws BusinessException {

		// 2. 校验数据的合法性:1.数据结构完整 2.根据组织+单据号校验是否重复.
		checkData(order);
		// 3.补全数据,并且调整单据状态
		fillData(order);
		RelationCalculate cal = new RelationCalculate();
		cal.calculate(order, "norigtaxmny");
		OrderVO bill2 = (OrderVO) insert(order);
		return bill2.getParentVO().getPrimaryKey();
	}

	private void fillData(OrderVO order) throws DAOException {

		//
		String[]

		formulas_H46 = {
				// 最新的库存组织
				"pk_group->getColValue(org_group,  pk_group ,code ,pk_group)",
				"pk_org->getColValue2(org_stockorg,  pk_stockorg ,code ,pk_org,islastversion ,\"Y\")",
				"cprocalbodyoid->pk_org",
				// 库存组织版本号
				"pk_org_v->getColValue2(org_stockorg,  pk_vid, pk_stockorg  , pk_org ,islastversion ,\"Y\")",
				"cprocalbodyvid->pk_org_v",

				// yewuyuan
				// 币种
				// "corigcurrencyid->(bd_currtype,pk_currtype,code,corigcurrencyid)",
				// 币种
				"corigcurrencyid->getColValue(bd_currtype,pk_currtype,code,corigcurrencyid)",
				"cemployeeid->getColValue(bd_psndoc,pk_psndoc,code,cemployeeid)",
				// 部门
				"pk_dept->getColValue2(org_dept,pk_dept,code,pk_dept,pk_org,pk_org)",
				"pk_dept_v->getColValue2(org_dept_v,pk_vid,pk_dept,pk_dept,islastversion,\"Y\")",
				// 供应商
				"pk_supplier->getColValue(bd_supplier,pk_supplier,code,pk_supplier)",

				"creator->getColValue(sm_user,cuserid,user_code,creator)",
				// 开票供应商
				"pk_invcsupllier->getColValue(bd_supplier,pk_supplier,code,pk_invcsupllier)",

				// 结算方式
				"pk_balatype->getColValue(bd_balatype,pk_balatype,code,pk_balatype)",

				// 项目
				"pk_project->getColValue(bd_project,pk_project,project_code,pk_project)",

				// 付款协议
				"pk_payterm->getColValue(bd_payment,pk_payment,code,pk_payterm)",
				// / "vtrantypecode->\"46-01\"",
				// 业务类型 TODO
				"ctrantypeid->getColValue2(bd_billtype,pk_billtypeid, pk_billtypecode,ctrantypeid,pk_group,pk_group)"

		};

		SuperVOUtil.execFormulaWithVOs(new OrderHeaderVO[] { order.getHVO() },
				formulas_H46, null);
		String pk_payterm = order.getHVO().getPk_payterm();
		if (null != pk_payterm) {
			String sql = "select   * from bd_paymentch where pk_payment='"
					+ pk_payterm + "' and dr=0";
			@SuppressWarnings("unchecked")
			List<OrderPaymentVO> list = (List<OrderPaymentVO>) getDao()
					.executeQuery(sql,
							new BeanListProcessor(OrderPaymentVO.class));
			order.setChildren(OrderPaymentVO.class,
					list.toArray(new OrderPaymentVO[list.size()]));
			for(int i=0;i<	order.getChildren(OrderPaymentVO.class).length;i++){
				order.getChildren(OrderPaymentVO.class)[i].setAttributeValue("pk_payment", null);
			}
		}
		
		
		// 填充表体付款协议
		String[] formulas_B46 = {

				// 物料
				"pk_material->getColValue(bd_material_v,pk_material,code,pk_material)",
				"pk_srcmaterial->pk_material",
				// 部门
				"pk_dept->getColValue2(org_dept,pk_dept,code,pk_dept,pk_org,pk_org)",
				"pk_dept_v->getColValue2(org_dept_v,pk_vid,pk_dept,pk_dept,islastversion,\"Y\")",
				// 本位币
				"ccurrencyid->getColValue(bd_currtype,pk_currtype,code,ccurrencyid)",
				// 生产厂商
				"cproductorid->getColValue(bd_defdoc, pk_defdoc,code,cproductorid)",
				// 单位
				"cunitid->getColValue(bd_measdoc, pk_measdoc,code,cunitid)",
				"castunitid->getColValue(bd_measdoc, pk_measdoc,code,castunitid)",

				"cqtunitid->getColValue(bd_measdoc, pk_measdoc,pk_measdoc,castunitid)",

				"nqtunitnum->nastnum",
				// "vchangerate->nnum/nassistnum",
				// "nexchangerate->nnum/nassistnum",
				"pk_org->getColValue2(org_purchaseorg,pk_purchaseorg,code ,pk_org,islastversion ,\"Y\")",
				 "pk_org_v->getColValue2(org_purchaseorg,  pk_vid,  pk_purchaseorg   , pk_org ,islastversion ,\"Y\")",
				"pk_psfinanceorg->pk_org",
				"pk_psfinanceorg_v->pk_org_v",
				// 需求库存组织
				"pk_reqstoorg->pk_org",
				"pk_reqstoorg_v->pk_org_v",
				// 收货库存组织
				"pk_arrvstoorg->pk_org",
				"pk_arrvstoorg_v->pk_org_v",
//				"pk_flowstockorg->pk_org",
//				"pk_flowstockorg_v->pk_org",
				"pk_flowstockorg->pk_org",
				"pk_flowstockorg_v->pk_org_v",
				"pk_reqcorp->pk_org",
				  
				
				"pk_apfinanceorg->pk_org",
				"pk_apfinanceorg_v ->pk_org_v",
				"pk_supplier->getColValue(bd_supplier,pk_supplier,code,pk_supplier)",
				"csendcountryid->getColValue(bd_supplier,pk_country,pk_supplier,pk_supplier)",
				// 税码
//				"ctaxcodeid->getColValue(bd_material,pk_mattaxes,pk_material,pk_material)",
				// 项目
				"pk_project->getColValue(bd_project,pk_project,project_code,pk_project)",

		// "pk_arrvstoorg->getColValue(org_stockorg,pk_stockorg,code,pk_arrvstoorg)",

		};

		SuperVOUtil.execFormulaWithVOs(order.getBVO(), formulas_B46, null);

		for (int i = 0; i < order.getBVO().length; i++) {
			order.getBVO()[i].setPk_group(order.getHVO()
					.getAttributeValue("pk_group").toString());
			// order.getBVO()[i].setpk_t(order.getHVO().getAttributeValue("pk_group").toString());
			order.getBVO()[i].setPk_group(order.getHVO()
					.getAttributeValue("pk_group").toString());
			order.getBVO()[i].setVqtunitrate(order.getBVO()[i].getNnum()
					.toString()
					+ "/"
					+ (order.getBVO()[i].getNastnum().toString()));
			// order.getBVO()[i].setVecbillcode(vecbillcode);(
			// order.getBVO()[i].getNnum().toString()+"/"+(
			// order.getBVO()[i].getNastnum().toString()));
			// order.getBVO()[i].setcq
			order.getBVO()[i].setVchangerate(order.getBVO()[i].getNnum()
					.toString()
					+ "/"
					+ (order.getBVO()[i].getNastnum().toString()));
			order.getBVO()[i].setNorigtaxprice(order.getBVO()[i]
					.getNqtorigtaxprice());
			order.getBVO()[i]
					.setNorigprice(order.getBVO()[i].getNqtorigprice());
			order.getBVO()[i].setCrececountryid("0001Z010000000079UJJ");
			order.getBVO()[i].setCtaxcountryid("0001Z010000000079UJJ");
			order.getBVO()[i].setCsendcountryid("0001Z010000000079UJJ");
			order.getBVO()[i].setNexchangerate(new UFDouble(1.0));

			order.getBVO()[i]
					.setNorigprice(order.getBVO()[i].getNqtorigprice());
			order.getBVO()[i].setNorignetprice(order.getBVO()[i]
					.getNqtorigprice());
			order.getBVO()[i].setNnetprice(order.getBVO()[i].getNqtorigprice());
			order.getBVO()[i].setNprice(order.getBVO()[i].getNqtorigprice());
			order.getBVO()[i].setNqtorigprice(order.getBVO()[i]
					.getNqtorigprice());
			order.getBVO()[i].setNqtorignetprice(order.getBVO()[i]
					.getNqtorigprice());
			order.getBVO()[i].setNqtprice(order.getBVO()[i].getNqtorigprice());
			order.getBVO()[i].setNqtnetprice(order.getBVO()[i]
					.getNqtorigprice());
			order.getBVO()[i].setNqttaxnetprice(order.getBVO()[i]
					.getNqtorigprice());
			order.getBVO()[i].setNqttaxprice(order.getBVO()[i]
					.getNqtorigprice());
			// order.getBVO()[i].setCtaxcodeid(iu.getCellStr(rowj.getCell(36)));
			order.getBVO()[i].setNorigmny(order.getBVO()[i].getNorigmny());
			order.getBVO()[i].setNmny(order.getBVO()[i].getNorigmny());
			order.getBVO()[i].setNcaltaxmny(order.getBVO()[i].getNorigtaxmny());
			order.getBVO()[i].setNtaxnetprice(order.getBVO()[i]
					.getNqtorigtaxprice());
			order.getBVO()[i].setNtaxprice(order.getBVO()[i]
					.getNqtorigtaxprice());

			order.getBVO()[i].setNorigtaxnetprice(order.getBVO()[i]
					.getNqtorigtaxprice());
			order.getBVO()[i].setNqtorigtaxprice(order.getBVO()[i]
					.getNqtorigtaxprice());
			order.getBVO()[i].setNqtorigtaxnetprc(order.getBVO()[i]
					.getNqtorigtaxprice());// nqtorigtaxnetprc
			order.getBVO()[i].setNorigtaxprice(order.getBVO()[i]
					.getNqtorigtaxprice());
			order.getBVO()[i].setNorigmny(order.getBVO()[i].getNorigmny());
			// order.getBVO()[i].setNorigprice(new
			// UFDouble(iu.getCellStr(rowj.getCell(11))));//主无税单价excel中无此数据需添加
			order.getBVO()[i]
					.setNcalcostmny(order.getBVO()[i].getNorigtaxmny());
			order.getBVO()[i].setNtaxmny(order.getBVO()[i].getNorigtaxmny());
			order.getBVO()[i].setNglobaltaxmny(order.getBVO()[i]
					.getNorigtaxmny());
			order.getBVO()[i].setNgrouptaxmny(order.getBVO()[i]
					.getNorigtaxmny());

		}

	}

	// public
	// 查询采购订单
	public OrderVO[] queryaggvo(String pk) {
		BillQuery<OrderVO> billquery = new BillQuery<OrderVO>(OrderVO.class);
		OrderVO[] aggvo = billquery.query(new String[] { pk });
		return aggvo;
	}
	
	protected OrderVO del(OrderVO billvo) throws BusinessException {
		
		

//		IPFBusiAction service1 = (IPFBusiAction) NCLocator.getInstance()
//				.lookup(IPFBusiAction.class);
//		OrderVO[] returnVO1 = (OrderVO[]) service1.processBatch("UNAPPROVE",
//				"21", new OrderVO[] { billvo }, null, null, null);
//		
//		
		
		HashMap map1 = new HashMap();
//		map1.put("nc.bs.scmpub.pf.ORIGIN_VO_PARAMETER", aggvoss);
		map1.put("notechecked", "notechecked");
		
		 IplatFormEntry iIplatFormEntry = (IplatFormEntry)NCLocator.getInstance().lookup(IplatFormEntry.class.getName());
		 /*      */ 
		 /* 1182 */  Object   retObj = iIplatFormEntry.processAction("UNAPPROVE"+billvo.getHVO().getCreator(), billvo.getHVO().getVtrantypecode(), null, billvo, null, map1);
		
		
//		N_21_
		OrderVO[] aggvoss = this.queryaggvo(billvo.getParent()
				.getPrimaryKey());

		IOrderMaintain service =(IOrderMaintain) NCLocator.getInstance().lookup(
				IOrderMaintain.class);
		service.delete(aggvoss, null);

	
		return null;
	}
	

	// 收款结算单表体

	// public
	protected OrderVO insert(OrderVO billvo) throws BusinessException {

		IPFBusiAction service = NCLocator.getInstance().lookup(
				IPFBusiAction.class);

		InvocationInfoProxy.getInstance().setGroupId(
				billvo.getHVO().getAttributeValue("pk_group").toString());
		InvocationInfoProxy.getInstance().setUserId(
				billvo.getHVO().getCreator());
		
		
//		
//		String actioncode = ApproveFlowUtil.getCommitActionCode(billvo.getHVO()
//				.getPk_org(), billvo.getHVO().getCtrantypeid());

		// OrderVO [] OrderVO = (OrderVO []) service.processBatch("WRITE",
		// "46",new OrderVO []{billvo}, null, null, null);

		IPFBusiAction service1 = (IPFBusiAction) NCLocator.getInstance()
				.lookup(IPFBusiAction.class);
		OrderVO[] returnVO1 = (OrderVO[]) service1.processBatch("SAVEBASE",
				"21", new OrderVO[] { billvo }, null, null, null);
		
//		
		OrderVO[] aggvoss = this.queryaggvo(returnVO1[0].getParent()
				.getPrimaryKey());
		
		HashMap map1 = new HashMap();
//		map1.put("nc.bs.scmpub.pf.ORIGIN_VO_PARAMETER", aggvoss);
		map1.put("notechecked", "notechecked");
		
		 IplatFormEntry iIplatFormEntry = (IplatFormEntry)NCLocator.getInstance().lookup(IplatFormEntry.class.getName());
		 /*      */ 
		 /* 1182 */  Object   retObj = iIplatFormEntry.processAction("APPROVE"+returnVO1[0].getHVO().getCreator(), returnVO1[0].getHVO().getVtrantypecode(), null, billvo, null, map1);
		
//		aggvoss[0].getHVO().setStatus(VOStatus.UPDATED);
//		 service.processBatch("APPROVE","21",aggvoss, null, null, null);
//		
		
		
//		OrderVO[] aggvoss= (OrderVO[]) obj;
//		@SuppressWarnings("rawtypes")
//		HashMap map1 = new HashMap();
//		map1.put("nc.bs.scmpub.pf.ORIGIN_VO_PARAMETER", aggvoss);
//		PfUserObject pfuserobject = new nc.vo.pubapp.pflow.PfUserObject();
//		 PFBatchExceptionInfo batchExceptionInfo = new PFBatchExceptionInfo();
//		   ((IPFBusiAction)NCLocator.getInstance().lookup(IPFBusiAction.class)).processAction("APPROVE", "21", null, aggvoss[0], new PfUserObject[] { pfuserobject }, map1);
////		    ((IOrderApprove)NCLocator.getInstance().lookup(IOrderApprove.class)).approve( aggvoss, null);
		
//		OrderVO[] aggvoss = this.queryaggvo("1001ZZ100000000BQ8ND");
//		aggvoss[0].getHVO().setStatus(VOStatus.UPDATED);
//		 service.processBatch("APPROVE","21",aggvoss, null, null, null);
		 
//		
//		ISysDispatcher service2 = NCLocator.getInstance().lookup(
//				ISysDispatcher.class);
//		service2.dispatch(aggvoss, "approvepoorder", null);
//		
//		
//		OrderVO[] aggvoss= (OrderVO[]) obj;
//		@SuppressWarnings("rawtypes")
//		HashMap map1 = new HashMap();
//		map1.put("nc.bs.scmpub.pf.ORIGIN_VO_PARAMETER", aggvoss);
//		map1.put("notechecked", "notechecked");
//		PfUserObject pfuserobject = new nc.vo.pubapp.pflow.PfUserObject();
//		 PFBatchExceptionInfo batchExceptionInfo = new PFBatchExceptionInfo();
//		   ((IPFBusiAction)NCLocator.getInstance().lookup(IPFBusiAction.class)).processAction("APPROVE", "21", null, aggvoss[0], new PfUserObject[] { pfuserobject }, map1);
//			
//		 
//			BaseDAO dao = new BaseDAO();
//			
//			String sql = " update po_order set  forderstatus='3', approver='"+aggvoss[0].getHVO().getCreator()+"', taudittime = '"+aggvoss[0].getHVO().getTs()+"' where   pk_order ='"+billvo.getHVO().getPk_order()+"'";
//			dao.executeUpdate(sql);
		 
//		InvocationInfoProxy.getInstance().setUserId(
//				billvo.getHVO().getCreator());
//		
//		HashMap map1 = new HashMap();
//		IplatFormEntry ip=(IplatFormEntry) NCLocator.getInstance()
//				.lookup(IplatFormEntry.class);
//		ip.processBatch("APPROVE", "21", new WorkflownoteVO(), aggvoss, null, map1);
//		
		
		
//		
//		@SuppressWarnings("rawtypes")
//		HashMap map1 = new HashMap();
//		map1.put("nc.bs.scmpub.pf.ORIGIN_VO_PARAMETER", aggvoss);
//		map1.put("notechecked", "notechecked");
//		
//		
//		PfUserObject pfuserobject = new nc.vo.pubapp.pflow.PfUserObject();
//		 PFBatchExceptionInfo batchExceptionInfo = new PFBatchExceptionInfo();
//	      Object[] retObjsAfterAction = ((IPFBusiAction)NCLocator.getInstance().lookup(IPFBusiAction.class)).processBatch("APPROVE", "21", aggvoss, new PfUserObject[] { pfuserobject }, null, map1, batchExceptionInfo);
		 /*     */ 
//		 /*     */ 
		
		
//		IplatFormEntry iplat = NCLocator.getInstance().lookup(IplatFormEntry.class);
//		@SuppressWarnings("rawtypes")
//		HashMap map1 = new HashMap();
//		PfUserObject pfuserobject = new nc.vo.pubapp.pflow.PfUserObject();
//		PfProcessBatchRetObject o=(PfProcessBatchRetObject) iplat.processBatch("APPROVE","21",null, aggvoss,new PfUserObject[]{ pfuserobject }, map1);
//		IplatFormEntry iplat = NCLocator.getInstance().lookup(
//				IplatFormEntry.class);
//		@SuppressWarnings("rawtypes")
//		HashMap map1 = new HashMap();
//		map1.put("nc.bs.scmpub.pf.ORIGIN_VO_PARAMETER", aggvoss);
//		service.processAction("APPROVE","21", null, aggvoss[0],null, map1);
//		PfUserObject pfuserobject = new nc.vo.pubapp.pflow.PfUserObject();
//		PfProcessBatchRetObject o = (PfProcessBatchRetObject) iplat
//				.processBatch("APPROVE", "21", null, aggvoss,
//						new PfUserObject[] { pfuserobject }, map1);
//		
//		
//		PfProcessBatchRetObject o = (PfProcessBatchRetObject) iplat
//				.processBatch("APPROVE", "21", null, aggvoss,
//						null, null);
//		OrderVO[] aggvoss2 = this.queryaggvo(returnVO1[0].getParent()
//				.getPrimaryKey());
		
//		 Object retValue = ((IOrderApprove)NCLocator.getInstance().lookup(IOrderApprove.class)).approve(aggvoss, null);
//		 OrderVO[] aggvoss2 = this.queryaggvo(returnVO1[0].getParent().getPrimaryKey());
		
		// 保存
		// OrderVO [] returnVO1 = (OrderVO []) service.processBatch("SAVE",
		// "F2",new OrderVO []{billvo}, null, null, null);
		return aggvoss[0];
	}

	private void checkData(OrderVO resvo) throws BusinessException {
		if (resvo == null || resvo.getParentVO() == null)
			throw new BusinessException("未获取的转换后的数据");
		if (resvo.getChildrenVO() == null || resvo.getChildrenVO().length == 0) {
			throw new BusinessException("表体不允许为空");
		}

	}

}
