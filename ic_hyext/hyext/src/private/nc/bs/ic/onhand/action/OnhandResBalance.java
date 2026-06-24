package nc.bs.ic.onhand.action;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.framework.common.NCLocator;
import nc.bs.ic.pub.env.ICBSContext;
import nc.impl.pubapp.pattern.rule.processer.AroundProcesser;
import nc.itf.scmpub.reference.uap.group.SysInitGroupQuery;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.jdbc.framework.processor.MapProcessor;
import nc.pubitf.ic.onhand.IOnhandQry;
import nc.vo.ic.location.ICLocationVO;
import nc.vo.ic.material.define.InvBasVO;
import nc.vo.ic.material.define.InvMeasVO;
import nc.vo.ic.onhand.define.BalanceOnhandRes;
import nc.vo.ic.onhand.define.ICBillOnhandReq;
import nc.vo.ic.onhand.define.IOnhandReq;
import nc.vo.ic.onhand.define.OnhandBalanceResult;
import nc.vo.ic.onhand.define.OnhandRes;
import nc.vo.ic.onhand.entity.OnhandDimVO;
import nc.vo.ic.onhand.entity.OnhandSNVO;
import nc.vo.ic.onhand.entity.OnhandSNViewVO;
import nc.vo.ic.onhand.entity.OnhandVO;
import nc.vo.ic.onhand.pub.OnhandSelectDim;
import nc.vo.ic.onhand.pub.OnhandVOTools;
import nc.vo.ic.pub.calc.BusiCalculator;
import nc.vo.ic.pub.define.ICPubMetaNameConst;
import nc.vo.ic.pub.util.CollectionUtils;
import nc.vo.ic.pub.util.DimMatchUtil;
import nc.vo.ic.pub.util.NCBaseTypeUtils;
import nc.vo.ic.pub.util.StringUtil;
import nc.vo.ic.pub.util.VOEntityUtil;
import nc.vo.ic.pub.util.ValueCheckUtil;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nccloud.base.type.ValueUtils;

/**
 * <p>
 * <b>结存资源平衡</b>
 * <ul>
 * <li>捡货的现存量处理分配
 * <li>预留的现存量分配
 * </ul>
 * <p>
 * <b>变更历史：</b>
 * <p>
 * 
 * @version v60
 * @since v60
 * @author yangb
 * @time 2010-4-10 下午02:13:20
 */
public class OnhandResBalance<T extends IOnhandReq> implements ResAlloc {

  private BusiCalculator calculator;

  // 使用交易类型作为结存的过滤条件
  private List<String> ctranstypes;

  // 分配结果
  private Map<T, OnhandBalanceResult<T>> mapResults;
  
//  // 分配结果
//  private Map<T, List<OnhandSNViewVO>> mapSnResults;

  // 现有的结存
  private Map<String, OnhandVO> mapyethandvo;

  // 需求对象
  private T[] onhandReq;
  
  // 序列号需求对象
  private T[] onhandSNReq;

  private OnhandResManager onhandres;

  // 需求与结存资源的匹配器
  private DimMatchUtil<T> reqsmatch;

  /**
   * OnhandResBalance 的构造子
   */
  public OnhandResBalance(T[] reqs) {
    this.onhandReq = reqs;
    this.calculator = BusiCalculator.getBusiCalculatorAtBS();
  }

  /**
   * 赋予资源 return true ---表示该资源已能满足需求, 或明细已经超过当前结存,无需继续搜索
   */
  @Override
  public boolean assignRes(OnhandRes handres) {
    List<T> lreqs = this.reqsmatch.searchMatchedDimObj(handres, -1);
    if (ValueCheckUtil.isNullORZeroLength(lreqs)) {
      return true;
    }
    boolean byetoverhand = this.processResDataForHand(handres);
    OnhandBalanceResult<T> balancerow = null;
    int ireqs = lreqs.size();
    for (T req : lreqs) {
      balancerow = this.mapResults.get(req);
      if (balancerow == null) {
        balancerow = new OnhandBalanceResult<T>(req);
        this.mapResults.put(req, balancerow);
      }
      // 如果调用balancerow.isFull() 辅单位不记结存的情况下，存量辅数量为空或0，会认为没有满足,改为调用balancerow.isFull(boolean,boolean,boolean)
      boolean isNumAssign = handres.isNumUseable();
      boolean isAstNumAssign = handres.isAstnumUseable();
      boolean isGrossNumAssign = handres.isGrossnumUseable();
      this.assignRes(balancerow, handres);      
      if (balancerow.isFull(isNumAssign, isAstNumAssign, isGrossNumAssign)) {
        this.reqsmatch.remoteMatchedDimObj(req);
        ireqs--;
      }
      if (!handres.isUseable()) {
        break;
      }
    }
    if (byetoverhand) {
      return true;
    }
    if (ireqs <= 0) {
      return true;
    }
    return false;
  }

  /**
   * 方法功能描述：获取平衡结果 <b>参数说明</b>
   * 
   * @author yangb
   * @time 2010-4-10 下午03:10:01
   */
  public OnhandBalanceResult<T> getResults(T req) {
    if (this.mapResults == null) {
      return null;
    }
	return this.mapResults.get(req);
  }

  /**
   * 方法功能描述：现存量平衡计算 <b>参数说明</b>
   * 
   * @author yangb
   * @time 2010-4-10 下午03:10:01
   */
  public void onhandBalance() {
    // 检查数据
    this.processData();
    //  add by wangceb 序列号拣货 start
    this.processAllocSNData();
    //  add by wangceb 序列号拣货 end
    
    if (ValueCheckUtil.isNullORZeroLength(this.onhandReq)
        || this.reqsmatch == null) {
      return;
    }
    if (ValueCheckUtil.isNullORZeroLength(this.mapyethandvo)) {
      return;
    }
    OnhandVO[] handvos =
      this.mapyethandvo.values().toArray(
          new OnhandVO[this.mapyethandvo.size()]);
    // 分配明细
    this.getOnhandRes().matchRes(handvos, this);
    // 分配单品
    this.allocSn(handvos);
  }

  /**
   * @param ctranstype 要设置的 ctranstype
   */
  public void setCtranstype(List<String> ctranstypes) {
    this.ctranstypes = ctranstypes;
  }

  /**
   * 分配单品,基于已分配结存的结果
   */
  @SuppressWarnings("restriction")
private void allocSn(OnhandVO[] handvos) {
    if (ValueCheckUtil.isNullORZeroLength(handvos)
        || ValueCheckUtil.isNullORZeroLength(this.mapResults)) {
      return;
    }
    Set<String> onhandpks =
        VOEntityUtil.getVOsValueSet(handvos, OnhandDimVO.PK_ONHANDDIM);

    // 查询单品资源
    List<String>[] ls = this.getOuttrackinOnhandRes();
    if (!ValueCheckUtil.isNullORZeroLength(ls)
        && !ValueCheckUtil.isNullORZeroLength(ls[0])) {
      this.getOnhandRes().loadSNRes(handvos,
          ls[0].toArray(new String[ls[0].size()]),
          ls[1].toArray(new String[ls[1].size()]));
    }
    else {
      this.getOnhandRes().loadSNRes(handvos, null, null);
    }
    OnhandSNVO[] snvos = null;
    for (OnhandBalanceResult<T> rts : this.mapResults.values()) {
      if (rts.getResults() == null) {
        continue;
      }
      for (BalanceOnhandRes res : rts.getResults()) {
        // 待处理存量主键如果不含资源中的存量主键，说明已经处理，不再处理
        if (res.getNastnum() == null
            || !onhandpks.contains(res.getOnhanddimvo().getPk_onhanddim())) {
          continue;
        }
        // 单品
        snvos = this.getOnhandRes().getSNRes(res.getOnhanddimvo(),
            res.getCgeneralbid(), res.getNastnum().intValue());
        res.addSnVo(snvos);
      }
    }
  }

  /**
   * 方法功能描述：分配计算 <b>参数说明</b>
   * 
   * @author yangb
   * @time 2010-4-10 下午03:10:01
   */
  private void assignRes(OnhandBalanceResult<T> balance, OnhandRes res) {
    if (!res.isUseable()) {
      return;
    }
    UFDouble dtemp = null;
    BalanceOnhandRes balanceres = new BalanceOnhandRes(res);
    //

     
     


    
//    
     BaseDAO dao =new BaseDAO();
     List<Map<String,Object>>list=null;
     String pk_org=res.getOnhanddimvo().getPk_org();
//     查询序列号  是否需要向上取整  序列号不能为小数
     UFBoolean b=getIsInt(pk_org);
	 String sql="select a.nonhandastnum, a.nonhandnum\n" +
	    				 "  from ic_onhandsn a\n" + 
	    				 " where a.pk_onhanddim = '"+res.getOnhanddimvo().getPk_onhanddim()+"'\n" + 
	    				 " order by nonhandastnum desc";
	     
	     try {
			list= (List<Map<String,Object>>)dao.executeQuery(sql, new MapListProcessor());
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//	     参数设置  根据序列号取整 并且该物料启用了序列号  序列号档案里有数据
	     if(b.booleanValue()&&list!=null&&list.size()>0){
//	     if(list)
	     if (res.isNumUseable()) {
	         dtemp = NCBaseTypeUtils.sub(res.getNuseablenum(), balance.getNreqnum());
	         if (NCBaseTypeUtils.isGtZero(dtemp)) {
//	       	  现存量数量大于要分拣的数量
	       		  if(dtemp.compareTo(UFDouble.ZERO_DBL)!=0){
	       	        	UFDouble nmuber=UFDouble.ZERO_DBL;
	       	        	if(list!=null){
	       	        		for(int i=0;i<list.size();i++){
	       	            		nmuber=nmuber.add(Double.valueOf(list.get(i).get("nonhandnum").toString()));
	       	            		if(nmuber.compareTo(balance.getNreqnum())>=0){
	       	            			break;
	       	            		}
	       	            	}
	       	        		balanceres.setNnum(nmuber);
	       	   		        balance.setNreqnum(UFDouble.ZERO_DBL);
	       	   		        res.setNuseablenum(dtemp);
	       	        	}     
	       	        }else{
	       	        	  balanceres.setNnum(balance.getNreqnum());
	       	    	        balance.setNreqnum(UFDouble.ZERO_DBL);
	       	    	        res.setNuseablenum(dtemp);
	       	        }
	         }
	         else {
	           balanceres.setNnum(res.getNuseablenum());
	           balance.setNreqnum(NCBaseTypeUtils.sub(balance.getNreqnum(), balanceres
	               .getNnum()));
	           res.setNuseablenum(UFDouble.ZERO_DBL);
	         }
	       }else{
	         balanceres.setNnum(UFDouble.ZERO_DBL);
	       }
	       

	       if (res.isAstnumUseable()) {
	         dtemp =
	           NCBaseTypeUtils.sub(res.getNuseableastnum(), balance.getNreqastnum());
	         if (NCBaseTypeUtils.isGtZero(dtemp)) {
	          
	           	   if(dtemp.compareTo(UFDouble.ZERO_DBL)!=0){
	           	      	UFDouble nmuber=UFDouble.ZERO_DBL;
	           	      	if(list!=null){
	           	      		for(int i=0;i<list.size();i++){
	           	          		nmuber=nmuber.add(Double.valueOf(list.get(i).get("nonhandastnum").toString()));
	           	          		if(nmuber.compareTo(balance.getNreqastnum())>=0){
	           	          			break;
	           	          		}
	           	          		
	           	          	}
	           	           balanceres.setNastnum(nmuber);
	           	            balance.setNreqastnum(UFDouble.ZERO_DBL);
	           	           res.setNuseableastnum(dtemp);
	           	      	}
	           	      }else{
	           	    	  balanceres.setNastnum(balance.getNreqastnum());
	           	          balance.setNreqastnum(UFDouble.ZERO_DBL);
	           	          res.setNuseableastnum(dtemp);
	           	          
	           	      }

	         }
	         else {
	           balanceres.setNastnum(res.getNuseableastnum());
	           balance.setNreqastnum(NCBaseTypeUtils.sub(balance.getNreqastnum(),
	               balanceres.getNastnum()));
	           res.setNuseableastnum(UFDouble.ZERO_DBL);
	         }
	       }else{
	         balanceres.setNastnum(UFDouble.ZERO_DBL);
	       }

	       if (res.isGrossnumUseable()) {
	         dtemp =
	           NCBaseTypeUtils.sub(res.getNuseablegrossnum(), balance
	               .getNreqgrossnum());
	         if (NCBaseTypeUtils.isGtZero(dtemp)) {
	       	  
	           balanceres.setNgrossnum(balance.getNreqgrossnum());
	           balance.setNreqgrossnum(UFDouble.ZERO_DBL);
	           res.setNuseablegrossnum(dtemp);
	         }
	         else {
	           balanceres.setNgrossnum(res.getNuseablegrossnum());
	           balance.setNreqgrossnum(NCBaseTypeUtils.sub(balance.getNreqgrossnum(),
	               balanceres.getNgrossnum()));
	           res.setNuseablegrossnum(UFDouble.ZERO_DBL);
	         }
	       }else{
	         balanceres.setNgrossnum(UFDouble.ZERO_DBL);
	       }
	       balance.addBalanceOnhandRes(balanceres);
	     
	     
	 }else{
		 if (res.isNumUseable()) {
		      dtemp = NCBaseTypeUtils.sub(res.getNuseablenum(), balance.getNreqnum());
		      if (NCBaseTypeUtils.isGtZero(dtemp)) {
		        balanceres.setNnum(balance.getNreqnum());
		        balance.setNreqnum(UFDouble.ZERO_DBL);
		        res.setNuseablenum(dtemp);
		      }
		      else {
		        balanceres.setNnum(res.getNuseablenum());
		        balance.setNreqnum(NCBaseTypeUtils.sub(balance.getNreqnum(), balanceres
		            .getNnum()));
		        res.setNuseablenum(UFDouble.ZERO_DBL);
		      }
		    }else{
		      balanceres.setNnum(UFDouble.ZERO_DBL);
		    }
		    

		    if (res.isAstnumUseable()) {
		      dtemp =
		        NCBaseTypeUtils.sub(res.getNuseableastnum(), balance.getNreqastnum());
		      if (NCBaseTypeUtils.isGtZero(dtemp)) {
		        balanceres.setNastnum(balance.getNreqastnum());
		        balance.setNreqastnum(UFDouble.ZERO_DBL);
		        res.setNuseableastnum(dtemp);
		      }
		      else {
		        balanceres.setNastnum(res.getNuseableastnum());
		        balance.setNreqastnum(NCBaseTypeUtils.sub(balance.getNreqastnum(),
		            balanceres.getNastnum()));
		        res.setNuseableastnum(UFDouble.ZERO_DBL);
		      }
		    }else{
		      balanceres.setNastnum(UFDouble.ZERO_DBL);
		    }

		    if (res.isGrossnumUseable()) {
		      dtemp =
		        NCBaseTypeUtils.sub(res.getNuseablegrossnum(), balance
		            .getNreqgrossnum());
		      if (NCBaseTypeUtils.isGtZero(dtemp)) {
		        balanceres.setNgrossnum(balance.getNreqgrossnum());
		        balance.setNreqgrossnum(UFDouble.ZERO_DBL);
		        res.setNuseablegrossnum(dtemp);
		      }
		      else {
		        balanceres.setNgrossnum(res.getNuseablegrossnum());
		        balance.setNreqgrossnum(NCBaseTypeUtils.sub(balance.getNreqgrossnum(),
		            balanceres.getNgrossnum()));
		        res.setNuseablegrossnum(UFDouble.ZERO_DBL);
		      }
		    }else{
		      balanceres.setNgrossnum(UFDouble.ZERO_DBL);
		    }
		    balance.addBalanceOnhandRes(balanceres);
	 }
    
    
   
  }

  
  /**
   * 方法功能描述：分配计算 <b>参数说明</b>
   * 
   * @author yangb
   * @time 2010-4-10 下午03:10:01
   */
//  private void assignRes(OnhandBalanceResult<T> balance, OnhandRes res) {
//    if (!res.isUseable()) {
//      return;
//    }
//    UFDouble dtemp = null;
//    BalanceOnhandRes balanceres = new BalanceOnhandRes(res);
//    // //单品
//    // OnhandSNVO[] snvos = getOnhandRes().getSNRes(res.getOnhanddimvo(),
//    // res.getCgeneralbid(),
//    // balance.getNreqnum().intValue());
//    // balanceres.addSnVo(snvos);
//    if (res.isNumUseable()) {
//      dtemp = NCBaseTypeUtils.sub(res.getNuseablenum(), balance.getNreqnum());
//      if (NCBaseTypeUtils.isGtZero(dtemp)) {
//        balanceres.setNnum(balance.getNreqnum());
//        balance.setNreqnum(UFDouble.ZERO_DBL);
//        res.setNuseablenum(dtemp);
//      }
//      else {
//        balanceres.setNnum(res.getNuseablenum());
//        balance.setNreqnum(NCBaseTypeUtils.sub(balance.getNreqnum(), balanceres
//            .getNnum()));
//        res.setNuseablenum(UFDouble.ZERO_DBL);
//      }
//    }else{
//      balanceres.setNnum(UFDouble.ZERO_DBL);
//    }
//    
//
//    if (res.isAstnumUseable()) {
//      dtemp =
//        NCBaseTypeUtils.sub(res.getNuseableastnum(), balance.getNreqastnum());
//      if (NCBaseTypeUtils.isGtZero(dtemp)) {
//        balanceres.setNastnum(balance.getNreqastnum());
//        balance.setNreqastnum(UFDouble.ZERO_DBL);
//        res.setNuseableastnum(dtemp);
//      }
//      else {
//        balanceres.setNastnum(res.getNuseableastnum());
//        balance.setNreqastnum(NCBaseTypeUtils.sub(balance.getNreqastnum(),
//            balanceres.getNastnum()));
//        res.setNuseableastnum(UFDouble.ZERO_DBL);
//      }
//    }else{
//      balanceres.setNastnum(UFDouble.ZERO_DBL);
//    }
//
//    if (res.isGrossnumUseable()) {
//      dtemp =
//        NCBaseTypeUtils.sub(res.getNuseablegrossnum(), balance
//            .getNreqgrossnum());
//      if (NCBaseTypeUtils.isGtZero(dtemp)) {
//        balanceres.setNgrossnum(balance.getNreqgrossnum());
//        balance.setNreqgrossnum(UFDouble.ZERO_DBL);
//        res.setNuseablegrossnum(dtemp);
//      }
//      else {
//        balanceres.setNgrossnum(res.getNuseablegrossnum());
//        balance.setNreqgrossnum(NCBaseTypeUtils.sub(balance.getNreqgrossnum(),
//            balanceres.getNgrossnum()));
//        res.setNuseablegrossnum(UFDouble.ZERO_DBL);
//      }
//    }else{
//      balanceres.setNgrossnum(UFDouble.ZERO_DBL);
//    }
//    balance.addBalanceOnhandRes(balanceres);
//  }
  
  private UFBoolean getIsInt(String pk_org) {
	// TODO Auto-generated method stub
	  IUAPQueryBS iuap = (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		String sql = 
		"select nvl(a.value,0)   as value " + "\n" +
				"from  pub_sysinit a " + "\n" + 
				"where a.initcode ='YF607' and  a.pk_org='"+pk_org+"'";
try {
	Map map = (Map)iuap.executeQuery(sql, new MapProcessor());
	String kzlx="否";
	if(map.get("value") != null){
		 kzlx = map.get("value").toString() ;
	}
	if("否".equals(kzlx)||"N".equals(kzlx)){
		return UFBoolean.FALSE;
	}else{
		return UFBoolean.TRUE;
	}
} catch (BusinessException e1) {
	// TODO Auto-generated catch block
	e1.printStackTrace();
	return UFBoolean.FALSE;
}
}

/**
   * 方法功能描述：过滤需求 <b>参数说明</b>
   * 
   * @author yangb
   * @time 2010-4-10 下午03:10:01
   */
  @SuppressWarnings("unchecked")
  private void filterReqsData() {
    if (ValueCheckUtil.isNullORZeroLength(this.onhandReq)) {
      return;
    }
    List<T> datas = new ArrayList<T>();
    List<T> sndatas = new ArrayList<T>();
    for (T req : this.onhandReq) {
      if (req == null) {
        continue;
      }
      if (req.getOnhandDim() == null) {
        continue;
      }
      if (req.getOnhandDim().getPk_org() == null
          || req.getOnhandDim().getCmaterialvid() == null
          || req.getOnhandDim().getCmaterialoid() == null) {
        continue;
      }
      if (NCBaseTypeUtils.isNullOrZero(req.getReqNum())
          && NCBaseTypeUtils.isNullOrZero(req.getReqAssistNum())
          && NCBaseTypeUtils.isNullOrZero(req.getReqGrossNum())) {
        continue;
      }
      if (this.isNeedSearchSN(req)) {
    	  sndatas.add(req);
    	  continue;
      }
      
      datas.add(req);
    }
    if (datas.size() > 0) {
      T[] arrtemp =
        (T[]) Array.newInstance(datas.get(0).getClass(), datas.size());
      this.onhandReq = datas.toArray(arrtemp);
    } else {
      this.onhandReq = null;
    }
    
    if (sndatas.size() > 0) {
        T[] arrtemp =
          (T[]) Array.newInstance(sndatas.get(0).getClass(), sndatas.size());
        this.onhandSNReq = sndatas.toArray(arrtemp);
      }
  }

  /**
   * 孙表录入序列号且序列号数量与应发数量相同
   * @param req
   * @return
   */
  private boolean isNeedSearchSN(T req) {
	ICLocationVO[] locationvos = req.getLocationVOs();
      if (ValueCheckUtil.isNullORZeroLength(locationvos)) {
    	  return false;
      }
      String key = SysInitGroupQuery.isSNEnabled() ? ICPubMetaNameConst.PK_SERIALCODE : ICPubMetaNameConst.VSERIALCODE;
      String[] snvalues = VOEntityUtil.getVOsValuesNotDel(locationvos, key, String.class);
      if (StringUtil.isSEmptyOrNullForAll(snvalues)) {
    	  return false;
      }
      if (NCBaseTypeUtils.isNullOrZero(req.getReqAssistNum()) || snvalues.length != req.getReqAssistNum().intValue()) {
    	  return false;
      }
	return true;
  }

  /**
   * 方法功能描述：获取现存量资源 <b>参数说明</b>
   * 
   * @author yangb
   * @time 2010-4-10 下午03:10:01
   */
  private OnhandResManager getOnhandRes() {
    if (this.onhandres == null) {
      this.onhandres = new OnhandResManager();
      if (!ValueCheckUtil.isNullORZeroLength(this.ctranstypes)) {
        this.onhandres.setCtranstype(this.ctranstypes);
      }
    }
    return this.onhandres;
  }

  /**
   * 获取出库跟踪入库的入库单明细
   */
  @SuppressWarnings("unchecked")
  private List<String>[] getOuttrackinOnhandRes() {
    if (ValueCheckUtil.isNullORZeroLength(this.mapResults)) {
      return null;
    }
    List<String> listbid = new ArrayList<String>();
    List<String> listbilltype = new ArrayList<String>();
    String cgeneralbid = null;
    for (OnhandBalanceResult<T> rts : this.mapResults.values()) {
      if (rts.getResults() == null) {
        continue;
      }
      for (BalanceOnhandRes res : rts.getResults()) {
        cgeneralbid = res.getCgeneralbid();
        if (cgeneralbid == null) {
          continue;
        }
        if (listbid.contains(cgeneralbid)) {
          continue;
        }
        listbid.add(cgeneralbid);
        listbilltype.add(res.getCbilltype());
      }
    }
    if (listbid.size() <= 0) {
      return null;
    }
    return new List[] {
        listbilltype, listbid
    };
  }

  /**
   * 方法功能描述： 预分配现存量资源,对于: 非(先进先出,后进先出) 非(出库跟踪入库) 的物料,<br>
   * 可以直接根据现存量分配资源即可,<br>
   * 不用继续查询单据明细,这样提高效率<br>
   * 
   * @author yangb
   * @time 2010-4-10 下午03:10:01
   */
  private OnhandVO[] preAllocHandRes(OnhandVO[] handvos) {
    if (ValueCheckUtil.isNullORZeroLength(handvos)) {
      return handvos;
    }
    this.getOnhandRes().loadInv(handvos);
    this.processNrsastnum(handvos);
    List<OnhandVO> lrethandvos = new ArrayList<OnhandVO>();
    List<OnhandVO> lprehandvos = new ArrayList<OnhandVO>();
    for (OnhandVO handvo : handvos) {
      if (NCBaseTypeUtils.isLEZero(OnhandVOTools.calcHandNum(handvo))
          && NCBaseTypeUtils.isLEZero(OnhandVOTools.calcHandAstNum(handvo))) {
        continue;
      }
      if (this.getOnhandRes().isNeedSearchFlow(handvo.getPk_org(),
          handvo.getCmaterialvid())) {
        lrethandvos.add(handvo);
        continue;
      }
      OnhandRes handres = new OnhandRes(handvo);
      List<T> lreqs = this.reqsmatch.searchMatchedDimObj(handres, -1);
      if (ValueCheckUtil.isNullORZeroLength(lreqs)) {
        continue;
      }
      OnhandBalanceResult<T> balancerow = null;
      lprehandvos.add(handvo);
      for (T req : lreqs) {
        balancerow = this.mapResults.get(req);
        if (balancerow == null) {
          balancerow = new OnhandBalanceResult<T>(req);
          this.mapResults.put(req, balancerow);
        }
        boolean isNumAssign=handres.isNumUseable();
        boolean isAstNumAssign=handres.isAstnumUseable();
        boolean isGrossNumAssign= handres.isGrossnumUseable();
//        TODO
        this.assignRes(balancerow, handres);
        if (balancerow.isFull(isNumAssign,isAstNumAssign,isGrossNumAssign)) {
          this.reqsmatch.remoteMatchedDimObj(req);
        }
        if (!handres.isUseable()) {
          break;
        }
      }
    }
    if (lprehandvos.size() > 0) {
      this.allocSn(CollectionUtils.listToArray(lprehandvos));
    }
    return CollectionUtils.listToArray(lrethandvos);
  }
  
  /**
   * 辅计量记结存的，并且有预留存量的，要重新计算预留辅数量，否则拣货后单据辅数量错误
   * @param handvos
   */
  private void processNrsastnum(OnhandVO[] handvos){
    Map<String, InvMeasVO> mapmeasInv =this.loadInvMeas(handvos);
    if (ValueCheckUtil.isNullORZeroLength(mapmeasInv)) {
      return ;
    }
    List<OnhandVO> reserveVOs = new ArrayList<OnhandVO>();
    
    for (OnhandVO onhandVO : handvos) {
      String key = onhandVO.getCmaterialvid()+onhandVO.getCastunitid();
      InvMeasVO invmeasVO = mapmeasInv.get(key);
      if(invmeasVO == null){
        reserveVOs.add(onhandVO);
        continue;
      }
      if(ValueCheckUtil.isTrue(invmeasVO.getIsstorebalance()) 
          && !NCBaseTypeUtils.isNullOrZero(onhandVO.getNrsnum())){
        reserveVOs.add(onhandVO);
      }
    }
    if(ValueCheckUtil.isNullORZeroLength(reserveVOs)){
      return;
    }
//    Map<String, InvBasVO> invvos =
//        this.loadInvBas(reserveVOs.toArray(new OnhandVO[0]));
    for (OnhandVO onhandVO : reserveVOs) {
//      InvBasVO invvo = invvos.get(onhandVO.getCmaterialvid());
//      if (invvo == null) {
//        continue;
//      }
//      String cunitid = invvo.getPk_measdoc();
      InvMeasVO invmeasvo =
          mapmeasInv.get(onhandVO.getCmaterialvid() + onhandVO.getCastunitid());
      if (invmeasvo == null) {
        onhandVO.setNrsastnum(onhandVO.getNrsnum());
        continue;
      }
      String convertRate = invmeasvo.getMeasrate();
      String cunitid = invmeasvo.getPk_measdoc();
      UFDouble nrsnum = onhandVO.getNrsnum();
      onhandVO.setNrsastnum(this.calculator.calculateAstNum(nrsnum,
          convertRate, cunitid));
    }
  }  
 
  /**
   * 加载物料辅计量信息
   * 
   * @param handvos
   * @return
   */
  private Map<String, InvMeasVO> loadInvMeas(OnhandVO[] handvos) {
    Map<String, InvMeasVO> mapInv = new HashMap<String, InvMeasVO>();
    if (handvos == null || handvos.length <= 0) {
      return null;
    }

    ICBSContext con = new ICBSContext();
    InvMeasVO[] invvos =
        con.getInvInfo().getInvMeasVO(
            VOEntityUtil.getVOsValues(handvos, OnhandDimVO.CMATERIALVID,
                String.class),
            VOEntityUtil.getVOsValues(handvos, OnhandDimVO.CASTUNITID,
                String.class));
    if (invvos == null) {
      return null;
    }
    for (InvMeasVO invvo : invvos) {
      if (invvo == null) {
        continue;
      }
      mapInv.put(invvo.getPk_material() + invvo.getPk_measdoc(), invvo);
    }
    return mapInv;
  }
 
  /**
   * 加载物料基本信息，用于获取主单位
   * @param handvos
   * @return
   */
  private Map<String, InvBasVO> loadInvBas(OnhandVO[] handvos) {
    Map<String, InvBasVO> mapInv = new HashMap<String, InvBasVO>();
    if (handvos == null || handvos.length <= 0) {
      return null;
    }

    ICBSContext con = new ICBSContext();
    InvBasVO[] invvos =
        con.getInvInfo().getInvBasVO(
            VOEntityUtil.getVOsValues(handvos, OnhandDimVO.CMATERIALVID,
                String.class));
    if (invvos == null) {
      return null;
    }
    for (InvBasVO invvo : invvos) {
      if (invvo == null) {
        continue;
      }
      mapInv.put(invvo.getPk_material(), invvo);
    }
    return mapInv;
  }
  
  /**
   * 根据序列号拣货
   */
  private void processAllocSNData() {
	if (ValueCheckUtil.isNullORZeroLength(onhandSNReq)) {
		return;
	}
	// 取得所有查询key -- 序列号或者pk
	Set<String> llist = this.getAllSNValues();
	// 查询序列号结存
	OnhandSNViewVO[] snvos = this.queryOnhandSNs(llist);
	if (ValueCheckUtil.isNullORZeroLength(snvos)) {
		return;
	}
	if (ValueCheckUtil.isNullORZeroLength(this.mapResults)) {
		this.mapResults = new HashMap<T, OnhandBalanceResult<T>>();
	}
	String key = SysInitGroupQuery.isSNEnabled() ? ICPubMetaNameConst.PK_SERIALCODE
			: ICPubMetaNameConst.VSERIALCODE;

	Map<String, OnhandSNViewVO> onhandsnmap = CollectionUtils.hashVOArray(
			key, snvos);
	for (T req : onhandSNReq) {
		// 处理每个需求对象
		this.processAllocSNReq(key, onhandsnmap, req);
	}
  }

  private void processAllocSNReq(String key,
		Map<String, OnhandSNViewVO> onhandsnmap, T req) {
	OnhandBalanceResult<T> balancerow = this.mapResults.get(req);
	if (balancerow == null) {
		balancerow = new OnhandBalanceResult<T>(req);
		this.mapResults.put(req, balancerow);
	}
	List<OnhandSNViewVO> snList = new ArrayList<OnhandSNViewVO>();
	// 选择孙表对应的序列号结存
	ICLocationVO[] lovs = req.getLocationVOs();
	for (ICLocationVO lov : lovs) {
		String keyvalue = (String) lov.getAttributeValue(key);
		snList.add(onhandsnmap.get(keyvalue));
	}
	Map<String, List<OnhandSNViewVO>> snmap = VOEntityUtil
			.groupVOByKeys(new String[] { OnhandDimVO.PK_ONHANDDIM },
					CollectionUtils.listToArray(snList));
	for (Map.Entry<String, List<OnhandSNViewVO>> entry : snmap
			.entrySet()) {
		BalanceOnhandRes res = new BalanceOnhandRes();
		res.setOnhanddimvo(entry.getValue().get(0).getOnhandDimVO());
		List<OnhandSNVO> listsnvo = new ArrayList<OnhandSNVO>();
		for (OnhandSNViewVO viewvo : entry.getValue()) {
			listsnvo.add(viewvo.getOnhandSNVO());
		}
		res.setListsnvo(listsnvo);
		UFDouble[] dsums = VOEntityUtil.sumVOsFieldValuesNotDel(
				CollectionUtils.listToArray(entry.getValue()),
				new String[] { OnhandSNVO.NONHANDNUM,
					OnhandSNVO.NONHANDASTNUM});
		res.setNnum(dsums[0]);
		res.setNastnum(dsums[1]);
		balancerow.addBalanceOnhandRes(res);
	}
  }

  private OnhandSNViewVO[] queryOnhandSNs(Set<String> llist) {
	OnhandSelectDim select = new OnhandSelectDim();
	select.addSelectFields(OnhandDimVO.getContentFields());
	OnhandSNViewVO[] snvos = null;
	try {
		if (SysInitGroupQuery.isSNEnabled()) {
			snvos = NCLocator
					.getInstance()
					.lookup(IOnhandQry.class)
					.queryOnhandSNBySNCode(select,
							CollectionUtils.setToArray(llist));

		} else {
			snvos = NCLocator
					.getInstance()
					.lookup(IOnhandQry.class)
					.queryOnhandSNBySNCodes(select,
							CollectionUtils.setToArray(llist));
		}
	} catch (BusinessException e) {
		ExceptionUtils.wrappException(e);
	}
	return snvos;
  }

  private Set<String> getAllSNValues() {
	Set<String> llist = new HashSet<String>();
	String key = SysInitGroupQuery.isSNEnabled() ? ICPubMetaNameConst.PK_SERIALCODE
			: ICPubMetaNameConst.VSERIALCODE;

	// 获取所有序列号
	for (T onhandSN : onhandSNReq) {
		String[] value = VOEntityUtil.getVOsValuesNotDel(
				onhandSN.getLocationVOs(), key, String.class);
		CollectionUtils.addArrayToSet(llist, value);
	}
	return llist;
  }
  
  

  
  /**
   * 方法功能描述：平衡计算前的数据预处理 <b>参数说明</b>
   * 
   * @author yangb
   * @time 2010-4-10 下午03:10:01
   */
  private void processData() {
    this.filterReqsData();
    if (ValueCheckUtil.isNullORZeroLength(this.onhandReq)) {
      return;
    }
    // 按实际结存维度处理查询条件
    OnhandDimVO[] dimvos = new OnhandDimVO[this.onhandReq.length];
    for (int i = 0; i < this.onhandReq.length; i++) {
      dimvos[i] = this.onhandReq[i].getOnhandDim();
    }
    dimvos =
      OnhandVOTools.getRealOnhandDim(new ICBSContext().getInvInfo(), dimvos);

    OnhandVO[] handvos = this.getOnhandRes().loadOnhandRes(dimvos);
    
	/* 712 */PickAutoRes pickAutoRes = new PickAutoRes();
	/* 713 */pickAutoRes.setHandvos(handvos);
//	pickAutoRes.setOnhandReq(onhandReq)
	/* 714 */pickAutoRes.setOnhandReq( (ICBillOnhandReq[]) this.onhandReq);
	/*     */
	/* 716 */AroundProcesser processer = new AroundProcesser(
	/* 717 */BPPluginPoint.PickAuto);
	/*     */
	/* 719 */processer.addAfterRule(new PickAutoOnhandFilter());
	/*     */
	/* 721 */PickAutoRes[] pickAutoResArray = (PickAutoRes[]) processer
			.after(new PickAutoRes[]{pickAutoRes});
	/*     */
	/* 723 */if (!(ValueCheckUtil.isNullORZeroLength(pickAutoResArray))) {
		/* 724 */handvos = pickAutoResArray[0].getHandvos();
		/*     */}
    
    if (ValueCheckUtil.isNullORZeroLength(handvos)) {
      return;
    }

    this.reqsmatch =
      new DimMatchUtil<T>(this.onhandReq, OnhandDimVO.getDimContentFields(),
          new String[] {
        OnhandDimVO.PK_GROUP, OnhandDimVO.PK_ORG,
        OnhandDimVO.CWAREHOUSEID, OnhandDimVO.CMATERIALOID,
        OnhandDimVO.CMATERIALVID,
      });

    this.mapResults = new HashMap<T, OnhandBalanceResult<T>>();

    handvos = this.preAllocHandRes(handvos);

    if (ValueCheckUtil.isNullORZeroLength(handvos)) {
      this.reqsmatch = null;
      return;
    }
    this.processOnhandData(handvos);
  }

  /**
   * 方法功能描述：处理查询后的现存量： 过滤0结存,按统计库存组织+仓库+物料统计实际可以结存 优化后续明细查询 <b>参数说明</b>
   * 
   * @author yangb
   * @time 2010-4-10 下午03:10:01
   */
  private void processOnhandData(OnhandVO[] handvos) {
    // 过滤并处理资源数量
    String key = null;
    this.mapyethandvo = new HashMap<String, OnhandVO>();
    for (OnhandVO handvo : handvos) {
      key =
        handvo.getAttributeValue(OnhandDimVO.VSUBHASHCODE)
        + handvo.getClocationid();
      this.mapyethandvo.put(key, handvo);
    }
  }

  /**
   * 处理资源与现有结存的关系
   */
  private boolean processResDataForHand(OnhandRes handres) {
    String onhandkey =
        handres.getOnhanddimvo().getVsubhashcode()
            + handres.getOnhanddimvo().getClocationid();
    OnhandVO handvo = this.mapyethandvo.get(onhandkey);
    if (handvo == null) {
      handres.setNuseablenum(UFDouble.ZERO_DBL);
      handres.setNuseableastnum(UFDouble.ZERO_DBL);
      handres.setNuseablegrossnum(UFDouble.ZERO_DBL);
      return true;
    }
    handres.getOnhanddimvo().setPk_onhanddim(handvo.getPk_onhanddim());
    // 是否超出结存资源
    boolean byetoverhand = false;
    // 不包含预留冻结数量
    UFDouble onhandnum = OnhandVOTools.calcHandNum(handvo);
    UFDouble dtemp = NCBaseTypeUtils.sub(onhandnum, handres.getNuseablenum());
    if (NCBaseTypeUtils.isLEZero(dtemp)) {
      handres.setNuseablenum(onhandnum);
      handvo.setNonhandnum(UFDouble.ZERO_DBL);
    }
    if (handvo.getCastunitid() != null) {
      // 不包含预留冻结数量
      UFDouble onhandasnum =
          OnhandVOTools.calcRealAstHandNum(handvo.getOnhandNumVO());
      UFDouble dtemp1 =
          NCBaseTypeUtils.sub(onhandasnum, handres.getNuseableastnum());
      if (NCBaseTypeUtils.isLEZero(dtemp1)) {
        handres.setNuseableastnum(onhandasnum);
        handvo.setNonhandastnum(UFDouble.ZERO_DBL);
      }
    }
    else {

      handres.setNuseableastnum(this.calculator.calculateAstNum(
          handres.getNuseablenum(), handres.getOnhanddimvo().getVchangerate(),
          handres.getOnhanddimvo().getCastunitid()));
      handvo.setNonhandastnum(UFDouble.ZERO_DBL);
    }
    if (NCBaseTypeUtils.isGtZero(handvo.getNgrossnum())) {
      // 不包含预留冻结数量
      UFDouble onhandgrossnum =
          OnhandVOTools.calcRealGrossHandNum(handvo.getOnhandNumVO());

      UFDouble dtemp2 =
          NCBaseTypeUtils.sub(onhandgrossnum, handres.getNuseablegrossnum());
      if (NCBaseTypeUtils.isLEZero(dtemp2)) {
        handres.setNuseablegrossnum(onhandgrossnum);
        handvo.setNgrossnum(UFDouble.ZERO_DBL);
      }
    }
    if (NCBaseTypeUtils.isLEZero(OnhandVOTools.calcHandNum(handvo))
        && NCBaseTypeUtils.isLEZero(OnhandVOTools.calcRealAstHandNum(handvo
            .getOnhandNumVO()))) {
      byetoverhand = true;
      this.mapyethandvo.remove(onhandkey);
    }
    return byetoverhand;
  }

}
