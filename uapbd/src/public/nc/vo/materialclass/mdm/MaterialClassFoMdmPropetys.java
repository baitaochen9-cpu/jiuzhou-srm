package nc.vo.materialclass.mdm;

import java.util.HashMap;
import java.util.Map;


public class MaterialClassFoMdmPropetys {

	public static final String[] mdmpropetys = {"id" ,
	    "code" ,
	    "mnecode",
	    "name",
	    "pk_defdoc",
	    "stu#name" ,
	    "c" };
	
	public static final Map<String, String> materialFoMdmPropetyMap = new HashMap<String,String>(){
		{
			put("id", "pk_defdoc") ;  
		put("mnecode", "mnecode ");//助记码
		put("code","code");/*编码*/
		put("name","name");//名称
		put("pk_defdoc","pk_defdoc");//档案ID
		put("stu#name","enablestate");//状态名称
		put("supcode#code","pid");/*上级编码*/
		put("dr","dr");
		}
	};
}


