/**
 * MD5 Generator
 *
 * @author Wen Xin
 */
package com.merlionportal.utility;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Generator {

    public static String hash(String key) {
        String result = "";
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            byte[] bytes = messageDigest.digest(key.getBytes());
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            result = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return result;
    }
}
