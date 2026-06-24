package nccloud.vo.excel.scheme;


import nccloud.vo.excel.pub.ExcelConstants;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

/**
 * Boolean类型数据的转换器，将Boolean类型数据的true/flase转换成PfxxConstants.YES/PfxxConstants.NO<br>
 * 主要是为了兼容以前的版本<br>
 * 
 * @author 
 * 
 */
public class BooleanConverter implements Converter {

	@Override
	public boolean canConvert(Class type) {
		return Boolean.class.isAssignableFrom(type);
	}

	@Override
	public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
		boolean flag = (Boolean) source;
		writer.setValue(flag ? ExcelConstants.YES : ExcelConstants.NO);
	}

	@Override
	public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
		return ExcelConstants.YES.equals(reader.getValue()) ? true : false;
	}

}