package nc.bs.jzyy.sys.oa.out.ctsale;

import java.util.HashMap;
import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.jzyy.sys.AbstracAdapter4Ext;
import nc.bs.logging.Logger;
import nc.impl.pubapp.pattern.data.bill.BillQuery;
import nc.impl.pubapp.pattern.data.vo.VOQuery;
import nc.itf.uap.pf.IPFBusiAction;
import nc.vo.ct.purdaily.entity.CtPuVO;
import nc.vo.ct.saledaily.entity.AggCtSaleVO;
import nc.vo.ct.saledaily.entity.CtSaleVO;
import nc.vo.pp.m28.entity.PriceAuditVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.so.m30.entity.SaleOrderHVO;
import nc.vo.so.m30.entity.SaleOrderVO;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSONObject;



public class OA_CtSaleFlow extends AbstracAdapter4Ext {

	@Override
	public JSONObject sys(Object bill) throws BusinessException {
		JSONObject jsonObject = (JSONObject) bill;
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
			
			AggCtSaleVO[] orders = queyrCtVo(pk_org, vbillcode);
			String action=jsonObject.getString("action");//0  fanshen  1 审核
			AggCtSaleVO[]  pricebills;
			if(action.equals("0")){
				pricebills = approveBill(orders);
				
			}else{
				pricebills = unapproveBill(orders);
			}
			 
			
			return getRsultJsonSuccess("生成成功",pricebills[0].getParentVO().getVbillcode());
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
			return getRsultJsonFailed("出错:" + e.getMessage());
		}
	}
	
	
	public AggCtSaleVO[] queyrCtVo(String pk_org,
			String vbillcode) throws BusinessException {

		VOQuery<CtSaleVO> query = new VOQuery<CtSaleVO>(
				CtSaleVO.class);
		CtSaleVO[] hvos = query.query(" and pk_org='" + pk_org
				+ "' and vbillcode='" + vbillcode + "'", null);
		if (hvos == null || hvos.length == 0) {
			throw new BusinessException("nc中不存在的单据,单据编号" + vbillcode);

		}

		String  pk=hvos[0].getPk_ct_sale();

		AggCtSaleVO[] bills= queryaggvo( pk);
		return bills;
	}

	public AggCtSaleVO[] queryaggvo(String pk) {
		BillQuery<AggCtSaleVO> billquery = new BillQuery<AggCtSaleVO>(AggCtSaleVO.class);
		AggCtSaleVO[] aggvo = billquery.query(new String[] { pk });
		return aggvo;
	}
	


	protected AggCtSaleVO[] approveBill(AggCtSaleVO[] orders) throws BusinessException {
		String approver = InvocationInfoProxy.getInstance().getUserId();
		if (!StringUtils.isEmpty(approver)) {
			InvocationInfoProxy.getInstance().setUserId(orders[0].getParentVO().getCreator());
		}
		IPFBusiAction baction = (IPFBusiAction) NCLocator.getInstance().lookup(IPFBusiAction.class);
		AggCtSaleVO[] object = (AggCtSaleVO[]) baction.processBatch("APPROVE", "Z3", orders, null, null, null);
		String sql=" update ct_sale a set a.fstatusflag ='3',a.approver ='"+InvocationInfoProxy.getInstance().getUserId()+"' " +
				", a.taudittime ='"+(new UFDateTime(System.currentTimeMillis())).toString()+"' " +
						"where pk_ct_sale = '"+object[0].getParentVO().getPk_ct_sale()+"'";
		BaseDAO dao=new  BaseDAO();
		dao.executeUpdate(sql);
		return object;
	}
	
	
	protected AggCtSaleVO[] unapproveBill(AggCtSaleVO[] bills) throws BusinessException {
		String approver = InvocationInfoProxy.getInstance().getUserId();
		if (!StringUtils.isEmpty(approver)) {
			InvocationInfoProxy.getInstance().setUserId(bills[0].getParentVO().getCreator());
		}
		IPFBusiAction baction = (IPFBusiAction) NCLocator.getInstance().lookup(IPFBusiAction.class);
		AggCtSaleVO[] object = (AggCtSaleVO[]) baction.processBatch("UNSAVEBILL", "Z3", bills, null, null, null);
		return object;
	}



}
