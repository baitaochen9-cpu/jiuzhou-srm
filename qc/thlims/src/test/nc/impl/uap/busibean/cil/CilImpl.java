/*
 * 创建日期 2005-12-28
 *
 * License的服务类.
 */
package nc.impl.uap.busibean.cil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import nc.bs.framework.common.RuntimeEnv;
import nc.bs.logging.Logger;
import nc.encry.decode.Decode;
import nc.itf.uap.cil.ICilService;
import nc.lic.LicenseControl;
import nc.modulemap.toolkit.Toolkit;

/**
 * @author fangj
 * 
 */
public class CilImpl implements ICilService {
	private static final String platformCode = "0000IUAPRT";
	private List<String> cpuLicList = null;
	private List<String> getCpuLicList() {
		if(cpuLicList == null){
			String ncHome = RuntimeEnv.getInstance().getNCHome();
			File file = new File(ncHome, "ierp/sf/cpuLic.dat");
			if(file.exists()){
				try {
					byte[] fileBytes = readFileContent(file);
					byte[] codes = Decode.decode(fileBytes);
					String str = new String(codes, "UTF-8").trim();
					String[] splits = str.split(",");
					cpuLicList =  new ArrayList<String>();
					for (int i = 0; i < splits.length; i++) {
						String code = splits[i].trim();
						if(code.length() > 0){
							cpuLicList.add(code);
						}
					}
				} catch (Exception e) {
					Logger.error(e.getMessage(), e);
				}
				
			}
			if(cpuLicList == null){
				cpuLicList =  new ArrayList<String>();
				cpuLicList.add("1221");
				cpuLicList.add("1227");//portal模块号由E001改为1227
				cpuLicList.add("8001");
				
				//03 数据处理平台
				cpuLicList.add("0313");//
				cpuLicList.add("0315");
				//05商业分析平台
				cpuLicList.add("0504");//
				cpuLicList.add("0506");
				cpuLicList.add("0508");
			}
		
		}
		return cpuLicList;
	}
	private List<String> freeList = null;
	private List<String> getFreeList(){
		if(freeList == null){
			String ncHome = RuntimeEnv.getInstance().getNCHome();
			File file = new File(ncHome, "ierp/sf/freeLic.dat");
			if(file.exists()){
				
				try {
					byte[] fileBytes = readFileContent(file);
					byte[] codes = Decode.decode(fileBytes);
					String str = new String(codes, "UTF-8").trim();
					String[] splits = str.split(",");
					freeList =  new ArrayList<String>();
					for (int i = 0; i < splits.length; i++) {
						String code = splits[i].trim();
						if(code.length() > 0){
							freeList.add(code);
						}
					}
				} catch (Exception e) {
					Logger.error(e.getMessage(), e);
				}
				
			}
			if(freeList == null){
				freeList = new ArrayList<String>();
				freeList.add("1000");
				freeList.add("E052");
				freeList.add("1010");
				freeList.add("1012");
				freeList.add("1020");
				freeList.add("8004");
				freeList.add("1223");
				freeList.add("1099");
			}
		}
		return freeList; 
	}
	private List<String> mustoccupyModuleList = null;
	private List<String> getMustOccupyModuleList(){
		if(mustoccupyModuleList == null){
			String ncHome = RuntimeEnv.getInstance().getNCHome();
			File file = new File(ncHome, "ierp/sf/mustOccupyModule.dat");
			if(file.exists()){
				try {
					byte[] fileBytes = readFileContent(file);
					byte[] codes = Decode.decode(fileBytes);
					String str = new String(codes, "UTF-8").trim();
					String[] splits = str.split(",");
					mustoccupyModuleList =  new ArrayList<String>();
					for (int i = 0; i < splits.length; i++) {
						String code = splits[i].trim();
						if(code.length() > 0){
							mustoccupyModuleList.add(code);
						}
					}
				} catch (Exception e) {
					Logger.error(e.getMessage(), e);
				}
				
			}
			if(mustoccupyModuleList == null){
				mustoccupyModuleList = new ArrayList<String>();
				mustoccupyModuleList.add("EC40");
				mustoccupyModuleList.add("EC22");
			}
		}
		return mustoccupyModuleList;
	}
	private static byte[] readFileContent(File file) throws Exception{
		FileInputStream fis = null;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			fis = new FileInputStream(file);
			byte[] buf = new byte[1024];
			int len = -1;
			while ((len = fis.read(buf))!=-1) {
				baos.write(buf, 0, len);
			}
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
			throw e;
		} finally{
			if(fis != null){
				try {
					fis.close();
				} catch (IOException e) {}
			}
		}
		return baos.toByteArray();
		
	}
	private Map<String, String> getDefaultUserOccupyProductMap(){
		Map<String, String> map = new HashMap<String, String>();
		map.put("1004", "1090");
		map.put("1010", "1090");
		map.put("1012", "1090");
		map.put("1014", "1090");
		map.put("1016", "1090");
		map.put("1017", "1090");
		map.put("1018", "1090");
		map.put("1020", "1090");
		map.put("1305", "1090");
		map.put("1312", "1090");
		map.put("1315", "1090");
		map.put("1317", "1090");
		map.put("1420", "1090");
		map.put("1418", "1090");
		map.put("1412", "1090");
		map.put("2000", "1090");
		map.put("3601", "1090");
		map.put("4001", "1090");
		map.put("4501", "1090");
		map.put("4801", "1090");
		map.put("6001", "1090");
		map.put("G001", "1090");
		
		//1890
		map.put("1830", "1890");
		map.put("1835", "1890");
		//2090
		map.put("2002", "2090");
		map.put("2012", "2090");
		map.put("2006", "2090");
		map.put("2008", "2090");
		//2091
		map.put("2011", "2091");
		map.put("2020", "2091");
		//3690
		map.put("3605", "3690");
		map.put("3607", "3690");
		map.put("3610", "3690");
		map.put("3612", "3690");
		map.put("3618", "3690");
		map.put("3650", "3690");
		map.put("3617", "3690");
		map.put("3613", "3690");
		map.put("3614", "3690");
		map.put("3615", "3690");
		map.put("3616", "3690");
		// 3691
		map.put("3630", "3691");
		map.put("3632", "3691");
		map.put("3634", "3691");
		map.put("3635", "3691");
		map.put("3637", "3691");
		//4090
		map.put("4020", "4090");
		map.put("4003", "4090");
		map.put("4004", "4090");
		map.put("4005", "4090");
		map.put("4012", "4090");
		map.put("4007", "4090");
		map.put("4008", "4090");
		map.put("2014", "4090");
		map.put("C010", "4090");
		//4091
		map.put("4006", "4091");
		map.put("4030", "4091");
		map.put("4032", "4091");
		map.put("4009", "4091");
		map.put("4014", "4091");
		//4590
		map.put("4510", "4590");
		map.put("4520", "4590");
		map.put("4530", "4590");
		map.put("4580", "4590");
		//4591
		map.put("4540", "4591");
		map.put("4550", "4591");
		map.put("4560", "4591");
		map.put("4583", "4591");
		map.put("4585", "4591");
		//4890
		map.put("4810", "4890");
		map.put("4820", "4890");

		//6090
		map.put("6005", "6090");
		map.put("6007", "6090");
		map.put("6009", "6090");
		map.put("6011", "6090");
		map.put("6055", "6090");
		map.put("6013", "6090");
		//8090
		map.put("8002", "8090");
		map.put("8011", "8090");
		map.put("8013", "8090");

		//5090
		map.put("5001", "5090");
		map.put("5002", "5090");
		map.put("5003", "5090");
		map.put("5004", "5090");
		map.put("5006", "5090");
		map.put("5007", "5090");
		map.put("5008", "5090");
		//
		map.put("EC30", "EC91");
		map.put("EC50", "EC91");
		
		//G099
		map.put("G011", "G099");
		map.put("G040", "G099");
		map.put("G050", "G099");
		map.put("G060", "G099");
	
		//E190
		map.put("E110", "E190");
		//E290
		map.put("E202", "E290");
		map.put("E204", "E290");
		//E390
		map.put("E001", "E390");
		map.put("E320", "E390");
		map.put("E322", "E390");
		map.put("E324", "E390");
		map.put("E326", "E390");
		map.put("E328", "E390");
		map.put("E330", "E390");
		map.put("E332", "E390");
		map.put("E334", "E390");
		return map;
	}
	private Map<String, String> userOccupyProductMap = null;
	private Map<String, String> uapToNCProductMap = null;
	@SuppressWarnings("unchecked")
	private Map<String, String> getUserOccupyProductMap() {
		if(userOccupyProductMap == null){
//			userOccupyProductMap = null;
			String ncHome = RuntimeEnv.getInstance().getNCHome();
			File root = new File(ncHome, "ierp/sf/userOccupyModuleMap");
			File[] childs = root.listFiles(new FileFilter(){
				@Override
				public boolean accept(File file) {
					return file.isFile() && file.getName().toLowerCase().endsWith(".uomm");
				}
				
			});
			int count = childs == null ? 0 : childs.length;
			userOccupyProductMap = new HashMap<String, String>();
			for(int i = 0 ; i < count; i++){
				try {
					File file = childs[i];
					Properties prop = Toolkit.loadUserOccupyModuleMap(file);
					Enumeration keys = prop.keys();
					while (keys.hasMoreElements()) {
						String key = (String) keys.nextElement();
						String value = prop.getProperty(key);
						userOccupyProductMap.put(key, value);
						
					}
				} catch (Exception e) {
					Logger.error(e.getMessage(), e);
				}
			}
			if(userOccupyProductMap.isEmpty()){
				userOccupyProductMap = getDefaultUserOccupyProductMap();
			}
		}
		return userOccupyProductMap;
	}
	@SuppressWarnings("unchecked")
	private Map<String, String> getUapToNcProductMap() { 
		if(uapToNCProductMap == null){
			uapToNCProductMap = new HashMap<String, String>();
			String ncHome = RuntimeEnv.getInstance().getNCHome();
			File root = new File(ncHome, "ierp/sf/concurrentModuleMap");
			if(root.exists()){
				File[] childs = root.listFiles(new FileFilter(){
					@Override
					public boolean accept(File file) {
						return file.isFile() && file.getName().toLowerCase().endsWith(".conmm");
					}
					
				});
				int count = childs == null ? 0 : childs.length;
				for(int i = 0 ; i < count; i++){
					try {
						File file = childs[i];
						Properties prop = Toolkit.loadUserOccupyModuleMap(file);
						Enumeration keys = prop.keys();
						while (keys.hasMoreElements()) {
							String key = (String) keys.nextElement();
							String value = prop.getProperty(key);
							uapToNCProductMap.put(key, value);
							
						}
					} catch (Exception e) {
						Logger.error(e.getMessage(), e);
					}
				}
			}
		}
		return uapToNCProductMap;
	}
	public boolean isUserConcurrentNumberChecker(){
		return getLicType() != 1;
	}
	private Integer licType = null;
	public Integer getLicType(){
		if(licType == null){
			if(isNCDEMO() || !containsProduct("NCLICTYPE")){
				licType = 0;
			}else{ 
				licType =LicenseControl.getInstance().getInt("NCLICTYPE");// getLicenseCount("NCLICTYPE");
			}
		}
		return licType;
	}
	/*
	 * （非 Javadoc）
	 * 
	 * @see nc.itf.uap.license.ILicenseService#getProductLicense(java.lang.String)
	 */
	public int getProductLicense(String product) {
		//add by wsl 2017年1月9日13:33:54
		if(true){
			return Integer.MAX_VALUE;
		}
		//add end
		if(isDevelopMode()){
			return 10;
		}
//		if("1000".equals(product)){
		if(getFreeList().contains(product.toUpperCase())){
			return Integer.MAX_VALUE;
		}
		if(isNonLicenseProduct(product)){
			return Integer.MAX_VALUE;
		}
		boolean contains = LicenseControl.getInstance().getAllProductCodes().contains(product);
		if(contains){
			if(!isUserConcurrentNumberChecker()){
				// 下列两个模块，在用户注册模式下，其占用数按照运行平台数算，其授权数等于平台授权数
				if("1390".equals(product) || "1090".equals(product)){
					return getPlatformLicenseCount();
				}
				
			}
			
			int value = LicenseControl.getInstance().getInt(product);
			
			//如果是cpulist里的模块，其lic数为cpu数，并且不受权限控制，所以liccount设置为最大值。
			if(getCpuLicList().contains(product)){
				return value > 0 ? Integer.MAX_VALUE : -1;
			}else if(value == 1 && product.length() == 4){
				value = LicenseControl.getInstance().getInt(product.substring(0,2));
			}
			return value;
		}else if(LicenseControl.getInstance().isDemo()){
			if("corp".equalsIgnoreCase(product)){
				return 500;
			}else{
				return LicenseControl.getInstance().getInt("*");
			}
		}else{
			return -1;
		}
	}
	
	/*
	 * （非 Javadoc）
	 * 
	 * @see nc.itf.uap.license.ILicenseService#getProductCode(java.lang.String)
	 */
	public String getProductCode(String moduleCode) {
		if(isDevelopMode()){
			return moduleCode;
		}
		if(isUserConcurrentNumberChecker()){
			return getConcurrentProductCode(moduleCode);
		}else{
			return getOccupyProductCode(moduleCode);
		}
	}
	private String getOccupyProductCode(String moduleCode) {
		if(moduleCode!= null && moduleCode.length() >= 4){
			String code = moduleCode.substring(0, 4);
			if(getUserOccupyProductMap().containsKey(code)){
				code= getUserOccupyProductMap().get(code);
			}
			return getConcurrentProductCode0(code);
		}else{
			return moduleCode;
		}
	}

	public String getConcurrentProductCode(String moduleCode) {
//		if("1000".equals(moduleCode)){
//			return "1000";
//		}
		if(getUapToNcProductMap().containsKey(moduleCode)){
			int value = LicenseControl.getInstance().getInt(moduleCode);
			if(value > 0){
				moduleCode = getUapToNcProductMap().get(moduleCode);
			}
		}
		return getConcurrentProductCode0(moduleCode);
	}
	private String getConcurrentProductCode0(String moduleCode) {
		if(isNonLicenseProduct(moduleCode)){
			return moduleCode.substring(0, 4);
		}
		if(getCpuLicList().contains(moduleCode)){
			return moduleCode;
		}
		if(getFreeList().contains(moduleCode)){
			return moduleCode;
		}
		String code = moduleCode;
//		if(code.startsWith("10")){
//			return "10";
//		}
		while (code.length() > 4) {
			int value = LicenseControl.getInstance().getInt(code);
			if(value > 0){
				return code;
			}
			code = code.substring(0, code.length() - 2);
		}
		if(code.length() == 4){
			int value = LicenseControl.getInstance().getInt(code);
			if(value == 1){
				return code.substring(0, 2);
			}else {
				return code;
			}
		}
		if(code.length() >= 2){
			return code.substring(0, 2);
		}else{
			return code;
		}
	}
	/*
	 * （非 Javadoc）
	 * 
	 * @see nc.itf.uap.license.ILicenseService#isUsedGLBook()
	 */
	public boolean isUsedGLBook() {
			if(isNCDEMO() || isDevelopMode()){
				return true;
			}else{
				return  LicenseControl.getInstance().getInt("1099") > 0;
			}

//		return CesCilCtl.getInstance().isUsedGLBook();
	}



	public HashMap<String, Integer> getAllProducts() {
//		return CesCilCtl.getInstance().getAllProducts();
		Set<String> set = LicenseControl.getInstance().getAllProductCodes();
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		Iterator<String> iter =set.iterator();
		while (iter.hasNext()) {
			String code = (String) iter.next();
			int value = LicenseControl.getInstance().getInt(code);
			map.put(code, value);
			
		}
		return map;
	}

	@Override
	public boolean containsProduct(String product) {
		return LicenseControl.getInstance().getAllProductCodes().contains(product);
	}
	@Override
	public int getLicenseCount(String code) {
		if(isDevelopMode()){
			return 10;
		}
		return LicenseControl.getInstance().getInt(code);
	}
	private boolean isNonLicenseProduct(String code){
//		boolean b = false;
//		if(code!=null && code.length() >=4){
//			String str = code.toUpperCase();
//			char ch = str.charAt(2);
//			b = ch=='H'||ch=='L'||ch=='R'||ch=='J'||ch=='K';
//		}
//		return b;
		return true;
	}

	@Override
	public boolean isUserOccupyModule(String productCode) {
		boolean isMustOccupyModule = false;
		String upperCode = productCode.toUpperCase();
		for (int i = 0; i < getMustOccupyModuleList().size(); i++) {
			String str = getMustOccupyModuleList().get(i);
			if(upperCode.startsWith(str)){
				isMustOccupyModule = true;
				break;
			}
		}
		return isMustOccupyModule;

	}

	@Override
	public String[] getProductSN() {
		return LicenseControl.getInstance().getProductSN();
	}
	private boolean isDevelopMode(){
		return true;
	}
	/*
	 * （非 Javadoc）
	 * 
	 * @see nc.itf.uap.license.ILicenseService#isNCDEMO()
	 */
	public boolean isNCDEMO() {
		return LicenseControl.getInstance().isDemo();

	}
	@Override
	public int getPlatformLicenseCount() {
		int val = 0;
		if(isDevelopMode() || isNCDEMO()){
			val = Integer.MAX_VALUE;
		}else{
			val =getLicenseCount(platformCode);
		}
		return val;
	}
	@Override
	public boolean existLicCount(String arg0) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public Date getEndDate() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public int getModuleLicense(String arg0) {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public HashMap<String, Integer> getModulesLicense(Map<String, String> arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public HashMap<String, String> getProductCodes(Map<String, String> arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public HashMap<String, Integer> getProductsLicense(Map<String, String> arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public boolean isExpired() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean verify() {
		// TODO Auto-generated method stub
		return false;
	}
}