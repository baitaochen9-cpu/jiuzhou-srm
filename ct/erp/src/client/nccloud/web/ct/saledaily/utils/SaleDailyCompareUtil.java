package nccloud.web.ct.saledaily.utils;

import java.util.HashMap;
import java.util.Map;

import nc.vo.ct.saledaily.entity.CtSaleExecVO;
import nccloud.framework.web.ui.pattern.extbillcard.ExtBillCard;
import nccloud.framework.web.ui.pattern.grid.Grid;
import nccloud.web.scmpub.pub.operator.SCMExtBillCardOperator;
import nccloud.web.scmpub.pub.utils.compare.SCMCompareUtil;

/**
 * @description 销售合同对比
 * @author wangshrc
 * @date 2019年1月23日 下午4:09:50
 * @version ncc1.0
 */
public class SaleDailyCompareUtil {
	public static final HashMap<String, String> bodyPkFields = new HashMap<String, String>();
	static {
		bodyPkFields.put("saledaily_exec", CtSaleExecVO.PK_CT_SALE_EXEC);
	}

	public static SCMExtBillCardOperator getBillCardOperator() {
		// 转换为前台结构
		SCMExtBillCardOperator operator = new SCMExtBillCardOperator("400600200_card");
		return operator;
	}

	public static ExtBillCard operator(SCMExtBillCardOperator operator, Object bill, Object origBill) {
		// 转换成前端视图
		ExtBillCard orignCard = operator.toNoTransCard(origBill);
		ExtBillCard billCard = operator.toNoTransCard(bill);
		// 精度处理
		SaleDailyPrecisionUtil.dealPrecision(orignCard);
		SaleDailyPrecisionUtil.dealPrecision(billCard);
		// 差异更新,bodyPkFields中只有执行过程子表为后端增行，需变rowid为主键更新前端页面
	  compareExtBillCardByFields(orignCard, billCard, bodyPkFields);
//		billCard = GridCompareUtils.compareExtBillCardGrid(orignCard, billCard);
		operator.translate(billCard);

		return billCard;
	}
	
	public static void compareExtBillCardByFields(ExtBillCard client, ExtBillCard server, Map<String, String> bodyPkFields) {
		if (client == null || server == null) {
			return;
		}
		// 比较表体
		Grid[] allServerBodys = server.getAllBodys();
		for (Grid serverGrid : allServerBodys) {
			String areacode = serverGrid.getModel().getAreacode();
			String bodyPkField = bodyPkFields.get(areacode);
			Grid clientGrid = client.getBody(areacode);
			if (clientGrid != null) {
				SCMCompareUtil.compareGrid(clientGrid, serverGrid, bodyPkField);
			}
		}
	}
}
