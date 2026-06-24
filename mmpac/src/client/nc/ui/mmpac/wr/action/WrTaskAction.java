package nc.ui.mmpac.wr.action;

import java.awt.event.ActionEvent;

import nc.bs.dao.BaseDAO;
import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.ui.mmf.framework.action.ActionInitializer;
import nc.ui.mmpac.wr.util.WrUIHelper;
import nc.ui.pubapp.uif2app.AppUiState;
import nc.util.mmf.framework.base.MMValueCheck;
import nc.vo.bd.bom.bom0202.enumeration.OutputTypeEnum;
import nc.vo.mmpac.apply.task.entity.AggTaskAVO;
import nc.vo.mmpac.wr.consts.WrBtnConst;
import nc.vo.mmpac.wr.consts.WrptLangConst;
import nc.vo.mmpac.wr.entity.AggWrVO;
import nc.vo.mmpac.wr.entity.WrItemVO;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

/** 作业量按钮
 * 
 * @since 6.0
 * @version 2013-6-16 下午10:03:24
 * @author liweiz */
public abstract class WrTaskAction extends WrBaseAction {

    private static final long serialVersionUID = -6419051295085536422L;

    /** 默认构造函数 */
    public WrTaskAction() {
        ActionInitializer.initializeAction(this, WrBtnConst.getBTN_CODE_WR_TASK(), WrBtnConst.getBTN_NAME_WR_TASK(),
                WrBtnConst.getBTN_TOOLTIP_WR_TASK());
    }

    @Override
    public void doAction(ActionEvent e) throws Exception {
        super.doAction(e);
        // 对选择的数据进行补全 考虑懒加载
        AggWrVO[] fullAggWrVOs = this.selectedAggWrVO();
        
        this.validate(fullAggWrVOs);
        //校验订单是否关闭 作业量连接数问题，在后台校验
        //        WrProcessMOUtil moUtil = new WrProcessMOUtil();
        //        moUtil.moCloseProcess(fullAggWrVOs);
        if (MMValueCheck.isEmpty(fullAggWrVOs)) {
            return;
        }
        //在后台进行统一VO交互及数据业务处理
        AggTaskAVO[] aggs = this.wrAggsChangeToTaskVO(fullAggWrVOs);
        // 打开作业申报节点
        this.openFuncNode(aggs);
    }

    protected AggWrVO[] selectedAggWrVO() {
        // 从界面获取选中数据
        AggWrVO[] aggWrVOs = WrUIHelper.getSelectedAggWrVOs(this.getModel(), this.getListView(), this.getBillForm());
        return aggWrVOs;
    }

    @Override
    protected boolean isActionEnable() {
        if (AppUiState.NOT_EDIT != this.getModel().getAppUiState()) {
            return false;
        }
        Object[] selDatas = this.getModel().getSelectedOperaDatas();
        if (null == selDatas || selDatas.length == 0) {
            return false;
        }
        if (this.getBillForm().isShowing()) {
            if (this.getBillForm().getBillCardPanel().getBillTable(AggWrVO.TBCODE_PICK).isShowing()) {
                return false;
            }
        }
        return true;

    }

    /** 返工前台校验类 */
    protected void validate(AggWrVO[] aggWrVOs) {
        if (MMValueCheck.isEmpty(aggWrVOs)) {
            return;
        }
        
        String pk_org=aggWrVOs[0].getParentVO().getPk_org();
//        是否允许多次维护作业量
        try {
	        String kz = querySysinitName("MMPAVC01",pk_org);
	        for(int i=0;i<aggWrVOs.length;i++){
	        	String vdef2=aggWrVOs[i].getParentVO().getVdef2();
	        	if(!"Y".equals(kz)){
	        		if("Y".equals(vdef2)){
	        			 ExceptionUtils.wrappBusinessException("已维护作业量不能重复维护");
	        		}
	        	}
	        }
        } catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        if (aggWrVOs.length == 1) {
            WrItemVO[] wrItemVOs = (WrItemVO[]) aggWrVOs[0].getChildren(WrItemVO.class);
            if (null != wrItemVOs && wrItemVOs.length == 1) {
                if (null != wrItemVOs[0]) {
                    //联副产品不支持作业量操作 V63文档中有描述
                    if (!OutputTypeEnum.MAIN_PRODUCT.equalsValue(wrItemVOs[0].getFbproducttype())) {
                        ExceptionUtils.wrappBusinessException(WrptLangConst.ERR_MAINPRODUCT_REWORK());
                    }
                }
            }
        }
    }

    //查询参数代码
	private String querySysinitName(String initcode,String pk_org) throws BusinessException {
		  String sql = "select value from pub_sysinit  where initcode='"+initcode+"' and pk_org='"+pk_org+"' and nvl(dr,0)=0";
		  Object obj = NCLocator.getInstance().lookup(IUAPQueryBS.class).executeQuery(sql, new ColumnProcessor());
		  return obj == null ? "" : obj.toString();
		 }
    protected abstract void openFuncNode(Object data);

    protected abstract AggTaskAVO[] wrAggsChangeToTaskVO(AggWrVO[] srcs);
}
