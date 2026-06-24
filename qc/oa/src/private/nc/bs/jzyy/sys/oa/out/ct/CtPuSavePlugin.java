package nc.bs.jzyy.sys.oa.out.ct;

import java.util.HashMap;
import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.bs.jzyy.sys.oa.ParamCheck;
import nc.bs.jzyy.sys.oa.out.AbstractSender4OA;
import nc.vo.ct.purdaily.entity.AggCtPuVO;
import nc.vo.pp.m28.entity.PriceAuditVO;
import nc.vo.pub.BusinessException;

import com.alibaba.fastjson.JSONObject;

public class CtPuSavePlugin {

	BaseDAO dao;

	public BaseDAO getDao() {
		if (dao == null) {
			dao = new BaseDAO();
		}
		return dao;
	}
	
	public Object sys(String billltypecode, Object obj, Map<String, Object> otherpms) throws BusinessException {
		ParamCheck ck = new ParamCheck();
		AggCtPuVO bill = (AggCtPuVO)obj;
		boolean is2oa = ck.is2oa(bill.getParentVO().getPk_org());
		if(!is2oa){
			return null ;
		}
		return this.process(billltypecode, obj, otherpms);
	}

	public Object process(String billltypecode, Object obj, Map<String, Object> otherpms) throws BusinessException {
		AbstractSender4OA sender = new CtpuSaveSender();
//		Map<String, Object> Data = (Map<String, Object>) obj;
		otherpms = new HashMap<String, Object>();
		try {
			JSONObject resp = (JSONObject) sender.process(billltypecode, obj, otherpms);
			// 获取返回信息
			if(resp !=null) {
				String code = String.valueOf(resp.get("code")) ;
				if("SUCCESS".equals(code)) {
					JSONObject data = (JSONObject) resp.get("data");
//					String successCount = (String)data.get("requestid");
					String successCount = String.valueOf(resp.get("requestid")) ;
					if(!successCount.isEmpty()) {
//						JSONObject reqFailMsg = (JSONObject) resp.get("reqFailMsg");
//						JSONObject otherParams = (JSONObject) reqFailMsg.get("otherParams");
//						String doAutoApprove = otherParams.getString("doAutoApprove");
//						sql = " update bd_marbasclass set  def1 ='"+ERPCode+"',def10='"+curTime+"' where    pk_marbasclass   ='" + billvo.getPrimaryKey() + "'";
//						getDao().executeUpdate(sql);
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
			return resp;
			// 如果同步成功,更新同步时间戳（）,使用不更新ts的方法
		} catch (Exception e) {
			throw new BusinessException("同步失败:" + e.getMessage());
		}

	}
	      
}
