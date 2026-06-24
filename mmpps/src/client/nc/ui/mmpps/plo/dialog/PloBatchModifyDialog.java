package nc.ui.mmpps.plo.dialog;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;

import nc.desktop.ui.WorkbenchEnvironment;
import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.bd.ref.model.BomVerRefModel;
import nc.ui.bd.ref.model.RoutVerRefModel;
import nc.ui.mmbd.pst.view.util.PermissionOrgProcessorUtil;
import nc.ui.mmbd.pub.ref.FilterBomVerRefUtils;
import nc.ui.mmf.busi.ref.FilterDefaultRefUtils;
import nc.ui.mmpps.plo.action.util.PloActionBeforeUtil;
import nc.ui.mmpps.plo.action.util.PoActionsUtil;
import nc.ui.mmpps.plo.serviceproxy.PloClientServiceProxy;
import nc.ui.pd.pd0302.view.DialogPanel;
import nc.ui.pub.beans.RefEditEvent;
import nc.ui.pub.beans.RefEditListener;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIComboBox;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillCardBeforeEditListener;
import nc.ui.pub.bill.BillData;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillEditListener;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillItemEvent;
import nc.ui.pubapp.bill.BillCardPanel;
import nc.ui.pubapp.uif2app.DefaultExceptionHanler;
import nc.ui.pubapp.uif2app.model.BillManageModel;
import nc.ui.uif2.IExceptionHandler;
import nc.ui.vorg.ref.DeptVersionDefaultNCRefModel;
import nc.ui.org.ref.DeptDefaultNCRefModel;
import nc.util.mmf.framework.base.MMValueCheck;
import nc.vo.bd.bom.bom0202.enumeration.BomTypeEnum;
import nc.vo.mmpps.mpm.res.MpmNoTranslateRes;
import nc.vo.mmpps.mpm.res.MpmRes;
import nc.vo.mmpps.mps0202.AggregatedPoVO;
import nc.vo.mmpps.mps0202.PoVO;
import nc.vo.pub.BusinessException;
import nc.vo.relation.IBusiRoleConst;
import nc.vo.uap.rbac.profile.FunctionPermProfileManager;
import nc.vo.uap.rbac.profile.IFunctionPermProfile;
import nc.vo.uif2.LoginContext;
//import nc.desktop.ui.WorkbenchEnvironment;

public class PloBatchModifyDialog extends UIDialog implements BillEditListener, BillCardBeforeEditListener

{
    private static final long serialVersionUID = 1L;

    private BillCardPanel card;

    /***
     * 转出按钮
     */
    private UIButton btnOK = null;

    /***
     * 取消按钮
     */
    private UIButton btnCancel = null;

    /**
     * 批改订单的计划工厂
     */
    private String pk_org = null;

    private BillManageModel model;

    public PloBatchModifyDialog(BillManageModel model, String title) {
        super(model.getContext().getEntranceUI(), title);
        this.model = model;
        this.initialize();
    }

    /***
     * 初始化
     */
    private void initialize() {
        this.setContentPane(this.getDialogPanel());
        this.initConnection();

    }

    /***
     * 初始化监听。
     */
    private void initConnection() {
        this.getBtnOK().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PloBatchModifyDialog.this.onOkEvent();
            }
        });
        this.getBtnCancel().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PloBatchModifyDialog.this.onCancelEvent();
            }
        });
    }

    /****
     * 取消按钮事件处理
     */
    protected void onCancelEvent() {
        this.closeCancel();

    }

    /****
     * 确定按钮事件处理
     * 
     * @throws BusinessException
     */
    protected void onOkEvent() {
        try {
            // 前处理业务规则
            AggregatedPoVO[] agg =
                    new PloActionBeforeUtil().doEditsActionBefore(this.model.getSelectedOperaDatas(),
                            this.getModifyKey(), this.getModifyValue());
            // 调用批改保存接口
            PloClientServiceProxy.getAcePoService().updateAggs(agg, this.model.getContext().getPk_loginUser(),
                    this.getModifyKey());
            this.closeOK();
        }
        catch (BusinessException e) {
            this.setMsg(MpmRes.getBatchEditPoCodeExpt());
            this.getExceptionHandler().handlerExeption(e);

        }
    }

    protected IExceptionHandler exceptionHandler;

    public IExceptionHandler getExceptionHandler() {
        DefaultExceptionHanler handler = new DefaultExceptionHanler(this.model.getContext().getEntranceUI());
        if (this.exceptionHandler == null) {
            handler.setContext(this.model.getContext());
            handler.setErrormsg(this.getMsg());
        }
        return handler;
    }

    private String msg;

    public String getMsg() {
        return this.msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    private UIButton getBtnCancel() {
        if (this.btnCancel == null) {
            this.btnCancel = new UIButton(MpmRes.getCancelBtnName());
            this.btnCancel.setMnemonic(KeyEvent.VK_C);
            this.btnCancel.setToolTipText(MpmRes.getCancelBtnName2());
            this.btnCancel.setName("btnCancel");
        }
        return this.btnCancel;
    }

    private UIButton getBtnOK() {
        if (this.btnOK == null) {
            this.btnOK = new UIButton(MpmRes.getConfirmBtnName());
            this.btnOK.setName("btnOK");
            this.btnOK.setMnemonic(KeyEvent.VK_Y);
            this.btnOK.setToolTipText(MpmRes.getConfirmBtnName2());
        }
        return this.btnOK;
    }

    private DialogPanel getDialogPanel() {
        DialogPanel dlgPanel = new DialogPanel(this.getCenterPnl(), null, new UIButton[] {
            this.getBtnOK(), this.getBtnCancel()
        }, true);
        return dlgPanel;
    }

    /***
     * 默认的背景颜色
     */
    private static final int COLOR = 0Xededed;

    /***
     * getCenterPnl
     * 
     * @return UIPanel
     */
    private UIPanel getCenterPnl() {
        UIPanel centerPnl = new UIPanel();
        centerPnl.setPreferredSize(new java.awt.Dimension(100, 50));
        centerPnl.setLayout(new BorderLayout());
        centerPnl.add(this.getBillCard(), BorderLayout.CENTER);
        centerPnl.setBackground(new java.awt.Color(PloBatchModifyDialog.COLOR));
        return centerPnl;
    }

    /**
     * 获取卡片面板
     * 
     * @return 面板实例
     */
    public BillCardPanel getBillCard() {
        if (this.card == null) {
            this.card = new BillCardPanel();
            BillData biData = new BillData();
            biData.setHeadItems(this.getBillItems());
            this.card.setBillData(biData);
            this.getBillCard().addBillEditListenerHeadTail(this);
            this.getBillCard().setBillBeforeEditListenerHeadTail(this);
            this.getBillCard().setBackground(new java.awt.Color(PloBatchModifyDialog.COLOR));
            this.initCardCombo();
            this.initCardRef(this.model.getContext());
        }
        return this.card;
    }

    // "批改字段"列的Key
    private static final String MODIFYKEY = "modifykey";

    // 所有批改界面BillItem的Keys
    private static final String[] KEYS = {
        PloBatchModifyDialog.MODIFYKEY, PoVO.CFACTORYVID, PoVO.CSTOCKORGID, PoVO.CPRODDEPTVID, PoVO.CWKID, PoVO.CBOMID,
        PoVO.CPACKBOMID, PoVO.CRTID, PoVO.DPLANSTARTTIME, PoVO.DBILLTIME, PoVO.DREWINDTIME, PoVO.DDEMANDTIME
    };

    // 所有批改界面BillItem的显示名称
    private final String[] NAMES = {
        MpmRes.getItemNameBatchField(), MpmRes.getItemNameStockOrg(), MpmRes.getItemNameFacotry(),
        MpmRes.getItemNameProcDept(), MpmRes.getItemNameWorkCenter(), MpmRes.getItemNameBOMVer(),
        MpmRes.getItemNamePBOM(), MpmRes.getItemNameRtVer(), MpmRes.getItemNameStartTime(),
        MpmRes.getItemNameBillDate(), MpmRes.getItemNameDrewindtime(), MpmRes.getItemNameDemandTime()
    };

    // 所有批改界面BillItem的类型
    @SuppressWarnings("static-access")
    private static final int[] DATA_TYPE = {
        BillItem.COMBO, BillItem.UFREF, BillItem.UFREF, BillItem.UFREF, BillItem.UFREF, BillItem.UFREF, BillItem.UFREF,
        BillItem.UFREF, BillItem.DATE, BillItem.DATE, BillItem.DATE, BillItem.DATE
    };

    // BillItem的默认宽度
    private static final int ITEM_WIDTH = 1;

    // BillItem的默认长度
    private static final int ITEM_LENGTH = 30;

    // 当前选中的BillItem
    private BillItem selectedItem;

    /**
     * 组装批改界面的BillItem
     * 
     * @return　
     */
    private BillItem[] getBillItems() {
        List<BillItem> itemList = new ArrayList<BillItem>();
        for (int i = 0; i < PloBatchModifyDialog.KEYS.length; i++) {
            BillItem bi = new BillItem();
            bi.setKey(PloBatchModifyDialog.KEYS[i]);
            bi.setName(this.NAMES[i]);
            bi.setDataType(PloBatchModifyDialog.DATA_TYPE[i]);
            // 第0项BillItem(即“批改字段”)默认显示，其余隐藏
            if (i <= 1) {
                bi.setShow(true);
                this.selectedItem = bi;
            }
            else {
                bi.setShow(false);
            }
            bi.setWidth(PloBatchModifyDialog.ITEM_WIDTH);
            bi.setLength(PloBatchModifyDialog.ITEM_LENGTH);
            bi.setEdit(true);
            bi.setPos(0);
            bi.setShowOrder(i);
            bi.setNull(true);
            itemList.add(bi);
        }
        return itemList.toArray(new BillItem[itemList.size()]);
    }

    private void initCardCombo() {
        // 初始化批改字段下拉框，设置其可选项为其余BillItem的名称
        UIComboBox modifyCombo = (UIComboBox) this.getHeadItem("modifykey");
        for (int i = 1; i < this.NAMES.length; i++) {
            modifyCombo.addItem(this.NAMES[i]);
        }
    }

    // 库存组织(参照不能多语)
    private static final String REFDEP_CFNAME = MpmNoTranslateRes.getREFDEP_CFNAME();

    // 部门参照的名称
    private static final String REFDEP_NODENAME = MpmNoTranslateRes.getREFDEP_NODENAME();

    // 工作中心参照的名称
    private static final String REFWK_NODENAME = MpmNoTranslateRes.getREFWK_NODENAME();

    // 生产BOM版本
    private static final String REFWK_VBOMVERSION = MpmNoTranslateRes.getREFWK_VBOMVERSION();

    // 工艺线路版本
    private static final String REFWK_VRTVERSION = MpmNoTranslateRes.getREFWK_VRTVERSION();

    // 生产工厂
    private static final String REFSTOCKORG = MpmNoTranslateRes.getREFSTOCKORG();

    // 包装BOM版本
    private static final String REFCPACKBOMID = MpmNoTranslateRes.getREFWK_VBOMVERSION();

    @SuppressWarnings("unused")
    private void initCardRef(final LoginContext context) {

        Object[] obj = this.model.getSelectedOperaDatas();
        AggregatedPoVO[] agg = PoActionsUtil.convertObj2AggVO(obj);
        PoVO po = agg[0].getParentVO();
        // 设置库存组织
        final UIRefPane cfRef = (UIRefPane) this.getHeadItem(PoVO.CFACTORYVID);
        cfRef.setRefNodeName(PloBatchModifyDialog.REFDEP_CFNAME);
        cfRef.addRefEditListener(new RefEditListener() {

            @Override
            public boolean beforeEdit(RefEditEvent event) {
                FilterDefaultRefUtils cdeptidRefUtils = new FilterDefaultRefUtils(cfRef);
                PermissionOrgProcessorUtil.processFactoryOrg(cfRef, context);
                return true;
            }
        });

        UIRefPane rfpProDept = (UIRefPane) this.getHeadItem(PoVO.CPRODDEPTVID);
        rfpProDept.setRefNodeName(PloBatchModifyDialog.REFDEP_NODENAME);
        // 设置工作中心参照
        UIRefPane cwkRef = (UIRefPane) this.getHeadItem(PoVO.CWKID);
        cwkRef.setRefNodeName(PloBatchModifyDialog.REFWK_NODENAME);
        // 设置工艺参照
        UIRefPane bomRef = (UIRefPane) this.getHeadItem(PoVO.CRTID);
        bomRef.setRefNodeName(PloBatchModifyDialog.REFWK_VRTVERSION);
        // 设置BOM参照
        UIRefPane vrtyRef = (UIRefPane) this.getHeadItem(PoVO.CBOMID);
        vrtyRef.setRefNodeName(PloBatchModifyDialog.REFWK_VBOMVERSION);

        // 设置包装BOM版本
        UIRefPane packbomRef = (UIRefPane) this.getHeadItem(PoVO.CPACKBOMID);
        packbomRef.setRefNodeName(PloBatchModifyDialog.REFCPACKBOMID);
        // 设置生产工厂
        final UIRefPane stockorgRef = (UIRefPane) this.getHeadItem(PoVO.CSTOCKORGID);
        stockorgRef.setRefNodeName(PloBatchModifyDialog.REFSTOCKORG);
        stockorgRef.addRefEditListener(new RefEditListener() {

            @Override
            public boolean beforeEdit(RefEditEvent event) {
                IFunctionPermProfile profile =
                        FunctionPermProfileManager.getInstance().getProfile(
                                WorkbenchEnvironment.getInstance().getLoginUser().getUser_code());
                String[] orgPKs = profile.getPermPkorgs();
                stockorgRef.getRefModel().setFilterPks(orgPKs);
                return true;
            }
        });
    }

    /**
     * 根据BillItem的Key，返回其Component
     * 
     * @param strKey
     *            字段名称
     * @return 字段名称对应的Component
     */
    private JComponent getHeadItem(String strKey) {
        return this.getBillCard().getHeadItem(strKey).getComponent();
    }

    @Override
    public void afterEdit(BillEditEvent e) {
        // 监听"修改字段"值改变事件
        if (PloBatchModifyDialog.MODIFYKEY.equals(e.getKey())) {
            this.changedShownItems(e);
        }
    }

    /**
     * "修改字段"选项改变事件，清除上次显示项，显示选择项
     * 
     * @param e
     */
    private void changedShownItems(BillEditEvent e) {
        BillItem[] items = this.card.getHeadItems();
        for (BillItem item : items) {
            // "待修改字段" 永远显示....
            if (PloBatchModifyDialog.MODIFYKEY.equals(item.getKey())) {
                continue;
            }
            if (e.getValue().equals(item.getName())) {
                // 显示选择的项
                item.setShow(true);
                this.selectedItem = item;
            }
            else {
                // 隐藏并清除旧数据
                item.setShow(false);
                item.clearViewData();
            }
        }
        // 重新构造表头显示
        this.card.initPanelByPos(0);

    }

    /**
     * 获取批改订单的计划工厂id
     * 
     * @param pk_org
     */
    public void setPk_org(String pk_org) {
        if (this.pk_org != pk_org) {
            this.pk_org = pk_org;
            this.orgChanged();
        }
    }

    private void orgChanged() {
        UIRefPane scbmRef = (UIRefPane) this.getHeadItem(PoVO.CPRODDEPTVID);
        AbstractRefModel refModel = scbmRef.getRefModel();
        if (null != refModel) {
            refModel.setPk_org(this.getPk_org());
        }

        UIRefPane cwkRef = (UIRefPane) this.getHeadItem(PoVO.CWKID);
        refModel = cwkRef.getRefModel();
        if (null != refModel) {
            refModel.setPk_org(this.getPk_org());
        }
    }

    /**
     * 设置批改订单的计划工厂id
     * 
     * @return
     */
    public String getPk_org() {
        return this.pk_org;
    }

    /**
     * 获取当前正在批改的字段
     * 
     * @return
     */
    public String getModifyKey() {
        return this.selectedItem.getKey();
    }

    /**
     * 获取当前批改字段的值
     * 
     * @return
     */
    public Object getModifyValue() {
        return this.selectedItem.getValueObject();
    }

    @Override
    public boolean beforeEdit(BillItemEvent e) {
        Object[] obj = this.model.getSelectedOperaDatas();
        AggregatedPoVO[] agg = PoActionsUtil.convertObj2AggVO(obj);
        PoVO po = agg[0].getParentVO();
        this.getBillCard().getHeadItem(PloBatchModifyDialog.MODIFYKEY).getComponent();
        UIComboBox comboBox =
                (UIComboBox) this.getBillCard().getHeadItem(PloBatchModifyDialog.MODIFYKEY).getComponent();
        String name = comboBox.getSelectedItemName().toString();
        if (name.equals(MpmRes.getREF_FACTORY())) {
            BillItem item = this.getBillCard().getHeadItem(PoVO.CFACTORYVID);
            item.setRefType(name);
            UIRefPane orgPane = (UIRefPane) item.getComponent();
            orgPane.getRefModel().getRefSql();
            PermissionOrgProcessorUtil.processFactoryOrg(orgPane, this.model.getContext());

        }
        else if (name.equals(MpmRes.getItemNameProcDept())) {
            BillItem item = this.getBillCard().getHeadItem(PoVO.CPRODDEPTVID);
            item.setRefType(name);
            UIRefPane rfpProDept = (UIRefPane) item.getComponent();
            DeptDefaultNCRefModel refModel = new nc.ui.org.ref.DeptDefaultNCRefModel();
            refModel.setPk_org(this.getPk_org());
            rfpProDept.setRefModel(new nc.ui.org.ref.DeptDefaultNCRefModel());
            // 跨组织部门参照
//            ((DeptVersionDefaultNCRefModel) rfpProDept.getRefModel()).setBusifuncode(IBusiRoleConst.MANUFACTURE);
//            ((DeptVersionDefaultNCRefModel) rfpProDept.getRefModel()).setPk_group(po.getPk_group());
//            ((DeptVersionDefaultNCRefModel) rfpProDept.getRefModel()).setPk_org(po.getCstockorgid());
              ((DeptDefaultNCRefModel) rfpProDept.getRefModel()).setBusifuncode(IBusiRoleConst.MANUFACTURE);
	          ((DeptDefaultNCRefModel) rfpProDept.getRefModel()).setPk_group(po.getPk_group());
	          ((DeptDefaultNCRefModel) rfpProDept.getRefModel()).setPk_org(po.getCstockorgid());
        }
        else if (name.equals(MpmRes.getItemNameWorkCenter())) {
            BillItem item = this.getBillCard().getHeadItem(PoVO.CWKID);
            item.setRefType(name);
            UIRefPane rfpWkCenter = (UIRefPane) item.getComponent();
            if (rfpWkCenter != null) {
                rfpWkCenter.getRefModel().setPk_group(po.getPk_group());
                if (!MMValueCheck.isEmpty(po.getCfactoryid())) {
                    rfpWkCenter.getRefModel().setPk_org(po.getCfactoryid());
                }
            }
        }
        else if (name.equals(MpmRes.getItemNameBOMVer())) {
            UIRefPane refPane = (UIRefPane) this.getBillCard().getHeadItem(PoVO.CBOMID).getComponent();
            BomVerRefModel refModel = (BomVerRefModel) refPane.getRefModel();
            FilterBomVerRefUtils filter = new FilterBomVerRefUtils(refPane);
            // 过滤条件：生产BOM
            filter.setBomtype(BomTypeEnum.PRODUCTFINISH);
            // 过滤条件：当前物料+有效BOM版本
            filter.filterBomVerByMaterialVidANDNULL(po.getCmaterialid(), po.getCmaterialvid());
            // 过滤条件：当前集团
            filter.filterItemRefByGroup(po.getPk_group());
            // 过滤条件：当前组织
            filter.filterItemRefByOrg(po.getPk_org());
            // 特征码
            filter.setFeaturecode(po.getCffileid());
            refModel.setMaterialoid(po.getCmaterialid());
        }
        else if (name.equals(MpmRes.getItemNameRtVer())) {
            UIRefPane pane = (UIRefPane) this.getBillCard().getHeadItem(PoVO.CRTID).getComponent();
            RoutVerRefModel refModel = (RoutVerRefModel) pane.getRefModel();
            refModel.setPk_group(po.getPk_group());
            refModel.setPk_org(po.getPk_org());
            // refModel.setCmaterialid(po.getCmaterialvid());
            refModel.setCmaterialid(po.getCmaterialid());
            refModel.setCmaterialoid(po.getCmaterialid());
            // 特征码
            refModel.setFeaturecode(po.getCffileid());
        }
        else if (name.equals(MpmRes.getItemNamePBOM())) {
            UIRefPane refPane = (UIRefPane) this.getBillCard().getHeadItem(PoVO.CPACKBOMID).getComponent();
            BomVerRefModel refModel = (BomVerRefModel) refPane.getRefModel();
            FilterBomVerRefUtils filter = new FilterBomVerRefUtils(refPane);
            // 过滤条件：包装BOM
            filter.setBomtype(BomTypeEnum.RTFINISH);
            // 过滤条件：当前物料+有效BOM版本
            filter.filterBomVerByMaterialVidANDNULL(po.getCmaterialid(), po.getCmaterialvid());
            // 过滤条件：当前集团
            filter.filterItemRefByGroup(po.getPk_group());
            // 过滤条件：当前组织
            filter.filterItemRefByOrg(po.getPk_org());
            // 特征码
            filter.setFeaturecode(po.getCffileid());
            refModel.setMaterialoid(po.getCmaterialid());
        }
        else if (name.equals(MpmRes.getItemNameFacotry())) {
            IFunctionPermProfile profile =
                    FunctionPermProfileManager.getInstance().getProfile(
                            WorkbenchEnvironment.getInstance().getLoginUser().getUser_code());
            String[] orgPKs = profile.getPermPkorgs();
            BillItem stockorgid = this.getBillCard().getHeadItem(PoVO.CSTOCKORGID);
            UIRefPane ref = (UIRefPane) stockorgid.getComponent();
            ref.getRefModel().setFilterPks(orgPKs);
        }

        return true;
    }

    @Override
    public void bodyRowChange(BillEditEvent e) {
    }

}
