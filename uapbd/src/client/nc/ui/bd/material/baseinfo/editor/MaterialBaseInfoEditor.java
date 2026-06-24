package nc.ui.bd.material.baseinfo.editor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.bs.uif2.BusinessExceptionAdapter;
import nc.itf.fi.pub.SysInit;
import nc.itf.org.IOrgConst;
import nc.itf.uap.IUAPQueryBS;
import nc.md.data.access.NCObject;
import nc.md.model.MetaDataException;
import nc.md.persist.framework.IMDPersistenceQueryService;
import nc.md.persist.framework.MDPersistenceService;
import nc.medpub.util.SysInitParamUtil;
import nc.pub.billcode.itf.IBillcodeManage;
import nc.pub.billcode.vo.BillCodeContext;
import nc.pubitf.para.SysInitQuery;
import nc.ui.bd.material.assistant.view.MarAsstPanel;
import nc.ui.bd.material.baseinfo.model.MaterialBaseInfoModel;
import nc.ui.bd.pub.bill.itemeditors.ImageBillItemEditor_m;
import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.medpub.pub.formular.cust.Pinyin4jUtil;
import nc.ui.medpub.utils.GroupCheckUtils;
import nc.ui.medpub.utils.MaterialPropertyUtil;
import nc.ui.medpub.utils.ReferencedValidationUtil;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIFractionTextField;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.beans.UIScrollPane;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillData;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillEditListener;
import nc.ui.pub.bill.BillEditListener2;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.bill.IBillItem;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.uif2.AppEvent;
import nc.ui.uif2.DefaultExceptionHanler;
import nc.ui.uif2.IExceptionHandler;
import nc.ui.uif2.IFunNodeClosingListener;
import nc.ui.uif2.LongUITask;
import nc.ui.uif2.UIState;
import nc.ui.uif2.components.AutoShowUpEventSource;
import nc.ui.uif2.components.IAutoShowUpComponent;
import nc.ui.uif2.components.IAutoShowUpEventListener;
import nc.ui.uif2.components.ITabbedPaneAwareComponent;
import nc.ui.uif2.components.ITabbedPaneAwareComponentListener;
import nc.ui.uif2.components.TabbedPaneAwareCompnonetDelegate;
import nc.ui.uif2.editor.BillForm;
import nc.ui.uif2.editor.IUserdefitemPreparator;
import nc.ui.uif2.model.AppEventConst;
import nc.uif.pub.exception.UifException;
import nc.vo.bd.material.IMaterialConst;
import nc.vo.bd.material.MaterialConvertVO;
import nc.vo.bd.material.MaterialTaxTypeVO;
import nc.vo.bd.material.MaterialVO;
import nc.vo.bd.material.marasstframe.MarAsstFrameVO;
import nc.vo.bd.material.marbasclass.MarBasClassVO;
import nc.vo.bd.material.measdoc.MeasdocVO;
import nc.vo.bd.material.pf.AggMaterialPfVO;
import nc.vo.bd.material.pf.MaterialPfVO;
import nc.vo.bd.pub.BDCacheQueryUtil;
import nc.vo.bd.pub.IBDSysInitCodeConst;
import nc.vo.bd.ref.IFilterStrategy;
import nc.vo.mmgp.util.MMBooleanUtil;
import nc.vo.pub.BusinessException;
import nc.vo.pub.ValidationException;
import nc.vo.pub.bill.BillTabVO;
import nc.vo.pub.bill.MetaDataEditPropertyAdpter;
import nc.vo.pub.lang.MultiLangText;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.uap.material.med_148;
import nc.vo.uap.material.extend.EnumMedtype_148;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

/**
 * 物料基本信息编辑界面
 * 
 * @author jiangjuna
 */
@SuppressWarnings("serial")
public class MaterialBaseInfoEditor extends BillForm implements IAutoShowUpComponent, ITabbedPaneAwareComponent,
		BillEditListener, BillEditListener2 {
	//add by zhaolid
	private static final String MARFRAMECODE2 = "MED002";//产品批号既库存状态辅助属性
	private static final String MARFRAMECODE1 = "MED001";
	private static final String MARANTCODE1 = "11";//产品批号
	private static final String MARANTCODE2 = "1";//库存状态
	//end zhaolid
	private static final String AUTO_CODE_BACK = "material_temp_code";

	private Set<String> assistunits = new HashSet<String>();

	private IAutoShowUpComponent autoShowUpComponent;

	private String billcode = null;

	private IBillcodeManage billcodeManage = null;

	private IFunNodeClosingListener closingListener = null;

	private Object currentData = null;

	private IExceptionHandler exceptionHandler = null;

	/** 物料辅助属性界面 */
	private MarAsstPanel marAsstPanel = null;

	private Map<String, MeasdocVO> measdoc_map = new HashMap<String, MeasdocVO>();

	private Map<String, BillCodeContext> orgID_billCodeContext_map = new HashMap<String, BillCodeContext>();

	/** 物料共享页签 */
	private MaterialShareInfoEditor shareInfoEditor;

	private ITabbedPaneAwareComponent tabbedPaneAwareComponent;

	{
		this.assistunits.add(MaterialConvertVO.ISPUMEASDOC);
		this.assistunits.add(MaterialConvertVO.ISSALEMEASDOC);
		this.assistunits.add(MaterialConvertVO.ISPRODMEASDOC);
		this.assistunits.add(MaterialConvertVO.ISSTOCKMEASDOC);
		this.assistunits.add(MaterialConvertVO.ISRETAILMEASDOC);
	}

	public MaterialBaseInfoEditor() {
		this.autoShowUpComponent = new AutoShowUpEventSource(this);
		this.tabbedPaneAwareComponent = new TabbedPaneAwareCompnonetDelegate();
		this.tabbedPaneAwareComponent.setComponentVisible(true);
	}

	@Override
	public void addTabbedPaneAwareComponentListener(ITabbedPaneAwareComponentListener l) {
		this.tabbedPaneAwareComponent.addTabbedPaneAwareComponentListener(l);
	}

	@SuppressWarnings("restriction")
	@Override
	public void afterEdit(BillEditEvent e) {
		try {
			// med huangbin 2013.04.24 start
			if (GroupCheckUtils.isMedGroup(this.getModel().getContext().getPk_group())) {
				if (e.getKey().equals("bsupervision_148")) {
					doAfterSuperVision(e);
				} else if (e.getKey().equals("bmedicine_148")) {
					doAfterHeadMedicineEdit();
				} else if (e.getKey().equals("cdosageform_148")) {
					doAfterHeadDosageform();
				} else if (e.getKey().equals("bexpirymanage_148")) {
					doAfterHeadExpirymanage(e);
				} else if (e.getKey().equals("icaredays_148")) {
					doAfterHeadCaredays(e);
				} else if (e.getKey().equals("blotno_148")) {
					doAfterFareMarasstFrame(e);
					doAfterLotColumnsChange();
				} else if (e.getKey().equals("blotnoicstatus_148")) {
					doAfterBlotNoSts(e);
					doAfterLotColumnsChange();
				} else if (e.getKey().equals("cbigclass_148")) {
					doAfterBigClzEditor(e);
				} else if (e.getKey().equals("csubclass_148")) {
					doAfterSubClzEditor(e);
				}else if(e.getKey().equals("imedtype_148")){
					doAfterIMedtypeEditor(e);
				}else if(e.getKey().equals("imed_148")){
					doAfterIMedEditor(e);
				}else if(e.getKey().equals("bvalidmanage_148")){
					doAfterBvalidmanageEditor();
				}else 
					
//					added by wangsyf 2014-10-30 start  通用名/商品名编辑后事件 生成对应字段的拼音码
				if("name".equals(e.getKey())){
//					genSPCode(e.getKey(), "vcommonname_sp_148");//医药页签通用名拼音
//					genSPCode(e.getKey(), "materialmnecode");//基本页签助记码
					Object obj = getBillCardPanel().getHeadItem(e.getKey()).getValueObject();
					String fromVal = obj!= null && obj.getClass().equals(MultiLangText.class) ? ((MultiLangText)obj).getText() : (String)obj;
					if (!StringUtils.isBlank(fromVal)) {
						  String pk_group =  MaterialBaseInfoEditor.this.getModel().getContext().getPk_group();
                    	  UFBoolean isOpen = UFBoolean.FALSE;
                  		try {
							 isOpen = SysInitParamUtil.getParaBoolean(pk_group,SysInitParamUtil.MEDGSP016PARAM);
						} catch (BusinessException e1) {
							ExceptionUtils.wrappException(e1);
						}
						
						//若助记码已有值，则不再生成
                  	  /*Object nameMneCodeOld = getBillCardPanel().getHeadItem("vcommonname_sp_148").getValueObject();
                  	  Object materialMneCodeOld = getBillCardPanel().getHeadItem("materialmnecode").getValueObject();*/
                  	if(isOpen != null && isOpen.booleanValue()){
                  		String mneCode = Pinyin4jUtil.converterToFirstSpell(fromVal,getModel().getContext().getEntranceUI());
                  		getBillCardPanel().getHeadItem("vcommonname_sp_148").setValue(mneCode);//医药页签通用名拼音
                  		getBillCardPanel().getHeadItem("materialmnecode").setValue(mneCode);//基本页签助记码
                  		}
					}else{
						getBillCardPanel().getHeadItem("vcommonname_sp_148").setValue(null);//医药页签通用名拼音
                  		getBillCardPanel().getHeadItem("materialmnecode").setValue(null);//基本页签助记码
					}
					
				}else if("vcommonname_148".equals(e.getKey())){
					genSPCode(e.getKey(), "vcommonname_sp_148");//医药页签通用名拼音
				}else if("vtradename_148".equals(e.getKey())){
					genSPCode(e.getKey(), "vtradename_sp_148");//医药页签商品名拼音
				}
//				added by wangsyf 2014-10-30 end

				// end
				//add by sunjiand 当物料分类为器械时，型号和材料（自定义项3）必输，经确认物料分类器械分类编码必以6开头
				else if ("pk_marbasclass".equals(e.getKey())) {
//					TODOOOOO
					doAfterMarbasclassEditor(e);
					Object pk_measdoc = getBillCardPanel().getHeadItem("pk_measdoc").getValueObject();
					getBillCardPanel().getHeadItem("cinnerpack_148").setValue(pk_measdoc);
					
					
				}
				//end
				if (e.getKey().equals(MaterialVO.PK_MEASDOC) && e.getPos() == IBillItem.HEAD) {
					this.doAfterHeadMeasdocEdit();
				}
				// else if (e.getKey().equals(MaterialVO.PK_MARBASCLASS)) {
				// this.synchronizeMarAsstFrameFromClass();//zhaoli
				// }
			}
			
			
			if (e.getKey().equals(MaterialVO.PK_MEASDOC) && e.getPos() == IBillItem.HEAD) {
		        this.doAfterHeadMeasdocEdit();
		      } else if (e.getKey().equals(MaterialVO.PK_MARBASCLASS)) {
		        BillCardPanel panel = this.getBillCardPanel();
			    Boolean blotnoicstatus_148 = (Boolean) panel.getHeadItem("blotnoicstatus_148").getValueObject();
				Boolean blotno_148 = (Boolean) panel.getHeadItem("blotno_148").getValueObject();
				IMDPersistenceQueryService qry = MDPersistenceService.lookupPersistenceQueryService();
				Collection<MarAsstFrameVO> result = null;
				try {
					result = qry.queryBillOfVOByCond(MarAsstFrameVO.class, " pk_group='" + InvocationInfoProxy.getInstance().getGroupId() + "'", false);
				} catch (MetaDataException eX) {
					ExceptionUtils.wrappException(eX);
				}
				if(blotno_148.booleanValue() && !result.isEmpty()){
					for (MarAsstFrameVO voX : result) {
						if(voX.getCode().equalsIgnoreCase("MED001")){
//							vo.setPk_marasstframe(voX.getPk_marasstframe());
							panel.getHeadItem("pk_marbasclass.pk_marasstframe").setValue(voX.getPk_marasstframe());
							break;
						}
					}
					
				}else if(blotnoicstatus_148.booleanValue()){
					for (MarAsstFrameVO voX : result) {
						if(voX.getCode().equalsIgnoreCase("MED002")){
							panel.getHeadItem("pk_marbasclass.pk_marasstframe").setValue(voX.getPk_marasstframe());
							break;
						}
					}
				}
				this.synchronizeMarAsstFrameFromClass();
		      } else if (e.getKey().equals(MaterialVO.FEE)) {
		        this.resetMaterialmgt();
		      } else if (e.getKey().equals(MaterialVO.DISCOUNTFLAG)) {
		        this.doAfterDiscountEdit();
		        this.resetMaterialmgt();
		      } else if (e.getKey().equals(MaterialConvertVO.PK_MEASDOC) && e.getPos() == IBillItem.BODY) {
		        this.doAfterConvertMeasdocEdit(e);
		      } else if (e.getKey().equals(MaterialConvertVO.ISSTOREBALANCE)) {
		        this.doAfterStorebalanceEdit(e);
		      } else if (e.getPos() == IBillItem.BODY && this.assistunits.contains(e.getKey())) {
		        this.doAfterAssisUnitEdit(e);
		      } else if (e.getKey().equals(MaterialVO.RETAIL)) {
		        this.doAfterRetailEdit(e);
		      } else if (e.getKey().equals(MaterialConvertVO.FIXEDFLAG)) {
		        this.doAfterFixedflagEdit(e);
		      } else if (e.getKey().equals(MaterialVO.PK_GOODSCODE)) {
		        MaterialVO materialVO = (MaterialVO) this.getModel().getSelectedData();
		        if (StringUtils.isEmpty(materialVO.getGoodsprtname())) {
		          this.setDefaultGoodsPrtName();
		        }
		      } else if (e.getKey().equals(MaterialVO.ISFEATURE)) {
		        resetFeatureclassValue();
		      }
			
			
//			if (e.getKey().equals(MaterialVO.FEE)) {
//				this.resetMaterialmgt();
//			} else if (e.getKey().equals(MaterialVO.DISCOUNTFLAG)) {
//				this.resetMaterialmgt();
//			} else if (e.getKey().equals(MaterialConvertVO.PK_MEASDOC) && e.getPos() == IBillItem.BODY) {
//				this.doAfterConvertMeasdocEdit(e);
//			} else if (e.getKey().equals(MaterialConvertVO.ISSTOREBALANCE)) {
//				this.doAfterStorebalanceEdit(e);
//			} else if (e.getPos() == IBillItem.BODY && this.assistunits.contains(e.getKey())) {
//				this.doAfterAssisUnitEdit(e);
//			}
			
		} catch (BusinessException ex) {
			this.getExceptionHandler().handlerExeption(ex);
		}
	}

	private void doAfterMarbasclassEditor(BillEditEvent e) {
		Object[] basclass = (Object[]) e.getValue();
		
		if (basclass != null && basclass.length > 0) {
			try {
				String code = (String) HYPubBO_Client.findColValue("bd_marbasclass", "code", "pk_marbasclass = '" + basclass[0] + "'");
				if (code != null && code.startsWith("6")) {
					getBillCardPanel().getHeadItem("materialtype").setNull(true);
					getBillCardPanel().getHeadItem("def3").setNull(true);
				}else {
					getBillCardPanel().getHeadItem("materialtype").setNull(false);
					getBillCardPanel().getHeadItem("def3").setNull(false);
				}
			} catch (UifException e1) {
				ExceptionUtils.wrappException(e1);
			}
		}else {
			getBillCardPanel().getHeadItem("materialtype").setNull(false);
			getBillCardPanel().getHeadItem("def3").setNull(false);
		}
	}
	
	private void genSPCode(String from,String to){
		Object obj = getBillCardPanel().getHeadItem(from).getValueObject();
		String fromVal = obj!= null && obj.getClass().equals(MultiLangText.class) ? ((MultiLangText)obj).getText() : (String)obj;
		if (!StringUtils.isBlank(fromVal)) {
			//若助记码已有值，则不再生成
	      	  Object mneCodeOld = getBillCardPanel().getHeadItem(to).getValueObject();
	      	 String pk_group =  MaterialBaseInfoEditor.this.getModel().getContext().getPk_group();
       	     UFBoolean isOpen = UFBoolean.FALSE;
     		try {
				 isOpen = SysInitParamUtil.getParaBoolean(pk_group,SysInitParamUtil.MEDGSP016PARAM);
			} catch (BusinessException e1) {
				ExceptionUtils.wrappException(e1);
			}
       	  if(null == mneCodeOld && (isOpen != null && isOpen.booleanValue())){
	      		  String mneCode = Pinyin4jUtil
	      				  .converterToFirstSpell(fromVal,getModel().getContext()
	      						  .getEntranceUI());
	      		  getBillCardPanel().getHeadItem(to).setValue(mneCode);
	      	  }
		}
	}
	/**
	 * 批号，批号库存状态checkbox编辑后事件 ，处理与效期管理的联动效果   wangsyf  2015-08-31
	 */
	private void doAfterLotColumnsChange(){
		boolean blotno = (Boolean) getBillCardPanel().getHeadItem("blotno_148").getValueObject();
		boolean blotnoicstatus =  (Boolean) getBillCardPanel().getHeadItem("blotnoicstatus_148").getValueObject();
		if (!blotno && !blotnoicstatus) {
			getBillCardPanel().getHeadItem("iexpiryunit_148").setEdit(Boolean.FALSE);//效期单位
			getBillCardPanel().getHeadItem("iexpiryunit_148").setValue(null);
			getBillCardPanel().getHeadItem("iexpiryunit_148").setNull(Boolean.FALSE);
			getBillCardPanel().getHeadItem("iexpirydate_148").setEdit(Boolean.FALSE);//有效期
			getBillCardPanel().getHeadItem("iexpirydate_148").setValue(null);
			getBillCardPanel().getHeadItem("iexpirydate_148").setNull(Boolean.FALSE);
			getBillCardPanel().getHeadItem("ivaliduntilunit_148").setEdit(Boolean.FALSE);//有效期至单位
			getBillCardPanel().getHeadItem("ivaliduntilunit_148").setValue(null);
			getBillCardPanel().getHeadItem("ivaliduntilunit_148").setNull(Boolean.FALSE);
			getBillCardPanel().getHeadItem("icaredays_148").setEdit(Boolean.FALSE);//近效期预警天数
			getBillCardPanel().getHeadItem("icaredays_148").setValue(null);
			getBillCardPanel().getHeadItem("icaredays_148").setNull(Boolean.FALSE);
			getBillCardPanel().getHeadItem("bvalidmanage_148").setEdit(false);
			getBillCardPanel().getHeadItem("bvalidmanage_148").setValue(false);
		}else{
			getBillCardPanel().getHeadItem("bvalidmanage_148").setEdit(true);
		}
	}
	
	
	private void doAfterBvalidmanageEditor(){
		Object bvalidmanage = getBillCardPanel().getHeadItem("bvalidmanage_148").getValueObject();
		if(bvalidmanage != null &&
				Boolean.TRUE.equals(bvalidmanage)){
			getBillCardPanel().getHeadItem("iexpiryunit_148").setEdit(Boolean.TRUE);//效期单位
			getBillCardPanel().getHeadItem("iexpiryunit_148").setNull(Boolean.TRUE);
			getBillCardPanel().getHeadItem("iexpirydate_148").setEdit(Boolean.TRUE);//有效期
			getBillCardPanel().getHeadItem("iexpirydate_148").setNull(Boolean.TRUE);
			getBillCardPanel().getHeadItem("ivaliduntilunit_148").setEdit(Boolean.TRUE);//有效期至单位
			getBillCardPanel().getHeadItem("ivaliduntilunit_148").setNull(Boolean.TRUE);
			getBillCardPanel().getHeadItem("icaredays_148").setEdit(Boolean.TRUE);//近效期预警天数
			getBillCardPanel().getHeadItem("icaredays_148").setNull(Boolean.TRUE);
		}else{
			getBillCardPanel().getHeadItem("iexpiryunit_148").setEdit(Boolean.FALSE);//效期单位
			getBillCardPanel().getHeadItem("iexpiryunit_148").setValue(null);
			getBillCardPanel().getHeadItem("iexpiryunit_148").setNull(Boolean.FALSE);
			getBillCardPanel().getHeadItem("iexpirydate_148").setEdit(Boolean.FALSE);//有效期
			getBillCardPanel().getHeadItem("iexpirydate_148").setValue(null);
			getBillCardPanel().getHeadItem("iexpirydate_148").setNull(Boolean.FALSE);
			getBillCardPanel().getHeadItem("ivaliduntilunit_148").setEdit(Boolean.FALSE);//有效期至单位
			getBillCardPanel().getHeadItem("ivaliduntilunit_148").setValue(null);
			getBillCardPanel().getHeadItem("ivaliduntilunit_148").setNull(Boolean.FALSE);
			getBillCardPanel().getHeadItem("icaredays_148").setEdit(Boolean.FALSE);//近效期预警天数
			getBillCardPanel().getHeadItem("icaredays_148").setValue(null);
			getBillCardPanel().getHeadItem("icaredays_148").setNull(Boolean.FALSE);
		}
	}
	private void doAfterIMedtypeEditor(BillEditEvent e){
		Object medtype = getBillCardPanel().getHeadItem("imedtype_148").getValueObject();
		if(medtype != null && 
				EnumMedtype_148.CNMED.getEnumValue().getValue().equals(medtype.toString())){
			getBillCardPanel().getHeadItem("bchnherbalmed_148").setEdit(true);
			getBillCardPanel().getHeadItem("bchnmed_148").setEdit(true);
			getBillCardPanel().getHeadItem("bchnmedgranules_148").setEdit(true);
		}else{
			getBillCardPanel().getHeadItem("bchnherbalmed_148").setEdit(false);
			getBillCardPanel().getHeadItem("bchnherbalmed_148").setValue(Boolean.FALSE);
			getBillCardPanel().getHeadItem("bchnmed_148").setEdit(false);
			getBillCardPanel().getHeadItem("bchnmed_148").setValue(Boolean.FALSE);
			getBillCardPanel().getHeadItem("bchnmedgranules_148").setEdit(false);
			getBillCardPanel().getHeadItem("bchnmedgranules_148").setValue(Boolean.FALSE);
		}
	}
	
	private void doAfterIMedEditor(BillEditEvent e){
		Object imed = getBillCardPanel().getHeadItem("imed_148").getValueObject();
		if(imed != null && 
				med_148.NONMED.getEnumValue().getValue().equals(imed.toString())){
			getBillCardPanel().getHeadItem("bfood_148").setEdit(true);
			getBillCardPanel().getHeadItem("bhealthfood_148").setEdit(true);
			getBillCardPanel().getHeadItem("bcosmetic_148").setEdit(true);
		}else{
			getBillCardPanel().getHeadItem("bfood_148").setEdit(false);
			getBillCardPanel().getHeadItem("bfood_148").setValue(Boolean.FALSE);
			getBillCardPanel().getHeadItem("bhealthfood_148").setEdit(false);
			getBillCardPanel().getHeadItem("bhealthfood_148").setValue(Boolean.FALSE);
			getBillCardPanel().getHeadItem("bcosmetic_148").setEdit(false);
			getBillCardPanel().getHeadItem("bcosmetic_148").setValue(Boolean.FALSE);
		}
	}
	
	private void doAfterSubClzEditor(BillEditEvent e) {
		UIRefPane pane = (UIRefPane) getBillCardPanel().getHeadItem("csubclass_148").getComponent();
		@SuppressWarnings("rawtypes")
		Vector v = pane.getRefModel().getSelectedData();
		int biclzIndex = pane.getRefModel().getFieldIndex("pk_type_up");
		if (v != null && v.size() > 0) {
			@SuppressWarnings("rawtypes")
			Vector data = (Vector) v.get(0);
			if (data != null && data.size() > 0) {
				getBillCardPanel().setHeadItem("cbigclass_148", data.get(biclzIndex));
			}
		}
	}

	private void doAfterBigClzEditor(BillEditEvent e) {
		Object typeup = getBillCardPanel().getHeadItem("cbigclass_148").getValueObject();
		if (typeup == null || typeup.toString().length() == 0){
			getBillCardPanel().setHeadItem("csubclass_148", null);
		} else {
			getheadRefModel("csubclass_148").addWherePart(" and pk_type_up = '"+ typeup +"'");
		}
	}

	// 为辅助属性结构赋值 zhaoli 2013-6-25
	protected void doAfterFareMarasstFrame(BillEditEvent e) throws BusinessException {
		if (MMBooleanUtil.objToBooleanValue(e.getValue())) {
			String pk = MaterialPropertyUtil.getPkMarasstframe(MARFRAMECODE1, MARANTCODE1);						
//					MMStringUtil.objectToString(HYPubBO_Client.findColValue("bd_marasstframe", "pk_marasstframe",
//					"code = 'MED001'"));
			getBillCardPanel().setHeadItem("pk_marasstframe", pk);
			// getMarAsstPanel().getBillCardPanel().setHeadItem("pk_marasstframe",
			// pk);
			if (this.getModel().getContext().getNodeCode().equals("10140MAG")||
					this.getModel().getContext().getNodeCode().equals("10140MAO")) {
				getMarAsstPanel().getBillCardPanel().setHeadItem("marasstframe", pk);
				getMarAsstPanel().setPk_marasstframe(pk);
			}
			getBillCardPanel().getHeadItem("blotnoicstatus_148").setEdit(false);
		} else {
			getBillCardPanel().getHeadItem("blotnoicstatus_148").setEdit(true);
			getBillCardPanel().setHeadItem("pk_marasstframe", null);
			if (this.getModel().getContext().getNodeCode().equals("10140MAG")||
					this.getModel().getContext().getNodeCode().equals("10140MAO")) {
				getMarAsstPanel().getBillCardPanel().setHeadItem("marasstframe", null);
				getMarAsstPanel().setPk_marasstframe(null);
			}
		}
	}

	protected void doAfterBlotNoSts(BillEditEvent e) throws BusinessException {
		boolean flag = (e == null ? true : Boolean.valueOf((boolean) e.getValue()));
		String pk = MaterialPropertyUtil.getPkMarasstframe(MARFRAMECODE2, MARANTCODE1);				
		if (MMBooleanUtil.objToBooleanValue(flag)) {	
//					
//					MMStringUtil.objectToString(HYPubBO_Client.findColValue("bd_marasstframe", "pk_marasstframe",
//					"code = 'MED002'"));
			getBillCardPanel().setHeadItem("pk_marasstframe", pk);
			if (this.getModel().getContext().getNodeCode().equals("10140MAG")||
					this.getModel().getContext().getNodeCode().equals("10140MAO")) {
				getMarAsstPanel().getBillCardPanel().setHeadItem("marasstframe", pk);
				getMarAsstPanel().setPk_marasstframe(pk);
			}
			getBillCardPanel().getHeadItem("blotno_148").setValue(false);
			getBillCardPanel().getHeadItem("blotno_148").setEdit(false);
		} else {
			getBillCardPanel().getHeadItem("blotno_148").setEdit(true);
			getBillCardPanel().setHeadItem("pk_marasstframe", null);
			if (this.getModel().getContext().getNodeCode().equals("10140MAG")||
					this.getModel().getContext().getNodeCode().equals("10140MAO")) {
				getMarAsstPanel().getBillCardPanel().setHeadItem("marasstframe", null);
				getMarAsstPanel().setPk_marasstframe(null);
			}
		}
//		Object valueObject = getMarAsstPanel().getBillCardPanel().getHeadItem("pk_marasstframe").getValueObject();
		MaterialVO value = (MaterialVO) getMarAsstPanel().getValue();
		String pk_marasstframe = value.getPk_marasstframe();
		System.out.println(pk_marasstframe);
		getMarAsstPanel().setValue(value);
		getMarAsstPanel().getBillCardPanel().setHeadItem("pk_marasstframe", pk_marasstframe);
	}

	// end
	private void doAfterHeadCaredays(BillEditEvent e) {
		String obj = e.getValue() == null ? "" : e.getValue().toString();
		if (obj.length() > 0) {
			for (int i = 0; i < obj.length(); i++) {
				char c = obj.charAt(i);
				if (c > '9' || c < '0') {
					MessageDialog.showErrorDlg(null, "提示", "预警天数只能是正整数");
					getBillCardPanel().setHeadItem("icaredays_148", null);
					getBillCardPanel().getHeadItem("icaredays_148").getComponent().requestFocus();
					return;
				}
			}
		}
	}

	private void doAfterHeadExpirymanage(BillEditEvent e) {
		boolean bexpirymanage = e.getValue() == null ? false : new UFBoolean(e.getValue().toString()).booleanValue();
		getBillCardPanel().getHeadItem("icaredays_148").setNull(bexpirymanage);

	}

	@SuppressWarnings("rawtypes")
	private void doAfterHeadDosageform() {
		Vector selV = getheadRefModel("cdosageform_148").getSelectedData();
		if (selV != null && selV.size() > 0) {
			Vector data = (Vector) selV.get(0);
			if ("N".equals(data.get(4))) {
				getBillCardPanel().setHeadItem("cdosageform_148", null);
			}
		}

	}

//	@SuppressWarnings("rawtypes")
//	private void doAfterHeadVariety() {
//		Vector selV = getheadRefModel("cvariety_148").getSelectedData();
//		if (selV != null && selV.size() > 0) {
//			Vector data = (Vector) selV.get(0);
//			if ("N".equals(data.get(4))) {
//				getBillCardPanel().setHeadItem("cvariety_148", null);
//			}
//		}
//	}

	private void doAfterHeadMedicineEdit() {
		Object ismed = getBillCardPanel().getHeadItem("bmedicine_148").getValueObject();
		boolean bmed = ismed == null ? false : new UFBoolean(ismed.toString()).booleanValue();
		//add by hedonga 2013-7-30 将医药物料模板里面的必填项给去掉了，当物料模板里面的医药物料时选中的状态时，要将医药物料模板里面的必填项填充上，反之亦然。
		getBillCardPanel().getHeadItem("imed_148").setNull(bmed);
		getBillCardPanel().getHeadItem("cdosageform_148").setNull(bmed);
//		getBillCardPanel().getHeadItem("cefficacy_148").setNull(bmed);
//		getBillCardPanel().getHeadItem("cvariety_148").setNull(bmed);
		// end add by hedonga 2013-7-30
		if (bmed) {
			getBillCardPanel().getHeadTabbedPane().setEnabledAt(1, true);
		} else {
			getBillCardPanel().getHeadTabbedPane().setEnabledAt(1, false);
		}
	}

	private void doAfterSuperVision(BillEditEvent e) {
		boolean bsupervision = e.getValue() == null ? false : new UFBoolean(e.getValue().toString()).booleanValue();
		if (bsupervision) {
			getBillCardPanel().getHeadItem("vsupervisioncode_148").setEdit(true);
		} else {
			getBillCardPanel().getHeadItem("vsupervisioncode_148").setEdit(false);
			getBillCardPanel().setHeadItem("vsupervisioncode_148", null);
		}
	}

	public void afterSetValue() {
		this.resetApartMeasdoc();
		this.resetMaterialmgt();
		if (this.isCreateVersionData()) {
			this.initCodeByBillCodeRule();
		}
		doAfterBvalidmanageEditor();
	}

	@Override
	public boolean beforeEdit(BillEditEvent e) {
		if (e.getKey().equals(MaterialConvertVO.PK_APARTMEASDOC)) {
			// 结存时拆解单位才可编辑
			return (Boolean) this.getBillCardPanel().getBodyValueAt(e.getRow(), MaterialConvertVO.ISSTOREBALANCE);
		}
		if (e.getKey().equals(MaterialConvertVO.MEASRATE)) {
			String main_measdocID = (String) this.getBillCardPanel().getHeadItem(MaterialVO.PK_MEASDOC)
					.getValueObject();
			String pk_measdoc = (String) this.getBillCardPanel().getBodyValueAt(e.getRow(),
					MaterialConvertVO.PK_MEASDOC + IBillItem.ID_SUFFIX);

			if (StringUtils.equals(main_measdocID, pk_measdoc)) {
				return false;
			}
		}
		if(e.getKey().equals("csubclass_148")){
//			getheadRefModel("csubclass_148").setPk_org(this.getModel().getContext().getPk_org());
			getheadRefModel("csubclass_148").setPk_org((String)this.getBillCardPanel().getHeadItem("pk_org").getValueObject());
		}
		if(e.getKey().equals("cbigclass_148")){
//			getheadRefModel("cbigclass_148").setPk_org(this.getModel().getContext().getPk_org());
			getheadRefModel("cbigclass_148").setPk_org((String)this.getBillCardPanel().getHeadItem("pk_org").getValueObject());
		}
		if(e.getKey().equals("cdosageform_148")){
//			getheadRefModel("cdosageform_148").setPk_org(this.getModel().getContext().getPk_org());
			getheadRefModel("cdosageform_148").setPk_org((String)this.getBillCardPanel().getHeadItem("pk_org").getValueObject());
		}
//		if(e.getKey().equals("cvariety_148")){
////			getheadRefModel("cvariety_148").setPk_org(this.getModel().getContext().getPk_org());
//			getheadRefModel("cvariety_148").setPk_org((String)this.getBillCardPanel().getHeadItem("pk_org").getValueObject());
//		}
//		if (e.getKey().equals("bmedicine_148")) {
//			Object pk_material = getBillCardPanel().getHeadItem("pk_material").getValueObject();
//			try {
//				Object retObj = ClientTool.getSingleColumnValue("gsp_syyp_148", "pk_material", "fstatusflag",
//						(String) pk_material);
//				if (retObj != null || !"".equals(retObj)) {
//					getBillCardPanel().getHeadItem("bmedicine_148").setEdit(false);
//				} else {
//					getBillCardPanel().getHeadItem("bmedicine_148").setEdit(true);
//				}
//			} catch (BusinessException e1) {
//			}
//		}
		return true;
	}

	@Override
	public void bodyRowChange(BillEditEvent e) {
		// 如果当前“适用零售”已经勾上，则将所有的固定换算都勾上，确保数据的正确性
	    if (this.isRetailSelected()) {
	      int rowCount =
	          this.getBillCardPanel().getBillModel(IMaterialConst.MATERIAL_CONVERT).getRowCount();
	      for (int i = 0; i < rowCount; i++) {
	        this.getBillCardPanel().setBodyValueAt(UFBoolean.TRUE, i, MaterialConvertVO.FIXEDFLAG);
	      }
	    }
	}

	@Override
	public boolean canBeHidden() {
		// 切换页签时，是否要判断界面状态
		if (this.closingListener != null) {
			return this.closingListener.canBeClosed();
		}
		return this.tabbedPaneAwareComponent.canBeHidden();
	}

	/**
	 * 获得新增时根据编码规则自动生成的编码
	 * 
	 * @return
	 */
	public String getBillcode() {
		return this.billcode;
	}

	public MarAsstPanel getMarAsstPanel() {
		return this.marAsstPanel;
	}

	public MaterialShareInfoEditor getShareInfoEditor() {
		return this.shareInfoEditor;
	}

	@Override
	public void handleEvent(AppEvent event) {
//		if (AppEventConst.UISTATE_CHANGED == event.getType()) {
//			if (UIState.ADD == this.getModel().getUiState() || UIState.EDIT == this.getModel().getUiState()) {
//				this.showMeUp();
//			}
//			this.billcode = null;//?????
//		} else if (AppEventConst.SHOW_EDITOR == event.getType()) {
//			this.showMeUp();
//		}
//		super.handleEvent(event);
		
		if (AppEventConst.UISTATE_CHANGED == event.getType()) {
		      if (UIState.ADD == this.getModel().getUiState()
		          || UIState.EDIT == this.getModel().getUiState()) {
		        this.showMaterialImageForAddOrEdit();
		        this.showMeUp();
		      }
		    } else if (AppEventConst.SHOW_EDITOR == event.getType()
		        || (isComponentVisible() && AppEventConst.SELECTION_CHANGED.equals(event.getType()))) {
		      // 当从卡片切换到列表时或者在卡片界面当前选择的数据发生变化时，设置图片的值
		      this.showMarImageAsShowCardEditor();
		      this.showMeUp();
		    }
		    super.handleEvent(event);
	}

	@Override
	public void initUI() {
		super.initUI();
		// start.用于物料图片控件，替换原来的ImageBillItemEditor
	    BillItem pictureitem = this.getBillCardPanel().getHeadItem(MaterialVO.PICTURE);
	    ImageBillItemEditor_m pictureItemEditor = new ImageBillItemEditor_m(pictureitem, this);
	    MaterialVO vo = (MaterialVO) getModel().getSelectedData();
	    if (vo != null) {
	      pictureItemEditor.initEditor(vo.getPrimaryKey());
	    }
	    pictureitem.setItemEditor(pictureItemEditor);
	    // end
		this.getBillCardPanel().addEditListener(this);
		this.getBillCardPanel().addBodyEditListener2(IMaterialConst.MATERIAL_CONVERT, this);
		this.getBillCardPanel().setBodyAutoAddLine(IMaterialConst.MATERIAL_CONVERT, true);
		this.getBillCardPanel().setBodyAutoAddLine(IMaterialConst.MATERIAL_TAXTYPE, true);
		try {
			this.initStandardMeasureName();
		} catch (BusinessException e) {
			throw new BusinessExceptionAdapter(e);
		}
		this.initRefModel();
		this.initConvertMeasrate();
		this.initMarAsstPanel();
	}

	private void initMedTabCodeVisible() {
		BillCardPanel cardPanel = this.getBillCardPanel();
		if(this.getModel().getUiState() == UIState.EDIT) {
			cardPanel.getHeadItem("bmedicine_148").setEdit(true);
		}
//		wangsyf added 65  start
		if(this.getModel().getUiState() == UIState.ADD){
			MaterialBaseInfoModel model = (MaterialBaseInfoModel) getModel();
			if (!model.isImportable()) {
				cardPanel.getHeadItem("bmedicine_148").setValue(true);
			}else{
				cardPanel.getHeadItem("bmedicine_148").setValue(false);
			}
			doAfterHeadMedicineEdit();
		}
//		end
		Object ismed = cardPanel.getHeadItem("bmedicine_148").getValueObject();
		
		boolean bmed = ismed == null ? false : new UFBoolean(ismed.toString()).booleanValue();
		this.getBillCardPanel().getHeadTabbedPane().setEnabledAt(1, bmed);		
	}

	@Override
	public boolean isComponentVisible() {
		return this.tabbedPaneAwareComponent.isComponentVisible();
	}

	public void resetPk_org(String pk_org) {
		this.rollbackPreBillCode();
		this.getBillCardPanel().getHeadItem(MaterialVO.PK_ORG).setValue(pk_org);
		this.resetRefModel();
		this.afterSetValue();
	}

	public void resetRefModel() {
		String pk_org = (String) this.getBillCardPanel().getHeadItem(MaterialVO.PK_ORG).getValueObject();
		UIRefPane refPane = (UIRefPane) this.getBillCardPanel().getHeadItem(MaterialVO.PK_MARBASCLASS).getComponent();
		refPane.getRefModel().setPk_org(pk_org);
		((IUserdefitemPreparator) this.getUserdefitemPreparator()).setPkorgForRefItems(pk_org);
	}

	/**
	 * 回滚预取的编码
	 */
	public void rollbackPreBillCode() {
		BillItem codeItem = this.getBillCardPanel().getHeadItem(MaterialVO.CODE);
		if (MaterialBaseInfoEditor.AUTO_CODE_BACK.equals(codeItem.getValueObject())) {
			codeItem.setValue(null);
		}
		if (StringUtils.isBlank(this.billcode)) {
			return;
		}
		try {
			// 不是前编码不存在退号的情况
			if (getBillCodeContext() == null
					|| !getBillCodeContext().isPrecode()) {
				return;
			}
			String pk_group = (String) this.getBillCardPanel().getHeadItem(MaterialVO.PK_GROUP).getValueObject();
			String pk_org = (String) this.getBillCardPanel().getHeadItem(MaterialVO.PK_ORG).getValueObject();
			this.getBillcodeManage().rollbackPreBillCode(IMaterialConst.BILLCODE_MATERIAL, pk_group, pk_org,
					this.billcode);
			this.billcode = null;
		} catch (BusinessException e1) {
			this.getExceptionHandler().handlerExeption(e1);
		}
	}

	@Override
	public void setAutoShowUpEventListener(IAutoShowUpEventListener l) {
		this.autoShowUpComponent.setAutoShowUpEventListener(l);
	}

	public void setClosingListener(IFunNodeClosingListener closingListener) {
		this.closingListener = closingListener;
	}

	@Override
	public void setComponentVisible(boolean visible) {
		this.tabbedPaneAwareComponent.setComponentVisible(visible);
		if (visible && UIState.ADD != this.getModel().getUiState()) {
			if (this.currentData != null && this.currentData.equals(this.getModel().getSelectedData())) {
				return;
			}
			this.synchronizeDataFromModel();
			this.currentData = this.getModel().getSelectedData();
		}
	}

	public void setExceptionHandler(IExceptionHandler exceptionHandler) {
		this.exceptionHandler = exceptionHandler;
	}

	public void setMarAsstPanel(MarAsstPanel marAsstPanel) {
		this.marAsstPanel = marAsstPanel;
	}

	public void setShareInfoEditor(MaterialShareInfoEditor shareInfoEditor) {
		this.shareInfoEditor = shareInfoEditor;
		this.shareInfoEditor.getBillCardPanel().addEditListener(new BillEditListener() {

			@Override
			public void afterEdit(BillEditEvent e) {
				Object value = MaterialBaseInfoEditor.this.getShareInfoEditor().getBillCardPanel()
						.getHeadItem(e.getKey()).getValueObject();
				if (e.getKey().equals(MaterialVO.PK_ORG)) {
					MaterialBaseInfoEditor.this.resetPk_org((String) value);
					MaterialBaseInfoEditor.this.getModel().getContext().setPk_org((String) value);
//					changeBillDefaultOrg(getBillCardPanel().getHeadItem("cvariety_148"),(String)value);
				} else {
					MaterialBaseInfoEditor.this.getBillCardPanel().getHeadItem(e.getKey()).setValue(value);
					if (e.getKey().equals(MaterialVO.NAME)) {
						getBillCardPanel().getHeadItem("vcommonname_148").setValue(value);
						// add by liuygc,为助记码自动添加自动生成 ---start 2013/07/01
//						String mneCode = MnemonicCode.getMneCode(value.toString());
				      	  //Object mneCodeOld = getBillCardPanel().getHeadItem("materialmnecode").getValueObject();
				      	 String pk_group =  MaterialBaseInfoEditor.this.getModel().getContext().getPk_group();
                   	  UFBoolean isOpen = UFBoolean.FALSE;
                 		try {
							 isOpen = SysInitParamUtil.getParaBoolean(pk_group,SysInitParamUtil.MEDGSP016PARAM);
						} catch (BusinessException e1) {
							ExceptionUtils.wrappException(e1);
						}
                   	  if(value != null && (isOpen != null && isOpen.booleanValue())){
				      		  String mneCode = Pinyin4jUtil
				      				  .converterToFirstSpell(value.toString(),getShareInfoEditor().getModel().getContext()
				      						  .getEntranceUI());
				      		  getBillCardPanel().setHeadItem("materialmnecode", mneCode);
				      		  // add by liuygc,为助记码自动添加自动生成 ---end 2013/07/01
				      	}
                   	  else{
                   		getBillCardPanel().setHeadItem("materialmnecode", "");
                   	  }
					}
				}
			}

			@Override
			public void bodyRowChange(BillEditEvent e) {
			}
		});

	}

	@Override
	public void showMeUp() {
		this.autoShowUpComponent.showMeUp();
		//医药页签可见性huangbin
		if(GroupCheckUtils.isMedGroup(this.getModel().getContext().getPk_group())) {
			this.initMedTabCodeVisible();
		}
	}

	@Override
	protected void beforeGetValue() {
		if (this.getShareInfoEditor() != null) {
			this.getShareInfoEditor().getBillCardPanel().stopEditing();
		}
		super.beforeGetValue();
		this.setMarAsstframe();
		try {
			this.getBillCardPanel().dataNotNullValidate();
		} catch (ValidationException e) {
			throw new BusinessExceptionAdapter(e);
		}
	}

	@Override
	protected int[] getDelLineIndex(String bodyTabcode) {
		List<Integer> unNecessaryRows = new ArrayList<Integer>();
		int count = this.getBillCardPanel().getBillModel(bodyTabcode).getRowCount();
		for (int row = 0; row < count; row++) {
			if (this.isBodyRowNull(bodyTabcode, row)) {
				unNecessaryRows.add(row);
			}
		}
		return this.changeToIntArray(unNecessaryRows);
	}

	protected IExceptionHandler getExceptionHandler() {
		if (this.exceptionHandler == null) {
			this.exceptionHandler = new DefaultExceptionHanler(this);
		}
		return this.exceptionHandler;
	}

	protected void initCodeByBillCodeRule() {
		try {
			this.setCodeFieldValueByCodeRule();
			this.setCodeFieldEnabledByCodeRule();
		} catch (BusinessException e) {
			this.getExceptionHandler().handlerExeption(e);
		}
	}

	protected boolean isAddWithData() {
		if (this.getModel() instanceof MaterialBaseInfoModel) {
			MaterialBaseInfoModel model = (MaterialBaseInfoModel) this.getModel();
			return model.isImportable() || model.getCopyPk() != null || model.getCreateVersionPk() != null;
		}
		return false;
	}

	@Override
	protected void onAdd() {
		this.setMatchPkWithWherePart(true);
		super.onAdd();	
		doAdd();	
		doAfterIMedtypeEditor(null);
	}
	//add by med zhaolid 2013-7-31
	private void doAdd() {
		getBillCardPanel().getHeadItem("blotno_148").setEdit(false);
		getBillCardPanel().getHeadItem("blotnoicstatus_148").setEdit(true);
		getBillCardPanel().getHeadItem("iexpiryunit_148").setEdit(Boolean.TRUE);//效期单位
		getBillCardPanel().getHeadItem("iexpiryunit_148").setNull(Boolean.TRUE);
		getBillCardPanel().getHeadItem("iexpirydate_148").setEdit(Boolean.TRUE);//有效期
		getBillCardPanel().getHeadItem("iexpirydate_148").setNull(Boolean.TRUE);
		getBillCardPanel().getHeadItem("ivaliduntilunit_148").setEdit(Boolean.TRUE);//有效期至单位
		getBillCardPanel().getHeadItem("ivaliduntilunit_148").setNull(Boolean.TRUE);
		getBillCardPanel().getHeadItem("icaredays_148").setEdit(Boolean.TRUE);//近效期预警天数
		getBillCardPanel().getHeadItem("icaredays_148").setNull(Boolean.TRUE);
		String pk = null;
		try {
			pk = MaterialPropertyUtil.getPkMarasstframe(MARFRAMECODE2, MARANTCODE1);
//			pk = MMStringUtil.objectToString(HYPubBO_Client.findColValue("bd_marasstframe inner join bd_marassistant on bd_marasstframe.pk_marasstframe = bd_marassistant.pk_marasstframe", "bd_marasstframe.pk_marasstframe",
//					"bd_marasstframe.code = 'MED002' and bd_marassistant.code = '11' and bd_marassistant.pk_org ='"+InvocationInfoProxy.getInstance().getGroupId()+"'"));
		} catch (BusinessException e) {
			this.getExceptionHandler().handlerExeption(e);
		}
//		getBillCardPanel().setHeadItem("pk_marasstframe", pk);
//		getBillCardPanel().setHeadItem("pk_marbasclass.pk_marasstframe", pk);
//		if(this.getModel().getContext().getNodeCode().equals("10140MAG")||
//				this.getModel().getContext().getNodeCode().equals("10140MAO")){
//			getMarAsstPanel().getBillCardPanel().setHeadItem("marasstframe", pk);
//			getMarAsstPanel().setPk_marasstframe(pk);
//		}
//		try {
//			doAfterBlotNoSts(null);
//		} catch (BusinessException e) {
//			nc.pub.templet.converter.util.helper.ExceptionUtils.wrapException(e);
//		}
	}

	@Override
	protected void onEdit() {
		super.onEdit();
		getBillCardPanel().getHeadItem("blotno_148").setEdit(false);
		getBillCardPanel().getHeadItem("blotnoicstatus_148").setEdit(true);
		doAfterIMedtypeEditor(null);
		doAfterIMedEditor(null);
		doAfterBvalidmanageEditor();
		this.resetRefModel();
		this.resetMaterialmgt();
		this.resetApartMeasdoc();
		this.setCodeFieldEnabledByCodeRule();
		this.resetFeatureclassValue();
		try {
			setCanEditLotNo(); // 勾选医药辅助属性结构的物料在系统中有功能引用后，不允许修改批号管理属性
			setCanEditLotNoicStatus();// 勾选医药辅助属性结构的物料在系统中有功能引用后，不允许修改批号管理属性
		} catch (BusinessException e) {
			ExceptionUtils.wrappException(e);
			Logger.error(e);
		}
	}

	private void setCanEditLotNo() throws BusinessException {
		// boolean isEdit =
		// this.getMarAsstPanel().getBillCardPanel().getHeadItem("marasstframe").isEdit();
		Object isSel = this.getBillCardPanel().getHeadItem("blotno_148").getValueObject();

		if (MMBooleanUtil.objToBooleanValue(isSel)) {
			String pk_material = (String) this.getBillCardPanel().getHeadItem(MaterialVO.PK_MATERIAL).getValueObject();
//			2017-03-01 wangsyf 影响效率  暂时去掉  
			/*
			boolean isrefenced = (new ReferencedValidationUtil(MaterialVO.getDefaultTableName(), pk_material))
					.isReferenced();
			if (isrefenced) {
				this.getBillCardPanel().getHeadItem("blotno_148").setEdit(false);
				// this.getBillCardPanel().getHeadItem("blotnoicstatus_148").setEdit(false);
			}
			*/
		}
	}
	
	private void setCanEditLotNoicStatus() throws BusinessException {
		Object isSel = this.getBillCardPanel().getHeadItem("blotnoicstatus_148").getValueObject();
		if (MMBooleanUtil.objToBooleanValue(isSel)) {
			String pk_material = (String) this.getBillCardPanel().getHeadItem(MaterialVO.PK_MATERIAL).getValueObject();
//			2017-03-01 wangsyf 影响效率  暂时去掉  
			/*
			boolean isrefenced = (new ReferencedValidationUtil(MaterialVO.getDefaultTableName(), pk_material))
					.isReferenced();
			if (isrefenced) {
				// this.getBillCardPanel().getHeadItem("blotno_148").setEdit(false);
				this.getBillCardPanel().getHeadItem("blotnoicstatus_148").setEdit(false);
			}
			*/
		}
	}
	//end zhaoli
	@Override
	protected void onNotEdit() {
		this.setMatchPkWithWherePart(false);
		super.onNotEdit();
	}

	protected void resetApartMeasdoc() {
		Set<String> measdocID_set = this.getAllApartMeasDocID();
		AbstractRefModel refModel = ((UIRefPane) this.getBillCardPanel()
				.getBodyItem(IMaterialConst.MATERIAL_CONVERT, MaterialConvertVO.PK_APARTMEASDOC).getComponent())
				.getRefModel();
		refModel.setFilterPks(measdocID_set.toArray(new String[0]), IFilterStrategy.INSECTION);
		this.clearWrongApartMeasdoc(measdocID_set);
	}

	protected void setCodeFieldEnabledByCodeRule() {
		try {
			BillCodeContext billCodeContext = this.getBillCodeContext();
			if (billCodeContext != null) {
				this.getBillCardPanel().getHeadItem(MaterialVO.CODE).setEnabled(billCodeContext.isEditable());
			}
		} catch (BusinessException e) {
			this.getExceptionHandler().handlerExeption(e);
		}
		this.setSharePanelCode();
	}

	@Override
	protected void setDefaultValue() {
		this.getBillCardPanel().setHeadItem(MaterialVO.PK_GROUP, this.getModel().getContext().getPk_group());
		this.getBillCardPanel().setHeadItem(MaterialVO.PK_ORG, this.getModel().getContext().getPk_org());
		if (!this.isAddWithData()) {
			this.resetRefModel();
			this.afterSetValue();
		}
	}

	protected void setMarAsstframe() {
		if (this.getMarAsstPanel() != null) {
			this.getMarAsstPanel().getBillCardPanel().stopEditing();
			this.getBillCardPanel().getHeadItem(MaterialVO.PK_MARASSTFRAME)
					.setValue(this.getMarAsstPanel().getPk_marasstframe());
		}
	}

	/**
	 * 重写数据同步方法，在界面不显示时，不进行数据同步，减少公式的执行次数 注：在界面被激活时，要主动同步数据
	 */
	@Override
	protected void synchronizeDataFromModel() {
		if (this.isComponentVisible()) {
			super.synchronizeDataFromModel();
		}
	}

	private void autoAddLineByMainMeasID() {
		this.getBillCardPanel().getBillModel(IMaterialConst.MATERIAL_CONVERT).addLine();
		// 计量单位同主计量
		String main_measdocID = (String) this.getBillCardPanel().getHeadItem(MaterialVO.PK_MEASDOC).getValueObject();
		this.getBillCardPanel().setBodyValueAt(main_measdocID, 0, MaterialConvertVO.PK_MEASDOC + IBillItem.ID_SUFFIX);
		this.getBillCardPanel().getBillModel(IMaterialConst.MATERIAL_CONVERT)
				.loadLoadRelationItemValue(0, MaterialConvertVO.PK_MEASDOC);
		// 与主计量单位换算率为1/1
		this.getBillCardPanel().setBodyValueAt("1/1", 0, MaterialConvertVO.MEASRATE);
		// 自动标记所有辅计量
		for (String assistunit : this.assistunits) {
			this.getBillCardPanel().setBodyValueAt(UFBoolean.TRUE, 0, assistunit);
		}
		// 默认为固定换算
		this.getBillCardPanel().setBodyValueAt(UFBoolean.TRUE, 0, MaterialConvertVO.FIXEDFLAG);
	}

	private int[] changeToIntArray(List<Integer> list) {
		int[] rows = null;
		if (list.size() > 0) {
			rows = new int[list.size()];
			for (int i = 0; i < rows.length; i++) {
				rows[i] = list.get(i);
			}
		}
		return rows;
	}

	private void clearApartMeasdoc(BillEditEvent e) {
//		Boolean isstorebalance = (Boolean) this.getBillCardPanel().getBodyValueAt(e.getRow(),
//				MaterialConvertVO.ISSTOREBALANCE);
//		if (!isstorebalance) {
//			this.getBillCardPanel().setBodyValueAt(null, e.getRow(),
//					MaterialConvertVO.PK_APARTMEASDOC + IBillItem.ID_SUFFIX);
//		}
		
//		update in 65
		Boolean isstorebalance = (Boolean) this.getBillCardPanel()
				.getBillModel(IMaterialConst.MATERIAL_CONVERT)
				.getValueAt(e.getRow(), MaterialConvertVO.ISSTOREBALANCE);
		if (!isstorebalance) {
			this.getBillCardPanel()
					.getBillModel(IMaterialConst.MATERIAL_CONVERT)
					.setValueAt(
							null,
							e.getRow(),
							MaterialConvertVO.PK_APARTMEASDOC
									+ IBillItem.ID_SUFFIX);
		}
	}

	private void clearWrongApartMeasdoc(Set<String> measdocID_set) {
		int rowCount = this.getBillCardPanel().getBillModel(IMaterialConst.MATERIAL_CONVERT).getRowCount();
		for (int row = 0; row < rowCount; row++) {
			this.clearWrongApartMeasdocByRow(measdocID_set, row);
		}
	}

	private void clearWrongApartMeasdocByRow(Set<String> measdocID_set, int row) {
		String pk_apartmeasdoc = (String) this.getBillCardPanel().getBodyValueAt(row,
				MaterialConvertVO.PK_APARTMEASDOC + IBillItem.ID_SUFFIX);
		if (!measdocID_set.contains(pk_apartmeasdoc)) {
			this.getBillCardPanel().setBodyValueAt(null, row, MaterialConvertVO.PK_APARTMEASDOC + IBillItem.ID_SUFFIX);
		}
	}

	private boolean convertExists() {
		return this.getBillCardPanel().getBillModel(IMaterialConst.MATERIAL_CONVERT).getRowCount() > 0;
	}

	private void doAfterAssisUnitEdit(BillEditEvent e) {
//		Boolean isSelected = (Boolean) this.getBillCardPanel().getBodyValueAt(e.getRow(), e.getKey());
//		update in 65
		Boolean isSelected = (Boolean) this.getBillCardPanel()
				.getBillModel(IMaterialConst.MATERIAL_CONVERT)
				.getValueAt(e.getRow(), e.getKey());
		if (isSelected) {
			int rowCount = this.getBillCardPanel().getBillModel(IMaterialConst.MATERIAL_CONVERT).getRowCount();
			for (int row = 0; row < rowCount; row++) {
				if (row != e.getRow()) {
					BillModel billmodel = this.getBillCardPanel().getBillModel(IMaterialConst.MATERIAL_CONVERT);
					billmodel.setValueAt(UFBoolean.FALSE, row, e.getKey());
					if (BillModel.ADD != billmodel.getRowState(row)) {
						billmodel.setRowState(row, BillModel.MODIFICATION);
					}
				}
			}
		}
	}

	private void doAfterConvertMeasdocEdit(BillEditEvent e) throws BusinessException {
		this.setConvertMeasRateByRow(e.getRow());
		this.resetApartMeasdoc();
	}

	private void doAfterHeadMeasdocEdit() throws BusinessException {
		if (this.convertExists()) {
			this.setAllConvertMeasRate();
		} else if (this.getModel().getUiState() == UIState.ADD) {
			this.autoAddLineByMainMeasID();
		}
		this.resetApartMeasdoc();
	}

	private void doAfterStorebalanceEdit(BillEditEvent e) {
		this.clearApartMeasdoc(e);
		this.resetApartMeasdoc();
	}

	private String[] filterNull(String[] pks) {
		List<String> pkList = new ArrayList<String>();
		if (!ArrayUtils.isEmpty(pks)) {
			for (String pk : pks) {
				pkList.add(pk);
			}
		}
		return pkList.toArray(new String[0]);
	}

	private Set<String> getAllApartMeasDocID() {
		Set<String> measdocID_set = new HashSet<String>();
		String pk_measdoc = (String) this.getBillCardPanel().getHeadItem(MaterialVO.PK_MEASDOC).getValueObject();
		if (StringUtils.isNotBlank(pk_measdoc)) {
			measdocID_set.add(pk_measdoc);
		}
		int rowCount = this.getBillCardPanel().getBillModel(IMaterialConst.MATERIAL_CONVERT).getRowCount();
		for (int row = 0; row < rowCount; row++) {
			Boolean isStoreBalance = (Boolean) this.getBillCardPanel().getBodyValueAt(row,
					MaterialConvertVO.ISSTOREBALANCE);
			String pk_asstmeasdoc = (String) this.getBillCardPanel().getBodyValueAt(row,
					MaterialConvertVO.PK_MEASDOC + IBillItem.ID_SUFFIX);
			if (isStoreBalance != null && isStoreBalance.booleanValue() && StringUtils.isNotBlank(pk_asstmeasdoc)) {
				measdocID_set.add(pk_asstmeasdoc);
			}
		}
		return measdocID_set;
	}

	private List<BillItem> getAllBillItems() {
		List<BillItem> items = new ArrayList<BillItem>();
		BillData billData = this.getBillCardPanel().getBillData();
		items.addAll(Arrays.asList(billData.getHeadTailItems()));
		items.addAll(Arrays.asList(billData.getBodyItems()));
		return items;
	}

	private BillCodeContext getBillCodeContext() throws BusinessException {
		String pk_group = (String) this.getBillCardPanel().getHeadItem(MaterialVO.PK_GROUP).getValueObject();
		String pk_org = (String) this.getBillCardPanel().getHeadItem(MaterialVO.PK_ORG).getValueObject();
		if (StringUtils.isBlank(pk_org) || StringUtils.isBlank(pk_group)) {
			return null;
		}
		if (!this.orgID_billCodeContext_map.keySet().contains(pk_group + pk_org)) {
			BillCodeContext billCodeContext = this.getBillcodeManage().getBillCodeContext(
					IMaterialConst.BILLCODE_MATERIAL, pk_group, pk_org);
			this.orgID_billCodeContext_map.put(pk_group + pk_org, billCodeContext);
		}
		return this.orgID_billCodeContext_map.get(pk_group + pk_org);
	}

	private IBillcodeManage getBillcodeManage() {
		if (this.billcodeManage == null) {
			this.billcodeManage = NCLocator.getInstance().lookup(IBillcodeManage.class);
		}
		return this.billcodeManage;
	}

	private AbstractRefModel getBodyRefModel(String tabCode, String itemKey) {
		UIRefPane refPane = (UIRefPane) this.getBillCardPanel().getBodyItem(tabCode, itemKey).getComponent();
		return refPane.getRefModel();
	}

	/**
	 * 根据辅计量单位与主计量单位获取默认换算率
	 * 
	 * @param pk_measdoc
	 * @param main_measdocID
	 * @return
	 * @throws BusinessException
	 */
	private String getDefaultMeasrate(String pk_measdoc, String main_measdocID) throws BusinessException {
		if (StringUtils.isBlank(pk_measdoc) || StringUtils.isBlank(main_measdocID)) {
			return null;
		}
		if (pk_measdoc.equalsIgnoreCase(main_measdocID)) {
			return "1/1";
		}
		MeasdocVO main = this.getMeasdocByPk(main_measdocID);
		MeasdocVO asst = this.getMeasdocByPk(pk_measdoc);
		if (this.isSameOppdimen(main, asst) && asst.getScalefactor() != null && main.getScalefactor() != null) {
			return asst.getScalefactor() + "/" + main.getScalefactor();
		}
		return null;
	}

	private AbstractRefModel getheadRefModel(String itemKey) {
		BillItem headItem = this.getBillCardPanel().getHeadItem(itemKey);
		if (itemKey.endsWith("_148") && headItem == null) {
			String err = "物料初始化时，未找到医药行业字段。请确认是否分配了医药行业单据模板";
			Logger.error(err);
			ExceptionUtils.wrappException(new IllegalStateException(err));
		}
		UIRefPane refPane = (UIRefPane) headItem.getComponent();
		return refPane.getRefModel();
	}

	private MeasdocVO getMeasdocByPk(String main_measdocID) throws BusinessException {
		MeasdocVO main = this.measdoc_map.get(main_measdocID);
		if (main == null) {
			this.measdoc_map.clear();
			this.initMeasdocMap();
			main = this.measdoc_map.get(main_measdocID);
		}
		return main;
	}

	private Map<String, String> getMeasdocsByPks(String[] pks) throws BusinessException {
		MeasdocVO[] vos = (MeasdocVO[]) BDCacheQueryUtil.queryVOsByIDs(MeasdocVO.class, MeasdocVO.PK_MEASDOC,
				this.filterNull(pks), new String[] { MeasdocVO.NAME, MeasdocVO.NAME2, MeasdocVO.NAME3, MeasdocVO.NAME4,
						MeasdocVO.NAME5, MeasdocVO.NAME6 });
		Map<String, String> pk_data_map = new HashMap<String, String>();
		if (vos != null) {
			for (MeasdocVO vo : vos) {
				if (vo != null) {
					String name = NCObject.newInstance(vo).getAttributeValue(MeasdocVO.NAME).toString();
					pk_data_map.put(vo.getPrimaryKey(), name);
				}
			}
		}
		return pk_data_map;
	}

	private String getPreCodeByCodeRule() throws BusinessException {
		String pk_group = (String) this.getBillCardPanel().getHeadItem(MaterialVO.PK_GROUP).getValueObject();
		String pk_org = (String) this.getBillCardPanel().getHeadItem(MaterialVO.PK_ORG).getValueObject();
		return this.getBillcodeManage().getPreBillCode_RequiresNew(IMaterialConst.BILLCODE_MATERIAL, pk_group, pk_org);
	}

	private Map<String, String> getSysInitCodeToFieldMap() {
		Map<String, String> sysCode_field_map = new HashMap<String, String>();
		sysCode_field_map.put(IBDSysInitCodeConst.MATERIAL_STORAGE_UNIT, MaterialVO.STOREUNITNUM);
		sysCode_field_map.put(IBDSysInitCodeConst.MATERIAL_VOLUME_UNIT, MaterialVO.UNITVOLUME);
		sysCode_field_map.put(IBDSysInitCodeConst.MATERIAL_WEIGHT_UNIT, MaterialVO.UNITWEIGHT);
		return sysCode_field_map;
	}

	private void initConvertMeasrate() {
		MetaDataEditPropertyAdpter metaDataProperty = new MetaDataEditPropertyAdpter(this.getBillCardPanel()
				.getBodyItem(MaterialConvertVO.MEASRATE).getMetaDataProperty());
		metaDataProperty.setDatatype(IBillItem.FRACTION);
		this.getBillCardPanel().getBodyItem(MaterialConvertVO.MEASRATE).setMetaDataProperty(metaDataProperty);
		// 控制换算率分数的小数位数4位
		UIFractionTextField fraction = (UIFractionTextField) this.getBillCardPanel()
				.getBodyItem(MaterialConvertVO.MEASRATE).getComponent();
		fraction.setNumberPoint(8);
	}

	private void initMarAsstPanel() {
		if (this.getMarAsstPanel() != null) {
			BillTabVO tabVO = new BillTabVO();
			tabVO.setTabcode("materialassistant");
			tabVO.setTabname(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("10140mag", "010140mag0110")/*
																												 * @
																												 * res
																												 * "辅助属性"
																												 */);
			UIScrollPane scrollPane = new UIScrollPane();
			this.getBillCardPanel().getBodyTabbedPane().addScrollPane(tabVO, scrollPane);
			scrollPane.getViewport().setView(this.getMarAsstPanel().getBillCardPanel().getHeaderPanel());
		}
	}

	@SuppressWarnings("unchecked")
	private void initMeasdocMap() throws BusinessException {
		IUAPQueryBS queryService = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		Collection<MeasdocVO> col = queryService.retrieveAll(MeasdocVO.class);
		if (col != null && col.size() > 0) {
			for (MeasdocVO measdocVO : col) {
				this.measdoc_map.put(measdocVO.getPrimaryKey(), measdocVO);
			}
		}
	}

	private void initRefModel() {
		// 初始化集团级参照，产品线、品牌
		String pk_group = this.getModel().getContext().getPk_group();
		this.getheadRefModel(MaterialVO.PK_PRODLINE).setPk_org(pk_group);
		this.getheadRefModel(MaterialVO.PK_BRAND).setPk_org(pk_group);
		// 初始化全局级参照，海关商品代码、税类、税类（表体）、国家（表体）
		this.getheadRefModel(MaterialVO.PK_MATTAXES).setPk_org(IOrgConst.GLOBEORG);
		this.getheadRefModel(MaterialVO.PK_GOODSCODE).setPk_org(IOrgConst.GLOBEORG);
		this.getBodyRefModel(IMaterialConst.MATERIAL_TAXTYPE, MaterialTaxTypeVO.PK_MATTAXES).setPk_org(
				IOrgConst.GLOBEORG);
		this.getBodyRefModel(IMaterialConst.MATERIAL_TAXTYPE, MaterialTaxTypeVO.PK_COUNTRYZONE).setPk_org(
				IOrgConst.GLOBEORG);

		// 设置物料基本分类只能选择叶节点
		UIRefPane refPane = (UIRefPane) this.getBillCardPanel().getHeadItem(MaterialVO.PK_MARBASCLASS).getComponent();
		refPane.setNotLeafSelectedEnabled(false);

		// med huangbin3 2013.04.27 start
		//getheadRefModel("csubclass_148").setPk_org(this.getModel().getContext().getPk_org());
		if (GroupCheckUtils.isMedGroup(this.getModel().getContext().getPk_group())) {
			getheadRefModel("csubclass_148").setWherePart("isnull(med_breedtpye_148.dr,0) = 0 and bisend = 'Y'");
			// getheadRefModel("cbigclass_148").setPk_org(this.getModel().getContext().getPk_org());
			getheadRefModel("cbigclass_148").addWherePart(" and isNull(pk_type_up,'~') = '~'");

			getheadRefModel("cdosageform_148").addWherePart(" and typeflag = 'medjitype'");
			getheadRefModel("cdosageform_148").setRefNodeName("剂型档案");
			UIRefPane cdosageform = (UIRefPane) this.getBillCardPanel().getHeadItem("cdosageform_148").getComponent();
			cdosageform.setNotLeafSelectedEnabled(false);
			// 剂型
			// getheadRefModel("cdosageform_148").setPk_org(this.getModel().getContext().getPk_org());

			// getheadRefModel("cvariety_148").setPk_org(this.getModel().getContext().getPk_org());
//			getheadRefModel("cvariety_148").setWherePart("isnull(med_invtype_148.dr,0) = 0 ");
//			UIRefPane pane = (UIRefPane) this.getBillCardPanel().getHeadItem("cvariety_148").getComponent();
//			pane.setNotLeafSelectedEnabled(false);
		}
		// end
	}

	private void initStandardMeasureName() throws BusinessException {
		Map<String, String> initCode_field_map = this.getSysInitCodeToFieldMap();
		Map<String, String> initCode_value_map = SysInitQuery.queryBatchParaValues(this.getModel().getContext()
				.getPk_group(), initCode_field_map.keySet().toArray(new String[0]));
		Map<String, String> pk_name_map = this.getMeasdocsByPks(initCode_value_map.values().toArray(new String[0]));
		for (String initcode : initCode_field_map.keySet()) {
			String measdocName = pk_name_map.get(initCode_value_map.get(initcode));
			if (StringUtils.isNotBlank(measdocName)) {
				BillItem item = this.getBillCardPanel().getHeadItem(initCode_field_map.get(initcode));
				item.setName(item.getName() + "(" + measdocName + ")");
			}
		}
		this.getBillCardPanel().setBillData(this.getBillCardPanel().getBillData());
	}

	private boolean isBodyRowNull(String bodyTabcode, int row) {
		if (IMaterialConst.MATERIAL_CONVERT.equals(bodyTabcode)) {
			String value = (String) this.getBillCardPanel().getBillModel(bodyTabcode)
					.getValueAt(row, MaterialConvertVO.PK_MEASDOC + IBillItem.ID_SUFFIX);
			return StringUtils.isBlank(value);
		} else if (IMaterialConst.MATERIAL_TAXTYPE.equals(bodyTabcode)) {
			String pk_countryzone = (String) this.getBillCardPanel().getBillModel(bodyTabcode)
					.getValueAt(row, MaterialTaxTypeVO.PK_COUNTRYZONE + IBillItem.ID_SUFFIX);
			String pk_mattaxes = (String) this.getBillCardPanel().getBillModel(bodyTabcode)
					.getValueAt(row, MaterialTaxTypeVO.PK_MATTAXES + IBillItem.ID_SUFFIX);
			return StringUtils.isBlank(pk_countryzone) && StringUtils.isBlank(pk_mattaxes);
		}
		return false;
	}

	private boolean isCreateVersionData() {
		if (this.getModel() instanceof MaterialBaseInfoModel) {
			MaterialBaseInfoModel model = (MaterialBaseInfoModel) this.getModel();
			if (StringUtils.isNotBlank(model.getCreateVersionPk())) {
				return false;
			}
		}
		return true;
	}

	private boolean isSameOppdimen(MeasdocVO main, MeasdocVO asst) {
		return StringUtils.equals(main.getOppdimen(), asst.getOppdimen());
	}

	private void resetMaterialmgt() {
		Boolean fee = (Boolean) this.getBillCardPanel().getHeadItem(MaterialVO.FEE).getValueObject();
		Boolean discountflag = (Boolean) this.getBillCardPanel().getHeadItem(MaterialVO.DISCOUNTFLAG).getValueObject();
		BillItem materialmgt = this.getBillCardPanel().getHeadItem(MaterialVO.MATERIALMGT);
//		if (fee || discountflag) {
//			materialmgt.setEnabled(false);
//			materialmgt.setValue(null);
//		} else {
//			materialmgt.setEnabled(true);
//		}
//		update in 65
		BillItem ishproitems = this.getBillCardPanel().getHeadItem(
				MaterialVO.ISHPROITEMS);
		if (fee || discountflag) {
			materialmgt.setEnabled(false);
			materialmgt.setValue(null);
			ishproitems.setEnabled(false);
			ishproitems.setValue(null);
		} else {
			materialmgt.setEnabled(true);
			ishproitems.setEnabled(true);
		}
	}

	private void setAllConvertMeasRate() throws BusinessException {
		int row = this.getBillCardPanel().getBillModel(IMaterialConst.MATERIAL_CONVERT).getRowCount();
		for (int i = 0; i < row; i++) {
			this.setConvertMeasRateByRow(i);
		}
	}

	private void setCodeFieldValueByCodeRule() throws BusinessException {
		if (this.getBillCodeContext() != null) {
			BillItem codeItem = this.getBillCardPanel().getHeadItem(MaterialVO.CODE);
			UFBoolean paraBoolean = SysInit.getParaBoolean("0001V110000000000FH0", "BD306"); /*yezhian 20260204 bug 00000033*/
			if (this.getBillCodeContext().isPrecode()) {
				this.billcode = this.getPreCodeByCodeRule();
				codeItem.setValue(this.billcode);
			}else 		
			if(paraBoolean.booleanValue()){
				codeItem.setValue(this.billcode);/*yezhian 20260204 bug 00000033*/
			}else{
				codeItem.setValue(MaterialBaseInfoEditor.AUTO_CODE_BACK); //零时挂载 编码，temp
			}
		}
	}

	private void setConvertMeasRateByRow(int row) throws BusinessException {
		String main_measdocID = (String) this.getBillCardPanel().getHeadItem(MaterialVO.PK_MEASDOC).getValueObject();
		String pk_measdoc = (String) this.getBillCardPanel().getBodyValueAt(row,
				MaterialConvertVO.PK_MEASDOC + IBillItem.ID_SUFFIX);
		String measrate = this.getDefaultMeasrate(pk_measdoc, main_measdocID);
		this.getBillCardPanel().setBodyValueAt(measrate, row, MaterialConvertVO.MEASRATE);
	}

	private void setMatchPkWithWherePart(boolean matchWithWherePart) {
		List<BillItem> items = this.getAllBillItems();
		for (BillItem billItem : items) {
			if (billItem.isEdit() && billItem.getDataType() == IBillItem.UFREF
					&& ((UIRefPane) billItem.getComponent()).getRefModel() != null) {
				((UIRefPane) billItem.getComponent()).getRefModel().setMatchPkWithWherePart(matchWithWherePart);
			}
		}
	}

	private void setSharePanelCode() {
		if (this.getShareInfoEditor() != null) {
			// 另起线程，因为要保证在共享界面新增操作结束后调用的
			LongUITask task = new LongUITask() {

				@Override
				public Object construct() throws Exception {
					return null;
				}

				@Override
				public void finished() {
					super.finished();
					try {
						String code = (String) MaterialBaseInfoEditor.this.getBillCardPanel()
								.getHeadItem(MaterialVO.CODE).getValueObject();
						MaterialBaseInfoEditor.this.getShareInfoEditor().initCodeByBillCodeRule(
								MaterialBaseInfoEditor.this.getBillCodeContext(), code);
					} catch (BusinessException e) {
						MaterialBaseInfoEditor.this.getExceptionHandler().handlerExeption(e);
					}
				}
			};
			// 启动查询线程
			this.getModel().getContext().getExecutor().execute(task);
		}
	}

	/**
	 * 同步物料分类的辅助属性结构
	 */
	private void synchronizeMarAsstFrameFromClass() {
		if (this.getMarAsstPanel() != null && this.getModel().getUiState() == UIState.ADD) {
			// 将物料分类的辅助属性结构设置为物料的辅助属性结构
			String pk_marasstframe = (String) this.getBillCardPanel()
					.getHeadItem(MaterialVO.PK_MARBASCLASS + "." + MarBasClassVO.PK_MARASSTFRAME).getValueObject();
			this.getBillCardPanel().getHeadItem(MaterialVO.PK_MARASSTFRAME).setValue(pk_marasstframe);
			this.getMarAsstPanel().setPk_marasstframe(pk_marasstframe);
		}
	}
	protected void changeBillDefaultOrg(BillItem billItem, String pkorg) {
			if (billItem == null)
				return;
			billItem.setValue(null);
			UIRefPane refPanel = (UIRefPane) billItem.getComponent();
			if (refPanel.getRefModel() != null) {
				refPanel.getRefModel().setPk_org(pkorg);
				refPanel.setWhereString(" isnull(med_invtype_148.dr,0) = 0 ");
			}
	}
	
	
//	65 added
	
	/**
	   * 
	   * <p>
	   * 说明：编辑“价格折扣”字段后，根据当前“适用零售”的状态 和编辑后“价格折扣”的状态，做相应的处理。
	   * 场景：“价格折扣”与“适用零售”之间存在约束关系：不能同时勾选“价格折扣”与“适用零售”。 若编辑了“价格折扣”字段后，发现“价格折扣”与“适用零售”都处于勾选状态，则应该给
	   * 出提示对话框，根据用户的对话结果设置“价格折扣”与“适用零售”的状态。
	   * <li></li>
	   * </p>
	   * 
	   * @date 2013-8-17 下午07:59:09
	   * @since NC6.31
	   * @author zhangyou3
	   */
	  private void doAfterDiscountEdit() {
	    // 如果“适用零售”已经处于勾选状态，并且又勾选了“价格折扣”
	    if (isRetailSelected() && isDiscountflagSelected()) {
	      // String msg = "勾选“价格折扣”后，适用零售将被取消勾选，是否继续勾选“价格折扣”？";
	      String msg =
	          nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("10140mag", "110140mag0340");
	      // 若用户选择继续勾选“价格折扣”，则将“适用零售”字段清空
	      if (MessageDialog.ID_YES == MessageDialog.showYesNoDlg(this, null, msg)) {
	        this.getBillCardPanel().getHeadItem(MaterialVO.RETAIL).setValue(null);
	      } else {// 若用户选择否，则将“价格折扣”字段清空
	        this.getBillCardPanel().getHeadItem(MaterialVO.DISCOUNTFLAG).setValue(null);
	      }
	    }
	  }
	  private void resetFeatureclassValue() {
		    Boolean isfeature =
		        (Boolean) getBillCardPanel().getHeadItem(MaterialVO.ISFEATURE).getValueObject();
		    BillItem billItem = getBillCardPanel().getHeadItem(MaterialVO.FEATURECLASS);
		    if (isfeature == null || !isfeature.booleanValue()) {
		      billItem.setValue(null);
		      billItem.setEnabled(false);
		    } else {
		      billItem.setEnabled(true);
		    }
		  }
	
	  /**
	   * 
	   * <p>
	   * 说明：当从列表切换到卡片时，处理物料图片的显示（即那个小图片）
	   * <li></li>
	   * </p>
	   * 
	   * @date 2015年9月30日 上午11:20:22
	   * @since NC6.5
	   */
	  public void showMarImageAsShowCardEditor() {
	    BillItem pictureitem = this.getBillCardPanel().getHeadItem(MaterialVO.PICTURE);
	    ImageBillItemEditor_m pictureItemEditor = (ImageBillItemEditor_m) pictureitem.getItemEditor();
	    pictureItemEditor.setTempPath(null);
	    Object obj = getModel().getSelectedData();
	    MaterialVO vo;
	    if (obj instanceof AggMaterialPfVO) {
	      // 处理当从申请单查询按钮联查时转化错误的处理
	      MaterialPfVO pfvo = (MaterialPfVO) ((AggMaterialPfVO) obj).getParentVO();
	      vo = (MaterialVO) pfvo.getMaterial();
	    } else {
	      vo = (MaterialVO) getModel().getSelectedData();
	    }
	    if (vo != null) {
	      String oldPk_material = pictureItemEditor.getPkID();
	      String pk_material = vo.getPk_material();
	      if (!StringUtils.equals(oldPk_material, pk_material) || StringUtils.isEmpty(oldPk_material)) {
	        pictureItemEditor.initEditor(pk_material);
	        pictureItemEditor.initMarImageBillItemValue();
	      } else {
	        if (pictureItemEditor.isSaveDeletePicture(pk_material)) {
	          pictureItemEditor.initEditor(pk_material);
	          pictureItemEditor.setMarImageUILabelValue(null);
	        } else {
	          pictureItemEditor.initEditor(pk_material);
	          pictureItemEditor.initMarImageBillItemValue();
	        }
	      }
	    } else {
	      pictureItemEditor.initEditor(null);
	      pictureItemEditor.setMarImageUILabelValue(null);
	    }
	  }
	
	  /**
	   * 
	   * <p>
	   * 说明：“固定换算”字段被编辑后，根据当前“适用零售”字段的状态，做相应的处理。 场景：“适用零售”与“固定换算”存在约束关系：勾选了“适用零售”，则必须勾选所有的固定换算；
	   * 若编辑“固定换算”后，“固定换算”处于未被勾选状态，并且当前已经勾选了“适用零售”， 则提示错误信息给用户，并且设置勾选““固定换算”字段
	   * <li></li>
	   * </p>
	   * 
	   * @param e
	   * @date 2013-8-17 下午08:05:18
	   * @since NC6.31
	   */
	  private void doAfterFixedflagEdit(BillEditEvent e) {
	    Boolean isFixedflag =
	        (Boolean) this.getBillCardPanel().getBodyValueAt(e.getRow(), MaterialConvertVO.FIXEDFLAG);
	    // 如果取消了勾选“固定换算”，并且当前已经勾选了“适用零售”字段
	    if (isRetailSelected() && !isFixedflag.booleanValue()) {
	      this.getBillCardPanel().setBodyValueAt(UFBoolean.TRUE, e.getRow(),
	          MaterialConvertVO.FIXEDFLAG);
	      // 勾选了适用零售的物料，不允许取消勾选固定换算
	      String msg =
	          nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("10140mag", "110140mag0336");
	      MessageDialog.showErrorDlg(this, null, msg);
	    }
	  }
	  
	  /**
	   * 
	   * <p>
	   * 说明：判断所有的固定换算是否都勾选中
	   * <li></li>
	   * </p>
	   * 
	   * @return
	   * @date 2013-8-17 下午07:58:55
	   * @since NC6.31
	   * @author zhangyou3
	   */
	  private boolean isAllFixedflagSelected() {
	    int rowCount =
	        this.getBillCardPanel().getBillModel(IMaterialConst.MATERIAL_CONVERT).getRowCount();
	    for (int i = 0; i < rowCount; i++) {
	      Boolean bool =
	          (Boolean) this.getBillCardPanel().getBodyValueAt(i, MaterialConvertVO.FIXEDFLAG);
	      if (!bool.booleanValue()) {
	        return false;
	      }
	    }
	    return true;
	  }
	  
	  /**
	   * 
	   * <p>
	   * 说明：编辑“适用零售”字段后，根据编辑后“适用零售”的状态、当前“固定换算”的状态、 当前“价格折扣”的状态做相应的处理。 场景：
	   * 1.“适用零售”与“固定换算”之间存在约束关系：勾选了“适用零售”，则必须勾选所有的固定换算； 存在非固定换算，则不允许勾选“适用零售”。
	   * 若编辑后“适用零售”处于勾选状态，并且存在非固定换算，则提示错误信息给用户，并且清空“适用零售”字段 2.“适用零售”与“价格折扣”之间存在约束关系：不能同时勾选“价格折扣”与“适用零售”。
	   * 若编辑了“适用零售”字段后，发现“价格折扣”与“适用零售”都处于勾选状态，则应该给 出提示对话框，根据用户的对话结果设置“价格折扣”与“适用零售”的状态。
	   * <li></li>
	   * </p>
	   * 
	   * @param e
	   * @date 2013-8-17 下午07:59:45
	   * @since NC6.31
	   */
	  private void doAfterRetailEdit(BillEditEvent e) {
	    // 如果勾选了“适用零售”字段
	    if (isRetailSelected()) {
	      // 如果存在非固定换算
	      if (!this.isAllFixedflagSelected()) {
	        // 清空适用零售
	        this.getBillCardPanel().getHeadItem(MaterialVO.RETAIL).setValue(null);
	        // 物料存在非固定换算，不允许勾选适用零售
	        String msg =
	            nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("10140mag", "110140mag0335");
	        MessageDialog.showErrorDlg(this, null, msg);
	      }
	      // 如果“价格折扣”已经处于勾选状态
	      else if (isDiscountflagSelected()) {
	        // String msg = "勾选“适用零售”后，价格折扣将被取消勾选，是否继续勾选“适用零售”？";
	        String msg =
	            nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("10140mag", "110140mag0339");
	        // 若用户选择继续勾选“适用零售”，则将“价格折扣”字段清空
	        if (MessageDialog.ID_YES == MessageDialog.showYesNoDlg(this, null, msg)) {
	          this.getBillCardPanel().getHeadItem(MaterialVO.DISCOUNTFLAG).setValue(null);
	          this.resetMaterialmgt();// 价格折扣的状态改变会影响“食物物料管理模式的改变”
	        } else {// 若用户选择否，则将“适用零售”字段清空
	          this.getBillCardPanel().getHeadItem(MaterialVO.RETAIL).setValue(null);
	        }
	      }
	    }
	  }
	  public void resetPk_materialclass() {
		    this.getBillCardPanel().getHeadItem(MaterialVO.PK_MARBASCLASS).setValue(null);
	  }
	  /**
	   * 
	   * <p>
	   * 说明：当从列表切换到卡片时时，如果是新增操作，则设置pictureItemEditor.initEditor(null)，否则如果只是切换到列表状态
	   * 或者修改状态，则获取当前模型中物料的PK，然后同[String vopk = vo.getPrimaryKey();],同时获取pictureItemEditor
	   * 缓存存储的PK，如果缓存的PK为空或者和当前模型物料的PK不一致，则[pictureItemEditor.initEditor(vopk);],否则只做 刷新操作
	   * 
	   * <li></li>
	   * </p>
	   * 
	   * @date 2015年8月11日 上午10:37:56
	   * @since NC6.5
	   */
	  public void showMaterialImageForAddOrEdit() {
	    BillItem pictureitem = this.getBillCardPanel().getHeadItem(MaterialVO.PICTURE);
	    ImageBillItemEditor_m pictureItemEditor = (ImageBillItemEditor_m) pictureitem.getItemEditor();
	    pictureItemEditor.setTempPath(null);
	    if (getModel().getUiState() != UIState.ADD) {
	      pictureItemEditor.getTempPath(true);
	      pictureItemEditor.setMarImageBillItemValue(pictureItemEditor.getValueMap());
	    }/* 新增时 */
	    else {
	      pictureItemEditor.initEditor(null);
	      pictureItemEditor.setMarImageUILabelValue(null);
	    }
	    /* end */
	  }
	  /**
	   * 
	   * <p>
	   * 说明：判断适用零售是否被勾选上
	   * <li></li>
	   * </p>
	   * 
	   * @return
	   * @date 2013-8-17 下午08:03:45
	   * @since NC6.31
	   * @author zhangyou3
	   */
	  private boolean isRetailSelected() {
	    BillItem retail = this.getBillCardPanel().getHeadItem(MaterialVO.RETAIL);
	    return ((Boolean) retail.getValueObject()).booleanValue();

	  }
	  
	  /**
	   * 
	   * <p>
	   * 说明：判断价格折扣是否被勾选上
	   * <li></li>
	   * </p>
	   * 
	   * @return
	   * @date 2013-8-17 下午08:03:45
	   * @since NC6.31
	   * @author zhangyou3
	   */
	  private boolean isDiscountflagSelected() {
	    BillItem discountflag = this.getBillCardPanel().getHeadItem(MaterialVO.DISCOUNTFLAG);
	    return ((Boolean) discountflag.getValueObject()).booleanValue();
	  }
	  /**
	   * 
	   * <p>
	   * 说明：当设置了海关商品代码时。设置海关商品打印名称的默认值为海关商品代码。 这里根据海关商品代码参照获得的Pk查询海关商品代码VO
	   * <li></li>
	   * </p>
	   * 
	   * @throws BusinessException
	   * 
	   * @date 2013-8-28 下午07:46:54
	   * @since NC6.31
	   */
	  private void setDefaultGoodsPrtName() throws BusinessException {
	    BillItem billItem = getBillCardPanel().getHeadItem(MaterialVO.GOODSPRTNAME);

	    UIRefPane uiRefPane =
	        (UIRefPane) getBillCardPanel().getHeadItem(MaterialVO.PK_GOODSCODE).getComponent();
	    MaterialVO materialVO = (MaterialVO) this.getModel().getSelectedData();
	    if (StringUtils.isBlank(materialVO.getGoodsprtname())) {
	      billItem.setValue(uiRefPane.getRefCode());
	    }
	  }
	  @Override
	  public void setValue(Object object) {
	    if (object != null) {
	      if (object instanceof AggMaterialPfVO) {
	        MaterialPfVO pfVO = (MaterialPfVO) ((AggMaterialPfVO) object).getParentVO();
	        MaterialVO materialVO = (MaterialVO) pfVO.getMaterial();
	        super.setValue(materialVO);
	        this.billcode = materialVO.getCode();
	      } else {
	        super.setValue(object);
	        this.billcode = ((MaterialVO) object).getCode();
	      }
	    } else {
	      super.setValue(object);
	    }
	  }
	  
}
