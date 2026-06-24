package nccloud.web.refer.sqlbuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import nc.bs.logging.Logger;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nccloud.framework.core.reflect.Constructor;
import nccloud.framework.service.ServiceLocator;
import nccloud.framework.web.processor.IRefSqlBuilder;
import nccloud.framework.web.processor.refgrid.RefQueryInfo;
import nccloud.framework.web.ui.meta.RefMeta;
import nccloud.pubitf.platform.db.SqlParameter;
import nccloud.pubitf.platform.db.SqlParameterCollection;
import nccloud.pubitf.web.refer.INCCRefQry;
import nccloud.web.refer.AbstractRefAction;
import nccloud.web.refer.DefaultTreeRefAction;
import nccloud.web.refer.IRefConst;
import nccloud.web.refer.IRefInitializer;
import nccloud.web.refer.RefHelper;
import nccloud.web.refer.usual.UsualRefSqlBuilder;

import nccloud.commons.lang.StringUtils;

/***************************************************************************
 * <br>
 * @author Rocex Wang
 * @version 2018-4-21 16:12:41
 ***************************************************************************/
public class DefaultRefSqlBuilder extends AbstractRefSqlBuilder
{
    private List<SqlParameter> listPara = new ArrayList<>();
    
    private List<IRefSqlBuilder> listRefSqlBuilder = new ArrayList<>();
    
    protected IRefSqlBuilder refSqlBuilder;
    
    /***************************************************************************
     * @author Rocex Wang
     * @version 2018-4-21 16:14:13
     ***************************************************************************/
    public DefaultRefSqlBuilder(AbstractRefAction refAction, IRefSqlBuilder refSqlBuilder, RefQueryInfo refQueryInfo)
    {
        super();
        
        this.refAction = refAction;
        this.refSqlBuilder = refSqlBuilder;
        
        addRefSqlBuilder(refSqlBuilder);// 参照本身条件
        
        initExtraRefSqlBuilder(refQueryInfo);// 前台业务定义的额外的条件
    }
    
    /***************************************************************************
     * @param refSqlBuilder
     * @author Rocex Wang
     * @version 2018-5-30 16:04:50
     ***************************************************************************/
    protected void addRefSqlBuilder(IRefSqlBuilder refSqlBuilder)
    {
        if (refSqlBuilder != null)
        {
            listRefSqlBuilder.add(refSqlBuilder);
        }
    }
    
    /***************************************************************************
     * @param refQueryInfo
     * @param refMeta
     * @return 数据权限条件
     * @author Rocex Wang
     * @version 2018-5-16 10:22:33
     ***************************************************************************/
    protected String getDataPowerSQL(RefQueryInfo refQueryInfo, RefMeta refMeta)
    {
        String strDataPowerEnable = refAction.getQueryValue(refQueryInfo, IRefConst.KeyDataPowerEnable);
        
        boolean blDataPowerEnable = false;
        
        if (StringUtils.isBlank(strDataPowerEnable))
        {
            blDataPowerEnable = true;
        }
        else
        {
            blDataPowerEnable = UFBoolean.valueOf(strDataPowerEnable).booleanValue();
        }
        
        if (!blDataPowerEnable)
        {
            return null;
        }
        
        String strDataPowerOperationCode = refAction.getQueryValue(refQueryInfo, IRefConst.KeyDataPowerOperationCode);
        if (StringUtils.isBlank(strDataPowerOperationCode))
        {
            strDataPowerOperationCode = nc.ui.bd.ref.IRefConst.DATAPOWEROPERATION_CODE;
        }
        
        String strDataPowerSQL = null;
        
        try
        {
            strDataPowerSQL =
                ServiceLocator.find(INCCRefQry.class).getRefDataPowerSQL(refMeta.getTableName(), refAction.getDataPowerColumn(refQueryInfo, refMeta),
                    refAction.getResourceCode(), strDataPowerOperationCode, refAction.getPk_group(refQueryInfo), refAction.getPk_user(refQueryInfo));
        }
        catch (BusinessException ex)
        {
            Logger.error(ex.getMessage(), ex);
        }
        
        return strDataPowerSQL;
    }
    
    /***************************************************************************
     * @param refQueryInfo
     * @param refMeta
     * @return 档案启用过滤条件
     * @author Rocex Wang
     * @version 2018-7-18 16:34:21
     ***************************************************************************/
    protected String getEnableStateSQL(RefQueryInfo refQueryInfo, RefMeta refMeta)
    {
        Boolean blShowDisabledData = refAction.isShowDisabledData();
        String strShowDisabledData = refAction.getQueryValue(refQueryInfo, IRefConst.KeyShowDisabledData);
        
        if (blShowDisabledData == null)
        {
            return null;
        }
        
        if (StringUtils.isBlank(strShowDisabledData))
        {
            refAction.setQueryValue(refQueryInfo, IRefConst.KeyShowDisabledData, blShowDisabledData.toString());
        }
        
        String strEnableStateSQL = RefHelper.getEnableStateFilterSQL(refQueryInfo);
        
        return strEnableStateSQL;
    }
    
    /***************************************************************************
     * @param refSqlBuilder
     * @param refQueryInfo
     * @param refMeta
     * @param strSQL
     * @return 参照条件
     * @author Rocex Wang
     * @version 2018-5-12 16:39:02
     ***************************************************************************/
    protected String getExtraRefSQL(IRefSqlBuilder refSqlBuilder, RefQueryInfo refQueryInfo, RefMeta refMeta, String strSQL)
    {
        if (refSqlBuilder == null)
        {
            return strSQL;
        }
        
        String strExtraSQL = refSqlBuilder.getExtraSql(refQueryInfo, refMeta);
        
        if (StringUtils.isBlank(strExtraSQL))
        {
            return strSQL;
        }
        
        String strLowerExtraSQL = strExtraSQL.trim().toLowerCase();
        
        if (strLowerExtraSQL.startsWith("and") || strLowerExtraSQL.startsWith("or"))
        {
            strSQL = strSQL + strExtraSQL;
        }
        else
        {
            strSQL = strSQL + " and (" + strExtraSQL + ")";
        }
        
        SqlParameterCollection paramCollection = refSqlBuilder.getExtraSqlParameter(refQueryInfo, refMeta);
        SqlParameter[] paras = null;
        
        if (paramCollection != null && (paras = paramCollection.getParameters()) != null)
        {
            listPara.addAll(Arrays.asList(paras));
        }
        
        return strSQL;
    }
    
    /****************************************************************************
     * {@inheritDoc}<br>
     * (参照本身条件) and (额外的条件) and (可见性条件) and (数据权限条件)
     * @see nccloud.framework.web.processor.IRefSqlBuilder#getExtraSql(nccloud.framework.web.processor.refgrid.RefQueryInfo,
     * nccloud.framework.web.ui.meta.RefMeta)
     * @author Rocex Wang
     * @version 2018-4-21 16:12:59
     ****************************************************************************/
    @Override
    public String getExtraSql(RefQueryInfo refQueryInfo, RefMeta refMeta)
    {
        // getExtraSql()方法会被调用多次，所以清掉以免重复
        listPara.clear();
        
        String strSQL = "";
        
        // 参照本身条件、前台业务定义的额外的条件
        if (!listRefSqlBuilder.isEmpty())
        {
            for (IRefSqlBuilder refSqlBuilder : listRefSqlBuilder)
            {
                strSQL = getExtraRefSQL(refSqlBuilder, refQueryInfo, refMeta, strSQL);
            }
        }
        
        // 可见性条件
        String strVisibleSQL = getVisibleSQL(refQueryInfo, refMeta);
        if (StringUtils.isNotBlank(strVisibleSQL))
        {
            strSQL = strSQL + " and (" + strVisibleSQL + ")";
        }
        
        // 档案启用过滤条件
        String strEnableStateSQL = getEnableStateSQL(refQueryInfo, refMeta);
        if (StringUtils.isNotBlank(strEnableStateSQL))
        {
            strSQL = strSQL + " and (" + strEnableStateSQL + ")";
        }
        
        // 数据权限条件
        String strDataPowerSQL = getDataPowerSQL(refQueryInfo, refMeta);
        if (StringUtils.isNotBlank(strDataPowerSQL))
        {
            strSQL = strSQL + " and (" + strDataPowerSQL + ")";
        }
        
        return strSQL;
    }
    
    /****************************************************************************
     * {@inheritDoc}<br>
     * @see nccloud.framework.web.processor.IRefSqlBuilder#getExtraSqlParameter(nccloud.framework.web.processor.refgrid.RefQueryInfo,
     * nccloud.framework.web.ui.meta.RefMeta)
     * @author Rocex Wang
     * @version 2018-4-21 16:12:59
     ****************************************************************************/
    @Override
    public SqlParameterCollection getExtraSqlParameter(RefQueryInfo refQueryInfo, RefMeta refMeta)
    {
        SqlParameterCollection collPara = new SqlParameterCollection();
        
        collPara.setParaList(listPara);
        
        return collPara;
    }
    
    /***************************************************************************
     * @return 扩展 SQLBuilder的key
     * @author Rocex Wang
     * @version 2018-9-4 18:30:55
     ***************************************************************************/
    protected String getKeyRefActionExt()
    {
        return refAction instanceof DefaultTreeRefAction ? IRefConst.KeyTreeRefActionExt : IRefConst.KeyGridRefActionExt;
    }
    
    /****************************************************************************
     * {@inheritDoc}<br>
     * @see nccloud.framework.web.processor.IRefSqlBuilder#getOrderSql(nccloud.framework.web.processor.refgrid.RefQueryInfo,
     * nccloud.framework.web.ui.meta.RefMeta)
     * @author Rocex Wang
     * @version 2018-4-21 16:12:59
     ****************************************************************************/
    @Override
    public String getOrderSql(RefQueryInfo refQueryInfo, RefMeta refMeta)
    {
        String strOrderSQL = refSqlBuilder.getOrderSql(refQueryInfo, refMeta);
        
        if (StringUtils.isNotBlank(strOrderSQL))
        {
            return strOrderSQL;
        }
        
        if (StringUtils.isNotBlank(refMeta.getCodeField()))
        {
            return " order by " + refMeta.getCodeField();
        }
        
        return null;
    }
    
    /***************************************************************************
     * @param refQueryInfo
     * @param refMeta
     * @return 可见性条件
     * @author Rocex Wang
     * @version 2018-5-16 10:15:55
     ***************************************************************************/
    protected String getVisibleSQL(RefQueryInfo refQueryInfo, RefMeta refMeta)
    {
        String strMdClassId = refAction.getMdClassId(refQueryInfo);
        
        if (StringUtils.isBlank(strMdClassId))
        {
            return null;
        }
        
        String strPk_org = refAction.getPk_org(refQueryInfo);
        String strPk_group = refAction.getPk_group(refQueryInfo);
        
        if (StringUtils.isBlank(strPk_org))
        {
            strPk_org = strPk_group;
        }
        
        String strVisibleSQL = null;
        
        try
        {
            strVisibleSQL = ServiceLocator.find(INCCRefQry.class).getRefVisibleSQL(strPk_group, strPk_org, strMdClassId);
        }
        catch (BusinessException ex)
        {
            Logger.error(ex.getMessage(), ex);
        }
        
        return strVisibleSQL;
    }
    
    /***************************************************************************
     * @param refQueryInfo
     * @return IRefSqlBuilder 前端传来的额外的参照条件
     * @author Rocex Wang
     * @version 2018-5-5 10:23:08
     ***************************************************************************/
    protected void initExtraRefSqlBuilder(RefQueryInfo refQueryInfo)
    {
        Map<String, String> queryCondition = null;
        
        if (refQueryInfo == null || (queryCondition = refQueryInfo.getQueryCondition()) == null)
        {
            return;
        }
        
        String strExtRefSqlBuilderValue = queryCondition.get(getKeyRefActionExt());
        
        if (StringUtils.isBlank(strExtRefSqlBuilderValue))
        {
            return;
        }
        
        String[] strExtRefSqlBuilders = strExtRefSqlBuilderValue.split(",");
        
        for (String strExtRefSqlBuilder : strExtRefSqlBuilders)
        {
            try
            {
                Class<?> clazz = Constructor.load(strExtRefSqlBuilder.trim());
                
                IRefSqlBuilder refSqlBuilder2 = (IRefSqlBuilder) Constructor.construct(clazz);
                
                // 我的常用需要使用refAction中的配置
                if (refSqlBuilder2 instanceof UsualRefSqlBuilder)
                {
                    ((UsualRefSqlBuilder) refSqlBuilder2).setRefAction(refAction);
                }
                else if (refSqlBuilder2 instanceof IRefInitializer)
                {
                    ((IRefInitializer) refSqlBuilder2).initRefer(refQueryInfo);
                }
                
                addRefSqlBuilder(refSqlBuilder2);
            }
            catch (Exception ex)
            {
                Logger.error(ex.getMessage(), ex);
            }
        }
    }
}
