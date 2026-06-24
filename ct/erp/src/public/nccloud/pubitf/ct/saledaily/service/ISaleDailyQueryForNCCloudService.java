package nccloud.pubitf.ct.saledaily.service;

import nc.vo.ct.saledaily.entity.AggCtSaleVO;
import nc.vo.so.salequotation.entity.AggSalequotationHVO;
import nccloud.dto.ct.pub.transfer.TransferInfo;

/**
 * @description 销售合同
 * @author wangshrc
 * @date 2019年3月1日 下午4:25:28
 * @version ncc1.0
 */
public interface ISaleDailyQueryForNCCloudService {
	public AggCtSaleVO setAddLineInfo(AggCtSaleVO vo) throws Exception;

	public AggCtSaleVO[] transToSaleDaily(TransferInfo[] info)
			throws Exception;
}
