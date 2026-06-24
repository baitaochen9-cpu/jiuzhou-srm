package nccloud.dto.ct.price.entity;

/**
 * 
 * @description 价格信息表动态列dto
 * @author zhaoypm
 * @time 2019-3-27 下午2:32:17
 * @since ncc1.0
 */
public class CtPriceDynamicColumn {
	private String attrCode;
	private String label;
	private String value;

	public String getAttrCode() {
		return attrCode;
	}

	public void setAttrCode(String attrCode) {
		this.attrCode = attrCode;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
