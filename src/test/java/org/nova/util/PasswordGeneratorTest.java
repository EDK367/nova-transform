package org.nova.util;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class PasswordGeneratorTest {

    @Inject
    PasswordGenerator passwordGenerator;

    @Test
    void testPasswordGenerationRules() {
        for (int i = 0; i < 100; i++) {
            String password = passwordGenerator.generate();
            assertNotNull(password);
            assertEquals(8, password.length());
            assertTrue(password.matches(".*[A-Za-z].*"));
            assertTrue(password.matches(".*[0-9].*"));
            assertFalse(password.contains("0"));
            assertFalse(password.contains("O"));
            assertFalse(password.contains("o"));
            assertFalse(password.contains("1"));
            assertFalse(password.contains("l"));
            assertFalse(password.contains("I"));
        }
    }
}
