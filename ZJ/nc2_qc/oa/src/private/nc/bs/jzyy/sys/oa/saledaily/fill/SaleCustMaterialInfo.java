package nc.bs.jzyy.sys.oa.saledaily.fill;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import nc.bs.framework.common.NCLocator;
import nc.pubitf.uapbd.ICustMaterialPubService;
import nc.vo.ct.saledaily.entity.AggCtSaleVO;
import nc.vo.ct.saledaily.entity.CtSaleBVO;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.uapbd.custmaterial.CustMaterialVO;
import nc.vo.uapbd.custmaterial.CustMaterialView;

import org.apache.commons.lang.StringUtils;


/**
 * 根据维度匹配客户物料码规则
 * @Description: 
 *   
 * @author: 刘伟
 * @date:   2019-4-27 下午6:22:18   
 * @version NCC1909
 */
public class SaleCustMaterialInfo {

  public void setCustMaterialInfo(AggCtSaleVO aggvo) {
    String pk_customer = aggvo.getParentVO().getPk_customer();
    if (StringUtils.isBlank(pk_customer)) {
      return;
    }
    Map<String, String> custMarMap = this.getCustMarIDMap(aggvo);

    for (CtSaleBVO bvo : aggvo.getCtSaleBVO()) {
    	
      StringBuffer keySB = this.getKey(bvo, pk_customer);
      if (custMarMap.get(keySB.toString()) != null) {
    	  bvo.setAttributeValue(CtSaleBVO.CCUSTMATERIALID, custMarMap.get(keySB.toString()));
      }
      else {
    	  bvo.setAttributeValue(CtSaleBVO.CCUSTMATERIALID, null);
    	  bvo.setAttributeValue(CtSaleBVO.CCUSTMATERIALID + ".name", custMarMap.get(keySB.toString()));
      }
    }
  }

  private Map<String, String> getCustMarIDMap(AggCtSaleVO aggvo) {
    String pk_org = aggvo.getParentVO().getPk_org();
    String pk_customer = aggvo.getParentVO().getPk_customer();
    CustMaterialView[] custMaterialViews =
        this.getCustMarViews(aggvo);
    Map<String, String> custmaridMap = new HashMap<String, String>();
    Map<CustMaterialView, CustMaterialVO> custMarMap =
        this.queryCustMaterials(pk_org, pk_customer, custMaterialViews);
    if (custMarMap != null && custMarMap.size() > 0) {
      for (Entry<CustMaterialView, CustMaterialVO> entry : custMarMap
          .entrySet()) {
        StringBuffer keySB = this.getResultKey(entry);
        custmaridMap.put(keySB.toString(), entry.getValue()
            .getPk_custmaterial());
      }
    }
    return custmaridMap;

  }

  private CustMaterialView[] getCustMarViews(AggCtSaleVO aggvo) {
    List<CustMaterialView> views = new ArrayList<CustMaterialView>();
    for (CtSaleBVO bvo : aggvo.getCtSaleBVO()) {
      String pk_material = bvo.getPk_material();
      String cproductorid = bvo.getCproductorid();
      String free1 = bvo.getVfree1();
      String free2 = bvo.getVfree2();
      String free3 = bvo.getVfree3();
      String free4 = bvo.getVfree4();
      String free5 = bvo.getVfree5();
      String free6 = bvo.getVfree6();
      String free7 = bvo.getVfree7();
      String free8 = bvo.getVfree8();
      String free9 = bvo.getVfree9();
      String free10 = bvo.getVfree10();

      CustMaterialView custmarview = new CustMaterialView();
      custmarview.setPk_material(pk_material);
      custmarview.setVfree4(cproductorid);
      custmarview.setVfree6(free1);
      custmarview.setVfree7(free2);
      custmarview.setVfree8(free3);
      custmarview.setVfree9(free4);
      custmarview.setVfree10(free5);
      custmarview.setVfree11(free6);
      custmarview.setVfree12(free7);
      custmarview.setVfree13(free8);
      custmarview.setVfree14(free9);
      custmarview.setVfree15(free10);
      views.add(custmarview);
    }
    return views.toArray(new CustMaterialView[views.size()]);

  }

  private StringBuffer getKey(CtSaleBVO bvo, String pk_customer) {
    StringBuffer keySB = new StringBuffer();
    keySB.append(pk_customer);
    keySB.append("|");
    keySB.append(bvo.getPk_material());
    keySB.append("|");
    keySB.append(bvo.getCproductorid());
    keySB.append("|");
    keySB.append(bvo.getVfree1());
    keySB.append("|");
    keySB.append(bvo.getVfree2());
    keySB.append("|");
    keySB.append(bvo.getVfree3());
    keySB.append("|");
    keySB.append(bvo.getVfree4());
    keySB.append("|");
    keySB.append(bvo.getVfree5());
    keySB.append("|");
    keySB.append(bvo.getVfree6());
    keySB.append("|");
    keySB.append(bvo.getVfree7());
    keySB.append("|");
    keySB.append(bvo.getVfree8());
    keySB.append("|");
    keySB.append(bvo.getVfree9());
    keySB.append("|");
    keySB.append(bvo.getVfree10());
    return keySB;
  }

  private StringBuffer getResultKey(
      Entry<CustMaterialView, CustMaterialVO> entry) {
    StringBuffer keySB = new StringBuffer();
    keySB.append(entry.getValue().getPk_customer());
    keySB.append("|");
    keySB.append(entry.getValue().getMaterialid());
    keySB.append("|");
    keySB.append(entry.getValue().getVfree4());
    keySB.append("|");
    keySB.append(entry.getValue().getVfree6());
    keySB.append("|");
    keySB.append(entry.getValue().getVfree7());
    keySB.append("|");
    keySB.append(entry.getValue().getVfree8());
    keySB.append("|");
    keySB.append(entry.getValue().getVfree9());
    keySB.append("|");
    keySB.append(entry.getValue().getVfree10());
    keySB.append("|");
    keySB.append(entry.getValue().getVfree11());
    keySB.append("|");
    keySB.append(entry.getValue().getVfree12());
    keySB.append("|");
    keySB.append(entry.getValue().getVfree13());
    keySB.append("|");
    keySB.append(entry.getValue().getVfree14());
    keySB.append("|");
    keySB.append(entry.getValue().getVfree15());
    return keySB;
  }

  private Map<CustMaterialView, CustMaterialVO> queryCustMaterials(
      String pk_org, String customer, CustMaterialView[] custMaterialViews) {
    try {
      ICustMaterialPubService custmarsrv =
          NCLocator.getInstance().lookup(ICustMaterialPubService.class);
      Map<CustMaterialView, CustMaterialVO> custmaterialmap = null;
      custmaterialmap =
          custmarsrv.queryCustMaterials(pk_org, customer, custMaterialViews);

      return custmaterialmap;
    }
    catch (Exception e1) {
      ExceptionUtils.wrappException(e1);
    }
    return null;
  }

}
