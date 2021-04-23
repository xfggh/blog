package com.xfggh.blog.util;

import com.xfggh.blog.BlogApplication;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashUtil {
    private static final Logger LOG = LoggerFactory.getLogger(BlogApplication.class);

    public static String getMD5Str(String str){
        byte[] digest = null;
        String md5Str = "";
        try {
            MessageDigest md5 = MessageDigest.getInstance("md5");
            digest  = md5.digest(str.getBytes("utf-8"));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //16是表示转换为16进制数
        md5Str = new BigInteger(1, digest).toString(16);
        return md5Str;
    }

    public static String getSHA1Str(String str){
        char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f' };
        String result;

        try{
            MessageDigest messageDigest = MessageDigest.getInstance("SHA1");
            messageDigest.update(str.getBytes("UTF-8"));
            byte[] bytes = messageDigest.digest();
            int j = bytes.length;
            char buf[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = bytes[i];
                buf[k++] = hexDigits[byte0 >>> 4 & 0xf];
                buf[k++] = hexDigits[byte0 & 0xf];
            }
            result = new String(buf);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
            result = "";
        }

        // 使用 Apache codes 获得 SHA1
        // String ss = new String(DigestUtils.sha1Hex(str));
        // LOG.info("ss:" + ss);
        // LOG.info("result:" + result);

        return result;
    }

    public static String getSHA256Str(String str){
        return new String(DigestUtils.sha256Hex(str));
    }

    public static String getSHA512Str(String str){
        return new String(DigestUtils.sha512Hex(str));
    }

}
