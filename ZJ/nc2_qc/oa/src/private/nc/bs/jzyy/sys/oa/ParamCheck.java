package nc.bs.jzyy.sys.oa;



import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
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
	 * 组织参数，判断是否同步
	 * @param pk_org
	 * @return
	 * @throws DAOException
	 */
	public  boolean is2oa(String pk_org) throws DAOException {
		String sql="select DISTINCT value  from  pub_sysinit where initcode='ISTOOA' and pk_org = '"+pk_org+"' ";
		
		String is2crm = (String) getDao().executeQuery(sql, columnprocessor);
		if("Y".equalsIgnoreCase(is2crm)) {
			return true;
		}
		
		return false;
	}
	
}
