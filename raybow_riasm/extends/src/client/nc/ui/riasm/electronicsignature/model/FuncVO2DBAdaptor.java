package nc.ui.riasm.electronicsignature.model;

import nc.vo.bd.meta.CAVO2BDObject;
import nc.vo.bd.meta.IBDObject;
import nc.vo.bd.meta.IBDObjectAdapterFactory;
import nc.vo.riasm.electronicsignature.ElectronicSignatureVO;
import nc.vo.sm.funcreg.FuncRegisterVO;
import nc.vo.sm.funcreg.ModuleVO;

/**
 * 对象转换类
 * 
 * @author lkp
 *
 */
public class FuncVO2DBAdaptor implements IBDObjectAdapterFactory {

	public IBDObject createBDObject(Object obj) {
		 
		if(obj instanceof ModuleVO)
		{
			ModuleVO module = (ModuleVO)obj;
			return new CAVO2BDObject(module, "moduleid","systypecode","systypename", "parentcode", null, null);
		}else if(obj instanceof FuncRegisterVO)
		{
			//将编码树按照特定策略转换成PK树进行处理，与页签放到一起统一处理
			return new CAVO2BDObject((FuncRegisterVO)obj , "cfunid", "fun_code", "fun_name", null, null,null);
		}else if(obj instanceof ElectronicSignatureVO)
		{
			//将编码树按照特定策略转换成PK树进行处理，与页签放到一起统一处理
			return new CAVO2BDObject((ElectronicSignatureVO)obj , "billid", "code", "name", null, null,null);
		}
		return null;
	}
 
} 
