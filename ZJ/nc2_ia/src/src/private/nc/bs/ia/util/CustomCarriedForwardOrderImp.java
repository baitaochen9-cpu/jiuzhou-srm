package nc.bs.ia.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.ia.pub.VOQryUtil;
import nc.impl.pubapp.pattern.data.vo.VOQuery;
import nc.itf.fi.pub.SysInit;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.sf.pub.util.QueryUtil;
import nc.vo.bd.material.cost.MaterialCostVO;
import nc.vo.corg.CostRegionStockStoreVO;
import nc.vo.corg.CostRegionVO;
import nc.vo.ia.util.CustomCarriedForwardOrder;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;

/**
 * 
 * @author zhian.ye 20210325
 *
 */
public  class CustomCarriedForwardOrderImp implements CustomCarriedForwardOrder {
	

	private Map<String,String> type= new HashMap<String,String>(){
			{put("I2", "IA701");
			put("I7", "IA702");
			put("I9", "IA703");
			put("I4", "IA704");
			put("I6", "IA705");
			put("I3", "IA706");
			}
			};
		
			/**
			 * 通过组织和仓库获取成本域
			 * @param pk_org
			 * @param stordoc
			 * @return
			 */
			BaseDAO dao = null;
			private BaseDAO getDao(){
				if(null == dao ){
					dao = new BaseDAO();
				}
				return dao;
			}
	public String getPk_CostRegion (String pk_org,String stordoc){
		  // 出入库单中，这两个参数都不参为空，只要有一个空值进来都是不对的
		if(pk_org.isEmpty() || stordoc.isEmpty()){
			return null;
		}
//		VOQuery<CostRegionVO>  query = new VOQuery<CostRegionVO>(CostRegionVO.class);
		String sql = " select * from org_costregion where nvl(dr,0)=0 and pk_org='"+pk_org+"' and enablestate = 2 ";
		List<CostRegionVO> query2 = null;
		try {
			query2 = (List<CostRegionVO>) getDao().executeQuery(sql, new BeanListProcessor(CostRegionVO.class));
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		CostRegionVO[] query2 = query.query(condition, null);
		if(null == query2 || query2.size() == 0){
			return pk_org;
		}
		// 找到对应成本域
		for(CostRegionVO costRegionVO : query2){
			Integer layertype = costRegionVO.getLayertype();
			if(layertype == 0 || layertype == 1){/*财务组织，库存组织：即可全组织覆盖，直接返回自身*/
				return costRegionVO.getPk_costregion();
			}
			if(layertype == 3){//否则需要加仓库进行判断 ，找对应子表数据看是否存在，如果存在的话返回当前成本域
				String sql2 = " select * from org_cr_stockstore where nvl(dr,0)=0" +
						" and  pk_stockorg = '"+pk_org+"' and pk_storage = '"+stordoc+"' and pk_costregion = '"+costRegionVO.getPk_costregion()+"'";
				List<CostRegionStockStoreVO> costRegionStockStoreVOs = null;
				try {
					costRegionStockStoreVOs = (List<CostRegionStockStoreVO>) getDao().executeQuery(sql2, new BeanListProcessor(CostRegionStockStoreVO.class));
				} catch (DAOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(null != costRegionStockStoreVOs || costRegionStockStoreVOs.size() > 0){
					return costRegionVO.getPk_costregion();
				}
			}
		}
		return null;
	} 

	@Override
	public UFDate getBizData(String pk_org, UFDate bizdate,String billtype) {
		// TODO Auto-generated method stub
		/*单据类型+组织获取时间  ，业务日期获取结算日期*/
		//1/检查组织能数功能是否开启
		UFDateTime newbizdate = null;
		try {
			UFBoolean paraBoolean = SysInit.getParaBoolean(pk_org, "IA708");
			if(paraBoolean == UFBoolean.FALSE){
				return bizdate;
			}
			Integer paraInt = SysInit.getParaInt(pk_org, "IA707"); //结算提前天数
			String time = SysInit.getParaString(pk_org, type.get(billtype));
			UFDate data = this.getData(bizdate.getYear(), bizdate.getMonth());
			data = data.getDateBefore(paraInt);
			newbizdate = new UFDateTime((data.toString()).substring(0, 10) +" "+ time);
		} catch (BusinessException e) {
			// TODO 如果发生异常，保留业务单据传过来的日期 和时间
//			e.printStackTrace();
			return bizdate;
		} 
		return newbizdate.getDate();
	}
	
	public UFBoolean isSelectMaterial(String material,String pk_org){
		String wheresql = " and pk_material = '"+material+"' and pk_org = '"+pk_org+ "' and def1 ='Y'";
//		VOQryUtil<MaterialCostVO > query =new  VOQryUtil<MaterialCostVO>(MaterialCostVO.class);
		VOQuery<MaterialCostVO>  query = new VOQuery<MaterialCostVO>(MaterialCostVO.class);
		MaterialCostVO[] qryByCondition = query.query(wheresql, null);;
		if(qryByCondition== null || qryByCondition.length == 0){
			return UFBoolean.FALSE;
		}
		
		return UFBoolean.TRUE;
		
	}

	private UFDate getData(int year,int month){
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, year);//设置年*/
		cal.set(Calendar.MONTH, month-1);/*设置月*/
		int lasDay =cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		cal.set(Calendar.DAY_OF_MONTH, lasDay);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		UFDate ufd = new UFDate(sdf.format(cal.getTime()));
		return ufd;
	}
}
