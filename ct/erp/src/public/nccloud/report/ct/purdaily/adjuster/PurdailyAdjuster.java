package nccloud.report.ct.purdaily.adjuster;

import java.util.ArrayList;
import java.util.List;

import com.ufida.dataset.IContext;
import com.ufida.report.anareport.FreeReportContextKey;
import com.ufida.report.anareport.areaset.AreaContentSet;
import com.ufida.report.anareport.areaset.AreaContentSetUtil;
import com.ufida.report.anareport.areaset.AreaFieldSet;
import com.ufida.report.anareport.model.AbsAnaReportModel;
import com.ufida.report.anareport.model.AnaReportModel;

import nc.bs.ic.icreport.providerutil.ProviderUtil;
import nc.itf.iufo.freereport.extend.IAreaCondition;
import nc.itf.iufo.freereport.extend.IQueryConditionProcessor;
import nc.itf.iufo.freereport.extend.IReportAdjustorForWeb;
import nc.pub.smart.model.SmartModel;
import nc.vo.ct.entity.CtAbstractBVO;
import nc.vo.ct.entity.CtAbstractVO;
import nc.vo.ct.purdaily.entity.CtPuVO;
import nc.vo.ct.report.enumeration.CtReportGroupEnum;
import nc.vo.ic.pub.util.ValueCheckUtil;
import nc.vo.pub.query.ConditionVO;

public class PurdailyAdjuster implements IReportAdjustorForWeb{

	@Override
	public void doAreaAdjust(IContext context, String areaPK,
			IAreaCondition areaCond, AbsAnaReportModel reportModel) {
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
	/**
	 * 如果汇总选择了汇总口径则设置隐藏字段
	 * 
	 * @param conditions
	 * @return
	 */
	private String[] getHidenFields(ConditionVO[] conditions) {
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

		    /** 物料 **/
		    if (CtReportGroupEnum.MAR.value().equals(code)) {
		      /**
		       * 隐藏字段：合同类型、合同编码、合同名称、委外、合同签订日期、供应商、人员、部门、合同状态、本币无税金额、本币税额、本币价税合计、税率、创建人、
		       * 创建时间
		       */
		      return new String[] {
		        CtAbstractVO.VTRANTYPECODE, CtAbstractVO.VBILLCODE,
		        CtAbstractVO.CTNAME, CtAbstractVO.BSC, CtAbstractVO.SUBSCRIBEDATE,
		        CtPuVO.CVENDORID, CtAbstractVO.PERSONNELID, CtAbstractVO.DEPID_V,
		        CtAbstractVO.FSTATUSFLAG, CtAbstractBVO.NMNY, CtAbstractBVO.NTAX,
		        CtAbstractBVO.NTAXMNY, CtAbstractBVO.NTAXRATE, CtAbstractVO.CREATOR,
		        CtAbstractVO.CREATIONTIME
		      };
		    }
		    /** 物料+部门 **/
		    else if (CtReportGroupEnum.MARDPT.value().equals(code)) {
		      /**
		       * 隐藏字段：合同类型、合同编码、合同名称、委外、合同签订日期、供应商、人员、合同状态、本币无税金额、本币税额、本币价税合计、税率、创建人、
		       * 创建时间
		       */
		      return new String[] {
		        CtAbstractVO.VTRANTYPECODE, CtAbstractVO.VBILLCODE,
		        CtAbstractVO.CTNAME, CtAbstractVO.BSC, CtAbstractVO.SUBSCRIBEDATE,
		        CtPuVO.CVENDORID, CtAbstractVO.PERSONNELID, CtAbstractVO.FSTATUSFLAG,
		        CtAbstractBVO.NMNY, CtAbstractBVO.NTAX, CtAbstractBVO.NTAXMNY,
		        CtAbstractBVO.NTAXRATE, CtAbstractVO.CREATOR, CtAbstractVO.CREATIONTIME
		      };
		    }
		    /** 物料+供应商 **/
		    else if (CtReportGroupEnum.MARSUP.value().equals(code)) {
		      /**
		       * 隐藏字段：合同类型、合同编码、合同名称、委外、合同签订日期、人员、部门、合同状态、本币无税金额、本币税额、本币价税合计、税率、创建人、创建时间
		       */
		      return new String[] {
		        CtAbstractVO.VTRANTYPECODE, CtAbstractVO.VBILLCODE,
		        CtAbstractVO.CTNAME, CtAbstractVO.BSC, CtAbstractVO.SUBSCRIBEDATE,
		        CtAbstractVO.PERSONNELID, CtAbstractVO.DEPID_V,
		        CtAbstractVO.FSTATUSFLAG, CtAbstractBVO.NMNY, CtAbstractBVO.NTAX,
		        CtAbstractBVO.NTAXMNY, CtAbstractBVO.NTAXRATE, CtAbstractVO.CREATOR,
		        CtAbstractVO.CREATIONTIME
		      };
		    }
		    /** 物料+供应商+部门 **/
		    else if (CtReportGroupEnum.MARSUPDPT.value()
		        .equals(code)) {
		      /**
		       * 隐藏字段：合同类型、合同编码、合同名称、委外、合同签订日期、人员、合同状态、本币无税金额、本币税额、本币价税合计、税率、创建人、创建时间
		       */
		      return new String[] {
		        CtAbstractVO.VTRANTYPECODE, CtAbstractVO.VBILLCODE,
		        CtAbstractVO.CTNAME, CtAbstractVO.BSC, CtAbstractVO.SUBSCRIBEDATE,
		        CtAbstractVO.PERSONNELID, CtAbstractVO.FSTATUSFLAG, CtAbstractBVO.NMNY,
		        CtAbstractBVO.NTAX, CtAbstractBVO.NTAXMNY, CtAbstractBVO.NTAXRATE,
		        CtAbstractVO.CREATOR, CtAbstractVO.CREATIONTIME
		      };
		    }
		
		}
			
		return null;
	}

	@Override
	public void doReportAdjust(IContext context, AnaReportModel reportModel) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public IQueryConditionProcessor getConditionProcessor(IContext context,
			AnaReportModel reportModel) {
		// TODO Auto-generated method stub
		return new PurdailyConditionProcessor();
	}

}
