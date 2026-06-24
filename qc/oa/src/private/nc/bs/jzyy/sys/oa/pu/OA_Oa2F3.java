package nc.bs.jzyy.sys.oa.pu;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import nc.bs.dao.DAOException;
import nc.bs.framework.common.NCLocator;
import nc.bs.jzyy.sys.AbstracAdapter4Ext;
import nc.itf.pu.m21.IOrderPayPlanQuery;
import nc.itf.scmpub.reference.uap.pf.PfServiceScmUtil;
import nc.pubitf.arap.pay.IArapPayBillPubService;
import nc.vo.arap.pay.AggPayBillVO;
import nc.vo.pu.m21.entity.AggPayPlanVO;
import nc.vo.pu.m21.entity.PayPlanViewVO;
import nc.vo.pu.m21.rule.OrderPayChkRule;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.scmpub.res.billtype.POBillType;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class OA_Oa2F3 extends AbstracAdapter4Ext {
	
	@Override
	public JSONObject sys(Object billvo) throws BusinessException, DAOException {
		AggPayBillVO praybillVO = this.F3(billvo);
		JSONObject data = new JSONObject();
		String re_mess="付款计划生成付款单成功";
		if(null!=praybillVO){
			data.put("code", praybillVO.getHeadVO().getBillno());
			data.put("pk", praybillVO.getPrimaryKey());
			data.put("billstatus", praybillVO.getHeadVO().getBillstatus());
		}else{
			re_mess="付款计划生成付款单异常";
		}
		// 将结果返回
		return getRsultDataSuccess(data,re_mess);
	}
	
	/**
	 * 根据采购付款计划生成付款单
	 * 付款 自动审批
	 * @param jsondata
	 * @return
	 */
	public AggPayBillVO F3(Object jsondata){
		//传入参数转换JSON对象
		JSONObject redataObject=(JSONObject)JSON.toJSON(jsondata);
		//表头信息
		JSONObject reObject=redataObject.getJSONObject("h");
		//当前日期
		UFDate dateNow=new UFDate();
		//转译后的表头信息
		Map<String,String> Hmap=new HashMap<String, String>();
		
		if(StringUtils.isEmpty(reObject.getString("pk_payplan"))){
			ExceptionUtils.wrappBusinessException("pk_payplan 付款计划PK信息非空!");
		}
		
		//查询付款计划
		PayPlanViewVO[] planViewVOs=null; 
		try {
			planViewVOs=this.getOrderPayPlanQuery().queryPayPlanViews(new String[]{reObject.getString("pk_payplan")});
		} catch (BusinessException e) {
			e.printStackTrace();
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		if(null==planViewVOs || planViewVOs.length==0){
			ExceptionUtils.wrappBusinessException(reObject.getString("pk_payplan")+" 未查询到对应的付款计划信息!");
		}
		
		//逻辑检查校验
		this.getPayChkRule().process(planViewVOs);
		
		//VO转换
		AggPayPlanVO[] vos = PayPlanViewVO.getAggPayPlanVO(planViewVOs);
		//单据VO转换 付款计划转为付款单
		AggPayBillVO[] payBillVOs=(AggPayBillVO[])PfServiceScmUtil.exeVOChangeByBillItfDef(POBillType.Order.getCode(), "F3", vos);
		if(null!=payBillVOs && payBillVOs.length>0){
			try {
				//付款保存
				AggPayBillVO[] saves = this.getPayBillPubService().save(payBillVOs);
				if(null!=saves && saves.length>0){
					//付款单提交&审批
					return saves[0];
				}else{
					ExceptionUtils.wrappBusinessException("付款单保存异常!");
				}
			} catch (BusinessException e) {
				e.printStackTrace();
				ExceptionUtils.wrappBusinessException(e.getMessage());
			}
		}else{
			ExceptionUtils.wrappBusinessException("付款计划转为付款单时异常!");
		}
		return null;
	}
	
	/*
	 * 付款单保存,审批服务
	 * */
	private IArapPayBillPubService payBillPubService;
	private IArapPayBillPubService getPayBillPubService(){
		if(null==payBillPubService){
			payBillPubService=(IArapPayBillPubService)NCLocator.getInstance().lookup(IArapPayBillPubService.class);
		}
		return payBillPubService;
	}
	
	/*
	 * 逻辑检查服务
	 * */
	private  OrderPayChkRule orderPayChkRule;
	private OrderPayChkRule getPayChkRule(){
		if(null==orderPayChkRule){
			orderPayChkRule=new OrderPayChkRule();
		}
		return orderPayChkRule;
	}
	/*
	 * 付款计划查询VO
	 * */
	private  IOrderPayPlanQuery orderPayPlanQuery;
	private IOrderPayPlanQuery getOrderPayPlanQuery(){
		if(null==orderPayPlanQuery){
			orderPayPlanQuery=(IOrderPayPlanQuery)NCLocator.getInstance().lookup(IOrderPayPlanQuery.class);
		}
		return orderPayPlanQuery;
	}

}
