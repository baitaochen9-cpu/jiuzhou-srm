package nccloud.web.ct.purdaily.utils;

import java.util.HashMap;
import java.util.Map;

import nc.vo.pubapp.pattern.model.entity.bill.AbstractBill;
import nc.vo.pubapp.pattern.model.entity.bill.IBill;
import nc.vo.scmpub.page.PageQueryVO;
import nc.vo.scmpub.util.ArrayUtil;
import nccloud.dto.baseapp.querytree.dataformat.QueryTreeFormatVO;
import nccloud.dto.ct.purdaily.constance.CommonConst;
import nccloud.framework.web.ui.model.PageInfo;
import nccloud.framework.web.ui.pattern.extbillcard.ExtBillCard;
import nccloud.framework.web.ui.pattern.grid.Grid;
import nccloud.framework.web.ui.pattern.grid.GridOperator;
import nccloud.web.scmpub.pub.operator.SCMExtBillCardOperator;
import nccloud.web.scmpub.pub.utils.compare.SCMCompareUtil;

/**
 * @description 对即将返回前端的后台数据进行再处理
 * @author xiahui
 * @date 创建时间：2019-1-15 上午11:07:35
 * @version ncc1.0
 **/
public class ResultUtil {

	/**
	 * 处理分页查询结果，生成Grid
	 * 
	 * @param pageQueryVO 分页查询结果VO
	 * @param formatVO    查询条件
	 * @return
	 */
	public static Grid processPageQueryVO(PageQueryVO pageQueryVO, QueryTreeFormatVO formatVO, String pageId) {
		IBill[] bills = pageQueryVO.getCurrentPageBills();
		if (ArrayUtil.isEmpty(bills)) {
			return null;
		}

		int pageSize = Integer.valueOf(formatVO.getPageInfo().getPageSize());
		int totalSize = pageQueryVO.getPks().length;
		int size = bills.length > pageSize ? pageSize : bills.length; // 首页显示数量

		Grid grid = convertBillsToGrid(bills, size, pageId);
		grid.getModel().setAllpks(pageQueryVO.getPks());

		// 处理分页信息
		PageInfo pageInfo = new PageInfo();
		pageInfo.setTotal(totalSize);
		pageInfo.setPageSize(Integer.valueOf(pageSize));
		pageInfo.setPageIndex(0); // 每次分页查询时，分页下标回到第一页
		pageInfo.setTotalPage((getTotalPage(pageSize, totalSize)));
		grid.getModel().setPageinfo(pageInfo);
		return grid;
	}

	/**
	 * 处理批量操作返回结果
	 * 
	 * @param bills
	 * @param origBills
	 * @return
	 */
	public static Map<String, Object> processScriptResult(Object[] bills, Object[] orginalBills) {
		Map<String, Object> retMap = new HashMap<>();
		if (bills != null && bills.length > 0) {
			if (orginalBills.length == 1) {
				ExtBillCard billCard = compareBillCard(bills[0], orginalBills[0]);
				retMap.put(CommonConst.SUCCESSKEY, billCard);
			} else {
				// 列表
				Grid grid = convertBillsToGrid(bills, bills.length, CommonConst.PAGECODE_LIST);
				// 精度处理
				PrecisionUtil.setGridPrecision(grid);
				retMap.put(CommonConst.SUCCESSKEY, grid);
			}
		}
		return retMap;

	}

	/**
	 * 对卡片数据进行差异处理
	 * 
	 * @param bill
	 * @param origBill
	 * @return
	 */
	public static ExtBillCard compareBillCard(Object bill, Object origBill) {
		// 转换成前端视图
		SCMExtBillCardOperator operator = new SCMExtBillCardOperator(CommonConst.PAGECODE_CARD);
		ExtBillCard orignCard = operator.toNoTransCard(origBill);
		ExtBillCard billCard = operator.toNoTransCard(bill);
		// 精度处理
		PrecisionUtil.setExtCardPrecision(orignCard);
		PrecisionUtil.setExtCardPrecision(billCard);
		// 差异更新
		SCMCompareUtil.compareExtBillCardByFields(orignCard, billCard, CommonConst.bodyPkFields);
		operator.translate(billCard);
		return billCard;
	}

	/**
	 * 
	 * @param pageSize  页条数
	 * @param totalSize 总条数
	 * @return 总页数
	 */
	private static int getTotalPage(int pageSize, int totalSize) {
		int totalPage = 0;
		if (totalSize % pageSize == 0) {
			totalPage = totalSize / pageSize;
		} else {
			totalPage = totalSize / pageSize + 1;
		}
		return totalPage;
	}

	/**
	 * 生成列表Grid
	 * 
	 * @param bills  待处理VO
	 * @param size   处理大小
	 * @param pageId 列表页面ID
	 * @return
	 */
	private static Grid convertBillsToGrid(Object[] bills, int size, String pageId) {
		Object[] heads = new Object[size];
		for (int i = 0; i < size; i++) {
			heads[i] = ((AbstractBill) bills[i]).getParent();
		}
		GridOperator operator = new GridOperator(pageId);
		Grid grid = operator.toGrid(heads);
		// 精度处理
		PrecisionUtil.setGridPrecision(grid);
		return grid;
	}
}
