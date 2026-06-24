package nc.api.rest.arap.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.pf.pub.PfDataCache;
import nc.bs.sec.esapi.NCESAPI;
import nc.itf.arap.fieldmap.IBillFieldGet;
import nc.itf.bd.pub.IBDMetaDataIDConst;
import nc.itf.org.IOrgConst;
import nc.itf.org.IOrgMetaDataIDConst;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.pubitf.bd.accessor.GeneralAccessorFactory;
import nc.pubitf.org.IAccountingBookPubService;
import nc.pubitf.uapbd.IAccountPubService;
import nc.util.fi.pub.SqlUtils;
import nc.vo.arap.basebill.BaseBillVO;
import nc.vo.arap.basebill.BaseItemVO;
import nc.vo.arap.pub.BillEnumCollection;
import nc.vo.arap.pub.BillEnumCollection.FromSystem;
import nc.vo.arap.utils.StringUtil;
import nc.vo.arap.verify.VerifyDetailVO;
import nc.vo.arap.verify.VerifyMainVO;
import nc.vo.bd.accessor.IBDData;
import nc.vo.bd.account.AccountVO;
import nc.vo.org.AccountingBookVO;
import nc.vo.pf.pub.BusitypeVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.BusinessRuntimeException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;

import org.apache.commons.lang.StringUtils;

public class ArapBillConvert {

	/**
	 * 将vo中的编码转换成pk
	 */
	public void convertVO(SuperVO vo) {
		String[] attrnames = vo.getAttributeNames();
		for (String attr : attrnames) {
			if (IBillFieldGet.PK_ORG.equals(attr)
					|| IBillFieldGet.PK_GROUP.equals(attr))
				continue;
			Object value = vo.getAttributeValue(attr);
			value = tranlate(vo, attr, value);
			vo.setAttributeValue(attr, value);
		}
	}

	public Object tranlate(SuperVO vo, String attr, Object value) {
		String pk_group = InvocationInfoProxy.getInstance().getGroupId();
		String pk_org = (String) vo.getAttributeValue(IBillFieldGet.PK_ORG);
		try {
			if (IBillFieldGet.PK_TRADETYPE.equals(attr)) {// 交易类型
				String pk_billtypeid = PfDataCache.getBillTypeInfo(pk_group,
						(String) value).getPk_billtypeid();
				vo.setAttributeValue(IBillFieldGet.PK_TRADETYPEID,
						pk_billtypeid);
			} else if (IBillFieldGet.PK_RECPAYTYPE.equals(attr)) {// 付款类型
				value = getRecpaytypeByName((String) value);
			} else if (IBillFieldGet.PK_BUSITYPE.equals(attr)
					&& !StringUtils.isEmpty((String) value)) {// 业务流程
																// （看后续是否有接口可以替换）
				@SuppressWarnings("unchecked")
				Collection<BusitypeVO> busiVOs = new BaseDAO()
						.retrieveByClause(BusitypeVO.class, "busicode='"
								+ (String) value + "'",
								new String[] { "pk_busitype" });
				if (busiVOs != null && busiVOs.size() > 0) {
					value = busiVOs.toArray(new BusitypeVO[0])[0]
							.getPk_busitype();
				}
			} else if (orgDoc().contains(attr)) {// 组织级档案
				String oldvalue = (String) value;
				value = getDocByCode(pk_org, (String) value, getMDID(attr));
				if (StringUtil.isEmpty((String) value)) {
					value = getDocByName(pk_org, oldvalue, getMDID(attr));
				}
			} else if (groupDoc().contains(attr)) {// 集团级档案
				String oldvalue = (String) value;
				value = getDocByCode(pk_group, (String) value, getMDID(attr));
				if (StringUtil.isEmpty((String) value)) {
					value = getDocByName(pk_group, oldvalue, getMDID(attr));
				}
			} else if (globalDoc().contains(attr)) {// 全局级档案
				String oldvalue = (String) value;
				value = getDocByCode(IOrgConst.GLOBEORG, (String) value,
						getMDID(attr));
				if (StringUtil.isEmpty((String) value)) {
					value = getDocByName(IOrgConst.GLOBEORG, oldvalue,
							getMDID(attr));
				}
			}
		} catch (Exception e) {
			throw new BusinessRuntimeException(nc.vo.ml.NCLangRes4VoTransl
					.getNCLangRes().getStrByID("2006pub_0", "02006pub-0963")/*
																			 * @res
																			 * "字段"
																			 */
					+ attr
					+ nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
							"2006pub_0", "02006pub-0964")/* @res "有误！" */);
		}
		return value;
	}

	/**
	 * 翻译会计科目及核算要素
	 */
	public void translateAcc(BaseItemVO[] itemvos, String pk_org,
			String billdate) throws BusinessException {
		// 查询组织主账簿
		Set<String> subjcodes = new HashSet<String>();
		Set<String> checkelements = new HashSet<String>();
		for (BaseItemVO item : itemvos) {
			if (!StringUtils.isEmpty(item.getPk_subjcode()))
				subjcodes.add(item.getPk_subjcode());
			if (!StringUtils.isEmpty(item.getCheckelement()))
				checkelements.add(item.getCheckelement());
		}

		Map<String, String> factorMap = new HashMap<String, String>();
		if (checkelements.size() > 0) {
			// 核算要素翻译 TODO
			// factorMap =
			// NCLocator.getInstance().lookup(IFactorPubService.class)
			// .getFactorAsoaPKByFactorCode(checkelements.toArray(new
			// String[0]), pk_liabook, billdate);
		}

		Map<String, String> accMap = new HashMap<String, String>();
		if (subjcodes.size() > 0) {
			Set<String> orgs = new HashSet<String>();
			orgs.add(pk_org);
			Map<String, AccountingBookVO> accountings = NCLocator.getInstance()
					.lookup(IAccountingBookPubService.class)
					.queryAccountingBookVObyFinanceOrgPks(orgs);
			if (accountings != null && accountings.containsKey(pk_org)) {
				String pk_accountingbook = accountings.get(pk_org)
						.getPk_accountingbook();
				AccountVO[] accounts = NCLocator
						.getInstance()
						.lookup(IAccountPubService.class)
						.queryAccountVOsByCodes(pk_accountingbook,
								subjcodes.toArray(new String[0]), billdate);
				if (accounts != null) {
					for (AccountVO accountVO : accounts) {
						accMap.put(accountVO.getCode(),
								accountVO.getPk_accasoa());
					}
				}
			}
		}

		if ((factorMap != null && factorMap.size() > 0) || accMap.size() > 0) {
			for (BaseItemVO item : itemvos) {
				if (!StringUtils.isEmpty(item.getPk_subjcode()))
					item.setPk_subjcode(factorMap.get(item.getPk_subjcode()));
				if (!StringUtils.isEmpty(item.getCheckelement()))
					item.setCheckelement(factorMap.get(item.getCheckelement()));
			}
		}

	}

	/**
	 * 将查询条件拼接成sql语句
	 */
	public String getConditionSql(Map<String, String> map, String tablename,
			String itemname) throws BusinessException {
		StringBuffer condition = new StringBuffer();
		String pk_group = InvocationInfoProxy.getInstance().getGroupId();
		// 集团
		String groupCodes = NCESAPI.sqlEncode((String) map
				.get(BaseBillVO.PK_GROUP));
		List<String> groups = new ArrayList<String>();
		if (!StringUtils.isEmpty(groupCodes)) {
			String[] pk_orgs = groupCodes.split(",");
			IBDData[] datas = GeneralAccessorFactory.getAccessor(
					IOrgMetaDataIDConst.GROUP).getDocByCodes(pk_group, pk_orgs);
			if (datas != null && datas.length > 0) {
				for (IBDData data : datas) {
					groups.add(data.getPk());
				}
				condition.append(SqlUtils.getInStr(tablename + "."
						+ BaseBillVO.PK_GROUP, groups));
			}
		} else {
			// 组织必录，没有录组织，所有条件清空
			return "1=2";
		}

		// 财务组织 ("pk_org":"yonyou00,001")
		String orgCodes = NCESAPI
				.sqlEncode((String) map.get(BaseBillVO.PK_ORG));
		List<String> orgs = new ArrayList<String>();
		if (!StringUtils.isEmpty(orgCodes)) {
			String[] pk_orgs = orgCodes.split(",");
			IBDData[] datas = GeneralAccessorFactory.getAccessor(
					IOrgMetaDataIDConst.FINANCEORG).getDocByCodes(pk_group,
					pk_orgs);
			if (datas != null && datas.length > 0) {
				for (IBDData data : datas) {
					orgs.add(data.getPk());
				}
				condition.append(" and "
						+ SqlUtils.getInStr(
								tablename + "." + BaseBillVO.PK_ORG, orgs));
			}
		} else {
			// 组织必录，没有录组织，所有条件清空
			return "1=2";
		}

		// 单据日期 ("billdate":"2019-06-01,2019-06-11")
		String billdateStr = map.get(BaseBillVO.BILLDATE);
		// 会计期间
		String yearMonthStr = map.get("yearmonth");
		// 核销 处理日期 busidate
		String busidateStr = map.get(VerifyDetailVO.BUSIDATE);

		if (!StringUtils.isEmpty(billdateStr)) {
			String[] billdates = billdateStr.split(",");
			if (billdates.length == 2) {
				// (ap_payablebill.billdate >= '2019-06-01 00:00:00' and
				// ap_payablebill.billdate <= '2019-06-06 23:59:59')
				if (billdates[0].length() == 10)
					billdates[0] = billdates[0] + " 00:00:00";
				condition.append(" and (" + tablename + "."
						+ BaseBillVO.BILLDATE + ">='" + billdates[0] + "' ");
				if (billdates[1].length() == 10)
					billdates[1] = billdates[1] + " 23:59:59";
				condition.append(" and " + tablename + "."
						+ BaseBillVO.BILLDATE + "<='" + billdates[1] + "')");
			} else if (billdates.length == 1) {
				// (ap_payablebill.billdate >= '2019-06-01 00:00:00' and
				// ap_payablebill.billdate <= '2019-06-01 23:59:59')
				String start = billdates[0];
				String end = billdates[0];
				if (billdates[0].length() == 10) {
					start = billdates[0] + " 00:00:00";
					end = billdates[0] + " 23:59:59";
				}
				condition.append(" and (" + tablename + "."
						+ BaseBillVO.BILLDATE + ">='" + start + "' ");
				condition.append(" and " + tablename + "."
						+ BaseBillVO.BILLDATE + "<='" + end + "')");
			}
		} else if (!StringUtils.isEmpty(yearMonthStr)) {
			String[] yearMonths = yearMonthStr.split(",");
			String field = tablename + "." + BaseBillVO.BILLYEAR + "||'-'||"
					+ tablename + "." + BaseBillVO.BILLPERIOD;
			if (yearMonths.length == 2) {
				// (ap_payablebill.billyear||'-'||ap_payablebill.billperiod >=
				// '2019-01' and
				// ap_payablebill.billyear||'-'||ap_payablebill.billperiod <=
				// '2019-06')
				condition.append(" and (" + field + ">='" + yearMonths[0]
						+ "' and " + field + "<='" + yearMonths[1] + "')");
			} else if (yearMonths.length == 1) {
				// (ap_payablebill.billyear||'-'||ap_payablebill.billperiod =
				// '2019-01')
				condition
						.append(" and (" + field + "='" + yearMonths[0] + "')");
			} else {
				return "1=2";
			}
		} else if (!StringUtils.isEmpty(busidateStr)) {
			// 核销 处理日期
			String[] busidates = busidateStr.split(",");
			if (busidates.length == 2) {
				// (arap_verifydetail.busidate >= '2019-06-01 00:00:00' and
				// arap_verifydetail.busidate <= '2019-06-06 23:59:59')
				if (busidates[0].length() == 10)
					busidates[0] = busidates[0] + " 00:00:00";
				condition
						.append(" and " + itemname + "."
								+ VerifyDetailVO.BUSIDATE + ">='"
								+ busidates[0] + "' ");
				if (busidates[1].length() == 10)
					busidates[1] = busidates[1] + " 23:59:59";
				condition.append(" and " + itemname + "."
						+ VerifyDetailVO.BUSIDATE + "<='" + busidates[1] + "'");
			} else if (busidates.length == 1) {
				// (arap_verifydetail.busidate >= '2019-06-01 00:00:00' and
				// arap_verifydetail.busidate <= '2019-06-06 23:59:59')
				String start = busidates[0];
				String end = busidates[0];
				if (busidates[0].length() == 10) {
					start = busidates[0] + " 00:00:00";
					end = busidates[0] + " 23:59:59";
				}
				condition.append(" and " + itemname + "."
						+ VerifyDetailVO.BUSIDATE + ">='" + start + "' ");
				condition.append(" and " + itemname + "."
						+ VerifyDetailVO.BUSIDATE + "<='" + end + "'");
			}
		} else {
			// 单据日期和会计期间必须有一个有值
			// return "1=2";
		}
		// 核销 处理批次号 (反核销)
		String businoStr = map.get(VerifyDetailVO.BUSINO);
		if (!StringUtils.isEmpty(businoStr)) {
			condition.append(" and " + itemname + "." + VerifyDetailVO.BUSINO
					+ "='" + businoStr + "'");
		}

		// 核销 处理标志
		// String busiflagStr = map.get(VerifyDetailVO.BUSIFLAG);
		// if (!StringUtils.isEmpty(busiflagStr)) {
		// condition.append(" and " + itemname + "." + VerifyDetailVO.BUSIFLAG +
		// " in (0,2)");
		// }

		// 核销 处理人code-->pk
		// String creatorStr = map.get(VerifyVO.CREATOR);
		// String creatorStr = NCESAPI.sqlEncode(map.get(BaseBillVO.CREATOR));
		// if (!StringUtils.isEmpty(creatorStr)) {
		// IBDData data =
		// GeneralAccessorFactory.getAccessor(IBDMetaDataIDConst.USER).getDocByCode(orgs.get(0),
		// creatorStr);
		// if (data != null) {
		// condition.append(" and ").append(tablename + "." + BaseBillVO.CREATOR
		// + "='" + data.getPk() + "'");
		// }
		// }
		// 核销 贷方币种 code --->pk
		String currtypeCrStr = NCESAPI.sqlEncode(map
				.get(IBillFieldGet.PK_CURRTYPE));
		if (!StringUtils.isEmpty(currtypeCrStr)) {
			IBDData data = GeneralAccessorFactory.getAccessor(
					IBDMetaDataIDConst.CURRTYPE).getDocByCode(orgs.get(0),
					currtypeCrStr);
			if (data != null) {
				condition.append(" and ").append(
						itemname + "." + IBillFieldGet.PK_CURRTYPE + "='"
								+ data.getPk() + "'");
			}
		}
		// 核销 借方币种 code --->pk
		// String currtypeDeStr =
		// NCESAPI.sqlEncode(map.get(VerifyMainVO.PK_CURRTYPE_DE));
		// if (!StringUtils.isEmpty(currtypeDeStr)) {
		// IBDData data =
		// GeneralAccessorFactory.getAccessor(IBDMetaDataIDConst.CURRTYPE).getDocByCode(orgs.get(0),
		// creatorStr);
		// if (data != null) {
		// condition.append(" and ").append(tablename + "." +
		// VerifyMainVO.PK_CURRTYPE_DE + "='" + data.getPk() + "'");
		// }
		// }

		// 核销 贷方处理全局本币金额
		// String globalMoneyCrStr = map.get(VerifyMainVO.GLOBAL_MONEY_CR);
		// if (!StringUtils.isEmpty(globalMoneyCrStr)) {
		// condition.append(" and " + tablename + "." +
		// VerifyMainVO.GLOBAL_MONEY_CR + "='" + globalMoneyCrStr + "'");
		// }

		// 核销 借方处理全局本币金额
		// String globalMoneyyDeStr = map.get(VerifyMainVO.GLOBAL_MONEY_DE);
		// if (!StringUtils.isEmpty(globalMoneyyDeStr)) {
		// condition.append(" and " + tablename + "." +
		// VerifyMainVO.GLOBAL_MONEY_DE + "='" + globalMoneyyDeStr + "'");
		// }

		// 核销 贷方处理集团本币金额
		// String groupMoneyCrStr = map.get(VerifyMainVO.GROUP_MONEY_CR);
		// if (!StringUtils.isEmpty(groupMoneyCrStr)) {
		// condition.append(" and " + tablename + "." +
		// VerifyMainVO.GROUP_MONEY_CR + "='" + groupMoneyCrStr + "'");
		// }

		// 核销 借方处理集团本币金额
		// String groupMoneyDeStr = map.get(VerifyMainVO.GROUP_MONEY_DE);
		// if (!StringUtils.isEmpty(groupMoneyDeStr)) {
		// condition.append(" and " + tablename + "." +
		// VerifyMainVO.GROUP_MONEY_DE + "='" + groupMoneyDeStr + "'");
		// }
		// 核销 贷方处理本币金额
		String localMoneyCrStr = map.get(VerifyMainVO.LOCAL_MONEY_CR);
		if (!StringUtils.isEmpty(localMoneyCrStr)) {
			condition.append(" and " + tablename + "."
					+ VerifyMainVO.LOCAL_MONEY_CR + "='" + localMoneyCrStr
					+ "'");
		}

		// 核销 借方处理本币金额
		// String localMoneyDeStr = map.get(VerifyMainVO.LOCAL_MONEY_DE);
		// if (!StringUtils.isEmpty(localMoneyDeStr)) {
		// condition.append(" and " + tablename + "." +
		// VerifyMainVO.LOCAL_MONEY_DE + "='" + localMoneyDeStr + "'");
		// }

		// 核销 贷方处理原币金额
		// String moneyCrStr = map.get(VerifyMainVO.MONEY_CR);
		// if (!StringUtils.isEmpty(moneyCrStr)) {
		// condition.append(" and " + tablename + "." + VerifyMainVO.MONEY_CR +
		// "='" + moneyCrStr + "'");
		// }

		// 核销 借方处理原币金额
		// String moneyDeStr = map.get(VerifyMainVO.MONEY_DE);
		// if (!StringUtils.isEmpty(moneyDeStr)) {
		// condition.append(" and " + tablename + "." + VerifyMainVO.MONEY_DE +
		// "='" + moneyDeStr + "'");
		// }

		// 核销 贷方处理数量
		// String quantityCrStr = map.get(VerifyMainVO.QUANTITY_CR);
		// if (!StringUtils.isEmpty(quantityCrStr)) {
		// condition.append(" and " + tablename + "." + VerifyMainVO.QUANTITY_CR
		// + "='" + quantityCrStr + "'");
		// }

		// 核销 借方处理数量
		// String quantityDeStr = map.get(VerifyMainVO.QUANTITY_DE);
		// if (!StringUtils.isEmpty(quantityDeStr)) {
		// condition.append(" and " + tablename + "." + VerifyMainVO.QUANTITY_DE
		// + "='" + quantityDeStr + "'");
		// }

		// 核销 摘要 vo中是comment 数据表中是scomment
		// String scommentStr = map.get(VerifyMainVO.COMMENT);
		// if (!StringUtils.isEmpty(scommentStr)) {
		// condition.append(" and " + tablename + ".scomment"+ "='" +
		// scommentStr + "'");
		// }

		// 交易类型("pk_tradetype":"D1")
		String pk_tradetype = map.get(BaseBillVO.PK_TRADETYPE);
		if (!StringUtils.isEmpty(pk_tradetype)) {
			String pk_billtypeid = PfDataCache.getBillTypeInfo(pk_group,
					pk_tradetype).getPk_billtypeid();
			String PK_TRADETYPE = NCESAPI.sqlEncode(pk_billtypeid);
			condition.append(" and ").append(
					tablename + "." + IBillFieldGet.PK_TRADETYPEID + "='"
							+ PK_TRADETYPE + "'");
		}

		// 来源系统
		String src_syscode = map.get(BaseBillVO.SRC_SYSCODE);
		if (!StringUtils.isEmpty(src_syscode)) {
			String srcCode = NCESAPI.sqlEncode(getSrcCode(src_syscode));
			condition
					.append(" and "
							+ SqlUtils.getInStr(tablename + "."
									+ BaseBillVO.SRC_SYSCODE,
									new String[] { srcCode }));
		}

		// 单据号
		// String billno = map.get(BaseBillVO.BILLNO);
		String BILLNO = NCESAPI.sqlEncode(map.get(BaseBillVO.BILLNO));
		if (!StringUtils.isEmpty(BILLNO)) {
			condition.append(" and " + tablename + "." + BaseBillVO.BILLNO
					+ " like '%" + BILLNO + "%' ");
		}

		// 单据状态
		// String billstatus = map.get(BaseBillVO.BILLSTATUS);
		String BILLSTATUS = NCESAPI.sqlEncode(map.get(BaseBillVO.BILLSTATUS));
		if (!StringUtils.isEmpty(BILLSTATUS)) {
			condition.append(" and ").append(
					tablename + "." + BaseBillVO.BILLSTATUS + "='"
							+ getBillStatusCode(BILLSTATUS) + "'");
		}

		// 审批状态
		// String approvestatus = map.get(BaseBillVO.APPROVESTATUS);
		String APPROVESTATUS = NCESAPI.sqlEncode(map
				.get(BaseBillVO.APPROVESTATUS));
		if (!StringUtils.isEmpty(APPROVESTATUS)) {
			condition.append(" and ").append(
					tablename + "." + BaseBillVO.APPROVESTATUS + "='"
							+ getApproveStatusCode(APPROVESTATUS) + "'");
		}

		// 生效状态
		// String effectstatus = map.get(BaseBillVO.EFFECTSTATUS);
		String EFFECTSTATUS = NCESAPI.sqlEncode(map
				.get(BaseBillVO.EFFECTSTATUS));
		if (!StringUtils.isEmpty(EFFECTSTATUS)) {
			condition.append(" and ").append(
					tablename + "." + BaseBillVO.EFFECTSTATUS + "='"
							+ getEffectStatusCode(EFFECTSTATUS) + "'");
		}

		// 制单人
		String BILLMAKER = NCESAPI.sqlEncode(map.get(BaseBillVO.BILLMAKER));
		if (!StringUtils.isEmpty(BILLMAKER)) {
			IBDData data = GeneralAccessorFactory.getAccessor(
					IBDMetaDataIDConst.USER).getDocByNameWithMainLang(
					orgs.get(0), BILLMAKER);
			if (data != null) {
				condition.append(" and ").append(
						tablename + "." + BaseBillVO.BILLMAKER + "='"
								+ data.getPk() + "'");
			}
		}

		// 原币金额
		String MONEY = NCESAPI.sqlEncode(map.get(BaseBillVO.MONEY));
		if (!StringUtils.isEmpty(MONEY)) {
			String[] money = MONEY.split(",");
			condition.append(" and (" + tablename + "." + BaseBillVO.MONEY
					+ ">='" + money[0] + "' ");
			condition.append(" and " + tablename + "." + BaseBillVO.MONEY
					+ "<='" + money[1] + "')");
		}

		// 往来对象
		// String objtype = map.get(IBillFieldGet.OBJTYPE);
		String OBJTYPE = NCESAPI.sqlEncode(map.get(IBillFieldGet.OBJTYPE));
		if ("供应商".equals(OBJTYPE)) {
			condition.append(" and " + itemname + "." + IBillFieldGet.OBJTYPE
					+ "=" + BillEnumCollection.ObjType.SUPPLIER.VALUE
					+ " and rowno=0 ");
		} else if ("客户".equals(OBJTYPE)) {
			condition.append(" and " + itemname + "." + IBillFieldGet.OBJTYPE
					+ "=" + BillEnumCollection.ObjType.CUSTOMER.VALUE
					+ " and rowno=0 ");
		} else if ("部门".equals(OBJTYPE)) {
			condition.append(" and " + itemname + "." + IBillFieldGet.OBJTYPE
					+ "=" + BillEnumCollection.ObjType.DEP.VALUE
					+ " and rowno=0 ");
		} else if ("业务员".equals(OBJTYPE)) {
			condition.append(" and " + itemname + "." + IBillFieldGet.OBJTYPE
					+ "=" + BillEnumCollection.ObjType.PERSON.VALUE
					+ " and rowno=0 ");
		}
		// String supplierStr = map.get(IBillFieldGet.SUPPLIER);
		String SUPPLIER = NCESAPI.sqlEncode(map.get(IBillFieldGet.SUPPLIER));
		if (!StringUtils.isEmpty(SUPPLIER)) {
			String[] suppliers = SUPPLIER.split(",");
			IBDData[] bddatas = GeneralAccessorFactory.getAccessor(
					IBDMetaDataIDConst.SUPPLIER).getDocByCodes(orgs.get(0),
					suppliers);
			if (bddatas != null && bddatas.length > 0) {
				List<String> pk_suppliers = new ArrayList<String>();
				for (IBDData data : bddatas) {
					pk_suppliers.add(data.getPk());
				}
				condition.append(" and "
						+ SqlUtils.getInStr(itemname + "."
								+ IBillFieldGet.SUPPLIER, pk_suppliers));
			}
		}
		// String customerStr = map.get(IBillFieldGet.CUSTOMER);
		String CUSTOMER = NCESAPI.sqlEncode(map.get(IBillFieldGet.CUSTOMER));
		if (!StringUtils.isEmpty(CUSTOMER)) {
			String[] customers = CUSTOMER.split(",");
			IBDData[] bddatas = GeneralAccessorFactory.getAccessor(
					IBDMetaDataIDConst.CUSTOMER).getDocByCodes(orgs.get(0),
					customers);
			if (bddatas != null && bddatas.length > 0) {
				List<String> pk_customers = new ArrayList<String>();
				for (IBDData data : bddatas) {
					pk_customers.add(data.getPk());
				}
				condition.append(" and "
						+ SqlUtils.getInStr(itemname + "."
								+ IBillFieldGet.CUSTOMER, pk_customers));
			}
		}
		// String deptidStr = map.get(IBillFieldGet.PK_DEPTID);
		String PK_DEPTID = NCESAPI.sqlEncode(map.get(IBillFieldGet.PK_DEPTID));
		if (!StringUtils.isEmpty(PK_DEPTID)) {
			String[] deptids = PK_DEPTID.split(",");
			IBDData[] bddatas = GeneralAccessorFactory.getAccessor(
					IBDMetaDataIDConst.DEPT)
					.getDocByCodes(orgs.get(0), deptids);
			if (bddatas != null && bddatas.length > 0) {
				List<String> pk_deptids = new ArrayList<String>();
				for (IBDData data : bddatas) {
					pk_deptids.add(data.getPk());
				}
				condition.append(" and "
						+ SqlUtils.getInStr(itemname + "."
								+ IBillFieldGet.PK_DEPTID, pk_deptids));
			}
		}

		String PK_PSNDOC = NCESAPI.sqlEncode(map.get(IBillFieldGet.PK_PSNDOC));
		if (!StringUtils.isEmpty(PK_PSNDOC)
				&& ("业务员".equals(OBJTYPE) || StringUtils.isEmpty(OBJTYPE))) {
			String[] psndocs = PK_PSNDOC.split(",");
			IBDData[] bddatas = GeneralAccessorFactory.getAccessor(
					IBDMetaDataIDConst.PSNDOC).getDocByCodes(orgs.get(0),
					psndocs);
			if (bddatas != null && bddatas.length > 0) {
				List<String> pk_psndocs = new ArrayList<String>();
				for (IBDData data : bddatas) {
					pk_psndocs.add(data.getPk());
				}
				condition.append(" and "
						+ SqlUtils.getInStr(itemname + "."
								+ IBillFieldGet.PK_PSNDOC, pk_psndocs));
			}
		}

		String PAYACCOUNT = NCESAPI
				.sqlEncode(map.get(IBillFieldGet.PAYACCOUNT));
		if (!StringUtils.isEmpty(PAYACCOUNT)) {
			String[] suppliers = PAYACCOUNT.split(",");
			IBDData[] bddatas = GeneralAccessorFactory.getAccessor(
					IBDMetaDataIDConst.BANKACCSUB).getDocByCodes(orgs.get(0),
					suppliers);
			if (bddatas != null && bddatas.length > 0) {
				List<String> pk_suppliers = new ArrayList<String>();
				for (IBDData data : bddatas) {
					pk_suppliers.add(data.getPk());
				}
				condition.append(" and "
						+ SqlUtils.getInStr(itemname + "."
								+ IBillFieldGet.PAYACCOUNT, pk_suppliers));
			}
		}

		String CASHACCOUNT = NCESAPI.sqlEncode(map
				.get(IBillFieldGet.CASHACCOUNT));
		if (!StringUtils.isEmpty(CASHACCOUNT)) {
			String[] suppliers = CASHACCOUNT.split(",");
			IBDData[] bddatas = GeneralAccessorFactory.getAccessor(
					IBDMetaDataIDConst.CASHACCOUNT).getDocByCodes(orgs.get(0),
					suppliers);
			if (bddatas != null && bddatas.length > 0) {
				List<String> pk_suppliers = new ArrayList<String>();
				for (IBDData data : bddatas) {
					pk_suppliers.add(data.getPk());
				}
				condition.append(" and "
						+ SqlUtils.getInStr(itemname + "."
								+ IBillFieldGet.CASHACCOUNT, pk_suppliers));
			}
		}

		String RECACCOUNT = NCESAPI
				.sqlEncode(map.get(IBillFieldGet.RECACCOUNT));
		if (!StringUtils.isEmpty(RECACCOUNT)) {
			String[] suppliers = RECACCOUNT.split(",");
			IBDData[] bddatas = GeneralAccessorFactory.getAccessor(
					IBDMetaDataIDConst.CUSTBANKACCSUB).getDocByCodes(
					orgs.get(0), suppliers);
			if (bddatas != null && bddatas.length > 0) {
				List<String> pk_suppliers = new ArrayList<String>();
				for (IBDData data : bddatas) {
					pk_suppliers.add(data.getPk());
				}
				condition.append(" and "
						+ SqlUtils.getInStr(itemname + "."
								+ IBillFieldGet.RECACCOUNT, pk_suppliers));
			}
		}

		// 源头单据主键
		String SRC_BILLID = NCESAPI
				.sqlEncode(map.get(IBillFieldGet.SRC_BILLID));
		if (!StringUtils.isEmpty(SRC_BILLID)) {
			String[] srcBillids = SRC_BILLID.split(",");
			condition.append(" and "
					+ SqlUtils.getInStr(itemname + "."
							+ IBillFieldGet.SRC_BILLID, srcBillids));
		}
		// 源头单据类型
		String SRC_BILLTYPE = NCESAPI.sqlEncode(map
				.get(IBillFieldGet.SRC_BILLTYPE));
		if (!StringUtils.isEmpty(SRC_BILLTYPE)) {
			String[] srcBilltypes = SRC_BILLTYPE.split(",");
			condition.append(" and "
					+ SqlUtils.getInStr(itemname + "."
							+ IBillFieldGet.SRC_BILLTYPE, srcBilltypes));
		}

		// 合同号
		String CONTRACTNO = NCESAPI
				.sqlEncode(map.get(IBillFieldGet.CONTRACTNO));
		if (!StringUtils.isEmpty(CONTRACTNO)) {
			String[] srcBilltypes = CONTRACTNO.split(",");
			condition.append(" and "
					+ SqlUtils.getInStr(itemname + "."
							+ IBillFieldGet.CONTRACTNO, srcBilltypes));
		}
		// 订单号
		String PURCHASEORDER = NCESAPI.sqlEncode(map
				.get(IBillFieldGet.PURCHASEORDER));
		if (!StringUtils.isEmpty(PURCHASEORDER)) {
			String[] srcBilltypes = PURCHASEORDER.split(",");
			condition.append(" and "
					+ SqlUtils.getInStr(itemname + "."
							+ IBillFieldGet.PURCHASEORDER, srcBilltypes));
		}
		// 发票号
		String INVOICENO = NCESAPI.sqlEncode(map.get(IBillFieldGet.INVOICENO));
		if (!StringUtils.isEmpty(INVOICENO)) {
			String[] srcBilltypes = INVOICENO.split(",");
			condition.append(" and "
					+ SqlUtils.getInStr(itemname + "."
							+ IBillFieldGet.INVOICENO, srcBilltypes));
		}

		// 自定义项def1~80
		for (int i = 1; i <= 80; i++) {
			String def = map.get("def" + i);
			String DEF = NCESAPI.sqlEncode(def);
			if (!StringUtils.isEmpty(DEF)) {
				condition.append(" and " + tablename + ".def" + i + "='" + DEF
						+ "'");
			}
		}

		for (int i = 1; i <= 80; i++) {
			String def = map.get("bodydef" + i);
			String DEF = NCESAPI.sqlEncode(def);
			if (!StringUtils.isEmpty(DEF)) {
				condition.append(" and " + itemname + ".def" + i + "='" + DEF
						+ "'");
			}
		}

		// condition.append(" and isnull("+tablename+".dr,0)=0 ");
		// condition.append(" and isnull("+itemname+".dr,0)=0 ");

		return condition.toString();
	}

	/**
	 * 将付款业务类型转成pk
	 * 
	 * @param value
	 * @return
	 * @throws DAOException
	 */
	public Object getRecpaytypeByName(String value) throws DAOException {
		StringBuffer condition = new StringBuffer();
		String select = "select distinct fi_recpaytype.pk_recpaytype ";
		String tablename = "fi_recpaytype";

		condition.append(tablename + "." + "name" + "='" + value + "'");
		String where = condition.toString();
		String from = "fi_recpaytype fi_recpaytype ";
		String sql = select + " from " + from + " where " + where;

		Object pks = new BaseDAO().executeQuery(sql, new ColumnProcessor());
		if (!StringUtil.isEmpty((String) pks)) {
			return pks;
		} else {
			return null;
		}

	}

	/**
	 * 将vo转换成Map并且加上name和code字段
	 */
	public Map<String, String> convertVOToMap(SuperVO vo) {
		Map<String, String> map = new HashMap<String, String>();
		String[] attributeNames = vo.getAttributeNames();
		for (String attr : attributeNames) {
			if (vo.getAttributeValue(attr) != null) {
				if (!(vo.getAttributeValue(attr) instanceof String)) {
					map.put(attr, String.valueOf(vo.getAttributeValue(attr)));
				} else {
					map.put(attr, (String) vo.getAttributeValue(attr));
				}
			}
		}
		fillNameCode(map);
		return map;
	}

	public void fillNameCode(Map<String, String> map) {
		String[] keys = map.keySet().toArray(new String[0]);
		for (String key : keys) {
			if (getMDID(key) != null && !StringUtils.isEmpty(map.get(key))) {
				String[] codeName = getDocNameCodeById(map.get(key),
						getMDID(key));
				if (codeName != null) {
					map.put(key + "_code", codeName[0]);
					map.put(key + "_name", codeName[1]);
				}
			}
		}
	}

	/**
	 * 将要修改的字段填入现有vo中
	 */
	public void updateVO(SuperVO vo, Map<String, Object> map)
			throws BusinessException {
		for (String key : map.keySet()) {
			if ("items".equals(key))
				continue;
			Object oldvalue = vo.getAttributeValue(key);
			String newvalue = (String) tranlate(vo, key, map.get(key));// 先翻译成pk
			if (!StringUtils.isEmpty((String) map.get(key))
					&& StringUtils.isEmpty(newvalue)) {
				throw new BusinessException("["
						+ key
						+ nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
								.getStrByID("2006pub_0", "02006pub-0965")/*
																		 * @res
																		 * "]找不到对应档案"
																		 */);
			}
			try {
				if (oldvalue instanceof UFDate) {
					vo.setAttributeValue(key, new UFDate(newvalue));
				} else if (oldvalue instanceof UFDouble) {
					vo.setAttributeValue(key, new UFDouble(newvalue));
				} else if (oldvalue instanceof UFBoolean) {
					vo.setAttributeValue(key, UFBoolean.valueOf(newvalue));
				} else if (oldvalue instanceof Integer) {
					vo.setAttributeValue(key, new Integer(newvalue));
				} else {
					vo.setAttributeValue(key, newvalue);
				}
			} catch (Exception e) {
				throw new BusinessException("["
						+ key
						+ nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
								.getStrByID("2006pub_0", "02006pub-0966")/*
																		 * @res
																		 * "]传入错误！"
																		 */);
			}
		}
	}

	private String getBillStatusCode(String billstatus) {
		Integer status = null;
		if ("保存".equals(billstatus)) {
			status = BillEnumCollection.BillSatus.Save.VALUE;
		} else if ("暂存".equals(billstatus)) {
			status = BillEnumCollection.BillSatus.Tempeorary.VALUE;
		}
		if ("审批通过".equals(billstatus)) {
			status = BillEnumCollection.BillSatus.Audit.VALUE;
		}
		if ("签字".equals(billstatus)) {
			status = BillEnumCollection.BillSatus.Sign.VALUE;
		}
		if ("未确认".equals(billstatus)) {
			status = BillEnumCollection.BillSatus.UnComfirm.VALUE;
		}
		return status == null ? null : status.toString();
	}

	private String getApproveStatusCode(String approvestatus) {
		Integer status = null;
		if ("自由".equals(approvestatus)) {
			status = BillEnumCollection.ApproveStatus.NOSTATE.VALUE;
		} else if ("审批不通过".equals(approvestatus)) {
			status = BillEnumCollection.ApproveStatus.NOPASS.VALUE;
		}
		if ("审批通过".equals(approvestatus)) {
			status = BillEnumCollection.ApproveStatus.PASSING.VALUE;
		}
		if ("审批中".equals(approvestatus)) {
			status = BillEnumCollection.ApproveStatus.GOINGON.VALUE;
		}
		if ("已提交".equals(approvestatus)) {
			status = BillEnumCollection.ApproveStatus.COMMIT.VALUE;
		}
		return status == null ? null : status.toString();
	}

	private String getEffectStatusCode(String effectstatus) {
		Integer status = null;
		if ("已生效".equals(effectstatus)) {
			status = BillEnumCollection.InureSign.OKINURE.VALUE;
		} else if ("未生效".equals(effectstatus)) {
			status = BillEnumCollection.InureSign.NOINURE.VALUE;
		}
		return status == null ? null : status.toString();
	}

	private String getSrcCode(String src_syscode) {
		Integer srccode = null;
		if ("应收管理".equals(src_syscode)) {
			srccode = FromSystem.AR.VALUE;
		} else if ("应付管理".equals(src_syscode)) {
			srccode = FromSystem.AP.VALUE;
		} else if ("现金管理".equals(src_syscode)) {
			srccode = FromSystem.CMP.VALUE;
		} else if ("销售系统".equals(src_syscode)) {
			srccode = FromSystem.SO.VALUE;
		} else if ("采购系统".equals(src_syscode)) {
			srccode = FromSystem.PO.VALUE;
		} else if ("资金结算".equals(src_syscode)) {
			srccode = FromSystem.FTS.VALUE;
		} else if ("网上银行".equals(src_syscode)) {
			srccode = FromSystem.NET.VALUE;
		} else if ("付款排程".equals(src_syscode)) {
			srccode = FromSystem.PS.VALUE;
		} else if ("票据管理".equals(src_syscode)) {
			srccode = FromSystem.CFBM.VALUE;
		} else if ("协同单据".equals(src_syscode)) {
			srccode = FromSystem.XTDJ.VALUE;
		} else if ("信贷系统".equals(src_syscode)) {
			srccode = FromSystem.CDMA.VALUE;
		} else if ("项目管理".equals(src_syscode)) {
			srccode = FromSystem.XBGL.VALUE;
		} else if ("全面预算".equals(src_syscode)) {
			srccode = FromSystem.TB.VALUE;
		} else if ("内部交易".equals(src_syscode)) {
			srccode = FromSystem.TO.VALUE;
		} else if ("外部交换平台".equals(src_syscode)) {
			srccode = FromSystem.WBJHPT.VALUE;
		} else if ("资金计息".equals(src_syscode)) {
			srccode = FromSystem.ZJJX.VALUE;
		} else if ("库存管理".equals(src_syscode)) {
			srccode = FromSystem.IC.VALUE;
		} else if ("合同".equals(src_syscode)) {
			srccode = FromSystem.CT.VALUE;
		} else if ("运输".equals(src_syscode)) {
			srccode = FromSystem.YS.VALUE;
		} else if ("资产管理".equals(src_syscode)) {
			srccode = FromSystem.AM.VALUE;
		} else if ("费用管理".equals(src_syscode)) {
			srccode = FromSystem.WSBX.VALUE;
		} else if ("零售系统".equals(src_syscode)) {
			srccode = FromSystem.CR09.VALUE;
		}
		return srccode == null ? null : srccode.toString();
	}

	public String getDocByCode(String pk_org, String code, String mdid) {
		if (StringUtils.isEmpty(code))
			return null;

		IBDData bddata = GeneralAccessorFactory.getAccessor(mdid).getDocByCode(
				pk_org, code);
		if (bddata != null)
			return bddata.getPk();
		return null;
	}

	public String getDocByName(String pk_org, String name, String mdid) {
		if (StringUtils.isEmpty(name))
			return null;

		IBDData bddata = GeneralAccessorFactory.getAccessor(mdid)
				.getDocByNameWithMainLang(pk_org, name);
		if (bddata != null)
			return bddata.getPk();
		return null;
	}

	public String[] getDocNameCodeById(String pk, String mdid) {
		if (StringUtils.isEmpty(pk))
			return null;

		IBDData bddata = GeneralAccessorFactory.getAccessor(mdid)
				.getDocByPk(pk);
		if (bddata != null) {
			return new String[] { bddata.getCode(), bddata.getName().toString() };
		}
		return null;
	}

	public String getMDID(String key) {
		if (IBillFieldGet.PK_GROUP.equals(key)) {
			return IOrgMetaDataIDConst.GROUP;
		} else if (IBillFieldGet.PK_ORG.equals(key)
				|| IBillFieldGet.SETT_ORG.equals(key)) {
			return IOrgMetaDataIDConst.FINANCEORG;
		} else if (IBillFieldGet.CUSTOMER.equals(key)) {
			return IBDMetaDataIDConst.CUSTOMER;
		} else if (IBillFieldGet.SUPPLIER.equals(key)
				|| IBillFieldGet.ORDERCUBASDOC.equals(key)) {
			return IBDMetaDataIDConst.SUPPLIER;
		} else if (IBillFieldGet.PK_PSNDOC.equals(key)
				|| IBillFieldGet.PU_PSNDOC.equals(key)) {
			return IBDMetaDataIDConst.PSNDOC;
		} else if (IBillFieldGet.PK_DEPTID.equals(key)
				|| IBillFieldGet.PU_DEPTID.equals(key)) {
			return IBDMetaDataIDConst.DEPT;
		} else if (IBillFieldGet.PU_ORG.equals(key)) {
			return IOrgMetaDataIDConst.ORG;
		} else if (IBillFieldGet.PK_PCORG.equals(key)) {
			return IOrgMetaDataIDConst.LIABILITYCENTER;
		} else if (IBillFieldGet.BILLMAKER.equals(key)
				|| IBillFieldGet.APPROVER.equals(key)
				|| IBillFieldGet.COMMPAYER.equals(key)
				|| IBillFieldGet.OFFICIALPRINTUSER.equals(key)) {
			return IBDMetaDataIDConst.USER;
		} else if (IBillFieldGet.MATERIAL.equals(key)) {
			return IBDMetaDataIDConst.MATERIAL;
		} else if (("yf_" + IBillFieldGet.PK_PAYTERM).equals(key)) {
			return IBDMetaDataIDConst.PAYMENT;// 付款协议
		} else if (("ys_" + IBillFieldGet.PK_PAYTERM).equals(key)) {
			return IBDMetaDataIDConst.INCOME;// 收款协议
		} else if (IBillFieldGet.PK_CURRTYPE.equals(key)) {
			return IBDMetaDataIDConst.CURRTYPE;
		} else if (IBillFieldGet.TAXCODEID.equals(key)) {
			return IBDMetaDataIDConst.TAXCODE;
		} else if (IBillFieldGet.PK_SUBJCODE.equals(key)) {// 收支项目
			return IBDMetaDataIDConst.INOUTBUSICLASS;
		} else if (IBillFieldGet.CASHITEM.equals(key)) {// 现金流量项目
			return IBDMetaDataIDConst.CASHFLOW;
		} else if (IBillFieldGet.BANKROLLPROJET.equals(key)) {// 资金计划项目
			return IBDMetaDataIDConst.FUNDPLAN;
		} else if (("yf_" + IBillFieldGet.PAYACCOUNT).equals(key)
				|| ("ys_" + IBillFieldGet.RECACCOUNT).equals(key)) {// yf_付款银行账户||ys_收款银行账户（使用权）
			return IBDMetaDataIDConst.BANKACCSUB;
		} else if (("yf_" + IBillFieldGet.RECACCOUNT).equals(key)
				|| ("ys_" + IBillFieldGet.PAYACCOUNT).equals(key)) {// yf_收款银行账户
																	// ||ys_付款银行账户
			return IBDMetaDataIDConst.CUSTBANKACCSUB;
		} else if (IBillFieldGet.FREECUST.equals(key)) {// 散户
			return IBDMetaDataIDConst.FREECUST;
		} else if (IBillFieldGet.SO_TRANSTYPE.equals(key)) {// 渠道类型
			return IBDMetaDataIDConst.CHANNELTYPE;
		} else if (IBillFieldGet.COSTCENTER.equals(key)) {// 成本中心
			return IOrgMetaDataIDConst.COSTCENTER;
		} else if (IBillFieldGet.PRODUCTLINE.equals(key)) {// 产品线
			return IBDMetaDataIDConst.PRODLINE;
		} else if (IBillFieldGet.PK_RECPAYTYPE.equals(key)) {// 收付款类型
			return "d3d946fd-7ee3-4d72-9bd7-f9d254f9d084";
		} else if (IBillFieldGet.CHECKTYPE.equals(key)) {// 票据类型
			return IBDMetaDataIDConst.NOTETYPE;
		} else if (IBillFieldGet.CASHACCOUNT.equals(key)) {// 现金账户
			return IBDMetaDataIDConst.CASHACCOUNT;
		} else if (IBillFieldGet.PAYACCOUNT.equals(key)) {// 付款银行账户
			return IBDMetaDataIDConst.BANKACCSUB;
		} else if (IBillFieldGet.RECACCOUNT.equals(key)) {// 收款银行账户
			return IBDMetaDataIDConst.CUSTBANKACCSUB;
		} else if (IBillFieldGet.PROJECT.equals(key)) {// 项目
			return "2ee58f9b-781b-469f-b1d8-1816842515c3";
		}
		return null;
	}

	public List<String> orgDoc() {// 组织级档案
		String[] keys = new String[] { IBillFieldGet.CUSTOMER,
				IBillFieldGet.SUPPLIER, IBillFieldGet.PK_PSNDOC,
				IBillFieldGet.PK_DEPTID, IBillFieldGet.BILLMAKER,
				IBillFieldGet.APPROVER, IBillFieldGet.COMMPAYER,
				IBillFieldGet.OFFICIALPRINTUSER, IBillFieldGet.MATERIAL,
				IBillFieldGet.SUBJCODE, IBillFieldGet.CASHITEM,
				IBillFieldGet.BANKROLLPROJET, IBillFieldGet.PAYACCOUNT,
				IBillFieldGet.RECACCOUNT, IBillFieldGet.FREECUST,
				IBillFieldGet.SO_TRANSTYPE, IBillFieldGet.PRODUCTLINE,
				IBillFieldGet.ORDERCUBASDOC, IBillFieldGet.PU_PSNDOC,
				IBillFieldGet.PU_DEPTID, IBillFieldGet.PK_RECPAYTYPE,
				IBillFieldGet.CASHACCOUNT, "ys_" + IBillFieldGet.PK_PAYTERM,
				IBillFieldGet.PROJECT };
		return Arrays.asList(keys);
	}

	public List<String> groupDoc() {// 集团档案
		String[] keys = new String[] { IBillFieldGet.PK_ORG,
				IBillFieldGet.PK_PCORG, IBillFieldGet.PU_ORG,
				IBillFieldGet.COSTCENTER, IBillFieldGet.SETT_ORG,
				IBillFieldGet.CHECKTYPE };
		return Arrays.asList(keys);
	}

	public List<String> globalDoc() {// 全局档案
		String[] keys = new String[] { IBillFieldGet.PK_CURRTYPE,
				IBillFieldGet.PK_GROUP };
		return Arrays.asList(keys);
	}
}
