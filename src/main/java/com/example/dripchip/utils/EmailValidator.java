package com.example.dripchip.utils;

import java.util.regex.Pattern;

public class EmailValidator {
    private static final Pattern pattern;

    static {
        pattern = Pattern.compile("^(.+)@(\\S+)$");
    }

    public static boolean isValid(String email) {
        return pattern.matcher(email).matches();
    }
}