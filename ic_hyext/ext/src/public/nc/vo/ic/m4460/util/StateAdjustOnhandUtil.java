/*     */ package nc.vo.ic.m4460.util;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;

/*     */ import nc.bs.framework.common.NCLocator;
import nc.bs.trade.business.HYPubBO;
/*     */ import nc.pubitf.ic.onhand.IOnhandQry;
import nc.vo.fi.pub.SqlUtils;
/*     */ import nc.vo.ic.m4460.entity.StateAdjustVO;
/*     */ import nc.vo.ic.material.define.InvBasVO;
/*     */ import nc.vo.ic.material.query.InvInfoQuery;
/*     */ import nc.vo.ic.onhand.entity.OnhandNumVO;
/*     */ import nc.vo.ic.onhand.entity.OnhandSNVO;
/*     */ import nc.vo.ic.onhand.entity.OnhandVO;
/*     */ import nc.vo.ic.onhand.pub.OnhandVOTools;
/*     */ import nc.vo.ic.pub.cache.DefaultCacheFactory;
/*     */ import nc.vo.ic.pub.util.NCBaseTypeUtils;
/*     */ import nc.vo.ic.pub.util.VOEntityUtil;
/*     */ import nc.vo.ic.pub.util.ValueCheckUtil;
/*     */ import nc.vo.pub.BusinessException;
/*     */ import nc.vo.pub.lang.UFDateTime;
/*     */ import nc.vo.pub.lang.UFDouble;
/*     */ import nc.vo.pubapp.pattern.pub.MapList;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class StateAdjustOnhandUtil
/*     */ {
/*     */   public List<StateAdjustVO> onhand2StateAdjust(OnhandVO[] resultVOs, boolean isShowSn, String[] sncodes)
/*     */     throws BusinessException
/*     */   {
/*  48 */     if (ValueCheckUtil.isNullORZeroLength(resultVOs))
/*  49 */       return null;
/*  50 */     OnhandSNVO[] onhandsnvos = fetchOnHandSN(resultVOs);
/*  51 */     Map<String, InvBasVO> map = getInvBasMap(resultVOs);
/*  52 */     if (ValueCheckUtil.isNullORZeroLength(map))
/*  53 */       return null;
/*  54 */     Map<String, UFDateTime> tsMap = getOnhandnumTS(resultVOs);
/*  55 */     if (ValueCheckUtil.isNullORZeroLength(tsMap))
/*  56 */       return null;
/*  57 */     MapList<String, OnhandSNVO> dim2sn = new MapList();
/*  58 */     for (OnhandSNVO onhandsnvo : onhandsnvos) {
/*  59 */       dim2sn.put(onhandsnvo.getPk_onhanddim(), onhandsnvo);
/*     */     }
/*  61 */     Set<String> sncodeset = new HashSet();
/*  62 */     if ((sncodes != null) && (sncodes.length > 0)) {
/*  63 */       for (String sncode : sncodes) {
/*  64 */         if (sncode != null) {
/*  65 */           sncodeset.add(sncode);
/*     */         }
/*     */       }
/*     */     }
Map<String, StateAdjustVO> map1 = getStateAdjustVO(resultVOs);
/*  69 */     InvBasVO inv = null;
/*  70 */     List<StateAdjustVO> billList = new ArrayList();
/*  71 */     for (OnhandVO onhandVO : resultVOs) {
/*  72 */       inv = (InvBasVO)map.get(onhandVO.getCmaterialvid());
/*     */       
/*  74 */       if ((inv != null) && (inv.getFix1().booleanValue()))
/*     */       {
/*     */ 
/*  77 */         if ((tsMap.get(onhandVO.getPk_onhanddim()) != null) && (canStateAdjust(onhandVO)))
/*     */         {
/*     */ 
/*     */ 
/*  81 */           StateAdjustVO vo = new StateAdjustVO();
/*  82 */           vo.setPk_onhanddim(onhandVO.getPk_onhanddim());
/*  83 */           vo.setPk_group(onhandVO.getPk_group());
/*  84 */           vo.setPk_org(onhandVO.getPk_org());
/*  85 */           vo.setNnum(onhandVO.getNonhandnum());
/*  86 */           vo.setNassistnum(onhandVO.getNonhandastnum());
/*  87 */           vo.setNgrossnum(onhandVO.getNgrossnum());
/*  88 */           vo.setNrsnum(onhandVO.getNrsnum());
/*  89 */           vo.setNlocknum(onhandVO.getNlocknum());
/*  90 */           vo.setNlockassistnum(onhandVO.getNlockastnum());
/*  91 */           vo.setOnhandnumts((UFDateTime)tsMap.get(onhandVO.getPk_onhanddim()));
/*  92 */           vo.setVchangerate(onhandVO.getVchangerate());
/*     */           
/*  94 */           for (int i = 1; i < 11; i++) {
/*  95 */             vo.setAttributeValue("vfree" + i, onhandVO.getAttributeValue("vfree" + i));
/*     */           }
if (!ValueCheckUtil.isNullORZeroLength(tsMap)){
	if(!ValueCheckUtil.isNullORZeroLength(map1)){
		StateAdjustVO statevo = (StateAdjustVO)map1.get(onhandVO.getPk_onhanddim());
		if(statevo != null){
				/*  95 */             vo.setVdef1(statevo.getVdef1());
				 vo.setVdef2(statevo.getVdef2());
				 vo.setVdef3(statevo.getVdef3());
				 vo.setVdef4(statevo.getVdef4());
				 vo.setVdef5(statevo.getVdef5());
				 vo.setVdef6(statevo.getVdef6());
				 vo.setVdef7(statevo.getVdef7());
				 vo.setVdef8(statevo.getVdef8());
				 vo.setVdef9(statevo.getVdef9());
				 vo.setVdef10(statevo.getVdef10());
//				 vo.setCstateadjustid(statevo.getCstateadjustid());
//				 vo.setVbillcode(statevo.getVbillcode());
//				 vo.setBillmaker(statevo.getBillmaker());
				/*     */           }
	}
	

}


/*     */           
/*     */ 
/*  99 */           vo.setCunitid(inv.getPk_measdoc());
/* 100 */           vo.setCastunitid(onhandVO.getCastunitid());
/* 101 */           if (!isShowSn) {
/* 102 */             billList.add(vo);
/*     */           }
/*     */           else {
/* 105 */             StateAdjustVO voclone = null;
/* 106 */             List<OnhandSNVO> snlist = dim2sn.get(onhandVO.getPk_onhanddim());
/* 107 */             int index; if ((snlist != null) && (snlist.size() > 0)) {
/* 108 */               index = 0;
/* 109 */               for (OnhandSNVO sn : snlist) {
/* 110 */                 if (((!UFDouble.ZERO_DBL.equals(sn.getNonhandnum())) || (!UFDouble.ZERO_DBL.equals(sn.getNonhandastnum())) || (!UFDouble.ZERO_DBL.equals(sn.getNrsnum())) || (!UFDouble.ZERO_DBL.equals(sn.getNlocknum())) || (!UFDouble.ZERO_DBL.equals(sn.getNlockastnum()))) && ((sncodeset.size() <= 0) || (sncodeset.contains(sn.getVsncode()))))
/*     */                 {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 118 */                   voclone = (StateAdjustVO)vo.clone();
/* 119 */                   voclone.setPk_onhand_sn((String)sn.getAttributeValue("pk_onhandsn"));
/* 120 */                   voclone.setPk_serialcode(sn.getPk_serialcode());
/* 121 */                   voclone.setVsncode(sn.getVsncode());
/* 122 */                   voclone.setNnum(sn.getNonhandnum());
/* 123 */                   voclone.setNassistnum(sn.getNonhandastnum());
/*     */                   
/* 125 */                   if (index != 0) {
/* 126 */                     voclone.setNgrossnum(null);
/*     */                   }
/* 128 */                   voclone.setNrsnum(onhandVO.getNrsnum());
/* 129 */                   voclone.setNlocknum(sn.getNlocknum());
/* 130 */                   voclone.setNlockassistnum(sn.getNlockastnum());
/* 131 */                   billList.add(voclone);
/* 132 */                   index++;
/*     */                 }
/*     */               }
/*     */             } else {
/* 136 */               billList.add(vo);
/*     */             }
/*     */           } } } }
/* 139 */     return billList;
/*     */   }
/*     */   
/*     */   private OnhandSNVO[] fetchOnHandSN(OnhandVO[] resultVOs) throws BusinessException
/*     */   {
/* 144 */     String[] pk_dims = VOEntityUtil.getVOsNotRepeatValue(resultVOs, "pk_onhanddim");
/*     */     
/* 146 */     return ((IOnhandQry)NCLocator.getInstance().lookup(IOnhandQry.class)).queryOnhandSNVOByDimPK(pk_dims);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean canStateAdjust(OnhandVO onhandVO)
/*     */   {
/* 157 */     if (NCBaseTypeUtils.isGt(onhandVO.getNlocknum(), onhandVO.getNonhandnum()))
/* 158 */       return false;
/* 159 */     if ((NCBaseTypeUtils.isGtZero(onhandVO.getNlocknum())) && (NCBaseTypeUtils.isGtZero(onhandVO.getNrsnum())))
/*     */     {
/* 161 */       return false; }
/* 162 */     if ((NCBaseTypeUtils.isNullOrZero(onhandVO.getNonhandnum())) && (NCBaseTypeUtils.isNullOrZero(onhandVO.getNonhandastnum())) && (NCBaseTypeUtils.isNullOrZero(onhandVO.getNgrossnum())))
/*     */     {
/*     */ 
/* 165 */       return false; }
/* 166 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private Map<String, UFDateTime> getOnhandnumTS(OnhandVO[] resultVOs)
/*     */     throws BusinessException
/*     */   {
/* 179 */     if (ValueCheckUtil.isNullORZeroLength(resultVOs)) {
/* 180 */       return null;
/*     */     }
/* 182 */     String[] pk_dims = VOEntityUtil.getVOsNotRepeatValue(resultVOs, "pk_onhanddim");
/*     */     
/*     */ 
/*     */ 
/* 186 */     IOnhandQry onbandquery = (IOnhandQry)NCLocator.getInstance().lookup(IOnhandQry.class);
/* 187 */     OnhandNumVO[] numvos = onbandquery.queryOnhandNumByDim(pk_dims);
/* 188 */     if (ValueCheckUtil.isNullORZeroLength(numvos))
/* 189 */       return null;
/* 190 */     Map<String, UFDateTime> map = new HashMap();
/* 191 */     for (OnhandNumVO vo : numvos) {
/* 192 */       map.put(vo.getPk_onhanddim(), vo.getTs());
/*     */     }
/* 194 */     return map;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private Map<String, InvBasVO> getInvBasMap(OnhandVO[] resultVOs)
/*     */   {
/* 204 */     String[] cmaterialvids = (String[])VOEntityUtil.getVOsValues(resultVOs, "cmaterialvid", String.class);
/*     */     
/*     */ 
/* 207 */     InvInfoQuery invInfoQuery = new InvInfoQuery(DefaultCacheFactory.getInstance());
/*     */     
/* 209 */     InvBasVO[] invvos = invInfoQuery.getInvBasVO(cmaterialvids);
/* 210 */     if (ValueCheckUtil.isNullORZeroLength(invvos)) {
/* 211 */       return null;
/*     */     }
/* 213 */     OnhandVOTools.fillOnhandVChangerate(resultVOs, invInfoQuery);
/*     */     
/* 215 */     Map<String, InvBasVO> map = new HashMap();
/* 216 */     for (InvBasVO vo : invvos) {
/* 217 */       map.put(vo.getPk_material(), vo);
/*     */     }
/* 219 */     return map;
/*     */   }
private Map<String, StateAdjustVO> getStateAdjustVO(OnhandVO[] resultVOs) throws BusinessException
/*     */   {
/* 204 */     String[] cmaterialvids = (String[])VOEntityUtil.getVOsValues(resultVOs, "pk_onhanddim", String.class);
/*     */     String wherepart = SqlUtils.getInStr("pk_onhanddim_adj", cmaterialvids, false);
StateAdjustVO[] vos = (StateAdjustVO[])new HYPubBO().queryByCondition(StateAdjustVO.class, wherepart);
if (ValueCheckUtil.isNullORZeroLength(vos)) {
/* 211 */       return null;
/*     */     }
Map<String, StateAdjustVO> map = new HashMap();
/* 216 */     for (StateAdjustVO vo : vos) {
/* 217 */       map.put(vo.getPk_onhanddim_adj(), vo);
/*     */     }
/*     */ 
/* 219 */     return map;
/*     */   }
/*     */ }



/* Location:           E:\kf_zhw\home0816\modules\ic\lib\pubic_invadjust.jar
 * Qualified Name:     nc.vo.ic.m4460.util.StateAdjustOnhandUtil
 * Java Class Version: 7 (51.0)
 * JD-Core Version:    0.7.1
 */