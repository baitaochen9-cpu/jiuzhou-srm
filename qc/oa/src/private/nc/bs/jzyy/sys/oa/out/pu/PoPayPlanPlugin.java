package nc.bs.jzyy.sys.oa.out.pu;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import nc.bs.dao.BaseDAO;
import nc.bs.jzyy.sys.oa.ParamCheck;
import nc.bs.jzyy.sys.oa.out.AbstractSender4OA;
import nc.bs.logging.Log;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.vo.pp.m28.entity.PriceAuditVO;
import nc.vo.pu.m21.entity.PayPlanViewVO;
import nc.vo.pub.BusinessException;
import com.alibaba.fastjson.JSONObject;

public class PoPayPlanPlugin {

	BaseDAO dao;
	public BaseDAO getDao() {
		if (dao == null) {
			dao = new BaseDAO();
		}
		return dao;
	}

	public Object sys(String billltypecode, Object obj, Map<String, Object> otherpms) throws BusinessException {
		ParamCheck ck = new ParamCheck();
		PayPlanViewVO bill = (PayPlanViewVO)obj;
		boolean is2oa = ck.is2oa(bill.getPk_org());
		if(!is2oa){
			return null;
		}
		return this.process(billltypecode, obj, otherpms);
	}
	
	public Object process(String billltypecode, Object obj, Map<String, Object> otherpms) throws BusinessException {
		PayPlanViewVO planViewVO=(PayPlanViewVO)obj;
		if(null==planViewVO){
			throw new BusinessException("付款计划数据异常");
		}
		/*
		 * 校验同步标识 oa_requestid
		 * */
		String  query_sql="select  oa_requestid from po_order_payplan where PK_ORDER_PAYPLAN='"+planViewVO.getPrimaryKey()+"' and oa_requestid is not null";
		List<Map<String,Object>> listMaps=(List<Map<String,Object>>)getDao().executeQuery(query_sql,new MapListProcessor());
		if(null!=listMaps && listMaps.size()>0 && null!=listMaps.get(0).get("oa_requestid")){
			throw new BusinessException("该采购付款计划已经存在于OA："+listMaps.get(0).get("oa_requestid"));
		}
		AbstractSender4OA sender = new PoPayPlanSender();
		otherpms = new HashMap<String, Object>();
		try {
			JSONObject resp = (JSONObject) sender.process(billltypecode, obj, otherpms);
			// 获取返回信息
			if(resp !=null) {
				String code = String.valueOf(resp.get("code")) ;
				if("SUCCESS".equals(code)) {
					JSONObject data = (JSONObject) resp.get("data");
					String requestid = String.valueOf(data.get("requestid")) ;
					if(!requestid.isEmpty()) {
						/*
						 * 更新同步表示 防止重复提交2022-07-22
						 * */
						String update_sql = " update po_order_payplan set  oa_requestid ='"+requestid+"' where PK_ORDER_PAYPLAN='" + planViewVO.getPrimaryKey() + "'";
						int executeUpdate = getDao().executeUpdate(update_sql);
						Log.getInstance("更新OA标识条数:").equals("===="+executeUpdate+"行");
						return requestid;
					}else {
						JSONObject errMsg = (JSONObject) resp.get("errMsg");
						String msg = errMsg.getString("msg");
						throw new BusinessException("返回结果："+msg);
					}
				}else {
					 throw new BusinessException("返回结果："+resp);
				}
			}else {
				 throw new BusinessException("返回结果为空.");
			}
			// 如果同步成功,更新同步时间戳（）,使用不更新ts的方法
		} catch (Exception e) {
			throw new BusinessException("同步失败:" + e.getMessage());
		}
	}
	      
}
