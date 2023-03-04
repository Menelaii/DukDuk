package com.example.dripchip.controllers;

import com.example.dripchip.SearchCriterias.AccountSearchCriteria;
import com.example.dripchip.SearchCriterias.XPage;
import com.example.dripchip.dto.AccountDTO;
import com.example.dripchip.entities.Account;
import com.example.dripchip.services.AccountsService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

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
        if( id == null || id <= 0){
            return  new ResponseEntity<>(null, HttpStatusCode.valueOf(400));
        }

        return ResponseEntity.ok(modelMapper.map(service.findOne(id), AccountDTO.class));
    }

    @GetMapping("/search")
    public ResponseEntity<List<AccountDTO>> search(AccountSearchCriteria searchCriteria, XPage page) {
        if (page.getFrom() == null || page.getFrom() < 0
            || page.getSize() == null || page.getSize() <= 0) {
            return  new ResponseEntity<>(null, HttpStatusCode.valueOf(400));
        }

        List<Account> accounts = service.findWithFilters(page, searchCriteria);
        List<AccountDTO> accountDTOS = accounts
                .stream()
                .map(a -> modelMapper.map(a, AccountDTO.class))
                .collect(Collectors.toList());

        return ResponseEntity.ok(accountDTOS);
    }
}
