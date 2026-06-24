package nc.bs.jzyy.sys;

import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.framework.common.NCLocator;
import nc.bs.jzyy.sys.oa.ParamCheck;
import nc.itf.jzyy.sys.IProcessService;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapProcessor;
import nc.vo.pub.BusinessException;

import org.apache.commons.lang3.StringUtils;

public class ProcessServiceImpl implements IProcessService {

private BaseDAO dao;

	
	public BaseDAO getDao() {
		if (dao == null) {
			dao = new BaseDAO();
		}
		return dao;
	}
	@Override
	public String getSysLIMSIp() throws Exception {
		// TODO Auto-generated method stub
//		String sql = "select nvl(a.value,0)   as value from  pub_sysinit a " 
//				+ " where a.initcode ='MDMURL' ";
//		
		String sql = "select name  from  bd_defdoc where code='SZLIMS' 	and pk_defdoclist=(select pk_defdoclist  from bd_defdoclist where code='JZYY_PZQD')";

		String url = (String) getDao().executeQuery(sql, new ColumnProcessor());
		if(StringUtils.isEmpty(url)){
			throw new BusinessException("请到自定义档案-集团(JZYY_PZQD) 中维护 编码SZLIMS 的同步到苏州瑞博LIMS的地址");
		}
		return url;
	}
	/**
	 * 
	 * 组织参数，判断是否同步
	 * @param pk_org
	 * @return
	 * @throws DAOException
	 */
	@Override
	public  boolean is2oa(String pk_org) throws DAOException {
		ParamCheck ck = new ParamCheck();
		
		boolean is2oa = ck.is2oa(pk_org);
		return is2oa;
	}
	
	public boolean isOutSystem(String pk_org) throws Exception {
		return qryBooleanParm(pk_org, "YFQC01", "是否LIMS报检", false);
	}
	@Override
	public boolean qryBooleanParm(String pk_org,String initcode,String intname,boolean defalut)
			throws Exception {
		boolean rs =defalut;
		IUAPQueryBS iuap = (IUAPQueryBS) NCLocator.getInstance().lookup(
				IUAPQueryBS.class.getName());

		// // 到货单是否自动报检】判断，如果参数=是则自动报检，如果参数=否，则不执行自动报检
		String sql = "select nvl(a.value,0)   as value " 
				+ " from  pub_sysinit a " 
				+ " where a.initcode ='"+initcode+"' and  a.pk_org='" + pk_org + "'";
		try {
			Map map = (Map) iuap.executeQuery(sql, new MapProcessor());
			if( map == null || map.get("value") == null)
				return true;
			String kzlx = map.get("value").toString();
			if (kzlx == null || "".equals(kzlx) || "否".equals(kzlx)
					|| "N".equals(kzlx)) {
				rs =  false;
			}else{
				rs= true;
			}
		} catch (BusinessException e1) {
			throw new BusinessException("查询参数"+initcode+intname+"异常:"+e1.getMessage());
		}
		return rs;
	}

}
