package nc.bs.pub.action.rule;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.apache.poi.util.ArrayUtil;

import nc.bs.dao.BaseDAO;
import nc.jdbc.framework.processor.ArrayListProcessor;
import nc.jdbc.framework.processor.ArrayProcessor;
import nc.jdbc.framework.processor.BaseProcessor;
import nc.vo.pub.BusinessException;
import nc.vo.mmpac.wr.entity.AggWrVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.itf.fi.pub.SysInit;
import nc.uap.ws.i18n.Message;

public class BeforeApproveProduceRule implements IRule<AggWrVO> {

	@Override
	public void process(AggWrVO[] paramArrayOfE) {
		try {
			for (AggWrVO clientVO : paramArrayOfE) {
				if(("0001V110000000012E56").equals(clientVO.getParent().getAttributeValue("pk_org"))){
					//获取控制参数
					UFBoolean paraBoolean = SysInit.getParaBoolean(clientVO.getParentVO().getPk_org(), "MMPAC05");
					//该逻辑仅校验了产生材料出库单的单据审批态，对未产生材料出库单的单据状态未做校验
					if(paraBoolean.booleanValue()){
						BaseDAO dao = new BaseDAO();
						StringBuffer sql = new StringBuffer();
						List<String> hs_execute = new ArrayList<String>();
						
						sql.append("select vbillcode ");
						sql.append("from ic_material_h ");
						sql.append("left join ic_material_b on ic_material_b.cgeneralhid = ic_material_h.cgeneralhid ");
						sql.append("where ic_material_h.fbillflag =2 ");
						sql.append("and ic_material_h.dr = 0 ");
						sql.append("and ic_material_b.cfirstbillbid = '");
						CircularlyAccessibleValueObject[] mm_wr_chiVO = clientVO.getAllChildrenVO();
						for (CircularlyAccessibleValueObject chiVO : mm_wr_chiVO) {							
							String chi_vo_cfirstbid = chiVO.getAttributeValue("cbfirstmobid").toString();
							StringBuffer sql_body = new StringBuffer();
							sql_body.append(sql);
							sql_body.append(chi_vo_cfirstbid + "'");
							
							hs_execute.add((String) dao.executeQuery(sql_body.toString(), new BaseProcessor(){
								@Override
								public Object processResultSet(ResultSet paramResultSet)
										throws SQLException {
									while(paramResultSet.next()){
										return paramResultSet.getString("vbillcode");
									}
									return null;
								}
								}));

							if (hs_execute.size() > 0 ) {
								for(Object s : hs_execute.toArray()){
									if(null != s){
										ExceptionUtils.wrappBusinessException(s + "未做材料出库签字，请先签字！");
									}
								}
//								ExceptionUtils.wrappBusinessException(Arrays.toString(hs_execute.toArray()) + "未做材料出库签字，请先签字！");						
							}
						}
					}
				}
			}
		} catch (BusinessException e) {
			/* 66 */ExceptionUtils.wrappException(e);
			/*    */}
	}
	

}
