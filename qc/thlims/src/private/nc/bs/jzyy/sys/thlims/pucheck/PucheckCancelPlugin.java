package nc.bs.jzyy.sys.thlims.pucheck;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.bs.trade.business.HYPubBO;
import nc.impl.pubapp.pattern.data.bill.BillQuery;
import nc.impl.pubapp.pattern.data.vo.VODelete;
import nc.impl.pubapp.pattern.data.vo.VOUpdate;
import nc.vo.pu.m23.entity.ArriveBbVO;
import nc.vo.pu.m23.entity.ArriveItemVO;
import nc.vo.pu.m23.entity.ArriveVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;

/**
 * 1、ERP采购到货单调用LIMS报检撤销接口
 * 
 * @author yunfeng.li
 * 
 */
public class PucheckCancelPlugin {


	BaseDAO dao;

	public BaseDAO getDao() {
		if (dao == null) {
			dao = new BaseDAO();
		}
		return dao;
	}

	/**
	 * 判断是否同步
	 * 
	 * 直接将报检数量更新为0
	 * @param billltypecode
	 * @param obj
	 * @param otherpms
	 * @return
	 * @throws BusinessException
	 */
	public Object sys(String billltypecode, Object obj,
			Map<String, Object> otherpms) throws BusinessException {
		ArriveVO bill = (ArriveVO)  obj;
		//前台可能没选择行,重新查询
		BillQuery<ArriveVO> qry = new BillQuery<ArriveVO>(ArriveVO.class);
		ArriveVO orgiVO = qry.query(new String[]{bill.getPrimaryKey()})[0];
		ArriveVO newvo = (ArriveVO)  orgiVO.clone();
		ArriveItemVO[] bvos = (ArriveItemVO[]) newvo.getBVO();
		
		HYPubBO hyPubBO = new HYPubBO();
		List<ArriveBbVO> listBbVOs=new ArrayList<ArriveBbVO>();
		for (ArriveItemVO bvo : bvos) {
			/*String vbdef11 = bvo.getVbdef11();//LIMS报检单号
			if("~".equalsIgnoreCase(vbdef11) || StringUtils.isEmpty(vbdef11)){
				throw new BusinessException("未通过LIMS报检,不需要撤销");
			}*/
			 //累计报检主数量 
			bvo.setNaccumchecknum(UFDouble.ZERO_DBL); 
			
			 //2. 删除报检数据
			ArriveBbVO[] bbVOs=(ArriveBbVO[])hyPubBO.queryByCondition(ArriveBbVO.class, "pk_arriveorder_b='"+bvo.getPrimaryKey()+"' and dr=0");
			
			for (ArriveBbVO arriveBbVO : bbVOs) {
				arriveBbVO.setStatus(VOStatus.UPDATED);
				arriveBbVO.setDr(1);
				arriveBbVO.setTs(new UFDateTime());
				listBbVOs.add(arriveBbVO);
			}
		}
		 //1. 更新报检数量
		new VOUpdate().update(bvos, new String[] {"naccumchecknum"});
		//2.删除报检子表
		 if(listBbVOs.size()>0){
			 new VODelete().delete(listBbVOs.toArray(new ArriveBbVO[listBbVOs.size()]));
		 }
		 //返回前台刷新
		 orgiVO = qry.query(new String[]{bill.getPrimaryKey()})[0];
		 return orgiVO;
	}

	private Object process(String billltypecode, Object obj,
			Map<String, Object> otherpms) throws BusinessException {
		return null;
	}

}
