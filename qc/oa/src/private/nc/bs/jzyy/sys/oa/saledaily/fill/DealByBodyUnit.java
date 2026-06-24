package nc.bs.jzyy.sys.oa.saledaily.fill;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nc.itf.scmpub.reference.uap.bd.material.MaterialPubService;
import nc.vo.bd.material.MaterialPubQueryVO;
import nc.vo.ct.entity.CtAbstractBVO;
import nc.vo.ct.entity.CtAbstractVO;
import nc.vo.ct.uitl.ValueUtil;
import nc.vo.scmpub.res.billtype.CTBillType;

/**
 * 处理表体物料单位、换算率信息
 * @Description: 
 *   
 * @author: 刘伟
 * @date:   2019-4-28 下午5:18:01   
 * @version NCC1909
 */
public class DealByBodyUnit {

	 private String billtype;

	  private Map<String, MaterialPubQueryVO> chgrateMap = null;

	  private Map<String, String> map = null;

	  private String NCHANGERAtE = "1.0000/1.0000";

	  private CtAbstractVO hvo;
	  
	  private CtAbstractBVO[] bvos;

	  public DealByBodyUnit(CtAbstractVO hvo, CtAbstractBVO[] bvos, String billtype) {
	    this.hvo = hvo;
	    this.bvos = bvos;
	    this.billtype = billtype;
	  }
	  
	  public void setEditable() {
	    Object pk_org = hvo.getPk_org();
	    Object pk_org_v = hvo.getPk_org_v();
	    Object pk_group = hvo.getPk_group();

	    Set<String> mars = new HashSet<String>();
	    for(CtAbstractBVO bvo : bvos){
	    	mars.add(bvo.getPk_material());
	    }
	    String[] materials = mars.toArray(new String[mars.size()]);
	    if (ValueUtil.equals(this.billtype, CTBillType.PurDaily.getCode())
	        || ValueUtil.equals(this.billtype, CTBillType.OtherPur.getCode())) {
	      this.map = MaterialPubService.queryPuMeasdocIDByPks(materials);
	    }
	    else if (ValueUtil.equals(this.billtype, CTBillType.SaleDaily.getCode())
	        || ValueUtil.equals(this.billtype, CTBillType.OtherSale.getCode())) {
	      this.map = MaterialPubService.querySaleMeasdocIDByPks(materials);
	    }

	    this.chgrateMap = this.getMarChgrateMap(bvos);

	    for(CtAbstractBVO bvo : bvos){
	    	this.setEditable(bvo, pk_org, pk_org_v, pk_group);
	    }
	  }
	  
	  private void setEditable(CtAbstractBVO bvo, Object pk_org, Object pk_org_v,
		      Object pk_group) {
		  bvo.setAttributeValue(CtAbstractBVO.PK_ORG, pk_org);
		  bvo.setAttributeValue(CtAbstractBVO.PK_ORG_V, pk_org_v);
		  bvo.setAttributeValue(CtAbstractBVO.PK_GROUP, pk_group);
	    String pk_material = bvo.getPk_material();
	    String castunitid = this.map.get(pk_material);
	    if (!ValueUtil.isEmpty(castunitid)) {
	    	bvo.setAttributeValue(CtAbstractBVO.CASTUNITID, castunitid);
	    	bvo.setAttributeValue(CtAbstractBVO.CQTUNITID, castunitid);
	      MaterialPubQueryVO vo =
	          this.chgrateMap.get(bvo.getPk_srcmaterial()
	              + bvo.getCastunitid()
	              + bvo.getCunitid());
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
	  
	  /**
		 * 获取表体物料的实体信息
		 * @param bvos
		 * @return
		 */
		private Map<String, MaterialPubQueryVO> getMarChgrateMap(CtAbstractBVO[] bvos) {
		    List<MaterialPubQueryVO> voList = new ArrayList<MaterialPubQueryVO>();
		    // 将参数转换成uap接口需要的参数list
		    for (CtAbstractBVO bvo : bvos) {
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
