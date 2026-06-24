package nc.bs.jzyy.sys.oa.saleorder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import nc.api.rest.arap.utils.ArapBillConvert;
import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.ic.pub.env.ICBSContext;
import nc.bs.jzyy.sys.AbstracAdapter4Ext;
import nc.bs.logging.Logger;
import nc.bs.scmpub.query.SCMBillQuery;
import nc.bs.uap.lock.PKLock;
import nc.impl.pubapp.pattern.data.bill.BillQuery;
import nc.impl.pubapp.pattern.data.vo.VOQuery;
import nc.itf.arap.fieldmap.IBillFieldGet;
import nc.itf.scmpub.reference.uap.bd.customer.CustomerPubService;
import nc.itf.scmpub.reference.uap.org.DeptPubService;
import nc.itf.scmpub.reference.uap.org.OrgUnitPubService;
import nc.itf.scmpub.reference.uap.pf.PfServiceScmUtil;
import nc.itf.so.m30.IQueryRelationOrg;
import nc.itf.so.m30trantype.IM30TranTypeService;
import nc.itf.uap.pf.IPFBusiAction;
import nc.uif.pub.exception.UifException;
import nc.vo.bd.cust.saleinfo.CustsaleVO;
import nc.vo.ct.saledaily.entity.AggCtSaleVO;
import nc.vo.ct.saledaily.entity.CtSaleBVO;
import nc.vo.ic.material.define.InvBasVO;
import nc.vo.ic.material.define.InvMeasVO;
import nc.vo.ic.org.OrgVO;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.pubapp.pattern.model.entity.bill.IBill;
import nc.vo.pubapp.pattern.pub.PubAppTool;
import nc.vo.so.m30.entity.SaleOrderBVO;
import nc.vo.so.m30.entity.SaleOrderHVO;
import nc.vo.so.m30.entity.SaleOrderVO;
import nc.vo.so.m30.pub.SaleOrderVOCalculator;
import nc.vo.so.m30.rule.HeadTotalCalculateRule;
import nc.vo.so.m30trantype.entity.M30TranTypeVO;
import nc.vo.so.pub.SOItemKey;
import nc.vo.so.pub.keyvalue.IKeyValue;
import nc.vo.so.pub.keyvalue.VOKeyValue;
import nc.vo.so.pub.rule.ReceiveCustDefAddrRule;
import nc.vo.so.pub.rule.SOBuysellTriaRule;
import nc.vo.so.pub.rule.SOCountryInfoRule;
import nc.vo.so.pub.rule.SOCurrencyRule;
import nc.vo.so.pub.rule.SOCustRelaDefValueRule;
import nc.vo.so.pub.rule.SOExchangeRateRule;
import nc.vo.so.pub.rule.SOProfitCenterValueRule;
import nc.vo.so.pub.rule.SOTaxInfoRule;
import nc.vo.so.pub.rule.SOUnitChangeRateRule;
import nc.vo.so.pub.rule.SaleOrgRelationRule;
import nc.vo.trade.checkrule.VOChecker;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class SaleOrderSave extends AbstracAdapter4Ext {

	@Override
	public JSONObject sys(Object billvo) throws BusinessException {
		// SaleOrderResource
		// nc.ui.so.m30.billui.editor.bodyevent.BodyBeforeEditHandler
		JSONObject jsonObject = (JSONObject) billvo;
		// 检索数据
		SaleOrderVO order = null;
		try {
			JSONObject bill = jsonObject.getJSONObject("bill");

			order = getSaleOrderVO(bill);
			processBill(order, bill);
		} catch (Exception e) {
			e.printStackTrace();
			Logger.error(e.getMessage(), e);
			return getRsultJsonFailed("生成销售订单出错:" + e.getMessage());
		}
		JSONObject data = new JSONObject();
		data.put("code", order.getParentVO().getVbillcode());
		data.put("bill_id", order.getParentVO().getPrimaryKey());
		// 将结果返回
		return getRsultDataSuccess(data, "生成销售订单成功");
	}

	private SaleOrderVO getSaleOrderVO(JSONObject bill)
			throws BusinessException {

		SaleOrderVO order = new SaleOrderVO();
		SaleOrderHVO hvo = getSaleOrderHVO(bill);
		isLock(hvo.getVtrantypecode(), hvo.getVbillcode(), hvo.getPk_org());
		List<SaleOrderBVO> list = getSaleOrderBVOs(hvo, bill);
		order.setParent(hvo);
		order.setChildrenVO(list.toArray(new SaleOrderBVO[list.size()]));
		return order;
	}

	private SaleOrderHVO getSaleOrderHVO(JSONObject bill)
			throws BusinessException {
		SaleOrderHVO hvo = new SaleOrderHVO();
		hvo.setStatus(VOStatus.NEW);

		String group_code = bill.getString("group_code");
		if (getString_TrimZeroLenAsNull(group_code) == null) {
			throw new BusinessException("集团不能为空");
		}

		String pk_group = getGroup(group_code);

		if (getString_TrimZeroLenAsNull(pk_group) == null) {
			throw new BusinessException("集团" + group_code + "在nc中不存在");
		}
		hvo.setPk_group(pk_group);

		String org_code = bill.getString("org_code");
		if (getString_TrimZeroLenAsNull(org_code) == null) {
			throw new BusinessException("销售组织不能为空");
		}

		String pk_org = getSalesorg(org_code);

		if (getString_TrimZeroLenAsNull(pk_org) == null) {
			throw new BusinessException("销售组织" + org_code + "在nc中不存在");
		}
		hvo.setPk_org(pk_org);// 销售组织
		ICBSContext context = new ICBSContext();
		OrgVO orgvo = context.getOrgInfo().getOrgVO(pk_org);
		if (!pk_group.equals(orgvo.getPk_group())) {
			throw new BusinessException("销售组织" + org_code + "不属于集团"
					+ group_code);
		}
		if (StringUtil.isEmpty(InvocationInfoProxy.getInstance().getGroupId())) {
			InvocationInfoProxy.getInstance().setGroupId(orgvo.getPk_group());
		}
		hvo.setPk_org_v(orgvo.getPk_vid());// 销售组织vid

		String dept_code = bill.getString("dept_code");
		if (getString_TrimZeroLenAsNull(dept_code) == null) {
			throw new BusinessException("销售部门不能为空");
		}
		String cdeptid = getCdeptid(dept_code, pk_org);
		if (getString_TrimZeroLenAsNull(cdeptid) == null) {
			throw new BusinessException("销售部门" + dept_code + "在nc中不存在");
		}
		hvo.setCdeptid(cdeptid);// 销售部门
		orgvo = context.getOrgInfo().getOrgVO(cdeptid);
		hvo.setCdeptvid(orgvo.getPk_vid());// 销售部门id

		String vbillcode = bill.getString("vbillcode");
		if (getString_TrimZeroLenAsNull(vbillcode) == null) {
			throw new BusinessException("单据号不能为空");
		}
		hvo.setVbillcode(vbillcode);// 单据号
		hvo.setVcooppohcode(vbillcode);// 对方单据号

		hvo.setIversion(bill.getIntValue("version"));

		String dbilldate = bill.getString("billdate");
		if (getString_TrimZeroLenAsNull(dbilldate) == null) {
			throw new BusinessException("单据日期不能为空");
		}
		hvo.setDbilldate(getUFDate(dbilldate));// 单据日期
		hvo.setDmakedate(getUFDate(dbilldate));// 制单日期

		String customer = bill.getString("customer");
		if (getString_TrimZeroLenAsNull(customer) == null) {
			throw new BusinessException("客户不能为空");
		}
		String ccustomerid = getCustomer(customer);
		if (getString_TrimZeroLenAsNull(ccustomerid) == null) {
			throw new BusinessException("客户" + customer + "在nc中不存在");
		}
		hvo.setCcustomerid(ccustomerid);// 客户
		hvo.setBpreceiveflag(UFBoolean.FALSE);// 收款限额控制预收

		setCustRelaDefValue(hvo);
		hvo.setChreceivecustid(ccustomerid);// 收货客户

		String chreceiveaddid = bill.getString("chreceiveaddid");
		if (getString_TrimZeroLenAsNull(chreceiveaddid) != null) {
			hvo.setChreceiveaddid(chreceiveaddid);// 收货地址
		} else {

			String[] defadds = CustomerPubService.getDefaultAddresses(
					new String[] { hvo.getChreceivecustid() }, pk_org);
			String defaddValue = null;
			if (null != defadds && defadds.length > 0) {
				defaddValue = defadds[0];
			}
			hvo.setChreceiveaddid(defaddValue);// 收货地址
		}

		String defacc = CustomerPubService.getDefaultBankAcc(hvo
				.getChreceivecustid());
		hvo.setCcustbankaccid(defacc);// 开户银行账户

		ArapBillConvert convert = new ArapBillConvert();

		String corigcurrency = bill.getString("corigcurrency");
		String pk_cuerrency = null;
		if (getString_TrimZeroLenAsNull(corigcurrency) == null) {
			pk_cuerrency = getOrgCurr(hvo);
		} else {
			pk_cuerrency = (String) convert.tranlate(hvo,
					IBillFieldGet.PK_CURRTYPE, corigcurrency);
			if (getString_TrimZeroLenAsNull(pk_cuerrency) == null) {
				throw new BusinessException("币种" + corigcurrency + "在nc中不存在");
			}
		}

		hvo.setCorigcurrencyid(pk_cuerrency);// 原币

		String payterm = bill.getString("payterm");// 收款协议
		if (getString_TrimZeroLenAsNull(payterm) == null) {
		} else {
			String cpaytermid = (String) convert.tranlate(hvo, "ys_"
					+ IBillFieldGet.PK_PAYTERM, payterm);
			if (getString_TrimZeroLenAsNull(cpaytermid) == null) {
				throw new BusinessException("收款协议" + payterm + "在nc中不存在");
			}
			hvo.setCpaytermid(cpaytermid);
		}

		hvo.setNtotalnum(getUFDouble_NullAsZero(bill
				.getBigDecimal("ntotalastnum")));

		hvo.setNtotalorigmny(getUFDouble_NullAsZero(bill
				.getBigDecimal("ntotalorigmny")));
		String creator = bill.getString("creator");
		if (getString_TrimZeroLenAsNull(creator) == null) {
			throw new BusinessException("制单人不能为空");
		}
		String userid = (String) convert.tranlate(hvo, IBillFieldGet.BILLMAKER,
				creator);
		if (getString_TrimZeroLenAsNull(userid) == null) {
			throw new BusinessException("制单人" + creator + "在nc中不存在");
		}

		InvocationInfoProxy.getInstance().setUserId(userid);
		hvo.setBillmaker(InvocationInfoProxy.getInstance().getUserId());// 制单人
		hvo.setCreator(InvocationInfoProxy.getInstance().getUserId());
		String creationtime = bill.getString("creationtime");
		if (getString_TrimZeroLenAsNull(creationtime) != null) {
			hvo.setCreationtime(new UFDateTime(creationtime));
		}

		String ts = bill.getString("ts");
		if (getString_TrimZeroLenAsNull(ts) != null) {
			hvo.setTs(new UFDateTime(ts));
		}

		// //30-Cxx-23-01 瑞博普通销售订单
		// String vtrantypecode = bill.getString("vtrantypecode");

		String vtrantypecode = "30-Cxx-23-01";
		if (getString_TrimZeroLenAsNull(vtrantypecode) == null) {
			throw new BusinessException("单据类型不能为空");
		}

		Map<String, String> map = PfServiceScmUtil
				.getTrantypeidByCode(new String[] { vtrantypecode });
		hvo.setCtrantypeid(map == null ? null : map.get(vtrantypecode));// 订单类型
		hvo.setVtrantypecode(vtrantypecode);// 订单类型编码

		String busitype = PfServiceScmUtil.getBusitype("30", vtrantypecode,
				hvo.getPk_org(), hvo.getBillmaker());
		hvo.setCbiztypeid(busitype);// 业务流程

		hvo.setVdef1(getString_TrimZeroLenAsNull(bill.getString("def1")));// def1
		hvo.setVdef2(getString_TrimZeroLenAsNull(bill.getString("def2")));// def2
		hvo.setVdef3(getString_TrimZeroLenAsNull(bill.getString("def3")));// def3
		hvo.setVdef4(getString_TrimZeroLenAsNull(bill.getString("def4")));// def4
		hvo.setVdef5(getString_TrimZeroLenAsNull(bill.getString("def5")));// def5

		hvo.setVnote(getString_TrimZeroLenAsNull(bill.getString("vnote")));// 备注
		return hvo;

	}

	private void setCustRelaDefValue(SaleOrderHVO hvo) {

		UFDouble old_discountrate = hvo.getNdiscountrate();
		// 1.根据客户档案设置默认值
		String[] fieldNames = new String[] {
				// 专管部门、专管业务员、开票客户、运输方式
				CustsaleVO.RESPDEPT, CustsaleVO.RESPPERSON,
				CustsaleVO.BILLINGCUST,
				CustsaleVO.SHIPPINGTYPE,
				// 默认交易币种、默认收付款协议、整单折扣、渠道类型
				CustsaleVO.CURRENCYDEFAULT, CustsaleVO.PAYTERMDEFAULT,
				CustsaleVO.DISCOUNTRATE, CustsaleVO.CHANNEL,
				CustsaleVO.PK_TRADETERM, CustsaleVO.ISSUECUST };
		CustsaleVO retVO = this.getCustSaleVO(fieldNames, hvo);
		// 设置默认值
		// 没有专管业务员不清空，登陆用户默认业务员，问题ID：NCdp205106387
		String cemployeeid = retVO.getRespperson();
		if (!PubAppTool.isNull(cemployeeid)) {
			hvo.setCemployeeid(cemployeeid);
		}
		hvo.setCchanneltypeid(retVO.getChannel());// 销售渠道类型
		hvo.setCtradewordid(retVO.getPk_tradeterm());// 贸易术语
		hvo.setCtransporttypeid(retVO.getShippingtype());// 设置运输方式
		// hvo.setCpaytermid(retVO.getPaytermdefault());// 收款协议

		// 2.客户没有专管部门
		String deptid = retVO.getRespdept();
		if (!PubAppTool.isNull(deptid)) {
			hvo.setCdeptid(deptid);
			String[] pk_depts = new String[] { deptid };
			Map<String, String> mapvids = DeptPubService
					.getLastVIDSByDeptIDS(pk_depts);
			hvo.setCdeptvid(mapvids.get(deptid));
		}
		// 3.客户没有默认开票客户，取客户本身
		String invcus = retVO.getBillingcust();
		if (PubAppTool.isNull(invcus)) {
			invcus = hvo.getCcustomerid();
		}
		hvo.setCinvoicecustid(invcus);// 开票客户

		if (StringUtil.isEmpty(hvo.getCorigcurrencyid())) {
			// 4.客户没有默认币种，取销售组织本位币
			String origcurr = retVO.getCurrencydefault();
			if (PubAppTool.isNull(origcurr)) {
				origcurr = this.getOrgCurr(hvo);
			}
			hvo.setCorigcurrencyid(origcurr);// 原币
		}

		// 5.整单折扣优先级：1)客户档案默认 2)原单值 3)默认100
		UFDouble discountrate = retVO.getDiscountrate();

		if (null == discountrate) {
			if (null != old_discountrate) {
				discountrate = old_discountrate;
			} else {
				discountrate = new UFDouble(100);
			}
		}
		hvo.setNdiscountrate(discountrate);// 整单折扣

		// 6.清空原先散户字段值
		hvo.setCfreecustid(null);
		// 7.设置表体收货客户
		String rececust = retVO.getIssuecust();
		if (PubAppTool.isNull(rececust)) {
			rececust = hvo.getCcustomerid();
		}
		// 7.2 V635新增，表头收货客户
		hvo.setChreceivecustid(rececust);// 表头收货客户

	}

	private CustsaleVO getCustSaleVO(String[] fieldNames, SaleOrderHVO hvo) {

		String pk_org = hvo.getPk_org();
		String customer = hvo.getCcustomerid();
		if (PubAppTool.isNull(customer)) {
			return new CustsaleVO();
		}
		Map<String, CustsaleVO> mret = CustomerPubService.getCustSaleVOByPks(
				new String[] { customer }, pk_org, fieldNames);

		if (null == mret || mret.size() == 0) {
			return new CustsaleVO();
		}
		return mret.get(customer);
	}

	private String getOrgCurr(SaleOrderHVO hvo) {
		String pk_org = hvo.getPk_org();
		Map<String, String> orgCurrMap = null;

		orgCurrMap = OrgUnitPubService
				.queryOrgCurrByPk(new String[] { pk_org });

		if (null != orgCurrMap) {
			return orgCurrMap.get(pk_org);
		}
		return null;
	}

	private List<SaleOrderBVO> getSaleOrderBVOs(SaleOrderHVO hvo,
			JSONObject bill) throws BusinessException {

		List<SaleOrderBVO> list = new ArrayList<>();

		JSONArray arrays = bill.getJSONArray("bodys");
		if (arrays == null || arrays.size() == 0) {
			throw new BusinessException("传入表体数据不能为空");
		}

		String billtype = bill.getString("billtype");
		if (getString_TrimZeroLenAsNull(billtype) == null) {
			throw new BusinessException("订单类型不能为空");
		}
		ArapBillConvert convert = new ArapBillConvert();
		String cproject = bill.getString("cproject");// 项目
		String cprojectid = null;
		if (getString_TrimZeroLenAsNull(cproject) == null) {
		} else {
			cprojectid = (String) convert.tranlate(hvo, IBillFieldGet.PROJECT,
					cproject);
			if (getString_TrimZeroLenAsNull(cprojectid) == null) {
				throw new BusinessException("项目" + cproject + "在nc中不存在");
			}
		}

		UFDouble nexchangerate = getUFDouble_NullAsZero(bill
				.getBigDecimal("nexchangerate"));// 汇率
		String ct_sale = bill.getString("ct_sale");
		AggCtSaleVO[] aggvo = qryOrignBills(ct_sale, hvo.getPk_org());

		String areaid = getCustomerArea(hvo.getChreceivecustid());

		int size = arrays.size();
		ICBSContext context = new ICBSContext();
		for (int i = 0; i < size; i++) {
			SaleOrderBVO bvo = new SaleOrderBVO();
			bvo.setStatus(VOStatus.NEW);
			bvo.setDbilldate(hvo.getDbilldate());
			JSONObject jsonObject = arrays.getJSONObject(i);

			bvo.setBlargessflag(UFBoolean.FALSE);
			bvo.setFlargesstypeflag(1);

			if ("1".equals(billtype)) {
				bvo.setFbuysellflag(1);
			} else if ("2".equals(billtype)) {
				bvo.setFbuysellflag(3);
			}
//			bvo.setVfirstcode(ct_sale);

			String material_code = jsonObject.getString("material_code");
			if (getString_TrimZeroLenAsNull(material_code) == null) {
				throw new BusinessException("物料编码不能为空");
			}
			String cmaterialid = getInvcode(material_code);

			if (getString_TrimZeroLenAsNull(cmaterialid) == null) {
				throw new BusinessException("物料编码" + material_code + "在nc中不存在");
			}

			InvBasVO basvo = context.getInvInfo().getInvBasVO(cmaterialid);
			bvo.setCmaterialid(basvo.getPk_source());
			bvo.setCmaterialvid(cmaterialid);
			bvo.setCunitid(basvo.getPk_measdoc());

			String main_unite = jsonObject.getString("main_unite");
			if (getString_TrimZeroLenAsNull(main_unite) == null) {
				throw new BusinessException("主计量单位不能为空");
			}
			String cunitid = getCastunitid(main_unite);

			if (getString_TrimZeroLenAsNull(cunitid) == null) {
				throw new BusinessException("主计量单位" + main_unite + "在nc中不存在");
			}

			if (!cunitid.equals(bvo.getCunitid())) {
				throw new BusinessException("主计量单位" + main_unite + "与nc中物料编码"
						+ material_code + "不一致");
			}
			bvo.setNnum(getUFDouble_NullAsZero(jsonObject
					.getBigDecimal("mainamount")));// 主数量

			String unite = jsonObject.getString("unite");
			if (getString_TrimZeroLenAsNull(unite) == null) {
				throw new BusinessException("计量单位不能为空");
			}
			String castunitid = getCastunitid(unite);

			if (getString_TrimZeroLenAsNull(castunitid) == null) {
				throw new BusinessException("计量单位" + unite + "在nc中不存在");
			}

			bvo.setCastunitid(castunitid);
			bvo.setCqtunitid(castunitid);
			bvo.setNastnum(getUFDouble_NullAsZero(jsonObject
					.getBigDecimal("num")));// 数量
			bvo.setNqtunitnum(bvo.getNastnum());

			InvMeasVO measvo = context.getInvInfo().getInvMeasVO(
					bvo.getCmaterialvid(), castunitid);
			if (measvo != null) {
				bvo.setVchangerate(measvo.getMeasrate());
			} else {
				bvo.setVchangerate("1/1");
			}

			UFDouble conversion_rate = getUFDouble_NullAsZero(jsonObject
					.getBigDecimal("conversion_rate"));// 换算率
			if (conversion_rate.equals(bvo.getVchangerate())) {

			}

			String taxcode = jsonObject.getString("ctaxcodeid");
			if (getString_TrimZeroLenAsNull(taxcode) == null) {
				throw new BusinessException("税码不能为空");
			}
			String ctaxcodeid = getCtaxcodeid(taxcode);

			if (getString_TrimZeroLenAsNull(ctaxcodeid) == null) {
				throw new BusinessException("税码" + taxcode + "在nc中不存在");
			}
			bvo.setCtaxcodeid(ctaxcodeid);
			bvo.setNtaxrate(getUFDouble_NullAsZero(jsonObject
					.getBigDecimal("ntaxrate")));

			String corigcurrency = bill.getString("currtype");
			String pk_cuerrency = null;
			if (getString_TrimZeroLenAsNull(corigcurrency) == null) {
				pk_cuerrency = getOrgCurr(hvo);
			} else {
				pk_cuerrency = (String) convert.tranlate(hvo,
						IBillFieldGet.PK_CURRTYPE, corigcurrency);
				if (getString_TrimZeroLenAsNull(pk_cuerrency) == null) {
					throw new BusinessException("币种" + corigcurrency
							+ "在nc中不存在");
				}
			}

			bvo.setCcurrencyid(pk_cuerrency);
			bvo.setNexchangerate(nexchangerate);
			bvo.setNorigtaxprice(getUFDouble_NullAsZero(jsonObject
					.getBigDecimal("tax_price")));
			bvo.setNorigtaxmny(getUFDouble_NullAsZero(jsonObject
					.getBigDecimal("total")));

			bvo.setNqtorigprice(getUFDouble_NullAsZero(jsonObject
					.getBigDecimal("nqttaxprice")));
			// "nqttaxnetprice": 2121,

			String arrvstoorg = jsonObject.getString("arrvstoorg");
			if (getString_TrimZeroLenAsNull(arrvstoorg) == null) {
				throw new BusinessException("发货组织不能为空");
			}
			String csendstockorgid = getStockorg(arrvstoorg);

			if (getString_TrimZeroLenAsNull(csendstockorgid) == null) {
				throw new BusinessException("发货组织" + arrvstoorg + "在nc中不存在");
			}
			bvo.setCsendstockorgid(csendstockorgid);

			String recvstordoc = jsonObject.getString("recvstordoc");
			if (getString_TrimZeroLenAsNull(arrvstoorg) == null) {
				throw new BusinessException("发货仓库不能为空");
			}
			String csendstordocid = getStordoc(recvstordoc);

			if (getString_TrimZeroLenAsNull(csendstordocid) == null) {
				throw new BusinessException("发货仓库" + recvstordoc + "在nc中不存在");
			}
			bvo.setCsendstordocid(csendstordocid);

			String creceiveaddr = jsonObject.getString("creceiveaddr");
			if (getString_TrimZeroLenAsNull(creceiveaddr) == null) {
			} else {
				String creceiveaddrid = getStockorg(creceiveaddr);

				if (getString_TrimZeroLenAsNull(creceiveaddrid) == null) {
					throw new BusinessException("收获地址" + creceiveaddr
							+ "在nc中不存在");
				}
				bvo.setCreceiveaddrid(creceiveaddrid);
			}

			bvo.setCreceiveareaid(areaid);
			bvo.setCprojectid(cprojectid);
			bvo.setDsenddate(hvo.getDbilldate());
			bvo.setDreceivedate(hvo.getDbilldate());
			// String dsenddate = jsonObject.getString("dsenddate");
			// if (getString_TrimZeroLenAsNull(dsenddate) == null) {
			// throw new BusinessException("计划发货日不能为空");
			// }
			// bvo.setDsenddate(getUFDate(dsenddate));// 计划发货日
			//
			// String dreceivedate = jsonObject.getString("dreceivedate");
			// if (getString_TrimZeroLenAsNull(dreceivedate) == null) {
			// throw new BusinessException("计划到货日不能为空");
			// }
			// bvo.setDreceivedate(getUFDate(dreceivedate));// 计划到货日

			bvo.setVbdef1(getString_TrimZeroLenAsNull(bill.getString("def1")));// def1
			bvo.setVbdef2(getString_TrimZeroLenAsNull(bill.getString("def2")));// def2
			bvo.setVbdef3(getString_TrimZeroLenAsNull(bill.getString("def3")));// def3
			bvo.setVbdef4(getString_TrimZeroLenAsNull(bill.getString("def4")));// def4
			bvo.setVbdef5(getString_TrimZeroLenAsNull(bill.getString("def5")));// def5
			bvo.setVrownote(getString_TrimZeroLenAsNull(jsonObject
					.getString("vrownote")));// 备注

			/** -----需要匹配销售合同行 获取的信息------------------------------ **/
			if (aggvo != null && aggvo.length > 0) {

				String srcKey = bvo.getCmaterialid();
				CtSaleBVO matchedItemVO = matchedSrcBody(aggvo, srcKey);
				if (matchedItemVO != null) {
					bvo.setVsrctype("Z3");
					bvo.setCsrcbid(matchedItemVO.getPk_ct_sale_b());
					bvo.setCsrcid(matchedItemVO.getPk_ct_sale());

					bvo.setVfirsttype("Z3");
					bvo.setCfirstbid(matchedItemVO.getPk_ct_sale_b());
					bvo.setCfirstid(matchedItemVO.getPk_ct_sale());
					//
					// bvo.setCsendstordocid(matchedItemVO.getCsendstordocid());
					// bvo.setCsendstockorgid(matchedItemVO.getCsendstockorgid());
					// bvo.setCsendstockorgvid(matchedItemVO.getCsendstockorgvid());
				}

			}
			list.add(bvo);
		}

		return list;

	}

	private void setBodyInfo(SaleOrderVO bill, SaleOrderHVO hvo,
			SaleOrderBVO[] bvos, int rows[]) {
		M30TranTypeVO m30transvo = getTransType(hvo.getVtrantypecode(),
				hvo.getPk_group());
		boolean isBlrgcashflag = m30transvo.getBlrgcashflag().booleanValue();

		if (!isBlrgcashflag) {
			for (SaleOrderBVO bvo : bvos) {
				bvo.setBlrgcashflag(UFBoolean.FALSE);//
			}
		}

		for (SaleOrderBVO bvo : bvos) {
			if (bvo.getBlargessflag() == null)
				bvo.setBlargessflag(UFBoolean.FALSE);//
		}

		IKeyValue keyValue = new VOKeyValue(bill);

		// --2.并计算换算率
		SOUnitChangeRateRule unitrate = new SOUnitChangeRateRule(keyValue);
		// 性能优化：批量处理 add by zhangby5
		unitrate.calcAstAndQtChangeRate(rows);

		// --4.发货库存组织变化后更新结算财务组织、应收组织、利润中心
		SaleOrgRelationRule orgrelarule = new SaleOrgRelationRule(keyValue);
		orgrelarule.setFinanceStockOrg(rows,
				this.GetRelationOrg(keyValue, rows));
		for (SaleOrderBVO bvo : bvos) {
			bvo.setCsendstordocid(hvo.getVdef5());
		}
		// --5.根据仓库所属库存组织或者发货库存组织查询物流委托关系获得默认物流组织
		orgrelarule.setTrafficOrg(rows);
		// --6.计算结算财务组织对应的币种
		SOCurrencyRule currencyrule = new SOCurrencyRule(keyValue);
		currencyrule.setCurrency(rows);
		// --7.根据组织本位币重新计算折本汇率
		SOExchangeRateRule changeraterule = new SOExchangeRateRule(keyValue);
		changeraterule.calcBodyExchangeRates(rows);

		// --10.设置默认收货客户------要在设置国家之前
		String headreceivecustid = keyValue
				.getHeadStringValue(SaleOrderHVO.CHRECEIVECUSTID);
		if (!PubAppTool.isNull(headreceivecustid)) {
			setReceCustAndAdr(keyValue, rows, SaleOrderBVO.CRECEIVECUSTID,
					headreceivecustid);
		} else {
			SOCustRelaDefValueRule custrefrule = new SOCustRelaDefValueRule(
					keyValue);
			custrefrule.setRelaReceiveCust(rows);
		}

		// --设置客户收货地址
		String headreceiveaddid = keyValue
				.getHeadStringValue(SaleOrderHVO.CHRECEIVEADDID);
		ReceiveCustDefAddrRule defaddrule = new ReceiveCustDefAddrRule(keyValue);
		if (!PubAppTool.isNull(headreceiveaddid)) {
			defaddrule.setCustAddDocByAddr(rows);
		} else {
			defaddrule.setCustDefaultAddress(rows);
		}

		// 4.设置国家
		SOCountryInfoRule countryrule = new SOCountryInfoRule(keyValue);
		countryrule.setCountryInfo(rows);

		for (SaleOrderBVO bvo : bvos) {
			bvo.setCrececountryid(bvo.getCsendcountryid());
		}

		// 5.购销类型和三角贸易
		SOBuysellTriaRule buyflgrule = new SOBuysellTriaRule(keyValue);
		buyflgrule.setBuysellAndTriaFlag(rows);

		BodyDefaultValueRule defrule = new BodyDefaultValueRule(keyValue);
		// 传入的boolean值表示不需要执行
		defrule.setBodyDefValue(rows, true);

		// 利润中心取值规则，直运非直运业务均如此取值
		SOProfitCenterValueRule profitRule = new SOProfitCenterValueRule(
				keyValue);
		profitRule.setProfitCenterValue(SaleOrderBVO.CSPROFITCENTERVID,
				SaleOrderBVO.CSPROFITCENTERID, rows);
		// --13.计算合计
		HeadTotalCalculateRule totalrule = new HeadTotalCalculateRule(keyValue);
		totalrule.calculateHeadTotal();

	}

	private void setReceCustAndAdr(IKeyValue keyValue, int[] rows, String key,
			String headreceiveaddid) {
		for (int row : rows) {
			keyValue.setBodyValue(row, key, headreceiveaddid);
		}
	}

	private Object processBill(SaleOrderVO order, JSONObject bill)
			throws BusinessException {

		// 2. 校验数据的合法性:1.数据结构完整 2.根据组织+单据号校验是否重复.
		checkData(order);
		// 3.补全数据,并且调整单据状态
		fillData(order, bill);

		SaleOrderVO bill2 = (SaleOrderVO) insert(order);
		// 重新查询，防止并发
		bill2 = query(bill2.getParentVO().getPrimaryKey());
		approve(bill2);

		return bill2.getParentVO().getPrimaryKey();
	}

	private SaleOrderVO query(String hid) {
		return new BillQuery<>(SaleOrderVO.class).query(new String[] { hid })[0];
	}

	private void fillData(AggregatedValueObject resvo, JSONObject jsonbill) {
		// 补全数量信息：BPM传递主数量，补全辅数量，
		SaleOrderVO bill = (SaleOrderVO) resvo;
		SaleOrderHVO parentVO = bill.getParentVO();
		SaleOrderBVO[] bvos = bill.getChildrenVO();
		// 审批流状态
		parentVO.setFpfstatusflag(-1);
		// 单据状态，自由
		parentVO.setFstatusflag(1);

		// 报价信息
		for (SaleOrderBVO bvo : bvos) {
			bvo.setCqtunitid(bvo.getCastunitid());
			bvo.setVqtunitrate(bvo.getVchangerate());
			// 1=国内销售，2=国内采购，3=出口，4=进口，5=不区分，
			// bvo.setFbuysellflag(1);
			// bvo.setCtaxcountryid("0001Z010000000079UJJ");// 中国
			// bvo.setCcurrencyid("1002Z0100000000001K1");// rmb
			// bvo.setNexchangerate(UFDouble.ONE_DBL);//
			bvo.setPk_group(parentVO.getPk_group());
			bvo.setPk_org(parentVO.getPk_org());
		}

		int rows[] = new int[bvos.length];
		for (int i = 0; i < bvos.length; i++) {
			rows[i] = i;
		}

		setBodyInfo(bill, parentVO, bvos, rows);
		// 计算税码信息
		IKeyValue keyValue = new VOKeyValue<IBill>(bill);
		// 询税
		SOTaxInfoRule taxInfo = new SOTaxInfoRule(keyValue);
		// taxInfo.setOnlyTaxCodeByBodyPos(rows);
		// -设置税率
		if (bvos[0].getNtaxrate() == null
				|| bvos[0].getNtaxrate().doubleValue() == 0) {
			taxInfo.setTaxTypeAndRate(rows);
		}

		// /根据价税合计，计算单价等
		SaleOrderVOCalculator cal = new SaleOrderVOCalculator(bill);
		cal.calculateOnlyNum(rows, "nastnum");
		cal.calculate(rows, "norigtaxmny");

	}

	protected AggregatedValueObject insert(AggregatedValueObject billvo)
			throws BusinessException {

		IPFBusiAction baction = (IPFBusiAction) NCLocator.getInstance().lookup(
				IPFBusiAction.class);
		Object o = baction.processAction("WRITE", "30", null, billvo, null,
				null);
		SaleOrderVO[] sales = (SaleOrderVO[]) o;
		return sales[0];
	}

	protected AggregatedValueObject approve(SaleOrderVO billvo)
			throws BusinessException {

		IPFBusiAction baction = (IPFBusiAction) NCLocator.getInstance().lookup(
				IPFBusiAction.class);
		baction.processAction("APPROVE", "30", null, billvo, null, null);

		BaseDAO dao = new BaseDAO();

		String sql = " update so_saleorder set taudittime = '"
				+ billvo.getParentVO().getDbilldate()
				+ "' where csaleorderid ='"
				+ billvo.getParentVO().getCsaleorderid() + "'";
		dao.executeUpdate(sql);
		return null;
	}

	private void checkData(SaleOrderVO resvo) throws BusinessException {
		if (resvo == null || resvo.getParentVO() == null)
			throw new BusinessException("未获取的转换后的数据");
		if (resvo.getChildrenVO() == null || resvo.getChildrenVO().length == 0) {
			throw new BusinessException("表体不允许为空");
		}
		SaleOrderHVO head = (SaleOrderHVO) resvo.getParentVO();
		checkSaleOrderHVO(head.getVbillcode(), head.getPk_org());

	}

	private boolean isLock(String vtrantypecode, String vbillcode, String pk_org)
			throws DAOException, BusinessException {
		if (PKLock.getInstance().addDynamicLock(
				vtrantypecode + vbillcode + pk_org)) {
			checkSaleOrderHVO(vbillcode, pk_org);
		} else {
			throw new BusinessException("生成单据号" + vbillcode + "的单据，系统繁忙,请稍后再试!");
		}
		return true;
	}

	private void checkSaleOrderHVO(String vbillcode, String pk_org)
			throws BusinessException {
		VOQuery<SaleOrderHVO> query = new VOQuery<SaleOrderHVO>(
				SaleOrderHVO.class);
		SaleOrderHVO[] hvos = query.query(" and pk_org='" + pk_org
				+ "' and vbillcode='" + vbillcode + "'", null);
		if (hvos != null && hvos.length > 0) {
			throw new BusinessException("已经存在相同单据号的销售订单");
		}
	}

	private String getInvcode(String pk_material) throws UifException {
		String code = getColValue2("bd_material", "pk_material", "code",
				pk_material, "latest", "Y");
		return code;
	}

	private String getCustomer(String code) throws UifException {
		String pk_supplier = getColValue("bd_customer", "pk_customer", "code",
				code);
		return pk_supplier;
	}

	private String getCustomerArea(String pk_customer) throws UifException {
		String pk_supplier = getColValue("bd_customer", "pk_areacl",
				"pk_customer", pk_customer);
		return pk_supplier;
	}

	private String getCustomerAddress(String code) throws UifException {
		String pk_supplier = getColValue("bd_address", "pk_address", "code",
				code);
		return pk_supplier;
	}

	private String getCdeptid(String code, String pk_org) throws UifException {
		String wherepart = " nvl(dr,0) = 0  and code ='" + code
				+ "' and pk_org ='" + pk_org + "' and islastversion='Y' ";
		String pk_dept = getString_TrimZeroLenAsNull(findColValue("org_dept",
				"pk_dept", wherepart));

		return pk_dept;
	}

	private String getCt_salec(String code, String pk_org) throws UifException {
		String pk_ct_sale = getColValue2("ct_sale", "pk_ct_sale", "vbillcode",
				code, "pk_org", pk_org);
		return pk_ct_sale;
	}

	private String getArea(String code) throws UifException {
		String pk_org = getColValue("bd_areacl", "pk_areacl", "code", code);
		return pk_org;
	}

	private String getCastunitid(String code) throws UifException {
		String castunitid = getColValue("bd_measdoc", "pk_measdoc", "code",
				code);
		return castunitid;
	}

	private String getCtaxcodeid(String code) throws UifException {
		String castunitid = getColValue("bd_taxcode", "pk_taxcode", "code",
				code);
		return castunitid;
	}

	private String getStordoc(String code) throws UifException {
		String pk_org = getColValue("bd_stordoc", "pk_stordoc", "code", code);
		return pk_org;
	}

	/**
	 * 从销售订单前台缓存中获取交易类型，如果不存在从后台获取，并缓存之
	 * 
	 * @param tranTypeCode
	 * @return
	 */
	public M30TranTypeVO getTransType(String tranTypeCode, String pk_group) {
		M30TranTypeVO tranType = null;
		// 缓存中没有销售订单交易类型从后台查询
		// 性能优化：add by zhangby5 若code为空，则不需要查询
		if (VOChecker.isEmpty(tranType) && !PubAppTool.isNull(tranTypeCode)) {
			try {

				IM30TranTypeService tranTypeService = NCLocator.getInstance()
						.lookup(IM30TranTypeService.class);
				tranType = tranTypeService
						.queryTranType(pk_group, tranTypeCode);
			} catch (BusinessException e) {
				ExceptionUtils.wrappBusinessException(e.getMessage());
			}
		}
		return tranType;
	}

	/**
	 * 查询发货库存组织、结算财务组织ID、应收组织ID、利润中心ID、默认物流组织、直运仓
	 * 
	 * @param keyValue
	 * @param rows
	 * @return
	 */
	private Map<String, String[]> GetRelationOrg(IKeyValue keyValue, int[] rows) {

		Map<String, String[]> hmRelationOrgid = null;
		// 组织、客户、交易类型、物料参数准备
		String pk_org = keyValue.getHeadStringValue(SOItemKey.PK_ORG);
		String ccustomerid = keyValue.getHeadStringValue(SOItemKey.CCUSTOMERID);

		List<String> alMaterialid = new ArrayList<String>();

		for (int row : rows) {
			String cmaterialid = keyValue.getBodyStringValue(row,
					SOItemKey.CMATERIALID);
			if (PubAppTool.isNull(cmaterialid)) {
				continue;
			}
			alMaterialid.add(cmaterialid);
		}
		if (alMaterialid.size() == 0) {
			return null;
		}

		String[] cmaterialids = new String[alMaterialid.size()];
		alMaterialid.toArray(cmaterialids);

		String transtypeID = keyValue
				.getHeadStringValue(SaleOrderHVO.CTRANTYPEID);
		// 查询结算财务组织ID、应收组织ID、利润中心ID和结算财务组织VID、应收组织VID、利润中心VID
		try {
			// 如果交易类型非空，按照交易类型获取直运仓

			IQueryRelationOrg service = NCLocator.getInstance().lookup(
					IQueryRelationOrg.class);
			hmRelationOrgid = service.querySaleRelationOrg(transtypeID,
					ccustomerid, pk_org, cmaterialids);

		} catch (BusinessException e1) {
			ExceptionUtils.wrappException(e1);
		}
		return hmRelationOrgid;
	}

	/**
	 * 匹配来源合同
	 * 
	 * @param srcBillS2
	 * @param srcKey
	 * @return
	 */
	private CtSaleBVO matchedSrcBody(AggCtSaleVO[] srcBillS2, String srcKey) {
		for (AggCtSaleVO bill : srcBillS2) {
			CtSaleBVO[] itemVOs = bill.getCtSaleBVO();
			for (CtSaleBVO body : itemVOs) {
				String key = body.getPk_material();
				if (key.equals(srcKey)) {
					return body;
				}
			}
		}
		return null;
	}

	public AggCtSaleVO[] qryOrignBills(String code, String pk_org)
			throws BusinessException {

		String pk_ct_sale = getCt_salec(code, pk_org);
		SCMBillQuery<AggCtSaleVO> queryTool = new SCMBillQuery<AggCtSaleVO>(
				AggCtSaleVO.class);
		AggCtSaleVO[] orderVOs = queryTool
				.queryVOByIDs(new String[] { pk_ct_sale });
		return orderVOs;
	}

}
