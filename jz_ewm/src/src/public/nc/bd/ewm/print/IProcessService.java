package nc.bd.ewm.print;

import java.util.List;

import nc.bs.dao.DAOException;
import nc.vo.ewm.printapply.AggPrintapply;
import nc.vo.ewm.workorder.AggWorkOrderVO;

public interface IProcessService {

	/**
	 * 
	 * 更新可打印标识为Y
	 * 
	 * @param pk_org
	 * @return
	 * @throws DAOException
	 */
	void printDefUpdate(AggPrintapply[] vo) throws DAOException;

	/**
	 * 
	 * 更新可打印标识为N
	 * 
	 * @param pk_org
	 * @return
	 * @throws DAOException
	 */
	void printDefUnUpdate(AggPrintapply[] vo) throws DAOException;

	/**
	 * 
	 * 组织参数，判断是否重新打印
	 * 
	 * @param pk_org
	 * @return
	 * @throws DAOException
	 */
	boolean isPrint(String pk_org) throws DAOException;

	/**
	 * 
	 * 更新可打印标识
	 * 
	 * @param pk_org
	 * @return
	 * @throws DAOException
	 */
	void printCountUpdate(AggWorkOrderVO[] vo) throws DAOException;

	/**
	 * 
	 * 查询已存在单据
	 * 
	 * @param pk_org
	 * @return
	 * @throws DAOException
	 */
	List printApplyQuery(String id) throws DAOException;

	/**
	 * 
	 * 更新可打印次数
	 * 
	 * @param pk_org
	 * @return
	 * @throws DAOException
	 */
	void printUpdate(int id, AggWorkOrderVO[] vo) throws DAOException;

	/**
	 * 
	 * 查询打印次数
	 * 
	 * @param pk_org
	 * @return
	 * @throws DAOException
	 */
	String qryCount(AggPrintapply[] vo) throws DAOException;

	void printCountUpdate(int count, AggPrintapply[] vo) throws DAOException;
	String qryApllyCount(AggPrintapply[] vo) throws DAOException;
}
