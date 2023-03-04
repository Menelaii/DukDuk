package com.example.dripchip.services;

import com.example.dripchip.SearchCriterias.AnimalSearchCriteria;
import com.example.dripchip.SearchCriterias.XPage;
import com.example.dripchip.entities.Animal;
import com.example.dripchip.exceptions.EntityNotFoundException;
import com.example.dripchip.repositories.AnimalCriteriaRepository;
import com.example.dripchip.repositories.AnimalsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class AnimalService {
    private final AnimalsRepository repository;
    private final AnimalCriteriaRepository criteriaRepository;

    @Autowired
    public AnimalService(AnimalsRepository animalsRepository, AnimalCriteriaRepository criteriaRepository) {
        this.repository = animalsRepository;
        this.criteriaRepository = criteriaRepository;
    }

    public Animal findOne(long id) {
        Optional<Animal> animal = repository.findById(id);

        if(animal.isEmpty()) {
            throw new EntityNotFoundException();
        }

        return animal.get();
    }

    public List<Animal> findWithFilters(XPage page, AnimalSearchCriteria searchCriteria) {
        return criteriaRepository.findWithFilters(page, searchCriteria);
    }
}
