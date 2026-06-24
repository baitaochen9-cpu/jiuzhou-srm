package nc.bs.srm.pub;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import nc.bs.framework.common.RuntimeEnv;
import nc.vo.pub.lang.UFDate;

import org.apache.log4j.Appender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Layout;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

/**
 * @author cyz
 *
 */
public class MakeNcLog {

	/**????log4j?????
	 * @param logname ???????
	 * @param logdec ???????
	 * @return
	 */
	public static Logger setParam(String logname, String logdec) {

		String nchome=RuntimeEnv.getInstance().getNCHome();
		nchome=nchome.replace("\\", "/");
		if(!nchome.endsWith("/"))
			nchome += "/";
		// ???????
		String rq = new UFDate().toString(TimeZone.getDefault(),  new SimpleDateFormat("yyyy-MM-dd"));
		// ???¡¤??
		StringBuilder sb = new StringBuilder();
		sb.append(nchome);
		sb.append("nclogs/").append(logname);
		sb.append("/").append(logdec).append("/").append(rq).append(".log");
	 
			Logger logger = Logger.getLogger(logname); // ???????
			logger.setLevel(Level.ALL);// ??????
			Layout layout = new PatternLayout("------------------ %d{yyyy-MM-dd HH:mm:ss}[%p]%n%m%n");// ??????
			try {
				logger.removeAllAppenders();//???
				Appender appender = new FileAppender(layout, sb.toString());// ???¡¤??
				logger.addAppender(appender);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				//??????¡¤?????????,?????????????anony-log??
			}
			
			
			return logger;
	}

}
