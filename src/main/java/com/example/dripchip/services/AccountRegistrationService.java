package com.example.dripchip.services;

import com.example.dripchip.entity.Account;
import com.example.dripchip.repositories.AccountRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@org.springframework.transaction.annotation.Transactional(readOnly = true)
public class AccountRegistrationService {
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AccountRegistrationService(AccountRepository accountRepository, PasswordEncoder passwordEncoder) {
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public Account register(Account account) {
        String encodedPassword = passwordEncoder.encode(account.getPassword());
        account.setPassword(encodedPassword);

        return accountRepository.save(account);
    }

    public boolean isEmailAlreadyTaken(String accountEmail) {
        return accountRepository.getByEmail(accountEmail).isEmpty();
    }
}
