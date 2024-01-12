import java.io.*;
import java.math.BigInteger;
import java.net.Socket;
import java.util.Scanner;


public class Client {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 12345);
            System.out.println("Sunucuya bağlandı.");

            // Diffie-Hellman anahtar değişimi
            DiffieHellmanKeyExchange keyExchange = new DiffieHellmanKeyExchange(
                    new BigInteger("23"), // Shared prime (p)
                    new BigInteger("5")   // Shared base (g)
            );

            BufferedReader keyReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BigInteger serverPublicKey = keyExchange.decodePublicKey(keyReader.readLine());

            BufferedWriter keyWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            keyWriter.write(keyExchange.encodePublicKey());
            keyWriter.newLine();
            keyWriter.flush();

            BigInteger sharedSecret = keyExchange.getSharedSecret(serverPublicKey);

            // Şifreleme ve çözme işlemleri için kullanılacak anahtarları oluştur
            CryptoUtils cryptoUtils = new CryptoUtils(sharedSecret.toString());

            // Şifreleme ve çözme işlemleri için Scanner, BufferedReader ve BufferedWriter kullan
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            Scanner scanner = new Scanner(System.in);

            while (true) {
                // Kullanıcıdan mesaj al
                System.out.print("Mesajınızı girin: ");
                String message = scanner.nextLine();

                // Girilen mesajı şifrele ve sunucuya gönder
                String encryptedMessageToSend = cryptoUtils.encrypt(message);
                writer.write(encryptedMessageToSend);
                writer.newLine();
                writer.flush();

                // Sunucudan şifreli mesaj al
                String encryptedMessage = reader.readLine();

                // Çözülen mesajı ekrana yazdır
                String decryptedMessage = cryptoUtils.decrypt(encryptedMessage);
                System.out.println("Sunucudan Gelen şifrelenmiş  Mesaj: " + encryptedMessage);
                System.out.println("Sunucudan Gelen Mesaj: " + decryptedMessage);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
