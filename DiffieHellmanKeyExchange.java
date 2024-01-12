import java.math.BigInteger;
import java.security.*;
import java.util.Base64;

public class DiffieHellmanKeyExchange {
    private BigInteger p; // Shared prime
    private BigInteger g; // Shared base
    private BigInteger privateKey; // Private key
    private BigInteger publicKey; // Public key

    public DiffieHellmanKeyExchange(BigInteger p, BigInteger g) {
        this.p = p;
        this.g = g;

        // Generate private key
        privateKey = new BigInteger(2048, new SecureRandom());
        // Calculate public key
        publicKey = g.modPow(privateKey, p);
    }

    public BigInteger getPublicKey() {
        return publicKey;
    }

    public BigInteger getSharedSecret(BigInteger otherPublicKey) {
        return otherPublicKey.modPow(privateKey, p);
    }

    public String encodePublicKey() {
        return Base64.getEncoder().encodeToString(publicKey.toByteArray());
    }

    public BigInteger decodePublicKey(String encodedKey) {
        byte[] keyBytes = Base64.getDecoder().decode(encodedKey);
        return new BigInteger(keyBytes);
    }
}
