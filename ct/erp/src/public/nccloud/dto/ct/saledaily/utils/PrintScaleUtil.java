package nccloud.dto.ct.saledaily.utils;

import nc.vo.ct.ap.entity.CtApBVO;
import nc.vo.ct.ap.entity.CtApVO;
import nc.vo.ct.entity.CtAbstractBVO;
import nc.vo.ct.entity.CtAbstractVO;
import nc.vo.ct.pub.CTVatNameConst;
import nc.vo.pubapp.scale.BillScaleProcessor;
import nc.vo.pubapp.scale.FieldInfo;
import nc.vo.pubapp.scale.PosEnum;
import nc.vo.pubapp.scale.TotalValueScale;

/**
 * 打印精度处理类
 * 
 * @since 6.0
 * @version 2010-11-6 下午01:57:17
 * @author wangfengd
 */
public class PrintScaleUtil {
  // 数量
  String[] assistNumkeys = new String[] {
    CtAbstractBVO.NASTNUM
  };

  // 换算率
  String[] changeRates = new String[] {
    CtAbstractBVO.VCHANGERATE, CtAbstractBVO.VQTUNITRATE
  };

  // 表头本币金额
  String[] headmnykeys = new String[] {
    CtAbstractVO.NPREPAYLIMITMNY, CtAbstractVO.NTOTALGPAMOUNT,
    CtAbstractVO.NTOTALTAXMNY, CtApVO.NTOTALCOPAMOUNT, CtApVO.NPREPAYMNY
  };

  // 表头原币金额
  String[] headorgmnykeys = new String[] {
    CtAbstractVO.NORIPREPAYLIMITMNY, CtAbstractVO.NORIGPSHAMOUNT,
    CtApVO.NORIGCOPAMOUNT, CtApVO.NORIPREPAYMNY, CtAbstractVO.NTOTALORIGMNY
  };

  // 本币金额
  String[] mnykeys = new String[] {
    CtAbstractBVO.NMNY, CtAbstractBVO.NTAXMNY, CtAbstractBVO.NTAX,
    CtAbstractBVO.NTOTALGPMNY, CTVatNameConst.NNOSUBTAX,
    CTVatNameConst.NCALTAXMNY, CTVatNameConst.NCALCOSTMNY
  };

  // 税率
  String[] ntaxratekeys = new String[] {
    CtAbstractBVO.NTAXRATE, CTVatNameConst.NNOSUBTAXRATE
  };

  // 主数量
  String[] numkeys = new String[] {
    CtAbstractBVO.NNUM, CtAbstractBVO.NORDNUM,

  };

  // 原币金额
  String[] orgmnykeys = new String[] {
    CtAbstractBVO.NORIGMNY, CtAbstractBVO.NORIGTAXMNY, CtAbstractBVO.NORDSUM,
    CtAbstractBVO.NORITOTALGPMNY, CtApBVO.NORICOPEGPMNY
  };

  // 单价
  // 原币
  String[] origpricekeys = new String[] {
    CtAbstractBVO.NQTORIGPRICE, CtAbstractBVO.NQTORIGTAXPRICE,
    CtAbstractBVO.NORIGPRICE, CtAbstractBVO.NORIGTAXPRICE,
  };

  // 本币
  String[] pricekeys = new String[] {
    CtAbstractBVO.NQTPRICE, CtAbstractBVO.NQTTAXPRICE,
    CtAbstractBVO.NGTAXPRICE, CtAbstractBVO.NGPRICE
  };

  // 业务单位数量
  String[] quoteNumkeys = new String[] {
    CtAbstractBVO.NQTUNITNUM
  };

  // 价格
  public void setScale(BillScaleProcessor scale, TotalValueScale totalScale) {
    // 全局本位币金额
    String[] globalmnykeys = new String[] {
      CtAbstractBVO.NGLOBALMNY, CtAbstractBVO.NGLOBALTAXMNY
    };
    // 集团本位币金额
    String[] groupmnykeys = new String[] {
      CtAbstractBVO.NGROUPMNY, CtAbstractBVO.NGROUPTAXMNY
    };
    // 全局本位币金额精度
    scale.setGlobalLocMnyCtlInfo(globalmnykeys, PosEnum.body, null);
    // 集团本位币金额精度
    scale.setGroupLocMnyCtlInfo(groupmnykeys, PosEnum.body, null);
    // 税率
    scale.setTaxRateCtlInfo(this.ntaxratekeys, PosEnum.body, null);
    // 换算率
    scale.setHslCtlInfo(this.changeRates, PosEnum.body, null);
    // 报价单位数量精度
    scale.setNumCtlInfo(this.quoteNumkeys, PosEnum.body, null,
        CtAbstractBVO.CQTUNITID, PosEnum.body, null);
    // 业务单位数量精度
    scale.setNumCtlInfo(this.assistNumkeys, PosEnum.body, null,
        CtAbstractBVO.CASTUNITID, PosEnum.body, null);
    // 主单位数量精度
    scale.setNumCtlInfo(this.numkeys, PosEnum.body, null,
        CtAbstractBVO.CUNITID, PosEnum.body, null);
    // 单价精度
    // 原币单价精度
    scale.setPriceCtlInfo(this.origpricekeys, PosEnum.body, null,
        CtAbstractVO.CORIGCURRENCYID, PosEnum.head, null);
    // 本币单价精度
    scale.setPriceCtlInfo(this.pricekeys, PosEnum.body, null,
        CtAbstractVO.CCURRENCYID, PosEnum.head, null);
    // scale.setPriceCtlInfo(this.pricekeys, PosEnum.body, null);
    // 本币金额精度
    scale.setMnyCtlInfo(this.mnykeys, PosEnum.body, null,
        CtAbstractVO.CCURRENCYID, PosEnum.head, null);
    // 原币金额精度
    scale.setMnyCtlInfo(this.orgmnykeys, PosEnum.body, null,
        CtAbstractVO.CORIGCURRENCYID, PosEnum.head, null);
    // 表头原币金额精度
    scale.setMnyCtlInfo(this.headorgmnykeys, PosEnum.head, null,
        CtAbstractVO.CORIGCURRENCYID, PosEnum.head, null);
    // 表头本币金额精度
    scale.setMnyCtlInfo(this.headmnykeys, PosEnum.head, null,
        CtAbstractVO.CCURRENCYID, PosEnum.head, null);
    // 汇率精度处理
    this.setOrgExchange(scale);
    this.setGroupExchange(scale);
    this.setGlobalExchaneg(scale);

    // 进行计算
    scale.process();
    // 合计信息精度控制器(整单数量整单金额)
    totalScale.setHeadTailKeys(new String[] {
      CtAbstractVO.NTOTALASTNUM
    });
  }

  private void setGlobalExchaneg(BillScaleProcessor scale) {
    FieldInfo rate =
        new FieldInfo(CtAbstractVO.NGLOBALEXCHGRATE, 0, null);
    FieldInfo orgOrigCurr =
        new FieldInfo(CtAbstractVO.CORIGCURRENCYID, 0, null);
    FieldInfo orgLocCurr =
        new FieldInfo(CtAbstractVO.CCURRENCYID, 0, null);
    scale.setGlobalExchangeCtlInfo(rate, orgOrigCurr, orgLocCurr);
  }

  private void setGroupExchange(BillScaleProcessor scale) {
    FieldInfo rate =
        new FieldInfo(CtAbstractVO.NGROUPEXCHGRATE, 0, null);
    FieldInfo orgOrigCurr =
        new FieldInfo(CtAbstractVO.CORIGCURRENCYID, 0, null);
    FieldInfo orgLocCurr =
        new FieldInfo(CtAbstractVO.CCURRENCYID, 0, null);
    scale.setGroupExchangeCtlInfo(rate, orgOrigCurr, orgLocCurr);
  }

  private void setOrgExchange(BillScaleProcessor scale) {
    FieldInfo rate =
        new FieldInfo(CtAbstractVO.NEXCHANGERATE, 0, null);
    FieldInfo srcCurr =
        new FieldInfo(CtAbstractVO.CORIGCURRENCYID, 0, null);
    FieldInfo destCurr =
        new FieldInfo(CtAbstractVO.CCURRENCYID, 0, null);
		FieldInfo org = new FieldInfo(CtAbstractVO.PK_ORG, 0, null);
    scale.setOrgExchangeCtlInfo(rate, srcCurr, destCurr, org);
  }

}
