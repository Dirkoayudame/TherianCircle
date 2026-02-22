package com.theriancircle.app.auth;

import java.util.regex.Pattern;

public final class AuthInputValidator {
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    private AuthInputValidator() {
    }

    public static boolean isEmailValid(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }

    public static boolean isPasswordValid(String password) {
        return password != null && password.length() >= 6;
    }

    public static String deriveUsernameFromEmail(String email, String fallback) {
        if (email == null || email.trim().isEmpty() || !email.contains("@")) {
            return fallback;
        }
        String localPart = email.substring(0, email.indexOf('@')).trim();
        return localPart.isEmpty() ? fallback : localPart;
    }
}
