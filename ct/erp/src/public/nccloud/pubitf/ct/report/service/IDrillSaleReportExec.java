package nccloud.pubitf.ct.report.service;

import java.util.List;
import java.util.Map;

//import nc.vo.ia.report.goodsledger.GoodsLedgerViewVO;
import nc.vo.pub.BusinessException;
import nccloud.dto.baseapp.querytree.dataformat.QueryTreeFormatVO;

/**
 * 销售合同执行查询接口
 * 
 * @author cuijunf
 * @date 2019-1-25 下午2:45:05
 * @version ncc1.0
 */
public interface IDrillSaleReportExec {
	/**
	 * 通过查询条件等内容获取要打开的销售合同执行明细的Url
	 * 
	 * @param drillConfigMap
	 * @param condition
	 * @param opents
	 * @return
	 * @throws BusinessException
	 */
	public String getUrlBuyConditions(QueryTreeFormatVO conditionTree,
			Map serializedObjMap) throws BusinessException;

	/**
	 * 获取当前行数据，用于报表转视图页面显示
	 * 
	 * @param id
	 * @param condition
	 * @return
	 */
	public String getViewVOByRow(QueryTreeFormatVO conditionTree,
			Map serializedObjMap) throws BusinessException;
}
