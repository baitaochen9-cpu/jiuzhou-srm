package nc.impl.ic.m4n;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.framework.common.NCLocator;
import nc.bs.ic.pub.env.ICBSContext;
import nc.bs.ic.pub.util.BillQueryUtils;
import nc.impl.ic.m4n.action.DeleteAction;
import nc.impl.ic.m4n.action.InsertAction;
import nc.impl.ic.m4n.action.UpdateAction;
import nc.impl.pubapp.pattern.data.vo.VOQuery;
import nc.itf.ic.m4n.ITransformMaitain;
import nc.itf.scmpub.reference.uap.pf.PfServiceScmUtil;
import nc.itf.uap.pf.IPFBusiAction;
import nc.ui.ic.material.query.InvInfoUIQuery;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.ui.trade.business.HYPubBO_Client;
import nc.uif.pub.exception.UifException;
import nc.vo.bd.defdoc.DefdocVO;
import nc.vo.ic.batchcode.BatchSynchronizer;
import nc.vo.ic.batchcode.ICBatchFields;
import nc.vo.ic.general.define.ICBillBodyVO;
import nc.vo.ic.general.define.ICBillVO;
import nc.vo.ic.m4a.entity.GeneralInVO;
import nc.vo.ic.m4i.entity.GeneralOutVO;
import nc.vo.ic.m4n.entity.TransformBodyVO;
import nc.vo.ic.m4n.entity.TransformHeadVO;
import nc.vo.ic.m4n.entity.TransformRowFlag;
import nc.vo.ic.m4n.entity.TransformVO;
import nc.vo.ic.m4n.entity.deal.TransformVOProcessor;
import nc.vo.ic.material.deal.UnitAndHslProc;
import nc.vo.ic.onhand.entity.OnhandSNViewVO;
import nc.vo.ic.param.ICSysParam;
import nc.vo.ic.pub.calc.BusiCalculator;
import nc.vo.ic.pub.define.ICPubMetaNameConst;
import nc.vo.ic.pub.util.ValueCheckUtil;
import nc.vo.ic.sncode.ICSnFields;
import nc.vo.ic.sncode.SnCodeSynchronizer;
import nc.vo.ic.storestate.StoreStateVO;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.pf.BillStatusEnum;
import nc.vo.pub.pf.workflow.IPFActionName;
import nc.vo.pubapp.AppContext;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.pubapp.util.VORowNoUtils;

/**
 * <p>
 * <b>本类主要完成以下功能：</b>
 * <ul>
 * <li>
 * </ul>
 * <p>
 * <p>
 * 
 * @version 6.0
 * @since 6.0
 * @author chennn
 * @time 2010-5-28 上午11:19:17
 */
public class TransformMaintainImpl implements ITransformMaitain {

	@Override
	public void delete(TransformVO[] bills) throws BusinessException {
		try {
			new DeleteAction().delete(bills);
		} catch (Exception ex) {
			ExceptionUtils.marsh(ex);
		}

	}

	@Override
	public TransformVO[] insert(TransformVO[] bills) throws BusinessException {
		try {
			return new InsertAction().insert(bills);
		} catch (Exception ex) {
			ExceptionUtils.marsh(ex);
			return null;
		}
	}

	@Override
	public TransformVO[] query(String whereSql) throws BusinessException {
		try {
			BillQueryUtils<TransformVO> query = new BillQueryUtils<TransformVO>(
					new TransformVO());
			return query.queryBills(whereSql);
		} catch (Exception ex) {
			ExceptionUtils.marsh(ex);
			return null;
		}
	}

	@Override
	public TransformVO[] query(IQueryScheme queryScheme)
			throws BusinessException {
		try {
			BillQueryUtils<TransformVO> query = new BillQueryUtils<TransformVO>(
					new TransformVO());
			return query.queryBills(queryScheme);
		} catch (Exception ex) {
			ExceptionUtils.marsh(ex);
			return null;
		}
	}

	@Override
	public TransformVO[] update(TransformVO[] bills, TransformVO[] originbills)
			throws BusinessException {
		try {
			return new UpdateAction().update(bills, originbills);
		} catch (Exception ex) {
			ExceptionUtils.marsh(ex);
			return null;
		}
	}

	@Override
	public TransformVO pushSave(OnhandSNViewVO datavo, OnhandSNViewVO[] datavos)
			throws BusinessException {

		TransformVO bill = new TransformVO();
		TransformHeadVO headvo = createHeadVO(datavo, "4N", "4N-01");
		TransformBodyVO[] bodys = createBodyVO(datavo, datavos);
		bill.setParentVO(headvo);
		bill.setChildrenVO(bodys);
		// 数量
		BusiCalculator.getBusiCalculatorAtBS().calcNum(bill.getChildrenVO(),
				ICPubMetaNameConst.NNUM);
		IPFBusiAction service = NCLocator.getInstance().lookup(
				IPFBusiAction.class);
		service.processBatch(IPFActionName.WRITE, "4N",
				new TransformVO[] { bill }, null, null, null);
		service.processBatch(IPFActionName.APPROVE, "4N",
				new TransformVO[] { bill }, null, null, null);
		//
		ArrayList<GeneralInVO> allinList = new ArrayList<GeneralInVO>();
		ArrayList<GeneralOutVO> alloutList = new ArrayList<GeneralOutVO>();

		new BsProcessor().getInAndOutBillVO(new TransformVO[] { bill },
				allinList, alloutList);
		// 必须保证下面两行处理顺序是先出后入
		this.processBizDate(alloutList, "4I-06",datavo);
		this.processBizDate(allinList, "4A-06",datavo);

		service.processBatch(IPFActionName.WRITE, "4I",
				alloutList.toArray(new GeneralOutVO[alloutList.size()]), null,
				null, null);
		service.processBatch(IPFActionName.WRITE, "4A",
				allinList.toArray(new GeneralInVO[allinList.size()]), null,
				null, null);
		return null;
	}

	private TransformHeadVO createHeadVO(OnhandSNViewVO datavo,
			String billtype, String transtype) throws DAOException {
		TransformHeadVO headvo = new TransformHeadVO();
		ICBSContext context = new ICBSContext();
		// 设置主组织默认值
		headvo.setPk_group(datavo.getPk_group());
		headvo.setPk_org(datavo.getPk_org());
		headvo.setPk_org_v(context.getOrgInfo()
				.getCalBodyVO(datavo.getPk_org()).getPk_vid());
		headvo.setFbillflag(BillStatusEnum.FREE.toIntValue());
		// 设置单据状态、单据业务日期默认值
		headvo.setDbilldate(AppContext.getInstance().getBusiDate());
		headvo.setCtrantypeid(PfServiceScmUtil.getTrantypeidByCode(
				new String[] { transtype }).get(transtype));
		headvo.setIprintcount(0);
		headvo.setVtrantypecode(transtype);
		headvo.setDmakedate(AppContext.getInstance().getBusiDate());
		headvo.setBillmaker(AppContext.getInstance().getPkUser());
		headvo.setCreationtime(AppContext.getInstance().getServerTime());
		headvo.setCreator(AppContext.getInstance().getPkUser());
		headvo.setVdef20(getDefDocVOPK());
		return headvo;
	}

	private TransformBodyVO[] createBodyVO(OnhandSNViewVO datavo,
			OnhandSNViewVO[] datavos) {
		List<TransformBodyVO> list = new ArrayList<>();

		int len = datavos.length;
		ICBSContext context = new ICBSContext();
		TransformBodyVO vo = getTransformBodyVO(datavo, context);
		vo.setPk_batchcode(datavo.getPk_batchcode());
		vo.setFbillrowflag(TransformRowFlag.BEFORECONVERT.toIntValue());
		list.add(vo);
		for (int i = 0; i < len; i++) {
			TransformBodyVO body = getTransformBodyVO(datavos[i], context);
			body.setFbillrowflag(TransformRowFlag.AFTERCONVERT.toIntValue());
			body.setVfree1(body.getVfree1()+((i+1)+""));
			list.add(body);
		}
		TransformBodyVO[] bodys = list
				.toArray(new TransformBodyVO[list.size()]);
		new UnitAndHslProc(new ICSysParam(datavo.getPk_group()), InvInfoUIQuery
				.getInstance().getInvInfoQuery()).procDefaltUnitHsl(bodys);
		// 同步表体批次辅助字段
		new BatchSynchronizer(new ICBatchFields()).fillBatchVOtoBill(bodys);
		// 同步表体序列号辅助字段
		new SnCodeSynchronizer(new ICSnFields()).fillBatchVOtoBill(bodys);
		VORowNoUtils.setVOsRowNoByRule(bodys, ICPubMetaNameConst.CROWNO);// 行号处理
		return bodys;
	}

	private TransformBodyVO getTransformBodyVO(OnhandSNViewVO datavo,
			ICBSContext context) {
		TransformBodyVO body = new TransformBodyVO();
		body.setCbodywarehouseid(datavo.getCwarehouseid());

		body.setNnum(datavo.getNonhandnum());
		body.setNassistnum(UFDouble.ONE_DBL);

		body.setCmaterialoid(datavo.getCmaterialoid());
		body.setCmaterialvid(datavo.getCmaterialvid());
		body.setCunitid(context.getInvInfo()
				.getInvBasVO(body.getCmaterialvid()).getPk_measdoc());
		body.setPk_group(datavo.getPk_group());
		body.setPk_org(datavo.getPk_org());
		body.setPk_batchcode(datavo.getPk_batchcode());
		body.setCstateid(datavo.getCstateid());
		body.setCastunitid(datavo.getCastunitid());
		body.setVchangerate(datavo.getVchangerate());
		body.setVfree1(datavo.getVfree1());
		return body;
	}

	protected String getStateid() {
		VOQuery<StoreStateVO> query = new VOQuery<StoreStateVO>(
				StoreStateVO.class);
		StoreStateVO[] query2 = query.query(" and dr=0 and vname='可用'", null);
		// cstateid 库存状态
		String cstateid = "~";
		if (query2 != null && query2.length > 0) {
			cstateid = query2[0].getPk_storestate();
		}
		return cstateid;
	}

	protected String getDefDocVOPK() throws DAOException {
		List<DefdocVO> idc = (List<DefdocVO>) new BaseDAO().retrieveByClause(
				DefdocVO.class,
				"  pk_defdoclist in (select pk_defdoclist from   bd_defdoclist t where t.code = 'XTLXTYPE') and code = '01'and nvl(dr,0) = 0 ");
		if (idc == null || idc.size() == 0) {
			return null;
		}
	
		return idc.get(0).getPk_defdoc();
	}

	/**
	 * 处理有实收发的单据的业务日期
	 * 
	 * @param bills
	 */
	private void processBizDate(List<? extends ICBillVO> bills, String billtype,OnhandSNViewVO datavo) {
		if (ValueCheckUtil.isNullORZeroLength(bills)) {
			return;
		}
		ICBSContext context = new ICBSContext();
		UFDate dbizdate = context.getBizDate();
		for (ICBillVO icBillVO : bills) {
			icBillVO.getParentVO().setVtrantypecode(billtype);
			icBillVO.getParentVO().setCtrantypeid(
					PfServiceScmUtil.getTrantypeidByCode(
							new String[] { billtype }).get(billtype));
			icBillVO.getParentVO().setPk_org_v(
					context.getOrgInfo()
							.getCalBodyVO(icBillVO.getParentVO().getPk_org())
							.getPk_vid());
			icBillVO.getParentVO().setDmakedate(AppContext.getInstance().getBusiDate());
			icBillVO.getParentVO().setBillmaker(AppContext.getInstance().getPkUser());
			icBillVO.getParentVO().setCreationtime(AppContext.getInstance().getServerTime());
			icBillVO.getParentVO().setCreator(AppContext.getInstance().getPkUser());
			icBillVO.getParentVO().setVdef20(null);
			for (ICBillBodyVO body : icBillVO.getBodys()) {
				if (body.getNnum() != null || body.getNassistnum() != null) {
					body.setDbizdate(dbizdate);
					body.setAttributeValue("csnunitid", body.getCastunitid());
					body.setVfree1(datavo.getVfree1());
					body.setPk_serialcode(datavo.getPk_serialcode());
					body.setVserialcode(datavo.getVsncode());
					if(StringUtil.isEmpty(body.getCsourcebillhid())){
						body.setCsourcetype(null);
					}
				}
			}
			VORowNoUtils.setVOsRowNoByRule(icBillVO.getBodys(),
					ICPubMetaNameConst.CROWNO);// 行号处理
		}
	}

	public static class BsProcessor extends TransformVOProcessor {
		// 如果父类的VO交换不好用，此方法做处理
		@Override
		protected ICBillVO[] getGeneralBill(TransformHeadVO head,
				List<TransformBodyVO> transformbodys, boolean isInBill) {
			return super.getGeneralBill(head, transformbodys, isInBill);
		}

	}

	@Override
	public GeneralInVO pushSaveIn(OnhandSNViewVO datavo,
			OnhandSNViewVO[] datavos) throws BusinessException {
		TransformVO bill = new TransformVO();
		TransformHeadVO headvo = createHeadVO(datavo, "4N", "4N-01");
		TransformBodyVO[] bodys = createBodyVO(datavo, datavos);
		datavo.setNonhandnum(datavos[0].getNonhandnum());
		for(TransformBodyVO body :bodys){
			body.setVfree1(datavo.getVfree1());
		}
		bill.setParentVO(headvo);
		bill.setChildrenVO(bodys);
		// 数量
		BusiCalculator.getBusiCalculatorAtBS().calcNum(bill.getChildrenVO(),
				ICPubMetaNameConst.NNUM);
		IPFBusiAction service = NCLocator.getInstance().lookup(
				IPFBusiAction.class);
//		service.processBatch(IPFActionName.WRITE, "4N",
//				new TransformVO[] { bill }, null, null, null);
//		service.processBatch(IPFActionName.APPROVE, "4N",
//				new TransformVO[] { bill }, null, null, null);
		//
		ArrayList<GeneralInVO> allinList = new ArrayList<GeneralInVO>();
		ArrayList<GeneralOutVO> alloutList = new ArrayList<GeneralOutVO>();

		new BsProcessor().getInAndOutBillVO(new TransformVO[] { bill },
				allinList, alloutList);
		//YF602-称量差异入库是否自动签字
		this.processBizDate(allinList, "4A-Cxx-23-04",datavo);
		service.processBatch(IPFActionName.WRITE, "4A",
				allinList.toArray(new GeneralInVO[allinList.size()]), null,
				null, null);
		Object pk_org = datavo.getPk_org();
		Object o = null;
		try {
			o = HYPubBO_Client.findColValue("pub_sysinit", "value",
					" nvl(dr,0)= 0 and pk_org = '" + pk_org + "' and initcode = 'YF602'");
		} catch (UifException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		
		if("是".equals(o)){
			service.processBatch("SIGN", "4A",
					allinList.toArray(new GeneralInVO[allinList.size()]), null,null, null);
		}
		return allinList.get(0);
	}

	@Override
	public GeneralOutVO pushSaveOut(OnhandSNViewVO datavo,
			OnhandSNViewVO[] datavos) throws BusinessException {
		TransformVO bill = new TransformVO();
		TransformHeadVO headvo = createHeadVO(datavo, "4N", "4N-01");
		TransformBodyVO[] bodys = createBodyVO(datavo, datavos);
		for(TransformBodyVO body :bodys){
			body.setVfree1(datavo.getVfree1());
		}
		bill.setParentVO(headvo);
		bill.setChildrenVO(bodys);
		// 数量
		BusiCalculator.getBusiCalculatorAtBS().calcNum(bill.getChildrenVO(),
				ICPubMetaNameConst.NNUM);
		IPFBusiAction service = NCLocator.getInstance().lookup(
				IPFBusiAction.class);
//		service.processBatch(IPFActionName.WRITE, "4N",
//				new TransformVO[] { bill }, null, null, null);
//		service.processBatch(IPFActionName.APPROVE, "4N",
//				new TransformVO[] { bill }, null, null, null);
		//
		ArrayList<GeneralInVO> allinList = new ArrayList<GeneralInVO>();
		ArrayList<GeneralOutVO> alloutList = new ArrayList<GeneralOutVO>();

		new BsProcessor().getInAndOutBillVO(new TransformVO[] { bill },
				allinList, alloutList);
		this.processBizDate(alloutList, "4I-Cxx-23-04",datavo);
		service.processBatch(IPFActionName.WRITE, "4I",
				alloutList.toArray(new GeneralOutVO[alloutList.size()]), null,
				null, null);
		//YF601-称量差异出库单是否自动签字
		Object pk_org = datavo.getPk_org();
		Object o = null;
		try {
			o = HYPubBO_Client.findColValue("pub_sysinit", "value",
					" nvl(dr,0)= 0 and pk_org = '" + pk_org + "' and initcode = 'YF601'");
		} catch (UifException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		
		if("是".equals(o)){
			service.processBatch("SIGN", "4I",
					alloutList.toArray(new GeneralOutVO[alloutList.size()]), null,null, null);
		}
		return alloutList.get(0);
	}
}
