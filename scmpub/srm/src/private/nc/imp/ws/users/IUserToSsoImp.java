package nc.imp.ws.users;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;


import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.framework.common.NCLocator;
import nc.itf.uap.rbac.IUserManage;
import nc.itf.ws.pub.SeviceTools;
import nc.jdbc.framework.processor.BeanProcessor;
import nc.pubitf.scmpub.api.rest.AbstractSCMPUBResource;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.sm.UserVO;
import net.sf.json.JSONObject;

import org.apache.commons.codec.digest.DigestUtils;
import org.json.JSONString;

@Path("user")
public class IUserToSsoImp  extends AbstractSCMPUBResource {

	@POST
	@Path("send")
	@Consumes({"application/json"})
	@Produces({"application/json"})
	public JSONString sendUser(Object josno) {

		JSONObject jsonobj = JSONObject.fromObject(josno);
		String requestId = (String) jsonobj.get(SeviceTools.REQUESTID);
		
		if (jsonobj != null && jsonobj.size() != 0) {
			
//			JSONObject jsonobj = JSONObject.fromObject(josn);
//			String requestId = (String) jsonobj.get(SeviceTools.REQUESTID);
			String appId = (String) jsonobj.get(SeviceTools.APPID);
			String appKey = (String) jsonobj.get(SeviceTools.APPKEY);
			String actionType = (String) jsonobj.get(SeviceTools.ACTIONTYPE);
			
			JSONObject userObject = JSONObject.fromObject(jsonobj
					.get(SeviceTools.ACCOUNT));
			
			
			boolean validaction = this.validaction(requestId, appId, appKey,
					actionType);

			if (!validaction) {
				return  SeviceTools.getRespRes(requestId, false, "1", "验证不通过"); // 验证不通过，返回失败信息~~~~~~~~~~~~~~~~~~~~~~
			}
			if (null == userObject || userObject.size() == 0) {
				return SeviceTools.getRespRes(requestId, false, "2", "接收用用户数据为空，或者JSON转意失败"); // 接收用用户数据为空，或者JSON转意失败，返回失败信息~~~~~~~~~~~~~~~~~
			}

			// 解析接收的用户数据
			UserVO users = null;
			UserVO user_old = null;
			try {
				users = parsingUserData(userObject);
			} catch (ClassNotFoundException e) {
				return SeviceTools.getRespRes(requestId, false, "2", "解析用户信息失败："+e.getMessage());
			} catch(NumberFormatException e){
				return SeviceTools.getRespRes(requestId, false, "2", "Uid："+e.getMessage());
			}
			IUserManage manage = NCLocator.getInstance().lookup(
					IUserManage.class);
			// 判断执行动作
			switch (actionType) {
			case "Add": // 创建一个新应用账号； HR入职、管理员新增
				try {
					manage.addUser(users);
				} catch (BusinessException e) {
					return SeviceTools.getRespRes(requestId, false, "3", "新增账号失败:"+e.getMessage());
				}
				return SeviceTools.getRespRes(requestId, true, "0", "");
			case "Delete":// 删除一个应用账号(物理删除)； 管理员删除
				try {
					user_old = queryOld(users);
				} catch (DAOException e4) {
					return SeviceTools.getRespRes(requestId, false, "3", "对数据库数据操作失败");
				}//查询
				
				try {
					
					this.userDel(user_old, manage);
					
				} catch (BusinessException e) {
					
					return SeviceTools.getRespRes(requestId, false, "3", "删除账号失败:"+e.getMessage());
				}// 除了新增，其他的都需要通过现有数据为条件查询NC数据进行操作
				return SeviceTools.getRespRes(requestId, true, "0", "");
			case "Modify": //修改一个有效应用账号的可修改属性； HR调整信息（非组织和邮箱）、管理员调整信息（非组织和邮箱）
				try {
					user_old = queryOld(users);
					if(user_old == null){
						return SeviceTools.getRespRes(requestId, false, "3", "对数据库数据查询操作失败");
					}
					user_old.setUser_code(users.getUser_code());	
					String codecPWD = DigestUtils.md5Hex(user_old.getCuserid() + users.getUser_password());
					user_old.setUser_password("U_U++--V"+codecPWD);
					user_old.setUser_name(users.getUser_name());
					user_old.setPk_org(users.getPk_org());
					user_old.setUser_note(users.getUser_note());
					user_old.setEnablestate(users.getEnablestate());
					user_old.setCreationtime(users.getCreationtime());
					user_old.setModifiedtime(users.getModifiedtime());
//			        "orgName":"技术部",
//			        "createDate":"2019-11-28 17:07:44",
//			        "modifyDate":"2019-11-28 19:07:44",
				} catch (DAOException e2) {
					e2.printStackTrace();
				}
				try {
					this.modefyUser(user_old, manage);
				} catch (BusinessException e1) {
					return SeviceTools.getRespRes(requestId, false,"3", "修改账号失败:"+e1.getMessage());
				}
				return SeviceTools.getRespRes(requestId, true, "0", "");
			case "Disable":// 停用一个有效的应用账号； 离职、管理员停用
				try {
					user_old = queryOld(users);
				} catch (DAOException e2) {
					return SeviceTools.getRespRes(requestId, false, "3", "对数据库数据查询操作失败");
				}// 除了新增，其他的都需要通过现有数据为条件查询NC数据进行操作
				try {
					this.disableUser(user_old, manage);
					
				} catch (BusinessException e) {
					
					return SeviceTools.getRespRes(requestId, false, "3", "停用账号失败:"+e.getMessage());
				}
				
				return SeviceTools.getRespRes(requestId, true, "0", "");
			case "Enable":// 启用一个已停用的应用账号； 管理员启用
				try {
					user_old = queryOld(users);
				} catch (DAOException e1) {
					return SeviceTools.getRespRes(requestId, false, "3", "对数据库数据操作失败");
				}// 除了新增，其他的都需要通过现有数据为条件查询NC数据进行操作
				
				try {
					this.enable(user_old,manage);
				} catch (BusinessException e) {
					return SeviceTools.getRespRes(requestId, false, "3", "启用账号失败:"+e.getMessage());
				}
				return SeviceTools.getRespRes(requestId, true, "0", "");

			default:
				return SeviceTools.getRespRes(requestId, false, "0", "没有对应的接口操作定义标识");
			}

		}
		return SeviceTools.getRespRes(requestId, false, "2", "传入数据为空");// 传入数据不能为空，返回失败信息
	}

	/**
	 * 启用
	 * 
	 * @param user_old
	 * @param manage 
	 * @throws BusinessException 
	 */
	private void enable(UserVO user_old, IUserManage manage) throws BusinessException {
		if(null == user_old){
			throw new BusinessException("没有在数据库查到对应数据，请检查Uid是否存在");
		}
		manage.enableUser(user_old);

	}

	/**
	 * 停用
	 * 
	 * @param user_old
	 * @param manage 
	 * @throws BusinessException 
	 */
	private void disableUser(UserVO user_old,IUserManage manage) throws BusinessException {		
		if(null == user_old){
			throw new BusinessException("没有在数据库查到对应数据，请检查Uid是否存在");
		}
		manage.disableUser(user_old);	
          
	}
	
	
	/**
	 * 删除
	 * 
	 * @param users
	 * @throws BusinessException
	 */
	private void userDel(UserVO user_old, IUserManage manage)
			throws BusinessException {
		if(null == user_old){
			throw new BusinessException("没有在数据库查到对应数据，请检查Uid是否存在");
		}	
		user_old.setStatus(VOStatus.DELETED);
		manage.delUser(user_old);
		// TODO 自动生成的方法存根

	}

	/**
	 * 修改
	 * 
	 * @param user_old
	 * @throws BusinessException 
	 */
	private void modefyUser(UserVO user_old,IUserManage manage) throws BusinessException {
		// TODO 自动生成的方法存根
		user_old.setStatus(VOStatus.UPDATED);
		
		manage.updateUser(user_old);
	}

	/**
	 * 查询老数据
	 * 
	 * @param users
	 * @param manage
	 * @return
	 * @throws DAOException 
	 */
	private UserVO queryOld(UserVO users) throws DAOException {
		// TODO 自动生成的方法存根
		String usercode=users.getUser_code();
		String  sql="select * from sm_user where user_code='"+usercode+"'";
		BeanProcessor processer = new BeanProcessor(UserVO.class);
		UserVO uservo=null;
		
        uservo = (UserVO) getDAO().executeQuery(sql, processer);
		
		return uservo;
	}
	private BaseDAO dao=null;
	public BaseDAO getDAO(){
		if(dao==null){
			dao=new BaseDAO();
		}
		return dao;
	}

	

	private UserVO parsingUserData(JSONObject aCCOUNT)
			throws ClassNotFoundException {
		UserVO new_user = new UserVO();
		new_user.setAbledate(new UFDate());
		new_user.setBase_doc_type(0);
		new_user.setContentlang("zhCN0000000000000000");
		if(aCCOUNT.get("createDate")!=null){
			if(!aCCOUNT.getString("createDate").equals("null")){
				new_user.setCreationtime(new UFDateTime(aCCOUNT.getString("createDate")));
			}
		}
		
		new_user.setCreator("00016A10000000002L77");
		new_user.setDirty(false);
		new_user.setUser_name((String) aCCOUNT.get("accountName"));
		new_user.setUser_password(aCCOUNT.getString("accountPwd"));
		new_user.setIdentityverifycode("staticpwd");
		new_user.setUser_type(1);
		new_user.setPk_org((String) aCCOUNT.get("orgId"));
		new_user.setFormat("FMT0Z000000000000000");
		new_user.setIsca(new UFBoolean(false));
		new_user.setIsLocked(new UFBoolean(false));
		if(aCCOUNT.get("modifyDate")!=null){
			if(!aCCOUNT.getString("modifyDate").equals("null")){
				new_user.setModifiedtime( new UFDateTime(aCCOUNT.getString("modifyDate")));
			}
		}
		//new_user.setModifiedtime(aCCOUNT.get("modifyDate").equals(null) ? null : new UFDateTime(aCCOUNT.getString("modifyDate")));
		new_user.setModifier(null);
		new_user.setPk_group("00016A10000000000ERD");
		new_user.setPk_usergroupforcreate("1001V110000000000EM8");
		new_user.setSystype("1");
		if(aCCOUNT.get("desc")!=null){
			
				new_user.setUser_note(aCCOUNT.getString("desc"));
			
		}
		//new_user.setUser_note(aCCOUNT.get("desc").equals(null) ? null : aCCOUNT.getString("desc"));
		
		try{
		Object o=aCCOUNT.get("status");
		if(o!=null){
				new_user.setEnablestate(Integer.valueOf((String) o));	
		}
		}catch(NumberFormatException e){
			throw new NumberFormatException("传入不是数字");
		}
		String b=(String) aCCOUNT.get("accountNo");
		new_user.setUser_code((String) aCCOUNT.get("Uid"));
		

		return new_user;
	}

	/**
	 * 接口头信息验证
	 * 
	 * @param requestId
	 *            //在API服务接收客户端调用时，通过传递过来的参数应用注册编码（appId）
	 *            与应用系统中配置的统一身份管理认证系统的注册应用编码进行校验，仅允许注册编码一致可以调用。
	 * @param appId
	 *            //集成的应用系统在统一身份管理认证系统注册的编码
	 * @param appKey
	 *            //集成的应用系统在统一身份管理认证系统注册的KEY
	 * @param actionType
	 *            //集成的应用系统在统一身份管理认证系统注册的KEY
	 * @return
	 */
	private boolean validaction(String requestId, String appId, String appKey,
			String actionType) {
		// TODO 自动生成的方法存根
		return true;
	}



}
