package nccloud.web.ct.price.action;

import nc.itf.ct.price.ICtPriceMaintain;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.ct.price.entity.AggCtPriceVO;
import nc.vo.ct.price.entity.CtPriceHeaderVO;
import nc.vo.pub.BusinessException;
import nccloud.dto.baseapp.querytree.dataformat.QueryTreeFormatVO;
import nccloud.framework.core.exception.ExceptionUtils;
import nccloud.framework.core.json.IJson;
import nccloud.framework.service.ServiceLocator;
import nccloud.framework.web.action.itf.ICommonAction;
import nccloud.framework.web.container.IRequest;
import nccloud.framework.web.json.JsonFactory;
import nccloud.framework.web.ui.model.PageInfo;
import nccloud.framework.web.ui.pattern.grid.Grid;
import nccloud.framework.web.ui.pattern.grid.GridOperator;
import nccloud.pubitf.platform.query.INCCloudQueryService;

/**
 * 
 * @description 合同价格信息表查询
 * @author zhaoypm
 * @time 2019-3-26 上午11:20:25
 * @since ncc1.0
 */
public class QueryAction implements ICommonAction {
	private String[] headPks = null;

	@Override
	public Object doAction(IRequest request) {
		String read = request.read();
		IJson factory = JsonFactory.create();
		QueryTreeFormatVO queryInfo = factory.fromJson(read,
				QueryTreeFormatVO.class);
		IQueryScheme queryScheme = ServiceLocator.find(
				INCCloudQueryService.class).convertCondition(queryInfo);
		ICtPriceMaintain service = ServiceLocator.find(ICtPriceMaintain.class);
		try {
			AggCtPriceVO[] vos = service.query(queryScheme);
			CtPriceHeaderVO[] heads = getHeads(vos);
			// 没有数据直接返回
			if (null == heads || heads.length == 0) {
				return null;
			}
			
			int pageSize = Integer.parseInt(queryInfo.getPageInfo().getPageSize());
			String pageCode = queryInfo.getPageCode();
			Grid grid = this.covert(heads, pageSize, pageCode);
			
			// 分页处理
			PageInfo pageInfo = new PageInfo();
			pageInfo.setTotal(heads.length);
			pageInfo.setPageSize(pageSize);
			pageInfo.setPageIndex(Integer.valueOf(queryInfo.getPageInfo().getPageIndex()));
			pageInfo.setTotalPage(this.getTotalPage(pageInfo, heads.length));
			grid.getModel().setPageinfo(pageInfo);
			
			return grid;
		} catch (BusinessException e) {
			ExceptionUtils.wrapException(e);

		}
		return null;
	}

	private CtPriceHeaderVO[] getHeads(AggCtPriceVO[] aggVOs) {
		headPks = new String[aggVOs.length];
		CtPriceHeaderVO[] heads = new CtPriceHeaderVO[aggVOs.length];
		for (int i = 0; i < aggVOs.length; i++) {
			heads[i] = (CtPriceHeaderVO) aggVOs[i].getParent();
			headPks[i] = heads[i].getPk_ct_price();
		}
		return heads;
	}

	private Grid covert(CtPriceHeaderVO[] bills, Integer pageSize,
			String pageCode) {
		if (this.headPks == null || this.headPks.length == 0) {
			return null;
		}
		if (bills == null || bills.length == 0) {
			return null;
		}

		int size = bills.length > pageSize ? pageSize : bills.length;
		Object[] heads = new Object[size];
		for (int i = 0; i < size; i++) {
			heads[i] = bills[i];
		}
		GridOperator operator = new GridOperator(pageCode);
		Grid grid = operator.toGrid(heads);

		grid.getModel().setAllpks(headPks);
		return grid;
	}

	private int getTotalPage(PageInfo pageInfo, int length) {
		int size = pageInfo.getPageSize();
		int total = 0;
		if (length % size == 0) {
			total = length / size;
		} else {
			total = length / size + 1;
		}
		return total;
	}

}
