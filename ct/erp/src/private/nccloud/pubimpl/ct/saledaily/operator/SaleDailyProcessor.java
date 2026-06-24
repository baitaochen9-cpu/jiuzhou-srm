package nccloud.pubimpl.ct.saledaily.operator;

import nc.vo.ct.saledaily.entity.AggCtSaleVO;
import nc.vo.pubapp.AppContext;
import nccloud.dto.ct.saledaily.utils.CtVOScaleRule;
import nccloud.pubitf.scmpub.pub.print.BaseMetaPrintService.IBeforePrintDataProcess;

/**
 * @description 发货单打印精度处理类
 * @author wangshrc
 * @date 2018年8月9日 下午5:09:15
 * @version ncc1.0
 */
public class SaleDailyProcessor implements IBeforePrintDataProcess {

	@Override
	public Object[] processData(Object[] datas) {
		if (null == datas || datas.length == 0) {
			return null;
		}
		AggCtSaleVO[] vos = new AggCtSaleVO[datas.length];
		int i = 0;
		for (Object obj : datas) {
			vos[i] = (AggCtSaleVO) obj;
		}
		// 精度处理
		CtVOScaleRule handler = new CtVOScaleRule(AppContext.getInstance()
				.getPkGroup(), vos);
		handler.setSacle();
		return vos;
	}

}
