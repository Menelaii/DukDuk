package com.example.dripchip.controllers;

import com.example.dripchip.dto.AccountDTO;
import com.example.dripchip.dto.AccountRegDTO;
import com.example.dripchip.entities.Account;
import com.example.dripchip.searchCriterias.AccountSearchCriteria;
import com.example.dripchip.searchCriterias.XPage;
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
        if (id == null || id <= 0) {
            return new ResponseEntity<>(null, HttpStatusCode.valueOf(400));
        }

        return ResponseEntity.ok(modelMapper.map(service.findOne(id), AccountDTO.class));
    }

    @GetMapping("/search")
    public ResponseEntity<List<AccountDTO>> search(AccountSearchCriteria searchCriteria, XPage page) {
        if (page.getFrom() == null) page.setFrom(0);
        if (page.getSize() == null) page.setSize(10);

        if (page.getFrom() < 0 || page.getSize() <= 0) {
            return new ResponseEntity<>(null, HttpStatusCode.valueOf(400));
        }

        List<Account> accounts = service.findWithFilters(page, searchCriteria);
        List<AccountDTO> accountDTOS = accounts
                .stream()
                .map(a -> modelMapper.map(a, AccountDTO.class))
                .collect(Collectors.toList());

        return ResponseEntity.ok(accountDTOS);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AccountDTO> update(@PathVariable("id") Integer id, @RequestBody AccountRegDTO accountRegDTO) {
        Account updated = service.update(id, modelMapper.map(accountRegDTO, Account.class));
        return new ResponseEntity<>(modelMapper.map(updated, AccountDTO.class), HttpStatusCode.valueOf(200));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Integer id) {
        service.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
