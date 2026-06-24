package nc.ui.jzqc.labelprintapply.ace.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import nc.bs.framework.common.NCLocator;
import nc.funcnode.ui.FuncletInitData;
import nc.itf.jzqc.ILabelprintapplyMaintain;
import nc.itf.uap.queryscheme.IQuerySchemeQueryService4User;
import nc.ui.pubapp.uif2app.model.DefaultFuncNodeInitDataListener;
import nc.vo.jzqc.labelprintapply.AggLabelprintapplyHVO;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.querytemplate.queryscheme.QuerySchemeVO;
import nc.vo.querytemplate.queryscheme.SimpleQuerySchemeVO;

public class LabelPrintInitDataListener extends DefaultFuncNodeInitDataListener {

	public void initData(FuncletInitData data) {
		if (data == null) {
			try {
				ILabelprintapplyMaintain operator = NCLocator.getInstance().lookup(
						ILabelprintapplyMaintain.class);
				String where = " and  nvl(dr,0)= 0 and pk_org ='"+getContext().getPk_org()+"' and approvestatus <> 1";
				AggLabelprintapplyHVO[] vos = operator.pubquerybills(where);
				getModel().initModel(vos);
			} catch (BusinessException e) {
				ExceptionUtils.wrappBusinessException(e.getMessage());
			}
		} else {
			Object obj = data.getInitData();
			if (data != null && data.getInitData() != null
					&& (data.getInitData() instanceof AggLabelprintapplyHVO[])) {
				// initDataByInitData(data);
				getModel().initModel((AggLabelprintapplyHVO[]) obj);
			} else {
				super.initData(data);
			}
		}
	}

	private SimpleQuerySchemeVO[] getSimpleQuerySchemeVOsBy(String pk_org,
			String funcode, String userid) throws BusinessException {
		QuerySchemeVO[] qsvos = getQuerySchemeQueryService4User()
				.getQuerySchemeVOsBy(pk_org, funcode, "0001AA10000000013FT0",
						userid);
		if (qsvos == null || qsvos.length == 0)
			return null;
		Arrays.sort(qsvos, QuerySchemeVO.bySequence());
		List<SimpleQuerySchemeVO> vos = new ArrayList<SimpleQuerySchemeVO>();
		for (QuerySchemeVO qsvo : qsvos) {
			if (qsvo.isQuickQueryScheme()) {
				continue;
			}
			vos.add(new SimpleQuerySchemeVO(qsvo));
		}
		return vos.toArray(new SimpleQuerySchemeVO[0]);
	}

	private IQuerySchemeQueryService4User getQuerySchemeQueryService4User() {
		return NCLocator.getInstance().lookup(
				IQuerySchemeQueryService4User.class);
	}

}