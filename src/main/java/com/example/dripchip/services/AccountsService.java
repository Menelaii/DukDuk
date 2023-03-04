package com.example.dripchip.services;

import com.example.dripchip.SearchCriterias.AccountSearchCriteria;
import com.example.dripchip.SearchCriterias.XPage;
import com.example.dripchip.entities.Account;
import com.example.dripchip.exceptions.EntityNotFoundException;
import com.example.dripchip.repositories.AccountRepository;
import com.example.dripchip.repositories.AccountsCriteriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class AccountsService {
    private final AccountRepository repository;
    private final AccountsCriteriaRepository criteriaRepository;

    @Autowired
    public AccountsService(AccountRepository repository, AccountsCriteriaRepository criteriaRepository) {
        this.repository = repository;
        this.criteriaRepository = criteriaRepository;
    }

    public Account findOne(int id) {
        Optional<Account> account = repository.findById(id);

        if(account.isEmpty()) {
            throw new EntityNotFoundException();
        }

        return account.get();
    }

    public List<Account> findWithFilters(XPage page, AccountSearchCriteria searchCriteria) {
        return criteriaRepository.findWithFilters(page, searchCriteria);
    }
}
