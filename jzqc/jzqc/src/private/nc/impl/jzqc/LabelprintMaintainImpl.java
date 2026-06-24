package nc.impl.jzqc;

import java.util.Map;

import nc.bs.jzqc.labelcontrol.bp.AceLabelprintCheckBP;
import nc.impl.pub.ace.AceLabelprintPubServiceImpl;
import nc.itf.jzqc.ILabelprintMaintain;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.bd.printcheck.PrintResultVO;
import nc.vo.jzqc.labelprint.AggLabelPrintHVO;
import nc.vo.jzqc.labelprintapply.AggLabelprintapplyHVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;

public class LabelprintMaintainImpl extends AceLabelprintPubServiceImpl
		implements ILabelprintMaintain {

	@Override
	public void delete(AggLabelPrintHVO[] clientFullVOs,
			AggLabelPrintHVO[] originBills) throws BusinessException {
		super.pubdeleteBills(clientFullVOs, originBills);
	}

	@Override
	public AggLabelPrintHVO[] insert(AggLabelPrintHVO[] clientFullVOs,
			AggLabelPrintHVO[] originBills) throws BusinessException {
		return super.pubinsertBills(clientFullVOs, originBills);
	}

	@Override
	public AggLabelPrintHVO[] update(AggLabelPrintHVO[] clientFullVOs,
			AggLabelPrintHVO[] originBills) throws BusinessException {
		return super.pubupdateBills(clientFullVOs, originBills);
	}

	@Override
	public AggLabelPrintHVO[] query(IQueryScheme queryScheme)
			throws BusinessException {
		return super.pubquerybills(queryScheme);
	}

	@Override
	public AggLabelPrintHVO[] save(AggLabelPrintHVO[] clientFullVOs,
			AggLabelPrintHVO[] originBills) throws BusinessException {
		return super.pubsendapprovebills(clientFullVOs, originBills);
	}

	@Override
	public AggLabelPrintHVO[] unsave(AggLabelPrintHVO[] clientFullVOs,
			AggLabelPrintHVO[] originBills) throws BusinessException {
		return super.pubunsendapprovebills(clientFullVOs, originBills);
	}

	@Override
	public AggLabelPrintHVO[] approve(AggLabelPrintHVO[] clientFullVOs,
			AggLabelPrintHVO[] originBills) throws BusinessException {
		return super.pubapprovebills(clientFullVOs, originBills);
	}

	@Override
	public AggLabelPrintHVO[] unapprove(AggLabelPrintHVO[] clientFullVOs,
			AggLabelPrintHVO[] originBills) throws BusinessException {
		return super.pubunapprovebills(clientFullVOs, originBills);
	}

	// 打印次数
	public Map<String, PrintResultVO> getPrintResultVO(Object[] datas)
			throws BusinessException {
		return super.getPrintResultVO(datas);
	}

	// 失效
	public AggLabelPrintHVO[] printInvalid(AggLabelPrintHVO[] clientFullVOs)
			throws BusinessException {
		return super.printInvalid(clientFullVOs);
	}

	// 重打申请
	public AggLabelPrintHVO[] repeatPrintApply(AggLabelPrintHVO[] clientFullVOs)
			throws BusinessException {
		return super.repeatPrintApply(clientFullVOs);
	}

	// 重打申请记录
	public AggLabelprintapplyHVO[] repeatPrintRecords(
			AggLabelPrintHVO clientFullVOs) throws BusinessException {
		return super.repeatPrintRecords(clientFullVOs);
	}

	@Override
	public AggLabelPrintHVO[] updatePrintResultVO(Object[] datas)
			throws BusinessException {
		return super.updatePrintResultVO(datas);
	}

	@Override
	public void checkExistsAggLabelPrintHVO(String pk_org, String billid,
			String transtype) throws BusinessException {
		super.checkExistsAggLabelPrintHVO(pk_org, new String[]{billid}, transtype);

	}
	
	@Override
	public void checkExistsAggLabelPrintHVO(String pk_org, String[] billid,
			String transtype) throws BusinessException {
		super.checkExistsAggLabelPrintHVO(pk_org, billid, transtype);

	}

	@Override
	public void checkExistsPower(String pk_org, String transtype,
			SuperVO billVO, String userid) throws BusinessException {
		AceLabelprintCheckBP bp = new AceLabelprintCheckBP();
		bp.checkExistsPower(pk_org, transtype, billVO, userid);
	}

}
