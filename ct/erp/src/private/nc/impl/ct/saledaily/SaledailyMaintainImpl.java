package nc.impl.ct.saledaily;

import java.util.HashMap;
import java.util.Map;

import nc.bs.ct.saledaily.query.RecPlanVOQueryBP;
import nc.bs.framework.common.NCLocator;
import nc.impl.ct.saledaily.action.MakePaybillAction;
import nc.impl.ct.saledaily.action.SaledailyDeleteAction;
import nc.impl.ct.saledaily.action.SaledailyInsertAction;
import nc.impl.ct.saledaily.action.SaledailyUpdateAction;
import nc.impl.pubapp.pattern.data.bill.BillQuery;
import nc.itf.ct.saledaily.ISaledailyMaintain;
import nc.itf.ct.saledaily.ISaledailyMaintainApp;
import nc.vo.ct.saledaily.entity.AggCtSaleVO;
import nc.vo.ct.saledaily.entity.CtSaleVO;
import nc.vo.ct.saledaily.entity.RecvPlanVO;
import nc.vo.ct.uitl.ValueUtil;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nccloud.util.so.TransBillUtil;

public class SaledailyMaintainImpl implements ISaledailyMaintain {

	@Override
	public void deleteSaledaily(AggCtSaleVO[] bills) throws BusinessException {
		try {
			new SaledailyDeleteAction().delete(bills);
		} catch (Exception ex) {
			ExceptionUtils.marsh(ex);
		}
	}

	// @Override
	// public AggCtSaleVO[] maintainQuery(IQueryScheme queryScheme)
	// throws BusinessException {
	// AggCtSaleVO[] bills = null;
	// try {
	// bills = new SaleQueryBP().query(queryScheme);
	// }
	// catch (Exception e) {
	// ExceptionUtils.marsh(e);
	// }
	// return bills;
	// }

	/**
	 * 父类方法重写
	 * 
	 * @see nc.itf.ct.saledaily.ISaledailyMaintain#makePaybill(nc.vo.ct.saledaily.entity.AggCtSaleVO[])
	 */
	@Override
	public AggregatedValueObject[] makePaybill(AggCtSaleVO[] srcVos) throws BusinessException {
		try {
			return new MakePaybillAction().makePaybill(srcVos);
		} catch (Exception ex) {
			ExceptionUtils.marsh(ex);
			return null;
		}
	}

	/**
	 * 根据表头主键查询Vo
	 * 
	 * @see nc.itf.ct.saledaily.ISaledailyMaintain#queryCtApVoByIds(java.lang.String[])
	 */
	@Override
	public AggCtSaleVO[] queryCtApVoByIds(String[] ids) throws BusinessException {
		if (ValueUtil.isEmpty(ids)) {
			return null;
		}
		AggCtSaleVO[] bills = null;
		try {
			BillQuery<AggCtSaleVO> queryVO = new BillQuery<AggCtSaleVO>(AggCtSaleVO.class);
			bills = queryVO.query(ids);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}

		return bills;
	}

	/**
	 * 取收款计划
	 */
	@Override
	public RecvPlanVO[] queryRecPlanVO(String pk, String vbillcode) throws BusinessException {
		try {
			return new RecPlanVOQueryBP().queryRecPlanVO(pk, vbillcode);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
			return null;
		}
	}

	/**
	 * 销售合同保存动作 父类方法重写
	 * 
	 * @see nc.itf.ct.saledaily.ISaledailyMaintain#save(nc.vo.ct.saledaily.entity.AggCtSaleVO[])
	 */
	@Override
	public AggCtSaleVO[] save(AggCtSaleVO[] bills, AggCtSaleVO[] originBills) throws BusinessException {
		try {
			CtSaleVO ctSaleVO = bills[0].getParentVO();
			if (ctSaleVO.getStatus() == VOStatus.UPDATED) {
				return new SaledailyUpdateAction().update(bills, originBills);
			}
			return new SaledailyInsertAction().insert(bills);
		} catch (Exception ex) {
			ExceptionUtils.marsh(ex);
		}
		return null;
	}

	@Override
	public AggregatedValueObject[] makePaybillByCtID(String[] id_tss) throws BusinessException {
		try {
			Map<String, UFDateTime> id_ts = new HashMap<String, UFDateTime>();
			// 拆分pk和ts
			String[] ids = TransBillUtil.splitBidts(id_tss, id_ts);
			ISaledailyMaintainApp service = 
					 NCLocator.getInstance().lookup(ISaledailyMaintainApp.class);
			AggCtSaleVO[] vos = service.queryMZ3App(ids);
			for (int i = 0; i < vos.length; i++) {
				if (vos[i] == null) {
					vos[i] = new AggCtSaleVO();
					vos[i].setParent(new CtSaleVO());
					vos[i].getParent().setAttributeValue(CtSaleVO.PK_CT_SALE, ids[i]);
				}
				vos[i].getParent().setAttributeValue(CtSaleVO.TS,id_ts.get(ids[i]));
			}
			return new MakePaybillAction().makePaybill(vos);
		} catch (Exception ex) {
			ExceptionUtils.marsh(ex);
			return null;
		}
	}

}
