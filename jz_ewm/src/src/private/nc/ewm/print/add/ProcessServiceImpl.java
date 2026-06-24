package nc.ewm.print.add;

import java.util.ArrayList;
import java.util.List;

import nc.bd.ewm.print.IProcessService;
import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.jdbc.framework.processor.ColumnListProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.vo.ewm.printapply.AggPrintapply;
import nc.vo.ewm.workorder.AggWorkOrderVO;

public class ProcessServiceImpl implements IProcessService{
	
	@Override
    public void printDefUpdate(AggPrintapply[] vo)throws DAOException{
		String sql = "update ewm_wo set def9 = 'Y' where pk_wo = '"+vo[0].getParentVO().getPk_wo()+"' ";	
		new BaseDAO().executeUpdate(sql);
	}
	
	@Override
    public void printDefUnUpdate(AggPrintapply[] vo)throws DAOException{
		String sql = "update ewm_wo set def9 = 'N' where pk_wo = '"+vo[0].getParentVO().getPk_wo()+"' ";	
		new BaseDAO().executeUpdate(sql);
	}
	
	@Override
	public  boolean isPrint(String pk_org) throws DAOException {
        String sql = "select DISTINCT value  from  pub_sysinit where " +
        		"initcode='YF_EWM01' and pk_org = '"+pk_org+"' ";	
		String isPrint = (String) new BaseDAO().executeQuery(sql, new ColumnProcessor());
		if("Y".equalsIgnoreCase(isPrint)) {
			return true;
		}
		return false;
	}
	
	@Override
	public String qryCount(AggPrintapply[] vo) throws DAOException {
        String sql = "select def1  from  ewm_wo where pk_wo = '"+vo[0].getParentVO().getPk_wo()+"' ";	
        Object res = new BaseDAO().executeQuery(sql, new ColumnProcessor());
        if(res == null){
        	return "0";
        }
        return (String)res;
	}
	
	@Override
	public String qryApllyCount(AggPrintapply[] vo) throws DAOException {
        String sql = "select def2  from  ewm_printapply "
        		+ "where billid = '"+vo[0].getPrimaryKey()+"' ";	
        return (String)new BaseDAO().executeQuery(sql, new ColumnProcessor());
	}
	
	
	
	@Override
    public void printCountUpdate(AggWorkOrderVO[] vo)throws DAOException{
		String sql = "update ewm_wo set def9 = 'N'  where pk_wo = '"+vo[0].getPrimaryKey()+"' ";	
		new BaseDAO().executeUpdate(sql);
	}
	
	@Override
    public void printCountUpdate(int count,AggPrintapply[] vo)throws DAOException{
		String sql = "update ewm_printapply set def2 = "+count+" "
				+ "where billid = '"+vo[0].getPrimaryKey()+"' ";	
		new BaseDAO().executeUpdate(sql);
	}
	
	
	@Override
	public List printApplyQuery(String id)throws DAOException{
		String sql = "select billno from  ewm_printapply where SRCBILLID" +
				" = '"+id+"' and APPROVESTATUS in ('-1','3') and dr = 0 ";	
		List result = (ArrayList) new BaseDAO().executeQuery(sql, new ColumnListProcessor());
		return result;
	}
	
	@Override
    public void printUpdate(int id,AggWorkOrderVO[] vo)throws DAOException{
		String sql = "update ewm_wo set def1 = "+id+"  where pk_wo = '"+vo[0].getPrimaryKey()+"' ";	
		new BaseDAO().executeUpdate(sql);
	}
}
