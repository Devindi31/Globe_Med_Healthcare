package Test;

import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class Test {

    private static final String KEY = "1234567890123456";

    public static void main(String[] args) {
        try {
            System.out.println(decrypt(""));
            
            System.out.println(encrypt(""));
            
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    public static String decrypt(String encryptedPassword) throws Exception {
        SecretKeySpec secretKey = new SecretKeySpec(KEY.getBytes("UTF-8"), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decodedBytes = Base64.getDecoder().decode(encryptedPassword);
        byte[] decryptedBytes = cipher.doFinal(decodedBytes);
        return new String(decryptedBytes, "UTF-8");
    }

    public static String encrypt(String password) throws Exception {
        SecretKeySpec secretKey = new SecretKeySpec(KEY.getBytes("UTF-8"), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedBytes = cipher.doFinal(password.getBytes("UTF-8"));
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

}
