package nc.bs.jzyy.sys;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class Base64Utils {

	/**
	 * 将inputstream转为Base64
	 *
	 * @param is
	 * @return
	 * @throws Exception
	 */
	public static String getBase64FromInputStream(InputStream is) throws Exception {
		byte[] data = null;
		try {
			ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
			byte[] buff = new byte[100];
			int rc = 0;
			while ((rc = is.read(buff, 0, 100)) > 0) {
				swapStream.write(buff, 0, rc);
			}
			data = swapStream.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					throw new Exception("输入流关闭异常");
				}
			}
		}

		return encryptBASE64(data);
	}

	/**
	 * base64转inputStream
	 *
	 * @param base64string
	 * @return
	 */
	public static InputStream getInputStreamFromBase64(String base64string) {
		ByteArrayInputStream stream = null;
		try {
			byte[] bytes = decryptBASE64(base64string);
			stream = new ByteArrayInputStream(bytes);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return stream;
	}
	
    /**
     * BASE64Encoder 加密
     * 
     * @param data
     *            要加密的数据
     * @return 加密后的字符串
     */
    public static String encryptBASE64(byte[] data) {
         BASE64Encoder encoder = new BASE64Encoder();
         String encode = encoder.encode(data);
        return encode;
    }
    /**
     * BASE64Decoder 解密
     * 
     * @param data
     *            要解密的字符串
     * @return 解密后的byte[]
     * @throws Exception
     */
    public static byte[] decryptBASE64(String data) throws Exception {
         BASE64Decoder decoder = new BASE64Decoder();
         byte[] buffer = decoder.decodeBuffer(data);
        return buffer;
    }

}
