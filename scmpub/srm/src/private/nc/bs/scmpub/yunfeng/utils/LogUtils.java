package nc.bs.scmpub.yunfeng.utils;

import nc.bs.logging.Log;
/**
 * 日志输出到指定文件中
 * @author htf
 *
 */
public class LogUtils {
    private static Log LOG = Log.getInstance("GWLog");
    
    public void sendTolog(String logs) {
       LOG.info(logs);
    }

}