package nc.impl.pu.m23.approve.rule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.framework.common.NCLocator;
import nc.impl.pubapp.pattern.database.DataAccessUtils;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.itf.jzyy.sys.IProcessService;
import nc.itf.jzyy.sys.ISysDispatcher;
import nc.itf.jzyy.sys.thlims.ISysDispatcherThLims;
import nc.itf.pu.m23.qc.IArriveForQC;
import nc.itf.scmpub.reference.uap.group.SysInitGroupQuery;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.MapProcessor;
import nc.pubitf.qc.c001.pu.ReturnObjectFor23;
import nc.vo.pu.m23.entity.ArriveItemVO;
import nc.vo.pu.m23.entity.ArriveVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.pattern.data.IRowSet;
import nc.vo.pubapp.pattern.data.ValueUtils;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.pubapp.pattern.model.transfer.bill.ClientBillToServer;
import nc.vo.qc.pub.enumeration.StrictLevelEnum;
import nc.vo.qc.pub.util.QCSysParamUtil;

/**
 * 到货单审批时自动生成报检单
 * 
 * @author yechd5
 * @since 2017-07-05 下午 19:47:21
 */
public class M23ApproveAndCreateZJ implements IRule<ArriveVO> {

	@Override
	public void process(ArriveVO[] vos) {
		String pk_org = vos[0].getHVO().getPk_org();
		try {
			// 到货单是否自动报检】判断，如果参数=是则自动报检，如果参数=否，则不执行自动报检
			boolean YF606 = this.getProcessService().qryBooleanParm(pk_org,
					"YF606", "到货单是否自动报检", false);
			if (!YF606) {
				return;
			}
			// 2021-10-25判断是生成NC的单子还是同步LIMS
			boolean outSystem = this.getProcessService().isOutSystem(pk_org);
			if (!outSystem) {
				pushNcQc(vos);
			} else {
				/*
				 * edit by xuchong 2022年9月8日 根据组织判断Lims 区分调用 23 审批报检 28 组织 非审批报检
				 * 通过点击 报检按钮报检
				 */
				if ("0001V110000000012E56".equalsIgnoreCase(pk_org)) {
					pushToLims28(vos);
				} else {
					pushToLims(vos);
				}

			}
		} catch (Exception e1) {
			ExceptionUtils.wrappBusinessException("异常:" + e1.getMessage());
		}

	}

	private void pushToLims28(ArriveVO[] vos) throws BusinessException {
		// TODO Auto-generated method stub
		for (ArriveVO vo : vos) {
			String pk_org = vo.getHVO().getPk_org();
			List<ArriveItemVO> reList = new ArrayList<ArriveItemVO>();
			ArriveItemVO[] bvos = vo.getBVO();
			for (ArriveItemVO itemVO : bvos) {
				String material = itemVO.getPk_material();
				UFDouble naccumstorenum = itemVO.getNaccumstorenum();
				if (naccumstorenum != null
						&& naccumstorenum.compareTo(UFDouble.ZERO_DBL) > 0) {
					continue;
				} else {
					if (!chekQC(pk_org, material)) {
						reList.add(itemVO);
					}
				}

			}
			if (reList == null || reList.size() == 0) {
				// throw new BusinessException("没有需要走外部质检的数据，请检查后重新选择数据！");
				return;
			}
			ISysDispatcherThLims outerService = (ISysDispatcherThLims) NCLocator
					.getInstance().lookup(ISysDispatcherThLims.class.getName());
			Map<String, Object> param = new HashMap<String, Object>();
			outerService.dispatch(vo, "TH_LIMS_PU_CHECK", param);
		}
	}

	private void pushToLims(ArriveVO[] vos) throws BusinessException {
		// TODO Auto-generated method stub
		for (ArriveVO vo : vos) {
			String pk_org = vo.getHVO().getPk_org();
			List<ArriveItemVO> reList = new ArrayList<ArriveItemVO>();
			ArriveItemVO[] bvos = vo.getBVO();
			for (ArriveItemVO itemVO : bvos) {
				String material = itemVO.getPk_material();
				UFDouble naccumstorenum = itemVO.getNaccumstorenum();
				if (naccumstorenum != null
						&& naccumstorenum.compareTo(UFDouble.ZERO_DBL) > 0) {
					continue;
				} else {
					if (!chekQC(pk_org, material)) {
						reList.add(itemVO);
					}
				}

			}
			if (reList == null || reList.size() == 0) {
				// throw new BusinessException("没有需要走外部质检的数据，请检查后重新选择数据！");
				return;
			}
			ISysDispatcher outerService = (ISysDispatcher) NCLocator
					.getInstance().lookup(ISysDispatcher.class.getName());
			Map<String, Object> param = new HashMap<String, Object>();
			outerService.dispatch(vo, "LIMS_PU_CHECK", param);
		}
	}

	/**
	 * 生成NC的报检单
	 * 
	 * @param vos
	 * @throws BusinessException
	 */
	private void pushNcQc(ArriveVO[] vos) throws BusinessException {
		// TODO Auto-generated method stub
		for (ArriveVO vo : vos) {
			String pk_org = vo.getHVO().getPk_org();
			// String pk_org = vo.getHVO().getPk_org();
			// 先判断质检模块是否启用,再判断对应库存组织是否质检启用
			if (!SysInitGroupQuery.isQCEnabled()
					|| UFBoolean.FALSE.equals(ValueUtils
							.getUFBoolean(QCSysParamUtil.getINI01(pk_org)))) {
				// continue;
				ExceptionUtils
						.wrappBusinessException(nc.vo.ml.NCLangRes4VoTransl
								.getNCLangRes().getStrByID("4004040_0",
										"04004040-0030")/*
														 * @res "质检模块未启用,无法报检!"
														 */);
			}
			ArriveItemVO[] bvos = vo.getBVO();

			List<ArriveItemVO> list = new ArrayList<ArriveItemVO>();
			for (ArriveItemVO itemVO : bvos) {
				String material = itemVO.getPk_material();
				UFDouble naccumchecknum = itemVO.getNaccumchecknum();

				if (naccumchecknum != null) {
					if (naccumchecknum.compareTo(itemVO.getNnum()) == 0) {
						UFDouble naccumstorenum = itemVO.getNaccumstorenum();
						if (naccumstorenum != null
								&& naccumstorenum.compareTo(UFDouble.ZERO_DBL) > 0) {
							// ShowStatusBarMsgUtil.showStatusBarMsg(
							// nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
							// "4004040_0", "04004040-0214", null, new String[]
							// {
							// itemVO.getCrowno()
							// })/* 第{0}行已经完全入库，不能生成报检单 */,
							// this.model.getContext());
							continue;
						}
					}
				}
				if (!chekQC(pk_org, material)) {
					list.add(itemVO);
				}

			}
			if (list == null || list.size() == 0) {
				return;
			}
			ArriveVO newvo = (ArriveVO) vo.clone();
			newvo.setBVO(list.toArray(new ArriveItemVO[list.size()]));
			ArriveVO[] newvos = new ArriveVO[] { newvo };
			ArriveVO[] orgivos = new ArriveVO[] { vo };
			ClientBillToServer<ArriveVO> tool = new ClientBillToServer<ArriveVO>();
			ArriveVO[] lightVOs = tool.construct(newvos, newvos);

			Object[] objects;

			objects = NCLocator.getInstance().lookup(IArriveForQC.class)
					.qualityCheck(lightVOs, false);

			ArriveVO[] returnVos = (ArriveVO[]) objects[0];
			ReturnObjectFor23 rof = (ReturnObjectFor23) objects[1];
			// ShowStatusBarMsgUtil.showStatusBarMsg(
			// nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("4004040_0",
			// "04004040-0034")/* @res "检验成功" */, this.getModel().getContext());
			// // 得到质检模块的提示信息
			if (rof != null) {
				Map<String, Integer> strictMap = rof
						.getCsourcebid_strictlevel();
				for (ArriveVO hvo : newvos) {
					ArriveItemVO[] bvs = hvo.getBVO();
					for (ArriveItemVO bvo : bvs) {
						String bid = bvo.getPk_arriveorder_b();
						UFDouble naccumstorenum = bvo.getNaccumstorenum();
						if (naccumstorenum != null
								&& naccumstorenum
										.compareTo(bvo.getNnum() == null ? UFDouble.ZERO_DBL
												: bvo.getNnum()) >= 0) {
						}
						if (strictMap.containsKey(bid)) {
							if (StrictLevelEnum.FREE.value().equals(
									strictMap.get(bid))) {
								// ShowStatusBarMsgUtil.showStatusBarMsg(
								// nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
								// "4004040_0", "04004040-0032", null, new
								// String[] {
								// bvo.getCrowno()
								// })/*
								// * @res
								// * "第{0}行为质检连续批的严格程度为免检，不需要生成报检单！"
								// */, this.model.getContext());
							} else if (StrictLevelEnum.PAUSE.value().equals(
									strictMap.get(bid))) {
								// ShowStatusBarMsgUtil.showStatusBarMsg(
								// nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
								// "4004040_0", "04004040-0033", null, new
								// String[] {
								// bvo.getCrowno()
								// })/*
								// * @res
								// * "第{0}行为质检连续批的严格程度为暂停，不能生成报检单！"
								// */, this.model.getContext());
							}
						}
					}
				}
			}

		}
	}

	// 检查物料是否免检
	public boolean chekQC(String pk_org, String material)
			throws BusinessException {
		String sql = " select chkfreeflag    from bd_materialstock where pk_material='"
				+ material + "' and   pk_org ='" + pk_org + "' and dr=0";
		IUAPQueryBS bs = (IUAPQueryBS) NCLocator.getInstance().lookup(
				IUAPQueryBS.class.getName());

		HashMap<String, Object> hashMap2 = (HashMap<String, Object>) bs
				.executeQuery(sql, new MapProcessor());

		if (hashMap2 != null && hashMap2.size() > 0) {
			UFBoolean b = UFBoolean.valueOf(hashMap2.get("chkfreeflag")
					.toString());
			return b.booleanValue();
		}
		return false;

	}

	/**
	 * 根据采购订单表体的pk查询采购合同pk
	 * 
	 * @param pk_order_b
	 * @return String
	 * @since 2017-07-05 上午 10:32:18
	 */
	public String getPk_ct_pu(String pk_order_b) {
		StringBuffer sql = new StringBuffer();
		sql.append("select ccontractid from po_order_b where dr=0 and pk_order_b =");
		sql.append("'");
		sql.append(pk_order_b);
		sql.append("'");

		DataAccessUtils util = new DataAccessUtils();
		IRowSet rowset = util.query(sql.toString());
		List<String> list = new ArrayList<String>();
		while (rowset.next()) {
			list.add(rowset.getString(0));
		}
		return list.get(0);
	}

	private IProcessService iuap;

	private IProcessService getProcessService() {
		if (null == iuap) {
			iuap = (IProcessService) NCLocator.getInstance().lookup(
					IProcessService.class.getName());
		}
		return iuap;
	}
}
