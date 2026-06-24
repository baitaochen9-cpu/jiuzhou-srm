package nc.bs.jzyy.sys.oa.saledaily.rule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.itf.scmpub.reference.uap.bd.material.MaterialPubService;
import nc.vo.bd.material.MaterialPubQueryVO;
import nc.vo.ct.entity.CtAbstractBVO;
import nc.vo.ct.saledaily.entity.AggCtSaleVO;
import nc.vo.ct.saledaily.entity.CtSaleBVO;
import nc.vo.ct.uitl.ValueUtil;

/**
 * 设置表体物料相关信息
 * @Description: 
 *   
 * @author: 刘伟
 * @date:   2019-4-28 下午5:28:04   
 * @version NCC1909
 */
public class ChooseBodyMaterialEvent extends CtFieldEvent{

	private Map<String, MaterialPubQueryVO> chgrateMap = null;

	private Map<String, String> map = null;

	private String NCHANGERAtE = "1.0000/1.0000";
	
	private AggCtSaleVO[] paramArrayOfE = null;
	
	public ChooseBodyMaterialEvent(AggCtSaleVO[] paramArrayOfE){
		this.paramArrayOfE = paramArrayOfE;
	}

	public void process() {
		for(AggCtSaleVO aggvo : paramArrayOfE){
			//根据物料设置信息
			this.setDateWithMaterial(aggvo);
		}
	}
	
	/**
	 * 根据物料设置表体默认信息
	 * @param aggvo
	 */
	private void setDateWithMaterial(AggCtSaleVO aggvo) {
		//1、设置表体物料原始版本、默认单位、 换算率
		List<String> materialList = new ArrayList<String>();
		for(CtSaleBVO bvo : aggvo.getCtSaleBVO()){
			materialList.add(bvo.getPk_material());
		}
		Map<String, String> srcmaterial = 
				MaterialPubService.queryMaterialOidByVid(materialList.toArray(new String[materialList.size()]));
		for(CtSaleBVO bvo : aggvo.getCtSaleBVO()){
			String pk_material = bvo.getPk_material();
			String pk_srcmaterial = srcmaterial.get(pk_material);
			bvo.setAttributeValue(CtAbstractBVO.PK_SRCMATERIAL, pk_srcmaterial);
		}
		this.map = 
				MaterialPubService.queryPuMeasdocIDByPks(materialList.toArray(new String[materialList.size()]));
		this.chgrateMap = getMarChgrateMap(aggvo);
		
		for(CtSaleBVO bvo : aggvo.getCtSaleBVO()){
			String pk_material = bvo.getPk_material();
			String castunitid = map.get(pk_material);
			if (!ValueUtil.isEmpty(castunitid)) {
				bvo.setAttributeValue(CtAbstractBVO.CASTUNITID, castunitid);
				bvo.setAttributeValue(CtAbstractBVO.CQTUNITID, castunitid);
				MaterialPubQueryVO vo =
						this.chgrateMap.get(bvo.getPk_srcmaterial() 
								+ bvo.getCastunitid()
								+ bvo.getCunitid());
				bvo.setAttributeValue(CtAbstractBVO.CUNITID, castunitid);
				this.NCHANGERAtE = vo.getO_src_measrate();
				bvo.setAttributeValue(CtAbstractBVO.VCHANGERATE, this.NCHANGERAtE);
				bvo.setAttributeValue(CtAbstractBVO.VQTUNITRATE, this.NCHANGERAtE);
			}
			else {
				this.NCHANGERAtE = "1.0000/1.0000";
				bvo.setAttributeValue(CtAbstractBVO.VCHANGERATE, this.NCHANGERAtE);
				bvo.setAttributeValue(CtAbstractBVO.VQTUNITRATE, this.NCHANGERAtE);
			}
				
		}
	}
	
	/**
	 * 获取表体物料的实体信息
	 * @param aggvo
	 * @return
	 */
	private Map<String, MaterialPubQueryVO> getMarChgrateMap(AggCtSaleVO aggvo) {
	    List<MaterialPubQueryVO> voList = new ArrayList<MaterialPubQueryVO>();
	    // 将参数转换成uap接口需要的参数list
	    for (CtSaleBVO bvo : aggvo.getCtSaleBVO()) {
	      MaterialPubQueryVO queryVO = new MaterialPubQueryVO();
	      queryVO.setOid(bvo.getPk_srcmaterial());
	      queryVO.setO_src_measdocid(this.map.get(bvo.getPk_material()));
	      queryVO.setO_des_measdocid(bvo.getCunitid());
	      voList.add(queryVO);
	    }
	    List<MaterialPubQueryVO> uapResult =
	        MaterialPubService.queryMeasrateByOidAndmeasdocs(voList);
	    Map<String, MaterialPubQueryVO> uapResultMap =
	        new HashMap<String, MaterialPubQueryVO>();
	    if (null == uapResult || uapResult.size() == 0) {
	      return uapResultMap;
	    }
	    for (MaterialPubQueryVO vo : uapResult) {
	      uapResultMap.put(
	          vo.getOid() + vo.getO_src_measdocid() + vo.getO_des_measdocid(), vo);
	    }
	    return uapResultMap;

	  }

}
