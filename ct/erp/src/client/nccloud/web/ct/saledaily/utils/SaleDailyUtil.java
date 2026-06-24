package nccloud.web.ct.saledaily.utils;

import nc.itf.ct.purdaily.IPurdailyMaintain;
import nc.vo.ct.saledaily.entity.AggCtSaleVO;
import nc.vo.ct.saledaily.entity.CtSaleExecVO;
import nc.vo.ct.saledaily.entity.CtSaleVO;
import nc.vo.ct.uitl.ArrayTool;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nccloud.framework.core.exception.ExceptionUtils;
import nccloud.framework.service.ServiceLocator;
import nccloud.framework.web.container.SessionContext;

/**
 * @description 销售合同工具类
 * @author wangshrc
 * @date 2019年1月21日 上午9:56:17
 * @version ncc1.0
 */
/**
 *
 * @description 获取pk数组
 * @author wangshrc
 * @date 2019年1月21日 上午10:14:14
 * @version ncc1.0
 */
public class SaleDailyUtil {
	public static String[] getSplitPks(String[] pktss) {
		String[] pks = new String[pktss.length];
		for (int i = 0; i < pktss.length; i++) {
			String pk = pktss[i].split(",")[0];
			pks[i] = pk;
		}
		return pks;
	}

	/**
	 *
	 * 获取ts数组
	 *
	 * @param pktss
	 * @return
	 *
	 */
	public static UFDateTime[] getSplitTss(String[] pktss) {
		UFDateTime[] tss = new UFDateTime[pktss.length];
		for (int i = 0; i < pktss.length; i++) {
			UFDateTime ts = null;
			if (pktss[i].split(",").length == 2) {
				String sts = pktss[i].split(",")[1];
				ts = new UFDateTime(sts);
			}
			tss[i] = ts;
		}
		return tss;
	}

	/**
	 *
	 * 重新设置vo的ts
	 *
	 * @param aggvos
	 * @param tss
	 *
	 */
	public static void resetTs(AggCtSaleVO[] aggvos, UFDateTime[] tss) {
		if (aggvos == null || aggvos.length != tss.length)
			ExceptionUtils.wrapBusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("4004132_0","04004132-0006")/*@res "该数据已被删除，请重新查询"*/);
		for (int i = 0; i < aggvos.length; i++) {
			aggvos[i].getParentVO().setTs(tss[i]);
		}

	}

	public static void addNewExecVO(AggCtSaleVO[] vos, Integer ctStatus,
			String sReason, String vexecflow) {
		String[] ids;
		try {
			ids = ServiceLocator.find(IPurdailyMaintain.class).getOIDs(
					vos.length);
		} catch (BusinessException e) {
			// 日志异常
			ExceptionUtils.wrapException(e);
			return;
		}

		int i = 0;
		for (AggCtSaleVO vo : vos) {
			CtSaleVO ctHeadVo = vo.getParentVO();
			// 批量数据（ 按状态 ）
			if (!ctHeadVo.getFstatusflag().equals(ctStatus)) {
				continue;
			}
			CtSaleExecVO[] oldExecVo = vo.getCtSaleExecVO();
			CtSaleExecVO[] newExecVo = new CtSaleExecVO[] { new CtSaleExecVO() };
			newExecVo[0].setPk_ct_sale_exec(ids[i]);
			i++;
			newExecVo[0].setPk_ct_sale(ctHeadVo.getPk_ct_sale());
			newExecVo[0].setPk_group(ctHeadVo.getPk_group());
			newExecVo[0].setPk_org(ctHeadVo.getPk_org());
			newExecVo[0].setPk_org_v(ctHeadVo.getPk_org_v());
			newExecVo[0].setVexecdate(new UFDate(SessionContext.getInstance()
					.getClientInfo().getBizDateTime()));
			newExecVo[0].setVexecreason(sReason);
			newExecVo[0].setVexecflow(vexecflow);
			newExecVo[0].setStatus(VOStatus.NEW);
			CtSaleExecVO[] execVo = ArrayTool.arrayToCombin(oldExecVo,
					newExecVo);
			vo.setChildren(CtSaleExecVO.class, execVo);
		}

	}
}