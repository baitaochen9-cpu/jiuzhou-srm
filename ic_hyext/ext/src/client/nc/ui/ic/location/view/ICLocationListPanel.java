package nc.ui.ic.location.view;

import java.util.HashMap;
import java.util.Map;

import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableColumn;

import nc.itf.scmpub.reference.uap.group.SysInitGroupQuery;
import nc.itf.uap.busibean.SysinitAccessor;
import nc.ui.ic.general.model.ICGenBizEditorModel;
import nc.ui.ic.general.util.GenUIUtil;
import nc.ui.ic.location.deal.ICLocationUIPrecision;
import nc.ui.ic.location.handler.LocationRefInitListener;
import nc.ui.ic.location.ref.LocationRefPane;
import nc.ui.ic.location.ref.LocationRefType;
import nc.ui.ic.location.util.LocationMaterialFreeUtil;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillCellEditor;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillItemUISet;
import nc.ui.pub.bill.BillListData;
import nc.ui.pub.bill.BillListPanel;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.bill.BillUIUtil;
import nc.ui.pub.bill.IBillItem;
import nc.vo.ic.general.define.ICBillBodyVO;
import nc.vo.ic.general.define.InOutFlag;
import nc.vo.ic.general.util.ICLocationUtil;
import nc.vo.ic.general.util.InOutHelp;
import nc.vo.ic.location.ICLocationVO;
import nc.vo.ic.onhand.entity.OnhandDimVO;
import nc.vo.ic.onhand.entity.OnhandSNViewVO;
import nc.vo.ic.pub.define.ICBillTableInfo;
import nc.vo.ic.pub.define.ICPubMetaNameConst;
import nc.vo.ic.pub.util.StringUtil;
import nc.vo.ic.pub.util.ValueCheckUtil;
import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.bill.BillTempletVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.scmpub.res.billtype.ICBillType;
import nc.vo.uif2.LoginContext;

import org.apache.commons.lang.ArrayUtils;

/**
 * <p>
 * <b>单品列表界面：</b>
 * <ul>
 * <li>主要完成单品的编辑
 * </ul>
 * <p>
 * <p>
 * 
 * @version 6.0
 * @since 6.0
 * @author liuzy
 * @time 2010-3-12 下午02:14:44
 */
public class ICLocationListPanel extends BillListPanel {
  private ICLocationUIPrecision pricision;

  private static String bodyRowInnerCode = "rowInnerCode";

  private static String detailEditData = "detailEditData";

  private static String rowAttributeData = "rowAttributeData";

  private static final long serialVersionUID = 1L;

  private LocationRefPane barRefPanel;

  private ICBillType billTypeEnum;

  private ICGenBizEditorModel editorModel;

  private ICLocationEnum locationEnum;

  private LocationRefPane serialRefPanel;

  public ICLocationListPanel(
      ICGenBizEditorModel editorModel, ICLocationEnum locationEnum) {
    super();
    this.locationEnum = locationEnum;
    this.editorModel = editorModel;
    this.initUI();
  }

  /**
   * 缓存表体行单品数据
   */
  public void cacheLocationVOs(int oldRow) {
    if (oldRow >= 0) {
      // 获取单品界面表体的单品VO数组
      ICLocationVO[] oldDetailEditVOs = this.getBodyLocationVOs();
      this.setDetailEditData(oldRow, oldDetailEditVOs);
    }
  }

  /**
   * 获取条码编辑参照
   */
  public LocationRefPane getBarcodeRefPane() {
    if (this.barRefPanel == null) {
      this.barRefPanel =
          new LocationRefPane(this, this.editorModel.getContext(),
              LocationRefType.Barcode, this.locationEnum);
    }
    return this.barRefPanel;
  }

  /**
   * 获取当前处理的单品VOs
   */
  public ICLocationVO[] getCurDetailVOs() {
    ICLocationVO[] locationVOs = null;
    if (this.editorModel.getICBizModel().isEditable()) {
      locationVOs = this.getBodyLocationVOs();
    }
    else {
      int selectHeadRow = this.getHeadTable().getSelectedRow();
      if (selectHeadRow < 0) {
        return null;
      }
      locationVOs = this.getDetailEditData(selectHeadRow);
    }
    return locationVOs;
  }

  public ICBillType getICBillType() {
    return this.billTypeEnum;
  }

  public ICLocationEnum getLocationEnum() {
    return this.locationEnum;
  }

  /**
   * 获得表头单据表体信息
   * 
   * @return
   */
  public ICBillBodyVO[] getHeadValueData() {
    String billtype = this.getICBillType().getCode();
    ICBillTableInfo info =
        ICBillTableInfo.getICBillTableInfo(ICBillType.getICBillType(billtype));
    ICBillBodyVO[] bodys =
        (ICBillBodyVO[]) this.getHeadBillModel().getBodyValueVOs(
            info.getBodyVoName());
    return bodys;
  }

  /**
   * 方法功能描述：获取处理后的VO
   * <p>
   * <b>参数说明</b>
   * 
   * @return Map<Integer, ICBillBodyVO> key -- 原始单据表体的行位置
   *         <p>
   * @since 6.0
   * @author yangb
   * @time 2010-8-27 上午12:26:03
   */
  public Map<Integer, ICLocationVO[]> getResultLocationVOs() {
    GenUIUtil.stopEdit(this.getBodyTable());
    // 先缓存当前表头选择行对应的货位数据
    int selrow = this.getHeadTable().getSelectedRow();
    this.cacheLocationVOs(selrow);
    Map<Integer, ICLocationVO[]> mapvos =
        new HashMap<Integer, ICLocationVO[]>();
    Integer index = null;
    ICLocationVO[] locvos = null;
    for (int row = 0, length = this.getHeadBillModel().getRowCount(); row < length; row++) {
      index = this.getBodyRowInnerCode(row);
      locvos = this.getDetailEditData(row);
      mapvos.put(index, locvos);
    }
    return mapvos;
  }

  /**
   * 获取序列号编辑参照
   */
  public LocationRefPane getSerialRefPane() {
    if (this.serialRefPanel == null) {
      this.serialRefPanel =
          new LocationRefPane(this, this.editorModel.getContext(),
              LocationRefType.Serial, this.locationEnum);
    }
    return this.serialRefPanel;
  }

  /**
   * 方法功能描述：设置表体数据到界面
   * <p>
   * <b>参数说明</b>
   * 
   * @param bodyvos
   *          单据表体
   * @param originindexs
   *          单据表体原始的顺序
   *          <p>
   * @since 6.0
   * @author yangb
   * @time 2010-8-26 下午10:42:57
   */
  public void loadBillBodyData(ICBillBodyVO[] bodyvos, Integer[] originindexs) {
    this.clearListPanelData();
    if (ValueCheckUtil.isNullORZeroLength(bodyvos)) {
      return;
    }
    this.getBillListData().getHeadBillModel().setSortColumn(new String[0]);
    this.setListPanelHeadData(bodyvos);

    for (int i = 0; i < bodyvos.length; i++) {
      ICLocationVO[] locVOs =
          this.editorModel.getBodyEditDetailData(originindexs[i].intValue());
      if(ArrayUtils.isEmpty(locVOs)){
        locVOs=bodyvos[i].getLocationVOs();
      }
      this.processCsnunitid(bodyvos[i], locVOs);
      this.setDetailEditData(i, locVOs);
      this.setBodyRowInnerCode(i, originindexs[i]);
    }
    // 默认选择表头第 0 行
    this.getHeadTable().setRowSelectionInterval(0, 0);
    this.setSelectedLocationVOs(0);
  }

  private void processCsnunitid(ICBillBodyVO bodyvo,
		ICLocationVO[] locVOs) {
	if (ValueCheckUtil.isNullORZeroLength(locVOs)) {
		return;
	}
	String csnunitid = bodyvo.getCSnunitid();
      for (ICLocationVO locvo : locVOs) {
    	  locvo.setAttributeValue(ICPubMetaNameConst.CSNUNITID, csnunitid);
      }
  }

  public void setICBillType(ICBillType icBillType) {
    this.billTypeEnum = icBillType;
  }

  public void setLocationEnum(ICLocationEnum locationEnum) {
    this.locationEnum = locationEnum;
  }

  /**
   * 设置列表界面表体数据
   */
  public void setSelectedLocationVOs(int selectedRow) {
    SuperVO[] newDetailEditVOs = this.getDetailEditData(selectedRow);
    this.setBodyValueVO(newDetailEditVOs);
    this.getBodyBillModel().loadLoadRelationItemValue();
  }

  /**
   * 设置序列号编辑器
   */
  public void setSerialCodeEditor(boolean isout, int bodyrow) {

    BillItem item = this.getBodyItem(ICLocationVO.VSERIALCODE);
    if (item == null || !item.isShow()) {
      return;
    }
    TableColumn tc = this.getBodyTable().getColumn(item.getName());
    if (tc == null) {
      return;
    }
    if (isout) {
	  String selectedSN = (String) this.getBodyBillModel().getValueAt(bodyrow,
          ICLocationVO.VSERIALCODE);
	  this.getSerialRefPane().setbBodySerial(false);
      this.getSerialRefPane().clearResultVOs();
      OnhandSNViewVO snvo = this.getParam(bodyrow);
      this.getSerialRefPane().setParam(snvo);
	  this.getSerialRefPane().setInitListener(
			new LocationRefInitListener(StringUtil
					.isNullStringOrNull(selectedSN) ? null
					: new String[] { selectedSN }));
      this.getSerialRefPane().getUITextField().setEditable(false);
      tc.setCellEditor(new BillCellEditor(this.getSerialRefPane()));
    }
    else {
      tc.setCellEditor(new BillCellEditor(new JTextField()));
    }
    

  }

  /**
   * 清除所有表体行属性数据
   */
  private void clearBodyRowAttributeData() {
    Map<String, Object> attrdata = null;
    for (int i = 0, loop = super.getHeadBillModel().getRowCount(); i < loop; i++) {
      attrdata = this.getRowAttributeData(i);
      if (attrdata != null) {
        attrdata.clear();
      }
    }
  }

  public ICLocationVO[] getBodyLocationVOs() {
    return (ICLocationVO[]) this.getBodyBillModel().getBodyValueVOs(
        ICLocationUtil.getLocVONameByBillType(this.getICBillType()));
  }

  /**
   * 获取表体内部行号
   */
  private Integer getBodyRowInnerCode(int row) {
    Map<String, Object> attrdata = this.getRowAttributeData(row);
    if (attrdata == null) {
      return null;
    }
    return (Integer) attrdata.get(ICLocationListPanel.bodyRowInnerCode);
  }
  
  public String getCurPkorg(){
    return (String) this.editorModel
        .getCurSelHeadValue(OnhandDimVO.PK_ORG);
  }

  /**
   * 
   */
  public OnhandSNViewVO getParam(int bodyrow) {
    int headrow = this.getHeadTable().getSelectedRow();
    if (headrow < 0) {
      return null;
    }
    BillModel headmodel = this.getHeadBillModel();
    OnhandSNViewVO snvo = new OnhandSNViewVO();
    BillItem bi = null;
    for (String key : OnhandDimVO.getDimContentFields()) {
      bi = headmodel.getItemByKey(key);
      System.out.println(key);
      if (bi == null) {
        continue;
      }
      if (bi.getDataType() == IBillItem.UFREF) {
        snvo.setAttributeValue(key,
            headmodel.getValueAt(headrow, key + IBillItem.ID_SUFFIX));
      }
      else {
        snvo.setAttributeValue(key, headmodel.getValueAt(headrow, key));
      }
    }
    snvo.setPk_group(this.editorModel.getContext().getPk_group());
    snvo.setPk_org((String) this.editorModel
        .getCurSelHeadValue(OnhandDimVO.PK_ORG));
    snvo.setCwarehouseid((String) this.editorModel
        .getCurSelHeadValue(OnhandDimVO.CWAREHOUSEID));
    String type=headmodel.getValueAt(headrow, "cbodytranstypecode")!=null?headmodel.getValueAt(headrow, "cbodytranstypecode").toString():"" ;
    if(type.startsWith("4D")){
    	String pk_org= headmodel.getValueAt(headrow, "pk_org").toString();
    	try {
			UFBoolean yf631 = SysinitAccessor.getInstance().getParaBoolean(pk_org, "YF631");
			if(yf631.booleanValue()){
				
				 this.getSerialRefPane().setMaterialOut(true);
			}
			
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }
   

    snvo.setCmaterialoid((String) headmodel.getValueAt(headrow,
        OnhandDimVO.CMATERIALOID + IBillItem.ID_SUFFIX));
    snvo.setCmaterialvid((String) headmodel.getValueAt(headrow,
        OnhandDimVO.CMATERIALVID + IBillItem.ID_SUFFIX));

    if (bodyrow >= 0) {
      snvo.setClocationid((String) this.getBodyBillModel().getValueAt(bodyrow,
          OnhandDimVO.CLOCATIONID + IBillItem.ID_SUFFIX));
//      snvo.setVbarcode((String) this.getBodyBillModel().getValueAt(bodyrow,
//          OnhandSNVO.VBARCODE));
//      snvo.setVbarcodesub((String) this.getBodyBillModel().getValueAt(bodyrow,
//          OnhandSNVO.VBARCODESUB));
//      snvo.setVsncode((String) this.getBodyBillModel().getValueAt(bodyrow,
//          OnhandSNVO.VSNCODE));
//      snvo.setPk_serialcode((String) this.getBodyBillModel().getValueAt(bodyrow,
//    		  ICPubMetaNameConst.PK_SERIALCODE));
    }

    return snvo;
  }

  @SuppressWarnings("unchecked")
  private Map<String, Object> getRowAttributeData(int row) {
    if (row < 0 || row >= this.getHeadBillModel().getRowCount()) {
      return null;
    }

    Map<String, Object> attrdata =
        (Map<String, Object>) this.getHeadBillModel().getRowAttributeObject(
            row, ICLocationListPanel.rowAttributeData);
    if (attrdata == null) {
      attrdata = new HashMap<String, Object>();
      this.getHeadBillModel().addRowAttributeObject(row,
          ICLocationListPanel.rowAttributeData, attrdata);
    }

    return attrdata;
  }

  private void initUI() {
    this.setICBillType(this.editorModel.getICBizModel().getBillTypeEnum());
    this.loadBillTemplet();
    this.getBodyTable().setSelectionMode(
        ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    this.getPricision().setListPrecision(
        this.editorModel.getContext().getPk_group(), this);
    this.initUIProp();
    this.initRef();
  }

  /**
   * 初始化界面属性
   * 包括屏蔽右键菜单
   * 键盘自动增行
   */
  private void initUIProp() {
    //孙表界面不允许右键弹出菜单
    this.getParentListPanel().setBBodyMenuShow(false);
    this.getChildListPanel().setBBodyMenuShow(false);
    this.getParentListPanel().setAutoAddLine(false);
    this.getChildListPanel().setAutoAddLine(false);
  }

  private void initRef() {
    if (this.getLocationEnum() == ICLocationEnum.Location) {
      return;
    }
    BillItem[] billItems = this.getBillListData().getBodyItems();
    LoginContext lc = this.editorModel.getContext().getLoginContext();
    if (null == billItems) {
      return;
    }
    for (BillItem billItem : billItems) {
      if (billItem.getDataType() == IBillItem.UFREF) {
        UIRefPane refPanel = (UIRefPane) billItem.getComponent();
        if (refPanel.getRefModel() != null) {
          refPanel.getRefModel().setPk_user(lc.getPk_loginUser());
          refPanel.getRefModel().setPk_group(lc.getPk_group());
          refPanel.getRefModel().setPk_org(lc.getPk_org());
        }
      }
    }
  }

  private void loadBillTemplet() {
    BillTempletVO billTempletVO =
        BillUIUtil.getDefaultTempletStatic("4ALoc", null, null, null, null,
            null);
    BillListData billListData = new BillListData(billTempletVO);
    
    if (InOutFlag.Out.equals(InOutHelp.getInOutFlagByBillType(this
        .getICBillType()))) {  
      billListData.getHeadItem(ICPubMetaNameConst.NSHOULDASSISTNUM).setName(
          NCLangRes4VoTransl.getNCLangRes().getStrByID("4008001_0",
              "04008001-0794")/*应发数量*/);
      billListData.getHeadItem(ICPubMetaNameConst.NSHOULDNUM).setName(
          NCLangRes4VoTransl.getNCLangRes().getStrByID("4008001_0",
              "04008001-0795")/*应发主数量*/);
      billListData.getHeadItem(ICPubMetaNameConst.NASSISTNUM).setName(
          NCLangRes4VoTransl.getNCLangRes().getStrByID("4008001_0",
              "04008001-0796")/*实发数量*/);
      billListData.getHeadItem(ICPubMetaNameConst.NNUM).setName(
          NCLangRes4VoTransl.getNCLangRes().getStrByID("4008001_0",
              "04008001-0797")/*实发主数量*/);
    }
    
    billListData.getBodyItem(ICLocationVO.VBARCODE).setEdit(false);
    BillItemUISet ui=new BillItemUISet();
    if (this.getLocationEnum() == ICLocationEnum.Location) {
      this.setSnInfoEdit(billListData);
      this.setSnInfoShow(billListData);
      billListData.getBodyItem(ICLocationVO.VSERIALCODE).setUiSet(ui);
	  billListData.getBodyItem(ICPubMetaNameConst.CSNUNITID).setShow(false);
      billListData.getBodyItem(ICLocationVO.VBARCODE).setUiSet(ui);
      billListData.getBodyItem(ICLocationVO.VSERIALCODE).setShow(false);
      billListData.getBodyItem(ICLocationVO.VBARCODE).setShow(false);
    }
    else if (this.getLocationEnum() == ICLocationEnum.Serial) {
      if (!SysInitGroupQuery.isSNEnabled()) {
        this.setSnInfoEdit(billListData);
        this.setSnInfoShow(billListData);
      }
      billListData.getBodyItem(ICLocationVO.CLOCATIONID).setUiSet(ui);
      billListData.getBodyItem(ICLocationVO.CLOCATIONID).setShow(false);
    }else{
      if (!SysInitGroupQuery.isSNEnabled()) {
        this.setSnInfoEdit(billListData);
        this.setSnInfoShow(billListData);
      }
    }
    // 处理序列号自定义项
    new LocationMaterialFreeUtil(this.editorModel)
    .processMaterialFree(billListData);
    
    this.setListData(billListData);

  }
  
  private void setSnInfoEdit(BillListData billListData) {
    
    for (int i = 1; i <= ICPubMetaNameConst.SNDEFNUM; i++) {
      billListData
          .getBodyItem(ICPubMetaNameConst.VSNDEFHEAD_IC + i).setEdit(false);
    }
    billListData
        .getBodyItem(ICPubMetaNameConst.CSNQUALITYLEVELID).setEdit(false);
  }

	private void setSnInfoShow(BillListData billListData) {
		if (!SysInitGroupQuery.isSNEnabled()) {
			return;
		}
		for (int i = 1; i <= ICPubMetaNameConst.SNDEFNUM; i++) {
			billListData.getBodyItem(ICPubMetaNameConst.VSNDEFHEAD_IC + i)
					.setShow(false);
		}
		billListData.getBodyItem(ICPubMetaNameConst.CSNQUALITYLEVELID).setShow(
				false);
	}
  
  /**
   * 设置表体内部行号
   */
  private void setBodyRowInnerCode(int row, Integer rowInnerCode) {
    Map<String, Object> attrdata = this.getRowAttributeData(row);
    if (attrdata == null) {
      return;
    }
    attrdata.put(ICLocationListPanel.bodyRowInnerCode, rowInnerCode);
  }

  /**
   * 设置表体货位编辑VO数组到行属性上
   */
  public void setDetailEditData(int row, ICLocationVO[] data) {
    Map<String, Object> attrdata = this.getRowAttributeData(row);
    if (attrdata == null) {
      return;
    }
    attrdata.put(ICLocationListPanel.detailEditData, data);
  }

  private void setListPanelHeadData(SuperVO[] bodyVOs) {
    this.setHeaderValueVO(bodyVOs);
    this.getHeadBillModel().loadLoadRelationItemValue();
  }

  protected void clearListPanelData() {
    this.clearBodyRowAttributeData();
    this.setHeaderValueVO(null);
    this.setBodyValueVO(null);
  }

  /**
   * 获取行属性上的表体货位编辑VO数组
   */
  public ICLocationVO[] getDetailEditData(int row) {
    Map<String, Object> attrdata = this.getRowAttributeData(row);
    if (attrdata == null) {
      return null;
    }
    return (ICLocationVO[]) attrdata.get(ICLocationListPanel.detailEditData);
  }

  public void setPricision(ICLocationUIPrecision pricision) {
    this.pricision = pricision;
  }

  public ICLocationUIPrecision getPricision() {
    if (this.pricision == null) {
      this.pricision = new ICLocationUIPrecision();
    }
    return this.pricision;
  }

}
