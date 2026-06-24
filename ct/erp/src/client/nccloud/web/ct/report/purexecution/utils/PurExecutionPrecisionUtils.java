package nccloud.web.ct.report.purexecution.utils;

import nc.vo.ct.purdaily.entity.CtPuBVO;
import nc.vo.ct.purdaily.entity.CtPuVO;
import nc.vo.pu.m21.entity.OrderItemVO;
import nccloud.framework.web.convert.precision.PositionType;
import nccloud.framework.web.ui.pattern.billcard.BillCard;
import nccloud.framework.web.ui.pattern.grid.Grid;
import nccloud.web.scmpub.pub.utils.scale.SCMBillCardPrecisionOperator;
import nccloud.web.scmpub.pub.utils.scale.SCMGridPrecisionOperator;

/**
 * 采购合同执行查询联查明细页面精度处理
 * 
 * @author yangls7
 * @date 2019年8月5日
 * @version ncc1909
 */
public class PurExecutionPrecisionUtils {

	private static final String[] bodyNums = { OrderItemVO.NNUM,
			OrderItemVO.NACCUMARRVNUM, OrderItemVO.NACCUMDEVNUM, OrderItemVO.NBACKSTORENUM,
			OrderItemVO.NACCUMSTORENUM, OrderItemVO.NACCUMINVOICENUM,OrderItemVO.NBACKARRVNUM};

	/**
	 * 处理子表精度
	 */
	public static void dealGridPrecision(Grid grid) {
		SCMGridPrecisionOperator scale = new SCMGridPrecisionOperator(grid);
		//主数量
		scale.addNumPrecision(bodyNums, OrderItemVO.CUNITID);
		//金额
		scale.addCurrencyMnyPrecision(new String[]{OrderItemVO.NORIGTAXMNY}, OrderItemVO.CORIGCURRENCYID);
		scale.processPrecision();
	}
	/**
	 * 处理表头合同信息部分精度
	 * @param card
	 */
	public static void dealHeadPrecision(BillCard card) {
		SCMBillCardPrecisionOperator scale = new SCMBillCardPrecisionOperator(card);
		//主数量
		scale.addNumPrecision(new String[] {CtPuBVO.NNUM}, PositionType.Body, CtPuBVO.CUNITID, PositionType.Body);
		//单价
		scale.addCurrencyPricePrecision(new String[] {CtPuBVO.NQTORIGTAXPRICE}, PositionType.Body, CtPuVO.CORIGCURRENCYID, PositionType.Head);
		//金额
		scale.addCurrencyMnyPrecision(new String[] { CtPuBVO.NORIGTAXMNY }, PositionType.Body, CtPuVO.CORIGCURRENCYID, PositionType.Head);
		scale.addCurrencyMnyPrecision(new String[] { CtPuVO.NORIGPSHAMOUNT }, PositionType.Head, CtPuVO.CORIGCURRENCYID, PositionType.Head);
		scale.processPrecision();
	}
}
