package com.example.dripchip.utils;

import java.util.Set;

public class AnimalValidator {
    private static final Set<String> LIFE_STATUSES = Set.of("ALIVE", "DEAD");
    private static final Set<String> GENDERS = Set.of("MALE", "FEMALE", "OTHER");

    public static boolean isValidLifeStatus(String str) {
        return LIFE_STATUSES.contains(str);
    }

    public static boolean isValidGender(String str) {
        return GENDERS.contains(str);
    }
}
