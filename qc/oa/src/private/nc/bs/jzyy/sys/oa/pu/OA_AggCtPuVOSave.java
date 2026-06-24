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
import nc.uif.pub.exception.UifException;

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

import nc.vo.trade.checkrule.VOChecker;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class OA_AggCtPuVOSave extends AbstracAdapter4Ext {
//	public  String pk_add="";
	@Override
	public JSONObject sys(Object billvo) throws BusinessException {
		// SaleOrderResource
		// nc.ui.so.m30.billui.editor.bodyevent.BodyBeforeEditHandler
		JSONObject jsonObject = (JSONObject) billvo;
		// 检索数据
		AggCtPuVO order = null;
		try {
//			JSONObject bill = jsonObject.getJSONObject("data");
			JSONArray childArray = jsonObject.getJSONArray("data");
			for(int i=0;i<childArray.size();i++){
				JSONObject bill =childArray.getJSONObject(i);
				order = getBillAggVO(bill);
				processBill(order,bill);
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
			Logger.error(e.getMessage(), e);
			return getRsultJsonFailed("生成采购合同出错:" + e.getMessage());
		}
		return getRsultJsonSuccess("生成采购合同成功",order.getParentVO().getVbillcode());
	}

	private AggCtPuVO getBillAggVO(JSONObject bill)
			throws BusinessException {

		AggCtPuVO order = new AggCtPuVO();
		CtPuVO   hvo = getBillVO(bill);
		
		List<CtPuBVO> list = getBillDetailVO(bill);
		order.setParent(hvo);
		order.setCtPuBVO(list.toArray(new CtPuBVO[list.size()]));
		
		List<CtPaymentVO> list2 = getBillPaymentVO(bill);
		order.setCtPaymentVO(list2.toArray(new CtPaymentVO[list2.size()]));
		//.setChildrenVO(list.toArray(new CtPuBVO[list.size()]));
		return order;
	}

	private CtPuVO   getBillVO(JSONObject bill)
			throws BusinessException {
		CtPuVO   hvo = new  CtPuVO ();
		Map<String,String> headmap=(Map)bill.getJSONObject("headvo"); 
	    for(String itemkey:headmap.keySet()){
       		 if(headmap.get(itemkey)!=null&&!"".equals(itemkey)){
       			 hvo.setAttributeValue(itemkey,headmap.get(itemkey));
       	 }}
	    hvo.setPk_group(InvocationInfoProxy.getInstance().getGroupId());
		hvo.setStatus(VOStatus.NEW);
		hvo.setBillmaker(InvocationInfoProxy.getInstance().getUserId());// 制单人
		
		return hvo;

	}

	
	private List<CtPaymentVO> getBillPaymentVO(JSONObject bill)
			throws BusinessException {

		List<CtPaymentVO> list =new ArrayList<CtPaymentVO>();

		JSONArray childArray = bill.getJSONArray("childvo2"); // 如果json格式的字符串里含有数组格式的属性，将其转换成JSONArray，以方便后面转换成对应的实体
		String pk_group= InvocationInfoProxy.getInstance().getGroupId();
	
    	for(int i=0;i<childArray.size();i++){
    		CtPaymentVO vo = new CtPaymentVO();
          Map<String,String> childVomap=(Map)childArray.get(i);
        	   for(String itemkey:childVomap.keySet()){
            		 if(childVomap.get(itemkey)!=null&&!"".equals(childVomap.get(itemkey))){
            			  vo.setAttributeValue(itemkey,childVomap.get(itemkey));
            			
            	 }
             } 
        	   
        	   vo.setPk_group(InvocationInfoProxy.getInstance().getGroupId());
        	   
        	   vo.setPk_balatype("0001Z0100000000000Y2");
        	   list.add(vo);
         }
		return list;

	}

	private List<CtPuBVO> getBillDetailVO(JSONObject bill)
			throws BusinessException {

		List<CtPuBVO> list =new ArrayList<CtPuBVO>();

		JSONArray childArray = bill.getJSONArray("childvo"); // 如果json格式的字符串里含有数组格式的属性，将其转换成JSONArray，以方便后面转换成对应的实体
		String pk_group= InvocationInfoProxy.getInstance().getGroupId();
	
    	for(int i=0;i<childArray.size();i++){
    		CtPuBVO vo = new CtPuBVO();
          Map<String,String> childVomap=(Map)childArray.get(i);
        	   for(String itemkey:childVomap.keySet()){
            		 if(childVomap.get(itemkey)!=null&&!"".equals(childVomap.get(itemkey))){
            			  vo.setAttributeValue(itemkey,childVomap.get(itemkey));
            			
            	 }
             } 
        	   
        	   vo.setPk_group(InvocationInfoProxy.getInstance().getGroupId());
        	   list.add(vo);
         }
		return list;

	}

	



	private Object processBill(AggCtPuVO  order,JSONObject bill) throws BusinessException {

		// 2. 校验数据的合法性:1.数据结构完整 2.根据组织+单据号校验是否重复.
		checkData(order);
		// 3.补全数据,并且调整单据状态
		fillData(order);
		
		RelationCalculate  cal=new RelationCalculate();
		
		cal.calculate(order, "nmny");
	
		AggCtPuVO bill2 = (AggCtPuVO) insert(order);
		
		
		
		
		AggCtPuVO[] bill3 = (AggCtPuVO[]) approveBill(new AggCtPuVO[]{bill2});
		
		AggCtPuVO[] bill4 = (AggCtPuVO[]) validate(new AggCtPuVO[]{bill2});
		
		IFileSystemService baction = (IFileSystemService) NCLocator.getInstance().lookup(
				IFileSystemService.class);
		JSONArray urlarr = bill.getJSONArray("url");
		if(urlarr!=null){
			for(int i=0;i<urlarr.size();i++){
				JSONObject url=(JSONObject) urlarr.get(i);
				NCFileVO file=new NCFileVO();
				file.setCreator(order.getParentVO().getCreator());
				file.setFiletype("url");
				file.setPk_doc(url.getString("url"));
				List<String> segsList = new ArrayList();
//				segsList.set(0, bill2.getParentVO().getPk_ct_pu());
//				segsList.set(1, String.valueOf(i));
				file.setPath(bill2.getParentVO().getPk_ct_pu()+"/"+i);
				baction.insertURLFileVO(file);
			}
		}
//	insertURLFileVO(NCFileVO paramNCFileVO)
		
		
		order=bill4[0];
		return bill2.getParentVO().getPrimaryKey();
	}

	protected AggCtPuVO[] validate(AggCtPuVO[] billvos)
			throws BusinessException {
       //重新查询，防止并发
		String[] hids = VOEntityUtil.getPksFromAggVO(billvos);
	    SCMBillQuery<AggCtPuVO> queryTool = new SCMBillQuery<AggCtPuVO>(AggCtPuVO.class);
	    AggCtPuVO[] vo = queryTool.queryVOByIDs(hids);	 

		IPFBusiAction baction = (IPFBusiAction) NCLocator.getInstance().lookup(
				IPFBusiAction.class);
		AggCtPuVO[] object = (AggCtPuVO[]) baction.processBatch("VALIDATE", "Z2", vo, null, null, null);
		return vo;
		
	}
	
	protected AggCtPuVO[] approveBill(AggCtPuVO[] billvos)
			throws BusinessException {
       //重新查询，防止并发
		String[] hids = VOEntityUtil.getPksFromAggVO(billvos);
	    SCMBillQuery<AggCtPuVO> queryTool = new SCMBillQuery<AggCtPuVO>(AggCtPuVO.class);
	    AggCtPuVO[] vo = queryTool.queryVOByIDs(hids);	 

		IPFBusiAction baction = (IPFBusiAction) NCLocator.getInstance().lookup(
				IPFBusiAction.class);
		AggCtPuVO[] object = (AggCtPuVO[]) baction.processBatch("APPROVE", "Z2", vo, null, null, null);
		String sCurrentDate = new UFDate(System.currentTimeMillis()).toString();
		
		String sql=" update ct_pu a set a.fstatusflag ='3',a.approver ='"+InvocationInfoProxy.getInstance().getUserId()+"' , a.taudittime ='"+(new UFDateTime(System.currentTimeMillis())).toString()+"' where pk_ct_pu='"+object[0].getParentVO().getPk_ct_pu()+"'";
		BaseDAO dao=new  BaseDAO();
		dao.executeUpdate(sql);
//		List localList = (List)new PfUtilActionVO().processAction("APPROVE", "Z2", sCurrentDate, null, vo, null);
		return vo;
		
	}


	private void fillData(AggCtPuVO   order) {
		// TODO Auto-generated method stub
		
		String[] formulas_F2 = {
				//最新的销售组织
				"pk_org->getColValue2(org_financeorg,pk_financeorg,code,pk_org,islastversion,\"Y\")",
				//销售组织版本号
				"pk_org_v->getColValue2(org_financeorg,pk_vid,pk_financeorg,pk_org,islastversion,\"Y\")",
				//单据类型编码
				"cbilltypecode->\"Z2\"",
				
				// 供应商
				"cvendorid->getColValue2(bd_supplier,pk_supplier,code,cvendorid,dr,\"0\")",

//				单据类型编码 
			//	"pk_tradetypeid->\"0001A110000000001NPK\"",
				"ctrantypeid->getColValue(bd_billtype,pk_billtypeid,pk_billtypecode,vtrantypecode)",
				"ccurrencyid->getColValue(bd_currtype,pk_currtype,code,\"CNY\")",
				"corigcurrencyid->getColValue(bd_currtype,pk_currtype,code,\"CNY\")",
				"depid->getColValue2(org_dept,pk_dept ,code  ,depid,islastversion,\"Y\")",
				"depid_v->getColValue2(org_dept_v,pk_vid ,pk_dept ,depid,islastversion,\"Y\")",
				"nexchangerate->\"1\"",
				"version->\"1\"",
				"bbracketorder->\"N\"",
				"bsc->\"N\"",
			//	"actualvalidate->valdate",
			//	fstatusflag 
				
				"fstatusflag ->\"0\"",
//				"creator->getColValue(sm_user,cuserid,user_code,creator)",
				"pk_purcorp->pk_org"

		};
		SuperVOUtil.execFormulaWithVOs(new CtPuVO[]{ order.getParentVO()} ,  formulas_F2,
				null);
		 String[] formulas_F2_B = {
				 "cunitid->getColValue(bd_measdoc, pk_measdoc,code,cunitid)",
//				 "cunitid->getColValue(bd_measdoc, pk_measdoc,code,cunitid)",
					"castunitid->getColValue(bd_measdoc, pk_measdoc,code,castunitid)",
				 
//				 "castunitid->cunitid",
				 "cqtunitid->cunitid",
//				 "nnum->nastnum",
				 "nqtunitnum->nastnum",
				
				 "pk_material->getColValue(bd_material_v,pk_material,code,pk_material)",
				 "crececountryid->\"0001Z010000000079UJJ\"",
				 "csendcountryid->\"0001Z010000000079UJJ\"",
				 "ctaxcountryid->\"0001Z010000000079UJJ\"",
//				 "ctaxcodeid->\"1001A11000000001M7E8\"",
				// 税码
					"ctaxcodeid->getColValue(bd_material,pk_mattaxes,pk_material,pk_material)",
				 "pk_srcmaterial->pk_material",
//				 FBUYSELLFLAG	FTAXTYPEFLAG
//				 2	1
//				 "vchangerate  ->\"1.0/1.0\"",
				 "vqtunitrate  ->vchangerate",
				  
//				 "crowno  ->\"10\"",
			
//				 "vbdef2->getColValue(qc_chkstdmatch,pk_checkstandard, pk_chkstdmatch,vbdef2)",
				 
					//最新的组织
					"fbuysellflag ->2",
					"ftaxtypeflag->1",
					"ntaxrate->0",
					"pk_org_v->getColValue2(org_purchaseorg_v,pk_vid, pk_purchaseorg ,pk_org,islastversion,\"Y\")",
					
					 "pk_arrvstock->pk_org",
					 "pk_arrvstock_v->pk_org_v",
					 "pk_financeorg->pk_org",
					 "pk_financeorg_v->pk_org_v"
//					 "pk_srcmaterial->getColValue2(bd_material_v,pk_vid, pk_org ,pk_org,islastversion,\"Y\")",
//					 	

//					//销售组织版本号
//					"local_rate->1",
//					"bill_type->\"F2\"",
//					"pk_org->getColValue2(org_financeorg,pk_financeorg,code,pk_org,islastversion,\"Y\")",
//					"teade_type->\"F2\"",
//					//单据类型编码
//					"pk_billtype->\"F2\"",
//					
//					"customer->getColValue(bd_customer, pk_customer,code,customer)",
//					"creator->getColValue(sm_user,cuserid,user_code,creator)",
//					"pk_currtype->getColValue(bd_currtype,pk_currtype,code,pk_currtype)",
//					//金额
//					"castunitid->cunitid",
//					"local_money->money",
//					"local_money_bal->money",
//					"local_money_cr->money",
//					"local_money_bal->money",
//					"local_notax_cr->money",
//					"money_bal->money",
//					"money_cr->money",
//					"notax_cr->money"

			};
		 
		 
			
		 
		 
		SuperVOUtil.execFormulaWithVOs( order.getCtPuBVO(),  formulas_F2_B,
				null);
		
		
 String[] formulas_F2_pay = {
				 
		 "pk_org_v->getColValue2(org_purchaseorg_v,pk_vid, pk_org ,pk_org,islastversion,\"Y\")"
//		

			};
		 
		 
			
		 
		 
		SuperVOUtil.execFormulaWithVOs( order.getCtPaymentVO(),  formulas_F2_pay,
				null);
//		
	}
	
	

	//public  
	
	
	
//	收款结算单表体
	
	//public 
	protected AggCtPuVO insert(AggCtPuVO billvo) throws BusinessException {

		IPFBusiAction service = NCLocator.getInstance().lookup(IPFBusiAction.class);
		
//
//		InvocationInfoProxy.getInstance().setGroupId(billvo.getHeadVO().getAttributeValue("pk_group").toString());
//		InvocationInfoProxy.getInstance().setUserId(billvo.getHeadVO().getCreator());
		
		//保存
		AggCtPuVO[] returnVO1 = (AggCtPuVO[]) service.processBatch("SAVEBASE", "Z2",new AggCtPuVO[]{billvo}, null, null, null);		
		return returnVO1[0];
	}


	private void checkData(AggCtPuVO  resvo) throws BusinessException {
		if (resvo == null || resvo.getParentVO() == null)
			throw new BusinessException("未获取的转换后的数据");
		if (resvo.getChildrenVO() == null || resvo.getChildrenVO().length == 0) {
			throw new BusinessException("表体不允许为空");
		}
	

	}

	
}
