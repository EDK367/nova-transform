package org.nova.util;

import jakarta.enterprise.context.ApplicationScoped;

import java.security.SecureRandom;

@ApplicationScoped
public class PasswordGenerator {

    private static final int LENGTH = 8;
    private static final String UPPER = "ABCDEFGHJKLMNPQRSTUVWXYZ";
    private static final String LOWER = "abcdefghijkmnpqrstuvwxyz";
    private static final String DIGITS = "23456789";
    private static final String ALL_CHARS = UPPER + LOWER + DIGITS;

    private final SecureRandom random = new SecureRandom();

    public String generate() {
        char[] password = new char[LENGTH];

        password[0] = UPPER.charAt(random.nextInt(UPPER.length()));
        password[1] = LOWER.charAt(random.nextInt(LOWER.length()));
        password[2] = DIGITS.charAt(random.nextInt(DIGITS.length()));

        for (int i = 3; i < LENGTH; i++) {
            password[i] = ALL_CHARS.charAt(random.nextInt(ALL_CHARS.length()));
        }

        for (int i = LENGTH - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            char temp = password[i];
            password[i] = password[j];
            password[j] = temp;
        }

        return new String(password);
    }
}
