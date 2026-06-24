package nc.bs.jzyy.sys.oa.priceaudit;

import java.util.HashMap;
import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.bs.jzyy.sys.oa.ParamCheck;
import nc.bs.jzyy.sys.oa.out.AbstractSender4OA;
import nc.vo.pp.m28.entity.PriceAuditVO;
import nc.vo.pub.BusinessException;

import com.alibaba.fastjson.JSONObject;

public class OA_PriceAuditPlugin {

	BaseDAO dao;

	public BaseDAO getDao() {
		if (dao == null) {
			dao = new BaseDAO();
		}
		return dao;
	}
	
	public void sys(String billltypecode, Object obj, Map<String, Object> otherpms) throws BusinessException {
		ParamCheck ck = new ParamCheck();
		PriceAuditVO bill = (PriceAuditVO)obj;
		boolean is2oa = ck.is2oa(bill.getParentVO().getPk_org());
		if(!is2oa){
			return;
		}
		this.process(billltypecode, obj, otherpms);
	}
	

	public void process(String billltypecode, Object obj, Map<String, Object> otherpms) throws BusinessException {
		AbstractSender4OA sender = new OA_PriceAuditSender();
		otherpms = new HashMap<String, Object>();
		try {
			JSONObject resp = (JSONObject) sender.process(billltypecode, obj, otherpms);
			// 获取返回信息
			if (resp != null) {
				// 判断一下返回报文，如果是更成功，则更新一下同步中信状态
			}
			// 如果同步成功,更新同步时间戳（）,使用不更新ts的方法
		} catch (Exception e) {
			throw new BusinessException("同步失败:" + e.getMessage());
		}

	}

}
