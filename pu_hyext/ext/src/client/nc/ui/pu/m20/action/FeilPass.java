package nc.ui.pu.m20.action;

import java.awt.HeadlessException;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.JFileChooser;

import org.apache.commons.collections.map.HashedMap;

import nc.cmp.tools.StringUtil;
import nc.ui.uif2.ShowStatusBarMsgUtil;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.Sheet;

public class FeilPass {
	
	public List<PraybillData> FileOpen() throws BiffException, IOException
    {
    File f;
    JFileChooser fc = new JFileChooser("C:\\"); //这里可以设置打开默认路径
    String fileName="",filetext;
    String flags="";
    
    List<PraybillData> billDatas=new ArrayList<>();
    
    try{
         fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
         //设置 JFileChooser，以允许用户只选择文件、只选择目录，或者可选择文件和目录。
         fc.showOpenDialog(null);
         //打开目录对话框
         flags=fc.getSelectedFile().getName();
           f=fc.getCurrentDirectory();
         //获得文件名    
           fileName=f.getAbsolutePath()+"\\"+flags;
           System.out.println(fileName);
           
           File chooseFile=fc.getSelectedFile();
           Workbook workBook=Workbook.getWorkbook(chooseFile);
           Sheet sheet=workBook.getSheet(1);
           int rowSize=sheet.getRows();
           PraybillData billData=new PraybillData();
           
           for(int i=2;i<rowSize;i++){
        	   String materrialCode =sheet.getCell(1, i).getContents();//物料编码
        	   materrialCode=materrialCode.trim();
        	   String unitID=sheet.getCell(3, i).getContents();//计量单位编码(位置未定)
        	   unitID=unitID.trim();
        	   String nastNumStr =sheet.getCell(4, i).getContents();//计划数量(位置未定)
        	   nastNumStr=nastNumStr.trim();
        	   boolean isNum=nastNumStr.matches("^[0-9]*$");//匹配是否为数字
        	   BigDecimal nastNum=BigDecimal.ZERO;
        	   if(isNum&&StringUtil.isNotEmpty(nastNumStr)){
        		   nastNum=new BigDecimal(nastNumStr);
        	   }
        	   billData=new PraybillData();
        	   billData.setMaterialCode(materrialCode);
        	   billData.setUnitId(unitID);
        	   billData.setNastNum(nastNum);
        	   billDatas.add(billData);
           }
     }  
    	catch(HeadlessException he){    
              System.out.println("Save File Dialog ERROR!");    
         }
     return billDatas;
    }
}
