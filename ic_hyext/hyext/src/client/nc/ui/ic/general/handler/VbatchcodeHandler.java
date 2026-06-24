/** <a href="http://www.cpupk.com/decompiler">Eclipse Class Decompiler</a> plugin, Copyright (c) 2017 Chen Chao. */
package nc.ui.ic.general.handler;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.swing.JTextField;
import javax.swing.table.TableColumn;
import nc.bs.framework.common.NCLocator;
import nc.impl.ic.util.DateCalUtil;
import nc.itf.ic.batch.IBatchRefQuery;
import nc.pubitf.scmf.ic.mbatchcode.IBatchcodePubService;
import nc.ui.ic.batchcode.ref.BatchRefPane;
import nc.ui.ic.general.model.ICGenBizEditorModel;
import nc.ui.ic.general.view.ICBizView;
import nc.ui.ic.general.view.uientity.ICUIBillEntity;
import nc.ui.ic.material.query.InvInfoUIQuery;
import nc.ui.ic.pub.env.ICUIContext;
import nc.ui.ic.pub.handler.card.ICCardEditEventHandler;
import nc.ui.ic.pub.model.ICBizEditorModel;
import nc.ui.ic.pub.util.CardPanelWrapper;
import nc.ui.medpub.editor.card.afteredit.body.BatchCode;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillCellEditor;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillModel;
import nc.ui.pubapp.uif2app.event.card.CardBodyAfterEditEvent;
import nc.ui.pubapp.uif2app.event.card.CardBodyBeforeEditEvent;
import nc.ui.uif2.factory.UIF2BeanFactory;
import nc.vo.ic.batch.BatchRefViewVO;
import nc.vo.ic.batchcode.BatchDlgParam;
import nc.vo.ic.batchcode.BatchSynchronizer;
import nc.vo.ic.batchcode.ICBatchFields;
import nc.vo.ic.batchcode.ICNewBatchFields;
import nc.vo.ic.material.define.InvCalBodyVO;
import nc.vo.ic.onhand.entity.OnhandDimVO;
import nc.vo.ic.onhand.entity.OnhandDlgGeneralHeadVO;
import nc.vo.ic.pub.define.IBizObject;
import nc.vo.ic.pub.util.CollectionUtils;
import nc.vo.ic.pub.util.NCBaseTypeUtils;
import nc.vo.ic.pub.util.StringUtil;
import nc.vo.ic.pub.util.VOEntityUtil;
import nc.vo.ic.pub.util.ValueCheckUtil;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.ISuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.calculator.HslParseUtil;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.pubapp.pattern.model.meta.entity.view.DataViewMeta;
import nc.vo.scmf.ic.mbatchcode.BatchcodeVO;
import nc.vo.scmpub.res.billtype.ICBillType;
import nc.vo.uif2.LoginContext;
import nc.bs.logging.Logger;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

public class VbatchcodeHandler
  extends ICCardEditEventHandler
  implements BeanFactoryAware
{
  private BatchCode medBacthAfter;
  private int refrow = 0;
  private final List<ICBillType> specialList = new ArrayList(
    Arrays.asList(new ICBillType[] {ICBillType.TransferWarehouse, ICBillType.LocAdjust, 
    ICBillType.Transform, ICBillType.DiscardOut, 
    ICBillType.WasterProcess, ICBillType.Assembly, 
    ICBillType.Teardown }));
  
  public void afterCardBodyEdit(CardBodyAfterEditEvent event)
  {
    BillModel bm = 
      getEditorModel().getCardPanelWrapper().getBillCardPanel()
      .getBillModel();
    boolean needCalculate = bm.isNeedCalculate();
    try
    {
      bm.setNeedCalculate(false);
      int row = event.getRow();
      String batchStr = 
        (String)getEditorModel().getCardPanelWrapper()
        .getBodyValueAt(row, "vbatchcode");
      
      
      
      if (!isEditEnable(event, batchStr)) {
        return;
      }
      
      // 修复bug:在clearBatchInfo清空批次信息前，先保存cprodprojectid，避免拆行后项目分配值丢失
      String cprodprojectidStr = 
	  	        (String)getEditorModel().getCardPanelWrapper()
	  	        .getBodyValueAt(row, "cprodprojectid");
      
      setOldassnum(getEditorModel().getCardPanelWrapper()
        .getBodyValueAt_UFDouble(row, "nassistnum"));
      clearBatchInfo(row);
      BatchDlgParam queryParam = getBatchDlgParam(row);
      queryParam.setIsQueryZeroLot(UFBoolean.TRUE);
      

      UFBoolean isByHand = UFBoolean.FALSE;
      if (!getBatchRefPane().isClicked())
      {
        isByHand = UFBoolean.TRUE;
      }
      else
      {
        List<BatchRefViewVO> refVOs = getBatchRefPane().getRefVOs();
        if ((refVOs != null) && (refVOs.size() > 0))
        {
          String batchCode = 
            (String)((BatchRefViewVO)getBatchRefPane().getRefVOs().get(0))
            .getAttributeValue("vbatchcode");
          if ((batchStr == null) || (!batchStr.equals(batchCode + "."))) {
            isByHand = UFBoolean.TRUE;
          }
        }
      }
      if (UFBoolean.TRUE.equals(isByHand))
      {
        this.medBacthAfter.setCurrentSelectRows(new int[] { event.getRow() });
        handleBatchByHand(batchStr, row, queryParam);
        return;
      }
      List<BatchRefViewVO> refVOs = getBatchRefPane().getRefVOs();
      
      sysBatchDocToVO(refVOs);
      
      setProDateAndDvalidate(refVOs, event);
      
      dealRedBillNum(row, refVOs);
      addLinesByVO(refVOs, row);
      
      for(int i=0;i<refVOs.size();i++){
    	  if (!StringUtil.isSEmptyOrNull(cprodprojectidStr)) {
    		  Logger.debug("VbatchcodeHandler.afterCardBodyEdit - 批次拆行设置cprodprojectid. row=" + i + ", value=" + cprodprojectidStr);
    		  getEditorModel().getCardPanelWrapper().setBodyValueAt(cprodprojectidStr, i, "cprodprojectid");
    	  } else {
    		  Logger.debug("VbatchcodeHandler.afterCardBodyEdit - cprodprojectid为空,跳过设置,保护界面原有项目分配值. row=" + i);
    	  }
      }
      
      clearBodySNInfo(row);
      if (getBatchRefPane().isClicked()) {
        getBatchRefPane().setClicked(false);
      }
    }
    finally
    {
      bm.setNeedCalculate(needCalculate);
      bm.loadLoadRelationItemValue();
    }
    bm.setNeedCalculate(needCalculate);
    bm.loadLoadRelationItemValue();
  }
  
  private void dealRedBillNum(int row, List<BatchRefViewVO> refVOs)
  {
    if (ValueCheckUtil.isNullORZeroLength(refVOs)) {
      return;
    }
    BillModel bm = 
      getEditorModel().getCardPanelWrapper().getBillCardPanel()
      .getBillModel();
    UFDouble nsnum = 
      (UFDouble)bm.getValueAt(row, "nshouldnum");
    UFDouble nasnum = 
      (UFDouble)bm.getValueAt(row, "nshouldassistnum");
    boolean isNeg = 
      (NCBaseTypeUtils.isLtZero(nasnum)) || (NCBaseTypeUtils.isLtZero(nsnum));
    if (!isNeg) {
      return;
    }
    for (BatchRefViewVO vo : refVOs)
    {
      UFDouble nnum = (UFDouble)vo.getAttributeValue("nnum");
      vo.setAttributeValue("nnum", 
        NCBaseTypeUtils.negUFDouble(nnum));
      UFDouble nassistnum = 
        (UFDouble)vo.getAttributeValue("nassistnum");
      vo.setAttributeValue("nassistnum", 
        NCBaseTypeUtils.negUFDouble(nassistnum));
    }
  }
  
  public void beforeCardBodyEdit(CardBodyBeforeEditEvent event)
  {
    super.beforeCardBodyEdit(event);
    BillItem billItem = 
      getEditorModel().getCardPanelWrapper().getBodyItem(event.getKey());
    TableColumn tc = 
      getEditorModel().getCardPanelWrapper()
      .getColumn(billItem.getName());
    if (!isEditable(event))
    {
      if (tc != null) {
        tc.setCellEditor(new BillCellEditor(new JTextField()));
      }
      event.setReturnValue(Boolean.FALSE);
      return;
    }
    if (tc != null) {
      tc.setCellEditor(new BillCellEditor(getBatchRefPane()));
    }
    BatchDlgParam param = getBatchDlgParam(event.getRow());
    getBatchRefPane().setParam(param);
    event.setReturnValue(Boolean.TRUE);
  }
  
  private List<Integer> addLine(int length, int row)
  {
    int lastRowIndex = 
      getEditorModel().getCardPanelWrapper().getRowCount(null) - 1;
    List<Integer> rowIndex = new ArrayList();
    
    int beforecurrrow = 0;
    rowIndex.add(Integer.valueOf(row));
    for (int i = 1; i < length; i++)
    {
      int tempRow;
//      int tempRow;
      if (row == lastRowIndex)
      {
        getEditorModel().getCardPanelWrapper().addLineByBodyMenu(row);
        tempRow = 
          getEditorModel().getCardPanelWrapper().getRowCount(null) - 1;
      }
      else
      {
        tempRow = row + i;
        getEditorModel().getCardPanelWrapper()
          .insertLineByBodyMenu(tempRow);
        beforecurrrow++;
      }
      rowIndex.add(Integer.valueOf(tempRow));
    }
    setRefrow(row + beforecurrrow);
    return rowIndex;
  }
  
  private ICBizEditorModel getGenEditorModel()
  {
    return getEditorModel();
  }
  
  private boolean isEditable(CardBodyBeforeEditEvent e)
  {
    String pk_org = 
      getEditorModel().getCardPanelWrapper()
      .getHeadValue_String("pk_org");
    String cinvidvid = 
      (String)getEditorModel().getCardPanelWrapper()
      .getBodyValueAt(e.getRow(), "cmaterialvid");
    
    boolean bHasCorr = 
    
      !StringUtil.isSEmptyOrNull(getEditorModel().getCardPanelWrapper().getBodyValueAt_String(e.getRow(), "ccorrespondhid"));
    if (bHasCorr) {
      return false;
    }
    if (e.getRow() != getEditorModel().getCardPanelWrapper()
      .getSelectedRow()) {
      return false;
    }
    if ((!StringUtil.isSEmptyOrNull(cinvidvid)) && 
      (InvInfoUIQuery.getInstance().isWholemanaflag(pk_org, cinvidvid))) {
      return true;
    }
    return false;
  }
  
  private boolean isEditEnable(CardBodyAfterEditEvent e, String batchStr)
  {
    if ((!getBatchRefPane().isClicked()) && 
      (StringUtil.isStringEqual(batchStr, (String)e.getOldValue()))) {
      return false;
    }
    if (!StringUtil.isStringEqual(batchStr, (String)e.getOldValue())) {
      return true;
    }
    int cardVersion = 
      getEditorModel().getCardPanelWrapper()
      .getBodyValueAt_int(e.getRow(), "ibcversion");
    BatchRefViewVO refVO = (BatchRefViewVO)getBatchRefPane().getRefVOs().get(0);
    String pk_batchcode = 
      getEditorModel().getCardPanelWrapper()
      .getBodyValueAt_String(e.getRow(), "pk_batchcode");
    boolean issamePk = 
      StringUtil.isStringEqual(
      (String)refVO.getAttributeValue("pk_batchcode"), 
      pk_batchcode);
    boolean issameVersion = 
      ((Integer)refVO.getAttributeValue("version")).intValue() == cardVersion;
    if ((getBatchRefPane().isClicked()) && (issameVersion) && (issamePk))
    {
      getEditorModel().getCardPanelWrapper().setBodyValueAt(refVO.getAttributeValue("vbatchcode"), 
        e.getRow(), "vbatchcode");
      return false;
    }
    return true;
  }
  
  private void sysBatchDocToVO(List<BatchRefViewVO> refVOs)
  {
    if (ValueCheckUtil.isNullORZeroLength(refVOs)) {
      return;
    }
    BatchcodeVO[] batchcodeVOs = null;
    try
    {
      String[] pkbatchcodes = VOEntityUtil.getVOsNotRepeatValue((CircularlyAccessibleValueObject[])CollectionUtils.listToArray(refVOs), "pk_batchcode");
      batchcodeVOs = ((IBatchcodePubService)NCLocator.getInstance().lookup(IBatchcodePubService.class)).queryBatchcodesByPks(pkbatchcodes);
    }
    catch (BusinessException e)
    {
      ExceptionUtils.wrappException(e);
    }
    if (ValueCheckUtil.isNullORZeroLength(batchcodeVOs)) {
      return;
    }
    Map<String, BatchcodeVO> batchdocmap = CollectionUtils.hashVOArray("pk_batchcode", batchcodeVOs);
    for (BatchRefViewVO refvo : refVOs)
    {
      String pk_batchcode = (String)refvo.getAttributeValue("pk_batchcode");
      if (!StringUtil.isSEmptyOrNull(pk_batchcode)) {
        refvo.setVO((ISuperVO)batchdocmap.get(pk_batchcode));
      }
    }
  }
  
  private void setProDateAndDvalidate(List<BatchRefViewVO> refVOs, CardBodyAfterEditEvent event)
  {
    InvCalBodyVO calbodyVO = 
      getGenEditorModel().getInvCalBodyVO(event.getRow());
    if ((calbodyVO != null) && 
      (ValueCheckUtil.isTrue(calbodyVO.getWholemanaflag())) && 
      (ValueCheckUtil.isTrue(calbodyVO.getQualitymanflag())))
    {
      Integer qualityUnit = calbodyVO.getQualityunit();
      Integer qualityNum = calbodyVO.getQualitynum();
      for (BatchRefViewVO refVO : refVOs) {
        if ((refVO.getAttributeValue("dproducedate") != null) && 
          (refVO.getAttributeValue("dvalidate") == null))
        {
          UFDate dproduceDate = 
            (UFDate)refVO.getAttributeValue("dproducedate");
          refVO.setAttributeValue("dvalidate", 
            DateCalUtil.calDvalidate(dproduceDate, qualityNum, qualityUnit));
        }
        else if ((refVO.getAttributeValue("dvalidate") != null) && 
          (refVO.getAttributeValue("dproducedate") == null))
        {
          UFDate dvalidate = 
            (UFDate)refVO.getAttributeValue("dvalidate");
          refVO.setAttributeValue(
            "dproducedate", 
            DateCalUtil.calDvalidate(dvalidate, 
            Integer.valueOf(0 - qualityNum.intValue()), qualityUnit));
        }
      }
    }
  }
  
  private void setCprodprojectid(List<BatchRefViewVO> refVOs, CardBodyAfterEditEvent event,String cprodprojectidStr)
  {
    InvCalBodyVO calbodyVO = 
      getGenEditorModel().getInvCalBodyVO(event.getRow());
    if ((calbodyVO != null) && 
      (ValueCheckUtil.isTrue(calbodyVO.getWholemanaflag())) && 
      (ValueCheckUtil.isTrue(calbodyVO.getQualitymanflag())))
    {
      for (BatchRefViewVO refVO : refVOs) {
          refVO.setAttributeValue("cprodprojectid",cprodprojectidStr);
      }
    }
  }
  
  private void synRowInfo(BatchRefViewVO refVO, int index)
  {
    IBizObject body = getCardBodyVO(index);
    



    synBatch(refVO, body);
  }
  
  protected void addLinesByVO(List<BatchRefViewVO> refVOs, int row)
  {
    List<Integer> rowIndex = addLine(refVOs.size(), row);
    this.medBacthAfter.setCurrentSelectRows(ArrayUtils.toPrimitive((Integer[])rowIndex.toArray(new Integer[0])));
    for (int i = 0; i < refVOs.size(); i++)
    {
      if (getRefrow() != ((Integer)rowIndex.get(i)).intValue()) {
        synSrcBillInfo(getRefrow(), ((Integer)rowIndex.get(i)).intValue(), 
          getBillFieldsNeedCopy(), getBillFieldsNoNeedCopy());
      }
      synRowInfo((BatchRefViewVO)refVOs.get(i), ((Integer)rowIndex.get(i)).intValue());
      caculateAfterNumEdit(((Integer)rowIndex.get(i)).intValue());
    }
  }
  
  private void caculateAfterNumEdit(int row)
  {
    if (!isFillNum()) {
      return;
    }
    UFDouble newnum = getEditorModel().getCardPanelWrapper().getBodyValueAt_UFDouble(row, "nnum");
    CardBodyAfterEditEvent event = 
      new CardBodyAfterEditEvent(getEditorModel().getCardPanelWrapper()
      .getBillCardPanel(), null, row, "nnum", newnum, 
      null);
    event.setContext(getEditorModel().getContext().getLoginContext());
    ICBillType type = getEditorModel().getICBillType();
    getEditorModel().getCardPanelWrapper().setBodyValueAt(null, row, "nassistnum");
    if (type.isAdjust())
    {
      nc.ui.ic.special.handler.MainNumHandler handler = new nc.ui.ic.special.handler.MainNumHandler();
      handler.setEditorModel(getEditorModel());
      handler.afterCardBodyEdit(event);
      return;
    }
    MainNumHandler handler = new MainNumHandler();
    handler.setEditorModel(getEditorModel());
    handler.afterCardBodyEdit(event);
  }
  
  protected Set<String> getBillFieldsNoNeedCopy()
  {
    Set<String> fields = new HashSet();
    String[] bodyfields = 
      {
      "crowno", "cgeneralbid", 
      "nnum", "nassistnum", 
      "nshouldnum", "nshouldassistnum", 
      "nqtunitnum" };
    
    String[] dimfields = 
      {
      "cwarehouseid", "castunitid", 
      "clocationid", "pk_batchcode", 
      "vbatchcode", "vchangerate", 
      "cvmivenderid", "ctplcustomerid", 
      "cstateid", "cvendorid", "cprojectid", 
      "casscustid", "cproductorid", "vfree1", 
      "vfree2", "vfree3", "vfree4", 
      "vfree5", "vfree6", "vfree7", 
      "vfree8", "vfree9", "vfree10" };
    
    CollectionUtils.addArrayToSet(fields, bodyfields);
    CollectionUtils.addArrayToSet(fields, dimfields);
    return fields;
  }
  
  protected String[] getBillFieldsNeedCopy()
  {
    BillItem[] billitems = 
      ((ICGenBizEditorModel)getEditorModel()).getCardPanelWrapper()
      .getBillModel().getBodyItems();
    List<String> fieldsNames = new ArrayList();
    for (BillItem billitem : billitems) {
      fieldsNames.add(billitem.getMetaDataAccessPath());
    }
    return (String[])fieldsNames.toArray(new String[fieldsNames.size()]);
  }
  
  @Deprecated
  protected String[] getSrcBillInfoFields()
  {
    String[] synsrcFields = 
      {
      "csourcebillhid", "csourcebillbid", 
      "csourcetranstype", "csourcetype", 
      "vsourcebillcode", "vsourcerowno", 
      "cfirstbillhid", "cfirstbillbid", 
      "cfirsttranstype", "cfirsttype", 
      "vfirstbillcode", "vfirstrowno", 
      "csrc2billhid", "csrc2billbid", 
      "csrc2billtype", "csrc2transtype", 
      "vsrc2billcode", "vsrc2billrowno", 
      "ccorrespondhid", "ccorrespondbid", 
      "ccorrespondcode", "ccorrespondrowno", 
      "ccorrespondtranstype", "ccorrespondtype" };
    

    String[] otherarrs = null;
    if (getGenEditorModel().getICBillType().isEqual(ICBillType.SaleOut.getCode()))
    {
      otherarrs = 
        new String[] {
        "csignwastbid", "csignwasthid", 
        "csignwasttype", "vsignwastcode", 
        "vsignwastrowno", "csourcewastbid", 
        "csourcewasthid", "csourcewasttranstype", 
        "csourcewasttype", "vsourcewastcode", 
        "vsourcewastrowno" };
      
      String[] otherExtArrs = 
        {
        "ccurrencyid", "cinvoicecustid", 
        "corigcurrencyid", "cqtunitid", 
        "creceieveid", "creceiveareaid", 
        "creceivepointid", "csignwastbid", 
        "csignwasthid", "csignwasttype", 
        "csourcematerialoid", "csourcewastbid", 
        "csourcewasthid", "csourcewasttranstype", 
        "csourcewasttype", "csourcebilldate", 
        "cfirstbilldate", "ddeliverdate", 
        "nchangestdrate", "cglobalcurrencyid", 
        "nglobalexchgrate", "nglobalmny", 
        "nglobaltaxmny", "cgroupcurrencyid", 
        "ngroupexchgrate", "nnetprice", 
        "norignetprice", "norigprice", 
        "norigtaxnetprice", "norigtaxprice", 
        "nprice", "nqtnetprice", 
        "nqtorignetprice", "nqtorigprice", 
        "nqtorigtaxnetprc", "nqtorigtaxnetprice", 
        "nqtorigtaxprice", "nqtprice", 
        "nqttaxnetprice", "nqttaxprice", 
        "ntaxnetprice", "ntaxprice", 
        "ntaxrate", "vqtunitrate", 
        "vreceiveaddress", "vsourcewastcode", 
        "vsourcewastrowno", "ccustmaterialid", 
        "ctaxcodeid" };
      

      return (String[])CollectionUtils.combineArrs(new String[][] { synsrcFields, otherarrs, otherExtArrs });
    }
    if (getGenEditorModel().getICBillType().isEqual(ICBillType.TransOut.getCode()))
    {
      otherarrs = 
        new String[] {
        "csignwastbid", "csignwasthid", 
        "csignwasttype", "vsignwastcode", 
        "vsignwastrowno", "csourcewastbid", 
        "csourcewasthid", "csourcewasttranstype", 
        "csourcewasttype", "vsourcewastcode", 
        "vsourcewastrowno" };
      

      return (String[])CollectionUtils.combineArrs(new String[][] { synsrcFields, otherarrs });
    }
    return synsrcFields;
  }
  
  private void synSrcBillInfo(int orirow, int destrow, String[] fields, Set<String> exceptFields)
  {
    IBizObject oribody = getCardBodyVO(orirow);
    IBizObject destbody = getCardBodyVO(destrow);
    for (String field : fields) {
      if ((oribody.getAttributeValue(field) != null) && 
        (!exceptFields.contains(field))) {
        destbody.setAttributeValue(field, oribody.getAttributeValue(field));
      }
    }
    otherAfterSrcInfoProcess(oribody, destbody);
  }
  
  private void otherAfterSrcInfoProcess(IBizObject oribody, IBizObject destbody)
  {
    if (NCBaseTypeUtils.isNull(destbody.getAttributeValue("castunitid"))) {
      destbody.setAttributeValue("castunitid", 
        oribody.getAttributeValue("castunitid"));
    }
    if (NCBaseTypeUtils.isNull(destbody.getAttributeValue("vchangerate"))) {
      destbody.setAttributeValue("vchangerate", 
        oribody.getAttributeValue("vchangerate"));
    }
  }
  
  protected void clearBatchInfo(int row)
  {
    getEditorModel().getCardPanelWrapper().setBodyValueAt(null, null, row, "vbatchcode");
    Set<String> billFields = 
      new ICBatchFields().getBilltoBatchFields().keySet();
    
    billFields.remove("pk_group");
    billFields.remove("cmaterialoid");
    billFields.remove("cmaterialvid");
    String[] clearColKeys = (String[])billFields.toArray(new String[billFields.size()]);
    
    getEditorModel().getCardPanelWrapper().clearRowData(row, clearColKeys);
  }
  
  protected BatchDlgParam getBatchDlgParam(int row)
  {
    BatchDlgParam param = new BatchDlgParam();
    List<String> headDims = new ArrayList();
    headDims.add("pk_org");
    headDims.add("cwarehouseid");
    for (String key : headDims)
    {
      String value = 
        getEditorModel().getCardPanelWrapper().getHeadRefID(key);
      if ((key.equals("cwarehouseid")) && (
        (value == null) || ("".equals(value)))) {
        value = 
          (String)getEditorModel().getCardPanelWrapper()
          .getBodyValueAt(row, "cbodywarehouseid");
      }
      param.getOnhandDim().setAttributeValue(key, value);
    }
    IBizObject bodyVO = getCardBodyVO(row);
    param.setLoginContext(getEditorModel().getContext().getLoginContext());
    
    String[] bodyDims = 
      removeDims(OnhandDimVO.getDimContentFields(), headDims);
    for (String dim : bodyDims)
    {
      Object dimValue = bodyVO.getAttributeValue(dim);
      param.getOnhandDim().setAttributeValue(dim, dimValue);
    }
    param.getOnhandDim().setAttributeValue("pk_group", getEditorModel().getContext().getLoginContext().getPk_group());
    param.setLoginContext(getEditorModel().getContext().getLoginContext());
    

    OnhandDimVO onhandDim = param.getOnhandDim();
    OnhandDlgGeneralHeadVO headVO = new OnhandDlgGeneralHeadVO();
    DataViewMeta dataViewMeta = new DataViewMeta(onhandDim.getClass());
    headVO.setDataViewMeta(dataViewMeta);
    headVO.setVO(onhandDim);
    headVO.setCrowno(
      (String)bodyVO.getAttributeValue("crowno"));
    headVO.setCunitid(
      (String)bodyVO.getAttributeValue("cunitid"));
    processNumForBatchDlg(param, row, bodyVO, headVO);
    param.setHeadVO(headVO);
    param.setIsNewBatchRef(UFBoolean.TRUE);
    

    return param;
  }
  
  protected int setFulfiltype(OnhandDlgGeneralHeadVO headVO)
  {
    UFDouble onhandshouldnum = NCBaseTypeUtils.abs(headVO.getOnhandshouldnum());
    UFDouble onhandcurrentnum = NCBaseTypeUtils.abs(headVO.getOnhandcurrentnum());
    int fulfiltype = 0;
    if (NCBaseTypeUtils.isNullOrZero(onhandcurrentnum)) {
      fulfiltype = 0;
    } else if (NCBaseTypeUtils.isGt(onhandshouldnum, onhandcurrentnum)) {
      fulfiltype = 1;
    } else if (NCBaseTypeUtils.isEquals(onhandshouldnum, onhandcurrentnum)) {
      fulfiltype = 2;
    } else {
      fulfiltype = 3;
    }
    return fulfiltype;
  }
  
  protected BatchRefPane getBatchRefPane()
  {
    BatchRefPane refPane = 
      ((ICBizView)getEditorModel().getICBizView()).getBatchRefPane();
    if ((refPane != null) && (refPane.getContext() == null)) {
      refPane.setContext(getEditorModel().getContext());
    }
    return refPane;
  }
  
  private void processNumForBatchDlg(BatchDlgParam param, int row, IBizObject bodyVO, OnhandDlgGeneralHeadVO headVO)
  {
    ICBillType billtype = getEditorModel().getICBillType();
    if (ICBillType.Invcount.equals(billtype)) {
      return;
    }
    if (isNeedSpecialProcess())
    {
      headVO.setOnhandshouldnum(
        (UFDouble)bodyVO.getAttributeValue("nnum"));
      headVO.setOnhandshouldassnum(
        (UFDouble)bodyVO.getAttributeValue("nassistnum"));
      headVO.setOnhandcurrentnum(null);
      headVO.setFulfiltype(Integer.valueOf(setFulfiltype(headVO)));
      return;
    }
    headVO.setOnhandshouldnum(
      (UFDouble)bodyVO.getAttributeValue("nshouldnum"));
    headVO.setOnhandshouldassnum(
      (UFDouble)bodyVO.getAttributeValue("nshouldassistnum"));
    headVO.setOnhandcurrentnum(
      (UFDouble)bodyVO.getAttributeValue("nnum"));
    headVO.setFulfiltype(Integer.valueOf(setFulfiltype(headVO)));
  }
  
  private boolean isNeedSpecialProcess()
  {
    ICBillType billtype = getEditorModel().getICBillType();
    if (this.specialList.contains(billtype)) {
      return true;
    }
    return false;
  }
  
  protected IBizObject getCardBodyVO(int row)
  {
    return 
      ((ICGenBizEditorModel)getEditorModel()).getICUIBillEntity().getBody(row);
  }
  
  protected void handleBatchByHand(String inputStr, int row, BatchDlgParam queryParam)
  {
    BatchRefViewVO[] refVOs = null;
    if (!StringUtil.isSEmptyOrNull(inputStr))
    {
      queryParam.setVbatchcode(inputStr);
      try
      {
        refVOs = 
        
          ((IBatchRefQuery)NCLocator.getInstance().lookup(IBatchRefQuery.class)).queryBatchNum(queryParam);
      }
      catch (BusinessException e)
      {
        ExceptionUtils.wrappException(e);
      }
    }
    if ((refVOs == null) || (refVOs.length == 0))
    {
      clearBatchInfo(row);
      getEditorModel().getCardPanelWrapper()
        .setBodyValueAt(inputStr, row, "vbatchcode");
      return;
    }
    BatchRefViewVO refVO = refVOs[0];
    if (StringUtil.isStringEqual(refVO.getAttributeValue("bseal").toString(), UFBoolean.TRUE.toString()))
    {
      clearBatchInfo(row);
      return;
    }
    IBizObject bodyVO = getCardBodyVO(row);
    synBatch(refVO, bodyVO);
  }
  
  protected String[] removeDims(String[] orgDims, List<String> removeKeys)
  {
    List<String> retDims = new ArrayList();
    for (int i = 0; i < orgDims.length; i++) {
      if (!removeKeys.contains(orgDims[i])) {
        retDims.add(orgDims[i]);
      }
    }
    return (String[])retDims.toArray(new String[retDims.size()]);
  }
  
  protected void synBatch(BatchRefViewVO batchcode, IBizObject body)
  {
    ICNewBatchFields batchandBill = new ICNewBatchFields();
    BatchSynchronizer syn = new BatchSynchronizer(batchandBill);
    syn.synBatchReftoBill(batchcode, body);
    

    synDimInfo(batchcode, body);
    
    synNnumInfo(batchcode, body);
  }
  
  private void synNnumInfo(BatchRefViewVO refVO, IBizObject bodyVO)
  {
    String[] fields = OnhandDimVO.getDimContentFields();
    for (String field : fields) {
      if ((refVO.getAttributeValue(field) != null) && (!StringUtil.isNullStringOrNull(refVO.getAttributeValue(field).toString()))) {
        bodyVO.setAttributeValue(field, refVO.getAttributeValue(field));
      }
    }
    if (!getBatchRefPane().isClicked()) {
      return;
    }
    if (!isFillNum()) {
      return;
    }
    UFDouble nnum = (UFDouble)refVO.getAttributeValue("nnum");
    if (NCBaseTypeUtils.isNullOrZero(nnum)) {
      return;
    }
    UFDouble nassistnum = null;
    String vchangerate = (String)refVO.getAttributeValue("vchangerate");
    if (!StringUtil.isNullStringOrNull(vchangerate))
    {
      nassistnum = (UFDouble)refVO.getAttributeValue("nassistnum");
      bodyVO.setAttributeValue("vchangerate", vchangerate);
    }
    else
    {
      nassistnum = 
      
        HslParseUtil.hslDivUFDouble((String)bodyVO
        .getAttributeValue("vchangerate"), nnum);
    }
    bodyVO.setAttributeValue("nnum", nnum);
    bodyVO.setAttributeValue("nassistnum", nassistnum);
  }
  
  protected boolean isFillNum()
  {
    return true;
  }
  
  private void synDimInfo(BatchRefViewVO refVO, IBizObject bodyVO)
  {
    synBizData(
      (CircularlyAccessibleValueObject)refVO.getVO(OnhandDimVO.class), 
      bodyVO, getSrctoTargetDimMap());
  }
  
  private Map<String, String> getSrctoTargetDimMap()
  {
    Map<String, String> map = new HashMap();
    map.put("cstateid", "cstateid");
    map.put("cvendorid", "cvendorid");
    map.put("cprojectid", "cprojectid");
    map.put("cproductorid", "cproductorid");
    map.put("casscustid", "casscustid");
    map.put("vfree1", "vfree1");
    map.put("vfree2", "vfree2");
    map.put("vfree3", "vfree3");
    map.put("vfree4", "vfree4");
    map.put("vfree5", "vfree5");
    map.put("vfree6", "vfree6");
    map.put("vfree7", "vfree7");
    map.put("vfree8", "vfree8");
    map.put("vfree9", "vfree9");
    map.put("vfree10", "vfree10");
    
    map.put("cvmivenderid", "cvmivenderid");
    
    map.put("ctplcustomerid", "ctplcustomerid");
    return map;
  }
  
  private void synBizData(CircularlyAccessibleValueObject srcObject, IBizObject targetObject, Map<String, String> srctoTargetFields)
  {
    if ((srcObject == null) || (targetObject == null) || (srctoTargetFields == null)) {
      return;
    }
    for (Map.Entry<String, String> srctoTarget : srctoTargetFields.entrySet())
    {
      Object srcValue = srcObject.getAttributeValue((String)srctoTarget.getKey());
      if (srcValue != null) {
        targetObject.setAttributeValue((String)srctoTarget.getValue(), srcValue);
      }
    }
  }
  
  public int getRefrow()
  {
    return this.refrow;
  }
  
  public void setRefrow(int refrow)
  {
    this.refrow = refrow;
  }
  
  public void setBeanFactory(BeanFactory bf)
    throws BeansException
  {
    UIF2BeanFactory ufbf = (UIF2BeanFactory)bf;
    List<String> beans = Arrays.asList(ufbf.getBeanDefinitionNames());
    for (String beanName : beans) {
      if (beanName.contains("batch"))
      {
        Object bean = ufbf.getBean(beanName);
        if (bean.getClass().isAssignableFrom(BatchCode.class))
        {
          this.medBacthAfter = ((BatchCode)bean);
          break;
        }
      }
    }
    if (this.medBacthAfter == null) {
      this.medBacthAfter = new BatchCode();
    }
  }
}
