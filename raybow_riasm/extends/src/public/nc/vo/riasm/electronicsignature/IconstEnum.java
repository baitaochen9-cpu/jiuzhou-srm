/** <a href="http://www.cpupk.com/decompiler">Eclipse Class Decompiler</a> plugin, Copyright (c) 2017 Chen Chao. */
package nc.vo.riasm.electronicsignature;
import java.awt.event.ActionEvent;

import nc.ui.trade.business.HYPubBO_Client;
import nc.uif.pub.exception.UifException;
import nc.vo.pub.lang.UFBoolean;

public enum IconstEnum {
	commits("commits", "提交"), approves("approves", "审核"), updates("updates",
			"修改"), uneables("uneables", "停用"), deletes("deletes", "删除"), confirm(
			"confirm", "确认"), save("save", "保存"), get("get", "领料"), ready(
			"ready", "备料"), frozen("frozen", "冻结"), traw("traw", "解冻"), generate(
			"generate", "生成条码");

	private final String code;
	private final String name;

	private IconstEnum(String code, String name) {
		this.code = code;
		this.name = name;
	}

	public static boolean isCheckElectronicSignature(
			ElectronicSignatureVO bill, String cmd) {
		if (bill == null)
			return false;
		
//		System.out.println(cmd);
		if("".equals(cmd)){			
			cmd = "保存";
		}
		boolean ischeck = false;
		// for (IconstEnum ienum : IconstEnum.values()) {
		// Object o = bill.getAttributeValue(ienum.getCode());
		// UFBoolean temp = UFBoolean.FALSE;
		// if (o == null) {
		// temp = UFBoolean.FALSE;
		// } else {
		// if (o instanceof UFBoolean) {
		// temp = (UFBoolean) o;
		// }
		// if (temp.booleanValue()) {
		// if (ienum.getName().equals(ae.getActionCommand())) {
		// ischeck = true;
		// break;
		// }
		// }
		// }
		// }

		try {
			ElectronicSignatureBVO[] vos = (ElectronicSignatureBVO[]) HYPubBO_Client
					.queryByCondition(
							ElectronicSignatureBVO.class,
							" nvl(dr,0)=0 and billid = '"
									+ bill.getPrimaryKey() + "'");
			if (vos == null || vos.length == 0)
				ischeck = false;
			for (ElectronicSignatureBVO vo : vos) {
				if (vo.getBtnname().equals(cmd)) {
					ischeck = true;
					break;
				}
			}
		} catch (UifException e) {
			e.printStackTrace();
			ischeck = false;
		}

		return ischeck;
	}

	public String getCode() {
		return code;
	}

	public String getName() {
		return name;
	}

}
