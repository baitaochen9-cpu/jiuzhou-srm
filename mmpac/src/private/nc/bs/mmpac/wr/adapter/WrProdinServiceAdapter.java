package nc.bs.mmpac.wr.adapter;



import java.util.ArrayList;
import java.util.Map;

import org.apache.catalina.tribes.util.Arrays;

import com.mysql.fabric.xmlrpc.base.Array;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.jdbc.framework.processor.ArrayListProcessor;
import nc.jdbc.framework.processor.ArrayProcessor;
import nc.medpub.util.SysInitParamUtil;
import nc.pub.jz.util.RaybowIcMassageUtil;
import nc.pubitf.ic.m46.m55a4.IProductInPubServiceFor55A4;
import nc.vo.bd.stordoc.StordocVO;
import nc.vo.ic.m46.entity.FinProdInBodyVO;
import nc.vo.ic.m46.entity.FinProdInVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

/**
 * 生产报告合格入库服务适配
 * 
 * @since 6.5
 * @version 2012-8-17 上午09:47:39
 * @author wangweiab
 */

public class WrProdinServiceAdapter {
    /**
     * 根据仓库ID数组查询属性（供应链）
     * wzy:已使用缓存
     * 
     * @param pks
     * @param fields
     * @return
     * @throws BusinessException
     */
    public static StordocVO[] queryStordocInfoByPks(String[] pks, String[] fields) {
        try {
            return NCLocator.getInstance().lookup(nc.pubitf.uapbd.IStordocPubService.class)
                    .queryStordocInfoByPks(pks, fields);
        }
        catch (BusinessException e) {
            ExceptionUtils.wrappException(e);
        }
        return null;
    }

    /**
     * 入库单
     * 
     * @return 入库单
     */
    private static IProductInPubServiceFor55A4 getIInBoundServiceFor55A4() {
        return NCLocator.getInstance().lookup(IProductInPubServiceFor55A4.class);
    }

    /**
     * 通过生产报告表体id查询是否有入库单
     * 
     * @param
     * @return
     */
    public static Map<String, Boolean> queryHasProdinByItemPks(String[] pks) {
        try {
            return WrProdinServiceAdapter.getIProductInPubServiceFor55A4().queryFor55A4(pks);
        }
        catch (BusinessException e) {
            Logger.error(e.getMessage(), e);
            ExceptionUtils.wrappException(e);
            return null;
        }
    }

    /**
     * 合格入库
     * 
     * @param paras 数据
     */
    public static void pushSaveFor55A4(FinProdInVO[] paras, boolean isFillNum) {
    	FinProdInVO[] pushSave = null;
        try {
        	
            if (isFillNum) {
                // 推式保存自由、实发态产成品入库单
                pushSave = WrProdinServiceAdapter.getIInBoundServiceFor55A4().pushSaveByFillNum(paras);
            }
            else {

                // 推式保存自由、应发态产成品入库单
            	 pushSave = WrProdinServiceAdapter.getIInBoundServiceFor55A4().pushSave(paras);

            }
        }
        catch (Exception e) {
            Logger.error(e.getMessage(), e);
            ExceptionUtils.wrappException(e);
        }
        
        if(null == pushSave || pushSave.length ==0){
        	return;
        }
       sedMassage(pushSave);
    }
	/**
	 * yezhian 产成品推单生成时，发送给指定角色发送信息 创建系统参数进行启用配置
	 * 
	 * @param insert
	 */
    private static void sedMassage(FinProdInVO[] pushSave) {
    
    		BaseDAO dao = new BaseDAO();
    		for (FinProdInVO bill : pushSave) {
    	
    			UFBoolean paraBoolean = UFBoolean.FALSE;
    			try {
    				paraBoolean = SysInitParamUtil.getParaBoolean(bill
    						.getParentVO().getPk_org(), "IC121");
    			} catch (BusinessException e) {
    				// TODO Auto-generated catch block
    				continue;
    			}
    			if (paraBoolean == UFBoolean.FALSE) {
    				continue;
    			}
    			/*
    			 * 档案对照表编码：IC121, 维度设置：当前组织/当前仓库/对照档案编码 返回角色==》用户
    			 */
    			StringBuffer sql = new StringBuffer();
    			sql.append(" select cuserid from sm_user_role where pk_role in ( ");
    			sql.append(" select pk_desvalue from gl_docmap ");
    			sql.append(" where pk_docmaptemplet in ( ");
    			sql.append(" select pk_docmaptemplet from gl_docmaptemplet  ");
    			sql.append(" where   templetcode='IC121' and pk_desorgbook = '"+bill
    					.getParentVO().getPk_org()+"') " +
    							"and pk_srcvalue = '"+bill.getParentVO().getCwarehouseid()+"') ");
    			
    			ArrayList<Object[]> list = null;
    			try {
    				list = (ArrayList<Object[]>) dao.executeQuery(sql.toString(), new ArrayListProcessor());
    			} catch (DAOException e) {
    				return;
    			}
    			if(null == list || list.size() == 0){
    				return;
    			}
    			FinProdInBodyVO[] bodys = bill.getBodys();
    			StringBuffer materialmsg= new StringBuffer();
    			for(FinProdInBodyVO body : bodys){
    				String vbatchcode = body.getVbatchcode();/*批次号*/
    				String cmaterialvid = body.getCmaterialvid();/*物料ID*/
    				String vsourcebillcode = body.getVsourcebillcode();/*来源单据号*/
    				String querymaterialsql = "select code ,name,pk_material from bd_material  where pk_material ='"+cmaterialvid+"'";
    				Object[] executeQuery ;
    				
    				try {
						 executeQuery = (Object[]) dao.executeQuery(querymaterialsql, new ArrayProcessor());
						 if(null != executeQuery && executeQuery.length  == 3){
							 materialmsg.append("\n\b");
							 materialmsg.append("来源生产报告："+vsourcebillcode+",\n\b");
							 materialmsg.append("物料编码："+executeQuery[0]+",\n\b");
							 materialmsg.append("物料名称："+executeQuery[1]+",\n\b");
							 materialmsg.append("生产批次："+vbatchcode+",\n\b");
						 }
					} catch (DAOException e) {
						continue;
					}
    			}
    			
    			for(Object[] cuserid : list){
    				String  userid = (String)cuserid[0];
    				try {
    					RaybowIcMassageUtil.sedMassage(bill.getParentVO().getBillmaker(),
    							userid, "产成品入库单通知", " 产成品入库单号："+bill.getParentVO().getVbillcode()+","+materialmsg.toString()+", 请及时处理！"
    							,bill.getParentVO().getCgeneralhid(),bill.getParentVO().getVtrantypecode() );
    				} catch (Exception e) {
    					continue;
    				}
    			}

    		}

    	}

		
	

	/**
     * 生产报告查询入库单接口
     * 
     * @return 入库单接口
     */
    private static IProductInPubServiceFor55A4 getIProductInPubServiceFor55A4() {
        return NCLocator.getInstance().lookup(IProductInPubServiceFor55A4.class);
    }

}
