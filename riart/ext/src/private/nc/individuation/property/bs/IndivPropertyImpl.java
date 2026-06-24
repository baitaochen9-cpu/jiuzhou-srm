package nc.individuation.property.bs;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.logging.Logger;
import nc.bs.ml.NCLangResOnserver;
import nc.bs.uap.lock.PKLock;
import nc.individuation.property.itf.IPropertyService;
import nc.individuation.property.vo.IndividualPropertyVO;
import nc.jdbc.framework.SQLParameter;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.jdbc.framework.processor.ResultSetProcessor;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.lang.UFTime;

/**
 * @author gaijf
 * @date 2009-6-9
 * 个性化属性服务实现类
 */
public class IndivPropertyImpl implements IPropertyService{
	BaseDAO dao = new BaseDAO();
	@Override
	/**
	 * @param pageID
	 * @param pk_user
	 * @param pk_group
	 * @return
	 * @throws DAOException
	 * 查找入口页面pageID;用户（pk_user）在集团(pk_group)下关联的所有属性的集合
	 * 同时根据属性类型字符串，将属性值封装成对应类型
	 */
	@SuppressWarnings("unchecked")
	public IndividualPropertyVO[] queryPropertyVOs(final String pageID,
			final String pk_user, final String pk_group) throws DAOException {
		StringBuffer sql = new StringBuffer("select pk_property, propertyname, value, type ,seq from sm_individual_property "
				+ "where pageid = ? ");//and pk_user=? and pk_group=? ";
		
		SQLParameter parameter = new SQLParameter();
		parameter.addParam(pageID);
		if(pk_user == null){
			sql.append(" and pk_user ='~' ");
		}else{			
			sql.append( " and pk_user=? ");
			parameter.addParam(pk_user);
		}
		if(pk_group == null){
			sql.append( " and pk_group ='~' ");
		}else{
			sql.append( " and pk_group=? ");
			parameter.addParam(pk_group);
		}
					
		List<IndividualPropertyVO> vos = (List<IndividualPropertyVO>) new BaseDAO()
				.executeQuery(sql.toString(), parameter, new ResultSetProcessor() {
					private static final long serialVersionUID = 2395823993172495213L;

					@SuppressWarnings("deprecation")
					public Object handleResultSet(ResultSet rs)
							throws SQLException {
						List<IndividualPropertyVO> list = new ArrayList<IndividualPropertyVO>();
						while (rs != null && rs.next()) {
							IndividualPropertyVO property = new IndividualPropertyVO();
							property.setPageID(pageID);
							property.setPk_group(pk_group);
							property.setPk_user(pk_user);
							property.setPk_property(rs.getString("pk_property"));
							property.setPropertyname(rs
									.getString("propertyname"));
							String type = rs.getString("type");
							property.setType(type);
							String sequence=rs.getString("seq");
							property.setSeq(sequence);
							// 根据属性类型（Type）设置属性值（Value）
							if (type != null) {
								if (type.startsWith("Integer")) {
									property.setValue(Integer.valueOf(rs
											.getString("value")));
								} else if (type.startsWith("String")) {
									property.setValue(rs.getString("value"));
								} else if (type.startsWith("Double")) {
									property.setValue(Double.valueOf(rs
											.getString("value")));
								} else if (type.startsWith("Float")) {
									property.setValue(Float.valueOf(rs
											.getString("value")));
								} else if (type.startsWith("Date")) {
									property.setValue(new Date(rs
											.getString("value")));
								} else if (type.startsWith("Boolean")) {
									property.setValue(new Boolean(rs
											.getString("value")));
								} else if (type.startsWith("Long")) {
									property.setValue(Long.valueOf(rs
											.getString("value")));
								} else if (type.startsWith("Short")) {
									property.setValue(Short.valueOf(rs
											.getString("value")));
								} else if (type.startsWith("UFBoolean")) {
									property.setValue(UFBoolean.valueOf(rs
											.getString("value")));
								} else if (type.startsWith("UFDouble")) {
									property.setValue(new UFDouble(rs
											.getString("value")));
								} else if (type.startsWith("UFDate")) {
									property.setValue(new UFDate(rs
											.getString("value")));
								} else if (type.startsWith("UFTime")) {
									property.setValue(new UFTime(rs
											.getString("value")));
								} else if (type.startsWith("UFDateTime")) {
									property.setValue(new UFDateTime(rs
											.getString("value")));
								} else{
									//否则获得String值
									property.setValue(rs.getString("value"));
								}
							} else {
								//没有类型，则认为是String
								property.setValue(rs.getString("value"));
							}						
							list.add(property);
						}
						return list;
					}
				});
		return vos.toArray(new IndividualPropertyVO[vos.size()]);
	}
	
	@SuppressWarnings("unchecked")
	public IndividualPropertyVO[] queryPropertyVOs(final String pk_user)
			throws DAOException {

		String sql = "select pk_property, propertyname, value, type, pk_group, pageid, seq  from sm_individual_property "
				+ "where pk_user=? ";
		SQLParameter parameter = new SQLParameter();
		parameter.addParam(pk_user);
		List<IndividualPropertyVO> vos = (List<IndividualPropertyVO>) new BaseDAO()
				.executeQuery(sql, parameter, new ResultSetProcessor() {
					private static final long serialVersionUID = 2395823993172495213L;

					@SuppressWarnings("deprecation")
					public Object handleResultSet(ResultSet rs)
							throws SQLException {
						List<IndividualPropertyVO> list = new ArrayList<IndividualPropertyVO>();
						while (rs != null && rs.next()) {
							IndividualPropertyVO property = new IndividualPropertyVO();
							property.setPk_user(pk_user);
							property.setPk_property(rs.getString("pk_property"));
							property.setPropertyname(rs
									.getString("propertyname"));							
							property.setPageID(rs.getString("pageid"));
							property.setPk_group(rs.getString("pk_group"));
							//设置属性值类型
							String type = rs.getString("type");							
							property.setType(type);
							String sequence=rs.getString("seq");
							property.setSeq(sequence);
							// 根据属性类型（Type）设置属性值（Value）
							if (type != null) {
								if (type.startsWith("Integer")) {
									property.setValue(Integer.valueOf(rs
											.getString("value")));
								} else if (type.startsWith("String")) {
									property.setValue(rs.getString("value"));
								} else if (type.startsWith("Double")) {
									property.setValue(Double.valueOf(rs
											.getString("value")));
								} else if (type.startsWith("Float")) {
									property.setValue(Float.valueOf(rs
											.getString("value")));
								} else if (type.startsWith("Date")) {
									property.setValue(new Date(rs
											.getString("value")));
								} else if (type.startsWith("Boolean")) {
									property.setValue(new Boolean(rs
											.getString("value")));
								} else if (type.startsWith("Long")) {
									property.setValue(Long.valueOf(rs
											.getString("value")));
								} else if (type.startsWith("Short")) {
									property.setValue(Short.valueOf(rs
											.getString("value")));
								} else if (type.startsWith("UFBoolean")) {
									property.setValue(UFBoolean.valueOf(rs
											.getString("value")));
								} else if (type.startsWith("UFDouble")) {
									property.setValue(new UFDouble(rs
											.getString("value")));
								} else if (type.startsWith("UFDate")) {
									property.setValue(new UFDate(rs
											.getString("value")));
								} else if (type.startsWith("UFTime")) {
									property.setValue(new UFTime(rs
											.getString("value")));
								} else if (type.startsWith("UFDateTime")) {
									property.setValue(new UFDateTime(rs
											.getString("value")));
								} else {
									// 否则获得String值
									property.setValue(rs.getString("value"));
								}
							} else {
								// 没有类型，则认为是String
								property.setValue(rs.getString("value"));
							}
							list.add(property);
						}
						return list;
					}
				});
		return vos.toArray(new IndividualPropertyVO[vos.size()]);

	}

	/*
	 * 根据属性状态，判断该记录是否在数据库中已经存在
	 */
	@Override
	public IndividualPropertyVO[] savePropertyVOs(IndividualPropertyVO[] properties)
			throws BusinessException {		
		if (properties == null||properties.length==0)
			return properties;
		IndividualPropertyVO property=properties[0];
		String pageID=property.getPageID();
		String pk_group=property.getPk_group();
		String pk_user=property.getPk_user();
		Set<String> propname = new HashSet<String>();
		for (IndividualPropertyVO vo : properties) {
			propname.add(vo.getPropertyname());
		}		
		
		// 删除所有属性数组类型
		deleteMultiPropertyType(pageID,pk_group,pk_user,propname);
		
		//begin 不让循环操作数据库 ，改 批量操作 2013--3-12
		ArrayList<IndividualPropertyVO> insertlist = new ArrayList<IndividualPropertyVO>();
		ArrayList<IndividualPropertyVO> deltelist = new ArrayList<IndividualPropertyVO>();
		ArrayList<IndividualPropertyVO> updatelist = new ArrayList<IndividualPropertyVO>();
		//end 
		
		// 写入数据库
		for (int i = 0; i < properties.length; i++) {
			if (properties[i].getPk_property() == null) {
				if (properties[i].getValue() != null) {
					// 主键值为空，值不为null，则新增
					insertlist.add(properties[i]);
				}
			} else {
				// 加动态锁
				addPKLock(properties[i]);
				if (properties[i].getValue() == null) {
					// 主键不为空，值为空；则删除记录
					deltelist.add(properties[i]);
				} else {
					// 主键不为空，值不为空；则更新记录
					updatelist.add(properties[i]);
				}
			}
		}
		
		//begin对应增删改操作2012-3-12
		try{
			dao.insertVOArray(insertlist.toArray(new IndividualPropertyVO[0]));
		}catch (Exception e) {
			Logger.error(e);
		}
		try{
			dao.deleteVOArray(deltelist.toArray(new IndividualPropertyVO[0]));
		}catch (Exception e) {
			Logger.error(e);
		}
		try{
			dao.updateVOArray(updatelist.toArray(new IndividualPropertyVO[0]));
		}catch (Exception e) {
			Logger.error(e);
		}
		//end 
		
		return properties;
	}

	
	private void deleteMultiPropertyType(String pageID, String pk_group,
			String pk_user,Set<String> propname) throws DAOException {
		StringBuilder sb=new StringBuilder();
	    sb.append(" pageID='").append(pageID).append("'");
	    sb.append(" and ");
	    if(pk_group==null){
	    	sb.append("pk_group ='~'");
	    }else{
	    	sb.append("pk_group='").append(pk_group).append("'");
	    }
	    sb.append(" and ");
	    if(pk_user==null){
	    	 sb.append("pk_user ='~'");
	    }else{
	    	 sb.append("pk_user='").append(pk_user).append("'");
	    }	   
	    sb.append(" and ");
	    sb.append("seq <> '~'");
	    
	    
	    StringBuilder sbin=new StringBuilder();
	    
	    if(propname != null){
	    	String [] arr =propname.toArray(new String[0]);	
	    	if(arr.length >0)
	    	{
	    		sbin.append("and propertyname in (");
		    	for(int i=0;i<arr.length;i++){
		    		if(i!=arr.length-1){
		    			sbin.append("'").append(arr[i]).append("',");
		    		}
		    		else{
		    			sbin.append("'").append(arr[i]).append("')");
		    		}
		    	}
		    	
	    	}
	    }
	    sb.append(sbin);
		dao.deleteByClause(IndividualPropertyVO.class, sb.toString());
	}

	private void addPKLock(IndividualPropertyVO lockedVo)
			throws BusinessException {
		String pk_individualProperty = lockedVo.getPk_property();
		//对主键加动态锁
		boolean isLocked = PKLock.getInstance().addDynamicLock(
				pk_individualProperty);
		if (!isLocked)
			throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("sfbase", "IndivPropertyImpl-000000", null, new String[]{lockedVo.getPropertyname()})/*属性{0}已经被锁定，无法完成操作！*/);

	}

	
	@Override
	public String queryDefaultDBizOrg(String pk_user, String pk_group,
			String propertname) throws BusinessException {
			String sql = "select value from sm_individual_property where propertyname = '"+propertname+"' and pk_user ='"+pk_user+"' and pk_group='"+pk_group+"' ";
			String value = (String) new BaseDAO().executeQuery(sql, new ResultSetProcessor() {
				
				@Override
				public String handleResultSet(ResultSet paramResultSet) throws SQLException {
					String value = null;
					while(paramResultSet != null && paramResultSet.next()){
						value = paramResultSet.getString("value");
						
					};
					return value;
				}
			});
		return value;
	}
}

