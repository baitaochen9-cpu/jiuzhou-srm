package nc.bs.jzqc.labelcontrol.bp;

import java.util.HashSet;
import java.util.Set;

import nc.buzimsg.vo.MsgresRcvConfVO;
import nc.vo.am.common.util.ExceptionUtils;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;

/**
 * 标准单据的BP
 */
public class AceLabelprintCheckBP {

	/**
	 * 校验
	 * 
	 * @param vos
	 * @param script
	 * @return
	 * @throws BusinessException
	 */
	public void checkExistsPower(String pk_org, String transtype,
			SuperVO billVO, String userid) throws BusinessException {

		Set<String> userPkSet = new HashSet();

		MsgresRcvConfVO[] rcvConfs = BuziMsgImplHelper.queryMsgresRcvConfVO(
				"labelcontrol", new String[] {pk_org}, transtype);

		BuziMsgImplHelper.fillUserPkSet(userPkSet, rcvConfs, billVO);
		if (userPkSet != null && userPkSet.size() > 0) {
			if (!userPkSet.contains(userid)) {
				ExceptionUtils
						.asBusinessRuntimeException(new BusinessException(
								"当前用户不能标签打印！"));
			}

		}else{
//			ExceptionUtils
//			.asBusinessRuntimeException(new BusinessException(
//					"没有设置标签打印控制！"));
		}
	}

}
