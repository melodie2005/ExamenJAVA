package utils;
import java.security.MessageDigest;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;

public class SecurityUtils {
    public static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[]
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}

