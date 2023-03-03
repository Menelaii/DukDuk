package com.example.dripchip.utils;

public class StringUtil {
    public static boolean isBlankOrEmpty(String str) {
        return  str == null || str.isEmpty() || str.trim().isEmpty();
    }
}
