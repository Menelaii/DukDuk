package com.example.dripchip.security;

import com.example.dripchip.entity.Account;
import com.example.dripchip.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountDetailsServiceImpl implements UserDetailsService {
    private final AccountRepository repository;

    @Autowired
    public AccountDetailsServiceImpl(AccountRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Account> account = repository.getByEmail(username);

        if(account.isEmpty()) {
            throw new UsernameNotFoundException("account not found");
        }

        return new AccountDetailsImpl(account.get());
    }
}
