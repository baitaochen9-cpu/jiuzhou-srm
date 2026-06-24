package nc.bs.jzyy.sys.lims.mmwrcheck;

import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.impl.pubapp.pattern.data.vo.VOQuery;
import nc.vo.mmpac.wr.entity.AggWrVO;
import nc.vo.mmpac.wr.entity.WrItemVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;

import org.apache.commons.lang.StringUtils;
/**
 * 1、检查是否质检回写
 * @author yunfeng.li
 *
 */
public class MmwrISCHECKPlugin {
	

	BaseDAO dao;

	public BaseDAO getDao() {
		if (dao == null) {
			dao = new BaseDAO();
		}
		return dao;
	}
	//判断是否同步
	public Object sys(String billltypecode, Object obj,Map<String,Object> otherpms) throws BusinessException{
		AggWrVO bill = (AggWrVO) obj;
		//前台可能没选择行,重新查询
//		BillQuery<AggWrVO> qry = new BillQuery<AggWrVO>(AggWrVO.class);
//		AggWrVO orgiVO = qry.query(new String[]{bill.getPrimaryKey()})[0];
		WrItemVO[] bodys=  (WrItemVO[]) bill.getChildren(WrItemVO.class);
		VOQuery<WrItemVO> qry = new VOQuery<>(WrItemVO.class);
		for(WrItemVO b1:bodys){
			WrItemVO body = qry.query(new String[]{b1.getPrimaryKey()}, null)[0];
//			bbstockbycheck  依据检验结果入库 
			UFBoolean bbstockbycheck =  body.getBbstockbycheck();
			if(bbstockbycheck == null){
				continue;
			}
			if(bbstockbycheck.booleanValue()){
				String vbdef12 = body.getVbdef12();
				if(StringUtils.isEmpty(vbdef12) ||"~".equalsIgnoreCase(vbdef12)){
					continue;
				}
				if(!vbdef12.startsWith("WR5")){
					continue;
				}
				//nbchecknum  检验完成主数量 ,
				UFDouble nbchecknum = body.getNbchecknum();
				if(nbchecknum == null || nbchecknum.doubleValue() ==0){
					throw new BusinessException("根据检验结果入库物料,请等待LIMS检验完成!");
				}


			}
		}
	
		 return bill;
	}
	
	
	

}
