package com.example.dripchip.utils;

import com.example.dripchip.entities.Account;

public class AccountValidator {
    public static boolean isInvalid(Account account) {
        return StringUtil.isBlankOrEmpty(account.getFirstName())
                || StringUtil.isBlankOrEmpty(account.getLastName())
                || StringUtil.isBlankOrEmpty(account.getFirstName())
                || StringUtil.isBlankOrEmpty(account.getPassword())
                || EmailValidator.isValid(account.getEmail());
    }
}
