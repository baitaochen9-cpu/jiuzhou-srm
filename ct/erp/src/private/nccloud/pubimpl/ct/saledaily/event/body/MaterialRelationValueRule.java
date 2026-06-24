package nccloud.pubimpl.ct.saledaily.event.body;

import java.util.Map;

import nc.itf.scmpub.reference.uap.bd.material.MaterialPubService;
import nc.vo.bd.material.MaterialVO;
import nc.vo.ct.saledaily.entity.CtSaleBVO;
import nc.vo.scmf.pub.keyvalue.IKeyValue;

/**
 * 
 * @description 
 * 根据物料自动带出物料相关信息 主要用于物料编辑后事件
 * @author cuijun
 * @date 2019年6月20日 下午4:28:28
 * @version ncc1.0
 */
public class MaterialRelationValueRule {

	private IKeyValue keyValue;

	public MaterialRelationValueRule(IKeyValue keyValue) {
		this.keyValue = keyValue;
	}

	/**
	 * 根据物料自动带出物料相关信息 主要用于物料编辑后事件
	 * 包括oid,主单位
	 * 
	 * @param rows
	 *
	 */
	public void setBodyDefValue(int[] rows) {
		

		String[] cmaterialvids = new String[rows.length];
		for (int i = 0; i < cmaterialvids.length; i++) {
			String cmaterialvid = this.keyValue.getBodyStringValue(rows[i],
					CtSaleBVO.PK_MATERIAL);
			
			cmaterialvids[i] = cmaterialvid;
		}

		Map<String, MaterialVO> materialMap = this
				.queryMaterialBaseInfo(cmaterialvids);

		for (int i = 0; i < cmaterialvids.length; i++) {
			String cmaterialvid = cmaterialvids[i];
			MaterialVO baseInfo = materialMap.get(cmaterialvid);
			if (baseInfo == null) {
				baseInfo = new MaterialVO();
			}
			this.keyValue.setBodyValue(rows[i], CtSaleBVO.PK_SRCMATERIAL,
					baseInfo.getPk_source());
			this.keyValue.setBodyValue(rows[i], CtSaleBVO.CUNITID,
					baseInfo.getPk_measdoc());
		}

	}

	private Map<String, MaterialVO> queryMaterialBaseInfo(String[] cmaterialvids) {
		return MaterialPubService.queryMaterialBaseInfo(cmaterialvids, new String[]{MaterialVO.PK_SOURCE,MaterialVO.PK_MEASDOC,MaterialVO.PK_PRODLINE,MaterialVO.FEE,MaterialVO.DISCOUNTFLAG});
	}
}
