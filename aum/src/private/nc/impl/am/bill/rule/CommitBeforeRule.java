package nc.impl.am.bill.rule;



import java.util.ArrayList;

import nc.bs.am.framework.action.ActionContext;
import nc.bs.am.framework.action.IRule;
import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ArrayProcessor;
import nc.ui.fts.commissiongathering.handler.GetModeEditHandler;
import nc.vo.am.common.util.ArrayUtils;
import nc.vo.am.common.util.StringUtils;
import nc.vo.am.constant.CommonKeyConst;
import nc.vo.aum.disused.DisusedBodyVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.util.AuditInfoUtil;

/**
 * 检索设备是否有启用的预防性维护
 * 
 * @author bbt 2024/09/26
 *
 * @param <T>
 */
public class CommitBeforeRule<T extends AggregatedValueObject> implements IRule<T>{

	private Object[] sqlExecute(String tableName,String columnName,String whereColumnName,String whereValue,String whereColumnName2,String whereValue2) {
		try {
			Object[] obj_result = null;
			IUAPQueryBS query = NCLocator.getInstance().lookup(IUAPQueryBS.class);
			StringBuilder sql = new StringBuilder();
			sql.append("select "  + columnName 
					+ " from " + tableName 
					+ " where " + whereColumnName + " = '" + whereValue 
					+ "' and " + whereColumnName2 + " = '" + whereValue2
					+ "';");
			obj_result = (Object[])query.executeQuery(sql.toString(),new ArrayProcessor());
			return obj_result;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
	}
	
	@Override
	public void process(ActionContext<T> context, T... aggVos)
			throws BusinessException {
		//1、得到资产报废所有子表资产
		CircularlyAccessibleValueObject[] cavos = aggVos[0].getChildrenVO();
		//定义一个存储预防性维护单号的数组
		ArrayList<Object> allMatchPM = new ArrayList<>();
		//2、遍历资产
		for(CircularlyAccessibleValueObject cavo : cavos){
			//3、获取设备信息
			String pkequip = (String) cavo.getAttributeValue("pk_equip");
			//4、去匹配预防性维护
			Object[] pmcodes = sqlExecute("emm_pm","bill_code","pk_equip",pkequip,"enablestate","2");
			//5、查询返回值非空则存储所有预防性维护单号，为空则直接下一次循环
			if(null != pmcodes && !"".equals(pmcodes)){
				for(Object pmcode : pmcodes)
					allMatchPM.add(pmcode);
			}
		}
		if(allMatchPM.size() > 0){
			throw new BusinessException("以下PM单关联资产，请先处理：\n" + allMatchPM.toString());
		}
		
	}
}

		