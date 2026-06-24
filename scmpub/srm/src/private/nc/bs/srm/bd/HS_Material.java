package nc.bs.srm.bd;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import nc.bs.bank_cvp.compile.registry.BussinessMethods;
import nc.bs.dao.DAOException;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.srm.pub.SenderQuerys;
import nc.cmp.tools.StringUtil;
import nc.impl.pubapp.pattern.data.vo.VOQuery;
import nc.itf.bd.bankacc.base.IBankAccBaseInfoService;
import nc.itf.bd.goodscode.IGoodscodeService;
import nc.itf.bd.material.baseinfo.IMaterialBaseInfoService;
import nc.itf.bd.pubinfo.ILinkmanService;
import nc.itf.bd.supplier.baseinfo.ISupplierBaseInfoService;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.vo.arap.bill.util.VORowNoUtils;
import nc.vo.bd.bankaccount.BankAccSubVO;
import nc.vo.bd.bankaccount.BankAccbasVO;
import nc.vo.bd.bankdoc.BankdocVO;
import nc.vo.bd.cust.CustbankVO;
import nc.vo.bd.goodscode.GoodscodeVO;
import nc.vo.bd.goodscode.GoodstaxesItemVO;
import nc.vo.bd.linkman.LinkmanVO;
import nc.vo.bd.material.MaterialVO;
import nc.vo.bd.supplier.SupLinkmanVO;
import nc.vo.bd.supplier.SupplierVO;
import nc.vo.ic.m4q.entity.LocAdjustBodyVO;
import nc.vo.ic.m4q.entity.LocAdjustHeadVO;
import nc.vo.ic.m4q.entity.LocAdjustVO;
import nc.vo.ic.pub.define.ICPubMetaNameConst;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVOUtil;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.sm.UserVO;
import nccloud.api.jzsrm.AbstracProcessor4Ext;
import bsh.ParseException;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 关务信息推送erp
 * 
 * @author Solomon
 * 
 */
public class HS_Material extends AbstracProcessor4Ext {

	private SenderQuerys sqy = new SenderQuerys();

	@Override
	public JSONObject process(Object billvo) throws Exception {
		
		JSONObject reqJson = (JSONObject) billvo;
		InvocationInfoProxy.getInstance().setUserCode("GW");
		JSONObject data = reqJson.getJSONObject("data");
		MaterialVO[] inbills = processBill(data);
		JSONObject rs = new JSONObject();
		String vbillcode = inbills[0].getCode();
		rs.put("erp_code", vbillcode);
		return this.getRsultDataSuccess(rs, "新增物料成功或修改物料成功");
	}

	public MaterialVO[] processBill(JSONObject jsonObject)
			throws BusinessException {
		MaterialVO[] apprveddBills = null;
		MaterialVO outbills = null;
		try {

			// 传过来的数据生成物料
			outbills = getInvCountBillVO(jsonObject);
			// 保存
			apprveddBills = savePurchaseInVO(outbills);

		} catch (Exception e) {
			ExceptionUtils.wrappBusinessException("关务信息后回写ERP出错："
					+ e.getMessage());
		}
		return apprveddBills;
	}

	private MaterialVO getInvCountBillVO(JSONObject jsonObject)
			throws Exception {
		String code = jsonObject.getString("code");
		if (StringUtil.isEmpty(code)) {
			throw new BusinessException("物料编码不能为空");
		}

		String def15 = jsonObject.getString("def15");
//		if (StringUtil.isNotEmpty(def15)) {
//			throw new BusinessException("检验检疫类别不能为空");
//		}
		String def16 = jsonObject.getString("def16");
//		if (StringUtil.isNotEmpty(def16)) {
//			throw new BusinessException("海关监管条件不能为空");
//		}
		String pk_goodscode = jsonObject.getString("pk_goodscode");
		if (StringUtil.isEmpty(pk_goodscode)) {
			throw new BusinessException("海关商品代码不能为空");
		}
		ArrayList<MaterialVO> mateVOList = (ArrayList<MaterialVO>) getDao()
				.retrieveByClause(MaterialVO.class, " code = '" + code + "' ");
		if (mateVOList == null || mateVOList.size() == 0) {
			throw new BusinessException("未找到对应的物料信息");
		}
		MaterialVO materialVO = mateVOList.get(0);
		materialVO.setDef19("1");
		List<GoodscodeVO> goodsVOs = (List<GoodscodeVO>)getDao().retrieveByClause(GoodscodeVO.class, " code  = '"+pk_goodscode+"'  or name like '%"+materialVO.getName()+"%'");

		GoodscodeVO goodsVo = null;
		boolean falg = true;
		if(goodsVOs ==null || goodsVOs.size() ==0){
			goodsVo = new GoodscodeVO();
			goodsVo.setEnablestate(2);
			goodsVo.setStatus(VOStatus.NEW);
		}else{
			goodsVo = goodsVOs.get(0);
			goodsVo.setEnablestate(2);
			goodsVo.setStatus(VOStatus.UPDATED);
			falg = false;
		}
		goodsVo.setDef1(def15);
		goodsVo.setDef2(def16);
		// 海关商品代码信息
		goodsVo.setCode(pk_goodscode);
		goodsVo.setPk_group(materialVO.getPk_group());
		goodsVo.setPk_org("GLOBLE00000000000000");
		goodsVo.setName(materialVO.getName());
		goodsVo.setPk_customunit(materialVO.getPk_measdoc());// 单位
		// 海关商品税率
		JSONObject taxest = jsonObject.getJSONObject("taxests");
		
		List<GoodstaxesItemVO> goodsItem = (List<GoodstaxesItemVO>)getDao().retrieveByClause(GoodstaxesItemVO.class, "  pk_goodscode = '"+materialVO.getPk_goodscode()+"'");
		GoodstaxesItemVO taxesVo = null;
		if(goodsItem ==null || goodsItem.size() ==0){
			taxesVo = new GoodstaxesItemVO();
			taxesVo.setStatus(VOStatus.NEW);
		}else{
			taxesVo = goodsItem.get(0);
			taxesVo.setStatus(VOStatus.UPDATED);
		}

		String favoredtaxes = taxest.getString("favoredtaxes");// 最惠国税率

		taxesVo.setFavoredtaxes(new UFDouble(favoredtaxes));

		String commontaxes = taxest.getString("commontaxes");// 普通税率
		taxesVo.setCommontaxes(new UFDouble(commontaxes));

		String consumetaxes = taxest.getString("consumetaxes");// 消费税率
		taxesVo.setConsumetaxes(new UFDouble(consumetaxes));

		String risetaxes = taxest.getString("risetaxes");// 增值税率
		taxesVo.setRisetaxes(new UFDouble(risetaxes));

		String supervise = taxest.getString("supervise");// 监管条件
		taxesVo.setSupervise(supervise);

		String exporttaxes = taxest.getString("exporttaxes");// 出口关税率
		taxesVo.setExporttaxes(new UFDouble(exporttaxes));
		
		String exportaxesback = taxest.getString("exportaxesback");// 出口关税率
		taxesVo.setExportaxesback(new UFDouble(exportaxesback));
		
		UFDateTime startdate2 = taxesVo.getTs();
		if (startdate2 == null) {
			taxesVo.setStartdate(new UFDate().asLocalBegin());
		} else {
			taxesVo.setStartdate(startdate2.getBeginDate());
		}
		taxesVo.setEnddate(new UFDate("2125-12-31"));

		goodsVo.setPk_goodstaxes(new GoodstaxesItemVO[]{ taxesVo });
		IGoodscodeService service = NCLocator.getInstance().lookup(
				IGoodscodeService.class);
		GoodscodeVO insertGoodscodeVO = null;
		
		if (falg) {
		
			insertGoodscodeVO = service.insertGoodscodeVO(goodsVo);
		} else {
			
			insertGoodscodeVO = service.updateGoodscodeVO(goodsVo);
		}

		materialVO.setPk_goodscode(insertGoodscodeVO.getPk_goodscode());
		return materialVO;
	}

	private MaterialVO[] savePurchaseInVO(MaterialVO vo)
			throws BusinessException {
		IMaterialBaseInfoService lookup = NCLocator.getInstance().lookup(
				IMaterialBaseInfoService.class);
		MaterialVO[] resVO = lookup.updateMaterial(vo);
		return resVO;
	}

}
