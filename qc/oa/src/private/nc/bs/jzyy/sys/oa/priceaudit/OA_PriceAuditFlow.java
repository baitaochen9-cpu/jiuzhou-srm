package nc.bs.jzyy.sys.oa.priceaudit;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSONObject;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;

import nc.bs.jzyy.sys.AbstracAdapter4Ext;
import nc.bs.logging.Logger;


import nc.impl.pubapp.pattern.data.bill.BillQuery;
import nc.impl.pubapp.pattern.data.vo.VOQuery;

import nc.itf.uap.pf.IPFBusiAction;

import nc.vo.ct.purdaily.entity.AggCtPuVO;
import nc.vo.pp.m28.entity.PriceAuditHeaderVO;

import nc.vo.pp.m28.entity.PriceAuditVO;

import nc.vo.pub.BusinessException;



public class OA_PriceAuditFlow extends AbstracAdapter4Ext {

	@Override
	public JSONObject sys(Object bill) throws BusinessException {
		JSONObject jsonObject = (JSONObject) bill;
		Map<String, Object> result = new HashMap<String, Object>();
		try {
//			PriceAuditRelationItemForCal  ChangeVOAdjustor20To28  TransferDoAfterServiceImpl PriceAuditMaintainServiceImpl
			
			
			

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
			
			PriceAuditVO[] orders = queyrCtVo(pk_org, vbillcode);
			String action=jsonObject.getString("action");//0  fanshen  1 审核
			if(action.equals("0")){
				PriceAuditVO[]  pricebills = approveBill(orders);
				
			}else{
				PriceAuditVO[]  pricebills = unapproveBill(orders);
			}
			 
			
			return getRsultJsonSuccess("生成价格审批单成功",orders[0].getParentVO().getVbillcode());
//			return this.getRsultData1Success(respData, "生成价格审批单成功");
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
			return getRsultJsonFailed("生成价格审批单出错:" + e.getMessage());
		}
	}
	
	
	public PriceAuditVO[] queyrCtVo(String pk_org,
			String vbillcode) throws BusinessException {

		VOQuery<PriceAuditHeaderVO> query = new VOQuery<PriceAuditHeaderVO>(
				PriceAuditHeaderVO.class);
		PriceAuditHeaderVO[] hvos = query.query(" and pk_org='" + pk_org
				+ "' and vbillcode='" + vbillcode + "'", null);
		if (hvos == null || hvos.length == 0) {
			throw new BusinessException("nc中不存在的合同,单据编号" + vbillcode);

		}

		String  pk=hvos[0].getPk_priceaudit();

		PriceAuditVO[] bills= queryaggvo( pk);
		return bills;
	}

	public PriceAuditVO[] queryaggvo(String pk) {
		BillQuery<PriceAuditVO> billquery = new BillQuery<PriceAuditVO>(PriceAuditVO.class);
		PriceAuditVO[] aggvo = billquery.query(new String[] { pk });
		return aggvo;
	}
	


	protected PriceAuditVO[] approveBill(PriceAuditVO[] bills) throws BusinessException {
		String approver = InvocationInfoProxy.getInstance().getUserId();
		if (!StringUtils.isEmpty(approver)) {
			InvocationInfoProxy.getInstance().setUserId(bills[0].getParentVO().getCreator());
		}

//		String[] hids = VOEntityUtil.getPksFromAggVO(bills);
//		SCMBillQuery<PriceAuditVO> queryTool = new SCMBillQuery<PriceAuditVO>(PriceAuditVO.class);
//		PriceAuditVO[] orderVOs = queryTool.queryVOByIDs(hids);
		IPFBusiAction baction = (IPFBusiAction) NCLocator.getInstance().lookup(IPFBusiAction.class);
		PriceAuditVO[] object = (PriceAuditVO[]) baction.processBatch("APPROVE", "28", bills, null, null, null);
		return object;
	}
	
	
	protected PriceAuditVO[] unapproveBill(PriceAuditVO[] bills) throws BusinessException {
		String approver = InvocationInfoProxy.getInstance().getUserId();
		if (!StringUtils.isEmpty(approver)) {
			InvocationInfoProxy.getInstance().setUserId(bills[0].getParentVO().getCreator());
		}

//		String[] hids = VOEntityUtil.getPksFromAggVO(bills);
//		SCMBillQuery<PriceAuditVO> queryTool = new SCMBillQuery<PriceAuditVO>(PriceAuditVO.class);
//		PriceAuditVO[] orderVOs = queryTool.queryVOByIDs(hids);
		IPFBusiAction baction = (IPFBusiAction) NCLocator.getInstance().lookup(IPFBusiAction.class);
		PriceAuditVO[] object = (PriceAuditVO[]) baction.processBatch("UNSAVEBILL", "28", bills, null, null, null);
		return object;
	}



}
