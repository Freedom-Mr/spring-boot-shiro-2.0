package casia.isiteam.springbootshiro.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;

/**
 * Author wzy
 * Date 2017/7/31 19:10
 */
public class EncodeUtil {
    private static final Logger logger = LoggerFactory.getLogger(EncodeUtil.class);

    //可配置到Constant中，并读取配置文件注入,16位,自己定义
    private static final String KEY = "abcdef0123456789";
    //
    private static final String IV = "abcdef0123456789";
    //参数分别代表 算法名称/加密模式/数据填充方式
    private static final String ALGORITHMSTR = "AES/CBC/PKCS5Padding";

    /**
     * 加密返回的数据转换成 String 类型
     * @param content 明文
     * @return
     * @throws Exception
     */
    public static synchronized String encrypt(String content) throws HttpMessageNotReadableException {
        if( !Validator.check(content) ){ return content; }
        return base64ToString( encrypt(content.getBytes(), KEY.getBytes(), IV.getBytes()) );
    }
    private static synchronized byte[] encrypt(byte[] content, byte[] keyBytes, byte[] iv) throws HttpMessageNotReadableException{
        try {
            SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");
            Cipher cipher = Cipher.getInstance(ALGORITHMSTR);
            cipher.init(Cipher.ENCRYPT_MODE,key, new IvParameterSpec(iv));
            byte[] result = cipher.doFinal(content);
            return result;
        } catch (Exception e) {
            logger.error("加密：exception:{}",e.toString());
        }
        return null;
    }
    /**
     * 将解密返回的数据转换成 String 类型
     * @param content Base64编码的密文
     * @return
     * @throws Exception
     */
    public static synchronized String decrypt(String content) throws HttpMessageNotReadableException{
        byte[] rs = decrypt( content, KEY.getBytes(), IV.getBytes() );
        if( rs == null ){
            throw new HttpMessageNotReadableException("参数解析异常");
        }
        return new String(rs);
    }
    private static synchronized byte[] decrypt(String content, byte[] keyBytes, byte[] iv) throws HttpMessageNotReadableException{
        try {
            SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");
            Cipher cipher = Cipher.getInstance(ALGORITHMSTR);
            cipher.init(Cipher.DECRYPT_MODE,key, new IvParameterSpec(iv));
            byte[] result = cipher.doFinal( stringToBase64(content) );
            return result;
        } catch (NoSuchAlgorithmException e) {
            logger.error("解密：exception:{}", e.getMessage());
        } catch (Exception e) {
            logger.error("解密：exception:{}", e.getMessage());
        }
        return null;
    }

    /**
     * 字符串装换成 Base64
     */

    public static byte[] stringToBase64(String key) throws HttpMessageNotReadableException {
        return Base64.decodeBase64(key.getBytes());
    }

    /**
     * Base64装换成字符串
     */
    public static String base64ToString(byte[] key) throws HttpMessageNotReadableException {
        return new Base64().encodeToString(key);
    }
}
