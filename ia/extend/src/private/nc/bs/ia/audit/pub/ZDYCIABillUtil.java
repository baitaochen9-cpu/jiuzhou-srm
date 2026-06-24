package nc.bs.ia.audit.pub;

 import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.impl.pubapp.pattern.data.vo.VOQuery;
import nc.impl.pubapp.pattern.database.DataAccessUtils;
import nc.itf.scmpub.reference.uap.bd.accesor.MaterialAccessor;
import nc.itf.scmpub.reference.uap.group.SysInitGroupQuery;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.vo.bd.accessor.IBDData;
import nc.vo.ia.detailledger.entity.DetailLedgerVO;
import nc.vo.ia.detailledger.view.ia.AuditViewVO;
import nc.vo.ia.enumeration.FAbnormalNabFlag;
import nc.vo.ia.enumeration.FAbnormalNabHandleFlag;
import nc.vo.ia.enumeration.FDataGetFlag;
import nc.vo.ia.enumeration.FIntransitFlag;
import nc.vo.ia.enumeration.FPriceModeFlag;
import nc.vo.ia.generalnab.entity.GeneralNABVO;
import nc.vo.ia.pub.entity.CalcRangeVO;
import nc.vo.ia.pub.util.IAParameter;
import nc.vo.ia.pub.util.IAScaleUtil;
import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.pattern.data.IRowSet;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.pubapp.pattern.pub.MathTool;
import nc.vo.pubapp.pattern.pub.SqlBuilder;
import nc.vo.scmpub.res.billtype.IABillType;

 public class ZDYCIABillUtil
 {
   private IAContext context;
   private GeneralNABVO nabvo;
   private AuditViewVO vo;
 
   public ZDYCIABillUtil(AuditViewVO vo, GeneralNABVO nabvo, IAContext context)
   {
/*  53 */     this.context = context;
/*  54 */     this.nabvo = nabvo;
/*  55 */     this.vo = vo;
   }
 
 
 
 
 
 
 
   public DetailLedgerVO createIADetail(String lastOutDetailID, UFDouble nlastinprice)
   {
/*  66 */     UFDouble adjustMny = calculateAdjustMny(nlastinprice);
/*  67 */     if (adjustMny.compareTo(UFDouble.ZERO_DBL) == 0) {
/*  68 */       return null;
     }
 
/*  71 */     DetailLedgerVO bill = BillUtil.createIADetail(this.vo, this.context, lastOutDetailID);
 
/*  73 */     String msg = NCLangRes4VoTransl.getNCLangRes().getStrByID("2014002_0", "02014002-0106");
 
 
/*  76 */     bill.setVnote(msg);
/*  77 */     if (SysInitGroupQuery.isCMEnabled()) {
/*  78 */       clearCMInfo(bill, lastOutDetailID);
     }
 
/*  81 */     bill.setNmny(adjustMny);
/*  82 */     BillUtil.fillGlobalMny(bill);
/*  83 */     BillUtil.fillGroupMny(bill);
/*  84 */     if (FPriceModeFlag.JHJ.equalsValue(this.nabvo.getFpricemodeflag())) {
/*  85 */       bill.setNvarymny(adjustMny);
     }
/*  87 */     bill.setFdatagetflag((Integer)FDataGetFlag.ZDYCTZ.value());
/*  88 */     bill.setFolddatagetflag((Integer)FDataGetFlag.ZDYCTZ.value());
 
/*  90 */     BillUtil.updateGeneralledgerForIA(this.nabvo, bill);
/*  91 */     return bill;
   }
 
   private void clearCMInfo(DetailLedgerVO bill, String lastOutDetailID) {
/*  95 */     String outaccountperiod = null;
/*  96 */     if (bill.getAttributeValue("outaccountperiod") != null) {
/*  97 */       outaccountperiod = (String)bill.getAttributeValue("outaccountperiod");
     }
     else {
/* 100 */       outaccountperiod = this.context.getCaccountperiod();
     }
/* 102 */     if (lastOutDetailID != null) {
/* 103 */       boolean flag = isAvailable(bill, outaccountperiod);
/* 104 */       if (!(flag)) {
/* 105 */         bill.setCtrantypeid(null);
/* 106 */         bill.setCcostobjid(null);
/* 107 */         bill.setCdeptid(null);
/* 108 */         bill.setCdeptvid(null);
/* 109 */         bill.setCcostcenterid(null);
       }
     }
   }
 
 
 
 
 
 
 
 
 
 
   public DetailLedgerVO createIADetail(String key, Map<String, DetailLedgerVO> detailMap, Map<String, UFDouble> lastInPriceMap)
   {
/* 125 */     UFDouble lastInPrice = (UFDouble)lastInPriceMap.get(key);
/* 126 */     UFDouble adjustMny = calculateAdjustMny(lastInPrice);
/* 127 */     if (adjustMny.compareTo(UFDouble.ZERO_DBL) == 0) {
/* 128 */       return null;
     }
//  2023-06-16 liyf 增加自定义参数和自定义档案,针对一些原逻辑需要生成出库调整单的，如果自定义档案中包换改存货，则不在生成
               if(isPass()){
            	   return null;
               }

//2023-06-16 Liyf  增加自定义参数和自定义档案,针对一些原逻辑需要生成出库调整单的，如果自定义档案中包换改存货，则不在生成


/* 130 */     DetailLedgerVO detail = (DetailLedgerVO)detailMap.get(key);
/* 131 */     if (detail != null) {
/* 132 */       BillUtil.clearData(detail);
/* 133 */       detail.setFintransitflag((Integer)FIntransitFlag.COMMON.value());
     }
     else {
/* 136 */       detail = new DetailLedgerVO();
/* 137 */       detail.setPk_group(this.context.getPk_group());
/* 138 */       detail.setPk_org(this.nabvo.getPk_org());
/* 139 */       detail.setCaccountperiod(this.context.getCaccountperiod());
/* 140 */       detail.setPk_book(this.context.getPk_book());
/* 141 */       detail.setCinventoryid(this.nabvo.getCinventoryid());
/* 142 */       detail.setCinventoryvid(this.nabvo.getCinventoryid());
/* 143 */       detail.setCcalcrangeid(this.nabvo.getCcalcrangeid());
/* 144 */       detail.setFintransitflag((Integer)FIntransitFlag.COMMON.value());
/* 145 */       VOQuery bo = new VOQuery(CalcRangeVO.class);
/* 146 */       CalcRangeVO[] rangevos = (CalcRangeVO[])bo.query(new String[] { this.nabvo.getCcalcrangeid() });
 
 
/* 149 */       detail.setVbatchcode(rangevos[0].getVbatchcode());
     }
/* 151 */     BillUtil.fillData(this.context, detail);
 
/* 153 */     String msg = NCLangRes4VoTransl.getNCLangRes().getStrByID("2014002_0", "02014002-0106");
 
 
/* 156 */     detail.setVnote(msg);
/* 157 */     detail.setNmny(adjustMny);
/* 158 */     BillUtil.fillGlobalMny(detail);
/* 159 */     BillUtil.fillGroupMny(detail);
/* 160 */     if (FPriceModeFlag.JHJ.equalsValue(this.nabvo.getFpricemodeflag())) {
/* 161 */       detail.setNvarymny(adjustMny);
     }
/* 163 */     detail.setFdatagetflag((Integer)FDataGetFlag.ZDYCTZ.value());
/* 164 */     detail.setFolddatagetflag((Integer)FDataGetFlag.ZDYCTZ.value());
 
/* 166 */     BillUtil.updateGeneralledgerForIA(this.nabvo, detail);
 
/* 168 */     return detail;
   }
		private boolean isPass(){
			// TODO Auto-generated method stub
			boolean ispass = false;
			String pk_costregion = this.nabvo.getPk_org();
			BaseDAO dao = new BaseDAO();
			String sql =" select value from pub_sysinit where initcode='YF641' and pk_org='"+pk_costregion+"'";
		
			try {
				//检查控制参数是否启用
				String yf641 = (String) dao.executeQuery(sql, new ColumnProcessor());
				if(!"Y".equalsIgnoreCase(yf641)){
					return ispass;
				}
				//检查是否在异常排除物料范围
				sql=" select count(0) from bd_defdoc where nvl(dr,0)=0 and enablestate=2 and  pk_defdoclist=(select pk_defdoclist from bd_defdoclist where code='YF641')" +
						"  and pk_org=(select pk_org from org_costregion where pk_costregion='"+pk_costregion+"' )" +
						" and def1='"+this.nabvo.getCinventoryid()+"'";
				Integer rs = (Integer) dao.executeQuery(sql, new ColumnProcessor());
				if(rs >0){
					ispass = true;
				}
			} catch (DAOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
//			    ExceptionUtils.wrappBusinessException("检查指定物料分类排除异常结存控制"+e.getMessage());
			}
			
			return ispass;
		}
 
 
 
 
 /**
 * 判断是否存在异常结存
 * 
 * @return boolean
 */
   public boolean isAbnormalNAB()
   {
/* 177 */     boolean isAbnormal = true;
/* 178 */     FAbnormalNabFlag abnormalType = getAbnormalType();
/* 179 */     String pk_org = this.nabvo.getPk_org();
 
/* 181 */     FAbnormalNabFlag[] abNabChoose = IAParameter.getIA0208(pk_org);
 
/* 183 */     if ((abNabChoose == null) || (abnormalType == null) || (!(isAbnormalTypeInAbNabChoose(abnormalType, abNabChoose))))
     {
/* 185 */       isAbnormal = false;
 
     }
 
/* 189 */     FAbnormalNabHandleFlag abNabHandleFlag = IAParameter.getIA0204(pk_org);
/* 190 */     if ((isAbnormal) && (abNabHandleFlag.value().equals(FAbnormalNabHandleFlag.PCZDYX.value())))
 
     {
/* 193 */       String msg = NCLangRes4VoTransl.getNCLangRes().getStrByID("2014002_0", "02014002-0055");
 
 
/* 196 */       ExceptionUtils.wrappBusinessException(msg);
     }
 
/* 199 */     return isAbnormal;
   }
 
 
 
 
 
   private UFDouble calculateAdjustMny(UFDouble nlastinprice)
   {
/* 208 */     UFDouble adjustMny = UFDouble.ZERO_DBL;
/* 209 */     UFDouble nabNum = (this.nabvo.getNabnum() == null) ? UFDouble.ZERO_DBL : this.nabvo.getNabnum();
 
 
 
 
/* 214 */     if (nabNum.compareTo(UFDouble.ZERO_DBL) == 0) {
/* 215 */       adjustMny = this.nabvo.getNabmny();
     }
     else
     {
/* 219 */       adjustMny = calculateMny(nlastinprice);
     }
/* 221 */     return adjustMny;
   }
 
   private UFDouble calculateMny(UFDouble nlastinprice) {
/* 225 */     UFDouble adjustMny = UFDouble.ZERO_DBL;
 
/* 227 */     if (nlastinprice == null) {
/* 228 */       IBDData bdDate = MaterialAccessor.getDocByPk(this.vo.getCinventoryid());
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
/* 244 */       String[] paras = { bdDate.getCode(), bdDate.getName().getText() };
 
 
/* 247 */       String msg = NCLangRes4VoTransl.getNCLangRes().getStrByID("2014002_0", "02014002-0256", null, paras);
 
 
/* 250 */       ExceptionUtils.wrappBusinessException(msg);
     }
     else {
/* 253 */       IAScaleUtil scale = IAScaleUtil.getScaleUtils();
/* 254 */       UFDouble mny = nlastinprice.multiply(this.nabvo.getNabnum());
/* 255 */       adjustMny = MathTool.sub(this.nabvo.getNabmny(), mny);
/* 256 */       adjustMny = scale.adjustMnyScale(adjustMny, this.context.getCcurrencyid());
     }
 
/* 259 */     return adjustMny;
   }
 
 
 
 
 
 
 
   private FAbnormalNabFlag getAbnormalType()
   {
/* 270 */     FAbnormalNabFlag abnormalType = null;
/* 271 */     UFDouble nabNum = (this.nabvo.getNabnum() == null) ? UFDouble.ZERO_DBL : this.nabvo.getNabnum();
 
 
/* 274 */     UFDouble nabMny = (this.nabvo.getNabmny() == null) ? UFDouble.ZERO_DBL : this.nabvo.getNabmny();
 
 
 
 
 
 
 
 
/* 283 */     if (nabNum.multiply(nabMny).compareTo(UFDouble.ZERO_DBL) < 0) {
/* 284 */       abnormalType = FAbnormalNabFlag.DIRECT_REVERSE;
     }
/* 286 */     else if ((nabNum.compareTo(UFDouble.ZERO_DBL) == 0) && (nabMny.compareTo(UFDouble.ZERO_DBL) < 0))
     {
/* 288 */       abnormalType = FAbnormalNabFlag.NUMZERO_MNYMINUS;
     }
/* 290 */     else if ((nabNum.compareTo(UFDouble.ZERO_DBL) == 0) && (nabMny.compareTo(UFDouble.ZERO_DBL) > 0))
     {
/* 292 */       abnormalType = FAbnormalNabFlag.NUMZERO_MNYPLUS;
     }
/* 294 */     else if ((nabNum.compareTo(UFDouble.ZERO_DBL) < 0) && (nabMny.compareTo(UFDouble.ZERO_DBL) == 0))
     {
/* 296 */       abnormalType = FAbnormalNabFlag.NUMMINUS_MNYZERO;
     }
/* 298 */     else if ((nabNum.compareTo(UFDouble.ZERO_DBL) > 0) && (nabMny.compareTo(UFDouble.ZERO_DBL) == 0))
     {
/* 300 */       abnormalType = FAbnormalNabFlag.NUMPLUS_MNYZERO;
     }
/* 302 */     return abnormalType;
   }
 
 
 
 
 
 
 
 
   private boolean isAvailable(DetailLedgerVO bill, String outaccountperiod)
   {
/* 314 */     if (bill.getCcostobjid() == null) {
/* 315 */       return true;
     }
 
/* 318 */     String crrentperiod = this.context.getCaccountperiod();
/* 319 */     DataAccessUtils util = new DataAccessUtils();
/* 320 */     if (crrentperiod.compareToIgnoreCase(outaccountperiod) == 0) {
/* 321 */       SqlBuilder sql = new SqlBuilder();
/* 322 */       sql.append(" select top 1 cdetailledgerid from ia_detailledger where dr=0 ");
/* 323 */       sql.append(" and caccountperiod", this.context.getCaccountperiod());
/* 324 */       sql.append(" and cinventoryid", bill.getCinventoryid());
/* 325 */       sql.append(" and ccostobjid", bill.getCcostobjid());
/* 326 */       sql.append(" and pk_org", bill.getPk_org());
/* 327 */       sql.append(" and pk_book", bill.getPk_book());
/* 328 */       sql.append(" and pk_group", bill.getPk_group());
/* 329 */       sql.append(" and fdatagetflag", (Integer)FDataGetFlag.XHLLQJ.value());
/* 330 */       IRowSet rowset = util.query(sql.toString());
/* 331 */       if (rowset.size() > 0)
/* 332 */         return false;
     }
     else
     {
/* 336 */       SqlBuilder sql = new SqlBuilder();
/* 337 */       sql.append(" select distinct fdatagetflag from ia_detailledger where dr=0 ");
/* 338 */       sql.append(" and caccountperiod", this.context.getCaccountperiod());
/* 339 */       sql.append(" and cinventoryid", bill.getCinventoryid());
/* 340 */       sql.append(" and ccostobjid", bill.getCcostobjid());
/* 341 */       sql.append(" and pk_org", bill.getPk_org());
/* 342 */       sql.append(" and pk_book", bill.getPk_book());
/* 343 */       sql.append(" and pk_group", bill.getPk_group());
/* 344 */       sql.append(" and cbilltypecode", IABillType.CLCK.getCode());
 
/* 346 */       IRowSet rowset = util.query(sql.toString());
/* 347 */       if (rowset.size() == 0) {
/* 348 */         return false;
       }
/* 350 */       while (rowset.next()) {
/* 351 */         Integer fdatagetflag = rowset.getInteger(0);
/* 352 */         if (fdatagetflag.equals(FDataGetFlag.XHLLQJ.value())) {
/* 353 */           return false;
         }
       }
     }
/* 357 */     return true;
   }
 
 
   private boolean isAbnormalTypeInAbNabChoose(FAbnormalNabFlag abnormalType, FAbnormalNabFlag[] abNabChoose)
   {
/* 363 */     boolean retFlag = false;
/* 364 */     for (FAbnormalNabFlag abnormalNabFlag : abNabChoose) {
/* 365 */       if (abnormalNabFlag.value().equals(abnormalType.value())) {
/* 366 */         retFlag = true;
/* 367 */         break;
       }
     }
/* 370 */     return retFlag;
   }
 }
