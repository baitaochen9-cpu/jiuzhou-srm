# 修改记录

## 2026-06-02

## 2026-(current) 特种设备预防性维护（交易类型4B72-Cxx-02）

- 新增交易类型 `4B72-Cxx-02` 作为特种设备预防性维护识别依据
- **核心逻辑**：永远按实际完成时间（pre_end_date）+ 周期计算下一工单目标日期，跳过允差判断
- 新增统一方法 `isSpecialEquipmentPM()`，判断 transi_type
- 修改文件：
  - `PMEventUtils.java`：`setNextStartDate` 增加特种PM分支，`setNextCreateDate` 特种PM时 DEF2=nextStartDate
  - `ChgNextDateAction.java`：`sendDate` 中特种PM时 DEF2=adjust_date（不加允差）
  - `PMPubServiceImpl.java`：修改 `setPMHeadWhenWOFinish`、`rayBowUpdateDates_WO_FINISHED`、`rayBowUpdateDates`、`rewritePMBackInfo`
  - `PMCreateWorOrderImpl.java`：`setPMBillInfoAfterMakeWO` 保存 pre_end_date 并在特种PM时恢复
- 后续风险：`4B72-Cxx-02` 交易类型需在 NC 交易类型管理节点配置后方可使用
