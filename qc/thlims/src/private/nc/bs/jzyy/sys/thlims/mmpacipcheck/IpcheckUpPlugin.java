package nc.bs.jzyy.sys.thlims.mmpacipcheck;

import java.util.HashMap;
import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.bs.framework.common.NCLocator;
import nc.bs.jzyy.sys.thlims.THLimsLogVO;
import nc.bs.jzyy.sys.thlims.out.AbstractSender4ThLims;
import nc.impl.pubapp.pattern.data.bill.BillQuery;
import nc.itf.jzyy.sys.thlims.ISysDispatcherThLims;
import nc.vo.mmpac.pmo.pac0002.entity.PMOAggVO;
import nc.vo.mmpac.pmo.pac0002.entity.PMOItemVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSONObject;
/**
 * 1、ERP流程生产订单调用LIMS报检接口
 * @author yunfeng.li
 *
 */
public class IpcheckUpPlugin {
	
	BaseDAO dao;

	public BaseDAO getDao() {
		if (dao == null) {
			dao = new BaseDAO();
		}
		return dao;
	}
	//判断是否同步
	public Object sys(String billltypecode, Object obj,Map<String,Object> otherpms) throws BusinessException{
		PMOAggVO bill = (PMOAggVO)obj;
		//前台可能没选择行,重新查询
		BillQuery<PMOAggVO> qry = new BillQuery<PMOAggVO>(PMOAggVO.class);
		PMOAggVO orgiVO = qry.query(new String[]{bill.getPrimaryKey()})[0];
		//
		PMOAggVO newvo = (PMOAggVO)  orgiVO.clone();
		PMOItemVO[] bvos = (PMOItemVO[]) newvo.getChildrenVO();
		for (PMOItemVO bvo : bvos) {
			String vbdef11 = bvo.getVdef11();//LIMS报检单号
			if("~".equalsIgnoreCase(vbdef11) || StringUtils.isEmpty(vbdef11)){
				continue;
			}else{
				throw new BusinessException("同步LIMS失败:行:"+bvo.getVrowno()+"已报检,不允许重复报检");
			}
		}
		
		//执行同步
		 process(billltypecode, orgiVO, otherpms);
		 //返回前台刷新
		 orgiVO = qry.query(new String[]{bill.getPrimaryKey()})[0];
		 return orgiVO;
	}
	
	private Object process(String billltypecode, Object obj,Map<String,Object> otherpms) throws BusinessException{
		AbstractSender4ThLims sender = new IpcheckUpSender();
		if(otherpms == null ){
			otherpms = new HashMap<String, Object>();
		}
		JSONObject resp=null;
		try {
			resp = (JSONObject) sender.process(billltypecode, obj, otherpms);
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessException(e.getMessage());
		}
		THLimsLogVO limsLogVO = sender.getLimsLogVO();
		if(null!=limsLogVO){
			this.addNewLog(limsLogVO);
		}
		return resp;
	}
	private ISysDispatcherThLims pross;
	private ISysDispatcherThLims getPross() {
		if(pross == null){
			pross = NCLocator.getInstance().lookup(ISysDispatcherThLims.class);
		}
		return pross;
	}
	
	private void addNewLog(SuperVO obj) {
		 try {
			this.getPross().dispatch_RequiresNew(obj, "LIMS_SYS_LOGGER", null);
		} catch (BusinessException e) {
			e.printStackTrace();
		}
	}
	
}
