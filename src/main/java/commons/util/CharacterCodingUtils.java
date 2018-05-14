package commons.util;

import java.io.UnsupportedEncodingException;

/**
 * 字符编码工具类
 * Created by wugaoshang on 2018/5/14.
 */
public class CharacterCodingUtils {

    public final static String UTF8 = "UTF-8";
    public final static String GBK = "GBK";


    /**
     * 将字符编码转换成GBK码
     */
    public static String toGBK(String str) throws UnsupportedEncodingException {
        return changeCharset(str, GBK);
    }


    /**
     * 将字符编码转换成UTF8码
     */
    public static String toUTF8(String str) throws UnsupportedEncodingException {
        return changeCharset(str, UTF8);
    }

    public static String toUTF8(Object str) throws UnsupportedEncodingException {
        return changeCharset((String) str, UTF8);
    }

    /**
     * 字符串编码转换的实现方法
     *
     * @param str        待转换编码的字符串
     * @param newCharset 目标编码
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String changeCharset(String str, String newCharset)
            throws UnsupportedEncodingException {
        if (str != null) {
            // 用默认字符编码解码字符串。
            byte[] bs = str.getBytes();
            // 用新的字符编码生成字符串
            return new String(bs, newCharset);
        }
        return null;
    }

    /**
     * 字符串编码转换的实现方法
     * 待转换编码的字符串
     *
     * @param oldCharset 原编码
     * @param newCharset 目标编码
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String changeCharset(String str, String oldCharset, String newCharset)
            throws UnsupportedEncodingException {
        if (str != null) {
            // 用旧的字符编码解码字符串。解码可能会出现异常。
            byte[] bs = str.getBytes(oldCharset);
            // 用新的字符编码生成字符串
            return new String(bs, newCharset);
        }
        return null;

    }

}
