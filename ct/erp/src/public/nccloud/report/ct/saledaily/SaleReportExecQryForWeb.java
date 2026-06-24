package nccloud.report.ct.saledaily;

import nc.bs.ic.icreport.providerutil.ProviderUtil;
import nc.itf.iufo.freereport.extend.IAreaCondition;
import nc.itf.iufo.freereport.extend.IQueryConditionProcessor;
import nc.itf.iufo.freereport.extend.IReportAdjustorForWeb;
import nc.pub.smart.model.SmartModel;
import nc.vo.ct.entity.CtAbstractBVO;
import nc.vo.ct.entity.CtAbstractVO;
import nc.vo.ct.report.enumeration.CtReportExecGroupEnum;
import nc.vo.ct.report.queryinfo.saledaily.CtSaleExecQryInfoPara;
import nc.vo.ct.saledaily.entity.CtSaleVO;
import nc.vo.ic.pub.util.ValueCheckUtil;
import nc.vo.pub.query.ConditionVO;

import java.util.ArrayList;
import java.util.List;

import com.ufida.dataset.IContext;
import com.ufida.report.anareport.FreeReportContextKey;
import com.ufida.report.anareport.areaset.AreaContentSet;
import com.ufida.report.anareport.areaset.AreaContentSetUtil;
import com.ufida.report.anareport.areaset.AreaFieldSet;
import com.ufida.report.anareport.model.AbsAnaReportModel;
import com.ufida.report.anareport.model.AnaReportModel;

/**
 * @description 销售合同执行情况查询报表
 * @author cuijun
 * @date 2018-5-24 上午10:04:40
 * @version ncc1.0
 */
public class SaleReportExecQryForWeb implements IReportAdjustorForWeb {

	@Override
	public IQueryConditionProcessor getConditionProcessor(IContext context,
			AnaReportModel reportModel) {
		return new SaleReportExecQryProcessor();
	}

	@Override
	public void doAreaAdjust(IContext context, String areaPK,IAreaCondition areaCond, AbsAnaReportModel reportModel) {
		SmartModel smart = reportModel.getAreaData(areaPK).getSmartModel();
		if (smart == null)
			return;
		ConditionVO[] conditions = (ConditionVO[]) context
				.getAttribute(FreeReportContextKey.KEY_REPORT_QUERYCONDITIONVOS);
		String[] hidenFields = this.getHideKeys(conditions);

		if (ValueCheckUtil.isNullORZeroLength(hidenFields)) {
			// 设置隐藏区域内容
			AreaContentSet contentSet = new AreaContentSet();
			contentSet.setAreaPk(areaPK);
			contentSet.setSmartModelDefID(smart.getId());
			contentSet.setDetailFldNames(null);

			AreaContentSetUtil.resetExCellByHideFields(contentSet, true,
					reportModel);
			return;
		}
		List<AreaFieldSet> AreaFieldList = new ArrayList<AreaFieldSet>();
		if (!ValueCheckUtil.isNullORZeroLength(hidenFields))
			for (String field : hidenFields) {
				AreaFieldList.add(new AreaFieldSet(ProviderUtil
						.buildField(field)));
			}
		// 设置隐藏区域内容
		AreaContentSet contentSet = new AreaContentSet();
		contentSet.setAreaPk(areaPK);
		contentSet.setSmartModelDefID(smart.getId());
		contentSet
				.setDetailFldNames(AreaFieldList.toArray(new AreaFieldSet[0]));
		AreaContentSetUtil.resetExCellByHideFields(contentSet, true,
				reportModel);

	}

	@Override
	public void doReportAdjust(IContext context, AnaReportModel reportModel) {
		// TODO Auto-generated method stub

	}
	public String[] getHideKeys(ConditionVO[] conditions) {
		boolean flag = false;
		String code = "";
		for (ConditionVO condition : conditions) {
			if ("groupcondition".equals(condition.getFieldCode())) {
				flag = true;
				code = condition.getValue();
				break;
			}
		}
		if (flag) {
	    /** 合同编码+行号+物料 **/
	    if (CtReportExecGroupEnum.CODENOMAR.value().equals(code)) {
	      /**
	       * 隐藏字段：合同类型,合同名称,供应商,人员,部门,换算率、订单号、订单日期、订单物料编码、订单物料名称
	       */
	      return new String[] {
	        CtAbstractVO.VTRANTYPECODE, CtAbstractVO.CTNAME, CtSaleVO.PK_CUSTOMER, CtAbstractVO.PERSONNELID, CtAbstractVO.DEPID_V,
	        CtAbstractBVO.VCHANGERATE, CtSaleExecQryInfoPara.DBILLDATE, CtSaleExecQryInfoPara.VBILLCODE1, CtSaleExecQryInfoPara.CMATERIALVID
	      };
	    }
	    /**
	     * 隐藏字段：币种
	     */
	    return new String[] {
	    // CtAbstractVO.CORIGCURRENCYID
	    };
		}
		return null;
	  }

}
