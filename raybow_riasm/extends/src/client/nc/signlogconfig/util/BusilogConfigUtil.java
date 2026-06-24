package nc.signlogconfig.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import nc.bs.logging.Logger;
import nc.bs.sm.logconfig.vo.AttributeVO;
import nc.bs.sm.logconfig.vo.BusilogRuleVO;
import nc.desktop.ui.WorkbenchEnvironment;
import nc.ui.md.signog.treetotree.SignLogMDTreeToTreePanel;
import nc.ui.md.tree.entityop.MDEntityTreeNodeInfo;
import nc.vo.pub.SuperVO;

public class BusilogConfigUtil {

	public static String getPk_Group() {
		return WorkbenchEnvironment.getInstance().getGroupVO().getPk_group();
	}

	public static void addNeedSaveData2Map(
			SignLogMDTreeToTreePanel treeToTreePanel,
			Map<String, Map<String, List<SuperVO>>> dataMap,
			List<BusilogRuleVO> oper4save, List<AttributeVO> attr4save) {
		List<MDEntityTreeNodeInfo> nodeInfoList = treeToTreePanel
				.getSelectedMDEntityTreeNodeInfo();
		if((nodeInfoList == null || nodeInfoList.size() == 0)
				&& (dataMap == null || dataMap.size() == 0)){
			return;
		}
		if (nodeInfoList == null || nodeInfoList.size() == 0) {
			purelyAddDelData(dataMap, oper4save, attr4save);
		} else {// 数据比对进行然后决定是增加还是删除
			//处理删除单个离散实体的情形
			Map<String, Map<String, List<SuperVO>>> discreteData4Delete = getDiscreteData4Del(dataMap, nodeInfoList);
			if(discreteData4Delete != null && discreteData4Delete.size() > 0){
				purelyAddDelData(discreteData4Delete, oper4save, attr4save);
			}
			//处理某一实体的属性、操作或增或减或不变的情形
			for (MDEntityTreeNodeInfo nodeInfo : nodeInfoList) {
				if("57507bca-d61f-4451-b5e0-72f5c1d433c0".equals(nodeInfo.getBeanID())){
					Logger.error("点击完右移按钮后右侧树的所有属性"+nodeInfo.getAttrPathList().toString());	
				}
				fillDataNeedSave(nodeInfo, dataMap, oper4save, attr4save);
			}
		}
	}

	public static List<MDEntityTreeNodeInfo> constructNodeInfoList(
			Map<String, Map<String, List<SuperVO>>> dataMap) {
		List<MDEntityTreeNodeInfo> nodeInfoList = new ArrayList<MDEntityTreeNodeInfo>();
		if (dataMap == null || dataMap.size() == 0) {
			return nodeInfoList;
		}
		Iterator<String> beanIDIT = dataMap.keySet().iterator();
		while (beanIDIT.hasNext()) {
			String beanID = beanIDIT.next();
			Map<String, List<SuperVO>> eachBean = dataMap.get(beanID);
			MDEntityTreeNodeInfo info = new MDEntityTreeNodeInfo();
			info.setBeanID(beanID);
			// 构造属性路径List
			List<SuperVO> attrList = eachBean.get("attribute");
			if (attrList != null && attrList.size() > 0) {
				for (SuperVO superVO : attrList) {
					AttributeVO attributeVO = (AttributeVO) superVO;
					List<String> attrNamePath = info.getAttrPathList();
					if (attrNamePath == null) {
						attrNamePath = new ArrayList<String>();
						info.setAttrPathList(attrNamePath);
					}
					attrNamePath.add(attributeVO.getName_path());
				}
			}
			// 构造操作名List
			List<SuperVO> operList = eachBean.get("operation");
			if (operList != null && operList.size() > 0) {
				for (SuperVO superVO : operList) {
					BusilogRuleVO busilogVO = (BusilogRuleVO) superVO;
					List<String> operName = info.getBusiOPPathList();
					if (operName == null) {
						operName = new ArrayList<String>();
						info.setBusiOPPathList(operName);
					}
					operName.add(busilogVO.getOper_name());
				}
			}
			nodeInfoList.add(info);
		}
		return nodeInfoList;
	}

	public static Map<String, Map<String, List<SuperVO>>> mergeMap(
			Map<String, Map<String, List<SuperVO>>> seletedMap,
			Map<String, Map<String, List<SuperVO>>> alwaysShowMap) {
		Map<String, Map<String, List<SuperVO>>> mergedMap = new HashMap<String, Map<String,List<SuperVO>>>();
		//2013-4-1修改　
		mergedMap.putAll(seletedMap);
		//合并map
		for(String beanid: alwaysShowMap.keySet()){
			if(mergedMap.containsKey(beanid)){
				if(alwaysShowMap.get(beanid)!=null){
					if(mergedMap.get(beanid).get("attribute")!=null&&alwaysShowMap!=null&&alwaysShowMap.get(beanid).get("attribute")!=null){
						mergedMap.get(beanid).get("attribute").addAll(alwaysShowMap.get(beanid).get("attribute"));
					}else{
						mergedMap.get(beanid).put("attribute", alwaysShowMap.get(beanid).get("attribute"));
					}
					if(mergedMap.get(beanid).get("operation")!=null&&alwaysShowMap!=null&&alwaysShowMap.get(beanid).get("operation")!=null){
						mergedMap.get(beanid).get("operation").addAll(alwaysShowMap.get(beanid).get("operation"));
					}else{
						mergedMap.get(beanid).put("operation",alwaysShowMap.get(beanid).get("operation"));
					}
				}
			}else{ 
				mergedMap.put(beanid, alwaysShowMap.get(beanid));
			}
		}
		
//		mergedMap.putAll(alwaysShowMap);
		return mergedMap; 
	}
	
	private static void fillDataNeedSave(MDEntityTreeNodeInfo nodeInfo,
			Map<String, Map<String, List<SuperVO>>> dataMap,
			List<BusilogRuleVO> oper4save, List<AttributeVO> attr4save) {
		List<String> operList = nodeInfo.getBusiOPPathList();
		List<String> operIDList = nodeInfo.getBusiOPIDList();
		List<String> attrList = nodeInfo.getAttrPathList();
		String beanID = nodeInfo.getBeanID();
		// **********日志*******//
		nc.bs.logging.Logger.error("每一个beanID="+beanID);
		if (dataMap == null || !dataMap.containsKey(beanID)) {// 处理新增的某一实体操作和属性
			// 处理属性
			if (attrList != null && attrList.size() > 0) {
				for (int i = 0; i < attrList.size(); i++) {
					AttributeVO attrVO = new AttributeVO();
					attrVO.setPk_metadata(beanID);
					attrVO.setPk_group(getPk_Group());
					attrVO.setName_path(attrList.get(i));
					attr4save.add(attrVO);
				}
			}
			// 处理操作
			if (operList != null && operList.size() > 0) {
				for (int i = 0; i < operList.size(); i++) {
					String operName = operList.get(i);
					String pk_operation = operIDList.get(i);
					BusilogRuleVO vo = new BusilogRuleVO();
					vo.setPk_group(getPk_Group());
					vo.setPk_metadata(beanID);
					vo.setPk_operation(pk_operation);
					vo.setOper_name(operName);
					oper4save.add(vo);
				}
			}
		} else {// 处理某一存在实体的属性和操作或增或减的情形
			Map<String, List<SuperVO>> eachBean = dataMap.get(beanID);
			// **********日志*******//
//			nc.bs.logging.Logger.error("获取添加新属性后右侧树以后的总个数dataMap.size()="+(dataMap == null ? 0 : dataMap.size()));

			List<SuperVO> attrVOInModel = eachBean.get("attribute");
			List<SuperVO> operVOInModel = eachBean.get("operation");
			
			nc.bs.logging.Logger.error("未添加前右侧树总个数attrVOInModel.size()="+(attrVOInModel == null ? 0 : attrVOInModel.size()));
			// **********处理属性*******//
			// 处理属性-新增
			if (attrList != null && attrList.size() > 0) {
				// **********日志*******//
				nc.bs.logging.Logger.error("添加后右侧所有属性的attrList.size()="+(attrList == null ? 0 : attrList.size()));
				for (String attr : attrList) {
					boolean isNeedAdd = true;
					if (attrVOInModel != null && attrVOInModel.size() > 0) {
						for (SuperVO attrVO : attrVOInModel) {
							AttributeVO vo = (AttributeVO) attrVO;
							if (attr.equals(vo.getName_path())) {
								isNeedAdd = false;
								break;
							}
						}
					}
					if (isNeedAdd) {
						AttributeVO attrVO = new AttributeVO();
						attrVO.setPk_metadata(beanID);
						attrVO.setPk_group(getPk_Group());
						attrVO.setName_path(attr);
						attr4save.add(attrVO);
					}
				}
			}
			// 处理属性-删除
			if (attrVOInModel != null && attrVOInModel.size() > 0) {
				for (SuperVO attrVO : attrVOInModel) {
					boolean isNeedDelete = true;
					AttributeVO vo = (AttributeVO) attrVO;
					if (attrList != null && attrList.size() > 0) {
						for (String attr : attrList) {
							if (attr.equals(vo.getName_path())) {
								isNeedDelete = false;
								break;
							}
						}
					}
					if (isNeedDelete) {
						attr4save.add(vo);
					}
				}
			}
			// **********处理操作*******//
			// 处理操作-新增
			if (operList != null && operList.size() > 0) {
				for (int i = 0; i < operList.size(); i++) {
					boolean isNeedAdd = true;
					if (operVOInModel != null && operVOInModel.size() > 0) {
						for (SuperVO operVO : operVOInModel) {
							BusilogRuleVO vo = (BusilogRuleVO) operVO;
							if (operList.get(i).equals(vo.getOper_name())) {
								isNeedAdd = false;
								break;
							}
						}
					}
					if (isNeedAdd) {
						BusilogRuleVO operVO = new BusilogRuleVO();
						operVO.setPk_metadata(beanID);
						operVO.setPk_group(getPk_Group());
						operVO.setOper_name(operList.get(i));
						operVO.setPk_operation(operIDList.get(i));
						oper4save.add(operVO);
					}
				}
			}
			// 处理操作-删除
			if (operVOInModel != null && operVOInModel.size() > 0) {
				for (SuperVO operVO : operVOInModel) {
					boolean isNeedDelete = true;
					BusilogRuleVO vo = (BusilogRuleVO) operVO;
					if (operList != null && operList.size() > 0) {
						for (String oper : operList) {
							if (oper.equals(vo.getOper_name())) {
								isNeedDelete = false;
								break;
							}
						}
					}
					if (isNeedDelete) {
						oper4save.add(vo);
					}
				}
			}

		}

	}
	
	private static Map<String, Map<String, List<SuperVO>>> getDiscreteData4Del(
			Map<String, Map<String, List<SuperVO>>> dataMap,
			List<MDEntityTreeNodeInfo> nodeInfoList) {
		if(dataMap == null || dataMap.size() == 0 || nodeInfoList == null || nodeInfoList.size() == 0){
			return null;
		}
		Map<String, String> beanIDMap = new HashMap<String, String>();
		for (MDEntityTreeNodeInfo entityTreeNodeInfo : nodeInfoList) {
			beanIDMap.put(entityTreeNodeInfo.getBeanID(), entityTreeNodeInfo.getBeanID());
		}
		Map<String, Map<String, List<SuperVO>>> discreteData4Delete = new HashMap<String, Map<String, List<SuperVO>>>();
		Iterator<String> beanIDIT = dataMap.keySet().iterator();
		while(beanIDIT.hasNext()){
			String beanID = beanIDIT.next();
			if(!beanIDMap.containsKey(beanID)){
				discreteData4Delete.put(beanID, dataMap.get(beanID));
			}
		}
		return discreteData4Delete;
	}
	
	private static void purelyAddDelData(Map<String, Map<String, List<SuperVO>>> needDelMap, List<BusilogRuleVO> oper4save, List<AttributeVO> attr4save){
		// 数据全部将用于删除
		Iterator<String> beanIDIT = needDelMap.keySet().iterator();
		while (beanIDIT.hasNext()) {
			String beanID = beanIDIT.next();
			Map<String, List<SuperVO>> eachBean = needDelMap.get(beanID);
			List<SuperVO> attrList = eachBean.get("attribute");
			if (attrList != null && attrList.size() > 0) {
				for (SuperVO attrVO : attrList) {
					attr4save.add((AttributeVO) attrVO);
				}
			}
			List<SuperVO> operList = eachBean.get("operation");
			if (operList != null && operList.size() > 0) {
				for (SuperVO operVO : operList) {
					oper4save.add((BusilogRuleVO) operVO);
				}
			}
		}
	}
}
