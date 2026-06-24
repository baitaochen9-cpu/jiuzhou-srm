package nc.bs.ic.m4n.insert.rule;

import nc.bs.ic.pub.base.ICRule;
import nc.bs.logging.Log;
import nc.bs.trade.business.HYPubBO;
import nc.ui.trade.business.HYPubBO_Client;
import nc.uif.pub.exception.UifException;
import nc.vo.ic.m4n.entity.TransformBodyVO;
import nc.vo.ic.m4n.entity.TransformVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.trade.voutils.SafeCompute;

public class CheckBodyNumRule extends ICRule<TransformVO> {

	@Override
	public void process(TransformVO[] vos) {

		try {
			if (vos == null || vos.length == 0) {
				return;
			}
			HYPubBO bo = new HYPubBO();
			for (TransformVO vo : vos) {
				// 单据表体校驗數量
				
				String code = null;
				try {
					code = (String)bo.findColValue("bd_defdoc", "code", " nvl(dr,0) = 0 and pk_defdoc = '"+vo.getHead().getVdef20()+"'");
				} catch (UifException e) {
					e.printStackTrace();
				}
				if ("01".equals(code)) {// 序列号拆分
					this.checkBodyBeforeNum(vo);
				}
			}
		} catch (BusinessException ex) {
			Log.getInstance(this.getClass()).error(ex);
			ExceptionUtils.wrappException(ex);
		}
	}

	public void checkBodyBeforeNum(TransformVO billvo) throws BusinessException {
		if (null == billvo) {
			return;
		}

		TransformBodyVO[] bodys = billvo.getBodys();

		if (bodys == null || bodys.length == 0)
			return;
		int count = bodys.length;

		if (count > 1) {
			TransformBodyVO bodyRowVO = bodys[0];

			// 计算第一行的总数量 然后计算当前行数量
			// 当前行数量 = 总数量减去其他行之前行的数量 按照顺序计算
			UFDouble totalnum = bodyRowVO.getNnum();
			UFDouble othertotalnum = UFDouble.ZERO_DBL;
			for (int i = 1; i < count; i++) {
				TransformBodyVO body = bodys[i];
				if (VOStatus.DELETED == body.getStatus())
					continue;
				othertotalnum = SafeCompute.add(othertotalnum, body.getNnum());
			}

			if (!totalnum.equals(othertotalnum)) {
				ExceptionUtils.wrappBusinessException("转换前后数量不一致");
			}
		}
	}
}
