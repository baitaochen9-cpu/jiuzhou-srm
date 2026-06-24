package nc.ui.uif2;
/*     */ 
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;

/*     */ import javax.xml.transform.Transformer;
/*     */ import javax.xml.transform.TransformerException;
/*     */ import javax.xml.transform.TransformerFactory;
/*     */ import javax.xml.transform.dom.DOMSource;
/*     */ import javax.xml.transform.stream.StreamResult;
/*     */ import javax.xml.xpath.XPath;
/*     */ import javax.xml.xpath.XPathConstants;
/*     */ import javax.xml.xpath.XPathFactory;

import nc.bs.busilog.vo.BusiLogXMLTemplet;
import nc.bs.busilog.vo.BusinessLogMSGVO;
/*     */ import nc.bs.logging.Logger;
/*     */ import nc.md.MDBaseQueryFacade;
/*     */ import nc.md.model.IBean;
/*     */ import nc.md.model.MetaDataException;

/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.NodeList;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BusilogInfo2XMLConverter
/*     */ {
/*     */   private static final String before = "before";
/*     */   private static final String attribute = "attribute";
/*     */   private static final String metaid = "metaid";
/*     */   private static final String displayname = "displayname";
/*     */   private static final String type = "type";
/*     */   
/*     */   public static String getXMLMsgVO(BusinessLogMSGVO msgvo) throws MetaDataException, UnsupportedEncodingException, TransformerException
/*     */   {
/*  37 */     if ((msgvo == null) || ((msgvo.getMap() == null) && (msgvo.getSublogobj() == null))) { return "";
/*     */     }
/*  39 */     Document doc = null;
/*  40 */     Element rootelem = null;
/*     */     try {
/*  42 */       doc = BusiLogXMLTemplet.getLogTemplet();
/*  43 */       NodeList nodelist = SelectNodes("/entity", doc);
/*  44 */       if (nodelist != null) {
/*  45 */         rootelem = (Element)nodelist.item(0);
/*     */       }
/*  47 */       if (rootelem == null) return "";
/*  48 */       rootelem.setAttribute("id", msgvo.getMetaid());
/*     */     }
/*     */     catch (Exception ex) {
/*  51 */       Logger.error(ex.getMessage());
/*     */     }
/*     */     
/*  54 */     if (msgvo.getMap() != null) {
/*  55 */       for (String key : msgvo.getMap().keySet()) {
/*  56 */         Element attelem = doc.createElement("attribute");
/*  57 */         attelem.setAttribute("displayname", key);
/*  58 */         Logger.debug("--------------zhoutest BusilogInfo2XMLConverter 主实体 新增删除-记录xml--dispname-------" + key);
/*  59 */         Logger.debug("--------------zhoutest BusilogInfo2XMLConverter 主实体新增删除-记录xml--value-------" + (String)msgvo.getMap().get(key));
/*  60 */         attelem.setTextContent((String)msgvo.getMap().get(key));
/*  61 */         rootelem.appendChild(attelem);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*  66 */     if (msgvo.getSublogobj() != null) {
	 Element childattelem;
/*  67 */       for (String key : msgvo.getSublogobj().keySet()) {
/*  68 */         List<BusinessLogMSGVO> listmsgvo = (List)msgvo.getSublogobj().get(key);
/*  69 */         if ((listmsgvo != null) && (listmsgvo.size() > 0)) {
/*  70 */           IBean bean = MDBaseQueryFacade.getInstance().getBeanByID(key);
/*  71 */           childattelem = doc.createElement("attribute");
/*  72 */           childattelem.setAttribute("displayname", bean.getDisplayName());
/*  73 */           childattelem.setAttribute("type", "child");
/*  74 */           childattelem.setAttribute("metaid", key);
/*  75 */           rootelem.appendChild(childattelem);
/*  76 */           for (BusinessLogMSGVO submsgvo : listmsgvo)
/*  77 */             processxml(submsgvo, childattelem, doc);
/*     */         }
/*     */       }
/*     */     }
/*     */    
/*  82 */     return Doc2StringConvert(doc);
/*     */   }
/*     */   
/*     */   private static void processxml(BusinessLogMSGVO submsgvo, Element elem, Document doc) throws MetaDataException
/*     */   {
/*  87 */     Element entityelem = doc.createElement("entity");
/*  88 */     entityelem.setAttribute("id", submsgvo.getPk_busiobj());
/*  89 */     entityelem.setAttribute("status", String.valueOf(submsgvo.getStatus()));
/*  90 */     elem.appendChild(entityelem);
/*     */     
/*     */ 
/*  93 */     for (String key : submsgvo.getMap().keySet()) {
/*  94 */       Element attelem = doc.createElement("attribute");
/*  95 */       attelem.setAttribute("displayname", key);
/*  96 */       attelem.setTextContent((String)submsgvo.getMap().get(key));
/*  97 */       entityelem.appendChild(attelem);
/*  98 */       Logger.debug("--------------zhoutest BusilogInfo2XMLConverter 子实体 新增删除-记录xml--dispname-------" + key);
/*  99 */       Logger.debug("--------------zhoutest BusilogInfo2XMLConverter 自实体新增删除-记录xml--value-------" + (String)submsgvo.getMap().get(key));
/*     */     }
/*     */     
/*     */ 
/* 103 */     if (submsgvo.getSublogobj() != null) {
/* 104 */       for (String key : submsgvo.getSublogobj().keySet()) {
/* 105 */         List<BusinessLogMSGVO> listmsgvo = (List)submsgvo.getSublogobj().get(key);
/* 106 */         if ((listmsgvo != null) && (listmsgvo.size() > 0)) {
/* 107 */           IBean bean = MDBaseQueryFacade.getInstance().getBeanByID(key);
/* 108 */           Element childattelem = doc.createElement("attribute");
/* 109 */           childattelem.setAttribute("displayname", bean.getDisplayName());
/* 110 */           childattelem.setAttribute("type", "child");
/* 111 */           childattelem.setAttribute("metaid", key);
/* 112 */           for (BusinessLogMSGVO submsgvo1 : listmsgvo) {
/* 113 */             processxml(submsgvo1, elem, doc);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private static String Doc2StringConvert(Document doc)
/*     */     throws TransformerException, UnsupportedEncodingException
/*     */   {
/* 124 */     TransformerFactory fac = TransformerFactory.newInstance();
/* 125 */     Transformer trans = fac.newTransformer();
/* 126 */     ByteArrayOutputStream outsm = new ByteArrayOutputStream();
/* 127 */     StreamResult xmlresult = new StreamResult(outsm);
/* 128 */     DOMSource domsource = new DOMSource(doc);
/*     */     
/* 130 */     trans.transform(domsource, xmlresult);
/*     */     
/* 132 */     return outsm.toString("utf-8");
/*     */   }
/*     */   
/* 135 */   private static NodeList SelectNodes(String express, Object source) throws javax.xml.xpath.XPathExpressionException { NodeList nlist = null;
/* 136 */     XPathFactory fac = XPathFactory.newInstance();
/* 137 */     XPath xpath = fac.newXPath();
/*     */     
/* 139 */     nlist = (NodeList)xpath.evaluate(express, source, XPathConstants.NODESET);
/*     */     
/* 141 */     return nlist;
/*     */   }
/*     */   
/*     */   public static String getXMLWithBeforeVO(BusinessLogMSGVO[] arrmsgvo)
/*     */   {
/* 146 */     Document doc = null;
/* 147 */     Element rootelem = null;
/* 148 */     if ((arrmsgvo == null) || (arrmsgvo.length < 2)) {
/* 149 */       return "";
/*     */     }
/*     */     try {
/* 152 */       BusinessLogMSGVO oldvo = arrmsgvo[0];
/* 153 */       BusinessLogMSGVO newvo = arrmsgvo[1];
/*     */       
/* 155 */       if ((oldvo == null) && (newvo != null)) {
/* 156 */         Logger.debug("------------zhoutest 只有修改后，没有修改前（是新增）");
/* 157 */         processMsgVO(newvo, 2);
/* 158 */         return getXMLMsgVO(newvo);
/*     */       }
/*     */       
/* 161 */       if ((oldvo != null) && (newvo == null)) {
/* 162 */         Logger.debug("------------zhoutest 只有修改前，修改后空是删除");
/* 163 */         processMsgVO(oldvo, 3);
/* 164 */         return getXMLMsgVO(oldvo);
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */       try
/*     */       {
/* 172 */         doc = BusiLogXMLTemplet.getLogTemplet();
/* 173 */         NodeList nodelist = SelectNodes("/entity", doc);
/* 174 */         if (nodelist != null) {
/* 175 */           rootelem = (Element)nodelist.item(0);
/* 176 */           rootelem.setAttribute("id", newvo.getPk_busiobj());
/* 177 */           rootelem.setAttribute("metaid", newvo.getMetaid());
/*     */         }
/*     */       }
/*     */       catch (Exception e)
/*     */       {
/* 182 */         Logger.error("生成日志消息XML失败！");
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 187 */       HashSet<String> oldandnewkeyset = new HashSet();
/* 188 */       if (oldvo.getMap() != null) {
/* 189 */         oldandnewkeyset.addAll(oldvo.getMap().keySet());
/*     */       }
/* 191 */       if (newvo.getMap() != null) {
/* 192 */         oldandnewkeyset.addAll(newvo.getMap().keySet());
/*     */       }
/*     */       
for (String key : oldandnewkeyset) {
	boolean isadd = false;

	if (newvo.getMap().get(key) == null) {
		if (oldvo.getMap().get(key) != null) {
			isadd = true;
		}
	} else if (oldvo.getMap().get(key) == null) {
		if (newvo.getMap().get(key) != null) {
			isadd = true;
		}
	} else if (!((String) oldvo.getMap().get(key)).equals(newvo
			.getMap().get(key))) {
		isadd = true;
	}

	if (isadd) {
		Element attelem = doc.createElement("attribute");
		attelem.setAttribute("displayname", key);
		rootelem.appendChild(attelem);
		Logger.debug("--------------zhoutest BusilogInfo2XMLConverter 对比主实体"
				+ key);
		attelem.setTextContent((String) newvo.getMap().get(key));
		Logger.debug("--------------zhoutest BusilogInfo2XMLConverter 对比主实体值"
				+ (String) newvo.getMap().get(key));
		Element from_elem = doc.createElement("before");
		from_elem.setTextContent((String) oldvo.getMap().get(key));
		attelem.appendChild(from_elem);
		Logger.debug("--------------zhoutest BusilogInfo2XMLConverter 对比主实体旧值"
				+ (String) oldvo.getMap().get(key));
	}
}
/*     */       
/*     */ 
/*     */       try
/*     */       {
/* 220 */         HashSet<String> submetaidkeyset = new HashSet();
/* 221 */         if ((oldvo.getSublogobj() != null) && (oldvo.getSublogobj().keySet() != null)) {
/* 222 */           submetaidkeyset.addAll(oldvo.getSublogobj().keySet());
/*     */         }
/* 224 */         if ((newvo.getSublogobj() != null) && (newvo.getSublogobj().keySet() != null)) {
/* 225 */           submetaidkeyset.addAll(newvo.getSublogobj().keySet());
/*     */         }
/*     */         
/*     */ 
/* 229 */         for (String key : submetaidkeyset) {
/*     */           Element childattelem;
/* 231 */           if ((oldvo.getSublogobj().keySet().contains(key)) && (!newvo.getSublogobj().keySet().contains(key)))
/*     */           {
/* 233 */             Logger.debug("-------------zhoutest---修改前包含子元数据，修改后不包含子元数据，认为修改后删掉了这个子元数据子实体（删）");
/* 234 */             List<BusinessLogMSGVO> listmsgvo = (List)oldvo.getSublogobj().get(key);
/* 235 */             if ((listmsgvo != null) && (listmsgvo.size() > 0)) {
/* 236 */               IBean bean = MDBaseQueryFacade.getInstance().getBeanByID(key);
/* 237 */               childattelem = doc.createElement("attribute");
/* 238 */               childattelem.setAttribute("displayname", bean.getDisplayName());
/* 239 */               childattelem.setAttribute("type", "child");
/* 240 */               childattelem.setAttribute("metaid", key);
/* 241 */               rootelem.appendChild(childattelem);
/* 242 */               for (BusinessLogMSGVO submsgvo : listmsgvo) {
/* 243 */                 submsgvo.setStatus(3);
/* 244 */                 processxml(submsgvo, childattelem, doc);
/*     */               }
/*     */             }
/*     */           } else {
/* 249 */             if ((!oldvo.getSublogobj().keySet().contains(key)) && (newvo.getSublogobj().keySet().contains(key)))
/*     */             {
/* 251 */               Logger.debug("-------------zhoutest---//3.2.2修改前不包含子元数据，修改后包含子元数据，认为修改后新增了这个子元数据子实体（增）");
/* 252 */               List<BusinessLogMSGVO> listmsgvo = (List)newvo.getSublogobj().get(key);
/* 253 */               if ((listmsgvo != null) && (listmsgvo.size() > 0)) {
/* 254 */                 IBean bean = MDBaseQueryFacade.getInstance().getBeanByID(key);
/* 255 */                 childattelem = doc.createElement("attribute");
/* 256 */                 childattelem.setAttribute("displayname", bean.getDisplayName());
/* 257 */                 childattelem.setAttribute("type", "child");
/* 258 */                 childattelem.setAttribute("metaid", key);
/* 259 */                 rootelem.appendChild(childattelem);
/* 260 */                 for (BusinessLogMSGVO submsgvo : listmsgvo) {
/* 261 */                   submsgvo.setStatus(2);
/* 262 */                   processxml(submsgvo, childattelem, doc);
/*     */                 }
/*     */                 
/*     */               }
/*     */               
/*     */             }
/*     */             else
/*     */             {
/* 270 */               Logger.debug("-------------zhoutest---//3.2.3修改前后都有某种子实体，认为子实体修改了（增删改都可能）");
/* 271 */               List<BusinessLogMSGVO> oldmsgvolist = (List)oldvo.getSublogobj().get(key);
/* 272 */               List<BusinessLogMSGVO> newmsgvolist = (List)newvo.getSublogobj().get(key);
/* 274 */               if ((oldmsgvolist == null) || (oldmsgvolist.size() == 0)) {
/* 275 */                 Logger.debug("-------------zhoutest---没有旧数据，认为新增");
/* 276 */                 List<BusinessLogMSGVO> listmsgvo = (List)newvo.getSublogobj().get(key);
/* 277 */                 if ((listmsgvo != null) && (listmsgvo.size() > 0)) {
/* 278 */                   IBean bean = MDBaseQueryFacade.getInstance().getBeanByID(key);
/* 279 */                   childattelem = doc.createElement("attribute");
/* 280 */                   childattelem.setAttribute("displayname", bean.getDisplayName());
/* 281 */                   childattelem.setAttribute("type", "child");
/* 282 */                   childattelem.setAttribute("metaid", key);
/* 283 */                   rootelem.appendChild(childattelem);
/* 284 */                   for (BusinessLogMSGVO submsgvo : listmsgvo) {
/* 285 */                     submsgvo.setStatus(2);
/* 286 */                     processxml(submsgvo, childattelem, doc);
/*     */                   }
/*     */                 }
/*     */               } else {
/* 291 */                 if ((newmsgvolist == null) || (newmsgvolist.size() == 0)) {
/* 292 */                   Logger.debug("-------------zhoutest---没有新数据，认为删除");
/* 293 */                   List<BusinessLogMSGVO> listmsgvo = (List)oldvo.getSublogobj().get(key);
/* 294 */                   if ((listmsgvo != null) && (listmsgvo.size() > 0)) {
/* 295 */                     IBean bean = MDBaseQueryFacade.getInstance().getBeanByID(key);
/* 296 */                     childattelem = doc.createElement("attribute");
/* 297 */                     childattelem.setAttribute("displayname", bean.getDisplayName());
/* 298 */                     childattelem.setAttribute("type", "child");
/* 299 */                     childattelem.setAttribute("metaid", key);
/* 300 */                     rootelem.appendChild(childattelem);
/* 301 */                     for (BusinessLogMSGVO submsgvo : listmsgvo) {
/* 302 */                       submsgvo.setStatus(3);
/* 303 */                       processxml(submsgvo, childattelem, doc);
/*     */                     }
/*     */                   }
/*     */                 }
/*     */                 else {
/* 308 */                   Logger.debug("-------------zhoutest---都不空，修改");
/*     */                   
/* 310 */                   IBean bean = MDBaseQueryFacade.getInstance().getBeanByID(key);
/* 311 */                   childattelem = doc.createElement("attribute");
/* 312 */                   childattelem.setAttribute("displayname", bean.getDisplayName());
/* 313 */                   childattelem.setAttribute("type", "child");
/* 314 */                   childattelem.setAttribute("metaid", key);
/* 315 */                   rootelem.appendChild(childattelem);
/*     */                   
/*     */  HashMap<String, BusinessLogMSGVO> oldmsg_pkvomap;
/*     */         HashMap<String, BusinessLogMSGVO> newmsg_pkvomap;
/* 318 */                   oldmsg_pkvomap = new HashMap();
/* 319 */                   for (BusinessLogMSGVO oldsubvo : oldmsgvolist) {
/* 320 */                     if (oldsubvo.getPk_busiobj() != null) {
/* 321 */                       oldmsg_pkvomap.put(oldsubvo.getPk_busiobj(), oldsubvo);
/*     */                     }
/*     */                   }
/* 324 */                   newmsg_pkvomap = new HashMap();
/* 325 */                   for (BusinessLogMSGVO newsubvo : newmsgvolist) {
/* 326 */                     if (newsubvo.getPk_busiobj() != null) {
/* 327 */                       newmsg_pkvomap.put(newsubvo.getPk_busiobj(), newsubvo);
/*     */                     }
/*     */                   }
/*     */                   
/* 331 */                   for (String oldsubpk : oldmsg_pkvomap.keySet())
/*     */                   {
/* 333 */                     if (newmsg_pkvomap.keySet().contains(oldsubpk)) {
/* 334 */                       processModifyMSGVOxml((BusinessLogMSGVO)oldmsg_pkvomap.get(oldsubpk), (BusinessLogMSGVO)newmsg_pkvomap.get(oldsubpk), childattelem, doc);
/*     */                     } else {
/* 336 */                       ((BusinessLogMSGVO)oldmsg_pkvomap.get(oldsubpk)).setStatus(3);
/* 337 */                       processxml((BusinessLogMSGVO)oldmsg_pkvomap.get(oldsubpk), childattelem, doc);
/*     */                     }
/*     */                   }
/*     */                   
/* 341 */                   for (String newsubpk : newmsg_pkvomap.keySet())
/*     */                   {
/* 343 */                     if (!oldmsg_pkvomap.keySet().contains(newsubpk)) {
/* 344 */                       ((BusinessLogMSGVO)newmsg_pkvomap.get(newsubpk)).setStatus(2);
/* 345 */                       processxml((BusinessLogMSGVO)newmsg_pkvomap.get(newsubpk), childattelem, doc);
/*     */                     } }
/*     */                 }
/*     */               }
/*     */             } } } } catch (Exception e) { 
/*     */        
/* 352 */         Logger.error(e);
/*     */       }
/* 354 */       return Doc2StringConvert(doc);
/*     */     } catch (Exception e) {
/* 356 */       Logger.error(e);
/*     */     }
/* 358 */     return null;
/*     */   }
/*     */   
/*     */   private static void processMsgVO(BusinessLogMSGVO msgvo, int status) {
/*     */     try {
/* 363 */       for (List<BusinessLogMSGVO> list : msgvo.getSublogobj().values()) {
/* 364 */         if (list != null) {
/* 365 */           for (BusinessLogMSGVO vo : list) {
/* 366 */             vo.setStatus(status);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     catch (Exception e) {
/* 372 */       Logger.error(e);
/*     */     }
/*     */   }
/*     */   
/* 376 */   private static void processModifyMSGVOxml(BusinessLogMSGVO oldmsgvo, BusinessLogMSGVO newmsgvo, Element childelem, Document doc) { HashSet<String> oldandnewkeyset = new HashSet();
/* 377 */     if (oldmsgvo.getMap() != null) {
/* 378 */       oldandnewkeyset.addAll(oldmsgvo.getMap().keySet());
/*     */     }
/* 380 */     if (newmsgvo.getMap() != null) {
/* 381 */       oldandnewkeyset.addAll(newmsgvo.getMap().keySet());
/*     */     }
/* 383 */     Element entityelem = doc.createElement("entity");
/* 384 */     entityelem.setAttribute("id", newmsgvo.getPk_busiobj());
/* 385 */     entityelem.setAttribute("status", String.valueOf(newmsgvo.getStatus()));
/*     */     
/* 387 */     boolean ischangeflag = false;
/*     */     
/* 389 */     for (String key : oldandnewkeyset)
/*     */     {
/* 391 */       if ((!ischangeflag) && (((newmsgvo.getMap().get(key) == null) && (oldmsgvo.getMap().get(key) == null)) || ((newmsgvo.getMap().get(key) != null) && (((String)newmsgvo.getMap().get(key)).equals(oldmsgvo.getMap().get(key))))))
/*     */       {
/* 393 */         ischangeflag = false;
/*     */       } else {
/* 395 */         ischangeflag = true;
/*     */       }
/*     */       
/* 398 */       Element attelem = doc.createElement("attribute");
/* 399 */       attelem.setAttribute("displayname", key);
/* 400 */       entityelem.appendChild(attelem);
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 405 */       Logger.debug("----zhoutest --加入子实体key==" + key);
/*     */       
/* 407 */       if (newmsgvo.getMap().get(key) != null) {
/* 408 */         attelem.setTextContent((String)newmsgvo.getMap().get(key));
/* 409 */         Logger.debug("----zhoutest --加入子实体value==" + (String)newmsgvo.getMap().get(key));
/*     */       }
/* 411 */       if ((oldmsgvo.getMap().get(key) != null) && (!((String)oldmsgvo.getMap().get(key)).equals(newmsgvo.getMap().get(key)))) {
/* 412 */         Element from_elem = doc.createElement("before");
/* 413 */         from_elem.setTextContent((String)oldmsgvo.getMap().get(key));
/* 414 */         Logger.debug("----zhoutest --加入子实体修改前==" + (String)oldmsgvo.getMap().get(key));
/* 415 */         attelem.appendChild(from_elem);
/*     */       }
/*     */     }
/*     */     
/* 419 */     if (ischangeflag) {
/* 420 */       Logger.debug("----zhoutest --子实体 修改标志＝＝＝＝" + ischangeflag);
/* 421 */       entityelem.setAttribute("status", "1");
/* 422 */       childelem.appendChild(entityelem);
/*     */     }
/*     */   }
/*     */   
/* 426 */   public static boolean CheckMsgVOIsNull(BusinessLogMSGVO msgvo) { if ((msgvo == null) || (CheckStrIsNull(msgvo.getMetaid())) || (CheckStrIsNull(msgvo.getPk_busiobj()))) {
/* 427 */       return true;
/*     */     }
/* 429 */     return false;
/*     */   }
/*     */   
/*     */   public static boolean CheckStrIsNull(String s) {
/* 433 */     if ((s == null) || (s.trim().isEmpty())) {
/* 434 */       return true;
/*     */     }
/*     */     
/* 437 */     return false;
/*     */   }
/*     */ }

/* Location:           D:\zhw\home0816\modules\baseapp\lib\pubbaseapp_applogsLevel-1.jar
 * Qualified Name:     nc.bs.busilog.vo.BusilogInfo2XMLConverter
 * Java Class Version: 7 (51.0)
 * JD-Core Version:    0.7.1
 */