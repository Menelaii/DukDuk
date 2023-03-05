package com.example.dripchip.services;

import com.example.dripchip.entities.AnimalVisitedLocation;
import com.example.dripchip.exceptions.EntityNotFoundException;
import com.example.dripchip.repositories.AnimalVisitedLocationRepository;
import com.example.dripchip.repositories.AnimalVisitedLocationsCriteriaRepository;
import com.example.dripchip.searchCriterias.AnimalVisitedLocationSearchCriteria;
import com.example.dripchip.searchCriterias.XPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnimalVisitedLocationsService {
    private final AnimalVisitedLocationRepository repository;
    private final AnimalVisitedLocationsCriteriaRepository criteriaRepository;

    @Autowired
    public AnimalVisitedLocationsService(AnimalVisitedLocationRepository repository,
                                         AnimalVisitedLocationsCriteriaRepository criteriaRepository) {
        this.repository = repository;
        this.criteriaRepository = criteriaRepository;
    }

    public List<AnimalVisitedLocation> findWithFilters(XPage page,
                                                       AnimalVisitedLocationSearchCriteria searchCriteria,
                                                       Long animalId) {

        if (!repository.existsById(animalId)) {
            throw new EntityNotFoundException();
        }

        return criteriaRepository.findWithFilters(page, searchCriteria, animalId);
    }
}
