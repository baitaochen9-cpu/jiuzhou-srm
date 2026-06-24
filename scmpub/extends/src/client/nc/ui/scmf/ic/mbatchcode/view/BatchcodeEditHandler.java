package nc.ui.scmf.ic.mbatchcode.view;
/** <a href="http://www.cpupk.com/decompiler">Eclipse Class Decompiler</a> plugin, Copyright (c) 2017 Chen Chao. **/
/*     */ 
/*     */ import java.util.HashMap;

import nc.bs.framework.common.NCLocator;
import nc.impl.scmf.ic.tools.cache.CacheTool;
/*     */ import nc.itf.scmpub.reference.uap.bd.material.MaterialPubService;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapProcessor;
/*     */ import nc.ui.pub.beans.UIRefPane;
/*     */ import nc.ui.pub.bill.BillCardPanel;
/*     */ import nc.ui.pub.bill.BillItem;
/*     */ import nc.ui.pub.bill.BillModel;
/*     */ import nc.ui.pubapp.uif2app.event.IAppEventHandler;
/*     */ import nc.ui.pubapp.uif2app.event.card.CardBodyAfterEditEvent;
/*     */ import nc.ui.pubapp.uif2app.view.BatchBillTable;
/*     */ import nc.vo.bd.material.stock.MaterialStockVO;
/*     */ import nc.vo.cache.CacheManager;
/*     */ import nc.vo.cache.ICache;
/*     */ import nc.vo.cache.config.CacheConfig;
/*     */ import nc.vo.pub.BusinessException;
/*     */ import nc.vo.pub.lang.UFBoolean;
/*     */ import nc.vo.pubapp.pattern.log.Log;
import nc.vo.scmf.ic.mbatchcode.BatchcodeVO;
/*     */ import nc.vo.uif2.LoginContext;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BatchcodeEditHandler
/*     */   implements IAppEventHandler<CardBodyAfterEditEvent>
/*     */ {
/*  28 */   private ICache cacheMap = null;
/*     */ 
/*  30 */   private BatchBillTable editor = null;
/*     */ 
/*  32 */   private LoginContext loginContext = null;
/*     */ 
/*     */   public BatchcodeEditHandler()
/*     */   {
/*  36 */     initCache();
/*     */   }
/*     */ 
/*     */ 
/*     */   public BatchcodeEditHandler(LoginContext context)
/*     */   {
/*  42 */     initCache();
/*  43 */     this.loginContext = context;
/*     */   }
/*     */ 
/*     */   public void afterEdit(BillCardPanel billCardPanel, CardBodyAfterEditEvent e) {
/*     */     try {
/*  48 */       if ("cmaterialvid".equals(e.getKey())) {
/*  49 */         afterMaterialEdit(billCardPanel, e);
/*     */       }
				// 如果修改了生产日期 或者 失效日期，标签重打标记为Y
				if("dproducedate".equals(e.getKey()) || "dvalidate".equals(e.getKey())){
					String pk_org = loginContext.getPk_org();
					// 只有九洲药物科技的需要28
					boolean is = isNeed(pk_org);
					if(is){
						afterDproduceateOrDvalidateEdit(billCardPanel, e);
					}
					
				}
/*  51 */       else if (!("dproducedate".equals(e.getKey())));
/*     */ 
/*     */ 
/*  54 */       if (!(e.getKey().equals("cqualitylevelid")));
/*     */ 
/*     */     }
/*     */     catch (BusinessException ex)
/*     */     {
/*  59 */       Log.error(ex);
/*     */     }
/*     */   }
			private boolean isNeed(String pk_org) throws BusinessException {
				String sql = "select distinct value  from  pub_sysinit where initcode='YF_BQCD_PC' and pk_org = '"
						+ pk_org + "'";
				IUAPQueryBS bs = (IUAPQueryBS) NCLocator.getInstance().lookup(
						IUAPQueryBS.class.getName());
			
				String flag = (String) bs
						.executeQuery(sql, new ColumnProcessor());
				if("Y".equals(flag)){
					return true;
				}else{
					return false;
				}
			}
			/**
			 * 修改生产日期 或者 失效日期，标签重打标记为Y
			 * @param billCardPanel
			 * @param e
			 */
			private void afterDproduceateOrDvalidateEdit(BillCardPanel billCardPanel,
					CardBodyAfterEditEvent e) {
				// TODO Auto-generated method stub
				UFBoolean qualitymanflag = UFBoolean.TRUE;
				billCardPanel.getBillModel().setValueAt(qualitymanflag, e.getRow(), "vdef16");
				
			}
/*     */ 
/*     */   public BatchBillTable getEditor()
/*     */   {
/*  65 */     return this.editor;
/*     */   }
/*     */ 
/*     */   public LoginContext getLoginContext() {
/*  69 */     return this.loginContext;
/*     */   }
/*     */ 
/*     */   public void handleAppEvent(CardBodyAfterEditEvent e)
/*     */   {
/*  74 */     BillCardPanel billCardPanel = this.editor.getBillCardPanel();
/*  75 */     afterEdit(billCardPanel, e);
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
/*     */   public void setEditor(BatchBillTable editor)
/*     */   {
/* 128 */     this.editor = editor;
/*     */   }
/*     */ 
/*     */   public void setLoginContext(LoginContext loginContext) {
/* 132 */     this.loginContext = loginContext;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void afterMaterialEdit(BillCardPanel billCardPanel, CardBodyAfterEditEvent e)
/*     */     throws BusinessException
/*     */   {
/* 144 */     UIRefPane materialRef = (UIRefPane)billCardPanel.getBodyItem("cmaterialvid").getComponent();
/*     */ 
/*     */ 
/* 147 */     if (materialRef == null) {
/* 148 */       return;
/*     */     }
/* 150 */     String pk_material = materialRef.getRefPK();
/* 151 */     if (pk_material == null) {
/* 152 */       return;
/*     */     }
/*     */ 
/* 155 */     MaterialStockVO materialStockVO = getMaterialInfo(pk_material);
/*     */ 
/* 157 */     UFBoolean qualitymanflag = getQualitymanflag(materialStockVO);
/* 158 */     billCardPanel.getBillModel().setValueAt(qualitymanflag, e.getRow(), "qualitymanflag");
/*     */ 
/* 160 */     if (UFBoolean.TRUE.equals(qualitymanflag)) {
/* 161 */       Integer qualitydaynum = getQualitydaynum(materialStockVO);
/* 162 */       Integer qualityunit = getQualityunit(materialStockVO);
/* 163 */       billCardPanel.getBillModel().setValueAt(qualitydaynum, e.getRow(), "qualitynum");
/*     */ 
/* 165 */       billCardPanel.getBillModel().setValueAt(qualityunit, e.getRow(), "qualityunit");
/*     */     }
/*     */     else
/*     */     {
/* 169 */       billCardPanel.getBillModel().setValueAt(null, e.getRow(), "qualitynum");
/*     */ 
/* 171 */       billCardPanel.getBillModel().setValueAt(null, e.getRow(), "qualityunit");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   private MaterialStockVO getMaterialInfo(String pk_material)
/*     */     throws BusinessException
/*     */   {
/* 179 */     if ((pk_material == null) || (pk_material.trim().equals(""))) {
/* 180 */       return null;
/*     */     }
/* 182 */     MaterialStockVO materialVO = (MaterialStockVO)this.cacheMap.get(pk_material);
/*     */ 
/* 184 */     if (materialVO == null) {
/* 185 */       materialVO = queryMaterialCorpVO(pk_material);
/* 186 */       if (materialVO != null) {
/* 187 */         this.cacheMap.put(pk_material, materialVO);
/*     */       }
/*     */     }
/*     */ 
/* 191 */     return materialVO;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private Integer getQualitydaynum(MaterialStockVO materialcorpVO)
/*     */   {
/* 199 */     if (materialcorpVO == null) {
/* 200 */       return null;
/*     */     }
/* 202 */     return materialcorpVO.getQualitynum();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private UFBoolean getQualitymanflag(MaterialStockVO materialcorpVO)
/*     */   {
/* 211 */     if (materialcorpVO == null) {
/* 212 */       return null;
/*     */     }
/* 214 */     return materialcorpVO.getQualitymanflag();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private Integer getQualityunit(MaterialStockVO materialcorpVO)
/*     */   {
/* 222 */     if (materialcorpVO == null) {
/* 223 */       return null;
/*     */     }
/* 225 */     return materialcorpVO.getQualityunit();
/*     */   }
/*     */ 
/*     */   private void initCache()
/*     */   {
/* 230 */     CacheConfig config = CacheTool.getInstance().getCacheByID("ic-lru-batchcode");
/*     */ 
/* 232 */     this.cacheMap = CacheManager.getInstance().getCache(config);
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
/*     */   private MaterialStockVO queryMaterialCorpVO(String pk_material)
/*     */     throws BusinessException
/*     */   {
/* 246 */     String[] fields = { "qualitymanflag", "qualityunit", "qualitynum" };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 251 */     if (this.loginContext.getPk_org() == null) {
/* 252 */       return null;
/*     */     }
/* 254 */     MaterialStockVO[] matrialStockVOs = MaterialPubService.queryMaterialStockInfoByPks(new String[] { pk_material }, this.loginContext.getPk_org(), fields);
/*     */ 
/*     */ 
/*     */ 
/* 258 */     if ((matrialStockVOs == null) || (matrialStockVOs.length == 0)) {
/* 259 */       return null;
/*     */     }
/* 261 */     MaterialStockVO mVO = matrialStockVOs[0];
/* 262 */     return mVO;
/*     */   }
/*     */ }
