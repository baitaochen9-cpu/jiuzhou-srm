package nccloud.web.ct.purdaily.ref;

import java.util.Map;

import nc.vo.ct.entity.CtAbstractBVO;
import nc.vo.ct.entity.CtAbstractVO;
import nc.vo.ct.price.entity.CtPriceHeaderVO;
import nc.vo.ct.purdaily.entity.CtPuVO;
import nc.vo.pubapp.pattern.pub.SqlBuilder;
import nc.vo.scmpub.util.StringUtil;
import nccloud.commons.lang.StringUtils;
import nccloud.framework.web.container.SessionContext;
import nccloud.framework.web.processor.refgrid.RefQueryInfo;
import nccloud.framework.web.ui.meta.RefMeta;
import nccloud.web.refer.DefaultGridRefAction;

/**
 * @description 合同价格信息过滤参照
 * @author xiahui
 * @date 2019年7月9日 上午10:17:02
 * @version ncc1.0
 */
public class Z2PriceInfoRefSql extends DefaultGridRefAction {
	@Override
	public RefMeta getRefMeta(RefQueryInfo refQueryInfo) {
		RefMeta meta = new RefMeta();
		meta.setCodeField(CtPriceHeaderVO.VCODE);
		meta.setNameField(CtPriceHeaderVO.VNAME);
		/** 设置多语name字段翻译 */
		meta.setMutilLangNameRef(true);
		meta.setPkField("pk_ct_price");
		meta.setTableName("ct_price");

		return meta;
	}

	@Override
	public String getExtraSql(RefQueryInfo para, RefMeta meta) {
		Map<String, String> conditions = para.getQueryCondition();
		if (conditions != null) {
			String pk_org = conditions.get(CtAbstractVO.PK_ORG);
			String cvendorid = conditions.get(CtPuVO.CVENDORID);
			String corigcurrencyid = conditions.get(CtAbstractVO.CORIGCURRENCYID);
			String pk_marbasclass = conditions.get(CtAbstractBVO.PK_MARBASCLASS);
			String pk_material = conditions.get(CtAbstractBVO.PK_MATERIAL);
			String cqtunitid = conditions.get(CtAbstractBVO.CQTUNITID);

			if (!StringUtil.isEmptyTrimSpace(pk_org) && !StringUtil.isEmptyTrimSpace(cvendorid)
					&& !StringUtil.isEmptyTrimSpace(corigcurrencyid) && (!StringUtil.isEmptyTrimSpace(pk_marbasclass)
							|| !StringUtil.isEmptyTrimSpace(pk_material) && !StringUtil.isEmptyTrimSpace(cqtunitid))) {
				SqlBuilder sql = new SqlBuilder();
				sql.append(" pk_ct_price in (");
				sql.append(" select pk_ct_price from ct_price ");
				sql.append(" where ");
				sql.append(CtPriceHeaderVO.PK_ORG, pk_org);
				sql.append(" and ");
				sql.append(CtPriceHeaderVO.CVENDORID, cvendorid);
				sql.append(" and ");
				sql.append(CtPriceHeaderVO.CORIGCURRENCYID, corigcurrencyid);
				if (!StringUtils.isEmpty(pk_material)) {
					sql.append(" and ((");
					sql.append(CtPriceHeaderVO.PK_MATERIAL, pk_material);
					sql.append(" and ");
					sql.append(CtPriceHeaderVO.CASTUNITID, cqtunitid);
					// 匹配物料分类类的价格信息表（物料分类可以是非末级分类）
					sql.append(") or (");
					sql.append(CtPriceHeaderVO.PK_MARBASCLASS);
					sql.append(" in (");
					sql.append(this.getBaseMarClsSql(pk_material));
					sql.append(")))");
				} else {
					sql.append(" and ");
					sql.append(CtPriceHeaderVO.PK_MARBASCLASS, pk_marbasclass);
				}
				sql.append(") and dr = 0 and bvalidateflag = 'Y' and ");
				sql.append(CtPriceHeaderVO.PK_GROUP, SessionContext.getInstance().getClientInfo().getPk_group());
				
				return sql.toString();
			}
		}

		return null;
	}

	/**
	 * 方法功能描述： 如果合同是物料类合同，则匹配末级和非末级分类物料的价格信息表 <b>参数说明</b>
	 * 
	 * @param pk_material
	 * @return
	 * @since 6.3
	 * @version 2013-8-9 上午10:39:07
	 * @author xihy1
	 */
	private String getBaseMarClsSql(String pk_material) {
		SqlBuilder sql = new SqlBuilder();
		sql.append(" select ct_price.pk_marbasclass from ct_price ct_price ");
		sql.append(
				" inner join bd_marbasclass mbcls1 on ct_price.pk_marbasclass=mbcls1.pk_marbasclass and  mbcls1.dr=0 ");
		sql.append(" inner join ");
		/** 查询物料所属的物料分类内码 */
		sql.append("(select ma.pk_source, mbcls2.innercode from bd_material_v ma ");
		sql.append(" inner join bd_marbasclass mbcls2 on ma.pk_marbasclass=mbcls2.pk_marbasclass and mbcls2.dr=0 ");
		sql.append(" where ma.dr=0 and ma.pk_source = '" + pk_material + "'");
		sql.append(") mclsjoin ");
		sql.append(" on substring(mclsjoin.innercode,1,len(mbcls1.innercode))=mbcls1.innercode ");
		return sql.toString();
	}

}
