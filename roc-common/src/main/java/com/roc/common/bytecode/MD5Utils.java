package com.roc.common.bytecode;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author jelly.wang
 */
public class MD5Utils {
    private static final Logger LOGGER = LoggerFactory.getLogger(MD5Utils.class);
    protected static char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    protected static MessageDigest messagedigest;

    static {
        try {
            messagedigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException nsaex) {
            LOGGER.error(MD5Utils.class.getName() + "init MessageDigest failed ,no support MD5Util", nsaex);
        }
    }

    private MD5Utils() {

    }

    public static String getMD5(String code) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            byte[] bytes = code.getBytes();
            byte[] results = messageDigest.digest(bytes);
            StringBuilder stringBuilder = new StringBuilder();
            for (byte result : results) {
                stringBuilder.append(String.format("%02x", result));
            }
            return stringBuilder.toString();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return StringUtils.EMPTY;
        }
    }

    public static String get16MD5(String code) {
        String md5 = getMD5(code);
        if (StringUtils.isEmpty(md5)) return StringUtils.EMPTY;
        return md5.substring(8, 24);
    }


    private static String bufferToHex(byte[] bytes) {
        return bufferToHex(bytes, 0, bytes.length);
    }

    private static String bufferToHex(byte[] bytes, int m, int n) {
        StringBuilder stringBuilder = new StringBuilder(2 * n);
        int k = m + n;
        for (int l = m; l < k; l++) {
            appendHexPair(bytes[l], stringBuilder);
        }
        return stringBuilder.toString();
    }

    private static void appendHexPair(byte bt, StringBuilder stringBuilder) {
        char c0 = hexDigits[(bt & 0xf0) >> 4];
        char c1 = hexDigits[bt & 0xf];
        stringBuilder.append(c0);
        stringBuilder.append(c1);
    }

    public static synchronized String getMD5String(byte[] bytes) {
        return bufferToHex(messagedigest.digest(bytes));
    }
}