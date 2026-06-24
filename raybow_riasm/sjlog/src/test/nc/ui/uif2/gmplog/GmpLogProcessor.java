package nc.ui.uif2.gmplog;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.sm.busilog.util.LogConfigServiceFacade;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.rbac.IUserManageQuery_C;
import nc.jdbc.framework.processor.BeanListProcessor;
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
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.uif2.model.AbstractUIAppModel;
import nc.uif.pub.exception.UifException;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.MultiLangText;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.riasm.gmplog.AggGmpLogConfigHvo;
import nc.vo.riasm.gmplog.GmpLogConfigBvo;
import nc.vo.riasm.gmplog.GmpLogConfigHvo;
import nc.vo.riasm.gmplog.GmpLogVO;
import nc.vo.sm.UserVO;
import nc.vo.uif2.LoginContext;

import org.apache.commons.lang3.StringUtils;

public class GmpLogProcessor {
	IUAPQueryBS service = (IUAPQueryBS) NCLocator.getInstance().lookup(
			IUAPQueryBS.class.getName());

	UFDateTime time = new UFDateTime();

	/**
	 * 
	 * @param ae
	 * @param context
	 * @param model
	 * @param signPK
	 *            电子签名的PK
	 * @param signVnote
	 *            电子签名的备注
	 * @param cilentBill
	 *            持久化前VO
	 * @throws BusinessException
	 */
	public void setGmpLog(ActionEvent ae, LoginContext context,
			AbstractUIAppModel model, String signPK, String signVnote,
			Object cilentBill) throws Exception {
		// TODO Auto-generated method stub
		// 持久化之前的VO
		NCObject oldncobj = NCObject.newInstance(cilentBill);

		// 当前数据库保存的VO
		
		String pk_org = null;
		String pk_group = InvocationInfoProxy.getInstance().getGroupId();
		String pk_bill = null;
		IBean bean = oldncobj.getRelatedBean();
		String contextmetid =bean.getID();
		//获取继承的接口
		Map<String, String> bditfmap = getNCObjMap(bean);
		pk_bill =getAttrValue(oldncobj, bditfmap.get("id"),"pk");
		pk_org =getAttrValue(oldncobj, bditfmap.get("pk_org"),"pk");
		IMDPersistenceQueryService qry = NCLocator.getInstance().lookup(IMDPersistenceQueryService.class);
		
		NCObject newncobj = qry.queryBillOfNCObjectByPK(oldncobj.getContainmentObject().getClass(), pk_bill);

	

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
		String cmd = ae.getActionCommand();
		// 快捷键保存操作
		if ("".equalsIgnoreCase(cmd)) {
			cmd = "保存";
		}
		if (StringUtils.isBlank(pk_bill)) {
			cmd = "新增";
		} else {

			if ("保存".equalsIgnoreCase(cmd)) {
				cmd = "修改";
			}
		}
		if ("新增".equalsIgnoreCase(cmd)) {
			GmpLogVO logvo = getLogCommon(oldncobj, context, logConfigHvo,
					bodys);
			// 动作
			logvo.setDef1(cmd);
			// 电子签名备注
			logvo.setChgdesc(signVnote);
			logList.add(logvo);
		} else {
			for (GmpLogConfigBvo body : bodys) {
				UFBoolean isEnable = body.getIsenable() == null ? UFBoolean.FALSE
						: body.getIsenable();
				if (!isEnable.booleanValue()) {
					continue;
				}
				GmpLogVO logvo = getLogCommon(oldncobj, context, logConfigHvo,
						bodys);
				logvo = isNeedLog(body, newncobj, oldncobj, logvo);
				// 如果修改前后的值没有变化,则不记录
				if (logvo == null) {
					continue;
				}
				// 动作
				logvo.setDef1(cmd);
				// 电子签名备注
				logvo.setChgdesc(signVnote);

				logList.add(logvo);
			}
		}
		if (logList.size() > 0) {
			HYPubBO_Client.insertAry(logList.toArray(new GmpLogVO[0]));

		}
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
		Attribute attr = (Attribute) bean.getAttributeByPath(strPath);
		String refBeanID = attr.getDataTypeID();//参照的元数据id
		IBean beanByID = MDBaseQueryFacade.getInstance().getBeanByID(refBeanID);
		// 修改后的值,获取属性的值,枚举类的会返回名称
		String value = getDASAttMultiLangMaintext(ncobj	.getAttributeValueWithEnumName(strPath));
		
		// 如果是参照类型获取参照的名称
		if (attr.getDataType().getTypeType() == IType.REF) {
			 if("code".equalsIgnoreCase(type)){
				 value = getRefCode(beanByID, value);
			 }
			 if("name".equalsIgnoreCase(type)){
				 value = getRefName(beanByID, value);
			 }
		}
		return value;
	}
	

	/**
	 * 查找对应 编辑字段编辑前后的值，如果前后的值一样
	 * 
	 * @param body
	 * @param oldncobj
	 * @param newncobj
	 * @param oldncobj
	 * @param logvo
	 * @param o
	 * @param oldBill
	 * @return
	 * @throws UifException
	 * @throws MetaDataException 
	 */
	private GmpLogVO isNeedLog(GmpLogConfigBvo body, NCObject newncobj,
			NCObject oldncobj, GmpLogVO logvo) throws Exception {
		// TODO Auto-generated method stub
		String strPath = body.getAtrrcode();
	
		String value = getAttrValue(newncobj, strPath,null);
		if (value == null) {
			value = "";
		}
		// 修改前的值
		String valueOld = getAttrValue(oldncobj,strPath,null);
		if (valueOld == null) {
			valueOld = "";
		}
		//如果修改前后的值相同,则返回null
		if (value.equals(valueOld)) {
//			return null;
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
			GmpLogConfigHvo logConfigHvo, GmpLogConfigBvo[] bodys)
			throws Exception {

		String funcode = context.getNodeCode();

		String pk_org = null;
		String pk_group = InvocationInfoProxy.getInstance().getGroupId();
		String busipk = null;
		IBean bean = ncobj.getRelatedBean();
		String contextmetid =bean.getID();
		//获取继承的接口
		Map<String, String> bditfmap = getNCObjMap(bean);
		if(bditfmap!=null){
			busipk =getAttrValue(ncobj, bditfmap.get("id"),null);
			pk_org =getAttrValue(ncobj, bditfmap.get("pk_org"),null);

		}
		 List<String> attstrlist = LogConfigServiceFacade.getInstance().getAttrbuteNamePath(pk_group, contextmetid, false);
		
		// 功能节点信息
		Map<String, String> funcInfor = getFuncInfor(funcode);
		// 登录人信息

		IUserManageQuery_C userQy = NCLocator.getInstance().lookup(
				IUserManageQuery_C.class);
		UserVO user = userQy.getUser(context.getPk_loginUser());

		// 查询启用的子表

		GmpLogVO logvo = new GmpLogVO();

		String busicode = "";
		String businame = "";
		for (GmpLogConfigBvo body : bodys) {
			//元数据的字段编码
			String strPath = body.getAtrrcode();
			UFBoolean iscode = body.getIscode() == null ? UFBoolean.FALSE
					: body.getIscode();
			if (iscode.booleanValue()) {
				busicode += getAttrValue(ncobj, strPath,"code");
			}

			UFBoolean isName = body.getIsname() == null ? UFBoolean.FALSE
					: body.getIsname();
			if (isName.booleanValue()) {
				businame +=getAttrValue(ncobj, strPath,"name");
			}

		}

		// 功能节点编码
		logvo.setFuncode(funcode);
		// 功能节点名称
		logvo.setFunname(funcInfor.get("funname"));
		logvo.setPk_group(pk_group);
		logvo.setPk_org(pk_org);
		// 当前单据PK
		logvo.setBusipk(busipk);
		// 编码
		logvo.setBusicode(busicode);
		// 名称
		logvo.setBusiname(businame);
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

	private String getRefName(IBean bean, String refPk) {
		if(refPk == null){
			return null;
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

		Map<String, String> returnmap = new HashMap();
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
	 * 
	 * @return
	 */
	private CircularlyAccessibleValueObject getHeadVO(Object o) {
		CircularlyAccessibleValueObject headVO = null;
		if (o instanceof AggregatedValueObject) {
			AggregatedValueObject aggvo = (AggregatedValueObject) o;
			headVO = aggvo.getParentVO();
		} else if (o instanceof CircularlyAccessibleValueObject) {
			headVO = (CircularlyAccessibleValueObject) o;
		}
		return headVO;

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
					+ configHvoLis.get(0).getPk_fralog_h() + "'";
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
