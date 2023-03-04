package com.example.dripchip.repositories;

import com.example.dripchip.SearchCriterias.LocationPointSearchCriteria;
import com.example.dripchip.entities.LocationPoint;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.data.domain.Page;

import java.util.Objects;
import java.util.function.Predicate;

public class LocationPointsCriteriaRepository {
    @PersistenceContext
    private EntityManager entityManager;
    private CriteriaBuilder criteriaBuilder;

    public LocationPointsCriteriaRepository() {
        criteriaBuilder = entityManager.getCriteriaBuilder();
    }

    public Page<LocationPoint> findAllWithFilters(Long id,
                                                  LocationPointSearchCriteria searchCriteria) {
        CriteriaQuery<LocationPoint> criteriaQuery = criteriaBuilder.createQuery(LocationPoint.class);
        Root<LocationPoint> root = criteriaQuery.from(LocationPoint.class);
        Predicate predicate = getPredicate(searchCriteria, root);
        criteriaQuery.where(predicate);
        setOrder(furniturePage, criteriaQuery, root);

        TypedQuery<Furniture> typedQuery = entityManager.createQuery(criteriaQuery);
        typedQuery.setFirstResult(furniturePage.getPage() * furniturePage.getItemsPerPage());
        typedQuery.setMaxResults(furniturePage.getItemsPerPage());

        Pageable pageable = createPageable(furniturePage);

        long furnitureTotalCount = getFurnitureTotalCountWith(predicate);

        return new PageImpl<Furniture>(typedQuery.getResultList(), pageable, furnitureTotalCount);
    }

    private long getFurnitureTotalCountWith(Predicate predicate) {
        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
        Root<Furniture> root = countQuery.from(Furniture.class);
        countQuery.select(criteriaBuilder.count(root)).where(predicate);
        return entityManager.createQuery(countQuery).getSingleResult();
    }

    private void setOrder(FurniturePage furniturePage,
                          CriteriaQuery<Furniture> criteriaQuery, Root<Furniture> root) {
        if (furniturePage.getSortDirection().equals(Sort.Direction.DESC)) {
            criteriaQuery.orderBy(criteriaBuilder.desc(root.get(furniturePage.getSortBy())));
        } else {
            criteriaQuery.orderBy(criteriaBuilder.asc(root.get(furniturePage.getSortBy())));
        }
    }

    private Pageable createPageable(FurniturePage furniturePage) {
        Sort sort = Sort.by(furniturePage.getSortDirection(), furniturePage.getSortBy());
        return PageRequest.of(furniturePage.getPage(), furniturePage.getItemsPerPage(), sort);
    }

    public Predicate getPredicate(FurnitureSearchCriteria furnitureSearchCriteria,
                                  Root<Furniture> root) {
        List<Predicate> predicates = new ArrayList<>();

        if (Objects.nonNull(furnitureSearchCriteria.getFurnitureTypeId())) {
            predicates.add(
                    criteriaBuilder.equal(root.get("furnitureType").get("id"), furnitureSearchCriteria.getFurnitureTypeId())
            );
        }

        if (Objects.nonNull(furnitureSearchCriteria.getForm())) {
            predicates.add(
                    criteriaBuilder.equal(root.get("form"), furnitureSearchCriteria.getForm())
            );
        }

        if (Objects.nonNull(furnitureSearchCriteria.getPriceMin())) {
            predicates.add(
                    criteriaBuilder.greaterThanOrEqualTo(root.get("price"), furnitureSearchCriteria.getPriceMin())
            );
        }

        if (Objects.nonNull(furnitureSearchCriteria.getPriceMax())) {
            predicates.add(
                    criteriaBuilder.lessThanOrEqualTo(root.get("price"), furnitureSearchCriteria.getPriceMax())
            );
        }

        if (Objects.nonNull(furnitureSearchCriteria.getLength())) {
            predicates.add(
                    criteriaBuilder.greaterThanOrEqualTo(root.get("length"), furnitureSearchCriteria.getLength())
            );
        }

        if (Objects.nonNull(furnitureSearchCriteria.getWidth())) {
            predicates.add(
                    criteriaBuilder.greaterThanOrEqualTo(root.get("width"), furnitureSearchCriteria.getWidth())
            );
        }

        if (Objects.nonNull(furnitureSearchCriteria.getHeight())) {
            predicates.add(
                    criteriaBuilder.greaterThanOrEqualTo(root.get("height"), furnitureSearchCriteria.getHeight())
            );
        }

        if (Objects.nonNull(furnitureSearchCriteria.getDiameter())) {
            predicates.add(
                    criteriaBuilder.greaterThanOrEqualTo(root.get("diameter"), furnitureSearchCriteria.getDiameter())
            );
        }

        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }
}
