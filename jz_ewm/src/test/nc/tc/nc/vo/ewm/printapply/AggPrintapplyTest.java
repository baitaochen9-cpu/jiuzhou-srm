package nc.tc.nc.vo.ewm.printapply;
import org.testng.*;
import nc.vo.pubapp.pattern.model.entity.bill.AbstractBill;
import nc.vo.pubapp.pattern.model.meta.entity.bill.BillMetaFactory;
import nc.vo.pubapp.pattern.model.meta.entity.bill.IBillMeta;
import java.util.ArrayList;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.io.Serializable;
import jxl.read.biff.BiffException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import com.yonyou.uat.util.ExcelDataProvider;
import com.yonyou.uat.util.DBDataProvider;
import com.yonyou.uat.dbmanagement.DBManage;
import com.yonyou.uat.dbmanagement.QueryInfoVO;
import nc.vo.ewm.printapply.AggPrintapply;
import nc.bs.framework.common.NCLocator;
import com.yonyou.uat.framework.BaseTestCase;
import nc.vo.pubapp.pattern.model.meta.entity.bill.IBillMeta;
import nc.vo.ewm.printapply.Printapply;
public class AggPrintapplyTest extends BaseTestCase {
  AggPrintapply aggPrintapply=null;
  DBManage dbManage=null;
  
  @BeforeClass 
  public void BeforeClass(){
    aggPrintapply=NCLocator.getInstance().lookup(AggPrintapply.class);
  }
  
  @AfterClass 
  public void AfterClass(){
  }
  
  @BeforeMethod 
  public void BeforeMethod(){
    List<String> tableList=new ArrayList<String>();
    tableList.add("pub_wfexptlog");
    dbManage=new DBManage();
    dbManage.setTableList(tableList);
    dbManage.tableExport();
  }
  
  @AfterMethod 
  public void AfterMethod(){
    dbManage.tableRollBack();
  }
  
  @Test(description="",dependsOnMethods={},groups="",timeOut=100000,dataProvider="dp") 
  public void getMetaData(  Map<String,ArrayList<String>> dp){
    
    //Construct method parameters
    
    //Invoke tested method
    IBillMeta retObj=null;
    retObj=aggPrintapply.getMetaData();
    
    //Verify result is ok
    
    //Verify Object1 == Object2
    Assert.assertNotNull(retObj);
    Assert.assertNotNull(retObj.getBusinessAttribute());
    Assert.assertEquals(retObj.getBusinessAttribute().size(),0);
    Assert.assertNotNull(retObj.getChildForeignKeys());
    Assert.assertNotNull(retObj.getChildren());
    Assert.assertNotNull(retObj.getComponentName());
    Assert.assertEquals(retObj.getComponentName(),"expectValue");
    Assert.assertNotNull(retObj.getParent());
    
    //Verify DB result is ok
    QueryInfoVO queryInfoVerify=new QueryInfoVO();
    queryInfoVerify.setDatasource("datasourceName");
    queryInfoVerify.setTableName("tableName");
    queryInfoVerify.setCondition("where condition");
    List<Object> actualObjects=super.getDBObjectClass(Object.class,queryInfoVerify);
    Object actualObject=(Object)actualObjects.get(0);
    Assert.assertEquals("actualObject.getxxx()",dp.get("colName").get(0));
    
    //Verify whether have exception information in log 
    super.verifyLog("Error key word");
  }
  
  @Test(description="",dependsOnMethods={},groups="",timeOut=100000,dataProvider="dp") 
  public void getParentVO(  Map<String,ArrayList<String>> dp){
    
    //Construct method parameters
    
    //Invoke tested method
    Printapply retObj=new Printapply();
    retObj=aggPrintapply.getParentVO();
    
    //Verify result is ok
    
    //Verify Object1 == Object2
    Assert.assertNotNull(retObj);
    Assert.assertNotNull(retObj.getApprovedate());
    Assert.assertNotNull(retObj.getApprovenote());
    Assert.assertEquals(retObj.getApprovenote(),"expectValue");
    Assert.assertNotNull(retObj.getApprover());
    Assert.assertEquals(retObj.getApprover(),"expectValue");
    Assert.assertNotNull(retObj.getApprovestatus());
    Assert.assertEquals(retObj.getApprovestatus(),Integer.valueOf("0"));
    Assert.assertNotNull(retObj.getBilldate());
    Assert.assertNotNull(retObj.getBillid());
    Assert.assertEquals(retObj.getBillid(),"expectValue");
    Assert.assertNotNull(retObj.getBillmaker());
    Assert.assertEquals(retObj.getBillmaker(),"expectValue");
    Assert.assertNotNull(retObj.getBillno());
    Assert.assertEquals(retObj.getBillno(),"expectValue");
    Assert.assertNotNull(retObj.getBilltype());
    Assert.assertEquals(retObj.getBilltype(),"expectValue");
    Assert.assertNotNull(retObj.getBillversionpk());
    Assert.assertEquals(retObj.getBillversionpk(),"expectValue");
    Assert.assertNotNull(retObj.getBusitype());
    Assert.assertEquals(retObj.getBusitype(),"expectValue");
    Assert.assertNotNull(retObj.getCode());
    Assert.assertEquals(retObj.getCode(),"expectValue");
    Assert.assertNotNull(retObj.getCreationtime());
    Assert.assertNotNull(retObj.getCreator());
    Assert.assertEquals(retObj.getCreator(),"expectValue");
    Assert.assertNotNull(retObj.getDef1());
    Assert.assertEquals(retObj.getDef1(),"expectValue");
    Assert.assertNotNull(retObj.getDef10());
    Assert.assertEquals(retObj.getDef10(),"expectValue");
    Assert.assertNotNull(retObj.getDef2());
    Assert.assertEquals(retObj.getDef2(),"expectValue");
    Assert.assertNotNull(retObj.getDef3());
    Assert.assertEquals(retObj.getDef3(),"expectValue");
    Assert.assertNotNull(retObj.getDef4());
    Assert.assertEquals(retObj.getDef4(),"expectValue");
    Assert.assertNotNull(retObj.getDef5());
    Assert.assertEquals(retObj.getDef5(),"expectValue");
    Assert.assertNotNull(retObj.getDef6());
    Assert.assertEquals(retObj.getDef6(),"expectValue");
    Assert.assertNotNull(retObj.getDef7());
    Assert.assertEquals(retObj.getDef7(),"expectValue");
    Assert.assertNotNull(retObj.getDef8());
    Assert.assertEquals(retObj.getDef8(),"expectValue");
    Assert.assertNotNull(retObj.getDef9());
    Assert.assertEquals(retObj.getDef9(),"expectValue");
    Assert.assertNotNull(retObj.getEmendenum());
    Assert.assertEquals(retObj.getEmendenum(),Integer.valueOf("0"));
    Assert.assertNotNull(retObj.getId());
    Assert.assertEquals(retObj.getId(),"expectValue");
    Assert.assertNotNull(retObj.getLastmaketime());
    Assert.assertNotNull(retObj.getMaketime());
    Assert.assertNotNull(retObj.getMetaData());
    Assert.assertNotNull(retObj.getModifiedtime());
    Assert.assertNotNull(retObj.getModifier());
    Assert.assertEquals(retObj.getModifier(),"expectValue");
    Assert.assertNotNull(retObj.getName());
    Assert.assertEquals(retObj.getName(),"expectValue");
    Assert.assertNotNull(retObj.getPk_apply());
    Assert.assertEquals(retObj.getPk_apply(),"expectValue");
    Assert.assertNotNull(retObj.getPk_group());
    Assert.assertEquals(retObj.getPk_group(),"expectValue");
    Assert.assertNotNull(retObj.getPk_org());
    Assert.assertEquals(retObj.getPk_org(),"expectValue");
    Assert.assertNotNull(retObj.getPk_org_v());
    Assert.assertEquals(retObj.getPk_org_v(),"expectValue");
    Assert.assertNotNull(retObj.getPkorg());
    Assert.assertEquals(retObj.getPkorg(),"expectValue");
    Assert.assertNotNull(retObj.getRowno());
    Assert.assertEquals(retObj.getRowno(),"expectValue");
    Assert.assertNotNull(retObj.getSrcbillid());
    Assert.assertEquals(retObj.getSrcbillid(),"expectValue");
    Assert.assertNotNull(retObj.getSrcbilltype());
    Assert.assertEquals(retObj.getSrcbilltype(),"expectValue");
    Assert.assertNotNull(retObj.getTranstype());
    Assert.assertEquals(retObj.getTranstype(),"expectValue");
    Assert.assertNotNull(retObj.getTranstypepk());
    Assert.assertEquals(retObj.getTranstypepk(),"expectValue");
    Assert.assertNotNull(retObj.getTs());
    
    //Verify Return or middle Object == expect Object(from object file)
    Object expectedObj=super.getExpectResultObject("caseName");
    if (expectedObj != null) {
      Assert.assertEquals(retObj,expectedObj);
    }
 else {
      super.saveResultObject((Serializable)retObj,"caseName");
    }
    
    //Verify DB result is ok
    QueryInfoVO queryInfoVerify=new QueryInfoVO();
    queryInfoVerify.setDatasource("datasourceName");
    queryInfoVerify.setTableName("tableName");
    queryInfoVerify.setCondition("where condition");
    List<Object> actualObjects=super.getDBObjectClass(Object.class,queryInfoVerify);
    Object actualObject=(Object)actualObjects.get(0);
    Assert.assertEquals("actualObject.getxxx()",dp.get("colName").get(0));
    
    //Verify whether have exception information in log 
    super.verifyLog("Error key word");
  }
}
