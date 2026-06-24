package nc.ui.uif2;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Date;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;

/*     */ import nc.bs.busilog.vo.BusiLogConvert;
/*     */ import nc.bs.busilog.vo.BusinessLogAsynInfo;
/*     */ import nc.bs.busilog.vo.BusinessLogContext;
/*     */ import nc.bs.busilog.vo.BusinessLogMSGVO;
/*     */ import nc.bs.busilog.vo.BusinessLogESVO;
/*     */ import nc.bs.framework.common.InvocationInfoProxy;
/*     */ import nc.bs.logging.Logger;
/*     */ import nc.bs.sm.busilog.util.LogConfigServiceFacade;
/*     */ import nc.md.MDBaseQueryFacade;
/*     */ import nc.md.data.access.DASFacade;
/*     */ import nc.md.data.access.NCObject;
/*     */ import nc.md.model.IBean;
/*     */ import nc.md.model.IBusinessEntity;
/*     */ import nc.md.model.MetaDataException;
/*     */ import nc.md.model.impl.Attribute;
/*     */ import nc.md.model.type.impl.EnumType;
/*     */ import nc.ui.pub.beans.constenum.IConstEnum;
/*     */ import nc.vo.pub.BusinessException;
/*     */ import nc.vo.pub.lang.MultiLangText;
/*     */ import nc.vo.pub.lang.UFDateTime;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BusiLogVOConvert
/*     */ {
/*     */   public static List<BusinessLogESVO> convertContextList2BusinessLogVOList(List<BusinessLogContext> paralistcontext)
/*     */     throws BusinessException
/*     */   {
/*  42 */     String pk_group = InvocationInfoProxy.getInstance().getGroupId();
/*  43 */     String userid = InvocationInfoProxy.getInstance().getUserId();
/*  44 */     UFDateTime logdate = new UFDateTime(new Date());
/*  45 */     String clientip = InvocationInfoProxy.getInstance().getClientHost();
/*  46 */     String dsname = InvocationInfoProxy.getInstance().getUserDataSource();
/*  47 */     return convertContextList2BusinessLogVOList(paralistcontext, pk_group, userid, logdate, clientip, dsname);
/*     */   }
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
/*     */   private static List<BusinessLogESVO> convertContextList2BusinessLogVOList(List<BusinessLogContext> paralistcontext, String pk_group, String userid, UFDateTime logdate, String clientip, String ds)
/*     */     throws BusinessException
/*     */   {
/*  65 */     if ((paralistcontext == null) || (pk_group == null)) {
/*  66 */       Logger.error("businesslog context or pk_group is null");
/*  67 */       return null;
/*     */     }
/*  69 */     InvocationInfoProxy.getInstance().setUserDataSource(ds);
/*     */     
/*     */ 
/*     */ 
/*  73 */     List<BusinessLogESVO> listretnbusilogvo = new ArrayList<>();
/*     */     
/*  75 */     HashMap<String, List<String>> meta_busilogarrlist_map = new HashMap<>();
/*     */     
/*  77 */     HashMap<String, List<String>> meta_mainattrlist_map = new HashMap<>();
/*     */     
/*  79 */     Map<String, Map<String, List<String>>> pbeanidsonattrmap = new HashMap<>();
/*     */     
/*  81 */     Map<String, Map<String, String>> pbeanidsonattrnamemap = new HashMap<>();
/*     */     
/*     */ 
/*  84 */     HashMap<String, HashMap<String, BusilogAttValueInfo[]>> metaid_VO_arrattvaulemap_map = new HashMap<>();
/*     */     
/*  86 */     HashMap<String, BusilogAttValueInfo[]> vopk_attvaluemap_map = new HashMap<>();
/*     */     
/*  88 */     HashMap<String, List<String>> allVOmetaid_attvaulemap_map = new HashMap<>();
/*     */     
/*     */ 
/*     */ 
/*  92 */     for (BusinessLogContext context : paralistcontext)
/*     */     {
/*  94 */       String contextmetid = context.getTypepk_busiobj();
/*  95 */       IBean bean = MDBaseQueryFacade.getInstance().getBeanByID(contextmetid);
/*     */       
/*     */ 
/*  98 */       listretnbusilogvo.add(getBaseLogVOInfo(context, pk_group, userid, logdate, clientip));
/*     */       
/*     */ 
/*     */ 
/* 102 */       String logattid = contextmetid + pk_group;
/* 103 */       List<String> attstrlist = (List)meta_busilogarrlist_map.get(logattid);
/* 104 */       if ((attstrlist == null) && (!meta_busilogarrlist_map.containsKey(logattid)))
/*     */       {
/* 106 */         attstrlist = LogConfigServiceFacade.getInstance().getAttrbuteNamePath(pk_group, contextmetid, false);
/*     */         
/* 108 */         meta_busilogarrlist_map.put(logattid, attstrlist);
/*     */         
/*     */ 
/* 111 */         processAttrMaps(meta_mainattrlist_map, pbeanidsonattrmap, pbeanidsonattrnamemap, contextmetid, bean, attstrlist);
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 118 */       NCObject newncobj = null;
/* 119 */       if (context.getBusiobjvo() != null) {
/* 120 */         newncobj = NCObject.newInstance(bean, context.getBusiobjvo());
/*     */       }
/*     */       
/* 123 */       NCObject oldncobj = null;
/* 124 */       if (context.getOldbusiobjvo() != null) {
/* 125 */         oldncobj = NCObject.newInstance(bean, context.getOldbusiobjvo());
/*     */       }
/*     */       
/*     */ 
/* 129 */       BusilogAttValueInfo[] arroldnewattvalue = new BusilogAttValueInfo[2];
/*     */       
/*     */ 
/* 132 */       BusilogAttValueInfo oldinfo = new BusilogAttValueInfo();
/* 133 */       oldinfo.setPk(context.getPk_busiobj());
/* 134 */       oldinfo.setMetaid(contextmetid);
/*     */       
/* 136 */       BusilogAttValueInfo newinfo = new BusilogAttValueInfo();
/* 137 */       newinfo.setPk(context.getPk_busiobj());
/* 138 */       newinfo.setMetaid(contextmetid);
/*     */       
/* 140 */       if (meta_mainattrlist_map.get(contextmetid) != null) {
/* 141 */         for (String mainpath : (List<String>)meta_mainattrlist_map.get(contextmetid)) {
/* 142 */           if (oldncobj != null) {
/* 143 */             String value = getDASAttMultiLangMaintext(oldncobj.getAttributeValue(mainpath));
/*     */             
/* 145 */             Attribute att = (Attribute)bean.getAttributeByPath(mainpath);
/*     */             
/*     */ 
/* 148 */             oldinfo.getMainmap().put(att, value);
/*     */             
/* 150 */             processMap(allVOmetaid_attvaulemap_map, value, att);
/*     */           }
/*     */           
/* 153 */           if (newncobj != null) {
/* 154 */             String value = getDASAttMultiLangMaintext(newncobj.getAttributeValue(mainpath));
/*     */             
/* 156 */             Attribute att = (Attribute)bean.getAttributeByPath(mainpath);
/*     */             
/* 158 */             newinfo.getMainmap().put(att, value);
/* 159 */             processMap(allVOmetaid_attvaulemap_map, value, att);
/*     */           }
/*     */         }
/*     */       }
/*     */       
/*     */ 
/* 165 */       if (pbeanidsonattrmap.get(contextmetid) != null) {
	Map<String, List<String>> pmap = pbeanidsonattrmap.get(contextmetid);
/* 166 */         for (String submetaid : pmap.keySet())
/*     */         {
/* 168 */           getSubInfo(pbeanidsonattrmap, pbeanidsonattrnamemap, allVOmetaid_attvaulemap_map, contextmetid, oldncobj, oldinfo, submetaid);
/*     */           
/*     */ 
/* 171 */           getSubInfo(pbeanidsonattrmap, pbeanidsonattrnamemap, allVOmetaid_attvaulemap_map, contextmetid, newncobj, newinfo, submetaid);
/*     */         }
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 179 */       arroldnewattvalue[0] = oldinfo;
/* 180 */       arroldnewattvalue[1] = newinfo;
/* 181 */       vopk_attvaluemap_map.put(context.getPk_busiobj(), arroldnewattvalue);
/*     */       
/* 183 */       metaid_VO_arrattvaulemap_map.put(contextmetid, vopk_attvaluemap_map);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 190 */     HashMap<String, HashMap<String, String>> metaid_refpk_valuemap = new HashMap();
/*     */     
/* 192 */     getRefValueMap(allVOmetaid_attvaulemap_map, metaid_refpk_valuemap);
/*     */     
/* 194 */     for (BusinessLogESVO vo : listretnbusilogvo) {
/* 195 */       String metaid = vo.getTypepk_busiobj();
/* 196 */       String pk = vo.getPk_busiobj();
/* 197 */       if ((vo.getLogmsg() == null) || (vo.getLogmsg().isEmpty())) {
/* 198 */         BusilogAttValueInfo[] arrinfo = (BusilogAttValueInfo[])((HashMap)metaid_VO_arrattvaulemap_map.get(metaid)).get(pk);
/*     */         
/* 200 */         BusinessLogMSGVO[] arrmsgvo = convert2ArrMsgVO(arrinfo, metaid_refpk_valuemap);
/*     */         
/* 202 */         String logmsg = BusilogInfo2XMLConverter.getXMLWithBeforeVO(arrmsgvo);
/*     */         
/*     */ 
/* 205 */         vo.setLogmsg(logmsg);
/*     */       }
/*     */     }
/*     */     
/* 209 */     return listretnbusilogvo;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static void getSubInfo(Map<String, Map<String, List<String>>> pbeanidsonattrmap, Map<String, Map<String, String>> pbeanidsonattrnamemap, HashMap<String, List<String>> allVOmetaid_attvaulemap_map, String contextmetid, NCObject ncobj, BusilogAttValueInfo info, String submetaid)
/*     */     throws MetaDataException
/*     */   {
/* 218 */     IBean subbean = MDBaseQueryFacade.getInstance().getBeanByID(submetaid);
/* 219 */     String pkpath = subbean.getPrimaryKey().getPKColumn().getName();
/* 220 */     String subbeanname = (String)((Map)pbeanidsonattrnamemap.get(contextmetid)).get(submetaid);
/*     */     
/* 222 */     List<String> subattlist = (List)((Map)pbeanidsonattrmap.get(contextmetid)).get(submetaid);
/*     */     
/* 224 */     if ((ncobj != null) && (subattlist != null)) {
/* 225 */       NCObject[] arrsub = (NCObject[])ncobj.getAttributeValue(subbeanname);
/*     */       
/* 227 */       List<BusilogAttValueInfo> oldsblist = new ArrayList();
/* 228 */       if (arrsub != null) {
/* 229 */         for (NCObject subncvo : arrsub) {
/* 230 */           BusilogAttValueInfo suboldinfo = new BusilogAttValueInfo();
/* 231 */           suboldinfo.setMetaid(submetaid);
/* 232 */           suboldinfo.setPk(getDASAttMultiLangMaintext(subncvo.getAttributeValue(pkpath)));
/*     */           
/* 234 */           for (String substr : subattlist) {
/* 235 */             String subv = getDASAttMultiLangMaintext(subncvo.getAttributeValue(substr));
/*     */             
/* 237 */             Attribute suba = (Attribute)subbean.getAttributeByPath(substr);
/*     */             
/* 239 */             processMap(allVOmetaid_attvaulemap_map, subv, suba);
/* 240 */             suboldinfo.getMainmap().put(suba, subv);
/*     */           }
/* 242 */           oldsblist.add(suboldinfo);
/*     */         }
/*     */       }
/* 245 */       info.getSubmainmap().put(submetaid, oldsblist);
/*     */     }
/*     */   }
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
/*     */   private static void getRefValueMap(HashMap<String, List<String>> allVOmetaid_attvaulemap_map, HashMap<String, HashMap<String, String>> metaid_refpk_valuemap)
/*     */     throws MetaDataException
/*     */   {
/* 263 */     for (String metaid : allVOmetaid_attvaulemap_map.keySet()) {
/* 264 */       List<String> listrefpk = (List)allVOmetaid_attvaulemap_map.get(metaid);
/* 265 */       if ((listrefpk != null) && (listrefpk.size() != 0))
/*     */       {
/*     */ 
/* 268 */         HashMap<String, String> refpk_value_map = new HashMap();
/* 269 */         IBean bean = MDBaseQueryFacade.getInstance().getBeanByID(metaid);
/* 270 */         Map<String, String> submap1 = getNCObjMap(bean);
/* 271 */         List<String> listpath = new ArrayList();
/*     */         
/* 273 */         if (submap1.get("name") != null) {
/* 274 */           listpath.add(submap1.get("name"));
/*     */         }
/* 276 */         if (listpath.size() > 0) {
/* 277 */           String[] arrrefpk = (String[])listrefpk.toArray(new String[0]);
/* 278 */           Map<String, Object[]> map = DASFacade.getAttributeValues(bean, arrrefpk, (String[])listpath.toArray(new String[0]));
/*     */           
/*     */           Object[] arrobjname;
/* 281 */           if ((submap1.get("name") != null) && ((arrobjname = (Object[])map.get(submap1.get("name"))) != null))
/*     */           {
/* 283 */             for (int i = 0; i < arrobjname.length; i++) {
/* 284 */               if (getDASAttMultiLangMaintext(arrobjname[i]) != null) {
/* 285 */                 refpk_value_map.put(arrrefpk[i], getDASAttMultiLangMaintext(arrobjname[i]));
/*     */               }
/*     */               
/*     */             }
/*     */           } else {
/* 290 */             for (String spk : arrrefpk) {
/* 291 */               refpk_value_map.put(spk, spk);
/*     */             }
/*     */           }
/*     */         } else {
/* 295 */           for (String spk : listrefpk) {
/* 296 */             refpk_value_map.put(spk, spk);
/*     */           }
/*     */         }
/*     */         
/*     */ 
/* 301 */         metaid_refpk_valuemap.put(metaid, refpk_value_map);
/*     */       }
/*     */     }
/*     */   }
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
/*     */   private static void processAttrMaps(HashMap<String, List<String>> meta_mainattrlist_map, Map<String, Map<String, List<String>>> pbeanidsonattrmap, Map<String, Map<String, String>> pbeanidsonattrnamemap, String contextmetid, IBean bean, List<String> attstrlist)
/*     */   {
/* 321 */     if (attstrlist != null)
/*     */     {
/* 323 */       List<String> mainattlist = new ArrayList();
/*     */       
/* 325 */       Map<String, List<String>> sonattrmap = new HashMap();
/*     */       
/* 327 */       Map<String, String> sonattrnamemap = new HashMap();
/* 328 */       for (String str : attstrlist) {
/* 329 */         if (str.indexOf('.') == -1)
/*     */         {
/* 331 */           mainattlist.add(str);
/*     */         }
/*     */         else
/*     */         {
/* 335 */           String subbeanname = str.substring(0, str.indexOf('.'));
/* 336 */           Attribute attin = (Attribute)bean.getAttributeByPath(subbeanname);
/*     */           
/*     */ 
/* 339 */           if (attin != null) {
/* 340 */             String subattrname = str.substring(str.indexOf('.') + 1);
/*     */             
/*     */ 
/* 343 */             List<String> sunattrlist = (List)sonattrmap.get(attin.getDataTypeID());
/*     */             
/* 345 */             if (sunattrlist == null) {
/* 346 */               sunattrlist = new ArrayList();
/*     */             }
/* 348 */             sunattrlist.add(subattrname);
/*     */             
/* 350 */             sonattrmap.put(attin.getDataTypeID(), sunattrlist);
/*     */             
/* 352 */             sonattrnamemap.put(attin.getDataTypeID(), subbeanname);
/*     */           }
/*     */         }
/*     */       }
/* 356 */       meta_mainattrlist_map.put(contextmetid, mainattlist);
/* 357 */       pbeanidsonattrmap.put(contextmetid, sonattrmap);
/* 358 */       pbeanidsonattrnamemap.put(contextmetid, sonattrnamemap);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private static void processMap(HashMap<String, List<String>> allVOmetaid_attvaulemap_map, String value, Attribute att)
/*     */   {
/* 365 */     if (att.getDataType().getTypeType() == 204) {
/* 366 */       List<String> pklist = (List)allVOmetaid_attvaulemap_map.get(att.getDataTypeID());
/*     */       
/* 368 */       if (pklist == null) {
/* 369 */         pklist = new ArrayList();
/*     */       }
/* 371 */       if (!pklist.contains(value)) {
/* 372 */         pklist.add(value);
/*     */       }
/* 374 */       allVOmetaid_attvaulemap_map.put(att.getDataTypeID(), pklist);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private static BusinessLogMSGVO[] convert2ArrMsgVO(BusilogAttValueInfo[] arrinfo, HashMap<String, HashMap<String, String>> metaid_refpk_valuemap)
/*     */   {
/* 381 */     BusinessLogMSGVO[] msg = new BusinessLogMSGVO[2];
/* 382 */     msg[0] = convert2MsgVO(arrinfo[0], metaid_refpk_valuemap);
/* 383 */     msg[1] = convert2MsgVO(arrinfo[1], metaid_refpk_valuemap);
/* 384 */     return msg;
/*     */   }
/*     */   
/*     */ 
/*     */   private static BusinessLogMSGVO convert2MsgVO(BusilogAttValueInfo infovo, HashMap<String, HashMap<String, String>> metaid_refpk_valuemap)
/*     */   {
/* 390 */     BusinessLogMSGVO msgvo = new BusinessLogMSGVO();
/* 391 */     Map<String, List<BusinessLogMSGVO>> sublogobj = new HashMap();
/* 392 */     msgvo.setMetaid(infovo.getMetaid());
/* 393 */     msgvo.setPk_busiobj(infovo.getPk());
/*     */     
/* 395 */     HashMap<String, String> attmap = new HashMap();
/*     */     
/*     */ 
/* 398 */     if (infovo.getMainmap() != null) {
/* 399 */       for (Attribute attr : infovo.getMainmap().keySet()) {
/* 400 */         String nm = attr.getLocalDisplayName();
/* 401 */         String value = (String)infovo.getMainmap().get(attr);
/* 402 */         if ((attr.getDataType().getTypeType() == 204) && (value != null))
/*     */         {
/* 404 */           value = (String)((HashMap)metaid_refpk_valuemap.get(attr.getDataTypeID())).get(value);
/*     */         }
/* 406 */         else if (attr.getDataType().getTypeType() == 203) {
/* 407 */           value = getAttrEnumValue(attr, value);
/*     */         }
/* 409 */         attmap.put(nm, value);
/*     */       }
/*     */     }
/* 412 */     msgvo.setMap(attmap);
/*     */     
/*     */ 
/* 415 */     if (infovo.getSubmainmap() != null) {
/* 416 */       for (String submetaid : infovo.getSubmainmap().keySet())
/*     */       {
/* 418 */         List<BusinessLogMSGVO> lista = new ArrayList();
/* 419 */         if (infovo.getSubmainmap().get(submetaid) != null)
/*     */         {
/* 421 */           for (BusilogAttValueInfo subkeyvo : (List<BusilogAttValueInfo>)infovo.getSubmainmap().get(submetaid)) {
/* 422 */             BusinessLogMSGVO submsgvo = new BusinessLogMSGVO();
/* 423 */             submsgvo.setMetaid(submetaid);
/* 424 */             submsgvo.setPk_busiobj(subkeyvo.getPk());
/* 425 */             HashMap<String, String> subattmap = new HashMap();
/* 426 */             if (subkeyvo.getMainmap() != null) {
/* 427 */               for (Attribute attr : subkeyvo.getMainmap().keySet())
/*     */               {
/*     */ 
/* 430 */                 String nm = attr.getLocalDisplayName();
/* 431 */                 String value = (String)subkeyvo.getMainmap().get(attr);
/* 432 */                 if ((attr.getDataType().getTypeType() == 204) && (value != null))
/*     */                 {
/* 434 */                   value = (String)((HashMap)metaid_refpk_valuemap.get(attr.getDataTypeID())).get(value);
/*     */                 }
/* 436 */                 else if (attr.getDataType().getTypeType() == 203) {
/* 437 */                   value = getAttrEnumValue(attr, value);
/*     */                 }
/* 439 */                 subattmap.put(nm, value);
/*     */               }
/* 441 */               submsgvo.setMap(subattmap);
/*     */             }
/* 443 */             lista.add(submsgvo);
/*     */           }
/* 445 */           sublogobj.put(submetaid, lista);
/*     */         }
/*     */       }
/*     */     }
/* 449 */     msgvo.setSublogobj(sublogobj);
/* 450 */     return msgvo;
/*     */   }
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
/*     */   private static BusinessLogESVO getBaseLogVOInfo(BusinessLogContext context, String pk_group, String userid, UFDateTime logdate, String clientip)
/*     */   {
/* 467 */     BusinessLogESVO bslogvo = new BusinessLogESVO();
/* 468 */     bslogvo.setBusiobjcode(context.getBusiobjcode());
/*     */     
/* 470 */     bslogvo.setPk_busiobj(context.getPk_busiobj());
/*     */     
/* 472 */     bslogvo.setBusiobjname(context.getBusiobjname());
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 477 */     bslogvo.setOrgpk_busiobj(context.getOrgpk_busiobj());
/*     */     
/* 479 */     bslogvo.setPk_operation(context.getPk_operation());
/*     */     
/* 481 */     bslogvo.setClient(clientip);
/* 482 */     bslogvo.setTypepk_busiobj(context.getTypepk_busiobj());
/* 483 */     bslogvo.setTablename(BusiLogConvert.getTableName(context.getTypepk_busiobj()));
/*     */     
/* 485 */     bslogvo.setLogdate(logdate);
/* 486 */     bslogvo.setPk_group(pk_group);
/* 487 */     bslogvo.setPk_user(userid);
/* 488 */     bslogvo.setLogmsg(context.getLogmsg());
/* 489 */     return bslogvo;
/*     */   }
/*     */   
/*     */   private static String getDASAttMultiLangMaintext(Object o) {
/* 493 */     if (o != null) {
/* 494 */       if ((o instanceof MultiLangText)) {
/* 495 */         MultiLangText mt = (MultiLangText)o;
/* 496 */         return mt.getText();
/*     */       }
/* 498 */       return o.toString();
/*     */     }
/*     */     
/* 501 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public static BusinessLogESVO convert2BusinessLogVO(BusinessLogContext context)
/*     */     throws BusinessException
/*     */   {
/* 515 */     BusinessLogESVO bslogvo = new BusinessLogESVO();
/*     */     
/* 517 */     BusiLogConvert.checkContext(context);
/*     */     
/* 519 */     bslogvo.setBusiobjcode(context.getBusiobjcode());
/*     */     
/* 521 */     bslogvo.setPk_busiobj(context.getPk_busiobj());
/*     */     
/* 523 */     bslogvo.setBusiobjname(context.getBusiobjname());
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 528 */     bslogvo.setOrgpk_busiobj(context.getOrgpk_busiobj());
/*     */     
/* 530 */     bslogvo.setPk_operation(context.getPk_operation());
/*     */     
/* 532 */     bslogvo.setClient(InvocationInfoProxy.getInstance().getClientHost());
/* 533 */     Logger.error("convert2BusinessLogVO-host ip;;;----" + InvocationInfoProxy.getInstance().getClientHost());
/*     */     
/*     */ 
/* 536 */     bslogvo.setLogdate(new UFDateTime(new Date()));
/*     */     
/* 538 */     bslogvo.setPk_user(InvocationInfoProxy.getInstance().getUserId());
/*     */     
/* 540 */     bslogvo.setPk_group(InvocationInfoProxy.getInstance().getGroupId());
/*     */     
/* 542 */     if (checkStringIsNull(context.getLogmsg()))
/*     */     {
/* 544 */       BusinessLogMSGVO oldmsgvo = BusinessContext2BusiLogMsgVOWithContent(context.getTypepk_busiobj(), context.getPk_busiobj(), context.getOldbusiobjvo(), InvocationInfoProxy.getInstance().getGroupId(), InvocationInfoProxy.getInstance().getUserDataSource());
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 549 */       BusinessLogMSGVO newmsgvo = BusinessContext2BusiLogMsgVOWithContent(context.getTypepk_busiobj(), context.getPk_busiobj(), context.getBusiobjvo(), InvocationInfoProxy.getInstance().getGroupId(), InvocationInfoProxy.getInstance().getUserDataSource());
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 554 */       BusinessLogMSGVO[] arrmsgvo = { oldmsgvo, newmsgvo };
/* 555 */       String xml = "";
/*     */       try {
/* 557 */         xml = BusilogInfo2XMLConverter.getXMLWithBeforeVO(arrmsgvo);
/*     */       } catch (Exception e) {
/* 559 */         Logger.error("构造消息失败");
/*     */       }
/* 561 */       if (checkStringIsNull(xml)) {
/* 562 */         Logger.error("构造消息失败");
/*     */       }
/* 564 */       bslogvo.setLogmsg(xml);
/*     */     } else {
/* 566 */       bslogvo.setLogmsg(context.getLogmsg());
/*     */     }
/* 568 */     bslogvo.setTypepk_busiobj(context.getTypepk_busiobj());
/* 569 */     bslogvo.setTablename(BusiLogConvert.getTableName(context.getTypepk_busiobj()));
/*     */     
/* 571 */     return bslogvo;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static HashMap<String, HashMap<String, List<BusinessLogESVO>>> convert2AsynSaveVOMap(List<BusinessLogAsynInfo> listasyninfo)
/*     */   {
/* 583 */     if (listasyninfo == null)
/* 584 */       return null;
/* 585 */     HashMap<String, HashMap<String, List<BusinessLogESVO>>> vomap = new HashMap();
/*     */     
/* 587 */     for (BusinessLogAsynInfo info : listasyninfo) {
/* 588 */       InvocationInfoProxy.getInstance().setUserDataSource(info.getDatasource());
/*     */       
/*     */ 
/* 591 */       List<BusinessLogESVO> listbslogvo1 = null;
/*     */       try {
/* 593 */         listbslogvo1 = convert2BusinessLogVO(info);
/*     */       }
/*     */       catch (Exception e) {
/* 596 */         Logger.error("异步写日志,转换BusinessLogAsynInfo为vo错误:" + e.getMessage());
/*     */       }
/*     */       
/*     */ 
/* 600 */       if (listbslogvo1 != null) {
/* 601 */         String tablename = BusiLogConvert.getTableName(((BusinessLogContext)info.getContext().get(0)).getTypepk_busiobj());
/*     */         
/* 603 */         String ds = info.getDatasource();
/* 604 */         Logger.error("------------------zhoutest asyn to ds key map  ds===ds=" + ds);
/*     */         
/*     */ 
/* 607 */         if (!vomap.containsKey(ds)) {
/* 608 */           HashMap<String, List<BusinessLogESVO>> subvomap = new HashMap();
/* 609 */           List<BusinessLogESVO> listvo = new ArrayList();
/* 610 */           listvo.addAll(listbslogvo1);
/* 611 */           subvomap.put(tablename, listvo);
/* 612 */           vomap.put(ds, subvomap);
/*     */ 
/*     */         }
/*     */         else
/*     */         {
/* 617 */           HashMap<String, List<BusinessLogESVO>> subvomap = (HashMap)vomap.get(ds);
/*     */           
/* 619 */           if (subvomap == null) {
/* 620 */             subvomap = new HashMap();
/*     */           }
/* 622 */           List<BusinessLogESVO> listvo = null;
/*     */           
/* 624 */           if (subvomap.containsKey(tablename)) {
/* 625 */             listvo = (List)subvomap.get(tablename);
/*     */           }
/*     */           
/* 628 */           if (listvo == null) {
/* 629 */             listvo = new ArrayList();
/*     */           }
/* 631 */           listvo.addAll(listbslogvo1);
/* 632 */           subvomap.put(tablename, listvo);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 637 */     Logger.error("------------------zhoutest map 转换完成　convert2AsynSaveVOMap");
/* 638 */     return vomap;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static List<BusinessLogESVO> convert2BusinessLogVO(BusinessLogAsynInfo info)
/*     */     throws BusinessException
/*     */   {
/* 651 */     if (info == null) {
/* 652 */       return null;
/*     */     }
/* 654 */     BusiLogConvert.checkAsyninfo(info);
/* 655 */     List<BusinessLogESVO> list = new ArrayList();
/* 656 */     list = convertContextList2BusinessLogVOList(info.getContext(), info.getPk_group(), info.getUserid(), info.getLogdate(), info.getClient(), info.getDatasource());
/*     */     
/*     */ 
/*     */ 
/* 660 */     return list;
/*     */   }
/*     */   
/*     */   public static boolean checkStringIsNull(String s) {
/* 664 */     if ((s == null) || (s.trim().equals(""))) {
/* 665 */       return true;
/*     */     }
/* 667 */     return false;
/*     */   }
/*     */   
/*     */   private static Map<String, String> getNCObjMap(IBean bean) {
/* 671 */     Map<String, String> bditfmap = ((IBusinessEntity)bean).getBizInterfaceMapInfo("nc.vo.bd.meta.IBDObject");
/*     */     
/* 673 */     Map<String, String> returnmap = new HashMap();
/* 674 */     if ((bditfmap != null) && (bditfmap.size() > 0))
/*     */     {
/* 676 */       String codeattr = (String)bditfmap.get("code");
/* 677 */       String nameattr = (String)bditfmap.get("name");
/*     */       
/* 679 */       String pk_org = (String)bditfmap.get("pk_org");
/*     */       
/* 681 */       returnmap.put("code", codeattr);
/* 682 */       returnmap.put("name", nameattr);
/* 683 */       returnmap.put("id", bean.getPrimaryKey().getPKColumn().getName());
/* 684 */       returnmap.put("pk_org", pk_org);
/*     */     }
/*     */     else {
/* 687 */       bditfmap = ((IBusinessEntity)bean).getBizInterfaceMapInfo("nc.itf.uap.pf.metadata.IFlowBizItf");
/*     */       
/* 689 */       String codeattr = (String)bditfmap.get("billno");
/*     */       
/*     */ 
/* 692 */       String pk_org = (String)bditfmap.get("pkorg");
/*     */       
/* 694 */       returnmap.put("code", codeattr);
/*     */       
/* 696 */       returnmap.put("id", bean.getPrimaryKey().getPKColumn().getName());
/* 697 */       returnmap.put("pk_org", pk_org);
/*     */     }
/* 699 */     return returnmap;
/*     */   }
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
/*     */   public static BusinessLogMSGVO BusinessContext2BusiLogMsgVOWithContent(String contextmetid, String pk_busiobjvo, Object fromortobusiobj, String pk_group, String ds)
/*     */     throws BusinessException
/*     */   {
/* 719 */     InvocationInfoProxy.getInstance().setUserDataSource(ds);
/*     */     
/* 721 */     BusinessLogMSGVO msgvo = new BusinessLogMSGVO();
/*     */     
/* 723 */     Object obj = fromortobusiobj;
/*     */     
/* 725 */     if (obj == null) {
/* 726 */       return null;
/*     */     }
/*     */     
/* 729 */     IBean beancontext = MDBaseQueryFacade.getInstance().getBeanByID(contextmetid);
/*     */     
/* 731 */     if (beancontext == null)
/* 732 */       return null;
/* 733 */     Logger.error("----------------zhoutest-----" + beancontext == null ? "" : beancontext.getFullClassName());
/*     */     
/*     */ 
/* 736 */     List<String> list = LogConfigServiceFacade.getInstance().getAttrbuteNamePath(pk_group, contextmetid, false);
/*     */     
/* 738 */     if ((list == null) || (list.size() == 0)) {
/* 739 */       return null;
/*     */     }
/* 741 */     Map<String, String> attrmap = new HashMap();
/*     */     
/*     */     try
/*     */     {
/* 745 */       NCObject ncobj = NCObject.newInstance(beancontext, obj);
/* 746 */       if (ncobj == null) {
/* 747 */         return null;
/*     */       }
/* 749 */       Map<String, List<String>> sonattrmap = new HashMap();
/*     */       
/* 751 */       Map<String, String> sonattrnamemap = new HashMap();
/*     */       
/* 753 */       for (String str : list) {
/* 754 */         if (str.indexOf('.') == -1) {
/* 755 */           Attribute attr = (Attribute)beancontext.getAttributeByPath(str);
/*     */           
/* 757 */           if (attr != null) {
/* 758 */             String key = attr.getLocalDisplayName();
/*     */             
/*     */ 
/* 761 */             String value = ncobj.getAttributeValue(attr) == null ? null : ncobj.getAttributeValue(attr).toString();
/*     */             
/*     */ 
/* 764 */             if (attr.getDataType().getTypeType() == 204) {
/* 765 */               value = getAttVaule(ncobj, attr, value);
/* 766 */             } else if (attr.getDataType().getTypeType() == 203) {
/* 767 */               value = getAttrEnumValue(ncobj, attr, value);
/*     */             } else {
/* 769 */               value = ncobj.getAttributeValue(attr) == null ? null : ncobj.getAttributeValue(attr).toString();
/*     */             }
/*     */             
/*     */ 
/* 773 */             if ((value != null) && (value.trim().length() > 0)) {
/* 774 */               attrmap.put(key, value);
/*     */             }
/*     */           }
/*     */         }
/*     */         else
/*     */         {
/* 780 */           String subbeanname = str.substring(0, str.indexOf('.'));
/* 781 */           Attribute attin = (Attribute)beancontext.getAttributeByPath(subbeanname);
/*     */           
/* 783 */           if (attin != null) {
/* 784 */             String subattrname = str.substring(str.indexOf('.') + 1);
/*     */             
/*     */ 
/* 787 */             List<String> sunattrlist = (List)sonattrmap.get(attin.getDataTypeID());
/*     */             
/* 789 */             if (sunattrlist == null) {
/* 790 */               sunattrlist = new ArrayList();
/*     */             }
/* 792 */             sunattrlist.add(subattrname);
/*     */             
/* 794 */             sonattrmap.put(attin.getDataTypeID(), sunattrlist);
/*     */             
/* 796 */             sonattrnamemap.put(attin.getDataTypeID(), subbeanname);
/*     */           }
/*     */         }
/*     */       }
/*     */       
/* 801 */       Map<String, List<BusinessLogMSGVO>> sublogmap = new HashMap();
/*     */       
/* 803 */       for (String key : sonattrmap.keySet())
/*     */       {
/* 805 */         List<String> sublogattrlist = (List)sonattrmap.get(key);
/*     */         
/* 807 */         IBean subbean = MDBaseQueryFacade.getInstance().getBeanByID(key);
/*     */         
/*     */ 
/* 810 */         NCObject[] subncobjarr = (NCObject[])ncobj.getAttributeValue((String)sonattrnamemap.get(key));
/*     */         
/* 812 */         if (subncobjarr != null)
/*     */         {
/*     */ 
/*     */ 
/* 816 */           List<BusinessLogMSGVO> listsubmsg = null;
/* 817 */           listsubmsg = (List)sublogmap.get(key);
/* 818 */           if (listsubmsg == null) {
/* 819 */             listsubmsg = new ArrayList();
/*     */           }
/*     */           
/* 822 */           for (NCObject subncojb : subncobjarr) {
/* 823 */             BusinessLogMSGVO newsubmsgvo = new BusinessLogMSGVO();
/* 824 */             newsubmsgvo.setMap(new HashMap());
/* 825 */             newsubmsgvo.setMetaid(subbean.getID());
/* 826 */             newsubmsgvo.setPk_busiobj(subncojb.getAttributeValue((String)getNCObjMap(subbean).get("id")) == null ? "" : subncojb.getAttributeValue((String)getNCObjMap(subbean).get("id")).toString());
/*     */             
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 834 */             for (String subarr : sublogattrlist) {
/* 835 */               Attribute subattr = (Attribute)subbean.getAttributeByPath(subarr);
/*     */               
/* 837 */               if (subattr != null) {
/* 838 */                 String sublogkey = subattr.getLocalDisplayName();
/* 839 */                 String svalue = subncojb.getAttributeValue(subattr) == null ? "" : subncojb.getAttributeValue(subattr).toString();
/*     */                 
/*     */ 
/*     */ 
/* 843 */                 if (subattr.getDataType().getTypeType() == 204) {
/* 844 */                   svalue = getAttVaule(subncojb, subattr, svalue);
/* 845 */                 } else if (subattr.getDataType().getTypeType() == 203) {
/* 846 */                   svalue = getAttrEnumValue(subncojb, subattr, svalue);
/*     */                 }
/*     */                 else {
/* 849 */                   svalue = subncojb.getAttributeValue(subattr) == null ? "" : subncojb.getAttributeValue(subattr).toString();
/*     */                 }
/*     */                 
/*     */ 
/* 853 */                 if ((svalue != null) && (svalue.trim().length() > 0)) {
/* 854 */                   newsubmsgvo.getMap().put(sublogkey, svalue);
/*     */                 }
/*     */               }
/*     */             }
/*     */             
/* 859 */             listsubmsg.add(newsubmsgvo);
/*     */           }
/*     */           
/*     */ 
/* 863 */           sublogmap.put(key, listsubmsg);
/*     */         }
/*     */       }
/*     */       
/* 867 */       Map<String, String> map = getNCObjMap(beancontext);
/* 868 */       msgvo.setPk_busiobj(ncobj.getAttributeValue((String)map.get("id")) == null ? "" : ncobj.getAttributeValue((String)map.get("id")).toString());
/*     */       
/* 870 */       msgvo.setMetaid(beancontext.getID());
/* 871 */       msgvo.setPk_busiobj(pk_busiobjvo);
/* 872 */       msgvo.setMap(attrmap);
/* 873 */       msgvo.setSublogobj(sublogmap);
/*     */     } catch (Exception e) {
/* 875 */       Logger.error(e);
/*     */     }
/* 877 */     return msgvo;
/*     */   }
/*     */   
/*     */   private static String getAttrEnumValue(Attribute attr, String value)
/*     */   {
/*     */     try {
/* 883 */       EnumType t = (EnumType)attr.getDataType();
/* 884 */       IConstEnum icom = null;
/* 885 */       for (IConstEnum ic : t.getConstEnums()) {
/* 886 */         if (ic.getValue().toString().equals(value)) {
/* 887 */           icom = ic;
/* 888 */           break;
/*     */         }
/*     */       }
/* 891 */       if (icom != null) {
/* 892 */         value = icom.getName();
/*     */       }
/*     */     }
/*     */     catch (Exception e) {}
/*     */     
/* 897 */     return value;
/*     */   }
/*     */   
/*     */   private static String getAttrEnumValue(NCObject toncobj, Attribute attr, String value) {
/*     */     try {
/* 902 */       EnumType t = (EnumType)attr.getDataType();
/* 903 */       IConstEnum icom = null;
/* 904 */       for (IConstEnum ic : t.getConstEnums()) {
/* 905 */         if (ic.getValue().toString().equals(value)) {
/* 906 */           icom = ic;
/* 907 */           break;
/*     */         }
/*     */       }
/* 910 */       if (icom != null) {
/* 911 */         value = icom.getName();
/*     */       }
/*     */     } catch (Exception e) {
/* 914 */       value = toncobj.getAttributeValue(attr) == null ? null : toncobj.getAttributeValue(attr).toString();
/*     */     }
/*     */     
/* 917 */     return value;
/*     */   }
/*     */   
/*     */   private static String getAttVaule(NCObject toncobj, Attribute att, String value)
/*     */   {
/*     */     try {
/* 923 */       IBean sonbean = null;
/*     */       try {
/* 925 */         sonbean = MDBaseQueryFacade.getInstance().getBeanByID(att.getDataTypeID());
/*     */       }
/*     */       catch (MetaDataException e) {
/* 928 */         Logger.error(e);
/*     */       }
/* 930 */       if (sonbean != null) {
/* 931 */         Map<String, String> submap1 = getNCObjMap(sonbean);
/*     */         
/* 933 */         ArrayList<String> subattlist = new ArrayList();
/* 934 */         if ((submap1 != null) && (submap1.size() > 0)) {
/* 935 */           for (String v : submap1.keySet()) {
/* 936 */             if ((submap1.get(v) != null) && (!((String)submap1.get(v)).trim().isEmpty()))
/*     */             {
/* 938 */               subattlist.add(submap1.get(v));
/*     */             }
/*     */           }
/*     */         }
/*     */         
/* 943 */         if (subattlist.size() > 0) {
/* 944 */           value = toncobj.getAttributeValue(att) == null ? null : toncobj.getAttributeValue(att).toString();
/*     */           
/* 946 */           if (subattlist.size() == 0) {
/* 947 */             return value;
/*     */           }
/* 949 */           if (value != null) {
/* 950 */             Map<String, Object> sonmap = DASFacade.getAttributeValues(sonbean, value, (String[])subattlist.toArray(new String[0]));
/*     */             
/*     */ 
/* 953 */             if (sonmap != null) {
/* 954 */               if ((submap1.get("name") != null) && (sonmap.keySet().contains(submap1.get("name"))))
/*     */               {
/*     */ 
/* 957 */                 if ((sonmap.get(submap1.get("name")) instanceof MultiLangText)) {
/* 958 */                   MultiLangText mt = (MultiLangText)sonmap.get(submap1.get("name"));
/*     */                   
/* 960 */                   value = mt.getText();
/*     */                 }
/* 962 */                 value = "" + sonmap.get(submap1.get("name")) + "";
/*     */               }
/* 964 */               else if ((submap1.get("code") != null) && (sonmap.keySet().contains(submap1.get("code"))))
/*     */               {
/*     */ 
/* 967 */                 value = "" + sonmap.get(submap1.get("code")) + "";
/*     */               }
/*     */               else {
/* 970 */                 value = toncobj.getAttributeValue(att) == null ? null : toncobj.getAttributeValue(att).toString();
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     catch (Exception e) {
/* 978 */       value = toncobj.getAttributeValue(att) == null ? null : toncobj.getAttributeValue(att).toString();
/*     */     }
/*     */     
/* 981 */     return value;
/*     */   }
/*     */ }

/* Location:           D:\zhw\home0816\modules\baseapp\META-INF\lib\baseapp_applogsLevel-1.jar
 * Qualified Name:     nc.bs.busilog.BusiLogVOConvert
 * Java Class Version: 7 (51.0)
 * JD-Core Version:    0.7.1
 */