package nc.bs.to.m5x.maintain;

import nc.bs.scmpub.rule.CrossRuleValidateRule;
import nc.bs.to.m5x.maintain.InsertBP;
import nc.bs.to.m5x.maintain.rule.MedCheckBathAndLotnoRule;
import nc.bs.to.m5x.maintain.rule.MedCheckPermitRule;
import nc.bs.to.m5x.maintain.rule.insert.AutoReserveRule;
import nc.bs.to.m5x.maintain.rule.insert.CheckBillRule;
import nc.bs.to.m5x.maintain.rule.insert.CheckCountryNotNullRule;
import nc.bs.to.m5x.maintain.rule.insert.CheckCountryRule;
import nc.bs.to.m5x.maintain.rule.insert.CheckScalesRule;
import nc.bs.to.m5x.maintain.rule.insert.CheckUniqueBillCodeRule;
import nc.bs.to.m5x.maintain.rule.insert.CodeImplRule;
import nc.bs.to.m5x.maintain.rule.insert.FillBillCodeRule;
import nc.bs.to.m5x.maintain.rule.insert.FillDataRule;
import nc.bs.to.m5x.maintain.rule.insert.InsertTransferMsgRule;
import nc.bs.to.m5x.maintain.rule.insert.ProfitCenterCheckRule;
import nc.bs.to.m5x.maintain.rule.insert.RewriteSrc4InsertRule;
import nc.bs.to.m5x.maintain.rule.insert.UserDefCheckRule;
import nc.bs.to.m5x.plugin.BPPlugInPoint;
import nc.bs.to.pub.rule.atp.TOATPAfterCheckRule;
import nc.bs.to.pub.rule.atp.TOATPBeforeCheckRule;
import nc.impl.pubapp.pattern.data.bill.template.InsertBPTemplate;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.plugin.IPluginPoint;
import nc.impl.pubapp.pattern.rule.processer.AroundProcesser;
import nc.vo.pubapp.pattern.model.entity.bill.IBill;
import nc.vo.scmpub.rule.StockOrgEnableCheckRule;
import nc.vo.to.m5x.entity.BillVO;

public class InsertBP {
	private void addAfterRule(AroundProcesser<BillVO> processer) {
		CheckUniqueBillCodeRule checkUniqueBillCodeRule = new CheckUniqueBillCodeRule();
		processer.addAfterRule((IRule) checkUniqueBillCodeRule);
		InsertTransferMsgRule insertTransferMsgRule = new InsertTransferMsgRule();
		processer.addAfterRule((IRule) insertTransferMsgRule);
		RewriteSrc4InsertRule rewriteSrc4InsertRule = new RewriteSrc4InsertRule();
		processer.addAfterRule((IRule) rewriteSrc4InsertRule);
		AutoReserveRule autoReserveRule = new AutoReserveRule();
		processer.addAfterRule((IRule) autoReserveRule);
		TOATPAfterCheckRule tOATPAfterCheckRule = new TOATPAfterCheckRule();
		processer.addAfterRule((IRule) tOATPAfterCheckRule);
	}

	private void addBeforeRule(AroundProcesser<BillVO> processer) {
		CodeImplRule codeImplRule = new CodeImplRule();
		processer.addBeforeRule((IRule) codeImplRule);
		FillDataRule fillDataRule = new FillDataRule();
		processer.addBeforeRule((IRule) fillDataRule);
		FillBillCodeRule fillBillCodeRule = new FillBillCodeRule();
		processer.addBeforeRule((IRule) fillBillCodeRule);
		CrossRuleValidateRule crossRuleValidateRule = new CrossRuleValidateRule();
		processer.addBeforeRule((IRule) crossRuleValidateRule);
		CheckBillRule checkBillRule = new CheckBillRule();
//		processer.addBeforeRule((IRule) checkBillRule);
		StockOrgEnableCheckRule stockOrgEnableCheckRule = new StockOrgEnableCheckRule();
		processer.addBeforeRule((IRule) stockOrgEnableCheckRule);
		TOATPBeforeCheckRule tOATPBeforeCheckRule = new TOATPBeforeCheckRule();
		processer.addBeforeRule((IRule) tOATPBeforeCheckRule);
		CheckCountryNotNullRule checkCountryNotNullRule = new CheckCountryNotNullRule();
		processer.addBeforeRule((IRule) checkCountryNotNullRule);
		CheckCountryRule checkCountryRule = new CheckCountryRule();
		processer.addBeforeRule((IRule) checkCountryRule);
		CheckScalesRule checkScalesRule = new CheckScalesRule();
		processer.addBeforeRule((IRule) checkScalesRule);
		UserDefCheckRule userDefCheckRule = new UserDefCheckRule();
		processer.addBeforeRule((IRule) userDefCheckRule);
		ProfitCenterCheckRule profitCenterCheckRule = new ProfitCenterCheckRule();
		processer.addBeforeFinalRule((IRule) profitCenterCheckRule);
		MedCheckPermitRule medCheckPermitRule = new MedCheckPermitRule();
		processer.addBeforeFinalRule((IRule) medCheckPermitRule);
		MedCheckBathAndLotnoRule medCheckBathAndLotnoRule = new MedCheckBathAndLotnoRule();
		processer.addBeforeFinalRule((IRule) medCheckBathAndLotnoRule);
	}

	public BillVO[] insert(BillVO[] bills) {
		InsertBPTemplate<BillVO> template = new InsertBPTemplate(
				(IPluginPoint) BPPlugInPoint.InsertBP);
		addBeforeRule(template.getAroundProcesser());
		addAfterRule(template.getAroundProcesser());
		return (BillVO[]) template.insert(bills);
	}
}
