package nc.bs.jzyy.sys.thlims.mmwrcheck;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.bs.framework.common.NCLocator;
import nc.bs.jzyy.sys.thlims.THLimsLogVO;
import nc.bs.jzyy.sys.thlims.out.AbstractSender4ThLims;
import nc.bs.trade.business.HYPubBO;
import nc.impl.pubapp.pattern.data.bill.BillQuery;
import nc.itf.jzyy.sys.thlims.ISysDispatcherThLims;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.SQLParameter;
import nc.jdbc.framework.processor.MapProcessor;
import nc.vo.bd.defdoc.DefdocVO;
import nc.vo.mmpac.wr.entity.AggWrVO;
import nc.vo.mmpac.wr.entity.WrItemVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSONObject;
/**
 * 1、ERP生产报告调用LIMS回写接口
 * @author yunfeng.li
 */
public class MmwrcheckUpPlugin {
	


	BaseDAO dao;

	public BaseDAO getDao() {
		if (dao == null) {
			dao = new BaseDAO();
		}
		return dao;
	}
	//判断是否同步
	public Object sys(String billltypecode, Object obj,Map<String,Object> otherpms) throws BusinessException{
		AggWrVO newvo = (AggWrVO) obj;
		//1. 校验是否重复报检
		List<WrItemVO> nedchk=new ArrayList<WrItemVO>();
		
		for(WrItemVO itemVO:newvo.getChildrenVO()){
			//1.1 校验是否免检 或者 直接下一步物料不可报检
			boolean chekQC = this.chekQC(newvo.getParentVO().getPk_org(),itemVO.getCbmaterialid());
			if(!chekQC){
				nedchk.add(itemVO);
			}else{
				throw new BusinessException("免检/直接下一步物料不可报检!");
			}
			//1.2 重复报检
			String vbdef11 = itemVO.getVbdef11();//LIMS报检单号
			if(StringUtils.isNotEmpty(vbdef11)){
				throw new BusinessException("同步LIMS失败:行:"+itemVO.getVbrowno()+"已报检,不允许重复报检!");
			}
			
		}
		//2. 报检行数为空 返回当前VO
		if(nedchk.size()==0){
			return newvo;
		}
		/*
		 * 执行同步
		 * 1. 根据物料+批次号 汇总数量报检
		 * 2. 如果选中多行 进行逐条调用 2022年9月23日
		 * */
		Map<String,WrItemVO> groupMap=new HashMap<String, WrItemVO>();
		//1. 同物料 同批次下 会有多个包装规格的情况 此处在合并前 就计算辅数量 2023年4月3日
		for (WrItemVO bvo : nedchk) {
			if(StringUtils.isNotEmpty(bvo.getVbfree2())){
				//bvo.getVbfree2() 标准包装规格
				DefdocVO defdocVO=(DefdocVO) this.getPubBO().queryByPrimaryKey(DefdocVO.class, bvo.getVbfree2());
				 if(null!=defdocVO && StringUtils.isNotEmpty(defdocVO.getDef2())){
					 if(StringUtils.equalsIgnoreCase(defdocVO.getDef2(), "0")){
						
					 }else{
						 //数量向上取整2023年4月3日 计算包装数量
						 UFDouble nbwrastnum=bvo.getNbwrnum().div(new UFDouble(defdocVO.getDef2())).setScale(0, UFDouble.ROUND_UP);
						 bvo.setNbwrastnum(nbwrastnum);
					 }
					
				 }
			}
		}
		//2. 物料+生产批次号汇总
		for (WrItemVO bvo : nedchk) {
			String key=bvo.getCbmaterialid()+bvo.getVbbatchcode();
			if(groupMap.containsKey(key)){
				WrItemVO itemVO = groupMap.get(key);
				itemVO.setNbwrnum(itemVO.getNbwrnum().add(bvo.getNbwrnum()));
				itemVO.setNbwrastnum(itemVO.getNbwrastnum().add(bvo.getNbwrastnum()));
			}else{
				groupMap.put(key, bvo);
			}
		}
		//3. 执行同步
		//逐行请求
		for (WrItemVO bvo : groupMap.values()) {
			newvo.setChildrenVO(new WrItemVO[] {bvo});
			process(billltypecode, newvo, otherpms);
		}
		//重新查询
		BillQuery<AggWrVO> qry = new BillQuery<AggWrVO>(AggWrVO.class);
		newvo=qry.query(new String[]{newvo.getPrimaryKey()})[0];
		return newvo;
	}
	private Object process(String billltypecode, Object obj,Map<String,Object> otherpms) throws BusinessException{
		AbstractSender4ThLims sender = new MmwrcheckUpSender();
		if(otherpms == null )
			otherpms = new HashMap<String, Object>();
		JSONObject resp=null;
		try {
			resp = (JSONObject) sender.process(billltypecode, obj, otherpms);
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessException(e.getMessage());
		}
		//记录报检日志
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
	
	// 检查物料是否免检
	public boolean chekQC(String pk_org, String material)
			throws BusinessException {
		//增加是否下一步物料校验
		this.isNextMater(material);
		
		String sql = " select chkfreeflag    from bd_materialstock where pk_material='"
				+ material + "' and   pk_org ='" + pk_org + "' and dr=0";

		HashMap<String, Object> hashMap2 = (HashMap<String, Object>) this.getQueryBS().executeQuery(sql, new MapProcessor());

		if (hashMap2 != null && hashMap2.size() > 0) {
			UFBoolean b = UFBoolean.valueOf(hashMap2.get("chkfreeflag").toString());
			return b.booleanValue();
		}
		return false;
	}
	
	private HYPubBO pubBO;
	private HYPubBO getPubBO(){
		if(null==pubBO){
			pubBO=new HYPubBO();
		}
		return pubBO;
	}

}
