/** <a href="http://www.cpupk.com/decompiler">Eclipse Class Decompiler</a> plugin, Copyright (c) 2017 Chen Chao. */
package nc.ui.ic.m4c.action.extend;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.Action;
import javax.swing.KeyStroke;

import nc.bs.framework.common.NCLocator;
import nc.bs.ic.cst.PickAutoConst;
import nc.itf.ic.onhand.OnhandResService;
import nc.md.persist.framework.IMDPersistenceQueryService;
import nc.ui.ic.general.handler.MainNumHandler;
import nc.ui.ic.general.handler.NumHandler;
import nc.ui.ic.general.model.ICGenBizEditorModel;
import nc.ui.ic.general.view.uientity.ICBizCardBodyEntity;
import nc.ui.ic.general.view.uientity.ICUIBillEntity;
import nc.ui.ic.location.util.BarcodeInputUIUtil;
import nc.ui.ic.material.query.InvInfoUIQuery;
import nc.ui.ic.pub.handler.card.ICCardEditEventHandler;
import nc.ui.ic.pub.util.CardPanelWrapper;
import nc.ui.ic.pub.util.UIBusiCalculator;
import nc.ui.pub.beans.UITable;
import nc.ui.pub.bill.BillModel;
import nc.ui.pubapp.uif2app.event.card.CardBodyAfterEditEvent;
import nc.ui.scmpub.action.SCMActionInitializer;
import nc.ui.uif2.NCAction;
import nc.ui.uif2.ShowStatusBarMsgUtil;
import nc.vo.bd.material.stock.MaterialStockVO;
import nc.vo.ic.batchcode.BatchSynchronizer;
import nc.vo.ic.batchcode.ICBatchFields;
import nc.vo.ic.general.deal.ICBillValueSetter;
import nc.vo.ic.general.define.ICBillBodyVO;
import nc.vo.ic.general.define.ICBillVO;
import nc.vo.ic.general.define.InOutFlag;
import nc.vo.ic.general.define.MetaNameConst;
import nc.vo.ic.general.util.ICLocationUtil;
import nc.vo.ic.general.util.InOutHelp;
import nc.vo.ic.location.ICLocationVO;
import nc.vo.ic.material.define.InvCalBodyVO;
import nc.vo.ic.material.define.InvMeasVO;
import nc.vo.ic.onhand.define.ICBillPickResults;
import nc.vo.ic.onhand.entity.OnhandDimVO;
import nc.vo.ic.pub.define.ICBillTableInfo;
import nc.vo.ic.pub.define.ICPubMetaNameConst;
import nc.vo.ic.pub.lang.OnhandRes;
import nc.vo.ic.pub.util.CollectionUtils;
import nc.vo.ic.pub.util.NCBaseTypeUtils;
import nc.vo.ic.pub.util.StringUtil;
import nc.vo.ic.pub.util.VOEntityUtil;
import nc.vo.ic.pub.util.ValueCheckUtil;
import nc.vo.ic.sncode.SnCodeUtil;
import nc.vo.medpub.med_lotno_148.MedLotno_148;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scmf.ic.mbatchcode.BatchcodeVO;
import nc.vo.scmpub.res.SCMActionCode;

/**
 * sunjiand
 * Aug 28, 2014
 */
public class PickAutoActionEx extends NCAction {
	  private static final long serialVersionUID = 1L;

	  private boolean bCalcMny = false;// 同时计算金额 销售出，采购入需要
	  private Map<String,UFBoolean> isasunitstore;

	  Map<String, InvCalBodyVO> invcalbodysmap =
	      new HashMap<String, InvCalBodyVO>();

	 private ICGenBizEditorModel editorModel;

	  public PickAutoActionEx() {
	    SCMActionInitializer.initializeAction(this, SCMActionCode.IC_PICKAUTO);
	    this.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_U,
	        InputEvent.SHIFT_MASK));
	  }

	  @Override
	  public void doAction(ActionEvent e) throws Exception {
	    if (!this.onCheck()) {
	      return;
	    }
	    // 此处为了保证处理好的数据能传递给自动拣货，只能取一次，否则billvo对象不一样导致无效
	    ICBillVO billvo = this.getBillVO();

	    this.preprocess(billvo);
	    OnhandResService resserver =
	      NCLocator.getInstance().lookup(OnhandResService.class);
	    ICBillPickResults results = resserver.pickAuto(billvo);
	    if (results == null) {
	      this.setBodyNoteOnhandErro(billvo.getBodys(), results);
	      this.showStatusMsg();
	      return;
	    }
	    this.loadInvCalBodyInfo();
	    this.setBodyNoteOnhandErro(billvo.getBodys(), results);
	    this.clearBodyDetailData();
	    UITable table =
	      this.getEditorModel().getICBizView().getBillCardPanel().getBillTable();
	    int row = 0;

	    boolean needCalculate =
	      this.getEditorModel().getCardPanelWrapper().getBillModel()
	      .isNeedCalculate();
	    try {
	      this.getEditorModel().getCardPanelWrapper().getBillModel()
	      .setNeedCalculate(false);
	      this.getBAstunitStorebalance();
	      // 设置数据
	      for (int i = 0, loop = billvo.getBodys().length; i < loop; i++) {
	        ICBillBodyVO[] bodys = results.getPickBodys(i);
	        if (bodys == null || bodys.length <= 0) {
	          row++;
	          continue;
	        }
	        table.getSelectionModel().setSelectionInterval(row, row);
	        // 清空原始行号，用拣货结果设置行号，否则拆行后会行号重复
	        this.getEditorModel().getCardPanelWrapper().setBodyValueAt(null, row, ICPubMetaNameConst.CROWNO);
	        this.getEditorModel().getCardPanelWrapper().copyLine();
	        for (int k = bodys.length - 1; k >= 0; k--) {
	          if (k == bodys.length - 1) {
	            this.setOnhandDataToBody(bodys[k], table.getSelectedRow());
	            if (this.getEditorModel().getCardPanelWrapper().getRowState(null,
	                row) != BillModel.ADD) {
	              this.getEditorModel().getCardPanelWrapper().setRowState(null,
	                  row, BillModel.MODIFICATION);
	            }
	            continue;
	          }
	          this.getEditorModel().getCardPanelWrapper().pasteLine();
	          
	          int setrow = table.getSelectedRow() - 1;
	          this.setQtunitnumNum4PastLine(setrow);
	          this.setVfree6RealValue(bodys[k],setrow);    //修正出库单自动拣货后vfree6在界面vo的value实际是被复制那一行的value------shangyu3 2015-11-12
	          this.setOnhandDataToBody(bodys[k], setrow);
	          table.getSelectionModel().setSelectionInterval(setrow, setrow);
	        }
	        row = row + bodys.length;
	      }
	      this.setNshouldNumNull(billvo.getBodys());
	      // 同步表体批次辅助字段
	      new BatchSynchronizer(new ICBatchFields()).fillBatchVOtoBill(this
	          .getEditorModel().getICUIBillEntity().getBodys());
	      
	      
			Set<String> medLotNoSet = new HashSet<String>();//存储批号id
			Set<String> medBatchSet = new HashSet<String>();//存储批次id
	      ICBizCardBodyEntity[] bodyEntitys = this.getEditorModel().getICUIBillEntity().getBodys();
	      if(bodyEntitys !=null&&bodyEntitys.length>0){
	    	  for(ICBizCardBodyEntity bodyEntity : bodyEntitys){
		    	  String vfree6 = bodyEntity.getVfree6();
		    	  String pk_batchcode = (String)bodyEntity.getAttributeValue("pk_batchcode");
		    	  if(vfree6!=null){
		    		  medLotNoSet.add(vfree6);
		    	  }
		    	  if(pk_batchcode!=null){
		    		  medBatchSet.add(pk_batchcode);
		    	  }
		      }
	      }
	      
		List<MedLotno_148> medlotList = null;
		List<BatchcodeVO> batcodeList = null;
		Map<String,MedLotno_148> medlotMap = new HashMap<String,MedLotno_148>();
		Map<String,BatchcodeVO> batcodeMap = new HashMap<String,BatchcodeVO>();
		IMDPersistenceQueryService queryService = NCLocator.getInstance().lookup(IMDPersistenceQueryService.class);
		
		if(medLotNoSet!=null&&!medLotNoSet.isEmpty()){
			medlotList =  (List<MedLotno_148>) queryService.queryBillOfVOByPKs(MedLotno_148.class, medLotNoSet.toArray(new String[medLotNoSet.size()]), false);
			for(MedLotno_148 medLotno_148 : medlotList){
				medlotMap.put(medLotno_148.getPk_lotno(), medLotno_148);
			}
		}
		if(medBatchSet!=null&&!medBatchSet.isEmpty()){
			
			batcodeList = (List<BatchcodeVO>) queryService.queryBillOfVOByPKs(BatchcodeVO.class, medBatchSet.toArray(new String[medBatchSet.size()]), false);
			for(BatchcodeVO batchcodeVO : batcodeList){
				batcodeMap.put(batchcodeVO.getPk_batchcode(), batchcodeVO);
			}
		}
		
	   if(bodyEntitys !=null&&bodyEntitys.length>0){
	    	  for(int i= 0; i< bodyEntitys.length;i++){
		    	  String vfree6 = bodyEntitys[i].getVfree6();
		    	  String pk_batchcode = (String)bodyEntitys[i].getAttributeValue("pk_batchcode");
		    	  if(pk_batchcode!=null&&batcodeMap.containsKey(pk_batchcode)){
						BatchcodeVO batchcode = batcodeMap.get(pk_batchcode);
						this.getEditorModel().getCardPanelWrapper().getBillCardPanel().setBodyValueAt(batchcode.getDproducedate(), i, "dproducedate");
						this.getEditorModel().getCardPanelWrapper().getBillCardPanel().setBodyValueAt(batchcode.getDvalidate(), i, "dvalidate");
						this.getEditorModel().getCardPanelWrapper().getBillCardPanel().setBodyValueAt(batchcode.getAttributeValue("vinvaliddate_148"), i, "vinvaliddate_148");
		    	  }
		    	  if(vfree6!=null&&medlotMap.containsKey(vfree6)){
						MedLotno_148 medlotno = medlotMap.get(vfree6);
						this.getEditorModel().getCardPanelWrapper().getBillCardPanel().setBodyValueAt(medlotno.getDprodate(), i, "dproducedate");
						this.getEditorModel().getCardPanelWrapper().getBillCardPanel().setBodyValueAt(medlotno.getDvaliduntil(), i, "dvalidate");
						this.getEditorModel().getCardPanelWrapper().getBillCardPanel().setBodyValueAt(medlotno.getVinvaliddate(), i, "vinvaliddate_148");
		    	  }
		      }
	      }
		
		
	      
	    }
	    finally {
	      this.getEditorModel().getCardPanelWrapper().getBillModel()
	      .setNeedCalculate(needCalculate);
	      this.getEditorModel().getCardPanelWrapper().getBillModel()
	      .loadLoadRelationItemValue();
	    }
	    this.showStatusMsg();
//	    String[] formulas = {
//	    		"vlotno_148->getcolvalue(med_lotno_148,vlotno,pk_lotno,vfree6)",
//	    		"dprodate_lotno->getcolvalue(med_lotno_148,dprodate,pk_lotno,vfree6)",
//	    		"vinvaliddate_lotno->getcolvalue(med_lotno_148,vinvaliddate,pk_lotno,vfree6)",
//	    		"dprodate_batchcode->getcolvalue(scm_batchcode,dproducedate,pk_batchcode,pk_batchcode)",
//	    		"vinvaliddate_batchcode->getcolvalue(med_batchcode_148,vinvaliddate,pk_batchcode,pk_batchcode)",
//	    		"dproducedate->iif(!isempty(vfree6),dprodate_lotno,(iif(!isempty(pk_batchcode),dprodate_batchcode,null)))",
//	    		"dproducedate_148->iif(!isempty(vfree6),dprodate_lotno,(iif(!isempty(pk_batchcode),dprodate_batchcode,null)))",
//	    		"vinvaliddate_148->iif(!isempty(vfree6),vinvaliddate_lotno,(iif(!isempty(pk_batchcode),vinvaliddate_batchcode,null)))"
//	    };
	    //add by chenqk
//	    String[] formulas = {
//	    		"vlotno_148->getcolvalue(med_lotno_148,vlotno,pk_lotno,vdef6_148)",
//	    		"dprodate_lotno->getcolvalue(med_lotno_148,dprodate,pk_lotno,vdef6_148)",
//	    		"vinvaliddate_lotno->getcolvalue(med_lotno_148,vinvaliddate,pk_lotno,vdef6_148)",
//	    		"dprodate_batchcode->getcolvalue(scm_batchcode,dproducedate,pk_batchcode,pk_batchcode)",
//	    		"vinvaliddate_batchcode->getcolvalue(med_batchcode_148,vinvaliddate,pk_batchcode,pk_batchcode)",
//	    		"dproducedate->iif(!isempty(vdef6_148),dprodate_lotno,(iif(!isempty(pk_batchcode),dprodate_batchcode,null)))",
//	    		"dproducedate_148->iif(!isempty(vdef6_148),dprodate_lotno,(iif(!isempty(pk_batchcode),dprodate_batchcode,null)))",
//	    		"vinvaliddate_148->iif(!isempty(vdef6_148),vinvaliddate_lotno,(iif(!isempty(pk_batchcode),vinvaliddate_batchcode,null)))"
//	    };
//	    this.getEditorModel().getCardPanelWrapper().getBillModel().execFormulas(formulas);
	  }
	  
	  /** 由于粘贴行会把复制行的报价数量带过来，导致浮动换算率时计算报价换算率错误*/
	  private void setQtunitnumNum4PastLine(int row){
	    this.getEditorModel().getCardPanelWrapper()
	        .setBodyValueAt(null, row, MetaNameConst.NQTUNITNUM);
	  }
	  
	  private void setVfree6RealValue(ICBillBodyVO bodys, int row) {
		  this.getEditorModel().getCardPanelWrapper()
	      .setBodyValueAt(bodys.getVfree6(), row,"vfree6");
		  this.getEditorModel().getCardPanelWrapper().setBodyValueAt(bodys.getVfree6(), row,"vdef6_148");//add by chenqk
		
	}
	  
	  /**
	   * 清除应发数量，只保留原始行
	   * 方法功能描述：
	   * <p>
	   * <b>参数说明</b>
	   * @param bodys
	   * <p>
	   * @since 6.3
	   * @author zhaofeid
	   * @time 2013-7-31 下午04:45:39
	   */
	  private void setNshouldNumNull(ICBillBodyVO[] bodys){
	    Set<String> origRowNo = new HashSet<String>();
	    for(ICBillBodyVO body : bodys){
	      String crowno = body.getCrowno();
	      origRowNo.add(crowno);
	    }
	    int rowCount = this.getEditorModel().getCardPanelWrapper().getRowCount(null);
	    for(int i = 0; i < rowCount; i++){
	      String rowno = this.getEditorModel().getCardPanelWrapper().getBodyValueAt_String(i, ICPubMetaNameConst.CROWNO);
	      if(!origRowNo.contains(rowno)){
	        this.getEditorModel().getCardPanelWrapper()
	            .setBodyValueAt(null, i, ICPubMetaNameConst.NSHOULDNUM);
	        this.getEditorModel().getCardPanelWrapper()
	            .setBodyValueAt(null, i, ICPubMetaNameConst.NSHOULDASSISTNUM);
	      }
	    }
	  }

	  /**
	   * 拣货之前，清除本次编辑单品信息
	   */
	  private void clearBodyDetailData() {
	    int rowcnt =
	        this.getEditorModel().getICBizView().getBillCardPanel().getRowCount();
	    if (rowcnt <= 0) {
	      return;
	    }
	    for (int i = 0; i < rowcnt; i++) {
	      this.getEditorModel().clearBodyDetailData(i);
	    }

	  }
	  public ICGenBizEditorModel getEditorModel() {
	    return this.editorModel;
	  }

	  /**
	   * @return bCalcMny
	   */
	  public boolean isBCalcMny() {
	    return this.bCalcMny;
	  }

	  /**
	   * @param calcMny 要设置的 bCalcMny
	   */
	  public void setBCalcMny(boolean calcMny) {
	    this.bCalcMny = calcMny;
	  }

	  public void setEditorModel(ICGenBizEditorModel editorModel) {
	    this.editorModel = editorModel;
	  }

	  /**
	   * 主数量编辑后处理
	   * @param row
	   * @param newnum
	   * @param oldnum
	   */
	  private void caculateAfterNumEdit(int row, UFDouble newnum, UFDouble oldnum) {
	    CardBodyAfterEditEvent event =
	      new CardBodyAfterEditEvent(this.getEditorModel().getCardPanelWrapper()
	          .getBillCardPanel(), null, row, ICPubMetaNameConst.NNUM, newnum,
	          oldnum);
	    event.setContext(this.getEditorModel().getContext().getLoginContext());
//	    MainNumHandler handler = new MainNumHandler();
	  //wangsyf add 2020-06-15 ***************************************************************************************
		String invpk = (String) editorModel.getCardPanelWrapper().getBodyValueAt(row, ICPubMetaNameConst.CMATERIALVID);
		String measpk = (String) editorModel.getCardPanelWrapper().getBodyValueAt(row, ICPubMetaNameConst.CASTUNITID);
		String pk_org = editorModel.getCardPanelWrapper().getHeadValue_String("pk_org");
		boolean bSerialmanaflag = SnCodeUtil.bSerialmanaflag(pk_org, invpk);
		InvMeasVO invmeasvo = InvInfoUIQuery.getInstance().getInvInfoQuery().getInvMeasVO(invpk, measpk);
		ICCardEditEventHandler handler = null;
		  if(bSerialmanaflag && invmeasvo != null && !invmeasvo.getFixedflag().booleanValue() && invmeasvo.getIsstorebalance().booleanValue()){
			  handler = new NumHandler();
			  
		  }else{
			  handler = new MainNumHandler();
		  }
//wangsyf add 2020-06-15 ***************************************************************************************
	    handler.setEditorModel(this.getEditorModel());
	    handler.afterCardBodyEdit(event);
	  }

	  /**
	   * 处理货位信息的辅数量，进行重新计算，并处理尾差，解决存量不足时货位单品信息数量错误的问题。
	   @param body
	   @param cal
	   */
	  private void processLocAstNum(ICBillBodyVO body, UIBusiCalculator cal) {
	    ICLocationVO[] locs = body.getLocationVOs();   
	    if (ValueCheckUtil.isNullORZeroLength(locs))
	      return;
	    
	    UFDouble bodynum = body.getNassistnum();
	    UFDouble nsum = UFDouble.ZERO_DBL;
	    UFDouble nassistnum = UFDouble.ZERO_DBL;
	  //wangsyf add 2020-06-15 ***************************************************************************************
		InvMeasVO invMeasVO = InvInfoUIQuery.getInstance().getInvInfoQuery().getInvMeasVO(body.getCmaterialvid(), body.getCastunitid());
		boolean fixedFlag = ValueCheckUtil.isTrue(invMeasVO == null ? UFBoolean.TRUE : invMeasVO.getFixedflag());
//wangsyf add 2020-06-15 ***************************************************************************************
	    for (int i = 0; i < locs.length - 1; i++) {
	      // 此处无论是否单品管理，都需要处理辅数量，否则有货位信息时，更新存量会有问题
	      nassistnum =
	        cal.calculateAstNum(locs[i].getNnum(),
	            body.getVchangerate(), body.getCasscustid());
	      locs[i].setNassistnum(nassistnum);
	      nsum = NCBaseTypeUtils.add(nsum, nassistnum);
	    }
	    if (locs.length != 1) {
	      locs[locs.length - 1].setNassistnum(NCBaseTypeUtils.sub(bodynum, nsum));
	    }
	    else if (locs.length == 1) {
	      locs[0].setNassistnum(bodynum);
	    }
	  }
	  /**
	   * 利用工具获得界面数据
	   * @return
	   */
	  private ICBillVO getBillVO() {
	    // 此处如果用 getValue(),逻辑型的自定义项即使没有启用，默认值为N而不是null，导致查询存量条件错误
	    // 经验证，该方法不起作用
	    return this
	    .getEditorModel()
	    .getCardPanelWrapper()
	    .getCardPanelValueUtils()
	    .getBillValueVO(
	        ICBillTableInfo.getICBillTableInfo(
	            this.getEditorModel().getICBizModel().getBillTypeEnum())
	            .getBillClass());
	  }

	  /**
	   * 拣货之前，清除旧的备注
	   * 
	   * @param uiBodys
	   */
	  private void clearBodyNote() {
	    CardPanelWrapper wrapper = this.getEditorModel().getCardPanelWrapper();
	    for (int i = 0; i < wrapper.getRowCount(null); i++) {
	      wrapper.setBodyValueAt(null, i, ICPubMetaNameConst.NNUM);
	      wrapper.setBodyValueAt(null, i, ICPubMetaNameConst.NASSISTNUM);
	      String vnote =
	        wrapper.getBodyValueAt_String(i, ICPubMetaNameConst.VNOTEBODY);
	      if (StringUtil.isSEmptyOrNull(vnote)
	          || vnote.indexOf(OnhandRes.getOnhandErro()) < 0) {
	        continue;
	      }
	      wrapper.setBodyValueAt(null, i, ICPubMetaNameConst.VNOTEBODY);
	    }
	  }

	  private void loadInvCalBodyInfo() {
	    Set<String> invpks = new HashSet<String>();    
	    int count = this.getEditorModel().getCardPanelWrapper().getRowCount(null);
	    for (int i = 0; i < count; i++) {
	      String cmaterialvid =
	          this.getEditorModel().getCardPanelWrapper()
	              .getBodyValueAt_String(i, ICPubMetaNameConst.CMATERIALVID);
	      if (StringUtil.isSEmptyOrNull(cmaterialvid)) {
	        continue;
	      }
	      invpks.add(cmaterialvid);
	    }
	   
	    if(invpks.size()==0){
	      return ;
	    }
	    InvCalBodyVO[] bodys = InvInfoUIQuery
	        .getInstance()
	        .getInvInfoQuery()
	        .getInvCalBodyVO(
	            this.getEditorModel().getCardPanelWrapper()
	                .getHeadValue_String(ICPubMetaNameConst.PK_ORG),
	            invpks.toArray(new String[invpks.size()]));
	    if(bodys== null || bodys.length==0){
	      return ;
	    }
	    this.invcalbodysmap = CollectionUtils.hashVOArray(MaterialStockVO.PK_MATERIAL, bodys);
	    
	  }
	  /**
	   * 拣货前预处理：清空实发数量、清空表体备注、格式化记结存的变动换算率
	   */
	  private void preprocess(ICBillVO billvo) {
	    // 清空单据表体数据
	    for (ICBillBodyVO body : billvo.getBodys()) {
	      body.setNnum(null);
	      body.setNassistnum(null);
	      body.setLocationVOs(null);
	      String vnote = body.getVnotebody();
	      if (!StringUtil.isSEmptyOrNull(vnote) 
	        && vnote.indexOf(OnhandRes.getOnhandErro()) >= 0)
	        body.setVnotebody(null);
	      }
	 
	    // 清空表体备注
	    this.clearBodyNote();

	    // 统一处理变动换算率格式，否则与结存的换算率格式不匹配
	    new ICBillValueSetter().procChangeRateFormat(new ICBillVO[] {
	        billvo
	    }, InvInfoUIQuery.getInstance().getInvInfoQuery());

	  }

	  /**
	   * 重新设置表体货位ID，如果单品信息分布在多个货位，清空表体，否则，设置单品货位信息到表体
	   * 
	   * @param bodyvo拣货结果
	   * @param row 当前行
	   */
	  private void resetBodyLocationID(ICBillBodyVO bodyvo, int row) {
	    ICLocationVO[] locs = bodyvo.getLocationVOs();

	    if (ValueCheckUtil.isNullORZeroLength(locs)) {
	      return;
	    }

	    List<String> locids =
	      VOEntityUtil.getVOsValueListNotDel(locs, ICLocationVO.CLOCATIONID);
	    if (ValueCheckUtil.isNullORZeroLength(locids)) {
	      return;
	    }

	    String locid = locids.get(0);
	    for (String id : locids) {
	      if (!StringUtil.isStringEqual(locid, id)) {
	        locid = null;
	        break;
	      }
	    }

	    CardPanelWrapper wrapper = this.getEditorModel().getCardPanelWrapper();
	    wrapper.setBodyValueAt(locid, row, ICPubMetaNameConst.CLOCATIONID);
	  }

	  /**
	   * 没有拣到存量的行，设置表体备注“现存量不足”
	   * 
	   * @param uiBodys
	   * @param results
	   */
	  private void setBodyNoteOnhandErro(ICBillBodyVO[] uiBodys,
	      ICBillPickResults results) {
	    CardPanelWrapper wrapper = this.getEditorModel().getCardPanelWrapper();
	    String errMsg = OnhandRes.getOnhandErro();
	    if (results == null) {
	      for (int i = 0, loop = uiBodys.length; i < loop; i++) {
	        if (null == wrapper.getBodyValueAt(i, ICPubMetaNameConst.CMATERIALVID)) {
	          continue;
	        }
	        if (!NCBaseTypeUtils.isNullOrZero(wrapper.getBodyValueAt_UFDouble(i,
	            ICPubMetaNameConst.NNUM))) {
	          continue;
	        }
	        wrapper.setBodyValueAt(errMsg, i, ICPubMetaNameConst.VNOTEBODY);
	        wrapper.getBodyValueAt(i, ICPubMetaNameConst.CROWNO);
	      }
	      return;
	    }

	    for (int i = 0, loop = uiBodys.length; i < loop; i++) {
	      ICBillBodyVO[] bodys = results.getPickBodys(i);
	      if (!ValueCheckUtil.isNullORZeroLength(bodys)) {
	        continue;
	      }

	      if (null == wrapper.getBodyValueAt(i, ICPubMetaNameConst.CMATERIALVID)) {
	        continue;
	      }
	      if (!NCBaseTypeUtils.isNullOrZero(wrapper.getBodyValueAt_UFDouble(i,
	          ICPubMetaNameConst.NNUM))) {
	        continue;
	      }
	      wrapper.setBodyValueAt(errMsg, i, ICPubMetaNameConst.VNOTEBODY);
	    }

	  }

	  /**
	   *
	   */
	  private void setOnhandDataToBody(ICBillBodyVO bodyvo, int row) {
	    if (bodyvo == null) {
	      return;
	    }
	    CardPanelWrapper wrapper = this.getEditorModel().getCardPanelWrapper();
	    // 表体主键复制，防止拣货拆行后，主键重复
	    wrapper.getBillModel().setValueAt(bodyvo.getCgeneralbid(), row,
	        MetaNameConst.CGENERALBID);
	    // 行号设置，已经清空原始行号，用拣货结果设置
	    wrapper.getBillModel().setValueAt(bodyvo.getCrowno(), row,
	        ICPubMetaNameConst.CROWNO);
	    this.processOnhandDimInfo(bodyvo, row, wrapper);
	    // 货位管理仓，货位信息单独处理
	    this.resetBodyLocationID(bodyvo, row);
	    this.processCcorrespondInfo(bodyvo, row, wrapper);
	    
	    this.processNumInfo(bodyvo, row, wrapper);
	  }

	  private void processOnhandDimInfo(ICBillBodyVO bodyvo, int row,
	      CardPanelWrapper wrapper) {
	    for (String field : OnhandDimVO.getDimContentFields()) {
	      if (field.equals(OnhandDimVO.VBATCHCODE)) {
	        wrapper.getBillModel().setValueAt(bodyvo.getAttributeValue(field), row,
	            field);
	        continue;
	      }
	      if (field.equals("vfree6")) {
	    	  //设置显示的界面内容,vfree6
	          wrapper.getBillModel().setValueAt(bodyvo.getAttributeValue(field), row,
	              field);
	          //设置实际的ID值，vfree6_ID
	          wrapper.setBodyValueAt(bodyvo.getAttributeValue(field),row, field);	
	          //add by chenqk
	          wrapper.getBillModel().setValueAt(bodyvo.getAttributeValue(field), row,
	        		  "vdef6_148");//add by chenqk
	          wrapper.setBodyValueAt(bodyvo.getAttributeValue(field),row, "vdef6_148");	
	          continue;
	        }
	      if (field.equals(OnhandDimVO.CLOCATIONID)) {
	        // V60，货位管理仓，货位信息单独处理
	        continue;
	      }
	      // 拣出的货非换算率记结存场合，不包含换算率信息
	      if (field.equals(OnhandDimVO.VCHANGERATE)
	          && StringUtil.isSEmptyOrNull(bodyvo.getVchangerate())) {
	        continue;
	      }
	      // 固定辅助属性(库存状态、项目、供应商、生产厂商、客户)
	      if (field.equals(OnhandDimVO.CSTATEID)
	          && StringUtil.isSEmptyOrNull(bodyvo.getCstateid())) {
	        continue;
	      }
	      if (field.equals(OnhandDimVO.CPROJECTID)
	          && StringUtil.isSEmptyOrNull(bodyvo.getCprojectid())) {
	        continue;
	      }
	      if (field.equals(OnhandDimVO.CVENDORID)
	          && StringUtil.isSEmptyOrNull(bodyvo.getCvendorid())) {
	        continue;
	      }
	      if (field.equals(OnhandDimVO.CPRODUCTORID)
	          && StringUtil.isSEmptyOrNull(bodyvo.getCproductorid())) {
	        continue;
	      }
	      if (field.equals(OnhandDimVO.CASSCUSTID)
	          && StringUtil.isSEmptyOrNull(bodyvo.getCasscustid())) {
	        continue;
	      }
	      wrapper.setBodyValueAt(bodyvo.getAttributeValue(field), row, field);
	    }
	  }

	  private void processCcorrespondInfo(ICBillBodyVO bodyvo, int row,
	      CardPanelWrapper wrapper) {
	    String[] corresponds =
	        new String[] {
	          MetaNameConst.CCORRESPONDTYPE, MetaNameConst.CCORRESPONDTRANSTYPE,
	          MetaNameConst.CCORRESPONDCODE, MetaNameConst.CCORRESPONDHID,
	          MetaNameConst.CCORRESPONDBID, MetaNameConst.CCORRESPONDROWNO,
	          ICPubMetaNameConst.CROWNO, ICPubMetaNameConst.NCOSTMNY,
	          ICPubMetaNameConst.NPLANNEDMNY, MetaNameConst.DBIZDATE,
	          ICPubMetaNameConst.VNOTEBODY
	        };
	    String cmateiralvid =
	        wrapper.getBodyValueAt_String(row, ICPubMetaNameConst.CMATERIALVID);
	    InvCalBodyVO invvo = this.invcalbodysmap.get(cmateiralvid);
	    // 对于非出入库跟踪物料，不处理对应入库单信息，否则会把原始信息清掉（出库参照入库单拉单）
	    if (invvo == null || !ValueCheckUtil.isTrue(invvo.getOuttrackin())) {
	      return;
	    }
	    for (String field : corresponds) {
	      wrapper.setBodyValueAt(bodyvo.getAttributeValue(field), row, field);
	    }
	  }
	  /**
	   * 
	   * <p>
	   * 使用场景：
	   * <ul>
	   * <li>
	   * </ul>
	   @param bodyvo
	   @param row
	   @param wrapper
	   */   
	  private void processNumInfo(ICBillBodyVO bodyvo, int row,
	      CardPanelWrapper wrapper) {
	    wrapper.setBodyValueAt(bodyvo.getNnum(), row, ICPubMetaNameConst.NNUM);
	    wrapper.setBodyValueAt(bodyvo.getNgrossnum(), row,
	        ICPubMetaNameConst.NGROSSNUM);
	    UFDouble shouldnum =
	        this.getEditorModel().getCardPanelWrapper()
	            .getBodyValueAt_UFDouble(row, ICPubMetaNameConst.NSHOULDNUM);
	    UFDouble shouldastnum =
	      this.getEditorModel().getCardPanelWrapper()
	          .getBodyValueAt_UFDouble(row, ICPubMetaNameConst.NSHOULDASSISTNUM);
	    UIBusiCalculator calc = new UIBusiCalculator();
	    UFDouble num =
	        this.getEditorModel().getCardPanelWrapper()
	            .getBodyValueAt_UFDouble(row, ICPubMetaNameConst.NNUM);
	    if (NCBaseTypeUtils.isNullOrZero(shouldnum) && NCBaseTypeUtils.isNullOrZero(shouldastnum) && NCBaseTypeUtils.isNullOrZero(num)) {
	      return;
	    }
	    
	    //清空辅数量，避免辅数量是拣货前数量，影响联动计算
	    this.getEditorModel().getCardPanelWrapper().setBodyValueAt(null, row
	        , ICPubMetaNameConst.NASSISTNUM);
	    
	    this.caculateAfterNumEdit(row, num, null);
	    // 对于辅计量记结存的物料，计算辅数量之后，会导致界面展示和结存数据不一致，所以用结存数据设置
	    if (!ValueCheckUtil.isNullORZeroLength(this.isasunitstore)) {
	      if (ValueCheckUtil.isTrue(this.isasunitstore.get(this.getEditorModel()
	          .getCardPanelWrapper()
	          .getBodyValueAt_String(row, ICPubMetaNameConst.CMATERIALVID)
	          + this.getEditorModel().getCardPanelWrapper()
	              .getBodyValueAt_String(row, ICPubMetaNameConst.CASTUNITID)))) {
	        wrapper.setBodyValueAt(bodyvo.getNassistnum(), row,
	            ICPubMetaNameConst.NASSISTNUM);
	      }
	      else {
	        bodyvo.setNassistnum(calc.calculateAstNum(
	            bodyvo.getNnum(),
	            this.getEditorModel().getCardPanelWrapper()
	                .getBodyValueAt_String(row, ICPubMetaNameConst.VCHANGERATE),
	            bodyvo.getCasscustid()));
	      }
	    }
	    
	    UFDouble newshouldastnum = this.getEditorModel().getCardPanelWrapper()
	    .getBodyValueAt_UFDouble(row, ICPubMetaNameConst.NSHOULDASSISTNUM);
	    if (shouldastnum!=null && !NCBaseTypeUtils.isEquals(shouldastnum, newshouldastnum)) {
	      this.getEditorModel()
	          .getCardPanelWrapper()
	          .setBodyValueAt(shouldastnum, row,
	              ICPubMetaNameConst.NSHOULDASSISTNUM);
	    }
	    if (bodyvo.getLocationVOs() != null) {
	      this.processLocAstNum(bodyvo, calc);
	      this.getEditorModel().setBodyEditDetailData(row, bodyvo.getLocationVOs());
//			wangsyf add 2020-06-15 ***************************************************************************************
				//1.根据孙表添加货位信息 如果单品的货位相同，则设置为单品的货位 否则设置为null; 
				ICLocationVO[] locs = bodyvo.getLocationVOs();
				Set<String> valueSet = VOEntityUtil.getVOsValueSet(locs, ICLocationVO.CLOCATIONID);
				String clocationid = null;
				if(valueSet.size() == 1){
					 clocationid = valueSet.iterator().next();
				}
				wrapper.setBodyValueAt(clocationid, row, ICPubMetaNameConst.CLOCATIONID);
				
				//2.根据单品序列号设置表体的序列号
				if(ValueCheckUtil.isNullORZeroLength(locs) || locs.length > 1 || locs[0].getVserialcode() == null){
					 this.getEditorModel().getCardPanelWrapper().setBodyValueAt(null, row,
				 	        ICPubMetaNameConst.VSERIALCODE);
					 this.getEditorModel().getCardPanelWrapper().setBodyValueAt(null, row,
				 	  	          ICPubMetaNameConst.PK_SERIALCODE);
					 this.getEditorModel().getCardPanelWrapper().setBodyValueAt(null, row,
				 		          ICPubMetaNameConst.CSNQUALITYLEVELID);
				 	  	
				 	      for (int i = 1; i <= ICPubMetaNameConst.SNDEFNUM; i++) {
				 	    	this.getEditorModel().getCardPanelWrapper().setBodyValueAt(null, row,
				 	    			ICPubMetaNameConst.VSNDEFHEAD_IC+i);
				 			}
				}else{
					 nc.ui.pub.bill.BillCardPanel panel = this.getEditorModel().getCardPanelWrapper().getBillCardPanel();
					 panel.setBodyValueAt(locs[0].getVserialcode(), row,
							 ICPubMetaNameConst.VSERIALCODE);
					 panel.setBodyValueAt(locs[0].getPk_serialcode(), row,
							 ICPubMetaNameConst.PK_SERIALCODE);
					 panel.setBodyValueAt(locs[0].getCsnqualitylevelid(), row,
							 ICPubMetaNameConst.CSNQUALITYLEVELID);
					 
					 for (int i = 1; i <= ICPubMetaNameConst.SNDEFNUM; i++) {
						 panel.setBodyValueAt(locs[0].getAttributeValue(ICPubMetaNameConst.VSNDEFHEAD_IC+i), row,
								 ICPubMetaNameConst.VSNDEFHEAD_IC+i);
					 }
				}
				
				
				
				if (this.getEditorModel().getCardPanelWrapper().getRowState(null, row) == BillModel.NORMAL) {
					this.getEditorModel().getCardPanelWrapper().setRowState(null, row,
							BillModel.MODIFICATION);
				}
				
				//3.模拟序列号条码编辑界面勾选是否修改单据数量，则用单品数量信息更新表体数量
				UFDouble nnum =
						this.getEditorModel().getCardPanelWrapper().getBodyValueAt_UFDouble(row,
								ICPubMetaNameConst.NNUM);
				UFDouble nassistnum =
						this.getEditorModel().getCardPanelWrapper().getBodyValueAt_UFDouble(row,
								ICPubMetaNameConst.NASSISTNUM);
				UFDouble ngrossnum =
						this.getEditorModel().getCardPanelWrapper().getBodyValueAt_UFDouble(row,
								ICPubMetaNameConst.NGROSSNUM);
				UFDouble totallocnums = ICLocationUtil.getLocationTotalNums(locs);
				UFDouble totallocnassistnums = ICLocationUtil.getLocationTotalNassistnums(locs);
				PickAutoConst.setPickConst();
				BarcodeInputUIUtil  tool = new BarcodeInputUIUtil();
				
				if (!NCBaseTypeUtils.isEquals(totallocnassistnums, nassistnum)) {
					this.getEditorModel().getCardPanelWrapper().setBodyValueAt(totallocnassistnums, row,
							ICPubMetaNameConst.NASSISTNUM);
					if (this.getEditorModel().getCardPanelWrapper().getRowState(null, row) == BillModel.NORMAL) {
						this.getEditorModel().getCardPanelWrapper().setRowState(null, row,
								BillModel.MODIFICATION);
					}
					// 先触发数量编辑时间再触发主数量编辑事件
					tool.caculateAfterNassistnumEdit(this.getEditorModel(), row, totallocnassistnums, nassistnum);
				}
				
				
				if (!NCBaseTypeUtils.isEquals(totallocnums, nnum)) {
					this.getEditorModel().getCardPanelWrapper().setBodyValueAt(totallocnums, row,
							ICPubMetaNameConst.NNUM);
					
					this.getEditorModel().getCardPanelWrapper().setRowState(null, row,
							BillModel.MODIFICATION);
					tool.caculateAfterNumEdit(this.getEditorModel(), row, totallocnums, nnum);
				}
				
				UFDouble totallocgrossnums = ICLocationUtil.getLocationTotalGrossNums(locs);
				if (!NCBaseTypeUtils.isEquals(totallocgrossnums, ngrossnum)) {
					this.getEditorModel().getCardPanelWrapper().setBodyValueAt(totallocgrossnums, row,
							ICPubMetaNameConst.NGROSSNUM);
				}
			}
	    //wangsyf add 2020-06-15 ***************************************************************************************
	  }

	  /**
	   * 得到界面的物料是否辅计量记结存
	   * @return
	   */
	  private void getBAstunitStorebalance() {
	    this.isasunitstore = null;
	    int count =
	      this.getEditorModel().getCardPanelWrapper().getBillCardPanel()
	      .getRowCount();
	    if (count <= 0) {
	      return ;
	    }
	    List<String[]> invmeaspk = new ArrayList<String[]>();
	    for (int i = 0; i < count; i++) {
	      String invpk =
	        (String) this.getEditorModel().getCardPanelWrapper()
	        .getBodyValueAt(i, ICPubMetaNameConst.CMATERIALVID);
	      if (StringUtil.isSEmptyOrNull(invpk)) {
	        continue;
	      }
	      String measpk =
	        (String) this.getEditorModel().getCardPanelWrapper()
	        .getBodyValueAt(i, ICPubMetaNameConst.CASTUNITID);
	      if (StringUtil.isSEmptyOrNull(measpk)) {
	        continue;
	      }
	      String[] pks = new String[] {
	          invpk, measpk
	      };
	      if (invmeaspk.size() == 0) {
	        invmeaspk.add(pks);
	      }
	      else {
	        boolean iscon = false;
	        for (String[] pk : invmeaspk) {
	          if (pk[0].equals(invpk) && pk[1].equals(measpk)) {
	            iscon = true;
	            break;
	          }
	        }
	        if (!iscon) {
	          invmeaspk.add(pks);
	        }
	      }

	    }
	    String[] invpks = new String[invmeaspk.size()];
	    String[] measpks = new String[invmeaspk.size()];
	    for (int i = 0; i < invmeaspk.size(); i++) {
	      invpks[i] = invmeaspk.get(i)[0];
	      measpks[i] = invmeaspk.get(i)[1];
	    }
	    InvMeasVO[] invmeasvos =
	      InvInfoUIQuery.getInstance().getInvInfoQuery()
	      .getInvMeasVO(invpks, measpks);
	    Map<String, UFBoolean> bastunitstore = new HashMap<String, UFBoolean>();
	    for (InvMeasVO invMeasVO : invmeasvos) {
	      if(invMeasVO == null){
	        // 物料可能没有辅计量信息
	        continue;
	      }
	      bastunitstore.put(invMeasVO.getPk_material() + invMeasVO.getPk_measdoc(),
	          invMeasVO.getIsstorebalance());
	    }
	    this.isasunitstore= bastunitstore;
	  }
	  /**
	   * 根据表体备注设置状态栏信息
	   */
	  private void showStatusMsg() {
	    CardPanelWrapper wrapper = this.getEditorModel().getCardPanelWrapper();
	    int rowconut = wrapper.getRowCount(null);
	    if (rowconut == 0) {
	      return;
	    }
	    List<String> nos = new ArrayList<String>();
	    for (int i = 0; i < rowconut; i++) {
	      String vnote =
	        wrapper.getBodyValueAt_String(i, ICPubMetaNameConst.VNOTEBODY);
	      if (StringUtil.isSEmptyOrNull(vnote)
	          || vnote.indexOf(OnhandRes.getOnhandErro()) < 0) {
	        continue;
	      }
	      nos.add((String) wrapper.getBodyValueAt(i, ICPubMetaNameConst.CROWNO));
	    }
	    this.showStatusMsg(nos);
	  }

	  /**
	   * 状态栏显示消息
	   * 
	   * @param nos
	   */
	  private void showStatusMsg(List<String> nos) {
	    if (ValueCheckUtil.isNullORZeroLength(nos)) {
	      ShowStatusBarMsgUtil.showStatusBarMsg(null, this
	          .getEditorModel().getContext().getLoginContext());
	      return;
	    }
	    StringBuilder errs =
	      new StringBuilder(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
	          .getStrByID("4008001_0", "04008001-0038")/* @res "拣货完成，以下行现存量不足：行 " */);
	    String[] errnos = nos.toArray(new String[0]);
	    for (int i = 0; i < nos.size(); i++) {
	      errs.append(errnos[i]);
	      errs.append(",");
	    }
	    errs.deleteCharAt(errs.length() - 1);
	    ShowStatusBarMsgUtil.showStatusBarMsg(errs.toString(), this
	        .getEditorModel().getContext().getLoginContext());
	  }

	  @Override
	  protected boolean isActionEnable() {
	    return true;
	  }

	  /**
	   * @author yangb
	   * @time 2010-4-17 上午02:01:26
	   */
	  protected boolean onCheck() {
	    ICUIBillEntity entity = this.getEditorModel().getICUIBillEntity();
	    if (StringUtil.isSEmptyOrNull(entity.getHead().getCwarehouseid())) {
	      this.getEditorModel().getContext().showStatusBarMessage(
	          nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("4008001_0",
	          "04008001-0039")/* @res "仓库未输入，不能拣货" */);
	      return false;
	    }

	    boolean isAutoAddLine =
	      this.getEditorModel().getICBizView().isAutoAddLine();
	    ICBizCardBodyEntity[] bodys = entity.getBodys();
	    int loop = bodys.length;
	    int lastRow = bodys.length - 1;
	    if (isAutoAddLine
	        && StringUtil.isSEmptyOrNull(bodys[lastRow].getCmaterialvid())) {
	      this.getEditorModel().getICBizView().getBillCardPanel().getBillTable()
	      .getSelectionModel().setSelectionInterval(lastRow, lastRow);
	      this.getEditorModel().getCardPanelWrapper().getBillCardPanel().delLine();
	      loop = lastRow;
	    }

	    for (int i = 0; i < loop; i++) {
	      if (StringUtil.isSEmptyOrNull(bodys[i].getCmaterialvid())) {
	        this.getEditorModel().getContext().showStatusBarMessage(
	            nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("4008001_0",
	            "04008001-0040")/* @res "存在物料未输入行，不能拣货" */);
	        return false;
	      }
	      if (InOutHelp.getRealInOutFlag(entity.getBilltype(), bodys[i]) != InOutFlag.Out) {
	        this.getEditorModel().getContext().showStatusBarMessage(
	            nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("4008001_0",
	            "04008001-0041")/* @res "存在非出方向的输入行，不能拣货" */);
	        return false;
	      }
	      if (NCBaseTypeUtils.isLEZero(bodys[i].getNshouldnum())) {
	        this.getEditorModel().getContext().showStatusBarMessage(
	            nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("4008001_0",
	            "04008001-0042")/* @res "存在应出数量小于等于零的输入行，不能拣货" */);
	        return false;
	      }
	    }
	    return true;
	  }

	}

