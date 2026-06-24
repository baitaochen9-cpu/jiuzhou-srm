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
import nc.itf.scmpub.reference.uap.pf.PfServiceScmUtil;
import nc.itf.uap.pf.IPFBusiAction;
import nc.itf.uap.pf.IplatFormEntry;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapProcessor;
import nc.bs.jzyy.sys.AbstracAdapter4Ext;

import nc.vo.arap.gathering.AggGatheringBillVO;
import nc.vo.arap.pay.PayBillItemVO;
import nc.vo.ic.m45.entity.PurchaseInVO;
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

public class OA_InvoiceVOSave extends AbstracAdapter4Ext {

	@Override
	public JSONObject sys(Object billvo) throws BusinessException {
		// SaleOrderResource
		// nc.ui.so.m30.billui.editor.bodyevent.BodyBeforeEditHandler
		JSONObject jsonObject = (JSONObject) billvo;
		// 检索数据
		InvoiceVO order = null;
		try {
			JSONObject bill = jsonObject.getJSONObject("data");
			order = getBillAggVO(bill);
			processBill(order);
		} catch (Exception e) {
			e.printStackTrace();
			Logger.error(e.getMessage(), e);
			return getRsultJsonFailed("生成发票单出错:" + e.getMessage());
		}
		JSONObject data = new JSONObject();
		data.put("code", order.getParent().getAttributeValue("vbillcode"));
		data.put("pk", order.getParentVO().getPk_invoice());
		// 将结果返回
		return getRsultDataSuccess(data, "生成发票成功");
	}

	private InvoiceVO getBillAggVO(JSONObject bill) throws BusinessException {
		InvoiceVO  inv= new InvoiceVO();
		
		
		InvoiceHeaderVO hvo = new InvoiceHeaderVO();
		List<InvoiceItemVO>list=new  ArrayList<InvoiceItemVO>();
//		InvoiceItemVO[] bvo = new InvoiceItemVO[];
		
		Map<String, String> hmap =(Map) bill.getJSONObject("hand"); 
		JSONArray childArray = bill.getJSONArray("bodys"); // 如果json格式的字符串里含有数组格式的属性，将其转换成JSONArray，以方便后面转换成对应的实体
		for (int i = 0; i < childArray.size(); i++) {
			Map<String, String> childVomap = (Map) childArray.get(i);
			
			String cgeneralhid=childVomap.get("cgeneralhid");
			String cgeneralbid=childVomap.get("cgeneralbid");
			PurchaseInVO[]  invo=queryaggvo(cgeneralhid);
			InvoiceVO[] changevo=(InvoiceVO[])PfServiceScmUtil.exeVOChangeByBizFlow("45", "25", invo);
			 String  vbillcode=hmap.get("vbillcode");
			
			 changevo[0].getParentVO().setVbillcode(vbillcode);
			 UFDate  dbilldate=new UFDate(hmap.get("dbilldate"));
			 changevo[0].getParentVO().setDbilldate(dbilldate);
			 
			 UFDate  darrivedate=new UFDate(hmap.get("darrivedate"));
			 changevo[0].getParentVO().setDarrivedate(darrivedate);
			 
			hvo=changevo[0].getParentVO();
			for(int  j=0;j<changevo[0].getChildrenVO().length;j++){
				String  csourcebid =changevo[0].getChildrenVO()[j].getCsourcebid();
				if(csourcebid.equals(cgeneralbid)){
					
					InvoiceItemVO  bvo = (InvoiceItemVO) changevo[0].getChildrenVO()[j].clone();
					
					UFDouble  norigtaxmny=new UFDouble(childVomap.get("norigtaxmny"));
					
					
//					加税合计
					bvo.setNorigtaxmny(norigtaxmny);
					bvo.setNtaxmny(norigtaxmny);
					bvo.setNcalcostmny(norigtaxmny);
					
					UFDouble  nmny=new UFDouble(childVomap.get("nmny"));
					bvo.setNmny(nmny);
					
					
					bvo.setNcalcostmny(bvo.getNmny());
					bvo.setNcaltaxmny(bvo.getNmny());
					bvo.setNorigmny(nmny);
					
					
					
					
					UFDouble  nastnum =new UFDouble(childVomap.get("nastnum"));
					UFDouble  nnum =new UFDouble(childVomap.get("nnum"));
					bvo.setNastnum(nastnum);
					bvo.setNnum(nnum);
					list.add(bvo);
					
					
				}
				
				
			}
			
		}
			
		for(int i=0;i<list.size();i++){
			list.get(i).setCrowno(String.valueOf((i+1)*10));
		}
			
		inv.setParentVO(hvo);
		inv.setChildrenVO(list.toArray(new InvoiceItemVO[list.size()]));
		
		
		return inv;
	}
	public PurchaseInVO [] queryaggvo(String pk) {
		BillQuery<PurchaseInVO> billquery = new BillQuery<PurchaseInVO>(PurchaseInVO.class);
		PurchaseInVO[] aggvo = billquery.query(new String[] { pk });
		return aggvo;
	}

	
	private Object processBill(InvoiceVO order) throws BusinessException {

		// 2. 校验数据的合法性:1.数据结构完整 2.根据组织+单据号校验是否重复.
		checkData(order);
//		// 3.补全数据,并且调整单据状态
//		fillData(order);

	

		InvoiceVO bill2 = (InvoiceVO) insert(order);

		return bill2.getParentVO().getPrimaryKey();
	}

	

	/**
	 * 付款单接口表体执行公式
	 */

	protected InvoiceVO insert(InvoiceVO billvo) throws BusinessException {

		IPFBusiAction service = NCLocator.getInstance().lookup(
				IPFBusiAction.class);
		// 保存

		InvocationInfoProxy.getInstance().setGroupId(
				billvo.getParentVO().getAttributeValue("pk_group").toString());
		InvocationInfoProxy.getInstance().setUserId(
				billvo.getParentVO().getCreator());
		billvo.getParentVO().setStatus(VOStatus.NEW);
		InvoiceVO[] pbvo = (InvoiceVO[])service.processBatch("SAVEBASE", "25",new InvoiceVO[]{billvo}, null, null, null);
		
		InvoiceVO[] appvo=queryaggvo2(pbvo[0].getParentVO().getPk_invoice()); 
		
//		service.processBatch("APPROVE", "25",appvo, null, null, null);
		
		HashMap map1 = new HashMap();
//		map1.put("nc.bs.scmpub.pf.ORIGIN_VO_PARAMETER", aggvoss);
		map1.put("notechecked", "notechecked");
		
		 IplatFormEntry iIplatFormEntry = (IplatFormEntry)NCLocator.getInstance().lookup(IplatFormEntry.class.getName());
		 /*      */ 
		 /* 1182 */  Object   retObj = iIplatFormEntry.processAction("APPROVE"+appvo[0].getParentVO().getCreator(), appvo[0].getParentVO().getVtrantypecode(), null, appvo[0], null, map1);
		
		
		return pbvo[0];
	}
	
	
	public InvoiceVO [] queryaggvo2(String pk) {
		BillQuery<InvoiceVO> billquery = new BillQuery<InvoiceVO>(InvoiceVO.class);
		InvoiceVO[] aggvo = billquery.query(new String[] { pk });
		return aggvo;
	}

	private void checkData(InvoiceVO resvo) throws BusinessException {
		if (resvo == null || resvo.getParentVO() == null)
			throw new BusinessException("未获取的转换后的数据");
		if (resvo.getChildrenVO() == null || resvo.getChildrenVO().length == 0) {
			throw new BusinessException("表体不允许为空");
		}

	}

}
