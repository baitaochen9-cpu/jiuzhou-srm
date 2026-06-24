package nc.bs.so.salepacklist.ace.bp.rule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.logging.Log;
import nc.bs.trade.business.HYPubBO;
import nc.impl.pubapp.env.BSContext;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.uif.pub.exception.UifException;
import nc.vo.am.common.util.ExceptionUtils;
import nc.vo.bd.defdoc.DefdocVO;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pub.lang.UFDouble;
import nc.vo.so.salepacklist.AggSalePackListHVO;
import nc.vo.so.salepacklist.SalePackListBVO;
import nc.vo.so.salepacklist.SalePackListHVO;
import nc.vo.trade.checkrule.VOChecker;
import nc.vo.trade.voutils.SafeCompute;
import nc.vo.uap.busibean.exception.BusiBeanException;

public class FillNewDefaultRule implements IRule<AggSalePackListHVO> {
	public void process(AggSalePackListHVO[] vos) {
		try {
			for (AggSalePackListHVO vo : vos) {
				setHeadDefault(vo);
			}
		} catch (Exception ex) {
			Log.getInstance(this.getClass()).error(ex);
			ExceptionUtils.asBusinessRuntimeException(ex);
		}

	}

	private void calculate(AggSalePackListHVO vo) throws UifException,
			BusiBeanException {
		UFDouble ngrosswt = new UFDouble(0.0D);
		UFDouble ntarenum = new UFDouble(0.0D);
		UFDouble nweight = new UFDouble(0.0D);
		SalePackListBVO[] bvo = (SalePackListBVO[]) vo.getChildrenVO();

		Map<String, DefdocVO> map = new HashMap<>();

		List<String> list = new ArrayList<>();
		for (SalePackListBVO dilverybvo : bvo) {
			if (dilverybvo.getNgrosswt() != null) {
				ngrosswt = ngrosswt.add(dilverybvo.getNgrosswt());
			}
			if (dilverybvo.getNtarenum() != null) {
				ntarenum = ntarenum.add(dilverybvo.getNtarenum());
			}
			if (dilverybvo.getNweight() != null) {
				nweight = nweight.add(dilverybvo.getNweight());
			}

			if (!list.contains(dilverybvo.getPalleno())) {
				list.add(dilverybvo.getPalleno());
				DefdocVO defvo = null;
				if (!map.containsKey(dilverybvo.getSpec_t())) {
					defvo = getDefdocVO(dilverybvo.getSpec_t());
				} else {
					defvo = map.get(dilverybvo.getSpec_t());
				}

				if (defvo == null) {
					throw new BusiBeanException("标准托盘规格不存在");
				}

				if (!StringUtil.isEmpty(defvo.getDef4())) {
					ngrosswt = SafeCompute.add(ngrosswt,
							new UFDouble(defvo.getDef4()));
				}

			}

		}
		vo.getParentVO().setNgrosswt(ngrosswt);
		vo.getParentVO().setNtarenum(ntarenum);
		vo.getParentVO().setNweight(nweight);
		vo.getParentVO().setDef5(Integer.toString(list.size()));
	}

	private DefdocVO getDefdocVO(String value) throws UifException {
		DefdocVO bvo = (DefdocVO) new HYPubBO().queryByPrimaryKey(
				DefdocVO.class, value);
		return bvo;
	}

	private void setHeadDefault(AggSalePackListHVO vo) throws UifException,
			BusiBeanException {
		SalePackListHVO head = vo.getParentVO();
		BSContext proxy = BSContext.getInstance();
		String groupid = proxy.getGroupID();

		if (VOChecker.isEmpty(head.getPk_group())) {
			head.setPk_group(groupid);
		}

		calculate(vo);
	}

}
