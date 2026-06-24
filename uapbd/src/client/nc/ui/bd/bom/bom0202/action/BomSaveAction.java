 package nc.ui.bd.bom.bom0202.action;
 
 import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.itf.bd.bom.bom0202.IBomBillQueryService;
import nc.md.data.access.NCObject;
import nc.ui.bd.bom.bom0202.model.BomMainBillManageModel;
import nc.ui.bd.bom.bom0202.serviceproxy.BomMaintainProxy;
import nc.ui.bd.bom.bom0202.view.BomBillForm;
import nc.ui.mmf.framework.action.GCDifferentVOSaveAction;
import nc.ui.mmf.framework.view.BillFormFacade;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pubapp.uif2app.components.grand.CardGrandPanelComposite;
import nc.ui.pubapp.uif2app.components.grand.model.MainGrandModel;
import nc.ui.uif2.UIState;
import nc.ui.uif2.gmplog.GmpLogProcessor;
import nc.util.mmf.framework.base.MMArrayUtil;
import nc.util.mmf.framework.base.MMStringUtil;
import nc.util.mmf.framework.base.MMValueCheck;
import nc.util.mmf.framework.gc.GCClientBillCombinServer;
import nc.util.mmf.framework.gc.GCClientBillToServer;
import nc.util.mmf.framework.gc.GCPseudoColUtil;
import nc.vo.bd.bom.bom0202.entity.AggBomVO;
import nc.vo.bd.bom.bom0202.entity.BomActivityVO;
import nc.vo.bd.bom.bom0202.entity.BomItemVO;
import nc.vo.bd.bom.bom0202.entity.BomOutputsVO;
import nc.vo.bd.bom.bom0202.entity.BomSelectVO;
import nc.vo.bd.bom.bom0202.entity.BomVO;
import nc.vo.bd.bom.bom0202.enumeration.BomCategoryEnum;
import nc.vo.bd.bom.bom0202.enumeration.BomTypeEnum;
import nc.vo.bd.bom.bom0202.message.MMBDLangConstBom0202;
import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.pub.BusinessException;
import nc.vo.pub.ISuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.pubapp.pattern.model.entity.bill.AbstractBill;
import nc.vo.pubapp.pattern.model.entity.bill.IBill;

 public class BomSaveAction extends GCDifferentVOSaveAction
 {
   private static final long serialVersionUID = 4609693901984179834L;
   private boolean isExecActionSuccess;
   private boolean fromBomTree;
   private CardGrandPanelComposite billForm;
   private MainGrandModel mainGrandModel;
   private String cbomid;
   private UFBoolean isDefault;
   private boolean isAddSave;
   private BillFormFacade billFormEditor;
   private boolean continueSave;
 
   public BomSaveAction()
   {
/*  63 */     this.isExecActionSuccess = true;
 
 
 
 
/*  68 */     this.fromBomTree = false;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
/*  83 */     this.cbomid = null;
 
 
 
 
 this.continueSave = false;
 
/*  90 */     this.isAddSave = false;
}
   public boolean isAddSave()
   {
/*  93 */     return this.isAddSave;
   }
 
   public void setAddSave(boolean isAddSave) {
/*  97 */     this.isAddSave = isAddSave;
   }
 
 
 
 
 
   public MainGrandModel getMainGrandModel()
   {
/* 106 */     return this.mainGrandModel;
   }
 
   public void setMainGrandModel(MainGrandModel mainGrandModel) {
/* 110 */     this.mainGrandModel = mainGrandModel;
   }
 
   public CardGrandPanelComposite getBillForm() {
/* 114 */     return this.billForm;
   }
 
   public void setBillForm(CardGrandPanelComposite billForm) {
/* 118 */     this.billForm = billForm;
   }
 
/* 121 */     
   
   public void actionPerformed(ActionEvent e)
   {
	/* 125 */     Logger.debug("Entering " + super.getClass().toString() + ".actionPerformed");
	  boolean ischeck = beforeDoAction(e);
	  if (!ischeck)
		  return;
		
/* 126 */     beforeDoAction();
     try {
/* 128 */       if ((this.interceptor == null) || (this.interceptor.beforeDoAction(this, e)))
         try {
        	//获取当前界面数据，用来后续比较
				Object cilentBill = null;
				//当前数据库保存的数据
				NCObject oldncobj =null;
				if(getPk() !=null){
					cilentBill =  getSuperVO(e);
					GmpLogProcessor process = new GmpLogProcessor();
					 oldncobj = process.qyrNCObj(cilentBill);
				}
				
			
/* 130 */           doAction(e);
/* 131 */           if ((this.interceptor != null) && (this.continueSave))
/* 132 */             this.interceptor.afterDoActionSuccessed(this, e);

			//如果增加了电子签名记录
			if(getPk() !=null){
				try {
		
			//	更新本次记录的电子签名的单据PK
					updatePk(e);
				//20230710 liyf 记录审计日志
					setGmpLog(e,cilentBill,oldncobj);
				} catch (Exception e1) {
					Logger.error("记录审计日志异常:-->>"+e1.getMessage());
				}
			}

         }
         catch (Exception ex)
         {
/* 136 */           if ((this.interceptor == null) || (this.interceptor.afterDoActionFailed(this, e, ex))) {
/* 137 */             if (getExceptionHandler() != null) {
/* 138 */               processExceptionHandler(ex);
             }
/* 140 */             else if (ex instanceof RuntimeException) {
/* 141 */               throw ((RuntimeException)ex);
             }
 
/* 144 */             throw new RuntimeException(ex);
           }
         }
     }
     finally
     {
/* 150 */       Logger.debug("Leaving " + super.getClass().toString() + ".actionPerformed");
     }
   }
 
 
   public void doAction(ActionEvent e)
     throws Exception
   {
/* 158 */     this.billFormEditor.getBillCardPanel().stopEditing();
/* 159 */     this.continueSave = true;
/* 160 */     AggBomVO aggBomVO = (AggBomVO)getBillForm().getValue();
/* 161 */     if (aggBomVO == null) {
/* 162 */       return;
     }
 
/* 165 */     BomVO headVO = (BomVO)aggBomVO.getParent();
 
 
/* 168 */     if (BomTypeEnum.CONFIGBOM.equalsValue(headVO.getFbomtype())) {
/* 169 */       BomItemVO[] items = (BomItemVO[])(BomItemVO[])aggBomVO.getChildren(BomItemVO.class);
 
/* 171 */       Map tempSelectVos = ((BomBillForm)getBillFormEditor()).getTempSelectVOs();
/* 172 */       if ((MMArrayUtil.isNotEmpty(items)) && (MMValueCheck.isNotEmpty(tempSelectVos)))
 
 
 
 
       {
/* 178 */         for (BomItemVO item : items)
         {
/* 180 */           if ((!(item.getBischoice().booleanValue())) || (item.getBbisfeature().booleanValue()))
             continue;
/* 182 */           if ((MMStringUtil.isNotEmpty(item.getVselectcond())) && (!(tempSelectVos.containsKey(item.getCmaterialvid() + item.getVselectcond()))))
           {
/* 184 */             ExceptionUtils.wrappBusinessException(NCLangRes4VoTransl.getNCLangRes().getStrByID("1014362_0", "01014362-0526") + item.getVrowno() + NCLangRes4VoTransl.getNCLangRes().getStrByID("1014362_0", "01014362-0527"));
 
 
 
 
           }
 
/* 191 */           if (MMValueCheck.isNotEmpty((Collection)tempSelectVos.get(item.getCmaterialvid() + item.getVselectcond()))) {
/* 192 */             BomSelectVO[] selects = (BomSelectVO[])((List)tempSelectVos.get(item.getCmaterialvid() + item.getVselectcond())).toArray(new BomSelectVO[((List)tempSelectVos.get(item.getCmaterialvid() + item.getVselectcond())).size()]);
 
 
 
/* 196 */             item.setSelect(selects);
           }
         }
 
       }
 
     }
 
/* 204 */     if ((BomTypeEnum.RTFINISH.equalsValue(headVO.getFbomtype())) && (MMArrayUtil.isNotEmpty(aggBomVO.getChildren(BomOutputsVO.class))))
     {
/* 206 */       int result = MessageDialog.showYesNoDlg(getModel().getContext().getEntranceUI(), MMBDLangConstBom0202.getTITLE_SAVE_DIALOG(), MMBDLangConstBom0202.getTITLE_SAVE_INFO());
 
 
/* 209 */       if (4 != result) {
/* 210 */         return;
       }
 
/* 213 */       ISuperVO[] outputs = aggBomVO.getChildren(BomOutputsVO.class);
/* 214 */       int[] rows = new int[outputs.length];
/* 215 */       for (int i = 0; i < rows.length; ++i) {
/* 216 */         rows[i] = i;
       }
 
/* 219 */       getBillFormEditor().getBillCardPanel().getBodyPanel("outputs").delLine(rows);
 
/* 221 */       aggBomVO.setChildren(BomOutputsVO.class, null);
     }
 
/* 224 */     if (!(this.isAddSave))
     {
/* 226 */       if ((headVO.getHbcustomized().equals(UFBoolean.TRUE)) && (headVO.getHbdefault().equals(UFBoolean.TRUE)))
       {
/* 228 */         String defaultVersion = getBomBillQueryService().isCBomDefaultVersionExist(headVO.getPk_org(), headVO.getHcmaterialid(), headVO.getHcmaterialvid(), headVO.getFbomtype().intValue(), headVO.getHcfeaturecode());
 
 
 
/* 232 */         String cbomid = ((BomVO)aggBomVO.getParent()).getCbomid();
/* 233 */         if (((MMStringUtil.isEmpty(cbomid)) && (MMStringUtil.isNotEmpty(defaultVersion))) || ((MMStringUtil.isNotEmpty(defaultVersion)) && (MMStringUtil.isNotEmpty(cbomid)) && (!(cbomid.equals(defaultVersion))) && (getModel().getUiState() == UIState.EDIT)) || ((getModel().getUiState() == UIState.ADD) && (MMStringUtil.isNotEmpty(defaultVersion)) && (MMStringUtil.isNotEmpty(cbomid)) && (cbomid.equals(defaultVersion))))
 
 
 
         {
/* 238 */           int result = MessageDialog.showYesNoDlg(getModel().getContext().getEntranceUI(), null, MMBDLangConstBom0202.getAreYouSureSetDefault(), 4);
 
 
/* 241 */           if (result != 4) {
/* 242 */             if (MMValueCheck.isNotEmpty(this.interceptor)) {
/* 243 */               this.continueSave = false;
             }
/* 245 */             return;
           }
 
         }
 
       }
 
/* 252 */       if ((((MMValueCheck.isEmpty(headVO.getHbcustomized())) || (headVO.getHbcustomized().equals(UFBoolean.FALSE)))) && (((MMValueCheck.isEmpty(headVO.getHfbomcategory())) || (headVO.getHfbomcategory().intValue() == BomCategoryEnum.NORMAL_BOM.toIntValue()))) && (headVO.getHbdefault().equals(UFBoolean.TRUE))) {
/* 253 */         String defaultVersion = getBomBillQueryService().isBomOrCfgBomDefaultVersionExist(headVO.getPk_org(), headVO.getHcmaterialid(), headVO.getHcmaterialvid(), headVO.getFbomtype());
 
 
/* 256 */         String cbomid = ((BomVO)aggBomVO.getParent()).getCbomid();
 
 
 
 
 
 
/* 263 */         if ((MMValueCheck.isNotEmpty(defaultVersion)) && (!(defaultVersion.equals(cbomid)))) {
/* 264 */           int result = MessageDialog.showYesNoDlg(getModel().getContext().getEntranceUI(), null, MMBDLangConstBom0202.getAreYouSureSetDefault(), 4);
 
 
/* 267 */           if (result != 4) {
/* 268 */             if (MMValueCheck.isNotEmpty(this.interceptor)) {
/* 269 */               this.continueSave = false;
             }
/* 271 */             return;
           }
         }
 
       }
 
     }
 
/* 279 */     if (this.billFormEditor.getBillCardPanel().getBodyItem("outputs", "cmaterialversion") != null) {
/* 280 */       this.billFormEditor.getBillCardPanel().getBodyItem("outputs", "cmaterialversion").setNull(false);
     }
/* 282 */     if (this.billFormEditor.getBillCardPanel().getBodyItem("bomitems", "cmaterialversion") != null) {
/* 283 */       this.billFormEditor.getBillCardPanel().getBodyItem("bomitems", "cmaterialversion").setNull(false);
 
 
     }
 
/* 288 */     BomVO header = (BomVO)aggBomVO.getParent();
/* 289 */     String pk_org = header.getPk_org();
/* 290 */     Integer hfbomtype = header.getFbomtype();
/* 291 */     BomMainBillManageModel billManageModel = (BomMainBillManageModel)getModel();
/* 292 */     if ((MMValueCheck.isNotEmpty(billManageModel)) && (billManageModel.getUseAggBomVO())) {
/* 293 */       if (header.getHfbomcategory().intValue() == BomCategoryEnum.NORMAL_BOM.toIntValue())
       {
/* 295 */         this.billFormEditor.getBillCardPanel().getHeadItem("vsobillcode").setValue(null);
/* 296 */         this.billFormEditor.getBillCardPanel().getHeadItem("vsobillno").setValue(null);
/* 297 */         header.setVsobillcode(null);
/* 298 */         header.setVsobillno(null);
 
/* 300 */         String hcmaterialid = header.getHcmaterialid();
/* 301 */         String hcmaterialvid = header.getHcmaterialvid();
 
/* 303 */         if (header.getHbdefault().equals(UFBoolean.TRUE)) {
/* 304 */           AggBomVO[] aggvos = getQueryService().queryValidBomByMaterialCond(pk_org, hcmaterialid, hcmaterialvid, hfbomtype.intValue());
/* 305 */           if ((aggvos != null) && (aggvos.length > 0)) {
/* 306 */             int result = MessageDialog.showYesNoDlg(getModel().getContext().getEntranceUI(), null, MMBDLangConstBom0202.getAreYouSureSetDefault(), 4);
 
 
/* 309 */             if (result != 4) {
/* 310 */               if (MMValueCheck.isNotEmpty(this.interceptor)) {
/* 311 */                 this.continueSave = false;
               }
/* 313 */               return;
             }
           }
 
         }
 
       }
       else
       {
/* 322 */         String vsobillcode = header.getVsobillcode();
/* 323 */         String vsobillno = header.getVsobillno();
/* 324 */         if ((vsobillcode == null) || (vsobillcode.length() == 0) || (vsobillno == null) || (vsobillno.length() == 0)) {
/* 325 */           ExceptionUtils.wrappBusinessException(MMBDLangConstBom0202.getOrderBomCodenoNullErrMsg());
         }
 
/* 328 */         if (header.getHbdefault().equals(UFBoolean.TRUE)) {
/* 329 */           AggBomVO[] aggvos = getQueryService().queryValidOrderBomBySoBillCond(pk_org, vsobillcode, vsobillno, hfbomtype.intValue());
/* 330 */           if ((aggvos != null) && (aggvos.length > 0)) {
/* 331 */             int result = MessageDialog.showYesNoDlg(getModel().getContext().getEntranceUI(), null, MMBDLangConstBom0202.getAreYouSureSetDefault(), 4);
 
 
/* 334 */             if (result != 4) {
/* 335 */               if (MMValueCheck.isNotEmpty(this.interceptor)) {
/* 336 */                 this.continueSave = false;
               }
/* 338 */               return;
             }
           }
         }
 
       }
 
     }
 
/* 347 */     validate(aggBomVO);
 
 
/* 350 */     this.cbomid = headVO.getCbomid();
/* 351 */     this.isDefault = headVO.getHbdefault();
 
 
/* 354 */     if ((MMValueCheck.isNotEmpty(this.cbomid)) && (getModel().getUiState() == UIState.ADD)) {
/* 355 */       headVO.setCbomid(null);
 
 
 
     }
 
/* 361 */     if ((MMValueCheck.isNotEmpty(headVO.getAttributeValue("vsobillcode"))) && (MMValueCheck.isNotEmpty(headVO.getAttributeValue("vsobillno"))) && (BomCategoryEnum.NORMAL_BOM.equalsValue(headVO.getHfbomcategory())))
 
     {
/* 364 */       headVO.setVsobillcode(null);
/* 365 */       headVO.setVsobillno(null);
     }
 
/* 368 */     if (getModel().getUiState() == UIState.ADD) {
/* 369 */       AggBomVO aggvo = (AggBomVO)excuteInsert(aggBomVO);
/* 370 */       GCPseudoColUtil.getInstance().setPseudoColInfo(aggBomVO);
/* 371 */       doAddSave(aggvo);
/* 372 */       getMainGrandModel().clearBufferData();
     }
/* 374 */     else if (getModel().getUiState() == UIState.EDIT) {
/* 375 */       AggBomVO aggvo = (AggBomVO)excuteUpdate(aggBomVO);
/* 376 */       GCPseudoColUtil.getInstance().setPseudoColInfo(aggBomVO);
/* 377 */       doEditSave(aggvo);
/* 378 */       getMainGrandModel().clearBufferData();
     }
 
/* 381 */     showSuccessInfo();
 
/* 383 */     ((BomMainBillManageModel)getModel()).setActivityVOs(null);
   }
 
 
 
 
 
 
 
   protected void doAddSave(Object value)
     throws Exception
   {
/* 395 */     IBill[] clientVOs = { (IBill)value };
 
 
 
/* 399 */     GCClientBillToServer tool = new GCClientBillToServer();
 
 
/* 402 */     IBill[] lightVOs = tool.constructInsert(clientVOs);
 
/* 404 */     IBill[] afterUpdateVOs = null;
 
 
 
/* 408 */     if (getService() == null) {
/* 409 */       throw new BusinessException("service不能为空。");
     }
/* 411 */     afterUpdateVOs = getService().insert(lightVOs);
 
 
/* 414 */     new GCClientBillCombinServer().combine(clientVOs, afterUpdateVOs);
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
/* 435 */     getModel().setUiState(UIState.NOT_EDIT);
/* 436 */     getMainGrandModel().directlyAdd(clientVOs[0]);
   }
 
 
 
 
 
 
 
   protected void doEditSave(Object value)
     throws Exception
   {
/* 448 */     BomMainBillManageModel model = (BomMainBillManageModel)getModel();
 
/* 450 */     IBill[] clientVOs = { (IBill)value };
 
 
/* 453 */     GCClientBillToServer tool = new GCClientBillToServer();
/* 454 */     AggBomVO oldAggBomVO = (AggBomVO)getMainGrandModel().getSelectedData();
/* 455 */     filteInsertActivity(oldAggBomVO);
/* 456 */     IBill[] oldVO = { oldAggBomVO };
 
 
 
 
/* 461 */     IBill[] lightVOs = tool.construct(oldVO, clientVOs);
 
/* 463 */     IBill[] afterUpdateVOs = null;
 
/* 465 */     if (getService() == null) {
/* 466 */       throw new BusinessException(MMBDLangConstBom0202.getSERVICE_NOT_NULL());
     }
/* 468 */     afterUpdateVOs = ((BomMaintainProxy)getService()).update(lightVOs, true, model.getIsEcn());
 
 
/* 471 */     new GCClientBillCombinServer().combine(clientVOs, afterUpdateVOs);
 
 
/* 474 */     UFBoolean isDefaultBeforeEdit = ((BomVO)clientVOs[0].getParent()).getHbdefault();
/* 475 */     UFBoolean isDefaultAfterEdit = ((BomVO)afterUpdateVOs[0].getParent()).getHbdefault();
 
/* 477 */     if ((!(isDefaultBeforeEdit.booleanValue())) && (MMValueCheck.isNotEmpty(isDefaultAfterEdit)) && (isDefaultAfterEdit.booleanValue()))
     {
/* 479 */       BomVO originalHeadVO = (BomVO)clientVOs[0].getParent();
/* 480 */       refreshAfterEdit(originalHeadVO);
     }
 
/* 483 */     getModel().setUiState(UIState.NOT_EDIT);
/* 484 */     if (!(this.fromBomTree))
/* 485 */       getMainGrandModel().directlyUpdate(clientVOs[0]);
   }
 
   private void filteInsertActivity(AggBomVO aggvo)
   {
/* 490 */     if (MMValueCheck.isEmpty(aggvo.getChildren(BomActivityVO.class))) {
/* 491 */       return;
     }
/* 493 */     List updateAndDeleteList = new ArrayList();
/* 494 */     BomActivityVO[] activityVOs = (BomActivityVO[])Arrays.asList(aggvo.getChildren(BomActivityVO.class)).toArray(new BomActivityVO[0]);
 
/* 496 */     for (BomActivityVO vo : activityVOs)
/* 497 */       if (vo.getStatus() != 2)
/* 498 */         updateAndDeleteList.add(vo);
   }
 
   protected AbstractBill excuteInsert(AggBomVO aggBomVO)
     throws Exception
   {
/* 504 */     BomVO headVO = (BomVO)aggBomVO.getParentVO();
 
/* 506 */     if (BomTypeEnum.RTFINISH.equalsValue(headVO.getFbomtype())) {
/* 507 */       aggBomVO.setChildren(BomOutputsVO.class, null);
     }
/* 509 */     aggBomVO.getParent().setStatus(2);
/* 510 */     return aggBomVO;
   }
 
   protected AbstractBill excuteUpdate(AggBomVO aggBomVO)
     throws Exception
   {
/* 516 */     if (((BomMainBillManageModel)getModel()).getActivityVOs() != null) {
/* 517 */       BomActivityVO[] actVOs = ((BomMainBillManageModel)getModel()).getActivityVOs();
/* 518 */       aggBomVO.setChildren(BomActivityVO.class, actVOs);
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
     }
 
/* 537 */     BomVO headVO = (BomVO)aggBomVO.getParentVO();
/* 538 */     if (BomTypeEnum.RTFINISH.equalsValue(headVO.getFbomtype())) {
/* 539 */       aggBomVO.setChildren(BomOutputsVO.class, null);
     }
 
/* 542 */     return aggBomVO;
   }
 
   public boolean isExecActionSuccess() {
/* 546 */     return this.isExecActionSuccess;
   }
 
 
 
 
 
   public void setExecActionSuccess(boolean isExecActionSuccess)
   {
/* 555 */     this.isExecActionSuccess = isExecActionSuccess;
/* 556 */     showSuccessInfo();
   }
 
 
 
 
 
 
 
   private void refreshAfterEdit(BomVO originalHeadVO)
   {
/* 567 */     List<AggBomVO> allDatas = getBillForm().getModel().getMainModel().getData();
/* 568 */     String preMaterialoid = originalHeadVO.getHcmaterialid();
/* 569 */     String preMaterialvid = originalHeadVO.getHcmaterialvid();
 
/* 571 */     String preBomid = originalHeadVO.getCbomid();
/* 572 */     Integer bomType = originalHeadVO.getFbomtype();
 
/* 574 */     UFBoolean isPreDefault = originalHeadVO.getHbdefault();
/* 575 */     if (MMValueCheck.isEmpty(preMaterialvid)) {
/* 576 */       preMaterialvid = "~";
     }
 
/* 579 */     String needRefreshBomid = null;
/* 580 */     for (AggBomVO aggBomVO : allDatas) {
/* 581 */       BomVO headvo = (BomVO)aggBomVO.getParent();
/* 582 */       String nowMaterialoid = headvo.getHcmaterialid();
/* 583 */       String nowMaterialvid = headvo.getHcmaterialvid();
/* 584 */       UFBoolean isNowDefault = headvo.getHbdefault();
 
/* 586 */       String nowCbomid = headvo.getCbomid();
 
/* 588 */       Integer nowBomtype = headvo.getFbomtype();
 
/* 590 */       if (MMValueCheck.isEmpty(nowMaterialvid)) {
/* 591 */         nowMaterialvid = "~";
       }
/* 593 */       boolean isEqualsOid = preMaterialoid.equals(nowMaterialoid);
 
/* 595 */       boolean isEquialsVid = preMaterialvid.equals(nowMaterialvid);
 
/* 597 */       boolean isBothDefault = (isPreDefault.booleanValue()) && (isNowDefault.booleanValue());
 
/* 599 */       boolean isOneBom = preBomid.equals(nowCbomid);
 
/* 601 */       boolean isOneTypebom = bomType.equals(nowBomtype);
 
/* 603 */       if ((isEqualsOid) && (isEquialsVid) && (isBothDefault) && (isOneTypebom) && (!(isOneBom)))
       {
/* 605 */         needRefreshBomid = nowCbomid;
/* 606 */         break;
       }
     }
 
/* 610 */     if (MMValueCheck.isNotEmpty(needRefreshBomid)) {
/* 611 */       IBomBillQueryService queryService = (IBomBillQueryService)NCLocator.getInstance().lookup(IBomBillQueryService.class);
       try
       {
/* 614 */         AggBomVO[] originalvos = queryService.queryAggBomByBomID(new String[] { needRefreshBomid });
 
 
/* 617 */         getModel().setUiState(UIState.NOT_EDIT);
/* 618 */         getMainGrandModel().directlyUpdate(originalvos[0]);
       }
       catch (Exception e) {
/* 621 */         ExceptionUtils.wrappException(e);
       }
     }
   }
 
   public BillFormFacade getBillFormEditor() {
/* 627 */     return this.billFormEditor;
   }
 
   public void setBillFormEditor(BillFormFacade billFormEditor) {
/* 631 */     this.billFormEditor = billFormEditor;
   }
 
 
 
 
 
   private IBomBillQueryService getBomBillQueryService()
   {
/* 640 */     return ((IBomBillQueryService)NCLocator.getInstance().lookup(IBomBillQueryService.class));
   }
 
   public boolean fromBomTree() {
/* 644 */     return this.fromBomTree;
   }
 
   public void setFromBomTree(boolean isFromBomTree) {
/* 648 */     this.fromBomTree = isFromBomTree;
   }
 
 
 
 
 
   public IBomBillQueryService getQueryService()
   {
/* 657 */     return ((IBomBillQueryService)NCLocator.getInstance().lookup(IBomBillQueryService.class));
   }
 }