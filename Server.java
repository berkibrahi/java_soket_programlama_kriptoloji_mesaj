import java.io.*;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(12345);
            System.out.println("Sunucu başlatıldı.");

            Socket socket = serverSocket.accept();
            System.out.println("İstemci bağlandı.");

            // Diffie-Hellman anahtar değişimi
            DiffieHellmanKeyExchange keyExchange = new DiffieHellmanKeyExchange(
                    new BigInteger("23"), // Shared prime (p)
                    new BigInteger("5")   // Shared base (g)
            );

            BufferedWriter keyWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            keyWriter.write(keyExchange.encodePublicKey());
            keyWriter.newLine();
            keyWriter.flush();

            BufferedReader keyReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BigInteger clientPublicKey = keyExchange.decodePublicKey(keyReader.readLine());
            BigInteger sharedSecret = keyExchange.getSharedSecret(clientPublicKey);

            // Şifreleme ve çözme işlemleri için kullanılacak anahtarları oluştur
            CryptoUtils cryptoUtils = new CryptoUtils(sharedSecret.toString());

            // Şifreleme ve çözme işlemleri için Scanner, BufferedReader ve BufferedWriter kullan
            Scanner scanner = new Scanner(System.in);
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            while (true) {
                // İstemciden şifreli mesaj al
                String encryptedMessage = reader.readLine();

                // Çözülen mesajı ekrana yazdır
                String decryptedMessage = cryptoUtils.decrypt(encryptedMessage);
                 System.out.println("İstemciden Gelen şifrelenmiş Mesaj: " + encryptedMessage);

                System.out.println("İstemciden Gelen Mesaj: " + decryptedMessage);

                // Kullanıcıdan mesaj al
                System.out.print("Mesajınızı girin: ");
                String message = scanner.nextLine();

                // Girilen mesajı şifrele ve istemciye gönder
                String encryptedMessageToSend = cryptoUtils.encrypt(message);
                writer.write(encryptedMessageToSend);
                writer.newLine();
                writer.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
