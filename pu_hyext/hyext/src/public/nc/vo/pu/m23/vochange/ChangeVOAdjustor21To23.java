
package nc.vo.pu.m23.vochange;

import java.util.HashMap;
import java.util.Map;

import nc.bs.framework.common.NCLocator;
import nc.itf.fi.pub.SysInit;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.vo.pf.change.ChangeVOAdjustContext;
/*    */
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.CircularlyAccessibleValueObject;
/*    */
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;

public class ChangeVOAdjustor21To23 extends ArriveChangeVOAdjustor
{
	public AggregatedValueObject[] batchAdjustAfterChange(
			AggregatedValueObject[] srcVOs, AggregatedValueObject[] destVOs,
			ChangeVOAdjustContext adjustContext)
	throws BusinessException
	{
		AggregatedValueObject[] resultVOs = super
				.batchAdjustAfterChange(srcVOs, destVOs, adjustContext);
		
		chengeMateriaByMdm(srcVOs, destVOs, adjustContext);
		MedFiledsInfoUtilFor21To23.processVOs(resultVOs);
		return resultVOs;
		}

	private void chengeMateriaByMdm(AggregatedValueObject[] srcVOs,
			AggregatedValueObject[] destVOs, ChangeVOAdjustContext adjustContext) throws BusinessException {

		String destorg = (String) destVOs[0].getParentVO().getAttributeValue( "pk_org"); //目标公司
		UFBoolean paraBoolean = SysInit.getParaBoolean(destorg, "PO90");                 //参数确认
		if (UFBoolean.TRUE == paraBoolean) {
			StringBuffer erro = new StringBuffer();
			Map<String, CircularlyAccessibleValueObject> sourceBodyMap = getSourceBodyMap(srcVOs);
			for (AggregatedValueObject bill : destVOs) {
				CircularlyAccessibleValueObject[] childrenVO = bill
						.getChildrenVO();
				for (CircularlyAccessibleValueObject body : childrenVO) {
					// body.setAttributeValue("pk_batchcode", null);
					// body.setAttributeValue("vbatchcode", null);

					String srcbid = (String) body
							.getAttributeValue("csourcebid");// 来源单据BID
					CircularlyAccessibleValueObject sourceBody = sourceBodyMap
							.get(srcbid);// 取来源单据表体明细
					String cmaterialoid = (String) sourceBody
							.getAttributeValue("pk_srcmaterial");// 来源物料
					String pk_material = getDestMaterial(cmaterialoid, destorg);

					if (null == pk_material
							|| "".equals(pk_material.toString())) {
						erro.append("转单失败，检查表体行【"
								+ sourceBody.getAttributeValue("crowno") + "】 物料编码【"+this.getMaterialCode(cmaterialoid)+"】,"
								+ "无法转换为目的单位对应物料，请检查主数据是否已关联。\n");
						continue;
					}

					body.setAttributeValue("pk_material", pk_material);
					body.setAttributeValue("pk_srcmaterial", pk_material);
					body.setAttributeValue("cunitid", sourceBody.getAttributeValue("cunitid"));
					

					// 库存状态转换，由于九洲库存状态下放到组织级，调拨时，需要转换为对放公司库存状态。
					// 1、检查目标公司下物料是否启用了库存状态管理
//					boolean checkMaterialMarasstframe = checkMaterialMarasstframe(pk_material);
//					if (checkMaterialMarasstframe) {
//						// 2、通过对照表获取对方公司库存状态 --cstateid
//						String cstateid = (String) sourceBody
//								.getAttributeValue("cstateid");
//						String sourceorg = (String) sourceBody.getAttributeValue("pk_org");
//						String destCstateid = getDestCstateid(cstateid,sourceorg, destorg);
//						body.setAttributeValue("cstateid", destCstateid);
//					}
					// 3、生产厂商主数据编码交换
				}
			}
			if(erro.length() > 0){
				throw new BusinessException(erro.toString());
			}
		}
		
	}
	
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
	
	private Map<String, CircularlyAccessibleValueObject> getSourceBodyMap(
			AggregatedValueObject[] srcVOs) {
		if(null == srcVOs){
			return null;
		}else{
			Map<String, CircularlyAccessibleValueObject> bodymap = new HashMap<String, CircularlyAccessibleValueObject>();
			for(AggregatedValueObject bill : srcVOs){
				CircularlyAccessibleValueObject[] childrenVO = bill.getChildrenVO();
				if(null == childrenVO || childrenVO.length ==0){
					continue;
				}
				for(CircularlyAccessibleValueObject body : childrenVO){
					bodymap.put((String)body.getAttributeValue("pk_order_b"), body);//取订单主键
				}
			}
			return bodymap;
		}
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
	
	private String getMaterialCode(String cmaterialoid)
			throws BusinessException {
		String sql = "select code from bd_material where pk_material ='"+cmaterialoid+"' ";

		Object destmaterial_pk = iuap.executeQuery(sql, new ColumnProcessor());
		if (null == destmaterial_pk) {

			return null;
		}
		return destmaterial_pk.toString();
	}

}