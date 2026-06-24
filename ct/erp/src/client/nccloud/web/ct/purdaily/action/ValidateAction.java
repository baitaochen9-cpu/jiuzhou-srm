package nccloud.web.ct.purdaily.action;

import java.util.Map;

import nc.vo.ct.enumeration.CtFlowEnum;
import nc.vo.ct.purdaily.entity.AggCtPuVO;
import nc.vo.pub.SuperVO;
import nc.vo.pub.VOStatus;
import nc.vo.pubapp.pattern.model.entity.bill.AbstractBill;
import nc.vo.pubapp.pattern.model.meta.entity.vo.PseudoColumnAttribute;
import nc.vo.scmpub.res.billtype.CTBillType;
import nc.vo.scmpub.util.ArrayUtil;
import nccloud.dto.scmpub.script.entity.SCMScriptResultDTO;
import nccloud.framework.service.ServiceLocator;
import nccloud.pubitf.riart.pflow.CloudPFlowContext;
import nccloud.pubitf.scmpub.commit.service.IBatchRunScriptService;
import nccloud.web.ct.pub.action.BaseScriptAction;
import nccloud.web.ct.purdaily.utils.ResultUtil;
import nccloud.web.ct.purdaily.utils.ScriptActionUtil;

/**
 * @description 生效
 * @author xiahui
 * @date 创建时间：2019-1-18 上午8:46:16
 * @version ncc1.0
 **/
public class ValidateAction extends BaseScriptAction {

	@Override
	public Map<String, Object> processSuccessResult(AbstractBill[] bills, AbstractBill[] orginalBills) {
		return ResultUtil.processScriptResult(bills, orginalBills);
	}

	@Override
	public void beforeProcess(Object[] objs, Map<String, Object> userObj) {
		AbstractBill[] vos = (AbstractBill[]) objs;
		String reason = (String) userObj.get("reason");
		ScriptActionUtil.addNewExecVO(vos, CtFlowEnum.APPROVE.toIntValue(), reason, ScriptActionUtil.getVALIDATE());

		// modify by liangchen1 港华合同变更生效以及重走审批流需求
		/**
		 * 界面vo是一组pk，生效的时候需要设置原始版本pk给新版本，以保证回写联查的正确
		 * 前台vo的pk与后台返回pk不同在流程平台进行差异处理时会出现问题（表头直接差异，表体根据pk匹配进行差异化) 本身流程平台差异处理会优先使用伪列进行
		 * ，但非编辑态下从界面得到的vo并没有自动加上伪列，所以加上伪列， 以保证流程平台能够正常差异处理
		 */
		for (AbstractBill vo : vos) {
			AggCtPuVO aggVo = (AggCtPuVO) vo;
			SuperVO[][] allChildren = aggVo.getAllChildren();
			for (SuperVO[] superVOs : allChildren) {
				if (ArrayUtil.isEmpty(superVOs)) {
					continue;
				}
				this.setFakeRowNO(superVOs);
			}
		}

	}

	@Override
	public SCMScriptResultDTO execScript(AbstractBill[] bills) {
		CloudPFlowContext context = new CloudPFlowContext();
		context.setActionName("VALIDATE");
		context.setBillType("Z2");
		context.setBillVos(bills);
		// 执行提交动作脚本
		SCMScriptResultDTO result = ServiceLocator.find(IBatchRunScriptService.class).runBacth(context,
				AggCtPuVO.class);
		ScriptActionUtil.resetExecVOStatus(result.getSucessVOs(), VOStatus.UNCHANGED);
		return result;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Class getClazz() {
		return AggCtPuVO.class;
	}

	@Override
	public String getPermissioncode() {
		return CTBillType.PurDaily.getCode();
	}

	@Override
	protected Boolean isHandleResumeException() {
		return Boolean.TRUE;
	}

	/**
	 * 方法功能描述：设置表体尾列
	 * <p>
	 * <b>参数说明</b>
	 * 
	 * @param vos
	 *            <p>
	 * @since 6.0
	 * @author lizhengb
	 * @time 2010-5-31 下午02:56:43
	 */
	protected void setFakeRowNO(SuperVO[] vos) {
		for (int i = 0; i < vos.length; i++) {
			vos[i].setAttributeValue(PseudoColumnAttribute.PSEUDOCOLUMN, Integer.valueOf(i));
		}
	}
}
