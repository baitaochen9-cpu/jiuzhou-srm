package nc.bs.jzyy.sys.thlims.pucheck;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.bs.framework.common.NCLocator;
import nc.bs.jzyy.sys.thlims.THLimsLogVO;
import nc.bs.jzyy.sys.thlims.out.AbstractSender4ThLims;
import nc.bs.logging.Log;
import nc.bs.trade.business.HYPubBO;
import nc.impl.pubapp.pattern.data.bill.BillQuery;
import nc.itf.jzyy.sys.lims.LimsLogVO;
import nc.itf.jzyy.sys.thlims.ISysDispatcherThLims;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.SQLParameter;
import nc.jdbc.framework.processor.MapProcessor;
import nc.vo.bd.defdoc.DefdocVO;
import nc.vo.pu.m23.entity.ArriveItemVO;
import nc.vo.pu.m23.entity.ArriveVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSONObject;

/**
 * 泰华Lims
 * ERP采购到货单调用LIMS报检接口
 * @author xuchong
 */
public class PucheckUpPlugin {

	boolean islogg = false;
	LimsLogVO logvo = null;

	BaseDAO dao;

	public BaseDAO getDao() {
		if (dao == null) {
			dao = new BaseDAO();
		}
		return dao;
	}

	// 判断是否同步
	public Object sys(String billltypecode, Object obj,
			Map<String, Object> otherpms) throws BusinessException {
		ArriveVO bill = (ArriveVO)  obj;
		//前台可能没选择行,重新查询
		BillQuery<ArriveVO> qry = new BillQuery<ArriveVO>(ArriveVO.class);
		ArriveVO orgiVO = qry.query(new String[]{bill.getPrimaryKey()})[0];
		//
		ArriveVO newvo = (ArriveVO)  orgiVO.clone();
		//检查免检数据
		newvo = checkMianJian(newvo,otherpms);
		/*如果没有需要同步的,直接返回*/
		if(newvo == null){
			return  bill;
		}
		ArriveItemVO[] bvos = (ArriveItemVO[]) newvo.getBVO();
		
		List<ArriveItemVO> newItemVOs=new ArrayList<ArriveItemVO>();
		for (ArriveItemVO bvo : bvos) {
			String vbdef11 = bvo.getVbdef11();//LIMS报检单号
			if("~".equalsIgnoreCase(vbdef11) || StringUtils.isEmpty(vbdef11)){
				newItemVOs.add(bvo);
			}else{
				continue;
				//throw new BusinessException("同步LIMS失败:行:"+bvo.getCrowno()+"已报检,不允许重复报检");
			}
		}
		if(newItemVOs.size()>0){
			bvos=newItemVOs.toArray(new ArriveItemVO[newItemVOs.size()]);
		}else{
			Log.getInstance("到货报检").error("重新请验,不与LIMS同步!");
			throw new BusinessException("重新请验,不与LIMS同步!");
			//return bill;
		}
		/*
		 * 执行同步
		 * 1. 根据物料+批次号 汇总数量报检
		 * 2. 如果选中多行 进行逐条调用 2022年9月23日
		 * */
		Map<String,ArriveItemVO> groupMap=new HashMap<String, ArriveItemVO>();
		//1. 同物料 同批次下 会有多个包装规格的情况 此处在合并前 就计算辅数量
		for (ArriveItemVO bvo : bvos) {
			if(StringUtils.isNotEmpty(bvo.getVbdef8())){
				 DefdocVO defdocVO=(DefdocVO) this.getPubBO().queryByPrimaryKey(DefdocVO.class, bvo.getVbdef8());
				 if(null!=defdocVO && StringUtils.isNotEmpty(defdocVO.getDef2())){
					//2023年2月4日 向上取整
					UFDouble nastnum= bvo.getNnum().div(new UFDouble(defdocVO.getDef2())).setScale(0, UFDouble.ROUND_HALF_UP); 
					bvo.setNastnum(nastnum);
				 }
			 }
		}
		//2. 物料+批次号汇总
		/*
		 * 2023年3月16日 同物料同批次 合并供应商批次 数量
		 * */
		for (ArriveItemVO bvo : bvos) {
			String key=bvo.getPk_material()+bvo.getVbatchcode();
			if(groupMap.containsKey(key)){
				ArriveItemVO itemVO = groupMap.get(key);
				itemVO.setNnum(itemVO.getNnum().add(bvo.getNnum()));
				itemVO.setNastnum(itemVO.getNastnum().add(bvo.getNastnum()));
				//供应商批次拼接
				itemVO.setBc_vvendbatchcode(itemVO.getBc_vvendbatchcode()+"#"+bvo.getBc_vvendbatchcode());
				//数量拼接
				itemVO.setBc_vdef1(itemVO.getBc_vdef1()+"#"+bvo.getNnum());
			}else{
				bvo.setBc_vdef1(bvo.getNnum().toString());
				groupMap.put(key, bvo);
			}
		}
		
		//逐行请求
		for (ArriveItemVO bvo : groupMap.values()) {
			newvo.setBVO(new ArriveItemVO[] {bvo});
			process(billltypecode, newvo, otherpms);
		}
		 //返回前台刷新
		 orgiVO = qry.query(new String[]{bill.getPrimaryKey()})[0];
		 return orgiVO;

	}
	
	private IUAPQueryBS bs;
	private IUAPQueryBS getQueryBS(){
		if(null==bs){
			bs = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		}
		return bs;
	}
	/*
	 * 是否直接下一步物料校验
	 * 2022年12月11日
	 * */
	private void isNextMater(String pk_material) throws BusinessException{
		String q_sql="select  blist.code,mater.pk_material,def.code,def.name,def.enablestate from  bd_defdoclist blist,bd_defdoc def,bd_material mater " +
				"where blist.code='H3010125' and def.pk_defdoclist=blist.pk_defdoclist and def.dr=0 and def.enablestate=2 " +
				"and def.code=mater.code and mater.dr=0 and mater.enablestate=2 and mater.pk_material=?";

		SQLParameter parameter=new SQLParameter();
		parameter.addParam(pk_material);
		HashMap<String, Object> hashMap = (HashMap<String, Object>) this.getQueryBS().executeQuery(q_sql,parameter ,new MapProcessor());
		
		if (hashMap != null && hashMap.size() > 0) {
			throw new BusinessException("该物料："+hashMap.get("code")+" 在直接下一步物料档案中,不可报检!");
		}
	}
	
	
	private ArriveVO checkMianJian(ArriveVO vo,Map<String, Object> otherpms) throws BusinessException{
		String pk_org = vo.getHVO().getPk_org();
	   	 ArriveItemVO[] bvos = vo.getBVO();
	     //如果是外系统质检
	  	 List<ArriveItemVO> reList = new ArrayList<ArriveItemVO>();
	  	boolean  shoudong = "手动报检".equalsIgnoreCase((String)otherpms.get("opetype"));
	  		for (ArriveItemVO itemVO : bvos) {
	  			if(itemVO == null){
	  				continue;
	  			}
	  			String material = itemVO.getPk_material();
//	  			UFDouble naccumstorenum = itemVO.getNaccumstorenum();
//	  			if (naccumstorenum != null
//	  					&& naccumstorenum.compareTo(UFDouble.ZERO_DBL) > 0) {
//	  				if(shoudong){
//			  			throw new BusinessException("已入库,不需要报检.请检查后重新选择数据！");
//	  				}else{
//		  				continue;
//	  				}	  			
//	  			}
	  			UFDouble naccumchecknum = itemVO.getNaccumchecknum();
	  			if (naccumchecknum  !=null && naccumchecknum.compareTo(UFDouble.ZERO_DBL) > 0){
	  				if(shoudong){
			  			throw new BusinessException("已报检,不需要报检.请检查后重新选择数据！");
	  				}else{
		  				continue;
	  				}
	  			}
	  		
	  			if (chekQC(pk_org, material)) {
	  				continue;
	  			}
  				reList.add(itemVO);
	  		}
	  		if (reList == null || reList.size() == 0) {
	  			if(shoudong){
		  			throw new BusinessException("全部是免检物料,不需要报检.请检查后重新选择数据！");
				}else{
					return null;
				}
	  		}
	  		vo.setBVO(reList.toArray(new  ArriveItemVO[0] ));
	  		return vo;
	}

	// 检查物料是否免检
	public boolean chekQC(String pk_org, String material)
			throws BusinessException {
		//是否直接下一步物料校验
		this.isNextMater(material);
		
		String sql = " select chkfreeflag from bd_materialstock where pk_material='"
				+ material + "' and   pk_org ='" + pk_org + "' and dr=0";

		HashMap<String, Object> hashMap2 = (HashMap<String, Object>) this.getQueryBS().executeQuery(sql, new MapProcessor());

		if (hashMap2 != null && hashMap2.size() > 0) {
			UFBoolean b = UFBoolean.valueOf(hashMap2.get("chkfreeflag")
					.toString());
			return b.booleanValue();
		}
		return false;
	}

	private Object process(String billltypecode, Object obj,Map<String, Object> otherpms) throws BusinessException {
		
		AbstractSender4ThLims sender = new PucheckUpSender();
		String pk_log = null;
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
	
	private HYPubBO pubBO;
	private HYPubBO getPubBO(){
		if(null==pubBO){
			pubBO=new HYPubBO();
		}
		return pubBO;
	}

}
