package nccloud.web.ct.purdaily.utils;

import java.util.HashMap;
import java.util.Map;

import nc.impl.pubapp.pattern.data.vo.VOQuery;
import nc.itf.uap.pf.IPfExchangeService;
import nc.vo.ct.purdaily.entity.AggCtPuVO;
import nc.vo.pp.m28.entity.PriceAuditHeaderVO;
import nc.vo.pp.m28.entity.PriceAuditItemVO;
import nc.vo.pp.m28.entity.PriceAuditVO;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pubapp.bill.CombineBill;
import nc.vo.pubapp.pattern.model.tool.BillComposite;
import nccloud.framework.core.json.IJson;
import nccloud.framework.service.ServiceLocator;
import nccloud.framework.web.container.IRequest;
import nccloud.framework.web.json.JsonFactory;
import nccloud.framework.web.processor.template.BillCardConvertProcessor;
import nccloud.web.ct.purdaily.info.PkTsParamsVO;
import nccloud.web.ct.purdaily.info.TransferData;
import nccloud.web.ct.purdaily.info.TransferInfo;

/**
 * 
 * @description 转单工具类
 * @author xiahui
 * @date 2018-9-30 上午10:11:45
 * @version ncc1.0
 */
public class Ref28TransferUtil {

	/**
	 * 获取vo转换后的vo
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public AggCtPuVO[] getAggCtPuVO(IRequest request) throws Exception {
		String read = request.read();
		IJson json = JsonFactory.create();
		TransferInfo queryInfo = json.fromJson(read, TransferInfo.class);
		PriceAuditVO[] sorceVOs = this.getAggVO(queryInfo);

		CombineBill<PriceAuditVO> cmbutil = new CombineBill<PriceAuditVO>();
		cmbutil.appendKey(PriceAuditHeaderVO.PK_PRICEAUDIT);
		// cmbutil.appendKey(TransInBodyVO.COUTSTOCKORGID);
		// cmbutil.appendKey(TransInBodyVO.COUTDEPTID);
		// cmbutil.appendKey(TransInBodyVO.COUTPSNID);
		PriceAuditVO[] comVO = cmbutil.combine(sorceVOs);

		// 转单
		IPfExchangeService service = ServiceLocator.find(IPfExchangeService.class);
		AggCtPuVO[] vos = (AggCtPuVO[]) service.runChangeDataAry("28", "Z2", comVO, null);

		return vos;
	}

	/**
	 * 获取已存在的vo
	 * 
	 * @param request
	 * @return
	 */
	public AggCtPuVO getOldVO(IRequest request) {
		TransferInfo info = getTransferInfo(request);
		BillCardConvertProcessor processor = new BillCardConvertProcessor();
		AggCtPuVO oldVO = processor.fromBillCard(info.getBill());
		return oldVO;
	}

	/**
	 * 转换请求数据
	 * 
	 * @param request
	 * @return
	 */
	public TransferInfo getTransferInfo(IRequest request) {
		String read = request.read();
		IJson json = JsonFactory.create();
		TransferInfo info = json.fromJson(read, TransferInfo.class);
		return info;
	}

	private PriceAuditVO[] getAggVO(TransferInfo queryInfo) {
		PriceAuditVO[] aggctvos = null;
		Map<String, UFDateTime> idTsHeadMap = this.getIdTsIndex(queryInfo, PriceAuditHeaderVO.PK_PRICEAUDIT);
		Map<String, UFDateTime> idTsIndex = this.getIdTsIndex(queryInfo, PriceAuditItemVO.PK_PRICEAUDIT_B);
		if (idTsHeadMap.size() == 0) {
			return aggctvos;
		}
		String[] hpks = new String[idTsHeadMap.size()];
		String[] bpks = null;
		hpks = idTsHeadMap.keySet().toArray(hpks);
		if (idTsIndex != null && idTsIndex.size() > 0) {
			bpks = new String[idTsIndex.size()];
			bpks = idTsIndex.keySet().toArray(bpks);
		}

		PriceAuditHeaderVO[] headers = new VOQuery<PriceAuditHeaderVO>(PriceAuditHeaderVO.class).query(hpks);

		PriceAuditItemVO[] items = new VOQuery<PriceAuditItemVO>(PriceAuditItemVO.class).query(bpks);

		BillComposite<PriceAuditVO> bc = new BillComposite<PriceAuditVO>(PriceAuditVO.class);
		PriceAuditVO tempVO = new PriceAuditVO();
		bc.append(tempVO.getMetaData().getParent(), headers);
		bc.append(tempVO.getMetaData().getVOMeta(PriceAuditItemVO.class), items);
		aggctvos = bc.composite();

		return aggctvos;
	}

	private Map<String, UFDateTime> getIdTsIndex(TransferInfo queryInfo, String attrcode) {
		Map<String, UFDateTime> idTsIndex = new HashMap<String, UFDateTime>();
		TransferData[] tData = queryInfo.getData();
		if (tData != null && tData.length > 0) {
			for (TransferData element : tData) {
				if (PriceAuditHeaderVO.PK_PRICEAUDIT.equalsIgnoreCase(attrcode)) {
					PkTsParamsVO head = element.getHead();
					if (head != null) {
						idTsIndex.put(head.getPk(), new UFDateTime(head.getTs()));
					}
				} else {
					PkTsParamsVO[] bodys = element.getBodys();
					for (PkTsParamsVO body : bodys) {
						idTsIndex.put(body.getPk(), new UFDateTime(body.getTs()));
					}
				}
			}
		}
		return idTsIndex;
	}

}
