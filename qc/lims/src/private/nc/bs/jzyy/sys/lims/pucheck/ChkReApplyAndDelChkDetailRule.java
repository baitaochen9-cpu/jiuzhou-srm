package nc.bs.jzyy.sys.lims.pucheck;

import java.util.ArrayList;
import java.util.List;

import nc.impl.pubapp.pattern.data.vo.VOOperator;
import nc.impl.pubapp.pattern.database.DataAccessUtils;
import nc.impl.pubapp.pattern.database.IDExQueryBuilder;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.vo.pu.m23.entity.ArriveBbVO;
import nc.vo.pu.m23.entity.ArriveVO;
import nc.vo.pu.pub.constant.PUTempTable;
import nc.vo.pu.pub.util.AggVOUtil;
import nc.vo.pubapp.pattern.data.IRowSet;
import nc.vo.pubapp.pattern.pub.SqlBuilder;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

public class ChkReApplyAndDelChkDetailRule
implements IRule<ArriveVO>
{
	public void process(ArriveVO[] vos)
	{
		/* 44 */String pk_org = vos[0].getHVO().getPk_org();

		String[] bids = (String[]) AggVOUtil
				.getDistinctItemFieldArray(vos, "pk_arriveorder_b",
						String.class);
		
		String[]  hids= (String[]) AggVOUtil
				.getDistinctItemFieldArray(vos, "pk_arriveorder", String.class);
		
		if (ArrayUtils.isEmpty(bids)) {
			return;
			}
		
		SqlBuilder sql = new SqlBuilder();
		sql.append(" select pk_arriveorder_bb from po_arriveorder_bb");
		sql.append(" where dr = 0 and ");
		IDExQueryBuilder idBuild = new IDExQueryBuilder(
				PUTempTable.tmp_po_23_05.name());
		
		sql.append(idBuild.buildSQL("pk_arriveorder_b", bids));
		sql.append(" and ");
		sql.append(idBuild.buildSQL("pk_arriveorder", hids));
		
		DataAccessUtils utils = new DataAccessUtils();
		IRowSet rowset = utils.query(sql.toString());
		List bbIds = new ArrayList();
		while (rowset.next()) {
			String bbid = rowset.getString(0);
			if ((StringUtils.isNotEmpty(bbid))
					&& (!(bbIds.contains(bbid)))) {
				bbIds.add(bbid);
				}
			}
		if (bbIds.size() == 0) {
			return;
			}
		VOOperator util = new VOOperator();
		String[] bbidArray = (String[]) bbIds.toArray(new String[0]);
		ArriveBbVO[] details = (ArriveBbVO[]) util.query(
				ArriveBbVO.class, bbidArray);
		
		util.delete(details);
		}
	
}