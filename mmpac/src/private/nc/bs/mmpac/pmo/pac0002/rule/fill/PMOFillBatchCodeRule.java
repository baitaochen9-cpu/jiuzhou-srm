/** <a href="http://www.cpupk.com/decompiler">Eclipse Class Decompiler</a> plugin, Copyright (c) 2017 Chen Chao. */
package nc.bs.mmpac.pmo.pac0002.rule.fill;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.framework.common.NCLocator;
import nc.bs.mmpac.pmo.pac0002.bp.service.PMOBPService;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.itf.fi.pub.SysInit;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ArrayProcessor;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.pub.billcode.itf.IBillcodeManage;
import nc.pubimpl.scmf.ic.batchcoderule.BatchcodeRulePubServiceImpl;
import nc.pub.scmf.ic.batchcoderule.JZBatchCodeVO;
import nc.pub.scmf.ic.batchcoderule.NewJZBatchCodeCreatUtil;
import nc.pub.scmf.ic.batchcoderule.NewJZBatchCodeVO;
import nc.ui.mmpac.pmo.pac0002.view.PMOBillForm;
import nc.ui.pub.beans.MessageDialog;
import nc.util.mmf.busi.service.MaterialPubService;
import nc.util.mmf.framework.base.MMArrayUtil;
import nc.util.mmf.framework.base.MMCollectionUtil;
import nc.util.mmf.framework.base.MMMapUtil;
import nc.util.mmf.framework.base.MMValueCheck;
import nc.vo.bd.material.IMaterialEnumConst;
import nc.vo.bd.material.prod.MaterialProdVO;
import nc.vo.mmpac.pickm.param.PickmTransParamForMO;
import nc.vo.mmpac.pmo.pac0002.constant.PMOConstLang;
import nc.vo.mmpac.pmo.pac0002.entity.PMOAggVO;
import nc.vo.mmpac.pmo.pac0002.entity.PMOHeadVO;
import nc.vo.mmpac.pmo.pac0002.entity.PMOItemVO;
import nc.vo.mmpac.pmo.pac0002.entity.PMOPlanOutputVO;
import nc.vo.mmpac.pmo.pac0002.enumeration.PMOFItemStatusEnum;
import nc.vo.mmpac.pmo.pac0002.util.PMOTransferUtil;
import nc.vo.pd.pd0332.entity.PbEventParamVO;
import nc.vo.pd.pd0332.entity.PbParamVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pubapp.AppContext;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.pubapp.pattern.pub.MapList;

public class PMOFillBatchCodeRule implements IRule<PMOAggVO> {

    @Override
    public void process(PMOAggVO[] aggvos) {
        this.fillBatchCode(aggvos);
    }

    private void fillBatchCode(PMOAggVO[] aggvos) {
        PMOItemVO[] filteritems = this.getItemByBatchCode(aggvos);
        if (MMArrayUtil.isEmpty(filteritems)) {
            return;
        }
        Map<String, PMOHeadVO> headMap = new HashMap<String, PMOHeadVO>();

        for (PMOAggVO aggvo : aggvos) {
            PMOHeadVO headvo = aggvo.getParentVO();
            headMap.put(headvo.getCpmohid(), headvo);

        }
        // 按照工厂进行分类
        MapList<String, PMOItemVO> orgMapList = new MapList<String, PMOItemVO>();
        for (PMOItemVO item : filteritems) {
            orgMapList.put(item.getPk_org(), item);
        }
        Set<String> orgids = orgMapList.keySet();

        // 需要生成批次号
        List<PMOItemVO> needbatcodeList = new ArrayList<PMOItemVO>();
        for (String orgid : orgids) {
            // 同一个工厂
            List<PMOItemVO> itemList = orgMapList.get(orgid);
            // 按照物料进行分类
            MapList<String, PMOItemVO> marMapList = new MapList<String, PMOItemVO>();
            for (PMOItemVO vo : itemList.toArray(new PMOItemVO[itemList.size()])) {
                marMapList.put(vo.getCmaterialvid(), vo);
            }
            Set<String> marSet = marMapList.keySet();
            Map<String, MaterialProdVO> resultMap =
                    MaterialPubService.queryMaterialProduceInfoByPks(marSet.toArray(new String[0]), orgid,
                            new String[] {
                                "pchscscd"
                            });
            needbatcodeList.clear();

            if (MMMapUtil.isNotEmpty(resultMap)) {
                for (String marid : marSet) {
                    MaterialProdVO provo = resultMap.get(marid);
                    List<PMOItemVO> itemvoList = marMapList.get(marid);
                    // V65批次号生成时机可能为空,增加判空逻辑.TODO：验证
                    if (MMValueCheck.isEmpty(provo.getPchscscd())) {
                        continue;
                    }
                    if (MMCollectionUtil.isEmpty(itemvoList)) {
                        continue;
                    }
                    for (PMOItemVO itemVO : itemvoList) {

                        // 投放时生成
                        if (PMOFItemStatusEnum.PUT.equalsValue(itemVO.getFitemstatus())
                                && provo.getPchscscd().intValue() == IMaterialEnumConst.PCHSCSCD_PROD) {
                            needbatcodeList.add(itemVO);
                        }
                        // 订单新增时生成( TODO 物料领域还没提供枚举或常量)
                        else if (PMOFItemStatusEnum.PLAN.equalsValue(itemVO.getFitemstatus())
                                && provo.getPchscscd().intValue() == 3) {
                            needbatcodeList.add(itemVO);
                        }
                    }
                }
            }
            if (needbatcodeList.isEmpty()) {
                continue;
            }
            // 根据批次号生成规则生成并回写生产订单和备料计划
            this.genBatchCode(needbatcodeList.toArray(new PMOItemVO[needbatcodeList.size()]), headMap);
        }

    }

    private PMOItemVO[] getItemByBatchCode(PMOAggVO[] aggvos) {
        if (MMArrayUtil.isEmpty(aggvos)) {
            return null;
        }
        List<PMOItemVO> itemList = new ArrayList<PMOItemVO>();
        for (PMOAggVO aggvo : aggvos) {
            PMOItemVO[] items = aggvo.getChildrenVO();
            if (!MMArrayUtil.isEmpty(items)) {
                for (PMOItemVO item : items) {
                    if (MMValueCheck.isEmpty(item.getVbatchcode())) {
                        itemList.add(item);
                    }
                }
            }
        }
        return itemList.toArray(new PMOItemVO[itemList.size()]);
    }

    private void genBatchCode(PMOItemVO[] paravos, Map<String, PMOHeadVO> headMap) {
        if (MMArrayUtil.isEmpty(paravos)) {
            return;
        }
        List<PMOItemVO> itemList = new ArrayList<PMOItemVO>();
        for (PMOItemVO vo : paravos) {
            itemList.add(vo);
        }
        if (itemList.size() > 0) {
            PMOItemVO[] vos = itemList.toArray(new PMOItemVO[itemList.size()]);
            IBillcodeManage billcodeManage = NCLocator.getInstance().lookup(IBillcodeManage.class);
            String[] billcodes = null;
            try {
                billcodes =
                        billcodeManage.getBatchBillCodes_RequiresNew("55A2-2", vos[0].getPk_group(),
                                vos[0].getPk_org(), vos[0], vos.length);
            }
            catch (BusinessException ex) {
                ExceptionUtils.wrappException(ex);
            }

            if (MMArrayUtil.isEmpty(billcodes)) {
                ExceptionUtils.wrappBusinessException(PMOConstLang.getMSG_RULE_PUT_BATCHCODECREATE_CHECK());
            }
            int i = 0;
            List<PbEventParamVO> eparaList = new ArrayList<PbEventParamVO>();
        	ArrayList pizh=new ArrayList();
            for (PMOItemVO vo : vos) {
            	//同物料生产不同批次号标识
				Boolean xhbs=true;
            	//新增生成 生产批次号代码
				try {
//					String pcgz = querySysinitName("pcgz",
//							vo.getPk_org());// 批次规则
//					if ("Y".equals(pcgz)) {// 如果批次为空
//					boolean flag = getStockInfo(vo.getCmaterialid(),
//							vo.getPk_org());
//					if (vo.getVbatchcode() == null
//							|| vo.getVbatchcode().equals("")){
//						if (flag == true) {
//							
//								String code = getcode(vo.getCmaterialid());
//								String pcqz = querySysinitName("pcqz",
//										vo.getPk_org());// 批次前缀采取流水号前几位
//								String pctsbs = querySysinitName("pctsbs",
//										vo.getPk_org());// 批次特殊标识
//								if (pcqz.equals("")) {
//									MessageDialog.showErrorDlg(null, null,
//											"生成批次参数pcqz为空");
//									return;
//								}
//								int pcqzcd = Integer.parseInt(pcqz);// 获取物料前缀的长度
//								code = getpcqz(pcqzcd, code);// 计算批次号前缀
//								String qz = code.substring(code.length()
//										- pcqzcd, code.length());// 前缀
//
//								String bccode = getscmbatchcode(qz + pctsbs,
//										vo.getCmaterialid());// 获取最新的批次号
//								String pch = "";// 生成的批次号
//								if (!bccode.equals("")) {// 流水号不为空
//									String pclshcd = querySysinitName(
//											"pclshcd", vo.getPk_org());// 批次流水号长度
//									if (pclshcd.equals("")) {
//										MessageDialog.showErrorDlg(null, null,
//												"生成批次参数pclshcd为空");
//										return;
//									}
//									int lshcd = Integer.parseInt(pclshcd);// 获取批次流水号的长度
//									String lsh = bccode.substring(
//											bccode.length() - lshcd,
//											bccode.length());// 获取当前流水号
//									Integer intcode = Integer.parseInt(lsh);
//									lsh = String.format("%0" + lshcd + "d",
//											(++intcode));
//									//处理同一张单子 同一个批次号情况
//									pch=qz+pctsbs+lsh;//生成的批次号
//									while(xhbs){
//									if(pizh.contains(pch)){
//										lsh= String.format("%0"+lshcd+"d", (++intcode));
//										pch=qz+pctsbs+lsh;//生成的批次号
//									}else{
//										xhbs=false;
//									}
//									}
//								} else {// 流水号为空
//									String pclsqsh = querySysinitName(
//											"pclsqsh", vo.getPk_org());// 批次流水号初始号
//									pch = qz + pctsbs + pclsqsh;// 生成默认批次号
//								}
//								pizh.add(pch);
//								vo.setVbatchcode(pch);
//							}
//
//						}
//				
//					}else
					/******zhian.ye 20210413 **********************************************/
					if(UFBoolean.TRUE.equals(SysInit.getParaBoolean( vo.getPk_org(), "IC132"))){
						this.raybowBatchCode(vo);
					}else
					{
						 vo.setVbatchcode(billcodes[i++]);
					}
				} catch (BusinessException ex) {
					// TODO Auto-generated catch block
					ex.printStackTrace();
				}
            	
            	
            	
            	
            	
            	
               
                if (!MMValueCheck.isEmpty(vo.getVbatchcode())) {
                    PbParamVO paravo = PMOTransferUtil.getBatchParamVO(headMap.get(vo.getCpmohid()), vo, false);
                    PbEventParamVO eparavo = new PbEventParamVO(paravo, null, null, null);
                    eparaList.add(eparavo);
                }
            }

            Map<String, PbParamVO> ResultParamVOMap =
                    PMOBPService.getIPbMaintainServiceForPac().batchSavePdParamVO(
                            eparaList.toArray(new PbEventParamVO[eparaList.size()]));
            if (MMMapUtil.isEmpty(ResultParamVOMap)) {
                return;
            }
            for (PMOItemVO item : vos) {
                PMOHeadVO headvo = headMap.get(item.getCpmohid());
                if (!MMValueCheck.isEmpty(item.getVbatchcode())) {
                    PbParamVO paravo =
                            ResultParamVOMap.get(headvo.getPk_org() + headvo.getVbillcode() + item.getVrowno());
                    if (!MMValueCheck.isEmpty(paravo)) {
                        item.setVbatchcode(paravo.getVprodbatchcode());
                        item.setCbatchid(paravo.getPk_batchcode());
                    }
                }
            }

            try {
                List<PickmTransParamForMO> paramList = new ArrayList<PickmTransParamForMO>();
                for (PMOItemVO vo : vos) {

                    PickmTransParamForMO param = new PickmTransParamForMO();
                    param.setSourceBillID(vo.getCpmohid());
                    param.setSourceBillRowID(vo.getCmoid());
                    param.setBatchCode(vo.getVbatchcode());
                    param.setBatchid(vo.getCbatchid());
                    paramList.add(param);

                }
                if (paramList.size() > 0) {
                    // 同步备料计划批次号
                    PMOBPService.getIPpickmBusinessServiceForPmo().updatePickmBatchCode(
                            paramList.toArray(new PickmTransParamForMO[0]));
                }

            }
            catch (Exception e) {
                ExceptionUtils.wrappException(e);
            }
        }
    }
    
    
    
    /**.
     * zhian.ye
     * 自定义批次号规则，IC130
     * @param vo
     * @throws BusinessException 
     */
    private void raybowBatchCode(PMOItemVO vo) throws BusinessException {
    	/*
    	 * 20230204 中山项目上线，升级独立生产批次号 ，增加生产模块 参数 MMPAC03/MMPAC04 控制启用生产模块批次号及规则
    	 */
    	String batchRuleSysInit = "IC131";
    	UFBoolean mmpac03 = nc.cmp.bill.util.SysInit.getParaBoolean(vo.getPk_org(), "MMPAC03");
    	if(mmpac03.booleanValue()){
    		batchRuleSysInit = "MMPAC04";
    	}
    	
    	
    	String vbatchcode = vo.getVbatchcode(); //当前单据行上的批次号  *****检查是否为空
    	PMOPlanOutputVO[] planoutputs = vo.getPlanoutputs();
    	Map<String,Integer> temp_outtype = new HashMap<String,Integer>();//产品列表，<产品ID,类型>
    	temp_outtype.put(vo.getCmaterialid(), 1 );
    	
    	if(planoutputs != null && planoutputs.length > 0){
    		//  说明有副产品需要处理
    		/*1、检查是否独立副产品批次号，获取业务参数
    		 * 2、检查副产品是否批次号管理
    		 * 3、按照统一规则生产批次号
    		 */
    		for(PMOPlanOutputVO svo : planoutputs){
    			String cmaterialid = svo.getCmaterialid(); 
    			UFBoolean checkMaterialFoBatchcode = NewJZBatchCodeCreatUtil.checkMaterialFoBatchcode(cmaterialid,vo.getPk_org());
    	        UFBoolean MMPAC02 = SysInit.getParaBoolean(vo.getPk_org(), "MMPAC02");
    			if(!checkMaterialFoBatchcode.booleanValue() || !MMPAC02.booleanValue())//顺便看一下参数是不是有开
    				continue;
    			temp_outtype.put(svo.getCmaterialid(),svo.getFoutputtype() );
    		}
    	}
    	for(String material :temp_outtype.keySet()){
    		if(temp_outtype.get(material) == 1 && vbatchcode != null){
    			continue ;   //如果 是主产品的话，并且批次已经写入，不做处理
    		}//否则的话，如果map里还有值，那一定是联副产品使用了批次号管理，这里不管理怎么样，要生产批次号，联副产品在系统里是没地放录入批次号的，只能在这里进行生成
    		 
    		
    	
    		
    		//检查批次流水表
//		JZBatchCodeVO jzbatch = queryBatchSerial(vo);
    		String codrul = SysInit.getParaString(vo.getPk_org(), batchRuleSysInit);
    		Map<String, String> parseRule = NewJZBatchCodeCreatUtil.parseRule(codrul);
    		String newBatchCode = null;
    		
    		NewJZBatchCodeVO batchcodevo = NewJZBatchCodeCreatUtil.queryBatchSerial(vo.getPk_org(), material, codrul);
    		if(null == batchcodevo ){
    			batchcodevo =  
    					new NewJZBatchCodeVO(parseRule, vo.getCmaterialvid(), vo.getPk_org(), NewJZBatchCodeCreatUtil.getMaterialCodeByID(material));
    			batchcodevo.setAttributeValue(NewJZBatchCodeVO.CODERULE, codrul);
    			batchcodevo.setStatus(VOStatus.NEW);
    			newBatchCode = NewJZBatchCodeCreatUtil.assemble(batchcodevo, parseRule);
    			
    		}else {
    			batchcodevo.setStatus(VOStatus.UPDATED);
    			
    		}
    		while (newBatchCode == null || NewJZBatchCodeCreatUtil.checkCode(newBatchCode) ) {
    			//
    			newBatchCode = NewJZBatchCodeCreatUtil.NextBatchCode(batchcodevo);
    		}; //以当前的批次号流水，去创建新的批次号；
    		
    		if(temp_outtype.get(material) == 1){
    			vo.setVbatchcode(newBatchCode);
    			
    		}else {
    			for(PMOPlanOutputVO svo : planoutputs){
    				if(material.equals(svo.getCmaterialid())){
    					svo.setVbdef2(newBatchCode);
    				}
    			}
    		}
    		NewJZBatchCodeCreatUtil.batchcodeDB(batchcodevo); //流水同步处理
    	}
		
		
		
	}
    
    /**
	 * 批次号流水表同步
	 * 
	 * @param creatBatchcode
	 * @throws DAOException
	 */
    BaseDAO dao = null;
	private void batchcodeDB(JZBatchCodeVO creatBatchcode) throws DAOException {
		// TODO Auto-generated method stub
		if (null == creatBatchcode) {
			return;
		}
		creatBatchcode.setTs(AppContext.getInstance().getServerTime());
		int status = creatBatchcode.getStatus();
		if (status == VOStatus.UPDATED) {
			getDAO().updateVO(creatBatchcode);
		} else if (status == VOStatus.NEW) {
			getDAO().insertVO(creatBatchcode);
		}
	}

    
    private BaseDAO getDAO() {
		if(null == dao){
			dao = new BaseDAO();
		}
		return dao;
	}




	//查询参数代码
   	private String querySysinitName(String initcode,String pk_org) throws BusinessException {
   		  String sql = "select value from pub_sysinit  where initcode='"+initcode+"' and pk_org='"+pk_org+"' and nvl(dr,0)=0";
   		  Object obj = NCLocator.getInstance().lookup(IUAPQueryBS.class).executeQuery(sql, new ColumnProcessor());
   		  return obj == null ? "" : obj.toString();
   		 }
   	
   	
   	//查询物料是否启用批次管理
   	public  boolean getStockInfo(String pk_material,String pk_org){
   		String sb="select count(wholemanaflag) from bd_materialstock where pk_material='"+pk_material+"' and pk_org='"+pk_org+"' and  wholemanaflag='Y'"; 
   		 Integer count = 0;
   		try {
   			   count = (Integer)NCLocator.getInstance().lookup(IUAPQueryBS.class).executeQuery(sb.toString(), new ColumnProcessor());
   			   return count>0?true:false;
   			  } catch (BusinessException e) {
   			   e.printStackTrace();
   			  }
   		
   		return false;
   	}
   	
   	//查询物料编码
   	public String getcode(String pk_material) throws BusinessException{
   		 String sql = "select code from  bd_material where pk_material='"+pk_material+"'";
   		  Object obj = NCLocator.getInstance().lookup(IUAPQueryBS.class).executeQuery(sql, new ColumnProcessor());
   		  return obj == null ? "" : obj.toString();
   	}
   	
   	//查询最新的批次号
   	public String  getscmbatchcode(String  scmqz,String pk_material)throws BusinessException{

   		 String sql =  " select max(vbatchcode) from(  " +
   				" 		select max(vbatchcode) vbatchcode from  scm_batchcode where cmaterialoid='"+pk_material+"' and  vbatchcode like '%"+scmqz+"%'    "+
   				" 		union all  	select max(vprodbatchcode) vbatchcode from pd_pb where  vprodbatchcode like '%"+scmqz+"%'  and cmaterialid='"+pk_material+"'  )";
   		 		Object obj = NCLocator.getInstance().lookup(IUAPQueryBS.class).executeQuery(sql, new ColumnProcessor());
   		  return obj == null ? "" : obj.toString();
   	}
   	
   	//递归 计算 批次号补0规则
   	public String getpcqz(int i ,String code){
   		if(i>code.length()){
   			code="0"+code;
   			getpcqz(i, code);
   		}
   		return code;
   	} 
    
    
    
}
