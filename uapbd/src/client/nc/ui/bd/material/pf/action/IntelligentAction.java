package nc.ui.bd.material.pf.action;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.dao.DAOException;
import nc.bs.framework.common.NCLocator;
import nc.itf.material.mdm.SendMdmImp;
import nc.itf.material.mdm.SendMdmItf;
import nc.md.persist.framework.IMDPersistenceService;
import nc.uap.lfw.util.StringUtil;
import nc.ui.uif2.NCAction;
import nc.ui.uif2.actions.RefreshAction;
import nc.ui.uif2.model.AbstractAppModel;
import nc.ui.uif2.model.BillManageModel;
import nc.vo.bd.material.MaterialVO;
import nc.vo.material.mdm.SendMdmPropetys;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;

public class IntelligentAction extends NCAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected AbstractAppModel model = null;
	private RefreshAction refreshAction = null;
	SendMdmItf itf = null;

	public IntelligentAction() {
		setBtnName("智能匹配");
		setCode("Intelligent");
	}

	@Override
	public void doAction(ActionEvent e) throws Exception {

		Object[] selectDatas = this.getSelectDatas();
		if (null == selectDatas) {
			return;
		}
		Map<String, List<MaterialVO>> dataBycode = getDataBycode(selectDatas);// 待更新数据
		// 通过查询主数据分组出已有主数据注册与未注册信息，未注册的直接去注册主表，已注册的直接放到另外的列表里且赋值
		Map<String, List<MaterialVO>> mdmhead = new HashMap<String, List<MaterialVO>>();// 缓存需要更新主数据的数据
		Map<String, List<MaterialVO>> mdmbody = new HashMap<String, List<MaterialVO>>();// 缓存需要更新辅表的数据

		this.getData_h(dataBycode, mdmhead, mdmbody);
		// return;

		Map<String, Map<String, String>> insetMdmMaterialHost = null;
		// 更新主表
		SendMdmItf itf = this.getSever();
		for (String pk_org : mdmhead.keySet()) {
			MaterialVO[] array = mdmhead.get(pk_org).toArray(new MaterialVO[0]);
			insetMdmMaterialHost = itf.insetMdmMaterialHost(array, pk_org);

		}
		// 重新处理辅表的主数据赋值
		this.getData_b( insetMdmMaterialHost, mdmbody);

		for (String pk_org : mdmbody.keySet()) {
			UFBoolean insetMdmMaterialAssist = getSever()
					.insetMdmMaterialAssist(
							mdmbody.get(pk_org).toArray(new MaterialVO[0]),
							pk_org);
			IMDPersistenceService imdp = NCLocator.getInstance().lookup(
					IMDPersistenceService.class);
			imdp.updateBillWithAttrs(
					mdmbody.get(pk_org).toArray(new MaterialVO[0]),
					new String[] { "def7", "def14" });
			// int updateVOArray = getDao().updateVOArray(materials,
			// new String[] { "def7", "def14" });
		}
		//
		// List<MaterialVO> list_b =
		// this.getData_b(dataBycode,insetMdmMaterialHost);
		refreshAction.doAction(e);
	}

	private SendMdmItf getSever() {
		// TODO Auto-generated method stub
		if (null == itf) {
			itf = NCLocator.getInstance().lookup(SendMdmItf.class);
		}
		return itf;
	}

	/**
	 * 补全辅表数据
	 * @param insetMdmMaterialHost  //主表返回列表  物料ID: 【主数据编码，统一分类 】
	 * @param dataBycode    //辅表待同步数据  组织：【物料··】
	 * @return
	 */
	private void getData_b(
			Map<String, Map<String, String>> insetMdmMaterialHost,
			Map<String, List<MaterialVO>> dataBycode) {
		if(null == insetMdmMaterialHost || insetMdmMaterialHost.size() == 0 ){
			return ;
		}
		
		Map<String,Map<String,String>> map = new HashMap<String,Map<String,String>>();
		for(String pk : insetMdmMaterialHost.keySet()){
			Map<String, String> map2 = insetMdmMaterialHost.get(pk);
			String material_code = map2.get("material_code");
			String code_ = this. subMaterialcode(material_code);
			map.put(code_, map2);
		}
		
//		List<MaterialVO> list_b = new ArrayList<MaterialVO>();·
		for (String key : dataBycode.keySet()) {
			List<MaterialVO> list = dataBycode.get(key);
			for(MaterialVO vo : list){
				String code = vo.getCode();
				String vocode_ =  this. subMaterialcode(code);
				Map<String, String> map2 = map.get(vocode_);
				if(null != map2){
					vo.setDef7(map2.get(SendMdmPropetys.MDMCODE));
					vo.setDef14(map2.get(SendMdmPropetys.MDMCLASS));
				}
			}
		}
	
	}

	/**
	 * 
	 * @param dataBycode
	 * @param mdmhead
	 * @param mdmbody
	 * @throws BusinessException
	 */
	private void getData_h(Map<String, List<MaterialVO>> dataBycode,
			Map<String, List<MaterialVO>> mdmhead,
			Map<String, List<MaterialVO>> mdmbody) throws BusinessException {
		/*
		 * 此方法会被执行两次，第一次执行拆分主表信息和子表信息，会跳过主表信息，为子表添加主数据编码的分类
		 */
		// Map<String, List<MaterialVO>> rst = new HashMap<String,
		// List<MaterialVO>>();
		// List<MaterialVO> list_h = new ArrayList<MaterialVO>();
		SendMdmItf itf = getSever();
		for (String key : dataBycode.keySet()) {
			Map<String, List<String>> conds = new HashMap<String, List<String>>();
			List<String> cond = new ArrayList<String>();

			cond.add("and");
			cond.add("like");
			cond.add("'%" + key + "'");
			conds.put("pk_material", cond);
			List<Map<String, String>> queryMdmPrimary = itf
					.queryMdmPrimary(conds);

			if (null == queryMdmPrimary || queryMdmPrimary.size() == 0) { // 主数据找不到对应数据
				List<MaterialVO> list = dataBycode.get(key);
				for (int i = 0; i < list.size(); i++) { // 选择一条数据放到主表里，其它的放到辅表里
					MaterialVO materialVO = list.get(i);
					String pk_org = materialVO.getPk_org();
					if (i == 0) {
						List<MaterialVO> list2 = mdmhead.get(pk_org);
						if (null == list2) {
							list2 = new ArrayList<MaterialVO>();
						}
						list2.add(materialVO);
						mdmhead.put(pk_org, list2);
					} else {

						List<MaterialVO> list2 = mdmbody.get(pk_org);
						if (null == list2) {
							list2 = new ArrayList<MaterialVO>();
						}
						list2.add(materialVO);
						mdmbody.put(pk_org, list2);
					}
				}
			} else { // 如果 从主数据系统 找到了相关主数据，那就要放到全部放到辅表里去，
						// 处理一下主数据主表返回的结果集团
				Map<String, Map<String, String>> mdm = laodmdm(queryMdmPrimary);

				for (String key1 : dataBycode.keySet()) {
					List<MaterialVO> list = dataBycode.get(key1);

					for (MaterialVO vo : list) {

//						String pk_material = vo.getPk_material();

						String code_ = subMaterialcode(vo.getCode()); // 加工一下物料编码，用户取主数据列表中的数据
						Map<String, String> map = mdm.get(code_);
						if (null == map || map.size() == 0){
							continue;
						}
						String groupMarbascode = map.get("groupMarbascode_@code");
						String mdm_code = map.get("mdm_code");
						String fromsystemid = map.get("fromsystemid");

						String pk_org = vo.getPk_org();
						// 1\修正集团分类
						String pk_marbas_g = getSever()
								.queryDocNameByID(
										groupMarbascode,
										"(select * from bd_defdoc where pk_defdoclist ='1001V1100000000W68CQ')",
										"code", "pk_defdoc");
						vo.setDef14(pk_marbas_g);
						// 2、修改主数据编码
						vo.setDef7(mdm_code);

						List<MaterialVO> list2 = mdmbody.get(pk_org);
						if (null == list2) {
							list2 = new ArrayList<MaterialVO>();
						}
						
						list2.add(vo);
						mdmbody.put(pk_org, list2);
					}

				}

			}

		}
	}

	private String subMaterialcode(String code) {

		String code_ = "";
		if (StringUtil.startsWith(code, "15-")
				|| StringUtil.startsWith(code, "16-")) {
			code_ = code.substring(3);
		} else {
			code_ = code;
		}

		return code_;

	}

	private Map<String, Map<String, String>> laodmdm(
			List<Map<String, String>> queryMdmPrimary) {
		if (null == queryMdmPrimary || queryMdmPrimary.size() == 0) {
			return null;
		}
		Map<String, Map<String, String>> map = new HashMap<String, Map<String, String>>();
		for (Map<String, String> data : queryMdmPrimary) {
			String materialcode = data.get("pk_material"); // 物料编码
			String code_ = subMaterialcode(materialcode);

			map.put(code_, data);

		}
		return map;
	}

	/**
	 * 通过物料编码分析出相关联的数据
	 * 
	 * @param selectDatas
	 * @return
	 */
	private Map<String, List<MaterialVO>> getDataBycode(Object[] selectDatas) {
		Map<String, List<MaterialVO>> dataBycode = new HashMap<String, List<MaterialVO>>();
		for (Object object : selectDatas) {
			MaterialVO material = (MaterialVO) object;
			String def14 = material.getDef14();
			if (null == def14) {// 集团统一分类为空时，需要赋值
				String pk_marbasclass = material.getPk_marbasclass();
				String groupClass = null;
				try {
					groupClass = this.getSever().queryDocNameByID(
							pk_marbasclass, "bd_marbasclass", "pk_marbasclass",
							"def1");
				} catch (DAOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (null != groupClass) {
					material.setDef14(groupClass);
				} else { // 如果匹配不到集团分类
					continue;
				}
			}

			String def7 = material.getDef7();

			if (null != def7) {
				continue;
			}
			// 通过物料编码分析
			String code = material.getCode();
			String code_ = subMaterialcode(code);

			List<MaterialVO> list = dataBycode.get(code_);
			if (null == list) {
				list = new ArrayList<MaterialVO>();
			}
			list.add(material);
			dataBycode.put(code_, list);
		}
		return dataBycode;
	}

	private Object[] getSelectDatas() {
		// TODO Auto-generated method stub
		Object[] selectedDatas = ((BillManageModel) this.getModel())
				.getSelectedOperaDatas();
		if (null == selectedDatas || selectedDatas.length == 0) {
			return null;
		}
		return selectedDatas;

	}

	public RefreshAction getRefreshAction() {
		return refreshAction;
	}

	public AbstractAppModel getModel() {
		return model;
	}

	public void setModel(AbstractAppModel model) {
		this.model = model;
	}

	public void setRefreshAction(RefreshAction refreshAction) {
		this.refreshAction = refreshAction;
	}

}
