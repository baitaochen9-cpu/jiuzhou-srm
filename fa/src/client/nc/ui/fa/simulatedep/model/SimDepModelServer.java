package nc.ui.fa.simulatedep.model;

import nc.itf.fa.prv.ISimulateDep;
import nc.jdbc.framework.SQLParameter;
import nc.ui.am.base.service.AppModelService;
import nc.vo.am.proxy.AMProxy;
import nc.vo.fa.simulatedep.AggSimulateDepVO;
import nc.vo.fa.simulatedep.SimDepConditionVO;
import nc.vo.fa.simulatedep.SimulateDepBVO;
import nc.vo.fa.simulatedep.SimulateDepHVO;
import nc.vo.pub.BusinessException;

/**
 * 
 * @author lilc
 *
 */
public class SimDepModelServer extends AppModelService {

	public AggSimulateDepVO simulateDep(SimDepConditionVO simDepConditionVO) throws BusinessException {
		// TODO Auto-generated method stub
		return getDepService().simulateDep(simDepConditionVO);
	}

	public SimulateDepBVO[] queryDetail(String pk_simulatedep, String pk_category) throws BusinessException {
		// TODO Auto-generated method stub
		return getDepService().queryDetail(pk_simulatedep, pk_category);
	}

	public SimulateDepHVO[] querySimDepHead(String whereSQL) throws BusinessException {
		// TODO Auto-generated method stub
		return getDepService().querySimDepHead(whereSQL);
	}

	public AggSimulateDepVO querySimulateDepVOByPK(String pk_simulatedep) throws BusinessException {
		// TODO Auto-generated method stub
		return getDepService().querySimulateDepVOByPK(pk_simulatedep);
	}
	
	public SimulateDepBVO[] queryPeriodDetail(String pk_simulatedep, String[] pk_categorys) throws BusinessException {
		// TODO Auto-generated method stub
		return getDepService().queryPeriodDetail(pk_simulatedep, pk_categorys);
	}
	
	public SimulateDepBVO[] queryCardDetail(String pk_simulatedep, String condition, SQLParameter parameter) throws BusinessException {
		// TODO Auto-generated method stub
		return getDepService().queryCardDetail(pk_simulatedep, condition, parameter);
	}
	
	
	private ISimulateDep getDepService() {
		return AMProxy.lookup(ISimulateDep.class);
	}

	@Override
	public void delete(Object object) throws Exception {
		// TODO Auto-generated method stub
		getDepService().deleteSimulateDepInfo((AggSimulateDepVO) object);
	}
}
