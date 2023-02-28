package com.example.dripchip.services;

import com.example.dripchip.entity.Account;
import com.example.dripchip.exceptions.EntityNotFoundException;
import com.example.dripchip.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class AccountsService {
    private AccountRepository repository;

    @Autowired
    public AccountsService(AccountRepository repository) {
        this.repository = repository;
    }

    public Account get(int id) {
        Optional<Account> account = repository.findById(id);

        if(account.isEmpty()) {
            throw new EntityNotFoundException();
        }

        return account.get();
    }
}
