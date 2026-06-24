package nccloud.pubimpl.ct.purdaily.service;

import nc.bs.framework.common.NCLocator;
import nc.impl.pubapp.pattern.data.vo.VOQuery;
import nc.vo.ct.purdaily.entity.AggCtPuVO;
import nc.vo.ct.purdaily.entity.CtPuVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nccloud.dto.ct.purdaily.constance.CommonConst;
import nccloud.dto.scmpub.pflow.SCMCloudPFlowContext;
import nccloud.dto.scmpub.script.entity.SCMScriptResultDTO;
import nccloud.pubitf.ct.purdaily.service.IPurdailyService;
import nccloud.pubitf.scmpub.commit.service.IBatchRunScriptService;
import nccloud.pubitf.scmpub.ssc.service.ISSCService;
import nccloud.pubitf.ssctp.sscbd.lientage.ISSClientageMatchService.BusiUnitTypeEnum;

/**
 * @description 采购合同后台服务
 * @author guozhq
 * @date 2019年6月20日 下午9:11:47
 * @version ncc1.0
 */
public class PurdailyServiceImpl implements IPurdailyService {

	@Override
	public SCMScriptResultDTO commit(AggCtPuVO[] vos) throws BusinessException {
		SCMCloudPFlowContext context = new SCMCloudPFlowContext();
		// ---- 判断 共享工作流是否启动START----------------
		String[] actionNames = NCLocator.getInstance().lookup(ISSCService.class).isStartSSCWorkFlow(vos,
				BusiUnitTypeEnum.PU);
		context.setActionNames(actionNames);
		context.setActionName(actionNames[0]);
		// ---- 判断 共享工作流是否启动END----------------
		context.setBillType("Z2");
		context.setBillVos(vos);
		// 执行提交动作脚本
		SCMScriptResultDTO result = NCLocator.getInstance().lookup(IBatchRunScriptService.class).runBacth(context,
				AggCtPuVO.class, CommonConst.actionNames);
		return result;
	}

	@Override
	public SCMScriptResultDTO uncommit(AggCtPuVO[] vos) throws BusinessException {
		SCMCloudPFlowContext context = new SCMCloudPFlowContext();
		// ---- 判断 共享工作流是否启动START----------------
		String[] actionNames = NCLocator.getInstance().lookup(ISSCService.class).isEndSSCWorkFlow(vos,
				BusiUnitTypeEnum.PU);
		for (int i = 0; i < actionNames.length; i++) {
			if (actionNames[i].equals("UNSAVE")) {
				actionNames[i] = "UNSAVEBILL";
			}
		}
		context.setActionNames(actionNames);
		context.setActionName(actionNames[0]);
		// ---- 判断 共享工作流是否启动END----------------
		context.setBillType("Z2");
		context.setBillVos(vos);
		// 执行提交动作脚本
		SCMScriptResultDTO result = NCLocator.getInstance().lookup(IBatchRunScriptService.class).runBacth(context,
				AggCtPuVO.class, CommonConst.actionNames);
		return result;
	}

	// 暂时先写到Impl里，后期若有其他查询，则统一新建一个AppImpl里
	@Override
	public String queryLatestId(String id) throws BusinessException {
		StringBuffer whereSql = new StringBuffer();

		whereSql.append(" and " + CtPuVO.PK_ORIGCT + " in (");
		whereSql.append(" ( select " + CtPuVO.PK_ORIGCT + " from ct_pu where " + CtPuVO.PK_CT_PU + " = '" + id
				+ "' and " + CtPuVO.PK_ORIGCT + " != '~' ) ");
		whereSql.append(", '" + id + "' )");
		whereSql.append(" and " + CtPuVO.BSHOWLATEST + " = ");
		whereSql.append("'" + UFBoolean.TRUE + "'");

		CtPuVO[] bills = new VOQuery<>(CtPuVO.class).query(whereSql.toString(), null);

		return bills == null || bills.length == 0 ? id : bills[0].getPk_ct_pu();
	}

}
