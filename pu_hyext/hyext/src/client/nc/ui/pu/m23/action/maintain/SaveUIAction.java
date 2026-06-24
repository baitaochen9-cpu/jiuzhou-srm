/** <a href="http://www.cpupk.com/decompiler">Eclipse Class Decompiler</a> plugin, Copyright (c) 2017 Chen Chao. */
package nc.ui.pu.m23.action.maintain;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import nc.bs.framework.common.NCLocator;
import nc.itf.pubapp.pub.exception.IResumeException;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.pubitf.pu.m23.pubquery.IArrivePubQuery;
import nc.ui.ml.NCLangRes;
import nc.ui.pu.m23.action.approve.CheckDataValide;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pubapp.uif2app.actions.pflow.SaveScriptAction;
import nc.ui.pubapp.uif2app.model.BillManageModel;
import nc.ui.pubapp.uif2app.view.BillForm;
import nc.ui.pubapp.uif2app.view.ShowUpableBillForm;
import nc.ui.uif2.IShowMsgConstant;
import nc.ui.uif2.ShowStatusBarMsgUtil;
import nc.vo.pu.m21.exception.AskNumException;
import nc.vo.pu.m23.entity.ArriveHeaderVO;
import nc.vo.pu.m23.entity.ArriveItemVO;
import nc.vo.pu.m23.entity.ArriveVO;
import nc.vo.pu.m23.env.ArrivalUIToBSEnv;
import nc.vo.pu.m23.exception.AskWithCheckException;
import nc.vo.pu.m23.rule.ChkLiabcenterWhenSave;
import nc.vo.pu.m23.rule.ChkNumSignWhenSave;
import nc.vo.pu.m23.rule.NumAndMnySumWhenSave;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.pubapp.pflow.PfUserObject;
import nc.vo.sc.m61.exception.SCOrderAskPriceException;

/**
 * <p>
 * <b>本类主要完成以下功能：</b>
 * <ul>
 * <li>到货单 保存 按钮处理类
 * </ul>
 * <p>
 * <p>
 * 
 * @version 6.0
 * @since 6.0
 * @author hanbin
 * @time 2010-1-12 下午02:15:12
 */
public class SaveUIAction extends SaveScriptAction {

  private static final long serialVersionUID = 6556301184427090719L;
  


@Override
  public void doBeforAction() {
    ArriveVO aggVO = (ArriveVO) this.editor.getValue();

  //云峰网络  2020-12-20 校验失效日期不能早于到货日期
	new CheckDataValide().check(aggVO);
    ChkNumSignWhenSave chkNumSign = new ChkNumSignWhenSave();
    NumAndMnySumWhenSave numAndMnySum = new NumAndMnySumWhenSave();
    // 检查非空项
    // chkEmpty.chkEmpty(aggVO);
    // 检查数量符号的正负性
    chkNumSign.chkNumSign(aggVO);
    // 计算表头整单数量、本币价税合计
    numAndMnySum.numAndMnySum(aggVO);
    ChkLiabcenterWhenSave chkcenter = new ChkLiabcenterWhenSave();
    // 检查两个利润中心
    chkcenter.chkTwoCenter(aggVO);

    
  }

  /**
   * 父类方法重写
   * 
   * @see nc.ui.pubapp.uif2app.actions.pflow.AbstractScriptExcAction#isResume(nc.itf.pubapp.pub.exception.IResumeException)
   */
  @Override
  protected boolean isResume(IResumeException resumeInfo) {
    int answer =
        MessageDialog.showYesNoDlg(this.getFlowContext().getParent(), null,
            ((Exception) resumeInfo).getMessage());

    if (UIDialog.ID_YES != answer) {
      return false;
    }

    ArrivalUIToBSEnv env = null;
    PfUserObject pfuo = this.getFlowContext().getUserObj();
    if (pfuo == null) {
      env = new ArrivalUIToBSEnv();
      pfuo = new PfUserObject();
    }
    else {
      env = (ArrivalUIToBSEnv) pfuo.getUserObject();
    }

    if (resumeInfo instanceof AskNumException) {
      env.setbConfirm(UFBoolean.TRUE);
    }
    else if (resumeInfo instanceof SCOrderAskPriceException) {
      env.setbConfirm(UFBoolean.TRUE);
    }
    else if (resumeInfo instanceof AskWithCheckException) {
      env.setbBack(UFBoolean.TRUE);
    }
    else {
      return super.isResume(resumeInfo);
    }

    pfuo.setUserObject(env);
    this.getFlowContext().setUserObj(pfuo);
    return true;
  }

  @Override
  protected void processReturnObj(Object[] pretObj) throws Exception {
    super.processReturnObj(pretObj);

    // 刷新界面
    List<String> arriveHIDs = new ArrayList<String>();
    ArriveVO vo = (ArriveVO) this.editor.getValue();

    for (ArriveItemVO itemVo : vo.getBVO()) {
      if (itemVo.getCsourcearriveid() != null
          && !arriveHIDs.contains(itemVo.getCsourcearriveid())) {
        arriveHIDs.add(itemVo.getCsourcearriveid());
      }
    }

    if (!arriveHIDs.isEmpty()) {
      IArrivePubQuery arrivePubQuery =
          NCLocator.getInstance().lookup(IArrivePubQuery.class);
      ArriveVO[] arriveVos =
          arrivePubQuery.queryAggVOByHids(arriveHIDs
              .toArray(new String[arriveHIDs.size()]));

      // 刷新前台数据
      List<ArriveVO> datas = this.getModel().getData();
      for (int i = 0; i < datas.size(); i++) {
        if (datas.get(i) == null || datas.get(i).getBVO() == null) {
          continue;
        }
        for (ArriveItemVO dataItemVo : datas.get(i).getBVO()) {
          for (ArriveVO arriveVo : arriveVos) {
            for (ArriveItemVO itemVo : arriveVo.getBVO()) {
              if (itemVo.getPk_arriveorder_b().equals(
                  dataItemVo.getPk_arriveorder_b())) {
                datas.set(i, arriveVo);
              }
            }
          }
        }
      }
    }
  }

  @Override
  protected void showSuccessInfo() {
    ShowStatusBarMsgUtil.showStatusBarMsg(nc.vo.ml.NCLangRes4VoTransl
        .getNCLangRes().getStrByID("common", "UCH005")/* @res "保存成功" */, this
        .getModel().getContext());
  }
  

	
	 @Override
	  public void doAction(ActionEvent e) throws Exception {
		 ArriveVO aggVO = (ArriveVO) this.editor.getValue();
		 
			try {
			    ArriveItemVO[] bvo= aggVO.getBVO();
			    for(int i =0 ;i<bvo.length;i++){
			    	//查看物料批次档案是否有启动
			    	boolean flag=getStockInfo(bvo[i].getPk_material(), bvo[i].getPk_org());
			    	if(bvo[i].getVbatchcode()==null||bvo[i].getVbatchcode().equals(""))
			    	if(flag==true){
			    		String pcgz=querySysinitName("pcgz", bvo[i].getPk_org());//批次规则
						if("Y".equals(pcgz)){//如果批次为空
							String code=getcode(bvo[i].getPk_material());
							String pcqz=querySysinitName("pcqz", bvo[i].getPk_org());//批次前缀采取流水号前几位
							String pctsbs=querySysinitName("pctsbs", bvo[i].getPk_org());//批次特殊标识
							if(pcqz.equals("")){
								MessageDialog.showErrorDlg(this.getFlowContext().getParent(), null,"生成批次参数pcqz为空");
								return;
							}
							int pcqzcd=Integer.parseInt(pcqz);//获取物料前缀的长度
							code=getpcqz(pcqzcd, code);//计算批次号前缀
							String qz=code.substring( code.length()-pcqzcd, code.length());//前缀
							
							String bccode=getscmbatchcode(qz+pctsbs, bvo[i].getPk_material());//获取最新的批次号
							String pch="";//生成的批次号
							if(!bccode.equals("")){//流水号不为空
								String pclshcd=querySysinitName("pclshcd", bvo[i].getPk_org());//批次流水号长度
								if(pclshcd.equals("")){
									MessageDialog.showErrorDlg(this.getFlowContext().getParent(), null,"生成批次参数pclshcd为空");
									return;
								}
								int lshcd=Integer.parseInt(pclshcd);//获取批次流水号的长度
								String lsh=bccode.substring( bccode.length()-lshcd, bccode.length());//获取当前流水号
								Integer intcode= Integer.parseInt(lsh);
								lsh= String.format("%0"+lshcd+"d", (++intcode)); 
								pch=qz+pctsbs+lsh;//生成的批次号
							}else{//流水号为空
								String pclsqsh=querySysinitName("pclsqsh", bvo[i].getPk_org());//批次流水号初始号
								pch=qz+pctsbs+pclsqsh;//生成默认批次号
							} 
							
							((BillForm) this.editor).getBillCardPanel().setBodyValueAt(pch, i, "vbatchcode");
							
						}
							
						
			    	}
			    }
				} catch (BusinessException ex) {
					// TODO Auto-generated catch block
					ex.printStackTrace();
				}
		 
		 
		 
		
		
			 super.doAction(e);
	
	 }
  
  
  
  
  
  //查询参数代码
	private String querySysinitName(String initcode,String pk_org) throws BusinessException {
		  String sql = "select value from pub_sysinit  where initcode='"+initcode+"' and pk_org='"+pk_org+"' and nvl(dr,0)=0";
		  Object obj = NCLocator.getInstance().lookup(IUAPQueryBS.class).executeQuery(sql, new ColumnProcessor());
		  return obj == null ? "" : obj.toString();
		 }
	
	
	//查询物料是否启用批次管理
	public  boolean getStockInfo(String pk_material,String pk_org){
		String sb="select count(wholemanaflag) from bd_materialstock where pk_material='"+pk_material+"' and pk_org='"+pk_org+"' and  wholemanaflag='Y'"; 
		 Integer count = 0;
		try {
			   count = (Integer)NCLocator.getInstance().lookup(IUAPQueryBS.class).executeQuery(sb.toString(), new ColumnProcessor());
			   return count>0?true:false;
			  } catch (BusinessException e) {
			   e.printStackTrace();
			  }
		
		return false;
	}
	
	//查询物料编码
	public String getcode(String pk_material) throws BusinessException{
		 String sql = "select code from  bd_material where pk_material='"+pk_material+"'";
		  Object obj = NCLocator.getInstance().lookup(IUAPQueryBS.class).executeQuery(sql, new ColumnProcessor());
		  return obj == null ? "" : obj.toString();
	}
	
	//查询最新的批次号
	public String  getscmbatchcode(String  scmqz,String pk_material)throws BusinessException{
		 String sql = "select max(vbatchcode) from  scm_batchcode where cmaterialoid='"+pk_material+"' and  vbatchcode like '%"+scmqz+"%' ";
		  Object obj = NCLocator.getInstance().lookup(IUAPQueryBS.class).executeQuery(sql, new ColumnProcessor());
		  return obj == null ? "" : obj.toString();
	}
	
	//递归 计算 批次号补0规则
	public String getpcqz(int i ,String code){
		if(i>code.length()){
			code="0"+code;
			getpcqz(i, code);
		}
		return code;
	} 
	
	
  
}
