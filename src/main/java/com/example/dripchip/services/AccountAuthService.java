package com.example.dripchip.services;

import com.example.dripchip.entity.Account;
import com.example.dripchip.security.AccountDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AccountAuthService {
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AccountAuthService(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    public UsernamePasswordAuthenticationToken authenticate(Account account) {
        AccountDetailsImpl accountDetails = new AccountDetailsImpl(account);

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(accountDetails.getUsername(), accountDetails.getPassword());

//        try {
//            authenticationManager.authenticate(authenticationToken);
//        } catch (BadCredentialsException e) {
//            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
//        }
//
        // посмотреть лекции что возвращать при аутентификации и куда
    }
}
