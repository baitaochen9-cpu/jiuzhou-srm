package nc.ui.pu.m21.editor.card.afteredit.body;
/** <a href="http://www.cpupk.com/decompiler">Eclipse Class Decompiler</a> plugin, Copyright (c) 2017 Chen Chao. **/
/*     */
/*     */import java.util.ArrayList;
/*     */
import java.util.HashMap;
/*     */
import java.util.List;
/*     */
import java.util.Map;
import nc.bs.framework.common.NCLocator;
/*     */
import nc.itf.pu.pub.IPURemoteCallCombinator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.BeanListProcessor;
/*     */
import nc.medpub.util.SpecialTaxRateHandler;
/*     */
import nc.medpub.util.assist.RateType;
/*     */
import nc.ui.pu.m21.editor.card.afteredit.PriceQuoterUtil;
/*     */
import nc.ui.pu.m21.editor.card.afteredit.RelationCalculate;
/*     */
import nc.ui.pu.m21.rule.ContractLinker;
/*     */
import nc.ui.pu.m21.rule.CurrencyRelated;
/*     */
import nc.ui.pu.m21.rule.EditableSetter;
/*     */
import nc.ui.pu.m21.util.OrderCalculatorUtils;
/*     */
import nc.ui.pu.pub.editor.CardEditorHelper;
/*     */
import nc.ui.pu.pub.editor.ClientContext;
/*     */
import nc.ui.pu.pub.editor.card.listener.ICardBodyAfterEditEventListener;
/*     */
import nc.ui.pub.bill.BillCardPanel;
/*     */
import nc.ui.pub.bill.BillModel;
/*     */
import nc.ui.pubapp.uif2app.event.card.CardBodyAfterEditEvent;
/*     */
import nc.ui.pubapp.uif2app.view.util.RefMoreSelectedUtils;
/*     */
import nc.vo.ct.business.enumeration.Ninvctlstyle;
/*     */
import nc.vo.ct.entity.CtBusinessVO;
import nc.vo.jcom.lang.StringUtil;
/*     */
import nc.vo.pu.m21.rule.AssitunitAndQtunit;
/*     */
import nc.vo.pu.m21.rule.Batchcode;
/*     */
import nc.vo.pu.m21.rule.FlowStockOrgValue;
/*     */
import nc.vo.pu.m21.rule.OrganizationDefaultValue;
/*     */
import nc.vo.pu.m21.rule.PurchaseOrgValue;
/*     */
import nc.vo.pu.m21.rule.ReqCorpDefaultValue;
/*     */
import nc.vo.pu.m21.rule.SupplierDefaultInfo;
/*     */
import nc.vo.pu.m21.rule.TaxRateSetter;
/*     */
import nc.vo.pu.m21.rule.TaxTypeSetter;
/*     */
import nc.vo.pu.m21.rule.UnitAndChangeRate;
/*     */
import nc.vo.pu.m21.rule.VendorMaterial;
/*     */
import nc.vo.pu.m21.rule.Vfree;
/*     */
import nc.vo.pu.m21.rule.vat.OrderVatValueFillRule;
/*     */
import nc.vo.pu.m21.rule.vat.setter.country.OrderReceiveCountrySetter;
/*     */
import nc.vo.pu.m21.rule.vat.setter.country.OrderSendCountrySetter;
/*     */
import nc.vo.pu.m21.rule.vat.setter.country.OrderTaxCountrySetter;
/*     */
import nc.vo.pu.pub.enumeration.PriceParam;
import nc.vo.pu.supqualistatus.SupplierqualityHVO;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.pub.SqlBuilder;
/*     */
import nc.vo.scmpub.res.billtype.CTBillType;
/*     */
import org.apache.commons.collections.CollectionUtils;
/*     */
import org.apache.commons.lang.ArrayUtils;
/*     */
import org.apache.commons.lang.ObjectUtils;
/*     */
/*     */public class Material
/*     */implements ICardBodyAfterEditEventListener
/*     */{
	/*  68 */private Map<String, IPURemoteCallCombinator> remoteCaller = new HashMap();
	/*     */
	/*     */public void afterEdit(CardBodyAfterEditEvent event)
	/*     */{
		/*  73 */boolean nc = event.getBillCardPanel().getBillModel()
				.isNeedCalculate();
		/*  74 */event.getBillCardPanel().getBillModel()
				.setNeedCalculate(false);
		/*     */
		/*  79 */ClientContext clientContext = (ClientContext) event
				.getContext();
		/*  80 */clientContext.setNeedLoadRelationItem(false);
		/*  81 */int[] rows = null;
		/*     */try
		/*     */{
			/*  84 */RefMoreSelectedUtils utils =
			/*  85 */new RefMoreSelectedUtils(event.getBillCardPanel());
			/*  86 */rows =
			/*  87 */utils.refMoreSelected(event.getRow(), "pk_material",
			/*  88 */true);
			/*     */}
		/*     */finally
		/*     */{
			/*  92 */clientContext.setNeedLoadRelationItem(true);
			/*     */}
		/*     */
		/*  98 */setQpbaseschemeNull(rows, event.getBillCardPanel());
		/*  99 */CardEditorHelper card = new CardEditorHelper(
				event.getBillCardPanel());
		/*     */
		/* 104 */processPurchaseOrgVid(card);
		/*     */
		/* 106 */processDefaultMaterialInof(card, rows);
		dealQualiStatus( event,event.getBillCardPanel(),  rows);
		/* 107 */setDefaultBodyFromCntClass(event, rows);
		/*     */
		/* 109 */ContractLinker contractLinker = new ContractLinker(event);
		/* 110 */Integer[] ctparams =
		/* 111 */getContractlinkParams(event.getBillCardPanel(), rows);
		/*     */
		/* 113 */registerRemoteCall(card, rows);
		/*     */
		/* 115 */processDefaultOrganizationValue(rows);
		/*     */
		/* 117 */new ReqCorpDefaultValue(card).setDefaultValue(rows);
		/*     */
		/* 119 */processFlowStockOrg(rows);
		/*     */
		/* 121 */processVendorMaterialInfo();
		/*     */
		/* 123 */processSupInfo();
		/*     */
		/* 126 */new CurrencyRelated(event.getBillCardPanel())
		/* 127 */.setCurrencyAndExchangeRate(rows);
		/*     */
		/* 129 */registerCountryRemoteCall(card, rows);
		/*     */
		/* 134 */processCountryRemoteCall(card);
		/*     */
		/* 136 */SpecialTaxRateHandler taxrate = new SpecialTaxRateHandler(
				"pk_org", "pk_material", "ntaxrate", RateType.IN);
		/* 137 */taxrate.afterEdit(event);
		/*     */
		/* 140 */new Vfree(card).setVfreeNull(rows);
		/*     */
		/* 142 */new Batchcode(card).setBatchcodeNull(rows);
		/* 143 */if (!(ArrayUtils.isEmpty(ctparams)))
		/*     */{
			/* 145 */contractLinker.contractLink(ctparams, false, true);
			/*     */}
		/*     */
		/* 148 */setCardEditable(event, rows);
		/*     */
		/* 151 */setDefaultPrice(event, rows);
		/*     */
		/* 154 */event.getBillCardPanel().getBillModel().setNeedCalculate(nc);
		/*     */
		/* 156 */event.getBillCardPanel().getBillModel()
				.execEditFormulaByKey(event.getRow(), event.getKey());
		/*     */}
	/*     */
	/*     */private Integer[] getContractlinkParams(BillCardPanel panel,
			int[] rows)
	/*     */{
		/* 171 */List notCntIndex = new ArrayList();
		/* 172 */for (int row : rows) {
			/* 173 */String sourcetype =
			/* 174 */(String) panel.getBodyValueAt(row, "csourcetypecode");
			/* 175 */if (!(ObjectUtils.equals(CTBillType.PurDaily.getCode(),
					sourcetype))) {
				/* 176 */panel.setBodyValueAt(null, row, "ccontractid");
				/* 177 */panel.setBodyValueAt(null, row, "ccontractrowid");
				/* 178 */panel.setBodyValueAt(null, row, "vcontractcode");
				/* 179 */notCntIndex.add(Integer.valueOf(row));
				/*     */}
			/*     */}
		/* 182 */if (CollectionUtils.isEmpty(notCntIndex)) {
			/* 183 */return null;
			/*     */}
		/* 185 */return ((Integer[]) notCntIndex
				.toArray(new Integer[notCntIndex.size()]));
		/*     */}
	/*     */
	/*     */private void processCountryRemoteCall(CardEditorHelper editor)
	/*     */{
		/* 194 */OrderVatValueFillRule vatrule =
		/* 196 */(OrderVatValueFillRule) this.remoteCaller
		/* 196 */.get(OrderVatValueFillRule.class.getName());
		/* 197 */vatrule.process();
		/* 198 */OrderCalculatorUtils.calculate(editor.getEditor(),
		/* 199 */vatrule.getValueChangeObject());
		/*     */}
	/*     */
	/*     */private void processDefaultMaterialInof(CardEditorHelper card,
			int[] rows)
	/*     */{
		/* 210 */new TaxTypeSetter(card).setBodyTaxType(rows);
		/*     */
		/* 212 */new TaxRateSetter(card).setBodyTaxRate(rows);
		/*     */
		/* 214 */new AssitunitAndQtunit(card).setAssistunitAndQtunit(rows);
		/*     */
		/* 216 */UnitAndChangeRate rate = new UnitAndChangeRate(card);
		/* 217 */rate.setChangeRate(rows);
		/* 218 */rate.setQtChangeRate(rows);
		/* 219 */relationCalculate(card.getEditor(), rows);
		/*     */}
	/*     */
	/*     */private void processPurchaseOrgVid(CardEditorHelper card) {
		/* 223 */new PurchaseOrgValue(card).setPurchaseOrgValue();
		/*     */}
	/*     */
	/*     */private void processDefaultOrganizationValue(int[] rows) {
		/* 227 */OrganizationDefaultValue odv =
		/* 229 */(OrganizationDefaultValue) this.remoteCaller
		/* 229 */.get(OrganizationDefaultValue.class.getName());
		/* 230 */odv.setDefaultOrganizationValue(rows);
		/*     */}
	/*     */
	/*     */private void processFlowStockOrg(int[] rows) {
		/* 234 */FlowStockOrgValue fsov =
		/* 235 */(FlowStockOrgValue) this.remoteCaller
				.get(FlowStockOrgValue.class
				/* 236 */.getName());
		/* 237 */fsov.setFlowStockOrg(rows);
		/*     */}
	/*     */
	/*     */private void processSupInfo() {
		/* 241 */SupplierDefaultInfo sup =
		/* 242 */(SupplierDefaultInfo) this.remoteCaller
				.get(SupplierDefaultInfo.class
				/* 243 */.getName());
		/* 244 */sup.setSupplierDefaultInfo();
		/*     */}
	/*     */
	/*     */private void processVendorMaterialInfo() {
		/* 248 */VendorMaterial vmRule =
		/* 249 */(VendorMaterial) this.remoteCaller.get(VendorMaterial.class
				.getName());
		/* 250 */vmRule.setMaterialInfo();
		/*     */}
	/*     */
	/*     */private CtBusinessVO queryCtBusinessByPks(
			CardBodyAfterEditEvent event, String ccontractrowid)
	/*     */{
		/* 255 */ClientContext ctx = (ClientContext) event.getContext();
		/* 256 */return ctx.getCtBusiType(ccontractrowid);
		/*     */}
	/*     */
	/*     */private void registerCountryRemoteCall(CardEditorHelper editor,
			int[] rows) {
		/* 260 */List countryList = new ArrayList();
		/* 261 */countryList.add(new OrderSendCountrySetter(editor, rows));
		/* 262 */countryList.add(new OrderReceiveCountrySetter(editor, rows));
		/* 263 */countryList.add(new OrderTaxCountrySetter(editor, rows));
		/* 264 */OrderVatValueFillRule vatrule =
		/* 265 */new OrderVatValueFillRule(editor, rows, countryList);
		/* 266 */vatrule.prepare();
		/* 267 */this.remoteCaller.put(OrderVatValueFillRule.class.getName(),
				vatrule);
		/*     */}
	/*     */
	/*     */private void registerRemoteCall(CardEditorHelper card, int[] rows)
	/*     */{
		/* 272 */OrganizationDefaultValue odv = new OrganizationDefaultValue(
				card);
		/* 273 */odv.registerCombineRemoteCall(rows);
		/* 274 */this.remoteCaller.put(
				OrganizationDefaultValue.class.getName(), odv);
		/*     */
		/* 276 */FlowStockOrgValue fsov = new FlowStockOrgValue(card);
		/* 277 */fsov.registerCombineRemoteCall(rows);
		/* 278 */this.remoteCaller
				.put(FlowStockOrgValue.class.getName(), fsov);
		/*     */
		/* 281 */VendorMaterial vmRule = new VendorMaterial(card, rows);
		/* 282 */vmRule.prepare();
		/* 283 */this.remoteCaller.put(VendorMaterial.class.getName(), vmRule);
		/*     */
		/* 285 */SupplierDefaultInfo supRule = new SupplierDefaultInfo(card,
				rows);
		/* 286 */supRule.prepare();
		/* 287 */this.remoteCaller.put(SupplierDefaultInfo.class.getName(),
				supRule);
		/*     */}
	/*     */
	/*     */private void relationCalculate(BillCardPanel panel, int[] rows) {
		/* 291 */RelationCalculate cal = new RelationCalculate();
		/* 292 */cal.calculate(panel, rows, "vchangerate");
		/* 293 */cal.calculate(panel, rows, "vqtunitrate");
		/*     */}
	/*     */
	/*     */private void setCardEditable(CardBodyAfterEditEvent event,
			int[] rows)
	/*     */{
		/* 298 */new EditableSetter(event.getBillCardPanel())
				.setEditableByMaterial(rows);
		/*     */
		/* 300 */new EditableSetter(event.getBillCardPanel())
				.setEditableByContract(rows);
		/*     */
		/* 302 */new EditableSetter(event.getBillCardPanel())
				.setEditableByUnit(rows);
		/*     */}
	/*     */
	/*     */private void setDefaultBodyFromCntClass(
			CardBodyAfterEditEvent event, int[] rows)
	/*     */{
		/* 307 */if ((rows == null) || (rows.length == 0)) {
			/* 308 */return;
			/*     */}
		/* 310 */BillCardPanel panel = event.getBillCardPanel();
		/* 311 */int row = event.getRow();
		/*     */
		/* 313 */boolean addLine = row == panel.getBillModel().getRowCount()
				- rows.length;
		/* 314 */if (!(addLine))
		/*     */{
			/* 316 */row = rows[0];
			/*     */}
		/*     */
		/* 319 */String csoucetypecode =
		/* 320 */(String) panel.getBodyValueAt(row, "csourcetypecode");
		/* 321 */if (!(ObjectUtils.equals(CTBillType.PurDaily.getCode(),
				csoucetypecode))) {
			/* 322 */return;
			/*     */}
		/*     */
		/* 325 */String ccontractrowid =
		/* 326 */(String) panel.getBodyValueAt(row, "ccontractrowid");
		/* 327 */CtBusinessVO ctvo = queryCtBusinessByPks(event,
				ccontractrowid);
		/* 328 */if ((ctvo == null)
				||
				/* 329 */(!(Ninvctlstyle.MARBASCLASS.value().equals(ctvo
						.getNinvctlstyle())))) {
			/* 330 */return;
			/*     */}
		/*     */
		/* 333 */for (int i = 1; i < rows.length; ++i){
			/* 334 */setDefaultValueFromCntValue(panel, row, rows[i]);
		}
		/*     */}
	/*     */
	/*     */private void setDefaultPrice(CardBodyAfterEditEvent event,
			int[] rows)
	/*     */{
		/* 347 */PriceQuoterUtil priceQuoterUtil = new PriceQuoterUtil();
		/* 348 */priceQuoterUtil.setDefaultPrice(event.getBillCardPanel(),
		/* 349 */PriceParam.Material, rows);
		/*     */}
	/*     */
	/*     */private void setDefaultValueFromCntValue(BillCardPanel panel,
			int row, int targetrow)
	/*     */{
		/* 354 */panel.setBodyValueAt(panel.getBodyValueAt(row, "csourcebid"),
		/* 355 */targetrow, "csourcebid");
		/* 356 */panel.setBodyValueAt(panel.getBodyValueAt(row, "csourceid"),
		/* 357 */targetrow, "csourceid");
		/* 358 */panel.setBodyValueAt(
		/* 359 */panel.getBodyValueAt(row, "csourcetypecode"), targetrow,
		/* 360 */"csourcetypecode");
		/* 361 */panel.setBodyValueAt(
				panel.getBodyValueAt(row, "vsourcecode"),
				/* 362 */targetrow, "vsourcecode");
		/* 363 */panel.setBodyValueAt(
				panel.getBodyValueAt(row, "vsourcerowno"),
				/* 364 */targetrow, "vsourcerowno");
		/* 365 */panel.setBodyValueAt(
		/* 366 */panel.getBodyValueAt(row, "vsourcetrantype"), targetrow,
		/* 367 */"vsourcetrantype");
		/* 368 */panel.setBodyValueAt(
				panel.getBodyValueAt(row, "ccontractid"),
				/* 369 */targetrow, "ccontractid");
		/* 370 */panel.setBodyValueAt(
				panel.getBodyValueAt(row, "ccontractrowid"),
				/* 371 */targetrow, "ccontractrowid");
		/* 372 */panel.setBodyValueAt(
				panel.getBodyValueAt(row, "vcontractcode"),
				/* 373 */targetrow, "vcontractcode");
		/* 374 */panel.setBodyValueAt(panel.getBodyValueAt(row, "sourcebts"),
		/* 375 */targetrow, "sourcebts");
		/* 376 */panel.setBodyValueAt(panel.getBodyValueAt(row, "sourcets"),
		/* 377 */targetrow, "sourcets");
		/*     */
		/* 379 */panel.setBodyValueAt(panel.getBodyValueAt(row, "norigprice"),
		/* 380 */targetrow, "norigprice");
		/* 381 */panel.setBodyValueAt(
				panel.getBodyValueAt(row, "norigtaxprice"),
				/* 382 */targetrow, "norigtaxprice");
		/*     */
		/* 384 */panel.setBodyValueAt(
				panel.getBodyValueAt(row, "nqtorigprice"),
				/* 385 */targetrow, "nqtorigprice");
		/* 386 */panel.setBodyValueAt(
		/* 387 */panel.getBodyValueAt(row, "nqtorigtaxprice"), targetrow,
		/* 388 */"nqtorigtaxprice");
		/*     */}
	/*     */
	/*     */private void setQpbaseschemeNull(int[] rows, BillCardPanel panel)
	/*     */{
		/* 393 */if ((rows == null) || (rows.length == 0)) {
			/* 394 */return;
			/*     */}
		/* 396 */for (int i = 0; i < rows.length; ++i)
			/* 397 */panel.setBodyValueAt(null, rows[i], "cqpbaseschemeid");
		/*     */}
	/*     */
	private void dealQualiStatus(CardBodyAfterEditEvent event,BillCardPanel panel, int[] rows){
		
		String pk_group = panel.getHeadItem("pk_group").getValue();
		String pk_org = panel.getHeadItem("pk_org").getValue();
		String pk_supplier = panel.getHeadItem("pk_supplier").getValue();
		SqlBuilder builder = new SqlBuilder();
		builder.append("select y.*,c.name,e.suppliergrade code from qc_supplierquality y ");
		builder.append(" join bd_defdoc c on y.pk_vendor = c.pk_defdoc ");
		builder.append(" join bd_supplier_grade e on y.pk_grade_info = e.pk_grade_info ");
		builder.append(" where nvl(y.dr,0)=0 and nvl(c.dr,0)=0 ");
		builder.append(" and y.pk_group", pk_group);
		builder.append(" and y.pk_org", pk_org);
		builder.append(" and y.pk_supplier", pk_supplier);
		
		
		try {
			IUAPQueryBS query = NCLocator.getInstance().lookup(IUAPQueryBS.class);
			List<SupplierqualityHVO> volist = (List<SupplierqualityHVO>) query
					.executeQuery(builder.toString(), new BeanListProcessor(
							SupplierqualityHVO.class));
			if (volist == null || volist.size() == 0)
				volist = new ArrayList<>();
			Map<String,List<String>> retMap = new HashMap<String, List<String>>();
			for (SupplierqualityHVO vo : volist) {
				String key = vo.getPk_material();
				List<String> slist = null;
				if (retMap.containsKey(key)) {
					slist = retMap.get(key);
				} else {
					slist = new ArrayList<String>();
				}
				
				if(StringUtil.isEmpty(vo.getCode())){
					continue;
				}
				if("待批准".equals(vo.getCode().trim()) || "已批准".equals(vo.getCode().trim()) || "认证".equals(vo.getCode().trim()) || "批准".equals(vo.getCode().trim())){
					slist.add(vo.getName());
					retMap.put(key, slist);
				}
			}
			
			for (int row : rows){
				String material = (String) panel.getBodyValueAt(row,
						"pk_material");
				List<String> list = retMap.get(material);
				
				if(list == null || list.size()==0)
					continue;
				String ventor = "";
				for(String str: list){
					ventor=ventor+str+",";
				}
				ventor = ventor.substring(0,ventor.length()-1);
				panel.setBodyValueAt(ventor, row, "vbdef18");
			}
		} catch (BusinessException e) {
			e.printStackTrace();
		}
		
		
	}
}