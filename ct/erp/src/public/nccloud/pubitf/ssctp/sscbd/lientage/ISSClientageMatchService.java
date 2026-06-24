package nccloud.pubitf.ssctp.sscbd.lientage;

import java.util.Map;

import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.ssctp.sscbd.lientage.SSClientageVO;

/**
 * 共享服务委托关系 匹配接口
 * 
 * @author gaozhdf@yonyou.com
 * 
 */
public interface ISSClientageMatchService {

	public enum BusiUnitTypeEnum {
		/**
		 * 费用管理
		 */
		ERM(SSClientageVO.BUSIUNITTYPE1),
		/**
		 * 应收管理
		 */
		AR(SSClientageVO.BUSIUNITTYPE2),
		/**
		 * 应付管理
		 */
		AP(SSClientageVO.BUSIUNITTYPE3),
		/**
		 * 固定资产
		 */
		FA(SSClientageVO.BUSIUNITTYPE4),
		/**
		 * 现金管理
		 */
		CMP(SSClientageVO.BUSIUNITTYPE5),
		/**
		 * 工单
		 */
		SSCWO(SSClientageVO.BUSIUNITTYPE6),
		/**
		 * 总账
		 */
		GL(SSClientageVO.BUSIUNITTYPE7),
		/**
		 * 存货核算
		 */
		IA(SSClientageVO.BUSIUNITTYPE8),		
		/**
		 * 到账通知
		 */
//		INFORMER(SSClientageVO.BUSIUNITTYPE9),
		/**
		 * 凭证单
		 */
		CV(SSClientageVO.BUSIUNITTYPE10),
		/**
		 * 收付款合同
		 */
		FCT(SSClientageVO.BUSIUNITTYPE11),
		/**
		 * 资产使用管理
		 */
		AUM(SSClientageVO.BUSIUNITTYPE12),
		/**
		 * 基础档案
		 */
		UAPBD(SSClientageVO.BUSIUNITTYPE13),
		/**
		 * 销售管理
		 */
		SO(SSClientageVO.BUSIUNITTYPE14),
		/**
		 * 采购管理
		 */
		PU(SSClientageVO.BUSIUNITTYPE15);

		// 业务分类字段名称
		public String busiunittype = "";

		private BusiUnitTypeEnum(String busiunittype) {
			this.busiunittype = busiunittype;
		}
	}

	/**
	 * 根据委托业务分类别名，获得对应的组织集合
	 * 
	 * @param pk_group
	 *            集团pk
	 * @param busiUnitTypeEnum
	 *            业务分类对应字段
	 * @return
	 * @throws BusinessException
	 */
	public String[] queryOrgsByBusiUnit(String pk_group,
			BusiUnitTypeEnum busiUnitTypeEnum) throws BusinessException;

	/**
	 * 根据委托业务分类别名和共享中心PK，获得对应的组织集合
	 * 
	 * @param pk_group
	 *            集团pk
	 * @param pk_sscunit
	 *            共享中心pk
	 * @param busiUnitTypeEnum
	 *            业务分类对应字段
	 * @return
	 * @throws BusinessException
	 */
	public String[] queryOrgsByBusiUnitAndUnitPk(String pk_group,
			String pk_sscunit, BusiUnitTypeEnum busiUnitTypeEnum)
			throws BusinessException;

	/**
	 * 根据委托业务分类别名和对应组织pk,判断该组织的本业务是否存在共享中心
	 * 
	 * @param pk_group
	 *            集团pk
	 * @param pk_org
	 *            组织pk
	 * @param busiUnitTypeEnum
	 *            业务分类对应字段
	 * @return
	 * @throws BusinessException
	 */
	public UFBoolean queryUnitsByBusiUnitAndOrgPk(String pk_group,
			String pk_org, BusiUnitTypeEnum busiUnitTypeEnum)
			throws BusinessException;

	/**
	 * 根据委托业务分类别名和对应组织pk集合,判断每个组织的该业务是否都存在共享中心
	 * 
	 * @param pk_group
	 *            集团pk
	 * @param pk_orgs
	 *            组织pks
	 * @param busiUnitTypeEnum
	 *            业务分类对应字段
	 * @return
	 * @throws BusinessException
	 */
	public Map<String, UFBoolean> queryUnitsByBusiUnitAndPkOrgs(
			String pk_group, String[] pk_orgs, BusiUnitTypeEnum busiUnitTypeEnum)
			throws BusinessException;
}
