package nc.bs.jzyy.sys.lims.mmwrcheck;

import java.util.HashMap;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.MapProcessor;
import nc.vo.mmpac.wr.entity.WrItemVO;
import nc.vo.pub.BusinessException;

public class MmwrSendTool {
	
	/**
	 * 先做完工报告后,流程生产订单有撤销重新报检的情况
	 * @param itemVO
	 * @return
	 * @throws BusinessException
	 */
	public String getSourceLimsID(WrItemVO itemVO) throws BusinessException {
		// TODO Auto-generated method stub
		String sql=" select vdef11  from mm_mo  where   cmoid='"+itemVO.getVbsrcrowid()+ "'";
		IUAPQueryBS bs = (IUAPQueryBS) NCLocator.getInstance().lookup(
				IUAPQueryBS.class.getName());
		HashMap<String, Object> hashMap2 = (HashMap<String, Object>) bs.executeQuery(sql, new MapProcessor());
		if (hashMap2 != null && hashMap2.size() > 0) {
			String vdef11 = (String) hashMap2.get("vdef11");
			return vdef11;
		}

		return null;
	}

}
