package nc.impl.scmf.ic.mbatchcode.rule;

import java.util.ArrayList;
import java.util.List;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.impl.pubapp.pattern.database.DataAccessUtils;
import nc.impl.pubapp.pattern.database.TempTable;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.individuation.property.itf.IPropertyService;
import nc.itf.fi.pub.SysInit;
import nc.vo.bd.meta.BatchOperateVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.JavaType;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pubapp.AppContext;
import nc.vo.pubapp.pattern.data.IRowSet;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.pubapp.pattern.pub.SqlBuilder;
import nc.vo.scmf.ic.mbatchcode.BatchcodeVO;
import nc.vo.scmf.pub.util.BatchCollectionUtils;

/**
 * @description
 *              批次档案保存时 ，存货+批次号进行批次档案集团内唯一性检查
 * @scene
 *        批次档案保存时
 * @param
 * 
 * @author chennn
 */
public class BatchcodeUniqueCheck implements IRule<BatchOperateVO> {

  private static final String TEMP_TABLE_NAME = "temp_ic_batch201501";
  private static final int MAX_COUNT = 100;
  
  private void accept(BatchOperateVO bill, ArrayList<String> pk_invbasdocList,
      ArrayList<String> vbatchcodeList) {
    Object[] upObjs = bill.getUpdObjs();
    Object[] addObjs = bill.getAddObjs();
    if (upObjs != null && upObjs.length > 0) {
      this.addInfoToList(upObjs, pk_invbasdocList, vbatchcodeList);
    }
    if (addObjs != null && addObjs.length > 0) {
      this.addInfoToList(addObjs, pk_invbasdocList, vbatchcodeList);
    }

  }

  @Override
  public void process(BatchOperateVO[] bill) {
    ArrayList<String> vbatchcodeList = new ArrayList<String>();
    ArrayList<String> pk_invbasdocList = new ArrayList<String>();
    this.accept(bill[0], pk_invbasdocList, vbatchcodeList);
    this.checkUnique(vbatchcodeList);

  }

  /**
   * 功能：存货+批次号进行批次档案集团内唯一性检查
   * 创建日期：2009-06-03
   *
   * @author chennn
   */
  private boolean checkUnique(ArrayList<String> vbatchcodeList) {

    if (vbatchcodeList == null || vbatchcodeList.size() <= 0)
      return true;
    String pk_group = InvocationInfoProxy.getInstance().getGroupId(); 
    
   	IPropertyService sv= NCLocator.getInstance().lookup(IPropertyService.class);
	String pkUser = AppContext.getInstance().getPkUser();
	String queryDefaultDBizOrg = null;
	try {
		 queryDefaultDBizOrg = sv.queryDefaultDBizOrg(pkUser, pk_group, "org_df_biz");
	} catch (BusinessException e) {
		ExceptionUtils.wrappBusinessException("获取默认登录组织失败，请设置默认登录组织 ！");
		return false;
	}
	if(null == queryDefaultDBizOrg || queryDefaultDBizOrg.isEmpty() || "~".equals(queryDefaultDBizOrg)){
		ExceptionUtils.wrappBusinessException("获取默认登录组织失败，请设置默认登录组织 ！");
		return false;
	}
    
	UFBoolean paraBoolean = UFBoolean.FALSE;
	 try {
		 paraBoolean = SysInit.getParaBoolean(queryDefaultDBizOrg, "SCM14");
	} catch (BusinessException e) {
		ExceptionUtils.wrappBusinessException("参数【SCM14】获取失败，请联系管理员！问题位置：BatchcodeUniqueCheck.checkUnique()");
		return false;
	}		//检查参数，同单同物料是否采用同一批次号
	
	 if(paraBoolean == UFBoolean.TRUE){
		 //参数允许同单同批次处理，本验证跳过！
		 return true;
	 }
	 
	 
    SqlBuilder sql = new SqlBuilder();
    sql.append("select " + BatchcodeVO.CMATERIALVID
        + ", vbatchcode,count(vbatchcode) from scm_batchcode where dr = 0 and ");
    sql.append("pk_group", pk_group);
    sql.append(" and ");
    //    批次号长度不为20，不能使用IDExQueryBuilder by zhangyan3    
    sql.append(this.getVBatchcodeSQL(vbatchcodeList));
    sql.append(" group by " + BatchcodeVO.CMATERIALVID + ",vbatchcode ");
    sql.append(" having count(vbatchcode)>1");
    DataAccessUtils util = new DataAccessUtils();
    IRowSet rows = util.query(sql.toString());

    if (rows.size() > 0) {
      StringBuilder vbatchcodeBuilder = new StringBuilder();
      vbatchcodeBuilder.append("[");
      while (rows.next()) {
        String vbatchcode = rows.getString(1);
        vbatchcodeBuilder.append(vbatchcode + " ,");

      }
      int index = vbatchcodeBuilder.length();
      vbatchcodeBuilder.deleteCharAt(index - 1);
      vbatchcodeBuilder.append("]");
      String vbatchcodes = vbatchcodeBuilder.toString();
      ExceptionUtils.wrappBusinessException(vbatchcodes + nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("4008028_0","04008028-0034")/*@res "物料+批次号重复!不能保存!"*/);
      // +
      // "物料+批次号重复!不能保存!");
      return false;
    }
    return true;
  }

  private void addInfoToList(Object[] modifyOjbs,
      ArrayList<String> pk_invbasdocList, ArrayList<String> vbatchcodeList) {
    for (int i = 0; i < modifyOjbs.length; i++) {
      String cmaterialvid = ((BatchcodeVO) modifyOjbs[i]).getCmaterialvid();
      String vbatchcode = ((BatchcodeVO) modifyOjbs[i]).getVbatchcode();

      if (cmaterialvid != null && vbatchcode != null) {
        pk_invbasdocList.add(cmaterialvid);
        vbatchcodeList.add(vbatchcode);
      }

    }
  }
  
  private String getVBatchcodeSQL(List<String> paraList){
	  if(paraList.size() > MAX_COUNT){
		  return this.getTempTableSQL(paraList);
	  }else{
		  SqlBuilder sql = new SqlBuilder();
		  sql.append(BatchcodeVO.VBATCHCODE,BatchCollectionUtils.listToArray(paraList));
		  return sql.toString();
	  }
  }

  /**
   * 创建临时表
   * @param batchcodes
   * @return
   */
  private  String getTempTableSQL(List<String> batchcodes) {
	  List<List<Object>> paraList = new ArrayList<List<Object>>();
	  for(String batchcode:batchcodes){
		  List<Object> row = new ArrayList<Object>();
		  row.add(batchcode);
		  paraList.add(row);
	  }
	  TempTable tempTable = new TempTable();
	  int length = new BatchcodeVO().getMetaData().getAttribute(BatchcodeVO.VBATCHCODE).getColumn().getLength();
	  String tempTableName = tempTable.getTempTable(TEMP_TABLE_NAME,
		    	new String[] { BatchcodeVO.VBATCHCODE },
				new String[] { "varchar(" + length + ")" },
				new JavaType[] { JavaType.String }, paraList);
	  SqlBuilder sql = new SqlBuilder();
	  sql.append(BatchcodeVO.VBATCHCODE);
	  sql.append(" in ");
	  sql.startParentheses();
	  sql.append(" select ");
      sql.append(BatchcodeVO.VBATCHCODE);
	  sql.append(" from ");
	  sql.append(tempTableName);
	  sql.endParentheses();
	  return sql.toString();
	  
  }

}