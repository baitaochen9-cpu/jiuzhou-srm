package nc.bs.jzyy.sys.thlims.coa;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import nc.bs.framework.common.NCLocator;
import nc.bs.jzyy.sys.thlims.THLimsLogVO;
import nc.bs.jzyy.sys.thlims.out.AbstractSender4ThLims;
import nc.bs.logging.Log;
import nc.bs.trade.business.HYPubBO;
import nc.impl.pubapp.pattern.data.vo.VOUpdate;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.MapProcessor;
import nc.vo.bd.cust.CustomerVO;
import nc.vo.bd.defdoc.DefdocVO;
import nc.vo.bd.material.MaterialVO;
import nc.vo.bd.material.measdoc.MeasdocVO;
import nc.vo.bd.psn.PsndocVO;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.org.OrgVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pubapp.AppContext;
import nc.vo.scmf.ic.mbatchcode.BatchcodeVO;
import nc.vo.sm.UserVO;
import nc.vo.so.m4331.entity.DeliveryBVO;
import nc.vo.so.m4331.entity.DeliveryVO;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class DeliveryCoaSender extends AbstractSender4ThLims {

	//中间平台参数名
	public static final String functype = "nc_th_inspection";
	private DeliveryVO vo;
	Map<String,Object>  otherParam = null;
	@Override
	public void init() throws Exception {
		this.vo=(DeliveryVO)this.getParam();
		limsLogVO=new THLimsLogVO();
		this.otherParam=this.getOtherParam();
	}

	@Override
	protected String getSendJson() throws Exception {
		String jsonObj = changeTOJson(this.vo);
		Log.getInstance("检验Lims").error("检验Lims："+jsonObj);
		return jsonObj;
	}
	private String changeTOJson(DeliveryVO vo) throws BusinessException {
		DeliveryBVO[] bvos = vo.getChildrenVO();
		Set<String> pk_marterial_vs = new HashSet<String>();
		Set<String> pk_measdoc = new HashSet<String>();
		
		for (DeliveryBVO bvo : bvos) {
			if (!StringUtil.isEmpty(bvo.getCmaterialvid())) {
				pk_marterial_vs.add(bvo.getCmaterialvid());
			}

			if (!StringUtil.isEmpty(bvo.getCunitid())) {
				pk_measdoc.add(bvo.getCunitid());
			}

			if (!StringUtil.isEmpty(bvo.getCastunitid())) {
				pk_measdoc.add(bvo.getCastunitid());
			}
		}
		
		Map<String, MaterialVO> mmap = getMaterialVOMap(pk_marterial_vs);

		Map<String, MeasdocVO> memap = getMeasdocVOMap(pk_measdoc);
		
		UserVO uservo = getUserVO(AppContext.getInstance().getPkUser());

		OrgVO orgvo = getOrgVO(vo.getParentVO().getPk_org());
		
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("functype", functype);
		
		JSONObject hjsonObject = new JSONObject();
		
		/**
		 * 1=采购报检
		   2=生产完工报检
		   3=库存报检（复验）
		   4=中控检验
		   5=清洁批报检
		 */
		int ctrantype =1;
		UFDateTime ufDateTime=new UFDateTime();
		//String sysFlowNO = getSysFlowNo();
		String sysFlowNO="COA28_"+vo.getParentVO().getVbillcode()+"_"+ufDateTime.toString(TimeZone.getTimeZone("GMT+08:00"),new SimpleDateFormat("yyyyMMddhhmmss"));
		hjsonObject.put("ID",sysFlowNO);// 同步号
		hjsonObject.put("ctrantype", "COA");// 业务类型
		hjsonObject.put("ts", ufDateTime.toString());// 报检时间

		//物料信息
		MaterialVO materialVO = mmap.get(vo.getChildrenVO()[0].getCmaterialvid());
		hjsonObject.put("material_code", materialVO.getCode());
		hjsonObject.put("material_name", materialVO.getName());
		
		//客户信息
		CustomerVO customerVO=(CustomerVO)this.getPubBO().queryByPrimaryKey(CustomerVO.class, vo.getChildrenVO()[0].getCordercustid());
		hjsonObject.put("Client_id", customerVO.getCode());
		hjsonObject.put("Client_name", customerVO.getName());
		hjsonObject.put("market", "");//质量市场   表体自定义项6
		if(StringUtils.isNotEmpty(vo.getChildrenVO()[0].getVbdef6())){
			DefdocVO zlDefVo = (DefdocVO)this.getPubBO().queryByPrimaryKey(DefdocVO.class, vo.getChildrenVO()[0].getVbdef6());
			if(null!=zlDefVo){
				hjsonObject.put("market",zlDefVo.getCode());
			}
		}
		hjsonObject.put("Orderno", vo.getChildrenVO()[0].getVsrccode());//来源订单号
		hjsonObject.put("vbillcode", vo.getParentVO().getVbillcode());// 来源单据号
		hjsonObject.put("bill_id", vo.getParentVO().getPrimaryKey());// 来源单据ID
		hjsonObject.put("billitem_id", vo.getChildrenVO()[0].getPrimaryKey());// 来源单据明细ID
		
		limsLogVO.setCtrantype("COA");
		limsLogVO.setSys_id(sysFlowNO);
		limsLogVO.setQctime(ufDateTime.toString());
		limsLogVO.setTs(ufDateTime.toString());
		limsLogVO.setVbillid(vo.getParentVO().getPrimaryKey());
		limsLogVO.setVbill_bid(vo.getChildrenVO()[0].getPrimaryKey());
		limsLogVO.setVbillcode(vo.getParentVO().getVbillcode());
		
		hjsonObject.put("Client_contract", "");//客户联系人==收货联系人
		if(StringUtils.isNotEmpty(vo.getChildrenVO()[0].getCreceivepersonid())){
			PsndocVO psndocVO=(PsndocVO)this.getPubBO().queryByPrimaryKey(PsndocVO.class, vo.getChildrenVO()[0].getCreceivepersonid());
			if(null!=psndocVO){
				hjsonObject.put("Client_contract", psndocVO.getName());
			}
		}
		hjsonObject.put("Client_Address", "");//客户地址==收货地址
		if(StringUtils.isNotEmpty(vo.getChildrenVO()[0].getCreceiveaddrid())){
			String q_sql="select ctry.name gj,sheng.name sheng,shi.name shi,xian.name xian,b.detailinfo from bd_address b,bd_countryzone ctry,bd_region sheng,bd_region shi, bd_region xian where b.country = ctry.pk_country and b.province = sheng.pk_region(+) and b.city = shi.pk_region(+) and b.vsection = xian.pk_region(+) and b.pk_address = '"+vo.getChildrenVO()[0].getCreceiveaddrid()+"'";
			
			Map<String,Object> mapvalue = (HashMap<String,Object>)this.getQueryBS().executeQuery(q_sql, new MapProcessor());
			StringBuffer address=new StringBuffer();
			if(null!=mapvalue){
				if(null!=mapvalue.get("gj")){
					address=address.append(mapvalue.get("gj"));
				}
				if(null!=mapvalue.get("sheng")){
					address=address.append(mapvalue.get("sheng"));
				}
				if(null!=mapvalue.get("shi")){
					address=address.append(mapvalue.get("shi"));
				}
				if(null!=mapvalue.get("xian")){
					address=address.append(mapvalue.get("xian"));
				}
				if(null!=mapvalue.get("detailinfo")){
					address=address.append(mapvalue.get("detailinfo"));
				}
			}
			hjsonObject.put("Client_Address", address);
			//AddressDocVO queryAddrDocVOByID = AddrdocPubService.queryAddrDocVOByID(vo.getChildrenVO()[0].getCreceiveaddrid());
			//System.out.println(queryAddrDocVOByID);
		}
		//客户订单号  2023年2月28日 取值表头Vdef14
		hjsonObject.put("Client_Package", "/");
		if(StringUtils.isNotEmpty(vo.getParentVO().getVdef14())){
			hjsonObject.put("Client_Package",vo.getParentVO().getVdef14());
		}
		//订单批次数量-字符类型
		hjsonObject.put("TotalQty", ""+vo.getChildrenVO()[0].getNnum().toDouble()+"");
		
		// 主单位名称
		MeasdocVO mesvo = memap.get(vo.getChildrenVO()[0].getCunitid());
		if (mesvo != null) {
			hjsonObject.put("unit", mesvo.getName());
		}
		//查询包装清单
		String q_plsql="select  so.billno,doc.code,doc.name,doc.def1 as storage_eng  " +
				" from  so_salepacklist so,bd_defdoc doc " +
				" where nvl(so.dr,0)=0 and nvl(doc.dr,0)=0 and  so.def2=doc.pk_defdoc(+) and  so.srcbillid='"+vo.getPrimaryKey()+"' and so.pk_srcmaterial='"+vo.getChildrenVO()[0].getCmaterialid()+"'";
		hjsonObject.put("PLNO","");//包装批
		hjsonObject.put("Storage", "");// 存储条件
		hjsonObject.put("Storage_Eng", "");// 存储条件_eng

		Map<String,Object> mapvalue = (HashMap<String,Object>)this.getQueryBS().executeQuery(q_plsql, new MapProcessor());
		if(null!=mapvalue){
			if(null!=mapvalue.get("billno")){
				hjsonObject.put("PLNO",mapvalue.get("billno"));//包装批
			}
			if(null!=mapvalue.get("name")){
				String value=mapvalue.get("name").toString();
				value = value.substring(value.indexOf(":")+1);//英文
				value = value.substring(value.indexOf("：")+1);//中文

//				hjsonObject.put("Storage",mapvalue.get("name"));// 存储条件
			}
//			hjsonObject.put("Storage_Eng",mapvalue.get("storage_eng"));// 存储条件_eng

		}
		
		hjsonObject.put("vbatchcode", "");
		hjsonObject.put("MFGdate", "");//生产日期
		hjsonObject.put("RetestDate", "");//复测日期
		if(StringUtils.isNotEmpty(vo.getChildrenVO()[0].getVbatchcode())){
			BatchcodeVO batchcodeVO=(BatchcodeVO)this.getPubBO().queryByPrimaryKey(BatchcodeVO.class, vo.getChildrenVO()[0].getPk_batchcode());
			if(null!=batchcodeVO){
				hjsonObject.put("vbatchcode", batchcodeVO.getVbatchcode());
				hjsonObject.put("MFGdate", batchcodeVO.getDproducedate().toString());
				hjsonObject.put("RetestDate", batchcodeVO.getDvalidate().toString());
			}
		}
		if (uservo != null) {
			hjsonObject.put("LoginBy", uservo.getUser_name());// 发送人
			limsLogVO.setCuserid(AppContext.getInstance().getPkUser());
			limsLogVO.setUsercode(uservo.getUser_code());
			limsLogVO.setUsername(uservo.getUser_name());
		}
		hjsonObject.put("Logindate",new UFDateTime().toString());//发送时间
		limsLogVO.setQctime(ufDateTime.toString());
		if (orgvo != null) {
			hjsonObject.put("org_code", orgvo.getCode());// 所属公司编码
			hjsonObject.put("org_name", orgvo.getName());// 所属公司
			limsLogVO.setPk_org(vo.getParentVO().getPk_org());
			limsLogVO.setPk_org_v(vo.getParentVO().getPk_org_v());
			
		}
		hjsonObject.put("group", "G");// 所属集团
		hjsonObject.put("def1", "");// 预留1
		hjsonObject.put("def2", "");// 预留2
		hjsonObject.put("def3", "");// 预留3
		hjsonObject.put("def4", "");// 预留4
		hjsonObject.put("def5", "");// 预留5
		
		//第三层
		Map<String,Object> data  = getHeadData();
        data.put("targetcode","LIMS-COA"); 
        data.put("targetrule","LIMS_COA");
        data.put("billCode",vo.getParentVO().getVbillcode());
        data.put("data",hjsonObject); 
        
        //组装
	  	String reJson = JSON.toJSONString(data);
	  	return  reJson;
	}
	

	@Override
	protected Object send(String sendJson) throws Exception {
		// 接口url
		String message = invoke(sendJson);
		return message;
	}

	@Override
	protected Object afterSend(JSONObject response) throws Exception {
		String succeed = response.getString("succeed");
		limsLogVO.setResp(succeed);
		if (!"Y".equals(succeed)) {
			Log.getInstance("泰华Lims COA请验回传").error(response.toJSONString());
			throw new BusinessException(response.getString("message"));
		} else {
			String syncid = response.getString("ID");
			
			/*
			 * 1. 如果是多行 报检是单行
			 * */
			//StringBuffer q_sql=new StringBuffer("cdeliveryid='"+vo.getPrimaryKey()+"' and dr=0 and cmaterialid=");
			/*for (DeliveryBVO bvo : vo.getChildrenVO()) {
				q_sql=q_sql.append("'"+bvo.getCmaterialid()+"' ");
				if(StringUtils.isNotEmpty(bvo.getPk_batchcode())){
					q_sql=q_sql.append(" and pk_batchcode='"+bvo.getPk_batchcode()+"'");
				}
				DeliveryBVO[] ItemVOs=(DeliveryBVO[])this.getPubBO().queryByCondition(DeliveryBVO.class, q_sql.toString());
				if(null!=ItemVOs && ItemVOs.length>0){
					for (DeliveryBVO iteBvo : ItemVOs) {
						
						//LIMS检验请求ID
						iteBvo.setVbdef7("Y");
						updateList.add(iteBvo);
					}
				}
			}*/
			//行PK
			Map<String, ArrayList<String>> pkMapList=(Map<String,ArrayList<String>>)this.otherParam.get("pkList");
			//原始VO信息
			DeliveryVO deliveryVO=(DeliveryVO)this.otherParam.get("vo");
			//合并后VO信息
			String key=this.vo.getChildrenVO()[0].getCmaterialid()+this.vo.getChildrenVO()[0].getVbatchcode();
			
			ArrayList<String> pkList=null;
			
			if(pkMapList.containsKey(key)){
				pkList=pkMapList.get(key);
			}
			
			List<DeliveryBVO> updateList=new ArrayList<DeliveryBVO>();
			
			for (DeliveryBVO bvo : deliveryVO.getChildrenVO()) {
				if(!pkList.contains(bvo.getPrimaryKey())){
					continue;
				}
				DeliveryBVO iteBvo=(DeliveryBVO)bvo.clone();
				//LIMS检验请求ID
				iteBvo.setVbdef7("Y");
				updateList.add(iteBvo);
			}
			
			DeliveryBVO[] updateArry = updateList.toArray(new DeliveryBVO[updateList.size()]);
			//1. 更新同步标识
			new VOUpdate().update(updateArry, new String[] {"vbdef7"});
			DeliveryVO newvo = (DeliveryVO) vo.clone();
			newvo.setChildrenVO(updateArry);
		}
		return response;
	}
	
}
