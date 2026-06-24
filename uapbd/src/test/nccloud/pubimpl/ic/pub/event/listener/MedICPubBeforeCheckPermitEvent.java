package nccloud.pubimpl.ic.pub.event.listener;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import nc.bs.businessevent.IBusinessEvent;
import nc.bs.businessevent.IBusinessListener;
import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.framework.common.NCLocator;
import nc.bs.ic.general.businessevent.ICGeneralCommonEvent;
import nc.bs.ic.general.businessevent.ICGeneralCommonEvent.ICGeneralCommonUserObj;
import nc.bs.logging.Logger;
import nc.bs.uif2.validation.ValidationException;
import nc.bs.uif2.validation.ValidationFailure;
import nc.impl.pubapp.pattern.data.vo.VOQuery;
import nc.impl.pubapp.pattern.database.IDExQueryBuilder;
import nc.itf.medpub.IMedpubSqlQueryService;
import nc.jdbc.framework.processor.BaseProcessor;
import nc.pubitf.uapbd.IMaterialPubService_C;
import nc.vo.arap.uforeport.SqlBuffer;
import nc.vo.bd.material.MaterialVO;
import nc.vo.bd.material.stock.MaterialStockVO;
import nc.vo.ic.general.define.ICBillBodyVO;
import nc.vo.ic.general.define.ICBillHeadVO;
import nc.vo.ic.general.define.ICBillVO;
import nc.vo.mmgp.util.MMStringUtil;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pubapp.AppContext;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.scmpub.res.billtype.ICBillType;

public class MedICPubBeforeCheckPermitEvent
  implements IBusinessListener
{
  public void doAction(IBusinessEvent event)
    throws BusinessException
  {
    ICGeneralCommonEvent.ICGeneralCommonUserObj obj = (ICGeneralCommonEvent.ICGeneralCommonUserObj)event
      .getUserObject();
    ICBillVO[] newBillVOs = (ICBillVO[])obj.getNewObjects();
    ICBillVO[] oldBillVOs = (ICBillVO[])obj.getOldObjects();
    if ("1001".equals(event.getEventType())) {
      checkSavePermit(oldBillVOs);
    }
    if ("1003".equals(event.getEventType())) {
      checkSavePermit(newBillVOs);
    }
  }
  
  public void checkSavePermit(ICBillVO[] aggvos)
  {
    try
    {
      if ((aggvos == null) || (aggvos.length == 0)) {
        return;
      }
      Set<String> pk_materials = new HashSet();
//      ICBillBodyVO item;
      for (ICBillVO aggvo : aggvos)
      {
        String billTypeCode = aggvo.getHead().getBillType().getCode();
        ICBillBodyVO[] items = aggvo.getBodys();
        for ( ICBillBodyVO item : items) {
          if ((!ICBillType.DiscardIn.getCode().equals(billTypeCode)) || (!"55A4".equals(item.getCsourcetype()))) {
            pk_materials.add(item.getCmaterialvid());
          }
        }
      }
      if ((pk_materials == null) || (pk_materials.size() == 0)) {
        return;
      }
      ValidationException exp = new ValidationException();
      Object map = isLotNoMap(pk_materials);
      Object vomap = queryMapMaterial(pk_materials);
//      ICBillBodyVO[] items = (item = aggvos).length;
      for (int i = 0; i < aggvos.length; i++)
      {
        ICBillVO aggvo = aggvos[i];
        Object retMap = wholeManaFalg(pk_materials, aggvo.getHead().getPk_org());
        Object resMap = qualityManFlag(pk_materials, aggvo.getHead().getPk_org());
        ICBillBodyVO[] items = aggvo.getBodys();
        for (ICBillBodyVO item : items)
        {
          String rowNo = item.getCrowno();
          String cmaterialvid = item.getCmaterialvid();
          Object dproducedate = item.getAttributeValue("dproducedate_148");
          Object vinvaliddate = item.getAttributeValue("vinvaliddate_148");
          
          boolean isLotNo = false;
          if ((map != null) && (((Map)map).get(cmaterialvid) != null)) {
            isLotNo = true;
          }
          boolean bvalidmanageFlag = false;
          if ((vomap != null) && (((Map)vomap).get(cmaterialvid) != null))
          {
            UFBoolean b = new UFBoolean(((MaterialVO)((Map)vomap).get(cmaterialvid)).getAttributeValue("bvalidmanage_148").toString());
            /*
             * 原开发在此处的b不清楚为什么取出来的值为null，理论上应当为Y/N
             * 暂时先增加判空直接跳过
             * bbt 25/09/03
             * */
            if (null != b && b.booleanValue()) {
              bvalidmanageFlag = true;
            }
          }
          Object vlotNo = item.getAttributeValue("vlotno_148");
          if ((isLotNo) && (MMStringUtil.isObjectStrEmpty(vlotNo)))
          {
            exp.getFailures().clear();
            exp.addValidationFailure(new ValidationFailure("第" + rowNo + "行【产品批号】不能为空！"));
          }
          if ((!MMStringUtil.isObjectStrEmpty(vlotNo)) && (isLotNo) && (bvalidmanageFlag) && ((MMStringUtil.isObjectStrEmpty(dproducedate)) || 
            (MMStringUtil.isObjectStrEmpty(vinvaliddate))))
          {
            exp.getFailures().clear();
            exp.addValidationFailure(new ValidationFailure("第" + rowNo + "行物料启用了批号管理，效期管理，【生产日期】、【有效期至】不可为空！"));
          }
          String vbatchcode = item.getVbatchcode();
          boolean wholeManaFlag = false;
          if ((retMap != null) && (((UFBoolean)((Map)retMap).get(cmaterialvid)).booleanValue())) {
            wholeManaFlag = true;
          }
          boolean qualityManFlag = false;
          if ((resMap != null) && (((UFBoolean)((Map)resMap).get(cmaterialvid)).booleanValue())) {
            qualityManFlag = true;
          }
          if ((!MMStringUtil.isObjectStrEmpty(vbatchcode)) && (wholeManaFlag) && (qualityManFlag) && ((MMStringUtil.isObjectStrEmpty(dproducedate)) || 
            (MMStringUtil.isObjectStrEmpty(vinvaliddate))))
          {
            exp.getFailures().clear();
//            exp.addValidationFailure(
//              new ValidationFailure("第" + rowNo + "行物料启用了批次管理，保质期管理，【生产日期】、【有效期至】不可为空！"));
          }
        }
      }
      if (exp.getFailureMessage().toArray().length > 0) {
        ExceptionUtils.wrappBusinessException(exp.getFailureMessage().toString());
      }
    }
    catch (Exception ex)
    {
      ExceptionUtils.wrappException(ex);
    }
  }
  
  public Map<String, UFBoolean> qualityManFlag(Set<String> pk_materials, String pk_org)
    throws BusinessException
  {
    if ((pk_materials == null) || (pk_materials.size() == 0)) {
      return null;
    }
    String[] fields = new String[3];
    fields[0] = "wholemanaflag";
    fields[1] = "qualitymanflag";
    IMaterialPubService_C service = (IMaterialPubService_C)NCLocator.getInstance().lookup(IMaterialPubService_C.class);
    Map<String, MaterialStockVO> map = service.queryMaterialStockInfoByPks(
      (String[])pk_materials.toArray(new String[pk_materials.size()]), pk_org, fields);
    Map<String, UFBoolean> retMap = new HashMap();
    for (String pk_material : pk_materials)
    {
      UFBoolean wholeManaFlag = UFBoolean.FALSE;
      if ((map != null) && (map.get(pk_material) != null) && (((MaterialStockVO)map.get(pk_material)).getWholemanaflag() != null))
      {
        wholeManaFlag = ((MaterialStockVO)map.get(pk_material)).getWholemanaflag();
        retMap.put(pk_material, wholeManaFlag);
      }
    }
    IDExQueryBuilder builder = new IDExQueryBuilder("tmp_ncc_pu_05");
    String bidsCond = builder.buildSQL("pk_material", (String[])pk_materials.toArray(new String[pk_materials.size()]));
    SqlBuffer sb = new SqlBuffer();
    sb.append("select bvalidmanage from med_invdoc_148 where ");
    sb.append(bidsCond);
    Map<String, Object> resMap = ((IMedpubSqlQueryService)NCLocator.getInstance().lookup(IMedpubSqlQueryService.class)).queryMapBySql(sb.toString());
    for (String pk_material : pk_materials) {
      if ((resMap != null) && (!resMap.isEmpty()))
      {
        String bvalidmanage = (String)resMap.get("bvalidmanage");
        if ("Y".equals(String.valueOf(bvalidmanage))) {
          retMap.put(pk_material, UFBoolean.TRUE);
        }
      }
    }
    return retMap;
  }
  
  public Map<String, UFBoolean> wholeManaFalg(Set<String> pk_materials, String pk_org)
    throws BusinessException
  {
    if ((pk_materials == null) || (pk_materials.size() == 0)) {
      return null;
    }
    String[] fields = new String[3];
    fields[0] = "wholemanaflag";
    fields[1] = "qualitymanflag";
    IMaterialPubService_C service = (IMaterialPubService_C)NCLocator.getInstance().lookup(IMaterialPubService_C.class);
    Map<String, MaterialStockVO> map = service.queryMaterialStockInfoByPks(
      (String[])pk_materials.toArray(new String[pk_materials.size()]), pk_org, fields);
    Map<String, UFBoolean> retMap = new HashMap();
    for (String pk_material : pk_materials)
    {
      UFBoolean wholeManaFlag = UFBoolean.FALSE;
      if ((map != null) && (map.get(pk_material) != null) && (((MaterialStockVO)map.get(pk_material)).getWholemanaflag() != null))
      {
        wholeManaFlag = ((MaterialStockVO)map.get(pk_material)).getWholemanaflag();
        retMap.put(pk_material, wholeManaFlag);
      }
    }
    return retMap;
  }
  
  public Map<String, String> isLotNoMap(Set<String> pk_materials)
  {
    if ((pk_materials == null) || (pk_materials.size() == 0)) {
      return null;
    }
    StringBuffer sql = new StringBuffer();
    AppContext.getInstance().getPkGroup();
    sql.append(" select bd_material.pk_material,bd_marassistant.pk_marassistant ");
    sql.append(" from bd_marassistant ");
    sql.append(" inner join bd_marasstframe on bd_marassistant.pk_marasstframe = bd_marasstframe.pk_marasstframe ");
    sql.append(" inner join bd_material on bd_material.pk_marasstframe = bd_marasstframe.pk_marasstframe ");
    sql.append(" where  bd_marassistant.code = '11' and bd_marassistant.pk_org = '" + AppContext.getInstance().getPkGroup() + "'");
    IDExQueryBuilder builder = new IDExQueryBuilder("tmp_ncc_pu_04");
    String bidsCond = builder.buildSQL("bd_material.pk_material", (String[])pk_materials.toArray(new String[pk_materials.size()]));
    sql.append(" and " + bidsCond);
    Map<String, String> map = null;
    try
    {
      map = (Map)new BaseDAO().executeQuery(sql.toString(), new MapResultProcessor());
    }
    catch (DAOException e)
    {
      Logger.error("查询bd_marassistant报错");
    }
    return map;
  }
  
  public Map<String, MaterialVO> queryMapMaterial(Set<String> materiall)
    throws BusinessException
  {
    if ((materiall == null) || (materiall.size() == 0)) {
      return null;
    }
    VOQuery<MaterialVO> query = new VOQuery(MaterialVO.class, new String[] { "pk_material", "name", "bvalidmanage_148" });
    MaterialVO[] materialVOs = (MaterialVO[])query.query((String[])materiall.toArray(new String[materiall.size()]));
    if ((materialVOs == null) || (materialVOs.length == 0)) {
      return null;
    }
    Map<String, MaterialVO> pk_materialVO = new HashMap();
    for (MaterialVO materialVO : materialVOs) {
      pk_materialVO.put(materialVO.getPk_material(), materialVO);
    }
    return pk_materialVO;
  }
  
  class MapResultProcessor
    extends BaseProcessor
  {
    private int keyIndex = 1;
    private int valueIndex = 2;
    
    public MapResultProcessor() {}
    
    public MapResultProcessor(int keyIndex, int valueIndex)
    {
      this.keyIndex = keyIndex;
      this.valueIndex = valueIndex;
    }
    
    public Object processResultSet(ResultSet rs)
      throws SQLException
    {
      Map<String, String> map = new HashMap();
      while (rs.next()) {
        map.put(rs.getString(this.keyIndex), rs.getString(this.valueIndex));
      }
      return map;
    }
  }
}
