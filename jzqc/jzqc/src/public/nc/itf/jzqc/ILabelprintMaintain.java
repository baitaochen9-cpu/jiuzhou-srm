package nc.itf.jzqc;

import java.util.Map;

import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.bd.printcheck.PrintResultVO;
import nc.vo.jzqc.labelprint.AggLabelPrintHVO;
import nc.vo.jzqc.labelprintapply.AggLabelprintapplyHVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;

public interface ILabelprintMaintain {

	public void delete(AggLabelPrintHVO[] clientFullVOs,
			AggLabelPrintHVO[] originBills) throws BusinessException;

	public AggLabelPrintHVO[] insert(AggLabelPrintHVO[] clientFullVOs,
			AggLabelPrintHVO[] originBills) throws BusinessException;

	public AggLabelPrintHVO[] update(AggLabelPrintHVO[] clientFullVOs,
			AggLabelPrintHVO[] originBills) throws BusinessException;

	public AggLabelPrintHVO[] query(IQueryScheme queryScheme)
			throws BusinessException;

	public AggLabelPrintHVO[] save(AggLabelPrintHVO[] clientFullVOs,
			AggLabelPrintHVO[] originBills) throws BusinessException;

	public AggLabelPrintHVO[] unsave(AggLabelPrintHVO[] clientFullVOs,
			AggLabelPrintHVO[] originBills) throws BusinessException;

	public AggLabelPrintHVO[] approve(AggLabelPrintHVO[] clientFullVOs,
			AggLabelPrintHVO[] originBills) throws BusinessException;

	public AggLabelPrintHVO[] unapprove(AggLabelPrintHVO[] clientFullVOs,
			AggLabelPrintHVO[] originBills) throws BusinessException;

	// 打印次数
	public Map<String, PrintResultVO> getPrintResultVO(Object[] datas)
			throws BusinessException;

	// 更新打印结果
	public AggLabelPrintHVO[] updatePrintResultVO(Object[] datas)
			throws BusinessException;

	// 失效
	public AggLabelPrintHVO[] printInvalid(AggLabelPrintHVO[] clientFullVOs)
			throws BusinessException;

	// 重打申请
	public AggLabelPrintHVO[] repeatPrintApply(AggLabelPrintHVO[] clientFullVOs)
			throws BusinessException;

	// 重打申请记录
	public AggLabelprintapplyHVO[] repeatPrintRecords(
			AggLabelPrintHVO clientFullVOs) throws BusinessException;

	// 校验是否存在标签
	public void checkExistsAggLabelPrintHVO(String pk_org, String billid,
			String transtype) throws BusinessException;

	// 校验是否存在标签
	public void checkExistsAggLabelPrintHVO(String pk_org, String[] billid,
			String transtype) throws BusinessException;

	// 校验是否存有权限
	public void checkExistsPower(String pk_org, String transtype,
			SuperVO billVO, String userid) throws BusinessException;

}
