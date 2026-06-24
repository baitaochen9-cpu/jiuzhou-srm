/** <a href="http://www.cpupk.com/decompiler">Eclipse Class Decompiler</a> plugin, Copyright (c) 2017 Chen Chao. **/
package nc.ui.ic.special.action;
 import java.awt.event.ActionEvent;

import nc.ui.ic.pub.action.SaveAction;
import nc.ui.pubapp.uif2app.model.BillManageModel;

 public class SpecialSaveAction  extends SaveAction
 {
   private static final long serialVersionUID = 8469585961052246358L;

 
 @Override
	public void doAction(ActionEvent e) throws Exception {
		// TODO Auto-generated method stub
		super.doAction(e);
		SpecialRefreshCardAction refreshaction=new SpecialRefreshCardAction();
		//传数据给这个按钮
		refreshaction.setModel(this.getModel());
		//新增一个事件,模拟这个按钮事件
		ActionEvent e1=new ActionEvent(refreshaction, 1001, "刷新");
		//执行这个按钮的doAction方法，把这个模拟事件传进去
		refreshaction.doAction(e1);
	}
 
   public void setModel(BillManageModel model)
   {
     super.setModel(model);
     model.addAppEventListener(this);
   }


   
 
 }
