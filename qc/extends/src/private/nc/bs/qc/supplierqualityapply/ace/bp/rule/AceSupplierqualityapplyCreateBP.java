package nc.bs.qc.supplierqualityapply.ace.bp.rule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.ic.pub.env.ICBSContext;
import nc.impl.pubapp.pattern.data.bill.BillQuery;
import nc.itf.scmpub.reference.uap.pf.PfServiceScmUtil;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.vo.bd.defdoc.DefdocVO;
import nc.vo.bd.supplier.suppliergradesys.SupplierGradeVO;
import nc.vo.pu.supqualistatus.SupplierqualityHVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.pf.BillStatusEnum;
import nc.vo.qc.supplierqualityapply.AggSupplierQualityApplyVO;
import nc.vo.qc.supplierqualityapply.SupplierQualityApplyVO;
import nc.vo.scmpub.util.VOEntityUtil;
import nc.vo.trade.pub.IBillStatus;

/**
 * 定时任务生成生产厂商物料质量状态申请单BP
 */
public class AceSupplierqualityapplyCreateBP {

	ICBSContext context = new ICBSContext();

	public AggSupplierQualityApplyVO[] createApply(String[] pk_orgs,
			int yearnum, int unit, String[] pk_materials,
			String after_qualitylevel) throws BusinessException {

		// 查询订单的信息
		List<SupplierqualityHVO> list = getSupplierqualityHVOByOrder(pk_orgs,
				pk_materials);

		if (list == null || list.size() == 0)
			return null;

		// 查找生成审批单的数据
		List<SupplierqualityHVO> list1 = getAggSupplierQualityApplyVO(list,
				pk_orgs, yearnum, unit, pk_materials, after_qualitylevel);

		if (list1 == null || list1.size() == 0)
			return null;

		List<AggSupplierQualityApplyVO> hlist = new ArrayList<AggSupplierQualityApplyVO>();

		Map<String, SupplierGradeVO> gmap = getSupplierGradeVO();
		for (SupplierqualityHVO clientBill : list1) {
			AggSupplierQualityApplyVO aggvo = changeAggSupplierQualityApplyVO(
					clientBill, after_qualitylevel, gmap);
			hlist.add(aggvo);
		}
		savechangeAggSupplierQualityApplyVO(hlist);
		return hlist.toArray(new AggSupplierQualityApplyVO[hlist.size()]);
	}

	private List<SupplierqualityHVO> getAggSupplierQualityApplyVO(
			List<SupplierqualityHVO> list, String[] pk_orgs, int yearnum,
			int unit, String[] pk_materials, String after_qualitylevel)
			throws DAOException {
		if (list == null || list.size() == 0)
			return null;

		List<String> newlist = new ArrayList<String>();
		// 3年前
		UFDate beforedate = PMCommonUtils.getDateByBefore(yearnum, unit,
				new UFDate()).asEnd();

		for (SupplierqualityHVO hvo : list) {
			String key = hvo.getPk_supplier() + "&" + hvo.getPk_material()
					+ "&" + hvo.getPk_vendor() + "&" + hvo.getPk_org();
			UFDate date = hvo.getBilldate();
			if (date.afterDate(beforedate)) {// 最近3年的数据
				if (!newlist.contains(key)) {
					newlist.add(key);
				}
			}
		}

		// 查询已经更新为过期的物料
		List<SupplierqualityHVO> list1 = getSupplierqualityHVO(pk_orgs,
				pk_materials, after_qualitylevel);
		List<String> explist = new ArrayList<String>();// 已经过期的数据
		for (SupplierqualityHVO hvo : list1) {
			String key = hvo.getPk_supplier() + "&" + hvo.getPk_material()
					+ "&" + hvo.getPk_vendor() + "&" + hvo.getPk_org();

			if (!explist.contains(key)) {
				explist.add(key);
			}
		}

		List<SupplierQualityApplyVO> list2 = getSupplierQualityApplyVO(pk_orgs,
				pk_materials, after_qualitylevel);
		List<String> checkinglist = new ArrayList<String>();// 未审批通过的数据
		for (SupplierQualityApplyVO hvo : list2) {
			String key = hvo.getPk_supplier() + "&" + hvo.getPk_material()
					+ "&" + hvo.getPk_vendor() + "&" + hvo.getPk_org();

			if (!checkinglist.contains(key)) {
				checkinglist.add(key);
			}
		}

		// 3年前未过期的的数据
		List<SupplierqualityHVO> hlist = new ArrayList<SupplierqualityHVO>();

		// 且没有审批中的数据
		for (SupplierqualityHVO hvo : list) {
			String key = hvo.getPk_supplier() + "&" + hvo.getPk_material()
					+ "&" + hvo.getPk_vendor() + "&" + hvo.getPk_org();

			UFDate date = hvo.getBilldate();

			if (date.beforeDate(beforedate)) {// 3年前的数据
				if (!newlist.contains(key) && !explist.contains(key)
						&& !checkinglist.contains(key)) {// 最近3年的数据没有
					// 并且没有更新为过期
					hlist.add(hvo);
				}
			}
		}

		List<SupplierqualityHVO> hlist1 = new ArrayList<SupplierqualityHVO>(); //
		List<String> filterlist = new ArrayList<String>();//
		for (SupplierqualityHVO hvo : hlist) {
			String key = hvo.getPk_supplier() + "&" + hvo.getPk_material()
					+ "&" + hvo.getPk_vendor() + "&" + hvo.getPk_org();
			if (!filterlist.contains(key)) {
				filterlist.add(key);
				hlist1.add(hvo);
			}
		}
		return hlist1;
	}

	private List<SupplierqualityHVO> getSupplierqualityHVOByOrder(
			String[] pk_orgs, String[] pk_materials) throws DAOException {

		StringBuffer strb = new StringBuffer();
		strb.append(" select r.pk_org,r.dbilldate billdate, r.pk_supplier, b.pk_material,ab.cproductorid pk_vendor ");
		strb.append("  from po_order r join po_order_b b on r.pk_order = b.pk_order ");
		strb.append("  join po_arriveorder_b ab on b.pk_order_b = ab.csourcebid ");
		strb.append(" where  isnull(r.dr,0)=0 and isnull(b.dr,0)= 0 and isnull(ab.dr,0)= 0  and isnull(ab.cproductorid,'~') <> '~' ");

		if (pk_orgs != null && pk_orgs.length > 0) {
			String where = append("r.pk_org", pk_orgs);
			strb.append(" and " + where);
		}

		if (pk_materials != null && pk_materials.length > 0) {
			String where = append("b.pk_material", pk_materials);
			strb.append(" and " + where);
		}
		strb.append(" group by r.pk_org,r.dbilldate, r.pk_supplier, b.pk_material, ab.cproductorid  order by r.dbilldate ");

		List<SupplierqualityHVO> list = (List<SupplierqualityHVO>) new BaseDAO()
				.executeQuery(strb.toString(), new BeanListProcessor(
						SupplierqualityHVO.class));

		if (list == null || list.size() == 0)
			return null;

		// try {
		// List<SupplierqualityHVO> glist = new
		// ArrayList<SupplierqualityHVO>();// 补充生产厂商为主键
		// Map<String, DefdocVO> map = getDefDoc();
		//
		// for (SupplierqualityHVO hvo : list) {
		// String vname = hvo.getPk_vendor();
		// if (!StringUtil.isEmpty(vname)) {
		// String[] names = vname.split(",");
		// for (String name : names) {
		// SupplierqualityHVO clone = (SupplierqualityHVO) ObjectUtils
		// .serializableClone(hvo);
		// DefdocVO defvo = map.get(name);
		// if (defvo != null) {
		// clone.setPk_vendor(defvo.getPk_defdoc());
		// glist.add(clone);
		// } else {
		// glist.add(clone);
		// }
		// }
		// }
		// }
		// return glist;
		// } catch (Exception e) {
		// ExceptionUtils.wrappException(e);
		// }
		return list;

	}

	private Map<String, DefdocVO> getDefDoc() throws DAOException {

		StringBuffer strb = new StringBuffer();
		strb.append(" select  r.*  ");
		strb.append("  from bd_defdoc r join bd_defdoclist b on r.pk_defdoclist = b.pk_defdoclist ");
		strb.append(" where  isnull(r.dr,0)=0 and isnull(b.dr,0)= 0  and  b.code = 'BD006_0xx' ");

		List<DefdocVO> list = (List<DefdocVO>) new BaseDAO().executeQuery(
				strb.toString(), new BeanListProcessor(DefdocVO.class));

		Map<String, DefdocVO> map = new HashMap<String, DefdocVO>();

		if (list == null || list.size() == 0)
			return map;

		for (DefdocVO hvo : list) {

			if (!map.containsKey(hvo.getName())) {
				map.put(hvo.getName(), hvo);
			}
		}
		return map;
	}

	private Map<String, SupplierGradeVO> getSupplierGradeVO()
			throws DAOException {

		StringBuffer strb = new StringBuffer();
		strb.append(" select * from bd_supplier_grade where isnull(dr,0)= 0 order by isdefault");

		List<SupplierGradeVO> list = (List<SupplierGradeVO>) new BaseDAO()
				.executeQuery(strb.toString(), new BeanListProcessor(
						SupplierGradeVO.class));

		Map<String, SupplierGradeVO> map = new HashMap<String, SupplierGradeVO>();

		if (list == null || list.size() == 0)
			return map;

		for (SupplierGradeVO hvo : list) {

			if (!map.containsKey(hvo.getPk_grade_info())) {
				map.put(hvo.getPk_grade_info(), hvo);
			}
		}
		return map;
	}

	private List<SupplierqualityHVO> getSupplierqualityHVO(String[] pk_orgs,
			String[] pk_materials, String after_qualitylevel)
			throws DAOException {

		StringBuffer strb = new StringBuffer();
		strb.append(" select  * from  qc_supplierquality r ");
		strb.append(" where  isnull(r.dr,0)=0  ");

		if (pk_orgs != null && pk_orgs.length > 0) {
			String where = append("r.pk_org", pk_orgs);
			strb.append(" and " + where);
		}

		if (pk_materials != null && pk_materials.length > 0) {
			String where = append("r.pk_material", pk_materials);
			strb.append(" and " + where);
		}

		strb.append(" and r.pk_grade_info ='" + after_qualitylevel + "'");

		// 按照年数过滤掉物料
		List<SupplierqualityHVO> list = (List<SupplierqualityHVO>) new BaseDAO()
				.executeQuery(strb.toString(), new BeanListProcessor(
						SupplierqualityHVO.class));
		return list;

	}

	private List<SupplierQualityApplyVO> getSupplierQualityApplyVO(
			String[] pk_orgs, String[] pk_materials, String after_qualitylevel)
			throws DAOException {

		StringBuffer strb = new StringBuffer();
		strb.append(" select  * from  qc_supplierqualityapply r ");
		strb.append(" where  isnull(r.dr,0)=0  ");

		if (pk_orgs != null && pk_orgs.length > 0) {
			String where = append("r.pk_org", pk_orgs);
			strb.append(" and " + where);
		}

		if (pk_materials != null && pk_materials.length > 0) {
			String where = append("r.pk_material", pk_materials);
			strb.append(" and " + where);
		}

		strb.append(" and r.pk_grade_info ='" + after_qualitylevel + "'");
		strb.append(" and r.approvestatus <> '" + IBillStatus.CHECKPASS + "'");

		// 按照年数过滤掉物料
		List<SupplierQualityApplyVO> list = (List<SupplierQualityApplyVO>) new BaseDAO()
				.executeQuery(strb.toString(), new BeanListProcessor(
						SupplierQualityApplyVO.class));
		return list;

	}

	private String internalParse(String str) {
		if (str == null) {
			throw new IllegalArgumentException("invalid date: " + str);
		}
		str = str.trim();
		int spaceIndex = str.indexOf(' ');
		if (spaceIndex > -1) {
			str = str.substring(0, spaceIndex);
		}

		String[] tokens = new String[3];
		StringTokenizer st = new StringTokenizer(str, "-/");
		if (st.countTokens() != 3) {
			throw new IllegalArgumentException("invalid date: " + str);
		}

		int i = 0;
		while (st.hasMoreTokens()) {
			tokens[(i++)] = st.nextToken();
		}
		try {
			int year = Integer.parseInt(tokens[0]);
			int month = Integer.parseInt(tokens[1]);
			if ((month < 1) || (month > 12))
				throw new IllegalArgumentException("invalid date: " + str);
			int day = Integer.parseInt(tokens[2]);

			int daymax = isLeapYear(year) ? UFDate.LEAP_MONTH_LENGTH[(month - 1)]
					: UFDate.MONTH_LENGTH[(month - 1)];

			if ((day < 1)) {
				return tokens[0] + "-" + tokens[1] + "-01";
			} else if ((day > daymax)) {
				return tokens[0] + "-" + tokens[1] + "-" + daymax;
			}
		} catch (Throwable thr) {
			if ((thr instanceof IllegalArgumentException)) {
				throw ((IllegalArgumentException) thr);
			}
			throw new IllegalArgumentException("invalid date: " + str);
		}
		return str;
	}

	private boolean isLeapYear(int year) {
		if ((year % 4 == 0) && ((year % 100 != 0) || (year % 400 == 0))) {
			return true;
		}
		return false;
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

	private AggSupplierQualityApplyVO changeAggSupplierQualityApplyVO(
			SupplierqualityHVO phvo, String after_qualitylevel,
			Map<String, SupplierGradeVO> gmap) {
		AggSupplierQualityApplyVO aggvo = new AggSupplierQualityApplyVO();
		SupplierQualityApplyVO hvo = new SupplierQualityApplyVO();
		// 设置主组织默认值
		hvo.setAttributeValue("pk_group", "0001V110000000000FH0");
		hvo.setAttributeValue("pk_org", phvo.getPk_org());
		hvo.setAttributeValue("pkorg", phvo.getPk_org());
		// 设置单据状态、单据业务日期默认值
		hvo.setAttributeValue("approvestatus", BillStatusEnum.FREE.value());
		hvo.setAttributeValue("billdate",
				new UFDate(System.currentTimeMillis()));
		hvo.setAttributeValue("billtype", "C060");
		hvo.setAttributeValue("transtype", "C060");
		hvo.setAttributeValue("transtypepk", PfServiceScmUtil
				.getTrantypeidByCode(new String[] { "C060" }).get("C060"));

		hvo.setAttributeValue("creationtime",
				new UFDateTime(System.currentTimeMillis()));
		hvo.setAttributeValue("creator", context.getUserID());
		hvo.setAttributeValue("maketime",
				new UFDateTime(System.currentTimeMillis()));
		hvo.setAttributeValue("billmaker", context.getUserID());

		SupplierGradeVO vo = gmap.get(after_qualitylevel);

		if (vo != null) {
			hvo.setPk_suppliergrade(vo.getPk_suppliergrade());
		}
		hvo.setPk_grade_info(after_qualitylevel);
		hvo.setPk_material(phvo.getPk_material());
		hvo.setPk_supplier(phvo.getPk_supplier());
		hvo.setPk_vendor(phvo.getPk_vendor());
		aggvo.setParentVO(hvo);
		return aggvo;
	}

	private void savechangeAggSupplierQualityApplyVO(
			List<AggSupplierQualityApplyVO> list) {
		AggSupplierQualityApplyVO[] billvos = list
				.toArray(new AggSupplierQualityApplyVO[list.size()]);
		billvos = (AggSupplierQualityApplyVO[]) PfServiceScmUtil.processBatch(
				"SAVEBASE", "C060", billvos, null, null);
		BillQuery<AggSupplierQualityApplyVO> billQry = new BillQuery<AggSupplierQualityApplyVO>(
				AggSupplierQualityApplyVO.class);

		AggSupplierQualityApplyVO[] bills = billQry.query(VOEntityUtil
				.getPksFromAggVO(billvos));
		PfServiceScmUtil.processBatch("SAVE", "C060", bills, null, null);
	}

}
