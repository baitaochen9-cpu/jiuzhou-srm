package nc.bs.jzyy.sys.oa.saledaily.rule;

import java.util.List;
import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.itf.scmpub.reference.uap.bd.psn.PsndocPubService;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.vo.ct.entity.CtAbstractVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.pubapp.AppContext;

/**
 * 处理表头人员带出部门信息
 * @Description: 
 *   
 * @author: 刘伟
 * @date:   2019-4-28 下午5:19:31   
 * @version NCC1909
 */
public class ChooseHeadPersionEvent extends CtFieldEvent {

	private CtAbstractVO[] paramArrayOfE = null;

	public ChooseHeadPersionEvent(CtAbstractVO[] hvos){
		this.paramArrayOfE = hvos;
	}
	public void process() {
		for(CtAbstractVO hvo : paramArrayOfE){
			this.setDateWithPersion(hvo);
		}
	}
	
	/**
	 * 根据人员设置信息
	 * @param hvo
	 */
	public void setDateWithPersion(CtAbstractVO hvo) {
		String pk_person = hvo.getPersonnelid();
	    if (pk_person == null) {
	      return;
	    }
	    UFDate busiDate = AppContext.getInstance().getBusiDate();
	    Map<String, List<String>> retMap = null;
	    retMap = PsndocPubService.queryDeptIDByPsndocIDs(new String[] {pk_person});
	    if (retMap != null && !retMap.isEmpty()) {
	      int len = retMap.get(pk_person).size();
	      // 设置部门，如果人员有唯一的部门时。
	      if (len == 1) {
	        String pk_dept = retMap.get(pk_person).get(0);
	        hvo.setAttributeValue(CtAbstractVO.DEPID, pk_dept);
	        String strDate = busiDate.toString();
	        String querySql = " select pk_vid from org_dept_v where pk_dept ='" + pk_dept + "' and '" + strDate + "' < venddate and '" + strDate + "'>= vstartdate; ";
	        String pk_vid = null;
			try {
				pk_vid = (String)new BaseDAO().executeQuery(querySql, new ColumnProcessor());
			} catch (DAOException e) {
				e.printStackTrace();
			}
	        hvo.setAttributeValue(CtAbstractVO.DEPID_V, pk_vid);
	      }

	    }
	}

}
