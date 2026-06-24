package nccloud.dto.ct.saledaily.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.itf.scmpub.reference.uap.bd.material.MaterialPubService;
import nc.vo.bd.material.MaterialPubQueryVO;
import nc.vo.ct.entity.CtAbstractBVO;
import nc.vo.ct.rule.SaleRelationCalculate;
import nc.vo.ct.saledaily.entity.AggCtSaleVO;
import nc.vo.ct.saledaily.entity.CtSaleBVO;
import nc.vo.ct.saledaily.entity.CtSaleVO;
import nc.vo.ct.uitl.ValueUtil;
import nc.vo.scmpub.res.billtype.CTBillType;

/**
 * @description 单位编辑处理
 * @author wangshrc
 * @date 2019年2月16日 下午2:36:50
 * @version ncc1.0
 */
public class UnitEditUtils {

	private String billtype;

	private Map<String, MaterialPubQueryVO> chgrateMap = null;

	private Map<String, String> map = null;

	private String NCHANGERAtE = "1.0000/1.0000";

	AggCtSaleVO billvo;

	public UnitEditUtils(AggCtSaleVO billvo, String billtype) {
		this.billvo = billvo;
		this.billtype = billtype;
	}

	public void setEditable(int[] rows) {
		CtSaleVO hvo = billvo.getParentVO();
		String pk_org = hvo.getPk_org();
		String pk_org_v = hvo.getPk_org_v();
		String pk_group = hvo.getPk_group();

		String[] materials = this.getBodyMaterial(rows);
		if (ValueUtil.equals(this.billtype, CTBillType.PurDaily.getCode())
				|| ValueUtil.equals(this.billtype,
						CTBillType.OtherPur.getCode())) {
			this.map = MaterialPubService.queryPuMeasdocIDByPks(materials);
		} else if (ValueUtil.equals(this.billtype,
				CTBillType.SaleDaily.getCode())
				|| ValueUtil.equals(this.billtype,
						CTBillType.OtherSale.getCode())) {
			this.map = MaterialPubService.querySaleMeasdocIDByPks(materials);
		}

		this.chgrateMap = this.getMarChgrateMap(rows);

		for (int row : rows) {
			this.setEditable(row, pk_org, pk_org_v, pk_group);
		}
		// int[] retrows =
		// RelationCalculateFilter.filterNoNeedRelationRows(this.panel, rows);
		// if (!ArrayUtils.isEmpty(retrows)) {
		// 以换算率联动计算
		new SaleRelationCalculate()
				.calculate(billvo, CtAbstractBVO.VCHANGERATE);
		// }
	}

	/**
	 * 方法功能描述：根据行号 取表体物料
	 * <p>
	 * <b>参数说明</b>
	 * 
	 * @param rows
	 * @return <p>
	 * @since 6.0
	 * @author lizhengb
	 * @time 2010-7-8 下午02:15:06
	 */
	private String[] getBodyMaterial(int[] rows) {
		CtSaleBVO[] bvos = billvo.getCtSaleBVO();
		List<String> list = new ArrayList<String>();
		for (int row : rows) {
			String pk_material = bvos[row].getPk_material();
			list.add(pk_material);
		}
		String[] materials = new String[list.size()];
		list.toArray(materials);
		return materials;
	}

	private Map<String, MaterialPubQueryVO> getMarChgrateMap(int[] rows) {
		List<MaterialPubQueryVO> voList = new ArrayList<MaterialPubQueryVO>();
		CtSaleBVO[] bvos = billvo.getCtSaleBVO();
		// 将参数转换成uap接口需要的参数list
		for (int row : rows) {
			MaterialPubQueryVO queryVO = new MaterialPubQueryVO();
			queryVO.setOid(bvos[row].getPk_srcmaterial());
			queryVO.setO_src_measdocid(this.map.get(bvos[row].getPk_material()));
			queryVO.setO_des_measdocid(bvos[row].getCunitid());
			voList.add(queryVO);
		}
		List<MaterialPubQueryVO> uapResult = MaterialPubService
				.queryMeasrateByOidAndmeasdocs(voList);
		Map<String, MaterialPubQueryVO> uapResultMap = new HashMap<String, MaterialPubQueryVO>();
		if (null == uapResult || uapResult.size() == 0) {
			return uapResultMap;
		}
		for (MaterialPubQueryVO vo : uapResult) {
			uapResultMap.put(
					vo.getOid() + vo.getO_src_measdocid()
							+ vo.getO_des_measdocid(), vo);
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
	private void setEditable(int row, String pk_org, String pk_org_v,
			String pk_group) {
		CtSaleBVO[] bvos = billvo.getCtSaleBVO();
		bvos[row].setPk_org(pk_org);
		bvos[row].setPk_org_v(pk_org_v);
		bvos[row].setPk_group(pk_group);
		// 财务组织
		// util.setBodyValue(row, CtAbstractBVO.PK_FINANCEORG, pk_org);
		// util.setBodyValue(row, CtAbstractBVO.PK_FINANCEORG_V, pk_org_v);
		String pk_material = bvos[row].getPk_material();
		String castunitid = this.map.get(pk_material);
		// 设置税率
		// if (ValueUtil.isEmpty(ntaxrate)) {
		// util.setBodyValue(row, CtAbstractBVO.NTAXRATE, this.NTAXRATE);
		// }
		if (!ValueUtil.isEmpty(castunitid)) {
			bvos[row].setCastunitid(castunitid);
			bvos[row].setCqtunitid(castunitid);
			MaterialPubQueryVO vo = this.chgrateMap.get(bvos[row]
					.getPk_srcmaterial()
					+ bvos[row].getCastunitid()
					+ bvos[row].getCunitid());
			this.NCHANGERAtE = vo.getO_src_measrate();
			bvos[row].setVchangerate(this.NCHANGERAtE);
			bvos[row].setVqtunitrate(this.NCHANGERAtE);
		} else {
			this.NCHANGERAtE = "1.0000/1.0000";
			bvos[row].setVchangerate(this.NCHANGERAtE);
			bvos[row].setVqtunitrate(this.NCHANGERAtE);
		}

	}

}
