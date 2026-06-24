package nc.ui.bd.material.config.action;
/*     */ 
/*     */ import java.io.Reader;
/*     */ import java.io.StringReader;
/*     */ import javax.xml.parsers.DocumentBuilder;
/*     */ import javax.xml.parsers.DocumentBuilderFactory;
/*     */ import javax.xml.xpath.XPath;
/*     */ import javax.xml.xpath.XPathConstants;
/*     */ import javax.xml.xpath.XPathExpressionException;
/*     */ import javax.xml.xpath.XPathFactory;
/*     */ import nc.bs.busilog.vo.BusinessLogVO;
/*     */ import nc.bs.logging.Logger;
/*     */ import nc.md.IMDQueryFacade;
/*     */ import nc.md.MDBaseQueryFacade;
/*     */ import nc.md.model.IBean;
/*     */ import nc.vo.ml.MainNCLangRes;
/*     */ import nc.vo.pub.BusinessException;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.Node;
/*     */ import org.w3c.dom.NodeList;
/*     */ import org.xml.sax.InputSource;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BusiLogDetailDomParser
/*     */ {
/*  48 */   private StringBuffer formattedDetail = new StringBuffer();
/*     */   private static final String ENTITY = "entity";
/*     */   private static final String TYPE = "type";
/*     */   private static final String DISPLAYNAME = "displayname";
/*     */   private static final String STATUS = "status";
/*     */   private static final String NAME = "name";
/*     */   private static final String ATTRIBUTE = "attribute";
/*     */   private static final String CHILD = "child";
/*  56 */   private BusinessLogVO busilogvo = null;
/*     */   
/*     */   private BusiLogDetailDomParser(BusinessLogVO vo) throws BusinessException
/*     */   {
/*  60 */     if ((vo == null) || (vo.getLogmsg() == null) || (vo.getLogmsg().isEmpty())) return;
/*  61 */     busilogvo = vo;
/*     */     
/*  63 */     Reader reader = new StringReader(vo.getLogmsg());
/*  64 */     InputSource is = new InputSource(reader);
/*  65 */     DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
/*     */     try {
/*  67 */       DocumentBuilder builder = factory.newDocumentBuilder();
/*  68 */       Document doc = builder.parse(is);
/*  69 */       forMat2(doc, 0);
/*     */     } catch (Exception e) {
/*  71 */       throw new BusinessException(e);
/*     */     }
/*     */   }
/*     */   
/*     */   public static BusiLogDetailDomParser getInstance(BusinessLogVO vo) throws BusinessException {
/*  76 */     return new BusiLogDetailDomParser(vo);
/*     */   }
/*     */   
/*     */   public StringBuffer getFormattedDetail() {
/*  80 */     return formattedDetail;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void forMat2(Node doc, int loop)
/*     */   {
/* 138 */     if (loop > 2) {
/* 139 */       return;
/*     */     }
/*     */     try
/*     */     {
/* 143 */       Element rootelem = null;
/* 144 */       NodeList nodelist = SelectNodes("entity", doc);
/* 145 */       if ((nodelist != null) && (nodelist.getLength() > 0)) {
/* 146 */         rootelem = (Element)nodelist.item(0);
/*     */       }
/* 148 */       if ((rootelem == null) || (nodelist.getLength() == 0)) {
/* 149 */         return;
/*     */       }
/* 151 */       boolean ischild = false;
/* 152 */       if (rootelem.getParentNode().getNodeType() == 9) {
/* 153 */         ischild = false;
/* 154 */         IBean bean = MDBaseQueryFacade.getInstance().getBeanByID(busilogvo.getTypepk_busiobj());
/* 155 */         String OBJname = bean.getDisplayName();
/* 156 */         formattedDetail.append(OBJname).append(":").append("\n").append("    ");
/*     */       } else {
/* 158 */         ischild = true;
/* 159 */         Element elemson = (Element)rootelem.getParentNode();
/* 160 */         String sonmetaid = elemson.getAttribute("metaid");
/* 161 */         IBean bean = MDBaseQueryFacade.getInstance().getBeanByID(sonmetaid);
/* 162 */         String OBJname = bean.getDisplayName();
/* 163 */         formattedDetail.append(OBJname).append(":").append("\n").append("    ");
/*     */       }
/*     */       
/* 166 */       for (int kk = 0; kk < nodelist.getLength(); kk++) {
/* 167 */         Node node1 = nodelist.item(kk);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 223 */         if (ischild) {
/* 224 */           Element entity_elem = (Element)node1;
/* 225 */           String Opername = "";
/* 226 */           if (checknull(entity_elem.getAttribute("status"))) {
/* 227 */             Opername = MainNCLangRes.getInstance().getStrByID("sfapp", "BusiLogDetailDomParser-000000");
/* 228 */           } else if (entity_elem.getAttribute("status").equals("1")) {
/* 229 */             Opername = MainNCLangRes.getInstance().getStrByID("sfapp", "SuperAdmUI-000018");
/* 230 */           } else if (entity_elem.getAttribute("status").equals("2")) {
/* 231 */             Opername = MainNCLangRes.getInstance().getStrByID("sfapp", "SuperAdmUI-000013");
/* 232 */           } else if (entity_elem.getAttribute("status").equals("3")) {
/* 233 */             Opername = MainNCLangRes.getInstance().getStrByID("sfapp", "SuperAdmUI-000016");
/*     */           } else {
/* 235 */             Opername = MainNCLangRes.getInstance().getStrByID("sfapp", "BusiLogDetailDomParser-000000");
/*     */           }
/* 237 */           formattedDetail.append(MainNCLangRes.getInstance().getStrByID("sfapp", "BusiLogDetailDomParser-000001")).append(Opername).append("]; ");
/*     */         }
/*     */         
/* 240 */         NodeList rootarrlist = SelectNodes("attribute", node1);
/*     */         
/*     */ 
/*     */ 
/* 244 */         for (int i = 0; i < rootarrlist.getLength(); i++)
/*     */         {
/*     */ 
/*     */ 
/*     */ 
/* 249 */           Element elem = (Element)rootarrlist.item(i);
/* 250 */           if (elem.getNodeName().equals("attribute")) {
/* 251 */             if ((elem.getAttribute("type") == null) || (!elem.getAttribute("type").equals("child")))
/*     */             {
/* 253 */               String dispnm = null;
/*     */               
/* 255 */               String newvalue = null;
/*     */               
/* 257 */               String oldvalue = null;
/*     */               
/* 259 */               dispnm = elem.getAttribute("displayname");
/* 260 */               if ((elem.getFirstChild() != null) && (elem.getFirstChild().getNodeType() == 3)) {
/* 261 */                 newvalue = elem.getFirstChild().getTextContent();
/*     */               }
/*     */               
/* 264 */               NodeList oldlist = SelectNodes("before", elem);
/* 265 */               if ((oldlist != null) && (oldlist.getLength() > 0)) {
/* 266 */                 Element oldelem = (Element)oldlist.item(0);
/* 267 */                 if ((oldelem != null) && (oldelem.getFirstChild() != null))
/* 268 */                   oldvalue = oldelem.getFirstChild().getTextContent();
/*     */               }
/* 270 */               if (!checknull(dispnm))
/*     */               {
/* 272 */                 if (!checknull(oldvalue)) {
/* 273 */                   formattedDetail.append(dispnm).append(MainNCLangRes.getInstance().getStrByID("sfapp", "BusiLogDetailDomParser-000002")).append(oldvalue).append(MainNCLangRes.getInstance().getStrByID("sfapp", "BusiLogDetailDomParser-000003")).append(newvalue == null ? "" : newvalue).append("]; ").append("    ");
/* 274 */                 } else if (!checknull(newvalue)) {
							formattedDetail.append(dispnm).append(MainNCLangRes.getInstance().getStrByID("sfapp", "BusiLogDetailDomParser-000002")).append(oldvalue == null ? "" : oldvalue).append(MainNCLangRes.getInstance().getStrByID("sfapp", "BusiLogDetailDomParser-000003")).append(newvalue).append("]; ").append("    ");
/*     */                 }
/*     */               }
/*     */             }
/* 279 */             else if (elem.getAttribute("type").equals("child"))
/*     */             {
/* 281 */               formattedDetail.append("\n\n");
/* 282 */               forMat2(elem, loop + 1);
/*     */             }
/*     */           }
/*     */         }
/* 286 */         formattedDetail.append("\n");
/* 287 */         if (ischild) {
/* 288 */           formattedDetail.append("    ");
/*     */         }
/*     */       }
/*     */     } catch (Exception e) {
/* 292 */       Logger.error(e);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean checknull(String str)
/*     */   {
/* 301 */     if ((str == null) || (str.trim().isEmpty())) {
/* 302 */       return true;
/*     */     }
/*     */     
/* 305 */     return false;
/*     */   }
/*     */   
/*     */   private static NodeList SelectNodes(String express, Object source) throws XPathExpressionException
/*     */   {
/* 310 */     NodeList nlist = null;
/* 311 */     XPathFactory fac = XPathFactory.newInstance();
/* 312 */     XPath xpath = fac.newXPath();
/*     */     
/* 314 */     nlist = (NodeList)xpath.evaluate(express, source, XPathConstants.NODESET);
/*     */     
/* 316 */     return nlist;
/*     */   }
/*     */ }