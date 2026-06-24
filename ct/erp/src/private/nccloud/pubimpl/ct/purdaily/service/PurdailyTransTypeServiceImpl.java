package nccloud.pubimpl.ct.purdaily.service;

import java.util.ArrayList;

import nc.impl.ct.business.BusinessTypeImpl;
import nc.vo.ct.business.entity.BusinessSetVO;
import nc.vo.ct.business.enumeration.Nbusitype;
import nc.vo.platform.workbench.AppRegisterVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.billtype.BilltypeVO;
import ncccloud.pubitf.riart.transtype.INccTranstypeBiz;

/**
 * @description 交易类型扩展服务
 * @author xiahui
 * @date 创建时间：2019-2-19 下午1:40:48
 * @version ncc1.0
 **/
public class PurdailyTransTypeServiceImpl implements INccTranstypeBiz {

	private BusinessTypeImpl bussinessType = new BusinessTypeImpl();
	
	@Override
	public void saveTransType(Object userObj, BilltypeVO transtypevo) throws BusinessException {
		this.fillData((BusinessSetVO)userObj, transtypevo); // 补充数据
		bussinessType.saveTransType(userObj);
	}

	@Override
	public void updateTransType(Object userObj, BilltypeVO transtypevo) throws BusinessException {
		bussinessType.updateTransType(userObj);
	}

	@Override
	public void deleteTransType(Object userObj, BilltypeVO transtypevo) throws BusinessException {
		bussinessType.deleteTransType(userObj);
	}

	@Override
	public void execOnPublish(BilltypeVO transtypevo, String oldApppk, String newApppk, boolean isExecFunc)
			throws BusinessException {
		// 未使用的方法
	}

	@Override
	public void execOnDelPublish(BilltypeVO transTypeVO, ArrayList<AppRegisterVO> appVOs) throws BusinessException {
		// 未使用的方法
	}

	/**
	 * 补全数据
	 * @param vo
	 * @param transTypeVO
	 */
	private void fillData(BusinessSetVO vo, BilltypeVO transTypeVO) {
		if (null == vo.getPk_group()) {
			vo.setPk_group(transTypeVO.getPk_group());
		}
		if (null == vo.getVtrantypecode()) {
			vo.setVtrantypecode(transTypeVO.getPk_billtypecode());
		}
		if (null == vo.getCtrantypeid()) {
			vo.setCtrantypeid(transTypeVO.getPk_billtypeid());
		}
		if(null == vo.getNbusitype()) {
			vo.setNbusitype(Nbusitype.CT_PURDAILY.toIntValue());
		}
		if(null == vo.getVtrantypename()) {
			vo.setVtrantypename(transTypeVO.getBilltypename());
		}
	}

}
