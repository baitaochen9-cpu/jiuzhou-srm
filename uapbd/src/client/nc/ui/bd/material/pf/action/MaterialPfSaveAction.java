package nc.ui.bd.material.pf.action;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;

import nc.bs.framework.common.NCLocator;
import nc.itf.bd.material.assign.IMaterialPFAssignService;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ArrayProcessor;
import nc.ui.bd.material.pf.view.MaterialPfApproveEditor;
import nc.ui.uif2.UIState;
import nc.ui.uif2.actions.SaveAction;
import nc.vo.bd.material.MaterialVO;
import nc.vo.bd.material.pf.AggMaterialPfVO;
import nc.vo.bd.material.pf.MaterialPfVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;

/**
 * 物料申请单保存按钮，根据申请组织给给组织页签虚分配数据
 * 
 * @author liangqq
 * @since NC6.5
 * 
 */
@SuppressWarnings({"restriction"})
public class MaterialPfSaveAction extends SaveAction {

  private static final long serialVersionUID = 4178631479588943079L;
  private MaterialPfApproveEditor editor;

  @Override
  public void doAction(ActionEvent e) throws Exception {
    editor = (MaterialPfApproveEditor) getEditor();
    AggMaterialPfVO aggVO = (AggMaterialPfVO) editor.getValue();
    validate(aggVO);
    if (getModel().getUiState() == UIState.ADD) {
      saveAsAddMaterialPf(aggVO);
    } else if (getModel().getUiState() == UIState.EDIT) {
      saveAsEditeMaterialPf(e, aggVO);
    }
    showSuccessInfo();

  }

  /**
   * 
   * <p>
   * 说明：当新增物料申请单
   * <li></li>
   * </p>
   * 
   * @param aggVO
   * @throws Exception
   * @date 2015年3月24日 下午8:36:04
   * @since NC6.36
   */
  private void saveAsAddMaterialPf(AggMaterialPfVO aggVO) throws Exception {
    virtualAssignToTabVO(aggVO);
    doAddSave(aggVO);
  }

  /**
   * 
   * <p>
   * 说明：当修改物料申请单时，需要特殊处理修改申请组织
   * <li></li>
   * </p>
   * 
   * @param e
   * @param aggVOvirtualAssignToTabVO
   * @throws Exception
   * @date 2015年3月24日 下午8:36:20
   * @since NC6.36
   */
  private void saveAsEditeMaterialPf(ActionEvent e, AggMaterialPfVO aggVO) throws Exception {
    Object source = e.getSource();
    if (source instanceof MaterialVO) {
      // 修改页签数据保存时的回调
      MaterialVO marVO = (MaterialVO) source;
      ((MaterialPfVO) aggVO.getParentVO()).setMaterial(marVO);
    } else {
      MaterialPfVO pfvo = (MaterialPfVO) aggVO.getParentVO();
      MaterialVO vo = (MaterialVO) pfvo.getMaterial();
      boolean isOrgchange = isMaterialPfOrgChanged(vo, pfvo.getPk_org());
      if (isOrgchange) {
        virtualAssignToTabVO(aggVO);
      }
    }
    doEditSave(aggVO);
  }

  /**
   * 
   * <p>
   * 说明：判断申请组织是否发生变化，采用的方法是页签上随意的一个PK_ORG和物料当前的PK_ORG做对比
   * <li></li>
   * </p>
   * 
   * @param vo
   * @return
   * @date 2015年11月24日 下午5:08:44
   * @since NC6.5
   */
  private boolean isMaterialPfOrgChanged(MaterialVO vo, String pk_org) {
    Map<String, List<SuperVO>> map = vo.getExbeanname_tabvo_map();
    Collection<List<SuperVO>> col = map.values();
    List<SuperVO> list = new ArrayList<SuperVO>();
    if (!CollectionUtils.isEmpty(col)) {
      for (List<SuperVO> superVOs : col) {
        list.addAll(superVOs);
      }
      if (!CollectionUtils.isEmpty(list)) {
        String oldOrg = (String) list.get(0).getAttributeValue("pk_org");
        if (!pk_org.equals(oldOrg)) {
          return true;
        }
      }
    }
    return false;
  }

  /**
   * <p>
   * 说明：只分配到VO中，无数据库交互操作
   * <li></li>
   * </p>
   * 
   * @param aggVO
   * @throws BusinessException
   * @date 2014年5月5日 下午3:43:53
   * @since NC6.5
   */
  private void virtualAssignToTabVO(AggMaterialPfVO aggVO) throws BusinessException {
    MaterialVO materialVO = (MaterialVO) ((MaterialPfVO) aggVO.getParentVO()).getMaterial();
    // 申请组织
    String applyPkOrg =
        editor.getBillCardPanel().getHeadItem(MaterialPfVO.PK_ORG).getValueObject().toString();
    // 申请组织成本域
    StringBuilder sql = new StringBuilder()  ;
    sql.append("select pk_org from org_orgs where code in ( select code from org_orgs where pk_org = '");
    sql.append(applyPkOrg);
    sql.append("') and pk_ownorg = '~' and isbusinessunit = 'N'");
    Object[] obj_result;
    String applyCostPkOrg = "";
	try {
		obj_result = null;
		IUAPQueryBS query = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		obj_result = (Object[])query.executeQuery(sql.toString(),new ArrayProcessor());
		applyCostPkOrg =(String) obj_result[0];
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
    
    IMaterialPFAssignService pfAssignService =
        NCLocator.getInstance().lookup(IMaterialPFAssignService.class);
    materialVO = pfAssignService.assignToTab(materialVO, new String[] {applyPkOrg,applyCostPkOrg});
    ((MaterialPfVO) aggVO.getParentVO()).setMaterial(materialVO);
  }
}
