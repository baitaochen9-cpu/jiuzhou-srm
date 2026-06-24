package nccloud.bs.excel.scheme;

import nc.bs.framework.core.util.ObjectCreator;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.ExtendedAggregatedValueObject;
import nc.vo.trade.pub.IExAggVO;
import nccloud.vo.excel.scheme.BillDefination;

/**
 * Created by IntelliJ IDEA. User: cch Date: 2004-11-8 Time: 9:56:10 根据单据类型或表名生成校验文件.
 */
public class SchemeCreator implements ISchemeCreator {

	private String moduleName;

	private ISchemeCreator m_creator = null;

	private String baseClassName;

	/**
	 * 通过单据类型构建校验文件
	 *
	 * @param account
	 * @param billType
	 */
	public SchemeCreator(String baseClassName) {
		this.baseClassName = baseClassName;
	}

	private ISchemeCreator getSchemeCreator() {
		if (m_creator == null) {
			Object baseClassObject = ObjectCreator.newInstance(moduleName, baseClassName);
			if (baseClassObject instanceof AggregatedValueObject || baseClassObject instanceof IExAggVO
					|| baseClassObject instanceof ExtendedAggregatedValueObject) {
				throw new UnsupportedOperationException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("pfxx","0pfxx0063")/*@res "暂时不支持聚合VO"*/);
				// m_creator = new AggVOSchemeCreator(moduleName, baseClassName);
			}
			m_creator = new BdVOSchemeCreator(moduleName, baseClassName);
		}
		return m_creator;
	}

	/**
	 * 创建校验文件
	 *
	 * @return
	 */
	@Override
	public BillDefination generate() throws BusinessException {
		return getSchemeCreator().generate();
	}
}