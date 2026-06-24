package nc.itf.fa.info;

import nc.pub.smart.data.DataSet;
import nc.vo.pub.BusinessException;

import com.ufida.dataset.IContext;
/**
 * 
 * @author zhian.ye
 *
 */
public interface DepreciationForecastItf {
	public DataSet forecastForMonth(IContext context , nc.itf.fa.info.MyParam param) throws  BusinessException;
}
