package nc.impl.eom.failure.rule;

import nc.bs.am.framework.action.ActionContext;
import nc.bs.am.framework.action.IRule;
import nc.bs.framework.common.NCLocator;
import nc.cmp.bill.util.SysInit;
import nc.impl.pubapp.pattern.data.vo.VOQuery;
import nc.itf.material.mdm.SendMdmItf;
import nc.message.itf.EmailSendingService;
import nc.message.vo.MessageVO;
import nc.message.vo.NCMessage;
import nc.pub.uap.util.SendMessageUtil;
import nc.vo.aim.equip.EquipHeadVO;
import nc.vo.eom.failure.FailureBodyVO;
import nc.vo.eom.failure.FailureHeadVO;
import nc.vo.eom.failure.FailureVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;

public class AddFailureSendEmilRule implements IRule<FailureVO>{

	@Override
	public void process(ActionContext<FailureVO> paramActionContext,
			FailureVO[] failureVOs) throws BusinessException {
		// TODO Auto-generated method stub
		/*
		 * 单据保存后发送邮件给对应用户
		 * 1\检查业务参数，功能是否关闭，
		 */
		
		if(null == failureVOs || failureVOs.length == 0){
			return;
		}
		
		UFBoolean paraBoolean = SysInit.getParaBoolean((String) failureVOs[0].getParent().getAttributeValue("pk_org"), "EOM06");//功能开启
		
		String paraString = SysInit.getParaString((String) failureVOs[0].getParent().getAttributeValue("pk_org"), "EOM07"); //接收地址
		
		if(!paraBoolean.booleanValue() || null == paraString){
			return;
		}
		StringBuffer massege = new StringBuffer();
		String subject = "";
		VOQuery<EquipHeadVO > equipQuery = new VOQuery<>(EquipHeadVO.class); //资产卡片查询
		
		for(FailureVO failurevo : failureVOs){
			FailureHeadVO parent = (FailureHeadVO) failurevo.getParent();
			subject = "故障记录："+ parent.getBill_code();
			FailureBodyVO[] children = (FailureBodyVO[]) failurevo.getChildren(FailureBodyVO.class  );
			for (FailureBodyVO failureBodyVO : children) {
				EquipHeadVO[] query = equipQuery.query(new String[]{failureBodyVO.getPk_equip()});
				
				massege.append("\n 资产信息" +query[0].getEquip_code()+"  " +
						"  "+ query[0].getEquip_name() +"  " +
								" "+failureBodyVO.getTrouble_location()+"    \n 请及时处理！");
				
			}
		}
		
		SendMessageUtil sendMessageUtil = new SendMessageUtil();
		MessageVO messageVO = sendMessageUtil.getMessageVO(subject,
				massege.toString(), paraString, "1");
		
		NCMessage ncMessage = new NCMessage();
	
		ncMessage.setMessage(messageVO);
		EmailSendingService lookup = NCLocator.getInstance().lookup(EmailSendingService.class);
		try {
			lookup.sendEmail(new NCMessage[]{ncMessage});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}


}
