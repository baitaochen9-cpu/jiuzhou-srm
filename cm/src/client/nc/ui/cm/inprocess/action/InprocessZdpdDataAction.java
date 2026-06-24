package nc.ui.cm.inprocess.action;

import java.awt.event.ActionEvent;

import nc.bs.framework.common.NCLocator;
import nc.itf.cm.inprocess.IInprocessBackFlushService2;
import nc.ui.pubapp.uif2app.view.BillForm;
import nc.ui.uif2.NCAction;
import nc.ui.uif2.components.IAutoShowUpComponent;
import nc.ui.uif2.model.AbstractAppModel;
import nc.vo.cm.inprocess.entity.InprocessAggVO;

/**
 * 自动盘点
 * 
 * @author:liyf
 */
public class InprocessZdpdDataAction extends NCAction {

    private static final long serialVersionUID = 599022514640769695L;

    /**
     * abstractAppModel
     **/
    private AbstractAppModel model;
    
    private BillForm editor;
     private IAutoShowUpComponent goComponent = null;


    /**
     * 自动盘点服务
     */

    public InprocessZdpdDataAction() {
        super();
        String btnName = "自动盘点";
        this.setBtnName(btnName);
        this.setCode("zdpd");
    }

    @Override
    public void doAction(ActionEvent e) throws Exception {
       
    	InprocessAggVO inprocessAggVO = (InprocessAggVO)getEditor().getValue();
    	IInprocessBackFlushService2 pullDataService=NCLocator.getInstance().lookup(IInprocessBackFlushService2.class);
    	InprocessAggVO[] agg=pullDataService.backFlushProduct(inprocessAggVO);
//    	 this.getEditor().getBillCardPanel().getBillModel()
    	getGoComponent().showMeUp();
//    	model.setUiState(UIState.INIT);
    	model.initModel(agg);
//       ShowStatusBarMsgUtil.showStatusBarMsg("自动盘点完成,请返回列表查看", getModel().getContext());

    }
   
    /**
     * abstractAppModel get方法
     * 
     * @return model
     **/
    public AbstractAppModel getModel() {
        return this.model;
    }

    /**
     * abstractAppModel set方法
     * 
     * @param model AbstractAppModel
     **/
    public void setModel(AbstractAppModel model) {
        this.model = model;
        model.addAppEventListener(this);
    }
    
     public void setEditor(BillForm editor)
     {
        this.editor = editor;
     }
     
       public IAutoShowUpComponent getGoComponent() {
        return this.goComponent;
      }
   
      public void setGoComponent(IAutoShowUpComponent goComponent) {
       this.goComponent = goComponent;
    }
   
 
   

   public BillForm getEditor()
  {
       return this.editor;
  }

}
