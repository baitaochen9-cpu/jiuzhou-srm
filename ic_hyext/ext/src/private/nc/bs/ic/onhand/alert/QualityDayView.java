package nc.bs.ic.onhand.alert;

import nc.bs.ic.pub.alert.AlertConst;
import nc.bs.ic.pub.alert.AlertConst.QualityAlertType;
import nc.bs.ic.pub.alert.SqlJoin;
import nc.bs.ml.NCLangResOnserver;
import nc.vo.bd.material.MaterialVO;
import nc.vo.bd.material.marbasclass.MarBasClassVO;
import nc.vo.bd.material.measdoc.MeasdocVO;
import nc.vo.bd.material.stock.MaterialStockVO;
import nc.vo.bd.stordoc.StordocVO;
import nc.vo.ic.onhand.entity.OnhandDimVO;
import nc.vo.ic.onhand.entity.OnhandNumVO;
import nc.vo.ic.pub.define.ICView;
import nc.vo.ic.pub.util.CollectionUtils;
import nc.vo.ic.pub.util.StringUtil;
import nc.vo.org.StockOrgVO;
import nc.vo.pub.JavaType;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scmf.ic.mbatchcode.BatchcodeVO;

/**
 * 保质期查询视图
 * 
 * @since 6.0
 * @version 2011-2-21 下午04:56:12
 * @author chennn
 */

public class QualityDayView extends ICView {

	private static final long serialVersionUID = 2637293767740921663L;

	// 库存组织名称
	public static final String ORGNAME = AlertConst.ORGNAME;

	// 仓库名称
	public static final String WHNAME = AlertConst.WHNAME;

	// 仓库编码
	public static final String WHCODE = AlertConst.WHCODE;

	// 物料分类编码
	public static final String INVCLASSCODE = AlertConst.INVCLASSCODE;

	// 物料分类名称
	public static final String INVCLASSNAME = AlertConst.INVCLASSNAME;

	// 物料的编码
	public static final String INVCODE = AlertConst.INVCODE;

	// 物料的名称
	public static final String INVNAME = AlertConst.INVNAME;

	// 物料的规格
	public static final String INVSPEC = AlertConst.INVSPEC;

	// 物料的型号
	public static final String INVTYPE = AlertConst.INVTYPE;

	// 保质期数量
	public static final String QUALITYNUM = MaterialStockVO.QUALITYNUM;

	public static final String QUALITYUNIT = MaterialStockVO.QUALITYUNIT;

	// 登录日期与保质期相差天数
	public static final String DAYS = AlertConst.DAYS;

	// 状态
	public static final String STATUS = AlertConst.STATUS;

	// 比例
	public static final String RATE = AlertConst.RATE;

	// 现存量维度pk
	public static final String ONHANDDIM = "onhanddim";

	// 内嵌现存量视图的别名
	public static final String ONHANDVIEW = "onhandview";

	// 物料主单位名称
	public static final String MAINMEASNAME = AlertConst.MAINMEASNAME;


	// 本视图的where条件
	private String qualityWhere = null;

	// 内嵌的现存量查询条件
	private String onhandWhere = null;

	// 登录日期
	private UFDate loginDate = null;

	private UFDouble ratio = null;
	
	private Integer advanceDay = 0; //20230216 zhian.ye 预警提前天数

	private QualityAlertType alertType = null;

	public QualityDayView(QualityAlertType type, UFDate loginDate) {
		this(type, loginDate, null, null, null);
	}

	public QualityDayView(QualityAlertType type, UFDate loginDate, UFDouble rate) {
		this(type, loginDate, rate, null, null);
	}
	
	/**
	 * 20230216 zhian.ye 增加构造初始化预警提前天数
	 * @param type 预警计算类型
	 * @param loginDate 登录日期
	 * @param days 
	 * @param alertType
	 */
	public QualityDayView (QualityAlertType type ,UFDate loginDate, Integer days ){
		this(type, loginDate, null, null, null,days);
	}
	public QualityDayView(QualityAlertType type, UFDate loginDate,
			UFDouble rate, String qwhere, String owhere, Integer advanceDay) {
		super();
		this.alertType = type;
		this.qualityWhere = qwhere;
		this.onhandWhere = owhere;
		this.loginDate = loginDate;
		this.ratio = rate;
		this.advanceDay = advanceDay;

	}

	public QualityDayView(QualityAlertType type, UFDate loginDate,
			UFDouble rate, String qwhere, String owhere) {
		super();
		this.alertType = type;
		this.qualityWhere = qwhere;
		this.onhandWhere = owhere;
		this.loginDate = loginDate;
		this.ratio = rate;

	}

	public void initView() {
		this.addViewFields();
		this.setWherePart();
	}

	private void setWherePart() {
		StringBuilder str = new StringBuilder();
		if (this.alertType.equals(QualityAlertType.Near)) {
			str.append(this.getDaysWhere(" > "));
			str.append(this.getRateWhere());
		} else if (this.alertType.equals(QualityAlertType.Due)) {
			str.append(this.getDaysWhere(" = "));
		} else if (this.alertType.equals(QualityAlertType.Over)) {
			str.append(this.getDaysWhere(" < "));
		}
		if (!StringUtil.isSEmptyOrNull(this.qualityWhere)) {
			str.append(" and " + this.qualityWhere);
		}
		this.setWhere(str.toString());
	}

	private String getRateWhere() {
		
		/*
		 * 20230216 zhian.ye 增加提前天数设置预警
		 */
		if(advanceDay != 0 ){
			return " and  datediff(dd,  '" + this.loginDate + "' , "
					+ QualityDayView.ONHANDVIEW + "." + "dvalidate )  <= "+ advanceDay;
		}else{
			
			return " and " + "((datediff(dd,  '" + this.loginDate + "' , "
					+ QualityDayView.ONHANDVIEW + "." + "dvalidate))  * 100/"
					+ " (qualitynum * ( CASE  WHEN qualityunit = 2 THEN 1"
					+ " WHEN qualityunit = 1 THEN 30"
					+ " WHEN qualityunit = 0 THEN 365 END  " + "))) < "
					+ this.ratio;
		}
	}

	private String getDaysWhere(String operation) {
		return this.getDaysExpress() + operation + " 0 ";
	}

	@Override
	public String getViewSql() {
		StringBuilder str = new StringBuilder(" select ");
		str.append(this.getSelectPart());
		str.append(" from " + this.getInnerView());
		str.append(this.getJoinPart());
		str.append(" where ");
		str.append(this.getWhere());
		return str.toString();

	}

	private void addViewFields() {
		// 库存组织
		final String orgTable = new StockOrgVO().getTableName();
		// 仓库
		final String warehouseTable = new StordocVO().getTableName();
		// 物料基本信息
		final String materialBasTable = new MaterialVO().getTableName();
		// 物料分类
		final String materialClassTable = new MarBasClassVO().getTableName();
		// 物料库存信息
		final String materialstockTable = new MaterialStockVO().getTableName();
		// 计量单位
		final String measdocTable = new MeasdocVO().getTableName();

		this.addViewField(orgTable, StockOrgVO.NAME, QualityDayView.ORGNAME,
				JavaType.String);
		this.addViewField(orgTable, StockOrgVO.NAME2, QualityDayView.ORGNAME
				+ AlertConst.TRASUFFIX, JavaType.String);
		this.addViewField(orgTable, StockOrgVO.NAME3, QualityDayView.ORGNAME
				+ AlertConst.ENGSUFFIX, JavaType.String);

		this.addViewField(warehouseTable, StordocVO.CODE,
				QualityDayView.WHCODE, JavaType.String);
		this.addViewField(warehouseTable, StordocVO.NAME,
				QualityDayView.WHNAME, JavaType.String);
		this.addViewField(warehouseTable, StordocVO.NAME2,
				QualityDayView.WHNAME + AlertConst.TRASUFFIX, JavaType.String);
		this.addViewField(warehouseTable, StordocVO.NAME3,
				QualityDayView.WHNAME + AlertConst.ENGSUFFIX, JavaType.String);

		this.addViewField(materialClassTable, MarBasClassVO.CODE,
				QualityDayView.INVCLASSCODE, JavaType.String);
		this.addViewField(materialClassTable, MarBasClassVO.NAME,
				QualityDayView.INVCLASSNAME, JavaType.String);
		this.addViewField(materialClassTable, MarBasClassVO.NAME2,
				QualityDayView.INVCLASSNAME + AlertConst.TRASUFFIX,
				JavaType.String);
		this.addViewField(materialClassTable, MarBasClassVO.NAME3,
				QualityDayView.INVCLASSNAME + AlertConst.ENGSUFFIX,
				JavaType.String);

		this.addViewField(materialBasTable, MaterialVO.CODE,
				QualityDayView.INVCODE, JavaType.String);
		this.addViewField(materialBasTable, MaterialVO.NAME,
				QualityDayView.INVNAME, JavaType.String);
		this.addViewField(materialBasTable, MaterialVO.NAME2,
				QualityDayView.INVNAME + AlertConst.TRASUFFIX, JavaType.String);
		this.addViewField(materialBasTable, MaterialVO.NAME3,
				QualityDayView.INVNAME + AlertConst.ENGSUFFIX, JavaType.String);
		// 物料规格、型号
		this.addViewField(materialBasTable, MaterialVO.MATERIALSPEC,
				QualityDayView.INVSPEC, JavaType.String);
		this.addViewField(materialBasTable, MaterialVO.MATERIALTYPE,
				QualityDayView.INVTYPE, JavaType.String);

		// 主单位
		this.addViewField(measdocTable, MeasdocVO.NAME,
				QualityDayView.MAINMEASNAME, JavaType.String);
		this.addViewField(measdocTable, MeasdocVO.NAME2,
				QualityDayView.MAINMEASNAME + AlertConst.TRASUFFIX,
				JavaType.String);
		this.addViewField(measdocTable, MeasdocVO.NAME3,
				QualityDayView.MAINMEASNAME + AlertConst.ENGSUFFIX,
				JavaType.String);

		this.addViewField(QualityDayView.ONHANDVIEW, OnhandDimVO.VBATCHCODE,
				OnhandDimVO.VBATCHCODE, JavaType.String);
		this.addViewField(QualityDayView.ONHANDVIEW, BatchcodeVO.DVALIDATE,
				BatchcodeVO.DVALIDATE, JavaType.String);
		this.addViewField(QualityDayView.ONHANDVIEW, OnhandNumVO.NONHANDNUM,
				OnhandNumVO.NONHANDNUM, JavaType.UFDouble);
		this.addViewField(QualityDayView.ONHANDVIEW, OnhandNumVO.NONHANDASTNUM,
				OnhandNumVO.NONHANDASTNUM, JavaType.UFDouble);
		this.addViewField(QualityDayView.ONHANDVIEW, OnhandDimVO.PK_ONHANDDIM,
				QualityDayView.ONHANDDIM, JavaType.String);
		this.addViewField(materialstockTable, MaterialStockVO.QUALITYNUM,
				QualityDayView.QUALITYNUM, JavaType.Integer);
		this.addViewField(materialstockTable, MaterialStockVO.QUALITYUNIT,
				QualityDayView.QUALITYUNIT, JavaType.Integer);
	}

	private String getInnerView() {
		QualityOnhandView view = new QualityOnhandView(this.onhandWhere);
		view.initView();
		String sql = view.getViewSql();
		return " ( " + sql + " ) " + QualityDayView.ONHANDVIEW;
	}

	private String getJoinPart() {
		SqlJoin tool = new SqlJoin();
		StringBuilder str = new StringBuilder();
		str.append(tool.innerJoinStockOrg(QualityDayView.ONHANDVIEW,
				OnhandDimVO.PK_ORG));
		str.append(tool.innerJoinWarehouse(QualityDayView.ONHANDVIEW,
				OnhandDimVO.CWAREHOUSEID));
		str.append(tool.innerJoinMaterialRelated(QualityDayView.ONHANDVIEW,
				OnhandDimVO.CMATERIALVID));
		final String materialBasTable = new MaterialVO().getTableName();
		str.append(tool.innerJoinMaterialConvert(materialBasTable,
				MaterialVO.PK_MATERIAL, MaterialVO.PK_MEASDOC));
		str.append(tool.innerJoinMaterialStock(QualityDayView.ONHANDVIEW,
				OnhandDimVO.CMATERIALVID, OnhandDimVO.PK_ORG));
		// 计量单位
		final String measdocTable = new MeasdocVO().getTableName();
		str.append(" inner join ");
		str.append(measdocTable + " on ");
		str.append(materialBasTable + "." + MaterialVO.PK_MEASDOC + " = "
				+ measdocTable + "." + MeasdocVO.PK_MEASDOC);

		return str.toString();

	}

	private String getSelectPart() {
		return this.getSelectFieldsPart() + this.getDays() + this.getStatus()
				+ this.getRate();
	}

	private String getStatus() {
		return ",(CASE WHEN datediff(dd,  '"
				+ this.loginDate
				+ "' , "
				+ QualityDayView.ONHANDVIEW
				+ "."
				+ "dvalidate) = 0 THEN '"
				+ NCLangResOnserver.getInstance().getStrByID("4008019_0",
						"04008019-0050")/* 到期 */
				+ "' WHEN datediff(dd, '"
				+ this.loginDate
				+ "' ,"
				+ QualityDayView.ONHANDVIEW
				+ "."
				+ "dvalidate) <= 0 THEN '"
				+ NCLangResOnserver.getInstance().getStrByID("4008019_0",
						"04008019-0051")/* 过期 */
				+ "' WHEN datediff(dd, '"
				+ this.loginDate
				+ "' , "
				+ QualityDayView.ONHANDVIEW
				+ "."
				+ "dvalidate) >= 0 THEN '"
				+ NCLangResOnserver.getInstance().getStrByID("4008019_0",
						"04008019-0052")/* 临近 */+ "' END) "
				+ QualityDayView.STATUS;
	}

	/**
	 * datediff
	 * 
	 * @return
	 */
	private String getDays() {
		return " , abs(" + this.getDaysExpress() + ") " + QualityDayView.DAYS;
	}

	private String getDaysExpress() {

		return " datediff(dd,  '" + this.loginDate + "' , "
				+ QualityDayView.ONHANDVIEW + "." + "dvalidate )  ";
	}

	private String getRate() {
		return ", CONVERT(varchar, cast(abs((datediff(dd,  '"
				+ this.loginDate
				+ "' , "
				+ QualityDayView.ONHANDVIEW
				+ "."
				+ "dvalidate))  * 100 / "
				+ " (qualitynum * ( CASE  WHEN qualityunit = 2 THEN 1"
				+ " WHEN qualityunit = 1 THEN 30 WHEN qualityunit = 0 THEN 365 END  "
				+ "))) as decimal(20,4)))|| '%' " + QualityDayView.RATE;
	}

	/**
	 * 视图的字段名
	 */
	@Override
	public String[] getViewFieldNames() {
		String[] fieldNames = super.getViewFieldNames();
		String[] addFields = { QualityDayView.DAYS, QualityDayView.STATUS,
				QualityDayView.RATE };
		return CollectionUtils.combineArrs(fieldNames, addFields);
	}

	/**
	 * 视图的字段类型
	 */
	@Override
	public JavaType[] getViewFieldTypes() {
		JavaType[] types = super.getViewFieldTypes();
		JavaType[] addTypes = { JavaType.Integer, JavaType.String,
				JavaType.String };
		return CollectionUtils.combineArrs(types, addTypes);
	}

	public String getQualityWhere() {
		return this.qualityWhere;
	}

	public void setQualityWhere(String qualityWhere) {
		this.qualityWhere = qualityWhere;
	}

	public String getOnhandWhere() {
		return this.onhandWhere;
	}

	public void setOnhandWhere(String onhandWhere) {
		this.onhandWhere = onhandWhere;
	}

}
