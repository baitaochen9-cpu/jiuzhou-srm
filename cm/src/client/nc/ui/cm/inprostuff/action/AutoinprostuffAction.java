package nc.ui.cm.inprostuff.action;

import java.awt.event.ActionEvent;

import nc.bs.framework.common.NCLocator;
import nc.itf.cm.inprocess.IInprocessBackFlushService2;
import nc.ui.pubapp.uif2app.view.BillForm;
import nc.ui.uif2.NCAction;
import nc.ui.uif2.ShowStatusBarMsgUtil;
import nc.ui.uif2.components.IAutoShowUpComponent;
import nc.ui.uif2.model.AbstractUIAppModel;
import nc.vo.cm.inprostuff.entity.InproStuffAggVO;
import nc.vo.cm.inprostuff.entity.InproStuffHeadVO;
import nc.vo.pub.BusinessException;

import org.apache.commons.lang3.StringUtils;

/**
 * 自动材料计算
 * @author yunfeng.li
 *
 */
public class AutoinprostuffAction extends NCAction {

    private static final long serialVersionUID = 599022514640769695L;

    /**
     * abstractAppModel
     **/
    private AbstractUIAppModel model;
    
    private BillForm editor;
     private IAutoShowUpComponent goComponent = null;


    /**
     *自动材料计算
     */

    public AutoinprostuffAction() {
        super();
        String btnName = "自动材料计算";
        this.setBtnName(btnName);
        this.setCode("zdpdxs");
    }

    @Override
    public void doAction(ActionEvent e) throws Exception {
       
    	InproStuffAggVO inprocessAggVO = (InproStuffAggVO)getEditor().getValue();
    	InproStuffHeadVO headVO = (InproStuffHeadVO)inprocessAggVO.getParentVO();
    	String pk_org=headVO.getPk_org();
		String  period=headVO.getCperiod();
		if(StringUtils.isEmpty(period)){
			throw new BusinessException("请选择会计期间");
		}
    	IInprocessBackFlushService2 pullDataService=NCLocator.getInstance().lookup(IInprocessBackFlushService2.class);
    	InproStuffAggVO[] aggVOs = pullDataService.autoInprostuff(pk_org, period);
        ShowStatusBarMsgUtil.showStatusBarMsg("自动材料计算完成", getModel().getContext());
        //切换到列表
		getGoComponent().showMeUp();
		//初始化数据
		getModel().initModel(aggVOs);

    }
   
    /**
     * abstractAppModel get方法
     * 
     * @return model
     **/
    public AbstractUIAppModel getModel() {
        return this.model;
    }

    /**
     * abstractAppModel set方法
     * 
     * @param model AbstractAppModel
     **/
    public void setModel(AbstractUIAppModel model) {
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