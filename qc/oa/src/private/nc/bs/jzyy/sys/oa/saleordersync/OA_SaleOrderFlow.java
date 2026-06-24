package nc.bs.jzyy.sys.oa.saleordersync;

import java.util.HashMap;
import java.util.Map;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.jzyy.sys.AbstracAdapter4Ext;
import nc.bs.logging.Logger;
import nc.impl.pubapp.pattern.data.bill.BillQuery;
import nc.impl.pubapp.pattern.data.vo.VOQuery;
import nc.itf.uap.pf.IPFBusiAction;
import nc.vo.pp.m28.entity.PriceAuditVO;
import nc.vo.pub.BusinessException;
import nc.vo.so.m30.entity.SaleOrderHVO;
import nc.vo.so.m30.entity.SaleOrderVO;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSONObject;



public class OA_SaleOrderFlow extends AbstracAdapter4Ext {

	@Override
	public JSONObject sys(Object bill) throws BusinessException {
		JSONObject jsonObject = (JSONObject) bill;
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			String pk_org = jsonObject.getString("org_code");
			if (getString_TrimZeroLenAsNull(pk_org) == null) {
				throw new BusinessException("组织不能为空");
			}

			pk_org = getStockorg(pk_org);

			if (getString_TrimZeroLenAsNull(pk_org) == null) {
				throw new BusinessException("组织在nc中不存在");
			}

			String vbillcode = jsonObject.getString("vbillcode");
			if (getString_TrimZeroLenAsNull(vbillcode) == null) {
				throw new BusinessException("单据号不能为空");
			}
			
			SaleOrderVO[] orders = queyrCtVo(pk_org, vbillcode);
			String action=jsonObject.getString("action");//0  fanshen  1 审核
			SaleOrderVO[]  pricebills;
			if(action.equals("0")){
				pricebills = approveBill(orders);
				
			}else{
				pricebills = unapproveBill(orders);
			}
			 
			
			return getRsultJsonSuccess("生成销售订单成功",pricebills[0].getParentVO().getVbillcode());
//			return this.getRsultData1Success(respData, "生成价格审批单成功");
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
			return getRsultJsonFailed("生成出错:" + e.getMessage());
		}
	}
	
	
	public SaleOrderVO[] queyrCtVo(String pk_org,
			String vbillcode) throws BusinessException {

		VOQuery<SaleOrderHVO> query = new VOQuery<SaleOrderHVO>(
				SaleOrderHVO.class);
		SaleOrderHVO[] hvos = query.query(" and pk_org='" + pk_org
				+ "' and vbillcode='" + vbillcode + "'", null);
		if (hvos == null || hvos.length == 0) {
			throw new BusinessException("nc中不存在的单据,单据编号" + vbillcode);

		}

		String  pk=hvos[0].getCsaleorderid();

		SaleOrderVO[] bills= queryaggvo( pk);
		return bills;
	}

	public SaleOrderVO[] queryaggvo(String pk) {
		BillQuery<SaleOrderVO> billquery = new BillQuery<SaleOrderVO>(SaleOrderVO.class);
		SaleOrderVO[] aggvo = billquery.query(new String[] { pk });
		return aggvo;
	}
	


	protected SaleOrderVO[] approveBill(SaleOrderVO[] bills) throws BusinessException {
		String approver = InvocationInfoProxy.getInstance().getUserId();
		if (!StringUtils.isEmpty(approver)) {
			InvocationInfoProxy.getInstance().setUserId(bills[0].getParentVO().getCreator());
		}
		IPFBusiAction baction = (IPFBusiAction) NCLocator.getInstance().lookup(IPFBusiAction.class);
		SaleOrderVO[] object = (SaleOrderVO[]) baction.processBatch("APPROVE", "30", bills, null, null, null);
		return object;
	}
	
	
	protected SaleOrderVO[] unapproveBill(SaleOrderVO[] bills) throws BusinessException {
		String approver = InvocationInfoProxy.getInstance().getUserId();
		if (!StringUtils.isEmpty(approver)) {
			InvocationInfoProxy.getInstance().setUserId(bills[0].getParentVO().getCreator());
		}
		IPFBusiAction baction = (IPFBusiAction) NCLocator.getInstance().lookup(IPFBusiAction.class);
		SaleOrderVO[] object = (SaleOrderVO[]) baction.processBatch("UNSAVE", "30", bills, null, null, null);
		return object;
	}



}
