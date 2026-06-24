package nc.ui.so.m30.billui.action.assist;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import nc.bs.framework.common.NCLocator;
import nc.itf.medpub.IBatchCodeQryService;
import nc.itf.medpub.med_lotno_148.IMed_lotno_148_Maintain;
import nc.itf.scmpub.reference.uap.group.SysInitGroupQuery;
import nc.itf.uap.pf.busiflow.PfButtonClickContext;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.pf.PfUtilClient;
import nc.ui.pubapp.uif2app.actions.AbstractReferenceAction;
import nc.ui.pubapp.uif2app.funcnode.trantype.TrantypeFuncUtils;
import nc.ui.pubapp.uif2app.view.BillForm;
import nc.ui.uif2.AppEventListener;
import nc.ui.uif2.UIState;
import nc.ui.uif2.model.AbstractAppModel;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.medpub.med_lotno_148.MedLotno_148;
import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.scmf.ic.mbatchcode.BatchcodeVO;
import nc.vo.scmpub.res.billtype.SOBillType;
import nc.vo.so.m30.entity.SaleOrderBVO;
import nc.vo.so.m30.entity.SaleOrderVO;
import nc.vo.so.pub.util.ArrayUtil;

public class ReturnSaleOrderAddFrom4CAction extends AbstractReferenceAction {
  private static final long serialVersionUID = -5130322362720789811L;
  
  private AbstractAppModel model;
  
  private BillForm editor;
  
  public AbstractAppModel getModel() {
    return this.model;
  }
  
  public void setModel(AbstractAppModel model) {
    this.model = model;
    model.addAppEventListener((AppEventListener)this);
  }
  
  public void setSourceBillName(String sourceBillName) {
    super.setSourceBillName(sourceBillName);
    setBtnName(NCLangRes.getInstance().getStrByID("4006011_0", 
          "04006011-0232"));
  }
  
  public BillForm getEditor() {
    return this.editor;
  }
  
  public void setEditor(BillForm editor) {
    this.editor = editor;
  }
  
  protected boolean isActionEnable() {
    return (this.model.getUiState() == UIState.NOT_EDIT);
  }
  
  public void doAction(ActionEvent e) throws Exception {
    if (!SysInitGroupQuery.isICEnabled())
      ExceptionUtils.wrappBusinessException(NCLangRes4VoTransl.getNCLangRes()
          .getStrByID("4006011_0", "04006011-0470")); 
    PfUtilClient.childButtonClickedNew(createPfButtonClickContext());
    if (PfUtilClient.isCloseOK()) {
      SaleOrderVO[] vos = (SaleOrderVO[])PfUtilClient.getRetVos(false);
      fillSaleOrderBVOs(vos);
      List<SaleOrderBVO> bodyVOs = new ArrayList<>();
      getTransferViewProcessor().processBillTransfer((Object[])vos);
      byte b;
      int i;
      SaleOrderVO[] arrayOfSaleOrderVO1;
      for (i = (arrayOfSaleOrderVO1 = vos).length, b = 0; b < i; ) {
        SaleOrderVO saleOrderVO = arrayOfSaleOrderVO1[b];
        SaleOrderBVO[] saleOrderBVOs = saleOrderVO.getChildrenVO();
        if (!ArrayUtil.isEmpty((Object[])saleOrderBVOs)) {
          byte b1;
          int j;
          SaleOrderBVO[] arrayOfSaleOrderBVO;
          for (j = (arrayOfSaleOrderBVO = saleOrderBVOs).length, b1 = 0; b1 < j; ) {
            SaleOrderBVO saleOrderBVO = arrayOfSaleOrderBVO[b1];
            bodyVOs.add(saleOrderBVO);
            b1++;
          } 
        } 
        b++;
      } 
      if (!bodyVOs.isEmpty())
        for (int j = 0; j < bodyVOs.size(); j++) {
          getEditor().getBillCardPanel().setBodyValueAt(((SaleOrderBVO)bodyVOs.get(j)).getAttributeValue("vlotno_148"), j, "vlotno_148", "bodytable1");
          getEditor().getBillCardPanel().setBodyValueAt(((SaleOrderBVO)bodyVOs.get(j)).getAttributeValue("dprodate"), j, "dprodate", "bodytable1");
          getEditor().getBillCardPanel().setBodyValueAt(((SaleOrderBVO)bodyVOs.get(j)).getAttributeValue("vinvaliddate_148"), j, "vinvaliddate_148", "bodytable1");
        }  
    } 
  }
  
  private PfButtonClickContext createPfButtonClickContext() {
    PfButtonClickContext context = new PfButtonClickContext();
    context.setParent(getModel().getContext().getEntranceUI());
    context.setSrcBillType(getSourceBillType());
    context.setPk_group(getModel().getContext().getPk_group());
    context.setUserId(getModel().getContext().getPk_loginUser());
    String vtrantype = 
      TrantypeFuncUtils.getTrantype(getModel().getContext());
    if (StringUtil.isEmptyWithTrim(vtrantype)) {
      context.setCurrBilltype(SOBillType.Order.getCode());
    } else {
      context.setCurrBilltype(vtrantype);
    } 
    context.setUserObj(null);
    context.setSrcBillId(null);
    context.setBusiTypes(null);
    context.setTransTypes(null);
    context.setClassifyMode(1);
    return context;
  }
  
  private void fillSaleOrderBVOs(SaleOrderVO[] saleOrderVOs) throws BusinessException {
    Set<String> lotNoSet = new HashSet<>();
    Set<String> batchcodeSet = new HashSet<>();
    byte b1;
    int i;
    SaleOrderVO[] arrayOfSaleOrderVO1;
    for (i = (arrayOfSaleOrderVO1 = saleOrderVOs).length, b1 = 0; b1 < i; ) {
      SaleOrderVO saleOrderVO = arrayOfSaleOrderVO1[b1];
      SaleOrderBVO[] saleOrderBVOs = saleOrderVO.getChildrenVO();
      if (!ArrayUtil.isEmpty((Object[])saleOrderBVOs)) {
        byte b;
        int k;
        SaleOrderBVO[] arrayOfSaleOrderBVO;
        for (k = (arrayOfSaleOrderBVO = saleOrderBVOs).length, b = 0; b < k; ) {
          SaleOrderBVO saleOrderBVO = arrayOfSaleOrderBVO[b];
          String lotNo = saleOrderBVO.getVfree6();
          String pk_batchcode = saleOrderBVO.getPk_batchcode();
          lotNoSet.add(lotNo);
          batchcodeSet.add(pk_batchcode);
          b++;
        } 
      } 
      b1++;
    } 
    MedLotno_148[] lotnos = null;
    BatchcodeVO[] batchcodes = null;
    IMed_lotno_148_Maintain medLotoService = (IMed_lotno_148_Maintain)NCLocator.getInstance().lookup(IMed_lotno_148_Maintain.class);
    IBatchCodeQryService batchcodeService = (IBatchCodeQryService)NCLocator.getInstance().lookup(IBatchCodeQryService.class);
    lotnos = medLotoService.queryLotnoVOByPks(lotNoSet.<String>toArray(new String[0]));
    batchcodes = batchcodeService.queryBatchCodesByIDs(batchcodeSet.<String>toArray(new String[0]));
    Map<String, MedLotno_148> idVOMap = new HashMap<>();
    Map<String, BatchcodeVO> batchcodeVOMap = new HashMap<>();
    if (!ArrayUtil.isEmptyOrNull((Object[])lotnos)) {
      byte b;
      int k;
      MedLotno_148[] arrayOfMedLotno_148;
      for (k = (arrayOfMedLotno_148 = lotnos).length, b = 0; b < k; ) {
        MedLotno_148 lotno = arrayOfMedLotno_148[b];
        idVOMap.put(lotno.getPk_lotno(), lotno);
        b++;
      } 
    } 
    if (!ArrayUtil.isEmptyOrNull((Object[])batchcodes)) {
      byte b;
      int k;
      BatchcodeVO[] arrayOfBatchcodeVO;
      for (k = (arrayOfBatchcodeVO = batchcodes).length, b = 0; b < k; ) {
        BatchcodeVO batchcode = arrayOfBatchcodeVO[b];
        batchcodeVOMap.put(batchcode.getPk_batchcode(), batchcode);
        b++;
      } 
    } 
    byte b2;
    int j;
    SaleOrderVO[] arrayOfSaleOrderVO2;
    for (j = (arrayOfSaleOrderVO2 = saleOrderVOs).length, b2 = 0; b2 < j; ) {
      SaleOrderVO saleOrderVO = arrayOfSaleOrderVO2[b2];
      SaleOrderBVO[] saleOrderBVOs = saleOrderVO.getChildrenVO();
      if (!ArrayUtil.isEmpty((Object[])saleOrderBVOs)) {
        byte b;
        int k;
        SaleOrderBVO[] arrayOfSaleOrderBVO;
        for (k = (arrayOfSaleOrderBVO = saleOrderBVOs).length, b = 0; b < k; ) {
          SaleOrderBVO saleOrderBVO = arrayOfSaleOrderBVO[b];
          MedLotno_148 lotno = idVOMap.get(saleOrderBVO.getVfree6());
          BatchcodeVO batchcode = batchcodeVOMap.get(saleOrderBVO.getPk_batchcode());
          if (lotno != null || batchcode != null) {
            saleOrderBVO.setAttributeValue("vlotno_148", (lotno == null) ? null : lotno.getVlotno());
            saleOrderBVO.setAttributeValue("dprodate", (lotno != null) ? lotno.getDprodate() : batchcode.getDproducedate());
            String dvalidate = null;
            if (batchcode != null && batchcode.getDvalidate() != null)
              dvalidate = batchcode.getDvalidate().toStdString(); 
            saleOrderBVO.setAttributeValue("vinvaliddate_148", (lotno != null) ? lotno.getVinvaliddate() : dvalidate);
          } 
          b++;
        } 
      } 
      b2++;
    } 
  }
}
