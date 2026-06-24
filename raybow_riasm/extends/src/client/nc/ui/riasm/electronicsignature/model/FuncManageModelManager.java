package nc.ui.riasm.electronicsignature.model;

import java.util.ArrayList;
import java.util.List;

import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.bbd.func.IFuncRegisterQueryService;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.vo.sm.funcreg.ModuleVO;
import nc.ui.uif2.model.HierachicalDataAppModel;
import nc.ui.uif2.model.IAppModelDataManager;
import nc.ui.uif2.model.IQueryAndRefreshManager;
import nc.vo.pub.SuperVO;
import nc.vo.riasm.electronicsignature.ElectronicSignatureVO;
import nc.vo.sm.funcreg.FuncRegisterVO;


/**
 * 节点注册的模型管理器
 * 
 * @author lkp
 *
 */
public class FuncManageModelManager implements IAppModelDataManager,IQueryAndRefreshManager{
	
	private HierachicalDataAppModel funcModel = null;
	
	@SuppressWarnings("unchecked")
	public void initModel() {
		
		try {

			List<SuperVO> list = new ArrayList<SuperVO>();
			IFuncRegisterQueryService funcService = NCLocator.getInstance().lookup(IFuncRegisterQueryService.class);
			List[] functionInfoArray = funcService.getFunctionInfoOfModule(null);
			StringBuffer strb = new StringBuffer();
			strb.append(" select k.*from riasm_electronicsignature  k where nvl(k.dr,0) = 0 ");
			IUAPQueryBS service = (IUAPQueryBS) NCLocator.getInstance().lookup(
					IUAPQueryBS.class.getName());
			List<ElectronicSignatureVO> list1= (List<ElectronicSignatureVO>)service
					.executeQuery(strb.toString(),
							new BeanListProcessor(ElectronicSignatureVO.class));
			
//			if(functionInfoArray[3] != null && !functionInfoArray[3].isEmpty()){
//				list.addAll(functionInfoArray[3]);
//			}
				
			if(functionInfoArray[0] != null && !functionInfoArray[0].isEmpty()){
				List<FuncRegisterVO> funcList  = functionInfoArray[0];
				for(FuncRegisterVO vo:funcList){
					for(ElectronicSignatureVO vo1 : list1){
						if(vo.getFuncode() .equals(vo1.getPk_parent())){
							vo1.setName(vo.getFun_name());
							vo1.setCode(vo.getOwn_module());
							vo1.setSrcbillid(vo.getParent_id());
							vo1.setSrcbilltype(vo.getOwn_module());
							list.add(vo1);
						}
					}
				}
			}
			List<SuperVO> list2 = new ArrayList<SuperVO>();
			if(functionInfoArray[3] != null && !functionInfoArray[3].isEmpty()){
				List<ModuleVO> funcList  = functionInfoArray[3];
				for(ModuleVO vo:funcList){
					for(ElectronicSignatureVO vo1 : list1){
						if(vo.getModuleid().equals(vo1.getSrcbillid())){
							list2.add(vo);
						}else if(vo.getModuleid().equals(vo1.getSrcbilltype())){
							list2.add(vo);
						}
					}
				}
			}
			list2.addAll(list);
			
			getFuncModel().initModel(list2.toArray(new SuperVO[0]));
			
		} catch (Exception e) { 
			Logger.error(e.getMessage(), e);
		}
	}

	public HierachicalDataAppModel getFuncModel() {
		return funcModel;
	}

	public void setFuncModel(HierachicalDataAppModel funcModel) {
		this.funcModel = funcModel;
	}

	@Override
	public void refresh() {
		initModel();
	}

	@Override
	public void initModelBySqlWhere(String sqlWhere) {
		
	}
	
}
