
package nc.vo.bd.payment;

public abstract interface IPaymentUtil {
	public static final int NUlll_EFFECT_MONTH = -1;
	public static final int EFFECT_CURRENT_MONTH = 0;
	public static final int EFFECT_NEXT_MONTH = 1;
	public static final String OUT_STORE_DATE = "1001Z01000000000E4JX";
	public static final String OUTSTORE_SIGNATURE_DATE = "1001Z01000000000E4JY";
	public static final String SALE_MAKE_BILL_DATE = "1001Z01000000000E4JZ";
	public static final String SALE_INVOICE_APPROVE_DATE = "1001Z01000000000E4K0";
	public static final String PURCHASE_CONTRACT_EFFECTIVE_DATE = "1001Z01000000000F04J";
	public static final String PURCHASE_ORDER_APPROVE_DATE = "1001Z01000000000F04K";
	public static final String PURCHASE_INVOICE_DATE = "1001Z01000000000F04L";
	public static final String RECEIPT_APPROVE_DATE = "1001Z01000000000F04N";
	public static final String STORE_RECEIPT_DATE = "1001Z01000000000F04O";
	public static final String STORE_RECEIPT_SIGNATURE_DATE = "1001Z01000000000F04P";
	public static final String PURCHASE_INVOICE_APPROVE_DATE = "1001Z01000000000F04M";
	public static final String SALE_ORDER_DATE = "1001Z01000000000E4K1";
	public static final String SALE_CONTRACT_EFFECTIVE_DATE = "1001Z01000000000E4K2";
	public static final String PROJECT_CONTRACT_EFFECTIVE_DATE = "1001Z01000000000F04Q";
	public static final String PROJECT_PACT_BAIL_APPROVE_DATE = "1001Z01000000000F04R";
	public static final String PAYMENTCH = "paymentch";
	//20220701 yezhian 增加采购发票到票日期
	public static final String ARRIVAL_INVOICE_DATE = "1001A3100000000DBNRH";
}