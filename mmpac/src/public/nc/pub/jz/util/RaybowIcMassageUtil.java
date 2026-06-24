package nc.pub.jz.util;

import nc.message.util.MessageCenter;
import nc.message.vo.MessageVO;
import nc.message.vo.NCMessage;
import nc.uap.lfw.core.constants.AppConsts;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pubapp.AppContext;

public class RaybowIcMassageUtil {

	/**
	 * 
	 * @param nc_sender 发送人
	 * @param user  接收人
	 * @param subject 标题
	 * @param content  内容
	 * @param billid  单据ID
	 * @param billtype 单据类型
	 * @throws Exception
	 */
	public static void sedMassage(String nc_sender ,String user,
			String subject,String content
			,String billid,String billtype) throws Exception {
		
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
	      messageVO.setBillid(billid);
	      messageVO.setBilltype(billtype);
	      messageVO.setPk_group(AppContext.getInstance().getPkGroup());
	      ncMsg.setMessage(messageVO);
	      MessageCenter.sendMessage(new NCMessage[] {      ncMsg    });
	   
	}
}
