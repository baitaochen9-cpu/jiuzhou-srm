package nc.ui.qc.supplierqualitystatus.action;

import nc.ui.pubapp.uif2app.actions.intf.ICopyActionProcessor;
import nc.vo.pu.supqualistatus.AggSupplierqualityHVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.pf.BillStatusEnum;
import nc.vo.pubapp.AppContext;
import nc.vo.uif2.LoginContext;

public class CopyActionProcessor implements
		ICopyActionProcessor<AggSupplierqualityHVO> {

	@Override
	public void processVOAfterCopy(AggSupplierqualityHVO paramT,
			LoginContext paramLoginContext) {
		paramT.getParentVO().setPrimaryKey(null);
		paramT.getParentVO().setAttributeValue("billno", null);
		paramT.getParentVO().setAttributeValue("approvestatus",
				BillStatusEnum.FREE.value());
		paramT.getParentVO().setAttributeValue("dbilldate",
				AppContext.getInstance().getBusiDate());
		paramT.getParentVO().setAttributeValue("approver", null);
		paramT.getParentVO().setAttributeValue("approvedate", null);
		paramT.getParentVO().setAttributeValue("creator",
				paramLoginContext.getPk_loginUser());
		paramT.getParentVO().setAttributeValue("creationtime",
				new UFDateTime(System.currentTimeMillis()));
		paramT.getParentVO().setAttributeValue("modifier",
				paramLoginContext.getPk_loginUser());
		paramT.getParentVO().setAttributeValue("modifiedtime",
				new UFDateTime(System.currentTimeMillis()));
		paramT.getParentVO().setAttributeValue("approver", null);

		String[] codes = paramT.getTableCodes();
		if (codes != null && codes.length > 0) {
			for (int i = 0; i < codes.length; i++) {
				String tableCode = codes[i];
				CircularlyAccessibleValueObject[] childVOs = paramT
						.getTableVO(tableCode);
				for (CircularlyAccessibleValueObject childVO : childVOs) {
					try {
						childVO.setPrimaryKey(null);
						childVO.setAttributeValue("srctrantype", null);
						childVO.setAttributeValue("srcrowno", null);
						childVO.setAttributeValue("vfirsttypecode", null);
						childVO.setAttributeValue("vristcode", null);
						childVO.setAttributeValue("cfirstid", null);
						childVO.setAttributeValue("cfirstbid", null);
						childVO.setAttributeValue("vfirstrowno", null);
						childVO.setAttributeValue("vfirsttrantype", null);
					} catch (BusinessException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
}
