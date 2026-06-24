package nc.bs.jzyy.sys.oa.pu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.ic.pub.env.ICBSContext;
import nc.bs.jzyy.sys.AbstracAdapter4Ext;
import nc.bs.logging.Logger;
import nc.bs.pub.filesystem.IFileSystemService;
import nc.bs.scmpub.query.SCMBillQuery;
import nc.impl.pubapp.pattern.data.bill.BillQuery;
import nc.impl.pubapp.pattern.data.vo.VOQuery;

import nc.itf.uap.pf.IPFBusiAction;
import nc.itf.uap.pf.IplatFormEntry;


import nc.vo.ic.org.OrgVO;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pu.m21.entity.*;

import nc.vo.pub.BusinessException;

import com.alibaba.fastjson.JSONObject;

public class OA_PoOrderApprove extends AbstracAdapter4Ext {
//	public  String pk_add="";
	@Override
	public JSONObject sys(Object billvo) throws BusinessException {
		// SaleOrderResource
		// nc.ui.so.m30.billui.editor.bodyevent.BodyBeforeEditHandler
		JSONObject jsonObject = (JSONObject) billvo;
		// 检索数据
		OrderVO order = null;
		try {
			
			String action=jsonObject.getString("action");//0  fanshen  1 审核
//			String  billno=jsonObject.getString("billno");
			String vbillcode = jsonObject.getString("vbillcode");
			if (getString_TrimZeroLenAsNull(vbillcode) == null) {
				throw new BusinessException("单据号不能为空");
			}

			String pk_org = jsonObject.getString("org_code");
			if (getString_TrimZeroLenAsNull(pk_org) == null) {
				throw new BusinessException("组织不能为空");
			}

			pk_org = getStockorg(pk_org);

			if (getString_TrimZeroLenAsNull(pk_org) == null) {
				throw new BusinessException("组织在nc中不存在");
			}

			ICBSContext context = new ICBSContext();
			OrgVO orgvo = context.getOrgInfo().getOrgVO(pk_org);
			if (StringUtil.isEmpty(InvocationInfoProxy.getInstance()
					.getGroupId())) {
				InvocationInfoProxy.getInstance().setGroupId(
						orgvo.getPk_group());
			}

			OrderVO[] orders = queyrCtVo(pk_org, vbillcode);
			order=orders[0];
			processBill(orders,action);
			
		} catch (Exception e) {
			e.printStackTrace();
			Logger.error(e.getMessage(), e);
			return getRsultJsonFailed("审核采购订单出错:" + e.getMessage());
		}
		return getRsultJsonSuccess("审核采购订单成功",order.getHVO().getVbillcode());
	}
	
	/**
	 * 查询合同
	 * 
	 * @param pk_order_b
	 * @return
	 * @throws BusinessException
	 */
	private OrderVO[] queyrCtVo(String pk_org,
			String vbillcode) throws BusinessException {

		VOQuery<OrderHeaderVO> query = new VOQuery<OrderHeaderVO>(
				OrderHeaderVO.class);
		OrderHeaderVO[] hvos = query.query(" and pk_org='" + pk_org
				+ "' and vbillcode='" + vbillcode + "'", null);
		if (hvos == null || hvos.length == 0) {
			throw new BusinessException("nc中不存在的合同,单据编号" + vbillcode);

		}

		String  pk=hvos[0].getPk_order();

		 OrderVO[] bills= queryaggvo( pk);
		return bills;
	}
	
	public OrderVO[] queryaggvo(String pk) {
		BillQuery<OrderVO> billquery = new BillQuery<OrderVO>(OrderVO.class);
		OrderVO[] aggvo = billquery.query(new String[] { pk });
		return aggvo;
	}




	private Object processBill(OrderVO[]  order,String  action) throws BusinessException {
		
		if(action.equals("0")){
			OrderVO vo=approveBill(order);
			return  vo;
		}else{
			OrderVO vo=unapproveBill(order);
			return  vo;
		}
		 
		
	}


	
	protected OrderVO approveBill(OrderVO[] billvos)
			throws BusinessException {


//		IPFBusiAction baction = (IPFBusiAction) NCLocator.getInstance().lookup(
//				IPFBusiAction.class);
//		OrderVO[] object = (OrderVO[]) baction.processBatch("APPROVE", "Z2", billvos, null, null, null);
//		String sCurrentDate = new UFDate(System.currentTimeMillis()).toString();
//	
		HashMap map1 = new HashMap();
//		map1.put("nc.bs.scmpub.pf.ORIGIN_VO_PARAMETER", aggvoss);
		map1.put("notechecked", "notechecked");
		
		 IplatFormEntry iIplatFormEntry = (IplatFormEntry)NCLocator.getInstance().lookup(IplatFormEntry.class.getName());
		 
		   Object   retObj = iIplatFormEntry.processAction("APPROVE"+billvos[0].getHVO().getCreator(), billvos[0].getHVO().getVtrantypecode(), null, billvos[0], null, map1);
		
		return billvos[0];
		
	}

	
	protected OrderVO unapproveBill(OrderVO[] billvos)
			throws BusinessException {
		IPFBusiAction baction = (IPFBusiAction) NCLocator.getInstance().lookup(
				IPFBusiAction.class);
		InvocationInfoProxy.getInstance().setUserId(billvos[0].getHVO().getBillmaker());
		OrderVO[] object = (OrderVO[]) baction.processBatch("UNSAVEBILL", "21", billvos, null, null, null);
		

		return object[0];
		
	}

	


	
}
