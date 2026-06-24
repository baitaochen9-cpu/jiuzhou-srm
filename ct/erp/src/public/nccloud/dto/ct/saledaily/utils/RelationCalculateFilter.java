package nccloud.dto.ct.saledaily.utils;

import java.util.ArrayList;
import java.util.List;

import nc.vo.ct.entity.CtAbstractBVO;
import nc.vo.ct.saledaily.entity.AggCtSaleVO;
import nc.vo.ct.saledaily.entity.CtSaleBVO;

public class RelationCalculateFilter {

	public static String[] relationNumPriceMnyField = new String[] {
			CtAbstractBVO.NASTNUM, CtAbstractBVO.NQTUNITNUM,
			CtAbstractBVO.NNUM, CtAbstractBVO.NGLOBALMNY,
			CtAbstractBVO.NGLOBALTAXMNY, CtAbstractBVO.NGPRICE,
			CtAbstractBVO.NGROUPMNY, CtAbstractBVO.NGROUPTAXMNY,
			CtAbstractBVO.NGTAXPRICE, CtAbstractBVO.NMNY,
			CtAbstractBVO.NORIGMNY, CtAbstractBVO.NORIGPRICE,
			CtAbstractBVO.NORIGTAXMNY, CtAbstractBVO.NORIGTAXPRICE,
			CtAbstractBVO.NQTORIGPRICE, CtAbstractBVO.NQTORIGTAXPRICE,
			CtAbstractBVO.NQTPRICE, CtAbstractBVO.NQTTAXPRICE,
			CtAbstractBVO.NTAXMNY, };

	public static int[] filterNoNeedRelationRows(AggCtSaleVO billvo, int[] rows) {
		List<Integer> filtRows = new ArrayList<Integer>();
		CtSaleBVO[] bvos = billvo.getCtSaleBVO();
		for (int row : rows) {
			for (String field : RelationCalculateFilter.relationNumPriceMnyField) {
				if (bvos[row].getAttributeValue(field) != null) {
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

	public static boolean isNeedRelationRow(AggCtSaleVO billvo, int row) {
		CtSaleBVO[] bvos = billvo.getCtSaleBVO();
		for (String field : RelationCalculateFilter.relationNumPriceMnyField) {
			if (bvos[row].getAttributeValue(field) != null) {
				return true;
			}
		}
		return false;
	}

}
