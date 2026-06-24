package nccloud.pubimpl.ct.purdaily.event.after.body;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import nc.bs.framework.common.NCLocator;
import nc.itf.ct.price.IQueryForPurdaily;
import nc.vo.ct.entity.CtAbstractVO;
import nc.vo.ct.purdaily.entity.AggCtPuVO;
import nc.vo.ct.purdaily.entity.CtPuBVO;
import nc.vo.ct.purdaily.entity.CtPuVO;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nccloud.dto.ct.pub.utils.AddLineUtil;
import nccloud.dto.ct.pub.utils.ExtBillUtil;
import nccloud.dto.scmpub.pub.context.BillCardBodyEditEvent;
import nccloud.dto.scmpub.pub.event.rule.IBodyAfterRule;

import nccloud.commons.lang.ArrayUtils;
import nccloud.commons.lang.StringUtils;

/**
 * @description 采购合同“物料分类”编辑后事件
 * @author xiahui
 * @date 创建时间：2019-1-22 上午11:20:06
 * @version ncc1.0
 * @ref nc.ui.ct.purdaily.editor.after.body.PuMarBasClass
 **/
public class PuMarBasClassAfterRule implements IBodyAfterRule<AggCtPuVO> {

	@Override
	public AggCtPuVO afterEdit(AggCtPuVO billvo, BillCardBodyEditEvent event,
			@SuppressWarnings("rawtypes") Map userobject) {
		ExtBillUtil util = new ExtBillUtil(billvo);
		int row = event.getRow();
		String pk_marbasclass = (String) event.getChangrows()[0].getNewvalue();

		List<String[]> bodyCondition = new ArrayList<String[]>();
		String pk_org = util.getHeadTailStringValue(CtAbstractVO.PK_ORG);
		String cvendorid = util.getHeadTailStringValue(CtPuVO.CVENDORID);
		String corigcurrencyid = util.getHeadTailStringValue(CtAbstractVO.CORIGCURRENCYID);
		// 设置表体默认值
		AddLineUtil.setCtPuBVODefaultValue(util, new int[] { row }, pk_org);
		if (!StringUtils.isEmpty(pk_org) && !StringUtils.isEmpty(cvendorid) && !StringUtils.isEmpty(corigcurrencyid)
				&& !StringUtils.isEmpty(pk_marbasclass)) {
			bodyCondition.add(new String[] { String.valueOf(row), pk_marbasclass });
		}
		if (bodyCondition.size() > 0) {
			try {
				IQueryForPurdaily iservice = NCLocator.getInstance().lookup(IQueryForPurdaily.class);
				String[][] ctPriceInfos = null;
				ctPriceInfos = iservice.queryCtPriceByOrgSupplierCurrencyMaterbas(pk_org, cvendorid, corigcurrencyid,
						bodyCondition.toArray(new String[bodyCondition.size()][]));
				if (!ArrayUtils.isEmpty(ctPriceInfos)) {
					for (String[] info : ctPriceInfos) {
						util.setBodyValue(Integer.parseInt(info[0]), CtPuBVO.PK_CT_PRICE, info[1]);
					}
				}
			} catch (Exception e1) {
				ExceptionUtils.wrappException(e1);
			}
		}

		

		return billvo;
	}

}
