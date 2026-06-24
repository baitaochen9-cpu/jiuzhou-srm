package nc.impl.mmpac.wr.pwr;

import nc.bs.dao.BaseDAO;
import nc.vo.mmpac.wr.entity.AggWrVO;
import nc.vo.mmpac.wr.entity.WrItemVO;
import nc.vo.pub.BusinessException;

import org.apache.commons.lang3.StringUtils;

/**
 * 1.药物科技28  LISM项目处理，
 * 
 * 完工报告需要保存后生成  批次档案，否在报检后无法更新批次档案在检状态
 * 
 * 2. 完工报告保存后生成的库存批次自定项目， 根据生产批次的自动项进行更新
 * @author yunfeng.li
 *
 */
public class WrICBatchcodeRule {
	
	private BaseDAO dao = null;

	 protected BaseDAO getDao() {
			if (dao == null) {
				dao = new BaseDAO();
			}
			return dao;
		}    
	
	public void dealIcBatchcodeUserdef(AggWrVO[]  bills) throws BusinessException{
		
		if(bills == null || bills.length ==0){
			return ;
		}
		
		for(AggWrVO bill:bills){
			String pk_org = bill.getParentVO().getPk_org();
			WrItemVO[] bodys = bill.getChildrenVO();
			for(WrItemVO bvo:bodys){
				String vbinbatchid = bvo.getVbinbatchid();
				if(StringUtils.isNotEmpty(vbinbatchid)){
					String batch_sql=" update scm_batchcode set vdef1='"+bvo.getVbprodbatdef1()+"',vdef2='"+bvo.getVbprodbatdef2()+"',vdef3= '"+bvo.getVbprodbatdef3()+"' " +
							"where  cmaterialvid ='"+bvo.getCbmaterialid()+"' and  pk_batchcode='"+bvo.getVbinbatchid()+"'";
					getDao().executeUpdate(batch_sql);
					
				}
			}
		}
		
	}

}
