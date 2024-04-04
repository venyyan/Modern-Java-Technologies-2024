package bg.sofia.uni.fmi.mjt.space.algorithm;

import bg.sofia.uni.fmi.mjt.space.exception.CipherException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class RijndaelTest {
    static SymmetricBlockCipher blockCipher;
    private static final int KEY_SIZE = 128;
    private static final String ENCRYPTION_ALGORITHM = "AES";
    private static SecretKey generateSecretKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(ENCRYPTION_ALGORITHM);
        keyGenerator.init(KEY_SIZE);
        SecretKey secretKey = keyGenerator.generateKey();

        String base64Key = Base64.getEncoder().encodeToString(secretKey.getEncoded());
        System.out.println("Generated Secret Key (Base64-encoded): " + base64Key);

        return secretKey;
    }
    @BeforeAll
    static void createRijndael() throws NoSuchAlgorithmException {
        blockCipher = new Rijndael(generateSecretKey());
    }
    @Test
    void testDecryptWithNullKey() {
        SymmetricBlockCipher cipher = new Rijndael(null);
        assertThrows(CipherException.class, () -> cipher.decrypt(new ByteArrayInputStream("".getBytes()), new ByteArrayOutputStream()));
    }

    @Test
    void testEncryptWithNullKey() {
        SymmetricBlockCipher cipher = new Rijndael(null);
        assertThrows(CipherException.class, () -> cipher.encrypt(new ByteArrayInputStream("".getBytes()), new ByteArrayOutputStream()));
    }

    @Test
    void testDecrypt() {
        String testString = "test string";
        byte[] encryptedBytes;
        try (ByteArrayOutputStream encrypted = new ByteArrayOutputStream()) {
            blockCipher.encrypt(new ByteArrayInputStream(testString.getBytes()), encrypted);
            encryptedBytes = encrypted.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (CipherException e) {
            throw new RuntimeException(e);
        }

        try (ByteArrayInputStream decryptedString = new ByteArrayInputStream(encryptedBytes);
             ByteArrayOutputStream toCheck = new ByteArrayOutputStream()) {
            blockCipher.decrypt(decryptedString, toCheck);

            String toCheckString = toCheck.toString();
            assertEquals(testString, toCheckString);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (CipherException e) {
            throw new RuntimeException(e);
        }
    }
}
