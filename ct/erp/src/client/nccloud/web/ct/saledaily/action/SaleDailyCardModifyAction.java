package nccloud.web.ct.saledaily.action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import nc.vo.ct.enumeration.CtFlowEnum;
import nc.vo.ct.saledaily.entity.AggCtSaleVO;
import nc.vo.ct.saledaily.entity.CtSaleChangeVO;
import nc.vo.ct.saledaily.entity.CtSaleVO;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.pattern.tool.performance.DeepCloneTool;
import nc.vo.scmpub.util.CollectionUtils;
import nccloud.framework.core.exception.ExceptionUtils;
import nccloud.framework.web.container.SessionContext;
import nccloud.framework.web.ui.pattern.extbillcard.ExtBillCard;
import nccloud.web.ct.saledaily.utils.SaleDailyCompareUtil;
import nccloud.web.scmpub.pub.operator.SCMExtBillCardOperator;

/**
 * @description 销售合同变更
 * @author wangshrc
 * @date 2019年2月28日 下午5:18:32
 * @version ncc1.0
 */
public class SaleDailyCardModifyAction extends SaleDailyCardCommonAction {
	protected Object action(AggCtSaleVO[] vos) {
		DeepCloneTool tool = new DeepCloneTool();
		AggCtSaleVO origBill = (AggCtSaleVO)tool.deepClone(vos[0]);
		this.checkPermission(vos);
		// 转换为前台结构
		SCMExtBillCardOperator operator = SaleDailyCompareUtil.getBillCardOperator();
		this.checkVersion(vos[0]);
		// 检查表头vo的状态
		this.checkHeadVO(vos[0]);
		this.setChangeVOWhenModify(vos[0]);
		ExtBillCard billcard = SaleDailyCompareUtil.operator(operator, vos[0], origBill);
		return billcard;

	}

	private void checkHeadVO(AggCtSaleVO aggvo) {
		Integer fStatus = aggvo.getParentVO().getFstatusflag();
		if (!CtFlowEnum.Free.value().equals(fStatus) && !CtFlowEnum.VALIDATE.value().equals(fStatus)
				&& !CtFlowEnum.UNAPPROVE.value().equals(fStatus)) {
			ExceptionUtils.wrapBusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("4020003_0",
					"04020003-0010")/*
									 * @res "该合同状态不能变更"
									 */);
		}
	}

	private void checkVersion(AggCtSaleVO aggvo) {
		UFBoolean bshowlatest = aggvo.getParentVO().getBshowlatest();
		;
		if (UFBoolean.FALSE.equals(bshowlatest)) {
			nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("4020003_0", "04020003-0342")/*
																								 * @res "合同不是最新版本，不允许变更"
																								 */;
		}
	}

	private void setChangeVOWhenModify(AggCtSaleVO aggvo) {
		CtSaleVO ctHeadVo = aggvo.getParentVO();
		CtSaleChangeVO[] chgVos = aggvo.getCtSaleChangeVO();
		/**
		 * 港华需求： 原有变更保存是直接版本号+1，生成生效态新版本合同 这里根据需求，先判断合同状态是否为”生效“
		 * 如果是，走原来的逻辑，版本号+1，后台save再处理单据状态原来设置为生效态的逻辑，改为自由态
		 * 如果不是，vo不做处理，对应场景是自由态新版本合同变更保存（修改）
		 */
		if (CtFlowEnum.VALIDATE.toIntValue() == ctHeadVo.getFstatusflag().intValue()) {
			UFDouble newVersion = ctHeadVo.getVersion().add(UFDouble.ONE_DBL);
			List<CtSaleChangeVO> volist = new ArrayList<CtSaleChangeVO>(Arrays.asList(chgVos));
			ctHeadVo.setVersion(newVersion);
			CtSaleChangeVO newChgVo = new CtSaleChangeVO();
			newChgVo.setStatus(VOStatus.NEW);
			newChgVo.setPk_group(ctHeadVo.getPk_group());
			newChgVo.setPk_org(ctHeadVo.getPk_org());
			newChgVo.setPk_org_v(ctHeadVo.getPk_org_v());
			newChgVo.setPk_ct_sale(ctHeadVo.getPk_ct_sale());
			newChgVo.setVchangecode(newVersion);
			newChgVo.setVchgdate(new UFDate(SessionContext.getInstance().getClientInfo().getBizDateTime()));
			newChgVo.setVchgpsn(SessionContext.getInstance().getClientInfo().getUserid());
			volist.add(newChgVo);
			CtSaleChangeVO[] results = CollectionUtils.listToArray(volist);
			aggvo.setCtSaleChangeVO(results);
		}

	}

	@Override
	protected String getPFActionName() {
		return "MODIFY";
	}

	@Override
	public String getActioncode() {
		return "modify";
	}
}
