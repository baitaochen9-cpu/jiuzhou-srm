/**
 * $文件说明$
 * 
 * @author zhaoyha
 * @version
 * @see
 * @since
 * @time 2009-6-29 下午03:24:34
 */
package nc.ui.pu.m25.view;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import nc.itf.org.IOrgConst;
import nc.itf.scmpub.reference.uap.org.OrgUnitPubService;
import nc.ui.ic.batchcode.ref.BatchRefPane;
import nc.ui.pu.m25.editor.TotalValueHanlder;
import nc.ui.pu.m25.model.InvoiceBillManageModel;
import nc.ui.pu.m25.rule.PurchaseOrgEditableHandler;
import nc.ui.pu.pub.editor.CardEditorHelper;
import nc.ui.pu.pub.util.SupplierFreeCustInfoUtil;
import nc.ui.pu.pub.view.PUBillForm;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillItem;
import nc.ui.pubapp.uif2app.view.BillOrgPanel;
import nc.ui.scmpub.ref.FilterDeptRefUtils;
import nc.ui.scmpub.ref.FilterMaterialRefUtils;
import nc.ui.scmpub.ref.FilterPaytermRefUtils;
import nc.ui.scmpub.ref.FilterProjectRefUtils;
import nc.ui.scmpub.ref.FilterPsndocRefUtils;
import nc.ui.scmpub.ref.FilterSupplierRefUtils;
import nc.ui.scmpub.ref.FilterWareHouseRefUtils;
import nc.ui.scmpub.util.BillCardPanelUtils;
import nc.ui.uif2.UIState;
import nc.vo.bc.pub.util.SysParaInitQuery;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pu.m25.entity.InvoiceHeaderVO;
import nc.vo.pu.m25.entity.InvoiceItemVO;
import nc.vo.pu.m25.entity.InvoiceVO;
import nc.vo.pu.m25.enumeration.InvoicePuImportEnum;
import nc.vo.pu.m25.pub.InvoiceTypeSetter;
import nc.vo.pu.pub.util.PubSysParamUtil;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.pattern.pub.MathTool;
import nc.vo.scmpub.res.para.NCPara;

import org.apache.commons.lang.StringUtils;

/**
 * <p>
 * <b>本类主要完成以下功能：</b>
 * <ul>
 * <li>采购发票卡片编辑器
 * <li>功能条目2
 * <li>...
 * </ul>
 * <p>
 * <b>变更历史（可选）：</b>
 * <p>
 * XXX版本增加XXX的支持。
 * <p>
 * <p>
 * 
 * @version 6.0
 * @since 6.0
 * @author zhaoyha
 * @time 2009-6-29 下午03:24:34
 */
@SuppressWarnings("serial")
public class InvoiceBillForm extends PUBillForm {

  private BillOrgPanel billOrgPanel;

  /**
   * 整单合计信息处理器,是为了即时显示之用; 如果有效率问题或需求认可,完全可以做到保存时再计算
   **/
  private TotalValueHanlder totalValueHandler;

  @Override
  public BillOrgPanel getBillOrgPanel() {
    if (null == this.billOrgPanel && this.isShowOrgPanel()) {
      this.billOrgPanel = this.createDefaultBillOrgPanel();
      this.billOrgPanel.setClearpkorgWhenAdd(true);
    }
    return this.billOrgPanel;
  }

  /**
   * @return totalValueHandler
   */
  public TotalValueHanlder getTotalValueHandler() {
    return this.totalValueHandler;
  }

  /**
   * @param totalValueHandler 要设置的 totalValueHandler
   */
  public void setTotalValueHandler(TotalValueHanlder totalValueHandler) {
    this.totalValueHandler = totalValueHandler;
  }

  /**
   * 父类方法重写
   * 
   * @see nc.ui.uif2.editor.BillForm#setValue(java.lang.Object)
   */
  @Override
  public void setValue(Object object) {
    // 先初始化参照,再将VO设置到界面,再执行其它处理
    String freecustid = "";
    if (object != null) {
      freecustid = ((InvoiceVO) object).getParentVO().getPk_freecust();
    }
    boolean isFreeCust = StringUtils.isEmpty(freecustid) ? false : true;
    SupplierFreeCustInfoUtil custBankUtil = new SupplierFreeCustInfoUtil();
    custBankUtil.initCustBankRefPanel(this.getBillCardPanel(), isFreeCust);
   
    
    super.setValue(object);
    custBankUtil.initCustBankValue(this.getBillCardPanel(), isFreeCust);
    // 合计信息的精度设置
    // if(null!=getTotalValueHandler()) getTotalValueHandler().reCalAll();
    // 根据VO设置模板的可编辑性
    this.handleItemsEnable((InvoiceVO) object);
  }

  @SuppressWarnings("restriction")
@Override
  public void showMeUp() {
    super.showMeUp();
    /***
     * 20210810 JZ_zhian.ye 调整设置界面之前，检查汇率，参数PUYE001 ,不管什么情况下，发票录入都采用当月的汇率进行处理。
     */
   CardEditorHelper helper = new CardEditorHelper(this.getBillCardPanel());
   nc.ui.pu.m25.editor.utils.ExchangeRateUtil.setExchangerate(helper);
  }

  private BillOrgPanel createDefaultBillOrgPanel() {
    BillOrgPanel orgPanel = new InvoiceBillOrgPanel();
    orgPanel.setModel(this.getModel());
    orgPanel.initUI();
    return orgPanel;
  }

  private String getCurrentPk_org() {
    return this.getModel().getContext().getPk_org();
  }

  /**
   * 方法功能描述：组织面板相关
   * <p>
   * <b>参数说明</b>
   * <p>
   * 
   * @since 6.0
   * @author tianft
   * @time 2010-4-22 下午04:53:20
   */
  private void handleBillOrgPanel() {
    InvoiceBillManageModel model = (InvoiceBillManageModel) this.getModel();
    if (model.getInvoiceUIState() == InvoiceUIState.FEE_INVC) {
      // 组织面板的编辑性
      ((InvoiceBillOrgPanel) this.getBillOrgPanel()).setFeeShowing(true);
      // 主组织
      InvoiceVO normInvoice = model.getNormInvoice();
      // 公共的组织面板改为了新增时使用Oid组织参照，非新增时使用Vid组织参照
      // 费用发票虽然是新增但是组织面板是不可编辑的，要保持跟货物发票一致
      ((InvoiceBillOrgPanel) this.getBillOrgPanel())
          .setOrgFromNormalInvoice(normInvoice.getParentVO().getPk_org_v());

    }
    else if (model.getInvoiceUIState() == InvoiceUIState.NORM_INVC) {
      ((InvoiceBillOrgPanel) this.getBillOrgPanel()).setFeeShowing(false);
    }
  }

  /**
   * 方法功能描述：处理字段默认值。
   * <p>
   * <b>参数说明</b>
   * <p>
   * 
   * @since 6.0
   * @author tianft
   * @time 2010-3-19 下午05:11:14
   */
  private void handleItemsDefaultValue() {
    InvoiceBillManageModel model = (InvoiceBillManageModel) this.getModel();
    if (model.getInvoiceUIState() == InvoiceUIState.FEE_INVC) {
      // 是否费用发票
      this.getBillCardPanel().getHeadItem(InvoiceHeaderVO.BFEE)
          .setValue(UFBoolean.TRUE);
      InvoiceVO normInvoice = model.getNormInvoice();
      // 主组织
      this.getBillCardPanel().getHeadItem(InvoiceHeaderVO.PK_ORG)
          .setValue(normInvoice.getParentVO().getPk_org());

      // 费用发票对应的货物发票
      this.getBillCardPanel().getHeadItem(InvoiceHeaderVO.PK_PARENTINVOICE)
          .setValue(normInvoice.getParentVO().getPk_invoice());
      this.getBillCardPanel().execHeadLoadFormulas();
    }
    // add by liangchen1 NC631需求，区分普通采购与进出口采购
    this.setInvoiceType();

    // 如果表头“财务组织”有库存属性，默认=“财务组织”
    String pk_org =
        (String) this.getBillCardPanel().getHeadItem(InvoiceHeaderVO.PK_ORG)
            .getValueObject();
    if (StringUtil.isEmptyWithTrim(pk_org)) {
      return;
    }
    if (OrgUnitPubService.isTypeOf(pk_org, IOrgConst.STOCKORGTYPE)) {
      this.getBillCardPanel().setHeadItem(InvoiceHeaderVO.PK_STOCKORG, pk_org);
      String pk_org_v = OrgUnitPubService.getNewVIDByOrgID(pk_org);
      this.getBillCardPanel().setHeadItem(InvoiceHeaderVO.PK_STOCKORG_V,
          pk_org_v);
    }   

  }

  /**
   * 方法功能描述：根据当前要设置的VO来处理。
   * <p>
   * <b>参数说明</b>
   * 
   * @param vo <p>
   * @since 6.0
   * @author zhaoyha
   * @time 2010-4-7 上午11:44:10
   */
  private void handleItemsEnable(InvoiceVO vo) {
    if (UIState.ADD == this.getModel().getUiState()
        || UIState.EDIT == this.getModel().getUiState()) {
      // 采购组织可编辑性
      new PurchaseOrgEditableHandler(this.getBillCardPanel()).setEditable();
    }
  }

  /**
   * 初始化的时候原币本币币种是一致的，这里只处理是否设置默认值1
   */
  private void initGroupAndGlobalExchangeRate() {
    String pk_group = this.getModel().getContext().getPk_group();
    // 集团本位币
    String nc001 = PubSysParamUtil.getNC001(pk_group);
    // 全局本位币
    String nc002 = PubSysParamUtil.getNC002();

    if (NCPara.NC002_NOUSEGLOBALCURRTYPE.getName().equals(nc002)) {
      this.getBillCardPanel().setHeadItem(InvoiceHeaderVO.NGLOBALEXCHGRATE,
          null);
    }
    else {
      this.getBillCardPanel().setHeadItem(InvoiceHeaderVO.NGLOBALEXCHGRATE,
          UFDouble.ONE_DBL);
    }

    if (NCPara.NC001_NOUSEGROUPCURRTYPE.getName().equals(nc001)) {
      this.getBillCardPanel()
          .setHeadItem(InvoiceHeaderVO.NGROUPEXCHGRATE, null);
    }
    else {
      this.getBillCardPanel().setHeadItem(InvoiceHeaderVO.NGLOBALEXCHGRATE,
          UFDouble.ONE_DBL);
    }
  }

  private void setPk_org(String pk_org) {
    if (StringUtil.isEmptyWithTrim(pk_org)) {
      return;
    }
    // 设置其它信息
    this.getBillCardPanel().getHeadItem(InvoiceHeaderVO.PK_ORG)
        .setValue(pk_org);
    // 联动出集团等信息
    this.getBillCardPanel().getBillData()
        .loadEditHeadRelation(InvoiceHeaderVO.PK_ORG);
    // 设置币种
    String pk_currtype = OrgUnitPubService.queryOrgCurrByPk(pk_org);
    this.getBillCardPanel().getHeadItem(InvoiceHeaderVO.CORIGCURRENCYID)
        .setValue(pk_currtype);
    this.getBillCardPanel().getHeadItem(InvoiceHeaderVO.CCURRENCYID)
        .setValue(pk_currtype);
    // 初始化集团和全局本位币汇率
    this.initGroupAndGlobalExchangeRate();
    // 设置采购组织
    if (OrgUnitPubService.isTypeOf(pk_org, IOrgConst.PURCHASEORGTYPE)) {
      this.getBillCardPanel().getHeadItem(InvoiceHeaderVO.PK_PURCHASEORG)
          .setValue(pk_org);
    }

    // 参照重设
    this.initRef();

  }

  @Override
  protected void enableFillableItems() {
    super.enableFillableItems();
    // 无税单价、含税单价、税率
    // 项目；
    // 备注、自定义项
    String[] enableItems =
        new String[] {
          InvoiceItemVO.NTAXRATE, InvoiceItemVO.NASTORIGPRICE,
          InvoiceItemVO.NASTORIGTAXPRICE, InvoiceItemVO.CPROJECTID,
          InvoiceItemVO.VMEMOB, InvoiceItemVO.VBDEF1, InvoiceItemVO.VBDEF2,
          InvoiceItemVO.VBDEF3, InvoiceItemVO.VBDEF4, InvoiceItemVO.VBDEF5,
          InvoiceItemVO.VBDEF6, InvoiceItemVO.VBDEF7, InvoiceItemVO.VBDEF8,
          InvoiceItemVO.VBDEF9, InvoiceItemVO.VBDEF10, InvoiceItemVO.VBDEF11,
          InvoiceItemVO.VBDEF12, InvoiceItemVO.VBDEF13, InvoiceItemVO.VBDEF14,
          InvoiceItemVO.VBDEF15, InvoiceItemVO.VBDEF16, InvoiceItemVO.VBDEF17,
          InvoiceItemVO.VBDEF18, InvoiceItemVO.VBDEF19, InvoiceItemVO.VBDEF20
        };
    BillCardPanelUtils cardUtils =
        new BillCardPanelUtils(this.getBillCardPanel());
    // 放开字段的批改
    cardUtils.enableItemsFill(enableItems);
  }

  protected void initRef() {
    // 设置批次号参照
    BatchRefPane pane = new BatchRefPane();
    this.getBillCardPanel().getBodyItem(InvoiceItemVO.VBATCHCODE)
        .setComponent(pane);
    // 供应商
    BillItem item =
        this.getBillCardPanel().getHeadItem(InvoiceHeaderVO.PK_SUPPLIER);
    new FilterSupplierRefUtils(item)
        .filterItemRefByOrg(this.getCurrentPk_org());
    // 初始化银行账户参照
    new SupplierFreeCustInfoUtil().initCustBankRefPanel(
        this.getBillCardPanel(), UFBoolean.FALSE.booleanValue());
    // 付款单位
    item =
        this.getBillCardPanel().getHeadItem(InvoiceHeaderVO.PK_PAYTOSUPPLIER);
    new FilterSupplierRefUtils(item)
        .filterItemRefByOrg(this.getCurrentPk_org());
    // 付款协议
    item = this.getBillCardPanel().getHeadItem(InvoiceHeaderVO.PK_PAYTERM);
    new FilterPaytermRefUtils(item).filterItemRefByOrg(this.getCurrentPk_org());

    new FilterSupplierRefUtils(item)
        .filterItemRefByOrg(this.getCurrentPk_org());
    // 物料
    item = this.getBillCardPanel().getBodyItem(InvoiceItemVO.PK_MATERIAL);
    new FilterMaterialRefUtils((UIRefPane) item.getComponent())
        .filterItemRefByOrg(this.getCurrentPk_org());
    // 物料（OID）
    item = this.getBillCardPanel().getBodyItem(InvoiceItemVO.PK_SRCMATERIAL);
    new FilterMaterialRefUtils((UIRefPane) item.getComponent())
        .filterItemRefByOrg(this.getCurrentPk_org());
    UIRefPane panel =
        (UIRefPane) this.getBillCardPanel()
            .getBodyItem(InvoiceItemVO.PK_MATERIAL).getComponent();
    panel.setMultiSelectedEnabled(true);

    // 仓库(表体)
    item = this.getBillCardPanel().getBodyItem(InvoiceItemVO.PK_STORDOC);
    new FilterWareHouseRefUtils((UIRefPane) item.getComponent())
        .filterItemRefByOrg(this.getCurrentPk_org());
    // 使用部门(表体)
    item = this.getBillCardPanel().getBodyItem(InvoiceItemVO.PK_USEDEPT);
    FilterDeptRefUtils.createFilterDeptRefUtilsOfPU(
        (UIRefPane) item.getComponent()).filterItemRefByOrg(
        this.getCurrentPk_org());
    // 使用部门(表体vid)
    item = this.getBillCardPanel().getBodyItem(InvoiceItemVO.PK_USEDEPT_V);
    FilterDeptRefUtils.createFilterDeptRefUtilsOfPU(
        (UIRefPane) item.getComponent()).filterItemRefByOrg(
        this.getCurrentPk_org());
    // 项目(表体)
    item = this.getBillCardPanel().getBodyItem(InvoiceItemVO.CPROJECTID);
    if (null != item) {
      new FilterProjectRefUtils(item).filterItemRefByOrg(this
          .getCurrentPk_org());
    }
    // 项目(收支项目)
    item = this.getBillCardPanel().getBodyItem(InvoiceItemVO.PK_COSTSUBJ);
    new FilterProjectRefUtils(item)
        .filterCostSubjByOrg(this.getCurrentPk_org());
    // 设置采购组织相关的参照
    this.initRefByPurOrg();
  }

  protected void initRefByPurOrg() {
    // 采购部门
    BillItem item =
        this.getBillCardPanel().getHeadItem(InvoiceHeaderVO.PK_DEPT);
    String pk_purorg =
        (String) this.getBillCardPanel()
            .getHeadItem(InvoiceHeaderVO.PK_PURCHASEORG).getValueObject();
    FilterDeptRefUtils.createFilterDeptRefUtilsOfPU(
        (UIRefPane) item.getComponent()).filterItemRefByOrg(pk_purorg);
    // 采购部门（vid）
    item = this.getBillCardPanel().getHeadItem(InvoiceHeaderVO.PK_DEPT_V);
    FilterDeptRefUtils.createFilterDeptRefUtilsOfPU(
        (UIRefPane) item.getComponent()).filterItemRefByOrg(pk_purorg);

    // 采购员(人员)
    item = this.getBillCardPanel().getHeadItem(InvoiceHeaderVO.PK_BIZPSN);
    FilterPsndocRefUtils.createFilterPsndocRefUtilsOfPU(
        (UIRefPane) item.getComponent()).filterItemRefByOrg(pk_purorg);
  }

  @Override
  protected void onAdd() {
    this.handleBillOrgPanel();
    super.onAdd();
  }

  /**
   * 父类方法重写
   * 
   * @see nc.ui.uif2.editor.BillForm#onEdit()
   */
  @Override
  protected void onEdit() {
    super.onEdit();
    // 单据模板可编辑处理
    this.handleItemsEnable((InvoiceVO) this.getModel().getSelectedData());
  }

  /**
   * 方法功能描述：返回要处理的数据前再计算一次整单合计。
   * <p>
   * <b>参数说明</b>
   * 
   * @param value <p>
   * @since 6.0
   * @author zhyhang
   * @time 2010-1-31 下午10:51:14
   */
  protected void reCalcTotalValue(Object value) {
    if (UIState.ADD != this.getModel().getUiState()
        && UIState.EDIT != this.getModel().getUiState()) {
      return;
    }
    if (null == this.getTotalValueHandler()) {
      return;
    }
    Map<String, String> bodyHeadMap =
        this.getTotalValueHandler().getBodyHeadMap();
    AggregatedValueObject bill = (AggregatedValueObject) value;
    if (null == bill || null == bill.getParentVO()
        || null == bill.getChildrenVO()) {
      return;
    }
    int rowCnt = bill.getChildrenVO().length;
    Map<String, UFDouble> allTotal = new HashMap<String, UFDouble>();
    for (Entry<String, String> entry : bodyHeadMap.entrySet()) {
      allTotal.put(entry.getValue(), UFDouble.ZERO_DBL);
    }
    for (int i = 0; i < rowCnt; ++i) {
      for (Entry<String, String> entry : bodyHeadMap.entrySet()) {
        String bodyName = entry.getKey();
        String headName = entry.getValue();
        UFDouble total = allTotal.get(headName);
        CircularlyAccessibleValueObject item = bill.getChildrenVO()[i];
        total =
            MathTool.add(total, (UFDouble) item.getAttributeValue(bodyName));
        allTotal.put(headName, total);
      }
    }
    for (Entry<String, UFDouble> entry : allTotal.entrySet()) {
      String headName = entry.getKey();
      UFDouble total = entry.getValue();
      bill.getParentVO().setAttributeValue(headName, total);
    }
  }

  @Override
  protected void setDefaultValue() {
    this.setPk_org(this.getModel().getContext().getPk_org());
    this.handleItemsDefaultValue();
  }

  /**
   * 区分普通采购与进出口采购
   * liangchen1
   */
  protected void setInvoiceType() {
    new InvoiceTypeSetter(InvoicePuImportEnum.PUINVOICE)
        .setInvocieType(new CardEditorHelper(this.getBillCardPanel()));
  }
}
