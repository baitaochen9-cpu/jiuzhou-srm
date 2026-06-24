package nccloud.report.ct.purexecution.adjuster;

import java.util.ArrayList;
import java.util.List;

import nc.bs.ic.icreport.providerutil.ProviderUtil;
import nc.itf.iufo.freereport.extend.IAreaCondition;
import nc.itf.iufo.freereport.extend.IQueryConditionProcessor;
import nc.itf.iufo.freereport.extend.IReportAdjustorForWeb;
import nc.pub.smart.model.SmartModel;
import nc.vo.ct.entity.CtAbstractBVO;
import nc.vo.ct.entity.CtAbstractVO;
import nc.vo.ct.purdaily.entity.CtPuVO;
import nc.vo.ic.pub.util.ValueCheckUtil;
import nc.vo.pub.query.ConditionVO;

import com.ufida.dataset.IContext;
import com.ufida.report.anareport.FreeReportContextKey;
import com.ufida.report.anareport.areaset.AreaContentSet;
import com.ufida.report.anareport.areaset.AreaContentSetUtil;
import com.ufida.report.anareport.areaset.AreaFieldSet;
import com.ufida.report.anareport.model.AbsAnaReportModel;
import com.ufida.report.anareport.model.AnaReportModel;

/**
 * 
 * @description 采购合同执行情况查询
 * @author zhengxinm
 * @date 2019-1-22 下午4:15:23
 * @version ncc1.0
 */

public class PurExecutionAdjuster implements IReportAdjustorForWeb {

	@Override
	public void doAreaAdjust(IContext context, String areaPK, IAreaCondition areaCond, AbsAnaReportModel reportModel) {

		SmartModel smart = reportModel.getAreaData(areaPK).getSmartModel();
		if (smart == null)
			return;
		ConditionVO[] conditions = (ConditionVO[]) context
				.getAttribute(FreeReportContextKey.KEY_REPORT_QUERYCONDITIONVOS);
		String[] hidenFields = this.getHidenFields(conditions);

		if (ValueCheckUtil.isNullORZeroLength(hidenFields)) {
			// 设置隐藏区域内容
			AreaContentSet contentSet = new AreaContentSet();
			contentSet.setAreaPk(areaPK);
			contentSet.setSmartModelDefID(smart.getId());
			contentSet.setDetailFldNames(null);

			AreaContentSetUtil.resetExCellByHideFields(contentSet, true, reportModel);
			return;
		}
		List<AreaFieldSet> AreaFieldList = new ArrayList<AreaFieldSet>();
		if (!ValueCheckUtil.isNullORZeroLength(hidenFields))
			for (String field : hidenFields) {
				AreaFieldList.add(new AreaFieldSet(ProviderUtil.buildField(field)));
			}
		// 设置隐藏区域内容
		AreaContentSet contentSet = new AreaContentSet();
		contentSet.setAreaPk(areaPK);
		contentSet.setSmartModelDefID(smart.getId());
		contentSet.setDetailFldNames(AreaFieldList.toArray(new AreaFieldSet[0]));
		AreaContentSetUtil.resetExCellByHideFields(contentSet, true, reportModel);
	}

	/**
	 * 如果汇总选择了汇总口径则设置隐藏字段
	 * 
	 * @param conditions
	 * @return
	 */
	private String[] getHidenFields(ConditionVO[] conditions) {
		boolean flag = false;
		String[] fields = new String[] { CtAbstractVO.VTRANTYPECODE, CtAbstractVO.CTNAME, CtPuVO.CVENDORID,
				CtAbstractVO.PERSONNELID, CtAbstractVO.DEPID_V, CtAbstractVO.BSC, CtAbstractBVO.VCHANGERATE };

		for (ConditionVO condition : conditions) {
			if ("groupcondition".equals(condition.getFieldCode())) {
				flag = true;
				break;
			}
		}
		if (flag)
			return fields;
		return null;
	}

	@Override
	public void doReportAdjust(IContext context, AnaReportModel reportModel) {
		// TODO Auto-generated method stub
	}

	@Override
	public IQueryConditionProcessor getConditionProcessor(IContext context, AnaReportModel reportModel) {
		return new PurExecutionConditionProcessor();
	}

}
