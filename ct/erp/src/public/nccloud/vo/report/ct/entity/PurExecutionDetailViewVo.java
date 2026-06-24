package nccloud.vo.report.ct.entity;

import nc.vo.ct.purdaily.entity.CtPuBVO;
import nc.vo.ct.purdaily.entity.CtPuVO;
import nc.vo.ic.pub.util.VOEntityUtil;
import nc.vo.pu.m21.entity.OrderHeaderVO;
import nc.vo.pu.m21.entity.OrderItemVO;
import nc.vo.pubapp.pattern.model.entity.view.AbstractDataView;
import nc.vo.pubapp.pattern.model.meta.entity.view.DataViewMeta;
import nc.vo.pubapp.pattern.model.meta.entity.view.DataViewMetaFactory;
import nc.vo.pubapp.pattern.model.meta.entity.view.IDataViewMeta;
/**
 * 采购合同执行情况查询视图vo
 * @description TODO
 * @author zhengxinm 
 * @date 2019-2-14 上午9:48:45 
 * @version ncc1.0
 */
public class PurExecutionDetailViewVo extends AbstractDataView {

	
	private static final long serialVersionUID = 1L;

	public static class PurExecutionDetailViewVoMeta extends DataViewMeta {

		public PurExecutionDetailViewVoMeta() {
			super();
			this.init();
		}

		protected void init() {

			this.add(
					CtPuVO.class,
					VOEntityUtil.getDBFieldNames(new CtPuVO().getMetaData(),
							new String[] {}).toArray(new String[0]));

			this.add(
					CtPuBVO.class,
					VOEntityUtil.getDBFieldNames(new CtPuBVO().getMetaData(),
							new String[] {}).toArray(new String[0]));

			this.add(OrderHeaderVO.class, new String[] {
					OrderHeaderVO.DBILLDATE, OrderHeaderVO.VBILLCODE });

			this.add(
					OrderItemVO.class,
					VOEntityUtil.getDBFieldNames(
							new OrderItemVO().getMetaData(), new String[] {})
							.toArray(new String[0]));

			this.addRelation(CtPuVO.class, CtPuVO.PK_CT_PU, CtPuBVO.class,
					CtPuBVO.PK_CT_PU);
			this.addRelation(CtPuBVO.class, CtPuBVO.PK_CT_PU_B,
					OrderItemVO.class, OrderItemVO.CCONTRACTROWID);
			this.addRelation(CtPuBVO.class, CtPuBVO.PK_CT_PU,
					OrderItemVO.class, OrderItemVO.CCONTRACTID);
			this.addRelation(OrderItemVO.class, OrderItemVO.PK_ORDER,
					OrderHeaderVO.class, OrderHeaderVO.PK_ORDER);
			// this.add(new DynimicAttribute(ICPubMetaNameConst.CUNITID,
			// JavaType.String, null));
		}
	}

	@Override
	public IDataViewMeta getMetaData() {
		return DataViewMetaFactory.getInstance().getDataViewMeta(
				PurExecutionDetailViewVoMeta.class);
	}

	public CtPuVO getCtPuVO() {
		CtPuVO ctPuVO = (CtPuVO) this.getVO(CtPuVO.class);
		return ctPuVO;
	}

	public CtPuBVO getCtPuBVO() {
		CtPuBVO ctPuBVO = (CtPuBVO) this.getVO(CtPuBVO.class);
		return ctPuBVO;
	}

	public OrderHeaderVO getOrderHeaderVO() {
		OrderHeaderVO orderHeaderVO = (OrderHeaderVO) this
				.getVO(OrderHeaderVO.class);
		return orderHeaderVO;
	}

	public OrderItemVO getOrderItemVO() {
		OrderItemVO orderItemVO = (OrderItemVO) this.getVO(OrderItemVO.class);
		return orderItemVO;
	}
}
