package nccloud.web.ct.purdaily.utils;

import java.util.Map;

import nc.vo.pub.lang.UFDateTime;
import nc.vo.pubapp.pattern.model.entity.bill.IBill;
import nccloud.framework.core.exception.ExceptionUtils;
import nccloud.framework.core.reflect.Constructor;
import nccloud.framework.service.ServiceLocator;
import nccloud.pubitf.scmpub.pub.service.ISCMPubQueryService;
import nccloud.web.ct.pub.action.OperateInfo;

/**
 * @description 合并Ts工具
 * @author guozhq
 * @date 2018-8-8 下午9:24:31
 * @version ncc1.0
 */
public class CombinTsTool<T extends IBill> {

	private Class<T> clazz;

	public CombinTsTool(Class<T> clazz){
		this.clazz = clazz;
	}

	/**
	 *
	 * 执行查询后台原始VO,进行合并
	 * @param infos
	 * @return
	 *
	 */
	public T[] combinToOriginal(OperateInfo[] infos){
		if(infos == null || infos.length == 0){
			return null;
		}
		Map<String,UFDateTime> map = OperateInfo.convertToMap(infos);
		T[] bills = this.queryOriginal(map.keySet().toArray(new String[0]));
		if(bills == null || bills.length == 0){
			ExceptionUtils.wrapBusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("4004132_0","04004132-0004")/*@res "单据已经被删除，请刷新后重试!"*/);
		}
		for(IBill bill : bills){
			String id = bill.getParent().getPrimaryKey();
			UFDateTime currentTs = map.get(id);
			bill.getParent().setAttributeValue("ts", currentTs);
		}
		return bills;
	}

	/**
	 *
	 * 根据操作信息合并Ts
	 * @param infos
	 * @return
	 *
	 */
	public T[] combin(OperateInfo[] infos){
		if(infos == null || infos.length == 0){
			return null;
		}
		Map<String,UFDateTime> map = OperateInfo.convertToMap(infos);
		String[] ids = map.keySet().toArray(new String[0]);
		T[] bills = Constructor.construct(clazz, ids.length);
		for(int i = 0 ; i < bills.length ; i++){
			bills[i] = Constructor.construct(clazz);
			String id = bills[i].getParent().getPrimaryKey();
			UFDateTime currentTs = map.get(id);
			bills[i].getParent().setAttributeValue("ts", currentTs);
		}
		return bills;
	}

	/**
	 *
	 * 查询原始单据
	 * @param ids
	 * @return
	 *
	 */
	private T[] queryOriginal(String[] ids) {
		try {
			T[] bills = ServiceLocator.find(ISCMPubQueryService.class).billquery(clazz, ids);
			return bills;
		} catch (Exception e) {
			ExceptionUtils.wrapException(e);
		}
		return null;
	}

}