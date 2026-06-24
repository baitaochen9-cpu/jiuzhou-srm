package nc.bs.qc.supplierqualitystatus.ace.bp.rule;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.logging.Log;
import nc.impl.am.db.processor.BeanListMapProcessor;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.vo.am.common.util.ExceptionUtils;
import nc.vo.pf.pub.util.SQLUtil;
import nc.vo.pu.supqualistatus.AggSupplierqualityHVO;
import nc.vo.pu.supqualistatus.SupplierqualityHistoryVO;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pubapp.AppContext;

import org.apache.commons.beanutils.BeanUtils;

public class AceSupplierqualitystatusInsertHistoyRule implements IRule {

	@Override
	public void process(Object[] vos) {
		// 根据字表数据生产历史数据 增加版本id

		if (vos == null || vos.length == 0)
			return;
		try {
			AggSupplierqualityHVO[] bills = (AggSupplierqualityHVO[]) vos;

			List<String> list = new ArrayList<>();
			List<SupplierqualityHistoryVO> volist = new ArrayList<>();
			BaseDAO dao = new BaseDAO();
			for (AggSupplierqualityHVO bill : bills) {
			
				SupplierqualityHistoryVO vo = new SupplierqualityHistoryVO();
					BeanUtils.copyProperties(vo, bill.getParentVO());
					vo.setTs(AppContext.getInstance().getServerTime());
					volist.add(vo);
				list.add(bill.getPrimaryKey());
			}

			if (volist == null || volist.size() == 0)
				return;
			String inwhere = SQLUtil.buildSqlForIn("y.pk_supplierquality",
					list.toArray(new String[list.size()]));
			StringBuffer strb = new StringBuffer();
			strb.append(" select y.pk_supplierquality,max(y.billversion) billversion  from qc_supplierhistory y ");
			strb.append(" where nvl(y.dr,0) = 0 and ");
			strb.append(inwhere);
			strb.append(" group by pk_supplierquality ");
			Map<String, List<SupplierqualityHistoryVO>> retMap = (Map<String, List<SupplierqualityHistoryVO>>) dao
					.executeQuery(strb.toString(), new BeanListMapProcessor<>(
							"pk_supplierquality",
							SupplierqualityHistoryVO.class));

			UFDateTime ts = new UFDateTime();
			for (SupplierqualityHistoryVO vo : volist) {
				int billversion = 1;
				if (retMap != null && retMap.size() > 0) {
					List<SupplierqualityHistoryVO> vlist = retMap.get(vo
							.getPk_supplierquality());
					if (vlist != null && vlist.size() > 0)
						billversion = vlist.get(0).getBillversion() == null ? 1
								: vlist.get(0).getBillversion().intValue() + 1;
				}
				vo.setDef10(ts.toString());
				vo.setBillversion(billversion);
			}

			dao.insertVOArray(volist
					.toArray(new SupplierqualityHistoryVO[volist.size()]));
		} catch (DAOException | IllegalAccessException
				| InvocationTargetException e) {
			Log.getInstance(this.getClass()).error(e);
			ExceptionUtils.asBusinessRuntimeException(e);
		}
	}
}