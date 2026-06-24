package nc.vo.pu.m23.rule;

import nc.vo.ic.batchcode.AbstractBatchFieldMap;
import nc.vo.pu.m23.entity.ArriveItemVO;

/**
 * 
 * @author zhian.ye
 *
 */
public class BatchCOdeFieldMapForIn extends BatchCodeFieldMap{


	  @Override
	  public String[] getBillFields  (){
	    String[] billFields = new String[38];
	    billFields[0] = ArriveItemVO.PK_GROUP;
	    billFields[1] = ArriveItemVO.PK_BATCHCODE;
	    billFields[2] = ArriveItemVO.PK_MATERIAL;
	    billFields[3] = ArriveItemVO.PK_SRCMATERIAL;
	    billFields[4] = ArriveItemVO.VBATCHCODE;
	    billFields[5] = ArriveItemVO.BC_VVENDBATCHCODE;
//	    billFields[5] = null;
	    billFields[6] = ArriveItemVO.BC_TCHECKTIME;
	    billFields[7] = ArriveItemVO.BC_CQUALITYLEVELID;
	    billFields[8] = ArriveItemVO.DPRODUCEDATE;
	    billFields[9] = ArriveItemVO.DINVALIDDATE;
	    billFields[10] = ArriveItemVO.BC_BSEAL;
	    billFields[11] = ArriveItemVO.BC_TBATCHTIME;
	    billFields[12] = ArriveItemVO.BC_VNOTE;
	    billFields[13] = null;
	    billFields[14] = ArriveItemVO.BC_VERSION;
	    billFields[15] = ArriveItemVO.BC_VDEF1;
	    billFields[16] = ArriveItemVO.BC_VDEF2;
	    billFields[17] = ArriveItemVO.BC_VDEF3;
	    billFields[18] = ArriveItemVO.BC_VDEF4;
	    billFields[19] = ArriveItemVO.BC_VDEF5;
	    billFields[20] = ArriveItemVO.BC_VDEF6;
	    billFields[21] = ArriveItemVO.BC_VDEF7;
	    billFields[22] = ArriveItemVO.BC_VDEF8;
	    billFields[23] = ArriveItemVO.BC_VDEF9;
	    billFields[24] = ArriveItemVO.BC_VDEF10;
	    billFields[25] = ArriveItemVO.BC_VDEF11;
	    billFields[26] = ArriveItemVO.BC_VDEF12;
	    billFields[27] = ArriveItemVO.BC_VDEF13;
	    billFields[28] = ArriveItemVO.BC_VDEF14;
	    billFields[29] = ArriveItemVO.BC_VDEF15;
	    billFields[30] = ArriveItemVO.BC_VDEF16;
	    billFields[31] = ArriveItemVO.BC_VDEF17;
	    billFields[32] = ArriveItemVO.BC_VDEF18;
	    billFields[33] = ArriveItemVO.BC_VDEF19;
	    billFields[34] = ArriveItemVO.BC_VDEF20;
	    billFields[35] = ArriveItemVO.BC_TS;
	    billFields[36] = ArriveItemVO.VPRODBATCHCODE;
	    billFields[37] = null;
	    return billFields;
	  }

}
