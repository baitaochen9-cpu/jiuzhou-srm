/**   
 * @description TODO
 * @author zhangchqf
 * @date 2019年5月15日 下午3:07:27 
 * @version ncc1.0   
 */
package nccloud.pubimpl.purp.priceaudit.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import nc.bs.framework.common.NCLocator;
import nc.bs.pp.m28.maintain.rule.insert.PAuditAfterMarAssRule;
import nc.itf.org.IOrgConst;
import nc.itf.pp.m28.IPriceAuditMaintain;
import nc.itf.scmpub.reference.uap.group.SysInitGroupQuery;
import nc.itf.scmpub.reference.uap.org.OrgUnitPubService;
import nc.itf.scmpub.reference.uap.pf.PfServiceScmUtil;
import nc.itf.uap.pf.IPfExchangeService;
import nc.pubitf.pu.m20.pub.IQueryPrayBill;
import nc.vo.org.OrgVO;
import nc.vo.pp.m28.entity.PriceAuditHeaderVO;
import nc.vo.pp.m28.entity.PriceAuditItemVO;
import nc.vo.pp.m28.entity.PriceAuditVO;
import nc.vo.pp.m28.enumeration.EnumPriceAuditBillStatus;
import nc.vo.pu.m20.entity.PraybillHeaderVO;
import nc.vo.pu.m20.entity.PraybillItemVO;
import nc.vo.pu.m20.entity.PraybillViewVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.pubapp.pattern.tool.performance.DeepCloneTool;
import nc.vo.scmpub.res.billtype.CTBillType;
import nc.vo.scmpub.res.billtype.POBillType;
import nc.vo.scmpub.res.billtype.PPBillType;
import nc.vo.scmpub.res.billtype.SCBillType;
import nc.vo.scmpub.util.ArrayUtil;
import nccloud.commons.lang.StringUtils;
import nccloud.pubitf.purp.priceaudit.service.IPriceAuditMaintainService;

/**
 * @description TODO
 * @author zhangchqf
 * @date 2019年5月15日 下午3:07:27
 * @version ncc1.0
 */
public class PriceAuditMaintainServiceImpl
		implements IPriceAuditMaintainService {

	@Override
	public AggregatedValueObject[] m28PriceAudit61(String[] ids)
			throws BusinessException {
		// 上游单据类型
		String upBillType = PPBillType.PriceAudit.getCode();
		// 目的（下游）单据类型
		String destBillType = SCBillType.Order.getCode();
		PriceAuditVO[] vos = this.getPriceAuditVOsByIds(ids);
		// 删除供应商行自由辅助属性
		PAuditAfterMarAssRule marassRule = new PAuditAfterMarAssRule();
		marassRule.process(vos);
		PriceAuditVO savebeforevo = (PriceAuditVO) new DeepCloneTool()
				.deepClone(vos[0]);
		this.openSCOrderDlg(vos[0], savebeforevo);
		AggregatedValueObject[] aggVO = this.execVOChange(upBillType, destBillType,
				vos);
		return aggVO;
	}

	@Override
	public AggregatedValueObject[] m28PriceAuditZ2(String[] ids)
			throws BusinessException {
		// 上游单据类型
		String upBillType = PPBillType.PriceAudit.getCode();
		// 目的（下游）单据类型
		String destBillType = CTBillType.PurDaily.getCode();
		PriceAuditVO[] vos = this.getPriceAuditVOsByIds(ids);
		// 过滤订货的单据
		this.filterSrcVos(vos[0], destBillType);
		this.openCTBillDlg(vos[0]);
		AggregatedValueObject[] aggVO = this.execVOChange(upBillType, destBillType,
				vos);
		return aggVO;
	}

	@Override
	public AggregatedValueObject[] m28PriceAudit21(String[] ids)
			throws BusinessException {
		// 上游单据类型
		String upBillType = PPBillType.PriceAudit.getCode();
		// 目的（下游）单据类型
		String destBillType = POBillType.Order.getCode();
		PriceAuditVO[] vos = this.getPriceAuditVOsByIds(ids);
		// 删除供应商行自由辅助属性
		// PAuditAfterMarAssRule marassRule = new PAuditAfterMarAssRule();
		// marassRule.process(vos);
		PriceAuditVO savebeforevo = (PriceAuditVO) new DeepCloneTool()
				.deepClone(vos[0]);
		AggregatedValueObject[] retSrcVos = this.openPUOrderDlg(vos[0],
				savebeforevo);
		// AggregatedValueObject[] aggVO = this.execVOChange(upBillType,
		// destBillType,
		// vos);
		AggregatedValueObject[] destVos = PfServiceScmUtil
				.exeVOChangeByBillItfDef(upBillType, destBillType, retSrcVos);
		return destVos;
	}

	private AggregatedValueObject[] openPUOrderDlg(PriceAuditVO selectedVO,
			PriceAuditVO savebeforevo) {

		if (!SysInitGroupQuery.isPOEnabled()) {
			ExceptionUtils.wrappBusinessException(
					nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("4005002_0",
							"04005002-0069")/*
															 * @res "采购模块未启用，无法生成采购订单!"
															 */);
		}

		AggregatedValueObject[] srcVosAfterFilter = this.filterSrcVos(selectedVO,
				POBillType.Order.getCode());

		// 补充库存组织，以及仓库、项目(放在21的vo交换处理类ChangeVOAdjust28To21，为了减少链接数)
		// this.fillupM20Info(selectedVO);
		// PfUtilUITools.runChangeDataAry(PPBillType.PriceAudit.getCode(),
		// POBillType.Order.getCode(), srcVosAfterFilter);
		// FuncletInitData initData = null;
		// initData = new FuncletInitData();
		// initData.setInitType(Integer.parseInt(PPBillType.PriceAudit.getCode()));
		// initData.setInitData(destVos);
		/*
		 * FuncletWindowLauncher.openFuncNodeDialog(WorkbenchEnvironment.getInstance
		 * () .getWorkbench(), funvo, initData, null, true, false);
		 * 
		 * // 处理计算属性 new
		 * PAuditFillCalcPreferRule().process(this.editor.getBillCardPanel(),
		 * savebeforevo);
		 */
		return srcVosAfterFilter;
	}

	/**
	 * 
	 * 根据id得到价格审批单
	 * 
	 * @param ids
	 * @return
	 * @throws BusinessException
	 * 
	 */
	private PriceAuditVO[] getPriceAuditVOsByIds(String[] ids)
			throws BusinessException {
		IPriceAuditMaintain service = NCLocator.getInstance()
				.lookup(IPriceAuditMaintain.class);
		PriceAuditVO[] vos = service.queryByIds(ids);
		return vos;
	}

	/**
	 * 推委外订单
	 * 
	 * @param selectedVO
	 */
	private void openSCOrderDlg(PriceAuditVO selectedVO,
			PriceAuditVO savebeforevo) {

		if (!SysInitGroupQuery.isSCEnabled()) {
			ExceptionUtils.wrappBusinessException(
					nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("4005002_0",
							"04005002-0070")/*
															 * @res "委外模块未启用，无法生成委外订单!"
															 */);
		}
		AggregatedValueObject[] srcVosAfterFilter = this.filterSrcVos(selectedVO,
				SCBillType.Order.getCode());
		// 补充 库存组织，以及仓库、项目(也应该放到vo交换处理类，减少连接数)
		this.fillupM20Info(selectedVO);
		// 处理计算属性
		// new PAuditFillCalcPreferRule().process(this.editor.getBillCardPanel(),
		// savebeforevo);

	}

	/**
	 * 补充需求库存组织以及需求仓库
	 * 
	 * @param selectedVO
	 */
	private void fillupM20Info(PriceAuditVO selectedVO) {
		PriceAuditItemVO[] children = selectedVO.getChildrenVO();
		PriceAuditHeaderVO header = selectedVO.getParentVO();
		if (ArrayUtil.isEmpty(children)) {
			return;
		}
		// 源头是请购单
		String srcType = children[0].getVfirsttype();
		if (StringUtils.equals(POBillType.PrayBill.getCode(), srcType)) {
			IQueryPrayBill prayBill = NCLocator.getInstance()
					.lookup(IQueryPrayBill.class);
			String[] bIDs = this.getPrayBillBID(selectedVO);
			if (ArrayUtil.isEmpty(bIDs)) {
				return;
			}
			String[] feilds = { PraybillItemVO.PK_PRAYBILL,
					PraybillItemVO.PK_PRAYBILL_B, PraybillItemVO.PK_ORG,
					PraybillItemVO.PK_REQSTOR, PraybillItemVO.CPROJECTID,
					PraybillHeaderVO.VBILLCODE, PraybillHeaderVO.CTRANTYPEID,
					PraybillItemVO.CROWNO, PraybillItemVO.CFIRSTID,
					PraybillItemVO.CFIRSTBID, PraybillItemVO.CFIRSTTYPECODE,
					PraybillItemVO.VFIRSTCODE, PraybillItemVO.VFIRSTROWNO,
					PraybillItemVO.VFIRSTTRANTYPE, PraybillHeaderVO.PK_ORG };
			try {
				Map<String, PraybillViewVO> prayBillMap = prayBill
						.queryViewByItemPK(bIDs, feilds);
				String vfirstBid = "";
				for (int i = 0; i < children.length; i++) {
					PriceAuditItemVO child = children[i];
					// 物料行
					if (!StringUtils.isEmpty(child.getPk_material())) {
						vfirstBid = child.getCfirstbid();
					}

					// 库存组织，仓库、项目
					String pk_stock = prayBillMap.get(vfirstBid).getHead().getPk_org();
					String pk_reqstor = prayBillMap.get(vfirstBid).getItem()
							.getPk_reqstor();
					String cprojectid = prayBillMap.get(vfirstBid).getItem()
							.getCprojectid();
					if (StringUtils.isBlank(header.getPk_stockorg())) {
						header.setPk_stockorg(pk_stock);
					}
					child.setPk_reqstor(pk_reqstor);
					child.setCprojectid(cprojectid);

					// 赵玉行、吴小亮要求(NCdp203153486)：价格审批单推订单时将来源和源头置为请购单和请购单的源头。
					String m20code = prayBillMap.get(vfirstBid).getHead().getVbillcode();
					String m20id = prayBillMap.get(vfirstBid).getItem().getPk_praybill();
					String m20bid = prayBillMap.get(vfirstBid).getItem()
							.getPk_praybill_b();
					String m20ctrantypeid = prayBillMap.get(vfirstBid).getHead()
							.getCtrantypeid();
					String m20crowno = prayBillMap.get(vfirstBid).getItem().getCrowno();
					String m20firstid = prayBillMap.get(vfirstBid).getItem()
							.getCfirstid();
					String m20firstbid = prayBillMap.get(vfirstBid).getItem()
							.getCfirstbid();
					String m20cfirsttypecode = prayBillMap.get(vfirstBid).getItem()
							.getCfirsttypecode();
					String m20vfirsttrantype = prayBillMap.get(vfirstBid).getItem()
							.getVfirsttrantype();
					String m20vfirstrowno = prayBillMap.get(vfirstBid).getItem()
							.getVfirstrowno();
					String m20vfirstcode = prayBillMap.get(vfirstBid).getItem()
							.getVfirstcode();

					child.setVsrctype(POBillType.PrayBill.getCode());
					child.setVsrctrantype(m20ctrantypeid);
					child.setVsrcrowno(m20crowno);
					child.setVsrccode(m20code);
					child.setCsrcid(m20id);
					child.setCsrcbid(m20bid);

					child.setCfirstid(m20firstid);
					child.setCfirstbid(m20firstbid);
					child.setVfirsttype(m20cfirsttypecode);
					child.setVfirsttrantype(m20vfirsttrantype);
					child.setVfirstrowno(m20vfirstrowno);
					child.setVfirstcode(m20vfirstcode);

				}
			} catch (BusinessException e) {
				ExceptionUtils.wrappException(e);
			}

		}
	}

	/***
	 * 取价格审批单来源单据表体主键。 请购单表体主键
	 * 
	 * @param selectedVO
	 * @return
	 */
	private String[] getPrayBillBID(PriceAuditVO selectedVO) {
		PriceAuditItemVO[] children = selectedVO.getChildrenVO();
		List<String> retList = new ArrayList<String>();
		for (int i = 0; i < children.length; i++) {
			String firstType = children[i].getVfirsttype();
			if (StringUtils.equals(POBillType.PrayBill.getCode(), firstType)) {
				// 价格审批单的源头id记录的是请购单
				retList.add(children[i].getCfirstbid());
			}
		}
		return retList.toArray(new String[0]);
	}

	private AggregatedValueObject[] filterSrcVos(PriceAuditVO selectedVO,
			String type) {
		// 委外的要过滤，只有审批状态才能退订单，表体至少有一行为订货。
		Integer status = selectedVO.getParentVO().getFbillstatus();
		if (!EnumPriceAuditBillStatus.APPROVE.value().equals(status)) {
			if (type.equals(CTBillType.PurDaily.getCode())) {
				ExceptionUtils.wrappBusinessException(
						nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("4005002_0",
								"04005002-0020")/*
																 * @res "该价格审批单没有审批，无法生成采购合同。"
																 */);
			} else if (type.equals(POBillType.Order.getCode())) {

				ExceptionUtils.wrappBusinessException(
						nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("4005002_0",
								"04005002-0022")/*
																 * @res "该价格审批单没有审批，无法生成采购订单,"
																 */);
			} else if (type.equals(SCBillType.Order.getCode())) {

				ExceptionUtils.wrappBusinessException(
						nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("4005002_0",
								"04005002-0022")/*
																 * @res "该价格审批单没有审批，无法生成采购订单,"
																 */);
			}
		}
		PriceAuditItemVO[] children = selectedVO.getChildrenVO();
		List<PriceAuditItemVO> newChildren = new ArrayList<PriceAuditItemVO>();
		for (PriceAuditItemVO child : children) {
			if (isHaveBorder(child).booleanValue()) {
				newChildren.add(child);
			}
		}
		selectedVO.setChildrenVO(
				newChildren.toArray(new PriceAuditItemVO[newChildren.size()]));
		AggregatedValueObject[] retSrcVos = new AggregatedValueObject[] {
				selectedVO };
		return retSrcVos;

	}

	public static UFBoolean isHaveBorder(PriceAuditItemVO child) {
		UFBoolean bOrder = child.getBorder();
		if (bOrder.equals(UFBoolean.TRUE)) {
			return UFBoolean.TRUE;
		}
		return UFBoolean.FALSE;
	}

	/**
	 * 
	 * 转单
	 * 
	 * @param srcBillType
	 * @param destBillType
	 * @param bills
	 * @return
	 * 
	 */
	private AggregatedValueObject[] execVOChange(String srcBillType,
			String destBillType, AggregatedValueObject[] bills)
			throws BusinessException {
		AggregatedValueObject[] destVos = null;
		IPfExchangeService service1 = NCLocator.getInstance()
				.lookup(IPfExchangeService.class);
		destVos = service1.runChangeDataAry(srcBillType, destBillType, bills, null);
		return destVos;
	}

	private void openCTBillDlg(PriceAuditVO selectedVO) {
		AggregatedValueObject[] srcVosAfterFilter = new AggregatedValueObject[] {
				selectedVO };
		AggregatedValueObject[] destVos = PfServiceScmUtil.executeVOChange(
				PPBillType.PriceAudit.getCode(), CTBillType.PurDaily.getCode(),
				srcVosAfterFilter);

		// 填充目的vo，即采购合同vo
		this.fillDestVOs(destVos);
	}

	private void fillDestVOs(AggregatedValueObject[] destVos) {
		// 填充目的vo，即采购合同vo
		for (AggregatedValueObject destVo : destVos) {
			Object pk_orgObj = destVo.getParentVO()
					.getAttributeValue(PriceAuditHeaderVO.PK_ORG);
			if (null == pk_orgObj || StringUtils.isEmpty(pk_orgObj.toString())) {
				break;
			}
			String pk_org = pk_orgObj.toString();
			String pk_org_v = null;
			if (!StringUtils.isEmpty(pk_org)) {
				pk_org_v = OrgUnitPubService.getOrgVid(pk_org);
			}
			boolean blen = OrgUnitPubService.isTypeOf(pk_org,
					IOrgConst.FINANCEORGTYPE);

			for (CircularlyAccessibleValueObject itemvo : destVo.getChildrenVO()) {
				if (blen) {
					itemvo.setAttributeValue("pk_financeorg", pk_org);
					itemvo.setAttributeValue("pk_financeorg_v", pk_org_v);
				} else {
					OrgVO vo = OrgUnitPubService.getOrg(pk_org);
					String pk_corp = vo.getPk_corp();
					String pk_corp_v = OrgUnitPubService.getOrgVid(pk_corp);
					itemvo.setAttributeValue("pk_financeorg", pk_corp);
					itemvo.setAttributeValue("pk_financeorg_v", pk_corp_v);
				}
			}
		}

	}
}
