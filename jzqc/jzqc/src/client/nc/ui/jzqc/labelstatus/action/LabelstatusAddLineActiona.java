package nc.ui.jzqc.labelstatus.action;

import nc.ui.pubapp.uif2app.actions.batch.BatchAddLineAction;
import nc.vo.jzqc.labelstatus.LabelStatusVO;
import nc.vo.pub.lang.UFDateTime;
/**
  batch addLine or insLine action autogen
*/
public class LabelstatusAddLineActiona extends BatchAddLineAction {

	private static final long serialVersionUID = 1L;

	@Override
	protected void setDefaultData(Object obj) {
		super.setDefaultData(obj);
		LabelStatusVO singleDocVO = (LabelStatusVO) obj;
		singleDocVO.setAttributeValue("pk_group", this.getModel().getContext().getPk_group());
		singleDocVO.setAttributeValue("pk_org", this.getModel().getContext().getPk_org());
		singleDocVO.setAttributeValue("creationtime",
				new UFDateTime(System.currentTimeMillis()));
		singleDocVO.setAttributeValue("creator",  this.getModel().getContext().getPk_loginUser());
		singleDocVO.setAttributeValue("maketime",
				new UFDateTime(System.currentTimeMillis()));
	}

}