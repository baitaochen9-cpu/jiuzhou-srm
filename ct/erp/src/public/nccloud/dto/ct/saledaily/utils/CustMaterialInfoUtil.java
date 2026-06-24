package nccloud.dto.ct.saledaily.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import nc.bs.framework.common.NCLocator;
import nc.pubitf.uapbd.ICustMaterialPubService;
import nc.vo.ct.entity.CtAbstractBVO;
import nc.vo.ct.entity.CtAbstractVO;
import nc.vo.ct.saledaily.entity.CtSaleBVO;
import nc.vo.ct.saledaily.entity.CtSaleVO;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.scmf.pub.keyvalue.IKeyValue;
import nc.vo.uapbd.custmaterial.CustMaterialVO;
import nc.vo.uapbd.custmaterial.CustMaterialView;

import nccloud.commons.lang.StringUtils;

/**
 * @description 根据维度匹配客户物料码规则
 * @author wangshrc
 * @date 2019年2月16日 上午10:38:09
 * @version ncc1.0
 */
public class CustMaterialInfoUtil {

	public void setCustMaterialInfo(IKeyValue keyValue, int[] rows) {
		String pk_customer = keyValue.getHeadStringValue(CtSaleVO.PK_CUSTOMER);
		if (StringUtils.isBlank(pk_customer)) {
			return;
		}
		Map<String, String> custMarMap = this.getCustMarIDMap(keyValue, rows);

		for (int row : rows) {
			StringBuffer keySB = this.getKey(keyValue, row);
			if (custMarMap.get(keySB.toString()) != null) {
				keyValue.setBodyValue(row, CtSaleBVO.CCUSTMATERIALID,
						custMarMap.get(keySB.toString()));
			} else {
				keyValue.setBodyValue(row, CtSaleBVO.CCUSTMATERIALID, null);
			}
		}
	}

	private Map<String, String> getCustMarIDMap(IKeyValue keyValue, int[] rows) {
		String pk_org = keyValue.getHeadStringValue(CtAbstractVO.PK_ORG);
		String pk_customer = keyValue.getHeadStringValue(CtSaleVO.PK_CUSTOMER);
		CustMaterialView[] custMaterialViews = this.getCustMarViews(keyValue,
				rows);
		Map<String, String> custmaridMap = new HashMap<String, String>();
		Map<CustMaterialView, CustMaterialVO> custMarMap = this
				.queryCustMaterials(pk_org, pk_customer, custMaterialViews);
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

	private CustMaterialView[] getCustMarViews(IKeyValue keyValue, int[] rows) {
		List<CustMaterialView> views = new ArrayList<CustMaterialView>();
		for (int row : rows) {
			String pk_material = keyValue.getBodyStringValue(row,
					CtAbstractBVO.PK_MATERIAL);
			String cproductorid = keyValue.getBodyStringValue(row,
					CtAbstractBVO.CPRODUCTORID);
			String free1 = keyValue.getBodyStringValue(row,
					CtAbstractBVO.VFREE1);
			String free2 = keyValue.getBodyStringValue(row,
					CtAbstractBVO.VFREE2);
			String free3 = keyValue.getBodyStringValue(row,
					CtAbstractBVO.VFREE3);
			String free4 = keyValue.getBodyStringValue(row,
					CtAbstractBVO.VFREE4);
			String free5 = keyValue.getBodyStringValue(row,
					CtAbstractBVO.VFREE5);
			String free6 = keyValue.getBodyStringValue(row,
					CtAbstractBVO.VFREE6);
			String free7 = keyValue.getBodyStringValue(row,
					CtAbstractBVO.VFREE7);
			String free8 = keyValue.getBodyStringValue(row,
					CtAbstractBVO.VFREE8);
			String free9 = keyValue.getBodyStringValue(row,
					CtAbstractBVO.VFREE9);
			String free10 = keyValue.getBodyStringValue(row,
					CtAbstractBVO.VFREE10);

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

	private StringBuffer getKey(IKeyValue keyValue, int row) {
		StringBuffer keySB = new StringBuffer();
		keySB.append(keyValue.getHeadStringValue(CtSaleVO.PK_CUSTOMER));
		keySB.append("|");
		keySB.append(keyValue
				.getBodyStringValue(row, CtAbstractBVO.PK_MATERIAL));
		keySB.append("|");
		keySB.append(keyValue.getBodyStringValue(row,
				CtAbstractBVO.CPRODUCTORID));
		keySB.append("|");
		keySB.append(keyValue.getBodyStringValue(row, CtAbstractBVO.VFREE1));
		keySB.append("|");
		keySB.append(keyValue.getBodyStringValue(row, CtAbstractBVO.VFREE2));
		keySB.append("|");
		keySB.append(keyValue.getBodyStringValue(row, CtAbstractBVO.VFREE3));
		keySB.append("|");
		keySB.append(keyValue.getBodyStringValue(row, CtAbstractBVO.VFREE4));
		keySB.append("|");
		keySB.append(keyValue.getBodyStringValue(row, CtAbstractBVO.VFREE5));
		keySB.append("|");
		keySB.append(keyValue.getBodyStringValue(row, CtAbstractBVO.VFREE6));
		keySB.append("|");
		keySB.append(keyValue.getBodyStringValue(row, CtAbstractBVO.VFREE7));
		keySB.append("|");
		keySB.append(keyValue.getBodyStringValue(row, CtAbstractBVO.VFREE8));
		keySB.append("|");
		keySB.append(keyValue.getBodyStringValue(row, CtAbstractBVO.VFREE9));
		keySB.append("|");
		keySB.append(keyValue.getBodyStringValue(row, CtAbstractBVO.VFREE10));
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
			ICustMaterialPubService custmarsrv = NCLocator.getInstance()
					.lookup(ICustMaterialPubService.class);
			Map<CustMaterialView, CustMaterialVO> custmaterialmap = null;
			custmaterialmap = custmarsrv.queryCustMaterials(pk_org, customer,
					custMaterialViews);

			return custmaterialmap;
		} catch (Exception e1) {
			ExceptionUtils.wrappException(e1);
		}
		return null;
	}

}
