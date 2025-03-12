/**
 * Name: Alina Kushareva and Lindsay Davis
 * Class: CSC335 Spring 2025
 * Project: MusicLibraryApp
 * File: PasswordUtil.java
 * Purpose: Provides utility methods for securely handling passwords,
 *          including generating random salts, hashing passwords using 
 *          PBKDF2 with HMAC-SHA1, and validating password hashes.
 */
package main.security;

import java.security.SecureRandom;
import java.security.spec.KeySpec;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.util.Base64;

public class PasswordUtil {
    private static final int SALT_LENGTH = 16; // Salt length in bytes
    private static final int ITERATIONS = 65536; // Number of iterations for PBKDF2
    private static final int KEY_LENGTH = 128; // Key length in bits for PBKDF2

    /**
     * Generates a random salt for password hashing.
     * 
     * @return Base64 encoded salt.
     */
    public static String generateSalt() {
        byte[] salt = new byte[SALT_LENGTH];
        new SecureRandom().nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    /**
     * Hashes the password using PBKDF2 with HMAC-SHA1.
     * 
     * @param password The plaintext password.
     * @param salt The salt used for hashing.
     * @return Base64 encoded hashed password.
     * @throws IllegalArgumentException If password or salt is null or empty.
     * @throws RuntimeException If hashing fails.
     */
    public static String hashPassword(String password, String salt) {
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty.");
        }
        if (salt == null || salt.trim().isEmpty()) {
            throw new IllegalArgumentException("Salt cannot be null or empty.");
        }

        try {
            byte[] saltBytes = Base64.getDecoder().decode(salt);
            KeySpec spec = new PBEKeySpec(password.toCharArray(), saltBytes, ITERATIONS, KEY_LENGTH);
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            byte[] hash = factory.generateSecret(spec).getEncoded();
            return Base64.getEncoder().encodeToString(hash);
        } catch (Exception e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }

    /**
     * Validates a given password against a stored hash and salt.
     * 
     * @param password The plaintext password to validate.
     * @param salt The salt used for hashing.
     * @param hashedPassword The stored hashed password.
     * @return True if the password matches the stored hash, false otherwise.
     */
    public static boolean validatePassword(String password, String salt, String hashedPassword) {
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty.");
        }
        if (salt == null || salt.trim().isEmpty()) {
            throw new IllegalArgumentException("Salt cannot be null or empty.");
        }
        if (hashedPassword == null || hashedPassword.trim().isEmpty()) {
            throw new IllegalArgumentException("Hashed password cannot be null or empty.");
        }

        String inputHash = hashPassword(password, salt);
        return inputHash.equals(hashedPassword);
    }
}