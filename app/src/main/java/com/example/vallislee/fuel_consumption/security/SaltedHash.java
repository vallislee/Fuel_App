package com.example.vallislee.fuel_consumption.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class SaltedHash {

    public static String generateHash(String S, String algorithm, byte[] salt) {
        String saltedhashPw = null;
        try {
            MessageDigest digest = MessageDigest.getInstance(algorithm);
            digest.reset();
            digest.update(salt);
            byte[] bytes = digest.digest(S.getBytes());
            StringBuilder sb = new StringBuilder();
            for (int i=0; i<bytes.length;i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            saltedhashPw = sb.toString();
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return saltedhashPw;
    }

    public static byte[] generateSalt() {
        byte[] bytes = new byte[20];
        SecureRandom random = new SecureRandom();
        random.nextBytes(bytes);
        return bytes;
    }
}
