package nccloud.pubimpl.ct.price.service;

import nc.impl.pubapp.pattern.data.vo.VOQuery;
import nc.vo.ct.price.entity.AggCtPriceVO;
import nc.vo.ct.price.entity.CtPriceBodyVO;
import nc.vo.ct.price.entity.CtPriceHeaderVO;
import nc.vo.pub.ISuperVO;
import nc.vo.pubapp.pattern.pub.Constructor;
import nc.vo.pubapp.pattern.pub.SqlBuilder;
import nccloud.pubitf.ct.price.service.ICtPriceQueryRecentlyService;

public class CtPriceQueryRecentlyServiceImpl implements ICtPriceQueryRecentlyService {

	@Override
	public AggCtPriceVO queryRecentlyBillByPkOid(String pk_oid) {
	    SqlBuilder whereSql = new SqlBuilder();
	    whereSql.append("where ");
	    whereSql.append("pk_oid", pk_oid);
	    whereSql.append(" and ");
	    whereSql.append("blatest","Y");
		ISuperVO[] parents = new VOQuery<CtPriceHeaderVO>(CtPriceHeaderVO.class).queryWithWhereKeyWord(whereSql.toString(),
				"order by ct_price.vcode");
		if (null == parents || parents.length == 0) {
			return null;
		}
		int length = parents.length;
		AggCtPriceVO[] bills = Constructor.construct(AggCtPriceVO.class, length);
		for (int i = 0; i < length; i++) {
			bills[i].setParent(parents[i]);
			this.loadChild(bills[i]);
		}
		return bills[0];
	}

	private void loadChild(AggCtPriceVO bill) {
		String primaryKey = bill.getParent().getPrimaryKey();
		 SqlBuilder whereSql = new SqlBuilder();
		 whereSql.append("and ");
		 whereSql.append("PK_CT_PRICE",primaryKey);
		ISuperVO[] children = new VOQuery<CtPriceBodyVO>(CtPriceBodyVO.class).query(whereSql.toString(),
				"order by crowno");
		bill.setChildren(CtPriceBodyVO.class, children);
	}

}
