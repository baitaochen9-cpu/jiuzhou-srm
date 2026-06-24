package nc.vo.bd.ref;

import nc.ui.bd.ref.IRefConst;
import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.template.TemplateLayer;

/**
 * 
 * <p>
 * <strong>提供者：</strong>UAP
 * <p>
 * <strong>使用者：</strong>
 * 
 * <p>
 * <strong>设计状态：</strong>详细设计
 * <p>
 * 
 * @version NC5.0
 * @author sxj
 */
public class RefInfoVO extends SuperVO {

	/**
	 * <code>serialVersionUID</code> 的注释
	 */
	private static final long serialVersionUID = 1L;

	private String pk_refinfo = null;

	private String code = null;

	private String name = null;

	private String residPath = null;

	private String resid = null;

	private String moduleName = null;

	private String refclass = null;

	private UFBoolean isspecialref = null;

	private UFBoolean isneedpara = null;

	private Integer refType = IRefConst.GRID;

	private String para1 = null;

	private String para2 = null;

	private String para3 = null;

	private String refsystem = null;

	private String reserv1 = null;

	private String reserv2 = null;

	private String reserv3 = null;
	
	private String refpath = null;
	
	private String mobilerefpath = null;
	
	private String isTreelazyLoad = null;
	
	private String metadataTypeName = null;
	
	private String wherePart = null;
	
	private int layer = TemplateLayer.PLATFORM;
	
	private java.lang.Integer dr = 0;
	
	private String metadatanamespace = null;
	
	public String getMetadatanamespace() {
		return metadatanamespace;
	}

	public void setMetadatanamespace(String metadatanamespace) {
		this.metadatanamespace = metadatanamespace;
	}

	public java.lang.Integer getDr() {
		return dr;
	}

	public void setDr(java.lang.Integer dr) {
		this.dr = dr;
	}

	public nc.vo.pub.lang.UFDateTime getTs() {
		return ts;
	}

	public void setTs(nc.vo.pub.lang.UFDateTime ts) {
		this.ts = ts;
	}

	private nc.vo.pub.lang.UFDateTime ts;

	public String getWherePart() {
		return wherePart;
	}

	public void setWherePart(String wherePart) {
		this.wherePart = wherePart;
	}

	@Override
	public String getPKFieldName() {
		// TODO 自动生成方法存根
		return "pk_refinfo";
	}

	@Override
	public String getParentPKFieldName() {
		// TODO 自动生成方法存根
		return null;
	}

	@Override
	public String getTableName() {
		// TODO 自动生成方法存根
		return "bd_refinfo";
	}

	/**
	 * @return 返回 reserv3。
	 */
	public String getReserv3() {
		return reserv3;
	}

	/**
	 * @param reserv3
	 *            要设置的 reserv3。
	 */
	public void setReserv3(String reserv3) {
		this.reserv3 = reserv3;
	}

	/**
	 * @return 返回 code。
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code
	 *            要设置的 code。
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * @return 返回 isneedpara。
	 */
	public UFBoolean getIsneedpara() {
		return isneedpara;
	}

	/**
	 * @param isneedpara
	 *            要设置的 isneedpara。
	 */
	public void setIsneedpara(UFBoolean isneedpara) {
		this.isneedpara = isneedpara;
	}

	/**
	 * @return 返回 isspecialref。
	 */
	public UFBoolean getIsspecialref() {
		return isspecialref;
	}

	/**
	 * @param isspecialref
	 *            要设置的 isspecialref。
	 */
	public void setIsspecialref(UFBoolean isspecialref) {
		this.isspecialref = isspecialref;
	}

	/**
	 * @return 返回 module。
	 */
	public String getModuleName() {
		return moduleName;
	}

	/**
	 * @param module
	 *            要设置的 module。
	 */
	public void setModuleName(String module) {
		this.moduleName = module;
	}

	/**
	 * @return 返回 name。
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            要设置的 name。
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return 返回 para1。
	 */
	public String getPara1() {
		return para1;
	}

	/**
	 * @param para1
	 *            要设置的 para1。
	 */
	public void setPara1(String para1) {
		this.para1 = para1;
	}

	/**
	 * @return 返回 para2。
	 */
	public String getPara2() {
		return para2;
	}

	/**
	 * @param para2
	 *            要设置的 para2。
	 */
	public void setPara2(String para2) {
		this.para2 = para2;
	}

	/**
	 * @return 返回 para3。
	 */
	public String getPara3() {
		return para3;
	}

	/**
	 * @param para3
	 *            要设置的 para3。
	 */
	public void setPara3(String para3) {
		this.para3 = para3;
	}

	/**
	 * @return 返回 pk_refinfo。
	 */
	public String getPk_refinfo() {
		return pk_refinfo;
	}

	/**
	 * @param pk_refinfo
	 *            要设置的 pk_refinfo。
	 */
	public void setPk_refinfo(String pk_refinfo) {
		this.pk_refinfo = pk_refinfo;
	}

	/**
	 * @return 返回 refclass。
	 */
	public String getRefclass() {
		return refclass;
	}

	/**
	 * @param refclass
	 *            要设置的 refclass。
	 */
	public void setRefclass(String refclass) {
		this.refclass = refclass;
	}

	/**
	 * @return 返回 refsystem。
	 */
	public String getRefsystem() {
		return refsystem;
	}

	/**
	 * @param refsystem
	 *            要设置的 refsystem。
	 */
	public void setRefsystem(String refsystem) {
		this.refsystem = refsystem;
	}

	/**
	 * @return 返回 reserv1。
	 */
	public String getReserv1() {
		return reserv1;
	}

	/**
	 * @param reserv1
	 *            要设置的 reserv1。
	 */
	public void setReserv1(String reserv1) {
		this.reserv1 = reserv1;
	}

	/**
	 * @return 返回 reserv2。
	 */
	public String getReserv2() {
		return reserv2;
	}

	/**
	 * @param reserv2
	 *            要设置的 reserv2。
	 */
	public void setReserv2(String reserv2) {
		this.reserv2 = reserv2;
	}

	/**
	 * @return 返回 resid。
	 */
	public String getResid() {
		return resid;
	}

	/**
	 * @param resid
	 *            要设置的 resid。
	 */
	public void setResid(String resid) {
		this.resid = resid;
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		// TODO 自动生成方法存根
		String mName = null;
		if(getResidPath() != null)
			mName = NCLangRes4VoTransl.getNCLangRes().getStrByID(getResidPath(), getResid());
		
		return mName==null?getName():mName;
		
		
	}

	/**
	 * @return 返回 residPath。
	 */
	public String getResidPath() {
		return residPath;
	}

	/**
	 * @param residPath
	 *            要设置的 residPath。
	 */
	public void setResidPath(String residPath) {
		this.residPath = residPath;
	}

	/**
	 * @return 返回 refType。
	 */
	public Integer getRefType() {
		return refType;
	}

	/**
	 * @param refType
	 *            要设置的 refType。
	 */
	public void setRefType(Integer refType) {
		this.refType = refType;
	}

	public String getMetadataTypeName() {
		return metadataTypeName;
	}

	public void setMetadataTypeName(String metadataTypeName) {
		this.metadataTypeName = metadataTypeName;
	}

	public int getLayer() {
		return layer;
	}

	public void setLayer(int layer) {
		this.layer = layer;
	}

	public String getRefpath() {
		return refpath;
	}

	public void setRefpath(String refpath) {
		this.refpath = refpath;
	}

	public String getIsTreelazyLoad() {
		return isTreelazyLoad;
	}

	public void setIsTreelazyLoad(String isTreelazyLoad) {
		this.isTreelazyLoad = isTreelazyLoad;
	}

	public String getMobilerefpath() {
		return mobilerefpath;
	}

	public void setMobilerefpath(String mobilerefpath) {
		this.mobilerefpath = mobilerefpath;
	}
}
