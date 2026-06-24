package nc.ui.cm.equivrate.action;

import java.awt.event.ActionEvent;

import org.apache.commons.lang3.StringUtils;

import nc.bs.framework.common.NCLocator;
import nc.itf.cm.inprocess.IInprocessBackFlushService2;
import nc.ui.pubapp.uif2app.view.BillForm;
import nc.ui.uif2.NCAction;
import nc.ui.uif2.ShowStatusBarMsgUtil;
import nc.ui.uif2.components.IAutoShowUpComponent;
import nc.ui.uif2.model.AbstractAppModel;
import nc.vo.cm.equivrate.entity.EquivrateAggVO;
import nc.vo.cm.equivrate.entity.EquivrateHeadVO;
import nc.vo.pub.BusinessException;

/**
 * 自动约当
 * @author yunfeng.li
 *
 */
public class AutoEquivrateAction extends NCAction {

    private static final long serialVersionUID = 599022514640769695L;

    /**
     * abstractAppModel
     **/
    private AbstractAppModel model;
    
    private BillForm editor;
     private IAutoShowUpComponent goComponent = null;


    /**
     * 自动约当系数
     */

    public AutoEquivrateAction() {
        super();
        String btnName = "自动约当系数";
        this.setBtnName(btnName);
        this.setCode("zdpdxs");
    }

    @Override
    public void doAction(ActionEvent e) throws Exception {
       
    	EquivrateAggVO inprocessAggVO = (EquivrateAggVO)getEditor().getValue();
    	EquivrateHeadVO headVO = (EquivrateHeadVO)inprocessAggVO.getParentVO();
    	String pk_org=headVO.getPk_org();
		String  period=headVO.getCperiod();
		if(StringUtils.isEmpty(period)){
			throw new BusinessException("请选择会计期间");
		}
    	IInprocessBackFlushService2 pullDataService=NCLocator.getInstance().lookup(IInprocessBackFlushService2.class);
    	EquivrateAggVO[] aggVOs = pullDataService.autoEquivrate(pk_org, period);
        ShowStatusBarMsgUtil.showStatusBarMsg("自动约当完成", getModel().getContext());
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