package nccloud.pubimpl.scmpub.pub.service;

import nc.impl.pubapp.pattern.data.bill.BillQuery;
import nc.impl.pubapp.pattern.data.vo.VOQuery;
import nc.vo.ml.MultiLangContext;
import nc.vo.pub.BusinessException;
import nc.vo.pub.ISuperVO;
import nc.vo.pubapp.pattern.model.entity.bill.IBill;
import nccloud.pubitf.scmpub.pub.service.ISCMPubQueryService;

/**
 * @description SCMPUB 公共查询
 * @author guozhq
 * @date 2018-8-8 下午7:27:06
 * @version ncc1.0
 */
public class SCMPubQueryServiceImpl implements ISCMPubQueryService {

	@Override
	public <T extends IBill> T[] billquery(Class<T> clazz, String[] ids) throws BusinessException {
		BillQuery<T> query = new BillQuery<T>(clazz);
		return query.query(ids);
	}

	@Override
	public Integer getCurrentLangSeq() throws BusinessException {
		return MultiLangContext.getInstance().getCurrentLangSeq();
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public <T extends ISuperVO> T[] queryByIDs(Class<T> clazz, String[] ids)  {
		return (T[])new VOQuery(clazz).query(ids);
	}
}
