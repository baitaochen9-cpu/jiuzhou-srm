package nc.api.rest.ic.utils;

import nc.ui.querytemplate.operator.BetweenOperator;
import nc.ui.querytemplate.operator.EqOperator;
import nc.ui.querytemplate.operator.GtOperator;
import nc.ui.querytemplate.operator.LtOperator;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.scmpub.util.QuerySchemeBuilder;

import org.apache.commons.lang.StringUtils;

/**
 * @Description: 日期作为查询条件的格式解析   2018-04-03 12:34:34~2019-04-03 12:34:34
 *   
 * @author: 刘伟
 * @date:   2019-5-6 下午5:39:13   
 * @version NCC1909
 */
public class QuerySchemeUtils {

	public static QuerySchemeBuilder dealDateFieldBuilder(QuerySchemeBuilder builder,String field, String fieldValue){
		if(fieldValue != null && fieldValue.trim().length() > 0){
			String[] date = fieldValue.split("~");
			if(date.length == 1){
				if(fieldValue.contains("~")){
					builder.append(field, GtOperator.getInstance(), new UFDateTime[]{new UFDateTime(date[0].trim())});
				}else{
					builder.append(field, EqOperator.getInstance(), new UFDateTime[]{new UFDateTime(date[0].trim())});
				}
			}
			if(date.length == 2){
				if(!StringUtils.isEmpty(date[0].trim()) && !StringUtils.isEmpty(date[1].trim())){
					builder.append(field, BetweenOperator.getInstance(), new UFDateTime[]{new UFDateTime(date[0].trim()), new UFDateTime(date[1].trim())});
				}else if(StringUtils.isEmpty(date[0].trim())){
					builder.append(field, LtOperator.getInstance(), new UFDateTime[]{new UFDateTime(date[1].trim())});
				}else if(StringUtils.isEmpty(date[1].trim())){
					builder.append(field, GtOperator.getInstance(), new UFDateTime[]{new UFDateTime(date[0].trim())});
				}
			}
		}
		return builder;
	}
	
	public static QuerySchemeBuilder dealYearMonthBuilder(QuerySchemeBuilder builder, String field, String year, String month){
		if(StringUtils.isEmpty(year) || StringUtils.isEmpty(month)){
			return builder;
		}
		UFDate beginDate = UFDate.fromPersisted(year + "-" + month + "-01 00:00:00");
		int daysMonth = beginDate.getDaysMonth();
		UFDate endDate = UFDate.fromPersisted(year + "-" + month + "-" + daysMonth + " 23:59:59");
		builder.append(field, BetweenOperator.getInstance(), new UFDate[]{beginDate, endDate});
		return builder;
	}
}
