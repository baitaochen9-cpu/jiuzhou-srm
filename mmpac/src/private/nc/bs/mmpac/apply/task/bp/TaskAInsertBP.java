package nc.bs.mmpac.apply.task.bp;

import nc.bs.mmpac.apply.task.plugin.TaskAPluginPoint;
import nc.bs.mmpac.apply.task.rule.CheckUpdateWGBGRule;
import nc.bs.mmpac.apply.task.rule.TaskACheckBillStatusRule;
import nc.bs.mmpac.apply.task.rule.TaskACheckNullRule;
import nc.bs.mmpac.apply.task.rule.TaskAMakeDateRule;
import nc.bs.mmpac.apply.task.rule.TaskARowNoCheckRule;
import nc.bs.pubapp.pub.rule.BillCodeCheckRule;
import nc.impl.pubapp.bd.material.assistant.MarAssistantSaveRule;
import nc.impl.pubapp.pattern.data.bill.template.InsertBPTemplate;
import nc.impl.pubapp.pattern.data.bill.tool.BillTransferTool;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.AroundProcesser;
import nc.itf.org.IOrgConst;
import nc.util.mmf.framework.base.MMValueCheck;
import nc.vo.mmpac.apply.task.consts.TaskABillConsts;
import nc.vo.mmpac.apply.task.entity.AggTaskAVO;
import nc.vo.mmpac.apply.task.entity.TaskABVO;
import nc.vo.mmpac.apply.task.entity.TaskAHVO;

/**
 * 作业申报新增 BP
 */
public class TaskAInsertBP {

    public AggTaskAVO[] insert(AggTaskAVO[] vos) {

        if (MMValueCheck.isEmpty(vos)) {
            return null;
        }

        // 数据库中数据和前台传递过来的差异VO合并后的结果
        BillTransferTool<AggTaskAVO> transferTool = new BillTransferTool<AggTaskAVO>(vos);

        // 获取前台的完整单据实体
        AggTaskAVO[] mergedVOs = transferTool.getClientFullInfoBill();

        // 创建新增数据模板BP
        InsertBPTemplate<AggTaskAVO> bp = new InsertBPTemplate<AggTaskAVO>(TaskAPluginPoint.INSERT);

        // 注入新增前规则
        this.addBeforeRule(bp.getAroundProcesser());

        // 注入新增后规则
        this.addAfterRule(bp.getAroundProcesser());

        // 执行新增操作
        AggTaskAVO[] retVOs = bp.insert(mergedVOs);

        // 构造返回数据
        return retVOs;
    }

    /**
     * 新增前规则
     * 
     * @param processor
     */

    @SuppressWarnings("unchecked")
    private void addBeforeRule(AroundProcesser<AggTaskAVO> processer) {
        IRule<AggTaskAVO> rule = null;
        // 验证单据状态是否为关闭态
        rule = new TaskACheckBillStatusRule();
        processer.addBeforeFinalRule(rule);
        // 组织是否启动规则
        rule = new nc.bs.pubapp.pub.rule.OrgDisabledCheckRule(TaskAHVO.PK_ORG, IOrgConst.FACTORYTYPE);
        processer.addBeforeRule(rule);
        // 补充默认值规则
        rule = new nc.bs.pubapp.pub.rule.FillInsertDataRule();
        processer.addBeforeRule(rule);
        rule = new TaskAMakeDateRule();
        processer.addBeforeRule(rule);
        // 创建单据号规则
        rule =
                new nc.bs.pubapp.pub.rule.CreateBillCodeRule(TaskABillConsts.TASKA_BILLTYPE, TaskAHVO.VBILLCODE,
                        TaskAHVO.PK_GROUP, TaskAHVO.PK_ORG);
        processer.addBeforeRule(rule);
        // 自由辅助属性检查的规则
        rule = new MarAssistantSaveRule<AggTaskAVO>();
        processer.addBeforeRule(rule);
        // 物料分配到主组织检查规则
        rule = new nc.bs.mmpac.apply.task.rule.TaskACheckMaterialPermissionRule();
        processer.addBeforeRule(rule);
        // 判空校验
        rule = new TaskACheckNullRule();
        processer.addBeforeRule(rule);

        rule = new TaskARowNoCheckRule();
        processer.addBeforeRule(rule);
        // 单据字段长度检查规则
        rule = new nc.bs.pubapp.pub.rule.FieldLengthCheckRule();
        processer.addBeforeRule(rule);
        // 自定义项检查rule
        rule = new nc.impl.pubapp.bd.userdef.UserDefSaveRule<AggTaskAVO>(new Class[] {
            TaskAHVO.class, TaskABVO.class
        });
        processer.addBeforeRule(rule);
        
        
        rule = new CheckUpdateWGBGRule();
        processer.addBeforeRule(rule);
    }

    /**
     * 新增后规则
     * 
     * @param processor
     */
    @SuppressWarnings("unchecked")
    private void addAfterRule(AroundProcesser<AggTaskAVO> processor) {
        // 保存单据号唯一性校验规则
        IRule<AggTaskAVO> rule =
                new BillCodeCheckRule(TaskABillConsts.TASKA_BILLTYPE, TaskAHVO.VBILLCODE, TaskAHVO.PK_GROUP,
                        TaskAHVO.PK_ORG);
//        IRule<AggTaskAVO> rule2 =
//                new WrSetBackNumRule2();
//        processor.addAfterRule(rule2);
        processor.addAfterRule(rule);
    }

}
