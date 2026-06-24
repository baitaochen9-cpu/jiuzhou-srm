package nc.bs.mmpps.calc.bp.fetch.supply;

import nc.bs.mmpps.calc.bp.fetch.base.CommonMapVO;
import nc.bs.mmpps.calc.bp.fetch.base.FetchSQLFactory;
import nc.bs.mmpps.calc.bp.fetch.supply.creator.OnHandSupplyCreator;
import nc.vo.mmpps.calc.entity.CalculateContext;
import nc.vo.mmpps.calc.enumeration.SupplyType;

/**
 * 现存量产生的供给数据<br>
 * 现存量只对<font color="red">运算时格的当期</font>产生供给<br>
 * 调用现存量提供的查询服务nc.itf.mmpd.adapter.scm.ic.OnhandQueryService（跨领域，需要在pd中创建适配器）<br>
 * 将现存量产生的供给数据持久化到nc.vo.mmpps.calc.entity.calculate.SupplyVO的实体中<br>
 * 
 * @since 6.0
 * @version 2011-4-13 下午02:14:43
 * @author duy
 */
class OnhandSupply extends BaseSupplyFetch {

	public OnhandSupply(CalculateContext context) {
		super(context);
	}

	@Override
	protected CommonMapVO CreateCommonMapVO() {
		SupplyMapVO supplyMapVO = SupplyMapVOCreator.getOnHandSupplyMapVO(this
				.getContext());
		if(null==supplyMapVO){
			return null;
		}
        //Modified by guojunf 去掉库存状态的过滤，在单据状态配置处统一处理
		supplyMapVO.setWhere(supplyMapVO.getWhere().replace("and (ic_onhanddim.cstateid = '~'  or ic_storestate.iusability='1')", ""));
		setSupplyType(supplyMapVO);
		
	
		return supplyMapVO;
	}

	@Override
	public Integer getDemandOrSupplyType() {
		return SupplyType.ONHAND_NUM.toInteger();
	}
	


	@Override
	protected FetchSQLFactory createFetchSQLFactory(CommonMapVO mapVO) {
		SupplyMapVO supplyMapVO = (SupplyMapVO) mapVO;
		OnHandSupplyCreator creator = new OnHandSupplyCreator(
				this.getContext(), supplyMapVO);
		return creator;
	}
	
	@Override
	public void afterFetch() {
		// TODO Auto-generated method stub
		//2023-11-03 liyf 增加库存失效日期
//		BaseDAO dao  = new BaseDAO();
//		try {
//			dao.executeUpdate(" update temp_mrp_supply set vdef3='2023-08-31 00:00:00' ");
//		} catch (DAOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		super.afterFetch();
	}
	

}
