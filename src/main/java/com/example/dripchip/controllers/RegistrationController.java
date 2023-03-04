package com.example.dripchip.controllers;

import com.example.dripchip.dto.AccountDTO;
import com.example.dripchip.dto.AccountRegDTO;
import com.example.dripchip.entities.Account;
import com.example.dripchip.services.AccountRegistrationService;
import com.example.dripchip.utils.EmailValidator;
import com.example.dripchip.utils.StringUtil;
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
    private final AccountRegistrationService registrationService;
    private final ModelMapper modelMapper;

    @Autowired
    public RegistrationController(AccountRegistrationService registrationService, ModelMapper modelMapper) {
        this.registrationService = registrationService;
        this.modelMapper = modelMapper;
    }

    @PostMapping("/registration")
    public ResponseEntity<AccountDTO> register(@RequestBody AccountRegDTO accountRegDTO) {
        if (SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
            return new ResponseEntity<>(null, HttpStatus.valueOf(403));
        }

        if (!isValid(accountRegDTO)) {
            return new ResponseEntity<>(null, HttpStatus.valueOf(400));
        }

        if (registrationService.isEmailAlreadyTaken(accountRegDTO.getEmail())) {
            return new ResponseEntity<>(null, HttpStatus.valueOf(409));
        }

        Account registeredAccount = registrationService.register(modelMapper.map(accountRegDTO, Account.class));

        return new ResponseEntity<>(modelMapper.map(registeredAccount, AccountDTO.class),
                HttpStatus.valueOf(201));
    }

    private boolean isValid(AccountRegDTO dto) {
        return StringUtil.isBlankOrEmpty(dto.getFirstName())
                || StringUtil.isBlankOrEmpty(dto.getLastName())
                || StringUtil.isBlankOrEmpty(dto.getFirstName())
                || StringUtil.isBlankOrEmpty(dto.getPassword())
                || EmailValidator.isValid(dto.getEmail());
    }
}
