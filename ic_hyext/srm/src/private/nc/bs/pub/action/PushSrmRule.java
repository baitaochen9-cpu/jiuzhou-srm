package nc.bs.pub.action;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.framework.common.NCLocator;
import nc.cmp.tools.StringUtil;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.vo.ic.m45.entity.PurchaseInVO;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nccloud.api.srm.ISysDispatcher;

public class PushSrmRule implements IRule<PurchaseInVO> {
	public void process(PurchaseInVO[] vos) {

		for (PurchaseInVO vo : vos) {
			try {
				boolean srm = isSrm(vo);
				boolean isToSrm = IsToSrm(vo.getHead().getPk_org());
				if (srm && isToSrm) {
					ISysDispatcher sys = NCLocator.getInstance().lookup(
							ISysDispatcher.class);
					// 采购入库签字推srm
					sys.dispatch(vo, "srm_m45_sign", null);
				}
			} catch (Exception e) {
				ExceptionUtils.wrappException(e);
			}
		}
	}

	public boolean isSrm(PurchaseInVO vo) throws Exception {
		boolean booleanValue;
		if (vo.getHead().getFreplenishflag() == null) {
			return false;
		} else {
			booleanValue = vo.getHead().getFreplenishflag().booleanValue();
		}
		String cgeneralbid = vo.getBodys()[0].getCgeneralbid();

		BaseDAO dao = new BaseDAO();

		String PK = "";
		if (booleanValue) {
			String sql = "select csourcebillhid   from ic_purchasein_b where cgeneralhid = '"
					+ cgeneralbid + "'";
			PK = (String) dao.executeQuery(sql, new ColumnProcessor());
		} else {
			String sql = "select csourceid   from po_arriveorder_b where pk_arriveorder_b =  (select csourcebillbid   from ic_purchasein_b where cgeneralbid = '"
					+ cgeneralbid + "')";
			PK = (String) dao.executeQuery(sql, new ColumnProcessor());
		}
		String billmakerSql = "select   billmaker  from po_order where pk_order = '"
				+ PK + "'";
		String billmaker = (String) dao.executeQuery(billmakerSql,
				new ColumnProcessor());
		String userSql = "select cuserid  from sm_user where user_code = 'SRM'";
		String user = (String) dao.executeQuery(userSql, new ColumnProcessor());
		if (StringUtil.isEmpty(user)) {
			throw new BusinessException("用户SRM未创建");
		}
		if (user.equalsIgnoreCase(billmaker)) {
			return true;
		}
		return false;
	}

	/**
	 * 
	 * 组织参数
	 * 
	 * @param pk_org
	 * @return
	 * @throws DAOException
	 */
	public boolean IsToSrm(String pk_org) throws DAOException {
		String sql = "select distinct value  from  pub_sysinit where initcode='YF_ISSRM' and pk_org = '"
				+ pk_org + "'";
		BaseDAO dao = new BaseDAO();
		String is2crm = (String) dao.executeQuery(sql, new ColumnProcessor());
		if (is2crm == null || StringUtil.isEmpty(is2crm)) {
			return false;
		}
		if ("Y".equalsIgnoreCase(is2crm)) {
			return true;
		}
		return false;
	}

}
