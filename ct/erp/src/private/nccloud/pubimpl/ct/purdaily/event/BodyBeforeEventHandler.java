package nccloud.pubimpl.ct.purdaily.event;

import nc.vo.ct.entity.CtAbstractBVO;
import nccloud.dto.scmpub.pub.event.rule.AbstractBeforeHandler;
import nccloud.dto.scmpub.pub.event.rule.IBeforeRule;
import nccloud.dto.ct.pub.event.body.MarAsstEditHandler;
import nccloud.dto.ct.pub.util.marasst.MarAsstEditUtils;
import nccloud.pubimpl.ct.purdaily.event.before.body.CastunitOrCqtunitidBeforeRule;
import nccloud.pubimpl.ct.purdaily.event.before.body.MaterialAndMarbasclassBeforeRule;
import nccloud.pubimpl.ct.purdaily.event.before.body.NqtunitrateBeforeRule;
import nccloud.pubimpl.ct.purdaily.event.before.body.VChangeRateBeforeRule;

/**
 * @description 表体编辑前事件
 * @author xiahui
 * @date 创建时间：2019-1-17 下午4:19:52
 * @version ncc1.0
 * @ref nc.ui.ct.purdaily.editor.before.PuBodyBeforeEventHandler
 **/
public class BodyBeforeEventHandler extends AbstractBeforeHandler {

	@Override
	protected IBeforeRule getBeforeRule(String key) {
		IBeforeRule rule = null;

		// 物料基本-物料 | 物料基本-物料基本分类
		if (CtAbstractBVO.PK_MATERIAL.equals(key) || CtAbstractBVO.PK_MARBASCLASS.equals(key)) {
			rule = new MaterialAndMarbasclassBeforeRule(key);
		}
		// 报价单位换算率
		else if (CtAbstractBVO.VQTUNITRATE.equals(key)) {
			rule = new NqtunitrateBeforeRule();
		}
		// 换算率
		else if (CtAbstractBVO.VCHANGERATE.equals(key)) {
			rule = new VChangeRateBeforeRule();
		}
		// 单位 | 报价单位
		else if (CtAbstractBVO.CASTUNITID.equals(key) || CtAbstractBVO.CQTUNITID.equals(key)) {
			rule = new CastunitOrCqtunitidBeforeRule();
		}
		// 标准辅助属性处理
		else if (MarAsstEditUtils.isFree(key)) {
			rule = new MarAsstEditHandler(key);
		}

		return rule;
	}

}
