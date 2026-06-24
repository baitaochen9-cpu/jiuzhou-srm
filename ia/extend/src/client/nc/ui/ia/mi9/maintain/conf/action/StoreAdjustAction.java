package nc.ui.ia.mi9.maintain.conf.action;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.framework.common.NCLocator;
import nc.itf.ia.mi9.II9Maintain;
import nc.itf.ia.mi9.II9MaintainApp;
import nc.itf.scmpub.reference.uap.pf.PFConfig;
import nc.itf.scmpub.reference.uap.pf.PfServiceScmUtil;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.jdbc.framework.processor.ColumnListProcessor;
import nc.ui.ia.bill.adjust.maintain.view.AdjustBillForm;
import nc.ui.ia.bill.adjust.maintain.view.AdjustListView;
import nc.ui.pubapp.uif2app.query2.model.IModelDataManager;
import nc.ui.uif2.NCAction;
import nc.ui.uif2.ShowStatusBarMsgUtil;
import nc.ui.uif2.model.BillManageModel;
import nc.vo.ia.mi3.entity.I3ItemVO;
import nc.vo.ia.mi9.entity.I9BillVO;
import nc.vo.ia.mi9.entity.I9HeadVO;
import nc.vo.ia.mi9.entity.I9ItemVO;
import nc.vo.ic.m46.entity.FinProdInVO;
import nc.vo.pf.pub.BusitypeVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.scmpub.util.VOEntityUtil;
import nc.vo.trade.voutils.SafeCompute;
import nc.vo.uif2.LoginContext;
import nccloud.pubitf.scmpub.pub.service.ISCMPubQueryService;

import org.apache.commons.lang.StringUtils;

/**
 * 库存调整单--调整验证批次
 * 
 * @author liyf
 * 
 */
public class StoreAdjustAction extends NCAction {
	private static final long serialVersionUID = 510372490113110846L;
	private AdjustListView list;
	private BillManageModel model;
	private AdjustBillForm form;
	private IModelDataManager dataManager;

	public StoreAdjustAction() {
//		SCMActionInitializer.initializeAction(this, "Confirm");
		this.setCode("tzyzpc");
		this.setBtnName("调整验证批次");
	}

	public void doAction(ActionEvent e) throws Exception {
		LoginContext context = this.form.getModel().getContext();
		if (StringUtils.isBlank(context.getPk_org())) {
			throw new BusinessException("请先选择成本域");
		}

		String caccountperiod = this.form.getBillCardPanel()
				.getHeadItem("caccountperiod").getValue();
		if (StringUtils.isBlank(caccountperiod)) {
			throw new BusinessException("请先选择会计期间");
		}

		try {
			I9BillVO[] retbills = changeFinProdInVO(context.getPk_org(),
					caccountperiod);
			II9Maintain service = (II9Maintain) NCLocator.getInstance().lookup(
					II9Maintain.class);
			retbills = service.insertI9(retbills);
			//保存返回的是差异VO，重新查询全VO
			String[] ids = VOEntityUtil.getPksFromAggVO(retbills);
			II9MaintainApp qry = NCLocator.getInstance().lookup(II9MaintainApp.class);
			I9BillVO[] billVOs = qry.queryI9App(ids);
			//切换到列表
			getList().showMeUp();
			//初始化数据
			getModel().initModel(billVOs);
		} catch (BusinessException exception) {
			ExceptionUtils.wrappException(exception);
		}
		ShowStatusBarMsgUtil.showStatusBarMsg("调整验证批次完成！", getModel()
				.getContext());
		
	}

	private I9BillVO[] changeFinProdInVO(String pk_org, String caccountperiod)
			throws BusinessException {
		FinProdInVO[] ins = getFinProdInVO(pk_org, caccountperiod);

		if (ins == null || ins.length == 0) {
			throw new BusinessException("没有需要调整的产成品入库单");
		}

		I9BillVO[] bills = getI9BillVO(ins, pk_org, caccountperiod);

		List<I9BillVO> list = new ArrayList<>();
		for (FinProdInVO prod : ins) {

			I9HeadVO headvo = getI9HeadVO(prod);
			I9ItemVO[] items = getI9ItemVO(prod);
			I9BillVO aggvo = new I9BillVO();
			aggvo.setParentVO(headvo);
			// VORowNoUtils.setVOsRowNoByRule(items, "crowno");
			aggvo.setChildrenVO(items);
			list.add(aggvo);
		}
		return bills;
	}

	private I9BillVO[] getI9BillVO(FinProdInVO[] ins, String pk_org,
			String caccountperiod) throws BusinessException {

		String[] cgeneralbids = VOEntityUtil.getVOsNotRepeatValue(
				VOEntityUtil.getBodyVOs(ins), "cgeneralbid");
		IUAPQueryBS iuap = (IUAPQueryBS) NCLocator.getInstance().lookup(
				IUAPQueryBS.class.getName());

		String sql = " select * from ia_i3bill_b  where nvl(dr,0) = 0 and "
				+ append("csrcbid", cgeneralbids);
		List<I3ItemVO> items = (List<I3ItemVO>) iuap.executeQuery(sql,
				new BeanListProcessor(I3ItemVO.class));

		Map<String, I3ItemVO> map = new HashMap<String, I3ItemVO>();
		for (I3ItemVO item : items) {
			map.put(item.getCsrcbid(), item);
		}

		I9BillVO[] bills = PfServiceScmUtil.executeVOChange("46", "I9", ins);
		UFDouble fl = new UFDouble(-1);
		BusitypeVO[] busi = PFConfig.querybillBusinessType(ins[0].getParentVO()
				.getPk_group(), "I9");
//		String begindate = caccountperiod + "-01";
//		UFDate date = new UFDate(begindate);
//		UFDate dateEnd = new UFDate(caccountperiod + "-"+date.getDaysMonth());
		for (I9BillVO bill : bills) {
//			bill.getParentVO().setDbilldate(dateEnd);
			bill.getParentVO().setPk_org(pk_org);
			bill.getParentVO().setPk_book("1001V110000000001GNQ");
			bill.getParentVO().setCaccountperiod(caccountperiod);
			bill.getParentVO().setPk_group(ins[0].getParentVO().getPk_group());
			bill.getParentVO().setCbiztypeid(
					busi != null && busi.length > 0 ? busi[0].getPk_busitype()
							: null);
			bill.getParentVO().setVnote("调整验证批次");
			I9ItemVO[] i9tems = bill.getChildrenVO();

			for (I9ItemVO item : i9tems) {
				I3ItemVO i83tem = map.get(item.getCsrcbid());
				if (i83tem == null) {
					continue;
				}

				item.setCcalcid(i83tem.getCcalcid());
				item.setCcalcthreadid(i83tem.getCcalcthreadid());
				item.setFcalcbizflag(i83tem.getFcalcbizflag());
				item.setFcalcthreadbizflag(i83tem.getFcalcthreadbizflag());
//				item.setNadjustnum(SafeCompute.multiply(i83tem.getNnum(), fl));
//				item.setNfactor1(SafeCompute.multiply(i83tem.getNmny(), fl));
				item.setNmny(SafeCompute.multiply(i83tem.getNmny(), fl));
			}

		}
		return bills;
	}

	private String append(String name, String[] values) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(" ");
		buffer.append(name);
		buffer.append(" in (");
		int length = values.length;
		for (int i = 0; i < length; i++) {
			buffer.append("'" + values[i] + "'");
			buffer.append(",");
		}
		length = buffer.length();
		buffer.deleteCharAt(length - 1);
		buffer.append(") ");
		return buffer.toString();
	}

	private I9HeadVO getI9HeadVO(FinProdInVO prod) {
		I9HeadVO hvo = new I9HeadVO();
		hvo.setStatus(VOStatus.NEW);
		return hvo;
	}

	private I9ItemVO[] getI9ItemVO(FinProdInVO prod) {

		List<I9ItemVO> list = new ArrayList<>();
		I9ItemVO bvo = new I9ItemVO();
		bvo.setStatus(VOStatus.NEW);

		list.add(bvo);
		return list.toArray(new I9ItemVO[list.size()]);
	}

	private FinProdInVO[] getFinProdInVO(String pk_org, String caccountperiod)
			throws BusinessException {
		if (StringUtils.isBlank(pk_org)) {
			throw new BusinessException("请先选择成本域");
		}

		if (StringUtils.isBlank(caccountperiod)) {
			throw new BusinessException("请先选择会计期间");
		}

		UFDate  begindate = new UFDate( caccountperiod + "-01").asBegin();
		UFDate  enddate =begindate.getDateAfter(begindate.getDaysMonth()).asEnd() ;
//		begindate = "2022-05-11";
		IUAPQueryBS iuap = (IUAPQueryBS) NCLocator.getInstance().lookup(
				IUAPQueryBS.class.getName());
		StringBuffer sql = new StringBuffer();
		sql.append(" select distinct cb.cgeneralhid from ic_finprodin_b cb   ");//库存-产成品入库单
		sql.append(" join  ia_i3bill_b ib on cb.cgeneralbid = ib.csrcbid ");//存货核算-产成品入库单
		sql.append(" join mm_mo oo on cb.cfirstbillbid = oo.cmoid ");//流程生产订单
//		sql.append(" join mm_plo lo on oo.vsrcid = lo.cpoid ");//计划订单
		sql.append(" left join bd_project t on oo.cprojectid = t.pk_project ");//项目
		sql.append(" join  org_costregion osn on cb.pk_org =  osn.pk_org");//成本域
		sql.append("  where  nvl(cb.dr,0)=0 and  nvl(oo.dr,0)=0  ");
		sql.append(" and cb.dbizdate >= '"+begindate+"' and  cb.dbizdate <= '" +enddate +"'");
		sql.append(" and ib.nmny <> 0 ");//产成品入库单表已经有了金额
		sql.append(" and  t.PROJECT_CODE like 'VAL%' ");
		sql.append(" and osn.pk_costregion = '"+pk_org+"'" );
		sql.append(" and  not exists (select i9b.csrcbid from  ia_i9bill_b i9b  where  cb.cgeneralbid = i9b.csrcbid and nvl(i9b.dr,0)=0 and i9b.pk_org ='"+ pk_org + "')" );
		List<String> list = (List<String>) iuap.executeQuery(sql.toString(),
				new ColumnListProcessor());

		ISCMPubQueryService query = (ISCMPubQueryService) NCLocator
				.getInstance().lookup(ISCMPubQueryService.class.getName());
		FinProdInVO[] bills = query.billquery(FinProdInVO.class,
				list.toArray(new String[list.size()]));
		return bills;
	}

	public AdjustListView getList() {
		return this.list;
	}

	public void setList(AdjustListView list) {
		this.list = list;
	}

	public void setModel(BillManageModel model) {
		this.model = model;
		model.addAppEventListener(this);
	}

	public BillManageModel getModel() {
		return this.model;
	}

	public AdjustBillForm getForm() {
		return form;
	}

	public void setForm(AdjustBillForm form) {
		this.form = form;
	}

	public IModelDataManager getDataManager() {
		return dataManager;
	}

	public void setDataManager(IModelDataManager dataManager) {
		this.dataManager = dataManager;
	}

}