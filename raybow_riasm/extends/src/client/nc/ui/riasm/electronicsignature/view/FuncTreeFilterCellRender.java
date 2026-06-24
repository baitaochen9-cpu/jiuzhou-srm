package nc.ui.riasm.electronicsignature.view;

import nc.ui.pub.beans.tree.FilterTreeCellRendererUtil;
import nc.ui.sf.functree.FuncTreeCellRender;

public class FuncTreeFilterCellRender extends FuncTreeCellRender {

	/**
	 * 
	 */
	private static final long serialVersionUID = 126305206443138110L;

	@Override
	public void setFiltertext(String filtertext) {
		// 初始化工具类
		FilterTreeCellRendererUtil TreeCellRendererUtil = FilterTreeCellRendererUtil
				.getInstance();
		TreeCellRendererUtil.setFilter(new FuncRegFilterByText());// 设置过滤功能类
		TreeCellRendererUtil.setFiltertext(filtertext);// 设置过滤文本
	}
}
