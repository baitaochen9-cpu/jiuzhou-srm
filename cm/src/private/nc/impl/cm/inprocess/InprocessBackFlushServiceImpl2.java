 
package nc.impl.cm.inprocess;
 import nc.bs.cm.equivrate.bp.EquivrateSaveBP;
import nc.bs.cm.equivrate.bp.ProWrOrderReportBP;
import nc.impl.cm.InProStuff.InProMaterialDetailReportBP;
import nc.impl.cm.InProStuff.InProMaterialReportBP;
import nc.impl.cm.InProStuff.InProStuffSaveBP;
import nc.itf.cm.inprocess.IInprocessBackFlushService2;
import nc.pub.smart.data.DataSet;
import nc.vo.cm.equivrate.entity.EquivrateAggVO;
import nc.vo.cm.inprocess.entity.InprocessAggVO;
import nc.vo.cm.inprostuff.entity.InproStuffAggVO;
import nc.vo.pub.BusinessException;

import com.ufida.dataset.IContext;
 
/**
 * 
 * @author yunfeng.li
 *
 */
 public class InprocessBackFlushServiceImpl2
   implements IInprocessBackFlushService2
 {

	@Override
	public InprocessAggVO[] backFlushProduct(InprocessAggVO bill)
			throws Exception {
		return new AutoPanDianBP().backFlushProduct( bill);
	}
	


	@Override
	public 	EquivrateAggVO[] autoEquivrate(String pk_org, String period)
			throws Exception {
		EquivrateSaveBP bp = new EquivrateSaveBP();
		return bp.create(pk_org,period);
		
	}



	@Override
	public InproStuffAggVO[] autoInprostuff(String pk_org, String period)
			throws Exception {
		InProStuffSaveBP bp = new InProStuffSaveBP();
		return bp.create(pk_org,period);
	}



	@Override
	public DataSet proMaterialsStuff(  IContext context) throws BusinessException {
		//liyf 较小改动来实现报表查询
		String reptorcode=(String) context.getAttribute("reptorcode");
		if("yf_zcp_clhz".equalsIgnoreCase(reptorcode)){
			InProMaterialReportBP bp = new InProMaterialReportBP();
			return bp.dealData(context);
		}else if("yf_zcp_clmx".equalsIgnoreCase(reptorcode)){
			InProMaterialDetailReportBP bp = new InProMaterialDetailReportBP();
			return bp.dealData(context);
		}else{
			return null;
		}
		
	}



	@Override
	public DataSet proWrOrders(IContext context) throws BusinessException {
		ProWrOrderReportBP bp = new ProWrOrderReportBP();
		return bp.proWrOrders(context);
	}
}
