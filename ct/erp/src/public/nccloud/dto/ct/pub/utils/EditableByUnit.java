package nccloud.dto.ct.pub.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.itf.scmpub.reference.uap.bd.material.MaterialPubService;
import nc.vo.bd.material.MaterialPubQueryVO;
import nc.vo.ct.entity.CtAbstractBVO;
import nc.vo.ct.entity.CtAbstractVO;
import nc.vo.ct.uitl.ValueUtil;
import nc.vo.scmpub.res.billtype.CTBillType;
import nc.vo.scmpub.util.ArrayUtil;

/**
 * @description
 * @author xiahui
 * @date 创建时间：2019-1-22 上午10:12:05
 * @version ncc1.0
 * @ref nc.ui.ct.rule.EditableByUnit
 **/
public class EditableByUnit {
	private String billtype;

	private Map<String, MaterialPubQueryVO> chgrateMap = null;

	private Map<String, String> map = null;
	
	private Map<String, String> materialMap = null;

	private String NCHANGERAtE = "1.0000/1.0000";

	private ExtBillUtil util;

	public EditableByUnit(ExtBillUtil util, String billtype) {
		this.util = util;
		this.billtype = billtype;
	}

	public void setEditable(int[] rows) {
		Object pk_org = this.util.getHeadValue(CtAbstractVO.PK_ORG);
		Object pk_org_v = this.util.getHeadValue(CtAbstractVO.PK_ORG_V);
		Object pk_group = this.util.getHeadValue(CtAbstractVO.PK_GROUP);

		String[] materials = this.getBodyMaterial(rows);
		if (ValueUtil.equals(this.billtype, CTBillType.PurDaily.getCode())
				|| ValueUtil.equals(this.billtype, CTBillType.OtherPur.getCode())) {
			this.map = MaterialPubService.queryPuMeasdocIDByPks(materials);
			this.materialMap = MaterialPubService.queryMaterialOidByVid(materials); // 多选物料时，需要设置oid
		} else if (ValueUtil.equals(this.billtype, CTBillType.SaleDaily.getCode())
				|| ValueUtil.equals(this.billtype, CTBillType.OtherSale.getCode())) {
			this.map = MaterialPubService.querySaleMeasdocIDByPks(materials);
		}

		this.chgrateMap = this.getMarChgrateMap(rows);

		for (int row : rows) {
			this.setEditable(row, pk_org, pk_org_v, pk_group);
		}
		int[] retrows = RelationCalculateFilter.filterNoNeedRelationRows(util, rows);
		if (!ArrayUtil.isEmpty(retrows)) {
			// 以换算率联动计算
			new RelationCalculate().calculate(util, rows, CtAbstractBVO.VCHANGERATE);
		}
	}

	/**
	 * 方法功能描述：根据行号 取表体物料
	 * <p>
	 * <b>参数说明</b>
	 * 
	 * @param rows
	 * @return
	 *         <p>
	 * @since 6.0
	 * @author lizhengb
	 * @time 2010-7-8 下午02:15:06
	 */
	private String[] getBodyMaterial(int[] rows) {
		List<String> list = new ArrayList<String>();
		for (int row : rows) {
			String pk_material = this.util.getBodyStringValue(row, CtAbstractBVO.PK_MATERIAL);
			list.add(pk_material);
		}
		String[] materials = new String[list.size()];
		list.toArray(materials);
		return materials;
	}

	private Map<String, MaterialPubQueryVO> getMarChgrateMap(int[] rows) {
		List<MaterialPubQueryVO> voList = new ArrayList<MaterialPubQueryVO>();
		// 将参数转换成uap接口需要的参数list
		for (int row : rows) {
			MaterialPubQueryVO queryVO = new MaterialPubQueryVO();
			this.util.setBodyValue(row, CtAbstractBVO.PK_SRCMATERIAL, this.materialMap.get(this.util.getBodyStringValue(row, CtAbstractBVO.PK_MATERIAL)));
			queryVO.setOid(this.util.getBodyStringValue(row, CtAbstractBVO.PK_SRCMATERIAL));
			queryVO.setO_src_measdocid(this.map.get(this.util.getBodyStringValue(row, CtAbstractBVO.PK_MATERIAL)));
			queryVO.setO_des_measdocid(this.util.getBodyStringValue(row, CtAbstractBVO.CUNITID));
			voList.add(queryVO);
		}
		List<MaterialPubQueryVO> uapResult = MaterialPubService.queryMeasrateByOidAndmeasdocs(voList);
		Map<String, MaterialPubQueryVO> uapResultMap = new HashMap<String, MaterialPubQueryVO>();
		if (null == uapResult || uapResult.size() == 0) {
			return uapResultMap;
		}
		for (MaterialPubQueryVO vo : uapResult) {
			uapResultMap.put(vo.getOid() + vo.getO_src_measdocid() + vo.getO_des_measdocid(), vo);
		}
		return uapResultMap;

	}

	/**
	 * 方法功能描述：设置表体默认单位（采购合同，其他付合同：默认采购单位） （销售合同，其他收合同：默认销售单位）
	 * <p>
	 * <b>参数说明</b>
	 * 
	 * @param row
	 *            <p>
	 * @since 6.0
	 * @author lizhengb
	 * @param pk_org_v
	 * @param pk_org
	 * @param pk_group
	 * @time 2010-7-8 下午02:15:32
	 */
	private void setEditable(int row, Object pk_org, Object pk_org_v, Object pk_group) {

		this.util.setBodyValue(row, CtAbstractBVO.PK_ORG, pk_org);
		this.util.setBodyValue(row, CtAbstractBVO.PK_ORG_V, pk_org_v);
		this.util.setBodyValue(row, CtAbstractBVO.PK_GROUP, pk_group);
		// 财务组织
		// util.setBodyValue(row, CtAbstractBVO.PK_FINANCEORG, pk_org);
		// util.setBodyValue(row, CtAbstractBVO.PK_FINANCEORG_V, pk_org_v);
		String pk_material = util.getBodyStringValue(row, CtAbstractBVO.PK_MATERIAL);
		String castunitid = this.map.get(pk_material);
		// 设置税率
		// if (ValueUtil.isEmpty(ntaxrate)) {
		// util.setBodyValue(row, CtAbstractBVO.NTAXRATE, this.NTAXRATE);
		// }
		if (!ValueUtil.isEmpty(castunitid)) {
			this.util.setBodyValue(row, CtAbstractBVO.CASTUNITID, castunitid);
			this.util.setBodyValue(row, CtAbstractBVO.CQTUNITID, castunitid);
			MaterialPubQueryVO vo = this.chgrateMap.get(this.util.getBodyStringValue(row, CtAbstractBVO.PK_SRCMATERIAL)
					+ util.getBodyStringValue(row, CtAbstractBVO.CASTUNITID)
					+ util.getBodyStringValue(row, CtAbstractBVO.CUNITID));
			this.NCHANGERAtE = vo.getO_src_measrate();
			this.util.setBodyValue(row, CtAbstractBVO.VCHANGERATE, this.NCHANGERAtE);
			this.util.setBodyValue(row, CtAbstractBVO.VQTUNITRATE, this.NCHANGERAtE);
		} else {
			this.NCHANGERAtE = "1.0000/1.0000";
			this.util.setBodyValue(row, CtAbstractBVO.VCHANGERATE, this.NCHANGERAtE);
			this.util.setBodyValue(row, CtAbstractBVO.VQTUNITRATE, this.NCHANGERAtE);
		}

	}
}
