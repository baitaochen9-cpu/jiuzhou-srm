package nc.bs.riasm.electronicsignature.ace.bp.rule;

import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.pub.IAttributeMeta;
import nc.vo.pub.ISuperVO;
import nc.vo.pub.VOStatus;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.pubapp.pattern.model.entity.bill.IBill;

public class VONotNullCheck {
	public static String aggvodataNotNullInfo(IBill[] obj, String[] names) {
		for (IBill item : obj) {
			ISuperVO supervo = item.getParent();
			ISuperVO[] supervos = new ISuperVO[1];
			supervos[0] = supervo;
			String message = dataNotNullInfo(supervos, names);
			if (message != null) {
				return message;
			}
		}

		return null;
	}

	public static String aggvodataNotNullInfo(IBill obj, String[] names) {
		ISuperVO supervo = obj.getParent();
		ISuperVO[] supervos = new ISuperVO[1];
		supervos[0] = supervo;
		String message = dataNotNullInfo(supervos, names);
		if (message != null) {
			return message;
		}
		return null;
	}

	public static String vodataNotNullInfo(IBill[] obj,
			Class<? extends ISuperVO>[] clazz, String[][] names) {
		if (clazz.length == names.length) {
			for (IBill item : obj) {
				for (int i = 0; i < clazz.length; i++) {
					ISuperVO[] tbody = item.getChildren(clazz[i]);
					String message = dataNotNullInfo(tbody, names[i]);
					if (message != null) {
						return message;
					}
				}
			}
		}

		return null;
	}

	public static String vodataNotNullInfo(IBill obj,
			Class<? extends ISuperVO>[] clazz, String[][] names) {
		try {
			for (int i = 0; i < clazz.length; i++) {
				ISuperVO[] tbody = obj.getChildren(clazz[i]);
				String message = dataNotNullInfo(tbody, names[i]);
				if (message != null) {
					return message;
				}
			}
		} catch (Exception e) {
			ExceptionUtils.wrappException(e);
		}

		return null;
	}

	public static String dataNotNullInfo(ISuperVO[] obj, String[] names) {
		for (ISuperVO supervo : obj) {

			if (supervo.getStatus() == VOStatus.DELETED)
				continue;
			StringBuffer message = null;

			for (int i = 0; i < names.length; i++) {
				if (isNULL(supervo.getAttributeValue(names[i]))) {
					if (message == null) {
						message = new StringBuffer();
						message.append(supervo.getMetaData().getLabel());
					}
					IAttributeMeta attributemeta = supervo.getMetaData()
							.getAttribute(names[i]);

					message.append("[");
					message.append(attributemeta.getColumn().getLabel());
					message.append("]");
					message.append(",");
				}
			}

			if (message != null) {
				message.deleteCharAt(message.length() - 1).append("\n");
				return message.toString()
						+ NCLangRes4VoTransl.getNCLangRes().getStrByID(
								"smcomm", "UPP1005-000239");
			}
		}

		return null;
	}

	private static boolean isNULL(Object o) {
		if ((o == null) || (o.toString().trim().equals(""))) {
			return true;
		}
		return false;
	}
}