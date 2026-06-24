package nc.vo.to.m5x.vochange.m30;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nc.vo.org.OrgVO;
import nc.vo.pf.change.ChangeVOAdjustContext;
import nc.vo.pf.change.IChangeVOAdjust;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.pattern.data.ValueUtils;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.pubapp.pattern.pub.MapList;
import nc.vo.scmpub.util.VOEntityUtil;
import nc.vo.to.m5x.entity.BillHeaderVO;
import nc.vo.to.m5x.entity.BillItemVO;
import nc.vo.to.m5x.entity.BillVO;
import nc.vo.to.m5x.pub.rule.FillRefForPushUtilsPub;
import nc.vo.to.m5x.vochange.pub.BillRefColumnName;
import nc.vo.to.pub.util.numdiff.DealNumDiffUtils;
import nc.vo.to.pub.util.numdiff.NumDiffMappingVO;

import nc.itf.fi.pub.SysInit;
import nc.itf.org.IBasicOrgUnitQryService;
import nc.itf.to.m5x.IQueryPCByStockOrgAndStock;
import nc.itf.to.m5x.ITransOrderRefManage;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ColumnProcessor;

import nc.bs.framework.common.NCLocator;

/**
 * 销售订单推单到调拨订单VO交换后处理类
 * 
 * @since 6.1
 * @version 2012-4-24 上午9:13:57
 * @author xuelm
 */
public class M30ChgVOAdjust implements IChangeVOAdjust {

	UFBoolean sys_to23 = UFBoolean.FALSE;
	String currtype = null;
	
	
  @Override
  public AggregatedValueObject adjustAfterChange(AggregatedValueObject srcVO,
      AggregatedValueObject destVO, ChangeVOAdjustContext adjustContext)
      throws BusinessException {
    return null;
  }

  @Override
  public AggregatedValueObject adjustBeforeChange(AggregatedValueObject srcVO,
      ChangeVOAdjustContext adjustContext) throws BusinessException {
    return null;
  }

  @Override
  public AggregatedValueObject[] batchAdjustAfterChange(
      AggregatedValueObject[] srcVOs, AggregatedValueObject[] destVOs,
      ChangeVOAdjustContext adjustContext) throws BusinessException {
    BillVO[] newDestVOs = null;
    try {
      // 推单到调拨订单时用户在前台选择的主组织在vo交换后还没有赋值给调拨订单表头的pk_org，所有在这里从上游订单的DEST_PK_ORG字段取
      this.setPkOrg(srcVOs, destVOs);
      FillRefForPushUtilsPub utils = new FillRefForPushUtilsPub();
      utils.fillDefaultData((BillVO[]) destVOs);
      // 填充发货利润中心字段
      this.fillPCId((BillVO[]) destVOs);

      DealNumDiffUtils dealNumUtil = new DealNumDiffUtils();
      dealNumUtil.dealNumDiff(this.getSrcMap(srcVOs), this.getDestMap(destVOs),
          this.getNumDiffMappingVO());
      ITransOrderRefManage service =
          NCLocator.getInstance().lookup(ITransOrderRefManage.class);
      newDestVOs = service.initM30Data((BillVO[]) destVOs);
      
//      UFBoolean paraBoolean = SysInit.getParaBoolean(newDestVOs[0].getParentVO().getPk_org(), "TO23"); 
//      if(UFBoolean.TRUE == paraBoolean){
//    	 //如果设置了参数，那么直接将
//  
//      }
//      
      this.setMaterial(srcVOs, destVOs);
      
     
    }
    catch (Exception ex) {
//      ExceptionUtils.marsh(ex);
    	throw new BusinessException(ex);
    }
    return newDestVOs;
  }

  

/**
   * 物料交换 转换为目标公司物料
   * @param srcVOs
   * @param destVOs
   */
  private void setMaterial(AggregatedValueObject[] srcVOs,
		AggregatedValueObject[] destVOs) throws BusinessException{
	  String destorg = (String) destVOs[0].getParentVO().getAttributeValue( "pk_org");//目标单据公司，转换后的物料所属公司
	  	UFDouble to24 = new UFDouble(SysInit.getParaString(destorg, "TO24"));     //内部交易折价率百分比
		UFBoolean paraBoolean = SysInit.getParaBoolean(destorg, "TO22");              //参数需要在分子单据所属公司开启
		if (UFBoolean.TRUE == paraBoolean) {

			Map<String, CircularlyAccessibleValueObject> sourceBodyMap = getSourceBodyMap(srcVOs);
			for (AggregatedValueObject bill : destVOs) {
				CircularlyAccessibleValueObject[] childrenVO = bill
						.getChildrenVO();
//				bill.getParentVO().setAttributeValue("ctrantypeid", "0001V110000000002QU8");
//				bill.getParentVO().setAttributeValue("vtrantypecode", "5X-01");
				UFDouble nexchangerate = (UFDouble) bill.getParentVO().getAttributeValue("nexchangerate"); //这本汇率
				
				for (CircularlyAccessibleValueObject body : childrenVO) {
					// body.setAttributeValue("pk_batchcode", null);
					// body.setAttributeValue("vbatchcode", null);

					String srcbid = (String) body
							.getAttributeValue("csrcbid");// 来源单据BID
					CircularlyAccessibleValueObject sourceBody = sourceBodyMap
							.get(srcbid);// 取来源单据表体明细
					String cmaterialoid = (String) sourceBody
							.getAttributeValue("cmaterialvid");// 来源物料
					
					body.setAttributeValue("vbdef1" ,cmaterialoid);//写入对方物料
					
					String pk_material = getDestMaterial(cmaterialoid, destorg);

					if (null == pk_material
							|| "".equals(pk_material.toString())) {
						throw new BusinessException("转单失败，检查表体行【"
								+ sourceBody.getAttributeValue("crowno") + "】,"
								+ "无法转换为目的单位对应物料，请检查主数据是否已关联。");
					}

					body.setAttributeValue("cinventoryid", pk_material);
					body.setAttributeValue("cinventoryvid", pk_material);
					body.setAttributeValue("cunitid", sourceBody.getAttributeValue("cunitid"));
					
					
				 // 处理单价信息
					UFDouble norigprice = (UFDouble) sourceBody.getAttributeValue("norigprice");//主无税单价	原币
					norigprice = norigprice.multiply(to24.div(100));  //折扣率
					body.setAttributeValue("norignetprice", norigprice);
					
					
					UFDouble ntaxrate = (UFDouble) body.getAttributeValue("ntaxrate"); //税率
					
					UFDouble norigtaxprice = norigprice.multiply(ntaxrate.div(100).add(1));	
					body.setAttributeValue("norigtaxnetprice", norigtaxprice );//主含税单价	
					
					
					UFDouble nprice = norigprice.multiply(nexchangerate); //原币无税单价* 汇率
					body.setAttributeValue("nnetprice", nprice);//主本币无税单价	
					body.setAttributeValue("nqtnetprice", nprice);//本币无税单价
					
					UFDouble ntaxnetprice = nprice.multiply(ntaxrate.div(100).add(1)); //本币无税*税率
					ntaxnetprice = ntaxnetprice.multiply(to24.div(100));
					body.setAttributeValue("ntaxnetprice",ntaxnetprice);//主本币含税单价	
					body.setAttributeValue("nqttaxnetprice", ntaxnetprice);//本币含税单价	
					

					UFDouble nqtorigprice =  (UFDouble) sourceBody.getAttributeValue("nqtorigprice");
					nqtorigprice = nqtorigprice.multiply(to24.div(100));
					body.setAttributeValue("nqtorignetprice", nqtorigprice);//无税单价
					
					UFDouble nastnum = (UFDouble) sourceBody.getAttributeValue("nastnum");//数量
					body.setAttributeValue("norigmny", nqtorigprice.multiply(nastnum)) ;//无税金额
					
					UFDouble nqtorigtaxprice = ((UFDouble)sourceBody.getAttributeValue("nqtorigprice"));
					nqtorigtaxprice = nqtorigtaxprice.multiply(to24.div(100));
					nqtorigtaxprice = nqtorigtaxprice.multiply(ntaxrate.div(100).add(1));
					body.setAttributeValue("nqtorigtaxnetprc", nqtorigtaxprice);//.含税单价
					
					
//					UFDouble nqtnetprice = ((UFDouble)sourceBody.getAttributeValue("nqtnetprice")).multiply(ntaxrate.div(100).add(1));
//					nqtnetprice = nqtnetprice.multiply(to24.div(100));
					
					UFDouble norigtaxmny =  nqtorigtaxprice.multiply(nastnum);
					body.setAttributeValue("norigtaxmny", norigtaxmny); //价税合计 = 数量*含税单价
					
					
					UFDouble nmny = nastnum.multiply(nprice);
					body.setAttributeValue("nmny", nmny);  //本币无税金额 
					
					UFDouble ntaxmny =  nastnum.multiply(ntaxnetprice);
					body.setAttributeValue("ntaxmny", ntaxmny);//本币价税合计  
					
					body.setAttributeValue("ntax", ntaxmny.sub(nmny)); //税额 = 价税合计 -本币无税金额 
					
					 
		
	
				}
			}
			}
	
}
  
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
	
	IUAPQueryBS iuap = (IUAPQueryBS) NCLocator.getInstance().lookup(
			IUAPQueryBS.class.getName());
		
		protected Map<String, CircularlyAccessibleValueObject> getSourceBodyMap(
				AggregatedValueObject[] srcVOs)
		/*     */throws BusinessException
		/*     */{
			/*  71 */Map bidtoSrcbody = new HashMap();
			/*     */
			/*  73 */for (CircularlyAccessibleValueObject srcbody : VOEntityUtil
					.getBodyVOs(srcVOs))
			/*     */{
				/*  75 */bidtoSrcbody.put(srcbody.getPrimaryKey(), srcbody);
				/*     */}
			/*  77 */return bidtoSrcbody;
			/*     */}

@Override
  public AggregatedValueObject[] batchAdjustBeforeChange(
      AggregatedValueObject[] srcVOs, ChangeVOAdjustContext adjustContext)
      throws BusinessException {

    return null;
  }

  private void fillPCId(BillVO[] billvos) {
    Set<String> toutorgSet = new HashSet<String>();
    for (BillVO vo : billvos) {
      BillHeaderVO head = vo.getParentVO();
      String ctoutorgid = head.getCtoutstockorgid();
      toutorgSet.add(ctoutorgid);
    }
    // 根据库存组织查询利润中心
    IQueryPCByStockOrgAndStock query =
        NCLocator.getInstance().lookup(IQueryPCByStockOrgAndStock.class);
    MapList<String, String> maplist = null;
    try {
      maplist = query.queryPCByStockOrg(toutorgSet);
    }
    catch (BusinessException e) {
      ExceptionUtils.wrappException(e);
    }
    for (BillVO vo : billvos) {
      BillHeaderVO head = vo.getParentVO();
      String ctoutorgid = head.getCtoutstockorgid();
      if (maplist != null && maplist.size() > 0) {
        List<String> list = maplist.get(ctoutorgid);
        if (list != null && list.size() > 0) {
          BillItemVO[] items = vo.getChildrenVO();
          for (BillItemVO item : items) {
            if (item.getCinpcid() != null) {
              item.setCoutpcid(list.get(0));
              item.setCoutpcvid(list.get(1));
            }
          }
        }
      }
    }
  }

  private Map<String, CircularlyAccessibleValueObject> getDestMap(
      AggregatedValueObject[] destVOs) {
    Map<String, CircularlyAccessibleValueObject> map =
        new HashMap<String, CircularlyAccessibleValueObject>();
    try {
      for (AggregatedValueObject vo : destVOs) {
        CircularlyAccessibleValueObject[] items = vo.getChildrenVO();
        for (CircularlyAccessibleValueObject item : items) {
          map.put(
              ValueUtils.getString(item.getAttributeValue(BillItemVO.CSRCBID)),
              item);
        }
      }
    }
    catch (Exception e) {
      ExceptionUtils.wrappException(e);
    }
    return map;
  }

  private NumDiffMappingVO getNumDiffMappingVO() {
    NumDiffMappingVO vo = new NumDiffMappingVO();
    vo.setDestAssNumKey(BillItemVO.NASTNUM);
    vo.setDestCassunitKey(BillItemVO.CASTUNITID);
    vo.setDestChangeRateKey(BillItemVO.VCHANGERATE);
    vo.setDestCqtchangeRateKey(BillItemVO.VQTUNITRATE);
    vo.setDestCqtunitKey(BillItemVO.CQTUNITID);
    vo.setDestNqtumKey(BillItemVO.NQTUNITNUM);
    vo.setDestNumKey(BillItemVO.NNUM);
    vo.setDestCunitKey(BillItemVO.CUNITID);
    vo.setSrcAssNumKey(BillRefColumnName.SaleOrderBVO_NASTNUM);
    vo.setSrcCassunitKey(BillRefColumnName.SaleOrderBVO_CASTUNITID);
    vo.setSrcChangeRateKey(BillRefColumnName.SaleOrderBVO_VCHANGERATE);
    vo.setSrcNumKey(BillRefColumnName.SaleOrderBVO_NNUM);
    return vo;
  }

  private Map<String, CircularlyAccessibleValueObject> getSrcMap(
      AggregatedValueObject[] srcVOs) {
    Map<String, CircularlyAccessibleValueObject> map =
        new HashMap<String, CircularlyAccessibleValueObject>();

    try {
      for (AggregatedValueObject vo : srcVOs) {
        CircularlyAccessibleValueObject[] items = vo.getChildrenVO();
        for (CircularlyAccessibleValueObject item : items) {
          map.put(item.getPrimaryKey(), item);
        }
      }
    }
    catch (Exception e) {
      ExceptionUtils.wrappException(e);
    }
    return map;
  }

  private void setPkOrg(AggregatedValueObject[] srcVOs,
      AggregatedValueObject[] destVOs) {
    String pk_org =
        (String) srcVOs[0].getParentVO().getAttributeValue(
            BillRefColumnName.SaleOrderBVO_DEST_PK_ORG);
    if (null != pk_org) {
      for (AggregatedValueObject destVO : destVOs) {
        destVO.getParentVO().setAttributeValue(BillHeaderVO.PK_ORG, pk_org);
      }
    }
  }
}
