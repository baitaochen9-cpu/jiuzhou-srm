package nc.bs.mmpac.pmo.pac0002.rule.check;

import nc.cmp.bill.util.SysInit;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.vo.mmpac.pmo.pac0002.entity.PMOAggVO;
import nc.vo.mmpac.pmo.pac0002.entity.PMOHeadVO;
import nc.vo.mmpac.pmo.pac0002.entity.PMOItemVO;
import nc.vo.pfxx.util.ArrayUtils;
import nc.vo.pub.lang.UFBoolean;

public class PMOCheckinspcetiontoLims implements IRule<PMOAggVO> {

	@Override
	public void process(PMOAggVO[] Vos) {
		// TODO Auto-generated method stub
		if (ArrayUtils.isEmpty(Vos)) {
			return;
		}
		for (PMOAggVO pmoAggVO : Vos) {
			PMOHeadVO headvo = pmoAggVO.getParentVO();
			PMOItemVO[] items = pmoAggVO.getChildrenVO();
			try {
				if (UFBoolean.TRUE == (SysInit.getParaBoolean(
						headvo.getPk_org(), "YFQC606"))) { // 是否启用外系统
					return;
				}
				for (PMOItemVO pmoItemVO : items) {
					if (!"~".equalsIgnoreCase(pmoItemVO.getVdef11())) {// LIMS报检号不为空
						nc.vo.ecpubapp.pattern.exception.ExceptionUtils
								.wrappBusinessException("请取消报检后执行！");
					}
				}

			} catch (Exception e) {

				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}
}
