package nc.bs.srm.bd;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import nc.bs.dao.DAOException;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.srm.pub.SenderQuerys;
import nc.cmp.tools.StringUtil;
import nc.impl.pubapp.pattern.data.vo.VOQuery;
import nc.itf.bd.bankacc.base.IBankAccBaseInfoService;
import nc.itf.bd.pubinfo.ILinkmanService;
import nc.itf.bd.supplier.baseinfo.ISupplierBaseInfoService;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.vo.arap.bill.util.VORowNoUtils;
import nc.vo.bd.bankaccount.BankAccSubVO;
import nc.vo.bd.bankaccount.BankAccbasVO;
import nc.vo.bd.bankdoc.BankdocVO;
import nc.vo.bd.cust.CustbankVO;
import nc.vo.bd.linkman.LinkmanVO;
import nc.vo.bd.supplier.SupLinkmanVO;
import nc.vo.bd.supplier.SupplierVO;
import nc.vo.ic.m4q.entity.LocAdjustBodyVO;
import nc.vo.ic.m4q.entity.LocAdjustHeadVO;
import nc.vo.ic.m4q.entity.LocAdjustVO;
import nc.vo.ic.pub.define.ICPubMetaNameConst;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVOUtil;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.sm.UserVO;
import nccloud.api.jzsrm.AbstracProcessor4Ext;
import bsh.ParseException;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 供应商新增 根据传递过来的数据生成供应商新增
 * 
 * @author Solomon
 * 
 */
public class SRM_Suppiler extends AbstracProcessor4Ext {

	private SenderQuerys sqy = new SenderQuerys();

	@Override
	public JSONObject process(Object billvo) throws Exception {
		JSONObject reqJson = (JSONObject) billvo;
		String taxpayerid = reqJson.getJSONObject("bill").getJSONObject("head")
				.getString("taxpayerid");
		if (taxpayerid.length() > 20) {
			throw new BusinessException(
					"新增供应商成功或修改供应商出错：纳税人登记号taxpayerid:长度过长 ");
		}
		if (StringUtil.isEmpty(taxpayerid)) {
			throw new BusinessException("新增供应商成功或修改供应商出错：纳税人登记号taxpayerid：不能为空");
		}
		String sql = "select pk_supplier from bd_supplier where taxpayerid = '"
				+ taxpayerid + "'";
		String pk_supplier = (String) getDao().executeQuery(sql,
				new ColumnProcessor());
		SupplierVO[] inbills = null;
		if (StringUtil.isEmpty(pk_supplier)) {
			// 供应商新增
			inbills = saveBill(reqJson);
		} else {
			// 供应商修改
			inbills = editBill(reqJson, taxpayerid);
		}
		JSONObject rs = new JSONObject();
		String vbillcode = inbills[0].getCode();
		rs.put("erp_code", vbillcode);
		return this.getRsultDataSuccess(rs, "新增供应商成功或修改供应商成功");
	}

	public SupplierVO[] saveBill(JSONObject jsonObject)
			throws BusinessException {
		SupplierVO[] apprveddBills = null;
		SupplierVO[] outbills = null;
		try {

			// 传过来的数据生成供应商新增
			outbills = getInvCountBillVO(jsonObject, null);
			// fillup(outbills);
			// 保存
			apprveddBills = savePurchaseInVOCommit(outbills);
			// 2.生成供应商银行账户
			List<BankAccbasVO> bankAccbas = getBillDetailVO2(jsonObject,
					apprveddBills[0]);
			BankaccbasForBpmAdd bakAccBP = new BankaccbasForBpmAdd();
			for (BankAccbasVO bankAccVO : bankAccbas) {
				bakAccBP.processBill(bankAccVO,apprveddBills[0]);
			}
			// 签字
			// apprveddBills = savePurchaseInVOCommit(outbills);
		} catch (Exception e) {
			ExceptionUtils.wrappBusinessException("新增供应商成功或修改供应商出错："
					+ e.getMessage());
		}
		return apprveddBills;
	}

	private SupplierVO[] getInvCountBillVO(JSONObject jsonObject, SupplierVO vo)
			throws BusinessException, ParseException {
		SupplierVO superVO = new SupplierVO();

		if (vo != null) {
			superVO = vo;
		}

		JSONObject json = jsonObject.getJSONObject("bill");
		// 供应商联系人
		JSONArray bjson = json.getJSONArray("linkman");
		JSONObject head = json.getJSONObject("head");

		Iterator<String> iterator = head.keySet().iterator();
		while (iterator.hasNext()) {
			String next = iterator.next();
			if (!"corpaddress".equals(next)) {
				superVO.setAttributeValue(next, head.get(next));
			}
		}
		String corpaddress = superVO.getCorpaddress();
		if (StringUtil.isNotEmpty(corpaddress)) {
			superVO.setDef2(corpaddress);
		}
		String supstate = head.getString("supstate");
		if (StringUtil.isEmpty(supstate)) {
			superVO.setSupstate(1);
		}
		// String pk_areacl = head.getString("pk_areacl");

		// 预留字段
		// String Field1 = json.getString("Field1");
		// head.setDef1(Field1);
		// String Field2 = json.getString("Field2");
		// head.setDef1(Field1);
		// String Field3 = json.getString("Field3");
		// head.setDef1(Field1);

		superVO.setPk_group("0001V110000000000FH0");
		String pk_supplier = superVO.getPk_supplier();
		if (StringUtil.isEmpty(pk_supplier)) {
			superVO.setStatus(VOStatus.NEW);

		} else {
			superVO.setStatus(VOStatus.UPDATED);

		}
		// 组织
		superVO.setPk_org("0001V110000000000FH0");
		// 时区
		superVO.setPk_timezone("0001Z010000000079U2P");
		// 数据格式 一会改一下
		superVO.setPk_format("FMT0Z000000000000000");
		// 最后修改时间
		superVO.setModifiedtime(new UFDateTime(new Date()));
		// 最后修改人
		String userId = InvocationInfoProxy.getInstance().getUserId();

		superVO.setModifier(userId);
		// 币种
		if (!StringUtil.isEmpty(superVO.getPk_currtype())) {
			String currtypeid = sqy.getCurrtypeid(superVO.getPk_currtype());
			superVO.setPk_currtype(currtypeid);
		}
		String defdocPkSup = sqy.getDefdocPkSup(null);
		superVO.setDef1(defdocPkSup);
		// 国家
		if (!StringUtil.isEmpty(superVO.getPk_country())) {
			String getPk_country = sqy.getcountry(superVO.getPk_country());
			if (StringUtil.isEmpty(getPk_country)) {
				throw new BusinessException("未找到对应的国家地区，请检查国家地区编码是否正确");
			}
			superVO.setPk_country(getPk_country);
		}
		// addBank(bank, superVO);

		// 供应商分类
		String pk_supplierclass = superVO.getPk_supplierclass();
		if (StringUtil.isEmpty(pk_supplierclass)) {
			throw new BusinessException("请输入供应商分类");
		}
		String pKsupplierclass = sqy.getPKsupplierclass(pk_supplierclass);
		if (StringUtil.isEmpty(pKsupplierclass)) {
			throw new BusinessException("未找到对应的供应商分类");
		}
		superVO.setPk_supplierclass(pKsupplierclass);
		// 供应商联系人
		List<SupLinkmanVO> billDetailVO = getBillDetailVO(bjson, superVO);
		superVO.setSuplinkman(billDetailVO
				.toArray(new SupLinkmanVO[billDetailVO.size()]));
		
		return new SupplierVO[] { superVO };
	}

	// 供应商新增
	public SupplierVO[] savePurchaseInVOCommit(SupplierVO[] prebill)
			throws BusinessException {

		ISupplierBaseInfoService service1 = (ISupplierBaseInfoService) NCLocator
				.getInstance().lookup(ISupplierBaseInfoService.class);
		SupplierVO sales = service1.insertSupplierVO(prebill[0], false);

		return new SupplierVO[] { sales };
	}

	// 供应商修改
	public SupplierVO[] savePurchaseInVOUpdate(SupplierVO[] prebill)
			throws BusinessException {

		ISupplierBaseInfoService service1 = (ISupplierBaseInfoService) NCLocator
				.getInstance().lookup(ISupplierBaseInfoService.class);
		SupplierVO sales = service1.updateSupplierVO(prebill[0], false);
		return new SupplierVO[] { sales };
	}

	public LocAdjustVO getGeneralOutVO(LocAdjustVO inbill, JSONObject jsonObject)
			throws DAOException, BusinessException, ParseException {
		LocAdjustHeadVO head = inbill.getHead();
		LocAdjustBodyVO[] bodys = inbill.getBodys();
		VORowNoUtils.setVOsRowNoByRule(bodys, ICPubMetaNameConst.CROWNO);// 行号处理

		return inbill;
	}

	UserVO getUserVO() throws BusinessException {
		UserVO uservo = getfillUpRule().getUser(
				InvocationInfoProxy.getInstance().getUserId());
		return uservo;
	}

	/**
	 * 联系人新增与修改 如果是修改联系人 需要删除所有联系人 然后重新创建新的联系人
	 * 
	 * @param bill
	 * @param superVO
	 * @return
	 * @throws BusinessException
	 */
	private List<SupLinkmanVO> getBillDetailVO(JSONArray bill,
			SupplierVO superVO) throws BusinessException {

		List<SupLinkmanVO> list = new ArrayList<SupLinkmanVO>();
		// 查询供应商联系人
		List<Map<String, String>> supLinkman = selBankLinkVO(superVO
				.getPk_supplier());
		ILinkmanService serviceDel = NCLocator.getInstance().lookup(
				ILinkmanService.class);

		
		// 获取供应商联系人vo
		// 如果新增不删除，修改先删后增
		if (supLinkman != null && supLinkman.size() > 0) {
			// 删除供应商联系人
			String delLinkMan = "delete from bd_suplinkman where   pk_supplier ='"
					+ superVO.getPk_supplier() + "' ";
			getDao().executeUpdate(delLinkMan);
			for (int i = 0; i < supLinkman.size(); i++) {
				Map<String, String> Linkman = supLinkman.get(i);
				LinkmanVO selLinkVO = new LinkmanVO();
				// 删除联系人
				selLinkVO.setPk_linkman(Linkman.get("pk_linkman"));
				serviceDel.delete(selLinkVO);
			}

		}
		// 新增供应商联系人
		for (int i = 0; i < bill.size(); i++) {
			SupLinkmanVO svo = new SupLinkmanVO();
			LinkmanVO link = new LinkmanVO();
			JSONObject json = bill.getJSONObject(i);
			String linkman_name = json.getString("linkman_name");

			if (StringUtil.isEmpty(linkman_name)) {
				throw new BusinessException("联系人姓名不能为空");
			}
			link.setName(linkman_name);

			String linkman_sex = json.getString("linkman_sex");
			if (StringUtil.isNotEmpty(linkman_sex)) {
				Integer sea = 2;
				if ("男".equals(linkman_sex)) {
					sea = 1;
				}
				link.setSex(sea);
			}

			String linkman_tel = json.getString("linkman_tel");
			if (StringUtil.isNotEmpty(linkman_tel)) {
				link.setPhone(linkman_tel);
			}
			String linkman_post = json.getString("linkman_post");
			if (StringUtil.isNotEmpty(linkman_post)) {
				link.setVjob(linkman_post);
			}
			link.setStatus(VOStatus.NEW);

			// 先保存联系人，以便已录人员主键
			String linkmanpk = serviceDel.insert(link);
			String linkPk = linkmanpk;
			svo.setStatus(VOStatus.NEW);
			svo.setPk_linkman(linkPk);
			svo.setIsdefault(link.getIsdefault());
			svo.setDr(0);

			list.add(svo);
		}
		return list;

	}

	/**
	 * 银行账户信息
	 * 
	 * @param bill
	 * @param order
	 * @return
	 * @throws BusinessException
	 */
	private List<BankAccbasVO> getBillDetailVO2(JSONObject bill,
			SupplierVO order) throws BusinessException {
		List<BankAccbasVO> list = new ArrayList<BankAccbasVO>();
		JSONObject jsonObject = bill.getJSONObject("bill");
		String pk_supplier = order.getPk_supplier();
		
		//所有银行账户信息
		Map<String,BankAccbasVO> bamk = getBankAll();
		
		JSONArray childArray = jsonObject.getJSONArray("bank"); // 如果json格式的字符串里含有数组格式的属性，将其转换成JSONArray，以方便后面转换成对应的实体
		List<CustbankVO> custList = (List<CustbankVO>)getDao().retrieveByClause(CustbankVO.class,"dr = 0 and pk_cust = '"+pk_supplier+"'");
		for (int i = 0; i < childArray.size(); i++) {
			BankAccbasVO bk = new BankAccbasVO();
			
			Map<String, Object> childVomap = (Map) childArray.get(i);

			String linkman_sex = (String) childVomap.get("accnum");
			if (StringUtil.isEmpty(linkman_sex)) {
				throw new BusinessException("户号不能为空");
			}
			BankAccbasVO vo = bamk.get(linkman_sex);
			if(vo != null){
				bk = vo;
			}
			for (String itemkey : childVomap.keySet()) {
				if ("bankdoc".equals(itemkey)) {
					String pk_bankdoc = childVomap.get(itemkey) == null ? ""
							: childVomap.get(itemkey).toString();
					String sql = "select  f.pk_bankdoc\n"
							+ "from  bd_bankdoc   f\n" + "where f.code='"
							+ pk_bankdoc + "'\n" + "and  f.dr=0";
					String pk = getStrFsql(sql);
					if (StringUtil.isEmpty(pk)) {
						throw new BusinessException(pk_bankdoc + "不存在.");
					}
					bk.setPk_bankdoc(pk);
					continue;
				}

//				if ("bank_type".equals(itemkey)) {
//					String pk_banktype = childVomap.get(itemkey) == null ? ""
//							: childVomap.get(itemkey).toString();
//					
//					String sql = "select f.pk_banktype\n"
//							+ "from  bd_banktype f\n" + "where f.name like'%"
//							+ pk_banktype + "%' " + "and f.dr=0";
//
//					String pk = getStrFsql(sql);
//					if(StringUtil.isEmpty(pk)){
//						throw new BusinessException("未找到对应的银行类别");
//					}
//					bk.setPk_banktype(pk);
//					continue;
//				}
				//
				if (childVomap.get(itemkey) != null
						&& !"".equals(childVomap.get(itemkey))) {
					bk.setAttributeValue(itemkey, childVomap.get(itemkey));

				}
			}
			String bankdoc = (String)childVomap.get("bankdoc");
			if (StringUtil.isEmpty(bankdoc)) {
				throw new BusinessException("开户行不能为空");
			}
			bk.setPk_group(InvocationInfoProxy.getInstance().getGroupId());
			bk.setPk_org(bk.getPk_group());
			bk.setAccclass(3);
			bk.setAccstate(0);
			bk.setName(bk.getAccname());
			// 银行状态启用20230223
			bk.setEnablestate(2);
			String linkman_name = (String) childVomap.get("accname");
			bk.setAccountproperty(0);
			if (StringUtil.isEmpty(linkman_name)) {
				throw new BusinessException("户名不能为空");
			}
			//人行行号连号
			List<BankdocVO> retrieveByClause = (List<BankdocVO>)getDao().retrieveByClause(BankdocVO.class, "pk_bankdoc = '"+bk.getPk_bankdoc()+"'");
			if(retrieveByClause ==null){
				throw new BusinessException("未找到对应的银行档案");	
			}
			//人行连号
			bk.setCombineaccnum(retrieveByClause.get(0).getPcombinenum());
			//人行连号名称
			bk.setCombineaccname(retrieveByClause.get(0).getPcombinename());
			//银行类别
			bk.setPk_banktype(retrieveByClause.get(0).getPk_banktype());
			bk.setAccname(linkman_name);
			bk.setAccnum(linkman_sex);
			BankAccSubVO bankSub = new BankAccSubVO();
			String pk_currtype = childVomap.get("pk_currtype") == null ? ""
					: childVomap.get("pk_currtype").toString();
			String sql = "select  f.pk_currtype\n" + "from  bd_currtype   f\n"
					+ "where f.code='" + pk_currtype + "'\n" + "and  f.dr=0";
			String pk_currtype2 = getStrFsql(sql);
			bankSub.setPk_currtype(pk_currtype2);
			bankSub.setDr(0);
			// 设置关联的供应商PK
			bk.setMemo(order.getPk_supplier());
			bk.setBankaccsub(new BankAccSubVO[] { bankSub });

			list.add(bk);

		}
		return list;

	}

	public SupplierVO[] editBill(JSONObject jsonObject, String pk_supplier)
			throws BusinessException {
		SupplierVO[] apprveddBills = null;
		SupplierVO[] outbills = null;
		try {
			SupplierVO[] prayVO = getPrayVO(pk_supplier);
			// 传过来的数据生成供应商新增
			outbills = getInvCountBillVO(jsonObject, prayVO[0]);
			// fillup(outbills);  
			// 保存
			apprveddBills = savePurchaseInVOUpdate(outbills);
			
			// 签字
			// apprveddBills = savePurchaseInVOCommit(outbills);
			// 2.生成供应商银行账户
			List<BankAccbasVO> bankAccbas = getBillDetailVO2(jsonObject,
					apprveddBills[0]);
			BankaccbasForBpmAdd bakAccBP = new BankaccbasForBpmAdd();
			for (BankAccbasVO bankAccVO : bankAccbas) {
				bakAccBP.processBill(bankAccVO,apprveddBills[0]);
			}
		} catch (Exception e) {
 			ExceptionUtils.wrappBusinessException("修改供应商出错：" + e.getMessage());
		}
		return apprveddBills;
	}

	// 查询供应商
	private SupplierVO[] getPrayVO(String pre_pk_detail)
			throws BusinessException {
		VOQuery<SupplierVO> vo = new VOQuery<>(SupplierVO.class);
		SupplierVO[] query = vo.query("and  taxpayerid  = '" + pre_pk_detail
				+ "'", null);

		return query;
	}

	// 查询供应商联系人
	private List<Map<String, String>> selBankLinkVO(String pk_supplier)
			throws BusinessException {
		String linkMansql = "select pk_linkman, pk_suplinkman  from bd_suplinkman where pk_supplier = '"
				+ pk_supplier + "'";
		List<Map<String, String>> linkMan = (List<Map<String, String>>) getDao()
				.executeQuery(linkMansql, maplistProcessor);
		return linkMan;
	}

	// 查询联系人
	private LinkmanVO selLinkVO(String pk_linkman) throws BusinessException {
		LinkmanVO vo = new LinkmanVO();
		// String linkMansql
		// ="select  pk_linkman   from bd_linkman where pk_linkman = '"+pk_linkman+"'";
		// String linkManVo = (String)getDao().executeQuery(linkMansql,
		// maplistProcessor);
		vo.setPk_linkman(pk_linkman);
		return vo;
	}
	// 查询银行账户
	private List<Map<String, String>> selCustbankVO(String pk_supplier)
			throws BusinessException {
		String linkMansql = "select   pk_bankaccbas  from bd_custbank  where   pk_cust  = '"+pk_supplier+"'";
		List<Map<String, String>> Custbank = (List<Map<String, String>>) getDao()
				.executeQuery(linkMansql, maplistProcessor);
		return Custbank;
	}
	
	// 查询所有银行账户
	private Map<String,BankAccbasVO>  getBankAll()
			throws BusinessException {
		List<BankAccbasVO> bamk =  (List<BankAccbasVO>)getDao().retrieveByClause(BankAccbasVO.class,"dr = 0");
		Map<String,BankAccbasVO> bankMap = new HashMap<>();
		for(BankAccbasVO item : bamk){
			bankMap.put(item.getAccnum(), item);
		}		
		return bankMap;
	}


	private SupplierVO[] fillup(SupplierVO[] prayVO) {

		String[] formulas_H46 = {
		// 地区分类
		// "pk_areacl->getColValue(bd_areacl,pk_areacl,code,pk_areacl)",
		// 国家地区
		// "pk_country->getColValue(bd_countryzone,pk_country,code,pk_country)",
		// 币种
		// "pk_currtype->getColValue(bd_currtype,pk_currtype,code,pk_currtype)",
		// 供应商基本分类
		// "pk_supplierclass->getColValue(bd_supplierclass,pk_supplierclass,code,pk_supplierclass)",

		};

		SuperVOUtil.execFormulaWithVOs(prayVO, formulas_H46, null);
		return prayVO;
	}
}
