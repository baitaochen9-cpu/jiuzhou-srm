package nc.bs.pub.action;

import nc.bs.bd.baseservice.busilog.IBusiOperateConst;
import nc.bs.framework.common.NCLocator;
import nc.bs.pub.compiler.AbstractCompiler2;
import nc.bs.pub.compiler.IWorkFlowRet;
import nc.bs.pub.filesystem.FSOption;
import nc.bs.pub.filesystem.IFileSystemService;
import nc.itf.bd.material.baseinfo.IMaterialBaseInfoService;
import nc.itf.bd.material.pf.IMaterialPfService;
import nc.itf.bd.pub.IBDMetaDataIDConst;
import nc.vo.bd.material.MaterialVO;
import nc.vo.bd.material.pf.AggMaterialPfVO;
import nc.vo.bd.material.pf.MaterialPfVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.compiler.PfParameterVO;
import nc.vo.uap.pf.PFBusinessException;

/**
 * 物料申请单审批动作
 * chenxt 20141121修改 申请单对应的类别为物料主键
 * @author jiangjuna
 * @since NC6.0
 * 
 */
public class N_10WL_APPROVE extends AbstractCompiler2 {

    public N_10WL_APPROVE() {
        super();
    }

    /*
     * 备注：平台编写规则类 接口执行类
     */
    public Object runComClass(PfParameterVO vo) throws BusinessException {
        try {
            super.m_tmpVo = vo;
            // 执行批审
            IWorkFlowRet woekFlowRet = (IWorkFlowRet) procActionFlow(vo);
            IMaterialPfService pfService =
                            NCLocator.getInstance().lookup(
                                            IMaterialPfService.class);
            // 已经审批完成的单据,写入基本档案真实库
            if (woekFlowRet == null) {
                MaterialPfVO pfvo =
                                (MaterialPfVO) ((AggMaterialPfVO) getVo())
                                                .getParentVO();
                MaterialVO material = (MaterialVO) pfvo.getMaterial();

                // 设置物料基本信息的申请单主键与单据类型
                material.setPk_material_pf(pfvo.getPrimaryKey());
                try {
                    material =
                                    NCLocator.getInstance()
                                                    .lookup(IMaterialBaseInfoService.class)
                                                    .insertMaterial(material);
                    // 来源路径
                    String rootPath =
                                    "uapbd/" + IBDMetaDataIDConst.MATERIALPF
                                                    + "/"
                                                    + pfvo.getPrimaryKey();
                    // 目标路径
                    String destDirPath =
                                    "uapbd/" + IBDMetaDataIDConst.MATERIAL
                                                    + "/"
                                                    + material.getPrimaryKey();
                    IFileSystemService service =
                                    NCLocator.getInstance().lookup(
                                                    IFileSystemService.class);
                    // 复制附件
                    service.copyTo(rootPath, destDirPath,
                                    FSOption.WHEN_DEST_EXIST_OVERWRITE,false);
                }
                catch (BusinessException e) {
                    pfService.writeErrMsgToMemo_RequiresNew(
                                    pfvo.getPrimaryKey(),
                                    nc.vo.ml.NCLangRes4VoTransl
                                                    .getNCLangRes()
                                                    .getStrByID("10140mag",
                                                                    "010140mag0189")
                                                    /* @res "物料基本信息校验失败，详细信息如下：" */
                                                    + "\n" + e.getMessage());
                    throw e;
                }
                pfvo.setMemo(null);
                pfvo.setMaterial(material);
                //FIXME zhangqyh 20180831  物料申请单审批后 ，需要更新物料申请单表中的物料编码和物料名称
                if( null ==  pfvo.getMaterialcode() || !pfvo.getMaterialcode().equals(material.getCode()) || !pfvo.getMaterialname().equals(material.getName())){
                	pfvo.setMaterialcode(material.getCode());
                	pfvo.setMaterialname(material.getName());
                }
                pfvo =
                                pfService.updateMaterialPfVOByAction(
                                                IBusiOperateConst.APPROVE, pfvo);
                getVo().setParentVO(pfvo);
                //chenxtc海王 更新物料类别信息
                MaterialPFBP.updateMateralClInfo(material.getPk_material(), pfvo.getPrimaryKey());
                return getVo();
            }
            else {
                MaterialPfVO pfvo =
                                (MaterialPfVO) ((AggMaterialPfVO) woekFlowRet.m_inVo)
                                                .getParentVO();
                pfvo =
                                pfService.updateMaterialPfVOByAction(
                                                IBusiOperateConst.APPROVE, pfvo);
                ((AggMaterialPfVO) woekFlowRet.m_inVo).setParentVO(pfvo);
                return woekFlowRet;
            }
        }
        catch (Exception ex) {
            if (ex instanceof BusinessException)
                throw (BusinessException) ex;
            else
                throw new PFBusinessException(ex.getMessage(), ex);
        }
    }

    /*
     * 备注：平台编写原始脚本
     */
    public String getCodeRemark() {
        return "	// 执行批审\n			IWorkFlowRet woekFlowRet = (IWorkFlowRet) procActionFlow(vo);\n			IMaterialPfService pfService = NCLocator.getInstance().lookup(\n					IMaterialPfService.class);\n			// 已经审批完成的单据,写入基本档案真实库\n			if (woekFlowRet == null) {\n				MaterialPfVO pfvo = (MaterialPfVO) ((AggMaterialPfVO) getVo())\n						.getParentVO();\n				MaterialVO material = (MaterialVO) pfvo.getMaterial();\n				// 设置物料基本信息的申请单主键与单据类型\n				material.setPk_material_pf(pfvo.getPrimaryKey());\n				try {\n					material = NCLocator.getInstance()\n							.lookup(IMaterialBaseInfoService.class)\n							.insertMaterial(material);\n				} catch (BusinessException e) {\n					pfService.writeErrMsgToMemo_RequiresNew(\n							pfvo.getPrimaryKey(),\n							nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()\n									.getStrByID(\"10140mag\", \"010140mag0189\")\n							/* @res \"物料基本信息校验失败，详细信息如下：\" */\n							+ \"\n\" + e.getMessage());\n					throw e;\n				}\n				pfvo.setMemo(null);\n				pfvo.setMaterial(material);\n				pfvo = pfService.updateMaterialPfVO(pfvo);\n				getVo().setParentVO(pfvo);\n				return getVo();\n			} else {\n				MaterialPfVO pfvo = (MaterialPfVO) ((AggMaterialPfVO) woekFlowRet.m_inVo)\n						.getParentVO();\n				pfvo = pfService.updateMaterialPfVO(pfvo);\n				((AggMaterialPfVO) woekFlowRet.m_inVo).setParentVO(pfvo);\n				return woekFlowRet;\n			}\n"; /*-=notranslate=-*/
    }

}
