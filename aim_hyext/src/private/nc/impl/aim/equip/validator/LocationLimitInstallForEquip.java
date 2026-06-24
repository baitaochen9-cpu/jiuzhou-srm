package nc.impl.aim.equip.validator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nc.impl.aim.validator.LocationLimitInstallValidator;
import nc.itf.aim.pub.IEquipService;
import nc.itf.am.pub.ILocationService;
import nc.vo.aim.equip.EquipHeadVO;
import nc.vo.am.common.AbstractAggBill;
import nc.vo.am.common.util.ArrayUtils;
import nc.vo.am.common.util.MapUtils;
import nc.vo.am.common.util.MultiLanguageUtil;
import nc.vo.am.common.util.StringUtils;
import nc.vo.am.constant.BillStatusConst;
import nc.vo.am.location.InstallLimitConst;
import nc.vo.am.location.LocationVO;
import nc.vo.am.manager.LockManager;
import nc.vo.am.proxy.AMProxy;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;

/**
 * 针对设备对位置安装进行校验
 * @version 6.3
 * @author wukq
 * @date 2012-12-17
 */
public class LocationLimitInstallForEquip<E extends AbstractAggBill> extends LocationLimitInstallValidator {
	 

	public LocationLimitInstallForEquip(String locationFiled) {
		super(locationFiled);
	}

	protected void validateBillVO(AggregatedValueObject billVO)
			throws Exception {
		// 校验表头
		validatorForHead(billVO);
	}

	@Override
	public boolean getPerformModeContinue() {
		return true;
	}

	/**
	 * 设备卡片由于复制会有多个设备的情况，如果第一个设备位置为只能安装一个则只需校验一次就可以，无需校验多次
	 * 并且数量大于1，选择的是唯一安装设备的位置，则不允许复制
	 * 
	 * @param billVO
	 * @return
	 */
	protected void validateBillVOs(AggregatedValueObject[] billVOs) throws Exception {
		if (!ArrayUtils.isEmpty(billVOs)) {
			if(billVOs.length > 1){
				String errInfo = validatorCanCopy(billVOs[0]);
				if(StringUtils.isNotBlank(errInfo)){
					throw new BusinessException(errInfo);				
				}
			}
			for (AggregatedValueObject billVO : billVOs) {
				validateBillVO(billVO);
			}
		}
	}
	
	
	private void validatorForHead(AggregatedValueObject aggVO)
			throws BusinessException {
		// 表体位置主键
		String[] pk_locations = new String[]{(String) aggVO.getParentVO().getAttributeValue(EquipHeadVO.PK_LOCATION)};
		try {
			if(StringUtils.isNotEmpty((String)aggVO.getParentVO().getAttributeValue(EquipHeadVO.PK_LOCATION))){
				LockManager.lockString(pk_locations);//对位置主键进行加锁
			}
		} catch (Exception e) {
			throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("equip_0","04510004-0376")/*@res "发生并发操作，请稍后再试"*/);
		}
		// 检查安装限制 ,设备卡片如果要修改位置必须弃审，而去数据库查询是必须审批通过所以这里判断库里超过一个就要提示
		String pk_equip = (String)aggVO.getParentVO().getAttributeValue(EquipHeadVO.PK_EQUIP);
		String errStrInfo = null;
		if(StringUtils.isEmpty(pk_equip)){
			errStrInfo = checkInstallLimitedForInsert(pk_locations);
		}else{ 
			errStrInfo = checkInstallLimitedForUpdate(pk_locations);
		}
		if(StringUtils.isNotBlank(errStrInfo)){
			appendErrInfo(errStrInfo);
		}
	}
	
	/**
	 * 校验是否能复制
	 * @param aggVO
	 * @throws BusinessException
	 */
	private String validatorCanCopy(AggregatedValueObject aggVO)
			throws BusinessException {
		// 表体位置主键
		String[] pk_locations = new String[]{(String) aggVO.getParentVO().getAttributeValue(EquipHeadVO.PK_LOCATION)};
		// 检查安装限制
		String errStrInfo = null;
	
		if(StringUtils.isNotEmpty((String)aggVO.getParentVO().getAttributeValue(EquipHeadVO.PK_LOCATION))){
				LockManager.lockString(pk_locations);//对位置主键进行加锁
		}
		errStrInfo = checkInstallLimitedForCopy(pk_locations);
		return errStrInfo;
	}
	
	/**
	 * 
	 * @param pk_locations
	 * @return
	 * @throws BusinessException
	 */
	public static String checkInstallLimitedForInsert(String[] pk_locations)
			throws BusinessException {
		StringBuffer errInfo = new StringBuffer();
		// 表体位置主键出现个数
		Map<String, Integer> locationPkCountMap = new HashMap<String, Integer>();
		// 表体位置VO
		Map<String, LocationVO> locationVOMap = AMProxy.lookup(
				ILocationService.class).queryLocationVOs(pk_locations);
		if(MapUtils.isEmpty(locationVOMap)){
			return null;
		}
		for (String locationPk1 : pk_locations) {
			for (String locationPk2 : pk_locations) {
				int flg = 0;
				if (StringUtils.equals(locationPk1, locationPk2)) {
					locationPkCountMap.put(locationPk1, ++flg);
				}
			}
		}
		Set<String> locationPks = locationPkCountMap.keySet();
		for (String locationPk : locationPks) {
			
			LocationVO locationVO = locationVOMap.get(locationPk);
			String locationName = MultiLanguageUtil.getMultiLanguageValue(
					locationVO, LocationVO.LOCATION_NAME);
			if (locationVO.getInstall_limit() == InstallLimitConst.non) {
				errInfo.append(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("equip_0","04510004-0377",null,new String[]{locationName})/*@res "指定位置[{0}]不支持设备安装，请更换位置！"*/);
				break;
			} else if (locationVO.getInstall_limit() == InstallLimitConst.single) {
				EquipHeadVO[] equips = AMProxy.lookup(IEquipService.class).queryEquipHeadVOs(" from "+EquipHeadVO.TABLE_NAME+" where "+EquipHeadVO.PK_LOCATION + " ='" + locationPk + "' and pam_equip.card_status = "+BillStatusConst.check_pass, new String[]{EquipHeadVO.PK_EQUIP});
				if (locationPkCountMap.get(locationPk) > 1) {
					errInfo.append(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("equip_0","04510004-0378",null,new String[]{locationName})/*@res "指定位置[{0}]只能安装一个设备!"*/);
				} else if (ArrayUtils.isNotEmpty(equips) && equips.length >=1) {
					errInfo.append(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("equip_0","04510004-0379",null,new String[]{locationName})/*@res "指定位置[{0}]已有设备安装，不能再安装其他设备!")*/);
				}
			}
		}
		return errInfo.toString();
	}
	
	
	/**
	 * 
	 * @param pk_locations
	 * @return
	 * @throws BusinessException
	 */
	public static String checkInstallLimitedForCopy(String[] pk_locations)
			throws BusinessException {
		StringBuffer errInfo = new StringBuffer();
	
		// 表体位置VO
		Map<String, LocationVO> locationVOMap = AMProxy.lookup(
				ILocationService.class).queryLocationVOs(pk_locations);
		if(MapUtils.isEmpty(locationVOMap)){
			return null;
		}
		
		for (String locationPk : locationVOMap.keySet()) {
	
			LocationVO locationVO = locationVOMap.get(locationPk);
			String locationName = MultiLanguageUtil.getMultiLanguageValue(
					locationVO, LocationVO.LOCATION_NAME);
			if (locationVO.getInstall_limit() == InstallLimitConst.non) {
				errInfo.append(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("equip_0","04510004-0377",null,new String[]{locationName})/*@res "指定位置[{0}]不支持设备安装，请更换位置！"*/);
				break;
			} else if (locationVO.getInstall_limit() == InstallLimitConst.single) {
				errInfo.append(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("equip_0","04510004-0377",null,new String[]{locationName})/*@res "指定位置[{0}]只能安装一个设备,不能同时安装多个设备,请更换位置"*/);
			}
		}
		return errInfo.toString();
	}
	
	/**
	 * 
	 * @param pk_locations
	 * @return
	 * @throws BusinessException
	 */
	public static String checkInstallLimitedForUpdate(String[] pk_locations)
			throws BusinessException {
		
		StringBuffer errInfo = new StringBuffer();
		// 表体位置主键出现个数
		Map<String, Integer> locationPkCountMap = new HashMap<String, Integer>();
		// 表体位置VO
		Map<String, LocationVO> locationVOMap = AMProxy.lookup(
				ILocationService.class).queryLocationVOs(pk_locations);
		if(MapUtils.isEmpty(locationVOMap)){
			return null;
		}
		for (String locationPk1 : pk_locations) {
			for (String locationPk2 : pk_locations) {
				int flg = 0;
				if (StringUtils.equals(locationPk1, locationPk2)) {
					locationPkCountMap.put(locationPk1, ++flg);
				}
			}
		}
		Set<String> locationPks = locationPkCountMap.keySet();
		for (String locationPk : locationPks) {
			LocationVO locationVO = locationVOMap.get(locationPk);		
			String locationName = MultiLanguageUtil.getMultiLanguageValue(
					locationVO, LocationVO.LOCATION_NAME);
			if (locationVO.getInstall_limit() == InstallLimitConst.non) {
				errInfo.append(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("equip_0","04510004-0377",null,new String[]{locationName})/*@res "指定位置[{0}]不支持设备安装，请更换位置！"*/);
				break;
			} else if (locationVO.getInstall_limit() == InstallLimitConst.single) {
				//在资产卡片中查找包含该位置信息的已通过审核的资产
				EquipHeadVO[] equips = AMProxy.lookup(IEquipService.class).queryEquipHeadVOs(" from "+EquipHeadVO.TABLE_NAME+" where "+EquipHeadVO.PK_LOCATION + " ='" + locationPk + "' and pam_equip.card_status = "+BillStatusConst.check_pass, new String[]{EquipHeadVO.PK_EQUIP});
				if (locationPkCountMap.get(locationPk) > 1) {
					errInfo.append(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("equip_0","04510004-0378",null,new String[]{locationName})/*@res "指定位置[{0}]只能安装一个设备!"*/);
				} else if (ArrayUtils.isNotEmpty(equips) && equips.length >=1) {
					/****************************bbt 2023.12.15***********************************************/
//					//原逻辑
//					errInfo.append(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("equip_0","04510004-0379",null,new String[]{locationName})/*@res "指定位置[{0}]已有设备安装，不能再安装其他设备!")*/);
					
					//新逻辑
					//冲突的位置编码+名称
					String errorLocation = locationVO.getLocation_code() + "（" + locationVO.getLocation_name() + "）" ;
					//冲突位置的资产编码+名称
					EquipHeadVO[] equipsCodeAndName = AMProxy.lookup(IEquipService.class).queryEquipHeadVOs(" from "+EquipHeadVO.TABLE_NAME+" where "+EquipHeadVO.PK_LOCATION + " ='" + locationPk + "' and pam_equip.card_status = "+BillStatusConst.check_pass, 
							new String[]{EquipHeadVO.EQUIP_CODE,EquipHeadVO.EQUIP_NAME});
					String errorEquipCodeAndName = "";
					errorEquipCodeAndName = equipsCodeAndName[0].getEquip_code() + 
						"（" + equipsCodeAndName[0].getEquip_name() + "）";
							
					
					errInfo.append("指定位置已有设备安装，不能再安装其他设备!\n" + 
					"位置信息如下：" + errorLocation + "\n" + 
					"设备信息如下：" + errorEquipCodeAndName);
					
					
					/***************************************************************************/
				}
			}
		}
		return errInfo.toString();
	}


}