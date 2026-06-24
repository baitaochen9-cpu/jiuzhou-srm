/** <a href="http://www.cpupk.com/decompiler">Eclipse Class Decompiler</a> plugin, Copyright (c) 2017 Chen Chao. */
package nc.ui.ic.general.action;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.jdbc.framework.processor.MapProcessor;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pubapp.pub.common.context.PFlowContext;
import nc.vo.ic.general.define.ICBillBodyVO;
import nc.vo.ic.general.define.ICBillVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.pattern.pub.PubAppTool;

public class RackCheckRule {

	IUAPQueryBS iuap = (IUAPQueryBS) NCLocator.getInstance().lookup(
			IUAPQueryBS.class.getName());
	PFlowContext context;

	public RackCheckRule(PFlowContext context) {
		this.context = context;
	}

	/**
	 *
	 * @param aggvo
	 * @return
	 * @throws BusinessException
	 */
	public String checkRack(ICBillVO aggvo) throws BusinessException {
		String type = aggvo.getHead().getVtrantypecode();
		String checkReust = "pass";
		if (PubAppTool.isNull(type)) {
			return checkReust;
		}
		if (type.startsWith("4455")) {
			return checkReust;
		}
		String pk_org = aggvo.getHead().getPk_org();
		// 控制类型 提醒 、强控、不控制
		Map map = getKzlx(pk_org);
		String kzlx = map.get("value").toString();
		if ("不控制".equals(kzlx)) {
			return checkReust;
		}
		if (type.startsWith("46") || type.startsWith("4A")
				|| type.startsWith("45")) {
			ICBillBodyVO[] bvo = aggvo.getBodys();
			for (int i = 0; i < bvo.length; i++) {
				String clocationid = bvo[i].getClocationid();
				// 货位调整单，取调入货位
				if (type.startsWith("HWTZ") || type.startsWith("4Q")) {
					clocationid = (String) bvo[i]
							.getAttributeValue("clocationinid");
				}

				String loc = getRockType(clocationid);
				// 如果不允许重复入库
				if (!"Y".equals(loc)) {
					UFDouble num = getRockNum(pk_org, clocationid);
					if (num.compareTo(UFDouble.ZERO_DBL) > 0) {
						if ("强控".equals(kzlx)) {
							MessageDialog.showErrorDlg(
									this.context.getParent(), null, "第"
											+ (i + 1) + "行货位有物料,不允许入库");
							checkReust = "notPass";
						}
						if ("提示".equals(kzlx)) {
							int issure = MessageDialog.showHintDlg(
									this.context.getParent(), "提示", "第"
											+ (i + 1) + "此货位有物料");
						}
					}
				}
			}

		}
		// 4Q 货位调整单
		if (type.startsWith("HWTZ") || type.startsWith("4Q")) {

			ICBillBodyVO[] bvo = aggvo.getBodys();
			StringBuffer strb=   new StringBuffer();
			for (int i = 0; i < bvo.length; i++) {
				String clocationid = bvo[i].getClocationid();
				// 货位调整单，取调入货位
				clocationid = (String) bvo[i].getAttributeValue("clocationinid");
				String loc = getRockType(clocationid);
				// 如果不允许重复入库,但是可以放入 同一物料的相同批次
				if (!"Y".equals(loc)) {
					String cmaterialoid = bvo[i].getCmaterialoid();
					String vbatchcode = bvo[i].getVbatchcode();
					String matcheKey = cmaterialoid+vbatchcode;
					List rockInforList= getRockInfor(pk_org, clocationid);
					List<String> list = null;
					if (rockInforList!=null && rockInforList.size() >0) {
						list = new ArrayList<String>();
						for(Object rs:rockInforList){
							Map rockInfor = (Map) rs;
							String rockKey = (String)rockInfor.get("cmaterialoid")+rockInfor.get("vbatchcode");
							list.add(rockKey);
						}


					}

					if(null == list){
						continue;
					}

					boolean sameComboExists = list.contains(matcheKey);
					if(!sameComboExists){

						strb.append( "\n第"+ (i + 1) +"行"+ "此货位有[非相同物料+批次]的物料; ");
					}


				}
			}
			if(  strb.length() > 0 ){
				if ("提示".equals(kzlx)){
					MessageDialog.showHintDlg(this.context.getParent(),null,kzlx+":"+strb);
				}else if("强控".equals(kzlx)){
					MessageDialog.showErrorDlg(this.context.getParent(),null,kzlx+":"+strb);
					checkReust = "notPass";
				}
			}

		}
		return checkReust;

	}
	/**
	 * 查询当前货位上是否存在物料，并返回物料信息
	 * @param pk_org
	 * @param clocationid
	 * @return
	 * @throws BusinessException
	 */
	private List getRockInfor(String pk_org, String clocationid) throws BusinessException {
		String numsql = "select  b.cmaterialoid, b.vbatchcode "
				+ " from ic_onhandnum a,ic_onhanddim b \n"
				+ " where b. pk_onhanddim =a.pk_onhanddim \n"
				+ " and  a.dr=0 and b.dr=0\n"
				+ " and b.pk_org ='" + pk_org+ "'"
				+ " and b. clocationid ='" + clocationid + "'"
				+ " and a.nonhandnum>0 ";
		List rs = (List) iuap.executeQuery(numsql, new MapListProcessor());
		return rs;
	}

	/**
	 * 获取货位数量
	 *
	 * @param pk_org
	 * @param clocationid
	 * @return
	 * @throws BusinessException
	 */

	public UFDouble getRockNum(String pk_org, String clocationid)
			throws BusinessException {
		String numsql = "select   sum(a.nonhandnum) as nonhandnum\n"
				+ "from ic_onhandnum a,ic_onhanddim b\n"
				+ "where b. pk_onhanddim =a.pk_onhanddim\n"
				+ "and  a.dr=0 and b.dr=0\n" + "and b.pk_org ='" + pk_org
				+ "'\n" +
				// " and  b.cmaterialoid ='"+cmaterialoid+"'\n"
				// +
				"and b. clocationid ='" + clocationid + "'";
		Object obj = iuap.executeQuery(numsql, new ColumnProcessor());
		UFDouble num = new UFDouble(obj == null ? "0.0" : obj.toString());
		return num;
	}

	// 货位是否允许重复入库
	public String getRockType(String clocationid) throws BusinessException {

		String locsql = " select isrestore  from bd_rack where dr=0 and pk_rack='"
				+ clocationid + "'";
		Object obj2 = iuap.executeQuery(locsql, new ColumnProcessor());
		String loc = obj2 == null ? "N" : obj2.toString();
		return loc;
	}

	public Map getKzlx(String pk_org) throws BusinessException {

		// // 控制类型 提醒 、强控、不控制
		String sql = "select nvl(a.value,0)   as value " + "\n"
				+ "from  pub_sysinit a " + "\n"
				+ "where a.initcode ='IC146' and  a.pk_org='" + pk_org + "'";
		Map rs = (Map) iuap.executeQuery(sql, new MapProcessor());
		return rs;
	}

	// 查询物料编码
	public String getcode(String pk_material) throws BusinessException {
		String sql = "select code from  bd_material where pk_material='"
				+ pk_material + "'";
		Object obj = iuap.executeQuery(sql, new ColumnProcessor());
		return obj == null ? "" : obj.toString();
	}

}
