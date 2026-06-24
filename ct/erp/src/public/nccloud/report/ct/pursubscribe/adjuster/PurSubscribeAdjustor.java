package nccloud.report.ct.pursubscribe.adjuster;

import nc.itf.iufo.freereport.extend.IQueryConditionProcessor;
import nc.pub.smart.metadata.Field;
import nc.vo.ct.report.queryinfo.CtQueryInfoPara;
import nccloud.report.ic.pub.adjuster.ICRptAdjustorForWeb;

import com.ufida.dataset.IContext;
import com.ufida.report.anareport.areaset.AreaFieldSet;
import com.ufida.report.anareport.model.AnaReportModel;

/**
 * 采购合同汇总表格式调整器
 * 
 * @author yangls7
 * @date 2019-3-4
 * @version ncc1909
 */
public class PurSubscribeAdjustor extends ICRptAdjustorForWeb {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2076925487369189368L;

	/**
	 * 待隐藏字段
	 * 
	 * @return
	 */
	@Override
	protected String[] getHidenFields() {
		CtQueryInfoPara para = null;
		para = (CtQueryInfoPara) this.getContext().getAttribute(
				CtQueryInfoPara.QUERY_PARA);
		if (para == null) {
			para = (CtQueryInfoPara) this.getTranMap()
					.get(CtQueryInfoPara.QUERY_PARA);
		}

		// 隐藏字段
		String[] detailkeys = para.getHideKeys();
		int detaillen = detailkeys.length;
		AreaFieldSet[] detailinfo = new AreaFieldSet[detaillen];
		for (int i = 0; i < detaillen; i++) {
			Field field = new Field();
			field.setFldname(detailkeys[i]);
			detailinfo[i] = new AreaFieldSet(field);
		}

		return detailkeys;
	}

	@Override
	public IQueryConditionProcessor getConditionProcessor(IContext context,
			AnaReportModel reportModel) {
		// TODO Auto-generated method stub
		return new PurSubscribeConditionProcessor();
	}

}
