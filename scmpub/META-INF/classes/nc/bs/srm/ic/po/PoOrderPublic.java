package nc.bs.srm.ic.po;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import nc.bs.dao.DAOException;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.ic.pub.env.ICBSContext;
import nc.bs.logging.Logger;
import nc.bs.scmpub.query.SCMBillQuery;
import nc.bs.srm.pub.SenderQuerys;
import nc.cmp.bill.util.SysInit;
import nc.cmp.tools.StringUtil;
import nc.impl.pubapp.pattern.data.bill.BillQuery;
import nc.impl.pubapp.pattern.data.vo.VOQuery;
import nc.itf.bd.srmbdpub.OrgUnitPubService;
import nc.itf.material.mdm.SendMdmItf;
import nc.itf.pu.m21.IOrderMaintain;
import nc.itf.scmpub.reference.uap.bd.vat.BuySellFlagEnum;
import nc.itf.uap.pf.IplatFormEntry;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.md.model.IBean;
import nc.md.util.MDManageUtil;
import nc.vo.bd.material.MaterialConvertVO;
import nc.vo.ic.material.define.InvBasVO;
import nc.vo.ic.org.OrgVO;
import nc.vo.ic.pub.calc.BusiCalculator;
import nc.vo.ic.pub.util.VOEntityUtil;
import nc.vo.pu.m20.entity.PraybillVO;
import nc.vo.pu.m21.entity.OrderHeaderVO;
import nc.vo.pu.m21.entity.OrderItemVO;
import nc.vo.pu.m21.entity.OrderVO;
import nc.vo.pu.m21.rule.RelationCalculate;
import nc.vo.pu.m21.rule.api.fill.FillOrderVORule;
import nc.vo.pu.m21.rule.api.fill.FillPaymentInfo;
import nc.vo.pu.m21.rule.api.fill.FillPuMaterialUnitRule;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVOUtil;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.pattern.pub.PubAppTool;
import nc.vo.pubapp.util.VORowNoUtils;
import nc.vo.scmpub.fill.billfill.IBillValueFill;
import nc.vo.scmpub.util.translate.TranslateUtils;
import nccloud.api.jzsrm.AbstracProcessor4Ext;

import com.alibaba.fastjson.JSONObject;
import com.borland.dx.sql.metadata.MetaDataException;

public class PoOrderPublic extends AbstracProcessor4Ext {

	public SenderQuerys sqy = new SenderQuerys();
	public ICBSContext context = new ICBSContext();

	public Map<String,String> orgmap =  new HashMap<String,String>(){{
		put("10", "10");
		put("15", "10");
		put("16", "10");
		put("31", "31");
		put("23", "23");
		put("28", "28");
	}};

	public OrderVO[] queryVOByCode(String vbillcode, String pk_org)
			throws BusinessException {
		VOQuery<OrderHeaderVO> query = new VOQuery<OrderHeaderVO>(
				OrderHeaderVO.class);
		//723增加
		pk_org = orgmap.get(pk_org);


		String org_purchaseorg = sqy.getOrg_purchaseorg(pk_org);
		if (StringUtil.isEmpty(org_purchaseorg)) {
			throw new BusinessException("未找到对应的采购组织");
		}
		OrderHeaderVO[] hvos = query.query("  and vbillcode='" + vbillcode
				+ "'and bislatest = 'Y' and pk_org = '" + org_purchaseorg
				+ "';", null);
		if (hvos == null || hvos.length == 0) {
			throw new BusinessException("nc中不存在的采购订单,单据编号" + vbillcode);

		}

		BillQuery<OrderVO> billquery = new BillQuery<OrderVO>(OrderVO.class);
		OrderVO[] order = billquery
				.query(new String[] { hvos[0].getPk_order() });
		if (order == null || order.length == 0) {
			throw new BusinessException("未查询到采购订单");
		}

		return order;
	}


	public void fillup(OrderVO order) throws BusinessException {
		// TODO Auto-generated method stub
		OrderHeaderVO hvo = order.getHVO();
		hvo.setStatus(VOStatus.NEW);

		// fbillstatus 单据状态 fbillstatus int 单据状态
		// 0=自由，1=提交，2=正在审批，3=审批通过，4=审批未通过，5=关闭，
		OrderItemVO[] bvos = order.getBVO();
		// 补全行号
		VORowNoUtils.setVOsRowNoByRule(bvos, OrderItemVO.CROWNO);
		// 没有默认币种，取组织本位币
		String origcurr = this.getOrgCurr(hvo.getPk_org());
		// 如果取不到币种 就默认人民币
		if (PubAppTool.isNull(origcurr)) {
			hvo.setCorigcurrencyid("1002Z0100000000001K1");
		} else {
			hvo.setCorigcurrencyid(origcurr);
		}
		// 重算金额等
		RelationCalculate cal = new RelationCalculate();
		cal.calculate(order, "ntaxprice");
		// RelationCalculate cal = new RelationCalculate();
		cal.calculate(order, "ntaxmny");
		// }else {
		// cal.calculate(order, "ntaxmny");
		// }

		OrderVO[] bills = new OrderVO[] { order };
		// 补全相关信息
		IBillValueFill fillrule = null;

		fillrule = new FillOrderVORule();
		fillrule.fillValue(bills);
		// 补全一些默认值--接口项目 前面自己处理
		// fillrule= new FillDefaultValueRule();
		// fillrule.fillValue(bills);
		// 补全付款协议
		fillrule = new FillPaymentInfo();
		fillrule.fillValue(bills);
		// 补充订单表体的物料的主单位
		fillrule = new FillPuMaterialUnitRule();
		fillrule.fillValue(bills);
		// 前台验证公式
		for (int j = 0; j < order.getBVO().length; j++) {
			String pk_marbasclassSQL = "select pk_marbasclass from bd_material where pk_material = '"
					+ order.getBVO()[j].getPk_material() + "'";
			// 物料基本分类
			String pk_marbasclass = (String) getDao().executeQuery(
					pk_marbasclassSQL, new ColumnProcessor());
			// 单据类型
			String vtrantypecode = hvo.getCtrantypeid();
			// 组织
			String pk_org = hvo.getPk_org();
			String yzSql = "select dr from  view_materialcompare where pk_org = '"
					+ pk_org
					+ "' and pk_marbasclass = '"
					+ pk_marbasclass
					+ "' and pk_billtype = '" + vtrantypecode + "'";
			List<Map<String, String>> drs = (List<Map<String, String>>) getDao()
					.executeQuery(yzSql, new MapListProcessor());
			if (drs == null || drs.size() <= 0) {
				Logger.error("物料与单据类型不符，请修改！");
			}
		}

	}

	/**
	 * 获取组织币种
	 *
	 * @param pk_org
	 * @return
	 */
	public String getOrgCurr(String pk_org) {
		Map<String, String> orgCurrMap = null;
		orgCurrMap = OrgUnitPubService
				.queryOrgCurrByPk(new String[] { pk_org });
		if (orgCurrMap != null)
			return orgCurrMap.get(pk_org);
		return null;
	}

	public OrderHeaderVO chg2Hvo(JSONObject bill, OrderHeaderVO hvo)
			throws BusinessException {
		Map headmap = (Map) bill.getJSONObject("head");
		Iterator<String> iterator = headmap.keySet().iterator();
		while (iterator.hasNext()) {
			String itemkey = (String) iterator.next();
			if (headmap.get(itemkey) != null && !"".equals(itemkey)) {
				hvo.setAttributeValue(itemkey, headmap.get(itemkey));
			}

		}
		// 集团
		hvo.setPk_group(InvocationInfoProxy.getInstance().getGroupId());
		// 组织+组织版本
		String org_code = (String) headmap.get("pk_org");
		org_code = orgmap.get(org_code);
		if (StringUtil.isNotEmpty(org_code)) {
			String pk_org = sqy.getOrgpk(org_code);
			if (StringUtil.isEmpty(pk_org)) {
				throw new BusinessException("未找到对应的采购组织" + org_code);
			}
			hvo.setPk_org(pk_org);
			OrgVO orgvo = context.getOrgInfo().getOrgVO(pk_org);
			hvo.setPk_org_v(orgvo.getPk_vid());
		}
		// 是否退货
		String isreturn = (String) headmap.get("isreturn");
		if (StringUtil.isNotEmpty(isreturn)) {
			if ("1".equals(isreturn)) {
				hvo.setBreturn(new UFBoolean(true));
			} else {
				hvo.setBreturn(new UFBoolean(false));
			}
		}
		// 交易类型
		String vtrantypecode = (String) headmap.get("billTypeCode");

		if (StringUtil.isEmpty(vtrantypecode)) {
			throw new BusinessException("订单类型不能为空");
		}

		String ctrantypeid = (String) getDao().executeQuery(
				"   select pk_billtypeid from bd_billtype where pk_billtypecode='"
						+ vtrantypecode + "' ", new ColumnProcessor());
		hvo.setCtrantypeid(ctrantypeid);
		hvo.setVtrantypecode(vtrantypecode);
		// 业务流程
		String busiType = sqy.getBusiType(vtrantypecode, org_code);
		hvo.setPk_busitype(busiType);
		String supplierCode = (String) headmap.get("supplierCode");
		if (StringUtil.isNotEmpty(supplierCode)) {
			String pksupplier = sqy.getPksupplier(supplierCode);
			if (StringUtil.isEmpty(pksupplier)) {
				throw new BusinessException("未找到对应的供应商");
			}
			hvo.setPk_supplier(pksupplier);
		}

		// 业务员部门
		String cgy_code = (String) headmap.get("cgy_code");
		if (StringUtil.isNotEmpty(cgy_code)) {
			String cemployeeid = sqy.getCemployerByCode(cgy_code);

			if (StringUtil.isEmpty(cemployeeid)) {
				throw new BusinessException("未找到对应的采购员");
			}
			hvo.setCemployeeid(cemployeeid);
			// 部门+部门版本
			String deptid = sqy.getDeptidByPsnPK(cemployeeid);
			hvo.setPk_dept(deptid);
			String dept_v = sqy.getDept_v(deptid);
			hvo.setPk_dept_v(dept_v);
		}
		String cq = (String) headmap.get("cq");
		if (StringUtil.isNotEmpty(cq)) {
			String defdocPK = sqy.getDefdocPKByList(cq);
			if (StringUtil.isEmpty(defdocPK)) {
				throw new BusinessException("未找到对应的厂区");
			}
			hvo.setVdef1(defdocPK);
		}
		String jsfs = (String) headmap.get("jsfs");
		if (StringUtil.isNotEmpty(jsfs)) {
			String balatypePK = sqy.getBalatypePK(jsfs);
			if (StringUtil.isEmpty(balatypePK)) {
				throw new BusinessException("未找到对应的结算方式");
			}

			hvo.setPk_balatype(balatypePK);
		}

		// 付款协议
		String termCode = (String) headmap.get("termCode");
		if (StringUtil.isNotEmpty(termCode)) {
			String pk_paytermPk = sqy.getPk_paytermPk(termCode);
			if (StringUtil.isEmpty(pk_paytermPk)) {
				throw new BusinessException("付款协议不能为空");
			}
			hvo.setPk_payterm(pk_paytermPk);

		}
		// 单据日期
		String poDate = (String) headmap.get("poDate");
		if (StringUtil.isEmpty(poDate)) {
			throw new BusinessException("订单日期不能为空");
		}
		hvo.setDbilldate(new UFDate(poDate));

		// 备注
		String remark = (String) headmap.get("remark");
		hvo.setVmemo(remark);

		// 制单人
		String userId = InvocationInfoProxy.getInstance().getUserId();
		hvo.setBillmaker(userId);
		// 创建人
		hvo.setCreator(userId);
		// status
		if (StringUtil.isEmpty(hvo.getPrimaryKey())) {
			hvo.setStatus(VOStatus.NEW);
		} else {
			hvo.setStatus(VOStatus.UPDATED);
		}

		// 版本号
		Integer nversion = hvo.getNversion();
		if (nversion == null) {
			nversion = 0;
		}
		hvo.setNversion(nversion + 1);
		// 最新版本
		hvo.setBislatest(new UFBoolean(true));
		if (hvo.getBreturn() == null) {
			// 不设置 有的环境会报空指针 。。。。。。。。。。。。。。。。
			hvo.setBreturn(UFBoolean.FALSE);
		}
		hvo.setPk_invcsupllier(hvo.getPk_supplier());

		hvo.setFhtaxtypeflag(1);
		// 基于原订单退货
		hvo.setBrefwhenreturn(UFBoolean.TRUE);

		return hvo;

	}

	/**
	 * 转换订单行
	 *
	 * @param childVomap
	 * @param vo
	 * @return
	 * @throws BusinessException
	 */
	public OrderItemVO chg2Body(Map childVomap, OrderHeaderVO hvo,
								OrderItemVO vo) throws BusinessException {
		// TODO Auto-generated method stub
		vo.setPk_group(hvo.getPk_group());
		vo.setPk_org(hvo.getPk_org());
		vo.setPk_org_v(hvo.getPk_org_v());
		vo.setDbilldate(hvo.getDbilldate());
		vo.setPk_supplier(hvo.getPk_supplier());

		// //购销类型
		vo.setFbuysellflag(BuySellFlagEnum.NATIONAL_BUY.value());
		// 地区默认国内
		vo.setCrececountryid("0001Z010000000079UJJ");
		vo.setCtaxcountryid("0001Z010000000079UJJ");
		vo.setCsendcountryid("0001Z010000000079UJJ");

		// 币种
		vo.setCorigcurrencyid(hvo.getCorigcurrencyid());
		// 折本汇率
		vo.setNexchangerate(new UFDouble(1.0));

		Iterator iterator = childVomap.keySet().iterator();
		while (iterator.hasNext()) {
			String itemkey = (String) iterator.next();
			// d有些字段NC同名先进行赋值,
			String value = (String) childVomap.get(itemkey);
			vo.setAttributeValue(itemkey, value);

			// 到货库存组织
			//723业务实体SRM，org_corp
//			if ("Kczz".equals(itemkey)) {
//				String pk_arrvstoorg = sqy.getStockorgPk(value);
//				if (StringUtil.isEmpty(pk_arrvstoorg)) {
//					throw new BusinessException("未找到库存组织");
//				}
//
//				vo.setPk_arrvstoorg(pk_arrvstoorg);
//				OrgVO orgvo = context.getOrgInfo().getOrgVO(pk_arrvstoorg);
//				vo.setPk_arrvstoorg_v(orgvo.getPk_vid());
//			}

			// 费用部门
			if ("attributeVarchar1".equals(itemkey)) {
				String pk_deptid = sqy.getDeptPk(value);
				vo.setPk_reqdept(value);
				String pk_reqdept_v = sqy.getDept_v(pk_deptid);
				vo.setPk_reqdept_v(pk_reqdept_v);
			}
			// 原币币种
			if ("currencyCode".equals(itemkey)) {
				String currtypeid = sqy.getCurrtypeid(value);
				if (StringUtil.isEmpty(currtypeid)) {
					throw new BusinessException("未找到对应的本币币种 " + value);
				}
				vo.setCcurrencyid(currtypeid);

			}
			if ("manufacturer".equals(itemkey)) {
				if (StringUtil.isNotEmpty(value)) {
					String getDefdocPK = sqy.getDefdocPK(value);
					if (StringUtil.isEmpty(getDefdocPK)) {
						throw new BusinessException("未找到对应的生产厂商 " + value);
					}
					vo.setCproductorid(getDefdocPK);
				}

			}
			if ("closedFlag".equals(itemkey)) {
				if ("0".equals(value)) {
					vo.setBstockclose(new UFBoolean(false));
				} else {
					vo.setBstockclose(new UFBoolean(false));
				}
			}

			if ("freeFlag".equals(itemkey)) {
				if ("0".equals(value)) {
					vo.setBlargess(new UFBoolean(false));
				} else {
					vo.setBlargess(new UFBoolean(true));
				}
			}

		}


		// 收货仓库
		String Shck = (String) childVomap.get("Shck");
		if (!StringUtil.isEmpty(Shck)) {
//			throw new BusinessException("收货仓库不能为空");
			String stordocpk = sqy.getStordocpk(Shck);
			vo.setPk_recvstordoc(stordocpk);
		}




		/*这里用新的字段接720改成pk_reqcorp需求公司*/
//		vo.setPk_reqcorp(vo.getPk_reqcorp());
		String orgCrop = sqy.getCorpoid( (String)childVomap.get("Kczz"));
		if(StringUtil.isEmpty(orgCrop)){
			throw new BusinessException("表体Kczz（库存组织）为空，请检查数据后重试！");
		}
		vo.setPk_reqcorp(orgCrop);

		// 需求库存组织    组织_业务单元_库存组织版本信息 库存组织版本
		vo.setPk_reqstoorg(sqy.getStockorgPk( (String)childVomap.get("Kczz")));

		OrgVO orgvo = context.getOrgInfo().getOrgVO(sqy.getStockorgPk( (String)childVomap.get("Kczz")));
//		vo.setPk_arrvstoorg_v(orgvo.getPk_vid());
		vo.setPk_reqstoorg_v(orgvo.getPk_vid());



		/*
		 * 20250714 到货库存由于外沙和岩头的采购时需要由本部来采购，
		 * 且收货也是先到本部，导致后续物料到货时到货库存组织会有时候先错的情况，所以这里需要检查并修正 通过参数来控制，增加业务参数，
		 */
		//------------------------------------------------------------------
		String kczz_code = (String)childVomap.get("Kczz");
		UFBoolean paraBoolean = SysInit.getParaBoolean(sqy.getOrgpkByCode(kczz_code),
				"JZ_SRM001");
		if (null != paraBoolean && paraBoolean == UFBoolean.TRUE) {
			kczz_code = orgmap.get(kczz_code);
			orgvo = context.getOrgInfo().getOrgVO(sqy.getStockorgPk(kczz_code));

			vo.setPk_arrvstoorg(orgvo.getPk_org()); // 收货仓库
			vo.setPk_arrvstoorg_v(orgvo.getPk_vid()); //收货仓库版本
			vo.setPk_recvstordoc(sqy.getStordocpks(vo.getPk_org(),  (String) childVomap.get("Shck"))); // 仓库ID重新处理
		}else {
			vo.setPk_arrvstoorg(sqy.getStockorgPk((String)childVomap.get("Kczz")));
			vo.setPk_arrvstoorg_v(orgvo.getPk_vid());
		}
		//--------------------------------------------------------------------------------------------


		String lineNum = (String) childVomap.get("lineNum");
		if (StringUtil.isEmpty(lineNum)) {
			throw new BusinessException("来源订单行号不能为空");
		}
		vo.setVbdef2(lineNum);

		// pk_apfinanceorg 应付组织最新版本 pk_apfinanceorg varchar(20)
		// 组织_业务单元_财务组织 财务组织
		// pk_apfinanceorg_v 应付组织 pk_apfinanceorg_v varchar(20)
		// 组织_业务单元_财务组织版本信息 财务组织版本
		vo.setPk_apfinanceorg(vo.getPk_org());
		vo.setPk_apfinanceorg_v(vo.getPk_org_v());
		// 计划订单日期 如果计划到货日期比订单日期早就用订单日期
		UFDate dplanarrvdate = vo.getDplanarrvdate();
		if (dplanarrvdate.compareTo(hvo.getDbilldate()) < 0) {
			vo.setDplanarrvdate(hvo.getDbilldate());
		}
		// pk_psfinanceorg 结算财务组织最新版本 pk_psfinanceorg varchar(20)
		// 组织_业务单元_财务组织 财务组织
		// pk_psfinanceorg_v 结算财务组织 pk_psfinanceorg_v varchar(20)
		// 组织_业务单元_财务组织版本信息
		vo.setPk_psfinanceorg(hvo.getPk_org());
		vo.setPk_psfinanceorg_v(hvo.getPk_org_v());
		// 物流组织最
		vo.setPk_flowstockorg(vo.getPk_org());
		vo.setPk_flowstockorg_v(vo.getPk_org_v());
		//

		// 项目
		String attributeVarchar6 = (String) childVomap.get("attributeVarchar6");
		vo.setCprojectid(attributeVarchar6);

		// /外系统编码转换成nc
		String[] formulas_B46 = {
				// 项目
				"cprojectid->getColValue(bd_project,pk_project,project_code,cprojectid )",
				// 收货仓库
				"pk_stordoc->getColValue(bd_stordoc,pk_stordoc ,code,pk_stordoc)" };

		SuperVOUtil.execFormulaWithVOs(new OrderItemVO[] { vo }, formulas_B46,
				null);

		// -------物料相关
		String Invcode = (String) childVomap.get("Invcode");
		if (StringUtil.isEmpty(Invcode)) {
			throw new BusinessException("物料编码不能为空");
		}




		IBean bean_body = MDManageUtil.getBeanByContainedObject(vo);
		String cmaterialid = TranslateUtils.trancelateCodeToID(bean_body,
				"pk_material", Invcode, hvo.getPk_org());

		if (getString_TrimZeroLenAsNull(cmaterialid) == null) {
			//物料获取失败，应该是采购组织和物料所属组织不一致，且未得到分配，尝试通过主数据获取本组织物料
			SendMdmItf lookup = NCLocator.getInstance().lookup(SendMdmItf.class);
			String materialCodeChange = lookup.materialCodeChange(Invcode, hvo.getPk_org())	;
			vo.setPk_material(materialCodeChange);
			cmaterialid = TranslateUtils.trancelateCodeToID(bean_body,
					"pk_material", materialCodeChange, hvo.getPk_org());
			if(getString_TrimZeroLenAsNull(cmaterialid) == null){
				throw new BusinessException("Invcode" + Invcode + "物料在nc中不存在,且未通过主数据配置到目标公司物料。" +
						"请联系物料管理员对物料档案进行分配或关联采购组织数据。");
			}
		}





		InvBasVO basvo = context.getInvInfo().getInvBasVO(cmaterialid);
		vo.setPk_srcmaterial(basvo.getPk_source());
		vo.setPk_material(cmaterialid);
		vo.setCunitid(basvo.getPk_measdoc());
//		vo.setCunitid(basvo.getPk_measdoc());
//		// 报价单位按照主单位,

		// mengeRk QUAN 13 入库数量
		if (hvo.getBreturn() == null) {
			hvo.setBreturn(UFBoolean.FALSE);
		}
		// -------单价相关 含税单价
		String ntaxPrice = (String) childVomap.get("ntaxPrice");
		vo.setNtaxprice(new UFDouble(ntaxPrice));
		// -------数量相关
		// MENGE QUAN 13 采购订单数量
		String quantity = (String) childVomap.get("nnum");
		if (quantity == null) {
			throw new BusinessException("数量不能为空");
		}
		UFDouble nnum = getUFDouble_NullAsZero(quantity);
		if (nnum.doubleValue() == 0) {
			throw new BusinessException("数量不能为空");
		}
		// ERP退货的处理--直接根据原正订单退货，增加判断处理，如果实际入库数量是负数
		// 则判定采购订单是退货 负订单
		if (hvo.getBreturn().booleanValue() && nnum.doubleValue() > 0) {
			nnum = nnum.multiply(-1);
		}
		// 计量单位 "1. 如果是产成品，销售单位如果不是件，则认为数量 是辅数量，单价是辅数量的单价，需要换算主数量
		// 2. 如果是非产成品，则主辅计量单位一致
		// 3. 主辅计量单位，取NC的主辅计量单位"
		vo.setNnum(nnum);


		vo.setCqtunitid(vo.getCunitid());
		vo.setVqtunitrate("1/1");
		MaterialConvertVO invMeasVO = getInvMeasVO2(cmaterialid);
		// 如果有辅计量 用辅计量 如果没有用主计量
		if (invMeasVO != null) {
			String castunitid = invMeasVO.getPk_measdoc();
			vo.setCastunitid(castunitid);
			vo.setVchangerate(invMeasVO.getMeasrate());
			// 根据主数量换算辅助数量,并按照辅单位得到精度
			BusiCalculator ncalc = BusiCalculator.getBusiCalculatorAtBS();
			UFDouble nastnum = ncalc.calculateAstNum(vo.getNnum(),
					vo.getVchangerate(), vo.getCunitid());
			vo.setNastnum(nastnum);
		} else {
			// 单位主键
			vo.setCastunitid(vo.getCunitid());
			// 换算率
			vo.setVchangerate("1/1");
			// 数量
			vo.setNastnum(nnum);
		}
		// BRTWR QUAN 13 采购订单含税金额
		UFDouble ntaxmny = getUFDouble_NullAsZero(childVomap.get("ntaxmoney"));
		if (hvo.getBreturn().booleanValue() && ntaxmny.doubleValue() > 0) {
			ntaxmny = ntaxmny.multiply(-1);
		}
		vo.setNtaxmny(ntaxmny);
		// 本币价税合计为 0 时，强制标记为赠品（优先级高于 freeFlag）
		if (ntaxmny.doubleValue() == 0) {
			vo.setBlargess(UFBoolean.TRUE);
			// 赠品需要设置所有单价字段为 0，避免后续校验报错
			vo.setNqtorigprice(UFDouble.ZERO_DBL);      // 无税单价
			vo.setNqtorigtaxprice(UFDouble.ZERO_DBL);   // 含税单价
			vo.setNorigtaxprice(UFDouble.ZERO_DBL);     // 主含税单价
			vo.setNorigprice(UFDouble.ZERO_DBL);        // 主无税单价
		}
		// 税率ntaxrate
		// MWSKZ_SL CHAR 10 税率
		String ntaxrate = (String) childVomap.get("taxRate");
		if (getString_TrimZeroLenAsNull(ntaxrate) == null) {
			throw new BusinessException("税率不能为空");
		}
		UFDouble nmwskzSl = new UFDouble(ntaxrate);
		vo.setNtaxrate(nmwskzSl);
		// 税码
		String taxcode = (String) childVomap.get("TaxCode");
		String ctaxcodeid = sqy.getTaxcode(taxcode);
		if (StringUtil.isEmpty(ctaxcodeid)) {
			throw new BusinessException("未找到对应的税码");
		}
		vo.setCtaxcodeid(ctaxcodeid);

		// ftaxtypeflag 扣税类别 ftaxtypeflag int 扣税类别
		// 1=应税外加，0=应税内含，
		vo.setFtaxtypeflag(1);

		return vo;
	}

	/**
	 * 获取存货的计量信息
	 *
	 * @param cmaterialid
	 * @return
	 * @throws BusinessException
	 * @throws MetaDataException
	 */
	public MaterialConvertVO getInvMeasVO2(String cmaterialid)
			throws BusinessException {
		// TODO Auto-generated method stub
		VOQuery<MaterialConvertVO> qry = new VOQuery<MaterialConvertVO>(
				MaterialConvertVO.class);
		MaterialConvertVO[] convertVOs = qry.query(" and pk_material='"
				+ cmaterialid + "'", null);
		try {
			if (convertVOs != null && convertVOs.length > 0) {
				return convertVOs[0];
			}
		} catch (MetaDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BusinessException("查询物料信息异常" + e.getMessage());

		}
		return null;
	}

	public OrderVO del(OrderVO billvo) throws BusinessException {
		HashMap map1 = new HashMap();
		// map1.put("nc.bs.scmpub.pf.ORIGIN_VO_PARAMETER", aggvoss);
		map1.put("notechecked", "notechecked");
		// 弃审
		if (3 == billvo.getHVO().getForderstatus()) {
			IplatFormEntry iIplatFormEntry = (IplatFormEntry) NCLocator
					.getInstance().lookup(IplatFormEntry.class.getName());
			Object retObj = iIplatFormEntry.processAction("UNAPPROVE"
					+ billvo.getHVO().getCreator(), billvo.getHVO()
					.getVtrantypecode(), null, billvo, null, map1);
		}

		//
		String pk = billvo.getPrimaryKey();
		OrderVO[] aggvo = queryVOByPk(pk);

		IOrderMaintain service = (IOrderMaintain) NCLocator.getInstance()
				.lookup(IOrderMaintain.class);
		service.delete(aggvo, null);

		return billvo;
	}

	// public
	// 查询采购订单
	public OrderVO[] queryaggvo(String vbillcode) throws BusinessException {
		String sql = "select pk_order from po_order where vbillcode = '"
				+ vbillcode + "'and bislatest = 'Y'";
		String pk = (String) getDao().executeQuery(sql, new ColumnProcessor());
		BillQuery<OrderVO> billquery = new BillQuery<OrderVO>(OrderVO.class);
		OrderVO[] aggvo = billquery.query(new String[] { pk });

		return aggvo;
	}

	// 查询请购单
	public PraybillVO[] queryQg(String vbillcode) throws BusinessException {
		String sql = "select pk_praybill from po_praybill where vbillcode = '"
				+ vbillcode + "'and dr = 0";
		String pk = (String) getDao().executeQuery(sql, new ColumnProcessor());
		BillQuery<PraybillVO> billquery = new BillQuery<PraybillVO>(
				PraybillVO.class);
		PraybillVO[] aggvo = billquery.query(new String[] { pk });

		return aggvo;
	}

	// public
	// 查询采购订单
	public OrderVO[] queryVOByPk(String pk) throws DAOException {
		BillQuery<OrderVO> billquery = new BillQuery<OrderVO>(OrderVO.class);
		OrderVO[] aggvo = billquery.query(new String[] { pk });
		return aggvo;
	}

	public OrderVO[] qryOrignBills(OrderVO[] billvos) throws BusinessException {
		String[] hids = VOEntityUtil.getPksFromAggVO(billvos);
		SCMBillQuery<OrderVO> queryTool = new SCMBillQuery<OrderVO>(
				OrderVO.class);
		OrderVO[] orderVOs = queryTool.queryVOByIDs(hids);
		return orderVOs;
	}

	@Override
	public JSONObject process(Object billvo) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
