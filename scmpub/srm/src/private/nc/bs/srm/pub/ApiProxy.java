 package nc.bs.srm.pub;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.lang3.StringUtils;

import nc.bs.logging.Log;
import nc.vo.pub.BusinessException;


public class ApiProxy {

    public static String httpPost(String uri,String requestBody) throws Exception {
//    	Logger.error(String.format("ApiProxy-requestUrl:%s,requestBody:%s", uri,requestBody));
    	Log.getInstance("jd-ncc").error(String.format("ApiProxy-requestUrl:%s,requestBody:%s", uri,requestBody));
    	if(StringUtils.isEmpty(uri)) {
    		throw new BusinessException("接口地址不能为空");
    	}
    	if(StringUtils.isEmpty(requestBody)) {
    		throw new BusinessException("请求参数不能为空");
    	}
        HttpURLConnection connection = null;
        
        OutputStream dataout = null;
        BufferedReader reader = null;
        String line = null;
        String resultStr="";
        try {
            URL url = new URL(uri);
            connection = (HttpURLConnection) url.openConnection();// 根据URL生成HttpURLConnection
            connection.setDoOutput(true);// 设置是否向connection输出，因为这个是post请求，参数要放在http正文内，因此需要设为true,默认情况下是false
            connection.setDoInput(true); // 设置是否从connection读入，默认情况下是true;
            connection.setRequestMethod("POST");// 设置请求方式为post,默认GET请求
            connection.setUseCaches(false);// post请求不能使用缓存设为false
            connection.setConnectTimeout(10000);// 连接主机的超时时间
            connection.setReadTimeout(10000);// 从主机读取数据的超时时间
            connection.setInstanceFollowRedirects(true);// 设置该HttpURLConnection实例是否自动执行重定向
            connection.setRequestProperty("connection", "Keep-Alive");// 连接复用
            connection.setRequestProperty("charset", "UTF-8");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.connect();// 建立TCP连接,getOutputStream会隐含的进行connect,所以此处可以不要
            dataout = new DataOutputStream(connection.getOutputStream());// 创建输入输出流,用于往连接里面输出携带的参数

            dataout.write(requestBody.getBytes("UTF-8"));
            dataout.flush();
            dataout.close();
            int code = connection.getResponseCode();
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));// 发送http请求
                StringBuilder result = new StringBuilder();
                // 循环读取流
                while ((line = reader.readLine()) != null) {
                    result.append(line).append(System.getProperty("line.separator"));//
                }
                   resultStr=result.toString();
            }else {
                throw new BusinessException("异常:"+code);
            }
//            Logger.error(String.format("ApiProxy-responseCode:%s,responsResult:%s", code,resultStr));
        } catch (IOException e) {
            Log.getInstance("jd-ncc").error("接口同步失败:"+e);
            e.printStackTrace();
            throw e;
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            connection.disconnect();
        }
        Log.getInstance("jd-ncc").error(String.format("ApiProxy-uri:%s,responsResult:%s", uri,resultStr));
        return resultStr;
    }
    public static String getTimestamp(){
        String timestamp = String.valueOf(System.currentTimeMillis()/1000);
        return timestamp;
    }

    public static String encrypt(String str, String type) throws NoSuchAlgorithmException {
        /*
         * MD5、SHA1、SHA-256
         * */
        MessageDigest md = MessageDigest.getInstance(type);

        /*
         * update方法负责加密
         * 字符串转字节数组：str.getBytes("编码格式")
         * */
        md.update(str.getBytes());
        /*
         *获取摘要结果，加密后的数组
         * */
        byte[] bs = md.digest();

        /*
         * 变为16进制，使用字符串进行拼接
         * */
        StringBuilder res = new StringBuilder();
        for (byte b : bs) {
            res.append(String.format("%02X", b));
        }
        return res.toString();
    }


}
