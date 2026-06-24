package nc.impl.bd.material.baseinfo;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nc.bs.bd.baseservice.IBaseServiceConst;
import nc.bs.bd.baseservice.busilog.IBusiOperateConst;
import nc.bs.bd.baseservice.md.SingleBaseService;
import nc.bs.bd.baseservice.md.VOArrayUtil;
import nc.bs.bd.baseservice.validator.RefPkExistsValidator;
import nc.bs.bd.cache.CacheProxy;
import nc.bs.bd.material.baseinfo.validator.MaterialAddClassDisableValidator;
import nc.bs.bd.material.baseinfo.validator.MaterialBaseClassValidator;
import nc.bs.bd.material.baseinfo.validator.MaterialConvertValidator;
import nc.bs.bd.material.baseinfo.validator.MaterialDataUpgradeValidator;
import nc.bs.bd.material.baseinfo.validator.MaterialMainMeasdocRefValidator;
import nc.bs.bd.material.baseinfo.validator.MaterialMeasRateValidator;
import nc.bs.bd.material.baseinfo.validator.MaterialRetailRuleValidator;
import nc.bs.bd.material.baseinfo.validator.MaterialTaxtypeUniqueValidator;
import nc.bs.bd.material.baseinfo.validator.MaterialUniqueRuleValidator;
import nc.bs.bd.material.baseinfo.validator.MaterialUniqueUtil;
import nc.bs.bd.pub.ansy.ReallyTread;
import nc.bs.bd.service.ValueObjWithErrLog;
import nc.bs.businessevent.EventDispatcher;
import nc.bs.businessevent.IEventType;
import nc.bs.businessevent.UsePermChangeEvent;
import nc.bs.businessevent.bd.BDCommonEvent;
import nc.bs.businessevent.bd.BDCommonEventUtil;
import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.framework.core.service.TimeService;
import nc.bs.framework.exception.ComponentNotFoundException;
import nc.bs.logging.Logger;
import nc.bs.ls.TaskConfigBuilder;
import nc.bs.uif2.validation.IValidationService;
import nc.bs.uif2.validation.NullValueValidator;
import nc.bs.uif2.validation.ValidationException;
import nc.bs.uif2.validation.ValidationFailure;
import nc.bs.uif2.validation.ValidationFrameworkUtil;
import nc.bs.uif2.validation.Validator;
import nc.itf.bd.config.mode.IBDMode;
import nc.itf.bd.config.uniquerule.UniqueRuleConst;
import nc.itf.bd.material.assign.IMaterialAssignService;
import nc.itf.bd.material.baseinfo.IMaterialBaseInfoService;
import nc.itf.bd.pub.IBDMetaDataIDConst;
import nc.itf.fi.pub.SysInit;
import nc.itf.ls.ILightScheduler;
import nc.itf.org.IOrgConst;
import nc.jdbc.framework.SQLParameter;
import nc.jdbc.framework.processor.ArrayProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.pub.billcode.itf.IBillcodeManage;
import nc.pub.billcode.vo.BillCodeContext;
import nc.pubitf.bd.material.IMaterailUpdateCheckForGL;
import nc.vo.bd.ansylog.AnsyDelLogVO;
import nc.vo.bd.config.BDModeSelectedVO;
import nc.vo.bd.errorlog.ErrLogReturnValue;
import nc.vo.bd.errorlog.ErrorLogUtil;
import nc.vo.bd.material.IMaterialConst;
import nc.vo.bd.material.MaterialConvertVO;
import nc.vo.bd.material.MaterialVO;
import nc.vo.bd.pub.SingleDistributedUpdateValidator;
import nc.vo.bd.supplier.SupplierVO;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.ls.TaskConfig;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.trade.sqlutil.IInSqlBatchCallBack;
import nc.vo.trade.sqlutil.InSqlBatchCaller;
import nc.vo.trade.voutils.VOUtil;
import nc.vo.util.AuditInfoUtil;
import nc.vo.util.BDModeManager;
import nc.vo.util.BDPKLockUtil;
import nc.vo.util.BDVersionValidationUtil;
import nc.vo.util.bizlock.BizlockDataUtil;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

/**
 * 物料后台服务实现类
 * 
 * @author jiangjuna
 */
@SuppressWarnings("restriction")
public class MaterialBaseInfoServiceImpl extends SingleBaseService<MaterialVO>
        implements IMaterialBaseInfoService {

    private IMaterialAssignService assignService = null;// 物料分配服务

    private BaseDAO baseDAO = null;

    private IBillcodeManage billcodeManage = null;// 单据编码管理服务

    private MaterialVersionDAO materialVersionDAO = null;

    private ILightScheduler lightScheduler;

    public MaterialBaseInfoServiceImpl() {
        super(IBDMetaDataIDConst.MATERIAL, new String[] {
            IMaterialConst.MATERIAL_CONVERT, IMaterialConst.MATERIAL_TAXTYPE
        });
    }

    @Override
    public ErrLogReturnValue copyInsertMaterial(MaterialVO vo,
            String pk_material) throws BusinessException {
        vo = this.insertVO(vo);
        return this.getAssignService().copyAssignMaterialByPk(vo, pk_material,
                true);
    }

    @Override
    public ErrLogReturnValue createMaterialVersion(MaterialVO vo,
            String pk_material) throws BusinessException {

        MaterialVO oldVO = this.queryOldVO(pk_material);

        // 主键锁（锁定同一物料的全部版本数据）
        this.lockSourcePKs(oldVO);

        // 业务锁
        BizlockDataUtil.lockDataByBizlock(vo);

        // 版本校验（时间戳校验）
        BDVersionValidationUtil.validateSuperVO(oldVO);

        // 处理新版本的数据
        vo = this.getNewVersionData(vo, oldVO);

        // 新增逻辑校验
        this.insertValidateVO(vo);

        // 设置审计信息
        this.setInsertAuditInfo(vo);

        // 插入前事件通知
        this.fireBeforeInsertEvent(vo);

        // 库操作
        String pk = super.dbInsertVO(vo);
        vo.setPrimaryKey(pk);

        // 更新原最新版本的最新版本标志（新增数据的版本-1为原最新版本）
        this.updateMaterialLatesetFlag(vo.getPk_source(), vo.getVersion() - 1,
                false);

        this.getMaterialVersionDAO().updateMaterialVersion(vo);

        // 重新检索出插入的VO
        vo = this.retrieveVO(pk);

        // 缓存通知
        this.notifyVersionChangeWhenDataInserted(vo);

        // 插入事件后通知
        this.fireAfterInsertEvent(vo);

        MaterialVO[] vos = this.synMaterialCode(vo, oldVO);

        ErrLogReturnValue result =
                this.getAssignService().copyAssignMaterialByPk(vo, pk_material,
                        false);

        // 业务日志
        this.getBusiLogUtil().writeBusiLog(
                IMaterialConst.BUSI_OPERATE_CREATEVERSION, null, vo);
        return new ErrLogReturnValue(result.getErrLogResult(), new Object[] {
            vo, vos
        }, result.getTotalNum());
    }

    @Override
    public void deleteMaterial(MaterialVO vo) throws BusinessException {
        this.deleteVO(vo);
    }

    @Override
    public ValueObjWithErrLog disableMaterial(MaterialVO[] vos)
            throws BusinessException {
        return this.disableVO(vos);
    }

    @Override
    public ValueObjWithErrLog enableMaterial(MaterialVO[] vos)
            throws BusinessException {
        ValueObjWithErrLog log = this.enableVO(vos);
        dealWithAnsyLog(vos);
        MaterialVO[] materialvos =
                VOArrayUtil.convertToVOArray(MaterialVO.class, log.getVos());
        getBaseDAO().updateVOArray(resetMaterialvos(materialvos), new String[] {
            MaterialVO.DELETESTATE, MaterialVO.DELPERSON, MaterialVO.DELTIME
        });
        SuperVO[] afterupdatevos = getAfterUpdatevos(materialvos);
        log.setVos(afterupdatevos);

        return log;
    }

    private MaterialVO[] resetMaterialvos(MaterialVO[] vos) {
        if (vos != null && vos.length > 0) {
            for (int i = 0; i < vos.length; i++) {
                vos[i].setDeletestate(0);
                vos[i].setDelperson(null);
                vos[i].setDeltime(null);
            }

        }
        return vos;
    }

    private void dealWithAnsyLog(MaterialVO[] vos) throws BusinessException {
        List<String> list =
                VOUtil.extractFieldValues(vos, MaterialVO.PK_MATERIAL, null);
        InSqlBatchCaller caller =
                new InSqlBatchCaller(list.toArray(new String[0]));
        try {
            List<AnsyDelLogVO> ansylist =
                    (List<AnsyDelLogVO>) caller
                            .execute(new IInSqlBatchCallBack() {
                                List<AnsyDelLogVO> list =
                                        new ArrayList<AnsyDelLogVO>();

                                @Override
                                public Object doWithInSql(String inSql)
                                        throws BusinessException, SQLException {
                                    Collection<AnsyDelLogVO> col =
                                            getBaseDAO().retrieveByClause(
                                                    AnsyDelLogVO.class,
                                                    AnsyDelLogVO.PK_BASDOC
                                                            + " in " + inSql);
                                    if (col != null && col.size() > 0) {
                                        this.list.addAll(col);
                                    }
                                    return this.list;
                                }
                            });
            getBaseDAO().deleteVOList(ansylist);
        }

        catch (SQLException e) {
            Logger.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }

    }

    public MaterialVersionDAO getMaterialVersionDAO() {
        if (this.materialVersionDAO == null) {
            this.materialVersionDAO = new MaterialVersionDAO();
        }
        return this.materialVersionDAO;
    }

    @Override
    public MaterialVO insertMaterial(MaterialVO vo) throws BusinessException {
        if (this.isOrgData(vo)) {
            vo = this.insertVO(vo);
            this.getAssignService().assignMaterialToSelfOrg(vo);
            return vo;
        }
        else {
            return this.insertVO(vo);
        }
    }

    
    String pkorg ;
    @Override
    public MaterialVO insertVO(MaterialVO vo) throws BusinessException {
    	/*
    	 * jiuzhoupharma
    	 * 这里拦截处理物料编码，公司编码为10/15/16时，启动物料编码的特殊处理
    	 * 1、检查物料所属公司，如果所属公司时，调用10公司编规则并且赋予物料号
    	 */
    	pkorg  = vo.getPk_org();
    	UFBoolean paraBoolean = SysInit.getParaBoolean(pkorg, "BD307");//参数【共享本部物料流水】
    	if(paraBoolean == UFBoolean.TRUE){
    		pkorg = "0001V11000000000374B";
    	}
        BillCodeContext billCodeContext =
                this.getBillcodeManage().getBillCodeContext(
                        IMaterialConst.BILLCODE_MATERIAL, vo.getPk_group(),
                        pkorg);
        // 设置本版为1，原始版本为null
        vo.setVersion(Integer.valueOf(1));
        vo.setPk_source(null);
        vo.setLatest(UFBoolean.TRUE);
        vo = this.insertWithAutoCode(vo, billCodeContext);
        // 前编码时提交编码（接口里会判断是否连续）
        if (billCodeContext != null && billCodeContext.isPrecode()) {
            this.getBillcodeManage().commitPreBillCode(
                    IMaterialConst.BILLCODE_MATERIAL, vo.getPk_group(),
                    vo.getPk_org(), vo.getCode());
        }
        return vo;
    }

    @Override
    public MaterialVO[] updateMaterial(MaterialVO vo) throws BusinessException {
        if (vo == null) {
            return null;
        }

        // 更新时的加锁操作
        this.updatelockOperate(vo);

        // 校验版本
        BDVersionValidationUtil.validateSuperVO(vo);

        // 获取更新前的编码与原始版本数据主键
        MaterialVO oldVO = this.retrieveVO(vo.getPrimaryKey());

        this.setVersionField(vo, oldVO);

        // 业务校验逻辑
        this.updateValidateVO(oldVO, vo);

        MaterialVO[] oldVOs =
                this.queryMaterialOldVOsWhenCodeChanged(vo, oldVO);
        MaterialVO[] vos = this.getSynCodeVOs(vo, oldVOs);

        // 设置审计信息
        AuditInfoUtil.updateData(vo);
        AuditInfoUtil.updateData(vos);

        // 更新前事件处理
        BDCommonEventUtil eventUtil = new BDCommonEventUtil(this.getMDId());
        eventUtil.setOldObjs(ArrayUtils.add(oldVOs, 0, oldVO));
        eventUtil.dispatchUpdateBeforeEvent(ArrayUtils.add(vos, 0, vo));

        // 库操作
        this.getPersistenceUtil().updateVO(vo);
        String[] fields = new String[] {
            MaterialVO.CODE, MaterialVO.MODIFIER, MaterialVO.MODIFIEDTIME
        };
        this.getPersistenceUtil().updateVOWithAttrs(fields, vos);

        // 更新最新版本表数据
        MaterialVO latestVO = this.getLatestVO(vo, vos);
        if (latestVO != null) {
            this.getMaterialVersionDAO().updateMaterialVersion(latestVO);
        }

        // 更新缓存
        this.notifyVersionChangeWhenDataUpdated(vo);

        // 重新检索出新数据
        String[] pks = VOArrayUtil.getPrimaryKeyArray(vos);
        vos =
                this.retrieveVO((String[]) ArrayUtils.add(pks, 0,
                        vo.getPrimaryKey()));

        // 记录修改业务日志
        // this.getBusiLogUtil().writeBusiLog(IBusiOperateConst.EDIT, null, vo);
        this.writeUpdatedBusiLog(oldVO, vo);

        // 更新后事件通知
        eventUtil.dispatchUpdateAfterEvent((Object[]) vos);

        return vos;
    }

    @Override
    public ErrLogReturnValue upgradeMaterials(MaterialVO[] vos)
            throws BusinessException {

        // 异常日志工具
        String pk_user = InvocationInfoProxy.getInstance().getUserId();
        ErrorLogUtil util =
                new ErrorLogUtil(IBDMetaDataIDConst.MATERIAL, pk_user,
                        nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
                                "10140mag", "010140mag0129")/* @res "物料升级" */,
                        false);

        if (vos == null || vos.length == 0) {
            return null;
        }

        // 校验管控模式
        BDModeSelectedVO bdmodeVO =
                BDModeManager.getInstance().getBDModeSelectedVOByMDClassID(
                        IBDMetaDataIDConst.MATERIAL);
        if (IBDMode.SCOPE_GROUP_ORG != bdmodeVO.getManagemode().intValue()) {
            throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl
                    .getNCLangRes().getStrByID("10140mag", "010140mag0130")
            /* @res "只有当管理模式为集团+组织的时候，才可以升级！" */);
        }

        // 主键锁（锁定同一物料的全部版本数据）
        this.lockSourcePKs(vos);

        // 校验版本
        BDVersionValidationUtil.validateSuperVO(vos);

        // 过滤需升级的数据（业务单元的物料），并记录原始版本主键与vo的映射关系，
        Map<String, MaterialVO> upgradeMap = new HashMap<String, MaterialVO>();
        for (int i = 0; i < vos.length; i++) {
            if (!StringUtils.equals(vos[i].getPk_org(), vos[i].getPk_group())) {
                upgradeMap.put(vos[i].getPk_source(), vos[i]);
            }
        }

        // 若数据全部为集团物料，不需升级
        if (upgradeMap.isEmpty()) {
            throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl
                    .getNCLangRes().getStrByID("10140mag", "010140mag0131")
            /* @res "待升级物料全部为集团数据，不需升级。" */);
        }

        // 查询待升级物料的全部版本数据
        Map<String, List<MaterialVO>> sourcePk_voList_map =
                this.queryAllVersionData(upgradeMap.keySet().toArray(
                        new String[0]));
        if (sourcePk_voList_map == null || sourcePk_voList_map.isEmpty()) {
            throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl
                    .getNCLangRes().getStrByID("10140mag", "010140mag0132")
            /* @res "数据已被删除，请刷新界面。" */);
        }

        Map<String, MaterialVO> pk_oldVO_map =
                new HashMap<String, MaterialVO>();// 记录修改前数据

        int count = 0;

        // 设置数据为所属组织为集团
        for (String pk_source : sourcePk_voList_map.keySet()) {
            List<MaterialVO> list = sourcePk_voList_map.get(pk_source);
            for (MaterialVO materialVO : list) {
                pk_oldVO_map.put(materialVO.getPrimaryKey(),
                        (MaterialVO) materialVO.clone());
                materialVO.setPk_org(materialVO.getPk_group());
            }
            count++;
            // 业务锁
            BizlockDataUtil.lockDataByBizlock(list.toArray(new MaterialVO[0]));
        }

        // 按唯一性等校验过滤可升级的数据
        this.filterBreakUniqueRuleSelfVOs(sourcePk_voList_map, util);
        this.filterCanUpgradeVO(sourcePk_voList_map, util);

        if (sourcePk_voList_map != null && sourcePk_voList_map.size() > 0) {
            List<MaterialVO> toUpdateVOList = new ArrayList<MaterialVO>();
            for (List<MaterialVO> list : sourcePk_voList_map.values()) {
                toUpdateVOList.addAll(list);
            }
            vos = toUpdateVOList.toArray(new MaterialVO[0]);
            MaterialVO[] oldVOs = new MaterialVO[vos.length];
            for (int i = 0; i < vos.length; i++) {
                oldVOs[i] = pk_oldVO_map.get(vos[i].getPrimaryKey());
            }

            // 设置审计信息
            AuditInfoUtil.addData(vos);

            // 事件前通知
            EventDispatcher.fireEvent(new BDCommonEvent(this.getMDId(),
                    IEventType.TYPE_UPGRADE_BEFORE, oldVOs, vos));

            // 库操作，仅更新所属组织、最后修改人、最后修改时间等字段
            String[] updateFields = new String[] {
                MaterialVO.PK_ORG, MaterialVO.MODIFIER, MaterialVO.MODIFIEDTIME
            };
            this.getBaseDAO().updateVOArray(vos, updateFields);

            this.synMaterialVersionForUpgrade(vos, updateFields);

            // 缓存通知
            CacheProxy.fireDataUpdated(vos[0].getTableName());

            // 重新检索出插入的VO
            vos = this.retrieveVO(VOArrayUtil.getPrimaryKeyArray(vos));

            // 事件前通知
            EventDispatcher.fireEvent(new BDCommonEvent(this.getMDId(),
                    IEventType.TYPE_UPGRADE_AFTER, oldVOs, vos));
            // 数据权限事件通知
            EventDispatcher.fireEvent(new UsePermChangeEvent(this.getMDId(),
                    IEventType.TYPE_DATAPERM_CHANGE));

            // 业务日志
            this.getBusiLogUtil().writeModefyBusiLog(IBusiOperateConst.UPGRADE,
                    vos, oldVOs);
            // this.writeUpdatedBusiLog(oldVO, vo);
        }
        return util.getErrLogReturnValue(vos, count);
    }

    @Override
    protected void dbDeleteVO(MaterialVO vo) throws BusinessException {
        this.getAssignService().cancelAssignMaterial(vo);
        super.dbDeleteVO(vo);
        this.synMaterialVersionForDelete(vo);
    }

    @Override
    protected void dbDisableVO(MaterialVO... vos) throws BusinessException {
        super.dbDisableVO(vos);
        String[] fields =
                new String[] {
                    IBaseServiceConst.ENABLESTATE_FIELD,
                    IBaseServiceConst.MODIFIER_FIELD,
                    IBaseServiceConst.MODIFIEDTIME_FIELD
                };
        this.getMaterialVersionDAO().updateMaterialVersion(fields, vos);
    }

    @Override
    protected void dbEnableVO(MaterialVO... vos) throws BusinessException {
        super.dbEnableVO(vos);
        String[] fields =
                new String[] {
                    IBaseServiceConst.ENABLESTATE_FIELD,
                    IBaseServiceConst.MODIFIER_FIELD,
                    IBaseServiceConst.MODIFIEDTIME_FIELD
                };
        this.getMaterialVersionDAO().updateMaterialVersion(fields, vos);
    }

    @Override
    protected String dbInsertVO(MaterialVO vo) throws BusinessException {
        String pk_material = super.dbInsertVO(vo);
        // 设置并更新原始版本的主键
        vo.setPk_material(pk_material);
        vo.setPk_source(pk_material);
        this.getBaseDAO().updateVO(vo, new String[] {
            MaterialVO.PK_SOURCE
        });
        this.getMaterialVersionDAO().insertMaterialVersion(vo);
        return pk_material;
    }

    @Override
    protected void deletelockOperate(MaterialVO vo) throws BusinessException {
        // 主键锁（锁定同一物料的全部版本数据）
        this.lockSourcePKs(vo);
        super.deletelockOperate(vo);
    }

    @Override
    protected Validator[] getInsertValidator() {
        // 非空校验
        NullValueValidator notNullValidator =
                NullValueValidator.createMDNullValueValidator(MaterialVO.class
                        .getName(), Arrays.asList(MaterialVO.PK_GROUP,
                        MaterialVO.PK_ORG, MaterialVO.CODE, MaterialVO.NAME,
                        MaterialVO.PK_MARBASCLASS, MaterialVO.PK_MEASDOC,
                        MaterialVO.PK_MATTAXES, MaterialVO.ENABLESTATE,
                        MaterialVO.VERSION));
        // 与主计量单位换算系数校验
        MaterialMeasRateValidator measRateValidator =
                new MaterialMeasRateValidator();
        // 辅计量管理校验
        MaterialConvertValidator convertValidator =
                new MaterialConvertValidator(null);
        // 物料税类唯一性校验
        MaterialTaxtypeUniqueValidator taxtypeUniqueValidator =
                new MaterialTaxtypeUniqueValidator(null);      
		 // 分类的存在性校验
		        RefPkExistsValidator refExistsValidator =
                new RefPkExistsValidator(MaterialVO.PK_MARBASCLASS);
      /*  //类别
        RefPkExistsValidator varietyValidator =
                new RefPkExistsValidator("cvariety_148");*/
        //功效
	        RefPkExistsValidator cdosageform =
		                new RefPkExistsValidator("cdosageform_148");
        //剂型
        RefPkExistsValidator cefficacy =
                new RefPkExistsValidator("cefficacy_148");
        
        // 物料基本分类末级校验
        MaterialBaseClassValidator classValidator =
                new MaterialBaseClassValidator();
        // 物料基本分类封存校验
        MaterialAddClassDisableValidator classDisableValidator =
                new MaterialAddClassDisableValidator();
        // 唯一性校验
        MaterialUniqueRuleValidator uniqueValidator =
                new MaterialUniqueRuleValidator();
        // "适用零售"校验器
        MaterialRetailRuleValidator retailRuleValidator =
                new MaterialRetailRuleValidator(null);
        return new Validator[] {
            notNullValidator, measRateValidator, convertValidator,
            taxtypeUniqueValidator, refExistsValidator, classValidator,
            classDisableValidator, uniqueValidator, retailRuleValidator,cefficacy,cdosageform
        };
    }

    @Override
    protected Validator[] getUpdateValidator(MaterialVO oldVO) {
        NullValueValidator notNullValidator =
                NullValueValidator.createMDNullValueValidator(MaterialVO.class
                        .getName(), Arrays.asList(MaterialVO.PK_GROUP,
                        MaterialVO.PK_ORG, MaterialVO.CODE, MaterialVO.NAME,
                        MaterialVO.PK_MARBASCLASS, MaterialVO.PK_MEASDOC,
                        MaterialVO.PK_MATTAXES, MaterialVO.ENABLESTATE,
                        MaterialVO.VERSION));
        MaterialMeasRateValidator measRateValidator =
                new MaterialMeasRateValidator();
        MaterialConvertValidator convertValidator =
                new MaterialConvertValidator(oldVO);
        MaterialTaxtypeUniqueValidator taxtypeUniqueValidator =
                new MaterialTaxtypeUniqueValidator(oldVO);
        RefPkExistsValidator refExistsValidator =
                new RefPkExistsValidator(MaterialVO.PK_MARBASCLASS);

     /* //类别
        RefPkExistsValidator varietyValidator =
                new RefPkExistsValidator("cvariety_148");*/
        //功效
        RefPkExistsValidator cdosageform =
                new RefPkExistsValidator("cdosageform_148");
        //剂型
        RefPkExistsValidator cefficacy =
                new RefPkExistsValidator("cefficacy_148");
        
        MaterialBaseClassValidator classValidator =
                new MaterialBaseClassValidator();
        MaterialMainMeasdocRefValidator measdocValidator =
                new MaterialMainMeasdocRefValidator(oldVO);
        MaterialUniqueRuleValidator uniqueValidator =
                new MaterialUniqueRuleValidator();
        // "适用零售"校验器
        MaterialRetailRuleValidator retailRuleValidator =
                new MaterialRetailRuleValidator(oldVO);
        return new Validator[] {
            notNullValidator, measRateValidator, convertValidator,
            taxtypeUniqueValidator, refExistsValidator, classValidator,
            measdocValidator, uniqueValidator,
            new SingleDistributedUpdateValidator(), retailRuleValidator
            ,cdosageform,cefficacy
        };
    }

    @Override
    protected void updatelockOperate(MaterialVO vo) throws BusinessException {
        // 主键锁（锁定同一物料的全部版本数据）
        this.lockSourcePKs(vo);
        super.updatelockOperate(vo);
    }

    /**
     * 过滤升级数据中自身唯一性重复的数据
     * 
     * @param sourcePk_voList_map
     * @param util
     * @throws BusinessException
     */
    private void filterBreakUniqueRuleSelfVOs(
            Map<String, List<MaterialVO>> sourcePk_voList_map, ErrorLogUtil util)
            throws BusinessException {

        // 校验数据自身之间是否满足唯一性（若从同一组织下选择要升级的数据，则彼此间的不重复，不需此校验）
        Map<MaterialVO, List<String>> map =
                MaterialUniqueUtil
                        .getBreakRuleofMaterialVOsSelf(sourcePk_voList_map);
        if (map != null && map.size() > 0) {
            Map<String, List<String>> result =
                    new HashMap<String, List<String>>();
            for (MaterialVO material : map.keySet()) {
                List<String> breakRuleList = map.get(material);
                // 若sourcePK_vo_map为空时
                if (breakRuleList == null || breakRuleList.size() == 0) {
                    continue;
                }
                // 校验失败，记录失败信息
                String errmsg =
                        nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
                                "10140mag",
                                "010140mag0190",
                                null,
                                new String[] {
                                    material.getVersion().toString(),
                                    StringUtil.toString(breakRuleList
                                            .toArray(new String[0]), ",")
                                })
                /* @res "版本 [{0}]: {1}" */;
                List<String> list = result.get(material.getPk_source());
                if (list == null) {
                    list = new LinkedList<String>();
                    result.put(material.getPk_source(), list);
                }
                list.add(errmsg);
            }

            // 按物料的OID分组记录日志
            for (String pk_source : result.keySet()) {
                MaterialVO material =
                        sourcePk_voList_map.get(pk_source).iterator().next();
                StringBuilder errMsg = new StringBuilder();
                for (String msg : result.get(pk_source)) {
                    errMsg.append("\n").append(msg);
                }
                util.writeLogMsg(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
                        .getStrByID("10140mag", "010140mag0133", null,
                                new String[] {
                                    material.getCode(), errMsg.toString()
                                })
                /* @res "编码 [{0}]的物料与要升级的其它物料存在如下重复的记录:{1}" */
                );
                sourcePk_voList_map.remove(material.getPk_source());
            }
        }
    }

    /**
     * 过滤不能升级的数据
     * 
     * @param returnValue
     * @param voList
     * @return
     * @throws BusinessException
     */
    private void filterCanUpgradeVO(
            Map<String, List<MaterialVO>> sourcePk_voList_map, ErrorLogUtil util)
            throws BusinessException {

        IValidationService validateService =
                ValidationFrameworkUtil.createValidationService(
                        new MaterialDataUpgradeValidator(),
                        new MaterialUniqueRuleValidator(),
                        new SingleDistributedUpdateValidator());

        // 按物料对其所有版本一起进行校验
        List<String> sourcePkList = new ArrayList<String>();
        for (String pk_source : sourcePk_voList_map.keySet()) {
            List<MaterialVO> list = sourcePk_voList_map.get(pk_source);
            if (list != null && list.size() > 0) {
                try {
                    validateService.validate(list.toArray(new MaterialVO[0]));
                }
                catch (ValidationException e) {
                    MaterialVO vo = list.iterator().next();
                    // 校验失败，记录失败信息
                    StringBuilder errMsg = new StringBuilder();
                    List<ValidationFailure> failures = e.getFailures();
                    for (ValidationFailure failure : failures) {
                        errMsg.append("\n").append(failure.getMessage());
                    }
                    util.writeLogMsg(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
                            .getStrByID("10140mag", "010140mag0134", null,
                                    new String[] {
                                        vo.getCode(), errMsg.toString()
                                    })
                    /* @res "编码为[{0}]的物料：{1}" */);
                    sourcePkList.add(vo.getPk_source());
                }
            }
        }

        for (String pk_source : sourcePkList) {
            sourcePk_voList_map.remove(pk_source);
        }
    }

    private IMaterialAssignService getAssignService() {
        if (this.assignService == null) {
            this.assignService =
                    NCLocator.getInstance()
                            .lookup(IMaterialAssignService.class);
        }
        return this.assignService;
    }

    private BaseDAO getBaseDAO() {
        if (this.baseDAO == null) {
            this.baseDAO = new BaseDAO();
        }
        return this.baseDAO;
    }

    private IBillcodeManage getBillcodeManage() {
        if (this.billcodeManage == null) {
            this.billcodeManage =
                    NCLocator.getInstance().lookup(IBillcodeManage.class);
        }
        return this.billcodeManage;
    }

    private MaterialVO getLatestVO(MaterialVO vo, MaterialVO[] vos) {
        if (vo.getLatest().booleanValue()) {
            return vo;
        }
        if (!ArrayUtils.isEmpty(vos)) {
            for (MaterialVO materialVO : vos) {
                if (materialVO.getLatest().booleanValue()) {
                    return materialVO;
                }
            }
        }
        return null;
    }

    /**
     * 获得待保存的新版本数据，处理主键、编码、审计信息及版本信息等
     * 
     * @param vo
     *            新版本数据
     * @param oldVO
     *            来源版本数据
     * @throws DAOException
     */
    private MaterialVO getNewVersionData(MaterialVO vo, MaterialVO oldVO)
            throws DAOException {
        // 设置原始版本主键
        vo.setPk_source(oldVO.getPk_source());
        Integer version = this.queryMaxVersion(vo.getPk_source());
        // 设置新版本为原有最大版本+1
        vo.setVersion(version + 1);
        // 设置为最新版本
        vo.setLatest(UFBoolean.TRUE);

        // 清空主键与审计信息等字段的值
        vo.setPrimaryKey(null);
        vo.setModifier(null);
        vo.setModifiedtime(null);
        // 设置审计信息
        AuditInfoUtil.addData(vo);

        // 处理主辅计量换算子表的数据
        MaterialConvertVO[] converts = vo.getMaterialconvert();
        if (converts != null && converts.length > 0) {
            for (int i = 0; i < converts.length; i++) {
                converts[i].setPrimaryKey(null);
                converts[i].setPk_material(null);
                converts[i].setStatus(VOStatus.NEW);
            }
        }
        return vo;
    }

    private MaterialVO[] getSynCodeVOs(MaterialVO vo, MaterialVO[] oldVOs) {
        if (ArrayUtils.isEmpty(oldVOs)) {
            return null;
        }
        MaterialVO[] newVOs = new MaterialVO[oldVOs.length];
        for (int i = 0; i < oldVOs.length; i++) {
            newVOs[i] = (MaterialVO) oldVOs[i].clone();
            newVOs[i].setCode(vo.getCode());
        }
        return newVOs;
    }

    private MaterialVO[] getUpdateVOByNewCode(MaterialVO[] oldVOs, MaterialVO vo) {
        List<MaterialVO> vos = new ArrayList<MaterialVO>();
        for (MaterialVO oldVO : oldVOs) {
            if (oldVO.getPrimaryKey().equals(vo.getPrimaryKey())) {
                continue;
            }
            MaterialVO newVO = (MaterialVO) oldVO.clone();
            newVO.setCode(vo.getCode());
            vos.add(newVO);
        }
        return vos.toArray(new MaterialVO[0]);
    }

    private MaterialVO insertWithAutoCode(MaterialVO vo,
            BillCodeContext billCodeContext) throws BusinessException {
    	UFBoolean paraBoolean = SysInit.getParaBoolean("0001V110000000000FH0", "BD306");
    	
    	String paraString = SysInit.getParaString(vo.getPk_org(), "BD308") ;
    	String str = paraString == null ? "" : paraString;
        boolean sucessed = false;
        while (!sucessed) {
            try {
                // 编码规则为后编码时，取编码
                if (billCodeContext != null && !billCodeContext.isPrecode()) {
              
                	String billcode_old = vo.getCode(); //单据编码
                   if(paraBoolean.booleanValue() && 
                		   (null != billcode_old && 
                		      !billcode_old.isEmpty() )&&
                		      !billcode_old.equals("material_temp_code")){ // yezhian 20260206  bug 00000033
                	   vo.setCode(billcode_old);  //使用单据界面指定的编码
                   }else{
                	   String billcode = "";
                    	do {
                    		 billcode = this.getBillcodeManage().getBillCode_RequiresNew(
                    				IMaterialConst.BILLCODE_MATERIAL,
                    				vo.getPk_group(), pkorg, vo); //获取新编码
							
						} while ( checkCodeRepeat(billcode));
                
                        str =  str + billcode ;
                        //这里检查一下，编码是否已经被占用，验证本公司和本部的编码，如果有任何一个被占用，直接获取下一个编码
                	   vo.setCode(str);
                   }
                    	
                    }
                
                vo = super.insertVO(vo);
                sucessed = true;
                break;
            }
            catch (BusinessException e) {
                if (billCodeContext != null
                        && UniqueRuleConst.CODEBREAKUNIQUE.equals(e
                                .getErrorCodeString())) {
                    this.getBillcodeManage().AbandonBillCode_RequiresNew(
                            IMaterialConst.BILLCODE_MATERIAL, vo.getPk_group(),
                            vo.getPk_org(), vo.getCode());
                    if (!billCodeContext.isPrecode()) {
                        continue;
                    }
                }
                throw e;
            }
        }
        return vo;
    }

    /**
     * 检查物料编码是否重复
     * @param billcode
     * @return
     */
    private boolean checkCodeRepeat(String billcode) {
		// TODO Auto-generated method stub
    	boolean repeat =  false;
    	String sql = "select * from bd_material where code ='"+billcode+"'";
    	BaseDAO dao = new BaseDAO();
    	try {
			Object executeQuery = dao.executeQuery(sql,new ArrayProcessor());
			if(null == executeQuery){
				return false;
			}
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}

	private boolean isOrgData(MaterialVO vo) {
        return !vo.getPk_group().equals(vo.getPk_org())
                && !vo.getPk_org().equals(IOrgConst.GLOBEORG);
    }

    /**
     * 对原始版本主键加锁操作，即锁定同一物料的全部版本数据
     * 
     * @param vos
     * @return     * @throws BusinessException    */
     
     private void lockSourcePKs(MaterialVO... vos) 
     throws BusinessException {
        String[] sourcePks = new String[vos.length];
        for (int i = 0; i < sourcePks.length; i++) {
            sourcePks[i] = vos[i].getPk_source();
        }
        BDPKLockUtil.lockString(sourcePks);
    }

    /**
     * 根据原始版本主键查询全部版本的数据，只包含主表数据
     * 
     * @param pk
     *            原始版本主键（或数组）
     * @return
     * @throws BusinessException
     */
    @SuppressWarnings("unchecked")
    private Map<String, List<MaterialVO>> queryAllVersionData(String... pk)
            throws BusinessException {
        if (pk == null || pk.length == 0) {
            return null;
        }

        Set<String> pkSet = new HashSet<String>();
        for (int i = 0; i < pk.length; i++) {
            pkSet.add(pk[i]);
        }

        InSqlBatchCaller caller =
                new InSqlBatchCaller(pkSet.toArray(new String[0]));

        Map<String, List<MaterialVO>> sourcePk_voList_map = null;
        try {
            sourcePk_voList_map =
                    (Map<String, List<MaterialVO>>) caller
                            .execute(new IInSqlBatchCallBack() {
                                Map<String, List<MaterialVO>> map =
                                        new HashMap<String, List<MaterialVO>>();

                                @Override
                                public Object doWithInSql(String inSql)
                                        throws BusinessException, SQLException {
                                    Collection<MaterialVO> c =
                                            MaterialBaseInfoServiceImpl.this
                                                    .getBaseDAO()
                                                    .retrieveByClause(
                                                            MaterialVO.class,
                                                            MaterialVO.PK_SOURCE
                                                                    + " in "
                                                                    + inSql);
                                    for (MaterialVO vo : c) {
                                        String pk_source = vo.getPk_source();
                                        List<MaterialVO> versionList =
                                                this.map.get(pk_source);
                                        if (versionList == null) {
                                            versionList =
                                                    new ArrayList<MaterialVO>();
                                            this.map.put(pk_source, versionList);
                                        }
                                        versionList.add(vo);
                                    }
                                    return this.map;
                                }

                            });
        }
        catch (SQLException e) {
            throw new BusinessException(e.getMessage(), e);
        }
        return sourcePk_voList_map;
    }

    @SuppressWarnings("unchecked")
    private MaterialVO queryLatestMaterial(String pk_source, Integer version)
            throws DAOException {
        SQLParameter param = new SQLParameter();
        param.addParam(pk_source);
        param.addParam(version);
        Collection<MaterialVO> col =
                this.getBaseDAO().retrieveByClause(MaterialVO.class,
                        "pk_source = ? and version = ?", param);
        MaterialVO latestVO = col.iterator().next();
        return latestVO;
    }

    @SuppressWarnings("unchecked")
    private MaterialVO[] queryMaterialOldVOsWhenCodeChanged(MaterialVO vo,
            MaterialVO oldVO) throws BusinessException {
        if (StringUtils.equals(vo.getCode(), oldVO.getCode())) {
            return null;
        }
        String condition =
                MaterialVO.PK_SOURCE + " = '" + vo.getPk_source()
                        + "' and pk_material <> '" + vo.getPrimaryKey() + "'";
        Collection<MaterialVO> col =
                this.getBaseDAO().retrieveByClause(MaterialVO.class, condition);
        return col.isEmpty() ? null : col.toArray(new MaterialVO[0]);
    }

    private Integer queryMaxVersion(String pk_source) throws DAOException {
        SQLParameter param = new SQLParameter();
        // 查询该物料其它版本数据中的最新版本
        String querySql =
                "select max(" + MaterialVO.VERSION + ") from "
                        + MaterialVO.getDefaultTableName() + " where "
                        + MaterialVO.PK_SOURCE + " = ?";
        param.addParam(pk_source);
        Integer version =
                (Integer) this.getBaseDAO().executeQuery(querySql, param,
                        new ColumnProcessor());
        return version;
    }

    private MaterialVO queryOldVO(String pk_material) throws DAOException,
            BusinessException {
        // 查询指定要生成新版本的数据
        MaterialVO oldVO =
                (MaterialVO) this.getBaseDAO().retrieveByPK(
                        MaterialVO.class,
                        pk_material,
                        new String[] {
                            MaterialVO.PK_MATERIAL, MaterialVO.CODE,
                            MaterialVO.PK_SOURCE, "ts"
                        });

        // 若指定要生成新版本的数据不存在，则提示
        if (oldVO == null) {
            throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl
                    .getNCLangRes().getStrByID("10140mag", "010140mag0128")
            /* @res "指定要生成新版本的数据已被删除，请刷新界面数据!" */);
        }
        return oldVO;
    }

    private void returnBillCode(MaterialVO vo) throws BusinessException {
        // 自动编码退号
        BillCodeContext billCodeContext =
                this.getBillcodeManage().getBillCodeContext(
                        IMaterialConst.BILLCODE_MATERIAL, vo.getPk_group(),
                        vo.getPk_org());
        // 存在编码规则时，退号
        if (billCodeContext != null) {
            this.getBillcodeManage().returnBillCodeOnDelete(
                    IMaterialConst.BILLCODE_MATERIAL, vo.getPk_group(),
                    vo.getPk_org(), vo.getCode(), vo);
        }
    }

    private void setVersionField(MaterialVO vo, MaterialVO oldVO) {
        // 同步版本的相关数据
        vo.setPk_source(oldVO.getPk_source());
        vo.setLatest(oldVO.getLatest());
        vo.setVersion(oldVO.getVersion());
    }

    private MaterialVO[] synMaterialCode(MaterialVO vo, MaterialVO oldVO)
            throws BusinessException {
        MaterialVO[] vos = null;
        // 编码修改时，要同步修改同一物料的全部被版本的数据
        if (!StringUtils.equals(vo.getCode(), oldVO.getCode())) {
            List<MaterialVO> list =
                    this.queryAllVersionData(vo.getPk_source()).get(
                            vo.getPk_source());
            if (list.isEmpty()) {
                return null;
            }
            MaterialVO[] oldVOs = list.toArray(new MaterialVO[0]);
            vos = this.getUpdateVOByNewCode(oldVOs, vo);

            // 设置审计信息
            AuditInfoUtil.updateData(vos);

            // 更新前事件处理
            BDCommonEventUtil eventUtil = new BDCommonEventUtil(this.getMDId());
            eventUtil.setOldObjs(oldVOs);
            eventUtil.dispatchUpdateBeforeEvent((Object[]) vos);

            // 库操作
            this.getPersistenceUtil().updateVO(vos);

            // 更新缓存
            this.notifyVersionChangeWhenDataUpdated(vo);

            // 重新检索出新数据
            vos = this.retrieveVO(VOArrayUtil.getPrimaryKeyArray(oldVOs));

            // 更新后事件通知
            eventUtil.dispatchUpdateAfterEvent((Object[]) vos);
        }
        return vos;
    }

    private void synMaterialVersionForDelete(MaterialVO vo)
            throws BusinessException {
        if (!vo.getLatest().booleanValue()) {
            return;
        }
        Integer version = this.queryMaxVersion(vo.getPk_source());
        if (version != null) {
            this.updateMaterialLatesetFlag(vo.getPk_source(), version, true);
            MaterialVO latestVO =
                    this.queryLatestMaterial(vo.getPk_source(), version);
            this.getMaterialVersionDAO().updateMaterialVersion(latestVO);
        }
        else {
            this.getMaterialVersionDAO().deleteMaterialVersion(vo);
            this.returnBillCode(vo);
        }
    }

    private void synMaterialVersionForUpgrade(MaterialVO[] vos,
            String[] updateFields) throws BusinessException {
        // 更新最新版本表数据
        List<MaterialVO> latestVOs = new ArrayList<MaterialVO>();
        for (int i = 0; i < vos.length; i++) {
            if (vos[i].getLatest().booleanValue()) {
                latestVOs.add(vos[i]);
            }
        }
        this.getMaterialVersionDAO().updateMaterialVersion(updateFields,
                latestVOs.toArray(new MaterialVO[0]));
    }

    private void updateMaterialLatesetFlag(String pk_source, Integer version,
            boolean latest) throws BusinessException {

        String updateSql =
                "update " + MaterialVO.getDefaultTableName() + " set "
                        + MaterialVO.LATEST + " = ? where "
                        + MaterialVO.PK_SOURCE + " = ? and "
                        + MaterialVO.VERSION + " = ?";
        SQLParameter param_up = new SQLParameter();
        SQLParameter param_qry = new SQLParameter();
        if (latest) {
            param_up.addParam("Y");
        }
        else {
            param_up.addParam("N");
        }
        param_up.addParam(pk_source);
        param_up.addParam(version);
        param_qry.addParam(pk_source);
        param_qry.addParam(version);

        this.getBaseDAO().executeUpdate(updateSql, param_up);
        Collection<MaterialVO> col =
                this.getBaseDAO().retrieveByClause(
                        MaterialVO.class,
                        MaterialVO.PK_SOURCE + " = ? and " + MaterialVO.VERSION
                                + " = ?", param_qry);
        // 补发一下原最新版本修改事件
        if (!CollectionUtils.isEmpty(col)) {
            BDCommonEventUtil eventUtil = new BDCommonEventUtil(this.getMDId());
            MaterialVO[] vos = col.toArray(new MaterialVO[0]);
            // 更新后事件通知
            eventUtil.dispatchUpdateAfterEvent(vos);
            // 更新缓存
            this.notifyVersionChangeWhenDataUpdated(vos[0]);
        }
    }

    @Override
    public void deleteMaterial_RequiresNew(MaterialVO vo)
            throws BusinessException {

        this.deleteVO(vo);
    }

    private ILightScheduler getLightScheduler() {
        if (this.lightScheduler == null)
            this.lightScheduler =
                    NCLocator.getInstance().lookup(ILightScheduler.class);
        return this.lightScheduler;
    }

    private MaterialVO[] getAfterUpdatevos(SuperVO[] supervos)
            throws BusinessException {
        List<String> pkList =
                VOUtil.extractFieldValues(supervos, MaterialVO.PK_MATERIAL,
                        null);
        MaterialVO[] vos = null;
        InSqlBatchCaller caller =
                new InSqlBatchCaller(pkList.toArray(new String[0]));
        try {
            List<MaterialVO> list =
                    (List<MaterialVO>) caller
                            .execute(new IInSqlBatchCallBack() {
                                List<MaterialVO> list =
                                        new ArrayList<MaterialVO>();

                                @Override
                                public Object doWithInSql(String inSql)
                                        throws BusinessException, SQLException {
                                    Collection<MaterialVO> col =
                                            getBaseDAO().retrieveByClause(
                                                    MaterialVO.class,
                                                    MaterialVO.PK_MATERIAL
                                                            + " in " + inSql);
                                    if (col != null && col.size() > 0) {
                                        this.list.addAll(col);
                                    }
                                    return this.list;
                                }
                            });
            vos = list.toArray(new MaterialVO[0]);
        }
        catch (SQLException e) {
            Logger.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
        return vos;

    }

    private void getMaterialvos(SuperVO[] currentvos, MaterialVO[] vos,
            List<SuperVO> list) {
        if (currentvos != null && currentvos.length > 0) {
            List<String> marlist =
                    VOUtil.extractFieldValues(vos, MaterialVO.PK_MATERIAL, null);
            for (int i = 0; i < vos.length; i++)
                for (int j = 0; j < currentvos.length; j++) {
                    if (marlist.contains(currentvos[j].getPrimaryKey())) {
                        list.add(currentvos[j]);
                    }
                    else
                        list.add(vos[i]);
                }
        }
        else {
            for (int i = 0; i < vos.length; i++) {
                list.add(vos[i]);
            }
        }
    }

    private void resetMaterialVos(SuperVO[] vos, String delperson,
            UFDateTime time) {
        for (SuperVO vo : vos) {
            vo.setAttributeValue(MaterialVO.DELETESTATE, 1);
            vo.setAttributeValue(MaterialVO.DELPERSON, delperson);
            vo.setAttributeValue(MaterialVO.DELTIME, time);
        }

    }

    @Override
    public void ansyDeleteMaterial(MaterialVO[] materialvos)
            throws BusinessException {
        ValueObjWithErrLog errorlog = super.disableVO(materialvos);
        SuperVO[] currentvos = errorlog.getVos();
        String delperson = AuditInfoUtil.getCurrentUser();
        UFDateTime currenttime = AuditInfoUtil.getCurrentTime();
        String mdid = IBDMetaDataIDConst.MATERIAL;
        List<SuperVO> list = new ArrayList<SuperVO>();
        getMaterialvos(currentvos, materialvos, list);
        SuperVO[] needdeletevos = list.toArray(new SuperVO[0]);
        resetMaterialVos(needdeletevos, delperson, currenttime);
        new BaseDAO().updateVOArray(needdeletevos, new String[] {
            SupplierVO.DELETESTATE, SupplierVO.DELPERSON, SupplierVO.DELTIME
        });
        TaskConfig taskconfig =
                new TaskConfigBuilder().name("delete").immediate().assign()
                        .bind();
        taskconfig.setDef1("10140material");
        taskconfig.setDef3(new UFDateTime(new Date(TimeService.getInstance()
                .getTime())).toString());
        String taskid = taskconfig.getId();
        SuperVO[] afterVos = getAfterUpdatevos(needdeletevos);
        ReallyTread thread =
                new ReallyTread(new MaterialAnsyDelete(), afterVos, delperson,
                        taskid, mdid);
        getLightScheduler().addTask(thread, taskconfig);
    }

    @Override
    public ErrLogReturnValue checkBeforeUpdateMabasclass(MaterialVO material)
            throws BusinessException {
        MaterialVO oldVO =
                (MaterialVO) this.getBaseDAO().retrieveByPK(MaterialVO.class,
                        material.getPk_material());
        if (oldVO == null) {
            return null;
        }
        // 检查物料分类是否被修改，未修改则不处理
        if (oldVO.getPk_marbasclass().equals(material.getPk_marbasclass()))
            return null;
        ErrLogReturnValue returnvalue =
                new ErrLogReturnValue(null, UFBoolean.FALSE, 1);
        try {
            IMaterailUpdateCheckForGL checkservice =
                    NCLocator.getInstance().lookup(
                            IMaterailUpdateCheckForGL.class);
            boolean isreferenced =
                    checkservice.isReferenceMaBasClass(oldVO, material);
            if (!isreferenced) {
                return null;
            }
            return returnvalue;

        }
        catch (ComponentNotFoundException ex) {
            Logger.error(ex.getMessage(), ex.getCause());
            return null;
        }
        catch (Exception ex) {
            Logger.error(ex.getMessage(), ex.getCause());
        }
        return null;
    }
}
