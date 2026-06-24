package nc.pub.jz.util;

import java.util.ArrayList;
import java.util.List;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.jdbc.framework.processor.ArrayListProcessor;
import nc.jdbc.framework.processor.ArrayProcessor;
import nc.medpub.util.SysInitParamUtil;
import nc.vo.ic.m46.entity.FinProdInVO;
import nc.vo.pf.change.ChangeVOAdjustContext;
import nc.vo.pf.change.IChangeVOAdjust;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;

public class M55A4T6OChangeVOAdjust implements IChangeVOAdjust{

	@Override
	public AggregatedValueObject adjustAfterChange(AggregatedValueObject arg0,
			AggregatedValueObject arg1, ChangeVOAdjustContext arg2)
			throws BusinessException {
		// TODO Auto-generated method stub
		System.out.println("123");
		return null;
	}

	@Override
	public AggregatedValueObject adjustBeforeChange(AggregatedValueObject arg0,
			ChangeVOAdjustContext arg1) throws BusinessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AggregatedValueObject[] batchAdjustAfterChange(
			AggregatedValueObject[] arg0, AggregatedValueObject[] arg1,
			ChangeVOAdjustContext arg2) throws BusinessException {
		// TODO Auto-generated method stub
		/* 20210103 yezhian 加入消息发送 */
		/*
		 * 信息发送加入到本类处理 增加信息可配置，配置对象为角色 多仓库物料不同，接收角色也不一样，需要维护接收关系
		 */
		this.sedMassage(arg1);
		return arg1;
	}
	
	/**
	 * yezhian 产成品推单生成时，发送给指定角色发送信息 创建系统参数进行启用配置
	 * 
	 * @param insert
	 */
	private void sedMassage(AggregatedValueObject[] insert) {
		if (null == insert || insert.length == 0) {
			return;
		}
		BaseDAO dao = new BaseDAO();
		for (AggregatedValueObject obj : insert) {
			
			FinProdInVO bill = (FinProdInVO) obj;
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
			
			Object[] list = null;
			try {
				 list = (Object[]) dao.executeQuery(sql.toString(), new ArrayProcessor());
			} catch (DAOException e) {
				return;
			}
			if(null == list || list.length == 0){
				return;
			}
			
			for(Object cuserid : list){
				String userid = (String) cuserid;
				try {
					RaybowIcMassageUtil.sedMassage(bill.getParentVO().getBillmaker(),
							userid, "产成品入库单通知", "/n 产成品入库单号："+bill.getParentVO().getVbillcode()+"/n 请及时处理！");
				} catch (Exception e) {
					continue;
				}
			}

		}

	}

	@Override
	public AggregatedValueObject[] batchAdjustBeforeChange(
			AggregatedValueObject[] arg0, ChangeVOAdjustContext arg1)
			throws BusinessException {
		// TODO Auto-generated method stub
		return null;
	}

}
