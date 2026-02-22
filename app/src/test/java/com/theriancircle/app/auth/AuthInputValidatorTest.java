package com.theriancircle.app.auth;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class AuthInputValidatorTest {

    @Test
    public void isEmailValid_returnsTrueForValidEmail() {
        assertTrue(AuthInputValidator.isEmailValid("therian@example.com"));
    }

    @Test
    public void isEmailValid_returnsFalseForInvalidEmail() {
        assertFalse(AuthInputValidator.isEmailValid("therianexample.com"));
    }

    @Test
    public void isPasswordValid_returnsFalseForShortPassword() {
        assertFalse(AuthInputValidator.isPasswordValid("12345"));
    }

    @Test
    public void deriveUsernameFromEmail_returnsLocalPart() {
        assertEquals("wolf.soul", AuthInputValidator.deriveUsernameFromEmail(
                "wolf.soul@example.com", "Fallback"));
    }
}
