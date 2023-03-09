package com.example.dripchip.validators;

import com.example.dripchip.entities.Account;

public class AccountValidator {
    public static boolean isInvalid(Account account) {
        return Validator.isBlankOrEmpty(account.getFirstName())
                || Validator.isBlankOrEmpty(account.getLastName())
                || Validator.isBlankOrEmpty(account.getFirstName())
                || Validator.isBlankOrEmpty(account.getPassword())
                || EmailValidator.isValid(account.getEmail());
    }
}
