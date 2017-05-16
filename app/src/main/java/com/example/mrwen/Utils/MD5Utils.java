package com.example.mrwen.Utils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by fate on 2017/1/30.
 */

public class MD5Utils {
    public static String Encode(String unencryted){
        unencryted = addSalt(unencryted);
        String encrypted = "";
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] digest = md5.digest(unencryted.getBytes());
            encrypted = new BigInteger(1,digest).toString(16);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return encrypted;
    }
    public static String addSalt(String origin){
        return "fate!7"+origin+"destiny!13";
    }
}
