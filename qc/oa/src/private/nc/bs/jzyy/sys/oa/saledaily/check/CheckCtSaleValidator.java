package nc.bs.jzyy.sys.oa.saledaily.check;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import nc.vo.ct.entity.CtAbstractBVO;
import nc.vo.ct.saledaily.entity.CtSaleBVO;
import nc.vo.ct.saledaily.entity.CtSaleVO;
import nc.vo.pub.IAttributeMeta;
import nc.vo.pub.ISuperVO;
import nc.vo.pub.IVOMeta;
import nc.vo.pub.ValidationException;
import nc.vo.pubapp.pattern.model.entity.bill.AbstractBill;
import nc.vo.pubapp.pattern.model.meta.entity.bill.IBillMeta;
import nc.vo.pubapp.pattern.pub.MapList;
import nc.vo.scmpub.check.vovalidate.VONullValidate;

/**
 * 
 * @Description: 销售合同新增保存必输项校验
 *   
 * @author: 刘伟
 * @date:   2019-4-25 上午10:22:38   
 * @version NCC1909
 */
public class CheckCtSaleValidator extends VONullValidate {

	@Override
	public String[] getHeadNotNullFields() {
		String[] headnames =
	        {
	          CtSaleVO.PK_ORG, CtSaleVO.CTNAME, CtSaleVO.VTRANTYPECODE,
	          CtSaleVO.VALDATE, CtSaleVO.INVALLIDATE, CtSaleVO.PK_CUSTOMER
	        };
	    return headnames;
	}

	@Override
	  public String [] getBodyNotNullFields() {

	    String[] bodynames = {};
	    return bodynames;
	  }

	  @Override
	  public MapList<String, String> getMultiBodyNotNullFields() {
	    // TODO Auto-generated method stub
	    return null;
	  }

	  @Override
	  public void otherCheck(AbstractBill billVO,
	      List<ValidationException> exceptions) {

	    // 表体不能同时为空
	    check(billVO, null, new String[] {
	    		CtAbstractBVO.PK_MATERIAL, CtAbstractBVO.PK_MARBASCLASS,
	            CtAbstractBVO.CUNITID, CtAbstractBVO.NNUM,
	            CtAbstractBVO.NQTORIGPRICE, CtAbstractBVO.NQTORIGTAXPRICE,
	            CtAbstractBVO.NORIGTAXMNY
	    }, exceptions);

	  }

	  protected void check(AbstractBill bill, String[] headnullkeys,
	      String[] bodynullkeys, List<ValidationException> exceptions) {
	    // 检查表头
	    checkHead(bill, headnullkeys, exceptions);

	    // 检查表体
	    checkBody(bill, bodynullkeys, exceptions);
	  }

	  private void checkHead(AbstractBill bill, String[] headnullkeys,
	      List<ValidationException> exceptions) {

	    IBillMeta billmeta = bill.getMetaData();
	    IVOMeta parent = billmeta.getParent();
	    // 检查表头
	    if (headnullkeys != null) {
	      Set<String> nullheadset = new HashSet<String>();
	      ISuperVO headvo = bill.getParent();
	      for (String headnullkey : headnullkeys) {
	        if (headvo.getAttributeValue(headnullkey) == null) {
	          IAttributeMeta attribute=parent.getAttribute(headnullkey);
	          nullheadset.add(attribute.getColumn().getLabel());
	        }
	      }
	      if (nullheadset.size() > 0 && nullheadset.size() == headnullkeys.length) {
	        String message = "表头以下字段不能同时为空：" + getshow(nullheadset, "、");/*-=notranslate=-*/
	        exceptions.add(new ValidationException(message));
	      }
	    }

	  }

	  private void checkBody(AbstractBill bill, String[] bodynullkeys,
	      List<ValidationException> exceptions) {
	    if (bodynullkeys != null) {
	      int row = 1;
	      ISuperVO[] supervos = bill.getChildren(CtSaleBVO.class);
	      Set<String> messagelist = new HashSet<String>();
	      for (ISuperVO vo : supervos) {
	        Set<String> nullbodyset = new HashSet<String>();
	        for (String bodynullkey : bodynullkeys) {
	          if (vo.getAttributeValue(bodynullkey) == null) {
	            IAttributeMeta attribute=vo.getMetaData().getAttribute(bodynullkey);
	            nullbodyset.add(attribute.getColumn().getLabel());
	          }
	        }
	        if (nullbodyset.size() > 0 && nullbodyset.size() == bodynullkeys.length) {
	          String message =
	              "表体第" + row + "行以下字段不能同时为空：" + getshow(nullbodyset, "、");/*-=notranslate=-*/
	          messagelist.add(message);
	        }
	        row++;
	      }
	      if (messagelist.size() > 0) {
	        exceptions.add(new ValidationException(getshow(messagelist, "\r\n")));
	      }
	    }
	  }

	  private String getshow(Set<String> nullfields, String partition) {
	    StringBuffer str = new StringBuffer();
	    for (String field : nullfields) {
	      str.append(field + partition);
	    }
	    str.deleteCharAt(str.length() - 1);
	    return str.toString();
	  }

	
}
