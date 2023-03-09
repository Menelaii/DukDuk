package com.example.dripchip.services;

import com.example.dripchip.entities.AnimalType;
import com.example.dripchip.exceptions.*;
import com.example.dripchip.repositories.*;
import com.example.dripchip.searchCriterias.AnimalSearchCriteria;
import com.example.dripchip.searchCriterias.XPage;
import com.example.dripchip.entities.Animal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.dripchip.utils.Constants.ALIVE_LIFE_STATUS;
import static com.example.dripchip.utils.Constants.DEAD_LIFE_STATUS;

@Service
@Transactional(readOnly = true)
public class AnimalService {
    private final AccountRepository accountsRepository;
    private final AnimalsRepository animalsRepository;
    private final AnimalTypesRepository animalTypesRepository;
    private final LocationPointsRepository locationPointsRepository;
    private final AnimalCriteriaRepository criteriaRepository;

    @Autowired
    public AnimalService(AccountRepository accountsRepository,
                         AnimalsRepository animalsRepository,
                         AnimalTypesRepository animalTypesRepository,
                         LocationPointsRepository locationPointsRepository,
                         AnimalCriteriaRepository criteriaRepository) {
        this.accountsRepository = accountsRepository;
        this.animalsRepository = animalsRepository;
        this.animalTypesRepository = animalTypesRepository;
        this.locationPointsRepository = locationPointsRepository;
        this.criteriaRepository = criteriaRepository;
    }

    public Animal findOne(long id) {
        return animalsRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
    }

    public List<Animal> findWithFilters(XPage page, AnimalSearchCriteria searchCriteria) {
        return criteriaRepository.findWithFilters(page, searchCriteria);
    }

    @Transactional
    public Animal save(Animal animal) {
        return animalsRepository.save(animal);
    }

    @Transactional
    public Animal update(Long id, Animal updatedEntity) {
        Animal savedEntity = animalsRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);

        if (!accountsRepository.existsById(updatedEntity.getChipper().getId())) {
            throw new EntityNotFoundException();
        }

        if (!locationPointsRepository.existsById(updatedEntity.getChippingLocation().getId())) {
            throw new EntityNotFoundException();
        }

        if (savedEntity.getLifeStatus().equals(DEAD_LIFE_STATUS)
                && updatedEntity.getLifeStatus().equals(ALIVE_LIFE_STATUS)) {
            throw new RevivingAnimalException();
        }

        if (!savedEntity.getChippingLocation()
                .equals(updatedEntity.getChippingLocation())
                && updatedEntity.getChippingLocation()
                .equals(savedEntity.getVisitedLocations().get(0).getLocationPoint())) {
            throw new InvalidEntityException();
        }

        savedEntity.setId(id);
        return animalsRepository.save(savedEntity);
    }

    @Transactional
    public void delete(Long id) {
        Animal animal = animalsRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);

        if (!animal.getVisitedLocations().isEmpty()
                && !animal.getVisitedLocations().get(0).getLocationPoint()
                .equals(animal.getChippingLocation())) {
            throw new EntityConnectedException();
        }

        animalsRepository.deleteById(id);
    }

    @Transactional
    public Animal addType(Long animalId, Long typeId) {
        Animal animal = animalsRepository.findById(animalId)
                .orElseThrow(EntityNotFoundException::new);

        AnimalType animalType = animalTypesRepository.findById(typeId)
                .orElseThrow(EntityNotFoundException::new);

        List<AnimalType> animalTypes = animal.getAnimalTypes();
        if (contains(animalTypes, animalType)) {
            throw new EntityAlreadyExistsException();
        }

        if (animalTypes == null) {
            animal.setAnimalTypes(List.of(animalType));
        } else {
            animal.getAnimalTypes().add(animalType);
        }

        return animal;
    }

    @Transactional
    public Animal updateType(Long animalId, Long oldTypeId, Long newTypeId) {
        Animal animal = animalsRepository.findById(animalId)
                .orElseThrow(EntityNotFoundException::new);

        AnimalType oldAnimalType = animalTypesRepository.findById(oldTypeId)
                .orElseThrow(EntityNotFoundException::new);

        AnimalType newAnimalType = animalTypesRepository.findById(newTypeId)
                .orElseThrow(EntityNotFoundException::new);

        List<AnimalType> animalTypes = animal.getAnimalTypes();

        if (!contains(animalTypes, oldAnimalType)) {
            throw new EntityNotFoundException();
        }

        if (contains(animalTypes, newAnimalType)) {
            throw new EntityAlreadyExistsException();
        }

        animalTypes.remove(oldAnimalType);
        animalTypes.add(newAnimalType);

        return animal;
    }

    public Animal deleteType(Long animalId, Long typeId) {
        AnimalType type = animalTypesRepository.findById(typeId)
                .orElseThrow(EntityNotFoundException::new);

        Animal animal = animalsRepository.findById(animalId)
                .orElseThrow(EntityNotFoundException::new);

        List<AnimalType> animalTypes = animal.getAnimalTypes();

        if (animalTypes.size() == 1) {
            throw new EntityConnectedException();
        }

        if (!contains(animalTypes, type)) {
            throw new EntityNotFoundException();
        }

        animalTypes.remove(type);

        return animal;
    }

    private boolean contains(List<AnimalType> types, AnimalType type) {
        return types != null && types.contains(type);
    }
}
