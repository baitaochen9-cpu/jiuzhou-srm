package nc.ui.riasm.electronicsignature.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.Action;

import nc.bs.logging.Logger;
import nc.funcnode.ui.FuncletContext;
import nc.funcnode.ui.action.SeparatorAction;
import nc.ui.mmgp.uif2.utils.MMGPMetaUtils;
import nc.ui.mmgp.uif2.view.MMGPTreeCardBillForm;
import nc.ui.mmgp.uif2.view.value.MMGPCardPanelDefaultValueSetter;
import nc.ui.pub.bill.BillTabbedPaneTabChangeEvent;
import nc.ui.pub.bill.BillTabbedPaneTabChangeListener;
import nc.ui.pubapp.uif2app.actions.AbstractBodyTableExtendAction;
import nc.ui.pubapp.uif2app.actions.intf.ICardPanelDefaultActionProcessor;
import nc.ui.pubapp.uif2app.event.OrgChangedEvent;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.uif2.AppEvent;
import nc.ui.uif2.ToftPanelAdaptor;
import nc.ui.uif2.UIState;
import nc.uif.pub.exception.UifException;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pub.BeanHelper;
import nc.vo.pub.bill.BillTabVO;
import nc.vo.riasm.electronicsignature.ElectronicSignatureBVO;
import nc.vo.riasm.electronicsignature.ElectronicSignatureVO;

public class TreeCardBillForm extends MMGPTreeCardBillForm {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2220632620703958100L;

	private String pkFieldName;

	private String parentPKFieldName;

	private MMGPCardPanelDefaultValueSetter defaultValueSetter ;
	private List<Action> bodyLineActions;
	private FuncletContext funContext;

	@Override
	public void initUI() {
		super.initUI();
		defaultValueSetter = new MMGPCardPanelDefaultValueSetter();
		changeBodyLineToMap();
		/*     */     
		/* 247 */     Map<String, List<Action>> bodyActionMap = getBodyActionMap();
		/* 248 */     for (Map.Entry<String, List<Action>> entry : bodyActionMap.entrySet()) {
		/* 249 */       processBodyLineActions((List)entry.getValue());
		/* 250 */       processPopupMenu((String)entry.getKey(), (List)entry.getValue());
		/*     */     }
		/*     */     
		/* 253 */     if (billCardPanel.getBodyTabbedPane() != null) {
		/* 254 */       String tabCode = billCardPanel.getBodyTabbedPane().getSelectedTableCode();
		/*     */       
		/* 256 */       setBodyTabActive(tabCode);
		/*     */     }
		/*     */     
		/*     */ 
		/* 260 */     BillTabbedPaneTabChangeListener tabChangeHandler = new BillTabbedPaneTabChangeListener()
		/*     */     {
		/*     */       public void afterTabChanged(BillTabbedPaneTabChangeEvent e) {
		/* 263 */         BillTabVO tab = e.getBtvo();
		/* 264 */         if (tab != null) {
		/* 265 */           setBodyTabActive(tab.getTabcode());
		/*     */         }
		/*     */         
		/*     */       }
		/*     */       
		/* 270 */     };
		/* 271 */     getBillCardPanel().setBodyAutoAddLine(false);
	}

	 private void changeBodyLineToMap()
	 /*     */   {
	 /* 593 */     if (getBodyLineActions().size() > 0) {
	 /* 594 */       Map<String, List<Action>> bodyActionMap = getBodyActionMap();
	 /* 595 */       if (null == bodyActionMap) {
	 /* 596 */         bodyActionMap = new HashMap();
	 /*     */       }
	 /* 598 */       String[] tableCodes = getBillCardPanel().getBillData().getBodyTableCodes();
	 /*     */       
	 /* 600 */       if (null != tableCodes) {
	 /* 601 */         for (String tableCode : tableCodes) {
	 /* 602 */           if (!bodyActionMap.containsKey(tableCode)) {
	 /* 603 */             List<Action> actions = new ArrayList(getBodyLineActions());
	 /*     */             
	 /* 605 */             bodyActionMap.put(tableCode, actions);
	 /*     */           }
	 /*     */         }
	 /*     */       }
	 /* 609 */       super.setBodyActionMap(bodyActionMap);
	 /*     */     }
	 /*     */   }
	 /*     */   
	 /*     */ 
	 /*     */ 
	 /*     */ 
	 /*     */   private void processBodyLineActions(List<? extends Action> bodyActions)
	 /*     */   {
	 /* 618 */     for (int i = 0; i < bodyActions.size(); i++) {
	 /* 619 */       Action action = (Action)bodyActions.get(i);
	 /* 620 */       if ((action instanceof AbstractBodyTableExtendAction)) {
	 /* 621 */         ((AbstractBodyTableExtendAction)action).setEnabled(false);
	 /* 622 */         ((AbstractBodyTableExtendAction)action).setCardPanel(getBillCardPanel());
	 /*     */         
	 /* 624 */         ((AbstractBodyTableExtendAction)action).setModel(getModel());
	 /*     */       }
	 /*     */     }
	 /*     */   }
	 /*     */   
	 /*     */ 
	 /*     */   private void processPopupMenu(String tableCode, List<? extends Action> bodyActions)
	 /*     */   {
	 /* 632 */     if ((tableCode == null) || ("".equals(tableCode)) || (bodyActions == null) || (bodyActions.size() == 0))
	 /*     */     {
	 /* 634 */       return;
	 /*     */     }
	 /*     */     
	 /* 637 */     getBillCardPanel().getBodyPanel(tableCode).clearDefalutEditAction();
	 /*     */     
	 /* 639 */     for (int i = 0; i < bodyActions.size(); i++) {
	 /* 640 */       Action action = (Action)bodyActions.get(i);
	 /*     */       
	 /* 642 */       if (isHasPowerAction(action))
	 /*     */       {
	 /*     */ 
	 /* 645 */         if ((action instanceof AbstractBodyTableExtendAction)) {
	 /* 646 */           if ((action instanceof ICardPanelDefaultActionProcessor)) {
	 /* 647 */             int type = ((ICardPanelDefaultActionProcessor)action).getType();
	 /*     */             
	 /* 649 */             getBillCardPanel().getBodyPanel(tableCode).replaceDefaultAction(type, action);
	 /*     */           }
	 /*     */           else {
	 /* 652 */             getBillCardPanel().getBodyPanel(tableCode).addFixAction(action);
	 /*     */           }
	 /*     */         }
	 /*     */       }
	 /*     */     }
	 /*     */   }
	 
	  private FuncletContext getFunContext()
	  /*     */   {
	  /* 665 */     if ((getModel() == null) || (getModel().getContext() == null)) {
	  /* 666 */       return null;
	  /*     */     }
	  /* 668 */     if ((funContext == null) && 
	  /* 669 */       ((getModel().getContext().getEntranceUI() instanceof ToftPanelAdaptor))) {
	  /* 670 */       funContext = ((ToftPanelAdaptor)getModel().getContext().getEntranceUI()).getFuncletContext();
	  /*     */     }
	  /*     */     
	  /*     */ 
	  /*     */ 
	  /* 675 */     return funContext;
	  /*     */   }
	  /*     */   
	  /*     */   private boolean isHasPowerAction(Action action)
	  /*     */   {
	  /* 680 */     if ((action instanceof SeparatorAction)) {
	  /* 681 */       return true;
	  /*     */     }
	  /* 683 */     String code = (String)action.getValue("Code");
	  /* 684 */     if (StringUtil.isEmptyWithTrim(code)) {
	  /* 685 */       return true;
	  /*     */     }
	  /* 687 */     if (getFunContext() == null) {
	  /* 688 */       return true;
	  /*     */     }
	  /* 690 */     boolean power = getFunContext().checkActionPower(code);
	  /* 691 */     return power;
	  /*     */   }
	  /*     */ 
	 
	   public List<Action> getBodyLineActions() {
		   /* 160 */     if (null == bodyLineActions) {
		   /* 161 */       bodyLineActions = new ArrayList(0);
		   /*     */     }
		   /* 163 */     return bodyLineActions;
		   /*     */   }
	   
	   public void setBodyLineActions(List<Action> actions) {
		   /* 311 */     bodyLineActions = actions;
		   /*     */   }
	   
	protected void setBillItemValue(String key, Object value) {
		if (getBillCardPanel().getHeadItem(key) != null) {
			getBillCardPanel().getHeadItem(key).setValue(value);
		}
	}
	
	@Override
	public void setValue(Object object) {
		super.setValue(object);
		if(getModel().getUiState() == UIState.ADD){
			setDefaultValue();
		}
		setBodyValue(object);
	}
	private void setBodyValue(Object object){
		ElectronicSignatureVO vo = (ElectronicSignatureVO)object;
		try {
			ElectronicSignatureBVO[] vos =
			(ElectronicSignatureBVO[]) HYPubBO_Client.queryByCondition(ElectronicSignatureBVO.class," nvl(dr,0)=0 and billid = '"+vo.getPrimaryKey()+"'");
			getBillCardPanel().getBillModel().setBodyDataVO(vos);
			getBillCardPanel().getBillModel().execLoadFormula();
			getBillCardPanel().getBillModel().loadLoadRelationItemValue();
		} catch (UifException e) {
			e.printStackTrace();
		}
		
		
	}

	@Override
	protected void setDefaultValue() {
		super.setDefaultValue();
		defaultValueSetter.setDefaultValue(getModel().getContext(),getBillCardPanel());
		Object selObj = getModel().getSelectedData();
		if (selObj == null) {
			return;
		}
		getBillCardPanel().getHeadItem(getParentPKFieldName()).setValue(
				BeanHelper.getProperty(selObj, getPkFieldName()));
	}

	public String getPkFieldName() {
		if (pkFieldName == null) {
			pkFieldName = MMGPMetaUtils.getPKFieldName(getModel().getContext());
		}
		return pkFieldName;
	}

	public void setPkFieldName(String pkFieldName) {
		this.pkFieldName = pkFieldName;
	}

	public String getParentPKFieldName() {
		if (parentPKFieldName == null) {
			parentPKFieldName = MMGPMetaUtils.getParentPKFieldName(getModel()
					.getContext());
		}
		return parentPKFieldName;
	}

	public void setParentPKFieldName(String parentPKFieldName) {
		this.parentPKFieldName = parentPKFieldName;
	}

	@Override
	public void handleEvent(AppEvent event) {
		super.handleEvent(event);
		if(OrgChangedEvent.class.getName().equals(event.getType())){
			OrgChangedEvent orgChangedEvent = (OrgChangedEvent) event;
			String pk_org = orgChangedEvent.getNewPkOrg();
			getModel().getContext().setPk_org(pk_org);
			getBillCardPanel().addNew();
			setDefaultValue();
		}
	}
	protected void synchronizeDataFromModel() {
		Logger.debug("entering synchronizeDataFromModel");
		Object selectedData = getModel().getSelectedData();
		if(selectedData instanceof ElectronicSignatureVO){
			setValue(selectedData);
		}
		Logger.debug("leaving synchronizeDataFromModel");
	}

}