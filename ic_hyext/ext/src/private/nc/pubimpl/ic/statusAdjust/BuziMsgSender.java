package nc.pubimpl.ic.statusAdjust;

import nc.bs.framework.common.NCLocator;
import nc.buzimsg.itf.IBuziMsgSending;
import nc.buzimsg.vo.BuziMsgSendingContext;
import nc.message.vo.MessageVO;
import nc.message.vo.NCMessage;
import nc.vo.ic.m4460.entity.StateAdjustVO;
import nc.vo.ic.m4460.util.ScmSendBuziMsgPara;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.AppContext;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

public class BuziMsgSender {
	public void send(StateAdjustVO vo, String[] orgs, ScmSendBuziMsgPara param) {
		try {
			IBuziMsgSending sender = (IBuziMsgSending) NCLocator.getInstance()
					.lookup(IBuziMsgSending.class);

			NCMessage message = getNCMessage(vo, orgs, param);
			BuziMsgSendingContext context = getContext(vo, orgs, param);
			sender.sendBuziMsg(message, context);
		} catch (BusinessException e) {
			ExceptionUtils.wrappException(e);
		}
	}

	protected BuziMsgSendingContext getContext(StateAdjustVO vo, String[] orgs,
			ScmSendBuziMsgPara param) {
		BuziMsgSendingContext context = new BuziMsgSendingContext();
		context.setBillVO(vo);
		context.setPk_billtypecode("4611");

		context.setMsgrescode(param.getMsgrescode());
		context.setPkorgs(orgs);
		return context;
	}

	protected NCMessage getNCMessage(StateAdjustVO vo, String[] orgs,
			ScmSendBuziMsgPara param) throws BusinessException {
		MessageVO mvo = new MessageVO();
		mvo.setPk_detail(param.getPk_detail());
		mvo.setDetail(param.getDetail());
		mvo.setSender(vo.getBillmaker());

		mvo.setSendtime(AppContext.getInstance().getServerTime());
		mvo.setContenttype(param.getContentType());
		mvo.setMsgsourcetype(param.getMsgSourceType());
		mvo.setPk_group(vo.getPk_group());

		mvo.setPk_org(orgs[0]);
		mvo.setBilltype(param.getBillType());
		mvo.setBillid(vo.getPk_onhanddim());
		NCMessage ncm = new NCMessage();
		ncm.setMessage(mvo);
		return ncm;
	}
}
