package com.example.dripchip.repositories;

import com.example.dripchip.searchCriterias.AnimalSearchCriteria;
import com.example.dripchip.searchCriterias.XPage;
import com.example.dripchip.entities.Animal;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Repository
public class AnimalCriteriaRepository {
    private final EntityManager entityManager;
    private final CriteriaBuilder cb;

    public AnimalCriteriaRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
        cb = entityManager.getCriteriaBuilder();
    }

    public List<Animal> findWithFilters(XPage page, AnimalSearchCriteria searchCriteria) {
        CriteriaQuery<Animal> criteriaQuery = cb.createQuery(Animal.class);
        Root<Animal> root = criteriaQuery.from(Animal.class);
        Predicate predicate = getPredicate(searchCriteria, root);
        criteriaQuery.where(predicate);
        setOrder(page, criteriaQuery, root);

        TypedQuery<Animal> typedQuery = entityManager.createQuery(criteriaQuery);
        typedQuery.setFirstResult(page.getFrom());
        typedQuery.setMaxResults(page.getSize());

        return typedQuery.getResultList();
    }

    private void setOrder(XPage page, CriteriaQuery<Animal> criteriaQuery, Root<Animal> root) {
        if (page.getSortDirection().equals(Sort.Direction.DESC)) {
            criteriaQuery.orderBy(cb.desc(root.get(page.getSortBy())));
        } else {
            criteriaQuery.orderBy(cb.asc(root.get(page.getSortBy())));
        }
    }

    private Predicate getPredicate(AnimalSearchCriteria searchCriteria, Root<Animal> root) {
        List<Predicate> predicates = new ArrayList<>();

        Path<LocalDateTime> chippingDateTime = root.get("chippingDateTime");
        Path<Integer> chipperId = root.get("chipper").get("id");
        Path<Long> chippingLocationId = root.get("chippingLocation").get("id");
        Path<String> lifeStatus = root.get("lifeStatus");
        Path<String> gender = root.get("gender");

        if (Objects.nonNull(searchCriteria.getStartDateTime())) {
            predicates.add(
                    cb.greaterThanOrEqualTo(chippingDateTime, searchCriteria.getStartDateTime())
            );
        }

        if (Objects.nonNull(searchCriteria.getEndDateTime())) {
            predicates.add(
                    cb.lessThanOrEqualTo(chippingDateTime, searchCriteria.getEndDateTime())
            );
        }

        if (Objects.nonNull(searchCriteria.getChipperId())) {
            predicates.add(
                    cb.equal(chipperId, searchCriteria.getChipperId())
            );
        }

        if (Objects.nonNull(searchCriteria.getChippingLocationId())) {
            predicates.add(
                    cb.equal(chippingLocationId, searchCriteria.getChippingLocationId())
            );
        }

        if (Objects.nonNull(searchCriteria.getLifeStatus())) {
            predicates.add(
              cb.equal(lifeStatus, searchCriteria.getLifeStatus())
            );
        }

        if (Objects.nonNull(searchCriteria.getGender())) {
            predicates.add(
                    cb.equal(gender, searchCriteria.getGender())
            );
        }


        return cb.and(predicates.toArray(new Predicate[0]));
    }
}
