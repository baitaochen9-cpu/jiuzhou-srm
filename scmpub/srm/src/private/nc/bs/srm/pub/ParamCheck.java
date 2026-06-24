package nc.bs.srm.pub;



import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.cmp.tools.StringUtil;
import nc.jdbc.framework.processor.ColumnProcessor;

/**
 * 
 * @author liyf
 *注意 用sql 直接查询，绕开NC的缓存机制
 */
public class ParamCheck {
	
	private BaseDAO dao = null;

    protected BaseDAO getDao() {
		if (dao == null) {
			dao = new BaseDAO();
		}
		return dao;
	}    
    
	public ColumnProcessor columnprocessor = new ColumnProcessor();

	
	/**
	 * 
	 * 组织参数
	 * @param pk_org
	 * @return
	 * @throws DAOException
	 */
	public  boolean IsToSrm(String pk_org) throws DAOException {
		String sql="select distinct value  from  pub_sysinit where initcode='YF_ISSRM' and pk_org = '"+pk_org+"'";
		
		String is2crm = (String) getDao().executeQuery(sql, columnprocessor);
		if(is2crm == null || StringUtil.isEmpty(is2crm)) {
			return false;
		}
		
		if("Y".equalsIgnoreCase(is2crm)) {
			return true;
		}
		
		return false;
	}
	
}
