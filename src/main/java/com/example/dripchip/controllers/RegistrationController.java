package com.example.dripchip.controllers;

import com.example.dripchip.dto.AccountDTO;
import com.example.dripchip.dto.AccountRegDTO;
import com.example.dripchip.entities.Account;
import com.example.dripchip.services.AccountsService;
import com.example.dripchip.validators.AccountValidator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegistrationController {
    private final AccountsService service;
    private final ModelMapper modelMapper;

    @Autowired
    public RegistrationController(AccountsService service, ModelMapper modelMapper) {
        this.service = service;
        this.modelMapper = modelMapper;
    }

    @PostMapping("/registration")
    public ResponseEntity<AccountDTO> register(@RequestBody AccountRegDTO accountRegDTO) {
        if (SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
            return new ResponseEntity<>(null, HttpStatus.valueOf(403));
        }

        Account account = modelMapper.map(accountRegDTO, Account.class);

        if (AccountValidator.isInvalid(account)) {
            return new ResponseEntity<>(null, HttpStatus.valueOf(400));
        }

        if (service.isEmailAlreadyTaken(accountRegDTO.getEmail())) {
            return new ResponseEntity<>(null, HttpStatus.valueOf(409));
        }

        Account registeredAccount = service.register(modelMapper.map(accountRegDTO, Account.class));

        return new ResponseEntity<>(modelMapper.map(registeredAccount, AccountDTO.class),
                HttpStatus.valueOf(201));
    }
}
