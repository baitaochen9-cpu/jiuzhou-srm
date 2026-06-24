package nc.bs.jzyy.sys.lims.pucheck;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import nc.bs.framework.common.NCLocator;
import nc.impl.pubapp.pattern.data.vo.VOInsert;
import nc.impl.pubapp.pattern.data.vo.VOUpdate;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.MapProcessor;
import nc.vo.pu.m23.entity.ArriveBbVO;
import nc.vo.pu.m23.entity.ArriveItemVO;
import nc.vo.pu.m23.entity.ArriveVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.ISuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.pattern.pub.MathTool;

import org.apache.commons.lang.ArrayUtils;

/**
 * 生成到货单报检信息
 * 
 * @author yunfeng.li
 * 
 */
public class WriteBackArriveCheckNumRule implements IRule<ArriveVO> {
	private boolean isQcCheck;

	public WriteBackArriveCheckNumRule(boolean isQcCheck) {
		this.isQcCheck = isQcCheck;
	}

	public void process(ArriveVO[] vos) {
		String pk_org = vos[0].getHVO().getPk_org();
		if (isQcCheck) {
			try {
				insertBBTable(vos);
			} catch (BusinessException e) {
				e.printStackTrace();
			}
		}
		updateBTableEligNum(vos);
	}

	private void insertBBTable(ArriveVO[] vos) throws BusinessException {
		List writeParas = new ArrayList();
		for (int i = 0; i < vos.length; ++i) {
			ArriveItemVO[] bvos = vos[i].getBVO();
			for (ArriveItemVO vo : bvos) {
				vo.setNchecknum(vo.getNnum());// 本次报检数量
				vo.setNwillelignum(vo.getNnum());// 合格主数量
				vo.setNwillnotelignum(UFDouble.ZERO_DBL);// 不合格主数量
				if (vo.getNwillelignum().compareTo(UFDouble.ZERO_DBL) > 0) {
					ArriveBbVO bbVO = new ArriveBbVO();
					bbVO.setNnum(UFDouble.ZERO_DBL);
					bbVO.setNastnum(UFDouble.ZERO_DBL);
					bbVO.setPk_group(vo.getPk_group());
					bbVO.setPk_arriveorder(vo.getPk_arriveorder());
					bbVO.setPk_arriveorder_b(vo.getPk_arriveorder_b());

					String ctrantype = getctrantype(
							vos[i].getHVO().getPk_org(), vo.getPk_material());
					if ("3".equals(ctrantype)) {
						// 罐区溶剂 检验完成才入库
						// bcanstore 是否可入库
						bbVO.setBcanstore(UFBoolean.FALSE);
					} else {
						// 非罐区溶剂 报检中入库
						// bcanstore 是否可入库
						bbVO.setBcanstore(UFBoolean.TRUE);
					}
					// beligible 是否合格 beligible char(1) UFBoolean
					bbVO.setBeligible(UFBoolean.TRUE);
					bbVO.setPk_inbatchcode(vo.getPk_batchcode());
					bbVO.setVinbatchcode(vo.getVbatchcode());
					writeParas.add(bbVO);
				}
				if (vo.getNwillnotelignum().compareTo(UFDouble.ZERO_DBL) > 0) {
					ArriveBbVO bbVO = new ArriveBbVO();
					bbVO.setNnum(UFDouble.ZERO_DBL);
					bbVO.setNastnum(UFDouble.ZERO_DBL);

					bbVO.setPk_group(vo.getPk_group());

					bbVO.setPk_arriveorder(vo.getPk_arriveorder());
					bbVO.setPk_arriveorder_b(vo.getPk_arriveorder_b());

					bbVO.setBcanstore(UFBoolean.FALSE);

					bbVO.setBeligible(UFBoolean.FALSE);

					bbVO.setPk_inbatchcode(vo.getPk_batchcode());
					bbVO.setVinbatchcode(vo.getVbatchcode());
					writeParas.add(bbVO);
				}
			}
		}
		ArriveBbVO[] bbvos = (ArriveBbVO[]) writeParas
				.toArray(new ArriveBbVO[writeParas.size()]);
		if (ArrayUtils.isEmpty(bbvos))
			return;
		VOInsert util = new VOInsert();
		util.insert(bbvos);
	}

	private void updateBTableEligNum(ArriveVO[] vos) {
		List volists = new ArrayList();
		for (int i = 0; i < vos.length; ++i) {
			ArriveItemVO[] bvos = vos[i].getBVO();
			for (int j = 0; j < bvos.length; ++j) {
				if (this.isQcCheck) {

					// getNchecknum 本次报检数量
					bvos[j].setNaccumchecknum(MathTool.add(
							bvos[j].getNaccumchecknum(), bvos[j].getNchecknum()));// 累计报检主数量
					// getNwillelignum 合格主数量
					bvos[j].setNelignum(MathTool.add(bvos[j].getNelignum(),
							bvos[j].getNwillelignum()));// 累计合格主数量
					// getNwillnotelignum 不合格主数量
					bvos[j].setNnotelignum(MathTool.add(
							bvos[j].getNnotelignum(),
							bvos[j].getNwillnotelignum()));// 累计不合格主数量
				} else {
					bvos[j].setNelignum(UFDouble.ZERO_DBL);// 累计合格主数量
					bvos[j].setNnotelignum(UFDouble.ZERO_DBL);// 累计不合格主数量
					bvos[j].setNaccumchecknum(UFDouble.ZERO_DBL);// 累计报检主数量
				}
				volists.add(bvos[j]);
			}
		}
		VOUpdate util = new VOUpdate();
		util.update(
				(ISuperVO[]) volists.toArray(new ArriveItemVO[volists.size()]),
				new String[] { "naccumchecknum", "nelignum", "nnotelignum" });
	}

	private String getctrantype(String pk_org, String pk_material)
			throws BusinessException {
		// stockbycheck 根据检验结果入库
		String sql = " select stockbycheck    from bd_materialstock where pk_material='"
				+ pk_material + "' and   pk_org ='" + pk_org + "' and dr=0";
		IUAPQueryBS bs = (IUAPQueryBS) NCLocator.getInstance().lookup(
				IUAPQueryBS.class.getName());
		HashMap<String, Object> hashMap2 = (HashMap<String, Object>) bs
				.executeQuery(sql, new MapProcessor());
		String chkfreeflag = (String) hashMap2.get("stockbycheck");
		if (chkfreeflag == null) {
			return "1";
		}
		UFBoolean b = UFBoolean.valueOf(chkfreeflag);
		// / 罐区溶剂--
		if (b.booleanValue()) {
			return "3";
		}

		return "1";
	}

}