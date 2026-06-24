package nccloud.dto.ct.pub.utils;

import java.util.ArrayList;
import java.util.List;

import nc.vo.ct.entity.CtAbstractBVO;

/**
 * @description
 * @author xiahui
 * @date 创建时间：2019-1-21 下午4:37:42
 * @version ncc1.0
 **/
public class RelationCalculateFilter {

	public static String[] relationNumPriceMnyField = new String[] { CtAbstractBVO.NASTNUM, CtAbstractBVO.NQTUNITNUM,
			CtAbstractBVO.NNUM, CtAbstractBVO.NGLOBALMNY, CtAbstractBVO.NGLOBALTAXMNY, CtAbstractBVO.NGPRICE,
			CtAbstractBVO.NGROUPMNY, CtAbstractBVO.NGROUPTAXMNY, CtAbstractBVO.NGTAXPRICE, CtAbstractBVO.NMNY,
			CtAbstractBVO.NORIGMNY, CtAbstractBVO.NORIGPRICE, CtAbstractBVO.NORIGTAXMNY, CtAbstractBVO.NORIGTAXPRICE,
			CtAbstractBVO.NQTORIGPRICE, CtAbstractBVO.NQTORIGTAXPRICE, CtAbstractBVO.NQTPRICE, CtAbstractBVO.NQTTAXPRICE,
			CtAbstractBVO.NTAXMNY, };

	public static int[] filterNoNeedRelationRows(ExtBillUtil util, int[] rows) {
		List<Integer> filtRows = new ArrayList<Integer>();
		for (int row : rows) {
			for (String field : RelationCalculateFilter.relationNumPriceMnyField) {
				if (util.getBodyUFDoubleValue(row, field) != null) {
					filtRows.add(Integer.valueOf(row));
				}
			}
		}
		if (filtRows.size() > 0) {
			int[] retRows = new int[filtRows.size()];
			Integer[] fr = filtRows.toArray(new Integer[filtRows.size()]);
			for (int i = 0; i < filtRows.size(); i++) {
				retRows[i] = fr[i].intValue();
			}
			return retRows;
		}
		return null;
	}

	public static boolean isNeedRelationRow(ExtBillUtil util, int row) {
		for (String field : RelationCalculateFilter.relationNumPriceMnyField) {
			if (util.getBodyUFDoubleValue(row, field) != null) {
				return true;
			}
		}
		return false;
	}
}
