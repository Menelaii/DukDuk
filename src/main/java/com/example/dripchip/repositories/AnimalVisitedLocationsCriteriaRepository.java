package com.example.dripchip.repositories;

import com.example.dripchip.searchCriterias.AnimalVisitedLocationSearchCriteria;
import com.example.dripchip.searchCriterias.XPage;
import com.example.dripchip.entities.AnimalVisitedLocation;
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
public class AnimalVisitedLocationsCriteriaRepository {
    private final EntityManager entityManager;
    private final CriteriaBuilder cb;

    public AnimalVisitedLocationsCriteriaRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
        cb = entityManager.getCriteriaBuilder();
    }

    public List<AnimalVisitedLocation> findWithFilters(XPage page,
                                                       AnimalVisitedLocationSearchCriteria searchCriteria,
                                                       Long animalId) {
        CriteriaQuery<AnimalVisitedLocation> criteriaQuery = cb.createQuery(AnimalVisitedLocation.class);
        Root<AnimalVisitedLocation> root = criteriaQuery.from(AnimalVisitedLocation.class);
        Predicate predicate = getPredicate(searchCriteria, root, animalId);
        criteriaQuery.where(predicate);
        setOrder(page, criteriaQuery, root);

        TypedQuery<AnimalVisitedLocation> typedQuery = entityManager.createQuery(criteriaQuery);
        typedQuery.setFirstResult(page.getFrom());
        typedQuery.setMaxResults(page.getSize());

        return typedQuery.getResultList();
    }

    private void setOrder(XPage page,
                          CriteriaQuery<AnimalVisitedLocation> criteriaQuery,
                          Root<AnimalVisitedLocation> root) {
        if (page.getSortDirection().equals(Sort.Direction.DESC)) {
            criteriaQuery.orderBy(cb.desc(root.get(page.getSortBy())));
        } else {
            criteriaQuery.orderBy(cb.asc(root.get(page.getSortBy())));
        }
    }

    private Predicate getPredicate(AnimalVisitedLocationSearchCriteria searchCriteria,
                                   Root<AnimalVisitedLocation> root, Long animalId) {
        List<Predicate> predicates = new ArrayList<>();

        Path<LocalDateTime> dateTimeOfVisitLocationPoint = root.get("dateTimeOfVisitLocationPoint");
        Path<Long> animalIdPath = root.get("animal").get("id");

        predicates.add(cb.equal(animalIdPath, animalId));

        if (Objects.nonNull(searchCriteria.getStartDateTime())) {
            predicates.add(
                    cb.greaterThanOrEqualTo(dateTimeOfVisitLocationPoint, searchCriteria.getStartDateTime())
            );
        }

        if (Objects.nonNull(searchCriteria.getEndDateTime())) {
            predicates.add(
                    cb.lessThanOrEqualTo(dateTimeOfVisitLocationPoint, searchCriteria.getEndDateTime())
            );
        }

        return cb.and(predicates.toArray(new Predicate[0]));
    }
}
