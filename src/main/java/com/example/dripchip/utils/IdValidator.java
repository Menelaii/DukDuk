package com.example.dripchip.utils;

import com.example.dripchip.exceptions.InvalidIdException;

public class IdValidator {

    public static void throwIfInvalid(Integer id) {
        if (id == null || id <= 0) {
            throw new InvalidIdException();
        }
    }

    public static void throwIfInvalid(Long id) {
        if (id == null || id <= 0) {
            throw new InvalidIdException();
        }
    }
}
