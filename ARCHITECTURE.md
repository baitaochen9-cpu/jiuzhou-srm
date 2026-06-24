# 项目架构说明

## 当前技术结构

- 项目为 NC Java 模块集合，本次涉及 `emm` 模块的预防性维护 PM 与工单生成/回写逻辑。
- PM 服务端核心代码位于 `emm/src/private/nc/impl/emm/pmbase`，包含工单生成、后台任务生成、工单完成/作废回写和 PM 日期重算逻辑。
- PM 客户端编辑逻辑位于 `emm/src/client/nc/ui/emm/pmbase` 与 `emm/src/client/nc/ui/emm/pmtime`，通过卡片编辑事件联动 PM 表头字段。

## 核心模块与数据流

- PM 生成工单：`PMCreateWorOrderImpl` 校验 PM、生成工单、回写 PM 的上一工单和下一工单日期。
- 后台生成工单：`WoMakePlugin` 查询满足 `next_create_date <= 当前时间` 的 PM，并调用 `PMCreateWorOrderImpl.makeWorkOrderForPlugin` 批量生成。
- 工单完成回写：`PMPubServiceImpl.woWriteBackPM4Finish` 定位来源 PM，回写完成时间并重算下一工单目标日期、生成日期和计划结束日期。
- PM 界面编辑：`PMTimeCardEditHandler` 监听开始时间、周期、周期单位、允差和特种 PM 标识变化，并调用 `PMEventUtils` 重新计算界面日期。

## 本次实现决策

- 特种设备预防性维护通过 **交易类型(transi_type) = '4B72-Cxx-02'** 识别，独立于 `DEF3` 字段。
- 新增 `isSpecialEquipmentPM()` 统一判断方法，用于 UI 编辑（PMEventUtils）、工单回写（PMPubServiceImpl）、工单生成（PMCreateWorOrderImpl）。
- 特种设备PM逻辑：**永远按实际完成时间计算下一工单目标日期**，`next_start_date = pre_end_date + period`，跳过允差判断。
  - UI 编辑：`setSpecialPMNextStartDate` 以 `pre_end_date` 为基准（首次无完成记录时回退 `initial_date`）。
  - 工单完成回写：`setPMHeadWhenWOFinish` / `rayBowUpdateDates_WO_FINISHED` 跳过允差，直接 `actuEndDate + period`。
  - 工单生成：`rayBowUpdateDates` 使用 `pre_end_date` 而非 `pre_start_date`。
  - 维修计划回写：`rewritePMBackInfo` 在 `setTimeFreqAfterMakeWO` 后覆盖为 `pre_end_date + period`。
