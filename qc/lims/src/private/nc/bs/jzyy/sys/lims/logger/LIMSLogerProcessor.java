package nc.bs.jzyy.sys.lims.logger;

import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.jdbc.framework.SQLParameter;
import nc.vo.pub.SuperVO;

public class LIMSLogerProcessor {

	public void deal(Object o,Map<String, Object> param) throws DAOException {
		// TODO Auto-generated method stub
		if (o instanceof SuperVO) {
			SuperVO loggvo = (SuperVO) o;
			new BaseDAO().insertVO(loggvo);
		}else{
			String sql = (String) o;
			if(param !=null){
				SQLParameter parameter = (SQLParameter) param.get("parameter");
				new BaseDAO().executeUpdate(sql,parameter);
			}else{
				new BaseDAO().executeUpdate(sql);

			}
			
		}
	}

}
