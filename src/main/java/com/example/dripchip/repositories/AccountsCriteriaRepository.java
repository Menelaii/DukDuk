package com.example.dripchip.repositories;

import com.example.dripchip.searchCriterias.AccountSearchCriteria;
import com.example.dripchip.searchCriterias.XPage;
import com.example.dripchip.entities.Account;
import jakarta.persistence.criteria.*;
import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.data.domain.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Repository
public class AccountsCriteriaRepository {
    @PersistenceContext
    private EntityManager entityManager;
    private final CriteriaBuilder cb;

    public AccountsCriteriaRepository() {
        cb = entityManager.getCriteriaBuilder();
    }

    public List<Account> findWithFilters(XPage page, AccountSearchCriteria searchCriteria) {
        CriteriaQuery<Account> criteriaQuery = cb.createQuery(Account.class);
        Root<Account> root = criteriaQuery.from(Account.class);
        Predicate predicate = getPredicate(searchCriteria, root);
        criteriaQuery.where(predicate);
        setOrder(page, criteriaQuery, root);

        TypedQuery<Account> typedQuery = entityManager.createQuery(criteriaQuery);
        typedQuery.setFirstResult(page.getFrom());
        typedQuery.setMaxResults(page.getSize());

        return typedQuery.getResultList();
    }

    private void setOrder(XPage page, CriteriaQuery<Account> criteriaQuery, Root<Account> root) {
        if (page.getSortDirection().equals(Sort.Direction.DESC)) {
            criteriaQuery.orderBy(cb.desc(root.get(page.getSortBy())));
        } else {
            criteriaQuery.orderBy(cb.asc(root.get(page.getSortBy())));
        }
    }

    private Predicate getPredicate(AccountSearchCriteria searchCriteria, Root<Account> root) {
        List<Predicate> predicates = new ArrayList<>();

        Path<String> firstName = root.get("firstName");
        Path<String> lastName = root.get("lastName");
        Path<String> email = root.get("email");

        if (Objects.nonNull(searchCriteria.getFirstName())) {
            predicates.add(
                    cb.like(cb.lower(firstName), "%"+searchCriteria.getFirstName().toLowerCase()+"%")
            );
        }

        if (Objects.nonNull(searchCriteria.getLastName())) {
            predicates.add(
                    cb.like(cb.lower(lastName), "%"+searchCriteria.getLastName().toLowerCase()+"%")
            );
        }

        if (Objects.nonNull(searchCriteria.getEmail())) {
            predicates.add(
                    cb.like(cb.lower(email), "%"+searchCriteria.getEmail().toLowerCase()+"%")
            );
        }

        return cb.and(predicates.toArray(new Predicate[0]));
    }
}
