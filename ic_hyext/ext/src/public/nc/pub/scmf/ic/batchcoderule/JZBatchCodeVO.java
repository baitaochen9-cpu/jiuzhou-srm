package nc.pub.scmf.ic.batchcoderule;

import java.util.HashMap;
import java.util.Map;

import nc.impl.pubapp.pattern.data.vo.VOQuery;
import nc.vo.bd.material.MaterialVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import net.sf.json.JSONObject;

import org.mozilla.javascript.edu.emory.mathcs.backport.java.util.Arrays;

/**
 * 
 * @author 20210414 zhian.ye
 *
 */
public class JZBatchCodeVO extends SuperVO{
 
	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return "jz_vbatchnumber";
	}
	@Override
	public String getPKFieldName(){
		return "id";
		
	}
	
	public static String ID="id";
	public static String PK_ORG="pk_org";
	public static String MATERIAL ="material";
	public static String MATERIALCODE = "materialcode";
	public static String CODERULE = "coderule";
	public static String ATZ ="atz";
	public static String NUM="num";
	public static String DR ="dr";
	public static String TS ="ts";
	public static final String[] strList = {"A","B","C","D","E","F","G","H","J","K","L","M","N","P","Q","R","S","T","U","V","W","X","Y","Z"}; /*递增范围，*/
	public static String Default_str = "default_str";
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPk_org() {
		return pk_org;
	}
	public void setPk_org(String pk_org) {
		this.pk_org = pk_org;
	}
	public String getMaterial() {
		return material;
	}
	public void setMaterial(String material) {
		this.material = material;
	}
	public String getMaterialcode() {
		return materialcode;
	}
	public void setMaterialcode(String materialcode) {
		this.materialcode = materialcode;
	}
	public String getCoderule() {
		return coderule;
	}
	public void setCoderule(String coderule) {
		this.coderule = coderule;
	}
	public String getAtz() {
		return atz;
	}
	public void setAtz(String atz) {
		this.atz = atz;
	}
	public String getNum() {
		return num;
	}
	public void setNum(String num) {
		this.num = num;
	}


	@Override
	public String toString() {
		return "JZBatchCodeVO [id=" + id + ", pk_org=" + pk_org + ", material="
				+ material + ", materialcode=" + materialcode + ", coderule="
				+ coderule + ", atz=" + atz + ", num=" + num + ", dr=" + dr
				+ "]";
	}

	private String id;
	private String pk_org;
	private String material;
	private String materialcode;
	private String coderule;
	private String atz;
	private String num;
	private Integer dr;
	// 20230202 zhian.ye 中山项目实施 对物料增加批次号默认字符串 ，些字符器只参与批次组装，不参与流水持久化
	private String default_str; 
	public Integer getDr() {
		return dr;
	}
	public UFDateTime  getTs() {
		return ts;
	}
	public void setDr(Integer dr) {
		this.dr = dr;
	}
	public void setTs(UFDateTime  ts) {
		this.ts = ts;
	}

	private UFDateTime  ts;
	
	/**
	 * 获取当前的批次号
	 * @throws BusinessException 
	 *      直接通过字符解析组合而成当前的批次号
	 */
	Map<String,Integer> ord = new HashMap<String,Integer> ();
	public String getThisCode() throws BusinessException {
		Map<String, Map<String,Integer>> parseRule = parseRule();
		assemble_ord(parseRule);
//		String code = "";
		for(String key : parseRule.keySet()){
			Map<String, Integer> map2 = parseRule.get(key);
			for(String key2 :map2.keySet()){
//				ord.put( key2,new Integer(key));
				String keyUpper = (String) this.getAttributeValue(key2); /*字段值*/
				Integer integer = map2.get(key2);/*位数*/
				//判断位数
				while (keyUpper.length() < integer) {
					keyUpper = "0"+keyUpper;	
				}
				String substring = keyUpper.substring(keyUpper.length()-integer , integer);
				this.setAttributeValue(key2, substring);
			}
		}
		String assemble = assemble();
		return assemble;
		

	}
	
	/**
	 * 获取下一个批次号
	 * 
	 *     根据当前数据判断是否需要进行进给，当前维度中只有ATZ/NUM 是进给数据
	 * @throws BusinessException 
	 */
	public String getNextCode() throws BusinessException{
		Map<String, Map<String,Integer>> parseRule = parseRule();
		Integer num = new Integer(this.getNum()); /*当前流水*/

		 assemble_ord(parseRule);
		for(String key : parseRule.keySet()){
			Map<String,Integer> map2 = parseRule.get(key);
			
			for(String key2 : map2.keySet()){
//				ord.put(key2, new Integer(key));
				if(key2.equals(MATERIALCODE)){//物料号直接取位数，从右边截取
					Integer integer = map2.get(key2);
					String substring = this.getMaterialcode().substring(getMaterialcode().length() - integer, integer);
					while (substring.length() < integer) { // 如果位数不够时候，+0补全
						substring = "0"+ substring;										
					}
					this.setMaterialcode(substring);
				}else if(key2.equals(ATZ) ){
					Integer integer = map2.get(key2); /*字符长度~~1*/
					char[] charArray = this.getAtz().toCharArray(); //当前字符串
					
					if(isNumMax(parseRule) == 1 ){
						for( int i = integer - 1 ; i >= 0 ; i--){
							String c = String.valueOf(charArray[i]); 
							if(c.equals("Z")){
								charArray[i] = "A".charAt(0);
							}else {
								for(int j = 0; j < strList.length  ; ++j){
									if(strList[j].equals(c)){
										charArray[i] = strList[j+1].charAt(0);
										break;
									}
								}
								/******zhian.ye 20210413 注意：当前以上逻辑只支持一位字母的变动，规则设置的时候不可改的字母位数，如果后续会有这一块的变更，需要重新更改计算逻辑！********************/
							}
						}
						this.setAtz(String.valueOf(charArray));
						
					}
					
				}else if(key2.equals(NUM)){
					Integer integer2 = map2.get(NUM);/*获取流水号长度  ，最大流水=N*9*/
					Integer nextNum = num+1;
					String newNum = "";
					if(isNumMax(parseRule)==1){
						
						String string = "1";
						while (string.length() < integer2) {
							string = "0"+string;
						}
						newNum= string;
						
					} else {
						String string = nextNum.toString();
						while (string.length() < integer2) {
							string = "0"+string;
							
						}
						newNum= string;
						
					}

					this.setNum(newNum);
				}
			}
		}
		String assemble = assemble();
		return assemble;
		
	}
	/**
	 * 检验当前数值范围
	 *  如果如果已超出 返回 ：1 ，如果符合范围 返回 ：0，如果需要退位时 返回 -1
	 * @return
	 */
	private Integer isNumMax(Map<String, Map<String,Integer>> parseRule ){
		Integer integer2 =parseRule.get(ord.get(NUM).toString()).get(NUM);/*获取流水号长度  ，最大流水=N*9*/
		String max = "9"; //999
		String min = "1";//000
		for(int i =0; i< integer2-1 ;i++){
			max="9"+max;
			min = "0"+min;
		}
		if(this.getNum().equals(max) ){
			return 1;
			}
		else if(this.getNum().equals(min)){
				return -1;
			}else{
				return 0 ;
			}
		
	}
	 /**
	  *组装字段排序
	  * @param parseRule
	  * @return
	  */
	private void assemble_ord(Map<String, Map<String, Integer>> parseRule) {
		for(String key : parseRule.keySet()){
			Map<String, Integer> map2 = parseRule.get(key);
			for(String key2 : map2.keySet()){
				ord.put(key2, new Integer(key));
			}
		}
	}
	/**
	 * 数据组装
	 */
	private String assemble() {
		// TODO Auto-generated method stub
		 String[] newcode = new String[ord.size()];
		for(String key :ord.keySet()){
			Integer integer = ord.get(key);
			newcode[integer-1] = (String) this.getAttributeValue(key);
		}
		String string = Arrays.toString(newcode);
		string =  string.replace(",", "");
		string =  string.replace("[", "");
		string =  string.replace("]", "");
		string = string.replace(" ", "");
		return string;
	}
	/**
	 * 规则解析
	 * @return
	 * @throws BusinessException
	 */
	private Map<String, Map<String,Integer>> parseRule() throws BusinessException {

		if(coderule == null || "".equals(coderule)){
			return null;
		}
		JSONObject obj = JSONObject.fromObject(coderule);
	
		return obj;


	}
	
	/**
	 * zhian.ye 通过物料ID来获取编码
	 * 
	 * @return
	 */
	public String getMaterialCodeByID(String pk_material) {
		VOQuery<MaterialVO> query = new VOQuery<>(MaterialVO.class);
		String wheresql = "and bd_material.pk_material = '" + pk_material + "'";
		MaterialVO[] query2 = query.query(wheresql, null);
		if (null == query2 || query2.length == 0) {
			return null;
		}
		// 有的话也应该只有一个；
		return query2[0].getCode();
	}

}
