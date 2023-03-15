package com.example.dripchip.services;

import com.example.dripchip.entities.Account;
import com.example.dripchip.exceptions.*;
import com.example.dripchip.repositories.AccountRepository;
import com.example.dripchip.repositories.AccountsCriteriaRepository;
import com.example.dripchip.searchCriterias.AccountSearchCriteria;
import com.example.dripchip.searchCriterias.XPage;
import com.example.dripchip.security.AccountDetailsImpl;
import com.example.dripchip.validators.AccountValidator;
import com.example.dripchip.validators.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class AccountsService {
    private final AccountRepository repository;
    private final AccountsCriteriaRepository criteriaRepository;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AccountsService(AccountRepository repository, AccountsCriteriaRepository criteriaRepository,
                           AccountRepository accountRepository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.criteriaRepository = criteriaRepository;
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Account findOne(Integer id) {
        Optional<Account> account = repository.findById(id);

        if (account.isEmpty()) {
            throw new EntityNotFoundException();
        }

        return account.get();
    }

    public List<Account> findWithFilters(XPage page, AccountSearchCriteria searchCriteria) {
        return criteriaRepository.findWithFilters(page, searchCriteria);
    }

    @Transactional
    public Account register(Account account) {
        //todo перенести валидацию из контроллера
        String encodedPassword = passwordEncoder.encode(account.getPassword());
        account.setPassword(encodedPassword);

        return accountRepository.save(account);
    }

    public boolean isEmailAlreadyTaken(String accountEmail) {
        return accountRepository.getByEmail(accountEmail).isPresent();
    }

    @Transactional
    public Account update(Integer id, Account account) {
        account.setId(id);

        Validator.throwIfInvalidId(id);
        throwIfInvalid(account);
        throw403IfNotExists(id);
        throwIfNotOwned(account);
//        throwIfTaken(account.getEmail());

        return repository.save(account);
    }

    @Transactional
    public void deleteById(Integer id) {
        System.out.println("id = " + id);

        Validator.throwIfInvalidId(id);

        System.out.println("find by id");
        Account account = repository.findById(id)
                .orElseThrow(NothingToChangeException::new);
        System.out.println("after find by id");
        if (!account.getAnimals().isEmpty()) {
            throw new EntityConnectedException();
        }

        //TODO проверка на админа вернуть
//        throwIfNotOwned(account);

        repository.deleteById(id);
    }

    public boolean isLogged() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return null != authentication && !("anonymousUser").equals(authentication.getName());
    }

    private boolean isAuthenticated(Account account) {
        AccountDetailsImpl currentAccount = (AccountDetailsImpl) SecurityContextHolder
                .getContext().getAuthentication().getPrincipal();

        System.out.println("logged ac id " + currentAccount.getAccountId());
        System.out.println("req ac id " + account.getId());

        return currentAccount.getAccountId().equals(account.getId());
    }

    private void throw403IfNotExists(Integer id) {
        if (!repository.existsById(id)) {
            throw new NothingToChangeException();
        }
    }

    private void throwIfTaken(String email) {
        if (isEmailAlreadyTaken(email)) {
            throw new EmailAlreadyTakenException();
        }
    }

    private void throwIfNotOwned(Account account) {
        if (!isAuthenticated(account)) {
            System.out.println("acc is not owned");
            throw new AccountIsNotYoursException();
        }
    }

    private void throwIfInvalid(Account account) {
        if (AccountValidator.isInvalid(account)) {
            throw new InvalidEntityException();
        }
    }
}
