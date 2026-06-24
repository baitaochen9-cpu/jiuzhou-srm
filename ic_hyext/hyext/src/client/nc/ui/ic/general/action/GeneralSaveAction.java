/** <a href="http://www.cpupk.com/decompiler">Eclipse Class Decompiler</a> plugin, Copyright (c) 2017 Chen Chao. */
package nc.ui.ic.general.action;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Log;
import nc.bs.uif2.validation.IValidationService;
import nc.itf.pubapp.pub.exception.IResumeException;
import nc.itf.scmpub.reference.uap.group.SysInitGroupQuery;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.busibean.ISysInitQry;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapProcessor;
import nc.ui.ic.general.model.ICGenBizEditorModel;
import nc.ui.ic.general.util.GenResumeExceptionHandle;
import nc.ui.ic.general.view.ICBizView;
import nc.ui.ic.pub.action.SaveAction;
import nc.ui.ic.pub.env.ICUIContext;
import nc.ui.ic.pub.model.ICBizModel;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pubapp.pub.common.context.PFlowContext;
import nc.ui.pubapp.pub.power.PowerSaveValidateService;
import nc.ui.pubapp.uif2app.AppUiState;
import nc.ui.pubapp.uif2app.actions.IActionExecutable;
import nc.ui.pubapp.uif2app.model.BillManageModel;
import nc.ui.pubapp.uif2app.validation.CompositeValidation;
import nc.ui.pubapp.uif2app.view.BillForm;
import nc.vo.ic.general.deal.ICBillValueSetter;
import nc.vo.ic.general.define.ICBillBodyVO;
import nc.vo.ic.general.define.ICBillVO;
import nc.vo.ic.general.util.ICLocationUtil;
import nc.vo.ic.location.ICLocationVO;
import nc.vo.ic.m4d.entity.MaterialOutBodyVO;
import nc.vo.ic.m4d.entity.MaterialOutVO;
import nc.vo.ic.pub.define.BillOperator;
import nc.vo.ic.pub.define.ICPubMetaNameConst;
import nc.vo.ic.pub.pf.ICPFParameter;
import nc.vo.ic.pub.util.StringUtil;
import nc.vo.ic.pub.util.ValueCheckUtil;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.para.SysInitVO;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.pubapp.pattern.model.entity.bill.AbstractBill;

import org.apache.commons.lang.StringUtils;

import com.ufida.zior.dialog.ErrorDialog;

/**
 * <p>
 * <b>保存动作：</b>
 * <ul>
 * <li>
 * </ul>
 * <p>
 * <p>
 * 
 * @version 6.0
 * @since 6.0
 * @author
 * @time 2010-8-27 下午09:08:51
 */
public class GeneralSaveAction extends SaveAction implements IActionExecutable {

  /**
   *
   */
  private static final long serialVersionUID = 2010082721520001L;

  /*
   * 取配置参数
   * */
  private ISysInitQry sysInitQry;
  //参数编码
  private static final String InitCode="YF22001";
  private static final String FLAGARGU0 = "不控制";
  private static final String FLAGARGU1 = "提示";
  private static final String FLAGARGU2 = "强控";
  
  private SysInitVO getSysInitParma(String pk_org){
  	if(null==sysInitQry){
  		sysInitQry=(ISysInitQry)NCLocator.getInstance().lookup(ISysInitQry.class);
  	}
  	try {
			 return sysInitQry.queryByParaCode(pk_org, InitCode);
		} catch (BusinessException e) {
			e.printStackTrace();
		}
  	return null;
  }
  @Override
  public void doBeforAction() {
    super.doBeforAction();
    // 过滤表体空行
    this.getEditorModel().getCardPanelWrapper().filterNullLine();
    
    // 当前的表体行数
    int iRowCount =this.getEditorModel().getCardPanelWrapper().getBillCardPanel().getRowCount();

    if (iRowCount <= 0) {
      ExceptionUtils.wrappBusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("4008001_0", "04008001-0031")/* @res "无表体行" */);
    }
    this.checkSnDuplicate();
    
    
    //nc.ui.ic.m4e.view.TransInBizView
    this.jz_transInbatch();
  }


private void jz_transInbatch() {
	 ICBizView view = (ICBizView) this.getEditorModel().getICBizView();
	    if (view == null  && !(view instanceof nc.ui.ic.m4e.view.TransInBizView)) {
	      return;
	    }
	    
	    Object value = this.editor.getValue();
		ICBillVO aggvo = (ICBillVO) value;
	    
		ICBillBodyVO[] bvo= aggvo.getBodys();
		IUAPQueryBS iuap = (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
	    for(int i =0 ;i<bvo.length;i++){
	    

	    	String pk_batchcode = (String) bvo[i].getAttributeValue("pk_batchcode");
	    	String cmaterialoid = (String) bvo[i].getAttributeValue("cmaterialoid");
	    	String vbatchcode = (String) bvo[i].getAttributeValue("vbatchcode");
	    	if (null == pk_batchcode || "".equals(pk_batchcode))
	    		continue;
	    	String sql = " select cmaterialoid from scm_batchcode where pk_batchcode = '"+pk_batchcode+"' and cmaterialoid ='"+cmaterialoid+"' and nvl(dr,0) = 0";
	    	String sql_2 = " select pk_batchcode from scm_batchcode where vbatchcode = '"+vbatchcode+"' and cmaterialoid ='"+cmaterialoid+"' and nvl(dr,0) = 0 " ;
	    	try {
				Map map = (Map)iuap.executeQuery(sql, new MapProcessor());
				Object pk_batchcode_dec = iuap.executeQuery(sql_2, new ColumnProcessor());
				if(map==null||map.size()==0){//无法匹配批次档案，需要创建新批次
					if(null == pk_batchcode_dec){
						((BillForm) this.editor).getBillCardPanel().setBodyValueAt(null, i, "pk_batchcode");
					}else{
						((BillForm) this.editor).getBillCardPanel().setBodyValueAt((String)pk_batchcode_dec, i, "pk_batchcode");
					}
				}
			} catch (BusinessException e) {
				// TODO Auto-generated catch block
				ExceptionUtils.wrappBusinessException("");
			}
	    }
	
}
@Override
  public void setModel(BillManageModel model) {
    super.setModel(model);
    model.addAppEventListener(this);
  }

  /**
   * 增加校验器
   * 
   * @param validationService
   */
  @Override
  public void setValidationService(IValidationService validationService) {
    // 校验器是权限校验器
    if (validationService instanceof PowerSaveValidateService) {
      IValidationService validator = this.getValidationService();
      // 是否已经传入组合校验器
      if (validator instanceof CompositeValidation) {
        List<IValidationService> list =
            ((CompositeValidation) validator).getValidate();
        list.add(validationService);
        ((CompositeValidation) validator).setValidators(list);
      }
      else {
        validator = validationService;
      }
      super.setValidationService(validator);
    }
    // 校验器是组合校验器
    else if (validationService instanceof CompositeValidation) {
      List<IValidationService> list =
          ((CompositeValidation) validationService).getValidate();
      if (this.getValidationService() != null) {
        list.add(this.getValidationService());
      }
      super.setValidationService(validationService);
    }
    else {// 其他校验器暂时不处理
      super.setValidationService(validationService);
    }

  }

  private void afterSave() {
    this.getEditorModel().setTempBillPK(null);
    this.getEditorModel().clearAllBodyDetailData();
    ((ICGenBizEditorModel) this.getEditorModel()).getICBizModel()
        .clearLocationVOsAtSelectedRow();
    this.getModel().setAppUiState(AppUiState.NOT_EDIT);
    ((ICBizModel) this.getModel()).getIcUIContext().showStatusBarMessage(
        nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("common",
            "UCH005")/* @res "保存成功" */);
  }
  


  
  /**
   * 校验本次操作的序列号是否重复（不校验数据库中数据）
   */
  private void checkSnDuplicate() {
    ICBizView view = (ICBizView) this.getEditorModel().getICBizView();
    if (view == null) {
      return;
    }
    ICBillVO bill = (ICBillVO) this.getEditorModel().getICBizView().getValue();
    ICBillBodyVO[] bodys = bill.getBodys();
    Map<String, String> sns = new HashMap<String, String>();
    Map<String, Set<String>> dupsnRowNoMap = new LinkedHashMap<String, Set<String>>();
    // 序列号模块是否启用 ？ true 物料+号唯一 ：号唯一
    boolean isSNEndabled = SysInitGroupQuery.isSNEnabled();
    
    for (int i = 0; i < bodys.length; i++) {
      String rowno =
          this.getEditorModel().getCardPanelWrapper()
              .getBodyValueAt_String(i, ICPubMetaNameConst.CROWNO);
      ICLocationVO[] locs = this.getEditorModel().getBodyEditDetailData(i);
      if (ValueCheckUtil.isNullORZeroLength(locs)) {
        continue;
      }
      for (int j = 0; j < locs.length; j++) {
        String sn = locs[j].getVserialcode();
        if (StringUtil.isSEmptyOrNull(sn)) {
          continue;
        }
        String key = isSNEndabled ? bodys[i].getCmaterialvid() + sn : sn;
        
        if (sns.containsKey(key)) {
          if (dupsnRowNoMap.get(sn) == null) {
            Set<String> dupset = new HashSet<String>();
            dupset.add(sns.get(key));
            dupset.add(rowno);
            dupsnRowNoMap.put(sn, dupset);
          }
          else {
            dupsnRowNoMap.get(sn).add(sns.get(key));
            dupsnRowNoMap.get(sn).add(rowno);
          }
        }
        sns.put(key, rowno);
      }
    }
    if (ValueCheckUtil.isNullORZeroLength(dupsnRowNoMap)) {
      return;
    }
    this.showErrMsg(dupsnRowNoMap);
  }

  private void showAutoBalancedHint(Object[] retObj) {// 有问题 计算属性前后台序列化 字段丢失
    if (!(retObj instanceof ICBillVO[])) {
      return;
    }
    ICBillVO[] bills = (ICBillVO[]) retObj;
    for (ICBillVO bill : bills) {
      if (bill.getHead().getHasbalanced() == null
          || !bill.getHead().getHasbalanced().booleanValue()) {
        continue;
      }
      MessageDialog.showWarningDlg(this.getEditorModel().getContext()
          .getContainUI(), null, nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
          .getStrByID("4008001_0", "04008001-0032")/* @res "单据发生数量倒挤" */);

    }

  }

  /**
   * 显示错误消息
   * 
   * @param dupsnRowNoMap
   */
  private void showErrMsg(Map<String, Set<String>> dupsnRowNoMap) {
    StringBuilder errMsg = new StringBuilder();
    errMsg.append(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
        "4008001_0", "04008001-0033")/* @res "以下序列号重复：\n" */);
    for (Map.Entry<String, Set<String>> snent : dupsnRowNoMap.entrySet()) {
      StringBuffer rownostr = new StringBuffer();
      for (String rowno : snent.getValue()) {
        rownostr.append(rowno);
        rownostr.append(",");
      }
      rownostr.deleteCharAt(rownostr.length() - 1);
      errMsg.append(NCLangRes.getInstance().getStrByID("4008001_0",
          "04008001-0669", null, new String[] {
            snent.getKey(), rownostr.toString()
          })/* 序列号：{0} 行：{1} */);
      errMsg.append("\n");
    }
    errMsg.deleteCharAt(errMsg.length() - 1);
    ExceptionUtils.wrappBusinessException(errMsg.toString());
  }

  /**
   *
   */
  @Override
  protected void fillUpContext(PFlowContext context) {
    ICPFParameter pfparam = (ICPFParameter) context.getUserObj();
    if (context.getUserObj() == null) {
      pfparam = new ICPFParameter();
    }
    if (this.getModel().getAppUiState() == AppUiState.ADD) {
      pfparam.setBillaction(BillOperator.New);
    }
    else if (this.getModel().getAppUiState() == AppUiState.EDIT) {
      pfparam.setBillaction(BillOperator.Edit);
    }
    context.setUserObj(pfparam);
    super.fillUpContext(context);
  }

  @Override
  protected boolean isResume(IResumeException resumeInfo) {
    boolean executed =
        new GenResumeExceptionHandle(this.getEditorModel(),
            this.getFlowContext()).isResume(resumeInfo);
    this.setExecuted(executed);
    return executed;
  }

  /**
   *
   */
  @Override
  protected Object[] processBefore(Object[] vos) {
    ICBillVO icBillVO = null;
    ICUIContext context = ((ICBizModel) this.getModel()).getIcUIContext();
    ICBillValueSetter setter = new ICBillValueSetter();
    // ICBillEntityCheck check=new ICBillEntityCheck(false);
    for (Object vo : vos) {
      icBillVO = (ICBillVO) vo;
      try {
        setter.setBillInitData(icBillVO, context);
        // 设置表体的单品数据
        ((ICGenBizEditorModel) this.getEditorModel())
            .setLocationVOToBodyForSave(icBillVO);
        icBillVO.setTempBillPK(this.getEditorModel().getTempBillPK());
        // 单据实体数据检查
        // delete 原因:此时子表外键并没有被设值，为null，在主子表字段一致的校验时候通不过
        // check.checkBill(icBillVO);
      }
      catch (Exception ex) {
        ExceptionUtils.wrappException(ex);
      }
    }
    return super.processBefore(vos);
  }

  @Override
  protected void processReturnObj(Object[] retObj) throws Exception {
    super.processReturnObj(retObj);
    this.afterSave();
    this.showAutoBalancedHint(retObj);
  }

  @Override
  protected AbstractBill[] produceLightVO(AbstractBill[] newVO) {
    Map<Integer, Map<Integer, ICLocationVO[]>> beforeUpdatedVOMap =
        ICLocationUtil.getLocationVO((ICBillVO[]) newVO);
    AbstractBill[] lightVOs = super.produceLightVO(newVO);
    this.fillBatchInfoAfterLight(lightVOs, newVO);
    ICLocationUtil.setLocationVO((ICBillVO[]) lightVOs, beforeUpdatedVOMap);
    
    return (AbstractBill[]) lightVOs;
  }
  
  /**
   * 方法功能描述：补充批次相关的计算属性
   * 对于单据上的批次质量等级，生产日期，失效日期
   * 当批次改变，上述字段录入与批次改变前的值相同时，
   * 生成的批次档案上述字段没有值
   * 差异VO的原因
   * <p>
   */
  private void fillBatchInfoAfterLight(AbstractBill[] lightVOs, AbstractBill[] newVO) {
    if (AppUiState.EDIT != this.model.getAppUiState()) {
      return;
    }

    Map<String, CircularlyAccessibleValueObject> newmap = getValueMap(newVO);
    if (ValueCheckUtil.isNullORZeroLength(newmap)) {
      return;
    }
    
    final String[] fillFields =
        new String[] {
          ICPubMetaNameConst.CQUALITYLEVELID, ICPubMetaNameConst.DPRODUCEDATE,
          ICPubMetaNameConst.DVALIDATE,ICPubMetaNameConst.CSNUNITID
        };

    try {
      for (AbstractBill lightVO : lightVOs) {
        fillBatchInfo(newmap, fillFields, lightVO);
      }

    }
    catch (BusinessException e) {
      ExceptionUtils.wrappException(e);
    }

  }

  private void fillBatchInfo(
      Map<String, CircularlyAccessibleValueObject> newmap,
      final String[] fillFields, AbstractBill lightVO) throws BusinessException {
    CircularlyAccessibleValueObject[] bodys = lightVO.getAllChildrenVO();
    if (ValueCheckUtil.isNullORZeroLength(bodys)) {
      return;
    }
    for (CircularlyAccessibleValueObject body : bodys) {
      String primarykey = body.getPrimaryKey();
      CircularlyAccessibleValueObject newbody = newmap.get(primarykey);
      if (StringUtil.isSEmptyOrNull(primarykey) || newbody == null) {
        continue;
      }
      for (String field : fillFields) {
        if (body.getAttributeValue(field) != null) {
          continue;
        }
        body.setAttributeValue(field, newbody.getAttributeValue(field));
      }
    }
  }

  private Map<String, CircularlyAccessibleValueObject> getValueMap(AbstractBill[] oldVOs) {
    Map<String, CircularlyAccessibleValueObject> oldmap =
        new HashMap<String, CircularlyAccessibleValueObject>();
    try {
      if (ValueCheckUtil.isNullORZeroLength(oldVOs)) {
        return null;
      }
      for (AbstractBill old : oldVOs) {
        CircularlyAccessibleValueObject[] bodys = old.getAllChildrenVO();
        for (CircularlyAccessibleValueObject body : bodys) {
          String primarykey = body.getPrimaryKey();
          if (StringUtil.isSEmptyOrNull(primarykey)) {
            continue;
          }
          oldmap.put(primarykey, body);
        }
      }
    }
    catch (BusinessException ex) {
      ExceptionUtils.wrappException(ex);
    }
    return oldmap;
  }

  private boolean isExecuted = true;

  @Override
  public boolean isExecuted() {
    return this.isExecuted;
  }

  public void setExecuted(boolean isExecuted) {
    this.isExecuted = isExecuted;
  }
  
	@Override
	public void doAction(ActionEvent e) throws Exception {
		Object value = this.editor.getValue();
		ICBillVO aggvo = (ICBillVO) value;
		String type=aggvo.getHead().getVtrantypecode();
		if(null != type && type.startsWith("4455")){
//			TODO
			String vbilllno=aggvo.getHead().getVdef1();
			if(vbilllno!=null&&!vbilllno.equals("")){
				String sql=" select bill_code from ewm_wo a where a.bill_code='"+vbilllno+"' and dr=0 and pk_org ='"+aggvo.getHead().getPk_org()+"'";
				IUAPQueryBS iuap = (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
				Map map = (Map)iuap.executeQuery(sql, new MapProcessor());
				if(map==null||map.size()==0){
					MessageDialog.showErrorDlg(this.getFlowContext().getParent(), null,"工单号不存在");
					return;
				}
			}
		}
		
		// 校验：csourcetype=55A3 且物料辅助属性勾选了"项目"时，产成品辅助属性-项目不能为空
		{
			IUAPQueryBS iuapProj = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
			ICBillBodyVO[] projBvo = aggvo.getBodys();
			StringBuilder projErrMsg = new StringBuilder();
			for (int j = 0; j < projBvo.length; j++) {
				String csourcetype = (String) projBvo[j].getAttributeValue("csourcetype");
				if (!"55A3".equals(csourcetype)) {
					continue;
				}
				String cmaterialoid = projBvo[j].getCmaterialoid();
				if (cmaterialoid == null || cmaterialoid.trim().isEmpty()) {
					continue;
				}
				String sqlProject =
					"select nvl(f.fix2, 'N') as fixproject" +
					" from bd_material m" +
					" left join bd_marasstframe f on m.pk_marasstframe = f.pk_marasstframe" +
					" where m.pk_material = '" + cmaterialoid + "'" +
					" and m.dr = 0 and nvl(f.dr, 0) = 0";
				Map mapProject = (Map) iuapProj.executeQuery(sqlProject, new MapProcessor());
				if (mapProject != null && "Y".equals(mapProject.get("fixproject"))) {
					Object cprodprojectid = projBvo[j].getAttributeValue("cprodprojectid");
					if (cprodprojectid == null || cprodprojectid.toString().trim().isEmpty()) {
						projErrMsg.append("第").append(j + 1).append("行：物料辅助属性含项目，产成品辅助属性-项目不能为空\n");
					}
				}
			}
			if (projErrMsg.length() > 0) {
				ExceptionUtils.wrappBusinessException(projErrMsg.toString().trim());
			}
		}

		/**************bbt 2024.11.13 增加IC144控制是否判断是否近效期管理************************************************/
		//YZIC01	出库效期管理，范围：强控,提示,不控制
		IUAPQueryBS iuap = (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		String pk_org=aggvo.getHead().getPk_org();
		String sqlGetArgu = "select nvl(a.value,0)   as value " + "\n" +
	    							"from  pub_sysinit a " + "\n" + 
	    							"where a.initcode ='YZIC01' and  a.pk_org='"+aggvo.getHead().getPk_org()+"'";
		Map mapGetArgu = (Map)iuap.executeQuery(sqlGetArgu, new MapProcessor());
		String isXiaoQiMan= String.valueOf(mapGetArgu.get("value").toString()) ;
		if(FLAGARGU0.equals(isXiaoQiMan)){
			//不控制
		}
		else if(FLAGARGU2.equals(isXiaoQiMan)){
			//近效期强控
			if(null != type && (type.startsWith("4C")||type.startsWith("4D")||type.startsWith("4I"))){
		    		ICBillBodyVO[] bvo=aggvo.getBodys();
		    		for(int j=0;j<bvo.length;j++){
		    			String marerial=bvo[j].getCmaterialoid();
		    			//批次主键
		    			String pk_batchcode=bvo[j].getPk_batchcode();
	//	    			IUAPQueryBS iuap = (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
	//	    			String pk_org=aggvo.getHead().getPk_org();
		    			//失效天数
		    			String sql = "select nvl(a.value,0)   as value " + "\n" +
		    							"from  pub_sysinit a " + "\n" + 
		    							"where a.initcode ='IC145' and  a.pk_org='"+pk_org+"'";
		    			Map map = (Map)iuap.executeQuery(sql, new MapProcessor());
		    				int tianshu= Integer.valueOf(map.get("value").toString()) ;
		    			
		    			//失效时间
		    			String sql2 = "select a.dvalidate " + "\n" +
		    							"from  scm_batchcode a " + "\n" + 
		    							"where a.pk_batchcode='"+pk_batchcode+"'";
		    			Map map2 = (Map)iuap.executeQuery(sql2, new MapProcessor());
		    				if (map2!=null&&map2.size()!= 0&&map2.get("dvalidate")!=null&&!"".equals(map2.get("dvalidate"))) {
		    					UFDate date= new UFDate( map2.get("dvalidate").toString());	
		    					UFDate dbilldate =aggvo.getHead().getDbilldate();
		        				if(dbilldate.getDateAfter(tianshu).after(date)){
//		        					MessageDialog.showHintDlg(null,"提示", "失效/复测日期≤10天，"+"\n"+"请确认是否发料！！！");
		        					ErrorDialog errorDia = new ErrorDialog();
		        					errorDia.setErrorMessage("失效/复测日期≤10天,"+"\n"+"不允许发料！");
		        					errorDia.showModal();
		        					ExceptionUtils.wrappBusinessException("失效/复测日期≤10天,"+"\n"+"不允许发料！");
//		        					throw new BusinessException("失效/复测日期≤10天,"+"\n"+"不允许发料！");
		        					
		        				}
		    				}
		    		}
			}
		}
		else if(FLAGARGU1.equals(isXiaoQiMan)){
		/****************************************************************************************************************/
			//近效期提醒
			if(null != type && (type.startsWith("4C")||type.startsWith("4D")||type.startsWith("4I"))){
		    		ICBillBodyVO[] bvo=aggvo.getBodys();
		    		for(int j=0;j<bvo.length;j++){
		    			String marerial=bvo[j].getCmaterialoid();
		    			//批次主键
		    			String pk_batchcode=bvo[j].getPk_batchcode();
	//	    			IUAPQueryBS iuap = (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
	//	    			String pk_org=aggvo.getHead().getPk_org();
		    			//失效天数
		    			String sql = "select nvl(a.value,0)   as value " + "\n" +
		    							"from  pub_sysinit a " + "\n" + 
		    							"where a.initcode ='IC145' and  a.pk_org='"+pk_org+"'";
		    			Map map = (Map)iuap.executeQuery(sql, new MapProcessor());
		    				int tianshu= Integer.valueOf(map.get("value").toString()) ;
		    			
		    			//失效时间
		    			String sql2 = "select a.dvalidate " + "\n" +
		    							"from  scm_batchcode a " + "\n" + 
		    							"where a.pk_batchcode='"+pk_batchcode+"'";
		    			Map map2 = (Map)iuap.executeQuery(sql2, new MapProcessor());
		    				if (map2!=null&&map2.size()!= 0&&map2.get("dvalidate")!=null&&!"".equals(map2.get("dvalidate"))) {
		    					UFDate date= new UFDate( map2.get("dvalidate").toString());	
		    					UFDate dbilldate =aggvo.getHead().getDbilldate();
		        				if(dbilldate.getDateAfter(tianshu).after(date)){
		        					MessageDialog.showHintDlg(null,"提示", "失效/复测日期≤10天，"+"\n"+"请确认是否发料！！！");
		        					
		        				}
		    				}
		    		}
			}
		}else{
			//可能参数有问题，不在定义的参数内
			throw new BusinessException("IC144参数可能存在问题，请联系管理员检查");
		}
//		TODO  货位检查
		RackCheckRule rackCheck = new RackCheckRule(this.getFlowContext());
		String checkRack = rackCheck.checkRack(aggvo);
		//如果校验不通过,则终止保存动作
		if(!"pass".equalsIgnoreCase(checkRack)){
			return ;
		}
//		TODO  货位检查
		if(type!=null&&type.contains("4A")){
		try {
			ICBillBodyVO[] bvo= aggvo.getBodys();
		    for(int i =0 ;i<bvo.length;i++){
		    	//查看物料批次档案是否有启动
		    	boolean flag=getStockInfo(bvo[i].getCmaterialoid(), bvo[i].getPk_org());
		    	if(bvo[i].getVbatchcode()==null||bvo[i].getVbatchcode().equals(""))
		    	if(flag==true){
		    		String pcgz=querySysinitName("pcgz", bvo[i].getPk_org());//批次规则
					if("Y".equals(pcgz)){//如果批次为空
						String code=getcode(bvo[i].getCmaterialoid());
						String pcqz=querySysinitName("pcqz", bvo[i].getPk_org());//批次前缀采取流水号前几位
						String pctsbs=querySysinitName("pctsbs", bvo[i].getPk_org());//批次特殊标识
						if(pcqz.equals("")){
							MessageDialog.showErrorDlg(this.getFlowContext().getParent(), null,"生成批次参数pcqz为空");
							return;
						}
						int pcqzcd=Integer.parseInt(pcqz);//获取物料前缀的长度
						code=getpcqz(pcqzcd, code);//计算批次号前缀
						String qz=code.substring( code.length()-pcqzcd, code.length());//前缀
						
						String bccode=getscmbatchcode(qz+pctsbs, bvo[i].getCmaterialoid());//获取最新的批次号
						String pch="";//生成的批次号
						if(!bccode.equals("")){//流水号不为空
							String pclshcd=querySysinitName("pclshcd", bvo[i].getPk_org());//批次流水号长度
							if(pclshcd.equals("")){
								MessageDialog.showErrorDlg(this.getFlowContext().getParent(), null,"生成批次参数pclshcd为空");
								return;
							}
							int lshcd=Integer.parseInt(pclshcd);//获取批次流水号的长度
							String lsh=bccode.substring( bccode.length()-lshcd, bccode.length());//获取当前流水号
							Integer intcode= Integer.parseInt(lsh);
							lsh= String.format("%0"+lshcd+"d", (++intcode)); 
							pch=qz+pctsbs+lsh;//生成的批次号
						}else{//流水号为空
							String pclsqsh=querySysinitName("pclsqsh", bvo[i].getPk_org());//批次流水号初始号
							pch=qz+pctsbs+pclsqsh;//生成默认批次号
						} 
						((BillForm) this.editor).getBillCardPanel().setBodyValueAt(pch, i, "vbatchcode");
					}
		    	}
		    }
			} catch (BusinessException ex) {
				ex.printStackTrace();
			}
		}
		//来源备料计划出库 进行上线容差控制 2022年11月5日
		if(null != type && type.startsWith("4D")){
			MaterialOutVO outVO=(MaterialOutVO) aggvo;
			String querySysinitName = this.querySysinitName("YF22001", outVO.getHead().getPk_org());
	        if(StringUtils.isNotEmpty(querySysinitName)){
	        	UFDouble UF100=new UFDouble("100");
	        	//检查是否超容差
	        	//校验逻辑：累计出库数量naccoutnum>=vbdef3 &&  vbdef4>=naccoutnum
	        	//校验逻辑：累计出库数量naccoutnum>vbdef3 &&  vbdef4>naccoutnum  2022年10月11日
	        	StringBuffer mes=new StringBuffer();
	        	
	        	for (MaterialOutBodyVO outBodyVO : outVO.getBodys()) {
					//判断容差比例
					if(StringUtils.isEmpty(outBodyVO.getVbdef6())){
						Log.getInstance("领料出库容差控制").error("Vbdef6 为空值!");
						continue;
					}
					/*********************bbt 2023.11.29***********************************/
					if(!(null == outBodyVO.getNshouldnum())){
					/**************************************************************/
						//下限数量
						UFDouble dowmNum=outBodyVO.getNshouldnum().multiply(UF100.sub(new UFDouble(outBodyVO.getVbdef6()))).div(UF100).setScale(3, UFDouble.ROUND_HALF_UP);
						//上限数量
						UFDouble upNum=outBodyVO.getNshouldnum().multiply(UF100.add(new UFDouble(outBodyVO.getVbdef6()))).div(UF100).setScale(3, UFDouble.ROUND_HALF_UP);
						if(outBodyVO.getNnum().compareTo(dowmNum)<0 ){
							mes.append(outBodyVO.getCrowno()+" 行, 容差下限:"+dowmNum+", 当前出库数量:"+outBodyVO.getNnum()+" \n");
						}else if(outBodyVO.getNnum().compareTo(upNum)>0){
							mes.append(outBodyVO.getCrowno()+" 行, 容差上限:"+upNum+", 当前出库数量:"+outBodyVO.getNnum()+" \n");
							//ExceptionUtils.wrappBusinessException(pickmItemVO.getVrowno()+" 行, 容差上限:"+upNum+", 累计出库数量:"+pickmItemVO.getNbccknum()+" ,不允许超容差上限出库!");
						}
					}
				}
	    		if(mes.length()>0){
	    			
	    			if("容差提醒".equals(querySysinitName)){
	    				//4 是 8 否
	    				int showYesNoDlg = MessageDialog.showYesNoDlg(null, "提醒", mes.append(" 									是否继续?").toString());
	    				if(showYesNoDlg==4){
	    					//运行系统标准逻辑处理
	    					super.doAction(e);
	    					//System.out.println("执行领料操作==========================");
	    				}else{
	    					return;
	    				}
	            	}else if("容差强控".equals(querySysinitName)){
	            		ExceptionUtils.wrappBusinessException(mes.append(" 不允许继续操作!").toString());
	            		
	            	}else{
	            		//运行系统标准逻辑处理
	            		super.doAction(e);
	            	}
	    		}else{
	    			//运行系统标准逻辑处理
	    			super.doAction(e);
	    			//System.out.println("else 执行领料操作==========================");
	    		}
	        }
		}
		try{
			super.doAction(e);
		}
		catch (Exception ex) {
			
	        MessageDialog.showErrorDlg(getModel().getContext().getEntranceUI(),"Error", ex.getMessage()); 
	      }
	}
	//查询参数代码
	private String querySysinitName(String initcode,String pk_org) throws BusinessException {
		  String sql = "select value from pub_sysinit  where initcode='"+initcode+"' and pk_org='"+pk_org+"' and nvl(dr,0)=0";
		  Object obj = NCLocator.getInstance().lookup(IUAPQueryBS.class).executeQuery(sql, new ColumnProcessor());
		  return obj == null ? "" : obj.toString();
		 }
	
	
	//查询物料是否启用批次管理
	public  boolean getStockInfo(String pk_material,String pk_org){
		String sb="select count(wholemanaflag) from bd_materialstock where pk_material='"+pk_material+"' and pk_org='"+pk_org+"' and  wholemanaflag='Y'"; 
		 Integer count = 0;
		try {
			   count = (Integer)NCLocator.getInstance().lookup(IUAPQueryBS.class).executeQuery(sb.toString(), new ColumnProcessor());
			   return count>0?true:false;
			  } catch (BusinessException e) {
			   e.printStackTrace();
			  }
		
		return false;
	}
	
	//查询物料编码
	public String getcode(String pk_material) throws BusinessException{
		 String sql = "select code from  bd_material where pk_material='"+pk_material+"'";
		  Object obj = NCLocator.getInstance().lookup(IUAPQueryBS.class).executeQuery(sql, new ColumnProcessor());
		  return obj == null ? "" : obj.toString();
	}
	
	//查询最新的批次号
	public String  getscmbatchcode(String  scmqz,String pk_material)throws BusinessException{
		 String sql = "select max(vbatchcode) from  scm_batchcode where cmaterialoid='"+pk_material+"' and  vbatchcode like '%"+scmqz+"%' ";
		  Object obj = NCLocator.getInstance().lookup(IUAPQueryBS.class).executeQuery(sql, new ColumnProcessor());
		  return obj == null ? "" : obj.toString();
	}
	
	//递归 计算 批次号补0规则
	public String getpcqz(int i ,String code){
		if(i>code.length()){
			code="0"+code;
			getpcqz(i, code);
		}
		return code;
	} 
}
