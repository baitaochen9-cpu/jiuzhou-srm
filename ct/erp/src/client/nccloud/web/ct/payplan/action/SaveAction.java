package nccloud.web.ct.payplan.action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import nc.itf.ct.purdaily.ICtPayPlan;
import nc.vo.bd.meta.BatchOperateVO;
import nc.vo.ct.purdaily.entity.PayPlanViewVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nccloud.dto.ct.payplan.constance.CommonConst;
import nccloud.framework.core.exception.ExceptionUtils;
import nccloud.framework.service.ServiceLocator;
import nccloud.framework.web.action.itf.ICommonAction;
import nccloud.framework.web.container.IRequest;
import nccloud.framework.web.convert.translate.Translator;
import nccloud.framework.web.ui.pattern.grid.Grid;
import nccloud.framework.web.ui.pattern.grid.GridOperator;

/**
 * @description 保存
 * @author xiahui
 * @date 创建时间：2019-3-8 上午10:53:53
 * @version ncc1.0
 **/
public class SaveAction implements ICommonAction {

	@Override
	public Object doAction(IRequest request) {
		try {
			GridOperator operator = new GridOperator(CommonConst.PAGECODE_LIST);
			PayPlanViewVO[] viewVOs = operator.toVos(request);
			if (viewVOs == null || viewVOs.length == 0) {
				return null;
			}

			BatchOperateVO vo = this.buildBatchOperateVO(viewVOs);
			vo = ServiceLocator.find(ICtPayPlan.class).batchSave(vo);

			List<Object> retVOs = new ArrayList<>();
			retVOs.addAll(Arrays.asList(vo.getAddObjs()));
			retVOs.addAll(Arrays.asList(vo.getUpdObjs()));

			Grid grid = operator.toGrid(retVOs.toArray(new PayPlanViewVO[retVOs.size()]));
			Translator translator = new Translator();
			translator.translate(grid);
			return grid;
		} catch (BusinessException e) {
			ExceptionUtils.wrapException(e);
		}
		return null;
	}

	/**
	 * 构建批量操作的包装VO
	 * 
	 * @param viewVOs
	 * @return
	 */
	private BatchOperateVO buildBatchOperateVO(PayPlanViewVO[] viewVOs) {
		BatchOperateVO vo = new BatchOperateVO();
		List<PayPlanViewVO> addVOs = new ArrayList<>();
		List<PayPlanViewVO> updVOs = new ArrayList<>();
		List<PayPlanViewVO> delVOs = new ArrayList<>();

		for (PayPlanViewVO viewVO : viewVOs) {
			int status = viewVO.getStatus();
			if (status == VOStatus.NEW) {
				addVOs.add(viewVO);
			} else if (status == VOStatus.UPDATED) {
				updVOs.add(viewVO);
			} else if (status == VOStatus.DELETED) {
				delVOs.add(viewVO);
			}
		}

		vo.setAddObjs(addVOs.toArray(new PayPlanViewVO[addVOs.size()]));
		vo.setUpdObjs(updVOs.toArray(new PayPlanViewVO[updVOs.size()]));
		vo.setDelObjs(delVOs.toArray(new PayPlanViewVO[delVOs.size()]));
		return vo;
	}
}
