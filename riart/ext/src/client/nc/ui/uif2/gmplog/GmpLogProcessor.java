package nc.ui.uif2.gmplog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.itf.bd.pubinfo.IAddressService_C;
import nc.itf.bd.userdefitem.IUserdefitemQryService;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.rbac.IUserManageQuery_C;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapProcessor;
import nc.md.MDBaseQueryFacade;
import nc.md.data.access.DASFacade;
import nc.md.data.access.NCObject;
import nc.md.model.IBean;
import nc.md.model.IBusinessEntity;
import nc.md.model.MetaDataException;
import nc.md.model.impl.Attribute;
import nc.md.model.type.IType;
import nc.md.persist.framework.IMDPersistenceQueryService;
import nc.ui.format.NCFormater;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.uif2.model.AbstractUIAppModel;
import nc.uif.pub.exception.UifException;
import nc.vo.bd.address.AddressFormatVO;
import nc.vo.bd.defdoc.DefdocVO;
import nc.vo.bd.material.MaterialVO;
import nc.vo.bd.supplier.SupplierVO;
import nc.vo.bd.userdefrule.UserdefitemVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.format.DefaultAddressObject;
import nc.vo.pub.format.exception.FormatException;
import nc.vo.pub.lang.MultiLangText;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.riasm.gmplog.AggGmpLogConfigHvo;
import nc.vo.riasm.gmplog.GmpLogConfigBvo;
import nc.vo.riasm.gmplog.GmpLogConfigHvo;
import nc.vo.riasm.gmplog.GmpLogVO;
import nc.vo.sm.UserVO;
import nc.vo.uif2.LoginContext;
import nc.vo.util.CloneUtil;

import org.apache.commons.lang3.StringUtils;


@SuppressWarnings("all")
public class GmpLogProcessor {
	IUAPQueryBS service = (IUAPQueryBS) NCLocator.getInstance().lookup(
			IUAPQueryBS.class.getName());

	UFDateTime time = new UFDateTime();
	
	
	public NCObject qyrNCObj(Object cilentBill) throws Exception{
		if(cilentBill == null){
			return null;
		}
		// 持久化之前的VO
		NCObject newncobj = NCObject.newInstance(cilentBill);	
		String pk_bill = null;
		IBean bean = newncobj.getRelatedBean();
//		String contextmetid =bean.getID();
		//获取继承的接口
		Map<String, String> bditfmap = getNCObjMap(bean);
		pk_bill =getAttrValue(newncobj, bditfmap.get("id"),"pk");
		if(StringUtils.isEmpty(pk_bill)){
			return null;
		}
		//企业地址特殊处理
		
		//
		
		IMDPersistenceQueryService qry = NCLocator.getInstance().lookup(IMDPersistenceQueryService.class);
		// 当前数据库保存的VO
		NCObject oldncobj = qry.queryBillOfNCObjectByPK(newncobj.getContainmentObject().getClass(), pk_bill);
		return oldncobj;
		
	}
	
	public Object  dealCorpAddress( Object cilentBill) throws Exception{
		if(cilentBill instanceof SupplierVO){
			SupplierVO vo = (SupplierVO) cilentBill;
			IAddressService_C service = NCLocator.getInstance().lookup(IAddressService_C.class);
			String pk= vo.getCorpaddress();
			AddressFormatVO[] vos = service.format(new String[]{pk});
			
			DefaultAddressObject addressObj = new DefaultAddressObject();
			addressObj.setCountry(vos[0].getCountry());//国家
			addressObj.setState(vos[0].getState());//省份
			addressObj.setCity(vos[0].getCity());//市
			addressObj.setSection(vos[0].getSection());//区县
			addressObj.setRoad(vos[0].getRoad());//详细地址
			addressObj.setPostcode(vos[0].getPostcode());//邮编
			String value = NCFormater.formatAddress(addressObj).getValue();
			vo.setAttributeValue("corpaddressName", value);
		}	
		
		return cilentBill;
	}

	/**
	 * 
	 * @param ae
	 * @param context
	 * @param model
	 * @param signPK
	 *            电子签名的PK
	 * @param signVnote
	 *            电子签名的备注
	 * @param cilentBill 界面数据
	 * @param oldncobj 持久化前数据库数据 新增保存的时候是null
	 *            
	 * @throws BusinessException
	 */
	public void setGmpLog( LoginContext context,
			AbstractUIAppModel model,HashMap<String ,Object> otherParams) throws Exception {
		
		String cmd = (String) otherParams.get("btnname");
		String signPK =(String) otherParams.get("signpk");
	    String signVnote =(String) otherParams.get("signvnote");
		Object cilentBill =  otherParams.get("bfsavevo");//保存前界面VO
		Object afcilentBill =  otherParams.get("afsavevo");//保存后界面VO
		NCObject newncobj = qyrNCObj(afcilentBill);//保存后ncobj
		
		otherParams.put("afncobj", newncobj);
		NCObject bfncobj =  (NCObject) otherParams.get("bfncobj");//保存前界面VO的NCOBJECT
		if(bfncobj == null){
			bfncobj = newncobj;
		}
		
		if(bfncobj == null){
			return ;
		}
		
	
	
		// TODO Auto-generated method stub
		IMDPersistenceQueryService qry = NCLocator.getInstance().lookup(IMDPersistenceQueryService.class);
		// 持久化之前的界面数据
		NCObject clientNCobj = NCObject.newInstance(cilentBill);	
		
		//生产厂商的修改保存获取界面数据有问题,单据进行处理下
		if("10141247".equalsIgnoreCase(context.getNodeCode())){
			IBean bean = clientNCobj.getRelatedBean();
			//获取继承的接口
			String pkpath = bean.getPrimaryKey().getPKColumn().getName();
			String pk_bill =  getAttrValue(clientNCobj, pkpath,null);
			if(StringUtils.isNotEmpty(pk_bill)){
				cilentBill = bfncobj.getContainmentObject();
				clientNCobj = NCObject.newInstance(cilentBill);
			}
		}
		NCObject parentNCobj  = clientNCobj;
		
		//如果是物料节点，修改库存信息等扩展页签，需要单独处理一下获取的表头物料基本信息
		boolean materialSub = isMaterialSub(context.getNodeCode(), clientNCobj);
		if(materialSub){
			String pk_material  = getAttrValue(clientNCobj, "pk_material","pk");
			parentNCobj = qry.queryBillOfNCObjectByPK(MaterialVO.class , pk_material);
		}
		
		String pk_org = null;
		String pk_group = InvocationInfoProxy.getInstance().getGroupId();
		String pk_bill = null;
		IBean bean = clientNCobj.getRelatedBean();
//		String contextmetid =bean.getID();
		//获取继承的接口
		String pkpath = bean.getPrimaryKey().getPKColumn().getName();
		pk_bill =  getAttrValue(clientNCobj, pkpath,null);
		pk_org =getAttrValue(parentNCobj, "pk_org",null);
		if(StringUtils.isEmpty(pk_org)){
			pk_org = pk_group;
		}
//		Map<String, String> bditfmap = getNCObjMap(parentNCobj.getRelatedBean());
//		pk_bill = getAttrValue(parentNCobj, bditfmap.get("id"),"pk");
		//如果是集团级节点，通过参照映射取到的是pk_group的值
//		pk_org = getAttrValue(parentNCobj, bditfmap.get("pk_org"),"pk");
		
		// 持久化以后,当前数据库保存的VO
	
		
		String funcode = context.getNodeCode();

		// 检查审计日志节点配置, 当前节点是否启用,如果没启用则返回
		AggGmpLogConfigHvo configBill = getGmpLogConfigBill(funcode, pk_org);
		if (configBill == null) {
			return;
		}
		GmpLogConfigHvo logConfigHvo = configBill.getParentVO();
		GmpLogConfigBvo[] bodys = (GmpLogConfigBvo[]) configBill
				.getChildrenVO();
		if (bodys == null || bodys.length == 0) {
			return;
		}
		
		List<GmpLogVO> logList = new ArrayList<GmpLogVO>();
		// 快捷键保存操作
		if ("".equalsIgnoreCase(cmd)) {
			cmd = "保存";
		}
		//如果界面数据主键是null 则认为是新增
		if (StringUtils.isBlank(pk_bill)) {
			cmd = "新增";
		} else {
			if ("保存".equalsIgnoreCase(cmd)) {
				cmd = "修改";
			}
		}
		//获取对象编码和名称：针对一些节点单独处理，因为可能是修改的库存信息页签
		Map<String, String> objInfor = getObjInfor(parentNCobj, context, logConfigHvo, bodys);
		objInfor.put("cmd", cmd);
		objInfor.put("qianming", signVnote);
		//如果是供应商-集团 保存前VO 编码是temp，单独处理一下
		if("10140SUG".equalsIgnoreCase(context.getNodeCode())){
			if(!"删除".equalsIgnoreCase(cmd)){
				String code = getAttrValue(newncobj,"code",null);
				objInfor.put("busicode", code);
			}
			
		}
		//公共信息：
		GmpLogVO logvo = getLogCommon(parentNCobj, context, logConfigHvo,bodys,objInfor);

		//如果新增或者删除,则只记录指令
		if ("新增".equalsIgnoreCase(cmd) || "删除".equalsIgnoreCase(cmd)) {
			logList.add(logvo);
		} else {
			//如果不是物料基本信息：比如是库存信息，则只检查库存信息下的字段
			List<GmpLogConfigBvo> materialSubList = new ArrayList<GmpLogConfigBvo>();
			if(materialSub){
				String beanName = bean.getName();
				for (GmpLogConfigBvo body : bodys) {
					UFBoolean isEnable = body.getIsenable() == null ? UFBoolean.FALSE
							: body.getIsenable();
					if (!isEnable.booleanValue()) {
						continue;
					}
					String strPath = body.getAtrrcode();
					//如果包含.则认为是子表，否在是主表
					String subbeanname =beanName+".";
					//如果不包含. 则认为是表头字段
					if(strPath.indexOf(subbeanname) ==-1){
						continue;
					}
					strPath = strPath.substring(strPath.indexOf('.')+1);
					//设置字段属性 把前缀去掉，当成主字段
					body.setAtrrcode(strPath);
					materialSubList.add(body);
						
				}
			
			}
			if(materialSubList.size() >0){
				bodys = materialSubList.toArray(new GmpLogConfigBvo[0]);
			}
		
			List<GmpLogVO> subLogList = getSubLoglist(bfncobj,newncobj,bodys,logvo); 
			if(subLogList!=null && subLogList.size() >0){
				logList.addAll(subLogList);
			}
		
		}
		if (logList.size() > 0) {
			 HYPubBO_Client.insertAry(logList.toArray(new GmpLogVO[0]));
			
		}
	}
	/**
	 * 判断是否物料-集团节点的扩展页签操作
	 * @param fucnode
	 * @param clientNCobj
	 * @return
	 */
	private boolean isMaterialSub(String fucnode,NCObject clientNCobj){
		boolean isMaterialSub = false;
		if("10140MAG".equalsIgnoreCase(fucnode)){
			String beanName = clientNCobj.getRelatedBean().getName();
			//如果不是物料基本信息：
			if(!"material".equalsIgnoreCase(beanName)){
				isMaterialSub = true;
			}
			
		}
		return isMaterialSub;
	}
	
	
	private List<GmpLogVO> getSubLoglist(NCObject oldncobj,
			NCObject newncobj, GmpLogConfigBvo[] bodys, GmpLogVO logvo) throws Exception {
		
		List<GmpLogVO> logList = new ArrayList<GmpLogVO>();
		IBean bean = newncobj.getRelatedBean();
		//按照表体属性页签,分组
		Map<String,List<GmpLogConfigBvo>>  arrMap = new HashMap<>();
		for (GmpLogConfigBvo body : bodys) {
			UFBoolean isEnable = body.getIsenable() == null ? UFBoolean.FALSE
					: body.getIsenable();
			if (!isEnable.booleanValue()) {
				continue;
			}
			String strPath = body.getAtrrcode();
			//如果包含.则认为是子表，否在是主表
			String subbeanname = "head";
			//如果不包含. 则认为是表头字段
			if(strPath.indexOf(".") ==-1){
				
			}else{
				subbeanname = strPath.substring(0, strPath.indexOf('.'));
			}
			if(!arrMap.containsKey(subbeanname)){
				arrMap.put(subbeanname, new ArrayList<GmpLogConfigBvo>());
			}
			arrMap.get(subbeanname).add(body);				
		}
		//循环每个bean 进行匹配
		for(String subbeanname:arrMap.keySet()){
			List<GmpLogConfigBvo> checkAttrList = arrMap.get(subbeanname);
			//head表头页签
			if("head".equalsIgnoreCase(subbeanname)){
				for(GmpLogConfigBvo body:checkAttrList){
					GmpLogVO needLog = isNeedLog(body, newncobj, oldncobj, logvo);
					//如果不等于 null 则认为值发生了变化
					if(needLog!=null){
						logList.add(needLog);
					}
				}
			}else{
				 NCObject[] newNCObjBodys = (NCObject[])newncobj.getAttributeValue(subbeanname);
				 NCObject[] oldNCObjBodys = (NCObject[])oldncobj.getAttributeValue(subbeanname);

				 Attribute bodyBean = (Attribute) bean.getAttributeByPath(subbeanname);
				 String submetaid = bodyBean.getDataTypeID();
				 IBean subbean = MDBaseQueryFacade.getInstance().getBeanByID(submetaid);
				 String pkpath = subbean.getPrimaryKey().getPKColumn().getName();//表体pk字段
				 if(newNCObjBodys!=null && newNCObjBodys.length > 0){
					 //循环表体 对比每个表体的每个需要审计追踪的属性
					  for (NCObject subncvo : newNCObjBodys) {
						  String pk_body = getDASAttMultiLangMaintext(subncvo.getAttributeValue(pkpath));
						  String rowName = getRowName(subncvo,checkAttrList);
						  Integer dr = (Integer) subncvo.getAttributeValue("dr");
						  if(dr ==1){
							  continue;
						  }
						  //匹配修改前的表体,如果匹配到则认为是行修改,匹配不到则认为行新增
						  NCObject matchedSub = matched(pk_body, pkpath, oldNCObjBodys);							
						  if(matchedSub == null){
							  GmpLogVO needLog = (GmpLogVO) CloneUtil.deepClone(logvo);
							  //行对象
							  needLog.setDef2(rowName);
							  needLog.setDef3("增行");
							  logList.add(needLog);
						  }else{
							  for(GmpLogConfigBvo body:checkAttrList){
									GmpLogVO needLog = isNeedLog(body, subncvo, matchedSub, logvo);
									//如果不等于 null 则认为值发生了变化
									if(needLog!=null){
									  //行对象
									  needLog.setDef2(rowName);
									  needLog.setDef3("修改行");
										logList.add(needLog);
									}
								}
						  }
						  
					  } 
				 }
				//判断删除的行
				  if(oldNCObjBodys!=null && oldNCObjBodys.length > 0){
					  for (NCObject subncvoOld : oldNCObjBodys) {
						  String pk_body = getDASAttMultiLangMaintext(subncvoOld.getAttributeValue(pkpath));
						  //如果是之前删除的不用再次判断
						  Integer dr = (Integer) subncvoOld.getAttributeValue("dr");
						  if(dr == 1){
							  continue;
						  }
						  //匹配修改后的表体 匹配不到则认为行删除
						  NCObject matchedSub = matched(pk_body, pkpath, newNCObjBodys);		
						  //
						  boolean  isDel = false;
				
						  if(matchedSub == null){
							  isDel = true;
						  }else{
							  Integer dr2 = (Integer) matchedSub.getAttributeValue("dr");
							  if(dr2 == 1){
								  isDel = true;
							  }
						  }
						  if(isDel){
							  GmpLogVO needLog = (GmpLogVO) CloneUtil.deepClone(logvo);
								//行对象值
							  String rowName = getRowName(subncvoOld,checkAttrList);
							  needLog.setDef2(rowName);
							  needLog.setDef3("删除行");
							  logList.add(needLog);
						  }
						 
					  }

				  }
				 
			}
		  
			
		}
		
		return logList;
	}

	/**
	 * 根据主键 和 编码 获取匹配到的表体行
	 * @param pkvalue
	 * @param pkpath
	 * @param oldNCObjBodys
	 * @return
	 */
	private NCObject  matched(String pkvalue,String  pkpath, NCObject[] oldNCObjBodys){
		
		  if(oldNCObjBodys ==null || oldNCObjBodys.length == 0){
			  return null;
		  }
		  NCObject matchedSub = null;
		  for (NCObject subncvoOld : oldNCObjBodys) {
			  String pk_bodyOld = getDASAttMultiLangMaintext(subncvoOld.getAttributeValue(pkpath));
			  if(pkvalue.equalsIgnoreCase(pk_bodyOld)){
				  matchedSub = subncvoOld;
			  }
		  }
		  return matchedSub;
	  
	}
	
	
	/**
	 * 表体行的 对象名称
	 * @param subncvo
	 * @param checkAttrList
	 * @return
	 * @throws Exception 
	 */
	private String getRowName(NCObject ncobj,
			List<GmpLogConfigBvo> checkAttrList) throws Exception {
		// TODO 自动生成的方法存根
		//对象名称
		String businame="";
		for(GmpLogConfigBvo body:checkAttrList){
			UFBoolean isName = body.getIsname() == null ? UFBoolean.FALSE
					: body.getIsname();
			if (isName.booleanValue()) {
				String strPath = body.getAtrrcode();
				//如果是子表截取最后的字段作为 attname
				if(strPath.indexOf(".") > -1){
					strPath = strPath.substring(strPath.lastIndexOf('.')+1);
				}				
				businame +=getAttrValue(ncobj, strPath,"name");
			}
		}
		
		
		return businame;
	}

	/**
	 * 
	 * @param ncobj
	 * @param strPath
	 * @param type 如果是参照类型默认返回pk  name=返回名称  code 返回编码
	 * @return
	 * @throws Exception
	 */
	private String getAttrValue(NCObject ncobj,String strPath,String type) throws Exception{
		IBean bean = ncobj.getRelatedBean();
		String value =null;
		if(ncobj.getContainmentObject() instanceof DefdocVO){
			DefdocVO vo = (DefdocVO) ncobj.getContainmentObject();
			value = getDASAttMultiLangMaintext(vo.getAttributeValue(strPath));
		}else{
			value = getDASAttMultiLangMaintext(ncobj.getAttributeValue(strPath));

		}
		
		//如果没有匹配到则返回null
		Attribute attr = (Attribute) bean.getAttributeByPath(strPath);
		if(attr != null){
			if(attr.getDataType().getTypeType() == IType.ENUM){
				if(StringUtils.isNotEmpty(value)){
					// 修改后的值,获取属性的值,枚举类的会返回名称
					 value = getDASAttMultiLangMaintext(ncobj.getAttributeValueWithEnumName(strPath));		
			    }
			}
			// 如果是参照类型获取参照的名称
			if (attr.getDataType().getTypeType() == IType.REF) {
				String refBeanID = attr.getDataTypeID();//参照的元数据id
				IBean beanByID = MDBaseQueryFacade.getInstance().getBeanByID(refBeanID);
				 if("code".equalsIgnoreCase(type)){
					 value = getRefCode(beanByID, value);
				 }
				 if("name".equalsIgnoreCase(type)){
					 value = getRefName(beanByID, value);
				 }
			}
			//自定义项目
			if (attr.getDataType().getTypeType() == IType.TYPE_CUSTOM) {			
				 //如果值不为空,找到对应的自定义项的设置,如果设置的是参照，转换成参照名称返回
				 if(StringUtils.isNotEmpty(value)){
					 IUserdefitemQryService userdefitemQryService = NCLocator.getInstance().lookup(IUserdefitemQryService.class);
					 UserdefitemVO userdefitemVO = userdefitemQryService.qeuryUserdefitemVOByMDPropertyID(attr.getID(), "0001V110000000000FH0");
					 if(userdefitemVO!=null && StringUtils.isNotEmpty(userdefitemVO.getClassid())){
						IBean beanByID = MDBaseQueryFacade.getInstance().getBeanByID(userdefitemVO.getClassid());
						value = getRefName(beanByID, value);
					 }
				 }

			}
		}
		
		

		
		return value;
	}
	
	
	private Map<String,String>  getObjInfor(NCObject ncobj, LoginContext context,
			GmpLogConfigHvo logConfigHvo, GmpLogConfigBvo[] bodys) throws Exception{
		Map<String,String>  objInfor = new HashMap<>();
		String busicode = "";
		String businame = "";
		for (GmpLogConfigBvo body : bodys) {
			//元数据的字段编码
			String strPath = body.getAtrrcode();
			//如果包含. 则认为是子表明细
			if(strPath.indexOf(".") > -1){
				continue;
			}
			//对象编码
			UFBoolean iscode = body.getIscode() == null ? UFBoolean.FALSE
					: body.getIscode();
			if (iscode.booleanValue()) {
				if(busicode.length() >1){
					busicode = busicode+" " +getAttrValue(ncobj, strPath,"code");
				}else{
					busicode = busicode+getAttrValue(ncobj, strPath,"code");
				}
				
			}
			//对象名称

			UFBoolean isName = body.getIsname() == null ? UFBoolean.FALSE
					: body.getIsname();
			if (isName.booleanValue()) {
				if(businame.length() >1){
					businame = businame+" " +getAttrValue(ncobj, strPath,"name");
				}else{
					businame = businame+getAttrValue(ncobj, strPath,"name");

				}

			}

		}
		objInfor.put("busicode", busicode);
//		// 名称
		objInfor.put("businame", businame);

		return objInfor;
	}
	

	/**
	 * 查找对应 编辑字段编辑前后的值，如果前后的值一样
	 * 
	 * @param body
	 * @param oldncobj 修改前的数据库保存的数据
	 * @param newncobj 修改后的数据库保存的数据
	 * @param logvo
	 * @param o
	 * @param oldBill
	 * @return
	 * @throws UifException
	 * @throws MetaDataException 
	 */
	private GmpLogVO isNeedLog(GmpLogConfigBvo body, NCObject newncobj,
			NCObject oldncobj, GmpLogVO commlogvo) throws Exception {
		// TODO Auto-generated method stub
		GmpLogVO logvo = (GmpLogVO) CloneUtil.deepClone(commlogvo);
		String strPath = body.getAtrrcode();
		//如果是子表截取最后的字段作为 attname
		if(strPath.indexOf(".") > -1){
			strPath = strPath.substring(strPath.lastIndexOf('.')+1);
		}
		//
		String refValueType="name";
		
		if("Y".equalsIgnoreCase(body.getDef1())){
			refValueType="code";
		}
		
		String value = getAttrValue(newncobj, strPath,refValueType);
		
		// 修改前的值
		String valueOld = getAttrValue(oldncobj,strPath,refValueType);
		
		//企业地址处理
//		if(strPath.contains("corpaddress")){
//			 ColumnProcessor columnProcessor = new ColumnProcessor();
//			 value = (String) service.executeQuery("select DETAILINFO from bd_address where  dr=0 and   PK_ADDRESS='"+value+"'",columnProcessor);
//			 valueOld = (String) service.executeQuery("select DETAILINFO from bd_address where  dr=0 and   PK_ADDRESS='"+valueOld+"'",columnProcessor);
//		}
		
		if (value == null) {
			value = "";
		}
		
		if (valueOld == null) {
			valueOld = "";
		}
		//如果修改前后的值相同,则返回null,不记录值
		if (value.equals(valueOld)) {
			return null;
		}

		// 字段编码
		logvo.setColumncode(body.getAtrrcode());
		// 字段名称
		logvo.setColumnname(body.getGmpname());
		// 修改前内容
		logvo.setContendold(valueOld.toString());
		// 修改后内容
		logvo.setContendchg(value.toString());
		return logvo;
	}

	private GmpLogVO getLogCommon(NCObject ncobj, LoginContext context,
			GmpLogConfigHvo logConfigHvo, GmpLogConfigBvo[] bodys,Map<String, String> objInfor)
			throws Exception {

		String funcode = context.getNodeCode();
		String pk_org = null;
		String pk_group = InvocationInfoProxy.getInstance().getGroupId();
		String busipk = null;
		IBean bean = ncobj.getRelatedBean();
		String contextmetid =bean.getID();

		String pkpath = bean.getPrimaryKey().getPKColumn().getName();
		busipk =getAttrValue(ncobj, pkpath,null);
		pk_org =getAttrValue(ncobj, "pk_org",null);
		if(StringUtils.isEmpty(pk_org)){
			pk_org = pk_group;
		}
		//如果是批次,九洲药业需要特殊处理.根据物料分类设置不同组织
		if("40010815".equalsIgnoreCase(funcode)){
			String cmaterialvid = getAttrValue(ncobj, "cmaterialvid", null);
			StringBuffer sql = new StringBuffer();
			sql.append(" select pk_org from org_orgs where ");
			sql.append(" isbusinessunit='Y' and code=(");
			sql.append(" select SUBSTR(bd_marbasclass.code,0,2) from bd_material inner join bd_marbasclass on  bd_material.pk_marbasclass =bd_marbasclass.pk_marbasclass    ");
			sql.append(" and pk_material='"+cmaterialvid+"'");
			sql.append(" )");
			String pk_org2 = (String) service.executeQuery(sql.toString(), new ColumnProcessor());
			if(StringUtils.isNotEmpty(pk_org2)){
				pk_org = pk_org2;
			}else{
				pk_org="0001V11000000000374G";
			}

		
		}
	
//		 List<String> attstrlist = LogConfigServiceFacade.getInstance().getAttrbuteNamePath(pk_group, contextmetid, false);
		
		// 功能节点信息
		Map<String, String> funcInfor = getFuncInfor(funcode);
		// 登录人信息

		IUserManageQuery_C userQy = NCLocator.getInstance().lookup(
				IUserManageQuery_C.class);
		UserVO user = userQy.getUser(context.getPk_loginUser());

		// 查询启用的子表

		GmpLogVO logvo = new GmpLogVO();

		// 功能节点编码
		logvo.setFuncode(funcode);
		// 功能节点名称
		logvo.setFunname(funcInfor.get("funname"));
		// 动作
		logvo.setDef1(objInfor.get("cmd"));
		// 电子签名备注
		logvo.setChgdesc(objInfor.get("qianming"));
//		// 对象编码
		logvo.setBusicode(objInfor.get("busicode"));
		// 对象名称
		logvo.setBusiname(objInfor.get("businame"));
		
		
		logvo.setPk_group(pk_group);
		logvo.setPk_org(pk_org);
		// 当前单据PK
		logvo.setBusipk(busipk);
	
		// 当前用户
		logvo.setChguserpk(user.getCuserid());
		logvo.setChgusername(user.getUser_name());
		logvo.setChgusercode(user.getUser_code());

		// 登录的客户端
		logvo.setClientip(InvocationInfoProxy.getInstance().getClientHost());
		// 时间
		logvo.setChgtime(time.toString());
		
		
		return logvo;
	}

	
	/**
	 * 获取参照的名称
	 * @param bean
	 * @param refPk
	 * @return
	 */
	private String getRefName(IBean bean, String refPk) {
		if(refPk == null){
			return null;
		}
		if(bean == null){
			return refPk;
		}
		Object refName = null;
		Map<String, String> submap1 = getNCObjMap(bean);
		//如果是档案类的参照才有name字段
		List<String> listpath = new ArrayList<String> ();
		 if (submap1.get("name") != null) {
			 listpath.add(submap1.get("name"));
		 }
		 if(listpath.size() >0){
			 Map<String, Object> map =  DASFacade.getAttributeValues(bean, refPk, (String[])listpath.toArray(new String[0]));
			 refName = map.get(submap1.get("name"));
		 }
		 if(refName== null){
			 return refPk;
		 }
		 return getDASAttMultiLangMaintext(refName);

	
	}
	
	private String getRefCode(IBean bean, String refPk) {
		if(refPk == null){
			return null;
		}
		Object refName = null;
		Map<String, String> submap1 = getNCObjMap(bean);
		//如果是档案类的参照才有name字段
		List<String> listpath = new ArrayList<String> ();
		 if (submap1.get("code") != null) {
			 listpath.add(submap1.get("code"));
		 }
		 if(listpath.size() >0){
			 Map<String, Object> map =  DASFacade.getAttributeValues(bean, refPk, (String[])listpath.toArray(new String[0]));
			 refName = map.get(submap1.get("code"));
		 }
		 if(refName== null){
			 return refPk;
		 }
		 return getDASAttMultiLangMaintext(refName);

	
	}

	/**
	 * 
	 * 得到参照的映射
	 * @param bean
	 * @return
	 */
	private static Map<String, String> getNCObjMap(IBean bean) {
		Map<String, String> bditfmap = ((IBusinessEntity) bean)
				.getBizInterfaceMapInfo("nc.vo.bd.meta.IBDObject");

		Map<String, String> returnmap = new HashMap<String, String> ();
		if ((bditfmap != null) && (bditfmap.size() > 0)) {
			String codeattr = (String) bditfmap.get("code");
			String nameattr = (String) bditfmap.get("name");

			String pk_org = (String) bditfmap.get("pk_org");

			returnmap.put("code", codeattr);
			returnmap.put("name", nameattr);
			returnmap.put("id", bean.getPrimaryKey().getPKColumn().getName());
			returnmap.put("pk_org", pk_org);
		} else {
			bditfmap = ((IBusinessEntity) bean)
					.getBizInterfaceMapInfo("nc.itf.uap.pf.metadata.IFlowBizItf");

			String codeattr = (String) bditfmap.get("billno");

			String pk_org = (String) bditfmap.get("pkorg");

			returnmap.put("code", codeattr);

			returnmap.put("id", bean.getPrimaryKey().getPKColumn().getName());
			returnmap.put("pk_org", pk_org);
		}
		return returnmap;
	}

	private static String getDASAttMultiLangMaintext(Object o) {
		if (o != null) {
			if ((o instanceof MultiLangText)) {
				MultiLangText mt = (MultiLangText) o;
				return mt.getText();
			}
			return o.toString();
		}

		return null;
	}



	
	/**
	 * 查询功能节点信息
	 * 
	 * @param fucnode
	 * @return
	 * @throws BusinessException
	 */
	private Map<String, String> getFuncInfor(String funcode)
			throws BusinessException {
		String sql = "select orgtypecode, fun_name funname  from sm_funcregister where  funcode ='"
				+ funcode + "'";
		Map<String, String> funcInfor = (Map<String, String>) service
				.executeQuery(sql, new MapProcessor());
		return funcInfor;
	}

	private AggGmpLogConfigHvo getGmpLogConfigBill(String funcode, String pk_org) {

		AggGmpLogConfigHvo bill = new AggGmpLogConfigHvo();
		try {

			StringBuffer strb = new StringBuffer();
			strb.append(" select k.* from riasm_gmplogconfig_H  k where nvl(k.dr,0) = 0 and k.pk_org='"
					+ pk_org + "' and pk_parent='" + funcode + "' ");
			List<GmpLogConfigHvo> configHvoLis = (List<GmpLogConfigHvo>) service
					.executeQuery(strb.toString(), new BeanListProcessor(
							GmpLogConfigHvo.class));
			if (configHvoLis == null || configHvoLis.size() == 0)
				return null;
			// 查询启用的子表
			String sqlb = " select *  from riasm_gmplogconfig_b where nvl(dr,0)=0 and pk_fralog_h='"
					+ configHvoLis.get(0).getPk_fralog_h() + "' order by  rowno";
			List<GmpLogConfigBvo> configBvoLis = (List<GmpLogConfigBvo>) service
					.executeQuery(sqlb.toString(), new BeanListProcessor(
							GmpLogConfigBvo.class));

			if (configBvoLis == null || configBvoLis.size() == 0)
				return null;

			bill.setParent(configHvoLis.get(0));
			bill.setChildrenVO(configBvoLis.toArray(new GmpLogConfigBvo[0]));

		} catch (BusinessException e) {
			e.printStackTrace();
		}

		return bill;
	}
}
