package nc.impl.qilibc;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import nc.bs.barcode.IDQueryBuilder;
import nc.bs.dao.BaseDAO;
import nc.bs.framework.common.NCLocator;
import nc.impl.pub.ace.AceTmprintPubServiceImpl;
import nc.impl.pubapp.pub.smart.BatchSaveAction;
import nc.itf.qilibc.IBarcodeconfigMaintain;
import nc.itf.qilibc.IBarcoderckMaintain;
import nc.itf.qilibc.IQilibc_packlistMaintain;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uif.pub.IUifService;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.bc.mapp.pub.BarcodeRCKVO;
import nc.vo.bd.meta.BatchOperateVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pubapp.pattern.pub.SqlBuilder;
import nc.vo.qilibc.busibean.exception.QiliBCException;
import nc.vo.qilibc.packlist.AggPackListVO;
import nc.vo.qilibc.packlist.PackListBVO;
import nc.vo.qilibc.packlist.PackListHVO;
import nc.vo.qilibc.printsnrule.PrintsnruleVO;
import nc.vo.qilibc.streamconfig.StreamconfigVO;
import nc.vo.qilibc.tmprintupdate.TmprintupdateVO;
import nc.vo.tmprint.tmprint.TmprintVO;

public class TmprintMaintainImpl extends AceTmprintPubServiceImpl implements
		nc.itf.qilibc.ITmprintMaintain {

	@Override
	public TmprintVO[] query(IQueryScheme queryScheme) throws BusinessException {
		return super.pubquerybasedoc(queryScheme);
	}

	@Override
	public BatchOperateVO batchSave(BatchOperateVO batchVO)
			throws BusinessException {
		BatchSaveAction<TmprintVO> saveAction = new BatchSaveAction<TmprintVO>();
		BatchOperateVO retData = saveAction.batchSave(batchVO);
		// 调用编码、名称唯一性校验规则
		// new DataUniqueCheckRule().process(new BatchOperateVO[] {
		// batchVO });
		return retData;
	}
	
	/***
	 * add by hhj 2021-10-18
	 * 根据传递信息获取最大流水号
	 * @param queryScheme
	 * @return
	 * @throws BusinessException
	 * @throws Exception
	 */
	@Override
	public String getMaxLsh(TmprintVO printVO, StreamconfigVO streamVO)
			throws BusinessException, Exception {

		IUAPQueryBS query = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		PrintsnruleVO  printsnrulevo =new PrintsnruleVO();
		for(String culmname:printVO.getAttributeNames()){
			printsnrulevo.setAttributeValue(culmname, printVO.getAttributeValue(culmname));
		}
		//条码流水组成年月日部分
		String nyr = "";
		String sqlwhere = " 1=1 ";
		String streamValue = streamVO.getValue(); 
		String[] fields = streamValue.split(";");
		//通过循环条码流水配置进行最大流水查询条件的拼装
		for (String field : fields) {
			String date = printVO.getPrinttime().toStdString();
			if("year".equals(field)){
				nyr = date.substring(0, 4);
				sqlwhere = sqlwhere +" and printtime like '"+date.substring(0, 4)+"%'";
			}else if("batchcode".equals(field) && printVO.getVbatchcode()!=null){
				sqlwhere = sqlwhere +" and vbatchcode like '"+printVO.getVbatchcode()+"%'";
			}else if("month".equals(field)){
				nyr = date.substring(0, 4) + date.toString().substring(5, 7);
				sqlwhere = sqlwhere +" and printtime like '"+date.substring(0, 7)+"%'";
			}else if("day".equals(field)){
				nyr = date.substring(0, 4) + date.toString().substring(5, 7) + date.toString().substring(8, 10);
				sqlwhere = sqlwhere +" and printtime like '"+date.substring(0, 10)+"%'";
			}else if("producedate".equals(field) && printVO.getDproducedate()!=null){
				sqlwhere = sqlwhere +" and dproducedate like '"+printVO.getDproducedate().toStdString().substring(0, 10)+"%'";
			}else{
				Object value = printVO.getAttributeValue(field);
				if(value != null && !"".equals(value)){
					sqlwhere = sqlwhere +" and "+field+"='"+printVO.getAttributeValue(field)+"'";
				}
			}
		}
		//流水长度
		Integer strlen = streamVO.getStrlenth();
		//流水号最大值9999
		String max99 = "";
		for(int j = 0; j < strlen; j++){
			max99 = "9" + max99;
		}
		String maxstr = null;
		//过滤掉流水号为空的数据
		sqlwhere +=" and isnull(lsh,'~')!='~' ";
		Collection<PrintsnruleVO> qryVos = (Collection<PrintsnruleVO>) query.retrieveByClause(PrintsnruleVO.class, sqlwhere);
		Integer maxlsh = 0;
		PrintsnruleVO maxtmprintVO = null;
		if(qryVos != null && qryVos.size()>0){
			for (PrintsnruleVO tmprintVO : qryVos) {
				Integer lsh1 = Integer.valueOf(tmprintVO.getLsh());
				if(lsh1>maxlsh){
					maxlsh = lsh1;
					maxtmprintVO = tmprintVO;
				}
			}
			if(max99.equals(maxlsh.toString())){
//				throw new QiliBCException("流水号已达最大值"+max99+"，请在“流水号规则”配置界面修改流水号位数后重新打印！");
				throw new QiliBCException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
				        .getStrByID("common", "40H1-MESSAGE0011", null, new String[] {
				        		max99
				        })/*@res "流水号已达最大值{0}，请在“流水号规则”配置界面修改流水号位数后重新打印！"*/);
			}
		}else{
			maxlsh = 0;
		}
		return String.valueOf(maxlsh);
	}
    /***
     * add by szc
     * 更新RULE表的数据到print表
     * @param queryScheme
     * @return
     * @throws BusinessException
     * @throws Exception
     */

	@Override
	public HashMap<String, Object> getGXLsh(TmprintVO printVO, StreamconfigVO streamVO,Boolean isfristserch)
			throws BusinessException, Exception {
		IUAPQueryBS query = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		PrintsnruleVO  printsnrulevo =new PrintsnruleVO();
		for(String culmname:printVO.getAttributeNames()){
			printsnrulevo.setAttributeValue(culmname, printVO.getAttributeValue(culmname));
		}
		//条码流水组成年月日部分
		String nyr = "";
		String sqlwhere = " 1=1 ";
		String streamValue = streamVO.getValue(); 
		String[] fields = streamValue.split(";");
		//通过循环条码流水配置进行最大流水查询条件的拼装
		for (String field : fields) {
			String date = printVO.getPrinttime().toStdString();
			if("year".equals(field)){
				nyr = date.substring(0, 4);
				sqlwhere = sqlwhere +" and printtime like '"+date.substring(0, 4)+"%'";
			}else if("batchcode".equals(field) && printVO.getVbatchcode()!=null){
				sqlwhere = sqlwhere +" and vbatchcode like '"+printVO.getVbatchcode()+"%'";
			}else if("month".equals(field)){
				nyr = date.substring(0, 4) + date.toString().substring(5, 7);
				sqlwhere = sqlwhere +" and printtime like '"+date.substring(0, 7)+"%'";
			}else if("day".equals(field)){
				nyr = date.substring(0, 4) + date.toString().substring(5, 7) + date.toString().substring(8, 10);
				sqlwhere = sqlwhere +" and printtime like '"+date.substring(0, 10)+"%'";
			}else if("producedate".equals(field) && printVO.getDproducedate()!=null){
				sqlwhere = sqlwhere +" and dproducedate like '"+printVO.getDproducedate().toStdString().substring(0, 10)+"%'";
			}else{
				Object value = printVO.getAttributeValue(field);
				if(value != null && !"".equals(value)){
					sqlwhere = sqlwhere +" and "+field+"='"+printVO.getAttributeValue(field)+"'";
				}
			}
		}
		//流水长度
		Integer strlen = streamVO.getStrlenth();
		//流水号最大值9999
		String max99 = "";
		for(int j = 0; j < strlen; j++){
			max99 = "9" + max99;
		}
		String maxstr = null;
		//过滤掉流水号为空的数据
		sqlwhere +=" and isnull(lsh,'~')!='~' ";
		Collection<PrintsnruleVO> qryVos = (Collection<PrintsnruleVO>) query.retrieveByClause(PrintsnruleVO.class, sqlwhere);
		Integer maxlsh = 0;
		Integer Tmmaxlsh = 0;
		PrintsnruleVO maxtmprintVO = null;
		  
		if(qryVos != null && qryVos.size()>0){
			for (PrintsnruleVO tmprintVO : qryVos) {
				Integer lsh1 = Integer.valueOf(tmprintVO.getLsh());
				if(lsh1>maxlsh){
					maxlsh = lsh1;
					maxtmprintVO = tmprintVO;
				}
			}
			if(max99.equals(maxlsh.toString())){
//				throw new QiliBCException("流水号已达最大值"+max99+"，请在“流水号规则”配置界面修改流水号位数后重新打印！");
				throw new QiliBCException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
				        .getStrByID("common", "40H1-MESSAGE0011", null, new String[] {
				        		max99
				        })/*@res "流水号已达最大值{0}，请在“流水号规则”配置界面修改流水号位数后重新打印！"*/);
			}
		}else{
			maxlsh = 0;
		}
		if(isfristserch){
		Collection<TmprintVO> TmqryVos = (Collection<TmprintVO>) query.retrieveByClause(TmprintVO.class, sqlwhere);
		if(qryVos != null && TmqryVos.size()>0){
			for (TmprintVO tmprintVO : TmqryVos) {
				Integer lsh1 = Integer.valueOf(tmprintVO.getLsh());
				if(lsh1>Tmmaxlsh){
					Tmmaxlsh = lsh1;
				}
			}
		}
		if(Tmmaxlsh !=maxlsh){
			maxlsh =Tmmaxlsh;
			maxtmprintVO.setAttributeValue("lsh",maxlsh);
		}
		isfristserch =false;
		}
		maxlsh = maxlsh +1;
		maxstr = String.valueOf(maxlsh);
//		maxstr = String.format("%0"+strlen+"d", maxlsh);
		//更新流水号
		if(maxtmprintVO!=null){
			maxtmprintVO.setLsh(maxstr);
			new BaseDAO().updateVO(maxtmprintVO);
		}else{
			printsnrulevo.setLsh(maxstr);
			printsnrulevo.setStatus(VOStatus.NEW);
			new BaseDAO().insertVO(printsnrulevo);
		}
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("lsh", maxstr);
		map.put("isfristserch", isfristserch);
		return map;
	}
    /***
     * add by wjh 2020-12-28
     * 根据传递信息生成流水号
     * @param queryScheme
     * @return
     * @throws BusinessException
     * @throws Exception
     */

	@Override
	public String getLsh(TmprintVO printVO, StreamconfigVO streamVO)
			throws BusinessException, Exception {
		IUAPQueryBS query = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		PrintsnruleVO  printsnrulevo =new PrintsnruleVO();
		for(String culmname:printVO.getAttributeNames()){
			printsnrulevo.setAttributeValue(culmname, printVO.getAttributeValue(culmname));
		}
		//条码流水组成年月日部分
		String nyr = "";
		String sqlwhere = " 1=1 ";
		String streamValue = streamVO.getValue(); 
		String[] fields = streamValue.split(";");
		//通过循环条码流水配置进行最大流水查询条件的拼装
		for (String field : fields) {
			String date = printVO.getPrinttime().toStdString();
			if("year".equals(field)){
				nyr = date.substring(0, 4);
				sqlwhere = sqlwhere +" and printtime like '"+date.substring(0, 4)+"%'";
			}else if("batchcode".equals(field) && printVO.getVbatchcode()!=null){
				sqlwhere = sqlwhere +" and vbatchcode like '"+printVO.getVbatchcode()+"%'";
			}else if("month".equals(field)){
				nyr = date.substring(0, 4) + date.toString().substring(5, 7);
				sqlwhere = sqlwhere +" and printtime like '"+date.substring(0, 7)+"%'";
			}else if("day".equals(field)){
				nyr = date.substring(0, 4) + date.toString().substring(5, 7) + date.toString().substring(8, 10);
				sqlwhere = sqlwhere +" and printtime like '"+date.substring(0, 10)+"%'";
			}else if("producedate".equals(field) && printVO.getDproducedate()!=null){
				sqlwhere = sqlwhere +" and dproducedate like '"+printVO.getDproducedate().toStdString().substring(0, 10)+"%'";
			}else{
				Object value = printVO.getAttributeValue(field);
				if(value != null && !"".equals(value)){
					sqlwhere = sqlwhere +" and "+field+"='"+printVO.getAttributeValue(field)+"'";
				}
			}
		}
		//流水长度
		Integer strlen = streamVO.getStrlenth();
		//流水号最大值9999
		String max99 = "";
		for(int j = 0; j < strlen; j++){
			max99 = "9" + max99;
		}
		String maxstr = null;
		//过滤掉流水号为空的数据
		sqlwhere +=" and isnull(lsh,'~')!='~' ";
		Collection<PrintsnruleVO> qryVos = (Collection<PrintsnruleVO>) query.retrieveByClause(PrintsnruleVO.class, sqlwhere);
		Integer maxlsh = 0;
		PrintsnruleVO maxtmprintVO = null;
		if(qryVos != null && qryVos.size()>0){
			for (PrintsnruleVO tmprintVO : qryVos) {
				Integer lsh1 = Integer.valueOf(tmprintVO.getLsh());
				if(lsh1>maxlsh){
					maxlsh = lsh1;
					maxtmprintVO = tmprintVO;
				}
			}
			if(max99.equals(maxlsh.toString())){
//				throw new QiliBCException("流水号已达最大值"+max99+"，请在“流水号规则”配置界面修改流水号位数后重新打印！");
				throw new QiliBCException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
				        .getStrByID("common", "40H1-MESSAGE0011", null, new String[] {
				        		max99
				        })/*@res "流水号已达最大值{0}，请在“流水号规则”配置界面修改流水号位数后重新打印！"*/);
			}
		}else{
			maxlsh = 0;
		}
		maxlsh = maxlsh +1;
		maxstr = String.valueOf(maxlsh);
//		maxstr = String.format("%0"+strlen+"d", maxlsh);
		//更新流水号
		if(maxtmprintVO!=null){
			maxtmprintVO.setLsh(maxstr);
			new BaseDAO().updateVO(maxtmprintVO);
		}else{
			printsnrulevo.setLsh(maxstr);
			printsnrulevo.setStatus(VOStatus.NEW);
			new BaseDAO().insertVO(printsnrulevo);
		}
		return maxstr;
	}

	@Override
	public void save(List<TmprintVO> prints,List<TmprintupdateVO> printsVO) throws BusinessException, Exception {
		String pk_group=prints.get(0).getPk_group();
		String pk_org=prints.get(0).getPk_org();
		IUifService query = NCLocator.getInstance().lookup(IUifService.class);
		IBarcoderckMaintain insert=NCLocator.getInstance().lookup(IBarcoderckMaintain.class);
		Object pk_org_v = query.findColValue("org_stockorg_v", "pk_vid", "pk_stockorg='"+pk_org+"'");
//		String pk_org_v =HYPubBO_Client.findColValue("org_stockorg_v", "pk_vid", "pk_stockorg='"+pk_org+"'").toString();
		/***********************打印期初处理条码流水表开始**********************************/
		ArrayList<BarcodeRCKVO> rkbarcodess = new ArrayList<BarcodeRCKVO>();
		for(TmprintVO  trintvo:prints){
			String xmcode=trintvo.getPk_material()+"#"+trintvo.getVbatchcode()+"#"+trintvo.getVfree1();
			    String cmaterialvid=trintvo.getPk_material();//物料主键
			    UFBoolean isqc=trintvo.getIsqc();//物料主键
			    if(isqc.booleanValue()){
			    	BarcodeRCKVO barckVO = new BarcodeRCKVO();// 条码流水VO
			    	barckVO.setVbarcode(trintvo.getBarcode());// 条码值
			    	barckVO.setLsh(trintvo.getLsh());// 流水号
			    	barckVO.setSerialcode(trintvo.getSerialcode());//序列号
			    	barckVO.setXmbarcode(xmcode);
			    	barckVO.setPk_group(pk_group);// 集团
			    	barckVO.setPk_org(pk_org);// 组织
			    	barckVO.setDdate(trintvo.getDindate());// 日期
			    	barckVO.setInfla("入库");
			    	barckVO.setPk_material(cmaterialvid);// 物料主键
			    	barckVO.setMemo("期初");// 备注
			    	barckVO.setPk_stordoc(trintvo.getCwarehouseid());// 仓库
			    	barckVO.setPk_rack(trintvo.getClocationid());//货位主键
			    	barckVO.setDindate(trintvo.getDindate());// 入库日期
			    	barckVO.setDr(0);
			    	barckVO.setCastunitid(trintvo.getCastunitid());//辅单位
			    	barckVO.setPk_measdoc(trintvo.getPk_measdoc());// 主单位
			    	barckVO.setNnum(trintvo.getNnum());//主数量
			    	barckVO.setNastnum(trintvo.getNassistnum());//辅数量
			    	barckVO.setVchangerate(trintvo.getMeasrate());// 换算率
			    	barckVO.setVbatchcode(trintvo.getVbatchcode());//批次号
			    	barckVO.setCqualitylevelid(trintvo.getCqualitylevelid());//质量等级
			    	barckVO.setCstateid(trintvo.getCstateid());//库存状态
			    	barckVO.setVersion(trintvo.getVersion());//版本
			    	//以下为赋值自由辅助属性
			    	barckVO.setVfree1(trintvo.getVfree1());
			    	barckVO.setVfree2(trintvo.getVfree2());
			    	barckVO.setVfree3(trintvo.getVfree3());
			    	barckVO.setVfree4(trintvo.getVfree4());
			    	barckVO.setVfree5(trintvo.getVfree5());
			    	barckVO.setVfree6(trintvo.getVfree6());
			    	barckVO.setVfree7(trintvo.getVfree7());
			    	barckVO.setVfree8(trintvo.getVfree8());
			    	barckVO.setVfree9(trintvo.getVfree9());
			    	barckVO.setVfree10(trintvo.getVfree10());
			    	rkbarcodess.add(barckVO);
			    }
		}
		
		/***********************打印期初处理条码流水表结束**********************************/
		/***********************打印期初处理条码装箱单开始**********************************/
		ArrayList<String>  tpcode= new ArrayList<String>();
		for(int i=0;i<prints.size();i++){
			if("Y".equals(prints.get(i).getVdef11())){
				String key=prints.get(i).getPk_material()+"#"+prints.get(i).getVbatchcode()+"#"+prints.get(i).getVfree1();
				if(!tpcode.contains(key)){
					tpcode.add(key);
				}
			}
			
		}
		/****
		 * 把同一个托盘号的条码放入一个list，再把list放入map，mapkey是物料主键+批次号+托盘号，value是条码vo
		 */
		List<AggPackListVO> supvo = new ArrayList<AggPackListVO>();
		HashMap<String, ArrayList<TmprintVO> >  map= new HashMap<String, ArrayList<TmprintVO> >();
		if(tpcode.size()>0){
			for(int i=0;i<tpcode.size();i++){
				ArrayList<TmprintVO>  billvos= new ArrayList<TmprintVO>();
				String key=tpcode.get(i);
				for(int j=0;j<prints.size();j++){
					if("Y".equals(prints.get(j).getVdef11())){
						String keyv=prints.get(j).getPk_material()+"#"+prints.get(j).getVbatchcode()+"#"+prints.get(j).getVfree1();
						if(keyv.equals(key)){
							billvos.add(prints.get(j));
						}
					}
				}
				map.put(key, billvos);
			}
			
			//循环箱码sn
			for(String key : map.keySet()) {
			    String tmcode="TPM"+key;
			    Object pickhid = query.findColValue("qilibc_packlist", "pk_packlist", " xmbarcode='"+tmcode+"' and dr=0 ");
//			    Object pickhid =HYPubBO_Client.findColValue("qilibc_packlist", "pk_packlist", "xmbarcode='"+tmcode+"' and dr=0 ");
			    String[] keys=key.split("#");
			    if(null==pickhid || pickhid.equals("")){//如果之前已经有组托单则修改入如果没有则新增
					    	ArrayList<TmprintVO> zjlist = map.get(key);
					    	AggPackListVO packaggvo = new AggPackListVO();
					    	PackListHVO packhvo = new PackListHVO();
					    	packhvo.setPk_group(pk_group);
					    	packhvo.setPk_org(pk_org);
					    	packhvo.setPk_org_v(pk_org_v.toString());
					    	packhvo.setXmbarcode(tmcode);// 箱码
					    	packhvo.setIsrk(UFBoolean.FALSE);// 是否入库前装箱
					    	packhvo.setMaketime(new UFDateTime(System.currentTimeMillis()));// 制单时间
					    	packhvo.setAttributeValue("dr", 0);
					    	packhvo.setBztype("组托");// 装箱类型
					    	//packhvo.setScantime(scantime);
					    	packaggvo.setParent(packhvo);
					    	List<PackListBVO> packlist = new ArrayList<PackListBVO>();
					    	for (int i = 0; i < zjlist.size(); i++) {
					    		PackListBVO packbvo = new PackListBVO();
//								for(String field : zjlist.get(i).getAttributeNames()){
//									try{
//										packbvo.setAttributeValue(field, zjlist.get(i).getAttributeValue(field));
//									}catch (Exception e) {
//										try{
//											if(null!=zjlist.get(i).getAttributeValue(field)){
//												packbvo.setAttributeValue(field, new UFDouble(zjlist.get(i).getAttributeValue(field).toString()));
//											}
//										}catch (Exception e1) {
//										}
//									}
//								}
					    		packbvo.setPk_group(pk_group);
					    		packbvo.setPk_org(pk_org);
					    		packbvo.setVbarcode(zjlist.get(i).getBarcode());// 条码
					    		packbvo.setXmbarcode(key);// 箱码
					    		packbvo.setPk_material(zjlist.get(i).getPk_material());// 物料主键
					    		packbvo.setNnum(zjlist.get(i).getNnum());// 入库数量
					    		packbvo.setLsh(zjlist.get(i).getLsh());// 流水号
					    		packbvo.setSerialcode(zjlist.get(i).getSerialcode());//序列号
					    		packbvo.setVbatchcode(zjlist.get(i).getVbatchcode());
//					    		packbvo.setVdef1(zjlist.get(i).getVbatchcode());// 批次号
					    		packbvo.setCastunitid(zjlist.get(i).getCasscustid());// 辅单位
					    		packbvo.setPk_measdoc(zjlist.get(i).getCastunitid());// 主计量
					    		packbvo.setNastnum(zjlist.get(i).getNassistnum());// 辅入库数量
					    		packbvo.setVchangerate(zjlist.get(i).getMeasrate());// 换算率
					    		packbvo.setVfree1(zjlist.get(i).getVfree1());
					    		packbvo.setVfree2(zjlist.get(i).getVfree2());
					    		packbvo.setVfree3(zjlist.get(i).getVfree3());
					    		packbvo.setVfree4(zjlist.get(i).getVfree4());
					    		packbvo.setVfree5(zjlist.get(i).getVfree5());
					    		packbvo.setVfree6(zjlist.get(i).getVfree6());
					    		packbvo.setVfree7(zjlist.get(i).getVfree7());
					    		packbvo.setVfree8(zjlist.get(i).getVfree8());
					    		packbvo.setVfree9(zjlist.get(i).getVfree9());
					    		packbvo.setVfree10(zjlist.get(i).getVfree10());
					    		packbvo.setVdef1(zjlist.get(i).getVdef1());
					    		packbvo.setVdef2(zjlist.get(i).getVdef2());
					    		packbvo.setVdef3(zjlist.get(i).getVdef3());
					    		packbvo.setVdef4(zjlist.get(i).getVdef4());
					    		packbvo.setVdef5(zjlist.get(i).getVdef5());
					    		packbvo.setVdef6(zjlist.get(i).getVdef6());
					    		packbvo.setVdef7(zjlist.get(i).getVdef7());
					    		packbvo.setVdef8(zjlist.get(i).getVdef8());
					    		packbvo.setVdef9(zjlist.get(i).getVdef9());
					    		packbvo.setVdef10(zjlist.get(i).getVdef10());
					    		packbvo.setVdef11(zjlist.get(i).getVdef11());
					    		packbvo.setVdef12(zjlist.get(i).getVdef12());
					    		packbvo.setVdef13(zjlist.get(i).getVdef13());
					    		packbvo.setVdef14(zjlist.get(i).getVdef14());
					    		packbvo.setVdef15(zjlist.get(i).getVdef15());
					    		packbvo.setVdef16(zjlist.get(i).getVdef16());
					    		packbvo.setVdef17(zjlist.get(i).getVdef17());
					    		packbvo.setVdef18(zjlist.get(i).getVdef18());
					    		packbvo.setVdef19(zjlist.get(i).getVdef19());
					    		packbvo.setVdef20(zjlist.get(i).getVdef20());
					    		packbvo.setCprojectid(zjlist.get(i).getCprojectid());//项目
					    		packbvo.setDproducedate(zjlist.get(i).getDproducedate());//生产日期
					    		packbvo.setCqualitylevelid(zjlist.get(i).getCqualitylevelid());//质量等级
					    		packbvo.setCproductorid(zjlist.get(i).getCproductorid());//生产厂商
					    		packbvo.setCstateid(zjlist.get(i).getCstateid());//库存状态
					    		packbvo.setCffileid(zjlist.get(i).getCffileid());//特征码
					    		packbvo.setCvendorid(zjlist.get(i).getCvendorid());//供应商
					    		packbvo.setCasscustid(zjlist.get(i).getCasscustid());//客户
					    		packbvo.setAttributeValue("dr", 0);
					    		packlist.add(packbvo);
					    		packaggvo.setChildrenVO(packlist
					    				.toArray(new PackListBVO[packlist.size()]));
					    	}
					supvo.add(packaggvo);
			    }else{//如果托盘重复则提示是否继续
			    	
			    	PackListBVO []  bodyvos=(PackListBVO [])query.queryByCondition(PackListBVO.class, " pk_packlist='"+pickhid+"'");
			    	HashMap<String, String> mapsn= new HashMap<String, String>();
			    	for(PackListBVO  bodyvo:bodyvos){
			    		mapsn.put(bodyvo.getSerialcode(), bodyvo.getSerialcode()+"#"+bodyvo.getLsh());
		    		}
			    	ArrayList<TmprintVO> zjlist = map.get(key);
			    	List<PackListBVO> packlist = new ArrayList<PackListBVO>();
			    	for (int i = 0; i < zjlist.size(); i++) {
			    		String sn=zjlist.get(i).getSerialcode();
			    		String lsh=zjlist.get(i).getLsh();
			    		if(mapsn.get(sn)!=null){//装箱单
			    		   String [] lshs=mapsn.get(sn).split("#");
			    			if(null!=lshs && lshs.length>1){
			    				if(lshs[1]!=null && !lshs[1].equals(lsh)){
			    					Object name = query.findColValue("bd_material", "name", "pk_material ='"+keys[0]+"'");
//			    			    	Object name =HYPubBO_Client.findColValue("bd_material", "name", "pk_material ='"+keys[0]+"'");
			    			    	throw new Exception("物料:"+name+"批次:"+keys[1]+"托盘号:"+keys[2]+"桶号:"+lshs[1]+"与上次打印桶号:"+lsh+"不一致");
			    				}else{
			    					continue;
			    				}
			    			}
			    			
			    		}else{
			    		PackListBVO packbvo = new PackListBVO();
			    		packbvo.setPk_packlist(pickhid.toString());
			    		packbvo.setPk_group(pk_group);
			    		packbvo.setPk_org(pk_org);
			    		packbvo.setVbarcode(zjlist.get(i).getBarcode());// 条码
			    		packbvo.setXmbarcode(key);// 箱码
			    		packbvo.setPk_material(zjlist.get(i).getPk_material());// 物料主键
			    		packbvo.setNnum(zjlist.get(i).getNnum());// 入库数量
			    		packbvo.setLsh(zjlist.get(i).getLsh());// 流水号
			    		packbvo.setSerialcode(zjlist.get(i).getSerialcode());//序列号
			    		packbvo.setVbatchcode(zjlist.get(i).getVbatchcode());
//			    		packbvo.setVdef1(zjlist.get(i).getVbatchcode());// 批次号
			    		packbvo.setCastunitid(zjlist.get(i).getCasscustid());// 辅单位
			    		packbvo.setPk_measdoc(zjlist.get(i).getCastunitid());// 主计量
			    		packbvo.setNastnum(zjlist.get(i).getNassistnum());// 辅入库数量
			    		packbvo.setVchangerate(zjlist.get(i).getMeasrate());// 换算率
			    		packbvo.setVfree1(zjlist.get(i).getVfree1());
			    		packbvo.setVfree2(zjlist.get(i).getVfree2());
			    		packbvo.setVfree3(zjlist.get(i).getVfree3());
			    		packbvo.setVfree4(zjlist.get(i).getVfree4());
			    		packbvo.setVfree5(zjlist.get(i).getVfree5());
			    		packbvo.setVfree6(zjlist.get(i).getVfree6());
			    		packbvo.setVfree7(zjlist.get(i).getVfree7());
			    		packbvo.setVfree8(zjlist.get(i).getVfree8());
			    		packbvo.setVfree9(zjlist.get(i).getVfree9());
			    		packbvo.setVfree10(zjlist.get(i).getVfree10());
			    		packbvo.setVdef1(zjlist.get(i).getVdef1());
			    		packbvo.setVdef2(zjlist.get(i).getVdef2());
			    		packbvo.setVdef3(zjlist.get(i).getVdef3());
			    		packbvo.setVdef4(zjlist.get(i).getVdef4());
			    		packbvo.setVdef5(zjlist.get(i).getVdef5());
			    		packbvo.setVdef6(zjlist.get(i).getVdef6());
			    		packbvo.setVdef7(zjlist.get(i).getVdef7());
			    		packbvo.setVdef8(zjlist.get(i).getVdef8());
			    		packbvo.setVdef9(zjlist.get(i).getVdef9());
			    		packbvo.setVdef10(zjlist.get(i).getVdef10());
			    		packbvo.setVdef11(zjlist.get(i).getVdef11());
			    		packbvo.setVdef12(zjlist.get(i).getVdef12());
			    		packbvo.setVdef13(zjlist.get(i).getVdef13());
			    		packbvo.setVdef14(zjlist.get(i).getVdef14());
			    		packbvo.setVdef15(zjlist.get(i).getVdef15());
			    		packbvo.setVdef16(zjlist.get(i).getVdef16());
			    		packbvo.setVdef17(zjlist.get(i).getVdef17());
			    		packbvo.setVdef18(zjlist.get(i).getVdef18());
			    		packbvo.setVdef19(zjlist.get(i).getVdef19());
			    		packbvo.setVdef20(zjlist.get(i).getVdef20());
			    		packbvo.setCprojectid(zjlist.get(i).getCprojectid());//项目
			    		packbvo.setDproducedate(zjlist.get(i).getDproducedate());//生产日期
			    		packbvo.setCqualitylevelid(zjlist.get(i).getCqualitylevelid());//质量等级
			    		packbvo.setCproductorid(zjlist.get(i).getCproductorid());//生产厂商
			    		packbvo.setCstateid(zjlist.get(i).getCstateid());//库存状态
			    		packbvo.setCffileid(zjlist.get(i).getCffileid());//特征码
			    		packbvo.setCvendorid(zjlist.get(i).getCvendorid());//供应商
			    		packbvo.setCasscustid(zjlist.get(i).getCasscustid());//客户
			    		packbvo.setDr(0);
			    		packlist.add(packbvo);
			    	  }
			    	}
			    	if(packlist.size()>0){
			    		query.insertAry(packlist.toArray(new PackListBVO[packlist.size()]));
			    	}
//			    	//如果是修改则
//			    	Object name =HYPubBO_Client.findColValue("bd_material", "name", "pk_material ='"+keys[0]+"'");
//			    	throw new Exception("物料:"+name+"批次:"+keys[1]+"托盘号重复:"+keys[2]);
			    }
		}
		}
		
		
		/***********************打印期初处理条码装箱单表结束**********************************/
		
		/************************开始保存标签修改数据、条码流水数据、装箱单数据*********************/
		ArrayList<String>  list=new ArrayList<String>();
		IBarcodeconfigMaintain  updatetoos= NCLocator.getInstance().lookup(IBarcodeconfigMaintain.class);
		if(printsVO.size()>0){
			//打印后存储打印数据到标签表
			for(TmprintupdateVO printd:printsVO){
				list.add(printd.getSerialcode());
			}
			IDQueryBuilder idQuery = new IDQueryBuilder();
			SqlBuilder builder = new SqlBuilder();
			builder.append("delete from qilibc_tmprintupdate  where 1=1" );
			builder.append("  and ");
			builder.append(idQuery.buildSQL("serialcode",list.toArray(new String[0])));
			updatetoos.updatebs(builder.toString());
			//打印后存储打印数据到标签表
//			query.insertAry(printsVO.toArray(new TmprintupdateVO[printsVO.size()]));
			BatchOperateVO vo = new BatchOperateVO();
			vo.setAddObjs(printsVO.toArray(new TmprintupdateVO[printsVO.size()])); 
			insert.batchSave(vo);


		}
		for(TmprintupdateVO  vo:printsVO){
			String sql="update sn_serialno set vdef1='"+vo.getLsh()+"' where vcode='"+vo.getSerialcode()+"' and pk_group='"+vo.getPk_group()+"' and pk_org='"+vo.getPk_org()+"'";
//			String sq2="update qilibc_tmprintupdate set vfree1=vfree2,vfree2='~' where serialcode='"+vo.getSerialcode()+"' and pk_group='"+vo.getPk_group()+"' and pk_org='"+vo.getPk_org()+"'";
			updatetoos.updatebs(sql);
			SqlBuilder builder = new SqlBuilder();
			builder.append("delete from qilibc_tmprintupdate  where 1=1 and pk_material='"+vo.getPk_material()+"' and vbatchcode='"+vo.getVbatchcode()+"' and lsh='"+vo.getLsh()+"' and vdef20='预标签'" );
			updatetoos.updatebs(builder.toString());

//			updatetoos.updatebs(sq2);
		}
		if(rkbarcodess.size()>0){
//			query.insertAry(rkbarcodess.toArray(new BarcodeRCKVO[rkbarcodess.size()]));
			BatchOperateVO vo = new BatchOperateVO();
			vo.setAddObjs(rkbarcodess.toArray(new BarcodeRCKVO[rkbarcodess.size()])); 
			insert.batchSave(vo);
		}
		if(supvo.size()>0){
			IQilibc_packlistMaintain pack = NCLocator.getInstance().lookup(
					IQilibc_packlistMaintain.class);
			pack.insert(supvo.toArray(new AggPackListVO[supvo.size()]),
					null);
		}
	}
	@Override
	public void savetmupdate(List<TmprintupdateVO> printsVO) throws BusinessException, Exception {
		IBarcoderckMaintain insert=NCLocator.getInstance().lookup(IBarcoderckMaintain.class);

		ArrayList<String>  list=new ArrayList<String>();
		IBarcodeconfigMaintain  updatetoos= NCLocator.getInstance().lookup(IBarcodeconfigMaintain.class);
		if(printsVO.size()>0){
			//预标签打印不删除之前打印过的条码 update by wtx 2021-12-14
			//打印后存储打印数据到标签表
//			for(TmprintupdateVO printd:printsVO){
//				list.add(printd.getSerialcode());
//				printd.setVdef20("预标签");
//				SqlBuilder builder = new SqlBuilder();
//				builder.append("delete from qilibc_tmprintupdate  where 1=1 and pk_material='"+printd.getPk_material()+"' and vbatchcode='"+printd.getVbatchcode()+"' and lsh='"+printd.getLsh()+"' and vdef20='预标签'" );
//				updatetoos.updatebs(builder.toString());
//			}
//			IDQueryBuilder idQuery = new IDQueryBuilder();
//			SqlBuilder builder = new SqlBuilder();
//			builder.append("delete from qilibc_tmprintupdate  where 1=1" );
//			builder.append("  and ");
//			builder.append(idQuery.buildSQL("serialcode",list.toArray(new String[0])));
//			updatetoos.updatebs(builder.toString());
			//打印后存储打印数据到标签表
			BatchOperateVO vo = new BatchOperateVO();
			vo.setAddObjs(printsVO.toArray(new TmprintupdateVO[printsVO.size()])); 
			insert.batchSave(vo);
		}
	}


}
