package nccloud.web.ct.purdaily.utils;

import java.util.Arrays;
import java.util.List;

import nc.itf.ct.purdaily.IPurdailyMaintain;
import nc.vo.ct.purdaily.entity.AggCtPuVO;
import nc.vo.ct.purdaily.entity.CtPuExecVO;
import nc.vo.ct.purdaily.entity.CtPuVO;
import nc.vo.ct.uitl.ArrayTool;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFDate;
import nc.vo.pubapp.pattern.model.entity.bill.AbstractBill;
import nc.vo.pubapp.pattern.pub.ListToArrayTool;
import nccloud.framework.core.exception.ExceptionUtils;
import nccloud.framework.service.ServiceLocator;
import nccloud.framework.web.container.SessionContext;

/**
 * @description 脚本操作工具类
 * @author xiahui
 * @date 创建时间：2019-3-15 上午9:08:11
 * @version ncc1.0
 **/
public class ScriptActionUtil {
	public static final String getFROZEN() {
		return nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("common", "UC001-0000030")/* 冻结 */;
	}

	public static final String getTERMINATE() {
		return nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("4020003_0", "04020003-0001")/* 实际终止 */;
	}

	public static final String getUNFROZEN() {
		return nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("common", "UC001-0000031")/* 解冻 */;
	}

	public static final String getUNTERMINATE() {
		return nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("4020003_0", "04020003-0293")/* 取消终止 */;
	}

	public static final String getUNVALIDATE() {
		return nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("4020003_0", "04020003-0002")/* 取消生效 */;
	}

	public static final String getVALIDATE() {
		return nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("4020003_0", "04020003-0003")/* 实际生效 */;
	}

	/**
	 * 批量操作时，在执行子表中新增一行数据
	 * 
	 * @param vos
	 * @param ctStatus
	 * @param sReason
	 * @param vexecflow
	 */
	public static void addNewExecVO(AbstractBill[] vos, Integer ctStatus, String sReason, String vexecflow) {
		List<AbstractBill> listDate = Arrays.asList(vos);
		ListToArrayTool<AbstractBill> tool = new ListToArrayTool<AbstractBill>();
		AggCtPuVO[] arrayBills = (AggCtPuVO[]) tool.convertToArray(listDate);

		String[] ids;
		try {
			ids = ServiceLocator.find(IPurdailyMaintain.class).getOIDs(vos.length);
		} catch (BusinessException e) {
			// 日志异常
			ExceptionUtils.wrapException(e);
			return;
		}

		int i = 0;
		for (AggCtPuVO bill : arrayBills) {
			CtPuVO ctHeadVo = bill.getParentVO();
			// 批量数据（ 按状态 ）
			if (!ctStatus.equals(ctHeadVo.getFstatusflag())) {
				continue;
			}
			CtPuExecVO[] oldExecVOs = bill.getCtPuExecVO();
			CtPuExecVO newExecVO = new CtPuExecVO();
			newExecVO.setPk_ct_pu(ctHeadVo.getPk_ct_pu());
			newExecVO.setPk_ct_pu_exec(ids[i]);
			newExecVO.setPk_group(ctHeadVo.getPk_group());
			newExecVO.setPk_org(ctHeadVo.getPk_org());
			newExecVO.setPk_org_v(ctHeadVo.getPk_org_v());
			newExecVO.setVexecdate(new UFDate(SessionContext.getInstance().getClientInfo().getBizDateTime()));
			newExecVO.setVexecreason(sReason);
			newExecVO.setVexecflow(vexecflow);
			newExecVO.setStatus(VOStatus.NEW); // 解决checkTS报并发问题
			CtPuExecVO[] execVO = ArrayTool.arrayToCombin(oldExecVOs, new CtPuExecVO[] { newExecVO });
			bill.setChildren(CtPuExecVO.class, execVO);
			i++;
		}
	}

	/**
	 * 设置执行过程VO的状态
	 * 
	 * @param vos
	 * @param status
	 */
	public static void resetExecVOStatus(AbstractBill[] vos, int status) {
		for (AbstractBill vo : vos) {
			CtPuExecVO[] execVOs = ((AggCtPuVO) vo).getCtPuExecVO();
			for (CtPuExecVO execVO : execVOs) {
				execVO.setStatus(status);
			}
		}
	}
}
