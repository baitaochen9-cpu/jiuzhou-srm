package nc.itf.material.mdm;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONException;


import nc.bs.dao.DAOException;
import nc.vo.bd.defdoc.DefdocVO;
import nc.vo.bd.material.MaterialVO;
import nc.vo.bd.material.pf.MaterialPfVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;

/**
 * 
 * @author zhian.ye 20240419
 * 主数据获取接口
 *
 */
public interface SendMdmItf {
	
	/**
	 * 主数据查询
	 * @return 条件查询的主数据信息
	 * @throws BusinessException 
	 */
	public List<Map<String,String>> queryMdmPrimary(Map<String,List<String>> contds) throws BusinessException;
	
	/**
	 * 主数据查询,条件查询
	 * @return 条件查询的主数据信息
	 * @throws BusinessException 
	 */
	public List<Map<String,String>> queryMdmPrimary(Map<String,List<String>> contds,String mode) throws BusinessException;

	
	/**
	 * 将数据升级到主数据主表并返回主数据信息主数据信息
	 * @param matial
	 * @return 
	 * @throws BusinessException
	 */
	public Map<String,Map<String, String>>  insetMdmMaterialHost(MaterialVO[] materials,String pk_org)throws BusinessException;

	/**
	 * 关联主数据主表，并同步到主数据辅表，
	 * @param materialvo
	 * @return 
	 * @throws BusinessException
	 */
	public UFBoolean insetMdmMaterialAssist(MaterialVO[] materialvo, String pk_org)throws BusinessException;
	


	/**
	 * 通过主键查询档案数据
	 * @param id 档案主键
	 * @param tablename 表名
	 * @param keyname 主键字段名
	 * @param fielName 查询字段名称
	 * @return 对应字段的值
	 * @throws DAOException
	 */
	public String queryDocNameByID(String id, String tablename, String keyname,
			String fielName) throws DAOException;
/**
 * 物料批量同步
 * @param pk_org
 * @return
 * @throws BusinessException
 * @throws JSONException
 */
	public boolean batchToMdm(String pk_org) throws BusinessException, JSONException;
	
	
	/**
	 * 集团物料基本分类同步到主数据
	 * @param vos
	 * @return
	 * @throws BusinessException
	 */
	public String insetMdmMaterialClass(DefdocVO[] vos) throws BusinessException;
	
	
	/**
	 * 集团物料基本信息推送关务
	 * @param vos
	 * @return
	 * @throws BusinessException
	 */
	
	public  void materialSendGw(MaterialVO[] materialVOs) throws BusinessException;
	
	/**
	 * ERP 内部物料ID交换
	 * @param pk_org  起始公司
	 * @param pk_mateiral  物料ID
	 * @param org  对方公司ID
	 * @return   对方公司物料信息
	 * @throws BusinessException
	 */
	public String materialExchange(String pk_org , String pk_mateiral ,String org) throws BusinessException;
	
	/**
	 * ERP 以主数据编码进行内部物料转换
	 * @param pk_material 物料ID
	 * @param pk_org_dest  目标组织
	 * @return  materialID
	 * @throws BusinessException
	 */
	public String materialChangeByOrg(String pk_material,String pk_org_dest)  throws BusinessException;
	
	/**
	 * 通过主数据进行物料编码交换
	 * @param mateiralcode  物料编码
	 * @param pk_org_dest    目标组织
	 * @return   物料编码
	 * @throws BusinessException
	 */
	public String materialCodeChange(String mateiralcode ,String pk_org_dest) throws BusinessException;
	
	/**
	 * 批量主数据更新(仅支持更新，物料必须携带主数据编码)
	 * @param datas 物料数组，必须带有主数据编码，否则将被剔除在执行列表外
	 * @return 执行信息，用于反给用户执行情况
	 * @throws BusinessException
	 */
	 
	public String updaterMdmMaterial(MaterialVO[] datas) throws BusinessException;
}
