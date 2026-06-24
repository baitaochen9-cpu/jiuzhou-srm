package nc.individuation.property.itf;

import nc.individuation.property.vo.IndividualPropertyVO;
import nc.vo.pub.BusinessException;

/**
 * @author gaijf
 * @date 2009-6-9
 * 个性化属性 服务接口
 */
public interface IPropertyService {
	/**
	 * @param pageID
	 * @param pk_user
	 * @param pk_group
	 * @return
	 * @throws BusinessException
	 *             查询用户(pk_user)、在登录集体(pk_group)下，个性化页面(pageID)关联的
	 *             所有属性对象(IndividualPropertyVO)的集合
	 */
	public IndividualPropertyVO[] queryPropertyVOs(String pageID,
			String pk_user, String pk_group) throws BusinessException;

	/**
	 * @param pk_user
	 * @return
	 * @throws BusinessException
	 * 查询用户(pk_user)，关联的所有属性对象的集合
	 */
	public IndividualPropertyVO[] queryPropertyVOs(String pk_user)
			throws BusinessException;

	/**
	 * @param properties
	 * @throws BusinessException
	 *             批量保存属性对象集合
	 */
	public IndividualPropertyVO[] savePropertyVOs(IndividualPropertyVO[] properties)
			throws BusinessException;
	
	/**
	 * @param pk_user
	 * @param pk_group
	 * @param propertname
	 * @return
	 * @throws BusinessException
	 */
	public String queryDefaultDBizOrg(String pk_user,String pk_group,String propertname ) 
			throws BusinessException;
}

