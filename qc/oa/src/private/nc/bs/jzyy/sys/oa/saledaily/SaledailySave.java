package nc.bs.jzyy.sys.oa.saledaily;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.api.rest.utils.NCCRestUtils;
import nc.bs.framework.common.NCLocator;
import nc.bs.jzyy.sys.oa.saledaily.check.CheckCtSaleValidator;
import nc.bs.jzyy.sys.oa.saledaily.check.CheckCtSaleValidatorAfter;
import nc.bs.jzyy.sys.oa.saledaily.fill.SaledailySaveDefValue;
import nc.itf.scmpub.reference.uap.pf.PfServiceScmUtil;
import nc.itf.uap.pf.IPFBusiAction;
import nc.vo.ct.enumeration.CtEntity;
import nc.vo.ct.enumeration.CtFlowEnum;
import nc.vo.ct.saledaily.entity.AggCtSaleVO;
import nc.vo.ct.saledaily.entity.CtSaleBVO;
import nc.vo.ct.saledaily.entity.CtSaleVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.scmpub.check.billvalidate.BillVOsCheckRule;

import org.apache.commons.lang.ArrayUtils;
import org.json.JSONString;

public class SaledailySave {

	public JSONString save(List<Map<String, Object>> paramList) {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("success", true);
		List<AggCtSaleVO> aggVOList = new ArrayList<AggCtSaleVO>();
		try {
			for (Map<String, Object> map : paramList) {
				if (!map.containsKey(CtEntity.ct_sale.name())
						|| !map.containsKey(CtEntity.ct_sale_b.name())) {
					result.put("message", "传入数据异常，合同要包含表头信息和表体信息");
					result.put("code", 1);
					return NCCRestUtils.toJSONString(result);
				}
				Map<String, String> headInfo = (Map<String, String>) map
						.get(CtEntity.ct_sale.name());
				List<Map<String, String>> bodyInfo = (List<Map<String, String>>) map
						.get(CtEntity.ct_sale_b.name());
				AggCtSaleVO aggvo = new AggCtSaleVO();
				CtSaleVO hvo = new CtSaleVO();
				for (String headkey : headInfo.keySet()) {
					hvo.setAttributeValue(headkey, headInfo.get(headkey));
				}
				aggvo.setParentVO(hvo);
				List<CtSaleBVO> bvoList = new ArrayList<>();
				for (Map<String, String> bodyMap : bodyInfo) {
					CtSaleBVO bvo = new CtSaleBVO();
					for (String bodykey : bodyMap.keySet()) {
						bvo.setAttributeValue(bodykey, bodyMap.get(bodykey));
					}
					bvoList.add(bvo);
				}
				aggvo.setCtSaleBVO(bvoList.toArray(new CtSaleBVO[bvoList.size()]));
				aggVOList.add(aggvo);
			}
			AggCtSaleVO[] ctSaleVOs = aggVOList
					.toArray(new AggCtSaleVO[aggVOList.size()]);
			AggCtSaleVO[] saveBase = saveBase(ctSaleVOs);
			result.put("message", "合同保存成功");
			result.put("data", saveBase);
			result.put("code", 0);
		} catch (BusinessException e) {
			result.put("message", "合同保存异常" + e.getMessage());
			result.put("code", 1);
			return NCCRestUtils.toJSONString(result);
		}
		return NCCRestUtils.toJSONString(result);
	}

	private AggCtSaleVO[] saveBase(AggCtSaleVO[] vos) throws BusinessException {
		if (ArrayUtils.isEmpty(vos)) {
			return null;
		}
		try {
			// 1、传入数据基本非空校验
			BillVOsCheckRule checker = new BillVOsCheckRule(
					new CheckCtSaleValidator());
			checker.check(vos);
			// 2、数据填充
			SaledailySaveDefValue fillUtil = new SaledailySaveDefValue();
			vos = fillUtil.setDefultValue(vos);
			// 3、补充数据后还要再做必输项校验（有些必输项自动填充）
			checker = new BillVOsCheckRule(new CheckCtSaleValidatorAfter());
			checker.check(vos);
			for (AggCtSaleVO bill : vos) {
				bill.getParentVO().setStatus(VOStatus.NEW);
			}
			return (AggCtSaleVO[]) PfServiceScmUtil.processBatch("SAVEBASE",
					"Z3", vos, null, null);
		} catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
	}

	private AggCtSaleVO[] sendApprove(AggCtSaleVO[] vos)
			throws BusinessException {
		if (ArrayUtils.isEmpty(vos)) {
			return null;
		}
		try {
			for (AggCtSaleVO aggvo : vos) {
				CtSaleVO parentVO = aggvo.getParentVO();
				if (!parentVO.getFstatusflag().equals(CtFlowEnum.Free.value())) {
					throw new BusinessException("当前合同编号："
							+ parentVO.getVbillcode() + "不是自由状态，不能提交");
				}
			}
			return (AggCtSaleVO[]) PfServiceScmUtil.processBatch("SAVE", "Z3",
					vos, null, null);
		} catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
	}

	private AggCtSaleVO[] approve(AggCtSaleVO[] vos) throws BusinessException {
		if (ArrayUtils.isEmpty(vos)) {
			return null;
		}
		try {
			for (AggCtSaleVO aggvo : vos) {
				CtSaleVO parentVO = aggvo.getParentVO();
				if (!parentVO.getFstatusflag().equals(CtFlowEnum.Free.value())
						&& !parentVO.getFstatusflag().equals(
								CtFlowEnum.COMMIT.value())
						&& !parentVO.getFstatusflag().equals(
								CtFlowEnum.APPROVING.value())) {
					throw new BusinessException("当前合同编号："
							+ parentVO.getVbillcode() + "状态不符合审批条件，不能审批");
				}
			}
			List<AggCtSaleVO> returnObj = new ArrayList<AggCtSaleVO>();
			for (AggCtSaleVO bill : vos) {
				AggCtSaleVO[] approveBill = (AggCtSaleVO[]) NCLocator
						.getInstance().lookup(IPFBusiAction.class)
						.processAction("APPROVE", "Z3", null, bill, null, null);
				returnObj.add(approveBill[0]);
			}
			return returnObj.toArray(new AggCtSaleVO[returnObj.size()]);
		} catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
	}

	private AggCtSaleVO[] validate(AggCtSaleVO[] vos) throws BusinessException {
		if (ArrayUtils.isEmpty(vos)) {
			return null;
		}
		try {
			for (AggCtSaleVO aggvo : vos) {
				CtSaleVO parentVO = aggvo.getParentVO();
				if (!parentVO.getBshowlatest().booleanValue()
						|| !parentVO.getFstatusflag().equals(
								CtFlowEnum.APPROVE.value())) {
					throw new BusinessException("当前合同编号："
							+ parentVO.getVbillcode() + "不是最新显示版本或未审批通过，不能生效");
				}
			}
			return (AggCtSaleVO[]) PfServiceScmUtil.processBatch("VALIDATE",
					"Z3", vos, null, null);
		} catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
	}

}
