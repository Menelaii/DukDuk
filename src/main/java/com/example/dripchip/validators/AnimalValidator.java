package com.example.dripchip.validators;

import com.example.dripchip.dto.AnimalShortDTO;

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

    public static boolean isValid(AnimalShortDTO animal) {
        return Validator.isNotNullAndGreaterThanZero(animal.getHeight())
                && Validator.isNotNullAndGreaterThanZero(animal.getLength())
                && Validator.isNotNullAndGreaterThanZero(animal.getWeight())
                && Validator.isNotNullAndGreaterThanZero(animal.getChipperId())
                && Validator.isNotNullAndGreaterThanZero(animal.getChippingLocationId())
                && Validator.isNotNullOrEmpty(animal.getAnimalTypes())
                && !Validator.hasNullElements(animal.getAnimalTypes())
                && isValidGender(animal.getGender())
                && isValidLifeStatus(animal.getLifeStatus());
    }
}
