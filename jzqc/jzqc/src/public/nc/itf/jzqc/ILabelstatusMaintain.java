package nc.itf.jzqc;

import nc.itf.pubapp.pub.smart.ISmartService;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.jzqc.labelstatus.LabelStatusVO;
import nc.vo.pub.BusinessException;
import nc.vo.uif2.LoginContext;

public interface ILabelstatusMaintain extends ISmartService{

	 public LabelStatusVO[] query(IQueryScheme queryScheme)
      throws BusinessException, Exception;
	 public  LabelStatusVO[] queryAllVOByContext(boolean paramBoolean, LoginContext paramLoginContext)
			    throws BusinessException;
}