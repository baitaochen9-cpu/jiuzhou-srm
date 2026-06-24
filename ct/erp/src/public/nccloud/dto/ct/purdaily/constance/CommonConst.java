package nccloud.dto.ct.purdaily.constance;

import java.util.HashMap;

import nc.vo.ct.purdaily.entity.CtPaymentVO;
import nc.vo.ct.purdaily.entity.CtPuBVO;
import nc.vo.ct.purdaily.entity.CtPuChangeVO;
import nc.vo.ct.purdaily.entity.CtPuExecVO;
import nc.vo.ct.purdaily.entity.CtPuExpVO;
import nc.vo.ct.purdaily.entity.CtPuMemoraVO;
import nc.vo.ct.purdaily.entity.CtPuTermVO;
import nc.vo.pub.pf.workflow.IPFActionName;

/**
 * @description 采购合同维护常量
 * @author xiahui
 * @date 创建时间：2019-1-15 上午11:14:30
 * @version ncc1.0
 **/
public class CommonConst {
	/**
	 * 列表模板编码
	 */
	public static final String PAGECODE_LIST = "400400604_list";
	/**
	 * 卡片模板编码
	 */
	public static final String PAGECODE_CARD = "400400604_card";

	/**
	 * 批量处理返回结果Key
	 */
	public static final String SUCCESSKEY = "successKey";

	/**
	 * 动作脚本actionNames
	 */
	public static final String[] actionNames = new String[] { IPFActionName.SAVE, IPFActionName.APPROVE, "UNSAVEBILL",
			IPFActionName.UNAPPROVE };

	public static final HashMap<String, String> bodyPkFields = new HashMap<String, String>();
	static {
		bodyPkFields.put("body", CtPuBVO.PK_CT_PU_B);
		bodyPkFields.put("contractterm", CtPuTermVO.PK_CT_PU_TERM);
		bodyPkFields.put("payagree", CtPaymentVO.PK_CT_PU_PAYMENT);
		bodyPkFields.put("contractfee", CtPuExpVO.PK_CT_PU_EXP);
		bodyPkFields.put("contractmemora", CtPuMemoraVO.PK_CT_PU_MEMORA);
		bodyPkFields.put("changehistory", CtPuChangeVO.PK_CT_PU_CHANGE);
		bodyPkFields.put("executeprocess", CtPuExecVO.PK_CT_PU_EXEC);
	}

}
