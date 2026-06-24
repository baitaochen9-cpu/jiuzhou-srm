package nc.bs.qc.supplierqualityapply.ace.bp.rule;

import java.util.ArrayList;
import java.util.List;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.impl.pubapp.pattern.data.vo.VOUpdate;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.vo.am.common.util.ArrayUtils;
import nc.vo.pu.supqualistatus.SupplierqualityHVO;
import nc.vo.pub.VOStatus;
import nc.vo.pub.pf.BillStatusEnum;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.qc.supplierqualityapply.AggSupplierQualityApplyVO;

/**
 * 更新生产厂商物料质量状态
 * 
 * @author zhw
 * 
 */
public class UpdateAceSupplierqualitystatusRule {
	public void process(AggSupplierQualityApplyVO[] vos) {
		if (ArrayUtils.isEmpty(vos))
			return;
		List<String> orglist = new ArrayList<String>();
		List<String> superlist = new ArrayList<String>();
		List<String> vendorlist = new ArrayList<String>();
		List<String> materiallist = new ArrayList<String>();
		for (AggSupplierQualityApplyVO clientBill : vos) {
			// 审批通过后的状态
			if (BillStatusEnum.APPROVED.value() == clientBill.getParentVO()
					.getApprovestatus()) {

				if (!orglist.contains(clientBill.getParentVO().getPk_org())) {
					orglist.add(clientBill.getParentVO().getPk_org());
				}

				if (!superlist.contains(clientBill.getParentVO()
						.getPk_supplier())) {
					superlist.add(clientBill.getParentVO().getPk_supplier());
				}

				if (!vendorlist.contains(clientBill.getParentVO()
						.getPk_vendor())) {
					vendorlist.add(clientBill.getParentVO().getPk_vendor());
				}

				if (!materiallist.contains(clientBill.getParentVO()
						.getPk_material())) {
					materiallist.add(clientBill.getParentVO().getPk_material());
				}

			}
		}

		if (vendorlist != null && vendorlist.size() > 0) {

			try {
				List<SupplierqualityHVO> sslist = getSupplierqualityHVO(
						orglist.toArray(new String[orglist.size()]),
						materiallist.toArray(new String[materiallist.size()]),
						superlist.toArray(new String[superlist.size()]),
						vendorlist.toArray(new String[vendorlist.size()]));

				if (sslist != null && sslist.size() > 0) {
					// 查询出生产厂商物料状态 信息 更新
					VOUpdate<SupplierqualityHVO> update = new VOUpdate<SupplierqualityHVO>();
					for (SupplierqualityHVO clientBill : sslist) {
						clientBill.setPk_grade_info(vos[0].getParentVO()
								.getPk_grade_info());
						clientBill.setStatus(VOStatus.UPDATED);
					}
					update.update(sslist.toArray(new SupplierqualityHVO[sslist
							.size()]), new String[] { "pk_grade_info" });
				}

			} catch (DAOException e) {
				ExceptionUtils.wrappException(e);
			}
		}

	}

	private List<SupplierqualityHVO> getSupplierqualityHVO(String[] pk_orgs,
			String[] pk_materials, String[] pk_suppliers, String[] pk_vendors)
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

		if (pk_suppliers != null && pk_suppliers.length > 0) {
			String where = append("r.pk_supplier", pk_suppliers);
			strb.append(" and " + where);
		}

		if (pk_vendors != null && pk_vendors.length > 0) {
			String where = append("r.pk_vendor", pk_vendors);
			strb.append(" and " + where);
		}

		List<SupplierqualityHVO> list = (List<SupplierqualityHVO>) new BaseDAO()
				.executeQuery(strb.toString(), new BeanListProcessor(
						SupplierqualityHVO.class));
		return list;

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
}