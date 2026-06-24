package nc.bs.pp.m28.maintain.rule.insert;

import nc.impl.pubapp.pattern.rule.IRule;
import nc.vo.pp.m28.entity.PriceAuditItemVO;
import nc.vo.pp.m28.entity.PriceAuditVO;

import nccloud.commons.lang.StringUtils;

/**
 * @description 本类主要完成以下功能：价格审批单-物料自由辅助属性保存后规则
 * @scene 价格审批单
 * @parma 无
 * @since 6.0
 * @version 2011-8-18 下午05:05:17
 * @author chendb
 */
public class PAuditAfterMarAssRule implements IRule<PriceAuditVO> {

  @Override
  public void process(PriceAuditVO[] vos) {
    if (null == vos || vos.length == 0) {
      return;
    }
    for (PriceAuditVO vo : vos) {
      // 懒加载
      if (null == vo.getChildrenVO()) {
        continue;
      }
      for (PriceAuditItemVO itemvo : vo.getChildrenVO()) {
        // 供应商行
        if (null == itemvo.getPk_material()
            || StringUtils.isEmpty(itemvo.getPk_material())) {
          itemvo.setVfree1(null);
          itemvo.setVfree2(null);
          itemvo.setVfree3(null);
          itemvo.setVfree4(null);
          itemvo.setVfree5(null);
          itemvo.setVfree6(null);
          itemvo.setVfree7(null);
          itemvo.setVfree8(null);
          itemvo.setVfree9(null);
          itemvo.setVfree10(null);
        }
      }
    }
  }

}
