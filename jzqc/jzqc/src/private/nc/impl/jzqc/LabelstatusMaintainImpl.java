package nc.impl.jzqc;

import java.util.Collection;

import nc.bs.framework.common.NCLocator;
import nc.impl.pub.ace.AceLabelstatusPubServiceImpl;
import nc.impl.pubapp.pub.smart.BatchSaveAction;
import nc.itf.jzqc.ILabelstatusMaintain;
import nc.md.persist.framework.IMDPersistenceQueryService;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.bd.meta.BatchOperateVO;
import nc.vo.jzqc.labelstatus.LabelStatusVO;
import nc.vo.pub.BusinessException;
import nc.vo.uif2.LoginContext;
import nc.vo.util.VisibleUtil;

public class LabelstatusMaintainImpl extends AceLabelstatusPubServiceImpl
		implements ILabelstatusMaintain {

	@Override
	public LabelStatusVO[] query(IQueryScheme queryScheme)
			throws BusinessException {
		return super.pubquerybasedoc(queryScheme);
	}

	@Override
	public BatchOperateVO batchSave(BatchOperateVO batchVO)
			throws BusinessException {
		BatchSaveAction<LabelStatusVO> saveAction = new BatchSaveAction<LabelStatusVO>();
		BatchOperateVO retData = saveAction.batchSave(batchVO);
		return retData;
	}

	public LabelStatusVO[] queryAllVOByContext(boolean isShowSealData,
			LoginContext context) throws BusinessException {

		String pk_org = context.getPk_org() == null ? "" : context.getPk_org()
				.trim();
		String pk_group = context.getPk_group() == null ? "" : context
				.getPk_group().trim();

		String sqlWhere = " pk_group = '" + pk_group + "' and nvl(dr,0) = 0 ";
		IMDPersistenceQueryService queryService = (IMDPersistenceQueryService) NCLocator
				.getInstance().lookup(IMDPersistenceQueryService.class);

		Collection<LabelStatusVO> list = queryService.queryBillOfVOByCond(
				LabelStatusVO.class, sqlWhere, true);

		return (LabelStatusVO[]) list.toArray(new LabelStatusVO[0]);
	}
}
