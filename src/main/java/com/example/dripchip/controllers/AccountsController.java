package com.example.dripchip.controllers;

import com.example.dripchip.dto.AccountDTO;
import com.example.dripchip.services.AccountsService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/accounts")
public class AccountsController {
    private final AccountsService service;
    private final ModelMapper modelMapper;

    @Autowired
    public AccountsController(AccountsService service, ModelMapper modelMapper) {
        this.service = service;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountDTO> findOne(@PathVariable("id") Integer id) {
        //todo 401 неверные авторизационные данные
        if( id == null || id <= 0){
            return  new ResponseEntity<>(null, HttpStatusCode.valueOf(400));
        }

        return ResponseEntity.ok(modelMapper.map(service.get(id), AccountDTO.class));
    }
}
