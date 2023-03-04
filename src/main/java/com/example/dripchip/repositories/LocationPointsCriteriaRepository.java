package com.example.dripchip.repositories;

import com.example.dripchip.SearchCriterias.AccountSearchCriteria;
import com.example.dripchip.SearchCriterias.LocationPointSearchCriteria;
import com.example.dripchip.SearchCriterias.XPage;
import com.example.dripchip.entities.Account;
import com.example.dripchip.entities.LocationPoint;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LocationPointsCriteriaRepository {
    @PersistenceContext
    private EntityManager entityManager;
    private CriteriaBuilder cb;

    public LocationPointsCriteriaRepository() {
        cb = entityManager.getCriteriaBuilder();
    }

    public List<Account> findWithFilters(XPage page, LocationPointSearchCriteria searchCriteria) {
        CriteriaQuery<Account> criteriaQuery = cb.createQuery(Account.class);
        Root<Account> root = criteriaQuery.from(Account.class);
        jakarta.persistence.criteria.Predicate predicate = getPredicate(searchCriteria, root);
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

    private Predicate getPredicate(LocationPointSearchCriteria searchCriteria, Root<Account> root) {
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
