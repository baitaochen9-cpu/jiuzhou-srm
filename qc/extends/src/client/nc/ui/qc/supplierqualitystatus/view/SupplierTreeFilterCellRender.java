package nc.ui.qc.supplierqualitystatus.view;

import nc.ui.pub.beans.tree.FilterTreeCellRendererUtil;

public class SupplierTreeFilterCellRender extends SupplierTreeCellRender {

	/**
	 * 
	 */
	private static final long serialVersionUID = 126305206443138110L;

	@Override
	public void setFiltertext(String filtertext) {
		// 初始化工具类
		FilterTreeCellRendererUtil TreeCellRendererUtil = FilterTreeCellRendererUtil
				.getInstance();
		TreeCellRendererUtil.setFilter(new SupplierRegFilterByText());// 设置过滤功能类
		TreeCellRendererUtil.setFiltertext(filtertext);// 设置过滤文本
	}
}
