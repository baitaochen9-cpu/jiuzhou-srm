/** <a href="http://www.cpupk.com/decompiler">Eclipse Class Decompiler</a> plugin, Copyright (c) 2017 Chen Chao. */
package nc.impl.ic.m4z;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.framework.common.NCLocator;
import nc.bs.ic.onhand.view.OnhandQueryView;
import nc.impl.pubapp.pattern.pub.LockOperator;
import nc.itf.ic.m4z.IFreezeCheck;
import nc.itf.ic.m4z.IFreezeThaw;
import nc.itf.ic.m4z.IFreezeThawQuery;
import nc.pubitf.ic.onhand.IOnhandQry;
import nc.pubitf.para.SysInitQuery;
import nc.pubitf.qc.c001.ic.IPushSaveFor4Z;
import nc.vo.ic.m4z.entity.FreezeThawAggVO;
import nc.vo.ic.m4z.entity.FreezeThawVO;
import nc.vo.ic.onhand.entity.OnhandDimVO;
import nc.vo.ic.onhand.entity.OnhandVO;
import nc.vo.ic.onhand.pub.OnhandQryCond;
import nc.vo.ic.pub.lang.OnhandRes;
import nc.vo.ic.pub.util.NCBaseTypeUtils;
import nc.vo.ic.pub.util.ValueCheckUtil;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.pattern.data.ValueUtils;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.pubapp.pattern.pub.SqlBuilder;

import org.apache.commons.lang.StringUtils;

/**
 * 冻结、解冻报检实现(报检时,应报检同一批次所有存量,解决方案为,根据现存量维度查询当前批次的物料,报检所有数量)
 * 
 * @since 6.0
 * @version 2011-2-21 上午11:30:24
 * @author chenlla
 */

public class FreezeCheckImpl implements IFreezeCheck {

    @Override
    public void check(FreezeThawVO[] vos) throws BusinessException {
		if (ValueCheckUtil.isNullORZeroLength(vos)) {
			return;
		}
		FreezeThawVO[] reQueryOnhandVOs = this.reQueryOnhandVOs(vos);
		if (ValueCheckUtil.isNullORZeroLength(reQueryOnhandVOs)) {
			return;
		}
      
		//wangsyf 2015-05-11  获取是否自动冻结参数 
		UFBoolean freezable = SysInitQuery.getParaBoolean(vos[0].getPk_org(),"PROIC002");
		/**
		 * 是否外系统质检 2022年11月22日
		 * 如果是外系统质检则不推NC的报检单
		 */
		UFBoolean isoutLims = SysInitQuery.getParaBoolean(vos[0].getPk_org(),"YFQC01");
		IFreezeThaw freezeThaw = null;
		FreezeThawVO[] retvos = null;
		// 医药版本修改，现不管是否自动冻结参数，都走水平的冻结，唯一区别在于推报检单之后的解冻 modefied by wumink
		// 执行冻结
		freezeThaw = NCLocator.getInstance().lookup(IFreezeThaw.class);
		retvos = freezeThaw.freeze(reQueryOnhandVOs);

		List<String> pks = new ArrayList<>();
		FreezeThawAggVO[] aggVOs = new FreezeThawAggVO[retvos.length];
		for (int i = 0; i < retvos.length; i++) {
			aggVOs[i] = new FreezeThawAggVO();
			aggVOs[i].setParent(retvos[i]);
			aggVOs[i].setChildrenVO(new FreezeThawVO[] { retvos[i] });
			pks.add(retvos[i].getPrimaryKey());
		}
		/**
		 * 执行库存检验
		 * 如果是外系统质检则不推NC的报检单2022年11月22日
		 */
		if(!isoutLims.booleanValue()){
			IPushSaveFor4Z checkPush = NCLocator.getInstance().lookup(IPushSaveFor4Z.class);
			checkPush.pushSave(aggVOs);
		}

		// 自动冻结参数为否，进行解冻
		if (!freezable.booleanValue()) {
			IFreezeThawQuery service = NCLocator.getInstance().lookup(IFreezeThawQuery.class);
			StringBuilder whereSql = new StringBuilder();
			FreezeThawVO vo = new FreezeThawVO();
			whereSql.append("from " + vo.getTableName() + " where ");
			whereSql.append(" " + FreezeThawVO.CFREEZEID + " in ('");
			whereSql.append(StringUtils.join(pks.toArray(new String[0]), "', '"));
			whereSql.append("')");
			freezeThaw.thaw(service.queryThawOnhand(whereSql.toString()));
		}
    }

  
	private FreezeThawVO[] reQueryOnhandVOs(FreezeThawVO[] vos)
			throws BusinessException {
		// 得到现存量维度pk
		Map<String, FreezeThawVO> freezeMap = new HashMap<>();
		if (vos == null || vos.length <= 0) {
			return null;
		}
		for (FreezeThawVO vo : vos) {
			freezeMap.put(vo.getPk_onhanddim(), vo);
		}
		if (freezeMap == null || freezeMap.size() <= 0) {
			return null;
		}
		new LockOperator().lock(freezeMap.keySet().toArray(new String[0]), OnhandRes.getOnhandlockErr());
		IOnhandQry onhandQry = NCLocator.getInstance().lookup(IOnhandQry.class);
		OnhandQryCond cond = new OnhandQryCond();
		cond.addAllSelectFields();
		cond.addSelectFields(new String[] { OnhandDimVO.PK_ONHANDDIM });
		SqlBuilder builder = new SqlBuilder();
		builder.append(OnhandQueryView.ViewAlias + "." + OnhandDimVO.PK_ONHANDDIM, freezeMap.keySet().toArray(new String[0]));
		cond.setWhere(builder.toString());
		OnhandVO[] onhandVOs = onhandQry.queryOnhand(cond);
		if (ValueCheckUtil.isNullORZeroLength(onhandVOs)) {
			return null;
		}

		FreezeThawVO[] freezeVO = new FreezeThawVO[onhandVOs.length];
		UFDouble num = null;
		for (int i = 0; i < onhandVOs.length; i++) {
			FreezeThawVO clientVO = freezeMap.get(onhandVOs[i].getPk_onhanddim());
			freezeVO[i] = new FreezeThawVO();
			// 集团、组织
			freezeVO[i].setPk_group(onhandVOs[i].getPk_group());
			freezeVO[i].setPk_org(onhandVOs[i].getPk_org());
			// 设置冻结相关项的值(维度pk)
			freezeVO[i].setPk_onhanddim(onhandVOs[i].getPk_onhanddim());
			// 设置主数量、数量、毛重(主数量-已冻结数量)
			num = NCBaseTypeUtils.sub(onhandVOs[i].getNonhandnum(), onhandVOs[i].getNlocknum());
			freezeVO[i].setNnum(num);
			freezeVO[i].setNfrznum(num);
			if (num == null || num.doubleValue() == 0) {
				ExceptionUtils.wrappBusinessException("报检主数量不可为空。");
			}
			num = NCBaseTypeUtils.sub(onhandVOs[i].getNonhandastnum(), onhandVOs[i].getNlockastnum());
			freezeVO[i].setNassistnum(num);
			freezeVO[i].setNfrzastnum(num);
			num = NCBaseTypeUtils.sub(onhandVOs[i].getNgrossnum(), onhandVOs[i].getNlockgrossnum());
			freezeVO[i].setNgrossnum(num);
			freezeVO[i].setNfrzgrsnum(num);

			// iwholeline勾选即为全量报检，没勾选即为部分报检
			if(ValueUtils.getUFBoolean(clientVO.getAttributeValue("iwholeline_148")).booleanValue()){
				freezeVO[i].setNfrznum(null);
				freezeVO[i].setNfrzastnum(null);
				freezeVO[i].setNfrzgrsnum(null);
				freezeVO[i].setAttributeValue("iwholeline_148", UFBoolean.TRUE);
							
			}else {
				freezeVO[i].setNfrznum(clientVO.getNfrznum());
				freezeVO[i].setNfrzastnum(clientVO.getNfrzastnum());
				freezeVO[i].setNfrzgrsnum(clientVO.getNfrzgrsnum());
				freezeVO[i].setAttributeValue("iwholeline", UFBoolean.FALSE);
			}
				
			freezeVO[i].setVchangerate(clientVO.getVchangerate());
			freezeVO[i].setCunitid(clientVO.getCunitid());
			freezeVO[i].setCastunitid(clientVO.getCastunitid());
		}
		return freezeVO;
	}

}
