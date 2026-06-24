/*
 * Created on 2005-10-11
 * 
 */
package nc.ui.pub.print;

import java.awt.Image;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.imageio.ImageIO;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.framework.common.RuntimeEnv;
import nc.bs.framework.exception.ComponentException;
import nc.bs.logging.Logger;
import nc.bs.pub.filesystem.IFileSystemService;
import nc.itf.uap.print.IPrintTemplateBS;
import nc.itf.uap.print.IPrintTemplateBSForHR;
import nc.itf.uap.print.IPrintTemplateQry;
import nc.ui.pub.print.datastruct.Constants;
import nc.vo.pub.BusinessException;
import nc.vo.pub.filesystem.NCFileNode;
import nc.vo.pub.print.CreateItemVO;
import nc.vo.pub.print.CreatePrintParaVO;
import nc.vo.pub.print.PrintTemplateVO;
import nc.vo.pub.print.PrintTempletmanageHeaderVO;
import nc.vo.pub.print.PrintTempletmanageItemVO;
import nc.vo.pub.print.PrintVO;
import nc.vo.pub.print.tools.PrintTemplateCreatorVO;
import nc.vo.uap.busibean.exception.BusiBeanException;

/**
 * 打印模板调用后台服务的代理
 * 
 * @author lhwei
 *
 */
public class PrintTemplateHelper {

    private static IPrintTemplateBS printTemplateBS;
    private static IPrintTemplateQry printTemplateQry;
    private static IPrintTemplateBSForHR printTemplateBSForHR;

    /**
     * @return Returns the printTemplateBS.
     * @throws ComponentException
     */
    public synchronized static IPrintTemplateBS getPrintTemplateBS() throws ComponentException {
	if (printTemplateBS == null) {
	    printTemplateBS = (IPrintTemplateBS) NCLocator.getInstance().lookup(IPrintTemplateBS.class.getName());
	}
	return printTemplateBS;
    }

    /**
     * @return Returns the printTemplateBS.
     * @throws ComponentException
     */
    public synchronized static IPrintTemplateQry getPrintTemplateQry() throws ComponentException {
	if (printTemplateQry == null) {
	    printTemplateQry = (IPrintTemplateQry) NCLocator.getInstance().lookup(IPrintTemplateQry.class.getName());
	}
	return printTemplateQry;
    }

    /**
     * @return Returns the printTemplateBSForHR.
     * @throws ComponentException
     */
    public synchronized static IPrintTemplateBSForHR getPrintTemplateBSForHR() throws ComponentException {
	if (printTemplateBSForHR == null) {
	    printTemplateBSForHR = (IPrintTemplateBSForHR) NCLocator.getInstance()
		    .lookup(IPrintTemplateBSForHR.class.getName());
	}
	return printTemplateBSForHR;
    }

    public static boolean delete(String key) throws BusinessException {
	return getPrintTemplateBS().delete(key);
    }

    /**
     * 此处插入方法说明。 创建日期：(2002-6-27 16:59:43)
     * 
     * @throws BusinessException
     * @throws ComponentException
     */
    public static String[] findDefaultIDwithTS(String id) throws BusinessException {
	return getPrintTemplateQry().findDefaultIDwithTS(id);
    }

    /**
     * 向数据库中插入一个VO对象。
     *
     * 创建日期：(2001-4-20)
     * 
     * @param printTemplate nc.vo.pub.print.PrintTemplateVO
     * @return java.lang.String 所插入VO对象的主键字符串。
     * @throws BusinessException
     * @throws ComponentException
     */
    public static PrintVO getCardPrintVO(String templateid) throws BusinessException {
	return getPrintTemplateQry().getCardPrintVO(templateid);
    }

    /**
     * 此处插入方法说明。 创建日期：(01-4-24 14:19:36)
     * 
     * @return java.lang.String
     * @param templateName java.lang.String
     * @throws BusinessException
     * @throws ComponentException
     */
    public static PrintTemplateVO[] getCardTempletByModule(String moduleName, String pkCorp) throws BusinessException {
	return getPrintTemplateQry().getCardTempletByModule(moduleName, pkCorp);
    }

    /**
     * 向数据库中插入一个VO对象。
     *
     * 创建日期：(2001-4-20)
     * 
     * @param printTemplate nc.vo.pub.print.PrintTemplateVO
     * @return java.lang.String 所插入VO对象的主键字符串。
     * @throws BusinessException
     * @throws ComponentException
     */
    public static PrintVO getPrintVO(String templateid) throws BusinessException {
	return getPrintTemplateQry().getPrintVO(templateid);
    }

    /**
     * 此处插入方法说明。 创建日期：(01-4-24 14:19:36)
     * 
     * @return java.lang.String
     * @param templateName java.lang.String
     * @throws BusinessException
     * @throws ComponentException
     */
    public static PrintTemplateVO[] getTempletByModule(String moduleName) throws BusinessException {
	return getPrintTemplateQry().getTempletByModule(moduleName);
    }

    /**
     * 此处插入方法说明。 创建日期：(01-4-24 14:19:36)
     * 
     * @return java.lang.String
     * @param templateName java.lang.String
     * @throws BusinessException
     * @throws ComponentException
     */
    public static PrintTemplateVO[] getTempletByModule(String moduleName, String pkCorp) throws BusinessException {
	return getPrintTemplateQry().getTempletByModule(moduleName, pkCorp);

//		// bq v55模板查找机制
//    	// 从模板分配表中找已分配的系统模板
//    	// 若找到：根据系统模板的nodecode找所有的模板
//    	// 若没找到：走旧机制，根据节点号找所有模板
//    	
//    	TemplateParaVO paraVo = new TemplateParaVO();
//        paraVo.setTemplateType(ITemplateStyle.printTemplate);       
//        paraVo.setFunNode(moduleName);
//        paraVo.setPk_orgUnit(nc.vo.sm.nodepower.OrgnizeTypeVO.COMPANY_TYPE);
//        
//        IPFTemplate name = (IPFTemplate) NCLocator.getInstance().lookup(IPFTemplate.class.getName());
//        String[] ids = name.getSingleTempIdAry(paraVo);
//    	
//        if(ids != null && ids.length > 0) {
//        	// 根据任意一个分配的模板，找到模板对应的节点号
//            IPrintTemplateQry templateQry = (IPrintTemplateQry) NCLocator.getInstance().lookup(IPrintTemplateQry.class.getName());
//            String[] sysids = new String[1];
//            sysids[0] = ids[0];
//    		PrintTempletmanageHeaderVO[] templateHeads = templateQry.queryHeaderVOByIDs(sysids);
//    		
//    		if(templateHeads != null && templateHeads.length > 0) {
//    			moduleName = templateHeads[0].getVnodecode();
//    		}
//    		
//    		if(templateHeads != null && templateHeads.length > 0)
//    			return templateQry.getTempletByModule(templateHeads[0].getVnodecode(), pkCorp);
//    		
//    		return null;
//        }
//        
//        return getPrintTemplateQry().getTempletByModule(moduleName, pkCorp);
    }

    public static PrintTemplateVO[] getTemplateByAppCode(String appcode, String pkCorp)
	    throws ComponentException, BusinessException {
	return getPrintTemplateQry().getTempletByAppCode(appcode, pkCorp);
    }

    /**
     * 向数据库中插入一个VO对象。
     *
     * 创建日期：(2001-4-20)
     * 
     * @param printTemplate nc.vo.pub.print.PrintTemplateVO
     * @return java.lang.String 所插入VO对象的主键字符串。
     * @throws BusinessException
     * @throws ComponentException
     */
    public static String insert(PrintVO printVo) throws BusinessException {
	return getPrintTemplateBS().insert(printVo);
    }

    /**
     * 向数据库中插入一个VO对象。
     */
    public static String[] insertVOS(PrintVO[] printVOS) throws BusinessException {
	return getPrintTemplateBS().insertPrintVOS(printVOS);
    }

    /**
     * 向数据库中插入一个VO对象。
     *
     * 创建日期：(2001-4-20)
     * 
     * @param printTemplate nc.vo.pub.print.PrintTemplateVO
     * @return java.lang.String 所插入VO对象的主键字符串。
     * @throws BusinessException
     * @throws ComponentException
     */
    public static String insertCard(PrintVO printVo) throws BusinessException {
	return getPrintTemplateBS().insertCard(printVo);
    }

    /**
     * 用VO对象的属性值更新数据库。
     *
     * 创建日期：(2001-4-20)
     * 
     * @param print nc.vo.pub.print.PrintVO
     * @throws BusinessException
     * @throws ComponentException
     */
    public static void update(PrintVO print) throws BusinessException {
	getPrintTemplateBS().update(print);
    }

    /**
     * 用VO对象的属性值更新数据库。
     *
     * 创建日期：(2001-4-20)
     * 
     * @param print nc.vo.pub.print.PrintVO
     * @throws BusinessException
     * @throws ComponentException
     */
    public static void updateCard(PrintVO print) throws BusinessException {
	getPrintTemplateBS().updateCard(print);
    }

    /**
     * 用templateName更新数据库。
     *
     * 创建日期：(2001-4-20)
     * 
     * @param templateID   String
     * @param templateCode String
     * @param templateName String
     * @throws BusinessException
     * @throws ComponentException
     */
    public static void updateTemplateName(String templateID, String templateCode, String templateName)
	    throws BusinessException {
	getPrintTemplateBS().updateTemplateName(templateID, templateCode, templateName);
    }

    /**
     * 校验并修改模板名称 创建日期：2015年1月19日
     * 
     * @param printTemplate
     * @throws BusinessException
     */
    public static void updateTemplateName(PrintTemplateVO printTemplate) throws BusinessException {
	getPrintTemplateBS().updateTemplateName(printTemplate);
    }

    /**
     * 通过主键获得VO对象。
     *
     * 创建日期：(2001-8-22)
     * 
     * @return nc.vo.pub.print.PrintTemplateVO
     * @param key String
     * @throws BusinessException
     * @throws ComponentException
     */
    public static PrintTemplateVO findByPrimaryKey(String key) throws BusinessException {
	return getPrintTemplateQry().findByPrimaryKey(key);
    }

    private static Map<String, Image> imageByFile = new ConcurrentHashMap<String, Image>();

    public static Image getSysImageFile(String fileName) throws Exception {
	Image image = imageByFile.get(fileName);
	if (image != null) {
	    return image;
	}

//    	String fullFileName = Constants.serverImagePath + "/" + fileName;
	String fullFileName = fileName;
//    	String dsName = ClientEnvironment.getInstance().getConfigAccount().getDataSourceName();

	ByteArrayOutputStream out1 = new ByteArrayOutputStream();
	if (RuntimeEnv.getInstance().isRunningInServer()) {
	    IFileSystemService service = NCLocator.getInstance().lookup(IFileSystemService.class);
	    service.downLoadFile(fullFileName, out1);
	    out1.flush();
	} else {
	    BufferedOutputStream out2 = new BufferedOutputStream(out1);
	    Class c = Class.forName("nc.ui.pub.filesystem.FileManageServletClient");
	    Method downloadFile = c.getMethod("downloadFile",
		    new Class[] { String.class, String.class, OutputStream.class });
	    String dsName = InvocationInfoProxy.getInstance().getUserDataSource();
	    downloadFile.invoke(c, new Object[] { dsName, fullFileName, out2 });
	    out2.flush();
	}

	ByteArrayInputStream in3 = new ByteArrayInputStream(out1.toByteArray());
	image = ImageIO.read(in3);
	if (image != null && fileName != null) {
	    imageByFile.put(fileName, image);
	}
	return image;
    }

    /**
     * 获得serverPath下的所有图标
     * 
     * @return
     * @throws BusinessException
     */
    public static String[] getSystemImages() throws BusinessException {
	/* 将公司的图片加作系统变量 */
	IFileSystemService fileManager = (IFileSystemService) NCLocator.getInstance()
		.lookup(IFileSystemService.class.getName());
	String[] images = null;
	try {
	    NCFileNode fileNode = fileManager.queryNCFileNodeTree(Constants.serverImagePath);

	    if (fileNode != null) {
		List<String> imagefiles = new ArrayList<String>();
		doChild(fileNode, fileNode.getFullPath(), imagefiles);

		images = imagefiles.toArray(new String[0]);
	    }
	} catch (BusiBeanException e) {
	    Logger.error(e.getMessage(), e);
	} catch (BusinessException e) {
	    Logger.error(e.getMessage(), e);
	}

	return images;
    }

    private static void doChild(NCFileNode node, String path, List<String> paths) {
	if (node.isFolder()) {
	    int fileCount = node.getChildCount();
	    for (int i = 0; i < fileCount; i++) {
		NCFileNode cnode = (NCFileNode) node.getChildAt(i);
		if (cnode.isFolder())
		    doChild(cnode, path + "/" + cnode.getName(), paths);
		else
		    paths.add(path + "/" + cnode.getName());
	    }
	}
    }

    /**
     * 向数据库中插入一批VO对象 创建日期：(2001-8-22)
     * 
     * @param printTemplate nc.vo.pub.print.PrintTemplateVO[]
     * @return java.lang.String[] 所插入VO对象数组的主键字符串数组。
     * @throws BusinessException
     * @throws ComponentException
     */
    public static String[] insertArray(PrintTemplateVO[] printTemplates) throws BusinessException {
	return getPrintTemplateBS().insertArray(printTemplates);
    }

    /**
     * 通过单位编码返回指定公司所有记录VO数组。如果单位编码为空返回所有记录。
     *
     * 创建日期：(2001-8-22)
     * 
     * @return nc.vo.pub.print.PrintTemplateVO[] 查到的VO对象数组
     * @param unitCode int
     * @throws BusinessException
     * @throws ComponentException
     */
    public static PrintTemplateVO[] queryAll(String unitCode) throws BusinessException {
	return getPrintTemplateQry().queryAll(unitCode);
    }

    /**
     * 此处插入方法说明。 创建日期：(2001-11-27 20:00:45)
     * 
     * @return nc.vo.pub.print.PrintTempletmanageHeaderVO[]
     * @param ids java.lang.String[]
     * @throws BusinessException
     * @throws ComponentException
     */
    public static PrintTempletmanageHeaderVO[] queryHeaderVOByIDs(String[] ids) throws BusinessException {
	return getPrintTemplateQry().queryHeaderVOByIDs(ids);
    }

    /////////////////////////////////////////
    public static void createPrintTempData(CreateItemVO[] printItems, CreatePrintParaVO paras)
	    throws BusinessException {
	getPrintTemplateBS().createPrintTempData(printItems, paras);
    }

    /**
     * 
     * @param printItems
     * @param paras
     * @return 主键
     * @throws BusinessException
     */
    public static String createMDPrintTempData(CreateItemVO[] printItems, CreatePrintParaVO paras)
	    throws BusinessException {
	return getPrintTemplateBS().createMDPrintTempData(printItems, paras);
    }

    /**
     * 通过节点编码获得ItemsVO对象。
     *
     * 创建日期：(2001-6-14)
     * 
     * @return nc.vo.pub.print.PrintTempletmanageVO
     * @param key String
     * @throws BusinessException
     * @throws ComponentException
     * @exception java.rmi.RemoteException 异常说明。
     */
    public static PrintTempletmanageItemVO[] findItemsForHeader(String nodeCode, String pkCorp)
	    throws BusinessException {
	return getPrintTemplateQry().findItemsForHeader(nodeCode, pkCorp);
    }

    ////////////////////////////////////////////////////// web端新增获取数据源变量的方法///////////////////////////////////////

    /**
     * 通过节点编码获得ItemsVO对象。
     *
     * 创建日期：(2001-6-14)
     * 
     * @return nc.vo.pub.print.PrintTempletmanageVO
     * @param key String
     * @throws Exception
     * @throws ComponentException
     * @exception java.rmi.RemoteException 异常说明。
     */
    public static PrintTempletmanageItemVO[] findItemsForHeaderByAppCode(String appcode, String pkCorp)
	    throws ComponentException, Exception {
	return getPrintTemplateQry().findItemsForHeaderByAppCode(appcode, pkCorp);
    }

    /////////////////////////////////// for hr
    /////////////////////////////////// bs//////////////////////////////////////

    /**
     * 批删除初始化数据[对外接口] （如果该字段未被已定义模板引用）删除了初始化字段，所有公司的用户已定义的模板的字段也被删除
     * 
     * @param key String
     * @throws BusinessException
     * @throws ComponentException
     */
    public static int deleteInitFields(PrintTempletmanageItemVO[] itemVOs, String corp) throws BusinessException {
	return getPrintTemplateBSForHR().deleteInitFields(itemVOs, corp);
    }

    /**
     * 向数据库中插入一个VO对象。
     *
     * 创建日期：(2001-6-14)
     * 
     * @param printTempletmanage nc.vo.pub.print.PrintTempletmanageVO
     * @return java.lang.String 所插入VO对象的主键字符串。
     * @throws BusinessException
     * @throws ComponentException
     */
    public static int insertInitFields(PrintTempletmanageItemVO[] itemVOs, String corp) throws BusinessException {
	return getPrintTemplateBSForHR().insertInitFields(itemVOs, corp);
    }

    /**
     * 批更新初始化数据[对外接口] 初始化数据做了修改，所有公司的用户已定义的模板也跟着修改
     * 
     * @param reportModel nc.vo.pub.report.ReportModelVO
     * @throws BusinessException
     * @throws ComponentException
     */
    public static int updateInitFields(PrintTempletmanageItemVO[] itemVOs, String corp) throws BusinessException {
	return getPrintTemplateBSForHR().updateInitFields(itemVOs, corp);
    }

    /**
     * 判断某项目是否已经被打印（/卡片）模板使用
     * 
     * @return nc.vo.pub.report.ReportModelVO[]
     * @throws BusinessException
     * @throws ComponentException
     * @exception java.sql.SQLException 异常说明。
     */
    public static boolean ifDeletingFldQuoted(String strNodeCode, String strTableCode, String itemExpress,
	    boolean isCard, String pkCorp) throws BusinessException {
	return getPrintTemplateBSForHR().ifDeletingFldQuoted(strNodeCode, strTableCode, itemExpress, isCard, pkCorp);
    }

    /**
     * 判断某初始化数据是否已存在[被对外接口方法调用] 打印模板要求：在一个节点下vvarexpress不允许重复·！￥·！#%·
     * 
     * @return boolean
     * @throws BusinessException
     * @throws ComponentException
     * @exception java.sql.SQLException 异常说明。
     */
    public static boolean ifItemExisted(String strNodeCode, String express, String pkCorp) throws BusinessException {
	return getPrintTemplateBSForHR().ifItemExisted(strNodeCode, express, pkCorp);
    }

    /**
     * 向数据库插入一个VO对象。
     *
     * 创建日期：(2001-6-14)
     * 
     * @param node nc.vo.pub.print.PrintTempletmanageItemVO
     * @throws BusinessException
     * @throws ComponentException
     * @exception java.sql.SQLException 异常说明。
     */
    public static String insertItem(PrintTempletmanageItemVO itemVO) throws BusinessException {
	return getPrintTemplateBSForHR().insertItem(itemVO);
    }

    public static PrintTemplateCreatorVO[] getSourceTemplates(String datasource, String condition)
	    throws BusinessException {
	return getPrintTemplateQry().getSourceTemplates(datasource, condition);
    }

    public static void delOldDefaulatPrintTemplates(PrintTemplateCreatorVO[] vos) throws BusinessException {
	getPrintTemplateBS().delOldDefaulatPrintTemplates(vos);
    }

//    public static void generatePrintTemplateData(PrintTemplateCreatorVO[] vos)
//		throws BusinessException{
//    	getPrintTemplateBS().generatePrintTemplateData(vos);
//    }
}
