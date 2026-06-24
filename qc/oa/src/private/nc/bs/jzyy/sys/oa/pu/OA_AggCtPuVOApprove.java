package nc.bs.jzyy.sys.oa.pu;

import java.util.ArrayList;
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
import nc.itf.scmpub.reference.uap.bd.customer.CustomerPubService;
import nc.itf.scmpub.reference.uap.org.DeptPubService;
import nc.itf.scmpub.reference.uap.org.OrgUnitPubService;
import nc.itf.scmpub.reference.uap.pf.PfServiceScmUtil;
import nc.vo.cmp.bill.BillDetailVO;
import nc.vo.cmp.bill.BillVO;
import nc.itf.uap.pf.IPFBusiAction;

import nc.vo.ct.rule.RelationCalculate;
import nc.pubitf.so.m30.api.ISaleOrderQueryAPI;
import nc.uif.pub.exception.UifException;

import nc.vo.arap.gathering.AggGatheringBillVO;
import nc.vo.bc.pub.util.VOEntityUtil;
import nc.vo.bd.cust.saleinfo.CustsaleVO;
import nc.vo.cmp.bill.BillAggVO;
import nc.vo.ct.purdaily.entity.AggCtPuVO;
import nc.vo.ct.purdaily.entity.CtPaymentVO;
import nc.vo.ct.purdaily.entity.CtPuBVO;
import nc.vo.ct.purdaily.entity.CtPuVO;
import nc.vo.ic.material.define.InvBasVO;
import nc.vo.ic.org.OrgVO;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVOUtil;
import nc.vo.pub.VOStatus;
import nc.vo.pub.filesystem.NCFileVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.pf.PfUtilActionVO;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.pubapp.pattern.model.entity.bill.IBill;
import nc.vo.pubapp.pattern.pub.PubAppTool;

import nc.vo.so.m30.entity.SaleOrderHVO;
import nc.vo.so.m30.entity.SaleOrderVO;
import nc.vo.trade.checkrule.VOChecker;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class OA_AggCtPuVOApprove extends AbstracAdapter4Ext {
//	public  String pk_add="";
	@Override
	public JSONObject sys(Object billvo) throws BusinessException {
		// SaleOrderResource
		// nc.ui.so.m30.billui.editor.bodyevent.BodyBeforeEditHandler
		JSONObject jsonObject = (JSONObject) billvo;
		// 检索数据
		AggCtPuVO order = null;
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

			AggCtPuVO[] orders = queyrCtVo(pk_org, vbillcode);
			order=orders[0];
			processBill(orders,action);
			
		} catch (Exception e) {
			e.printStackTrace();
			Logger.error(e.getMessage(), e);
			return getRsultJsonFailed("生成采购合同出错:" + e.getMessage());
		}
		return getRsultJsonSuccess("生成采购合同成功",order.getParentVO().getVbillcode());
	}
	
	/**
	 * 查询合同
	 * 
	 * @param pk_order_b
	 * @return
	 * @throws BusinessException
	 */
	private AggCtPuVO[] queyrCtVo(String pk_org,
			String vbillcode) throws BusinessException {

		VOQuery<CtPuVO> query = new VOQuery<CtPuVO>(
				CtPuVO.class);
		CtPuVO[] hvos = query.query(" and pk_org='" + pk_org
				+ "' and vbillcode='" + vbillcode + "'", null);
		if (hvos == null || hvos.length == 0) {
			throw new BusinessException("nc中不存在的合同,单据编号" + vbillcode);

		}

		String  pk=hvos[0].getPk_ct_pu();

		 AggCtPuVO[] bills= queryaggvo( pk);
		return bills;
	}
	
	public AggCtPuVO[] queryaggvo(String pk) {
		BillQuery<AggCtPuVO> billquery = new BillQuery<AggCtPuVO>(AggCtPuVO.class);
		AggCtPuVO[] aggvo = billquery.query(new String[] { pk });
		return aggvo;
	}




	private Object processBill(AggCtPuVO[]  order,String  action) throws BusinessException {
		
		if(action.equals("0")){
			AggCtPuVO[] vo=approveBill(order);
			return  vo;
		}else{
			AggCtPuVO[] vo=unapproveBill(order);
			return  vo;
		}
		 
		
	}


	
	protected AggCtPuVO[] approveBill(AggCtPuVO[] billvos)
			throws BusinessException {


		IPFBusiAction baction = (IPFBusiAction) NCLocator.getInstance().lookup(
				IPFBusiAction.class);
		AggCtPuVO[] object = (AggCtPuVO[]) baction.processBatch("APPROVE", "Z2", billvos, null, null, null);
		String sCurrentDate = new UFDate(System.currentTimeMillis()).toString();
		
		String sql=" update ct_pu a set a.fstatusflag ='3',a.approver ='"+InvocationInfoProxy.getInstance().getUserId()+"' , a.taudittime ='"+(new UFDateTime(System.currentTimeMillis())).toString()+"' where pk_ct_pu='"+object[0].getParentVO().getPk_ct_pu()+"'";
		BaseDAO dao=new  BaseDAO();
		dao.executeUpdate(sql);
//		List localList = (List)new PfUtilActionVO().processAction("APPROVE", "Z2", sCurrentDate, null, vo, null);
		return object;
		
	}

	
	protected AggCtPuVO[] unapproveBill(AggCtPuVO[] billvos)
			throws BusinessException {
		IPFBusiAction baction = (IPFBusiAction) NCLocator.getInstance().lookup(
				IPFBusiAction.class);
		InvocationInfoProxy.getInstance().setUserId(billvos[0].getParentVO().getBillmaker());
		AggCtPuVO[] object = (AggCtPuVO[]) baction.processBatch("UNSAVEBILL", "Z2", billvos, null, null, null);
		

		return object;
		
	}


	
}
