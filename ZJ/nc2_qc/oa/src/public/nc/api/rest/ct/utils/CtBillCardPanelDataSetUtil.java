package nc.api.rest.ct.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import nc.vo.ct.entity.CtAbstractBVO;
import nc.vo.ct.entity.CtAbstractVO;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.calculator.data.IDataSetDollarForCal;
import nc.vo.pubapp.calculator.data.IDataSetForCal;
import nc.vo.pubapp.calculator.data.IRelationDollarForItems;
import nc.vo.pubapp.calculator.data.IRelationForItems;
import nc.vo.pubapp.pattern.data.ValueUtils;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

/**
 * 采购 销售合同单价 金额联动计算数据设置
 * @Description: 
 *   
 * @author: 刘伟
 * @date:   2019-4-26 下午2:44:17   
 * @version NCC1909
 */
public class CtBillCardPanelDataSetUtil implements IDataSetForCal, IDataSetDollarForCal {
	
	private CircularlyAccessibleValueObject headVO;
	private CircularlyAccessibleValueObject bodyVO;
	private IRelationForItems item;
	private Set<String> itemKeys;
	
	
	public CtBillCardPanelDataSetUtil(){
		
	}
	/**
	 * OrderBillCardPanelDataSet 的构造子
	 * 
	 * @param cardPanel
	 * @param row
	 * @param item
	 */
	public CtBillCardPanelDataSetUtil(CircularlyAccessibleValueObject headVO,
			CircularlyAccessibleValueObject bodyVO, IRelationForItems item,
			Set<String> allItemKeys) {
		this.headVO = headVO;
		this.bodyVO = bodyVO;
		this.item = item;
		this.itemKeys = allItemKeys;
	}
	
	/**
	 * 父类方法重写
	 * 
	 */
	@Override
	public UFDate getBillDate() {
		return (UFDate) this.headVO.getAttributeValue(CtAbstractVO.DBILLDATE);
	}
	
	/**
	 * 父类方法重写
	 * 
	 */
	@Override
	public String getCcurrencyid() {
		return (String) this.headVO.getAttributeValue(CtAbstractVO.CCURRENCYID);
	}
	
	/**
	 * 父类方法重写
	 * 
	 */
	@Override
	public String getCorigcurrencyid() {
		return (String) this.headVO
				.getAttributeValue(CtAbstractVO.CORIGCURRENCYID);
	}
	
	/**
	 * 父类方法重写
	 * 
	 */
	@Override
	public UFDouble getNexchangerate() {
		return (UFDouble) this.headVO
				.getAttributeValue(CtAbstractVO.NEXCHANGERATE);
	}
	
	/**
	 * 父类方法重写
	 * 
	 */
	@Override
	public boolean hasItemKey(String key) {
		if (CtAbstractVO.NEXCHANGERATE.equals(key)) {
			return true;
		} else if (CtAbstractVO.CORIGCURRENCYID.equals(key)) {
			return true;
		} else if (CtAbstractVO.CCURRENCYID.equals(key)) {
			return true;
		} else if (CtAbstractVO.DBILLDATE.equals(key)) {
			return true;
		} else {
			return this.itemKeys.contains(key);
		}
	}
	
	@Override
	public Object getAttributeValue(String key) {
		Object value = this.bodyVO.getAttributeValue(key);
		if (value == null) {
			value = this.headVO.getAttributeValue(key);
		}
		return value;
	}
	
	@Override
	public String getCastunitid() {
		String value = this.getString(this.item.getCastunitidKey());
		return value;
	}
	
	/** 获得报价币种 */
	@Override
	public String getCqtcurrencyid() {
		String value = this.getString(this.item.getCqtcurrencyidKey());
		return value;
	}
	
	@Override
	public String getCqtunitid() {
		String value = this.getString(this.item.getCqtunitidKey());
		return value;
	}
	
	@Override
	public String getCunitid() {
		String value = this.getString(this.item.getCunitidKey());
		return value;
	}
	
	@Override
	public int getFtaxtypeflag() {
		int value = this.getInt(this.item.getFtaxtypeflagKey());
		return value;
	}
	
	@Override
	public UFDouble getNaskqtorigprice() {
		UFDouble value = this.getUFDoubleValue(this.item.getNaskqtorigpriceKey());
		return value;
	}
	
	@Override
	public UFDouble getNaskqtorigtaxprc() {
		UFDouble value = this
				.getUFDoubleValue(this.item.getNaskqtorigtaxprcKey());
		return value;
	}
	
	@Override
	public UFDouble getNaskqtprice() {
		UFDouble value = this.getUFDoubleValue(this.item.getNaskqtpriceKey());
		return value;
	}
	
	@Override
	public UFDouble getNaskqttaxprice() {
		UFDouble value = this.getUFDoubleValue(this.item.getNaskqttaxpriceKey());
		return value;
	}
	
	@Override
	public UFDouble getNassistnum() {
		UFDouble value = this.getUFDoubleValue(this.item.getNassistnumKey());
		return value;
	}
	
	@Override
	public String getNchangerate() {
		String value = this.getString(this.item.getNchangerateKey());
		return value;
	}
	
	@Override
	public UFDouble getNcostmny() {
		UFDouble value = this.getUFDoubleValue(this.item.getNcostmnyKey());
		return value;
	}
	
	@Override
	public UFDouble getNcostprice() {
		UFDouble value = this.getUFDoubleValue(this.item.getNcostpriceKey());
		return value;
	}
	
	@Override
	public UFDouble getNdiscount() {
		UFDouble value = this.getUFDoubleValue(this.item.getNdiscountKey());
		return value;
	}
	
	@Override
	public UFDouble getNdiscountrate() {
		UFDouble value = this.getUFDoubleValue(this.item.getNdiscountrateKey());
		return value;
	}
	
	@Override
	public UFDouble getNglobalexchgrate() {
		UFDouble value = this
				.getUFDoubleValue(this.item.getNglobalexchgrateKey());
		return value;
	}
	
	@Override
	public UFDouble getNglobalmny() {
		UFDouble value = this.getUFDoubleValue(this.item.getNglobalmnyKey());
		return value;
	}
	
	@Override
	public UFDouble getNglobaltaxmny() {
		UFDouble value = this.getUFDoubleValue(this.item.getNglobaltaxmnyKey());
		return value;
	}
	
	@Override
	public UFDouble getNgroupexchgrate() {
		UFDouble value = this.getUFDoubleValue(this.item.getNgroupexchgrateKey());
		return value;
	}
	
	@Override
	public UFDouble getNgroupmny() {
		UFDouble value = this.getUFDoubleValue(this.item.getNgroupmnyKey());
		return value;
	}
	
	@Override
	public UFDouble getNgrouptaxmny() {
		UFDouble value = this.getUFDoubleValue(this.item.getNgrouptaxmnyKey());
		return value;
	}
	
	@Override
	public UFDouble getNitemdiscountrate() {
		UFDouble value = this.getUFDoubleValue(this.item
				.getNitemdiscountrateKey());
		return value;
	}
	
	@Override
	public UFDouble getNmny() {
		UFDouble value = this.getUFDoubleValue(this.item.getNmnyKey());
		return value;
	}
	
	@Override
	public UFDouble getNnetprice() {
		UFDouble value = this.getUFDoubleValue(this.item.getNnetpriceKey());
		return value;
	}
	
	@Override
	public UFDouble getNnum() {
		UFDouble value = this.getUFDoubleValue(this.item.getNnumKey());
		return value;
	}
	
	@Override
	public UFDouble getNorigdiscount() {
		UFDouble value = this.getUFDoubleValue(this.item.getNorigdiscountKey());
		return value;
	}
	
	@Override
	public UFDouble getNorigmny() {
		UFDouble value = this.getUFDoubleValue(this.item.getNorigmnyKey());
		return value;
	}
	
	@Override
	public UFDouble getNorignetprice() {
		UFDouble value = this.getUFDoubleValue(this.item.getNorignetpriceKey());
		return value;
	}
	
	@Override
	public UFDouble getNorigprice() {
		UFDouble value = this.getUFDoubleValue(this.item.getNorigpriceKey());
		return value;
	}
	
	@Override
	public UFDouble getNorigtaxmny() {
		UFDouble value = this.getUFDoubleValue(this.item.getNorigtaxmnyKey());
		return value;
	}
	
	@Override
	public UFDouble getNorigtaxnetprice() {
		UFDouble value = this
				.getUFDoubleValue(this.item.getNorigtaxnetpriceKey());
		return value;
	}
	
	@Override
	public UFDouble getNorigtaxprice() {
		UFDouble value = this.getUFDoubleValue(this.item.getNorigtaxpriceKey());
		return value;
	}
	
	@Override
	public UFDouble getNprice() {
		UFDouble value = this.getUFDoubleValue(this.item.getNpriceKey());
		return value;
	}
	
	@Override
	public UFDouble getNqtnetprice() {
		UFDouble value = this.getUFDoubleValue(this.item.getNqtnetpriceKey());
		return value;
	}
	
	@Override
	public UFDouble getNqtorignetprice() {
		UFDouble value = this.getUFDoubleValue(this.item.getNqtorignetpriceKey());
		return value;
	}
	
	@Override
	public UFDouble getNqtorigprice() {
		UFDouble value = this.getUFDoubleValue(this.item.getNqtorigpriceKey());
		return value;
	}
	
	@Override
	public UFDouble getNqtorigtaxnetprc() {
		UFDouble value = this
				.getUFDoubleValue(this.item.getNqtorigtaxnetprcKey());
		return value;
	}
	
	@Override
	public UFDouble getNqtorigtaxprice() {
		UFDouble value = this.getUFDoubleValue(this.item.getNqtorigtaxpriceKey());
		return value;
	}
	
	@Override
	public UFDouble getNqtprice() {
		UFDouble value = this.getUFDoubleValue(this.item.getNqtpriceKey());
		return value;
	}
	
	@Override
	public UFDouble getNqttaxnetprice() {
		UFDouble value = this.getUFDoubleValue(this.item.getNqttaxnetpriceKey());
		return value;
	}
	
	@Override
	public UFDouble getNqttaxprice() {
		UFDouble value = this.getUFDoubleValue(this.item.getNqttaxpriceKey());
		return value;
	}
	
	@Override
	public UFDouble getNqtunitnum() {
		UFDouble value = this.getUFDoubleValue(this.item.getNqtunitnumKey());
		return value;
	}
	
	@Override
	public String getNqtunitrate() {
		String value = this.getString(this.item.getNqtunitrateKey());
		return value;
	}
	
	@Override
	public UFDouble getNtax() {
		UFDouble value = this.getUFDoubleValue(this.item.getNtaxKey());
		return value;
	}
	
	@Override
	public UFDouble getNtaxmny() {
		UFDouble value = this.getUFDoubleValue(this.item.getNtaxmnyKey());
		return value;
	}
	
	@Override
	public UFDouble getNtaxnetprice() {
		UFDouble value = this.getUFDoubleValue(this.item.getNtaxnetpriceKey());
		return value;
	}
	
	@Override
	public UFDouble getNtaxprice() {
		UFDouble value = this.getUFDoubleValue(this.item.getNtaxpriceKey());
		return value;
	}
	
	@Override
	public UFDouble getNtaxrate() {
		UFDouble value = this.getUFDoubleValue(this.item.getNtaxrateKey());
		return value;
	}
	
	@Override
	public UFDouble getNtotalnum() {
		UFDouble value = this.getUFDoubleValue(this.item.getNtotalnumKey());
		return value;
	}
	
	@Override
	public UFDouble getNtotalorigmny() {
		UFDouble value = this.getUFDoubleValue(this.item.getNtotalorigmnyKey());
		return value;
	}
	
	@Override
	public UFDouble getNtotalorigtaxmny() {
		UFDouble value = this
				.getUFDoubleValue(this.item.getNtotalorigtaxmnyKey());
		return value;
	}
	
	@Override
	public String getPk_group() {
		String value = this.getString(this.item.getPk_group());
		return value;
	}
	
	@Override
	public String getPk_org() {
		String value = this.getString(this.item.getPk_org());
		return value;
	}
	
	@Override
	public UFDouble getQualifiedNum() {
		UFDouble value = this.getUFDoubleValue(this.item.getQualifiedNumKey());
		return value;
	}
	
	@Override
	public IRelationForItems getRelationForItem() {
		return this.item;
	}
	
	@Override
	public UFDouble getUnQualifiedNum() {
		UFDouble value = this.getUFDoubleValue(this.item.getUnQualifiedNumKey());
		return value;
	}
	
	@Override
	public void setBillDate(UFDate date) {
		this.setAttributeValue(this.item.getBillDate(), date);
	}
	
	@Override
	public void setCastunitid(String value) {
		this.setAttributeValue(this.item.getUnQualifiedNumKey(), value);
	}
	
	/** 设置本位币币种 */
	@Override
	public void setCcurrencyid(String value) {
		this.setAttributeValue(this.item.getCcurrencyidKey(), value);
	}
	
	/** 设置原币币种 */
	@Override
	public void setCorigcurrencyid(String value) {
		this.setAttributeValue(this.item.getCorigcurrencyidKey(), value);
	}
	
	/** 设置报价币种 */
	@Override
	public void setCqtcurrencyid(String value) {
		this.setAttributeValue(this.item.getCqtcurrencyidKey(), value);
	}
	
	@Override
	public void setCqtunitid(String value) {
		this.setAttributeValue(this.item.getUnQualifiedNumKey(), value);
	}
	
	@Override
	public void setCunitid(String value) {
		this.setAttributeValue(this.item.getCunitidKey(), value);
	}
	
	@Override
	public void setFtaxtypeflag(int value) {
		this.setAttributeValue(this.item.getFtaxtypeflagKey(),
				Integer.valueOf(value));
	}
	
	@Override
	public void setNaskqtorigprice(UFDouble value) {
		this.setAttributeValue(this.item.getNaskqtorigpriceKey(), value);
	}
	
	@Override
	public void setNaskqtorigtaxprc(UFDouble value) {
		this.setAttributeValue(this.item.getNaskqtorigtaxprcKey(), value);
	}
	
	@Override
	public void setNaskqtprice(UFDouble value) {
		this.setAttributeValue(this.item.getNaskqtpriceKey(), value);
	}
	
	@Override
	public void setNaskqttaxprice(UFDouble value) {
		this.setAttributeValue(this.item.getNaskqttaxpriceKey(), value);
	}
	
	@Override
	public void setNassistnum(UFDouble value) {
		this.setAttributeValue(this.item.getNassistnumKey(), value);
	}
	
	@Override
	public void setNchangerate(String value) {
		this.setAttributeValue(this.item.getNchangerateKey(), value);
	}
	
	@Override
	public void setNcostmny(UFDouble value) {
		this.setAttributeValue(this.item.getNcostmnyKey(), value);
	}
	
	@Override
	public void setNcostprice(UFDouble value) {
		this.setAttributeValue(this.item.getNcostpriceKey(), value);
	}
	
	@Override
	public void setNdiscount(UFDouble value) {
		this.setAttributeValue(this.item.getNdiscountKey(), value);
	}
	
	@Override
	public void setNdiscountrate(UFDouble value) {
		this.setAttributeValue(this.item.getNdiscountrateKey(), value);
	}
	
	@Override
	public void setNexchangerate(UFDouble value) {
		this.setAttributeValue(this.item.getNexchangerateKey(), value);
	}
	
	@Override
	public void setNglobalexchgrate(UFDouble value) {
		this.setAttributeValue(this.item.getNglobalexchgrateKey(), value);
	}
	
	@Override
	public void setNglobalmny(UFDouble value) {
		this.setAttributeValue(this.item.getNglobalmnyKey(), value);
	}
	
	@Override
	public void setNglobaltaxmny(UFDouble value) {
		this.setAttributeValue(this.item.getNglobaltaxmnyKey(), value);
	}
	
	@Override
	public void setNgroupexchgrate(UFDouble value) {
		this.setAttributeValue(this.item.getNgroupexchgrateKey(), value);
	}
	
	@Override
	public void setNgroupmny(UFDouble value) {
		this.setAttributeValue(this.item.getNgroupmnyKey(), value);
	}
	
	@Override
	public void setNgrouptaxmny(UFDouble value) {
		this.setAttributeValue(this.item.getNgrouptaxmnyKey(), value);
	}
	
	@Override
	public void setNitemdiscountrate(UFDouble value) {
		this.setAttributeValue(this.item.getNitemdiscountrateKey(), value);
	}
	
	@Override
	public void setNmny(UFDouble value) {
		this.setAttributeValue(this.item.getNmnyKey(), value);
	}
	
	@Override
	public void setNnetprice(UFDouble value) {
		this.setAttributeValue(this.item.getNnetpriceKey(), value);
	}
	
	@Override
	public void setNnum(UFDouble value) {
		this.setAttributeValue(this.item.getNnumKey(), value);
	}
	
	@Override
	public void setNorigdiscount(UFDouble value) {
		this.setAttributeValue(this.item.getNorigdiscountKey(), value);
	}
	
	@Override
	public void setNorigmny(UFDouble value) {
		this.setAttributeValue(this.item.getNorigmnyKey(), value);
	}
	
	@Override
	public void setNorignetprice(UFDouble value) {
		this.setAttributeValue(this.item.getNorignetpriceKey(), value);
	}
	
	@Override
	public void setNorigprice(UFDouble value) {
		this.setAttributeValue(this.item.getNorigpriceKey(), value);
	}
	
	@Override
	public void setNorigtaxmny(UFDouble value) {
		this.setAttributeValue(this.item.getNorigtaxmnyKey(), value);
	}
	
	@Override
	public void setNorigtaxnetprice(UFDouble value) {
		this.setAttributeValue(this.item.getNorigtaxnetpriceKey(), value);
	}
	
	@Override
	public void setNorigtaxprice(UFDouble value) {
		this.setAttributeValue(this.item.getNorigtaxpriceKey(), value);
	}
	
	@Override
	public void setNprice(UFDouble value) {
		this.setAttributeValue(this.item.getNpriceKey(), value);
	}
	
	@Override
	public void setNqtnetprice(UFDouble value) {
		this.setAttributeValue(this.item.getNqtnetpriceKey(), value);
	}
	
	@Override
	public void setNqtorignetprice(UFDouble value) {
		this.setAttributeValue(this.item.getNqtorignetpriceKey(), value);
	}
	
	@Override
	public void setNqtorigprice(UFDouble value) {
		this.setAttributeValue(this.item.getNqtorigpriceKey(), value);
	}
	
	@Override
	public void setNqtorigtaxnetprc(UFDouble value) {
		this.setAttributeValue(this.item.getNqtorigtaxnetprcKey(), value);
	}
	
	@Override
	public void setNqtorigtaxprice(Object value) {
		this.setAttributeValue(this.item.getNqtorigtaxpriceKey(), value);
	}
	
	@Override
	public void setNqtprice(UFDouble value) {
		this.setAttributeValue(this.item.getNqtpriceKey(), value);
	}
	
	@Override
	public void setNqttaxnetprice(UFDouble value) {
		this.setAttributeValue(this.item.getNqttaxnetpriceKey(), value);
	}
	
	@Override
	public void setNqttaxprice(UFDouble value) {
		this.setAttributeValue(this.item.getNqttaxpriceKey(), value);
	}
	
	@Override
	public void setNqtunitnum(UFDouble value) {
		this.setAttributeValue(this.item.getNqtunitnumKey(), value);
	}
	
	@Override
	public void setNqtunitrate(String value) {
		this.setAttributeValue(this.item.getNqtunitrateKey(), value);
	}
	
	@Override
	public void setNtax(UFDouble value) {
		this.setAttributeValue(this.item.getNtaxKey(), value);
	}
	
	@Override
	public void setNtaxmny(UFDouble value) {
		this.setAttributeValue(this.item.getNtaxmnyKey(), value);
	}
	
	@Override
	public void setNtaxnetprice(UFDouble value) {
		this.setAttributeValue(this.item.getNtaxnetpriceKey(), value);
	}
	
	@Override
	public void setNtaxprice(UFDouble value) {
		this.setAttributeValue(this.item.getNtaxpriceKey(), value);
	}
	
	@Override
	public void setNtaxrate(UFDouble value) {
		this.setAttributeValue(this.item.getNtaxrateKey(), value);
	}
	
	@Override
	public void setNtotalnum(UFDouble value) {
		this.setAttributeValue(this.item.getNtotalnumKey(), value);
	}
	
	@Override
	public void setNtotalorigmny(UFDouble value) {
		this.setAttributeValue(this.item.getNtotalorigmnyKey(), value);
	}
	
	@Override
	public void setNtotalorigtaxmny(UFDouble value) {
		this.setAttributeValue(this.item.getNtotalorigtaxmnyKey(), value);
	}
	
	@Override
	public void setPk_group(String value) {
		this.setAttributeValue(this.item.getPk_group(), value);
	}
	
	@Override
	public void setPk_org(String value) {
		this.setAttributeValue(this.item.getPk_org(), value);
	}
	
	@Override
	public void setQualifiedNum(UFDouble value) {
		this.setAttributeValue(this.item.getQualifiedNumKey(), value);
	}
	
	@Override
	public void setUnQualifiedNum(UFDouble value) {
		this.setAttributeValue(this.item.getUnQualifiedNumKey(), value);
	}
	
	private int getInt(String key) {
		if (key == null) {
			return -1;
		}
		Object obj = this.bodyVO.getAttributeValue(key);
		int value = ValueUtils.getInt(obj);
		return value;
	}
	
	private String getString(String key) {
		if (key == null) {
			return null;
		}
		Object value = this.bodyVO.getAttributeValue(key);
		String str = ValueUtils.getString(value);
		return str;
	}
	
	private UFDouble getUFDoubleValue(String key) {
		if (key == null) {
			return null;
		}
		Object value = this.bodyVO.getAttributeValue(key);
		UFDouble d = ValueUtils.getUFDouble(value);
		return d;
	}
	
	private void setAttributeValue(String key, Object value) {
		this.bodyVO.setAttributeValue(key, value);
	}
	
	@Override
	public UFDouble getNcaltaxmny() {
		UFDouble value = this.getUFDoubleValue(this.item.getNcaltaxmnyKey());
		return value;
	}
	
	@Override
	public UFDouble getNdeductibleTaxrate() {
		UFDouble value = this.getUFDoubleValue(this.item
				.getNdeductibleTaxrateKey());
		return value;
	}
	
	@Override
	public UFDouble getNdeductibletax() {
		UFDouble value = this.getUFDoubleValue(this.item.getNdeductibletaxKey());
		return value;
	}
	
	@Override
	public void setNcaltaxmny(UFDouble value) {
		this.setAttributeValue(this.item.getNcaltaxmnyKey(), value);
	
	}
	
	@Override
	public void setNdeductibleTaxrate(UFDouble value) {
		this.setAttributeValue(this.item.getNdeductibleTaxrateKey(), value);
	
	}
	
	@Override
	public void setNdeductibletax(UFDouble value) {
		this.setAttributeValue(this.item.getNdeductibletaxKey(), value);
	
	}
	
	@Override
	public UFDouble getNorigtax() {
		UFDouble value = this.getUFDoubleValue(this.item.getNorigtaxKey());
		return value;
	}
	
	@Override
	public void setNorigtax(UFDouble value) {
		this.setAttributeValue(this.item.getNorigtaxKey(), value);
	
	}
	
	@Override
	public void setNusdmny(UFDouble value) {
		this.setAttributeValue(
				((IRelationDollarForItems) this.getRelationForItem()).getNusdmnyKey(),
				value);
	}
	
	@Override
	public UFDouble getNusdmny() {
		UFDouble value = this.getUFDoubleValue(((IRelationDollarForItems) this
				.getRelationForItem()).getNusdmnyKey());
		return value;
	}
	
	@Override
	public void setNqtusdprice(UFDouble value) {
		this.setAttributeValue(((IRelationDollarForItems) this
				.getRelationForItem()).getNqtusdpriceKey(), value);
	}
	
	@Override
	public UFDouble getNqtusdprice() {
		UFDouble value = this.getUFDoubleValue(((IRelationDollarForItems) this
				.getRelationForItem()).getNqtusdpriceKey());
		return value;
	}
	
	@Override
	public void setNusdprice(UFDouble value) {
		this.setAttributeValue(((IRelationDollarForItems) this
				.getRelationForItem()).getNusdpriceKey(), value);
	}
	
	@Override
	public UFDouble getNusdprice() {
		UFDouble value = this.getUFDoubleValue(((IRelationDollarForItems) this
				.getRelationForItem()).getNusdpriceKey());
		return value;
	}
	
	@Override
	public void setNusdexchgrate(UFDouble value) {
		this.setAttributeValue(((IRelationDollarForItems) this
				.getRelationForItem()).getNusdexchgrateKey(), value);
	}
	
	@Override
	public UFDouble getNusdexchgrate() {
		UFDouble value = this.getUFDoubleValue(((IRelationDollarForItems) this
				.getRelationForItem()).getNusdexchgrateKey());
		return value;
	}
	
	@Override
	public void setCusdcurrencyid(String value) {
		this.setAttributeValue(((IRelationDollarForItems) this
				.getRelationForItem()).getCusdcurrencyidKey(), value);
	}
	
	@Override
	public String getCusdcurrencyid() {
		String value = this.getString(((IRelationDollarForItems) this
				.getRelationForItem()).getCusdcurrencyidKey());
		return value;
	}
	
	/**
	 * 
	 * 计算之前根据传入的数据组装计算的依据
	 * @param itemVO
	 * @return
	 */
	public String[] calculateBefor(CtAbstractBVO itemVO){
		List<String> calKey = new ArrayList<String>();
		//检验可联动计算条件
		UFDouble nastnum = itemVO.getNastnum();  //数量
		UFDouble nnum = itemVO.getNnum(); //主数量
		UFDouble nqtorigprice = itemVO.getNqtorigprice();//无税单价  norigtaxprice
		UFDouble nqtorigtaxprice = itemVO.getNqtorigtaxprice();  //含税单价 
		UFDouble norigmny = itemVO.getNorigmny();   //无税金额
		UFDouble norigtaxmny = itemVO.getNorigtaxmny();  //价税合计
		
		boolean numisNull = (nastnum == null || UFDouble.ZERO_DBL.compareTo(nastnum) == 0) &&
				(nnum == null || UFDouble.ZERO_DBL.compareTo(nnum) == 0);
		boolean priceisNull = (nqtorigprice == null || UFDouble.ZERO_DBL.compareTo(nqtorigprice) == 0) &&
				(nqtorigtaxprice == null || UFDouble.ZERO_DBL.compareTo(nqtorigtaxprice) == 0);
		boolean mnyisNull = (norigmny == null || UFDouble.ZERO_DBL.compareTo(norigmny) == 0) &&
				(norigtaxmny == null || UFDouble.ZERO_DBL.compareTo(norigtaxmny) == 0);
		
		if((numisNull && priceisNull) || (numisNull && mnyisNull) || (priceisNull && mnyisNull)){
			ExceptionUtils.wrappBusinessException("表体数量（数量、主数量），单价（无税单价、含税单价），金额（无税金额、价税合计）不能同时为空，" +
					"必须满足两个参数中的其中一个值有值。");
		}
		if(numisNull){ //单价 、金额
			if(nqtorigprice != null)
				calKey.add(CtAbstractBVO.NQTORIGPRICE);
			if(nqtorigtaxprice != null)
				calKey.add(CtAbstractBVO.NQTORIGTAXPRICE);
			if(norigmny != null){
				calKey.add(CtAbstractBVO.NORIGMNY);
			}
			if(norigtaxmny != null){
				calKey.add(CtAbstractBVO.NORIGTAXMNY);
			}
		}else if(priceisNull){  //数量、 金额
			if(nastnum != null)
				calKey.add(CtAbstractBVO.NASTNUM);
			if(nnum != null)
				calKey.add(CtAbstractBVO.NNUM);
			if(norigmny != null){
				calKey.add(CtAbstractBVO.NORIGMNY);
			}
			if(norigtaxmny != null){
				calKey.add(CtAbstractBVO.NORIGTAXMNY);
			}
		}else if(mnyisNull){  //数量 、单价
			if(nastnum != null)
				calKey.add(CtAbstractBVO.NASTNUM);
			if(nnum != null)
				calKey.add(CtAbstractBVO.NNUM);
			if(nqtorigprice != null)
				calKey.add(CtAbstractBVO.NQTORIGPRICE);
			if(nqtorigtaxprice != null)
				calKey.add(CtAbstractBVO.NQTORIGTAXPRICE);
		}
		return calKey.toArray(new String[calKey.size()]);
	}
}