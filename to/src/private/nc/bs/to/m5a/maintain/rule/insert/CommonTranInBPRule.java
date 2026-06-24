package nc.bs.to.m5a.maintain.rule.insert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import nc.vo.bd.material.MaterialVO;
import nc.vo.bd.material.stock.MaterialStockVO;
import nc.vo.org.OrgVO;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.pubapp.pattern.pub.MapList;
import nc.vo.scmpub.util.VOFieldLengthChecker;
import nc.vo.to.m5a.entity.TransInBodyVO;
import nc.vo.to.m5a.entity.TransInHeadVO;
import nc.vo.to.m5a.entity.TransInVO;

import nc.itf.fi.pub.SysInit;
import nc.itf.scmpub.reference.uap.bd.material.MaterialPubService;
import nc.itf.scmpub.reference.uap.org.OrgUnitPubService;

import nc.bs.ml.NCLangResOnserver;

import nc.impl.pubapp.pattern.rule.IRule;

/**
 * @description 保存通用检查规则
 * 
 * @scene 调入申请新增保存
 * 
 * 
 * @since 6.36
 * @version 2014-10-22 上午11:23:10
 * @author yanrh
 */
public class CommonTranInBPRule implements IRule<TransInVO> {

  private void checkInv(String[] pk_materials, String pk_org,
      Map<String, MaterialVO> materialVOs, String pk_orgname) {
    try {
      Map<String, MaterialStockVO> map =
          MaterialPubService.queryMaterialStockInfo(pk_materials, pk_org,
              new String[] {
                MaterialStockVO.PK_MATERIAL, MaterialStockVO.PK_ORG
              });
      if (map.size() == pk_materials.length) {
        return;
      }
      StringBuilder error = new StringBuilder();
      for (String pk : pk_materials) {
        if (!map.containsKey(pk)) {
          String code = materialVOs.get(pk).getCode();
          if (error.length() > 0) {
            error.append("、"/* -=notranslate=- */
                + NCLangResOnserver.getInstance().getStrByID("4009002_0",
                    "04009002-0132", null, new String[] {
                      code, pk_orgname
                    })/* 物料{0}未分配到{1}该库存组织 */);
          }
          else {
        	 String isTO18 = SysInit.getParaString(pk_org, "TO18");
             String isTO19 = SysInit.getParaString(pk_org, "TO19");
             if(("不跳过".equals(isTO18) || "不跳过".equals(isTO19))){
            error.append(NCLangResOnserver.getInstance().getStrByID(
                "4009002_0", "04009002-0132", null, new String[] {
                  code, pk_orgname
                })/* 物料{0}未分配到{1}该库存组织 */);
             }
          }
        }
      }
      if (error.length() > 0) {
        ExceptionUtils.wrappBusinessException(error.toString());
      }
    }
    catch (Exception e) {
      ExceptionUtils.wrappException(e);
    }
  }

  private void checkInvAllot(TransInVO bill) {

    TransInHeadVO head = bill.getParentVO();
    TransInBodyVO[] items = bill.getChildrenVO();
    String pk_org = head.getPk_org();
    List<String> list = new ArrayList<String>();
    List<String> orglist = new ArrayList<String>();
    orglist.add(pk_org);
    MapList<String, String> maplist = new MapList<String, String>();

    for (TransInBodyVO item : items) {
      String cinventroyvid = item.getCinventoryvid();
      maplist.put(item.getCoutstockorgid(), cinventroyvid);
      if (!list.contains(cinventroyvid)) {
        list.add(cinventroyvid);
      }
    }
    try {
      String[] pk_materials = new String[list.size()];
      pk_materials = list.toArray(pk_materials);
      Map<String, MaterialVO> materialVOs =
          MaterialPubService.queryMaterialBaseInfo(pk_materials, new String[] {
            MaterialVO.PK_MATERIAL, MaterialVO.CODE
          });

      Iterator<String> it = maplist.keySet().iterator();

      while (it.hasNext()) {
        String org = it.next();
        orglist.add(org);

      }
      String[] orgs = new String[orglist.size()];
      orgs = orglist.toArray(orgs);

      OrgVO[] orgvos = OrgUnitPubService.getOrgsByPks(orgs, new String[] {
        OrgVO.PK_ORG, OrgVO.CODE, OrgVO.NAME
      });
      Map<String, String> orgmap = new HashMap<String, String>();
      for (OrgVO orgvo : orgvos) {
        orgmap.put(orgvo.getPk_org(), orgvo.getName());
      }

      this.checkInv(pk_materials, pk_org, materialVOs, orgmap.get(pk_org));

      Map<String, List<String>> map = maplist.toMap();
      it = maplist.keySet().iterator();
      while (it.hasNext()) {
        String org = it.next();
        List<String> invlist = map.get(org);
        this.checkInv(invlist.toArray(new String[0]), org, materialVOs,
            orgmap.get(org));

      }
    }
    catch (Exception e) {
      ExceptionUtils.wrappException(e);
    }

  }

  private void checkNumConvert() {
    // TODO 自动生成方法存根

  }

  private void checkNumPrecision() {
    // TODO 自动生成方法存根

  }

  @Override
  public void process(TransInVO[] bills) {
    // 检查自定义项是否匹配
    for (TransInVO bill : bills) {
      // 存货是否分配
      this.checkInvAllot(bill);

      // 检查数据精度
      this.checkNumPrecision();
      // 检查数据换算是否正确
      this.checkNumConvert();
    }
    // 检查极限值
    VOFieldLengthChecker.checkVOFieldsLength(bills);
  }

}
