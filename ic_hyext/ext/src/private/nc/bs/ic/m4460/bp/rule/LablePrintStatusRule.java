package nc.bs.ic.m4460.bp.rule;

import java.util.HashMap;
import java.util.Map;

import nc.bs.ic.pub.base.ICRule;
import nc.impl.pubapp.pattern.data.vo.VOQuery;
import nc.vo.fi.pub.SqlUtils;
import nc.vo.ic.m4460.entity.StateAdjustVO;
import nc.vo.ic.onhand.entity.OnhandDimVO;
import nc.vo.ic.pub.util.VOEntityUtil;
import nc.vo.ic.pub.util.ValueCheckUtil;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

public class LablePrintStatusRule extends ICRule<StateAdjustVO> {
	public void process(StateAdjustVO[] vos) {
		try {
			setStateUsable(vos);
		} catch (BusinessException e) {
			ExceptionUtils.wrappException(e);
		}
	}

	private void setStateUsable(StateAdjustVO[] vos) throws BusinessException {
		if (vos.length <= 0) {
			return;
		}

		Map<String, OnhandDimVO> map = getStateAdjustVO(vos);
		for (StateAdjustVO vo : vos) {
			if (VOStatus.DELETED == vo.getStatus()) {
				continue;
			}
			String newstate = vo.getCadjuststateid();
			OnhandDimVO dimvo = map.get(vo.getPk_onhanddim());
			if (dimvo == null) {
				throw new BusinessException("现存量维度不存在");
			}
			/************************bbt 2024.01.17******************************/
			//组织参数，控制库存调整前后状态一致时，是否允许打印新状态标签
			UFBoolean LM_001 = nc.cmp.bill.util.SysInit.getParaBoolean(vo.getPk_org(), "LM_001");
			if (UFBoolean.FALSE == LM_001 || UFBoolean.FALSE.equals(LM_001)){
				if (!newstate.equals(dimvo.getCstateid())) {
					vo.setVdef1("Y");
				}
			}else if(UFBoolean.TRUE == LM_001 || UFBoolean.TRUE.equals(LM_001)){
					vo.setVdef1("Y");
			}
			/*原逻辑*/
//			if (!newstate.equals(dimvo.getCstateid())) {
//				vo.setVdef1("Y");
//			}
			/*******/
			/*********************************************************************/
		}
	}

	private Map<String, OnhandDimVO> getStateAdjustVO(StateAdjustVO[] resultVOs)
			throws BusinessException {
		String[] cmaterialvids = (String[]) VOEntityUtil.getVOsValues(
				resultVOs, "pk_onhanddim", String.class);
		String wherepart = SqlUtils.getInStr("pk_onhanddim", cmaterialvids,
				false);
		VOQuery<OnhandDimVO> query = new VOQuery<>(OnhandDimVO.class);
		OnhandDimVO[] vos = (OnhandDimVO[]) query.query(" and " + wherepart,
				null);
		if (ValueCheckUtil.isNullORZeroLength(vos)) {
			return null;
		}
		Map<String, OnhandDimVO> map = new HashMap();
		for (OnhandDimVO vo : vos) {
			map.put(vo.getPk_onhanddim(), vo);
		}
		return map;
	}
}