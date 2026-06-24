package nc.ui.mmpac.wr.action;

 import java.awt.event.ActionEvent;

import nc.cmp.bill.util.SysInit;
import nc.ui.mmf.framework.action.ActionInitializer;
import nc.ui.mmpac.wr.util.WrProcessMOUtil;
import nc.ui.mmpac.wr.util.WrUIHelperPatch;
import nc.ui.mmpub.dpub.service.IUIService;
import nc.ui.uif2.ShowStatusBarMsgUtil;
import nc.util.mmf.framework.base.MMValueCheck;
import nc.vo.mmpac.wr.consts.WrBtnConst;
import nc.vo.mmpac.wr.consts.WrptLangConst;
import nc.vo.mmpac.wr.entity.AggWrVO;
import nc.vo.mmpac.wr.entity.WrItemVO;
import nc.vo.pub.lang.UFBoolean;
 
 
 
 
 
 
 
 public class WrProdInAction extends WrBaseAction
 {
   private static final long serialVersionUID = 1668227361587214554L;
   private IUIService uiService;
   private UFBoolean SZ_PU01;
   
   public IUIService getUiService()
   {
     return this.uiService;
   }
 
   public void setUiService(IUIService uiService) {
     this.uiService = uiService;
   }
 
   public WrProdInAction() {
    ActionInitializer.initializeAction(this, WrBtnConst.getBTN_CODE_WR_PROD_IN(), WrBtnConst.getBTN_NAME_WR_PROD_IN(), WrBtnConst.getBTN_TOOLTIP_WR_PROD_IN());
   }
 
 
   public void doAction(ActionEvent e)
     throws Exception
   {
     AggWrVO[] aggWrVOs = new WrUIHelperPatch(getModel(), getBillForm()).getProductSelDatas();
     if ((MMValueCheck.isEmpty(aggWrVOs)) || (MMValueCheck.isEmpty(getUiService()))) {
      return;
     }
     /*表头：Vdef10,作为交换单生产日期
      *表体：本次入库主数量，更新到产成品入库交换单
      *@param RAYBOW_PU01
      *@author hw 20220425
      * */
     for (AggWrVO agg : aggWrVOs){
    	 SZ_PU01 =SysInit.getParaBoolean(agg.getParentVO().getPk_org(), "SZ_PU01");
    	 if(SZ_PU01 == UFBoolean.FALSE){/*根据参数判断，如果是否则直接按照系统逻辑继续*/
    		break;
    	 }
    	WrItemVO[] WRitem= agg.getChildrenVO();
    	for (WrItemVO item : WRitem){
    		if(item.getFbproducttype().equals(1)){  /*只更新主产成品*/
    			item.setAttributeValue("dproducedate_148", 
    											agg.getParentVO().getAttributeValue("vdef10"));/* 生产日期 */
 
    		}		
    		
    	}
    	 
     }
     

     
//   //如果是外系统质检--检验是否已经入库
// 	ISysDispatcher outerService = (ISysDispatcher) NCLocator.getInstance().lookup(
//			ISysDispatcher.class.getName());
//	IProcessService  busiparam = (IProcessService) NCLocator.getInstance().lookup(
//			IProcessService.class.getName());
//	 if(busiparam.isOutSystem( aggWrVOs[0].getParentVO().getPk_org())){
//		 for(AggWrVO agg:aggWrVOs){
//			 Map<String, Object> param = new HashMap<String, Object>();
//			 outerService.dispatch(agg, "LIMS_WR_ISCHECK", param);
//		 }
//		
//	 }
	

     super.doAction(e);
 
     WrProcessMOUtil moUtil = new WrProcessMOUtil();
     moUtil.moCloseProcess(aggWrVOs);

     Object result = getUiService().process(aggWrVOs);
 
    if ((MMValueCheck.isEmpty(result)) || (!(Boolean.parseBoolean(result.toString()))))
       return;
     ShowStatusBarMsgUtil.showStatusBarMsg(WrptLangConst.getProdinHIT_SUCCESSPRODIN(), getModel().getContext());
   }
 
 
 
   protected boolean isActionEnable()
   {
     return super.isActionEnable();
   }
 }
