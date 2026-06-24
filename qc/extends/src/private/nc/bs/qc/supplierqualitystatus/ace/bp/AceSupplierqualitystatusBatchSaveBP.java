package nc.bs.qc.supplierqualitystatus.ace.bp;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.bs.ml.NCLangResOnserver;
import nc.itf.scmpub.reference.uap.pf.PfServiceScmUtil;
import nc.jdbc.framework.SQLParameter;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.vo.bd.meta.BatchOperateVO;
import nc.vo.bdsearch.PageVO;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pu.supqualistatus.AggSupplierqualityHVO;
import nc.vo.pu.supqualistatus.SupplierqualityHVO;
import nc.vo.pub.BusinessException;
import nc.vo.sm.funcreg.ButtonRegVO;
import nc.vo.trade.sqlutil.IInSqlBatchCallBack;
import nc.vo.trade.sqlutil.InSqlBatchCaller;

/**
 * 保存的BP
 * 
 */
public class AceSupplierqualitystatusBatchSaveBP {

	public static int NEED_INSERT = 0;
	public static int NEED_DELETE = 1;
	public static int NEED_UPDATE = 2;

	public BatchOperateVO batchSaveVO(BatchOperateVO vo)
			throws BusinessException {
		if (vo == null)
			return null;

		if (vo.getAddObjs() != null && vo.getAddObjs().length > 0) {

			AggSupplierqualityHVO[] aggvos = getAggSupplierqualityHVO(vo
					.getAddObjs());
			aggvos = (AggSupplierqualityHVO[]) PfServiceScmUtil.processBatch(
					"SAVEBASE", "C055", aggvos, null, null);
			Object[] addVos = getObjs(aggvos);
			vo.setAddObjs(addVos);
		}

		if (vo.getUpdObjs() != null && vo.getUpdObjs().length > 0) {
			AggSupplierqualityHVO[] aggvos = getAggSupplierqualityHVO(vo
					.getUpdObjs());
			aggvos = (AggSupplierqualityHVO[]) PfServiceScmUtil.processBatch(
					"SAVEBASE", "C055", aggvos, null, null);
		}

		if (vo.getDelObjs() != null && vo.getDelObjs().length > 0) {
			AggSupplierqualityHVO[] aggvos = getAggSupplierqualityHVO(vo
					.getDelObjs());
			aggvos = (AggSupplierqualityHVO[]) PfServiceScmUtil.processBatch(
					"DELETE", "C055", aggvos, null, null);
		}

		return vo;
	}

	private AggSupplierqualityHVO[] getAggSupplierqualityHVO(Object[] objs) {
		if (objs == null || objs.length == 0)
			return new AggSupplierqualityHVO[0];

		AggSupplierqualityHVO[] aggvos = new AggSupplierqualityHVO[objs.length];
		for (int i = 0; i < objs.length; i++) {
			Object obj = objs[i];
			AggSupplierqualityHVO aggvo = null;
			if (obj instanceof AggSupplierqualityHVO) {
				aggvo = (AggSupplierqualityHVO) obj;
			} else {
				aggvo = new AggSupplierqualityHVO();
				SupplierqualityHVO hvo = (SupplierqualityHVO) obj;
				aggvo.setParentVO(hvo);
			}
			aggvos[i] = aggvo;
		}
		return aggvos;
	}

	private Object[] getObjs(AggSupplierqualityHVO[] aggvos) {
		if (aggvos == null || aggvos.length == 0)
			return new SupplierqualityHVO[0];

		SupplierqualityHVO[] objs = new SupplierqualityHVO[aggvos.length];
		for (int i = 0; i < aggvos.length; i++) {
			AggSupplierqualityHVO aggvo = (AggSupplierqualityHVO) aggvos[i];
			objs[i] = aggvo.getParentVO();
		}
		return objs;
	}

	private void checkButtonVO(ButtonRegVO btn) throws BusinessException {

		BaseDAO dao = new BaseDAO();
		String cfunid = null;
		if (btn.getBtnownertype() != null
				&& btn.getBtnownertype().intValue() == 0)
			cfunid = btn.getParent_id();
		else {
			PageVO page = (PageVO) dao.retrieveByPK(PageVO.class,
					btn.getParent_id());
			// cfunid = page.getParentid();
		}

		String sql = "select count(*) from sm_butnregister where btncode=? and (parent_id=? or parent_id in (select pk_page from sm_pageregister where parentid=?))";
		if (!StringUtil.isEmptyWithTrim(btn.getPk_btn()))
			sql += " and pk_btn<>'" + btn.getPk_btn() + "'";

		SQLParameter parameter = new SQLParameter();
		parameter.addParam(btn.getBtncode());
		parameter.addParam(cfunid);
		parameter.addParam(cfunid);
		Integer count = (Integer) dao.executeQuery(sql, parameter,
				new ColumnProcessor());
		if (count != null && count.intValue() > 0)
			throw new BusinessException(NCLangResOnserver.getInstance()
					.getStrByID("funcreg", "FuncRegisterImpl-000009")/*
																	 * 相同功能下的所有按钮编码不能重复
																	 * ！
																	 */);
	}

}
