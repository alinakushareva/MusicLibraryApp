package test.security;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import main.security.PasswordUtil;

class PasswordUtilTest {

    // ================== TEST generateSalt() ================== //

    @Test
    void testGenerateSalt_ReturnsNonNull() {
        String salt = PasswordUtil.generateSalt();
        assertNotNull(salt, "Generated salt should not be null.");
    }

    @Test
    void testGenerateSalt_ReturnsBase64EncodedString() {
        String salt = PasswordUtil.generateSalt();
        assertDoesNotThrow(() -> java.util.Base64.getDecoder().decode(salt));
    }

    @Test
    void testGenerateSalt_ReturnsUniqueValues() {
        String salt1 = PasswordUtil.generateSalt();
        String salt2 = PasswordUtil.generateSalt();
        assertNotEquals(salt1, salt2);
    }

    // ================== TEST hashPassword() ================== //

    @Test
    void testHashPassword_ValidInput_ReturnsNonNull() {
        String salt = PasswordUtil.generateSalt();
        String hashedPassword = PasswordUtil.hashPassword("password123", salt);
        assertNotNull(hashedPassword);
    }

    @Test
    void testHashPassword_ValidInput_ReturnsBase64EncodedString() {
        String salt = PasswordUtil.generateSalt();
        String hashedPassword = PasswordUtil.hashPassword("password123", salt);
        assertDoesNotThrow(() -> java.util.Base64.getDecoder().decode(hashedPassword));
    }

    @Test
    void testHashPassword_SameInput_ReturnsSameHash() {
        String salt = PasswordUtil.generateSalt();
        String hashedPassword1 = PasswordUtil.hashPassword("password123", salt);
        String hashedPassword2 = PasswordUtil.hashPassword("password123", salt);
        assertEquals(hashedPassword1, hashedPassword2);
    }

    @Test
    void testHashPassword_DifferentSalts_ReturnsDifferentHashes() {
        String salt1 = PasswordUtil.generateSalt();
        String salt2 = PasswordUtil.generateSalt();
        String hashedPassword1 = PasswordUtil.hashPassword("password123", salt1);
        String hashedPassword2 = PasswordUtil.hashPassword("password123", salt2);
        assertNotEquals(hashedPassword1, hashedPassword2);
    }

    @Test
    void testHashPassword_NullPassword_ThrowsIllegalArgumentException() {
        String salt = PasswordUtil.generateSalt();
        assertThrows(IllegalArgumentException.class, () -> PasswordUtil.hashPassword(null, salt),
            "Hashing a null password should throw IllegalArgumentException.");
    }

    @Test
    void testHashPassword_EmptyPassword_ThrowsIllegalArgumentException() {
        String salt = PasswordUtil.generateSalt();
        assertThrows(IllegalArgumentException.class, () -> PasswordUtil.hashPassword("", salt),
            "Hashing an empty password should throw IllegalArgumentException.");
    }

    @Test
    void testHashPassword_NullSalt_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> PasswordUtil.hashPassword("password123", null),
            "Hashing with a null salt should throw IllegalArgumentException.");
    }

    @Test
    void testHashPassword_EmptySalt_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> PasswordUtil.hashPassword("password123", ""),
            "Hashing with an empty salt should throw IllegalArgumentException.");
    }

    // ================== TEST validatePassword() ================== //

    @Test
    void testValidatePassword_CorrectPassword_ReturnsTrue() {
        String salt = PasswordUtil.generateSalt();
        String hashedPassword = PasswordUtil.hashPassword("password123", salt);
        assertTrue(PasswordUtil.validatePassword("password123", salt, hashedPassword));
    }

    @Test
    void testValidatePassword_IncorrectPassword_ReturnsFalse() {
        String salt = PasswordUtil.generateSalt();
        String hashedPassword = PasswordUtil.hashPassword("password123", salt);
        assertFalse(PasswordUtil.validatePassword("wrongpassword", salt, hashedPassword));
    }

    @Test
    void testValidatePassword_NullPassword_ThrowsIllegalArgumentException() {
        String salt = PasswordUtil.generateSalt();
        String hashedPassword = PasswordUtil.hashPassword("password123", salt);
        assertThrows(IllegalArgumentException.class, () -> PasswordUtil.validatePassword(null, salt, hashedPassword),
            "Validating a null password should throw IllegalArgumentException.");
    }

    @Test
    void testValidatePassword_EmptyPassword_ThrowsIllegalArgumentException() {
        String salt = PasswordUtil.generateSalt();
        String hashedPassword = PasswordUtil.hashPassword("password123", salt);
        assertThrows(IllegalArgumentException.class, () -> PasswordUtil.validatePassword("", salt, hashedPassword),
            "Validating an empty password should throw IllegalArgumentException.");
    }

    @Test
    void testValidatePassword_NullSalt_ThrowsIllegalArgumentException() {
        String salt = PasswordUtil.generateSalt();
        String hashedPassword = PasswordUtil.hashPassword("password123", salt);
        assertThrows(IllegalArgumentException.class, () -> PasswordUtil.validatePassword("password123", null, hashedPassword),
            "Validating with a null salt should throw IllegalArgumentException.");
    }

    @Test
    void testValidatePassword_EmptySalt_ThrowsIllegalArgumentException() {
        String salt = PasswordUtil.generateSalt();
        String hashedPassword = PasswordUtil.hashPassword("password123", salt);
        assertThrows(IllegalArgumentException.class, () -> PasswordUtil.validatePassword("password123", "", hashedPassword),
            "Validating with an empty salt should throw IllegalArgumentException.");
    }

    @Test
    void testValidatePassword_NullHashedPassword_ThrowsIllegalArgumentException() {
        String salt = PasswordUtil.generateSalt();
        assertThrows(IllegalArgumentException.class, () -> PasswordUtil.validatePassword("password123", salt, null),
            "Validating with a null hashed password should throw IllegalArgumentException.");
    }

    @Test
    void testValidatePassword_EmptyHashedPassword_ThrowsIllegalArgumentException() {
        String salt = PasswordUtil.generateSalt();
        assertThrows(IllegalArgumentException.class, () -> PasswordUtil.validatePassword("password123", salt, ""),
            "Validating with an empty hashed password should throw IllegalArgumentException.");
    }
}