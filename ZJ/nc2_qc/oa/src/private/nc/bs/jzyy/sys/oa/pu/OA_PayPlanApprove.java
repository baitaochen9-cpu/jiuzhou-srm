package nc.bs.jzyy.sys.oa.pu;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.framework.common.NCLocator;
import nc.bs.jzyy.sys.AbstracAdapter4Ext;
import nc.bs.logging.Log;
import nc.itf.pu.m21.IOrderPayPlanQuery;
import nc.itf.scmpub.reference.uap.pf.PfServiceScmUtil;
import nc.pubitf.arap.pay.IArapPayBillPubService;
import nc.vo.arap.pay.AggPayBillVO;
import nc.vo.pu.m21.entity.AggPayPlanVO;
import nc.vo.pu.m21.entity.PayPlanViewVO;
import nc.vo.pu.m21.rule.OrderPayChkRule;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.scmpub.res.billtype.POBillType;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * @author qi.dong
 * OA回传付款计划审批结果
 * 
 * action 1:驳回  0:审批通过
 */
public class OA_PayPlanApprove extends AbstracAdapter4Ext {
	
	BaseDAO dao;
	public BaseDAO getDao() {
		if (dao == null) {
			dao = new BaseDAO();
		}
		return dao;
	}
	
	@Override
	public JSONObject sys(Object billvo) throws BusinessException, DAOException {
		int rows = this.F3(billvo);
		JSONObject data = new JSONObject();
		String re_mess="付款计划审批结果同步成功!";
		if(rows>0){
			data.put("rows", rows);
		}else{
			re_mess="付款计划审批结果同步异常!";
		}
		// 将结果返回
		return getRsultDataSuccess(data,re_mess);
	}
	
	/**
	 * @param jsondata
	 * @return
	 */
	public int F3(Object jsondata){
		//传入参数转换JSON对象
		JSONObject reObject=(JSONObject)JSON.toJSON(jsondata);
		//action 1:驳回  0:审批通过
		if("0".equals(reObject.getString("action"))){
			return 1;
		}
		//当前日期
		UFDateTime dateNow=new UFDateTime();
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
		
		/*
		 * 更新更新同步表示字段
		 * */
		String update_sql = " update po_order_payplan set  oa_requestid ='~',ts='"+dateNow.toLocalString()+"' where PK_ORDER_PAYPLAN='" + planViewVOs[0].getPrimaryKey() + "'";
		int executeUpdate=0;
		try {
			executeUpdate= getDao().executeUpdate(update_sql);
			Log.getInstance("更新OA标识条数:").equals("===="+executeUpdate+"行");
		} catch (DAOException e) {
			e.printStackTrace();
		}
		return executeUpdate;
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
