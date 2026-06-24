package nc.ui.medpub.editor.card.afteredit.body;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.table.TableColumn;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.impl.medpub.JXQUtil;
import nc.itf.bd.material.baseinfo.IMaterialBaseInfoQueryService;
import nc.itf.medpub.IBatchCodeQryService;
import nc.itf.medpub.med_lotno_148.IMed_lotno_148_Maintain;
import nc.md.model.MetaDataException;
import nc.md.persist.framework.MDPersistenceService;
import nc.pubitf.medpub.adaptor.IBillFieldsNoNeedCopy;
import nc.ui.ic.batchcode.ref.BatchRefDlg;
import nc.ui.ic.batchcode.ref.BatchRefPane;
import nc.ui.ic.pub.model.ICBizEditorModel;
import nc.ui.medpub.editor.card.listener.ICardBodyAfterEditEventListener;
import nc.ui.medpub.utils.MaterialPropertyUtil;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillCellEditor;
import nc.ui.pub.bill.BillItem;
import nc.ui.pubapp.uif2app.event.card.CardBodyAfterEditEvent;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.uif2.model.AbstractAppModel;
import nc.uif.pub.exception.UifException;
import nc.vo.bd.material.MaterialVO;
import nc.vo.bd.material.stock.MaterialStockVO;
import nc.vo.ic.batch.BatchRefViewVO;
import nc.vo.ic.batchcode.BatchDlgParam;
import nc.vo.ic.pub.util.StringUtil;
import nc.vo.medpub.med_lotno_148.MedLotno_148;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.scmf.ic.mbatchcode.BatchcodeVO;
import nc.vo.uap.material.extend.EnumValiduntilunit_148;
import nc.vo.uif2.LoginContext;

public class BatchCode implements ICardBodyAfterEditEventListener {
    private LoginContext context;
    private String materialStr = null;
    private String cmaterialoidStr = null;
    private String dproducedateStr = null;
    private String dinvaliddateStr = null;
    private String stordocidStr = null;
    private ICBizEditorModel editorModel;
    private AbstractAppModel model;
    private String VONameStr = null;
    private IBillFieldsNoNeedCopy billFieldsNoNeedCopy;
    private boolean isMultiRef = false;
    private boolean isClearBatch = true;
    private boolean isICUsed = false;
    private static Map<Integer, String> dateMap = new HashMap();
    private int[] currentSelectRows;

    static {
        dateMap.put(EnumValiduntilunit_148.DAY.toIntValue(), "YYYY-MM-DD");
        dateMap.put(EnumValiduntilunit_148.MONTH.toIntValue(), "YYYY-MM");
    }

    public BatchCode() {
    }

    public void setCurrentSelectRows(int[] currentSelectRows) {
        this.currentSelectRows = currentSelectRows;
    }

    public void afterEdit(CardBodyAfterEditEvent e) {
        try {
            BillCardPanel card = e.getBillCardPanel();
            card.stopEditing();
            this.clearBodyCellValues(card, e.getRow(), new String[]{"vfree6"});
            String pk_material = (String)card.getBodyValueAt(e.getRow(), this.getMaterialStr());
            boolean isLotNo = MaterialPropertyUtil.isLotNo(pk_material, (String)null);
            BillItem billItem = card.getBodyItem(e.getKey());
            String batchStr = (String)card.getBodyValueAt(e.getRow(), "vbatchcode");
            BatchRefPane refpane = null;
            if (this.getEditorModel() != null) {
                TableColumn tc = 
				this.getEditorModel().getCardPanelWrapper()
				.getColumn(billItem.getName());
                refpane = (BatchRefPane)((BillCellEditor)tc.getCellEditor()).getComponent();
            }

            if (billItem.getComponent() instanceof BatchRefPane) {
                refpane = (BatchRefPane)billItem.getComponent();
            }

            if (refpane == null) {
                return;
            }

            BatchDlgParam queryParam = this.getBatchDlgParam(card, e.getRow());
            queryParam.setIsQueryZeroLot(UFBoolean.TRUE);
            BatchRefDlg dlg = refpane.getBatchRefDlg();
            List<BatchRefViewVO> voList = refpane.getRefVOs();
            if (this.currentSelectRows != null && this.currentSelectRows.length > 1) {
                this.handleMultilBatchcodeSelected(this.currentSelectRows, voList, card);
                return;
            }

            if (this.isICUsed && batchStr != null && voList == null) {
                this.handleBatchByHand(card, batchStr, e, queryParam, isLotNo, dlg);
                return;
            }

            if (batchStr != null || voList == null) {
                this.handleBatchByHand(card, batchStr, e, queryParam, isLotNo, dlg);
                return;
            }

            if (this.isMultiRef) {
                if (voList.size() > 1 && card.getBodyValueAt(card.getRowCount() - 1, this.getMaterialStr()) == null) {
                    this.clearBodyCellValues(card, card.getRowCount() - 1, new String[]{"vfree6", "csendstordocid", "vdef6_148"});
                }

                this.handleLotNoByRef(e, voList, isLotNo);
                return;
            }

            if (voList != null && voList.size() > 0) {
                if (voList.size() == 1) {
                    BatchRefViewVO batchcode = (BatchRefViewVO)voList.get(0);
                    this.dealbathcode(card, batchcode, e.getRow());
                } else {
                    List<Integer> addRowIndexes = new ArrayList();
                    int endindex = e.getRow() + voList.size();

                    for(int i = e.getRow(); i < endindex; ++i) {
                        addRowIndexes.add(i);
                    }

                    if (this.isICUsed) {
                        BatchRefViewVO batchcode = (BatchRefViewVO)voList.get(0);
                        this.dealbathcode(card, batchcode, e.getRow());
                    } else {
                        for(int j = 0; j < voList.size(); ++j) {
                            this.dealbathcode(card, (BatchRefViewVO)voList.get(j), (Integer)addRowIndexes.get(j));
                        }
                    }
                }
            }

            if (isLotNo) {
                this.setEditable(card, e.getRow());
            } else {
                this.setEditableWithNoLotNo(card, e.getRow());
            }
        } catch (BusinessException e1) {
            ExceptionUtils.wrappException(e1);
        }

    }

    private void handleMultilBatchcodeSelected(int[] rows, List<BatchRefViewVO> voList, BillCardPanel card) {
        Set<String> set = new HashSet();

        for(BatchRefViewVO refvo : voList) {
            String vfree6 = (String)refvo.getAttributeValue("vfree6");
            if (vfree6 != null) {
                set.add(vfree6);
            }
        }

        try {
            List<MedLotno_148> vos = (List)MDPersistenceService.lookupPersistenceQueryService().queryBillOfVOByPKs(MedLotno_148.class, (String[])set.toArray(new String[0]), false);

            for(int i = 0; i < rows.length; ++i) {
                int row = rows[i];
                String value = (String)((BatchRefViewVO)voList.get(i)).getAttributeValue("vfree6");
                if (value != null) {
                    for(MedLotno_148 lotno : vos) {
                        if (lotno.getPk_lotno().equals(value)) {
                            card.setBodyValueAt(lotno.getVlotno(), row, "vlotno_148");
                            card.setBodyValueAt(lotno.getDprodate(), row, "dproducedate_148");
                            card.setBodyValueAt(lotno.getVinvaliddate(), row, "vinvaliddate_148");
                            card.setBodyValueAt(value, row, "vfree6");
                            card.setBodyValueAt(value, row, "vdef6_148");
                            break;
                        }
                    }
                }
            }
        } catch (MetaDataException e) {
            ExceptionUtils.wrappException(e);
        }

    }

    private void dealbathcode(BillCardPanel card, BatchRefViewVO viewVO, int index) {
        card.setBodyValueAt(viewVO.getAttributeValue("vfree6"), index, "vfree6");
        card.setBodyValueAt(viewVO.getAttributeValue("vfree6"), index, "vdef6_148");
        card.setBodyValueAt(viewVO.getAttributeValue("dproducedate"), index, "dproducedate_148");
        if (viewVO.getAttributeValue("cwarehouseid") != null && this.getStordocidStr() != null) {
            if (card.getBodyItem(this.getStordocidStr()) != null) {
                card.setBodyValueAt(viewVO.getAttributeValue("cwarehouseid"), index, this.getStordocidStr());
            } else {
                card.setHeadItem(this.getStordocidStr(), viewVO.getAttributeValue("cwarehouseid"));
            }
        }

        this.exeformulas(card, index);
    }

    protected void handleBatchByHand(BillCardPanel card, String inputStr, CardBodyAfterEditEvent e, BatchDlgParam queryParam, boolean isLotNo, BatchRefDlg dlg) {
        int row = e.getRow();
        BatchcodeVO[] refVos = null;
        BatchRefViewVO viewVO = null;

        try {
            if (!StringUtil.isSEmptyOrNull(inputStr)) {
                BatchcodeVO queryVo = new BatchcodeVO();
                queryVo.setAttributeValue("cmaterialvid", this.getStringCellValue(card, row, this.getMaterialStr()));
                queryVo.setAttributeValue("vbatchcode", inputStr);
                refVos = ((IBatchCodeQryService)NCLocator.getInstance().lookup(IBatchCodeQryService.class)).queryBatchCode(queryVo);
                viewVO = dlg.getRefVO(this.getStringCellValue(card, row, this.getMaterialStr()), inputStr);
            }

            if (refVos == null || refVos.length == 0) {
                card.setBodyValueAt(inputStr, row, "vbatchcode");
                if (!this.isClearBatch()) {
                    card.setBodyValueAt((Object)null, row, "vbatchcode");
                }

                Object dpro_148 = card.getBodyValueAt(row, "dproducedate_148");
                if (dpro_148 != null) {
                    card.setBodyValueAt(dpro_148, row, this.getDproducedateStr());
                }

                MedLotno_148 queryLotnoVo = new MedLotno_148();
                queryLotnoVo.setAttributeValue("cmaterialid", this.getStringCellValue(card, row, this.getMaterialStr()));
                queryLotnoVo.setAttributeValue("vlotno", this.getStringCellValue(card, row, "vlotno_148"));
                MedLotno_148[] matchvos = null;
                if (queryLotnoVo.getVlotno() != null) {
                    matchvos = ((IMed_lotno_148_Maintain)NCLocator.getInstance().lookup(IMed_lotno_148_Maintain.class)).queryLotnoVO(queryLotnoVo);
                }

                if (matchvos != null && matchvos.length > 0) {
                    card.setBodyValueAt(matchvos[0].getPk_lotno(), row, "vfree6");
                    card.setBodyValueAt(matchvos[0].getPk_lotno(), row, "vdef6_148");
                    card.setBodyValueAt(matchvos[0].getDvaliduntil(), row, this.getDinvaliddateStr());
                } else {
                    this.reSetRelatedFileds(card, row, isLotNo);
                }

                card.setBodyValueAt((Object)null, row, "vdisinfectlot_148");
                card.setBodyValueAt((Object)null, row, "pk_agent_148");
                card.setBodyValueAt((Object)null, row, "ntaxprcin_148");
                card.setBodyValueAt((Object)null, row, "cproductorid");
                if (!this.isRecheckdateEditable((String)card.getHeadItem("pk_org").getValueObject(), this.getStringCellValue(card, row, this.getMaterialStr()))) {
                    card.setBodyValueAt((Object)null, row, "recheckdate_148");
                    card.setCellEditable(row, "recheckdate_148", false);
                } else if (StringUtil.isSEmptyOrNull(inputStr)) {
                    card.setBodyValueAt((Object)null, row, "recheckdate_148");
                    card.setCellEditable(row, "recheckdate_148", false);
                } else {
                    card.setBodyValueAt((Object)null, row, "recheckdate_148");
                    card.setCellEditable(row, "recheckdate_148", true);
                }

                card.setBodyValueAt((Object)null, row, "vfree5");
                if (isLotNo) {
                    this.setEditable(card, row);
                } else {
                    this.setEditableWithNoLotNo(card, row);
                }

                return;
            }

            BatchcodeVO refVO = refVos[0];
            if (StringUtil.isStringEqual(refVO.getAttributeValue("bseal").toString(), UFBoolean.TRUE.toString())) {
                return;
            }

            card.setBodyValueAt(refVO.getAttributeValue("vlotno_148"), row, "vlotno_148");
            card.setBodyValueAt(refVO.getAttributeValue("dproducedate"), row, "dproducedate_148");
	card.setBodyValueAt(refVO.getAttributeValue("dvalidate"), row, "dvalidate");
			card.setBodyValueAt(refVO.getAttributeValue("dproducedate"), row, "dproducedate");
            card.setBodyValueAt(refVO.getAttributeValue("pk_lotno_148"), row, "vfree6");
            card.setBodyValueAt(refVO.getAttributeValue("pk_lotno_148"), row, "vdef6_148");
            String vinvaliddate_148 = (String)refVO.getAttributeValue("vinvaliddate_148");
            if (StringUtil.isSEmptyOrNull(vinvaliddate_148)) {
                vinvaliddate_148 = (String)refVO.getAttributeValue("vinvaliddate");
            }
			
			           if (StringUtil.isSEmptyOrNull(vinvaliddate_148) && 
					   			refVO.getAttributeValue("dvalidate") != null) {
                vinvaliddate_148 = ((UFDate)refVO.getAttributeValue("dvalidate")).toString();
            }

            card.setBodyValueAt(vinvaliddate_148, row, "vinvaliddate_148");
            card.setBodyValueAt(refVO.getAttributeValue("nkhchbprc_148"), row, "nkhchbprc_148");
            card.setBodyValueAt(refVO.getAttributeValue("vdisinfectlot_148"), row, "vdisinfectlot_148");
            card.setBodyValueAt(refVO.getAttributeValue("pk_agent_148"), row, "pk_agent_148");
            card.setBodyValueAt(refVO.getAttributeValue("pk_supplier_148"), row, "cvendorid");
            card.setBodyValueAt(refVO.getAttributeValue("ntaxprcin_148"), row, "ntaxprcin_148");
            card.setBodyValueAt(refVO.getAttributeValue("pk_producer_148"), row, "cproductorid");
            card.setBodyValueAt(refVO.getAttributeValue("recheckdate_148"), row, "recheckdate_148");
            if (refVO.getAttributeValue("recheckdate_148") != null && !refVO.getAttributeValue("recheckdate_148").equals("")) {
                card.setCellEditable(row, "recheckdate_148", false);
            }

            this.clearLotNoInfo(card, row);
            if (this.getStordocidStr() != null && viewVO != null) {
                if(card.getBodyValueAt(row,this.getStordocidStr())==null){
                    card.setBodyValueAt(viewVO.getAttributeValue("cwarehouseid"), row, this.getStordocidStr());
                }
            }

            this.exeformulas(card, row);
            if (isLotNo) {
                this.setEditable(card, row);
            } else {
                this.setEditableWithNoLotNo(card, row);
            }
        } catch (Exception ex) {
            ExceptionUtils.wrappException(ex);
        }

    }

    private void clearLotNoInfo(BillCardPanel card, int row) {
        
			String[] itemArray = new String[]{
			"vapprovalnumber_148", "vregistrationno_148", 
		"cinnerpackspec_148", "cmediumpackspec_148", "couterpackspec_148", "ctinypack_148", 
		"cinnerpack_148", "cmediumpack_148", "couterpack_148"};
        this.clearBodyCellValues(card, row, itemArray);
        this.exelotnoformulas(card, row);
    }

    private void exelotnoformulas(BillCardPanel card, int row) {
        String vfree6 = (String)card.getBodyValueAt(row, "vfree6");
        String pk_material = (String)card.getBodyValueAt(row, this.getMaterialStr());

        try {
            MedLotno_148[] lotno = ((IMed_lotno_148_Maintain)NCLocator.getInstance().lookup(IMed_lotno_148_Maintain.class)).queryLotnoVOByPks(new String[]{vfree6});
            MaterialVO[] mater = ((IMaterialBaseInfoQueryService)NCLocator.getInstance().lookup(IMaterialBaseInfoQueryService.class)).queryDataByPks(new String[]{pk_material});
            card.setBodyValueAt(lotno != null && lotno.length != 0 && lotno[0].getVregistrationno() != null ? lotno[0].getVregistrationno() : mater[0].getAttributeValue("vregistrationno_148"), row, "vregistrationno_148");
            card.setBodyValueAt(lotno != null && lotno.length != 0 && lotno[0].getVapprovalnumber() != null ? lotno[0].getVapprovalnumber() : mater[0].getAttributeValue("vapprovalnumber_148"), row, "vapprovalnumber_148");
            card.setBodyValueAt(lotno != null && lotno.length != 0 && lotno[0].getCouterpack() != null ? lotno[0].getCouterpack() : mater[0].getAttributeValue("couterpack_148"), row, "couterpack_148");
            card.setBodyValueAt(lotno != null && lotno.length != 0 && lotno[0].getCouterpackspec() != null ? lotno[0].getCouterpackspec() : mater[0].getAttributeValue("couterpackspec_148"), row, "couterpackspec_148");
            card.setBodyValueAt(lotno != null && lotno.length != 0 && lotno[0].getCmediumpack() != null ? lotno[0].getCmediumpack() : mater[0].getAttributeValue("cmediumpack_148"), row, "cmediumpack_148");
            card.setBodyValueAt(lotno != null && lotno.length != 0 && lotno[0].getCmediumpackspec() != null ? lotno[0].getCmediumpackspec() : mater[0].getAttributeValue("cmediumpackspec_148"), row, "cmediumpackspec_148");
            card.setBodyValueAt(lotno != null && lotno.length != 0 && lotno[0].getCinnerpack() != null ? lotno[0].getCinnerpack() : mater[0].getAttributeValue("cinnerpack_148"), row, "cinnerpack_148");
            card.setBodyValueAt(lotno != null && lotno.length != 0 && lotno[0].getCinnerpackspec() != null ? lotno[0].getCinnerpackspec() : mater[0].getAttributeValue("cinnerpackspec_148"), row, "cinnerpackspec_148");
            card.setBodyValueAt(lotno != null && lotno.length != 0 && lotno[0].getCtinypack() != null ? lotno[0].getCtinypack() : mater[0].getAttributeValue("ctinypack_148"), row, "ctinypack_148");
        } catch (BusinessException e) {
            Logger.error(e);
        }

    }

    protected void handleLotNoByRef(CardBodyAfterEditEvent e, List<BatchRefViewVO> refVos, boolean isLotNo) {
        BillCardPanel card = e.getBillCardPanel();
        int editRow = e.getRow();
        BatchCodeRefMoreSelectUtil util = new BatchCodeRefMoreSelectUtil(card);
        util.setBillFieldsNoNeedCopy(this.getBillFieldsNoNeedCopy());
        util.setVoNameStr(this.getVONameStr());
        util.setStordocidStr(this.getStordocidStr());
        util.setModel(this.getModel());
        int[] rows = util.refMoreSelected(editRow, e.getKey(), true);

        for(int i = 0; i < rows.length; ++i) {
            if (isLotNo) {
                this.setEditable(card, rows[i]);
            }
        }

    }

    private void exeformulas(BillCardPanel card, int row) {
        String vfree6 = (String)card.getBodyValueAt(row, "vfree6");
        String pk_batchcode = (String)card.getBodyValueAt(row, "pk_batchcode");

        try {
            MedLotno_148[] lotno = ((IMed_lotno_148_Maintain)NCLocator.getInstance().lookup(IMed_lotno_148_Maintain.class)).queryLotnoVOByPks(new String[]{vfree6});
            card.setBodyValueAt(lotno != null && lotno.length != 0 && lotno[0] != null ? lotno[0].getVlotno() : null, row, "vlotno_148");
        } catch (BusinessException e) {
            Logger.error(e);
        }

    }

    private void reSetRelatedFileds(BillCardPanel card, int row, boolean isLotNo) 
	throws Exception
	 {
        Object dProduceDate = card.getBodyValueAt(row, this.getDproducedateStr());
        if (this.getDproducedateStr() != null && dProduceDate != null) {
            if (isLotNo) {
                if (card.getBodyValueAt(row, this.getMaterialStr() + ".ivaliduntilunit_148") != null) {
                    int ivaliduntilunit = (Integer)card.getBodyValueAt(row, this.getMaterialStr() + ".ivaliduntilunit_148");
                    int iexpirydate = (Integer)card.getBodyValueAt(row, this.getMaterialStr() + ".iexpirydate_148");
                    int iexpiryunit = (Integer)card.getBodyValueAt(row, this.getMaterialStr() + ".iexpiryunit_148");
					                    String vinvaliddate = JXQUtil.getValidDateByProduceDate(
										((UFDate)dProduceDate).toString(), (String)dateMap.get(ivaliduntilunit), iexpiryunit, iexpirydate);
                    String dvaliduntil = JXQUtil.getDValidDateByValidDate(vinvaliddate);
                    if (this.getDinvaliddateStr() != null) {
                        card.setBodyValueAt(dvaliduntil, row, this.getDinvaliddateStr());
                    }
                } else {
                    ExceptionUtils.wrappException(new BusinessException("无法生成失效日期！  原因：物料启用了批号管理，没有启用效期管理。 解决方案：关闭库存保质期管理或启用效期管理。"));
                }
				
            } else {
                card.setBodyValueAt((Object)null, row, "dproducedate_148");
                card.setBodyValueAt((Object)null, row, "vinvaliddate_148");
            }
        }

    }

    private BatchDlgParam getBatchDlgParam(BillCardPanel card, int row) {
        BatchDlgParam para = new BatchDlgParam();
        para.setCmaterialoid(this.getStringCellValue(card, row, this.getCmaterialoidStr()));
        para.setCmaterialvid(this.getStringCellValue(card, row, this.getMaterialStr()));
        para.setLoginContext(this.context);
        para.getOnhandDim().setCwarehouseid(this.getStringCellValue(card, row, "pk_receivestore"));
        para.getOnhandDim().setPk_org(this.getStringCellValue(card, row, "pk_org"));
        para.getOnhandDim().setCastunitid(this.getStringCellValue(card, row, "castunitid"));
        para.getOnhandDim().setPk_group(this.getStringCellValue(card, row, "pk_group"));
        if (card.getHeadItem("pk_supplier") != null) {
            para.getOnhandDim().setCvendorid((String)card.getHeadItem("pk_supplier").getValueObject());
        }

        para.getOnhandDim().setCprojectid(this.getStringCellValue(card, row, "cprojectid"));
        para.getOnhandDim().setCproductorid(this.getStringCellValue(card, row, "cproductorid"));
        para.getOnhandDim().setVchangerate(this.getStringCellValue(card, row, "vchangerate"));

        for(int i = 1; i < 11; ++i) {
            para.getOnhandDim().setAttributeValue("VFREE" + i, this.getStringCellValue(card, row, "VFREE" + i));
        }

        return para;
    }

    public String getStringCellValue(BillCardPanel card, int rowIndex, String itemKey) {
        Object cellValue = card.getBodyValueAt(rowIndex, itemKey);
        return cellValue == null ? null : (String)cellValue;
    }

    protected String[] getNeedNotifiedItems() {
        String[] itemArray = new String[]{this.getDproducedateStr(), "dproducedate_148", "vinvaliddate_148", "dvalidate", "dvaliduntil_148"};
        return itemArray;
    }

    protected String[] getNeedNotifiedItemsWithNoLotNo() {
        String[] itemArray = new String[]{this.getDproducedateStr(), "dproducedate_148", "vinvaliddate_148"};
        return itemArray;
    }

    public void clearBodyCellValues(BillCardPanel card, int rowIndex, String[] itemKeys) {
        int j = 0;

        for(int itemLength = itemKeys.length; j < itemLength; ++j) {
            card.setBodyValueAt((Object)null, rowIndex, itemKeys[j]);
        }

    }

    private void setEditable(BillCardPanel panel, int row) {
        if (panel.getBodyValueAt(row, "pk_batchcode") == null && panel.getBodyValueAt(row, "vfree6") == null) {
            String[] var10;
            for(String item : var10 = this.getNeedNotifiedItems()) {
                panel.getBillModel().getRowAttribute(row).addCellEdit(item, true);
            }
        } else {
            String[] var6;
            for(String item : var6 = this.getNeedNotifiedItems()) {
                panel.getBillModel().getRowAttribute(row).addCellEdit(item, false);
            }
        }

    }

    private void setEditableWithNoLotNo(BillCardPanel panel, int row) {
        if (panel.getBodyValueAt(row, "pk_batchcode") == null && panel.getBodyValueAt(row, "vfree6") == null) {
            String[] var10;
            for(String item : var10 = this.getNeedNotifiedItemsWithNoLotNo()) {
                panel.getBillModel().getRowAttribute(row).addCellEdit(item, true);
            }
        } else {
            String[] var6;
            for(String item : var6 = this.getNeedNotifiedItemsWithNoLotNo()) {
                panel.getBillModel().getRowAttribute(row).addCellEdit(item, false);
            }
        }

    }

    public String getMaterialStr() {
        return this.materialStr;
    }

    public void setMaterialStr(String materialStr) {
        this.materialStr = materialStr;
    }

    public LoginContext getContext() {
        return this.context;
    }

    public void setContext(LoginContext context) {
        this.context = context;
    }

    public String getCmaterialoidStr() {
        return this.cmaterialoidStr;
    }

    public void setCmaterialoidStr(String cmaterialoidStr) {
        this.cmaterialoidStr = cmaterialoidStr;
    }

    public String getDproducedateStr() {
        return this.dproducedateStr;
    }

    public void setDproducedateStr(String dproducedateStr) {
        this.dproducedateStr = dproducedateStr;
    }

    public String getDinvaliddateStr() {
        return this.dinvaliddateStr;
    }

    public void setDinvaliddateStr(String dinvaliddateStr) {
        this.dinvaliddateStr = dinvaliddateStr;
    }

    public ICBizEditorModel getEditorModel() {
        return this.editorModel;
    }

    public void setEditorModel(ICBizEditorModel editorModel) {
        this.editorModel = editorModel;
    }

    public String getStordocidStr() {
        return this.stordocidStr;
    }

    public void setStordocidStr(String stordocidStr) {
        this.stordocidStr = stordocidStr;
    }

    public AbstractAppModel getModel() {
        return this.model;
    }

    public void setModel(AbstractAppModel model) {
        this.model = model;
    }

    public String getVONameStr() {
        return this.VONameStr;
    }

    public void setVONameStr(String vONameStr) {
        this.VONameStr = vONameStr;
    }

    public IBillFieldsNoNeedCopy getBillFieldsNoNeedCopy() {
        return this.billFieldsNoNeedCopy;
    }

    public void setBillFieldsNoNeedCopy(IBillFieldsNoNeedCopy billFieldsNoNeedCopy) {
        this.billFieldsNoNeedCopy = billFieldsNoNeedCopy;
    }

    public boolean isMultiRef() {
        return this.isMultiRef;
    }

    public void setMultiRef(boolean isMultiRef) {
        this.isMultiRef = isMultiRef;
    }

    public boolean isClearBatch() {
        return this.isClearBatch;
    }

    public void setClearBatch(boolean isClearBatch) {
        this.isClearBatch = isClearBatch;
    }

    public boolean isICUsed() {
        return this.isICUsed;
    }

    public void setICUsed(boolean isICUsed) {
        this.isICUsed = isICUsed;
    }

    private boolean isRecheckdateEditable(String pk_org, String pk_material) 
	throws UifException 
	{
	        MaterialStockVO[] materialStockVOs = (MaterialStockVO[])
			HYPubBO_Client.queryByCondition(MaterialStockVO.class, " pk_material = '" + pk_material + "' " + "and pk_org='" + pk_org + "'");
        if (materialStockVOs != null && materialStockVOs.length != 0) {
            Object isrecheck_148 = HYPubBO_Client.findColValue("med_marstock_148", "isrecheck_148", " pk_materialstock = '" + materialStockVOs[0].getPk_materialstock() + "'");
            Object wholemanaflag = materialStockVOs[0].getWholemanaflag();
            return wholemanaflag != null && isrecheck_148 != null;
        } else {
            return false;
        }
    }
}
