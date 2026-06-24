package nc.vo.material.mdm;

import nc.vo.bd.material.MaterialVO;

public class IMaterialVO extends MaterialVO{
	
		public static final String MDMCODE = "mdmcode";//主数据编码
		public static final String GROUPMATERIALCLASS = "groupmaterialclass";//集团统一物料分类ID
		public static final String GROUPCLASSCODE = "groupclasscode";//集团统一物料分类编码
		public static final String GROUPCLASSNAME = "groupclassname";//集团统一物料分类名称
		public static final String INVCLASSID = "pk_marasstframe";//物料基本分类ID
		public static final String INVCLASSIDCODE = "invclassidcode"; //物料基本分类编码
		public static final String INVCLASSIDNAME = "invclassidname"; //物料基本分类名称
		public static final String INVNAME = "invname" ;//通用名
		public static final String  MODEL = "model";//型号
		public static final String SPEC = "spec";//规格
		public static final String ORGCODE = "orgcode";//组织编码
		public static final String ORGNAME = "orgname";//组织名称
		public static final String UNITCODE = "unitcode";//计量单位编码
		public static final String UNITNAME = "unitname";//计量单位名称
		public static final String MATERIALMNECODE ="materialmnecode";//助记码
				
		private String mdmcode ;
		private String groupmaterialclass;
		private String groupclasscode;
		private String groupclassname;
		private String pk_marasstframe;
		private String invclassidcode;
		private String invclassidname;
		private String invname;
		private String model;
		private String spec;
		private String orgcode;
		private String orgname;
		private String unitcode;
		private String unitname;
		private String materialmnecode;
		
		

		
		public String getMaterialmnecode() {
			return materialmnecode;
		}
		public void setMaterialmnecode(String materialmnecode) {
			this.materialmnecode = materialmnecode;
		}
		public String getGroupclasscode() {
			return groupclasscode;
		}
		public void setGroupclasscode(String groupclasscode) {
			this.groupclasscode = groupclasscode;
		}
		public String getGroupclassname() {
			return groupclassname;
		}
		public void setGroupclassname(String groupclassname) {
			this.groupclassname = groupclassname;
		}
		public String getPk_marasstframe() {
			return pk_marasstframe;
		}
		public void setPk_marasstframe(String pk_marasstframe) {
			this.pk_marasstframe = pk_marasstframe;
		}
		public String getInvclassidcode() {
			return invclassidcode;
		}
		public void setInvclassidcode(String invclassidcode) {
			this.invclassidcode = invclassidcode;
		}
		public String getInvclassidname() {
			return invclassidname;
		}
		public void setInvclassidname(String invclassidname) {
			this.invclassidname = invclassidname;
		}
		public String getInvname() {
			return invname;
		}
		public void setInvname(String invname) {
			this.invname = invname;
		}
		public String getModel() {
			return model;
		}
		public void setModel(String model) {
			this.model = model;
		}
		public String getSpec() {
			return spec;
		}
		public void setSpec(String spec) {
			this.spec = spec;
		}
		public String getOrgcode() {
			return orgcode;
		}
		public void setOrgcode(String orgcode) {
			this.orgcode = orgcode;
		}
		public String getOrgname() {
			return orgname;
		}
		public void setOrgname(String orgname) {
			this.orgname = orgname;
		}
		public String getUnitcode() {
			return unitcode;
		}
		public void setUnitcode(String unitcode) {
			this.unitcode = unitcode;
		}
		public String getUnitname() {
			return unitname;
		}
		public void setUnitname(String unitname) {
			this.unitname = unitname;
		}

		public String getMdmcode() {
			return mdmcode;
		}
		public void setMdmcode(String mdmcode) {
			this.mdmcode = mdmcode;
		}
		public String getGroupmaterialclass() {
			return groupmaterialclass;
		}
		public void setGroupmaterialclass(String groupmaterialclass) {
			this.groupmaterialclass = groupmaterialclass;
		}
		
}
