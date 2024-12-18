package kz.beta.chatblock;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class BlockchainUtils {


    public static String generateChatID(String userID1, String userID2) {
        try {
            // Sort the user IDs to ensure the order doesn't matter
            String[] users = {userID1, userID2};
            Arrays.sort(users);

            // Combine the sorted user IDs into a single string
            String combined = users[0] + ":" + users[1];

            // Hash the combined string using SHA-256
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(combined.getBytes());

            // Convert the hash bytes to a hexadecimal string
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString(); // The unique chat ID
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error generating hash: " + e.getMessage());
        }
    }


    //-----
    private static final String ALGORITHM = "AES";

    private static SecretKeySpec getSecretKey(String secretKey) {
        // Ensure the key length is valid for AES (16, 24, 32 bytes)
        byte[] keyBytes = secretKey.getBytes();
        byte[] validKeyBytes = new byte[16]; // Use 16 bytes for 128-bit AES
        System.arraycopy(keyBytes, 0, validKeyBytes, 0, Math.min(keyBytes.length, validKeyBytes.length));
        return new SecretKeySpec(validKeyBytes, ALGORITHM);
    }


    public static String encrypt(String plainText, String secret) throws Exception {
        System.out.println("Secret: " + secret);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, getSecretKey(secret));
        byte[] encryptedBytes = cipher.doFinal(plainText.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    public static String decrypt(String encryptedText, String secret) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, getSecretKey(secret));
        byte[] decodedBytes = Base64.getDecoder().decode(encryptedText);
        byte[] decryptedBytes = cipher.doFinal(decodedBytes);
        return new String(decryptedBytes);
    }
}
