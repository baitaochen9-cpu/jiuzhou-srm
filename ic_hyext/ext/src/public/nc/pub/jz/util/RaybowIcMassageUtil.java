package nc.pub.jz.util;

import nc.message.util.MessageCenter;
import nc.message.vo.MessageVO;
import nc.message.vo.NCMessage;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDateTime;

public class RaybowIcMassageUtil {

	/**
	 * yezhian
	 * @param nc_sender 发送人
	 * @param user  接收人
	 * @param subject 标题
	 * @param content  内容
	 * @throws Exception
	 */
	public static void sedMassage(String nc_sender ,String user,String subject,String content) throws Exception {
		
	    //String nc_sender ="NC_USER0000000000000";	  
	      NCMessage ncMsg = new NCMessage();
	      MessageVO messageVO = new MessageVO();
	      messageVO.setMsgtype("nc");
	      messageVO.setSender(nc_sender);
	      messageVO.setContenttype("text/plain");
	      messageVO.setDestination("outbox");
	      messageVO.setDetail(null);
	      messageVO.setIsdelete(UFBoolean.FALSE);
	      messageVO.setIsread(UFBoolean.FALSE);
	      messageVO.setExpiration(null);
	      messageVO.setSendtime(new UFDateTime());
	      messageVO.setSendstate(UFBoolean.TRUE);
	      messageVO.setPriority(10);
	      messageVO.setSubject(subject);/*标题*/
	      messageVO.setContent(content);/*内容*/
	      messageVO.setReceiver(user);/**/
	      messageVO.setMsgsourcetype("notice");//通知(notice)、预警、工作任务
	      ncMsg.setMessage(messageVO);
	      MessageCenter.sendMessage(new NCMessage[] {      ncMsg    });
	   
	}
}
