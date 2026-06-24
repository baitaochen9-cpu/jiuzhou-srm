/**
 * $文件说明$
 * 
 * @author GGR
 * @version 6.0
 * @see
 * @since 6.0
 * @time 2010-7-3 下午05:45:45
 */
package nc.bs.pu.position.maintain.rule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import nc.bs.ml.NCLangResOnserver;
import nc.impl.pubapp.pattern.database.DataAccessUtils;
import nc.impl.pubapp.pattern.database.IDExQueryBuilder;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.itf.scmpub.reference.uap.bd.material.MaterialPubService;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pu.position.entity.PositionHeaderVO;
import nc.vo.pu.position.entity.PositionItemVO;
import nc.vo.pu.position.entity.PositionVO;
import nc.vo.pu.position.enumeration.EnumUseMove;
import nc.vo.pu.pub.constant.PUParaValue.po85;
import nc.vo.pu.pub.constant.PUTempTable;
import nc.vo.pu.pub.util.PUSysParamUtil;
import nc.vo.pubapp.pattern.data.IRowSet;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.pubapp.pattern.pub.SqlBuilder;

/**
 * <p>
 * <b>本类主要完成以下功能：</b>
 * <ul>
 * <li>唯一性检查
 * </ul>
 * <p>
 * <p>
 * 
 * @version 6.0
 * @since 6.0
 * @author GGR
 * @time 2010-7-3 下午05:45:45
 */
public class CheckUniqueRule implements IRule<PositionVO> {

	@Override
	public void process(PositionVO[] vos) {
		for (PositionVO vo : vos) {
			int positiontype = vo.getHVO().getFpositiontype().intValue();

			String classkey = PositionItemVO.PK_MARBASCLASS;
			if (positiontype == PositionHeaderVO.purchasePosition) {
				if (po85.pu_marclass == PUSysParamUtil.getPO85(vo.getHVO()
						.getPk_group())) {
					classkey = PositionItemVO.PK_MARPUCLASS;
				}
			}
			// 唯一性检查
			this.checkUnique(vo, classkey);
		}
	}

	private String checkClassOther(PositionVO vo, String classkey,
			String classCode) {
		// 检查物料分类是否在其他物料分类应用
		SqlBuilder sql = new SqlBuilder();
		sql.append(" select distinct h.code,b." + classCode + ",v.class_code  ");
		sql.append(" from po_position_b b,po_position h ,");

		sql.append(" (select b." + classkey + " pk_marclass");
		sql.append(" ,b." + classCode + " class_code ");
		sql.append("  from po_position_b b ");
		sql.append("  where b.pk_position ='" + vo.getHVO().getPk_position() + "'");
		sql.append("  and b.fflag = 1 and b.dr = 0 and isnull(b.pk_srcmaterial,'~')='~' ) v ");

		sql.append(" where h.pk_position = b.pk_position and b.dr =0 and h.dr=0 ");
		sql.append(" and b." + classkey + " = v.pk_marclass ");
		sql.append(" and b.fflag = 1 and ");
		sql.append("h.pk_org", vo.getHVO().getPk_org());
		sql.append(" and ");
		sql.append(" h.code ", "!= ", vo.getHVO().getCode());
		sql.append(" and ");
		sql.append("h.fpositiontype", vo.getHVO().getFpositiontype());

		// String[][] result =
		// new DataAccessUtils().query(sql.toString()).toTwoDimensionStringArray();

		String[][] result = this.query(sql.toString(), vo, true);

		if (null == result || result.length == 0) {
			return "";
		}

		StringBuffer err = new StringBuffer();
		for (int i = 0, len = result.length; i < len; i++) {
			err.append(NCLangResOnserver.getInstance().getStrByID("4004080_0",
					"04004080-0012", null,
					new String[] { result[i][2], result[i][0], result[i][1] })/*
																																		 * 物料分类：{0}
																																		 * 与岗位编码
																																		 * ：{1}的
																																		 * 物料分类：{2}
																																		 * 设置重复。\n
																																		 */);
		}

		return err.toString();
	}

	/**
	 * 方法功能描述：检查物料对应的分类是否被其他岗位应用。
	 * <p>
	 * <b>参数说明</b>
	 * 
	 * @param pkclasss
	 * @param code
	 * @param pk_org
	 * @param map
	 * @return <p>
	 * @since 6.0
	 * @author GGR
	 * @time 2010-7-7 上午09:00:07
	 */
	private String checkMaterialA(String[] pkclasss, PositionVO vo,
			HashMap<String, PositionItemVO> map, String classkey) {

		// 查询出物料对应的分类是否被其他岗位应用
		String[][] result = this.getOtherUseClass(pkclasss, vo, classkey);
		if (null == result || result.length == 0) {
			return "";
		}

		String[] codes = new String[result.length];
		for (int i = 0, len = result.length; i < len; i++) {
			// code,pk_marclass,marbasclass_code
			codes[i] = result[i][0];
		}

		// 查询出物料对应的分类是被其他岗位应用的排除的物料
		HashMap<String, HashSet<String>> result2 = this.getMoveMaterial(codes, vo);

		StringBuffer err = new StringBuffer();

		for (int i = 0, len = result.length; i < len; i++) {
			String code = result[i][0];
			String pk_marclass = result[i][1];
			String marbasclass_code = result[i][2];
			// 判断被其他岗位应用的物料分类是否排除对应的物料
			if (null == result2.get(code)
					|| !result2.get(code).contains(
							map.get(pk_marclass).getPk_srcmaterial())) {
				for (PositionItemVO bvo : (PositionItemVO[]) vo.getChildrenVO()) {
					err.append(NCLangResOnserver.getInstance().getStrByID("4004080_0",
							"04004080-0013", null,
							new String[] { bvo.getMaterial_code(), code, marbasclass_code })/*
																																							 * 物料：
																																							 * {
																																							 * 0
																																							 * }
																																							 * 与岗位编码
																																							 * ：
																																							 * {
																																							 * 1
																																							 * }
																																							 * 的
																																							 * 物料分类
																																							 * ：
																																							 * {
																																							 * 2
																																							 * }
																																							 * 设置重复
																																							 * 。
																																							 * \
																																							 * n
																																							 */);
				}
			}
		}

		return err.toString();
	}

	/**
	 * 方法功能描述：检查物料是否被其他岗位应用。
	 * <p>
	 * <b>参数说明</b>
	 * 
	 * @param pks
	 * @param code
	 * @param pk_org
	 * @param map
	 * @return <p>
	 * @since 6.0
	 * @author GGR
	 * @time 2010-7-7 上午08:59:32
	 */
	private String checkMaterialB(String[] pks, PositionVO vo,
			HashMap<String, PositionItemVO> map) {
		SqlBuilder sql = new SqlBuilder();
		sql.append(" select distinct h.code,b.pk_srcmaterial ");
		sql.append(" from po_position h, po_position_b b ");
		sql.append(" where h.pk_position = b.pk_position and h.dr =0 and b.dr = 0");
		sql.append(" and ");
		sql.append("h.code " ,"!= ", vo.getHVO().getCode());
		sql.append(" and ");
		sql.append("h.fpositiontype", vo.getHVO().getFpositiontype());
		sql.append(" and ");
		sql.append("h.pk_org", vo.getHVO().getPk_org());
		sql.append(" and ");
		// 临时表
		IDExQueryBuilder builder = new IDExQueryBuilder(
				PUTempTable.tmp_po_pos_04.name());
		sql.append(builder.buildSQL("b.pk_srcmaterial", pks));
		sql.append(" and ");
		sql.append("b.fflag", 1);

		String[][] result =
		// new DataAccessUtils().query(sql.toString()).toTwoDimensionStringArray();
		this.query(sql.toString(), vo, true);
		if (null == result || result.length == 0) {
			return "";
		}

		StringBuffer err = new StringBuffer();

		for (String[] row : result) {
			err.append(NCLangResOnserver.getInstance().getStrByID("4004080_0",
					"04004080-0014", null,
					new String[] { map.get(row[1]).getMaterial_code(), row[0] })/*
																																			 * 物料：{0}
																																			 * 与岗位编码
																																			 * ：{1}
																																			 * 设置重复。\n
																																			 */);
		}
		return err.toString();
	}

	private String checkPk_class(PositionVO vo, String classkey) {
		String classCode;
		if (PositionItemVO.PK_MARBASCLASS.equals(classkey)) {
			classCode = PositionItemVO.MARBASCLASS_CODE;
		} else {
			classCode = PositionItemVO.MARPUCLASS_CODE;
		}

		/*20230105  yezhian 去掉物料分类重复校验    begin*/
//		return this.checkPk_classA(vo, classkey, classCode)
//				+ this.checkPk_classInOtherMaterial(vo, classkey, classCode);
		/*20230105  yezhian 去掉物料分类重复校验    end*/
		return ""
				+ this.checkPk_classInOtherMaterial(vo, classkey, classCode);

	}

	private String checkPk_classA(PositionVO vo, String classkey, String classCode) {

		// 查询物料分类是否与其它岗位重复
		SqlBuilder sql = new SqlBuilder();
		sql.append(" select distinct t.code,b." + classCode + ",v.class_code  ");
		sql.append(" from po_position_b b,po_position_t t ,");

		sql.append(" (select t.pk_marclass,b." + classCode + " class_code ");
		sql.append("  from po_position_b b,po_position_t t ");
		sql.append("  where t.pk_position ='" + vo.getHVO().getPk_position() + "'");
		sql.append("  and b.pk_position_b = t.pk_position_b and b.dr = 0) v ");

		sql.append(" where t.pk_position_b = b.pk_position_b and b.dr =0 ");
		sql.append(" and t.pk_marclass = v.pk_marclass and b.fflag = 1 ");
		sql.append(" and ");
		sql.append("t.pk_org", vo.getHVO().getPk_org());
		sql.append(" and ");
		sql.append(" t.code ", "!= ", vo.getHVO().getCode());
		sql.append(" and ");
		sql.append("t.fpositiontype", vo.getHVO().getFpositiontype());

		String[][] result =
		// new DataAccessUtils().query(sql.toString()).toTwoDimensionStringArray();
		this.query(sql.toString(), vo, true);

		if (null == result || result.length == 0) {
			return this.checkClassOther(vo, classkey, classCode);
		}

		StringBuffer err = new StringBuffer();
		for (int i = 0, len = result.length; i < len; i++) {
			err.append(NCLangResOnserver.getInstance().getStrByID("4004080_0",
					"04004080-0012", null,
					new String[] { result[i][2], result[i][0], result[i][1] })/*
																																		 * 物料分类：{0}
																																		 * 与岗位编码
																																		 * ：{1}的
																																		 * 物料分类：{2}
																																		 * 设置重复。\n
																																		 */);
		}

		return err.toString();
	}

	private String checkPk_classInOtherMaterial(PositionVO vo, String classkey,
			String classCode) {
		// 查询物料分类是否与其它岗位设置的物料重复
		/**
		 * modify by liangchen1
		 * positionB表中应用的物料对应(包括本岗位)是基本分类还是采购分类关联分类主键，看该主键与t表中的分类主键inner join是否有数据
		 * 如果有，则现在数据库中的物料分类包含应用级物料，报错
		 */
		// 排除的物料
		List<String> movepks = new ArrayList<String>();
		// 物料基本分类
		List<String> marclass = new ArrayList<String>();
		// 物料采购分类
		List<String> marpuclass = new ArrayList<String>();
		for (PositionItemVO item : vo.getBVO()) {
			if (EnumUseMove.MOVE == item.getFflag().intValue()
					&& item.getPk_material() != null) {
				movepks.add(item.getPk_material());
			}
			if (item.getPk_marbasclass() != null) {
				marclass.add(item.getPk_marbasclass());
			}
			if (item.getPk_marpuclass() != null) {
				marpuclass.add(item.getPk_marpuclass());
			}
		}
		if (PositionItemVO.MARBASCLASS_CODE.equals(classCode)) {
			if (marclass.size() == 0) {
				return "";
			}
		} else {
			if (marpuclass.size() == 0) {
				return "";
			}
		}
		SqlBuilder sql = new SqlBuilder();
		sql.append("select t.marclasscode,v.code,v.pcode from po_position_t t");
		sql.append(" inner join (select");
		if (PositionItemVO.MARBASCLASS_CODE.equals(classCode)) {
			sql.append(" mv.code code,h.code pcode,mv.pk_marbasclass pk_marclass from po_position_b b inner join bd_material_v mv on b.pk_srcmaterial=mv.pk_source");
		} else {
			sql.append(" mv.code code,h.code pcode,ms.pk_marpuclass pk_marclass from po_position_b b inner join bd_material_v mv on b.pk_srcmaterial=mv.pk_source");
			sql.append(" inner join bd_materialstock ms on b.pk_srcmaterial=ms.pk_material");
		}
		sql.append(" inner join po_position h on h.pk_position=b.pk_position");
		sql.append(" where b.pk_srcmaterial <> '~'");
		if (movepks.size() != 0) {
			sql.appendNot(" and b.pk_srcmaterial",
					movepks.toArray(new String[movepks.size()]));
		}
		sql.append(" and b.pk_org", vo.getHVO().getPk_org());
		sql.append(" and b.fflag", EnumUseMove.USE);
		if (PositionItemVO.MARBASCLASS_CODE.equals(classCode)) {
			sql.append(" and mv.pk_marbasclass",
					marclass.toArray(new String[marclass.size()]));
		} else {
			sql.append(" and ms.pk_marpuclass",
					marpuclass.toArray(new String[marpuclass.size()]));
			sql.append(" and ms.pk_group", vo.getHVO().getPk_group());
			sql.append(" and ms.pk_org", vo.getHVO().getPk_org());
			sql.append(" and ms.dr", 0);
		}
		sql.append(" and b.dr", 0);
		sql.append(" and mv.dr", 0);
		sql.append(" and h.fpositiontype", vo.getHVO().getFpositiontype());
		sql.append(") v");
		sql.append(" on t.pk_marclass= v.pk_marclass where");
		sql.append(" t.pk_position", vo.getHVO().getPk_position());
		sql.append(" and t.dr=0");

		String[][] result =
		// new DataAccessUtils().query(sql.toString()).toTwoDimensionStringArray();
		this.query(sql.toString(), vo, false);
		if (null == result || result.length == 0) {
			return "";
		}

		StringBuffer err = new StringBuffer();
		for (int i = 0, len = result.length; i < len; i++) {
			err.append(NCLangResOnserver.getInstance().getStrByID("4004080_0",
					"04004080-0015", null,
					new String[] { result[i][0], result[i][2], result[i][1] }/*
																																		 * 物料分类：{0}
																																		 * 与岗位编码
																																		 * ：{1}的
																																		 * 物料：{2}
																																		 * 设置重复。\n
																																		 */));
		}
		return err.toString();

	}

	private String checkPk_srcmaterial(PositionVO vo, String classkey) {

		PositionItemVO[] items = vo.getBVO();

		// 物料PK和子表VO map
		HashMap<String, PositionItemVO> mapMaterial = new HashMap<String, PositionItemVO>();
		// 物料分类PK和子表VO map
		HashMap<String, PositionItemVO> mapClass = new HashMap<String, PositionItemVO>();
		// 物料PK
		ArrayList<String> pks = new ArrayList<String>();
		// 物料分类PK
		ArrayList<String> pkclasss = new ArrayList<String>();
		for (PositionItemVO item : items) {
			if (!StringUtil.isEmptyWithTrim(item.getPk_material())
					&& EnumUseMove.USE == item.getFflag().intValue()) {
				pks.add(item.getPk_material());
				mapMaterial.put(item.getPk_material(), item);
				if (item.getAttributeValue(classkey) != null) {
					pkclasss.add((String) item.getAttributeValue(classkey));
					mapClass.put((String) item.getAttributeValue(classkey), item);
				}
			}
		}

		if (pks.size() > 0) {
			// modified by fanly3 at 2013-07-02 查询物料对应的分类
			// IMaterialBaseInfoQueryService service =
			// NCLocator.getInstance().lookup(IMaterialBaseInfoQueryService.class);
			// MaterialVO[] materialVos =
			// service.queryDataByPks(pks.toArray(new String[pks.size()]));

			Map<String, String> baseclassMap = MaterialPubService
					.queryMaterialBaseClassPk(pks.toArray(new String[pks.size()]));

			for (Entry<String, String> entry : baseclassMap.entrySet()) {
				pkclasss.add(entry.getValue());
				mapClass.put(entry.getValue(), mapMaterial.get(entry.getKey()));
			}

			// for (MaterialVO materialVO : materialVos) {
			// pkclasss.add(materialVO.getPk_marbasclass());
			// mapClass.put(materialVO.getPk_marbasclass(),
			// mapMaterial.get(materialVO.getPk_material()));
			// }

			return this.checkMaterialB(pks.toArray(new String[pks.size()]), vo,
					mapMaterial)
					+ this.checkMaterialA(pkclasss.toArray(new String[pkclasss.size()]),
							vo, mapClass, classkey);
		}

		return "";
	}

	private void checkUnique(PositionVO vo, String classkey) {
		// 检查有物料的表体行
		String err = this.checkPk_srcmaterial(vo, classkey);
		// 检查没有物料的表体行
		err += this.checkPk_class(vo, classkey);
		if (!StringUtil.isEmptyWithTrim(err)) {
			ExceptionUtils.wrappBusinessException(err);
		}
	}

	private HashMap<String, HashSet<String>> getMoveMaterial(String[] codes,
			PositionVO vo) {
		// 查询出物料对应的分类是被其他岗位应用的排除的物料
		SqlBuilder sql = new SqlBuilder();
		sql.append(" select distinct h.code,b.pk_srcmaterial ");
		sql.append(" from po_position h, po_position_b b ");
		sql.append(" where h.pk_position = b.pk_position and h.dr =0 and b.dr = 0");
		sql.append(" and ");
		sql.append("h.pk_org", vo.getHVO().getPk_org());
		sql.append(" and ");
		sql.append("h.fpositiontype", vo.getHVO().getFpositiontype());
		sql.append(" and ");
		sql.append("h.code", codes);
		sql.append(" and ");
		sql.append("b.fflag", 2);

		String[][] result =
		// new DataAccessUtils().query(sql.toString()).toTwoDimensionStringArray();
		this.query(sql.toString(), vo, false);

		HashMap<String, HashSet<String>> map = new HashMap<String, HashSet<String>>();
		if (null == result || result.length == 0) {
			return map;
		}

		for (String[] row : result) {
			String code = row[0];
			if (map.containsKey(code)) {
				map.get(code).add(row[1]);
			} else {
				HashSet<String> pk = new HashSet<String>();
				pk.add(row[1]);
				map.put(code, pk);
			}
		}

		return map;
	}

	private String[][] getOtherUseClass(String[] pkclasss, PositionVO vo,
			String classkey) {
		// 查询出物料对应的分类是否被其他岗位应用
		SqlBuilder sql = new SqlBuilder();
		if (PositionItemVO.PK_MARBASCLASS.equals(classkey)) {
			sql.append(" select distinct t.code,t.pk_marclass,b.marbasclass_code ");
		} else {
			sql.append(" select distinct t.code,t.pk_marclass,b.marpuclass_code ");
		}

		sql.append(" from po_position_b b,po_position_t t ");
		sql.append(" where t.pk_position_b = b.pk_position_b and b.dr=0 ");
		sql.append(" and t.pk_position_b = b.pk_position_b and b.fflag =1 ");
		sql.append(" and ");
		sql.append(" t.code ","!= ", vo.getHVO().getCode());
		sql.append(" and ");
		sql.append("t.fpositiontype", vo.getHVO().getFpositiontype());
		sql.append(" and ");
		sql.append("t.pk_org", vo.getHVO().getPk_org());
		sql.append(" and ");
		// 临时表
		IDExQueryBuilder builder = new IDExQueryBuilder(
				PUTempTable.tmp_po_pos_07.name());
		sql.append(builder.buildSQL("t.pk_marclass", pkclasss == null
				|| pkclasss[0] == null ? new String[] { "~" } : pkclasss));

		return this.query(sql.toString(), vo, true);
	}

	private String[][] query(String sql, PositionVO vo, boolean code) {
		IRowSet rs = new DataAccessUtils().query(sql);
		return rs.toTwoDimensionStringArray();
	}
}
