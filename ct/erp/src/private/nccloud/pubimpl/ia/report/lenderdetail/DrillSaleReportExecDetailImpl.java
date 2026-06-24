package nccloud.pubimpl.ia.report.lenderdetail;

import java.util.Map;

import nc.bs.framework.common.NCLocator;
import nc.pub.report.util.ReportDrillBillUtil;
import nc.vo.ct.saledaily.entity.CtSaleBVO;
import nc.vo.ct.saledaily.entity.CtSaleVO;
import nc.vo.pub.BusinessException;
import nccloud.dto.baseapp.querytree.dataformat.QueryTreeFormatVO;
import nccloud.pubitf.ct.report.service.IDrillSaleReportExec;
import nccloud.pubitf.platform.workbench.IAppPage;

import com.ufida.report.anareport.base.FreeReportDrillParam;

public class DrillSaleReportExecDetailImpl implements IDrillSaleReportExec {
	private String appCode = "400612014";
	private String pageCode = "400612014_page";

	@Override
	public String getUrlBuyConditions(QueryTreeFormatVO conditionTree,
			Map serializedObjMap) throws BusinessException {
		String opents = (String) serializedObjMap.get("opents");
		// 调用平台的构建param方法
		ReportDrillBillUtil utils = new ReportDrillBillUtil();
		FreeReportDrillParam drillParam = utils.drillBill(serializedObjMap,
				conditionTree, serializedObjMap, opents);
		String ctSaleVbillCode = (String) drillParam.getTraceDatas()[0]
				.getValue(CtSaleVO.PK_CT_SALE);
		String pk_ct_sale_b = (String) drillParam.getTraceDatas()[0]
				.getValue(CtSaleBVO.PK_CT_SALE_B);
		String material = (String) drillParam.getTraceDatas()[0]
				.getValue(CtSaleBVO.PK_MATERIAL);
		String marbasclass = (String) drillParam.getTraceDatas()[0]
				.getValue(CtSaleBVO.PK_MARBASCLASS);
		String rowno = (String) drillParam.getTraceDatas()[0]
				.getValue(CtSaleBVO.CROWNO);
		// getClass()Data(DetailLedgerVO.CDETAILLEDGERID);
		// if (cdetailledgerid == null) {
		// String msg = "参数为空";
		// ExceptionUtils.wrapBusinessException(msg);
		// }

		IAppPage service = NCLocator.getInstance().lookup(IAppPage.class);
		String url = service.queryPageUrl(appCode, pageCode);
		url = url.replace("/nccloud/resources", "");
		url = url + "?id=" + ctSaleVbillCode + "&material=" + material
				+ "&marbasclass=" + marbasclass+ "&bid=" + pk_ct_sale_b+ "&rowno=" + rowno;
		return url;
	}

	@Override
	public String getViewVOByRow(QueryTreeFormatVO conditionTree,
			Map serializedObjMap) throws BusinessException {
		String opents = (String) serializedObjMap.get("opents");
		// 调用平台的构建param方法
		ReportDrillBillUtil utils = new ReportDrillBillUtil();
		FreeReportDrillParam drillParam = utils.drillBill(serializedObjMap,
				conditionTree, serializedObjMap, opents);
		String rowData = (String) drillParam.getTraceDatas()[0].toString();
		return rowData;
	}

}
