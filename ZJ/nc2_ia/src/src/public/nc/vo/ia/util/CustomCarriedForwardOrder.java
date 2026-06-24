package nc.vo.ia.util;

import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;

/**
 * 
 * @author zhian.ye 20200325
 *
 */
public interface CustomCarriedForwardOrder {

	/**
	 * 自定义结转顺序
	 * 启用自定义结转后，返回对应结转时间
	 * 如果 不启用自定义结转则返回空值
	 * 参数： 组织/业务日期/单据类型
	 */
	public UFDate getBizData(String pk_org,UFDate bizdate,String billtype);
	public  UFBoolean isSelectMaterial(String material,String pk_org);
	public String getPk_CostRegion (String pk_org,String stordoc);
}