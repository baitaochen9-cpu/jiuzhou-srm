package nc.itf.jzyy.sys;

import nc.bs.dao.DAOException;

public interface IProcessService {
	
	/**
	 * 查询boolean类型参数,
	 * @param pk_org 组织
	 * @param initcode
	 * @param intname
	 * @param defalut 默认值
	 * @return
	 * @throws Exception
	 */
	boolean qryBooleanParm(String pk_org,String initcode,String intname,boolean defalut) throws Exception;

	/**
	 * 
	 * 组织参数，判断是否同步
	 * @param pk_org
	 * @return
	 * @throws DAOException
	 */
	boolean is2oa(String pk_org) throws DAOException;
}
