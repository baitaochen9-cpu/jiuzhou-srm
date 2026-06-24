package nc.ui.qc.supplierqualitystatus.billref;

import nc.ui.bd.ref.AbstractRefModel;
import nc.vo.bd.supplier.suppliergradesys.SupplierGradeVO;

public class SupplierGradeRefModel extends AbstractRefModel {

	public SupplierGradeRefModel() {
		super();
		this.reset();
	}

	//SupplierGradeRefModel
	@Override
	public void reset() {
		this.setFieldCode(new String[] { SupplierGradeVO.ISDEFAULT,
				SupplierGradeVO.SUPPLIERGRADE, SupplierGradeVO.SUPSTATUS,
				SupplierGradeVO.GRADEEXP });
		this.setFieldName(new String[] {
				nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
						"10140sgrade", "010140sgrade-0008"),
				nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
						"10140sgrade", "010140sgrade-0005"),
				nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
						"10140sgrade", "010140sgrade-0006"),
				nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
						"10140sgrade", "010140sgrade-0007")
		// "等级", "状态", "说明", "是否默认"
		});
		this.setRefNameField(SupplierGradeVO.SUPPLIERGRADE);
		this.setRefCodeField(SupplierGradeVO.SUPPLIERGRADE);

		// 默认显示列数
		this.setDefaultFieldCount(4);
		this.setHiddenFieldCode(new String[] { SupplierGradeVO.PK_GRADE_INFO });
		this.setTableName(SupplierGradeVO.getDefaultTableName());

		// 返回主键
		this.setPkFieldCode(SupplierGradeVO.PK_GRADE_INFO);
		this.setOrderPart(" isdefault ");
		this.setRefTitle("等级状态参照");// 参照名称
		this.resetFieldName();
	}
}
