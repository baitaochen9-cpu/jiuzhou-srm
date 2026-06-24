/** <a href="http://www.cpupk.com/decompiler">Eclipse Class Decompiler</a> plugin, Copyright (c) 2017 Chen Chao. **/
/*    */package nc.vo.ic.m4e.vochange;

/*    */
/*    */import java.util.Map;

import nc.bs.framework.common.NCLocator;
import nc.bs.ic.general.util.GenBsUtil;
import nc.itf.fi.pub.SysInit;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.md.persist.framework.IMDPersistenceQueryService;
/*    */
import nc.vo.ic.m4e.deal.IC4YTo4EProcess;
/*    */
import nc.vo.ic.m4e.entity.TransInVO;
/*    */
import nc.vo.ic.pub.pf.ICDefaultChangeVOAdjust;
/*    */
import nc.vo.ic.pub.util.NCBaseTypeUtils;
/*    */
import nc.vo.pf.change.ChangeVOAdjustContext;
/*    */
import nc.vo.pub.AggregatedValueObject;
/*    */
import nc.vo.pub.BusinessException;
/*    */
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFBoolean;
/*    */
import nc.vo.pub.lang.UFDouble;

/*    */
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */public class M4YTO4EChangeVOAdjust extends ICDefaultChangeVOAdjust
/*    */{
	/*    */public AggregatedValueObject[] batchAdjustAfterChange(
			AggregatedValueObject[] srcVOs, AggregatedValueObject[] destVOs,
			ChangeVOAdjustContext adjustContext)
	/*    */throws BusinessException
	/*    */{
		/* 29 */balanceTransinAstNum(srcVOs, destVOs);

		/*
		 * 依据表头调入公司参数，确认是否强制通过主数据进行转码 不考虑单据转换规则的字段映射关系 只取来源数据的主数据编码交换目地公司物料
		 */

		String destorg = (String) destVOs[0].getParentVO().getAttributeValue(
				"pk_org");
		UFBoolean paraBoolean = SysInit.getParaBoolean(destorg, "TO22");
		if (UFBoolean.TRUE == paraBoolean) {

			Map<String, CircularlyAccessibleValueObject> sourceBodyMap = getSourceBodyMap(srcVOs);
			for (AggregatedValueObject bill : destVOs) {
				CircularlyAccessibleValueObject[] childrenVO = bill
						.getChildrenVO();
				for (CircularlyAccessibleValueObject body : childrenVO) {
					// body.setAttributeValue("pk_batchcode", null);
					// body.setAttributeValue("vbatchcode", null);

					String srcbid = (String) body
							.getAttributeValue("csourcebillbid");// 来源单据BID
					CircularlyAccessibleValueObject sourceBody = sourceBodyMap
							.get(srcbid);// 取来源单据表体明细
					String cmaterialoid = (String) sourceBody
							.getAttributeValue("csrcmaterialoid");// 来源物料
					String pk_material = getDestMaterial(cmaterialoid, destorg);

					if (null == pk_material
							|| "".equals(pk_material.toString())) {
						throw new BusinessException("转单失败，检查表体行【"
								+ sourceBody.getAttributeValue("crowno") + "】,"
								+ "无法转换为目的单位对应物料，请检查主数据是否已关联。");
					}

					body.setAttributeValue("cmaterialoid", pk_material);
					body.setAttributeValue("cmaterialvid", pk_material);
					body.setAttributeValue("cunitid", sourceBody.getAttributeValue("cunitid"));
					

					// 库存状态转换，由于九洲库存状态下放到组织级，调拨时，需要转换为对放公司库存状态。
					// 1、检查目标公司下物料是否启用了库存状态管理
					boolean checkMaterialMarasstframe = checkMaterialMarasstframe(pk_material);
					if (checkMaterialMarasstframe) {
						// 2、通过对照表获取对方公司库存状态 --cstateid
						String cstateid = (String) sourceBody
								.getAttributeValue("cstateid");
						String sourceorg = (String) sourceBody.getAttributeValue("pk_org");
						String destCstateid = getDestCstateid(cstateid,sourceorg, destorg);
						body.setAttributeValue("cstateid", destCstateid);
					}
					// 3、生产厂商主数据编码交换
				}
			}
		}

		/*    */
		/*    */
		/* 32 */ExeformulasBodyUtilFor4E.exeformulas(destVOs);
		/*    */
		/*    */
		/*    */
		/* 36 */IC4YTo4EProcess proc = new IC4YTo4EProcess();
		/* 37 */GenBsUtil.initTransBillBaseProcess(proc);
		/* 38 */return proc.processBillVOs((TransInVO[]) destVOs);
		/*    */}

	/**
	 * 
	 * @param cstateid
	 *            来源库存状态
	 * @param destorg
	 *            目标组织
	 * @param destorg2 
	 * @throws BusinessException 
	 */
	private String getDestCstateid(String cstateid, String sourceorg, String destorg) throws BusinessException {
		// TODO Auto-generated method stub
		String sql = " select pk_desvalue from ( " 
       +"  select gl_docmaptemplet.pk_srcorgbook ,  gl_docmaptemplet.pk_desorgbook, gl_docmap.pk_srcvalue,  gl_docmap.pk_desvalue " 
       +"  from gl_docmaptemplet  " 
       +"  inner join gl_docmap on gl_docmap.pk_docmaptemplet = gl_docmaptemplet.pk_docmaptemplet " 
       +"  WHERE gl_docmaptemplet.templetcode in ('JZ02','JZ01','JZ03') " 
       +"   union " 
       +"  select    gl_docmaptemplet.pk_desorgbook pk_srcorgbook,gl_docmaptemplet.pk_srcorgbook  pk_desorgbook, " 
       +"  gl_docmap.pk_desvalue pk_srcvalue,gl_docmap.pk_srcvalue pk_desvalue " 
       +"  from gl_docmaptemplet  " 
       +"  inner join gl_docmap on gl_docmap.pk_docmaptemplet = gl_docmaptemplet.pk_docmaptemplet " 
       +"  WHERE gl_docmaptemplet.templetcode in ('JZ02','JZ01','JZ03')   )  " 
       +"   where     pk_desorgbook   ='"+destorg+"' and   pk_srcorgbook = '"+sourceorg+"' " 
       +"  and pk_srcvalue          = '"+cstateid+"' ";
		Object destmaterial_pk = iuap.executeQuery(sql, new ColumnProcessor());
		if(null == destmaterial_pk){
			return null;
		}
		return destmaterial_pk.toString();
		
		
	}

	/**
	 * 
	 * @param pk_material
	 * @return
	 * @throws BusinessException
	 */
	private boolean checkMaterialMarasstframe(String pk_material)
			throws BusinessException {
		String sql = "select bd_marassistant.code  "
				+ " from  bd_material "
				+ " left join bd_marasstframe on bd_material.pk_marasstframe = bd_marasstframe.pk_marasstframe "
				+ " left join bd_marassistant on bd_marasstframe.pk_marasstframe = bd_marassistant.pk_marasstframe "
				+ " where bd_material.pk_material ='" + pk_material + "' "
				+ " and  bd_marassistant.code = 1 ";
		Object destmaterial_pk = iuap.executeQuery(sql, new ColumnProcessor());
		if (null == destmaterial_pk) {
			return false;
		}
		return true;

	}

	IUAPQueryBS iuap = (IUAPQueryBS) NCLocator.getInstance().lookup(
			IUAPQueryBS.class.getName());

	/**
	 * 利用主数据编码实现物料ID转换
	 * 
	 * @param cmaterialoid
	 *            原物料ID
	 * @param destorg
	 *            目标库存组织
	 * @return 目标库存组织物料ID
	 * @throws BusinessException
	 */
	private String getDestMaterial(String cmaterialoid, String destorg)
			throws BusinessException {
		String sql = "select destmaterial.pk_material    "
				+ "from bd_material srcmaterial "
				+ "inner  join bd_material destmaterial on srcmaterial.def7 = destmaterial.def7 "
				+ " and nvl(srcmaterial.def7,'~') <>'~' and nvl(destmaterial.def7,'~')<>'~' "
				+ "where nvl(srcmaterial.dr,0)=0 and nvl(destmaterial.dr,0)=0  "
				+ " and srcmaterial.pk_material ='" + cmaterialoid + "'  "
				+ "  and destmaterial.pk_org = '" + destorg + "'";

		Object destmaterial_pk = iuap.executeQuery(sql, new ColumnProcessor());
		if (null == destmaterial_pk) {

			return null;
		}
		return destmaterial_pk.toString();
	}

	/*    */
	/*    */
	/*    */private void balanceTransinAstNum(AggregatedValueObject[] srcVOs,
			AggregatedValueObject[] destVOs)
	/*    */{
		/* 44 */balanceNshouldAstNum(srcVOs, destVOs, "nassistnum", null);
		/*    */}

	/*    */
	/*    */
	/*    */
	/*    */
	/*    */protected boolean isFirstTransFullNumBill(
			CircularlyAccessibleValueObject srcbody,
			CircularlyAccessibleValueObject destbody)
	/*    */{
		/* 52 */UFDouble shouldnum = (UFDouble) destbody
				.getAttributeValue("nshouldnum");
		/* 53 */UFDouble srcnum = (UFDouble) srcbody.getAttributeValue("nnum");
		/*    */
		/* 55 */return (NCBaseTypeUtils.isEquals(shouldnum, srcnum));
		/*    */}
	/*    */
}
