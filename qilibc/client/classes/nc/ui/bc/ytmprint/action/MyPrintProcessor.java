package nc.ui.bc.ytmprint.action;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import nc.bs.framework.common.NCLocator;
import nc.itf.qilibc.ITmprintMaintain;
import nc.itf.uap.IUAPQueryBS;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.print.IPrintListener;
import nc.ui.pub.print.PrintException;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pubapp.AppContext;
import nc.vo.qilibc.tmprintupdate.TmprintupdateVO;

/**
 * 产品登记--打印处理类
 * @author tts20
 *
 */
public class MyPrintProcessor implements IPrintListener {
	List<TmprintupdateVO> volist;
	BillModel billModel;
	List<Integer> selectrows;
	public MyPrintProcessor(List<TmprintupdateVO> printdatas, BillModel billModel, List<Integer> selectrow) {
		volist = printdatas;
		this.billModel = billModel;
		this.selectrows = selectrows;
	}

	/**
	 * 打印后更新条码配置标识
	 */
	@Override
	public void afterPrint(int rowCount) throws PrintException {
		try {
//			IBarcodeconfigMaintain  updatetoos= NCLocator.getInstance().lookup(IBarcodeconfigMaintain.class);
//			for(int i = 0; i < volist.size(); i ++){
//				TmprintVO vo = volist.get(i);
//				String pk = (String) vo.getAttributeValue("configcode");
//				Object o = HYPubBO_Client.findColValue("qilibc_barconfig", "def1", "id='"+pk+"' and dr=0");
//				if(o != null && !"~".equals(o)){
//					continue;
//				}
//				updatetoos.updatebs("update qilibc_barconfig set def1='Y' where id='"+pk+"' and dr=0");
//			}
			TmprintupdateVO printVO = volist.get(rowCount);
//			String lsh = printVO.getLsh();
//			String vbatchcode = printVO.getVbatchcode();
//			if(lsh != null && !"".equals(lsh)){
//				String sqlwhere = "lsh='"+printVO.getLsh()+"' and pk_material ='"+printVO.getPk_material()+"' and dr=0  ";
//				if (vbatchcode != null && !"".equals(vbatchcode)) {
//					sqlwhere = "lsh='"+printVO.getLsh()+"' and pk_material ='"+printVO.getPk_material()+"' and dr=0  and vbatchcode ='"+vbatchcode+"'";
//				}
//				Object lsh2 =HYPubBO_Client.findColValue("qilibc_tmprintupdate", "lsh", sqlwhere);
//				if(null!=lsh2 &&  !lsh2.equals("")){
//					int  version=Integer.parseInt(printVO.getVersion())+1;
//					printVO.setVersion(String.format("%04d", version));
//					billModel.setValueAt(String.format("%04d", version),rowCount , "version");
//				}
//			}
			List<TmprintupdateVO> printVOs = new ArrayList<TmprintupdateVO>();
			for (TmprintupdateVO tmprintVO : volist) {
				TmprintupdateVO tmprintVOc = (TmprintupdateVO) tmprintVO.clone();
				tmprintVOc.setPk_tmprint(null);//清空主键
				tmprintVOc.setStatus(VOStatus.NEW);
				tmprintVOc.setTs(new UFDateTime(new Date()));
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				if(tmprintVOc.getDproducedate()!=null){
					String s = sdf.format(tmprintVOc.getDproducedate().toDate());
					Date date =  sdf.parse(s);
					tmprintVOc.setDproducedate(new UFDate(date));
				}
				tmprintVOc.setVfree8(null);
				tmprintVOc.setVfree8name(null);
				tmprintVOc.setPrinter(AppContext.getInstance().getPkUser());
				printVOs.add(tmprintVOc);
			}
//			List<TmprintupdateVO> printVOs = new ArrayList<TmprintupdateVO>();
//			printVOs.add(printVO);
			ITmprintMaintain save = NCLocator.getInstance().lookup(ITmprintMaintain.class);
			save.savetmupdate(printVOs);
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}

	@Override
	public void beforePrintPage(int pageNum, int rowCount)
			throws PrintException {
	}

	@Override
	public void afterPrintPage(int pageNum, int rowCount) throws PrintException {
	}
	private IUAPQueryBS query = null;
	private IUAPQueryBS getQry() {
		if(query == null)
			query = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		return query;
	}

	@Override
	public void beforePrint() throws PrintException {
		// TODO 自动生成的方法存根
		
	}
}
