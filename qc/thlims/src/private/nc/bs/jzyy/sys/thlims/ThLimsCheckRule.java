package nc.bs.jzyy.sys.thlims;

import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;

import nc.bs.framework.common.NCLocator;
import nc.bs.trade.business.HYPubBO;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.SQLParameter;
import nc.jdbc.framework.processor.MapProcessor;
import nc.uif.pub.exception.UifException;
import nc.vo.bd.defdoc.DefdocVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;

public class ThLimsCheckRule {

	
	private IUAPQueryBS bs;
	private IUAPQueryBS getQueryBS(){
		if(null==bs){
			bs = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		}
		return bs;
	}
	
	private HYPubBO pubBO;
	private HYPubBO getPubBO(){
		if(null==pubBO){
			pubBO=new HYPubBO();
		}
		return pubBO;
	}
	

	/**
	 * 
	 * @param pk_bgzg 自定义档案标准包装规格的主键
	 * @param nnum 数量
	 * @return 包装数量
	 * @throws BusinessException
	 */
  public UFDouble  dealPackNum(String vfree2,UFDouble nnum) throws BusinessException{
	  
	     UFDouble npacknum = nnum;
		try {
			DefdocVO defdocVO = (DefdocVO) getPubBO().queryByPrimaryKey(DefdocVO.class, vfree2);
			//如果
			 if(null!=defdocVO && StringUtils.isNotEmpty(defdocVO.getDef2())){
				 if(StringUtils.equalsIgnoreCase(defdocVO.getDef2(), "0")){
					
				 }else{
					 //数量向上取整2023年4月3日 计算包装数量
					 double  npacknum2=nnum.div(new UFDouble(defdocVO.getDef2())).doubleValue();
					  //
					 npacknum = new UFDouble(Math.ceil(npacknum2));
					  
				 }
				
			 }
		} catch (UifException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BusinessException("计算包装数量异常:"+e.getMessage());
		}
		
		 return npacknum;
	
  }
  /**
   * 如果是VAR的包装数量返回1 否在返回原包装数量
   * @param pk_bgzg
   * @param nnum
   * @return
   * @throws BusinessException
   */
  public UFDouble  isVar(String vfree2,UFDouble npacknum) throws BusinessException{
	  
		if(StringUtils.isEmpty(vfree2)){
			 return npacknum;
		}
		DefdocVO defdocVO=(DefdocVO) this.getPubBO().queryByPrimaryKey(DefdocVO.class,vfree2);
		 if("VAR".equals(defdocVO.getCode()) || "NA".equals(defdocVO.getCode())){
			return UFDouble.ONE_DBL;
		 }else{
			 return npacknum;
		 }
	 
	}


	/* * 检查是否直接下一步物料校验
	 * 2022年12月11日
	 * */
	public boolean isNextMaterCheck(String pk_material) throws BusinessException{
		String q_sql="select  blist.code,mater.pk_material,def.code,def.name,def.enablestate from  bd_defdoclist blist,bd_defdoc def,bd_material mater " +
				"where blist.code='H3010125' and def.pk_defdoclist=blist.pk_defdoclist and def.dr=0 and def.enablestate=2 " +
				"and def.code=mater.code and mater.dr=0 and mater.enablestate=2 and mater.pk_material=?";

		SQLParameter parameter=new SQLParameter();
		parameter.addParam(pk_material);
		HashMap<String, Object> hashMap = (HashMap<String, Object>) this.getQueryBS().executeQuery(q_sql,parameter ,new MapProcessor());
		
		if (hashMap != null && hashMap.size() > 0) {
			return true;
		}
		return false;
	}
	
	/**
	 * 是否直接下一步物料校验,直接下一步物料直接报错
	 * @param pk_material
	 * @throws BusinessException
	 */
	public void isNextMaterCtr(String pk_material) throws BusinessException{
		boolean check = isNextMaterCheck(pk_material);
		if (check) {
			throw new BusinessException("在直接下一步物料档案中,不可报检!");
		}
	}
	
	
	
	/**
	 * // 检查物料是否免检,如果是免检直接报错
	 * @param pk_org
	 * @param material
	 * @return
	 * @throws BusinessException
	 */
	public void isQcCtr(String pk_org, String pk_material)
			throws BusinessException {
		boolean chkfreeflag = isQcCheck(pk_org, pk_material);
		if(chkfreeflag){
			throw new BusinessException("免检物料不可报检!");
		}
	}
	
	/**
	 * // 检查物料是否免检
	 * @param material
	 * @return
	 * @throws BusinessException
	 */
	public boolean isQcCheck(String pk_org, String pk_material)
			throws BusinessException {
		String sql = " select chkfreeflag    from bd_materialstock where pk_material='"
				+ pk_material + "' and   pk_org ='" + pk_org + "' and dr=0";

		HashMap<String, Object> hashMap2 = (HashMap<String, Object>) this.getQueryBS().executeQuery(sql, new MapProcessor());

		if (hashMap2 != null && hashMap2.size() > 0) {
			String s = hashMap2.get("chkfreeflag").toString();
			if(StringUtils.isEmpty(s) ||"~".equalsIgnoreCase(s)){
				s="N";
			}
			UFBoolean chkfreeflag = UFBoolean.valueOf(s);
			return chkfreeflag.booleanValue();
		}
		return false;
	}
	
}
