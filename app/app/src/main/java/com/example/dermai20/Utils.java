package com.example.dermai20;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class Utils {

    public static final String encryption_algo = "SHA-256";

    /**
     * This class turns a encrypts a String with a SHA-256 encryption.
     *
     * @param clear_text: clear text
     * @return encrypted_text: encrypted text
     */
    public static String encrypt_password(String clear_text){
        MessageDigest digest = null;
        String encrypted_text = null;
        try {
            digest = MessageDigest.getInstance(encryption_algo);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        byte[] hash = new byte[0];
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            hash = digest.digest(clear_text.getBytes(StandardCharsets.UTF_8));
        }
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            encrypted_text = Base64.getEncoder().encodeToString(hash);
        }
        return encrypted_text;
    }
}
