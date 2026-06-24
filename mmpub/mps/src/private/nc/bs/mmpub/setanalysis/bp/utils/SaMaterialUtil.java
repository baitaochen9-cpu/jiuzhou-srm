package nc.bs.mmpub.setanalysis.bp.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.itf.bd.pub.IBDMetaDataIDConst;
import nc.util.mmf.framework.base.MMValueCheck;
import nc.vo.bd.accessor.GeneralAccessor;
import nc.vo.bd.accessor.IBDData;
import nc.vo.mmpub.setanalysis.entity.SaDemandVO;
import nc.vo.mmpub.setanalysis.entity.SaMaterialVO;
import nc.vo.pub.SuperVO;
import nc.vo.pubapp.pattern.pub.MapList;

/**
 * 物料类工具(构造物料固定辅助属性cgroupkey)
 * 
 * @since 6.3
 * @version 2012-8-21 下午12:52:55
 * @author zhy
 */
public class SaMaterialUtil {

    private static String VFREE = "vfree";

    private static String BVFREE = "bvfree";

    private static String BBASEINFOFREE = "bbaseinfofree";

    /**
     * 批量生成物料分类标示
     * 
     * @param vos
     */
    public static void setMaterialGroupkey(Collection<SaDemandVO> vos, SaMaterialVO smvo) {
        // 取物料计划页签敏感设置
        for (SaDemandVO vo : vos) {
            String strValue = SaMaterialUtil.getGroupkey(vo, smvo);
            vo.setAttributeValue(SaDemandVO.CGROUPKEY, strValue);
        }
    }

    /**
     * 补全结构辅助属性并且分组
     * 
     * @param vos
     * @param smvo
     * @return
     */
    public static MapList<String, SaDemandVO> getMaterialGroupkeyMapList(Collection<SaDemandVO> vos, SaMaterialVO smvo) {
        MapList<String, SaDemandVO> dmeandMapList = new MapList<String, SaDemandVO>();
        // 取物料计划页签敏感设置
        for (SaDemandVO vo : vos) {
            String strValue = SaMaterialUtil.getGroupkey(vo, smvo);
            vo.setAttributeValue(SaDemandVO.CGROUPKEY, strValue);
            dmeandMapList.put(vo.getCgroupkey(), vo);
        }
        return dmeandMapList;
    }

    /**
     * 取得项目、供应商、生产厂商、客户与自由辅助属性是否为主键
     * modified bu malid NCdp204685603
     * 辅助属性为空不能为null，改赋为“~”
     * 
     * @param bvo
     * @param usMap
     * @return
     */
    public static String getGroupkey(SuperVO bvo, SaMaterialVO smvo) {
        if (MMValueCheck.isEmpty(smvo)) {
            return "";
        }
        StringBuilder ret = new StringBuilder();
        ret.append(bvo.getAttributeValue(SaDemandVO.CMATERIALID));
//        if (!MMValueCheck.isEmpty(smvo)) {
//            // 项目
//            if (!MMValueCheck.isEmpty(smvo.getAttributeValue(SaMaterialVO.BPROJECTID))
//                    && smvo.getAttributeValue(SaMaterialVO.BPROJECTID).equals(UFBoolean.TRUE)) {
//                ret.append(MMValueCheck.isEmpty(bvo.getAttributeValue(SaDemandVO.CPROJECTID)) ? "~" : bvo
//                        .getAttributeValue(SaDemandVO.CPROJECTID));
//            }
//            else {
//                ret.append("~");
//            }
//            // 供应商
//            if (!MMValueCheck.isEmpty(smvo.getAttributeValue(SaMaterialVO.BVENDORID))
//                    && smvo.getAttributeValue(SaMaterialVO.BVENDORID).equals(UFBoolean.TRUE)) {
//                ret.append(MMValueCheck.isEmpty(bvo.getAttributeValue(SaDemandVO.CVENDORID)) ? "~" : bvo
//                        .getAttributeValue(SaDemandVO.CVENDORID));
//            }
//            else {
//                ret.append("~");
//            }
//            // 生产厂商
//            if (!MMValueCheck.isEmpty(smvo.getAttributeValue(SaMaterialVO.BPRODUCTORID))
//                    && smvo.getAttributeValue(SaMaterialVO.BPRODUCTORID).equals(UFBoolean.TRUE)) {
//                ret.append(MMValueCheck.isEmpty(bvo.getAttributeValue(SaDemandVO.CPRODUCTORID)) ? "~" : bvo
//                        .getAttributeValue(SaDemandVO.CPRODUCTORID));
//            }
//            else {
//                ret.append("~");
//            }
//            // 客户
//            if (!MMValueCheck.isEmpty(smvo.getAttributeValue(SaMaterialVO.BCUSTOMERID))
//                    && smvo.getAttributeValue(SaMaterialVO.BCUSTOMERID).equals(UFBoolean.TRUE)) {
//
//                ret.append(MMValueCheck.isEmpty(bvo.getAttributeValue(SaDemandVO.CCUSTOMERID)) ? "~" : bvo
//                        .getAttributeValue(SaDemandVO.CCUSTOMERID));
//            }
//            else {
//                ret.append("~");
//            }
//            // 特征码
//            if (!MMValueCheck.isEmpty(smvo.getAttributeValue(SaMaterialVO.BCFFILEID))
//                    && smvo.getAttributeValue(SaMaterialVO.BCFFILEID).equals(UFBoolean.TRUE)) {
//
//                ret.append(MMValueCheck.isEmpty(bvo.getAttributeValue(SaDemandVO.CFFILEID)) ? "~" : bvo
//                        .getAttributeValue(SaDemandVO.CFFILEID));
//            }
//            else {
//                ret.append("~");
//            }
//            // 辅助属性
//            for (int i = 1; i < 11; i++) {
//                if (!MMValueCheck.isEmpty(smvo.getAttributeValue(SaMaterialUtil.BVFREE + i))
//                        && smvo.getAttributeValue(SaMaterialUtil.BVFREE + i).equals(UFBoolean.TRUE)) {
//                    ret.append(MMValueCheck.isEmpty(bvo.getAttributeValue(SaMaterialUtil.VFREE + i)) ? "~" : bvo
//                            .getAttributeValue(SaMaterialUtil.VFREE + i));
//                }
//                else {
//                    ret.append("~");
//                }
//            }
//        }
//        else {
//            ret.append("~~~~~~~~~~~~~~");
//        }
        // 加密
        String strMd5 = SaStructVFreeUtil.getMD5String(ret.toString());
        return strMd5;
    }

    /**
     * 取得项目、供应商、生产厂商、客户与自由辅助属性是否为主键
     * 重写方法,为存量使用
     * 
     * @param SaDemandVO sdvo
     * @return
     */
    public static String getGroupkeyForOnhand(SaDemandVO sdvo, SaMaterialVO smvo) {
        StringBuilder ret = new StringBuilder();
        ret.append(sdvo.getAttributeValue(SaDemandVO.CMATERIALID));
//        if (!MMValueCheck.isEmpty(sdvo)) {
//
//            // 项目
//            if (!MMValueCheck.isEmpty(smvo.getAttributeValue(SaMaterialVO.BBASEINFOPROJECTID))
//                    && smvo.getAttributeValue(SaMaterialVO.BBASEINFOPROJECTID).equals(UFBoolean.TRUE)) {
//                ret.append(MMValueCheck.isEmpty(sdvo.getAttributeValue(SaDemandVO.CPROJECTID)) ? "~" : sdvo
//                        .getAttributeValue(SaDemandVO.CPROJECTID));
//            }
//            else {
//                ret.append("~");
//            }
//            // 供应商
//            if (!MMValueCheck.isEmpty(smvo.getAttributeValue(SaMaterialVO.BBASEINFOVENDORID))
//                    && smvo.getAttributeValue(SaMaterialVO.BBASEINFOVENDORID).equals(UFBoolean.TRUE)) {
//                ret.append(MMValueCheck.isEmpty(sdvo.getAttributeValue(SaDemandVO.CVENDORID)) ? "~" : sdvo
//                        .getAttributeValue(SaDemandVO.CVENDORID));
//            }
//            else {
//                ret.append("~");
//            }
//            // 生产厂商
//            if (!MMValueCheck.isEmpty(smvo.getAttributeValue(SaMaterialVO.BBASEINFOPRODUCTORID))
//                    && smvo.getAttributeValue(SaMaterialVO.BBASEINFOPRODUCTORID).equals(UFBoolean.TRUE)) {
//                ret.append(MMValueCheck.isEmpty(sdvo.getAttributeValue(SaDemandVO.CPRODUCTORID)) ? "~" : sdvo
//                        .getAttributeValue(SaDemandVO.CPRODUCTORID));
//            }
//            else {
//                ret.append("~");
//            }
//            // 客户
//            if (!MMValueCheck.isEmpty(smvo.getAttributeValue(SaMaterialVO.BBASEINFOCUSTOMERID))
//                    && smvo.getAttributeValue(SaMaterialVO.BBASEINFOCUSTOMERID).equals(UFBoolean.TRUE)) {
//
//                ret.append(MMValueCheck.isEmpty(sdvo.getAttributeValue(SaDemandVO.CCUSTOMERID)) ? "~" : sdvo
//                        .getAttributeValue(SaDemandVO.CCUSTOMERID));
//            }
//            else {
//                ret.append("~");
//            }
//            // 特征码
//            if (!MMValueCheck.isEmpty(smvo.getAttributeValue(SaMaterialVO.BBASEINFOCFFILEID))
//                    && smvo.getAttributeValue(SaMaterialVO.BBASEINFOCFFILEID).equals(UFBoolean.TRUE)) {
//
//                ret.append(MMValueCheck.isEmpty(sdvo.getAttributeValue(SaDemandVO.CFFILEID)) ? "~" : sdvo
//                        .getAttributeValue(SaDemandVO.CFFILEID));
//            }
//            else {
//                ret.append("~");
//            }
//            // 辅助属性
//            for (int i = 1; i < 11; i++) {
//                if (!MMValueCheck.isEmpty(smvo.getAttributeValue(SaMaterialUtil.BBASEINFOFREE + i))
//                        && smvo.getAttributeValue(SaMaterialUtil.BBASEINFOFREE + i).equals(UFBoolean.TRUE)) {
//                    ret.append(MMValueCheck.isEmpty(sdvo.getAttributeValue(SaMaterialUtil.VFREE + i)) ? "~" : sdvo
//                            .getAttributeValue(SaMaterialUtil.VFREE + i));
//                }
//                else {
//                    ret.append("~");
//                }
//            }
//        }
//        else {
//            ret.append("~~~~~~~~~~~~~~");
//        }
        // 加密
        String strMd5 = SaStructVFreeUtil.getMD5String(ret.toString());
        return strMd5;
    }

    /**
     * 需求是否启用辅助属性.
     * 
     * @param sdvo
     * @return
     */
    public static boolean isVfreeEnable(SaDemandVO sdvo) {
        boolean isEnable = false;
        if (MMValueCheck.isEmpty(sdvo)) {
            return isEnable;
        }
        // 项目
        else if (!MMValueCheck.isEmpty(sdvo.getAttributeValue(SaDemandVO.CPROJECTID))) {
            isEnable = true;
        }
        // 供应商
        else if (!MMValueCheck.isEmpty(sdvo.getAttributeValue(SaDemandVO.CVENDORID))) {
            isEnable = true;
        }
        // 生产厂商
        else if (!MMValueCheck.isEmpty(sdvo.getAttributeValue(SaDemandVO.CPRODUCTORID))) {
            isEnable = true;
        }
        // 客户
        else if (!MMValueCheck.isEmpty(sdvo.getAttributeValue(SaDemandVO.CCUSTOMERID))) {
            isEnable = true;
        }
        // 特征码
        else if (!MMValueCheck.isEmpty(sdvo.getAttributeValue(SaDemandVO.CFFILEID))) {
            isEnable = true;
        }
        else {
            // 辅助属性
            for (int i = 1; i < 11; i++) {
                if (!MMValueCheck.isEmpty(sdvo.getAttributeValue(SaMaterialUtil.VFREE + i))) {
                    isEnable = true;
                    break;
                }
            }
        }
        return isEnable;
    }

    /**
     * 按物料OID分组
     * 
     * @param vos
     * @param name
     * @return
     */
    public static Map<String, SaMaterialVO> getMap(SuperVO[] vos, String name) {
        Map<String, SaMaterialVO> sortMap = new HashMap<String, SaMaterialVO>();
        for (SuperVO vo : vos) {
            sortMap.put(vo.getAttributeValue(name).toString(), (SaMaterialVO) vo);
        }
        return sortMap;
    }

    /**
     * 物料主键转换成物料名称
     * 
     * @param materials
     * @return
     */
    public static String getMetaDataNames(List<String> materials, String metaData) {
        if (MMValueCheck.isEmpty(materials)) {
            return null;
        }
        // 获取物料名称
        GeneralAccessor accessor = new GeneralAccessor(metaData);
        List<String> retList = new ArrayList<String>();
        for (String material : materials) {
            IBDData materialdata = accessor.getDocByPk(material);
            retList.add(materialdata.getName().toString());
        }
        return SaMaterialUtil.getString(retList.toArray(new String[0]));
    }

    public static Map<String, String> getMetaDataNames(List<SaMaterialVO> aMaterialList) {
        if (MMValueCheck.isEmpty(aMaterialList)) {
            return null;
        }
        // 获取物料名称
        GeneralAccessor accessor = new GeneralAccessor(IBDMetaDataIDConst.MATERIAL);
        Map<String, String> retMap = new HashMap<String, String>();
        for (SaMaterialVO material : aMaterialList) {
            IBDData materialdata = accessor.getDocByPk(material.getCmaterialid());
            retMap.put(material.getCmaterialid(), materialdata.getName().toString());
        }
        return retMap;
    }

    /**
     * @return
     */
    public static String getMaterialNames(String material) {
        GeneralAccessor accessor = new GeneralAccessor(IBDMetaDataIDConst.MATERIAL);
        IBDData materialdata = accessor.getDocByPk(material);
        return materialdata.getName().toString();
    }

    /**
     * 集合转换成字符串
     * 
     * @param strings
     * @return
     */
    public static String getString(String[] strings) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < strings.length; i++) {
            if (i == strings.length - 1) {
                sb.append(strings[i]);
            }
            else {
                sb.append(strings[i]).append(",");
            }
        }
        return sb.toString();
    }

}
