/** <a href="http://www.cpupk.com/decompiler">Eclipse Class Decompiler</a> plugin, Copyright (c) 2017 Chen Chao. **/
/*      */ package nc.ui.bd.ref;
/*      */ 
/*      */ import java.lang.reflect.Constructor;
/*      */ import java.lang.reflect.Method;
/*      */ import java.math.BigDecimal;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collection;
/*      */ import java.util.Comparator;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.LinkedList;
/*      */ import java.util.List;
/*      */ import java.util.ListIterator;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ import java.util.Vector;
/*      */ import nc.bs.framework.common.InvocationInfoProxy;
/*      */ import nc.bs.framework.common.NCLocator;
/*      */ import nc.bs.logging.Logger;
/*      */ import nc.itf.uap.IUAPQueryBS;
/*      */ import nc.itf.uap.busibean.IRefForTempTable;
/*      */ import nc.itf.uap.rbac.IUserManageQuery;
/*      */ import nc.ui.pub.para.SysInitBO_Client;
/*      */ import nc.vo.bd.ref.RefInfoVO;
/*      */ import nc.vo.bd.ref.RefcolumnVO;
/*      */ import nc.vo.bd.ref.ReftableVO;
/*      */ import nc.vo.bd.refdatatemp.RefdatatempVO;
/*      */ import nc.vo.cache.ext.TableVersionMonitor;
/*      */ import nc.vo.logging.Debug;
/*      */ import nc.vo.ml.AbstractNCLangRes;
/*      */ import nc.vo.ml.NCLangRes4VoTransl;
/*      */ import nc.vo.pub.BusinessException;
/*      */ import nc.vo.pub.lang.UFBoolean;
/*      */ import nc.vo.pub.lang.UFDouble;
/*      */ import nc.vo.pub.pinyin.CnToSpell;
/*      */ import nc.vo.sm.UserVO;
/*      */ import org.apache.commons.collections.CollectionUtils;
/*      */ 
/*      */ public class RefPubUtil
/*      */ {
/*   43 */   private static Set<String> set_nonSqlMatchRef = null;
/*      */ 
/*   45 */   private static String[] nonSqlMatchRefNodeName = { "公司目录(集团)", "公司目录(集团)S", "权限公司目录(集团)" };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*   50 */   private static String REFNODENAMEVSREFINFOVO = "refNodeNameVsRefinfoVO";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*   56 */   private static String DATASOURCEVSREFINIFOVOS = "datasourceVsRefinfoVOs";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*   62 */   private static String BLACKLIST = "blacklist";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*   68 */   private static Map hm_dataSource = new HashMap();
/*      */ 
/*   70 */   private static RefInfoVO[] infoVOs = null;
/*      */ 
/*      */ 
/*      */ 
/*   74 */   private static TableVersionMonitor tableVersionMonitor = new TableVersionMonitor(new String[] { "bd_refinfo" }, 300000L);
/*      */ 
/*      */ 
/*   77 */   private static Set<String> set_specialRef = null;
/*      */ 
/*   79 */   private static Set<String> set_imRef = null;
/*      */ 
/*   81 */   private static Set<String> set_imUserRef = null;
/*      */   private static TableVersionMonitor getTableVersionMonitor()
/*      */   {
/*   84 */     return tableVersionMonitor;
/*      */   }
/*      */ 
/*   87 */   private static String[] specialRef = { "文本框", "日历", "日期", "计算器", "颜色", "文件", "参照定制", "日期时间" };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static synchronized boolean isSpecialRef(String refNodeName)
/*      */   {
/*   95 */     if (set_specialRef == null) {
/*   96 */       set_specialRef = new HashSet();
/*      */ 
/*   98 */       set_specialRef.addAll(Arrays.asList(specialRef));
/*      */     }
/*      */ 
/*  101 */     return set_specialRef.contains(refNodeName);
/*      */   }
/*      */ 
/*  104 */   private static String[] psnRef = { "人员", "用户", "用户（所有）" };
/*      */ 
/*  106 */   private static String[] userRef = { "用户", "用户（所有）" };
/*      */   public static synchronized boolean isIMRef(String refNodeName)
/*      */   {
/*  109 */     if (set_imRef == null) {
/*  110 */       set_imRef = new HashSet();
/*      */ 
/*  112 */       set_imRef.addAll(Arrays.asList(psnRef));
/*      */     }
/*      */ 
/*  115 */     return set_imRef.contains(refNodeName); }
/*      */ 
/*      */   public static synchronized boolean isIMUserRef(String refNodeName) {
/*  118 */     if (set_imUserRef == null) {
/*  119 */       set_imUserRef = new HashSet();
/*      */ 
/*  121 */       set_imUserRef.addAll(Arrays.asList(userRef));
/*      */     }
/*      */ 
/*  124 */     return set_imUserRef.contains(refNodeName);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String toLowerCaseStr(AbstractRefModel refModel, String str)
/*      */   {
/*  131 */     if (str == null) {
/*  132 */       return str;
/*      */     }
/*  134 */     return ((!(refModel.isCaseSensitve())) ? str.toLowerCase() : str);
/*      */   }
/*      */ 
/*      */   public static String toLowerDBFunctionWrapper(AbstractRefModel refModel, String fieldName)
/*      */   {
/*  139 */     if (fieldName == null) {
/*  140 */       return fieldName;
/*      */     }
/*  142 */     return ((!(refModel.isCaseSensitve())) ? "lower(" + fieldName + ")" : fieldName);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String getCodeRuleFromPara(String paraCode, String pk_corp)
/*      */   {
/*  161 */     String codingRule = "";
/*      */     try {
/*  163 */       codingRule = SysInitBO_Client.getParaString(pk_corp, paraCode);
/*      */     }
/*      */     catch (Exception e) {
/*  166 */       Debug.error(e.getMessage(), e);
/*      */ 
/*      */ 
/*      */     }
/*      */ 
/*  171 */     return codingRule;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String getNumberCodingRule(String str)
/*      */   {
/*  179 */     StringBuffer sb = new StringBuffer();
/*  180 */     if (str != null) {
/*  181 */       for (int i = 0; i < str.length(); ++i)
/*      */       {
/*  183 */         if ((str.charAt(i) > '0') && (str.charAt(i) <= '9'))
/*  184 */           sb.append(str.charAt(i));
/*      */       }
/*      */     }
/*  187 */     return sb.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   public static int[] getCodingRule(String str)
/*      */   {
/*  194 */     int[] codingRule = null;
/*  195 */     if (str == null) {
/*  196 */       return null;
/*      */     }
/*  198 */     if (str.indexOf("/") >= 0)
/*      */     {
/*  200 */       while (str.startsWith("/")) {
/*  201 */         str = str.substring(1);
/*      */       }
/*      */ 
/*  204 */       String[] strRules = str.split("/");
/*  205 */       codingRule = new int[strRules.length];
/*  206 */       for (int i = 0; i < strRules.length; ++i) {
/*  207 */         codingRule[i] = Integer.parseInt(strRules[i]);
/*      */       }
/*      */     }
/*      */     else
/*      */     {
/*  212 */       codingRule = new int[str.length()];
/*  213 */       for (int i = 0; i < str.length(); ++i)
/*      */       {
/*  215 */         if ((str.charAt(i) > '0') && (str.charAt(i) <= '9')) {
/*  216 */           codingRule[i] = Integer.parseInt(String.valueOf(str.charAt(i)));
/*      */         }
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  222 */     return codingRule;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String getCodeRuleFromPara(String orgTypeCode, String pk_org, String paraCode, UFBoolean isPkValue)
/*      */   {
/*  241 */     String str = "";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  251 */     String codingRule = getNumberCodingRule(str);
/*      */ 
/*  253 */     return codingRule;
/*      */   }
/*      */ 
/*      */   public static int[] getHiddenFieldIndexs(AbstractRefModel refModel) {
/*  257 */     int[] hiddenFieldsIndex = new int[refModel.getHiddenFieldCode().length];
/*  258 */     for (int i = 0; i < hiddenFieldsIndex.length; ++i) {
/*  259 */       hiddenFieldsIndex[i] = refModel.getFieldIndex(refModel.getHiddenFieldCode()[i]);
/*      */     }
/*      */ 
/*  262 */     return hiddenFieldsIndex;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static Vector mapData(Vector v, AbstractRefModel model)
/*      */   {
/*  273 */     Vector retVec = v;
/*  274 */     ReftableVO vo = model.getRefTableVO(model.getPk_corp());
/*  275 */     if (vo == null) {
/*  276 */       retVec = mapDataByShowColumn(v, model);
/*      */     }
/*      */     else {
/*  279 */       retVec = mapDataByColumnVOs(v, model);
/*      */     }
/*      */ 
/*  282 */     return retVec;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static Vector mapDataByShowColumn(Vector v, AbstractRefModel model)
/*      */   {
/*  354 */     int fieldCount = getAllColumnNames(model).size();
/*      */ 
/*  356 */     if ((v == null) || (v.size() == 0)) {
/*  357 */       return v;
/*      */     }
/*      */ 
/*  360 */     Vector aRecord = (Vector)v.get(0);
/*  361 */     int length = model.getShownColumns().length + model.getHiddenFieldCode().length;
/*      */ 
/*      */ 
/*  364 */     if (aRecord.size() > length) {
/*  365 */       return v;
/*      */     }
/*      */ 
/*  368 */     Vector data = new Vector();
/*  369 */     int[] hiddenFieldIndex = getHiddenFieldIndexs(model);
/*  370 */     int hiddenIndex = 0;
/*  371 */     long begin = System.currentTimeMillis();
/*  372 */     for (int i = 0; i < v.size(); ++i) {
/*  373 */       Vector oldRecord = (Vector)v.get(i);
/*  374 */       Vector record = new Vector(fieldCount);
/*  375 */       record.setSize(fieldCount);
/*  376 */       hiddenIndex = 0;
/*  377 */       for (int j = 0; j < oldRecord.size(); ++j) {
/*  378 */         if (j < model.getShownColumns().length) {
/*  379 */           record.set(model.getShownColumns()[j], oldRecord.get(j));
/*      */         } else {
/*  381 */           record.set(hiddenFieldIndex[hiddenIndex], oldRecord.get(j));
/*  382 */           ++hiddenIndex;
/*      */         }
/*      */       }
/*  385 */       data.add(record);
/*      */     }
/*      */ 
/*  388 */     v = data;
/*  389 */     long end = System.currentTimeMillis();
/*  390 */     Logger.debug("convertdataTime is " + (end - begin));
/*  391 */     return v;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static Vector mapDataByColumnVOs(Vector v, AbstractRefModel model)
/*      */   {
/*  402 */     if ((v == null) || (v.size() == 0)) {
/*  403 */       return v;
/*      */     }
/*      */ 
/*  406 */     ReftableVO vo = model.getRefTableVO(model.getPk_corp());
/*  407 */     RefcolumnVO[] auxColumnVOs = (RefcolumnVO[])vo.getColumnVOs().clone();
/*  408 */     sortRefcolumnVOs(auxColumnVOs);
/*  409 */     int fieldCount = auxColumnVOs.length;
/*      */ 
/*  411 */     Vector data = new Vector();
/*      */ 
/*  413 */     long begin = System.currentTimeMillis();
/*  414 */     for (int i = 0; i < v.size(); ++i) {
/*  415 */       Vector oldRecord = (Vector)v.get(i);
/*  416 */       Vector record = new Vector(fieldCount);
/*  417 */       record.setSize(fieldCount);
/*  418 */       for (int j = 0; j < fieldCount; ++j)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       {
/*  424 */         record.setElementAt(oldRecord.get(auxColumnVOs[j].getColumnshowindex().intValue()), auxColumnVOs[j].getColumnshowindex().intValue());
/*      */ 
/*      */ 
/*      */       }
/*      */ 
/*  429 */       data.add(record);
/*      */     }
/*      */ 
/*  432 */     v = data;
/*  433 */     long end = System.currentTimeMillis();
/*  434 */     Logger.debug("convertdataTime is " + (end - begin));
/*  435 */     return v;
/*      */   }
/*      */ 
/*      */   private static void sortRefcolumnVOs(RefcolumnVO[] vos)
/*      */   {
/*  440 */     Arrays.sort(vos, new Comparator()
/*      */     {
/*      */       public int compare(Object o1, Object o2)
/*      */       {
/*  444 */         RefcolumnVO vo1 = (RefcolumnVO)o1;
/*  445 */         RefcolumnVO vo2 = (RefcolumnVO)o2;
/*      */ 
/*  447 */         return vo1.getColumnshowindex().compareTo(vo2.getColumnshowindex());
/*      */       }
/*      */     });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   public static Vector getAllColumnNames(AbstractRefModel refModel)
/*      */   {
/*  456 */     if (refModel == null) {
/*  457 */       return null;
/*      */     }
/*  459 */     return refModel.getAllColumnNames();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String getFieldShowName(AbstractRefModel model, String fieldCode)
/*      */   {
/*  479 */     int index = model.getFieldIndex(fieldCode);
/*  480 */     if ((index < 0) || (index >= getAllColumnNames(model).size())) {
/*  481 */       return null;
/*      */     }
/*  483 */     return ((String)getAllColumnNames(model).elementAt(index));
/*      */   }
/*      */ 
/*      */   public static int getRefType(AbstractRefModel model) {
/*  487 */     int refType = 0;
/*  488 */     if (model instanceof AbstractRefGridTreeModel) {
/*  489 */       refType = 2;
/*      */     }
/*  491 */     else if (model instanceof AbstractRefTreeModel) {
/*  492 */       refType = 1;
/*      */ 
/*      */ 
/*      */ 
/*      */     }
/*      */ 
/*  498 */     return refType;
/*      */   }
/*      */ 
/*      */   public static synchronized boolean isNonSqlMatchRef(String refNodeName) {
/*  502 */     if (set_nonSqlMatchRef == null) {
/*  503 */       set_nonSqlMatchRef = new HashSet();
/*      */ 
/*  505 */       set_nonSqlMatchRef.addAll(Arrays.asList(nonSqlMatchRefNodeName));
/*      */     }
/*      */ 
/*  508 */     return set_nonSqlMatchRef.contains(refNodeName);
/*      */   }
/*      */ 
/*      */   public static boolean isIncludeBlurChar(String value) {
/*  512 */     boolean include = false;
/*  513 */     if ((value != null) && (((value.indexOf(95) >= 0) || (value.indexOf(37) >= 0))))
/*      */     {
/*  515 */       include = true;
/*      */     }
/*  517 */     return include;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static AbstractRefModel getModel(String refClassName, String refNodeName, RefInfoVO refInfoVO)
/*      */   {
/*  535 */     if ((refClassName == null) || ((refNodeName != null) && (refNodeName.trim().length() == 0)))
/*      */     {
/*  537 */       return null;
/*      */     }
/*  539 */     AbstractRefModel model = null;
/*  540 */     Class modelClass = null;
/*      */ 
/*      */     try
/*      */     {
/*  544 */       modelClass = Class.forName(refClassName);
/*      */ 
/*  546 */       model = (AbstractRefModel)modelClass.newInstance();
/*      */     }
/*      */     catch (ClassNotFoundException ce) {
/*  549 */       Logger.debug("RefPubUtil构造参照Model--ClassNotFoundException,参照refnodename is  " + refNodeName);
/*      */ 
/*      */     }
/*      */     catch (Exception e)
/*      */     {
/*  554 */       Logger.info("RefPubUtil无参构造参照Model--NoSuchMethodException转入带参构造,参照Model is " + refClassName);
/*      */ 
/*      */ 
/*      */       try
/*      */       {
/*  559 */         if ((refNodeName != null) && (refNodeName.trim().length() > 0)) {
/*  560 */           Constructor cs = null;
/*  561 */           cs = modelClass.getConstructor(new Class[] { String.class });
/*      */ 
/*  563 */           model = (AbstractRefModel)cs.newInstance(new Object[] { refNodeName });
/*      */         }
/*      */         else {
/*  566 */           model = (AbstractRefModel)modelClass.newInstance();
/*      */         }
/*      */       } catch (Exception pe) {
/*  569 */         Logger.warn("RefPubUtil带参构造参照Model--Exception", pe);
/*      */       }
/*      */ 
/*  572 */       Logger.debug("RefPubUtil构造参照Model--Exception,参照refnodename is  " + refNodeName);
/*      */ 
/*      */     }
/*      */ 
/*  576 */     if ((refInfoVO != null) && (model != null)) {
/*  577 */       model.setPara1(refInfoVO.getPara1());
/*  578 */       model.setPara2(refInfoVO.getPara2());
/*  579 */       model.setPara3(refInfoVO.getPara3());
/*  580 */       model.setRefNodeName(refNodeName);
/*  581 */       model.reset();
/*  582 */       if (refInfoVO.getWherePart() != null) {
/*  583 */         model.setWherePart(refInfoVO.getWherePart());
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  588 */     return model;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   public static AbstractRefModel getRefModeByClassName(String refClassName)
/*      */   {
/*  595 */     if (refClassName == null) {
/*  596 */       return null;
/*      */     }
/*  598 */     return getModel(refClassName, null, null);
/*      */   }
/*      */ 
/*      */ 
/*      */   public static String[] getDefFields(String[] fields)
/*      */   {
/*  604 */     String[] defFields = null;
/*  605 */     if (fields != null)
/*      */     {
/*  607 */       List list = new ArrayList();
/*  608 */       String fieldName = null;
/*  609 */       for (int i = 0; i < fields.length; ++i) {
/*  610 */         fieldName = fields[i];
/*  611 */         if (fieldName.indexOf(".def") > 0) {
/*  612 */           list.add(fieldName);
/*      */         }
/*      */       }
/*  615 */       if (list.size() > 0) {
/*  616 */         defFields = new String[list.size()];
/*  617 */         list.toArray(defFields);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  622 */     return defFields;
/*      */   }
/*      */ 
/*      */   public static String[] getShowFields(AbstractRefModel refModel) {
/*  626 */     String[] showFields = new String[refModel.getShownColumns().length];
/*  627 */     for (int i = 0; i < showFields.length; ++i) {
/*  628 */       showFields[i] = refModel.getFieldCode()[refModel.getShownColumns()[i]];
/*      */     }
/*  630 */     return showFields;
/*      */   }
/*      */ 
/*      */   public static String[] ArrayCombine(String[] array1, String[] array2)
/*      */   {
/*  635 */     List strList = new ArrayList();
/*  636 */     if (array1 != null) {
/*  637 */       strList.addAll(Arrays.asList(array1));
/*      */     }
/*  639 */     if (array2 != null) {
/*  640 */       strList.addAll(Arrays.asList(array2));
/*      */     }
/*  642 */     if (strList.size() == 0) {
/*  643 */       return null;
/*      */     }
/*  645 */     return ((String[])strList.toArray(new String[0]));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   public static RefcolumnVO[] getColumnSequences(AbstractRefModel model)
/*      */   {
/*  652 */     if (model == null) {
/*  653 */       return null;
/*      */     }
/*  655 */     RefcolumnVO[] vos = null;
/*  656 */     ReftableVO vo = model.getRefTableVO(model.getPk_corp());
/*      */ 
/*      */ 
/*  659 */     if (vo != null)
/*      */     {
/*  661 */       vos = vo.getColumnVOs();
/*  662 */       if ((vos == null) || (vos.length <= 0)) {
/*  663 */         return vos;
/*      */       }
/*  665 */       RefcolumnVO columnvo = null;
/*  666 */       int length = vos.length;
/*      */ 
/*  668 */       for (int i = 0; i < length - 1; ++i)
/*      */       {
/*  670 */         for (int j = i; j < length; ++j) {
/*  671 */           int i_index = vos[i].getLocateshowindex().intValue();
/*  672 */           int j_index = vos[j].getLocateshowindex().intValue();
/*  673 */           if (i_index <= j_index)
/*      */             continue;
/*  675 */           columnvo = vos[i];
/*  676 */           vos[i] = vos[j];
/*  677 */           vos[j] = columnvo;
/*      */ 
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  683 */       for (int i = 0; i < vos.length; ++i)
/*      */       {
/*  685 */         vos[i].setResid(model.getFieldShowName(vos[i].getFieldname()));
/*      */       }
/*      */     }
/*      */     else
/*      */     {
/*  690 */       vo = getNewReftableVO(null, model);
/*  691 */       vos = vo.getColumnVOs();
/*      */ 
/*      */     }
/*      */ 
/*  695 */     return vos;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static ReftableVO getNewReftableVO(ReftableVO groupVO, AbstractRefModel refModel)
/*      */   {
/*  713 */     ReftableVO reftableVO = null;
/*  714 */     if (refModel == null) {
/*  715 */       return null;
/*      */ 
/*      */ 
/*      */ 
/*      */     }
/*      */ 
/*  721 */     reftableVO = new ReftableVO();
/*  722 */     reftableVO.setPk_corp(refModel.getPk_corp());
/*  723 */     reftableVO.setDefaultfieldcount(Integer.valueOf(refModel.getDefaultFieldCount()));
/*      */ 
/*  725 */     reftableVO.setRefnodename(refModel.getRefNodeName());
/*  726 */     reftableVO.setReftablename(refModel.getTableName());
/*  727 */     reftableVO.setWherepart(refModel.getWherePart());
/*      */ 
/*      */ 
/*  730 */     int lencode = refModel.getFieldCode().length;
/*  731 */     int hiddenLenth = (refModel.getHiddenFieldCode() == null) ? 0 : refModel.getHiddenFieldCode().length;
/*      */ 
/*  733 */     int length = lencode + hiddenLenth;
/*  734 */     boolean isColumnShow = false;
/*  735 */     RefcolumnVO[] vos = new RefcolumnVO[length];
/*  736 */     for (int i = 0; i < length; ++i) {
/*  737 */       vos[i] = new RefcolumnVO();
/*  738 */       vos[i].setTablename(refModel.getTableName());
/*  739 */       vos[i].setColumnshowindex(Integer.valueOf(i));
/*  740 */       vos[i].setLocateshowindex(Integer.valueOf(i));
/*  741 */       vos[i].setIspkfield(UFBoolean.valueOf(false));
/*  742 */       vos[i].setIshiddenfield(UFBoolean.valueOf(false));
/*      */ 
/*  744 */       setFieldType(groupVO, refModel, vos, i);
/*      */ 
/*  746 */       if (i >= lencode)
/*      */       {
/*  748 */         isColumnShow = false;
/*      */ 
/*  750 */         vos[i].setFieldname(refModel.getHiddenFieldCode()[(i - lencode)]);
/*  751 */         vos[i].setIshiddenfield(UFBoolean.valueOf(true));
/*      */ 
/*  753 */         if (refModel.getPkFieldCode().equals(refModel.getHiddenFieldCode()[(i - lencode)]))
/*      */         {
/*  755 */           vos[i].setIspkfield(UFBoolean.valueOf(true));
/*      */         }
/*      */       }
/*      */       else {
/*  759 */         if (i < refModel.getDefaultFieldCount())
/*  760 */           isColumnShow = true;
/*      */         else {
/*  762 */           isColumnShow = false;
/*      */         }
/*  764 */         vos[i].setFieldname(refModel.getFieldCode()[i]);
/*      */ 
/*  766 */         if (i < refModel.getFieldName().length) {
/*  767 */           vos[i].setFieldshowname(refModel.getFieldName()[i]);
/*      */         }
/*      */         else {
/*  770 */           vos[i].setFieldshowname(refModel.getFieldCode()[i]);
/*      */         }
/*      */ 
/*  773 */         vos[i].setResid(refModel.getFieldShowName(refModel.getFieldCode()[i]));
/*      */ 
/*      */       }
/*      */ 
/*  777 */       vos[i].setIscolumnshow(UFBoolean.valueOf(isColumnShow));
/*  778 */       vos[i].setIslocateshow(UFBoolean.valueOf(isColumnShow));
/*      */     }
/*      */ 
/*  781 */     reftableVO.setColumnVOs(vos);
/*      */ 
/*  783 */     return reftableVO;
/*      */   }
/*      */ 
/*      */   private static void setFieldType(ReftableVO groupVO, AbstractRefModel refModel, RefcolumnVO[] vos, int i)
/*      */   {
/*  788 */     if ((groupVO != null) && (groupVO.getColumnVOs() != null) && (i < groupVO.getColumnVOs().length))
/*      */ 
/*      */     {
/*  791 */       vos[i].setDatatype(groupVO.getColumnVOs()[i].getDatatype());
/*      */ 
/*      */     }
/*  794 */     else if (refModel.getIntFieldType() == null)
/*  795 */       vos[i].setDatatype(Integer.valueOf(0));
/*      */     else
/*  797 */       vos[i].setDatatype(Integer.valueOf(refModel.getIntFieldType()[i]));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String getCompositeSql(String[] fieldNames, String value, String operator, String preValue, String sufValue, String logicOP)
/*      */   {
/*  809 */     String sql = null;
/*      */ 
/*  811 */     if ((fieldNames == null) || (value == null)) {
/*  812 */       return null;
/*      */     }
/*  814 */     if (preValue == null) {
/*  815 */       preValue = "";
/*      */     }
/*  817 */     if (sufValue == null) {
/*  818 */       sufValue = "";
/*      */     }
/*  820 */     StringBuffer sb = new StringBuffer();
/*  821 */     for (int i = 0; i < fieldNames.length; ++i) {
/*  822 */       if (fieldNames[i] == null)
/*      */         continue;
/*  824 */       sb.append(" ").append(fieldNames[i]).append(operator).append(" '").append(preValue).append(value).append(sufValue).append("' ").append(logicOP);
/*      */ 
/*      */ 
/*      */     }
/*      */ 
/*  829 */     sql = sb.toString();
/*  830 */     if (sql != null) {
/*  831 */       sql = " ( " + sql.substring(0, sql.lastIndexOf(logicOP)) + " ) ";
/*      */     }
/*      */ 
/*  834 */     return sql;
/*      */   }
/*      */ 
/*      */   public static String getInSubSql(String[] values)
/*      */   {
/*  839 */     String subSql = "";
/*  840 */     StringBuffer sb = new StringBuffer();
/*  841 */     for (int i = 0; i < values.length; ++i) {
/*  842 */       sb.append("'").append(values[i]).append("',");
/*      */     }
/*  844 */     subSql = sb.toString();
/*  845 */     subSql = " (" + subSql.substring(0, subSql.length() - 1) + ") ";
/*  846 */     return subSql;
/*      */   }
/*      */ 
/*      */   public static String getInSubSql(String fieldName, String[] values)
/*      */   {
/*  851 */     StringBuffer sb = new StringBuffer();
/*      */ 
/*  853 */     if ((fieldName != null) && (values != null) && (values.length > 0)) {
/*  854 */       ListPageIterator pageIt = new ListPageIterator(Arrays.asList(values), 900.0D);
/*      */ 
/*      */ 
/*  857 */       List aPage = null;
/*  858 */       aPage = pageIt.nextPage();
/*  859 */       String[] inValues = (String[])(String[])aPage.toArray(new String[0]);
/*  860 */       sb.append(fieldName).append(" in ").append(getInSubSql(inValues));
/*      */ 
/*  862 */       while (pageIt.hasNext()) {
/*  863 */         aPage = pageIt.nextPage();
/*  864 */         inValues = (String[])(String[])aPage.toArray(new String[0]);
/*  865 */         sb.append(" OR ");
/*  866 */         sb.append(fieldName).append(" in ").append(getInSubSql(inValues));
/*      */       }
/*      */     }
/*      */ 
/*  870 */     return sb.toString();
/*      */   }
/*      */ 
/*      */   public static boolean equals(String s1, String s2) {
/*  874 */     boolean isEqual = false;
/*  875 */     if ((s1 != null) && (s1.trim().length() == 0))
/*  876 */       s1 = null;
/*  877 */     if ((s2 != null) && (s2.trim().length() == 0)) {
/*  878 */       s2 = null;
/*      */     }
/*  880 */     if ((s1 == null) && (s2 == null))
/*  881 */       isEqual = true;
/*  882 */     else if (((s1 == null) && (s2 != null)) || ((s1 != null) && (s2 == null))) {
/*  883 */       isEqual = false;
/*      */     }
/*      */     else {
/*  886 */       isEqual = s1.equals(s2);
/*      */     }
/*  888 */     return isEqual;
/*      */   }
/*      */ 
/*      */   public static boolean equals(Vector v1, Vector v2) {
/*  892 */     if ((v1 != null) && (v1.size() == 0))
/*  893 */       v1 = null;
/*  894 */     if ((v2 != null) && (v2.size() == 0))
/*  895 */       v2 = null;
/*  896 */     if ((v1 == null) && (v2 == null))
/*  897 */       return true;
/*  898 */     if (((v1 == null) && (v2 != null)) || ((v1 != null) && (v2 == null)))
/*  899 */       return false;
/*  900 */     if (v1.size() != v2.size())
/*  901 */       return false;
/*  902 */     boolean in = false;
/*  903 */     for (int i = 0; i < v1.size(); ++i) {
/*  904 */       Vector vTemp1 = (Vector)v1.elementAt(i);
/*  905 */       in = false;
/*  906 */       for (int j = 0; j < v2.size(); ++j) {
/*  907 */         Vector vTemp2 = (Vector)v2.elementAt(j);
/*  908 */         if ((((vTemp1 != null) || (vTemp2 != null))) && (((vTemp1 == null) || (!(vTemp1.equals(vTemp2))))))
/*      */           continue;
/*  910 */         in = true;
/*  911 */         break;
/*      */       }
/*      */ 
/*  914 */       if (!(in))
/*      */         break;
/*      */     }
/*  917 */     return in;
/*      */   }
/*      */ 
/*      */   public static String getFirstTableName(String tableName) {
/*  921 */     String firstTableName = tableName;
/*  922 */     if (tableName == null) {
/*  923 */       return null;
/*      */     }
/*  925 */     if (tableName.indexOf(" ") > 0) {
/*  926 */       firstTableName = tableName.trim().substring(0, tableName.indexOf(" "));
/*      */     }
/*  928 */     else if (tableName.indexOf(",") > 0) {
/*  929 */       firstTableName = tableName.trim().substring(0, tableName.indexOf(","));
/*      */     }
/*      */ 
/*  932 */     return firstTableName;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String getRefMultiLangStr(String resid, String fieldCode)
/*      */   {
/*  949 */     String value = null;
/*  950 */     if (resid == null)
/*      */ 
/*      */ 
/*      */     {
/*  954 */       value = NCLangRes4VoTransl.getNCLangRes().getStrByID("datadict", "D" + fieldCode);
/*      */     }
/*      */     else {
/*  957 */       value = NCLangRes4VoTransl.getNCLangRes().getStrByID("ref", resid);
/*      */     }
/*  959 */     if (value == null) {
/*  960 */       value = fieldCode;
/*      */     }
/*  962 */     return value;
/*      */   }
/*      */ 
/*      */   public static RefInfoVO[] getRefinfoVos()
/*      */   {
/*  967 */     return ((RefInfoVO[])getHM_DatasourceVSRefinfoVOs().get(getDefaultDateSource()));
/*      */   }
/*      */ 
/*      */   private static String getDefaultDateSource()
/*      */   {
/*  972 */     String ds = InvocationInfoProxy.getInstance().getUserDataSource();
/*  973 */     return ds;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String getRefNodeName_mLang(String refNodeName)
/*      */   {
/*  981 */     String refNodeName_mlang = refNodeName;
/*  982 */     String resID = null;
/*  983 */     String resPath = null;
/*      */ 
/*  985 */     if (refNodeName != null) {
/*  986 */       RefInfoVO vo = getRefinfoVO(refNodeName);
/*  987 */       if (vo != null) {
/*  988 */         resID = vo.getResid();
/*  989 */         resPath = (vo.getResidPath() == null) ? "ref" : vo.getResidPath();
/*      */       }
/*      */ 
/*  992 */       if (resID != null) {
/*  993 */         refNodeName_mlang = NCLangRes4VoTransl.getNCLangRes().getStrByID(resPath, resID);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1002 */     return refNodeName_mlang;
/*      */   }
/*      */ 
/*      */   public static RefInfoVO getRefinfoVO(String refNodeName) {
/* 1006 */     if ((refNodeName == null) || (refNodeName.trim().length() == 0)) {
/* 1007 */       return null;
/*      */     }
/* 1009 */     if (getBlackList().get(refNodeName) != null) {
/* 1010 */       return null;
/*      */     }
/* 1012 */     if (getTableVersionMonitor().isCacheOutOfDate()) {
/* 1013 */       clearRefInfoVOs();
/*      */     }
/*      */ 
/* 1016 */     RefInfoVO vo = (RefInfoVO)getHM_RefNodeNameVSRefInfoVO().get(refNodeName);
/* 1017 */     if (vo == null) {
/* 1018 */       Logger.debug("通过 refNodeName取RefInfoVO没取到RefinfoVOCache要重新初始化化,参照refnodename is  " + refNodeName + "  业务组要把该参照注册到bd_refinfo表，否则RefinfoVOCache每次打开IE都会重新初始化");
/*      */ 
/* 1020 */       RefInfoAccessor.getInstance().refeshCache();
/*      */ 
/*      */ 
/* 1023 */       getHM_RefNodeNameVSRefInfoVO().clear();
/* 1024 */       initRefNodeNameVsRefInfoVO(getHM_RefNodeNameVSRefInfoVO(), getAllRefInfoVOs());
/*      */ 
/*      */ 
/*      */ 
/* 1028 */       vo = (RefInfoVO)getHM_RefNodeNameVSRefInfoVO().get(refNodeName);
/*      */ 
/* 1030 */       if (vo == null) {
/* 1031 */         getBlackList().put(refNodeName, refNodeName);
/*      */       }
/*      */     }
/*      */ 
/* 1035 */     return vo;
/*      */   }
/*      */ 
/*      */   private static Map<String, String> getBlackList() {
/* 1039 */     String key = getKey(BLACKLIST);
/*      */ 
/* 1041 */     Map hm = null;
/* 1042 */     if (hm_dataSource.get(key) == null) {
/* 1043 */       hm = new HashMap();
/* 1044 */       hm_dataSource.put(key, hm);
/*      */     } else {
/* 1046 */       hm = (Map)hm_dataSource.get(key);
/*      */     }
/* 1048 */     return hm;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static void initRefNodeNameVsRefInfoVO(Map<String, RefInfoVO> map, RefInfoVO[] refInfoVOs)
/*      */   {
/* 1056 */     if (refInfoVOs == null) {
/* 1057 */       return;
/*      */     }
/*      */ 
/* 1060 */     for (int i = 0; i < refInfoVOs.length; ++i)
/* 1061 */       map.put(refInfoVOs[i].getName(), refInfoVOs[i]);
/*      */   }
/*      */ 
/*      */ 
/*      */   private static Map<String, RefInfoVO> getHM_RefNodeNameVSRefInfoVO()
/*      */   {
/* 1067 */     String key = getKey(REFNODENAMEVSREFINFOVO);
/*      */ 
/* 1069 */     Map hm = null;
/* 1070 */     if (hm_dataSource.get(key) == null) {
/* 1071 */       hm = new HashMap();
/* 1072 */       RefInfoVO[] vos = getAllRefInfoVOs();
/* 1073 */       initRefNodeNameVsRefInfoVO(hm, vos);
/* 1074 */       hm_dataSource.put(key, hm);
/*      */     } else {
/* 1076 */       hm = (Map)hm_dataSource.get(key);
/*      */     }
/* 1078 */     return hm;
/*      */   }
/*      */ 
/*      */   private static Map<String, RefInfoVO[]> getHM_DatasourceVSRefinfoVOs() {
/* 1082 */     String key = getKey(DATASOURCEVSREFINIFOVOS);
/* 1083 */     Map hm = null;
/* 1084 */     if (hm_dataSource.get(key) == null) {
/* 1085 */       hm = new HashMap();
/* 1086 */       RefInfoVO[] vos = getAllRefInfoVOs();
/* 1087 */       hm.put(getDefaultDateSource(), vos);
/* 1088 */       hm_dataSource.put(key, hm);
/*      */     } else {
/* 1090 */       hm = (Map)hm_dataSource.get(key);
/*      */     }
/* 1092 */     return hm;
/*      */   }
/*      */ 
/*      */   private static String getKey(String key) {
/* 1096 */     return key + getDefaultDateSource();
/*      */   }
/*      */ 
/*      */   public static void clearRefInfoVOs() {
/* 1100 */     hm_dataSource.clear();
/* 1101 */     infoVOs = null;
/*      */   }
/*      */ 
/*      */   public static AbstractRefModel getRefModel(String refNodeName) {
/* 1105 */     String refClassName = getRefModelClassName(refNodeName);
/*      */ 
/* 1107 */     return getModel(refClassName, refNodeName, getRefinfoVO(refNodeName));
/*      */   }
/*      */ 
/*      */   public static String getRefModelClassName(String refNodeName) {
/* 1111 */     if ((refNodeName == null) || (refNodeName.trim().length() == 0)) {
/* 1112 */       return null;
/*      */     }
/* 1114 */     RefInfoVO vo = getRefinfoVO(refNodeName);
/* 1115 */     String modelClassName = null;
/* 1116 */     if (vo != null) {
/* 1117 */       modelClassName = vo.getRefclass();
/*      */     }
/*      */ 
/* 1120 */     return modelClassName;
/*      */   }
/*      */ 
/*      */ 
/*      */   private static synchronized RefInfoVO[] getAllRefInfoVOs()
/*      */   {
/* 1126 */     RefInfoVO[] vos = null;
/*      */     try {
/* 1128 */       vos = RefInfoAccessor.getInstance().getRefinfoVOs();
/*      */     }
/*      */     catch (BusinessException e) {
/* 1131 */       Logger.error(e);
/*      */     }
/* 1133 */     if ((vos != null) && (vos.length > 0)) {
/* 1134 */       List result = filterRefInfoVO(Arrays.asList(vos));
/*      */ 
/* 1136 */       infoVOs = new RefInfoVO[result.size()];
/* 1137 */       result.toArray(infoVOs);
/*      */     } else {
/* 1139 */       return null;
/*      */     }
/* 1141 */     return infoVOs;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static Collection getRefInfoVOsFromDB()
/*      */   {
/*      */     Collection col;
/*      */     try
/*      */     {
/* 1189 */       col = getIUapQueryBS().retrieveByClause(RefInfoVO.class, " 1=1 order by name ");
/*      */     }
/*      */     catch (BusinessException e) {
/* 1192 */       Logger.error(e);
/* 1193 */       throw new IllegalStateException("查询RefInfoVO出错," + e.getMessage());
/*      */     }
/* 1195 */     return col;
/*      */   }
/*      */ 
/*      */   public static List<RefInfoVO> filterRefInfoVO(List<RefInfoVO> list) {
/* 1199 */     List result = new ArrayList();
/* 1200 */     Map<String, List<RefInfoVO>> key_refInfoVOList_map = getKey_RefInfoVOList_map(list);
/* 1201 */     for (String key : key_refInfoVOList_map.keySet()) {
/* 1202 */       List refInfoVOList = (List)key_refInfoVOList_map.get(key);
/* 1203 */       if (refInfoVOList != null) {
/* 1204 */         RefInfoVO refInfoVO = getPriorityRefInfoVO(refInfoVOList);
/* 1205 */         result.add(refInfoVO);
/*      */       }
/*      */     }
/* 1208 */     return result;
/*      */   }
/*      */ 
/*      */   private static RefInfoVO getPriorityRefInfoVO(List<RefInfoVO> refInfoVOList) {
/* 1212 */     if (CollectionUtils.isEmpty(refInfoVOList))
/* 1213 */       return null;
/* 1214 */     RefInfoVO priorRefInfoVO = (RefInfoVO)refInfoVOList.get(0);
/* 1215 */     int priorLayer = -1;
/* 1216 */     for (RefInfoVO refInfoVO : refInfoVOList) {
/* 1217 */       if (refInfoVO.getLayer() > priorLayer) {
/* 1218 */         priorLayer = refInfoVO.getLayer();
/* 1219 */         priorRefInfoVO = refInfoVO;
/*      */       }
/*      */     }
/* 1222 */     return priorRefInfoVO;
/*      */   }
/*      */ 
/*      */   private static Map<String, List<RefInfoVO>> getKey_RefInfoVOList_map(List<RefInfoVO> refInfoVOList)
/*      */   {
/* 1227 */     Map key_refInfoVOList_map = new HashMap();
/* 1228 */     for (RefInfoVO vo : refInfoVOList) {
/* 1229 */       List tempRefInfoVOList = (List)key_refInfoVOList_map.get(vo.getName());
/*      */ 
/* 1231 */       if (tempRefInfoVOList == null) {
/* 1232 */         tempRefInfoVOList = new ArrayList();
/* 1233 */         key_refInfoVOList_map.put(vo.getName(), tempRefInfoVOList);
/*      */       }
/* 1235 */       tempRefInfoVOList.add(vo);
/*      */     }
/* 1237 */     return key_refInfoVOList_map;
/*      */   }
/*      */ 
/*      */   private static IUAPQueryBS getIUapQueryBS() {
/* 1241 */     IUAPQueryBS iUAPQueryBS = (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
/*      */ 
/* 1243 */     return iUAPQueryBS;
/*      */   }
/*      */ 
/*      */   public static Object bigDecimal2UFDouble(Object obj) {
/* 1247 */     if (obj instanceof BigDecimal)
/*      */     {
/* 1249 */       return new UFDouble((BigDecimal)obj);
/*      */     }
/* 1251 */     return obj;
/*      */   }
/*      */ 
/*      */ 
/*      */   public static void appendOrderPart(String[] hiddenFields, String strOrderField, String[] showFields, StringBuffer buffer)
/*      */   {
/* 1257 */     if (strOrderField == null)
/*      */       return;
/* 1259 */     boolean appendOrder = true;
/* 1260 */     String[] strs = strOrderField.split(",");
/* 1261 */     for (int i = 0; i < strs.length; ++i) {
/* 1262 */       if (isContainedInArray(strs[i].trim(), showFields)) continue; if (isContainedInArray(strs[i].trim(), hiddenFields)) {
/*      */         continue;
/*      */       }
/*      */ 
/* 1266 */       appendOrder = false;
/*      */     }
/* 1268 */     if (appendOrder)
/* 1269 */       buffer.append(" order by ").append(strOrderField);
/*      */   }
/*      */ 
/*      */ 
/*      */   private static boolean isContainedInArray(String str, String[] strs)
/*      */   {
/* 1275 */     if ((str == null) || (strs == null)) {
/* 1276 */       return false;
/*      */     }
/* 1278 */     for (int i = 0; i < strs.length; ++i) {
/* 1279 */       if (strs[i].indexOf(str) > -1) {
/* 1280 */         return true;
/*      */       }
/*      */     }
/* 1283 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   static class ListPageIterator
/*      */   {
/* 1289 */     protected List valueList = new ArrayList();
/*      */ 
/*      */ 
/*      */ 
/* 1293 */     protected double pageSize = 10.0D;
private ListIterator listIterator;
/*      */ 
/*      */     public ListPageIterator(List valueList, double pageSize) {
/* 1296 */       if (valueList == null) {
/* 1297 */         throw new IllegalArgumentException("ListPageIterator的初始化参数List valueList 不能为null!");
/*      */       }
/*      */ 
/* 1300 */       this.valueList = valueList;
/* 1301 */       this.listIterator = valueList.listIterator();
/* 1302 */       this.pageSize = pageSize;
/*      */     }
/*      */ 
/*      */     public int getPageCount() {
/* 1306 */       return (int)Math.ceil(this.valueList.size() / this.pageSize);
/*      */     }
/*      */ 
/*      */     public Collection getValueList() {
/* 1310 */       return this.valueList;
/*      */     }
/*      */ 
/*      */     public int getSize() {
/* 1314 */       return this.valueList.size();
/*      */     }
/*      */ 
/*      */     public List previousPage() {
/* 1318 */       int i = 0;
/* 1319 */       Object object = null;
/* 1320 */       LinkedList list = new LinkedList();
/* 1321 */       while ((this.listIterator.hasPrevious()) && (i < this.pageSize)) {
/* 1322 */         object = this.listIterator.previous();
/* 1323 */         list.add(object);
/* 1324 */         ++i;
/*      */       }
/* 1326 */       return list;
/*      */     }
/*      */ 
/*      */     public List nextPage() {
/* 1330 */       int i = 0;
/* 1331 */       Object object = null;
/* 1332 */       LinkedList list = new LinkedList();
/* 1333 */       while ((this.listIterator.hasNext()) && (i < this.pageSize)) {
/* 1334 */         object = this.listIterator.next();
/* 1335 */         list.add(object);
/* 1336 */         ++i;
/*      */       }
/* 1338 */       return list;
/*      */     }
/*      */ 
/*      */     public void resetIndex() {
/* 1342 */       this.listIterator = this.valueList.listIterator();
/*      */     }
/*      */ 
/*      */     public boolean hasNext() {
/* 1346 */       return this.listIterator.hasNext();
/*      */     }
/*      */ 
/*      */     public boolean hasPrevious() {
/* 1350 */       return this.listIterator.hasPrevious();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static Vector getFilterPKsVector(Vector vecData, AbstractRefModel model)
/*      */   {
/* 1361 */     Vector filterVector = null;
/* 1362 */     int pkIndex = model.getFieldIndex(model.getPkFieldCode());
/* 1363 */     int filterStrategy = model.getFilterStrategy();
/* 1364 */     Set set = new HashSet();
/* 1365 */     if (model.getFilterPks() == null) {
/* 1366 */       return vecData;
/*      */     }
/* 1368 */     set.addAll(Arrays.asList(model.getFilterPks()));
/*      */ 
/* 1370 */     if (vecData != null)
/*      */     {
/* 1372 */       if (model.getFilterPks() == null) {
/* 1373 */         return vecData;
/*      */       }
/* 1375 */       filterVector = new Vector();
/* 1376 */       for (int i = 0; i < vecData.size(); ++i) {
/* 1377 */         Object vecpk = ((Vector)vecData.elementAt(i)).elementAt(pkIndex);
/*      */ 
/*      */ 
/* 1380 */         switch (filterStrategy)
/*      */         {
/*      */         case 0:
/* 1383 */           if (set.size() == 0) {
/* 1384 */             return null;
/*      */           }
/* 1386 */           if (set.contains(vecpk.toString()))
/* 1387 */             filterVector.add(vecData.elementAt(i)); break;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         case 1:
/* 1393 */           if (set.size() == 0) {
/* 1394 */             return vecData;
/*      */           }
/* 1396 */           if (!(set.contains(vecpk.toString()))) {
/* 1397 */             filterVector.add(vecData.elementAt(i));
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1406 */     return filterVector;
/*      */   }
/*      */ 
/*      */   public static boolean isNull(String str)
/*      */   {
/* 1411 */     return ((str == null) || (str.trim().length() == 0));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void saveTempData(AbstractRefModel model, Object[] retPKs)
/*      */   {
/* 1418 */     if ((retPKs == null) || (retPKs.length == 0)) {
/* 1419 */       model.setTempDataWherePart(null);
/* 1420 */       return;
/*      */     }
/*      */ 
/* 1423 */     String pk_ts = null;
/*      */ 
/* 1425 */     RefdatatempVO[] vos = new RefdatatempVO[retPKs.length];
/* 1426 */     for (int i = 0; i < retPKs.length; ++i) {
/* 1427 */       vos[i] = new RefdatatempVO();
/* 1428 */       vos[i].setPk_selecteddata(retPKs[i].toString());
/*      */ 
/* 1430 */       vos[i].setPk_org(model.getPk_org());
/* 1431 */       vos[i].setCuserid(model.getPk_user());
/* 1432 */       vos[i].setRefnodename(model.getRefNodeName());
/* 1433 */       vos[i].setPk_ts("temp");
/*      */     }
/*      */     try
/*      */     {
/* 1437 */       pk_ts = saveRefData(vos);
/*      */     }
/*      */     catch (Exception e) {
/* 1440 */       Debug.error(e.getMessage(), e);
/*      */     }
/*      */ 
/* 1443 */     String selSql = "select pk_selecteddata from bd_refdatatemp ";
/*      */ 
/* 1445 */     String wherePart = selSql + " where pk_org='" + model.getPk_org() + "' and cuserid = '" + model.getPk_user() + "' " + " and refnodename='" + model.getRefNodeName() + "' and pk_ts = '" + pk_ts + "'";
/*      */ 
/*      */ 
/*      */ 
/* 1449 */     model.setTempDataWherePart(wherePart);
/*      */   }
/*      */ 
/*      */ 
/*      */   private static String saveRefData(RefdatatempVO[] vos)
/*      */     throws BusinessException
/*      */   {
/* 1456 */     IRefForTempTable iRefForTempTable = (IRefForTempTable)NCLocator.getInstance().lookup(IRefForTempTable.class.getName());
/*      */ 
/*      */ 
/* 1459 */     return iRefForTempTable.saveRefDataTempVOs(vos);
/*      */   }
/*      */ 
/*      */   public static boolean isGetDataOverridden(AbstractRefModel model)
/*      */   {
/* 1464 */     boolean isOverridden = false;
/*      */ 
/*      */     try
/*      */     {
/* 1468 */       Method getData = model.getClass().getDeclaredMethod("getData", new Class[0]);
/*      */ 
/* 1470 */       if (getData != null) {
/* 1471 */         isOverridden = true;
/*      */       }
/*      */     }
/*      */     catch (Exception e)
/*      */     {
/*      */     }
/*      */ 
/* 1478 */     return isOverridden;
/*      */   }
/*      */ 
/*      */   public static String getComposedString(String[] names) {
/* 1482 */     String showName = "";
/* 1483 */     StringBuffer sb = new StringBuffer();
/* 1484 */     if ((names != null) && (names.length > 0))
/*      */     {
/* 1486 */       for (int i = 0; i < names.length; ++i) {
/* 1487 */         if (names[i] == null) {
/*      */           continue;
/*      */         }
/* 1490 */         sb.append(names[i]).append(",");
/*      */ 
/*      */       }
/*      */ 
/* 1494 */       if (sb.length() > 0) {
/* 1495 */         showName = sb.toString().substring(0, sb.length() - 1);
/*      */       }
/*      */     }
/*      */ 
/* 1499 */     return showName;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static boolean isMatch(String searchString, String targetString)
/*      */   {
/* 1509 */     String[] strs = CnToSpell.getFirstSpells(targetString);
/* 1510 */     for (int i = 0; i < strs.length; ++i) {
/* 1511 */       if (strs[i].indexOf(searchString) > -1) {
/* 1512 */         return true;
/*      */       }
/*      */     }
/* 1515 */     return false;
/*      */   }
/*      */ 
/*      */   public static String getUserCode(String pk_psndoc) {
/* 1519 */     String userCode = null;
/* 1520 */     UserVO userVO = getUserVO(pk_psndoc);
/* 1521 */     if (userVO != null) {
/* 1522 */       userCode = userVO.getUser_code();
/*      */     }
/* 1524 */     return userCode;
/*      */   }
/*      */ 
/*      */   public static UserVO getUserVO(String pk_psndoc) {
/* 1528 */     UserVO userVO = null;
/* 1529 */     if (pk_psndoc == null)
/* 1530 */       return null;
/*      */     try
/*      */     {
/* 1533 */       userVO = getUserManageQuery().queryUserVOByPsnDocID(pk_psndoc);
/*      */     }
/*      */     catch (BusinessException e)
/*      */     {
/* 1537 */       e.printStackTrace();
/*      */     }
/* 1539 */     return userVO;
/*      */   }
/*      */ 
/*      */   private static IUserManageQuery getUserManageQuery() {
/* 1543 */     IUserManageQuery query = (IUserManageQuery)NCLocator.getInstance().lookup(IUserManageQuery.class.getName());
/* 1544 */     return query; }
/*      */ 
/*      */   public static String getDelCommonDataSql(String tableName, List<String> pks, String pk_group, String pk_user, String operationCode) {
/* 1547 */     String pk_docINSql = getInSubSql((String[])pks.toArray(new String[0]));
/*      */ 
/*      */ 
/* 1550 */     String sql = "delete from " + tableName + "_c where pk_doc in " + pk_docINSql + " and pk_group='" + pk_group + "' and pk_user='" + pk_user + "'";
/* 1551 */     return sql;
/*      */   }
/*      */ }
