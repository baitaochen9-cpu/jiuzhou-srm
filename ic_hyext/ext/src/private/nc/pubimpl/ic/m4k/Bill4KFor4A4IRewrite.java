package nc.pubimpl.ic.m4k;

import nc.bs.ic.pub.db.ICDBVisitor;
import nc.bs.ic.pub.db.SqlIn;
import nc.bs.ic.pub.rewrite.RewriteBPTemplate;
import nc.cmp.bill.util.SysInit;
import nc.impl.obm.pattern.data.bill.BillQuery;
import nc.impl.pubapp.pattern.data.vo.VOQuery;
import nc.impl.pubapp.pattern.data.vo.VOUpdate;
import nc.vo.ic.m4k.entity.WhsTransBillBodyVO;
import nc.vo.ic.m4k.entity.WhsTransBillHeaderVO;
import nc.vo.ic.m4k.entity.WhsTransBillVO;
import nc.vo.ic.pub.define.ICPubMetaNameConst;
import nc.vo.ic.pub.define.ViewField;
import nc.vo.ic.pub.sql.AttributeResultSet;
import nc.vo.ic.pub.sql.ICQueryView;
import nc.vo.ic.pub.util.NCBaseTypeUtils;
import nc.vo.ic.pub.util.VOEntityUtil;
import nc.vo.ic.special.define.SpecialMetaNameConst;
import nc.vo.pub.BusinessException;
import nc.vo.pub.JavaType;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.AppContext;
import nc.vo.pubapp.pattern.pub.AssertUtils;

/**
 * <p>
 * <b>其它入库单回写转库单：</b>
 * <ul>
 * <li>
 * </ul>
 * <p>
 * <p>
 *
 * @version 6.0
 * @since 6.0
 * @author yangb
 * @time 2010-9-16 下午10:44:18
 */
public class Bill4KFor4A4IRewrite extends RewriteBPTemplate<WhsTransBillBodyVO> {

  private String cadjuster;
  private  boolean isIn;

  /**
   * Bill4KFor4A4IRewrite 的构造子
   */
  public Bill4KFor4A4IRewrite(
      String vadjuster, boolean isIn) {
    super(WhsTransBillBodyVO.class);
    this.cadjuster = vadjuster;
    this.isIn = isIn;
    this.additionalFields(new String[] {
      SpecialMetaNameConst.CSPECIALHID, SpecialMetaNameConst.CSPECIALBID,
      ICPubMetaNameConst.CROWNO, WhsTransBillBodyVO.NGROSSNUM,
      ICPubMetaNameConst.NNUM,
    });
    if (isIn) {
      this.addUpdateField(WhsTransBillBodyVO.NTRANSINNUM);
      this.addUpdateField(WhsTransBillBodyVO.NTRANSINGROSSNUM);
      this.additionalFields(new String[] {
        WhsTransBillBodyVO.NTRANSOUTNUM, WhsTransBillBodyVO.NTRANSOUTGROSSNUM,
      });
    }
    else {
      this.addUpdateField(WhsTransBillBodyVO.NTRANSOUTNUM);
      this.addUpdateField(WhsTransBillBodyVO.NTRANSOUTGROSSNUM);
      this.additionalFields(new String[] {
        WhsTransBillBodyVO.NTRANSINNUM, WhsTransBillBodyVO.NTRANSINGROSSNUM,
      });
    }

  }

  protected void checkIn(WhsTransBillBodyVO vo) throws BusinessException {
    UFDouble dvalue = null;
    dvalue =
        NCBaseTypeUtils.sub(NCBaseTypeUtils.abs(vo.getNtransoutnum()),
            NCBaseTypeUtils.abs(vo.getNtransinnum()));
    if (NCBaseTypeUtils.isLtZero(dvalue)) {
      throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("4008010_0","04008010-0008")/*@res "累计转入数量超过累计转出数量"*/);
    }
    dvalue = NCBaseTypeUtils.mult(vo.getNtransoutnum(), vo.getNtransinnum());
    if (NCBaseTypeUtils.isLtZero(dvalue)) {
      throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("4008010_0","04008010-0009")/*@res "实际转出数量与实际转入数量不能方向相反"*/);
    }
    dvalue =
        NCBaseTypeUtils.sub(NCBaseTypeUtils.abs(vo.getNtransoutgrossnum()),
            NCBaseTypeUtils.abs(vo.getNtransingrossnum()));
    if (NCBaseTypeUtils.isLtZero(dvalue)) {
      throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("4008010_0","04008010-0010")/*@res "累计转入毛重超过累计转出毛重"*/);
    }
    dvalue =
        NCBaseTypeUtils.mult(vo.getNtransoutgrossnum(), vo
            .getNtransingrossnum());
    if (NCBaseTypeUtils.isLtZero(dvalue)) {
      throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("4008010_0","04008010-0011")/*@res "实际转出毛重与实际转入毛重不能方向相反"*/);
    }
  }

  protected void checkOut(WhsTransBillBodyVO vo) throws BusinessException {
    UFDouble dvalue = null;
    dvalue =
        NCBaseTypeUtils.sub(NCBaseTypeUtils.abs(vo.getNnum()), NCBaseTypeUtils
            .abs(vo.getNtransoutnum()));
    
    
    // 20230207 yezhian 中山项目增加支持超出转库单进行出库  组织参数控制【IC133】
    VOQuery<WhsTransBillHeaderVO > voquery = new VOQuery<>(WhsTransBillHeaderVO .class);
    WhsTransBillHeaderVO[] query = voquery.query(new String[]{vo.getCspecialhid()});
    UFBoolean b = SysInit.getParaBoolean(query[0].getPk_org(), "IC133");
    if (NCBaseTypeUtils.isLtZero(dvalue) && !b.booleanValue()) {
      throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("4008010_0","04008010-0012")/*@res "累计转出数量超过转库单数量"*/);
    }
    dvalue = NCBaseTypeUtils.mult(vo.getNnum(), vo.getNtransoutnum());
    if (NCBaseTypeUtils.isLtZero(dvalue)) {
      throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("4008010_0","04008010-0013")/*@res "实际转出数量与转库单数量不能方向相反"*/);
    }
    dvalue =
        NCBaseTypeUtils.sub(NCBaseTypeUtils.abs(vo.getNgrossnum()),
            NCBaseTypeUtils.abs(vo.getNtransoutgrossnum()));
    if (NCBaseTypeUtils.isLtZero(dvalue)) {
      throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("4008010_0","04008010-0014")/*@res "累计转出毛重超过转库单毛重"*/);
    }
    dvalue = NCBaseTypeUtils.mult(vo.getNgrossnum(), vo.getNtransoutnum());
    if (NCBaseTypeUtils.isLtZero(dvalue)) {
      throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("4008010_0","04008010-0015")/*@res "实际转出毛重与转库单毛重不能方向相反"*/);
    }
  }

  @Override
  protected void reWriteAfterProcess(WhsTransBillBodyVO[] vos)
      throws BusinessException {
    super.reWriteAfterProcess(vos);
    for (int i = 0; i < vos.length; i++) {
      this.checkOut(vos[i]);
      this.checkIn(vos[i]);
    }
    // 更新表头调整人
    this.reWriteHead(vos);
  }

  /**
   * 方法功能描述：更新表头调整人
   * <p>
   * <b>参数说明</b>
   *
   * @param vos
   *          <p>
   * @since 6.0
   * @author yangb
   * @time 2010-9-17 上午12:07:05
   */
  protected void reWriteHead(WhsTransBillBodyVO[] vos) {
    ICDBVisitor db = new ICDBVisitor();
    String table =
        vos[0].getMetaData().getPrimaryAttribute().getColumn().getTable()
            .getName();
    ICQueryView view = new ICQueryView(" from " + table + " where dr=0 ");
    view.addWherePart(SqlIn.formInSQL(SpecialMetaNameConst.CSPECIALHID,
        VOEntityUtil
            .getVOsNotRepeatValue(vos, SpecialMetaNameConst.CSPECIALHID)));
    view.addViewField(table, SpecialMetaNameConst.CSPECIALHID,
        SpecialMetaNameConst.CSPECIALHID, JavaType.String);
    view.setViewSumFields(new ViewField[] {
      new ViewField(WhsTransBillBodyVO.NTRANSINNUM, "sum("
          + WhsTransBillBodyVO.NTRANSINNUM + ")", JavaType.UFDouble),
      new ViewField(WhsTransBillBodyVO.NTRANSOUTNUM, "sum("
          + WhsTransBillBodyVO.NTRANSOUTNUM + ")", JavaType.UFDouble),
    });

    AttributeResultSet dataset = db.query(view, null);
    AssertUtils.assertValue(
        dataset != null && dataset.getRowCount() > 0, nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("4008010_0","04008010-0016")/*@res "转库单表体数据错误"*/);
    if (dataset == null) {
      return;
    }
    WhsTransBillHeaderVO[] heads =
        new WhsTransBillHeaderVO[dataset.getRowCount()];
    for (int i = 0; i < dataset.getRowCount(); i++) {
      heads[i] = new WhsTransBillHeaderVO();
      heads[i].setCspecialhid((String) dataset.getAttributeValue(i,
          SpecialMetaNameConst.CSPECIALHID));
      // if (NCBaseTypeUtils.isNullOrZero((UFDouble)
      // dataset.getAttributeValue(i,
      // WhsTransBillBodyVO.NTRANSINNUM))
      // && NCBaseTypeUtils.isNullOrZero((UFDouble) dataset.getAttributeValue(
      // i, WhsTransBillBodyVO.NTRANSOUTNUM))) {
      // heads[i].setVadjuster(null);
      // heads[i].setCauditorid(null);
      // }
      // else {
      if (this.isIn) {
        if (NCBaseTypeUtils.isNullOrZero((UFDouble) dataset.getAttributeValue(
            i, WhsTransBillBodyVO.NTRANSINNUM))) {
          heads[i].setVadjuster(null);
        }
        else {
          heads[i].setVadjuster(this.cadjuster);
        }
      }
      else {
        if (NCBaseTypeUtils.isNullOrZero((UFDouble) dataset.getAttributeValue(
            i, WhsTransBillBodyVO.NTRANSOUTNUM))) {
          heads[i].setCauditorid(null);
        }
        else {
          heads[i].setCauditorid(this.cadjuster);
        }
      }

      // }
    }
    String updateField =WhsTransBillHeaderVO.CAUDITORID;
    if(this.isIn){
      updateField =WhsTransBillHeaderVO.VADJUSTER;
    }
    VOUpdate<WhsTransBillHeaderVO> voupdate =
        new VOUpdate<WhsTransBillHeaderVO>();
    voupdate.update(heads, new String[] {
        updateField
    });
  }
}