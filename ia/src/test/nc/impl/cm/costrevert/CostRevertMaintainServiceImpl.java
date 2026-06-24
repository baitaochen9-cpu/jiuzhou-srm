package nc.impl.cm.costrevert;

import java.util.ArrayList;
import java.util.List;

import nc.bs.cm.costrevert.CtRevertUtil;
import nc.bs.cm.costrevert.maintain.CostRevertDBMaintian;
import nc.bs.cm.costrevert.revert.batchrevert.AbstractCostRevert;
import nc.bs.cm.costrevert.revert.batchrevert.ActualCostRevert;
import nc.bs.cm.costrevert.revert.batchrevert.ActualCostRevertWithStd;
import nc.cmpub.framework.lock.CMBusinessLockTools;
import nc.itf.cm.costrevert.ICostRevertMaintainService;
import nc.vo.cm.costrevert.CtRevertParamVO;
import nc.vo.cm.costrevert.CtRevertStatusEnum;
import nc.vo.pub.BusinessException;

/**
 * 成本 还原实现类
 *
 * @author sz
 */
public class CostRevertMaintainServiceImpl implements ICostRevertMaintainService {

    @Override
    public String revertCost(CtRevertParamVO paramVO) throws BusinessException {
        // 加锁
        new CMBusinessLockTools().addDynamicLock(paramVO.getPk_org(), paramVO.getCperiod());
        AbstractCostRevert costRevert = null;
        if (paramVO.isActualRevert()) {
            costRevert = new ActualCostRevert(paramVO);
            costRevert.doRevertCost();// 实际成本还原
        }
        else {
            costRevert = new ActualCostRevertWithStd(paramVO);
            costRevert.doRevertCost();// 按标准成本还原实际成本
        }
        // ExceptionUtils.wrappBusinessException(CMMLangConstCostRevert.getERR_REVERT_METHOD());
        return null;
        // return new ActualCostRevertWithStd(paramVO).revertCost();// 按标准成本还原实际成本
    }

    @Override
    public void delCtRevertData(CtRevertParamVO paramVO) throws BusinessException {
        List<String> batchProdCostClassIDs = new ArrayList<String>();
        batchProdCostClassIDs.addAll(paramVO.getBatchProdCostClassIDs());
        // 执行到这里需要 删除当前参数中已经还原数据
        CostRevertDBMaintian.deleteRevertDatas(paramVO, batchProdCostClassIDs);
        // 删除期初
        CtRevertUtil.delRevertBgAggVOs(paramVO, batchProdCostClassIDs);
        // 删除形态转换数据，dingyma
        CostRevertDBMaintian.deleteTransformRevertDatas(paramVO, batchProdCostClassIDs);
        // 删除已有的还原比率，dingyma
        CostRevertDBMaintian.deleteRevertRatioDatas(paramVO, batchProdCostClassIDs);
        // 更新还原层次状态到“未还原”
        CostRevertDBMaintian.updateCtRvertLevelStatus(paramVO.getBatchProdCostClassIDs().toArray(new String[0]),
                paramVO.getCperiod(), CtRevertStatusEnum.NO_REVERT.toIntValue());
    }
}
