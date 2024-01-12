import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class CryptoUtils {
    private SecretKeySpec secretKey;
    private Cipher cipher;

    public CryptoUtils(String secret) throws Exception {
        byte[] key = new byte[16];
        byte[] input = secret.getBytes("UTF-8");

        // Anahtar boyutunu kontrol et ve eksikse sıfırlar ile doldur
        for (int i = 0; i < input.length && i < key.length; i++) {
            key[i] = input[i];
        }

        secretKey = new SecretKeySpec(key, "AES");
        cipher = Cipher.getInstance("AES");
    }

    public String encrypt(String message) throws Exception {
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedBytes = cipher.doFinal(message.getBytes("UTF-8"));
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    public String decrypt(String encryptedMessage) throws Exception {
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] encryptedBytes = Base64.getDecoder().decode(encryptedMessage);
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
        return new String(decryptedBytes, "UTF-8");
    }
}
