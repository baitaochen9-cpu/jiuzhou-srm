package nc.bs.jzyy.sys.oa.saledaily.check;

import nc.vo.ct.entity.CtAbstractBVO;
import nc.vo.ct.entity.CtAbstractVO;
import nc.vo.ct.purdaily.entity.CtPuVO;
import nc.vo.ct.saledaily.entity.CtSaleVO;

/**
 * 禁止修改变更的字段
 * @Description: 
 *   
 * @author: 刘伟
 * @date:   2019-4-28 下午6:02:21   
 * @version NCC1909
 */
public class CheckProhibitFields {
	
	//表头
	public static String[] HEADFIELDS = {
		CtAbstractVO.ACTUALINVALIDATE,
		CtAbstractVO.ACTUALVALIDATE,CtAbstractVO.APPROVER,CtAbstractVO.BLATEST,CtAbstractVO.BORDERNUMEXEC,
		CtAbstractVO.CBILLTYPECODE,CtAbstractVO.CCURRENCYID,CtAbstractVO.FSTATUSFLAG,CtAbstractVO.IPRINTCOUNT,
		CtAbstractVO.NGLOBALEXCHGRATE,CtAbstractVO.NGROUPEXCHGRATE,CtAbstractVO.NORIGPSHAMOUNT,CtAbstractVO.NTOTALASTNUM,
		CtAbstractVO.NTOTALGPAMOUNT,CtAbstractVO.NTOTALORIGMNY,CtAbstractVO.NTOTALTAXMNY,CtAbstractVO.PK_GROUP,
		CtAbstractVO.PK_ORG,CtAbstractVO.PK_ORG_V,CtAbstractVO.PK_ORIGCT,CtAbstractVO.TAUDITTIME,CtAbstractVO.VBILLCODE,
		CtAbstractVO.VERSION,CtPuVO.PK_CT_PU,CtSaleVO.PK_CT_SALE
	};
	
	//表体
	public static String[] BODYFIELDS = {
		CtAbstractBVO.BSOURCEEC,CtAbstractBVO.CROWNO,CtAbstractBVO.CSRCBBID,CtAbstractBVO.CSRCBID,CtAbstractBVO.CSRCID,
		CtAbstractBVO.NORDNUM,CtAbstractBVO.NORDSUM,CtAbstractBVO.NORITOTALGPMNY,CtAbstractBVO.NTOTALGPMNY,
		CtAbstractBVO.PK_GROUP,CtAbstractBVO.PK_ORG,CtAbstractBVO.PK_ORG_V,CtAbstractBVO.SOURCEBTS,CtAbstractBVO.SOURCETS,
		CtAbstractBVO.VRSTRANTYPECODE,CtAbstractBVO.VSRCCODE,CtAbstractBVO.VSRCROWNO,CtAbstractBVO.VSRCTYPE
	};

}
