package nc.bs.srm.qc;

import java.util.HashMap;
import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.logging.Logger;
import nc.bs.srm.pub.AbstractSender4Mdm;
import nc.bs.srm.pub.ParamCheck;
import nc.vo.pu.supqualistatus.SupplierqualityHVO;
import nc.vo.pub.BusinessException;

import com.alibaba.fastjson.JSONObject;

/**
 * 生产厂商推srm
 * 
 * @author yinsen.zhang
 * 
 */
public class QuanlityAddPlugin {

	BaseDAO dao;

	public BaseDAO getDao() {
		if (dao == null) {
			dao = new BaseDAO();
		}
		return dao;
	}

	public void process(String billltypecode, Object obj,
			Map<String, Object> otherpms) throws BusinessException {
	
			AbstractSender4Mdm sender = new QuanlityAddSender();
			otherpms = new HashMap<String, Object>();
			try {

				// 新增日志
				JSONObject resp = (JSONObject) sender.process(billltypecode,
						obj, otherpms);
				if (resp != null) {
					String code = resp.getString("responseStatus");
					String message = resp.getString("responseMessage");
					if (code.equalsIgnoreCase("SUCCESS")) {

					} else {
						throw new BusinessException("srm拒收." + message);
					}
				} else {
					throw new BusinessException("srm质量管理接口返回结果为空.");
				}
				// 更新日志
			} catch (Exception e) {
				Logger.error(e.getMessage(), e);
				throw new BusinessException("srm质量管理接口同步失败:" + e.getMessage());
			}
		

	}

	public void sendSrm(String billltypecode, Object obj,
			Map<String, Object> otherpms) throws Exception{
		ParamCheck ck = new ParamCheck();
		//判断前台是否允许推送srm
		SupplierqualityHVO hvo = (SupplierqualityHVO) obj;
		boolean isToSrm = ck.IsToSrm(hvo.getPk_org());
		if (isToSrm) {
			process(billltypecode,
					obj, otherpms);
		}
	}

}
