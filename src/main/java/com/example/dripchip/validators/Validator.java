package com.example.dripchip.validators;

import com.example.dripchip.exceptions.InvalidIdException;

import java.util.HashSet;
import java.util.Set;

public class Validator {
    public static <T extends Number & Comparable<T>> boolean isNotNullAndGreaterThanZero(T number) {
        return number != null && number.doubleValue() > 0;
    }

    public static <T extends Number> boolean isNotNullOrEmpty(T[] array) {
        return array == null || array.length == 0;
    }

    public static <T> boolean hasDuplicates(T[] zipcodelist) {
        Set<T> lump = new HashSet<>();
        for (T i : zipcodelist) {
            if (lump.contains(i)) return true;
            lump.add(i);
        }
        return false;
    }

    public static <T> boolean hasNullElements(T[] list) {
        for (T i : list) {
            if (i == null) return true;
        }
        return false;
    }

    public static <T extends Number & Comparable<T>> void throwIfInvalidId(T id) {
        if (!Validator.isNotNullAndGreaterThanZero(id)) {
            throw new InvalidIdException();
        }
    }

    public static boolean isBlankOrEmpty(String str) {
        return  str == null || str.isEmpty() || str.trim().isEmpty();
    }
}
