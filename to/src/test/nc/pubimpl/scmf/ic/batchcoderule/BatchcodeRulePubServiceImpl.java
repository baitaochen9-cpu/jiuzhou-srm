/** <a href="http://www.cpupk.com/decompiler">Eclipse Class Decompiler</a> plugin, Copyright (c) 2017 Chen Chao. */
package nc.pubimpl.scmf.ic.batchcoderule;
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
import nc.bs.ic.pub.db.ICDBVisitor;
import nc.bs.ic.pub.db.SqlIn;
import nc.bs.ic.pub.env.ICBSContext;
import nc.impl.pubapp.pattern.data.vo.VOQuery;
import nc.impl.pubapp.pattern.data.vo.VOUpdate;
import nc.itf.fi.pub.SysInit;
import nc.itf.scmpub.reference.uap.para.SysParaInitQuery;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ArrayProcessor;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.pub.billcode.cooper.itf.IBatchCodeEntity;
import nc.pub.billcode.cooper.vo.BatchCodeEntity;
import nc.pub.billcode.cooper.vo.BatchCodeResult;
import nc.pub.scmf.ic.batchcoderule.JZBatchCodeVO;
import nc.pub.scmf.ic.batchcoderule.NewJZBatchCodeCreatUtil;
import nc.pub.scmf.ic.batchcoderule.NewJZBatchCodeVO;
import nc.pubitf.ic.query.CommonQuery;
import nc.pubitf.scmf.ic.batchcoderule.IBatchcodeRulePubService;
import nc.vo.bd.accessor.IBDData;
import nc.vo.ic.batch.BatchcodeRelation;
import nc.vo.ic.general.define.ICBillBodyVO;
import nc.vo.ic.m4e.entity.TransInBodyVO;
import nc.vo.ic.material.define.InvBasVO;
import nc.vo.ic.material.define.InvCalBodyVO;
import nc.vo.ic.pub.sql.SqlUtil;
import nc.vo.ic.pub.util.CollectionUtils;
import nc.vo.ic.pub.util.NCBaseTypeUtils;
import nc.vo.ic.pub.util.StringUtil;
import nc.vo.ic.pub.util.VOEntityUtil;
import nc.vo.ic.pub.util.ValueCheckUtil;
import nc.vo.pu.m21.rule.Batchcode;
import nc.vo.pub.BusinessException;
import nc.vo.pub.IAttributeMeta;
import nc.vo.pub.ISuperVO;
import nc.vo.pub.IVOMeta;
import nc.vo.pub.SuperVO;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.AppContext;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.pubapp.pattern.model.entity.bill.AbstractBill;
import nc.vo.pubapp.pattern.pub.AssertUtils;
import nc.vo.pubapp.pattern.pub.Constructor;
import nc.vo.pubapp.pattern.pub.SqlBuilder;
import nc.vo.scmf.bc.mbatchcode.BatchcodeVO;
import nc.vo.scmf.ic.batchcoderule.BatchcodeRuleVO;
import nc.vo.scmf.ic.batchcoderule.enumobj.BatchCodeRuleEnum;
import nc.vo.scmpub.util.TimeUtils;

/**
 * <p>
 * <b>本类主要完成以下功能：</b>
 * <ul>
 * <li>物料批次号规则维护
 * <li>根据物料批次号规则生成批次号
 * </ul>
 * <p>
 * <p>
 * 
 * @version 6.0
 * @since 6.0
 * @author wangxhi
 * @time 2010-7-12 下午09:28:31
 */
public class BatchcodeRulePubServiceImpl implements IBatchcodeRulePubService {

  /**
   * 父类方法重写
   * 
   * @see nc.pubitf.scmf.ic.batchcoderule.IBatchcodeRulePubService#generateBatchcode(nc.vo.pubapp.pattern.model.entity.bill.AbstractBill[],
   *      nc.vo.ic.batch.BatchcodeRelation[])
   */
  @Override
  public void generateBatchcode(AbstractBill[] bills, BatchcodeRelation bizObj)
      throws BusinessException {
//    try {
      for (int i = 0; i < bills.length; i++) {
    	  //zhian.ye 20210414 update -------------------------
    	  if(UFBoolean.TRUE.equals(SysInit.getParaBoolean( (String)bills[i].getParent().getAttributeValue("pk_org"), "IC132"))){
				this.jzBatchCode(bills[i],bizObj);
			}
    	  else
    	  if (bills[i] instanceof nc.vo.ic.m4e.entity.TransInVO  && 
    			  UFBoolean.TRUE.equals(SysInit.getParaBoolean((String) bills[i].getParent().getAttributeValue("pk_org"), "TO22"))){
    		 
    	    	  TransInBodyVO[] bodys = ((nc.vo.ic.m4e.entity.TransInVO) bills[i] ).getBodys();
    	    	  for(TransInBodyVO body : bodys){
    	    		  String cmaterialoid = body.getCmaterialoid();
    	    		  String pk_batchcode = body.getPk_batchcode();
    	    		  //创建批次号检查方法
    	    		  checkBatchcodeFoMaterial(cmaterialoid,pk_batchcode);
    	    	  }
    	      
    	      //调拨入库时，由于系统已经转进来批次号档案
    		  
    	  }else
    	  {
			//-----------------------------	
    		  this.generateBillCodeByRuleBody(bills[i], bizObj);
    		  
    	  }
      }
//    }
//    catch (Exception e) {
//      ExceptionUtils.marsh(e);
//    }

  }
  
  private void jzBatchCode(AbstractBill bill, BatchcodeRelation bizObj) throws BusinessException{
	  //SuperVO[] bodys = (SuperVO[]) bill.getChildrenVO();
      List<SuperVO> needbcbodys = this.filterBody(bill, bizObj); 			 //获取需要生成批次号表体数据
    
      
      if(ValueCheckUtil.isNullORZeroLength(needbcbodys)){         			//检查表体数据是否为空
        return;
      }
      SuperVO[] bodys = CollectionUtils.listToArray(needbcbodys);   
      
      
      SuperVO head = (SuperVO) bill.getParent();							//获取表头数据
      String pkOrg = (String) head.getAttributeValue(bizObj.getPkorg()); 	//组织ID
      
      String codrul = SysInit.getParaString(pkOrg, "IC131");//参数获取批次号规则
      Map<String, String> parseRule = NewJZBatchCodeCreatUtil.parseRule(codrul);
      
      UFBoolean paraBoolean = SysInit.getParaBoolean(pkOrg, "SCM14");		//检查参数，同单同物料是否采用同一批次号
      Map<String,String> material_newBatchcode = new HashMap<String,String>(); //物料：批次号
      Map<String,NewJZBatchCodeVO> new_VO = new HashMap<String,NewJZBatchCodeVO>();//批次VO的缓存，用于持久化
      
      List<String> batchs = new ArrayList<String>();
      
     for(SuperVO bodyvo: bodys){
    	 String pk_material = (String) bodyvo.getAttributeValue(bizObj.getMaterialKey());
    	 String batchcode = null;
    	 
    	 if(paraBoolean.booleanValue() && !StringUtil.isSEmptyOrNull(material_newBatchcode.get(pk_material))){ //尝试获取缓存已生成批次
    		  batchcode = material_newBatchcode.get(pk_material) ;
    		  //如果此处能得到，可直接返回了；SMC14 控制多行同批次
    		  bodyvo.setAttributeValue(bizObj.getVbatchcode(), batchcode);
    		  continue ;
    		  
    	 }else {    		 
    		 //以物料、组织、批次号规则查询流水表是否已初始化创建，否则需要初始化
    		 NewJZBatchCodeVO queryBatchSerial = NewJZBatchCodeCreatUtil.queryBatchSerial(pkOrg,pk_material,codrul);//尝试查询流水表	
    		 if(null == queryBatchSerial ) {
    			 //如果流水表查询出来没有数据时，需要系统进行初始化数据
    			 queryBatchSerial =  
    				 new NewJZBatchCodeVO(parseRule, pk_material, pkOrg, NewJZBatchCodeCreatUtil.getMaterialCodeByID(pk_material));
    			 queryBatchSerial.setAttributeValue(NewJZBatchCodeVO.CODERULE, codrul);//设置流水批次规则
    			 queryBatchSerial.setStatus(VOStatus.NEW);//调整VO状态为新增
    			 batchcode = NewJZBatchCodeCreatUtil.assemble(queryBatchSerial, parseRule);//通过规则组装批次号
    			 batchs.add(batchcode);
    		 }else  if(null == new_VO.get(pk_material)){
    			 queryBatchSerial.setStatus(VOStatus.UPDATED);
    			 while (batchcode == null || NewJZBatchCodeCreatUtil.checkCode(pk_material+batchcode) ) {
    				 //
    			batchcode = NewJZBatchCodeCreatUtil.NextBatchCode(queryBatchSerial);
    			batchs.add(batchcode);
    			 }; //以当前的批次号流水，去创建新的批次号；
    		 } else {
    			 queryBatchSerial = new_VO.get(pk_material);
    			 while ( batchcode == null ||( NewJZBatchCodeCreatUtil.checkCode(pk_material+batchcode) && batchs.contains(batchcode)) ) {
    			 batchcode = NewJZBatchCodeCreatUtil.NextBatchCode(queryBatchSerial);
    			 batchs.add(batchcode);
    			 }
    		 }
        	 new_VO.put(pk_material, queryBatchSerial);
        	 material_newBatchcode.put(pk_material, batchcode);//缓存批次号
        	 
    	 }
    	 //已得到可用批次号
    	 bodyvo.setAttributeValue(bizObj.getVbatchcode(), batchcode);
    	  
     }
     if(null != new_VO && new_VO.size() > 0){
    	 for(String material : material_newBatchcode.keySet()){
    		 NewJZBatchCodeCreatUtil.batchcodeDB(new_VO.get(material)); //流水同步处理
    		 
    	 }
     }

  }
  

 



 /**
  * 检查批次号档案，时否与物料相匹配，
  * @param cmaterialoid
  * @param pk_batchcode
  */
private boolean checkBatchcodeFoMaterial(String cmaterialoid, String pk_batchcode) {
	VOQuery<BatchcodeVO> voquery =  new VOQuery<>(BatchcodeVO.class);
	BatchcodeVO[] query = voquery.query(" pk_batchcode = '"+pk_batchcode+"' and cmaterialoid = '"+cmaterialoid+"' and nvl(dr,0)=0", 
			null); //主键锁定，有也只有一行
	
	return false;
	
}

/**
   * 父类方法重写
   * 
   * @see nc.pubitf.scmf.ic.batchcoderule.IBatchcodeRulePubService#generateBillCodeByRuleBody(nc.vo.pubapp.pattern.model.entity.bill.AbstractBill,
   *      nc.vo.ic.batch.BatchcodeRelation)
   */
  @Override
  public void generateBillCodeByRuleBody(AbstractBill bill,
      BatchcodeRelation bizObj) throws BusinessException {
    try {
      //SuperVO[] bodys = (SuperVO[]) bill.getChildrenVO();
      List<SuperVO> needbcbodys = this.filterBody(bill, bizObj);
      if(ValueCheckUtil.isNullORZeroLength(needbcbodys)){
        return;
      }
      SuperVO[] bodys = CollectionUtils.listToArray(needbcbodys);
      SuperVO head = (SuperVO) bill.getParent();
      String pkOrg = (String) head.getAttributeValue(bizObj.getPkorg());
      // 取得需要生成批次号的物料主键
      HashSet<String> invSet = this.getInvVidSet(bodys, pkOrg, bizObj);
      if (ValueCheckUtil.isNullORZeroLength(invSet)) {
        return;
      }

      // 取得物料和分类信息
      Map<String, String> invMap = this.getInvClsMap(invSet);

      // 集团
      String pkGroup =
          (String) bill.getParent().getAttributeValue(bizObj.getPkgroup());

      // 根据集团和物料信息查询物料批次号规则
      Map<String, BatchcodeRuleVO> invRuleMap =
          this.queryRuleVOByInvs(pkGroup, invMap);

      // 更新批次号规则，并查询最新批次号规则
      invRuleMap = this.updateBatchcodeRule(invMap, invRuleMap, bizObj, bodys);

      // 生成批次号
      this.generateBatchCodeList(head, bodys,invRuleMap, bizObj, invSet);
    }
    catch (Exception e) {
      ExceptionUtils.marsh(e);
    }

  }

  /**
   * 方法功能描述：更新批次号规则
   * <p>
   * <b>参数说明</b>
   * 
   * @param vos 批次号规则
   * @param fieldNames 要更新的字段名
   *          <p>
   * @since 6.0
   * @author wangxhi
   * @time 2010-7-28 下午08:28:03
   */
  @Override
  public void update(BatchcodeRuleVO[] vos, String[] fieldNames) {
    // 排序
    Arrays.sort(vos, this.getComparatorForUpdate());
    // 更新
    VOUpdate<BatchcodeRuleVO> update = new VOUpdate<BatchcodeRuleVO>();
    update.update(vos, fieldNames);
  }

  /**
   * 方法功能描述：格式化字符串。长度超过所需，则截取;长度不够，后边补指定字符
   * <p>
   * <b>参数说明</b>
   * 
   * @param ori 源字符串
   * @param length 要求长度
   * @param replacement 补位字符
   * @return 目标字符串
   *         <p>
   * @since 6.0
   * @author wangxhi
   * @time 2010-7-13 下午09:21:27
   */
  private String format(String ori, int length, char replacement) {
    StringBuilder dest = new StringBuilder();
    if (StringUtil.isSEmptyOrNull(ori)) {
      for (int i = 0; i < length; i++) {
        dest.append(replacement);
      }
      return dest.toString();
    }

    if (ori.length() >= length) {
      return ori.substring(0, length);
    }

    dest.append(ori);
    for (int i = 0; i < length - ori.length(); i++) {
      dest.append(replacement);
    }
    return dest.toString();
  }

  /**
   * 方法功能描述：生成单个批次号
   * <p>
   * <b>参数说明</b>
   * 
   * @param head 单据表头
   * @param body 单据表体
   * @param ruleVO 批次号规则
   * @param obj 单据实体字段对应关系
   * @return 批次号
   *         <p>
   * @since 6.0
   * @author wangxhi
   * @time 2010-7-15 下午10:23:39
   */
  private String generateBatchCode(SuperVO head, SuperVO body,
      BatchcodeRuleVO ruleVO, BatchcodeRelation obj) {
    // 声明批次号
    StringBuffer batchcode = new StringBuffer();

    // 连接前缀到批次号中
    batchcode.append(ruleVO.getVprefix() == null ? "" : ruleVO.getVprefix());

    // 根据规则来取得对应的业务对象映射值
    String[] bizObjs = this.getBizObjs(head, body, ruleVO, obj);
    // 连接业务对象1，2到批次号中
    batchcode.append(bizObjs[0]);
    batchcode.append(bizObjs[1]);

    // 取得年月日，连接到批次号中
    String[] date = this.getDateByRuleVO(ruleVO);
    batchcode.append(StringUtil.nullToEmptyElseTrim(date[0]));
    batchcode.append(StringUtil.nullToEmptyElseTrim(date[1]));
    batchcode.append(StringUtil.nullToEmptyElseTrim(date[2]));

    // 根据规则来取得最新的流水号,连接最新流水号到批次号中
    String lastsn = this.getLastsnByRule(ruleVO);
    batchcode.append(lastsn);

    // 连接后缀到批次号中
    batchcode.append(ruleVO.getVsuffix() == null ? "" : ruleVO.getVsuffix());
    return batchcode.toString();
  }

  /**
   * 方法功能描述：根据批次号规则和单据生成批次号
   * <p>
   * <b>参数说明</b>
   * 
   * @param head 要生成批次号的单据表头
   * @param bodys 需要生成批次号的单据表体
   * @param ruleMap 批次号规则Map
   * @param obj 单据字段与规则业务对象映射关系类
   * @param invSet 物料主键集合
   *          <p>
   * @since 6.0
   * @author wangxhi
   * @time 2010-7-12 下午09:29:08
   */
  private void generateBatchCodeList(SuperVO head,SuperVO[] bodys,
      Map<String, BatchcodeRuleVO> ruleMap, BatchcodeRelation obj,
      Set<String> invSet) {
    //SuperVO head = (SuperVO) bill.getParent();
    //SuperVO[] bodys = (SuperVO[]) bill.getChildrenVO();
    
    Map<String, String> invBCMap = new HashMap<String, String>();
    int k = 0;
    for (int i = bodys.length - 1; i >= 0; i--) {
      SuperVO body = bodys[i];
//      if (!this.isNeedGenBatchcode(invSet, body, obj)) {
//        continue;
//      }
      String invId = (String) body.getAttributeValue(obj.getMaterialKey());
      BatchcodeRuleVO r = this.getBatchruleByInvID(ruleMap, invId);
      // 一单内相同物料的批次号取相同流水号
      String pk_org = (String) body.getAttributeValue(obj.getPkorg());
      UFBoolean isSame = SysParaInitQuery.getParaBoolean(pk_org, "SCM14");
      if (isSame != null && !isSame.booleanValue()) {
        k++;
      }
      // 该物料如果已经生成批次号，直接使用该批次号对表体批次号字段赋值
      String comKey = invId + String.valueOf(k);
      if (invBCMap.containsKey(comKey)) {
        body.setAttributeValue(obj.getVbatchcode(), invBCMap.get(comKey));
        continue;
      }
      // 取得物料对应的批次号规则，用于生成批次号
      String batchcode = this.generateBatchCode(head, body, r, obj);
      // 对表体行赋值
      body.setAttributeValue(obj.getVbatchcode(), batchcode);
      invBCMap.put(comKey, batchcode);
    }
  }

  /**
   * 获得需要生成批次的表体
   * @param bill
   * @param obj
   * @return
   */
  private List<SuperVO> filterBody(AbstractBill bill, BatchcodeRelation obj) {
    List<SuperVO> needbcBodys = new ArrayList<SuperVO>();
    List<SuperVO> resultBodys = new ArrayList<SuperVO>();
    List<String> pk_orgs = new ArrayList<String>();
    List<String> invpks = new ArrayList<String>();
    SuperVO[] bodys = (SuperVO[]) bill.getChildrenVO();
    String pk_org = (String) bill.getParent().getAttributeValue(obj.getPkorg());
    for (SuperVO bodyVO : bodys) {
      if (!StringUtil.isSEmptyOrNull((String) bodyVO.getAttributeValue(obj
          .getVbatchcode()))) {
        continue;
      }
      // 库存单据，控制应发不生成批次号
      if (bodyVO instanceof ICBillBodyVO) {
        UFDouble nnum = (UFDouble) bodyVO.getAttributeValue(obj.getNnumKey());
        if (NCBaseTypeUtils.isNullOrZero(nnum)) {
          continue;
        }
      }
      String bodyorg = (String) bodyVO.getAttributeValue(obj.getStoorgKey());

      if (StringUtil.isSEmptyOrNull(bodyorg)
          || StringUtil.isStringEqual(pk_org, bodyorg)) {
        bodyorg = pk_org;
      }
      pk_orgs.add(bodyorg);
      String invID = (String) bodyVO.getAttributeValue(obj.getMaterialKey());
      invpks.add(invID);
      needbcBodys.add(bodyVO);
    }
    InvCalBodyVO[] invvos =
        new ICBSContext().getInvInfo().getInvCalBodyVO(
            pk_orgs.toArray(new String[pk_orgs.size()]),
            invpks.toArray(new String[invpks.size()]));
    if (ValueCheckUtil.isNullORZeroLength(invvos)) {
      return null;
    }
    Map<String, InvCalBodyVO> map = new HashMap<String, InvCalBodyVO>();
    for (InvCalBodyVO invCalBodyVO : invvos) {
      if (invCalBodyVO == null) {
        continue;
      }
      map.put(invCalBodyVO.getPk_org() + invCalBodyVO.getPk_material(),
          invCalBodyVO);

    }

    if (ValueCheckUtil.isNullORZeroLength(map)) {
      return null;
    }
    for (SuperVO bodyVO : needbcBodys) {
      String bodyorg = (String) bodyVO.getAttributeValue(obj.getStoorgKey());

      if (StringUtil.isSEmptyOrNull(bodyorg)
          || StringUtil.isStringEqual(pk_org, bodyorg)) {
        bodyorg = pk_org;
      }
      InvCalBodyVO invCalBodyVO =
          map.get(bodyorg
              + (String) bodyVO.getAttributeValue(obj.getMaterialKey()));
      if (invCalBodyVO == null) {
        continue;
      }
      if (ValueCheckUtil.isTrue(invCalBodyVO.getWholemanaflag())) {
        resultBodys.add(bodyVO);
      }
    }

    return resultBodys;
  }

  /**
   * 判断当前物料对应的物料分类有没有定义批次号规则
   * 
   * @param rule
   * @param invId
   */
  private BatchcodeRuleVO getBatchruleByInvID(
      Map<String, BatchcodeRuleVO> ruleMap, String invId) {
    if (ValueCheckUtil.isNullORZeroLength(ruleMap)) {
      IBDData[] docinv =
          CommonQuery.getInstance().getMaterialDocbyPks(new String[] {
            invId
          });
      String name = docinv[0].getName().toString();
      ExceptionUtils.wrappBusinessException(nc.vo.ml.NCLangRes4VoTransl
          .getNCLangRes().getStrByID("4008028_0", "04008028-0040")/*@res "物料【"*/
          + name
          + nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("4008028_0",
              "04008028-0041")/*@res "】还没有定义批次号规则，不能生成批次号！"*/);
    }
    BatchcodeRuleVO r = ruleMap.get(invId);
    if (r == null) {
      IBDData[] docinv =
          CommonQuery.getInstance().getMaterialDocbyPks(new String[] {
            invId
          });
      String name = docinv[0].getName().toString();
      ExceptionUtils.wrappBusinessException(nc.vo.ml.NCLangRes4VoTransl
          .getNCLangRes().getStrByID("4008028_0", "04008028-0040")/*@res "物料【"*/
          + name
          + nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("4008028_0",
              "04008028-0041")/*@res "】还没有定义批次号规则，不能生成批次号！"*/);
    }
    return r;

  }

  /**
   * 方法功能描述：取得业务对象
   * <p>
   * <b>参数说明</b>
   * 
   * @param head 单据表头
   * @param body 单据表体
   * @param obj 映射关系
   * @param pkGroup 集团
   * @param objName 对象
   * @param length 对象长度
   * @param fixChar 长度不够时，用于进行填充的字符
   * @return 业务对象
   *         <p>
   * @since 6.0
   * @author wangxhi
   * @time 2010-7-15 下午09:23:14
   */
  private String getBizObj(SuperVO head, SuperVO body, BatchcodeRelation obj,
      String pkGroup, String objName, int length, char fixChar) {

    // 业务对象类型：是否实体
    int objType = BatchCodeRuleEnum.getTypeByObjName(objName);

    // 非实体的情况下，取得业务对象的值
    if (objType != BatchCodeRuleEnum.TYPE_ENTITY) {
      return this.getBizObjNotEntity(head, body, obj, length, objName, fixChar);
    }

    // 实体的情况，取得业务对象的映射值
    return this.getBizObjEntity(head, body, obj, objName, pkGroup, fixChar);

  }

  /**
   * 方法功能描述：实体的情况，取得业务对象的映射值
   * <p>
   * <b>参数说明</b>
   * 
   * @param head 单据表头
   * @param body 单据表体
   * @param obj 映射关系
   * @param pkGroup 集团
   * @param objName 对象
   * @param fixChar 长度不够时，用于进行填充的字符
   * @return 业务对象映射值
   * @return <p>
   * @since 6.0
   * @author wangxhi
   * @time 2010-7-15 下午09:30:30
   */
  private String getBizObjEntity(SuperVO head, SuperVO body,
      BatchcodeRelation obj, String objName, String pkGroup, char fixChar) {
    String bizObj = "";
    // 实体主键
    String entityPK = this.getEntityPKByEntityName(head, body, obj, objName);
    // 实体元数据ID
    String metaID = BatchCodeRuleEnum.getMetaIdByObjName(objName);
    Map<String, String> metaMap = new HashMap<String, String>();
    metaMap.put(metaID, entityPK);
    BatchCodeResult result = null;

    IBatchCodeEntity service =
        NCLocator.getInstance().lookup(IBatchCodeEntity.class);
    try {
      result = service.getBatchCodeResult(metaMap, pkGroup);
    }
    catch (BusinessException e) {
      ExceptionUtils.wrappException(e);
    }
    if (result == null) {
      return bizObj;
    }

    // 取得对应映射值
    BatchCodeEntity entity = result.getBatchCodeEntity(metaID);
    if (entity == null || entity.getCevo() == null) {
      return bizObj;
    }

    bizObj = entity.getReflect();
    int length = entity.getCevo().getElength();
    return this.format(bizObj, length, fixChar);
  }

  private Map<String, String> getBizObjInvMap(
      Map<String, BatchcodeRuleVO> ruleMap, SuperVO[] bodys,
      BatchcodeRelation obj) {
    Map<String, String> m = new HashMap<String, String>();
    int i = 0;
    for (SuperVO bodyVO : bodys) {
      // 取得表体物料主键
      String invID = (String) bodyVO.getAttributeValue(obj.getMaterialKey());
      if (!StringUtil.isSEmptyOrNull((String) bodyVO.getAttributeValue(obj
          .getVbatchcode()))) {
        continue;
      }
      BatchcodeRuleVO rule = ruleMap.get(invID);
      if (rule == null) {
        continue;
      }
      // 一单内相同物料的批次号取相同流水号
      String pk_org = (String) bodyVO.getAttributeValue(obj.getPkorg());
      UFBoolean isSame = SysParaInitQuery.getParaBoolean(pk_org, "SCM14");
      if (isSame != null && !isSame.booleanValue()) {
        i++;
      }
      String key = invID + String.valueOf(i);
      m.put(key, invID);
    }
    return m;

  }

  /**
   * 方法功能描述：非实体的情况下，取得业务对象的值
   * <p>
   * <b>参数说明</b>
   * 
   * @param head 表头
   * @param body 表体
   * @param obj 业务对象与单据对应关系
   * @param length 业务对象定义长度
   * @param objName 业务对象名
   * @param fixChar
   * @return 指定长度的业务对象值
   *         <p>
   * @since 6.0
   * @author wangxhi
   * @time 2010-7-13 下午09:22:18
   */
  private String getBizObjNotEntity(SuperVO head, SuperVO body,
      BatchcodeRelation obj, int length, String objName, char fixChar) {
    String attvalue = this.getEntityPKByEntityName(head, body, obj, objName);
    // 如果单据的属性实际长度较大，则截取定长部分，否则补位
    attvalue = attvalue == null ? "" : attvalue;
    return this.format(attvalue, length, fixChar);

  }

  /**
   * 方法功能描述：根据批次号规则和表体得到对象1和对象2的映射值
   * <p>
   * <b>参数说明</b>
   * 
   * @param head 表头
   * @param body 表体
   * @param ruleVO 批次号规则
   * @param obj 业务对象与单据对应关系
   * @return 对象映射值数组
   *         <p>
   * @since 6.0
   * @author wangxhi
   * @time 2010-7-13 下午09:19:32
   */
  private String[] getBizObjs(SuperVO head, SuperVO body,
      BatchcodeRuleVO ruleVO, BatchcodeRelation obj) {
    String[] bizObjs = {
      "", ""
    };

    if (StringUtil.isSEmptyOrNull(ruleVO.getObject1())
        && StringUtil.isSEmptyOrNull(ruleVO.getObject2())) {
      return bizObjs;
    }

    // 集团
    String pkGroup = ruleVO.getPk_group();

    // 业务对象1
    String objName1 = ruleVO.getObject1();
    if (objName1 != null) {
      int length1 = -1;
      if (ruleVO.getObj1length() != null) {
        length1 = ruleVO.getObj1length().intValue();
      }
      bizObjs[0] =
          this.getBizObj(head, body, obj, pkGroup, objName1, length1, '*');
    }

    // 业务对象2
    String objName2 = ruleVO.getObject2();
    if (objName2 != null) {
      int length2 = -1;
      if (ruleVO.getObj2length() != null) {
        length2 = ruleVO.getObj2length().intValue();
      }

      bizObjs[1] =
          this.getBizObj(head, body, obj, pkGroup, objName2, length2, '#');
    }

    return bizObjs;
  }

//  private Object getBodyValueByObjKey(SuperVO body, BatchcodeRelation rel,
//      String objName) {
//    if (StringUtil.isSEmptyOrNull(objName)) {
//      return null;
//    }
//    int index = BatchCodeRuleEnum.getIndexByObjName(objName);
//    int pos = rel.getFieldPosByEnumKey(index);
//    String attrName = rel.getFieldNameByEnumKey(index);
//    Object obj = null;
//    if (pos == BatchcodeRelation.POS_IN_BODY) {
//      obj = body.getAttributeValue(attrName);
//    }
//    return obj;
//  }

  /**
   * 方法功能描述：获得比较器，用于更新前排序
   * <p>
   * <b>参数说明</b>
   * 
   * @return 针对批次号规则主键的比较器
   *         <p>
   * @since 6.0
   * @author wangxhi
   * @time 2010-7-20 下午10:45:08
   */
  private Comparator<BatchcodeRuleVO> getComparatorForUpdate() {
    Comparator<BatchcodeRuleVO> com = new Comparator<BatchcodeRuleVO>() {
      @Override
      public int compare(BatchcodeRuleVO vo1, BatchcodeRuleVO vo2) {
        return vo1.getPk_batchcoderule().compareTo(vo2.getPk_batchcoderule());
      }

    };

    return com;
  }

  /**
   * 获得年月日
   * 
   * @param ruleVO
   * @return
   */
  private String[] getDateByRuleVO(BatchcodeRuleVO ruleVO) {
    // 获得当前的年月日
    UFDate date = TimeUtils.getsrvBaseDate();
    String currtYear = String.valueOf(date.getYear());
    String currtMonth = String.valueOf(date.getStrMonth());
    String currtDay = String.valueOf(date.getStrDay());
    String day = ruleVO.getVday();
    String month = ruleVO.getVmonth();
    String year = ruleVO.getVyear();
    if (!StringUtil.isSEmptyOrNull(day)) {
      if (!day.equals(currtDay) || !month.equals(currtMonth)
          || !year.equals(currtYear)) {
        day = currtDay;
        month = currtMonth;
        year = currtYear;

      }
    }
    else if (!StringUtil.isSEmptyOrNull(month)) {
      if (!month.equals(currtMonth) || !year.equals(currtYear)) {
        month = currtMonth;
        year = currtYear;

      }
    }
    else if (!StringUtil.isSEmptyOrNull(year)) {
      if (!year.equals(currtYear)) {
        year = currtYear;
      }
    }

    return new String[] {
      year, month, day
    };
  }

  /**
   * 方法功能描述：根据单据上的实体名称获得对应的实体主键值
   * <p>
   * <b>参数说明</b>
   * 
   * @param head 表头
   * @param body 表体
   * @param billObj 业务关系实体 ，与单据字段集对应
   * @param objName 实体名称，例如：运输商
   * @return 实体主键
   *         <p>
   * @since 6.0
   * @author wangxhi
   * @time 2010-7-13 下午09:23:22
   */
  private String getEntityPKByEntityName(SuperVO head, SuperVO body,
      BatchcodeRelation billObj, String objName) {
    int index = BatchCodeRuleEnum.getIndexByObjName(objName);
    int pos = billObj.getFieldPosByEnumKey(index);
    String attrName = billObj.getFieldNameByEnumKey(index);
    Object obj = null;
    if (pos == BatchcodeRelation.POS_IN_BODY) {
      obj = body.getAttributeValue(attrName);
    }
    else {
      obj = head.getAttributeValue(attrName);
    }

    if (obj instanceof UFDate) {
      String year = String.valueOf(((UFDate) obj).getYear());
      String month = ((UFDate) obj).getStrMonth();
      String day = ((UFDate) obj).getStrDay();
      return year + month + day;
    }
    return obj == null ? "" : obj.toString();

  }

  /**
   * 方法功能描述：获得要查询的VO的全部字段名称
   * <p>
   * <b>参数说明</b>
   * 
   * @param cls 批次号规则VO类
   * @return 全部字段名称
   *         <p>
   * @since 6.0
   * @author wangxhi
   * @time 2010-7-28 下午06:51:29
   */
  private String[] getFieldNamesByCls(Class<BatchcodeRuleVO> clz) {
    ISuperVO vo = Constructor.construct(clz);
    IVOMeta voMeta = vo.getMetaData();
    String[] dbfields =
        VOEntityUtil.getDBFieldNames(voMeta, null).toArray(new String[0]);
    return dbfields;
  }

  /**
   * 方法功能描述：根据物料主键查询对应的物料基本信息
   * <p>
   * <b>参数说明</b>
   * 
   * @param invSet 物料主键
   * @return 库存物料主键和分类Map
   *         <p>
   * @since 6.0
   * @author wangxhi
   * @time 2010-7-20 下午11:24:59
   */
  private Map<String, String> getInvClsMap(HashSet<String> invSet) {
    // 根据物料主键查询对应的物料基本信息
    Map<String, String> invMap = new HashMap<String, String>();

    InvBasVO[] vos =
        new ICBSContext().getInvInfo().getInvBasVO(
            invSet.toArray(new String[0]));

    for (InvBasVO invVO : vos) {
      invMap.put(invVO.getPk_material(), invVO.getPk_marbasclass());
    }

    if (ValueCheckUtil.isNullORZeroLength(invMap)) {
      return null;
    }

    return invMap;
  }

  /**
   * 方法功能描述：根据物料Map和物料分类与规则Map获得物料与规则映射关系
   * <p>
   * <b>参数说明</b>
   * 
   * @param invMap 物料Map
   * @param clsRuleMap 物料分类与规则Map
   * @return 物料与规则映射关系Map
   *         <p>
   * @since 6.0
   * @author wangxhi
   * @time 2010-7-20 下午11:28:26
   */
  private Map<String, BatchcodeRuleVO> getInvRuleMap(
      Map<String, String> invMap, Map<String, BatchcodeRuleVO> clsRuleMap) {

    if (ValueCheckUtil.isNullORZeroLength(invMap)) {
      return null;
    }
    if (ValueCheckUtil.isNullORZeroLength(clsRuleMap)) {
      return null;
    }
    // 通过物料分类主键，将批次号规则与物料主键建立对应关系
    Map<String, BatchcodeRuleVO> invRuleMap =
        new HashMap<String, BatchcodeRuleVO>();
    for (Map.Entry<String, String> inv : invMap.entrySet()) {
      if (clsRuleMap.containsKey(inv.getValue())) {
        // key：物料主键，value：物料对应的规则
        invRuleMap.put(inv.getKey(), clsRuleMap.get(inv.getValue()));
      }
    }

    return invRuleMap;
  }

  /**
   * 方法功能描述：取得需要生成批次号的物料主键
   * <p>
   * <b>参数说明</b>
   * 
   * @param bodys 单据表体
   * @param pkOrg 组织
   * @param obj 单据字段与业务对象映射关系
   * @return 物料主键列表
   *         <p>
   * @since 6.0
   * @author wangxhi
   * @time 2010-7-29 下午04:10:30
   */
  private HashSet<String> getInvVidSet(SuperVO[] bodys, String pkOrg,
      BatchcodeRelation obj) {
    HashSet<String> invSet = new HashSet<String>();
    for (SuperVO bodyVO : bodys) {

      String invID = (String) bodyVO.getAttributeValue(obj.getMaterialKey());
      invSet.add(invID);
    }

    return invSet;
  }

  /**
   * 方法功能描述：根据批次号规则获得最新的流水号,递减
   * <p>
   * <b>参数说明</b>
   * 
   * @param ruleVO 批次号规则
   * @return 最新的流水号
   *         <p>
   * @since 6.0
   * @author wangxhi
   * @time 2010-7-13 下午09:03:41
   */
  private String getLastsnByRule(BatchcodeRuleVO ruleVO) {

    // 流水号位数
    int snnum = ruleVO.getSnnum().intValue();
    if (snnum <= 0) {
      return "";
    }

    // 上次领取的流水号
    String lastsn = ruleVO.getLastsn();
    if (StringUtil.isSEmptyOrNull(lastsn) || Long.parseLong(lastsn) < 0) {
      ExceptionUtils
          .wrappBusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
              .getStrByID("4008028_0", "04008028-0042")/*@res "没有多余的物料批次号可供分配,获得物料批次号失败!"*/);
    }

    // 流水号间隔
    int interval = 1;
    long longSN = Long.parseLong(lastsn) - interval;
    if (longSN < 0) {
      ruleVO.setLastsn(String.valueOf(longSN));
      return lastsn;
    }

    String str = String.valueOf(Long.parseLong("9" + lastsn) - interval);
    ruleVO.setLastsn(str.substring(1));
    return lastsn;
  }

  private Map<String, BatchcodeRuleVO> getLockedRules(
      Map<String, String> invMap, Map<String, BatchcodeRuleVO> ruleMap) {
    // 更新
    BatchcodeRuleVO[] updateVOs =
        ruleMap.values().toArray(new BatchcodeRuleVO[0]);
    this.updateRuleForLock(updateVOs);

    // 重新查询
    Map<String, BatchcodeRuleVO> clsRuleMap =
        this.queryBatchcodeRule(this.getQueryCondition(updateVOs),
            this.getFieldNamesByCls(BatchcodeRuleVO.class));
    Map<String, BatchcodeRuleVO> newRuleMap =
        this.getInvRuleMap(invMap, clsRuleMap);
    // assert
    AssertUtils.assertValue(
        !ValueCheckUtil.isNullORZeroLength(newRuleMap)
            && ruleMap.values().size() == newRuleMap.values().size(),
        nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("4008028_0",
            "04008028-0043")/*@res "出现并发，请重新查询！"*/);
    return newRuleMap;
  }

  /**
   * 方法功能描述：生成查询条件的where以后部分
   * <p>
   * <b>参数说明</b>
   * 
   * @param vos 批次号规则
   * @return <p>
   * @since 6.0
   * @author wangxhi
   * @time 2010-7-29 下午09:54:59
   */
  private String getQueryCondition(BatchcodeRuleVO[] vos) {
    // 生成查询条件
    SqlBuilder sql = new SqlBuilder();
    sql.append(" and ");
    sql.append(BatchcodeRuleVO.PK_GROUP, vos[0].getPk_group());
    String[] ids =
        VOEntityUtil.getVOsValues(vos, BatchcodeRuleVO.PK_BATCHCODERULE,
            String.class);
    sql.append(SqlIn.formInSQL(BatchcodeRuleVO.PK_BATCHCODERULE, ids));
    return sql.toString();
  }

  /**
   * 方法功能描述：
   * <p>
   * <b>参数说明</b>
   * 
   * @param invruleMap
   * @param objInvMap
   * @return <p>
   * @since 6.0
   * @author wangxhi
   * @time 2011-1-5 下午05:51:36
   */
  private Map<String, Integer> getSNCountPerRule(
      Map<String, BatchcodeRuleVO> invruleMap, Map<String, String> objInvMap) {
    Map<String, Integer> ruleInvCntMap = new HashMap<String, Integer>();
    for (Map.Entry<String, String> oi : objInvMap.entrySet()) {
      String inv = oi.getValue();
      BatchcodeRuleVO r = invruleMap.get(inv);
      if (r == null) {
        continue;
      }
      if (ruleInvCntMap.get(r.getPk_batchcoderule()) != null) {
        int count = ruleInvCntMap.get(r.getPk_batchcoderule()).intValue();
        ruleInvCntMap.put(r.getPk_batchcoderule(), Integer.valueOf(++count));
      }
      else {
        ruleInvCntMap.put(r.getPk_batchcoderule(), Integer.valueOf(1));
      }
    }
    return ruleInvCntMap;
  }

  /**
   * 方法功能描述：方法功能描述：获得是否需要初始化流水号的标志
   * <p>
   * <b>参数说明</b>
   * 
   * @param ruleVO 批次号规则
   * @return <p>
   * @since 6.0
   * @author wangxhi
   * @time 2010-7-16 上午09:00:29
   */
  private boolean isNeedInitSN(BatchcodeRuleVO ruleVO) {
    // 年
    String year = "";
    if (ruleVO.getVyear() != null) {
      year = ruleVO.getVyear();
    }
    // 月
    String month = "";
    if (ruleVO.getVmonth() != null) {
      month = ruleVO.getVmonth();
    }
    // 日
    String day = "";
    if (ruleVO.getVday() != null) {
      day = ruleVO.getVday();
    }

    // 获得当前的年月日
    UFDate date = TimeUtils.getsrvBaseDate();
    String currtYear = String.valueOf(date.getYear());
    String currtMonth = String.valueOf(date.getStrMonth());
    String currtDay = String.valueOf(date.getStrDay());

    // 归零标志
    String snResetFlag = ruleVO.getSnresetflag();
    if ("3"/* @res "日" */.equals(snResetFlag)
        && (!currtDay.equals(day) || !month.equals(currtMonth) || !currtYear
            .equals(year))
        || "2"/*@res "月"*/.equals(snResetFlag) && (!currtMonth.equals(month) || !currtYear.equals(year))
        || "1"/*@res "年"*/.equals(snResetFlag) && !currtYear.equals(year)) {
      return true;
    }
    return false;
  }

  /**
   * 方法功能描述：根据查询语句和要查询的字段查询批次号规则
   * <p>
   * <b>参数说明</b>
   * 
   * @param condition 查询条件
   * @param names 要查询的字段
   * @return 物料分类主键及其对应批次号规则Map
   *         <p>
   * @since 6.0
   * @author wangxhi
   * @time 2010-7-29 下午09:22:41
   */
  private Map<String, BatchcodeRuleVO> queryBatchcodeRule(String condition,
      String[] names) {
    // 查询
    VOQuery<BatchcodeRuleVO> query =
        new VOQuery<BatchcodeRuleVO>(BatchcodeRuleVO.class, names);
    BatchcodeRuleVO[] vos = query.query(condition, null);
    if (ValueCheckUtil.isNullORZeroLength(vos)) {
      return null;
    }
    Map<String, BatchcodeRuleVO> ruleMap =
        new HashMap<String, BatchcodeRuleVO>();
    for (BatchcodeRuleVO batchcodeRuleVO : vos) {
      ruleMap.put(batchcodeRuleVO.getPk_marbasclass(), batchcodeRuleVO);
    }
    return ruleMap;
  }

  /**
   * 方法功能描述：根据集团主键和物料分类查询对应的批次号规则部分字段
   * <p>
   * <b>参数说明</b>
   * 
   * @param pkGroup 集团主键
   * @param invMap 物料Map
   * @return 物料及其对应批次号规则Map
   *         <p>
   * @since 6.0
   * @author wangxhi
   * @time 2010-7-13 下午09:25:16
   */
  private Map<String, BatchcodeRuleVO> queryBatchcodeRuleFields(String pkGroup,
      Map<String, String> invMap) {
    // 获得需要查询字段
    String[] dbfields =
        {
          BatchcodeRuleVO.PK_BATCHCODERULE, BatchcodeRuleVO.PK_GROUP,
          BatchcodeRuleVO.PK_MARBASCLASS, BatchcodeRuleVO.LASTSN
        };

    // 生成查询条件
    SqlBuilder sql = new SqlBuilder();
    sql.append(" and ");
    sql.append(BatchcodeRuleVO.PK_GROUP, pkGroup);
    HashSet<String> invIds = new HashSet<String>();

    // 过滤掉相同物料分类
    for (Map.Entry<String, String> entry : invMap.entrySet()) {
      invIds.add(entry.getValue());
    }    
    sql.append(SqlIn.formInSQL(BatchcodeRuleVO.PK_MARBASCLASS,
        invIds.toArray(new String[0])));

    // 查询
    Map<String, BatchcodeRuleVO> ruleMap =
        this.queryBatchcodeRule(sql.toString(), dbfields);

    return ruleMap;
  }

  /**
   * 方法功能描述：根据集团和物料主键查询对应的批次号规则
   * <p>
   * <b>参数说明</b>
   * 
   * @param pkGroup 集团
   * @param invMap 物料信息
   * @return 批次号规则Map<物料主键，批次号规则>
   *         <p>
   * @since 6.0
   * @author wangxhi
   * @time 2010-7-21 上午09:18:11
   */
  private Map<String, BatchcodeRuleVO> queryRuleVOByInvs(String pkGroup,
      Map<String, String> invMap) {

    // 根据物料基本信息查询对应的批次号规则部分字段
    Map<String, BatchcodeRuleVO> clsRuleMap =
        this.queryBatchcodeRuleFields(pkGroup, invMap);

    // 根据物料Map和物料分类与规则Map获得物料与规则映射关系
    return this.getInvRuleMap(invMap, clsRuleMap);
  }

  /**
   * 方法功能描述：根据批次号规则以及对应物料数量，设置最终要更新的流水号值
   * <p>
   * <b>参数说明</b>
   * 
   * @param ruleVO 批次号规则
   * @param sncntMap 批次号规则对应物料数量
   *          <p>
   * @since 6.0
   * @author wangxhi
   * @time 2010-7-19 下午09:35:07
   */
  private void setLastsnByRule(BatchcodeRuleVO ruleVO,
      Map<String, Integer> sncntMap) {

    // 流水号位数
    int snnum = ruleVO.getSnnum().intValue();
    if (snnum <= 0) {
      return;
    }

    // 定义一个初始流水号字符串， 其长度为流水号位数+1即可
    StringBuilder initSN = new StringBuilder();
    for (int i = 0; i <= snnum; i++) {
      initSN.append('0');
    }

    // 标志是否应该初始化流水号
    boolean initFlg = this.isNeedInitSN(ruleVO);

    // 上次领取的流水号
    String lastsn = ruleVO.getLastsn();

    // 初始化时增加的流水号个数
    int initSNCnt = 0;
    if (initFlg || StringUtil.isSEmptyOrNull(lastsn)) {
      // 初始化流水号
      lastsn = initSN.substring(0, snnum);
      initSNCnt = 1;
    }

    // 流水号 增量
    int interval =
        sncntMap.get(ruleVO.getPk_batchcoderule()).intValue() - initSNCnt;
    lastsn = String.valueOf((Long.parseLong("9" + lastsn) + interval));
    lastsn = lastsn.substring(1);
    if (lastsn.length() > snnum) {
      ExceptionUtils
          .wrappBusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
              .getStrByID("4008028_0", "04008028-0042")/*@res "没有多余的物料批次号可供分配,获得物料批次号失败!"*/);
    }

    ruleVO.setLastsn(lastsn);
  }

  /**
   * 方法功能描述： 重新设置规则日期
   * <p>
   * <b>参数说明</b>
   * 
   * @param ruleVO 批次号规则
   *          <p>
   * @since 6.0
   * @author wangxhi
   * @time 2010-7-16 上午09:44:35
   */
  private void setRuleDate(BatchcodeRuleVO ruleVO) {
    boolean initFlg = this.isNeedInitSN(ruleVO);
    if (!initFlg && !StringUtil.isSEmptyOrNull(ruleVO.getLastsn())) {
      return;
    }
    // 获得当前的年月日
    UFDate date = TimeUtils.getsrvBaseDate();
    String currtYear = String.valueOf(date.getYear());
    String currtMonth = String.valueOf(date.getStrMonth());
    String currtDay = String.valueOf(date.getStrDay());

    // 重新设置规则日
    if (!StringUtil.isSEmptyOrNull(ruleVO.getVday())) {
      ruleVO.setVday(currtDay);
    }
    // 重新设置规则月
    if (!StringUtil.isSEmptyOrNull(ruleVO.getVmonth())) {
      ruleVO.setVmonth(currtMonth);
    }
    // 重新设置规则年
    if (!StringUtil.isSEmptyOrNull(ruleVO.getVyear())) {
      ruleVO.setVyear(currtYear);
    }

  }

  /**
   * 方法功能描述：更新批次号规则，并取得更新后的值
   * <p>
   * <b>参数说明</b>
   * 
   * @param invClsMap 物料
   * @param invRuleMap 批次号规则
   * @return 更新后的值
   *         <p>
   * @since 6.0
   * @author wangxhi
   * @time 2010-7-19 下午09:44:41
   */
  private Map<String, BatchcodeRuleVO> updateBatchcodeRule(
      Map<String, String> invClsMap, Map<String, BatchcodeRuleVO> invRuleMap,
      BatchcodeRelation rel, SuperVO[] bodys) {

    if (ValueCheckUtil.isNullORZeroLength(invRuleMap)) {
      return null;
    }
    // 加锁
    Map<String, BatchcodeRuleVO> newRuleMap =
        this.getLockedRules(invClsMap, invRuleMap);

    // 获得每一批次规则对应应增加的流水号个数
    Map<String, Integer> sncntMap =
        this.getSNCountPerRule(newRuleMap,
            this.getBizObjInvMap(newRuleMap, bodys, rel));

    BatchcodeRuleVO[] vos = newRuleMap.values().toArray(new BatchcodeRuleVO[0]);
    HashSet<BatchcodeRuleVO> set = new HashSet<BatchcodeRuleVO>();
    set.addAll(Arrays.asList(vos));
    for (BatchcodeRuleVO ruleVO : set) {
      // 重新设置最新流水号
      this.setLastsnByRule(ruleVO, sncntMap);
      // 重新设置日期
      this.setRuleDate(ruleVO);
    }
    // 更新批次号规则
    String[] upFields =
        {
          BatchcodeRuleVO.VYEAR, BatchcodeRuleVO.VMONTH, BatchcodeRuleVO.VDAY,
          BatchcodeRuleVO.LASTSN
        };
    this.update(set.toArray(new BatchcodeRuleVO[0]), upFields);
    // 返回
    return newRuleMap;
  }

  /**
   * 方法功能描述：查询前的更新，对当前记录加锁
   * <p>
   * <b>参数说明</b>
   * 
   * @param updateVOs 要更新的规则
   *          <p>
   * @since 6.0
   * @author wangxhi
   * @time 2010-7-29 下午09:03:40
   */
  private void updateRuleForLock(BatchcodeRuleVO[] updateVOs) {
    String[] ids = new String[updateVOs.length];
    for (int i = 0; i < updateVOs.length; i++) {
      ids[i] = updateVOs[i].getPk_batchcoderule();
    }

    // 排序
    Arrays.sort(ids, 0, ids.length);

    // 构建SQL
    String sql = SqlUtil.getUpdateSql(BatchcodeRuleVO.TABLE_NAME, new String[] {
      BatchcodeRuleVO.TS
    }, new String[] {
      BatchcodeRuleVO.TS
    }, new String[] {
      BatchcodeRuleVO.PK_BATCHCODERULE
    }, null, null);
    Object[][] params = new Object[ids.length][];
    for (int i = 0; i < ids.length; i++) {
      params[i] = new Object[] {
        ids[i]
      };
    }

    // 更新
    new ICDBVisitor().update(
        sql,
        new IAttributeMeta[] {
          new BatchcodeRuleVO().getMetaData().getAttribute(
              BatchcodeRuleVO.PK_BATCHCODERULE)
        }, params);
  }

}
