package com.neuro.util;

import org.mindrot.jbcrypt.BCrypt;
import java.security.MessageDigest;
public class PasswordUtil {

    // 🔐 Hash password
    public static String hash(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    // 🔍 Verify password
    public static boolean verify(String password, String hashedPassword) {
        return BCrypt.checkpw(password, hashedPassword);
    }
    public static String sha256(String password) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hash = md.digest(password.getBytes("UTF-8"));

        StringBuilder hex = new StringBuilder();

        for (byte b : hash) {
            String s = Integer.toHexString(0xff & b);
            if (s.length() == 1) hex.append('0');
            hex.append(s);
        }

        return hex.toString();
    }
}