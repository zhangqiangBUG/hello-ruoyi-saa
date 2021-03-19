package com.zhangqiang.common.utils;

import com.zhangqiang.common.constant.Constants;
import org.apache.commons.lang3.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;
import java.security.MessageDigest;

/**
 * AES加密解密工具类
 * 
 */
public class AESUtil {

    private static final String defaultCharset = "utf-8";
    private static final String KEY_AES = "AES";
    private static final String TYPE_AES = "AES/ECB/NoPadding";//算法/模式/补码方式

    /**
     * 加密
     *
     * @param data
     *            需要加密的内容
     * @param key
     *            加密密码
     * @return
     */
    public static String encrypt(String data, String key) {
        return doAES(data, key, Cipher.ENCRYPT_MODE);
    }

    /**
     * 解密
     *
     * @param data
     *            待解密内容
     * @param key
     *            解密密钥
     * @return
     */
    public static String decrypt(String data, String key) {
        return doAES(data, key, Cipher.DECRYPT_MODE);
    }

    /**
     * 加解密
     *
     * @param data
     *            待处理数据
     * @param key
     *            密钥
     * @param mode
     *            加解密mode
     * @return
     */
    private static String doAES(String data, String key, int mode) {
        try {
            if (StringUtils.isBlank(data) || StringUtils.isBlank(key)) {
                return null;
            }
            // 判断是加密还是解密
            boolean encrypt = mode == Cipher.ENCRYPT_MODE;
            byte[] content;
            // true 加密内容 false 解密内容
            if (encrypt) {
                // 明文字符串加上#作为结束
                data = formatHexStr(data+Constants.AES_DATA_C);
                content = data.getBytes(defaultCharset);
            } else {
                content = parseHexStr2Byte(data);
            }
            // 加密key只能是16位
            String key_16 = getSHA1(key).substring(0,16).toUpperCase();
            byte[] enCodeFormat = key_16.getBytes(defaultCharset);
            // 根据字节数组生成AES密钥
            SecretKeySpec keySpec = new SecretKeySpec(enCodeFormat, KEY_AES);
            // 根据指定算法AES自成密码器
            Cipher cipher = Cipher.getInstance(TYPE_AES);// 创建密码器
            // 初始化密码器，第一个参数为加密(Encrypt_mode)或者解密解密(Decrypt_mode)操作，第二个参数为使用的KEY
            cipher.init(mode, keySpec);// 初始化
            byte[] result = cipher.doFinal(content);
            if (encrypt) {
                // 将二进制转换成16进制
                return parseByte2HexStr(result);
            } else {
                String mw = new String(result, defaultCharset);
                int w = mw.indexOf(Constants.AES_DATA_C);
                return w!=-1?mw.substring(0,w):mw;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * SHA 32位
     * @param str
     * @return
     */
    public static String getSHA1(String str){
        if(str==null||str.length()==0){
            return null;
        }
        char hexDigits[] = {'0','1','2','3','4','5','6','7','8','9',
                'a','b','c','d','e','f'};
        try {
            MessageDigest mdTemp = MessageDigest.getInstance("SHA1");
            mdTemp.update(str.getBytes("UTF-8"));

            byte[] md = mdTemp.digest();
            int j = md.length;
            char buf[] = new char[j*2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                buf[k++] = hexDigits[byte0 >>> 4 & 0xf];
                buf[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(buf);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 将二进制转换成16进制
     *
     * @param buf
     * @return
     */
    public static String parseByte2HexStr(byte buf[]) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

    /**
     * 将16进制转换为二进制
     *
     * @param hexStr
     * @return
     */
    public static byte[] parseHexStr2Byte(String hexStr) {
        if (hexStr.length() < 1) {
            return null;
        }
        byte[] result = new byte[hexStr.length() / 2];
        for (int i = 0; i < hexStr.length() / 2; i++) {
            int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }

    public static String formatHexStr(String str) {
        int len = str.getBytes(Charset.forName(defaultCharset)).length;
        if (len % 16 == 0) {
            return str;
        }
        // str.length()
        return str + String.format("%0" + (16 - len % 16) + "d", 0);
    }

}