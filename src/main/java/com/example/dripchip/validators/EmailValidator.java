package com.example.dripchip.validators;

import java.util.regex.Pattern;

public class EmailValidator {
    private static final Pattern pattern;

    static {
        pattern = Pattern.compile("^(.+)@(\\S+)$");
    }

    public static boolean isValid(String email) {
        return email != null && pattern.matcher(email).matches();
    }
}
