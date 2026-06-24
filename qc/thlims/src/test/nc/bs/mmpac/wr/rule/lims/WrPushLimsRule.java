package nc.bs.mmpac.wr.rule.lims;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.framework.common.NCLocator;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.itf.jzyy.sys.IProcessService;
import nc.itf.jzyy.sys.ISysDispatcher;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.MapProcessor;
import nc.util.mmf.framework.base.MMValueCheck;
import nc.vo.mmpac.wr.entity.AggWrVO;
import nc.vo.mmpac.wr.entity.WrItemVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

public class WrPushLimsRule implements IRule<AggWrVO> {

	@Override
	public void process(AggWrVO[] vos) {
		// 判空
		if (MMValueCheck.isEmpty(vos)) {
			return;
		}
	
		// 如果是外系统质检
		IProcessService outerService = (IProcessService) NCLocator.getInstance()
				.lookup(IProcessService.class.getName());
		try {
			String pk_org1 = vos[0].getParentVO().getPk_org();
			/*
	 		 * edit by xuchong 2022年9月8日
	 		 * 根据组织判断Lims 区分调用
	 		 * 23 审批报检
	 		 * 28 组织 非审批报检  通过点击 报检按钮报检
	 		 * */
			if("0001V110000000012E56".equalsIgnoreCase(pk_org1)){
				return ;
			}
			
			if (!outerService.isOutSystem(pk_org1)) {
				return ;
			}
			for (AggWrVO aggWrVO : vos) {
				List<WrItemVO> reList = new ArrayList<WrItemVO>();
				String pk_org = aggWrVO.getParentVO().getPk_org();
				// 如果是外系统质检
				if (MMValueCheck.isEmpty(aggWrVO
						.getChildren(WrItemVO.class))) {
					continue;
				}
				for (WrItemVO itemVO : (WrItemVO[]) aggWrVO
						.getChildren(WrItemVO.class)) {					
//					fbproducttype  产品类型  fbproducttype int  产出类型   1=主产品，2=联产品，3=副产品，   
					if(itemVO.getFbproducttype()!=1){
						continue;
					}
					if(!isMmoToLims(itemVO)){
						continue;
					}
					if (chekQC(pk_org, itemVO.getCbmaterialid())) {
						continue;
					}
					
					reList.add(itemVO);

				}
				if (reList == null || reList.size() == 0) {
					continue;
				}
				
				ISysDispatcher sysdisptch = (ISysDispatcher) NCLocator.getInstance().lookup(ISysDispatcher.class.getName());
				Map<String, Object> param = new HashMap<String, Object>();
				param.put("bvos",reList.toArray( new WrItemVO[0]) );
				sysdisptch.dispatch(aggWrVO, "LIMS_WR_CHECK", param);
////				//生成检验记录
//				WrFreeQualityBP freeBp = new WrFreeQualityBP();
//				freeBp.process((WrItemVO[]) reList	.toArray(new WrItemVO[0]));
		
			}
		} catch (Exception e) {
			ExceptionUtils.wrappException(e);
		}
	
	}
	/**
	 * 只有来源流程生产订单同步了LIMS的才执行同步
	 * @param itemVO
	 * @return
	 * @throws BusinessException 
	 */
	private boolean isMmoToLims(WrItemVO itemVO) throws BusinessException {
		// TODO Auto-generated method stub
		String sql=" select vdef11  from mm_mo  where   cmoid='"+itemVO.getVbsrcrowid()+ "'";
		IUAPQueryBS bs = (IUAPQueryBS) NCLocator.getInstance().lookup(
				IUAPQueryBS.class.getName());
		HashMap<String, Object> hashMap2 = (HashMap<String, Object>) bs.executeQuery(sql, new MapProcessor());
		if (hashMap2 != null && hashMap2.size() > 0) {
			String vdef11 = (String) hashMap2.get("vdef11");
			if(vdef11!=null && vdef11.startsWith("ER")){
				return true;
			}
		}

		return false;
	}

	// 检查物料是否免检
	public boolean chekQC(String pk_org, String material)
			throws BusinessException {
		String sql = " select chkfreeflag    from bd_materialstock where pk_material='"
				+ material + "' and   pk_org ='" + pk_org + "' and dr=0";
		IUAPQueryBS bs = (IUAPQueryBS) NCLocator.getInstance().lookup(
				IUAPQueryBS.class.getName());

		HashMap<String, Object> hashMap2 = (HashMap<String, Object>) bs
				.executeQuery(sql, new MapProcessor());

		if (hashMap2 != null && hashMap2.size() > 0) {
			UFBoolean b = UFBoolean.valueOf(hashMap2.get("chkfreeflag")
					.toString());
			return b.booleanValue();
		}
		return false;

	}
}
